package org.digijava.module.aim.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.form.VisibilityManagerForm;
import org.digijava.module.aim.util.FeaturesUtil;
 
public class VisibilityManager extends MultiAction {
	
	private static Logger logger = Logger.getLogger(VisibilityManager.class);
	
	private ServletContext ampContext = null;
	
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Collection templates=FeaturesUtil.getAMPTemplatesVisibility();
		VisibilityManagerForm vForm=(VisibilityManagerForm) form;
		vForm.setTemplates(templates);
		return  modeSelect(mapping, form, request, response);
	}

	public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		//return modeNew(mapping, form, request, response);
		if(request.getParameter("action")!=null)
			{
				if(request.getParameter("action").compareTo("add")==0) return modeAddTemplate(mapping, form, request, response);
				if(request.getParameter("action").compareTo("viewFields")==0) return modeViewFields(mapping, form, request, response);
				if(request.getParameter("action").compareTo("edit")==0) return modeEditTemplate(mapping, form, request, response);
				if(request.getParameter("action").compareTo("delete")==0) return modeDeleteTemplate(mapping, form, request, response);				
				if(request.getParameter("action").compareTo("deleteFFM")==0) return modeDeleteFFM(mapping, form, request, response);
			}
		if(request.getParameter("newTemplate")!=null) return modeSaveTemplate(mapping, form, request, response);
		if(request.getParameter("saveEditTemplate")!=null) return modeSaveEditTemplate(mapping, form, request, response);
		if(request.getParameter("saveTreeVisibility")!=null) return modeSaveTreeVisibility(mapping, form, request, response);
		return mapping.findForward("forward");
	}
	
	
	public ActionForward modeAddTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		VisibilityManagerForm vForm=(VisibilityManagerForm) form;
		Collection modules=FeaturesUtil.getAMPModulesVisibility();
		vForm.setMode("addNew");
		vForm.setModules(modules);
		return mapping.findForward("forward");
	}
	
	public ActionForward modeViewFields(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		VisibilityManagerForm vForm=(VisibilityManagerForm) form;
		Collection modules=FeaturesUtil.getAMPModulesVisibility();
		Collection features=FeaturesUtil.getAMPFeaturesVisibility();
		Collection fields=FeaturesUtil.getAMPFieldsVisibility();
		vForm.setMode("viewFields");
		vForm.setAllModules(modules);
		vForm.setAllFeatures(features);
		vForm.setAllFields(fields);
		return mapping.findForward("forward");
	}
	
	public ActionForward modeSaveTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Session hbsession=this.createSession();
		if(FeaturesUtil.existTemplateVisibility(request.getParameter("templateName")))
		{
			ActionErrors errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"error.aim.templateExistent"));
			
			saveErrors(request, errors);
		}
		else
		{
			System.out.println(request.getParameter("templateName"));
			FeaturesUtil.insertTemplate(request.getParameter("templateName"), hbsession);
		}
		//for refreshing the page...
		VisibilityManagerForm vForm=(VisibilityManagerForm) form;
		Collection templates=FeaturesUtil.getAMPTemplatesVisibility();
		vForm.setTemplates(templates);
		try {
			PersistenceManager.releaseSession(hbsession);
		} catch (Exception rsf) {
			logger.error("Release session failed :1" + rsf.getMessage());
		}
		return mapping.findForward("forward");
	}
	
	public ActionForward modeEditTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		VisibilityManagerForm vForm = (VisibilityManagerForm) form;
		Long templateId=null;
		Session hbsession = this.createSession();
		HttpSession session=request.getSession();
		if(request.getParameter("templateId")!=null)
			templateId=new Long(Long.parseLong(request.getParameter("templateId")));
		if(templateId==null) templateId=(Long)request.getAttribute("templateId");
		
		String templateName=FeaturesUtil.getTemplateNameVisibility(templateId);
		session.setAttribute("templateName", templateName);
		session.setAttribute("templateId",templateId);
		AmpTemplatesVisibility templateVisibility = FeaturesUtil.getTemplateVisibility(templateId, hbsession);
		AmpTreeVisibility ampTreeVisibility=new AmpTreeVisibility();
		ampTreeVisibility.buildAmpTreeVisibility(templateVisibility);
		vForm.setAmpTreeVisibility(ampTreeVisibility);
		
		
		AmpTreeVisibility x=vForm.getAmpTreeVisibility();
		for(Iterator i=x.getItems().values().iterator();i.hasNext();)
		{
			AmpTreeVisibility m=(AmpTreeVisibility) i.next();
			AmpModulesVisibility aop=(AmpModulesVisibility) m.getRoot();
			//System.out.println("	"+m.getRoot().getName()+m.getRoot()+aop.isVisibleId((Long)session.getAttribute("templateId")));
			for(Iterator j=m.getItems().values().iterator();j.hasNext();)
			{
				AmpTreeVisibility n=(AmpTreeVisibility) j.next();
				//System.out.println("		"+n.getRoot().getName());
				for(Iterator k=n.getItems().values().iterator();k.hasNext();)
				{
					AmpTreeVisibility p=(AmpTreeVisibility) k.next();
					//System.out.println("xxx			"+p.getRoot().getName());
				}

			}
		}
		
		vForm.setMode("editTemplateTree");

		{//for refreshing the page
			Collection templates=FeaturesUtil.getAMPTemplatesVisibility();
			vForm.setTemplates(templates);
		}
		try {
			PersistenceManager.releaseSession(hbsession);
		} catch (Exception rsf) {
			logger.error("Release session failed :2" + rsf.getMessage());
		}
		return mapping.findForward("forward");
	}
	
	public ActionForward modeSaveEditTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		VisibilityManagerForm vForm = (VisibilityManagerForm) form;
		Long templateId;
		HttpSession session=request.getSession();
		templateId=vForm.getTemplateId();
		String templateName=FeaturesUtil.getTemplateNameVisibility(templateId);
		session.setAttribute("templateName", templateName);
		Collection allAmpModules=FeaturesUtil.getAMPModules();
		Collection newTemplateModulesList=new ArrayList();
		for(Iterator it=allAmpModules.iterator();it.hasNext();)
		{
			
			AmpModulesVisibility ampModule=(AmpModulesVisibility)it.next();
			String existentModule=ampModule.getName().replaceAll(" ","");
			if(request.getParameter("moduleVis:"+existentModule)!=null)
				if(request.getParameter("moduleVis:"+existentModule).compareTo("enable")==0)
					{
						newTemplateModulesList.add(ampModule);
					}
		}
		FeaturesUtil.updateModulesTemplate(newTemplateModulesList, templateId, vForm.getTemplateName());
		{//for refreshing the page
			Collection templates=FeaturesUtil.getAMPTemplatesVisibility();
			vForm.setTemplates(templates);
		}
		
		ActionErrors errors = new ActionErrors();
	 	errors.add("title", new ActionError("error.aim.visibility.updatedTemplate"));
	 	saveErrors(request, errors);

		
		return mapping.findForward("forward");
	}
	
	public ActionForward modeDeleteTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(Long.parseLong(request.getParameter("templateId")));
		Session hbsession = null;
		hbsession = this.createSession();
		FeaturesUtil.deleteTemplateVisibility(new Long(Long.parseLong(request.getParameter("templateId"))),hbsession);
		{//for refreshing the page
			VisibilityManagerForm vForm = (VisibilityManagerForm) form;
			Collection templates=FeaturesUtil.getAMPTemplatesVisibility();
			vForm.setTemplates(templates);
		}
		ActionErrors errors = new ActionErrors();
	 	errors.add("title", new ActionError("error.aim.visibility.deletedTemplate"));
	 	saveErrors(request, errors);
		return mapping.findForward("forward");
	}
	
	public ActionForward modeDeleteFFM(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println(Long.parseLong(request.getParameter("fieldId")));
		//System.out.println(Long.parseLong(request.getParameter("featureId")));
		Session hbsession = null;
		hbsession = this.createSession();
		if(request.getParameter("fieldId")!=null) FeaturesUtil.deleteFieldVisibility(new Long(Long.parseLong(request.getParameter("fieldId"))),hbsession);//delete field
		if(request.getParameter("featureId")!=null) FeaturesUtil.deleteFeatureVisibility(new Long(Long.parseLong(request.getParameter("featureId"))),hbsession);//delete feature
		if(request.getParameter("moduleId")!=null) FeaturesUtil.deleteModuleVisibility(new Long(Long.parseLong(request.getParameter("moduleId"))),hbsession);//delete module
		hbsession.close();
		return modeViewFields(mapping, form, request, response);
	}
	
	public ActionForward modeSaveTreeVisibility(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		AmpTreeVisibility ampTreeVisibility=new AmpTreeVisibility();
		Session hbsession = null;

		hbsession = this.createSession();
		HttpSession session=request.getSession();
		Long templateId=(Long)session.getAttribute("templateId");
		AmpTemplatesVisibility templateVisibility = FeaturesUtil.getTemplateVisibility(templateId, hbsession);
		ampTreeVisibility.buildAmpTreeVisibility(templateVisibility);
		TreeSet modules=new TreeSet();
		TreeSet features=new TreeSet();
		TreeSet fields=new TreeSet();
		
		AmpTreeVisibility x=ampTreeVisibility;
		for(Iterator i=x.getItems().values().iterator();i.hasNext();)
		{
			AmpTreeVisibility m=(AmpTreeVisibility) i.next();
			if(request.getParameter("moduleVis:"+m.getRoot().getId())!=null)
			{
				modules.add(m.getRoot());
				for(Iterator j=m.getItems().values().iterator();j.hasNext();)
				{
					AmpTreeVisibility n=(AmpTreeVisibility) j.next();
					if(request.getParameter("featureVis:"+n.getRoot().getId())!=null)
					{
						features.add(n.getRoot());
						for(Iterator k=n.getItems().values().iterator();k.hasNext();)
						{
							AmpTreeVisibility p=(AmpTreeVisibility) k.next();
							if(request.getParameter("fieldVis:"+p.getRoot().getId())!=null)
							{
								fields.add(p.getRoot()); 
							}
						}
					}
				}
			}
		}
		//FeaturesUtil.updateAmpTreeVisibility(modules, features, fields, templateId);

		//request.getParameter("templateName")
		FeaturesUtil.updateAmpTemplateNameTreeVisibility(request.getParameter("templateName"), templateId, hbsession);
		FeaturesUtil.updateAmpModulesTreeVisibility(modules, templateId, hbsession);
		FeaturesUtil.updateAmpFeaturesTreeVisibility(features, templateId, hbsession);
		FeaturesUtil.updateAmpFieldsTreeVisibility(fields, templateId, hbsession);
		request.setAttribute("templateId", templateId);
		ActionErrors errors = new ActionErrors();
	 	errors.add("title", new ActionError("error.aim.visibility.visibilityTreeUpdated"));
	 	saveErrors(request, errors);

    	//AmpTreeVisibility ampTreeVisibility=new AmpTreeVisibility();
    	//get the default amp template!!!
    	//Session session=this.createSession();
    	AmpTemplatesVisibility currentTemplate=FeaturesUtil.getTemplateVisibility(FeaturesUtil.getGlobalSettingValueLong("Visibility Template"),hbsession);
    	ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
    	ampContext=session.getServletContext();
    	ampContext.setAttribute("ampTreeVisibility",ampTreeVisibility);
    	try {
			PersistenceManager.releaseSession(hbsession);
		} catch (Exception rsf) {
			logger.error("Release session failed :4" + rsf.getMessage());
		}
		return modeEditTemplate(mapping,form,request,response);
	}
	

}