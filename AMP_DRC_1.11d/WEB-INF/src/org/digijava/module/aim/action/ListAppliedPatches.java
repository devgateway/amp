package org.digijava.module.aim.action;
/*
* @ author Clipa Cornel Dan
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.form.ListAppliedPatchesForm;
import org.digijava.module.aim.form.GlobalSettingsForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.aim.helper.CountryBean;
import org.digijava.module.autopatcher.core.PatchFile;

public class ListAppliedPatches extends Action {
	private static Logger logger 				= Logger.getLogger(ListAppliedPatches.class);
	private ActionErrors errors					= new ActionErrors();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		String param = request.getParameter("view");
		
		if (param != null) {
			
			File dir1 = new File(".");
			File dir2 = dir1.getCanonicalFile().getParentFile();
			
			ActionServlet serv = getServlet();
			//logger.info("alt path :"+serv.getServletContext().getRealPath("/patches/"));
			File dir = new File(serv.getServletContext().getRealPath("/patches/")+"/"+param);
			logger.info(dir.getPath());
			
			BufferedReader r = new BufferedReader(new FileReader(dir));
			
			char buffer[] = new char[(int)dir.length()];
			
			r.read(buffer,0, (int)dir.length());
			String s = new String(buffer);

			ListAppliedPatchesForm apForm = (ListAppliedPatchesForm) form;
			apForm.setContent(s);
				
			return mapping.findForward("viewPatch");
		}
		else {
			ListAppliedPatchesForm apForm = (ListAppliedPatchesForm) form;
			List patches = null;
			patches = this.getPatchNames();
			apForm.setPatch(patches);
			return mapping.findForward("forward");
		}
	}

	private List getPatchNames()
	{
		Collection ret 	= new Vector();
		Session session = null;
		String qryStr = null;
		Query qry = null;
		List col = null;
		
		try{
				session				= PersistenceManager.getSession();
				Connection	conn	= session.connection();
				Statement st		= conn.createStatement();

				Query query = session.createQuery("select plm from " + PatchFile.class.getName() + " plm order by plm.invoked desc");
				col = query.list();
				
				Iterator it = col.iterator();

				while (it.hasNext()) {
					PatchFile p = (PatchFile) it.next();
					//logger.info("Getting patches:" + p.getAbsolutePatchName() + "," + p.getInvoked());
				}

				conn.close();

		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return col;
	}
}
