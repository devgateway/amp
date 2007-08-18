/**
 * 
 */
package org.digijava.module.autopatcher.core;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.autopatcher.dbentity.AmpPatch;
import org.digijava.module.autopatcher.dbentity.AmpPatchLog;
import org.digijava.module.autopatcher.exceptions.InvalidPatchDataException;
import org.digijava.module.autopatcher.exceptions.InvalidPatchRepositoryException;
import org.digijava.module.autopatcher.exceptions.MissingPatchException;
import org.digijava.module.autopatcher.schema.Patch;
import org.digijava.module.autopatcher.schema.SqlType;
import org.exolab.castor.mapping.xml.Sql;

/**
 * @author mihai
 *
 */
public class PatcherWorker {
	private static Logger logger = Logger.getLogger(PatcherWorker.class);
	private String patchesDir;
	private static String catalogName;
	
	private static Map patches;
	
	public PatcherWorker(String patchesDir) {
		this.patchesDir=patchesDir;
	}

	public String getPatchesDir() {
		return patchesDir;
	}

	
	public AmpPatch getPatchByName(String patchName) throws HibernateException, SQLException, MissingPatchException {
		Session session = PersistenceManager.getSession();
		Query query = session.createQuery("select from p from "
				+ AmpPatch.class.getName() + " p.name=:name");
		query.setParameter("name", patchName);
		List list = query.list();
		session.close();
		if(list.size()==0) throw new MissingPatchException("Cannot find a db reference for a patch with name "+patchName);
		return (AmpPatch) list.get(0);
	}
	
	private void addPatchLog(AmpPatchLog apl) throws HibernateException, SQLException {
		Session session = PersistenceManager.getSession();
		long endTime=System.currentTimeMillis();		
		apl.setElapsed(new Long(endTime-apl.getInvoked().getTime()));
		logger.debug("Saving AmpPatchLog for patch: "+apl.getPatch().getName());
		session.save(apl);
		session.close();
	}
	
	public boolean executePatch(String patchName,String reason) throws HibernateException, SQLException, MissingPatchException, InvalidPatchDataException  {
		if(reason==null) reason=AmpPatchLog.REASON_PENDING_EXEC;
		AmpPatchLog apl=new AmpPatchLog();
		apl.setActionName(AmpPatchLog.ACTION_APPLY);
		apl.setInvoked(new Timestamp(System.currentTimeMillis()));
		apl.setActionReason(reason);
		
		
		AmpPatch ap = getPatchByName(patchName);
		apl.setPatch(ap);
		
		logger.info("-->Applying patch "+ap.getName());

		
		if(ap.isSuccessful() && !ap.isPendingExec())  {
			logger.info("-->Patch has already been applied successfully in the past and it is not pending for re-exec. Will not re-apply.");
			return true;
		}
		
		if(!ap.isPendingExec()) {
			logger.info("-->Patch is not pending for execution! Will not apply.");
			return false;
		}
		
		try {
		//load the related xml patch
		File f=new File(ap.getAbstractLocation());
		if (!f.canRead()) throw new MissingPatchException("Cannot read the file " + ap.getAbstractLocation());
		Patch up;
			up = PatcherUtil.getUnmarshalledPatch(f);
		
		
		logger.info(Util.getBeanAsString(up, "", false));
		
		if(up.getDependencies()!=null && !up.getDependencies().trim().equals("")) {
			//the patch has dependencies, check their status first
			logger.info("-->Applying dependencies");
			StringTokenizer st=new StringTokenizer(up.getDependencies());
			while(st.hasMoreTokens()) {
				String depPatchName=st.nextToken().trim();
				if(patchName.equals(depPatchName)) 
					throw new InvalidPatchDataException("Dependefilesncies on _self_ are not allowed! Check the dependencies element!");				
				logger.info("-->Checking patch dependency "+ap.getName()+"->"+depPatchName);
				boolean b = executePatch(depPatchName,AmpPatchLog.REASON_REQUESTED_BY_DEPENDENCY);
				if(b==false) return false;
			}
		}
		
		logger.debug("Check if the patch can be applied to this database");
		String databaseTargets = up.getDatabaseTargets();
			if(databaseTargets!=null && databaseTargets.trim().length()>0 && databaseTargets.indexOf(catalogName)==-1) {
				apl.setResponse("Patch cannot be applied to this database. Allowed databaseTargets are "+databaseTargets);
				logger.info(apl.getResponse());
				addPatchLog(apl);
			}
			
		
		List sql = up.getSql();		 	
		Iterator i=sql.iterator();
		while (i.hasNext()) {
			SqlType element = (SqlType) i.next();
			element.getType()
		}
			
		
		
		
		} catch (JAXBException e) {
			apl.setResponse("XML validation/parsing failed for patch "+ap.getAbstractLocation());
			logger.error(apl);
			addPatchLog(apl);
		}
		return true;
	}
	
