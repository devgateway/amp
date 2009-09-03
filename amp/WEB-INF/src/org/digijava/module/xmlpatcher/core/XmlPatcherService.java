/**
 * XmlPatcherService.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.core;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceException;
import org.digijava.module.xmlpatcher.dbentity.XmlPatch;
import org.digijava.module.xmlpatcher.scheduler.NaturalOrderXmlPatcherScheduler;
import org.digijava.module.xmlpatcher.scheduler.XmlPatcherScheduler;
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;
import org.digijava.module.xmlpatcher.util.XmlPatcherUtil;
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

	@Override
	public void processInitEvent(ServiceContext serviceContext)
			throws ServiceException {
		try {
			performPatchDiscovery(serviceContext.getRealPath("/"));
			List<XmlPatch> rawPatches = XmlPatcherUtil
					.getAllDiscoveredUnclosedPatches();
			scheduler = (XmlPatcherScheduler) Class.forName(
					XmlPatcherConstants.schedulersPackage + schedulerName)
					.getConstructors()[0].newInstance(new Object[] {
					schedulerProperties, rawPatches });
			Collection<XmlPatch> scheduledPatches = scheduler
					.getScheduledPatchCollection();

		} catch (DgException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (InstantiationException e) {
			logger
					.error("Cannot instantiate the specified scheduler. Please check digi.xml and specify a valid scheduler name");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			logger
					.error("Cannot find the specified scheduler. Please check digi.xml and specify a valid class name");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HibernateException e) {
			logger.error(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			logger
					.error("Use only one constructor in the scheduler, with the same signature as its superclass");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			logger.error(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			logger.error(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Discovers new patches in the known locations (Digi modules directories or
	 * the generic patch directory).
	 * 
	 * @param serviceContext
	 * @throws DgException
	 * @throws SQLException
	 * @throws HibernateException
	 */
	private void performPatchDiscovery(String appPath) throws DgException,
			HibernateException, SQLException {
		// start by getting a complete list of patch locations:
		Set<File> patchDirs = XmlPatcherUtil.discoverPatchDirs(appPath);
		logger.info("Discovered XML Patch Directories: " + patchDirs);

		// get a full list of known patch files from the database
		Set<String> allDiscoveredPatchNames = XmlPatcherUtil
				.getAllDiscoveredPatchNames();

		Iterator<File> i = patchDirs.iterator();
		while (i.hasNext()) {
			File dir = i.next();
			XmlPatcherUtil.recordNewPatchesInDir(appPath, dir,
					allDiscoveredPatchNames);
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
