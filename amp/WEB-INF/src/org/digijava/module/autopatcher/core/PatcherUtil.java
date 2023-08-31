package org.digijava.module.autopatcher.core;

import org.apache.log4j.Logger;
import org.digijava.module.autopatcher.exceptions.InvalidPatchRepositoryException;
import org.digijava.module.autopatcher.schema.Patch;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class PatcherUtil {

    public static class PatchFilesComparator implements java.util.Comparator<File>
    {
        public int compare(File f1, File f2) {
            return Long.compare(f1.lastModified(), (f2.lastModified()));
        }

    }
    
    private static Logger logger = Logger.getLogger(PatcherUtil.class);

    

    public static Set getAllAppliedPatches(Session session) throws HibernateException {
        
        Query query = session.createQuery("select p.absolutePatchName from "
                + PatchFile.class.getName() + " p");
        List col = query.list();
        TreeSet ret = new TreeSet(col);
        return ret;
    
    }
    
    public static String getFileMD5(File f) throws NoSuchAlgorithmException,
            IOException {
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();

        BufferedInputStream bis = new BufferedInputStream(
                Files.newInputStream(f.toPath()));

        byte[] buffer = new byte[8192];
        int read = 0;
        while ((read = bis.read(buffer)) > 0) {
            algorithm.update(buffer, 0, read);
        }
        bis.close();
        byte[] md5sum = algorithm.digest();
        BigInteger bigInt = new BigInteger(1, md5sum);
        return bigInt.toString(16);
    }

    public static Collection<File> getAllPatchesFiles(String abstractPatchesLocation)
            throws InvalidPatchRepositoryException {
        File dir = new File(abstractPatchesLocation);
        List<File> patchFiles = new ArrayList<File>();
        if (!dir.isDirectory())
            throw new InvalidPatchRepositoryException(
                    "Patches repository needs to be a dir!");
        String[] files = dir.list();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            File f = new File(dir, files[i]);
            if (f.isDirectory() && !f.getName().equals("CVS") && !f.getName().equals(".svn"))
                patchFiles.addAll(getAllPatchesFiles(f.getAbsolutePath()));
            if(!f.isDirectory())
                patchFiles.add(f);
        }
        patchFiles.sort(new PatchFilesComparator());
        return patchFiles;
    }

    public static Patch getUnmarshalledPatch(File patchFile)
            throws JAXBException {
        JAXBContext jc = JAXBContext
                .newInstance("org.digijava.module.autopatcher.schema");
        Unmarshaller m = jc.createUnmarshaller();
        m.setValidating(true);

        Patch p = (Patch) m.unmarshal(patchFile);
        
        return p;
    }

    /*
     
    public static Collection<File> getAllXMLPatchFiles(String abstractPatchesLocation)
            throws InvalidPatchRepositoryException {
        HashMap<File, Patch> xmlPatches = new HashMap<File, Patch>();
        Collection abstractFileNames = getAllPatchesFiles(abstractPatchesLocation);
        Iterator i = abstractFileNames.iterator();
        while (i.hasNext()) {
            File element = (File) i.next();
            try {
                xmlPatches.put(element,getUnmarshalledPatch(element));
            } catch (JAXBException e) {
                logger.error("XML Validation failed for file "
                        + element.getAbsolutePath());
                logger.error(e);
                e.printStackTrace();
            }
        }
        return xmlPatches;
    }
    */

}
