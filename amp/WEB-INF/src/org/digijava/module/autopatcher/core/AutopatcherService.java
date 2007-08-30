package org.digijava.module.autopatcher.core;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceException;
import org.digijava.module.autopatcher.exceptions.InvalidPatchRepositoryException;

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

	
	protected void processInitEvent(ServiceContext serviceContext)
			throws ServiceException {
		
			Session session;
			appliedPatches=new ArrayList();
			try {
				session = PersistenceManager.getSession();
			String realRootPath=serviceContext.getRealPath("/WEB-INF/");
			logger.debug("Computed WEB-INF realPath is "+realRootPath);
			logger.info("Applying patches...");
			Collection<File> allPatchesFiles = PatcherUtil.getAllPatchesFiles(serviceContext.getRealPath(patchesDir));
			Set allAppliedPatches = PatcherUtil.getAllAppliedPatches(session);
			Iterator i=allPatchesFiles.iterator();
			while (i.hasNext()) {
				File element = (File) i.next();
				if(allAppliedPatches.contains(element.getAbsolutePath())) continue;
				
				try {
				LineNumberReader bis=new LineNumberReader(new FileReader(element));
				StringBuffer sb= new StringBuffer();
				String s=bis.readLine();
				while(s!=null) {
				 sb.append(s);
				 s = bis.readLine();					
				}
				bis.close();
							
				StringTokenizer stok=new StringTokenizer(sb.toString(),";");
				while(stok.hasMoreTokens()) {
					
				String sqlCommand=stok.nextToken();
				if(sqlCommand.trim().equals("")) continue;
					
				Connection connection = PersistenceManager.getSession().connection();
				Statement st=connection.createStatement();
	
				st.execute(sqlCommand);
				st.close();
				
				}
				
				PatchFile pf=new PatchFile();
				pf.setAbsolutePatchName(element.getAbsolutePath());
				pf.setInvoked(new Timestamp(System.currentTimeMillis()));
				
				
				session.save(pf);
				
				appliedPatches.add(element.getAbsolutePath());
				
				} catch (Exception e ) {
					logger.error(e);
					e.printStackTrace();
				}
		
			}
			session.close();
			
			} catch (HibernateException e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException( "HibernateException Exception encountered", e1);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException( "SQLException Exception encountered", e1);
			} catch (InvalidPatchRepositoryException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException( "InvalidPatchRepositoryException Exception encountered", e);
			}
			
			
			logger.info(this.toString());
			
		

	}
	
	public String toString() {
		return "Autopatcher: Patches applied : "+appliedPatches;		
	}

	public String getPatchesDir() {
		return patchesDir;
	}

	public void setPatchesDir(String patchesDir) {
		this.patchesDir = patchesDir;
	}
	}

