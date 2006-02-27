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
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;

/**
 * @author jose
 */
public class AddFundingDetail extends Action {

	private static Logger logger = Logger.getLogger(AddFundingDetail.class);

	private ArrayList fundingDetails = null;

	private String event;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		EditActivityForm formBean = (EditActivityForm) form;
		formBean.setReset(false);
		Long orgId = formBean.getOrgId();
		event = formBean.getEvent();
		
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		String perspective = "MOFED";
		String currCode = Constants.DEFAULT_CURRENCY;
		if (teamMember.getAppSettings() != null) {
			ApplicationSettings appSettings = teamMember.getAppSettings();
			if (appSettings.getPerspective() != null) {
				perspective = appSettings.getPerspective();
			}
			if (appSettings.getCurrencyId() != null) {
				currCode = DbUtil.getCurrency(appSettings.getCurrencyId()).getCurrencyCode();
			}
		}		
				
		long index = formBean.getTransIndexId();
		logger.info("Index = " + index);
		String subEvent = event.substring(0,3);
		FundingDetail fd = null;
		if (subEvent.equalsIgnoreCase("del") || subEvent.equalsIgnoreCase("add")) {
			if (formBean.getFundingDetails() == null) {
				fundingDetails = new ArrayList();
				fd = getFundingDetail(perspective,currCode);
				fundingDetails.add(fd);		
			} else {
				fundingDetails = new ArrayList(formBean.getFundingDetails());
				logger.info("Sub event : " + subEvent);
				if (subEvent.equals("del")) {
					for (int i = 0;i < fundingDetails.size();i ++) {
						FundingDetail temp = (FundingDetail) fundingDetails.get(i);
						if (temp.getIndexId() == index) {
							if (temp.getTransactionType() == 0) {
								formBean.setNumComm(formBean.getNumComm() - 1);
							} else if (temp.getTransactionType() == 1) {
								formBean.setNumDisb(formBean.getNumDisb() - 1);
							} else if (temp.getTransactionType() == 2) {
								formBean.setNumExp(formBean.getNumExp() - 1);
							}
							break;
						}
					}
					deleteFundingDetails(index);
					logger.info("Deleted");
				} else {
					fd = getFundingDetail(perspective,currCode);
					fundingDetails.add(fd);							
				}
			}
			if (fd != null && fd.getTransactionType() == 0) {
				formBean.setNumComm(formBean.getNumComm() + 1);
				logger.info("Setting num comm to " + formBean.getNumComm());
			} else if (fd != null && fd.getTransactionType() == 1) {
				formBean.setNumDisb(formBean.getNumDisb() + 1);
			} else if (fd != null && fd.getTransactionType() == 2) {
				formBean.setNumExp(formBean.getNumExp() + 1);
			}
			formBean.setFundingDetails(fundingDetails);			
		}
		
		formBean.setEvent(null);
		return mapping.findForward("forward");
	}

	private FundingDetail getFundingDetail(String perspective,String currCode) {
		logger.info("In add funding detail");
		FundingDetail fundingDetail = new FundingDetail();
		if (event.equalsIgnoreCase("addCommitments")) {
			fundingDetail.setTransactionType(Constants.COMMITMENT);
		} else if (event.equalsIgnoreCase("addDisbursements")) {
			fundingDetail.setTransactionType(Constants.DISBURSEMENT);
		} else if (event.equalsIgnoreCase("addExpenditures")) {
			fundingDetail.setTransactionType(Constants.EXPENDITURE);
			fundingDetail.setClassification("");
		}
		fundingDetail.setCurrencyCode(currCode);
		fundingDetail.setAdjustmentType(Constants.ACTUAL);
		fundingDetail.setIndex(fundingDetails.size());
		fundingDetail.setIndexId(System.currentTimeMillis());
		/*
		if (perspective.equalsIgnoreCase("MOFED")) {
			fundingDetail.setPerspectiveCode(Constants.MOFED);	
		} else {
			fundingDetail.setPerspectiveCode(Constants.DONOR);
		}
		*/
		fundingDetail.setPerspectiveCode(Constants.MOFED);
		
		return fundingDetail;
		
	}

	private void deleteFundingDetails(long indexId) {
		logger.info("in deleteFundingDetails ");
		fundingDetails.remove(new FundingDetail(indexId));
	}
}