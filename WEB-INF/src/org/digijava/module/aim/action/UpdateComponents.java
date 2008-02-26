package org.digijava.module.aim.action;

import java.util.Iterator;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import javax.servlet.http.*;

import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.form.UpdateComponentsForm;
import org.digijava.module.aim.dbentity.AmpComponent;



public class UpdateComponents extends Action{
		private static Logger logger = Logger.getLogger(UpdateComponents.class);
		public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws java.lang.Exception {
	
		HttpSession session = request.getSession();
			if (session.getAttribute("ampAdmin") == null) {
					return mapping.findForward("index");
				} else {
						String str = (String)session.getAttribute("ampAdmin");
							if (str.equals("no")) {
								return mapping.findForward("index");
								}
						}
			//logger.debug("\n\ncame into the update components manager");
			String event = request.getParameter("event");
			String compId = request.getParameter("componentId");
			logger.debug("\n\nthe event got is  "+ event+ "   the id got is "+ compId);
			UpdateComponentsForm updCompForm = (UpdateComponentsForm) form;
			
			if(event !=null){
				if(event.equalsIgnoreCase("add")){
					logger.debug("add");
					updCompForm.setCompCode(null);
					updCompForm.setCompTitle(null);
					updCompForm.setCompDes(null);
					updCompForm.setCompType(null);
				}
				else if(event.equalsIgnoreCase("edit")){
					Iterator itr = ComponentsUtil.getComponentForEditing(new Long(compId)).iterator();
					while(itr.hasNext()){
						AmpComponent ampComp = (AmpComponent) itr.next();
						updCompForm.setId(ampComp.getAmpComponentId());
						updCompForm.setCompTitle(ampComp.getTitle());
						updCompForm.setCompDes(ampComp.getDescription());
						updCompForm.setCompType(ampComp.getType());
						updCompForm.setCompCode(ampComp.getCode());
					}
					updCompForm.setCheck("false");
					return mapping.findForward("editComponent");
				}
				else if (event.equals("saveEditComp")) {
					logger.debug("Updating Component" + compId);
					Long id = new Long(compId);
					AmpComponent ampComp = new AmpComponent();
					ampComp.setAmpComponentId(id);
					ampComp.setTitle(updCompForm.getCompTitle());
					ampComp.setCode(updCompForm.getCompCode());
					ampComp.setType(updCompForm.getCompType());
					if (updCompForm.getCompDes() == null) {
						ampComp.setDescription(" ");
					} else {
						ampComp.setDescription(updCompForm.getCompDes());
					}
					ComponentsUtil.updateComponents(ampComp);
					updCompForm.setCheck("save");
					return mapping.findForward("editComponent");
				}
				else if(event.equalsIgnoreCase("save"))
				{  
					logger.debug("save");
					boolean flag = ComponentsUtil.checkComponentNameExists(updCompForm.getCompTitle());
					boolean flagCode = ComponentsUtil.checkComponentCodeExists(updCompForm.getCompCode());
					if (flag || flagCode) {
						ActionErrors errors = new ActionErrors();
						if(flag)
						errors.add("title", new ActionError("error:aim:componentName:Present"));
						else
							errors.add("code", new ActionError("error:aim:componentCode:Present"));
						saveErrors(request, errors);
						updCompForm.setCheck("true");
						logger.debug("Duplicate Values::::::::::::::;;");
						return mapping.findForward("forward");
					}
					AmpComponent ampComp = new AmpComponent();
					if(compId==null || compId.equalsIgnoreCase("")  || compId.trim().length()==0){
					logger.debug("just born");
					ampComp.setTitle(updCompForm.getCompTitle());
					ampComp.setCode(updCompForm.getCompCode());
					ampComp.setType(updCompForm.getCompType());
					ampComp.setDescription(updCompForm.getCompDes());
					}
					else{
					logger.debug("not new");
					ampComp.setAmpComponentId(new Long(compId));
					ampComp.setTitle(updCompForm.getCompTitle());
					ampComp.setCode(updCompForm.getCompCode());
					ampComp.setType(updCompForm.getCompType());
					ampComp.setDescription(updCompForm.getCompDes());
					}
					DbUtil.add(ampComp);
					updCompForm.setCheck("save");
					return mapping.findForward("forward");
				}
				else if(event.equalsIgnoreCase("delete")){
					Long id = new Long(compId);
					ComponentsUtil.deleteComponent(id);
					return mapping.findForward("delete");
				}
			}	
				updCompForm.setCheck("false");
				return mapping.findForward("forward");
	}
}