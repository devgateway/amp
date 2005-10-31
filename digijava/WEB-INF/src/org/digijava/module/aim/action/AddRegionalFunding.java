/*
 * AddRegionalFunding.java
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpPerspective;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.RegionalFunding;
import org.digijava.module.aim.helper.TeamMember;

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
		if (action != null && action.equalsIgnoreCase("show")) {
			logger.debug("Forarding to forward");
			eaForm.setFundingRegionId(new Long(-1));
			if (tm.getAppSettings().getPerspective()
					.equalsIgnoreCase(Constants.DEF_DNR_PERSPECTIVE)) {
				request.setAttribute("defPerspective",Constants.DONOR);
			} else if (tm.getAppSettings().getPerspective().
					equalsIgnoreCase(Constants.DEF_MFD_PERSPECTIVE)) {
				request.setAttribute("defPerspective",Constants.MOFED);
			}
			String defCurr = DbUtil.getCurrency(
					tm.getAppSettings().getCurrencyId()).getCurrencyCode();
			request.setAttribute("defCurrency",defCurr);
			
			return mapping.findForward("forward");
		} else if (action != null && action.equalsIgnoreCase("showEdit")) {
			Iterator itr = eaForm.getRegionalFundings().iterator();
			String id = request.getParameter("fundId");
			long fId = Long.parseLong(id); 
			while (itr.hasNext()) {
				RegionalFunding rd = (RegionalFunding) itr.next();
				if (rd.getRegionId().longValue() == fId) {
					eaForm.setFundingRegionId(rd.getRegionId());
					break;
				}
			}
			
			if (tm.getAppSettings().getPerspective()
					.equalsIgnoreCase(Constants.DEF_DNR_PERSPECTIVE)) {
				request.setAttribute("defPerspective",Constants.DONOR);
			} else if (tm.getAppSettings().getPerspective().
					equalsIgnoreCase(Constants.DEF_MFD_PERSPECTIVE)) {
				request.setAttribute("defPerspective",Constants.MOFED);
			}
			String defCurr = DbUtil.getCurrency(
					tm.getAppSettings().getCurrencyId()).getCurrencyCode();
			request.setAttribute("defCurrency",defCurr);			
			return mapping.findForward("forward");
		} else if (action != null && action.equalsIgnoreCase("update")){

			RegionalFunding regFund = new RegionalFunding();
			
			Iterator itr = eaForm.getSelectedLocs().iterator();
			while (itr.hasNext()) {
				Location loc = (Location) itr.next();
				if (loc.getRegionId().equals(eaForm.getFundingRegionId())) {
					regFund.setRegionName(loc.getRegion());
					break;
				}
			}
			regFund.setRegionId(eaForm.getFundingRegionId());
			
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
							fd.setAdjustmentType(Integer.parseInt(val));
							if (fd.getAdjustmentType() == 1) {
								fd.setAdjustmentTypeName("Actual");
							} else if (fd.getAdjustmentType() == 0) {
								fd.setAdjustmentTypeName("Planned");
							}
							break;
						case 2:
							fd.setTransactionAmount(val);
							break;
						case 3:
							fd.setCurrencyCode(val);
							break;
						case 4:
							fd.setTransactionDate(val);
							break;
						case 5:
							fd.setPerspectiveCode(val);
							Iterator itr1 = eaForm.getPerspectives().iterator();
							while (itr1.hasNext()) {
								AmpPerspective pers = (AmpPerspective) itr1.next();
								if (pers.getCode().equals(val)) {
									fd.setPerspectiveName(pers.getName());
								}
							}
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
							fd.setAdjustmentType(Integer.parseInt(val));
							logger.debug("Adjustment type = " + fd.getAdjustmentType());
							if (fd.getAdjustmentType() == 1) {
								fd.setAdjustmentTypeName("Actual");
							} else if (fd.getAdjustmentType() == 0) {
								fd.setAdjustmentTypeName("Planned");
							}
							break;
						case 2:
							fd.setTransactionAmount(val);
							break;
						case 3:
							fd.setCurrencyCode(val);
							break;
						case 4:
							fd.setTransactionDate(val);
							break;
						case 5:
							fd.setPerspectiveCode(val);
							Iterator itr1 = eaForm.getPerspectives().iterator();
							while (itr1.hasNext()) {
								AmpPerspective pers = (AmpPerspective) itr1.next();
								if (pers.getCode().equals(val)) {
									fd.setPerspectiveName(pers.getName());
								}
							}
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
							fd.setAdjustmentType(Integer.parseInt(val));
							logger.debug("Adjustment type = " + fd.getAdjustmentType());
							if (fd.getAdjustmentType() == 1) {
								fd.setAdjustmentTypeName("Actual");
							} else if (fd.getAdjustmentType() == 0) {
								fd.setAdjustmentTypeName("Planned");
							}
							break;
						case 2:
							fd.setTransactionAmount(val);
							break;
						case 3:
							fd.setCurrencyCode(val);
							break;
						case 4:
							fd.setTransactionDate(val);
							break;
						case 5:
							fd.setPerspectiveCode(val);
							Iterator itr1 = eaForm.getPerspectives().iterator();
							while (itr1.hasNext()) {
								AmpPerspective pers = (AmpPerspective) itr1.next();
								if (pers.getCode().equals(val)) {
									fd.setPerspectiveName(pers.getName());
								}
							}
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
			
			if (eaForm.getRegionalFundings() == null) {
				eaForm.setRegionalFundings(new ArrayList());
			}
			if (eaForm.getRegionalFundings().contains(regFund)) {
				eaForm.getRegionalFundings().remove(regFund);
			}
			eaForm.getRegionalFundings().add(regFund);
			
			return mapping.findForward("updated");
		}
		
		} catch (Exception e) {
			logger.debug("Exception");
			e.printStackTrace(System.out);
		}
		return null;
	}
 } 