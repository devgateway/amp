/**
 * XmlPatcherUtil.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.util;

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
import org.hibernate.Transaction;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.bind.util.JAXBResult;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;

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
     *            the application Path - usually serviceContext.getRealPath("/")
     * @param patchesMap the already discovered patches
     */
    public static void recordNewPatchesInDir(String appPath, File dir,
            Set<String> patchNames, Map<String, AmpXmlPatch> patchesMap) throws DgException {
        if (!dir.isDirectory())
            throw new RuntimeException(
                    "Patch discovery location is not a directory!");
        String[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            File f = new File(dir, files[i]);
            // directories ignored in xmlpatch dir
            if (f.isDirectory()){
                if (f.getName().compareTo(".svn") != 0) {
                    recordNewPatchesInDir(appPath, f, patchNames, patchesMap);
                }
                continue;
            }
            /*if(f.getName().endsWith(".class")){
                continue;
            }*/
            if (patchNames.contains(f.getName())) {
                
                AmpXmlPatch patch = patchesMap.get(f.getName());                
                //if no recorded patch is found, then there are two unrecorded patches with same name=>fail
                //if there is a recorded patch but its path is different than the current file=>fail
                if(patch==null || !patch.getLocation().equals(computePatchFileLocation(f,appPath))) {
                    if(patch!=null){
                        logger.info("old location: "+patch.getLocation());
                        logger.info("new location: "+computePatchFileLocation(f,appPath));
                    }
                    else{
                        logger.info("pacth is null ");
                    }
                    logger.error("Patch duplication detected! The name "+f.getName()+" is used by two or more patches." +
                            " Remove duplicates and restart the server.\n You are not allowed to use one patch name twice even if the older patch has been deleted.");
                }
            }
            else {
                String location=computePatchFileLocation(f, appPath);
                AmpXmlPatch patch = new AmpXmlPatch(f.getName(), location);
                DbUtil.add(patch);
                patchNames.add(f.getName());
                logger.info("Found new patch "+patch.getPatchId()+" in "+patch.getLocation());
            }
        }
    }

    /**
     * Gets the location parameter saved with AmpXmlPatch based on the patch File and the appPath
     * @param patchFile the File object for the patch as it has been found
     * @param appPath the application Path - usually serviceContext.getRealPath("/")
     * @return
     */
    public static String computePatchFileLocation(File patchFile, String appPath) {
        return patchFile.getAbsolutePath().substring(
                appPath.length(),
                patchFile.getAbsolutePath().length() - patchFile.getName().length()).replaceAll("\\\\", "/");
        
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
        boolean ret = false;
        try {
            con = getConnection();
            DatabaseMetaData metaData = con.getMetaData();
            if (metaData.getURL().toLowerCase().indexOf(langType.toLowerCase()) > -1)
                ret = true;
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
    
        try {
            return PersistenceManager.getJdbcConnection();
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
            if (f.isDirectory() && !f.getName().equals("CVS") && !f.getName().equals(".svn")) {
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
//beginTransaction();
        Query query = session.createQuery("select p.patchId from "
                + AmpXmlPatch.class.getName() + " p");
        List list = query.list();
        //tx.commit();
        Set<String> ret = new TreeSet<String>();
        ret.addAll(list);
        return ret;
    }

    static javax.xml.transform.TransformerFactory transFact = javax.xml.transform.TransformerFactory.newInstance( );
    static javax.xml.transform.Transformer cached_transformer;
    static String lastPathTransformerPath = null;
    
    static Unmarshaller cached_unmarshaller;
    static String lastUnmarshallerPath = null;

    static javax.xml.transform.Transformer getTransformer(String xslName) throws TransformerConfigurationException
    {
        //logger.error("requested Transformer for " + xslName);
        // recreate transformer if a different one was requested or this is the first call
        boolean needToRecreate = lastPathTransformerPath == null || (!lastPathTransformerPath.equals(xslName));
        
        //logger.error("needToRecreate = " + needToRecreate);
        if (needToRecreate)
            cached_transformer = transFact.newTransformer(new StreamSource(xslName));
        
        lastPathTransformerPath = xslName;
        return cached_transformer;
    }
    
    static Unmarshaller getUnmarshaller(String schemaURI) throws JAXBException, SAXException
    {
        boolean needToRecreate = lastUnmarshallerPath == null || (!lastUnmarshallerPath.equals(schemaURI));
        
        if (needToRecreate)
        {
            JAXBContext jc = JAXBContext.newInstance(XmlPatcherConstants.jaxbPackage);
            cached_unmarshaller = jc.createUnmarshaller();

            // initialize JAXB 2.0 validation
            SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = sf.newSchema(new File(schemaURI));
            cached_unmarshaller.setSchema(schema);
            cached_unmarshaller.setEventHandler(new DefaultValidationEventHandler());
        }
        lastUnmarshallerPath = schemaURI;
        return cached_unmarshaller;
    }
    
    public static java.util.Map<AmpXmlPatch, Patch> loadedPatches = new HashMap<AmpXmlPatch, Patch>();
    /**
     * Unmarshalls using JAXB the xml file that the AmpXmlPatch object points to
     * 
     * @param serviceContext
     * @param p
     *            the patch file metaobject that holds the location URI
     * @param log
     * @param serviceContext
     *            the service context of the caller servlet, used to get the
     *            real application path the patch log file that will be written
     *            in the end to the db
     * @return the Patch object, unmarshalled
     */
    public static Patch getUnmarshalledPatch(ServiceContext serviceContext,
            AmpXmlPatch p, AmpXmlPatchLog log) {
        try 
        {
            if ((!loadedPatches.containsKey(p)))
            {
                //perform XSLT transformation. See xmlpatcher.xsl
                javax.xml.transform.Transformer trans = getTransformer(serviceContext.getRealPath("/")+XmlPatcherConstants.xslLocation);
                Unmarshaller um = getUnmarshaller(serviceContext.getRealPath("/") + XmlPatcherConstants.xsdLocation);
                JAXBResult result = new JAXBResult(um);
                
                trans.transform(new StreamSource(getXmlPatchAbsoluteFileName(p, serviceContext)), result);
                Object tree=result.getResult();
            
                JAXBElement<Patch> enclosing = (JAXBElement<Patch>) tree;
                Patch funcResult = enclosing.getValue();
            
                loadedPatches.put(p, funcResult);
            }
            return loadedPatches.get(p);
        }
        catch (Exception e)
        {
            logger.error(String.format("error while unmarshalling patch %s: %s", p, e.getMessage()), e);
            if (log != null) log.appendToLog(e);
            throw new Error(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void deleteUnitTestPatches() throws DgException,
            HibernateException, SQLException {
        Session session = PersistenceManager.getRequestDBSession();
        Query query = session.createQuery("select p from "
                + AmpXmlPatch.class.getName() + " p WHERE p.patchId LIKE 'junit-test%'");
        List list = query.list();
        
        Iterator iterator = list.iterator();
        while(iterator.hasNext()) {
            AmpXmlPatch p=(AmpXmlPatch) iterator.next();
            DbUtil.delete(p);
        }
    }

    /**
     * Adds a new log object to an existing patch object
     * @param p the existing patch object, this will be re-fetched from db to get a lazy version
     * @param log the log object
     */
    public static void addLogToPatch(AmpXmlPatch p, AmpXmlPatchLog log) {
        Session sess = null;
        Transaction tx = null;

        try {
            sess = PersistenceManager.getSession();
//beginTransaction();
            AmpXmlPatch lazyPatch = (AmpXmlPatch) sess.load(AmpXmlPatch.class,
                    p.getPatchId());
            log.setPatch(lazyPatch);
            lazyPatch.getLogs().add(log);
            sess.saveOrUpdate(lazyPatch);
            //tx.commit();
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Reconstructs the absolute location of the patch file on the disk based on
     * the patch name, the relative path inside the WAR plus the absolute WAR
     * path
     * 
     * @param p
     * @param serviceContext
     * @return
     */
    public static String getXmlPatchAbsoluteFileName(AmpXmlPatch p,
            ServiceContext serviceContext) {
        return serviceContext.getRealPath("/") + p.getLocation()
                + p.getPatchId();
    }
    
    /**
     * Checks if the current patch is deprecating other patches (has deprecate tags). If so, it deprecates those patches.
     * This is run BEFORE the actual patch execution is invoked, thus it prevents deprecated patches to ever be applied.
     * @param p
     * @param log
     * @throws SQLException 
     * @throws HibernateException 
     */
    public static void applyDeprecationTags(Patch p, AmpXmlPatchLog log) throws HibernateException, SQLException {
        if (p==null || p.getDeprecate()==null) return;
        for (String deprecatedId : p.getDeprecate()) {
            // load the deprecated patch metadata:
            Session session = PersistenceManager.getSession();
            
            AmpXmlPatch patch = (AmpXmlPatch) session.get(AmpXmlPatch.class,
                    deprecatedId);
            if (patch == null) {
                log.appendToLog("Referenced deprecated patch does not exist: "
                        + deprecatedId);
                return;
            }
            if (XmlPatcherConstants.PatchStates.DEPRECATED != patch.getState()) {
                patch.setState(XmlPatcherConstants.PatchStates.DEPRECATED);
                DbUtil.update(patch);
                logger.info("Patch "+deprecatedId+" marked deprecated");
            }
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
                        + " p WHERE p.state NOT IN ("
                        + XmlPatcherConstants.PatchStates.CLOSED+","+XmlPatcherConstants.PatchStates.DEPRECATED+","+XmlPatcherConstants.PatchStates.DELETED+")");
        List<AmpXmlPatch> list = query.list();
        return list;
    }
    

    
    /**
     * Returns the count of the list of discovered XmlPatches
     * 
     * @return the Hibernate query result
     * @throws DgException
     * @throws HibernateException
     * @throws SQLException
     */
    public static Integer countAllDiscoveredPatches()
            throws DgException, HibernateException, SQLException {
        Session session = PersistenceManager.getRequestDBSession();
            Integer ret= ((Integer)session.createQuery("select count(*) from " + AmpXmlPatch.class.getName()).iterate().next()).intValue();
        return ret;
    }
    
    
    
    /**
     * Returns the list of discovered XmlPatches
     * 
     * @return the Hibernate query result
     * @throws DgException
     * @throws HibernateException
     * @throws SQLException
     */
    public static List<AmpXmlPatch> getAllDiscoveredPatches()
            throws DgException, HibernateException, SQLException {
        Session session = PersistenceManager.getRequestDBSession();
//beginTransaction();
        Query query = session
                .createQuery("from " + AmpXmlPatch.class.getName());
        List<AmpXmlPatch> list = query.list();
        return list;
    }
    
    
    /**
     * Creates a map with the key patchId and the value AmpXmlPatch based on {@link #getAllDiscoveredPatches()}
     * @return the map
     * @throws HibernateException
     * @throws DgException
     * @throws SQLException
     * @see {@link #getAllDiscoveredPatches()}
     */
    public static Map<String,AmpXmlPatch> getAllDiscoveredPatchesMap() throws HibernateException, DgException, SQLException {
        List<AmpXmlPatch> discoveredPatches = getAllDiscoveredPatches();
        Map<String,AmpXmlPatch> ret=new HashMap<String,AmpXmlPatch>();
        for(AmpXmlPatch patch: discoveredPatches) {
            ret.put(patch.getPatchId(), patch);
        }
        return ret;
    }
    
    /**
     * Returns the list of discovered XmlPatches using pagination
     * @param startIndexInt - the start of index
     * @param records - the max number of records
     * @return the Hibernate query result
     * @throws DgException
     * @throws HibernateException
     * @throws SQLException
     */
    public static List<Object[]> getAllDiscoveredPatches(int startIndexInt,int recordsInt,String sortBy,String dir)
            throws DgException, HibernateException, SQLException {
        Session session = PersistenceManager.getRequestDBSession();
        if(sortBy.equals("attempts")) sortBy="count(l)";else sortBy="p."+sortBy;
        Query query = session
                .createQuery("SELECT p.patchId,p.discovered,p.location,p.state,count(l) FROM " + AmpXmlPatch.class.getName()+ " p left join p.logs l group by p.patchId, p.discovered,p.location,p.state order by "+sortBy+" "+dir);
        query.setFirstResult(startIndexInt);
        query.setMaxResults(recordsInt);
        List<Object[]> list = query.list();
        return list;
    }

    public static Session getHibernateSession() {
        return PersistenceManager.getSession();
    }

    public static void closeHibernateSession(Session session) {
        //PersistenceManager.releaseSession(session);
    }

    /**
     * Digests the file contents and produces its MD5 as output
     * 
     * @param f
     *            the file to digest the contents
     * @return the MD5 for the file
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getFileMD5(File f) throws NoSuchAlgorithmException,
            IOException {
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();

        BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(f));

        byte[] buffer = new byte[8192];
        int read = 0;
        while ((read = bis.read(buffer)) > 0) {
            algorithm.update(buffer, 0, read);
        }
        bis.close();
        byte[] md5sum = algorithm.digest();
        BigInteger bigInt = new BigInteger(1, md5sum);
        String md5 = bigInt.toString(16);
        return md5;
    }
}
