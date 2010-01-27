package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.form.ViewIndicatorsForm;
import org.digijava.module.aim.util.IndicatorUtil;

public class RemoveIndicator extends Action {
	 public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {		 
		 ViewIndicatorsForm allIndForm = (ViewIndicatorsForm) form;
		 String id = request.getParameter("indicatorId");
		 if (id != null){ 
			 AmpIndicator indicator=IndicatorUtil.getIndicator(new Long(id));
			 if(indicator.getValuesActivity()!=null){
				ActionErrors errors=new ActionErrors();
				Long siteId = RequestUtils.getSite(request).getId();
				String locale = RequestUtils.getNavigationLanguage(request).getCode();
				
				errors.add("indManError",new ActionError("error.admin.indManager.indicatorRemovalError", TranslatorWorker.translateText("Indicator is assigned to one or more activities", locale, siteId)));
				saveErrors(request, errors);
				return mapping.findForward("error");
			 }else{
				 IndicatorUtil.deleteIndicator(new Long(id));
			 }
	     }
		 return mapping.findForward("viewAll");
	 }
}
