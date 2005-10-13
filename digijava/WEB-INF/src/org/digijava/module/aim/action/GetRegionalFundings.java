/*
 * GetRegionalFundings.java
 * Created : 06-Oct-2005
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.form.RegionalFundingForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;

/**
 * Action class for getting the regional funding details of an activity
 * 
 * @author Priyajith
 */
public class GetRegionalFundings extends TilesAction {
	
	private static Logger logger = Logger.getLogger(GetRegionalFundings.class);
	
	public ActionForward execute(ComponentContext context,
			ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		HttpSession sesion = request.getSession();
		TeamMember tm = (TeamMember) sesion.getAttribute("currentMember");
		
		RegionalFundingForm rfForm = (RegionalFundingForm) form;
		logger.debug("In GetRegionalFundings");
		logger.debug("Region id " + rfForm.getRegionId());
		if (rfForm.getAmpActivityId() > 0) {
			Collection regFunds = ActivityUtil.getRegionalFundings(new Long(rfForm.getAmpActivityId()));
			logger.debug("Num Reg.Fundings = " + regFunds.size());
			Iterator itr = regFunds.iterator();
			
			ArrayList temp = new ArrayList();
			while (itr.hasNext()) {
				AmpRegionalFunding regFund = (AmpRegionalFunding) itr.next();
				RegionalFunding rf = new RegionalFunding();
				rf.setRegionId(regFund.getRegion().getAmpRegionId());
				rf.setRegionName(regFund.getRegion().getName());
				int index = -1;
				if (temp.contains(rf) == true) {
					index = temp.indexOf(rf);
					rf = (RegionalFunding) temp.get(index);
				}
				FundingDetail fd = new FundingDetail();
				fd.setCurrencyCode(regFund.getCurrency().getCurrencyCode());
				fd.setCurrencyName(regFund.getCurrency().getCurrencyName());
				fd.setPerspectiveCode(regFund.getPerspective().getCode());
				fd.setPerspectiveName(regFund.getPerspective().getName());
				fd.setTransactionAmount(DecimalToText.getString(regFund.getTransactionAmount().doubleValue()));
				fd.setTransactionDate(DateConversion.ConvertDateToString(regFund.getTransactionDate()));
				fd.setTransactionType(regFund.getTransactionType().intValue());

				double amt = 0;
				fd.setAdjustmentType(regFund.getAdjustmentType().intValue());
				if (fd.getAdjustmentType() == Constants.PLANNED) {
					fd.setAdjustmentTypeName("Planned");
				} else if (fd.getAdjustmentType() == Constants.ACTUAL) {
					fd.setAdjustmentTypeName("Actual");
					Date dt = regFund.getTransactionDate();
					double frmExRt = DbUtil.getExchangeRate(fd.getCurrencyCode(),1,dt);
					double toExRt = DbUtil.getExchangeRate(DbUtil.getAmpcurrency(
							tm.getAppSettings().getCurrencyId()).getCurrencyCode(),1,dt);
					amt = CurrencyWorker.convert1(regFund.getTransactionAmount().doubleValue(),frmExRt,toExRt);
					
				}
				
				if (fd.getTransactionType() == Constants.COMMITMENT) {
					if (rf.getCommitments() == null) {
						rf.setCommitments(new ArrayList());
					}
					rf.getCommitments().add(fd);
					if (fd.getAdjustmentType() == Constants.ACTUAL) {
						amt += rf.getTotCommitments();
						rf.setTotCommitments(amt);						
					}
				} else if (fd.getTransactionType() == Constants.DISBURSEMENT) {
					if (rf.getDisbursements() == null) {
						rf.setDisbursements(new ArrayList());
					}
					rf.getDisbursements().add(fd);
					if (fd.getAdjustmentType() == Constants.ACTUAL) {
						amt += rf.getTotDisbursements();
						rf.setTotDisbursements(amt);						
					}					
				} else if (fd.getTransactionType() == Constants.EXPENDITURE) {
					if (rf.getExpenditures() == null) {
						rf.setExpenditures(new ArrayList());
					}
					rf.getExpenditures().add(fd);
					if (fd.getAdjustmentType() == Constants.ACTUAL) {
						amt += rf.getTotExpenditures();
						rf.setTotExpenditures(amt);						
					}					
				}
				if (index > -1) {
					temp.set(index,rf);
				} else {
					temp.add(rf);
				}
			}
			
			double totComm = 0;
			double totDisb = 0;
			double totUnDisb = 0;
			double totExp = 0;
			double totUnExp = 0;
			
			logger.debug("Temp size = " + temp.size());
			for (int i = 0;i < temp.size();i ++) {
				RegionalFunding regFund = (RegionalFunding) temp.get(i);
				regFund.setTotUnDisbursed(regFund.getTotCommitments() - regFund.getTotDisbursements());
				regFund.setTotUnExpended(regFund.getTotDisbursements() - regFund.getTotExpenditures());
				temp.set(i,regFund);
				totComm += regFund.getTotCommitments();
				totDisb += regFund.getTotDisbursements();
				totUnDisb += regFund.getTotUnDisbursed();
				totExp += regFund.getTotExpenditures();
				totUnExp += regFund.getTotUnExpended();
			}
			
			rfForm.setTotCommitments(totComm);
			rfForm.setTotDisbursements(totDisb);
			rfForm.setTotExpenditures(totExp);
			rfForm.setTotUnDisbursed(totUnDisb);
			rfForm.setTotUnExpended(totUnExp);
			rfForm.setRegionalFundings(temp);
			rfForm.setPerspective(tm.getAppSettings().getPerspective());
		}
		return null;
	}
}

