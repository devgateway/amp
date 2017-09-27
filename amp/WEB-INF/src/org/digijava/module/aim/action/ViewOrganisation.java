/*
 * Created on 12/04/2005
 * @author akashs
 * 
 */
package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ViewOrgForm;
import org.digijava.module.aim.util.DbUtil;

public class ViewOrganisation extends Action {

              private static Logger logger = Logger.getLogger(ViewOrganisation.class);

              public ActionForward execute(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws java.lang.Exception {

                         logger.debug("In view organisation action");

                         ViewOrgForm editForm = (ViewOrgForm) form;
                         
                         Collection ampOrg = null;
                         ampOrg = DbUtil.getOrganisationAsCollection(new Long(Integer.parseInt(request.getParameter("ampOrgId"))));
                         
                         if (ampOrg == null) {
                            logger.info("ampOrg is null");
                            return mapping.findForward("index");
                         }
                         else {
                            logger.info("ampOrg is not null");
                            editForm.setOrg(ampOrg);
                            return mapping.findForward("forward");
                         }
    }
}
