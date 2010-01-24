package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.form.ComponentTypeForm;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class UpdateComponentType extends Action {
	private static Logger logger = Logger.getLogger(UpdateComponentType.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}

		ActionErrors errors = (ActionErrors) session.getAttribute("AddComponentTypeError");
		if(errors != null){
			saveErrors(request, errors);
			session.setAttribute("AddComponentTypeError", null);
		}
		
		String event = request.getParameter("event");

		if (event != null) {
			if (event.equalsIgnoreCase("add")) {
				return add(mapping, form, request, response);
			} else if (event.equalsIgnoreCase("edit")) {
				return edit(mapping, form, request, response);
			} else if (event.equalsIgnoreCase("save")) {
				return save(mapping, form, request, response);
			} else if (event.equalsIgnoreCase("delete")) {
				return delete(mapping, form, request, response);
			}

		}
		return list(mapping, form, request, response);
	}
	

	
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		ComponentTypeForm compForm = (ComponentTypeForm) form;
		AmpComponentType existComponent = existComponent(form);

		if (compForm.getId()==0) {
			if(existComponent != null) addErrorsToSession(request);
			else{
				AmpCategoryValue type = new AmpCategoryValue();
				type.setValue(compForm.getName());
				ComponentsUtil.addNewComponentType(type);
			}
		} else {
			AmpCategoryValue type = ComponentsUtil.getComponentTypeById(compForm.getId());
			if(existComponent != null && existComponent.getType_id().equals(compForm.getId())){
				type.setValue(compForm.getName());
				ComponentsUtil.addNewComponentType(type);
			}
			else  addErrorsToSession(request);

		}
		compForm.setCheck("save");
		return mapping.findForward("afterSave");
	}

	public AmpComponentType existComponent(ActionForm form){
		ComponentTypeForm compForm = (ComponentTypeForm) form;
		ArrayList<AmpComponentType> com = compForm.getComponentTypesList();
		for (Iterator it = com.iterator(); it.hasNext();) {
			AmpComponentType ampComponentType = (AmpComponentType) it.next();
			if(ampComponentType.getName().equals(compForm.getName()) || ampComponentType.getCode().equals(compForm.getCode())) return ampComponentType;
			
		}
		return null;
	}
	
	public void addErrorsToSession(HttpServletRequest request) throws java.lang.Exception{
		Long siteId = RequestUtils.getSite(request).getId();
		String locale= RequestUtils.getNavigationLanguage(request).getCode();
		HttpSession session = request.getSession();
		ActionErrors errors = new ActionErrors();
		errors.add("title", new ActionError("error.aim.componentType.componentTypeCodeNameExist", TranslatorWorker.translateText("The component type NAME or CODE you added already exist. Please add other name or code.",locale,siteId)));
		session.setAttribute("AddComponentTypeError",errors);
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		ArrayList<AmpComponentType> com = new ArrayList<AmpComponentType>(ComponentsUtil.getAmpComponentTypes());
		ComponentTypeForm compForm = (ComponentTypeForm) form;
		compForm.setComponentTypesList(com);
		return mapping.findForward("default");
	}

	
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		ComponentTypeForm compForm = (ComponentTypeForm) form;
		Long id=Long.parseLong(request.getParameter("id"));
		
		AmpCategoryValue type=ComponentsUtil.getComponentTypeById(id);
		if (type!=null){
			compForm.setId(type.getId());
			compForm.setName(type.getValue());
		}
		return mapping.findForward("edit");

	}

	public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		return mapping.findForward("add");

	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		
		ComponentsUtil.deleteComponentType(Long.parseLong(request.getParameter("id")));

		return list(mapping, form, request, response);
	}

}