	/**
	 * Reads the db patches list and searches for all xml files in the pachets dir. 
	 * If new xml patches are found, or if their md5 has changed, they are flagged for exec 
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public void refreshPatches() throws HibernateException, SQLException {
		
		logger.debug(PatcherMessages.getString("PatcherWorker.1")); //$NON-NLS-1$
		Map<String, AmpPatch> patches;
		Session hs=PersistenceManager.getSession();
		//read the catalog name for later usage:
		catalogName=hs.connection().getCatalog();
		
		try {
			patches = PatcherUtil.getAllRecordedPatches(hs);
		} catch (HibernateException e) {
			logger.error(PatcherMessages.getString("PatcherWorker.0")); //$NON-NLS-1$
			logger.error(e);
			return;
						
		} catch (SQLException e) {
			logger.error(PatcherMessages.getString("PatcherWorker.0")); //$NON-NLS-1$
			logger.error(e);
			return;
		}
		
		logger.debug(PatcherMessages.getString("PatcherWorker.2")); //$NON-NLS-1$
		Collection<File> allXMLPatches;
		try {
			allXMLPatches = PatcherUtil.getAllPatchesFiles(patchesDir);
		} catch (InvalidPatchRepositoryException e) {
			logger.error(PatcherMessages.getString("PatcherWorker.3")); //$NON-NLS-1$
			e.printStackTrace();
			return;
		}
		
		//add all new patches from xml as a reference in the database
		logger.debug(PatcherMessages.getString("PatcherWorker.4")); //$NON-NLS-1$
		Iterator i=allXMLPatches.iterator();
		while (i.hasNext()) {
			File element = (File) i.next();
			String fileName=element.getName();
			if(patches.get(fileName)==null) {
				//patch is new
				logger.debug(fileName);
				logger.debug(PatcherMessages.getString("PatcherWorker.5")); //$NON-NLS-1$
				AmpPatch ap=new AmpPatch();
				ap.setAbstractLocation(element.getAbsolutePath());
				ap.setName(element.getName());
				ap.setDiscovered(new Timestamp(System.currentTimeMillis()));
				ap.setLastInvoked(null);
				try {
					ap.setMd5(PatcherUtil.getFileMD5(element));
				} catch (NoSuchAlgorithmException e) {
					logger.error(PatcherMessages.getString("PatcherWorker.6")); //$NON-NLS-1$
					logger.error(e);									
				} catch (IOException e) {
					logger.error(PatcherMessages.getString("PatcherWorker.7"));  //$NON-NLS-1$
					logger.error(e);
					return;
				}
				hs.save(ap);
				patches.put(ap.getName(), ap);
			}
		}
		
		
		/* patches get flagged for re-execution if:
			- md5 changes
			- modif date changes (optional) - not implemented
		*/
		
		logger.debug("Checking for MD5 modified patches..."); //$NON-NLS-1$
		i=allXMLPatches.iterator();
		while (i.hasNext()) {
			File element = (File) i.next();
			String fileMD5=null;
			try {
				fileMD5 = PatcherUtil.getFileMD5(element);
				AmpPatch p = patches.get(element.getName());
				if(!p.getMd5().equals(fileMD5)) {
					//file has been changed. flag for re-execution
					p.setPendingExec(true);
					hs.saveOrUpdate(p);
				}
			} catch (NoSuchAlgorithmException e) {
				logger.error(PatcherMessages.getString("PatcherWorker.6")); //$NON-NLS-1$
				logger.error(e);				
			} catch (IOException e) {
				logger.error(PatcherMessages.getString("PatcherWorker.7"));  //$NON-NLS-1$
				logger.error(e);
				return;
			}
			
		}
		
		hs.close();
	}

	public static String getCatalogName() {
		return catalogName;
	}

	public static void setCatalogName(String catalogName) {
		PatcherWorker.catalogName = catalogName;
	}
	
}
