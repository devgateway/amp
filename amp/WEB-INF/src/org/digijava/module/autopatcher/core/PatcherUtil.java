package org.digijava.module.autopatcher.core;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.digijava.module.autopatcher.exceptions.InvalidPatchRepositoryException;
import org.digijava.module.xmlpatcher.util.XmlPatcherUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class PatcherUtil {

	public static class PatchFilesComparator implements java.util.Comparator<File>
	{
		public int compare(File f1, File f2) {
			return ((Long)f1.lastModified()).compareTo((Long)(f2.lastModified()));
		}

	}
	
	private static Logger logger = Logger.getLogger(PatcherUtil.class);


	public static Set getAllAppliedPatches(Session session) throws HibernateException {
		
		Query query = session.createQuery("select p.absolutePatchName from "
				+ PatchFile.class.getName() + " p");
		List col = query.list();
		TreeSet ret=new TreeSet();
		ret.addAll(col);
		return ret;
	
	}
	
	/**
	 * @deprecated Use {@link XmlPatcherUtil#getFileMD5(File)} instead
	 */
	public static String getFileMD5(File f) throws NoSuchAlgorithmException,
			IOException {
				return XmlPatcherUtil.getFileMD5(f);
			}

	public static Collection<File> getAllPatchesFiles(String abstractPatchesLocation)
			throws InvalidPatchRepositoryException {
		File dir = new File(abstractPatchesLocation);
		List<File> patchFiles = new ArrayList<File>();
		if (!dir.isDirectory())
			throw new InvalidPatchRepositoryException(
					"Patches repository needs to be a dir!");
		String[] files = dir.list();
		for (int i = 0; i < files.length; i++) {
			File f = new File(dir, files[i]);
			if (f.isDirectory() && !f.getName().equals("CVS") && !f.getName().equals(".svn"))
				patchFiles.addAll(getAllPatchesFiles(f.getAbsolutePath()));
			if(!f.isDirectory())
				patchFiles.add(f);
		}
		Collections.sort(patchFiles, new PatchFilesComparator());
		return patchFiles;
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
