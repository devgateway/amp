package org.digijava.module.autopatcher.core;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceException;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.autopatcher.exceptions.InvalidPatchRepositoryException;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

/**
 * AutopatcherService.java
 * (c) 2007 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since 2007 08 08
 * 
 */
public class AutopatcherService extends AbstractServiceImpl {

    private String patchesDir;
    private Collection appliedPatches;
    private static Logger logger = Logger.getLogger(AutopatcherService.class);

    public void processInitEvent(ServiceContext serviceContext) throws ServiceException {

        if (!this.checksBeforeApplyingPatches())
            return;

        Session session;
        appliedPatches = new ArrayList();
        try {
            session = PersistenceManager.getSession();

            String realRootPath = serviceContext.getRealPath("/" + patchesDir + "/");
            logger.info("Applying patches...");
            logger.info("Patch directory is " + realRootPath);
            Collection<File> allPatchesFiles = PatcherUtil.getAllPatchesFiles(serviceContext.getRealPath(patchesDir));
            Set allAppliedPatches = PatcherUtil.getAllAppliedPatches(session);
            Iterator i = allPatchesFiles.iterator();
            while (i.hasNext()) {
                session = PersistenceManager.getSession();
                File element = (File) i.next();
                String localPatchPath = element.getAbsolutePath().substring(realRootPath.length() + 1, element.getAbsolutePath().length());
                if (allAppliedPatches.contains(localPatchPath))
                    continue;

                try {
                    String delimiter = ";";
                    boolean firstLine = true;
                    LineNumberReader bis = new LineNumberReader(new FileReader(element));
                    StringBuffer sb = new StringBuffer();
                    String s = bis.readLine();
                    while (s != null) {
                        if (firstLine && s.length() >= 11 && s.substring(0, 9).equalsIgnoreCase("delimiter")) {
                            delimiter = s.substring(10, 11);
                            s = bis.readLine();
                            continue;
                        }

                        sb.append(s);
                        firstLine = false;
                        s = bis.readLine();
                    }
                    bis.close();

                    StringTokenizer stok = new StringTokenizer(sb.toString(), delimiter);
                    logger.info("Applying patch " + element.getAbsolutePath());
                    logger.debug("Executing sql commands: " + sb.toString());

                    Connection connection = ((SessionImplementor)session).connection();
                    connection.setAutoCommit(false);

                    try {
                        Statement st = connection.createStatement();

                        while (stok.hasMoreTokens()) {

                            String sqlCommand = stok.nextToken();
                            if (sqlCommand.trim().equals(""))
                                continue;

                            st.addBatch(sqlCommand);

                        }

                        st.executeBatch();
                        connection.commit();
                        // st.close();

                        session = PersistenceManager.getSession();
                        PatchFile pf = new PatchFile();
                        pf.setAbsolutePatchName(localPatchPath);
                        pf.setInvoked(new Timestamp(System.currentTimeMillis()));
//beginTransaction();
                        session.save(pf);
                        //tx.commit();
                        appliedPatches.add(element.getAbsolutePath());

                    }

                    catch (BatchUpdateException e) {
                        logger.error(e.getMessage(), e);
                        connection.rollback();

                    } finally {
                        connection.setAutoCommit(true);
                    }

                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

            }

        } catch (HibernateException e1) {
            // TODO Auto-generated catch block
            throw new RuntimeException("HibernateException Exception encountered", e1);
        } catch (InvalidPatchRepositoryException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("InvalidPatchRepositoryException Exception encountered", e);
        }

        logger.info(this.toString());

    }

    public String toString() {
        return "Autopatcher: Patches applied : " + appliedPatches;
    }

    public String getPatchesDir() {
        return patchesDir;
    }

    public void setPatchesDir(String patchesDir) {
        this.patchesDir = patchesDir;
    }

    /**
     * All the things that need to be done before db patches are applied can be
     * inserted here.
     */
    private boolean checksBeforeApplyingPatches() {
        String errorString = CategoryManagerUtil.checkImplementationLocationCategory();
        if (errorString != null) {
            logger.error("There is a problem in category Implementation Location: " + errorString);
            logger.error("Patches will NOT be applied !");
            return false;
        }
        DynLocationManagerUtil.synchronizeCountries();
        return true;
    }
}
