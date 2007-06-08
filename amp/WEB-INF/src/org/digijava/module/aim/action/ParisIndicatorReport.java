/*
 * Created on 12/05/2006
 * @author akashs
 * 
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.ParisIndicatorReportForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CommonWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.EthiopianCalendar;
import org.digijava.module.aim.helper.ParisIndicator;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;

public class ParisIndicatorReport extends Action {

	private static Logger logger = Logger.getLogger(ParisIndicatorReport.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response) {
		
		// if user is not logged in, forward him to the home page
		if (request.getSession().getAttribute("currentMember") == null)
			return mapping.findForward("index");

		TeamMember tm = (TeamMember) request.getSession().getAttribute("currentMember");
		
		// if user is not a DONOR then forward him to his portfolio
		//if (!tm.getTeamType().equalsIgnoreCase(Constants.DEF_DNR_PERSPECTIVE)) 
			//return mapping.findForward("viewMyDesktop");
		
		logger.debug("In paris-indicator survey report action");
		
		if (form != null) {
			ParisIndicatorReportForm svForm = (ParisIndicatorReportForm) form;
			
			if (null == svForm.getIndicatorsColl() || svForm.getIndicatorsColl().size() < 1) {
				svForm.setIndicatorsColl(DbUtil.getAllAhSurveyIndicators());
				return mapping.findForward("menu");
			}
			
			if (svForm.getReset().booleanValue()) {
				//svForm.setReset(Boolean.TRUE);
				svForm.reset(mapping, request);
			}
			
			String indcId = request.getParameter("indcId");
			
			// Populating indicator-report filters here
			if ((null != indcId && indcId.trim().length() > 0) || svForm.getFilterFlag().booleanValue()) {
				
				if (!svForm.getFilterFlag().booleanValue()) {
					ApplicationSettings apps = null;
					if (null != tm) {
						apps = tm.getAppSettings();
						svForm.setPerspective(CommonWorker.getPerspective(apps.getPerspective()));
						if (null != apps.getFisCalId())
							svForm.setCalendar(apps.getFisCalId().toString());
						else
							svForm.setCalendar(DbUtil.getBaseFiscalCalendar().toString());
						svForm.setCurrency(CurrencyUtil.getAmpcurrency(apps.getCurrencyId()).getCurrencyCode());
					}
				}
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
				//if (null == svForm.getTermAssistColl() || svForm.getTermAssistColl().size() < 1)
					//svForm.setTermAssistColl(DbUtil.getAllTermAssist());
				if (null == svForm.getFinancingInstrumentColl() || svForm.getFinancingInstrumentColl().size() < 1)
					svForm.setFinancingInstrumentColl(DbUtil.getAllFinancingInstruments());
				if (null == svForm.getCalendarColl() || svForm.getCalendarColl().size() < 1)
					svForm.setCalendarColl(DbUtil.getAllFisCalenders());
				if (null == svForm.getDonorColl() || svForm.getDonorColl().size() < 1)
					svForm.setDonorColl(DbUtil.getAllDonorOrgs());
				if (null == svForm.getSectorColl() || svForm.getSectorColl().size() < 1) {
					svForm.setSectorColl(new ArrayList());
					Iterator iter = SectorUtil.getAmpSectors().iterator() ;
					while(iter.hasNext()) {
						AmpSector ampSector = (AmpSector) iter.next();
						if(ampSector.getName().length() > 30) {
							String temp=ampSector.getName().substring(0,30) + "...";
							ampSector.setName(temp);
						}
						svForm.getSectorColl().add(ampSector);
							
						Iterator iter1 = SectorUtil.getAmpSubSectors(ampSector.getAmpSectorId()).iterator(); 
						while(iter1.hasNext()) {
							AmpSector ampSubSector = (AmpSector) iter1.next();
							if(ampSubSector.getName().length() > 35) {
								ampSubSector.setName("--" + ampSubSector.getName().substring(0,35) + "...");
							} else {
								ampSubSector.setName("--" + ampSubSector.getName());	
							}
							svForm.getSectorColl().add(ampSubSector);
							
							Iterator iter2 = SectorUtil.getAmpSubSectors(ampSubSector.getAmpSectorId()).iterator();
							while(iter2.hasNext()) {
								AmpSector ampSubSubSector = (AmpSector) iter2.next();
								if(ampSubSubSector.getName().length() > 35) {
									ampSubSubSector.setName("----" + ampSubSubSector.getName().substring(0,35) + "...");
								} else {
									ampSubSubSector.setName("----" + ampSubSubSector.getName());	
								}
								svForm.getSectorColl().add(ampSubSubSector);
							}
						}
					}
				}
				
				try {
					AmpAhsurveyIndicator indc = DbUtil.getIndicatorById(Long.valueOf(indcId));
					svForm.setIndicatorId(indcId);
					svForm.setIndicatorName(indc.getName());
					svForm.setIndicatorCode(indc.getIndicatorCode());
					if (svForm.getFilterFlag().booleanValue()) {
						svForm.setFilterFlag(Boolean.FALSE);
						svForm.setNumColsCalculated("4");
					}
					if ("10a".equalsIgnoreCase(svForm.getIndicatorCode())) {
						svForm.setDonorsColl(DbUtil.getAidSurveyReportByIndicator10a(svForm.getOrgGroup(), svForm.getDonor(), 
								svForm.getStartYear().intValue(),svForm.getCloseYear().intValue()));
						return mapping.findForward("report1");
					}
					if ("5a".equalsIgnoreCase(svForm.getIndicatorCode()))
						svForm.setNumColsCalculated("8");
					else if ("9".equalsIgnoreCase(svForm.getIndicatorCode()))
						svForm.setNumColsCalculated("5");
					svForm.setDonorsColl(DbUtil.getAidSurveyReportByIndicator(svForm.getIndicatorCode(),svForm.getDonor(),
							svForm.getOrgGroup(),svForm.getStatus(),svForm.getStartYear().intValue(),svForm.getCloseYear().intValue(),
							svForm.getCurrency(),svForm.getTermAssist(),svForm.getFinancingInstrument(),
							svForm.getPerspective(),svForm.getSector(),svForm.getCalendar()));
					
					if ("5a".equalsIgnoreCase(svForm.getIndicatorCode()) || "5b".equalsIgnoreCase(svForm.getIndicatorCode())) {
						if (!svForm.getDonorsColl().isEmpty()) {
							int dnSize = svForm.getDonorsColl().size() - 1;
							int lastIndex = Integer.parseInt(svForm.getNumColsCalculated()) - 1;
							int numCols = svForm.getCloseYear().intValue() - svForm.getStartYear().intValue() + 1;
							String donor[] = {"Less than 10%", "From 10 to 50%", "From 50 to 90%", "More than 90%"};
							String dnIndc5Row[] = null;
							int answers[] = new int[numCols];
							double temp[]   = new double[lastIndex + 1];
							int j = 0;
							double val = 0.0;
							Iterator itr1 = null;
							Iterator itr2 = null;
							
							svForm.setDonorsCollIndc5(new ArrayList());
							dnIndc5Row = new String[numCols + 1];
							
							// creating header row
							if ("5a".equalsIgnoreCase(svForm.getIndicatorCode()))
								dnIndc5Row[0] = "Percent of ODA using all three partner's PFM procedures";
							else
								dnIndc5Row[0] = "Percent of ODA using national procurement systems";
							
							for(; j < numCols; j++)
								dnIndc5Row[j + 1] = Integer.toString(svForm.getStartYear().intValue() + j);
							svForm.getDonorsCollIndc5().add(dnIndc5Row);
							
							for (int cntr = 0; cntr < donor.length; cntr++) {
								dnIndc5Row = new String[numCols + 1];
								dnIndc5Row[0] = donor[cntr];
								itr1 = svForm.getDonorsColl().iterator();
								while (itr1.hasNext()) {
									ParisIndicator pi = (ParisIndicator) itr1.next();
									if ("All Donors".equalsIgnoreCase(pi.getDonor()))
										continue;
									j =  0;
									itr2 = pi.getAnswers().iterator();
									while (itr2.hasNext()) {
										temp = (double[]) itr2.next();
										switch (cntr) {
											case 0:	if (temp[lastIndex] < 10)
														answers[j] += 1;
													break;
											case 1:	if (temp[lastIndex] >= 10 && temp[lastIndex] < 50)
														answers[j] += 1;
													break;
											case 2: if (temp[lastIndex] >= 50 && temp[lastIndex] <= 90)
														answers[j] += 1;
													break;
											case 3: if (temp[lastIndex] > 90)
														answers[j] += 1;
										}
										j++;
									}
								}
								for(j = 0; j < numCols; j++) {
									val = (100.0 * answers[j]) / dnSize ;
									if ((val - (int) val) < 0.5)
										dnIndc5Row[j + 1] = Integer.toString((int) val);
									else
										dnIndc5Row[j + 1] = Long.toString(Math.round(val));
									answers[j] = 0;
								}
								svForm.getDonorsCollIndc5().add(dnIndc5Row);
							}
						}
					}
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

