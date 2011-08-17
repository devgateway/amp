package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.gateperm.core.GatePermConst;

/**
 * @author jose
 */
public class AddFundingDetail extends Action {

	private static Logger logger = Logger.getLogger(AddFundingDetail.class);

	private ArrayList<FundingDetail> fundingDetails = null;

	private String event;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		EditActivityForm formBean = (EditActivityForm) form;
		formBean.setReset(false);
		event = formBean.getFunding().getEvent();
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.EDIT);

		String currCode = Constants.DEFAULT_CURRENCY;
		if (teamMember.getAppSettings() != null) {
			ApplicationSettings appSettings = teamMember.getAppSettings();
			if (appSettings.getCurrencyId() != null) {
				currCode = CurrencyUtil.getCurrency(appSettings.getCurrencyId()).getCurrencyCode();
			}
		}

		long index = formBean.getFunding().getTransIndexId();
                if(event!=null){
		String subEvent = event.substring(0,3);
		FundingDetail fd = null;
		if (subEvent.equalsIgnoreCase("del") || subEvent.equalsIgnoreCase("add")) {
			if (formBean.getFunding().getFundingDetails() == null) {
				fundingDetails = new ArrayList<FundingDetail>();
				fd = getFundingDetail(currCode);
				fundingDetails.add(fd);
			} else {
				fundingDetails = new ArrayList<FundingDetail>(formBean.getFunding().getFundingDetails());
				if (subEvent.equals("del")) {
					FundingDetail temp = new FundingDetail();
					temp.setIndexId(index);
                                        temp=(FundingDetail)fundingDetails.get(fundingDetails.indexOf(temp));
                                        Iterator <FundingDetail> iter=fundingDetails.iterator();
                                        while(iter.hasNext()){
                                              FundingDetail det=iter.next();
                                              if (det.getTransactionType()==Constants.DISBURSEMENT&&
                                                  det.getDisbOrderId() != null &&
                                                  det.getDisbOrderId().equals(temp.getDisbOrderId())) {
                                                      det.setDisbOrderId(null);
                                              }
                                        }
					fundingDetails.remove(temp);
				} else {
					fd = getFundingDetail(currCode);
					fundingDetails.add(fd);
				}
			}
			if (fd != null && fd.getTransactionType() == 0) {
				formBean.getFunding().setNumComm(formBean.getFunding().getNumComm() + 1);
			} else if (fd != null && fd.getTransactionType() == 1) {
				formBean.getFunding().setNumDisb(formBean.getFunding().getNumDisb() + 1);
			} else if (fd != null && fd.getTransactionType() == 2) {
				formBean.getFunding().setNumExp(formBean.getFunding().getNumExp() + 1);
			}
                        else if (fd != null && fd.getTransactionType() == 4) {
                                int numDisbOrder=formBean.getFunding().getNumDisbOrder() + 1;
                                formBean.getFunding().setNumDisbOrder(numDisbOrder);
                                Iterator<FundingDetail> iter=fundingDetails.iterator();
                                long max=100;
                                while(iter.hasNext()){
                                       FundingDetail det=iter.next();
                                       if(det.getDisbOrderId()!=null&&!det.getDisbOrderId().equals("")){
                                                 int id = Integer.parseInt(det.getDisbOrderId());
                                                 if (max < id) {
                                                   max = id;
                                                 }

                                       }


                                }
                                fd.setDisbOrderId(""+(++max));
                        }
			formBean.getFunding().setFundingDetails(fundingDetails);
		}
                }
		formBean.getFunding().setEvent(null);
		formBean.getFunding().setDupFunding(true);
		formBean.getFunding().setFirstSubmit(false);
		return mapping.findForward("forward");
	}

	private FundingDetail getFundingDetail(String currCode) {
		FundingDetail fundingDetail = new FundingDetail();

		if (event.equalsIgnoreCase("addCommitments")) {
			fundingDetail.setTransactionType(Constants.COMMITMENT);
		} else if (event.equalsIgnoreCase("addDisbursements")) {
			fundingDetail.setTransactionType(Constants.DISBURSEMENT);
		} else if (event.equalsIgnoreCase("addExpenditures")) {
			fundingDetail.setTransactionType(Constants.EXPENDITURE);
			fundingDetail.setClassification("");
		}
                else if (event.equalsIgnoreCase("addDisbursementOrders")) {
                       fundingDetail.setTransactionType(Constants.DISBURSEMENT_ORDER);
                       fundingDetail.setClassification("");

               }
		fundingDetail.setCurrencyCode(currCode);
		fundingDetail.setAdjustmentType(Constants.ACTUAL);
        fundingDetail.setIndexId(System.currentTimeMillis());
		fundingDetail.setIndex(fundingDetails.size());
		fundingDetail.setReportingDate(new Date(System.currentTimeMillis()));
		return fundingDetail;
	}
}
