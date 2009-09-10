/**
 * XmlPatcherUtil.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.util;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatch;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.jaxb.Patch;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.engine.SessionFactoryImplementor;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
public final class XmlPatcherUtil {
	private static Logger logger = Logger.getLogger(XmlPatcherUtil.class);

	/**
	 * Finds and saves to db all new patch names and locations, that were not
	 * recorded previously.
	 * 
	 * @param dir
	 *            the dir to seek for unrecorded patches
	 * @param patchNames
	 *            the existing patch names
	 * @throws DgException
	 * @param appPath
	 *            the application path
	 */
	public static void recordNewPatchesInDir(String appPath, File dir,
			Set<String> patchNames) throws DgException {
		if (!dir.isDirectory())
			throw new RuntimeException(
					"Patch discovery location is not a directory!");
		String[] files = dir.list();
		for (int i = 0; i < files.length; i++) {
			File f = new File(dir, files[i]);
			// directories ignored in xmlpatch dir
			if (f.isDirectory())
				continue;
			if (patchNames.contains(f.getName()))
				continue;
			else {
				String location = f.getAbsolutePath().substring(
						appPath.length(),
						f.getAbsolutePath().length() - f.getName().length());
				AmpXmlPatch patch = new AmpXmlPatch(f.getName(), location);
				DbUtil.add(patch);
				patchNames.add(f.getName());
			}
		}
	}

	/**
	 * Checks if the jdbc connection is compatible with the given language type.
	 * This will check if the language type is part of the URL of the
	 * connection. Example language type "oracle". The oracle string should
	 * always be part of the jdbc URL
	 * 
	 * @return true if the langType is compatible with the connection
	 */
	public static boolean isSQLCompatible(String langType) {
		Connection con;
		boolean ret=false;
		try {
			con = getConnection();
			DatabaseMetaData metaData = con.getMetaData();
			if (metaData.getURL().toLowerCase().indexOf(langType.toLowerCase()) > -1)
				ret=true;
			con.close();
			return ret;
		} catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the JDBC connection out of the Session Factory. Do not get the
	 * connection directly from the session
	 * (org.hibernate.session.Session#connection is deprecated)
	 * 
	 * @return the connection object
	 */
	public static Connection getConnection() {
		SessionFactoryImplementor sfi = (SessionFactoryImplementor) PersistenceManager
				.getSessionFactory();
		try {
			return sfi.getConnectionProvider().getConnection();
		} catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Seeks the application directories in search of patch directories. The
	 * usual location is inside the modules dir
	 * (repository/modulename/xmlpatches). However this is not hardcoded. One
	 * usual location that is outside /repository/ is the generic patches dir
	 * (that do not belong to one specific module)
	 * 
	 * @param root
	 *            the root dir to start searching
	 * @return a set of FileS that represent discovered patch directories
	 */
	public static Set<File> discoverPatchDirs(String root) {
		File dir = new File(root);
		Set<File> patchDirs = new HashSet<File>();

		if (!dir.isDirectory())
			throw new RuntimeException(
					"Patch discovery location is not a directory!");
		String[] files = dir.list();
		for (int i = 0; i < files.length; i++) {
			File f = new File(dir, files[i]);
			if (f.isDirectory()) {
				if (f.getName().equals(XmlPatcherConstants.patchDirName))
					patchDirs.add(f);
				else
					patchDirs.addAll(discoverPatchDirs(f.getAbsolutePath()));
			}
		}
		return patchDirs;
	}

	/**
	 * Gets all the patch names which are also the primary keys, of all
	 * discovered patches.
	 * 
	 * @return a set with the patch names, naturally ordered
	 * @throws DgException
	 * @throws SQLException
	 * @throws HibernateException
	 */
	@SuppressWarnings("unchecked")
	public static Set<String> getAllDiscoveredPatchNames() throws DgException,
			HibernateException, SQLException {
		Session session = PersistenceManager.getRequestDBSession();
		Query query = session.createQuery("select p.patchId from "
				+ AmpXmlPatch.class.getName() + " p");
		List list = query.list();
		Set<String> ret = new TreeSet<String>();
		ret.addAll(list);
		PersistenceManager.releaseSession(session);
		return ret;
	}

	/**
	 * Unmarshalls using JAXB the xml file that the AmpXmlPatch object points to
	 * 
	 * @param serviceContext
	 * @param p
	 *            the patch file metaobject that holds the location URI
	 * @param log
	 * @param serviceContext the service context of the caller servlet, used to get the real application path
	 *            the patch log file that will be written in the end to the db
	 * @return the Patch object, unmarshalled
	 */
	public static Patch getUnmarshalledPatch(ServiceContext serviceContext,
			AmpXmlPatch p, AmpXmlPatchLog log) {
		FileInputStream inputStream;
		try {
			String filePath = serviceContext.getRealPath("/") + p.getLocation()
					+ p.getPatchId();

			logger.info("Unmarshalling " + filePath);
			inputStream = new FileInputStream(filePath);

			JAXBContext jc = JAXBContext
					.newInstance("org.digijava.module.xmlpatcher.jaxb");
			Unmarshaller m = jc.createUnmarshaller();

			// initialize JAXB 2.0 validation
			SchemaFactory sf = SchemaFactory
					.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(new File(serviceContext
					.getRealPath("/")
					+ XmlPatcherConstants.xsdLocation));
			m.setSchema(schema);
			m.setEventHandler(new DefaultValidationEventHandler());

			JAXBElement<Patch> enclosing = (JAXBElement<Patch>) m
					.unmarshal(inputStream);
			inputStream.close();
			return enclosing.getValue();
		} catch (Exception e) {
			logger.error(e);
			log.appendToLog(e);
			return null;
		}
	}

	/**
	 * Returns the list of XmlPatches that are not in close state
	 * 
	 * @see XmlPatcherConstants.PatchStates
	 * @return the Hibernate query result
	 * @throws DgException
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public static List<AmpXmlPatch> getAllDiscoveredUnclosedPatches()
			throws DgException, HibernateException, SQLException {
		Session session = PersistenceManager.getRequestDBSession();
		Query query = session
				.createQuery("from " + AmpXmlPatch.class.getName()
						+ " p WHERE p.state!="
						+ XmlPatcherConstants.PatchStates.CLOSED);
		List<AmpXmlPatch> list = query.list();
		PersistenceManager.releaseSession(session);
		return list;
	}
	
	public static Session getHibernateSession() {
		try {
			return PersistenceManager.getSession();
		} catch (HibernateException e1) {
			logger.error(e1);
			throw new RuntimeException(e1);
		} catch (SQLException e1) {
			logger.error(e1);
			throw new RuntimeException(e1);
		}
	}

	public static void closeHibernateSession(Session session) {
		try {
			PersistenceManager.releaseSession(session);
		} catch (SQLException e1) {
			logger.error(e1);
			throw new RuntimeException(e1);
		}
	}
}
