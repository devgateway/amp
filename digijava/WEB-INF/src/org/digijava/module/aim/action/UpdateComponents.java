package org.digijava.module.aim.action;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import java.util.*;

import javax.servlet.http.*;


import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.form.UpdateComponentsForm;
import org.digijava.module.aim.dbentity.AmpComponent;


public class UpdateComponents extends Action{
		private static Logger logger = Logger.getLogger(GetSectorSchemes.class);
	
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
							logger.info("came into the update components manager");
							String event = request.getParameter("event");
							String compId = request.getParameter("componentId");
							logger.info("the event got is  "+ event+ "   the id got is "+ compId);
							UpdateComponentsForm updCompForm = (UpdateComponentsForm) form;
							
							if(event.equals("edit"))
							{
								Long id = new Long(compId);
								logger.info("the id got is "+id);
								Collection col  = ComponentsUtil.getComponentForEditing(id);
								
								Iterator itr = col.iterator();
								while(itr.hasNext())
								{
									AmpComponent ampComp = (AmpComponent) itr.next();
									updCompForm.setId(ampComp.getAmpComponentId());
									updCompForm.setCompTitle(ampComp.getTitle());
									updCompForm.setCompDes(ampComp.getDescription());
									updCompForm.setCompType(ampComp.getType());
									updCompForm.setCompCode(ampComp.getCode());
									logger.info(" ampTitle "+ampComp.getTitle());
									logger.info(" amp Descr "+ ampComp.getDescription());
								}
								
								return mapping.findForward("editComponent");
							}
							if(event.equals("delete"))
							{
								Long id = new Long(compId);
								ComponentsUtil.deleteComponent(id);
								return mapping.findForward("comps");	
							}
							if(event.equals("add"))
							{
								updCompForm.setCompCode("");
								updCompForm.setCompDes("");
								updCompForm.setCompTitle("");
								updCompForm.setCompType("");
								return mapping.findForward("addComponent");
							}
							if(event.equals("saveEditComp"))
							{
								Long id = new Long(compId);
								AmpComponent ampComp = new AmpComponent();
								ampComp.setAmpComponentId(id);
								ampComp.setTitle(updCompForm.getCompTitle());
								ampComp.setCode(updCompForm.getCompCode());
								ampComp.setType(updCompForm.getCompType());
								ampComp.setDescription(updCompForm.getCompDes());
								ComponentsUtil.updateComponents(ampComp);
								return mapping.findForward("comps");
							}
							if(event.equals("newComp"))
							{
								
								AmpComponent ampComp = new AmpComponent();
								ampComp.setAmpComponentId(null);
								ampComp.setActivities(null);
								/*ampComp.setActivity(null);
								ampComp.setComponentFundings(null);
								ampComp.setCurrency(null);
								ampComp.setReportingDate(null);
								ampComp.setPhysicalProgress(null);*/
								ampComp.setTitle(updCompForm.getCompTitle());
								ampComp.setCode(updCompForm.getCompCode());
								ampComp.setType(updCompForm.getCompType());
								ampComp.setDescription(updCompForm.getCompDes());
								DbUtil.add(ampComp);
								
								return mapping.findForward("comps");
								
								
							}
							
							
		return mapping.findForward("default");
	  }
	
}