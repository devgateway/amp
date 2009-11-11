/**
 * XmlPatcherStructureTest.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.test.xmlpatcher;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.kernel.config.ServiceConfig;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.service.Service;
import org.digijava.kernel.service.WebappServiceContext;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.module.xmlpatcher.core.XmlPatcherService;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatch;
import org.digijava.module.xmlpatcher.util.XmlPatcherUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.mockrunner.mock.web.MockServletContext;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
public class XmlPatcherStructureTest extends BasicActionTestCaseAdapter {
	XmlPatcherService service = null;

	private static final String testSQLConditionPatch="junit-testpatch-SQLCondition.xml";
	private static final String testBSHConditionPatch="junit-testpatch-BSHCondition.xml";
	private static final String testDbNamePatch="junit-testpatch-dbname.xml";
	private static final String testDependencyPatch="junit-testpatch-dependency.xml";
	
	private MockServletContext servletContext;



	private WebappServiceContext serviceContext;


	
	private static Logger logger = Logger.getLogger(XmlPatcherStructureTest.class);
	
	protected void setUp() throws Exception {
		super.setUp();
		
		logger.info("Setup patcher test");
		Configuration.initConfig();
		Map serviceDefinitions = DigiConfigManager.getConfig().getServices();

		Iterator serviceIter = serviceDefinitions.entrySet().iterator();
		while (serviceIter.hasNext()) {
			Map.Entry item = (Map.Entry) serviceIter.next();
			String serviceId = (String) item.getKey();
			ServiceConfig serviceConfig = (ServiceConfig) item.getValue();
			
			if(!serviceConfig.getServiceId().equals("XmlPatcherService")) continue;
			
			
			try {
				service = (XmlPatcherService) createService(serviceConfig);
				break;
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		
		XmlPatcherUtil.deleteUnitTestPatches();
		servletContext 	= getActionMockObjectFactory().getMockServletContext();
		File configDirFile = new File("");
		servletContext.setRealPath("/", configDirFile.getAbsolutePath());
        serviceContext = new WebappServiceContext(servletContext);

	}

	private Service createService(ServiceConfig serviceConfig)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException, InvocationTargetException {

		Service service = (Service) Class.forName(
				serviceConfig.getServiceClass()).newInstance();
		service.setDescription(serviceConfig.getDescription());
		service.setLevel(serviceConfig.getLevel());
		BeanUtils.populate(service, serviceConfig.getProperties());

		return service;
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		service.destroy();
		XmlPatcherUtil.deleteUnitTestPatches();
	}

	/**
	 * @param arg0
	 */
	public XmlPatcherStructureTest(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	
	
	/**
	 * Tests to see if the generic XSLT Transformation works properly
	 * @throws DgException
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public void testDbNameCondition() throws DgException, HibernateException, SQLException {
		String realPath = serviceContext.getRealPath("/");
		service.performPatchDiscovery(realPath);
		Session session = PersistenceManager.getRequestDBSession();
		AmpXmlPatch xmlpatch= (AmpXmlPatch) session.get(AmpXmlPatch.class, testDbNamePatch);

		Collection<AmpXmlPatch> scheduledPatches=new ArrayList<AmpXmlPatch>();
		scheduledPatches.add(xmlpatch);
		
		int failedCount = service.processUnclosedPatches(scheduledPatches, serviceContext);
		
		assertFalse("Patch "+testDbNamePatch+" has failed to be applied properly", failedCount!=0);
	}
	
	/**
	 * Tests to see if the generic XSLT Transformation works properly
	 * @throws DgException
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public void testPatchDependencyCondition() throws DgException, HibernateException, SQLException {
		String realPath = serviceContext.getRealPath("/");
		service.performPatchDiscovery(realPath);
		Session session = PersistenceManager.getRequestDBSession();
		AmpXmlPatch xmlpatch1= (AmpXmlPatch) session.get(AmpXmlPatch.class, testDbNamePatch);
		AmpXmlPatch xmlpatch2= (AmpXmlPatch) session.get(AmpXmlPatch.class, testDependencyPatch);

		Collection<AmpXmlPatch> scheduledPatches=new ArrayList<AmpXmlPatch>();
		scheduledPatches.add(xmlpatch1);
		scheduledPatches.add(xmlpatch2);
		
		int failedCount = service.processUnclosedPatches(scheduledPatches, serviceContext);
		
		assertFalse("Patch "+testDependencyPatch+" has failed to be applied properly", failedCount!=0);
	}
	
	/**
	 * Tests to see if the generic SQL condition inside the patch works properly
	 * @throws DgException
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public void testSQLCondition() throws DgException, HibernateException, SQLException {
		String realPath = serviceContext.getRealPath("/");
		service.performPatchDiscovery(realPath);
		Session session = PersistenceManager.getRequestDBSession();
		AmpXmlPatch xmlpatch= (AmpXmlPatch) session.get(AmpXmlPatch.class, testSQLConditionPatch);
		
		Collection<AmpXmlPatch> scheduledPatches=new ArrayList<AmpXmlPatch>();
		scheduledPatches.add(xmlpatch);
		
		int failedCount = service.processUnclosedPatches(scheduledPatches, serviceContext);
		
		assertFalse("Patch "+testSQLConditionPatch+" has failed to be applied properly", failedCount!=0);
	}
	
	/**
	 * BSH Condition type test
	 * @throws DgException
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public void testBSHCondition() throws DgException, HibernateException, SQLException {
		String realPath = serviceContext.getRealPath("/");
		service.performPatchDiscovery(realPath);
		Session session = PersistenceManager.getRequestDBSession();
		AmpXmlPatch xmlpatch= (AmpXmlPatch) session.get(AmpXmlPatch.class, testBSHConditionPatch);
		
		Collection<AmpXmlPatch> scheduledPatches=new ArrayList<AmpXmlPatch>();
		scheduledPatches.add(xmlpatch);
		
		int failedCount = service.processUnclosedPatches(scheduledPatches, serviceContext);
		
		assertFalse("Patch "+testBSHConditionPatch+" has failed to be applied properly", failedCount!=0);
	}
	
	/**
	 * Tests to see if the patch Discovery step works properly
	 * @throws HibernateException
	 * @throws DgException
	 * @throws SQLException
	 */
	public void testPatchDiscovery() throws HibernateException, DgException, SQLException {
		String realPath = serviceContext.getRealPath("/");
		service.performPatchDiscovery(realPath);
		
		Session session = PersistenceManager.getRequestDBSession();
		File configDirFile = new File("testxmlpatches/xmlpatches");
		String[] list = configDirFile.list();
		for(int i=0;i<list.length;i++) {
			Object object = session.get(AmpXmlPatch.class, list[i]);
			assertFalse("Patch "+list[i]+" was not recorded properly", object==null);
		}
	}

}
