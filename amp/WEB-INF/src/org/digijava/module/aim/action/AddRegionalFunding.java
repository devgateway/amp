/*
 * AddRegionalFunding.java
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import sun.awt.geom.AreaOp.CAGOp;

public class AddRegionalFunding extends Action {
	
	private static Logger logger = Logger.getLogger(AddRegionalFunding.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
	
		String action = request.getParameter("regFundAct");
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		
		logger.debug("Action is " + action);
		
		try {
		EditActivityForm eaForm = (EditActivityForm) form;	
		eaForm.setStep("4");
		if (action != null && action.equalsIgnoreCase("show")) {
			logger.debug("Forarding to forward");
			eaForm.getFunding().setFundingRegionId(new Long(-1));
			String defCurr = CurrencyUtil.getCurrency(
					tm.getAppSettings().getCurrencyId()).getCurrencyCode();
			request.setAttribute("defCurrency",defCurr);
            if (eaForm.getFunding().getFundingCurrCode()== null) {
                eaForm.getFunding().setFundingCurrCode(defCurr);
            }
            setFundingTotals(eaForm, session);
			
			return mapping.findForward("forward");
		} else if (action != null && action.equalsIgnoreCase("showEdit")) {
			Iterator itr = eaForm.getFunding().getRegionalFundings().iterator();
			String id = request.getParameter("fundId");
			long fId = Long.parseLong(id); 
			while (itr.hasNext()) {
				RegionalFunding rd = (RegionalFunding) itr.next();
				if (rd.getRegionId().longValue() == fId) {
					eaForm.getFunding().setFundingRegionId(rd.getRegionId());
					break;
				}
			}
			String defCurr = CurrencyUtil.getCurrency(
					tm.getAppSettings().getCurrencyId()).getCurrencyCode();
			request.setAttribute("defCurrency",defCurr);
            setFundingTotals(eaForm, session);
			return mapping.findForward("forward");
		} else if (action != null && action.equalsIgnoreCase("update")){

			RegionalFunding regFund = new RegionalFunding();			
			Iterator<AmpCategoryValueLocations> itr = eaForm.getFunding().getFundingRegions().iterator();
			while (itr.hasNext()) {
				AmpCategoryValueLocations ampRegion	= itr.next();
				if (ampRegion.getId().equals(eaForm.getFunding().getFundingRegionId())) {
					regFund.setRegionName( ampRegion.getName() );
					break;
				}
			}
			regFund.setRegionId(eaForm.getFunding().getFundingRegionId());
			
			Enumeration paramNames = request.getParameterNames();
			String param = "";
			String val = "";
			
			Map comm = new HashMap();
			Map disb = new HashMap();
			Map exp = new HashMap();
			
			while (paramNames.hasMoreElements()) {
				param = (String) paramNames.nextElement();
				if (param.startsWith("comm_")) {
					val = request.getParameter(param);
					StringTokenizer st = new StringTokenizer(param,"_");
					st.nextToken();
					int index = Integer.parseInt(st.nextToken());
					int num = Integer.parseInt(st.nextToken());
					
					if (comm.containsKey(new Integer(index)) == false) {
						comm.put(new Integer(index),new FundingDetail());	
					}

					FundingDetail fd = (FundingDetail) comm.get(new Integer(index));
					
					
					if (fd != null) {
						switch (num) {
						case 1:
							//fd.setAdjustmentType(Integer.parseInt(val));
							fd.setAdjustmentTypeName(CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.ADJUSTMENT_TYPE_KEY, Long.parseLong(val)) );
							break;
						case 2:
							fd.setTransactionAmount(CurrencyWorker.formatAmount(val));
							break;
						case 3:
							fd.setCurrencyCode(val);
							break;
						case 4:
							fd.setTransactionDate(val);
							break;
						}
						comm.put(new Integer(index),fd);
					}
				} else if (param.startsWith("disb_")) {
					val = request.getParameter(param);
					StringTokenizer st = new StringTokenizer(param,"_");
					st.nextToken();
					int index = Integer.parseInt(st.nextToken());
					int num = Integer.parseInt(st.nextToken());
					
					if (disb.containsKey(new Integer(index)) == false) {
						disb.put(new Integer(index),new FundingDetail());	
					}

					FundingDetail fd = (FundingDetail) disb.get(new Integer(index));
					
					
					if (fd != null) {
						switch (num) {
						case 1:
							//fd.setAdjustmentType(Integer.parseInt(val));
							fd.setAdjustmentTypeName(CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.ADJUSTMENT_TYPE_KEY, Long.parseLong(val)) );
							logger.debug("Adjustment type = " + fd.getAdjustmentTypeName().getValue());
							break;
						case 2:
							fd.setTransactionAmount(CurrencyWorker.formatAmount(val));
							break;
						case 3:
							fd.setCurrencyCode(val);
							break;
						case 4:
							fd.setTransactionDate(val);
							break;
						}
						disb.put(new Integer(index),fd);					
					}
				} else if (param.startsWith("expn_")) {
					val = request.getParameter(param);
					StringTokenizer st = new StringTokenizer(param,"_");
					st.nextToken();
					int index = Integer.parseInt(st.nextToken());
					int num = Integer.parseInt(st.nextToken());
					
					if (exp.containsKey(new Integer(index)) == false) {
						exp.put(new Integer(index),new FundingDetail());	
					}

					FundingDetail fd = (FundingDetail) exp.get(new Integer(index));
					
					
					if (fd != null) {
						switch (num) {
						case 1:
							//fd.setAdjustmentType(Integer.parseInt(val));
							fd.setAdjustmentTypeName(CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.ADJUSTMENT_TYPE_KEY, Long.parseLong(val)) );
							logger.debug("Adjustment type = " + fd.getAdjustmentTypeName().getValue());
							break;
						case 2:
							fd.setTransactionAmount(CurrencyWorker.formatAmount(val));
							break;
						case 3:
							fd.setCurrencyCode(val);
							break;
						case 4:
							fd.setTransactionDate(val);
							break;
						}
						exp.put(new Integer(index),fd);					
					}					
				}
					
			}
			
			Iterator itrS = comm.keySet().iterator();
			while (itrS.hasNext()) {
				Integer index = (Integer) itrS.next();
				FundingDetail fd = (FundingDetail) comm.get(index);
				if (regFund.getCommitments() == null) {
					regFund.setCommitments(new ArrayList());
				}
				regFund.getCommitments().add(fd);
			}
			
			itrS = disb.keySet().iterator();
			while (itrS.hasNext()) {
				Integer index = (Integer) itrS.next();
				FundingDetail fd = (FundingDetail) disb.get(index);
				if (regFund.getDisbursements() == null) {
					regFund.setDisbursements(new ArrayList());
				}
				regFund.getDisbursements().add(fd);
			}
			
			itrS = exp.keySet().iterator();
			while (itrS.hasNext()) {
				Integer index = (Integer) itrS.next();
				FundingDetail fd = (FundingDetail) exp.get(index);
				if (regFund.getExpenditures() == null) {
					regFund.setExpenditures(new ArrayList());
				}
				regFund.getExpenditures().add(fd);
			}			
			
			if (eaForm.getFunding().getRegionalFundings() == null) {
				eaForm.getFunding().setRegionalFundings(new ArrayList());
			}
			if (eaForm.getFunding().getRegionalFundings().contains(regFund)) {
				eaForm.getFunding().getRegionalFundings().remove(regFund);
			}
			
			List list = null;
			if (regFund.getCommitments() != null) {
				list = new ArrayList(regFund.getCommitments());
				Collections.sort(list,FundingValidator.dateComp);
			}
			regFund.setCommitments(list);
			list = null;
			if (regFund.getDisbursements() != null) {
				list = new ArrayList(regFund.getDisbursements());
				Collections.sort(list,FundingValidator.dateComp);
			}
			regFund.setDisbursements(list);
			list = null;
			if (regFund.getExpenditures() != null) {
				list = new ArrayList(regFund.getExpenditures());
				Collections.sort(list,FundingValidator.dateComp);
			}
			regFund.setExpenditures(list);
			
			eaForm.getFunding().getRegionalFundings().add(regFund);
			
			return mapping.findForward("updated");
		}
		
		} catch (Exception e) {
			logger.error("Exception",e);
                        throw new DgException(e);
		}
		return null;
	}
     public void setFundingTotals(EditActivityForm eaForm, HttpSession session){
        FundingCalculationsHelper cal = new FundingCalculationsHelper();
        Collection fundDets = eaForm.getFunding().getFundingDetails();
        if (fundDets != null) {
            Collection<AmpFundingDetail> ampFundDets = ActivityUtil.createAmpFundingDetails(fundDets);
            cal.doCalculations(ampFundDets, eaForm.getFunding().getFundingCurrCode(), true);
            session.setAttribute("totalComm", cal.getTotActualComm());
            session.setAttribute("totalDisb", cal.getTotActualDisb());
            session.setAttribute("totalExpn", cal.getTotActualExp());
        }
        else{
            session.setAttribute("totalComm", 0);
            session.setAttribute("totalDisb", 0);
            session.setAttribute("totalExpn", 0);

        }
    }
 } 