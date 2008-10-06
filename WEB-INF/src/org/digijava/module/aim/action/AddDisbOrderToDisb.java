package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.log4j.Logger;
import org.digijava.module.aim.form.EditActivityForm;
import java.util.List;
import org.digijava.module.aim.helper.FundingDetail;

public class AddDisbOrderToDisb  extends Action {
        private static Logger logger = Logger.getLogger(AddDisbOrderToDisb.class);

        public ActionForward execute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws java.lang.Exception {
                        EditActivityForm eaForm = (EditActivityForm) form;
                        String event=eaForm.getFunding().getEvent();
                        if(event.equals("Add")){
                                long indexId = eaForm.getTransIndexId();
                                FundingDetail fd = new FundingDetail();
                                fd.setIndexId(indexId);
                                int index = eaForm.getFunding().getFundingDetails().indexOf(
                                    fd);
                                FundingDetail disb = eaForm.getFundingDetail(
                                    index);
                                disb.setDisbOrderId(eaForm.getFunding().getDisbOrderId());
                                eaForm.getFunding().setEvent(null);
                                request.setAttribute("close", "close");
                        }
                        return mapping.findForward("forward");

          }

}
