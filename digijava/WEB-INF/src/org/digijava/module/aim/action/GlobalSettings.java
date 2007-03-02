package org.digijava.module.aim.action;
/*
* @ author Govind G Dalwani
*/
import org.apache.log4j.Logger;
import org.apache.struts.action.Action ;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm ;
import org.apache.struts.action.ActionMapping ;
import org.apache.struts.action.ActionForward ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.form.GlobalSettingsForm;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class GlobalSettings extends Action {
	private static Logger logger = Logger.getLogger(GlobalSettings.class);
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) {
					return mapping.findForward("index");
				}
			}
		
		FeaturesUtil featUtil = new FeaturesUtil();
		GlobalSettingsForm gsForm = (GlobalSettingsForm) form;
		if(request.getParameter("save")!=null)
		{
			String save = request.getParameter("save");
			logger.info(" this is the action "+save);
			AmpGlobalSettings ampGS = new AmpGlobalSettings();
			logger.info(" id is "+gsForm.getGlobalId()+"   name is "+gsForm.getGlobalSettingsName()+ "  value is... "+gsForm.getGsfValue());
			ampGS.setGlobalId(gsForm.getGlobalId());
			ampGS.setGlobalSettingsName(gsForm.getGlobalSettingsName());
			ampGS.setGlobalSettingsValue(gsForm.getGsfValue());
			DbUtil.update(ampGS);
			ActionErrors errors = new ActionErrors(); 
					
		}
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
		gsForm.setGsfCol(col);
		Iterator itr = col.iterator();
		while (itr.hasNext())
		{
			AmpGlobalSettings ampGS = (AmpGlobalSettings)itr.next();
			gsForm.setGlobalId(ampGS.getGlobalId());
			gsForm.setGlobalSettingsName(ampGS.getGlobalSettingsName());
			gsForm.setGsfValue(ampGS.getGlobalSettingsValue());
		}
		Collection countries = featUtil.getCountryNames();
		gsForm.setCountryNameCol(countries);
		return mapping.findForward("viewGS");
	}
}