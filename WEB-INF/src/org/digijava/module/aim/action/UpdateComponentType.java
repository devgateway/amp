package org.digijava.module.aim.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.form.ComponentTypeForm;
import org.digijava.module.aim.util.ComponentsUtil;

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

		if (compForm.getId()==0) {
			AmpComponentType type = new AmpComponentType();
			type.setName(compForm.getName());
			type.setCode(compForm.getCode());
			type.setEnable(compForm.getEnable());
			type.setSelectable(compForm.getSelectable());
			ComponentsUtil.addNewComponentType(type);
		} else {
			AmpComponentType type = ComponentsUtil.getComponentTypeById(compForm.getId());
			type.setName(compForm.getName());
			type.setCode(compForm.getCode());
			type.setEnable(compForm.getEnable());
			type.setSelectable(compForm.getSelectable());
			ComponentsUtil.addNewComponentType(type);

		}
		compForm.setCheck("save");
		return mapping.findForward("afterSave");
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
		
		AmpComponentType type=ComponentsUtil.getComponentTypeById(id);
		if (type!=null){
			compForm.setId(type.getType_id());
			compForm.setName(type.getName());
			compForm.setCode(type.getCode());
			compForm.setEnable(type.getEnable());
			compForm.setSelectable(type.getSelectable());
			
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