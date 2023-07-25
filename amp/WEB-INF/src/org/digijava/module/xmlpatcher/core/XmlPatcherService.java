/**
 * XmlPatcherService.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.core;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.FatalServiceException;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceException;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatch;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherHaltExecutionException;
import org.digijava.module.xmlpatcher.jaxb.Patch;
import org.digijava.module.xmlpatcher.scheduler.NaturalOrderXmlPatcherScheduler;
import org.digijava.module.xmlpatcher.scheduler.XmlPatcherScheduler;
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;
import org.digijava.module.xmlpatcher.util.XmlPatcherUtil;
import org.digijava.module.xmlpatcher.worker.XmlPatcherWorker;
import org.hibernate.HibernateException;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
public class XmlPatcherService extends AbstractServiceImpl {
    private static Logger logger = Logger.getLogger(XmlPatcherService.class);

    /**
     * The name of the scheduler as read from digi.xml. This will be later used
     * to dynamically instantiate the scheduler
     */
    private String schedulerName;
    
    /**
     * The scheduler used by the patcher service. The default scheduler is set
     * in the service constructor. However another scheduler may be provided in
     * digi.xml, using the property name schedulerName. This scheduler
     */
    private XmlPatcherScheduler scheduler;

    /**
     * Useful properties that may be set in digi.xml file, for the use of the
     * scheduler You may set them inside the service entity: &lt;property&gt;
     * name="schedulerProperties(propertyKey)">propertyValue&lt;/property&gt;
     */
    private Map<String, Object> schedulerProperties;

    /**
     * @see org.digijava.kernel.service.ServiceManager Default constructor
     *      invoked by ServiceManager
     */
    public XmlPatcherService() {
        super();
        schedulerProperties = new HashMap<String, Object>();
        schedulerName = NaturalOrderXmlPatcherScheduler.class.getSimpleName();
    }

    
    /**
     * Schedules the patch list for execution until the previous list is identical in size with the current list, meaning no more patches have been executed.
     * This is required to have all patch dependencies fullfilled
     * @param scheduledPatches
     * @param serviceContext
     * @return
     * @throws DgException
     * @see {@link XmlPatcherService#processUnclosedPatches(Collection, ServiceContext)}
     */
    public int processAllUnclosedPatches(
            Collection<AmpXmlPatch> scheduledPatches,
            ServiceContext serviceContext) throws DgException {
    
        Collection<AmpXmlPatch> previouslyUnclosedPatches=null;
        Collection<AmpXmlPatch> currentUnclosedPatches=null;
        
        do {
            previouslyUnclosedPatches=currentUnclosedPatches;
            currentUnclosedPatches = processUnclosedPatches(scheduledPatches, serviceContext);
        }  while(previouslyUnclosedPatches==null || currentUnclosedPatches.size()!=previouslyUnclosedPatches.size());
        return currentUnclosedPatches.size();
    }
    
    /**
     * Iterates the list of patches and applies deprecation tags
     * @param scheduledPatches
     * @param serviceContext
     * @throws DgException
     */
    private void processDeprecation(
            Collection<AmpXmlPatch> scheduledPatches,
            ServiceContext serviceContext) throws DgException {
        Iterator<AmpXmlPatch> iterator = scheduledPatches.iterator();
        logger.info(scheduledPatches.size()+" patches scheduled for execution...");
        while (iterator.hasNext()) {
            AmpXmlPatch ampPatch = iterator.next();
            long timeStart = System.currentTimeMillis();
            AmpXmlPatchLog log = new AmpXmlPatchLog(ampPatch);
            logger.debug("Reading patch: "+ampPatch.getPatchId());
            try {
                File f=new File(
                        XmlPatcherUtil.getXmlPatchAbsoluteFileName(ampPatch,
                                serviceContext));
                if(!f.exists()) {
                    //mark file as deleted
                    log.appendToLog("Patch file deleted");
                    //logger.info("Marking patch "+ampPatch.getPatchId()+" as deleted");
                    XmlPatcherUtil.addLogToPatch(ampPatch, log);
                    ampPatch.setState(XmlPatcherConstants.PatchStates.DELETED);
                    DbUtil.update(ampPatch);
                    iterator.remove();
                    continue;
                }
                log.setFileChecksum(XmlPatcherUtil.getFileMD5(f));
                Patch patch = XmlPatcherUtil.getUnmarshalledPatch(serviceContext,
                        ampPatch, null); //we don't record unmarshalling logs here. we do that when we run the patch
                
                XmlPatcherUtil.applyDeprecationTags(patch,log);
            } catch (NoSuchAlgorithmException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            } catch (HibernateException e) {
                logger.error(e.getMessage(), e);
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }

            //the error may be that the patch is referencing for deprecation patches that do not exist.
            //in order to prevent typing mistakes, this error makes the patch fail
            if (log.getError()) {
                logger.info("Failed to apply patch " + ampPatch.getPatchId());
                ampPatch.setState(XmlPatcherConstants.PatchStates.FAILED);
                iterator.remove();
                log.setElapsed(System.currentTimeMillis() - timeStart);
                XmlPatcherUtil.addLogToPatch(ampPatch, log);
                DbUtil.update(ampPatch);
            }   
        }

    }
    
    /**
     * Attempts to execute the given collection of unclosed patches. The
     * collection is ordered using a scheduler that was given as a parameter in
     * digi.xml. If the scheduler is missing then the default scheduler is used.
     * <p>
     * The collection is iterated and each patch is invoked. If the patch worker
     * will return false it means the patch was not applied.
     * 
     * @param scheduledPatches
     * @param serviceContext
     * @throws DgException
     */
    private Collection<AmpXmlPatch> processUnclosedPatches(Collection<AmpXmlPatch> scheduledPatches,
            ServiceContext serviceContext) throws DgException {
        Iterator<AmpXmlPatch> iterator = scheduledPatches.iterator();
        logger.info(scheduledPatches.size()+" patches scheduled for execution...");
        while (iterator.hasNext()) {
            AmpXmlPatch ampPatch = iterator.next();
            long timeStart = System.currentTimeMillis();
            AmpXmlPatchLog log = new AmpXmlPatchLog(ampPatch);
            logger.debug("Reading patch: "+ampPatch.getPatchId());
            try {
                log.setFileChecksum(XmlPatcherUtil.getFileMD5(new File(
                        XmlPatcherUtil.getXmlPatchAbsoluteFileName(ampPatch,
                                serviceContext))));
            } catch (NoSuchAlgorithmException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
            Patch patch = XmlPatcherUtil.getUnmarshalledPatch(serviceContext,
                    ampPatch, log);
            boolean success = false;
            if (patch != null) {
                if (ampPatch.getState().equals(
                        XmlPatcherConstants.PatchStates.FAILED)
                        && !patch.isRetryOnFail()) {
                    logger.info("Skipping failed patch "
                            + ampPatch.getPatchId());
                    continue;
                }
            logger.debug("Applying patch: "+ampPatch.getPatchId());
                XmlPatcherWorker<?, ?> patcherWorker = XmlPatcherWorkerFactory
                        .createWorker(patch, null, log);
                success = patcherWorker.run();
            }
            if (success) {
                logger.info("Succesfully applied patch "
                        + ampPatch.getPatchId());
                if(patch.isCloseOnSuccess()) ampPatch.setState(XmlPatcherConstants.PatchStates.CLOSED);
                else ampPatch.setState(XmlPatcherConstants.PatchStates.OPEN);
                iterator.remove();
                
            } else if (log.getError()) {
                logger.info("Failed to apply patch " + ampPatch.getPatchId());
                ampPatch.setState(XmlPatcherConstants.PatchStates.FAILED);
                iterator.remove();
            } else {    
                logger.debug("Will not apply " + ampPatch.getPatchId()
                        + " due to conditions not met.");
                ampPatch.setState(XmlPatcherConstants.PatchStates.OPEN);
            }

            log.setElapsed(System.currentTimeMillis() - timeStart);
            if(success || log.getError()) XmlPatcherUtil.addLogToPatch(ampPatch, log);
            DbUtil.update(ampPatch);
            if (!success && log.getError() && patch != null && patch.isFailOnError()) {
                throw new XmlPatcherHaltExecutionException(ampPatch.getPatchId());
            }
        }
        logger.info(scheduledPatches.size()+" patches left unexecuted");
        return scheduledPatches;
    }

    @Override
    public void processInitEvent(ServiceContext serviceContext)
            throws ServiceException {
        boolean patcherFailed = false;
        try {
            // workaround a design fault whereas a patch which was deleted and then a different one with the same name was readded remains marked as "deleted" by AMP and never executed.
            // AMP 2.7.1+ does not start without this fix (unless one does manual intervetions to the DB)
            // this has been added here instead of an XML patch because this code has to be run before the XML patches have been discovered
            // DO NOT CHANGE THIS TO USE PersistenceManager.getSession().createNativeQuery(), as this leads to AMP hanging (Postgres hangs when having the same UPDATE in 2 different transactions open - there is a patch which does the same)
            java.sql.Connection conn = PersistenceManager.getJdbcConnection();
            conn.prepareStatement("UPDATE amp_xml_patch set state=0 WHERE patch_id similar to '((v)|(z)|(zz))%' AND location ='xmlpatches/general/views/' AND state=4;").executeUpdate();
            conn.close();

            //discover newly added patches
            performPatchDiscovery(serviceContext.getRealPath("/"));

            //applying deprecation tags -read all deprecate tags in all patches
            //and flag deprecated the patches mentioned
            List<AmpXmlPatch> rawPatches = XmlPatcherUtil.getAllDiscoveredUnclosedPatches();

            processDeprecation(rawPatches, serviceContext);

            //read patches again, after deprecation flags set (so only the ones that are not depr)
            rawPatches = XmlPatcherUtil.getAllDiscoveredUnclosedPatches();
            scheduler = (XmlPatcherScheduler) Class.forName(
                    XmlPatcherConstants.schedulersPackage + schedulerName)
                    .getConstructors()[0].newInstance(new Object[]{
                    schedulerProperties, rawPatches});
            Collection<AmpXmlPatch> scheduledPatches = scheduler
                    .getScheduledPatchCollection();


            processAllUnclosedPatches(scheduledPatches, serviceContext);

        } catch (XmlPatcherHaltExecutionException e) {
            logger.error("Failed to apply patch.", e);
            patcherFailed = true;
        } catch(Throwable e) {
            logger.error(e.getMessage(), e);
            PersistenceManager.rollbackCurrentSessionTx();
            throw new ServiceException(e);
        }
        logger.info("XML Patcher session finished");
        logger.info("refreshing GlobalSettingsCache...");
        PersistenceManager.getSession().getTransaction().commit();
        FeaturesUtil.buildGlobalSettingsCache(FeaturesUtil.getGlobalSettings()); // refresh global settings cache, as the startup process might have changed it (through XML patches, for example)
        if (patcherFailed) {
            throw new FatalServiceException("At least one patch failed to apply.");
        }
    }

    /**
     * Discovers new patches in the known locations (Digi modules directories or
     * the generic patch directory).
     * 
     * @param  appPath the application Path - usually serviceContext.getRealPath("/")
     * @throws DgException
     * @throws SQLException
     * @throws HibernateException
     */
    public void performPatchDiscovery(String appPath) throws DgException,
            HibernateException, SQLException {
        // start by getting a complete list of patch locations:
        Set<File> patchDirs = XmlPatcherUtil.discoverPatchDirs(appPath);
        logger.info("Discovered XML Patch Directories: " + patchDirs);

        // get a full list of known patch files from the database
        Set<String> allDiscoveredPatchNames = XmlPatcherUtil
                .getAllDiscoveredPatchNames();
        Map<String, AmpXmlPatch> patchesMap = XmlPatcherUtil.getAllDiscoveredPatchesMap();

        Iterator<File> i = patchDirs.iterator();
        while (i.hasNext()) {
            File dir = i.next();
            XmlPatcherUtil.recordNewPatchesInDir(appPath, dir,
                    allDiscoveredPatchNames,patchesMap);
        }
    }

    public String getSchedulerProperties(String key) {
        return (String) schedulerProperties.get(key);
    }

    public void setSchedulerProperties(String key, Object value) {
        schedulerProperties.put(key, value);
    }

    public Map<String, Object> getSchedulerProperties() {
        return schedulerProperties;
    }

    public void setSchedulerProperties(Map<String, Object> schedulerProperties) {
        this.schedulerProperties = schedulerProperties;
    }

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

}
