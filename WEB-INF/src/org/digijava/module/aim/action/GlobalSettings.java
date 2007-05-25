package org.digijava.module.aim.action;
/*
* @ author Govind G Dalwani
*/
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.form.GlobalSettingsForm;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.FeaturesUtil;

public class GlobalSettings extends Action {
	private static Logger logger = Logger.getLogger(GlobalSettings.class);
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		ActionErrors errors							= new ActionErrors();
		boolean refreshGlobalSettingsCache			= false;
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) {
					return mapping.findForward("index");
				}
			}
		
		//FeaturesUtil featUtil = new FeaturesUtil();
		GlobalSettingsForm gsForm = (GlobalSettingsForm) form;
		if(request.getParameter("save")!=null)
		{
			String save = request.getParameter("save");
			logger.info(" this is the action "+save);
			
			logger.info(" id is "+gsForm.getGlobalId()+"   name is "+gsForm.getGlobalSettingsName()+ "  value is... "+gsForm.getGsfValue());
			this.updateGlobalSetting(gsForm.getGlobalId(), gsForm.getGsfValue());
			//ActionErrors errors = new ActionErrors(); 
			refreshGlobalSettingsCache	= true;		
		}
//		if (gsForm.getNewSettingName() != null && gsForm.getNewSettingName().length() > 0){
//			
//			
//		}
		/*Collection a = FeaturesUtil.getDefaultCountryISO();
		String iso=null;
		Iterator itr1 = a.iterator();
		while (itr1.hasNext())
		{
			AmpGlobalSettings ampGS = (AmpGlobalSettings)itr1.next();
			logger.info(" hope this is the correct one.. "+ampGS.getGlobalSettingsValue());
			iso = ampGS.getGlobalSettingsValue();
		}
		logger.info(" this is the ISO .... in iso "+iso);
		Collection b = FeaturesUtil.getDefaultCountry(iso);
		Iterator itr2 = b.iterator();
		while (itr2.hasNext())
		{
			Country ampGS = (Country)itr2.next();
			logger.info(" hope this is the correct country name one.. "+ampGS.getCountryName());
		}*/
		Collection col = FeaturesUtil.getGlobalSettings();
		if (refreshGlobalSettingsCache) {
			FeaturesUtil.setGlobalSettingsCache(col);
			FeaturesUtil.logGlobalSettingsCache();
			FeatureManager.refreshTemplateGlobalSettings(getServlet());
		}
		gsForm.setGsfCol(col);
		Iterator itr = col.iterator();
		while (itr.hasNext())
		{
			AmpGlobalSettings ampGS = (AmpGlobalSettings)itr.next();
//			gsForm.setGlobalId(ampGS.getGlobalId()); 
//			gsForm.setGlobalSettingsName(ampGS.getGlobalSettingsName());
//			gsForm.setGsfValue(ampGS.getGlobalSettingsValue());
//			
			String possibleValuesTable		= ampGS.getGlobalSettingsPossibleValues();
			Collection possibleValues		= null;
			Map possibleValuesDictionary	= null;
			if ( possibleValuesTable != null ) {
				possibleValues				= this.getPossibleValues(possibleValuesTable);
				possibleValuesDictionary	= new HashMap();
				Iterator pvIterator			= possibleValues.iterator();
				
				while (pvIterator.hasNext()) {
					KeyValue keyValue	= (KeyValue) pvIterator.next();
					possibleValuesDictionary.put(keyValue.getKey(), keyValue.getValue());
				}
			}
			gsForm.setPossibleValues( ampGS.getGlobalSettingsName(), possibleValues );
			gsForm.setPossibleValuesDictionary( ampGS.getGlobalSettingsName(), possibleValuesDictionary );
		}
		Collection countries = FeaturesUtil.getCountryNames();
		gsForm.setCountryNameCol(countries);
		return mapping.findForward("viewGS");
	}
	
	
	private Collection getPossibleValues(String tableName)
	{
		Collection ret 	= new Vector();
		Session session = null;
		String qryStr = null;
		Query qry = null;
		
		if (tableName == null || tableName.length() == 0)
			return ret;
		
		try{
				session				= PersistenceManager.getSession();
				Connection	conn	= session.connection();
				Statement st		= conn.createStatement();
				qryStr 				= "select id, value from "+tableName ;
				//qry 				= session.createSQLQuery(qryStr,"kv",KeyValue.class);
				ResultSet rs		= st.executeQuery(qryStr);
				//qry.setString (0, tableName);
				//Iterator iterator 	= session.iterate(qryStr);
				
				//Collection coll		= qry.list();
				//Iterator iterator 	= coll.iterator(); 
				while (rs.next()){
					
					//logger.info("Values:" + rs.getString(1) + "," + rs.getString(2) );
					KeyValue keyValue	= new KeyValue( rs.getObject(1)+"", rs.getString(2) );
					
					ret.add( keyValue );
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
		return ret;
	}
	
	private void  updateGlobalSetting(Long id, String value) {
		
		Session session 	= null;
		String qryStr 		= null;
		Query qry 			= null;
		Transaction tx		= null;
		try{
				session					= PersistenceManager.getSession();
				tx						= session.beginTransaction();
				
				qryStr 					= "select gs from "+ AmpGlobalSettings.class.getName() + " gs where gs.globalId = :id " ;
				qry 					= session.createQuery(qryStr);
				qry.setLong ("id", id.longValue());
				AmpGlobalSettings ags	= (AmpGlobalSettings) qry.list().get(0);
				ags.setGlobalSettingsValue(value);
				tx.commit();

		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed !");
				}
			}
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
	}
}