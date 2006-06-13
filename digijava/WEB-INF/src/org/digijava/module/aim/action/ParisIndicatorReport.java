/*
 * Created on 12/05/2006
 * @author akashs
 * 
 */
package org.digijava.module.aim.action;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.form.ParisIndicatorReportForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.ParisIndicator;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;

public class ParisIndicatorReport extends Action {

	private static Logger logger = Logger.getLogger(ParisIndicatorReport.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response) {
		
		// if user is not logged in, forward him to the home page
		if (request.getSession().getAttribute("currentMember") == null)
			return mapping.findForward("index");

		TeamMember tm = (TeamMember) request.getSession().getAttribute("currentMember");
		
		// if user is not a DONOR then forward him to his portfolio
		if (!tm.getTeamType().equalsIgnoreCase(Constants.DEF_DNR_PERSPECTIVE)) 
			return mapping.findForward("viewMyDesktop");
		
		logger.debug("In survey report action");
		
		if (form != null) {
			ParisIndicatorReportForm svForm = (ParisIndicatorReportForm) form;
			
			if (null == svForm.getIndicatorsColl() || svForm.getIndicatorsColl().size() < 1) {
				svForm.setIndicatorsColl(DbUtil.getAllAhSurveyIndicators());
				return mapping.findForward("menu");
			}
			
			if (svForm.getReset().booleanValue()) {
				svForm.setReset(Boolean.TRUE);
				svForm.reset(mapping, request);
			}
			
			String indcId = request.getParameter("indcId");
			/*
			logger.debug("indcId[request scope]   : " + indcId);
			logger.debug("svForm.getIndicatorId() : " + svForm.getIndicatorId());
			logger.debug("svForm.getReset()       : " + svForm.getReset());
			logger.debug("svForm.getFilterFlag()  : " + svForm.getFilterFlag()); */
			
			if ((null != indcId && indcId.trim().length() > 0) || svForm.getFilterFlag().booleanValue()) {
				
				// Populating indicator-report filters here
				if (null == svForm.getYearColl() || svForm.getYearColl().size() < 1) {
					int startYear = svForm.getStartYear().intValue() - Constants.FROM_YEAR_RANGE + 2;
					int closeYear = svForm.getCloseYear().intValue() + Constants.TO_YEAR_RANGE;
					svForm.setYearColl(new ArrayList());
					while (startYear <= closeYear)
						svForm.getYearColl().add(new Integer(startYear++));
				}
				if (null == svForm.getCurrencyColl() || svForm.getCurrencyColl().size() < 1)
					svForm.setCurrencyColl(CurrencyUtil.getAllCurrencies(1));
				if (null == svForm.getOrgGroupColl() || svForm.getOrgGroupColl().size() < 1)
					svForm.setOrgGroupColl(DbUtil.getAllOrgGroups());
				if (null == svForm.getStatusColl() || svForm.getStatusColl().size() < 1)
					svForm.setStatusColl(DbUtil.getAllActivityStatus());
				if (null == svForm.getTermAssistColl() || svForm.getTermAssistColl().size() < 1)
					svForm.setTermAssistColl(DbUtil.getAllTermAssist());
				if (null == svForm.getFinancingInstrumentColl() || svForm.getFinancingInstrumentColl().size() < 1)
					svForm.setFinancingInstrumentColl(DbUtil.getAllFinancingInstruments());
				
				//if (!indcId.equalsIgnoreCase(svForm.getIndicatorId()) || svForm.getFilterFlag().booleanValue()) {
				try {
					logger.debug("svForm.getIndicatorId()[insideIF:before] : " + svForm.getIndicatorId());
					svForm.setFilterFlag(Boolean.FALSE);
					AmpAhsurveyIndicator indc = DbUtil.getIndicatorById(Long.valueOf(indcId));
					svForm.setIndicatorId(indcId);
					svForm.setIndicatorName(indc.getName());
					svForm.setIndicatorCode(indc.getIndicatorCode());
					if ("5a".equalsIgnoreCase(svForm.getIndicatorCode()))
						svForm.setNumColsCalculated("7");
					if ("9".equalsIgnoreCase(svForm.getIndicatorCode()))
						svForm.setNumColsCalculated("5");
					logger.debug("svForm.getIndicatorId()[insideIF:after] : " + svForm.getIndicatorId());
					logger.debug("svForm.getIndicatorCode()				  : " + svForm.getIndicatorCode());
					//svForm.setQuestionsColl(DbUtil.getSurveyQuestionsByIndicator(Long.valueOf(indcId)));
					svForm.setDonorsColl(DbUtil.getAidSurveyReportByIndicator(svForm.getIndicatorCode(),svForm.getOrgGroup(),svForm.getStatus(),
							svForm.getStartYear().intValue(),svForm.getCloseYear().intValue(),svForm.getCurrency(),
							svForm.getTermAssist(),svForm.getFinancingInstrument()));
					
					if ("5a".equalsIgnoreCase(svForm.getIndicatorCode()) || "5b".equalsIgnoreCase(svForm.getIndicatorCode())) {
						if (!svForm.getDonorsColl().isEmpty()) {
							int i = Integer.parseInt(svForm.getNumColsCalculated()) - 1;
							logger.debug("i[NumColsCalculated - 1] : " + i);
							int j = 0;
							int numCols = svForm.getCloseYear().intValue() - svForm.getStartYear().intValue() + 2;
							double result[] = new double[numCols - 1];
							double temp[]   = new double[numCols];
							double percent  = 0.0;
							boolean itrFlag = true;
							NumberFormat formatter = new DecimalFormat("#.##");
							svForm.setDonorsCollIndc5(new ArrayList());
							String dnIndc5Row[] = new String[numCols];
							dnIndc5Row[0] = "Donor(s)";
							for(; j < (numCols - 1); j++) {
								result[j] = 0.0;
								dnIndc5Row[j + 1] = Integer.toString(svForm.getStartYear().intValue() + j);
							}
							svForm.getDonorsCollIndc5().add(dnIndc5Row);
							//logger.debug("[5ab-second table]First Row DATA here: ");
							for (int b = 0; b < numCols; b++) {
								logger.debug("dnIndc5Row[" + b + "] : " + dnIndc5Row[b]);
							}
							svForm.getDonorsCollIndc5().add(new String[numCols]);
							
							//logger.debug("[5ab-second table] From Third Row onwards DATA here: ");
							Iterator itr1 = svForm.getDonorsColl().iterator();
							Iterator itr2 = null;
							while (itr1.hasNext()) {
								if (itrFlag) {	// to skip first 'All Donors' row
									itrFlag = false;
									itr1.next();
									continue;
								}
								ParisIndicator pi = (ParisIndicator) itr1.next();
								dnIndc5Row = new String[numCols];
								dnIndc5Row[0] = pi.getDonor();
								//logger.debug("Donor(dnIndc5Row[0]) : " + dnIndc5Row[0]);
								j = 1;
								//logger.debug("pi.getAnswers().size() : " + pi.getAnswers().size());
								itr2 = pi.getAnswers().iterator();
								while (itr2.hasNext()) {
									temp = (double[]) itr2.next();
									//logger.debug("((double[]) itr2.next())[" + i + "] : " + ((double[]) itr2.next())[i]);
									//logger.debug("temp[" + i + "] : " + temp[i]);
									if (temp[i] > 50) {
										dnIndc5Row[j] = "Yes";
										result[j - 1] += 1;
										logger.debug("result[" + (j - 1) + "] : " + result[j - 1]);
										j++;
									}
									else {
										dnIndc5Row[j++] = "No";
									}
								}
								svForm.getDonorsCollIndc5().add(dnIndc5Row);
								/*for (int b = 0; b < numCols; b++) {
									logger.debug("dnIndc5Row[" + b + "] : " + dnIndc5Row[b]);
									if (b < (numCols - 1))
										logger.debug("result[" + b + "] : " + result[b]);
								}*/
							}
							dnIndc5Row = (String []) svForm.getDonorsCollIndc5().get(1);
							dnIndc5Row[0] = "All Donors";
							//logger.debug("((String []) svForm.getDonorsCollIndc5().get(1))[0] : " + ((String []) svForm.getDonorsCollIndc5().get(1))[0]);
							i = (svForm.getDonorsCollIndc5().size() - 2);
							//logger.debug("(svForm.getDonorsCollIndc5().size() - 2) : " + i);
							for(j = 0; j < (numCols - 1); j++) {
								if (result[j] != 0) {
									percent = (result[j] * 100) / i ;
									dnIndc5Row[j + 1] = formatter.format(percent) + "%";
								}
								else
									dnIndc5Row[j + 1] = "0%";
							}
							/*logger.debug("[5ab-second table] From Second Row onwards DATA here: ");
							for (int b = 0; b < numCols; b++) {
								logger.debug("dnIndc5Row[" + b + "] : " + dnIndc5Row[b]);
							}*/
						}
					}
					
					/* 
					 * Getting numColsCalculated dynamically 
					if (svForm.getDonorsColl().size() > 0) {
						ParisIndicator pi = (ParisIndicator)((ArrayList)svForm.getDonorsColl()).get(0);
						svForm.setNumColsCalculated(Integer.toString(((double[])pi.getAnswers().get(0)).length - 1));
						logger.debug("svForm.getNumColsCalculated() : " + svForm.getNumColsCalculated());
					}
					*/
					//Collections.sort((List)svForm.getDonorsColl());
				}
				catch (NumberFormatException nex) {
					logger.debug(nex);
					nex.printStackTrace(System.out);
				}
				catch (Exception ex) {
					logger.debug(ex);
					ex.printStackTrace(System.out);
				}
				if ("7".equalsIgnoreCase(svForm.getIndicatorCode()) 
						|| "6".equalsIgnoreCase(svForm.getIndicatorCode()))
					return mapping.findForward("report2");
				else
					return mapping.findForward("report1");
			}
			return mapping.findForward("menu");
		}
		else {
			logger.debug("ActionForm is null.");
			return mapping.findForward("viewMyDesktop");
		}
	}
}

