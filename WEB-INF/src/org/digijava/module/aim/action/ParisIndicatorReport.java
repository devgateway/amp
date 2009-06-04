/*
 * Created on 12/05/2006
 * @author akashs
 *
 */
package org.digijava.module.aim.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicatorCalcFormula;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.ParisIndicatorReportForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.ParisIndicator;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AmpMath;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

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

        ParisIndicatorReportForm svForm = (ParisIndicatorReportForm) form;
        
        svForm.setDonorsColl(null);
        svForm.setDonorColl(null);
        svForm.setIndicatorsColl(null);

        if (null == svForm.getIndicatorsColl() || svForm.getIndicatorsColl().size() < 1) {
            svForm.setIndicatorsColl(DbUtil.getAllAhSurveyIndicators());
            //return mapping.findForward("menu");
        }

        if (svForm.getReset().booleanValue()) {
            //svForm.setReset(Boolean.TRUE);
            svForm.reset(mapping, request);
        }

        String indcId = request.getParameter("indcId");

        // Populating indicator-report filters here
        if ( (null != indcId && indcId.trim().length() > 0) || svForm.getFilterFlag().booleanValue()) {

            if (!svForm.getFilterFlag().booleanValue()) {
                ApplicationSettings apps = null;
                if (null != tm) {
                    apps = tm.getAppSettings();
                    if (null != apps.getFisCalId())
                        svForm.setCalendar(apps.getFisCalId().toString());
                    else
                        svForm.setCalendar(DbUtil.getBaseFiscalCalendar().toString());
                    svForm.setCurrency(CurrencyUtil.getAmpcurrency(apps.getCurrencyId()).getCurrencyCode());
                }
            }
            AmpAhsurveyIndicator indc = null;
            if(indcId != null){
            	indc = DbUtil.getIndicatorById(Long.valueOf(indcId));
        	} else {
        		indc = DbUtil.getIndicatorById(Long.valueOf(svForm.getIndicatorId()));
        	}
            AmpAhsurveyIndicatorCalcFormula fl = getFormula(indc.getCalcFormulas());

            if (svForm.getStartYear() == null || svForm.getCloseYear() == null) {
                if (indc != null && fl != null && fl.getEnabled()!=null && fl.getEnabled().booleanValue()) {
                    try {
                        Integer i = Integer.valueOf(fl.getBaseLineValue());
                        svForm.setStartYear(i);
                        svForm.setCloseYear(i + 2);
                    } catch (Exception ex) {
                        svForm.setStartYear(new Integer(Calendar.getInstance().get(Calendar.YEAR) - 2));
                        svForm.setCloseYear(new Integer(Calendar.getInstance().get(Calendar.YEAR)));
                    }
                } else {
                    svForm.setStartYear(new Integer(Calendar.getInstance().get(Calendar.YEAR) - 2));
                    svForm.setCloseYear(new Integer(Calendar.getInstance().get(Calendar.YEAR)));
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
                svForm.setCurrencyColl(CurrencyUtil.getAllCurrencies(CurrencyUtil.ALL_ACTIVE));
            if (null == svForm.getOrgGroupColl() || svForm.getOrgGroupColl().size() < 1)
                svForm.setOrgGroupColl(DbUtil.getAllOrgGroups());
            if (null == svForm.getStatusColl() || svForm.getStatusColl().size() < 1)
                svForm.setStatusColl(DbUtil.getAllActivityStatus());
            //if (null == svForm.getTermAssistColl() || svForm.getTermAssistColl().size() < 1)
            //svForm.setTermAssistColl(DbUtil.getAllTermAssist());
//				if (null == svForm.getFinancingInstrumentColl() || svForm.getFinancingInstrumentColl().size() < 1)
//					svForm.setFinancingInstrumentColl(DbUtil.getAllFinancingInstruments());
            if (null == svForm.getCalendarColl() || svForm.getCalendarColl().size() < 1)
                svForm.setCalendarColl(DbUtil.getAllFisCalenders());
            if (null == svForm.getDonorColl() || svForm.getDonorColl().size() < 1)
                svForm.setDonorColl(DbUtil.getAllDonorOrgs());
            if (null == svForm.getSectorColl() || svForm.getSectorColl().size() < 1) {
                svForm.setSectorColl(new ArrayList());
                Iterator iter = SectorUtil.getAmpSectors().iterator();
                while (iter.hasNext()) {
                    AmpSector ampSector = (AmpSector) iter.next();
                    if (ampSector.getName() != null && ampSector.getName().length() > 30) {
                        String temp = ampSector.getName().substring(0, 30) + "...";
                        ampSector.setName(temp);
                    }
                    svForm.getSectorColl().add(ampSector);

                    Iterator iter1 = SectorUtil.getAmpSubSectors(ampSector.getAmpSectorId()).iterator();
                    while (iter1.hasNext()) {
                        AmpSector ampSubSector = (AmpSector) iter1.next();
                        if (ampSubSector.getName() != null && ampSubSector.getName().length() > 35) {
                            ampSubSector.setName("--" + ampSubSector.getName().substring(0, 35) + "...");
                        } else {
                            ampSubSector.setName("--" + ampSubSector.getName());
                        }
                        svForm.getSectorColl().add(ampSubSector);

                        Iterator iter2 = SectorUtil.getAmpSubSectors(ampSubSector.getAmpSectorId()).iterator();
                        while (iter2.hasNext()) {
                            AmpSector ampSubSubSector = (AmpSector) iter2.next();
                            if (ampSubSubSector.getName() != null && ampSubSubSector.getName().length() > 35) {
                                ampSubSubSector.setName("----" + ampSubSubSector.getName().substring(0, 35) + "...");
                            } else {
                                ampSubSubSector.setName("----" + ampSubSubSector.getName());
                            }
                            svForm.getSectorColl().add(ampSubSubSector);
                        }
                    }
                }
            }

            try {

                svForm.setIndicatorId(indcId);
                svForm.setIndicatorName(indc.getName());
                svForm.setIndicatorCode(indc.getIndicatorCode());
                if (svForm.getFilterFlag().booleanValue()) {
                    svForm.setFilterFlag(Boolean.FALSE);
                    svForm.setNumColsCalculated("4");
                }
                if ("10a".equalsIgnoreCase(svForm.getIndicatorCode())) {
                    svForm.setDonorsColl(DbUtil.getAidSurveyReportByIndicator10a(svForm.getOrgGroup(), svForm.getDonor(),
                        svForm.getStartYear().intValue(), svForm.getCloseYear().intValue(), RequestUtils.getSite(request).getId(), RequestUtils.getNavigationLanguage(request).getCode()));
                    svForm.setDonorsColl(filterDonors(svForm.getDonorsColl(), 1));
                    return mapping.findForward("report1");
                }
                if ("5a".equalsIgnoreCase(svForm.getIndicatorCode()))
                    svForm.setNumColsCalculated("8");
                else if ("9".equalsIgnoreCase(svForm.getIndicatorCode()))
                    svForm.setNumColsCalculated("5");

                /* Added by Alex Gartner for category manager compatibility.
                 * Getting the AmpCategoryValue object for the financingInstrument ID*/
                AmpCategoryValue financingInstrument = CategoryManagerUtil.getAmpCategoryValueFromDb(svForm.getFinancingInstrument());
                AmpCategoryValue status = CategoryManagerUtil.getAmpCategoryValueFromDb(svForm.getStatus());
                /* End by Alex Gartner*/
                Collection spCol = DbUtil.getAidSurveyReportByIndicator(svForm.getIndicatorCode(), svForm.getDonor(),
                    svForm.getOrgGroup(), status, svForm.getStartYear().intValue(), svForm.getCloseYear().intValue(),
                    svForm.getCurrency(), svForm.getTermAssist(), financingInstrument,
                    svForm.getSector(), svForm.getCalendar(), RequestUtils.getSite(request).getId(), RequestUtils.getNavigationLanguage(request).getCode());

                svForm.setDonorsColl(spCol);

                if (svForm.getIndicatorCode().equalsIgnoreCase("5a") || svForm.getIndicatorCode().equalsIgnoreCase("5b")) {
                    if (!svForm.getDonorsColl().isEmpty()) {
                        int dnSize = svForm.getDonorsColl().size() - 1;
                        int lastIndex = Integer.parseInt(svForm.getNumColsCalculated()) - 1;
                        int numCols = svForm.getCloseYear().intValue() - svForm.getStartYear().intValue() + 1;
                        String donor[] = {TranslatorWorker.unicodeToUTF8(TranslatorWorker.translateText("Less than 10%", RequestUtils.getNavigationLanguage(request).getCode(), RequestUtils.getSite(request).getId()))
                        		, TranslatorWorker.unicodeToUTF8(TranslatorWorker.translateText("From 10 to 50%", RequestUtils.getNavigationLanguage(request).getCode(), RequestUtils.getSite(request).getId()))
                        		, TranslatorWorker.unicodeToUTF8(TranslatorWorker.translateText("From 50 to 90%", RequestUtils.getNavigationLanguage(request).getCode(), RequestUtils.getSite(request).getId()))
                        		, TranslatorWorker.unicodeToUTF8(TranslatorWorker.translateText("More than 90%", RequestUtils.getNavigationLanguage(request).getCode(), RequestUtils.getSite(request).getId()))};
                        
                        String dnIndc5Row[] = null;
                        int answers[] = new int[numCols];
                        double temp[] = new double[lastIndex + 1];
                        int j = 0;
                        double val = 0.0;
                        Iterator itr1 = null;
                        Iterator itr2 = null;

                        svForm.setDonorsCollIndc5(new ArrayList());
                        dnIndc5Row = new String[numCols + 1];

                        // creating header row
                        if ("5a".equalsIgnoreCase(svForm.getIndicatorCode()))
                            dnIndc5Row[0] = TranslatorWorker.unicodeToUTF8(TranslatorWorker.translateText("Percent of ODA using all three partner's PFM procedures", RequestUtils.getNavigationLanguage(request).getCode(), RequestUtils.getSite(request).getId()));
                        else
                            dnIndc5Row[0] = TranslatorWorker.unicodeToUTF8(TranslatorWorker.translateText("Percent of ODA using national procurement systems", RequestUtils.getNavigationLanguage(request).getCode(), RequestUtils.getSite(request).getId()));

                        for (; j < numCols; j++)
                            dnIndc5Row[j + 1] = Integer.toString(svForm.getStartYear().intValue() + j);
                        svForm.getDonorsCollIndc5().add(dnIndc5Row);

                        for (int cntr = 0; cntr < donor.length; cntr++) {
                            dnIndc5Row = new String[numCols + 1];
                            dnIndc5Row[0] = donor[cntr];
                            itr1 = svForm.getDonorsColl().iterator();
                            while (itr1.hasNext()) {
                                ParisIndicator pi = (ParisIndicator) itr1.next();
                                if (TranslatorWorker.unicodeToUTF8(TranslatorWorker.translateText("All Donors", RequestUtils.getNavigationLanguage(request).getCode(), RequestUtils.getSite(request).getId())).equalsIgnoreCase(pi.getDonor()))
                                    continue;
                                j = 0;
                                itr2 = pi.getAnswers().iterator();
                                while (itr2.hasNext()) {
                                    BigDecimal[] tempBD = ((BigDecimal[]) itr2.next());
                                    switch (cntr) {
                                        case 0:
                                            if (tempBD[lastIndex].doubleValue() < 10)
                                                answers[j] += 1;
                                            break;
                                        case 1:
                                            if (tempBD[lastIndex].doubleValue() >= 10 && tempBD[lastIndex].doubleValue() < 50)
                                                answers[j] += 1;
                                            break;
                                        case 2:
                                            if (tempBD[lastIndex].doubleValue() >= 50 && tempBD[lastIndex].doubleValue() <= 90)
                                                answers[j] += 1;
                                            break;
                                        case 3:
                                            if (tempBD[lastIndex].doubleValue() > 90)
                                                answers[j] += 1;
                                    }
                                    j++;
                                }
                            }

                            for (j = 0; j < numCols; j++) {
                                val = (100.0 * answers[j]) / dnSize;
                                if ( (val - (int) val) < 0.5)
                                    dnIndc5Row[j + 1] = Integer.toString( (int) val);
                                else
                                    dnIndc5Row[j + 1] = Long.toString(Math.round(val));
                                answers[j] = 0;
                            }
                            svForm.getDonorsCollIndc5().add(dnIndc5Row);
                        }

                    }
                }
            } catch (NumberFormatException nex) {
                logger.debug(nex);
                nex.printStackTrace(System.out);
            } catch (Exception ex) {
                logger.debug(ex);
                ex.printStackTrace(System.out);
            }

            List flDonorCol = null;
            if (svForm.getIndicatorCode().equalsIgnoreCase("3") ||
                svForm.getIndicatorCode().equalsIgnoreCase("4") ||
                svForm.getIndicatorCode().equalsIgnoreCase("5a") ||
                svForm.getIndicatorCode().equalsIgnoreCase("5b") ||
                svForm.getIndicatorCode().equalsIgnoreCase("9") ||
                svForm.getIndicatorCode().equalsIgnoreCase("10a")) {

                flDonorCol = filterDonors(svForm.getDonorsColl(), 1);
            } else {
                flDonorCol = filterDonors(svForm.getDonorsColl(), 0);
            }

            if (svForm.getIndicatorCode().equalsIgnoreCase("3")) {
                svForm.setCalcResult(clacTargetValue(fl, flDonorCol, true));
                if (fl != null &&
                    fl.getEnabled() != null &&
                    fl.getEnabled().booleanValue() &&
                    fl.getCalcFormula() != null) {
                    svForm.setTargetValue(fl.getTargetValue());
                }

                svForm.setDonorsColl(flDonorCol);
                return mapping.findForward("report1");
            } else if (svForm.getIndicatorCode().equalsIgnoreCase("5a") || svForm.getIndicatorCode().equalsIgnoreCase("5b")) {
                svForm.setCalcResult(clacTargetValue(fl, flDonorCol, true));
                if (fl != null &&
                    fl.getEnabled() != null &&
                    fl.getEnabled().booleanValue() &&
                    fl.getCalcFormula() != null) {
                    svForm.setTargetValue(fl.getTargetValue());
                }

                svForm.setDonorsColl(flDonorCol);
                return mapping.findForward("report1");
            } else if (svForm.getIndicatorCode().equalsIgnoreCase("6")) {
                svForm.setCalcResult(clacTargetValue(fl, flDonorCol, false));
                if (fl != null &&
                    fl.getEnabled() != null &&
                    fl.getEnabled().booleanValue() &&
                    fl.getCalcFormula() != null) {
                    svForm.setTargetValue(fl.getTargetValue());
                }

                svForm.setDonorsColl(flDonorCol);
                return mapping.findForward("report2");
            } else if (svForm.getIndicatorCode().equalsIgnoreCase("7")) {
                svForm.setCalcResult(clacTargetValue(fl, flDonorCol, true));
                if (fl != null &&
                    fl.getEnabled() != null &&
                    fl.getEnabled().booleanValue() &&
                    fl.getCalcFormula() != null) {
                    svForm.setTargetValue(fl.getTargetValue());
                }

                svForm.setDonorsColl(flDonorCol);
                return mapping.findForward("report2");
            } else {
                svForm.setDonorsColl(flDonorCol);
                return mapping.findForward("report1");
            }
        }
        return mapping.findForward("menu");
    }

    private String clacTargetValue(AmpAhsurveyIndicatorCalcFormula fl, List donorCol, boolean lastColumn) {
        if (donorCol != null && fl != null && fl.getCalcFormula() != null) {
            if(donorCol.size() == 0) {
            	return null;
            }
        	ParisIndicator donor = (ParisIndicator) donorCol.get(0);
            ArrayList answ1 = donor.getAnswers();
            BigDecimal[] ans1 = (BigDecimal[]) answ1.get(0);
            if (ans1 != null) {
                String formula = null;
                if (lastColumn) {
                    formula = getFormulaText(fl, ans1[ans1.length - 1].doubleValue());
                } else {
                    formula = getFormulaText(fl, ans1[0].doubleValue());
                }

                return String.valueOf(AmpMath.calcExp(formula));
            }
        }
        return null;
    }

    private AmpAhsurveyIndicatorCalcFormula getFormula(Set set) {
        AmpAhsurveyIndicatorCalcFormula retSurvey = null;
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            retSurvey = (AmpAhsurveyIndicatorCalcFormula) itr.next();
        }
        return retSurvey;
    }

    private String getFormulaText(AmpAhsurveyIndicatorCalcFormula formula, double constant) {
        String flText = null;
        if (formula != null) {
            flText = formula.getCalcFormula().replace(formula.getConstantName(), String.valueOf(constant));
        }
        return flText;
    }

    private List filterDonors(Collection donorsCol, int st) {
        if(donorsCol==null) return null;

        List filteredDonorsCol = new ArrayList();
        List donorsLst = new ArrayList(donorsCol);

        boolean flag;
        for (ListIterator dIter = donorsLst.listIterator(); dIter.hasNext(); ) {
            ParisIndicator donor = (ParisIndicator) dIter.next();

            flag = true;
            for (Iterator fdIter = filteredDonorsCol.iterator(); fdIter.hasNext(); ) {
                ParisIndicator fDonor = (ParisIndicator) fdIter.next();

                if (fDonor.getDonor().equalsIgnoreCase(donor.getDonor())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                dIter.remove();
                filteredDonorsCol.add(donor);
            }
        }

        for (ListIterator dIter = filteredDonorsCol.listIterator(); dIter.hasNext(); ) {
            ParisIndicator donor = (ParisIndicator) dIter.next();

            for (Iterator fdIter = donorsLst.iterator(); fdIter.hasNext(); ) {
                ParisIndicator fDonor = (ParisIndicator) fdIter.next();
                if (fDonor.getDonor().equalsIgnoreCase(donor.getDonor())) {
                    ArrayList answ1 = fDonor.getAnswers();
                    ArrayList answ2 = donor.getAnswers();

                    for (int i = 0; i < answ1.size(); i++) {
                        BigDecimal ans1[] = (BigDecimal[]) answ1.get(i);
                        BigDecimal ans2[] = (BigDecimal[]) answ2.get(i);
                        for (int j = st; j < ans1.length; j++) {
                        	//This method should not exist :( but for now if the value is < 0 then make it 0, otherwise a 100% percentage will become 98%, etc.
                        	if(ans2[j].doubleValue()<0) {
                        		ans2[j] = new BigDecimal(0);
                        	}
                        	if(ans1[j].doubleValue()<0) {
                        		ans1[j] = new BigDecimal(0);
                        	}
                            ans2[j].add(ans1[j]);
                        }
                    }
                }
            }
        }

        return filteredDonorsCol;
    }
}