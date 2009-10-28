package org.digijava.module.aim.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.fmtool.util.FMToolConstants;
import org.digijava.module.aim.form.FMAdvancedForm;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.Session;

public class FMAdvancedModeAction extends DispatchAction{

	private static Logger log = Logger.getLogger(FMAdvancedModeAction.class);
	private static int paddingShift = 20;
	private ServletContext ampContext = null;

	public ActionForward showModule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		FMAdvancedForm fmForm = (FMAdvancedForm)form;
		String fmName = request.getParameter("fmName");
		fmForm.getFmeList().clear();
		
		if (fmName != null){
			AmpModulesVisibility module = FeaturesUtil.getModuleVisibility(fmName);
			fmForm.setPaddingOffset(addToList(fmForm, module, 0)*-1);
		}
		
		return  mapping.findForward("default");
	}

	public ActionForward showFeature(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		FMAdvancedForm fmForm = (FMAdvancedForm)form;
		String fmName = request.getParameter("fmName");
		fmForm.getFmeList().clear();
		
		if (fmName != null){
			AmpFeaturesVisibility feature = FeaturesUtil.getFeatureVisibility(fmName);
			fmForm.setPaddingOffset(addToList(fmForm, feature, 0)*-1);
		}
		
		return  mapping.findForward("default");
	}
	
	public ActionForward showField(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		FMAdvancedForm fmForm = (FMAdvancedForm)form;
		String fmName = request.getParameter("fmName");
		fmForm.getFmeList().clear();
		
		if (fmName != null){
			AmpFieldsVisibility field = FeaturesUtil.getFieldVisibility(fmName);
			fmForm.setPaddingOffset(addToList(fmForm, field, 0)*-1);
		}
		
		return  mapping.findForward("default");
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session=request.getSession();

		FMAdvancedForm fmForm = (FMAdvancedForm)form;
		Long currentTemplateId = FeaturesUtil.getGlobalSettingValueLong("Visibility Template");
		AmpTemplatesVisibility currentTemplate=FeaturesUtil.updateTemplateAndFme(currentTemplateId, fmForm.getFmeList());

		AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
		ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);

		ampContext=this.getServlet().getServletContext();
		ampContext.setAttribute("ampTreeVisibility",ampTreeVisibility);
		
		return null;
	}
	
	
	private int addToList(FMAdvancedForm fmForm, AmpModulesVisibility module, int index){
		int retValue = index;
		
		fmForm.getFmeList().addFirst(fmForm.createFMAdvancedDisplay(module.getId(), module.getName(), FMToolConstants.FEATURE_TYPE_MODULE, 
				isFMEVisible(module), retValue));
		if (module.getParent() != null){
			retValue = addToList(fmForm, (AmpModulesVisibility)module.getParent(),  retValue - paddingShift);
		}
		return retValue;
	}
	
	private int addToList(FMAdvancedForm fmForm, AmpFeaturesVisibility feature, int index){
		int retValue = index;

		fmForm.getFmeList().addFirst(fmForm.createFMAdvancedDisplay(feature.getId(), feature.getName(), FMToolConstants.FEATURE_TYPE_FEATURE, 
				isFMEVisible(feature), retValue));
		if (feature.getParent() != null){
			retValue = addToList(fmForm, (AmpModulesVisibility)feature.getParent(),  retValue - paddingShift);
		}
		return retValue;
	}

	private int addToList(FMAdvancedForm fmForm, AmpFieldsVisibility field, int index){
		int retValue = 0;

		fmForm.getFmeList().addFirst(fmForm.createFMAdvancedDisplay(field.getId(), field.getName(), FMToolConstants.FEATURE_TYPE_FIELD, 
				isFMEVisible(field), retValue));
		if (field.getParent() != null){
			retValue = addToList(fmForm, (AmpFeaturesVisibility)field.getParent(),  retValue - paddingShift);
		}
		return retValue;
	}
	
	private boolean isFMEVisible(AmpObjectVisibility fme){
		boolean retValue = false;
		Long currentTemplateId = FeaturesUtil.getGlobalSettingValueLong("Visibility Template");
		
		for (Iterator iterator = fme.getTemplates().iterator(); iterator.hasNext();) {
			AmpTemplatesVisibility item = (AmpTemplatesVisibility) iterator.next();
			if (item.getId().equals(currentTemplateId)){
				retValue = true;
				break;
			}
		}
		
		return retValue;
	}
}
