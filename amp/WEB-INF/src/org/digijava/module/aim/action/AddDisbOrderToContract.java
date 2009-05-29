/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.digijava.module.aim.action;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.FundingDetail;

/**
 *
 * 
 */
public class AddDisbOrderToContract  extends Action {
    private static Logger logger = Logger.getLogger(AddDisbOrderToContract.class);

        public ActionForward execute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws java.lang.Exception {
                        EditActivityForm eaForm = (EditActivityForm) form;
                        String event=eaForm.getFunding().getEvent();
                        if(event.equals("Add")){
                                long indexId = eaForm.getFunding().getTransIndexId();
                                Integer conId=eaForm.getContracts().getSelContractId();
                                FundingDetail fd = new FundingDetail();
                                fd.setIndexId(indexId);
                                int index = eaForm.getFunding().getFundingDetails().indexOf(
                                    fd);
                                FundingDetail disbOrder = eaForm.getFundingDetail(
                                    index);
                                List<IPAContract> contracts=eaForm.getContracts().getContracts();
                                disbOrder.setContract(contracts.get(conId-1));
                                eaForm.getFunding().setEvent(null);
                                request.setAttribute("close", "close");
                        }
                           
                        return mapping.findForward("forward");

          }

}
