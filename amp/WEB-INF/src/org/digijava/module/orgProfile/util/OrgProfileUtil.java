package org.digijava.module.orgProfile.util;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeMap;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocToOrgDAO;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.orgProfile.helper.Project;
import org.digijava.module.parisindicator.util.PIConstants;
import org.digijava.module.widget.helper.DonorSectorFundingHelper;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.rtf.table.RtfCell;

import fi.joensuu.joyds1.calendar.EthiopicCalendar;
import fi.joensuu.joyds1.calendar.NepaliCalendar;

/**
 *
 * @author medea
 */
public class OrgProfileUtil {

    private static Logger logger = Logger.getLogger(OrgProfileUtil.class);
    public static final Color TITLECOLOR = new Color(34, 46, 93);
    public static final Color BORDERCOLOR = new Color(255, 255, 255);
    public static final Color CELLCOLOR = new Color(219, 229, 241);
    public static final Font PLAINFONT = new Font(Font.TIMES_ROMAN, 10);
    public static final Font HEADERFONT = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
    public static final Font HEADERFONTWHITE = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.WHITE);

    /**
     *
     * @param indCode
     * @param currCode
     * @param orgId
     * @param orgGroupId
     * @param startDate
     * @param endDate
     * @param teamMember
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    public static Long getValue(String indCode, String currCode, Long[] orgIds, Long orgGroupId, Date startDate, Date endDate, TeamMember teamMember, Collection<Long> locationIds, boolean showOnlyApprovedActivities) throws DgException {
        Long total = 0l;
        try {
            // questions that must be answered with 'yes' in the survey, nominator column
            int nominator[] = null;
            // questions that must be answered with 'yes' in the survey, denominator column
            int denominator[] = null;
            int adjustmentType = Constants.ACTUAL;
            if (indCode.equals("3")) {
                nominator = new int[]{2, 1};
                denominator = new int[]{1};
            } else {
                if (indCode.equals("4")) {
                    nominator = new int[]{3};
                    denominator = new int[]{0};
                } else {
                    if (indCode.equals("5a")) {
                        nominator = new int[]{1, 5, 6, 7};
                        denominator = new int[]{1};

                    } else {
                        if (indCode.equals("5b")) {
                            nominator = new int[]{8, 1};
                            denominator = new int[]{1};

                        } else {
                            if (indCode.equals("6")) {
                                total = getPIUValue(orgIds, orgGroupId, startDate, endDate, teamMember, locationIds, showOnlyApprovedActivities);
                                return total;
                            } else {
                                if (indCode.equals("7")) {
                                    nominator = new int[]{1};
                                    denominator = new int[]{1};

                                } else {
                                    if (indCode.equals("9")) {
                                        nominator = new int[]{10};
                                        denominator = new int[]{0};

                                    } else {
                                        if (indCode.equals("10a")) {
                                            total = getIndicator10aValue(startDate, endDate, orgIds, orgGroupId);
                                            return total;
                                        } else {
                                            if (indCode.equals("5aii")) {
                                                nominator = new int[]{1, 5, 6, 7};
                                                total = getDonorsCount(nominator, orgIds, orgGroupId, startDate, endDate, teamMember, locationIds, showOnlyApprovedActivities);
                                                return total;
                                            } else {
                                                if (indCode.equals("5bii")) {
                                                    nominator = new int[]{8, 1};
                                                    total = getDonorsCount(nominator, orgIds, orgGroupId, startDate, endDate, teamMember, locationIds, showOnlyApprovedActivities);
                                                    return total;
                                                } else {
                                                    if (indCode.equals("10b") || indCode.equals("8")) {
                                                        return total;
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                }
            }
            String nominatorCondition = getAmpAhsurveyCondition(nominator);
            String denominatorCondition = getAmpAhsurveyCondition(denominator);
            Double nominatorValue = null;
            Double denominatorValue = null;
            if (nominatorCondition.length() > 0) {
                if (indCode.equals("7")) {
                    // in that case we are calculating  planned disb.
                    nominatorValue = getValue(indCode, Constants.PLANNED, currCode, orgIds, orgGroupId, startDate, endDate, teamMember, nominatorCondition, locationIds, showOnlyApprovedActivities);
                } else {
                    //calculating  actual disb.
                    nominatorValue = getValue(indCode, adjustmentType, currCode, orgIds, orgGroupId, startDate, endDate, teamMember, nominatorCondition, locationIds, showOnlyApprovedActivities);
                }
            }
            if (denominatorCondition.length() > 0) {
                //calculating denominator value
                denominatorValue = getValue(indCode, adjustmentType, currCode, orgIds, orgGroupId, startDate, endDate, teamMember, denominatorCondition, locationIds, showOnlyApprovedActivities);
            }
            if (denominatorValue != null && denominatorValue != 0 && nominatorValue != null) {
                total = Math.round(nominatorValue / denominatorValue * 100);
            }



        } catch (Exception e) {
            logger.error("Unable get value ", e);

        }

        return total;
    }

    /**
     *
     * @param questionNumber
     * @param indId
     * @param adjustmentType
     * @param currCode
     * @param orgId
     * @param year
     * @return
     */
    public static Long getDonorsCount(int questionNumber[], Long[] orgIds, Long orgGroupId, Date startDate, Date endDate, TeamMember member, Collection<Long> locationIds, boolean showOnlyApprovedActivities) {
        long size = 0;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "";
            String whereCondition = getAmpAhsurveyCondition(questionNumber);
            boolean locationCondition = locationIds != null && !locationIds.isEmpty();
            Query qry = null;
            if (whereCondition.length() > 0) {
                queryString = "select  distinct f.ampDonorOrgId from " + AmpAhsurvey.class.getName() + " ah inner join ah.responses res  " + " inner join res.ampQuestionId  q  "
                        + " inner join ah.ampActivityId act   " + " inner join act.funding f   " + " inner join  f.fundingDetails fd   ";
                if (locationCondition) {
                    queryString += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
                }

                queryString += "  where fd.transactionType =1 and  fd.adjustmentType =1"
                        + " and fd.transactionDate>=:startDate and  fd.transactionDate<=:endDate ";
                queryString += " and ah.ampAHSurveyId in (" + whereCondition + ")";
                if (locationCondition) {
                    queryString += " and loc.id in (:locations) ";

                }
                if (orgIds == null) {
                    if (orgGroupId != null && orgGroupId != -1) {
                        queryString += " and ah.ampDonorOrgId.orgGrpId=:orgGroupId ";
                    }
                } else {
                    queryString += " and ah.ampDonorOrgId in (" + ChartWidgetUtil.getInStatment(orgIds) + ")";
                }
                
                if (showOnlyApprovedActivities) {
					queryString += ActivityUtil.getApprovedActivityQueryString("act");
				}
                
                queryString += ChartWidgetUtil.getTeamQuery(member);
                qry = session.createQuery(queryString);
                qry.setDate("startDate", startDate);
                qry.setDate("endDate", endDate);
                if (orgIds == null && orgGroupId != null && orgGroupId != -1) {
                    qry.setLong("orgGroupId", orgGroupId);
                }
                if (locationCondition) {
                    qry.setParameterList("locations", locationIds);
                }
                size = qry.list().size();
            }

        } catch (Exception e) {
            logger.error("Unable get value ", e);

        }

        return size;
    }

    /**
     *
     * @param questionNumber
     * @param indId
     * @param adjustmentType
     * @param currCode
     * @param orgId
     * @param year
     * @return
     */
    public static long getPIUValue(Long[] orgIds, Long orgGroupId, Date startDate, Date endDate, TeamMember member, Collection<Long> locationIds, boolean showOnlyApprovedActivities) {
        long size = 0;
        boolean locationCondition = locationIds != null && !locationIds.isEmpty();
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "select  distinct act ";

            queryString += " from " + AmpAhsurvey.class.getName() + " ah inner join ah.responses res  " + " inner join res.ampQuestionId  q  "
                    + " inner join ah.ampActivityId act   " + " inner join act.funding f   " + " inner join  f.fundingDetails fd   ";
            if (locationCondition) {
                queryString += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
            }

            queryString += " where " + " fd.transactionType =1 and  fd.adjustmentType =1"
                    + " and act.actualStartDate>=:startDate and  act.actualCompletionDate<=:endDate  ";
            queryString += ChartWidgetUtil.getTeamQuery(member);
            queryString += " and res.response='Yes' and q.questionNumber=9";
            if (locationCondition) {
                queryString += " and loc.id in (:locations) ";

            }

            if (orgIds == null) {
                if (orgGroupId != null && orgGroupId != -1) {
                    queryString += " and ah.ampDonorOrgId.orgGrpId=:orgGroupId ";
                }

            } else {
                queryString += " and ah.ampDonorOrgId in (" + ChartWidgetUtil.getInStatment(orgIds) + ")";
            }
            
            if (showOnlyApprovedActivities) {
				queryString += ActivityUtil.getApprovedActivityQueryString("act");
			}
            
            Query qry = session.createQuery(queryString);
            qry.setDate("startDate", startDate);
            qry.setDate("endDate", endDate);
            if (orgIds == null && orgGroupId != null && orgGroupId != -1) {
                qry.setLong("orgGroupId", orgGroupId);
            }
            if (locationCondition) {
                qry.setParameterList("locations", locationIds);
            }
            size = qry.list().size();
        } catch (Exception e) {
            logger.error("Unable get value ", e);

        }

        return size;
    }

    /**
     *
     * @param questionNumber
     * @param indId
     * @param adjustmentType
     * @param currCode
     * @param orgId
     * @param year
     * @return
     */
    public static Double getQ4Value(Long ampAhsurveyId) {
        Double value = null;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "select  res.response" + " from " + AmpAhsurveyResponse.class.getName() + " res inner join res.ampQuestionId  q  ";


            queryString += " where q.questionNumber=4 and res.ampAHSurveyId=:ampAHSurveyId";

            Query qry = session.createQuery(queryString);
            qry.setLong("ampAHSurveyId", ampAhsurveyId);
            String val = (String) qry.uniqueResult();
            if (val != null && !val.equals("")) {
                value = Double.parseDouble(val);
            } else {
                value = new Double(0);
            }


        } catch (Exception e) {
            logger.error("Unable get value ", e);

        }

        return value;
    }

    /**
     *
     * @param questionNumber
     * @param indId
     * @param adjustmentType
     * @param currCode
     * @param orgId
     * @param year
     * @return
     */
    public static long getIndicator10aValue(Date startDate, Date endDate, Long[] orgIds, Long orgGroupId) {
        long value = 0;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "select  distinct cal  from " + AmpCalendar.class.getName() + " cal inner join cal.eventsType  type " + " left join cal.organisations org "
                    + " where (cal.calendarPK.calendar.startDate>=:startDate and cal.calendarPK.calendar.endDate<=:endDate) " + " and type.value='Mission' "; //I think we need made changes in db structure

            if (orgIds != null) {
                queryString += " and (";
                for (Long orgId : orgIds) {
                    queryString += " ( " + orgId + " in elements(cal.organisations)) or";
                }
                // cutting last or
                queryString = queryString.substring(0, queryString.length() - 2);
                queryString += ")";
            } else {
                if (orgGroupId != null && orgGroupId != -1) {
                    queryString += " and org.orgGrpId=:orgGroupId";
                }
            }
            Query qry = session.createQuery(queryString + " and size(cal.organisations)>1 "); //joint
            qry.setDate("startDate", startDate);
            qry.setDate("endDate", endDate);
            if (orgIds == null && orgGroupId != null && orgGroupId != -1) {
                qry.setLong("orgGroupId", orgGroupId);
            }
            long jointMisssion = qry.list().size();
            qry = session.createQuery(queryString); // all missions
            qry.setDate("startDate", startDate);
            qry.setDate("endDate", endDate);
            if (orgIds == null && orgGroupId != null && orgGroupId != -1) {
                qry.setLong("orgGroupId", orgGroupId);
            }
            long allMisssion = qry.list().size();
            if (allMisssion > 0) {
                value = jointMisssion / allMisssion;
            }


        } catch (Exception e) {
            logger.error("Unable get value ", e);
            e.printStackTrace();
        }

        return value;
    }

    /**
     * Returns list of 5 (or less) largest projects
     * TODO review this method
     * @param filter
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    public static List<Project> getOrganisationLargestProjects(FilterHelper filter) throws DgException {
        Session session = null;
        String queryString = null;
        TeamMember teamMember = filter.getTeamMember();
        List<Project> projects = new ArrayList<Project>();
        Integer maxResult = filter.getLargestProjectNumb();

        Long currId = filter.getCurrId();
        String currCode;
        Long orgGroupId = filter.getOrgGroupId();
        if (currId == null) {
            currCode = "USD";
        } else {
            currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
        }
        Long[] orgIds = filter.getOrgIds();
        // apply calendar filter
        Date startDate = filter.getStartDate();
        Date endDate = filter.getEndDate();
        Collection<Long> locationIds = filter.getLocationIds();
        boolean locationCondition = locationIds != null && locationIds.size() > 0;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = " select act from " + AmpActivity.class.getName() + " act  ";

            queryString += " inner join act.funding f "
                    + " inner join f.fundingDetails fd ";


            if (locationCondition) {
                queryString += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
            }

            queryString += "  where "
                    + " fd.transactionType = 0 and  fd.adjustmentType = 1";

            if (locationCondition) {
                queryString += " and loc.id in (:locations) ";

            }

            if (filter.getShowOnlyApprovedActivities()) {
				queryString += ActivityUtil.getApprovedActivityQueryString("act");
			}

            if (orgIds == null) {
                if (orgGroupId != -1) {
                    queryString += ChartWidgetUtil.getOrganizationQuery(true, orgIds);
                }
            } else {
                queryString += ChartWidgetUtil.getOrganizationQuery(false, orgIds);
            }
            queryString += " and fd.transactionDate>=:startDate and  fd.transactionDate<=:endDate  ";
            
            if(filter.getFromPublicView()){
                queryString += ChartWidgetUtil.getTeamQueryManagement();
            }
            else
            {
                queryString += ChartWidgetUtil.getTeamQuery(teamMember);
            }
            
            queryString += " group by act.ampActivityId order by sum(fd.transactionAmountInBaseCurrency) desc ";

            Query query = session.createQuery(queryString);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            query.setMaxResults(maxResult);
            if (orgIds == null && orgGroupId != -1) {
                query.setLong("orgGroupId", orgGroupId);
            }
            if (locationCondition) {
                query.setParameterList("locations", locationIds);
            }
            List result = query.list();


            Iterator<AmpActivity> activityIter = result.iterator();
            // converting funding to selected currency amount and creating projects
            while (activityIter.hasNext()) {
                AmpActivity activity = activityIter.next();

                if (locationCondition) {
                    queryString = " select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actloc.locationPercentage,fd.fixedExchangeRate) ";
                } else {
                    queryString = "select fd ";
                }
                queryString += " from ";
                queryString += AmpFundingDetail.class.getName() + " fd  inner join fd.ampFundingId f ";
                queryString += "   inner join f.ampActivityId act ";
                if (locationCondition) {
                    queryString += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
                }
                queryString += " where    fd.adjustmentType = 1  ";
                if (orgIds == null) {
                    if (orgGroupId != -1) {
                        queryString += ChartWidgetUtil.getOrganizationQuery(true, orgIds);
                    }
                } else {
                    queryString += ChartWidgetUtil.getOrganizationQuery(false, orgIds);
                }
                queryString += " and fd.transactionDate>=:startDate and  fd.transactionDate<=:endDate  and act=" + activity.getAmpActivityId();
                if (locationCondition) {
                    queryString += " and loc.id in (:locations) ";
                }
                query = session.createQuery(queryString);
                query.setDate("startDate", startDate);
                query.setDate("endDate", endDate);
                if (orgIds == null && orgGroupId != -1) {
                    query.setLong("orgGroupId", orgGroupId);
                }
                if (locationCondition) {
                    query.setParameterList("locations", locationIds);
                }
                List<AmpFundingDetail> details = query.list();
                Project project = new Project();
                Set<AmpActivitySector> sectors = activity.getSectors();
                Iterator<AmpActivitySector> sectorIter = sectors.iterator();
                project.setSectorNames(new ArrayList<String>());
                while (sectorIter.hasNext()) {
                    String sectorsName = "";
                    AmpActivitySector actSector = sectorIter.next();
                    AmpSector sector = actSector.getSectorId();
                    AmpSector parentSector = sector.getParentSectorId();
                    sectorsName += sector.getAmpSecSchemeId().getSecSchemeName() + " -> ";

                    if (parentSector != null) {
                        if (parentSector.getParentSectorId() != null) {
                            sectorsName += parentSector.getParentSectorId().getName() + " -> ";
                        }
                        sectorsName += parentSector.getName() + " -> ";
                    }
                    sectorsName += sector.getName();
                    project.getSectorNames().add(sectorsName);

                }

                //project.setSectorNames(sectorsName);
                FundingCalculationsHelper cal = new FundingCalculationsHelper();
                cal.doCalculations(details, currCode);

                BigDecimal amount = cal.getTotActualComm().getValue();
                project.setAmount(FormatHelper.formatNumber(amount));
                if (filter.getTransactionType() == 2) { // we are showing disb only when comm&disb is selected
                    BigDecimal disbAmount = cal.getTotActualDisb().getValue();
                    project.setDisbAmount(FormatHelper.formatNumber(disbAmount));
                }

                String title = activity.getName();
                if (title.length() > 150) {
                    project.setFullTitle(title);
                    title = title.substring(0, 150) + "...";
                }
                project.setTitle(title);
                project.setActivityId(activity.getAmpActivityId());
                projects.add(project);

            }
        } catch (Exception e) {
            logger.error("error", e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }


        return projects;
    }

    public static Date getStartDate(Long fiscalCalendarId, int year) {
        Date startDate = null;
        if (fiscalCalendarId != null && fiscalCalendarId != -1) {
            AmpFiscalCalendar calendar = FiscalCalendarUtil.getAmpFiscalCalendar(fiscalCalendarId);
            if (calendar.getBaseCal().equalsIgnoreCase("GREG-CAL")) {
                startDate = getStartOfYear(year, calendar.getStartMonthNum() - 1, calendar.getStartDayNum());
            } else {      
                 startDate = getGregorianCalendarDate(calendar, year, true);
            }
        } else {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.YEAR, year);
            startDate = cal.getTime();
        }
        return startDate;
    }

    public static Date getEndDate(Long fiscalCalendarId, int year) {
        Date endDate = null;
        if (fiscalCalendarId != null && fiscalCalendarId != -1) {
            AmpFiscalCalendar calendar = FiscalCalendarUtil.getAmpFiscalCalendar(fiscalCalendarId);
            if (calendar.getBaseCal().equalsIgnoreCase("GREG-CAL")) {
                //we need data including the last day of toYear,this is till the first day of toYear+1
                int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
                endDate = new Date(getStartOfYear(year + 1, calendar.getStartMonthNum() - 1, calendar.getStartDayNum()).getTime() - MILLISECONDS_IN_DAY);
            } else {
                endDate=getGregorianCalendarDate(calendar, year, false);
            }

        } else {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.YEAR, year + 1);
            endDate = cal.getTime();
        }
        return endDate;
    }

    public static Date getStartOfYear(int year, int month, int day) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(year, month, day, 0, 0, 0);
        return cal.getTime();
    }

    

    /**
     * Creates string consisting of surveys ids
     * @param questionNumbers
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    public static String getAmpAhsurveyCondition(int[] questionNumbers) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        String queryString = "";
        String whereCondition = "";
        Query qry = null;
        List<AmpAhsurvey> surveys = new ArrayList<AmpAhsurvey>();
        List<AmpAhsurvey> selectedSurveys = new ArrayList<AmpAhsurvey>();
        for (int i = 0; i < questionNumbers.length; i++) {
            queryString = " select ah from " + AmpAhsurvey.class.getName() + " ah ";
            // not all  actual disbursement
            if (questionNumbers[0] != 0) {
                queryString += " inner join ah.responses res  " + " inner join res.ampQuestionId  q  ";
                queryString += " where  res.response='Yes'" + " and q.questionNumber=" + questionNumbers[i] + ") ";
            }
            qry = session.createQuery(queryString);
            List surveyList = qry.list();
            if (questionNumbers.length == 1) {
                surveys.addAll(surveyList);

            } else {
                if (surveyList.size() > 0) {
                    if (i == 0) {
                        surveys.addAll(surveyList);
                    } else {
                        Iterator<AmpAhsurvey> iter = surveyList.iterator();
                        while (iter.hasNext()) {
                            AmpAhsurvey survey = iter.next();
                            if (surveys.contains(survey)) {
                                selectedSurveys.add(survey);
                            }
                        }
                        surveys.clear();
                        surveys.addAll(selectedSurveys);
                        selectedSurveys.clear();

                    }

                } else {
                    surveys.clear();
                }

            }

        }
        Iterator<AmpAhsurvey> iter = surveys.iterator();
        while (iter.hasNext()) {
            if (whereCondition.length() != 0) {
                whereCondition += ",";
            }
            AmpAhsurvey survey = iter.next();
            whereCondition += survey.getAmpAHSurveyId();
        }
        return whereCondition;
    }

    /**
     * Returns funding amount for the specified surveys
     * @param indCode
     * @param adjustmentType
     * @param currCode
     * @param orgId
     * @param orgGroupId
     * @param startDate
     * @param endDate
     * @param teamMember
     * @param condition
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    public static Double getValue(String indCode, int adjustmentType, String currCode, Long[] orgIds, Long orgGroupId, Date startDate, Date endDate, TeamMember teamMember, String condition, Collection<Long> locationIds, boolean showOnlyApprovedActivities) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        String queryString = "";
        Double total = new Double(0);
        boolean locationCondition = locationIds != null && !locationIds.isEmpty();
        Query qry = null;
        queryString = "select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,";
        queryString += "fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,fd.fixedExchangeRate";
        if (indCode.equals("4")) {
            queryString += ", ah.ampAHSurveyId";
        }
        queryString += ") from "
                + AmpAhsurvey.class.getName()
                + " ah inner join ah.responses res  " + " inner join ah.ampActivityId act   " + " inner join act.funding f   " + " inner join  f.fundingDetails fd   ";
        if (locationCondition) {
            queryString += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        }
        queryString += " where  fd.transactionType =1 and  fd.adjustmentType =:adjustmentType"
                + "  and fd.transactionDate>=:startDate and  fd.transactionDate<=:endDate ";
        queryString += ChartWidgetUtil.getTeamQuery(teamMember);

        if (orgIds == null) {
            if (orgGroupId != null && orgGroupId != -1) {
                queryString += " and ah.ampDonorOrgId.orgGrpId=:orgGroupId ";
            }
        } else {
            queryString += " and ah.ampDonorOrgId in (" + ChartWidgetUtil.getInStatment(orgIds) + ") ";
        }

        if (showOnlyApprovedActivities) {
			queryString += ActivityUtil.getApprovedActivityQueryString("act");
		}
        
        // specified survyes
        queryString += " and ah.ampAHSurveyId in (" + condition + ")";
        if (locationCondition) {
            queryString += " and loc.id in (:locations) ";
        }

        qry = session.createQuery(queryString);
        qry.setDate("startDate", startDate);
        qry.setDate("endDate", endDate);
        qry.setInteger("adjustmentType", adjustmentType);
        if (orgIds == null && orgGroupId != null && orgGroupId != -1) {
            qry.setLong("orgGroupId", orgGroupId);
        }
        if (locationCondition) {
            qry.setParameterList("locations", locationIds);
        }
        List<AmpFundingDetail> fundingDets = qry.list();
        FundingCalculationsHelper cal = new FundingCalculationsHelper();
        cal.doCalculations(fundingDets, currCode);
        DecimalWraper tot = null;
        if (adjustmentType == 1) {
            tot = cal.getTotActualDisb();
        } else {
            tot = cal.getTotPlanDisb();
        }
        total = tot.doubleValue();
        return total;
    }

   

  

    public static Date getGregorianCalendarDate(AmpFiscalCalendar fiscalCalendar, int year, boolean startDate) {
        Date date;
        fi.joensuu.joyds1.calendar.Calendar calendar = getCalendar(fiscalCalendar, startDate, year);
        Calendar gregorianCal = calendar.toJavaUtilGregorianCalendar();
        date = gregorianCal.getTime();
        return date;
    }
     public static fi.joensuu.joyds1.calendar.Calendar getCalendar(AmpFiscalCalendar fiscalCalendar, boolean startDate, int year) {
        fi.joensuu.joyds1.calendar.Calendar calendar = null;
        String calendarType = fiscalCalendar.getBaseCal();
        if (calendarType.equals("ETH-CAL")) {
            calendar = new EthiopicCalendar();
        } else {
            if (calendarType.equals("NEP-CAL")) {
                calendar = new NepaliCalendar();
            }
        }
        if (startDate) {
            calendar.set(year, fiscalCalendar.getStartMonthNum(), fiscalCalendar.getStartDayNum());
        } else {
            calendar.set(year + 1, fiscalCalendar.getStartMonthNum(), fiscalCalendar.getStartDayNum());
            calendar.addDays(-1);
        }
        return calendar;
    }

  
    public static void saveAdditionalInfo(Long orgId, String orgBackground,String orgDescription) throws DgException{
        Session sess = null;
        Transaction tx = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            tx = sess.beginTransaction();
            AmpOrganisation org = (AmpOrganisation) sess.get(AmpOrganisation.class, orgId);
            org.setOrgBackground(orgBackground);
            org.setOrgDescription(orgDescription);
            sess.update(org);
            tx.commit();
        } catch (Exception e) {
            logger.error("Unable to update", e);
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException ex) {
                    logger.error("rollback() failed", ex);
                }
            }
              throw new DgException(e);
        }

    }

	public static long getParisIndicator10bValue(Long year,
			List<NodeWrapper> nodeWrappers, Long[] orgIds, Long groupId)
			throws DgException {
		long value = 0;
		List <AmpOrganisation> filteredOrgs=new ArrayList<AmpOrganisation>();
		int jointDonorCount = 0, donorCount = 0;
		if (orgIds != null) {
			for (Long orgId : orgIds) {
				AmpOrganisation org = DbUtil.getOrganisation(orgId);
				String orgCode = org.getOrgGrpId().getOrgType()
						.getOrgTypeCode();
				if (orgCode.equals(PIConstants.ORG_GRP_BILATERAL)||orgCode.equals(PIConstants.ORG_GRP_MULTILATERAL)) {
					filteredOrgs.add(org);
				}
			}
		}

		else {
			if (groupId != null && groupId != -1) {
				AmpOrgGroup orgGroup = DbUtil.getAmpOrgGroup(groupId);
				String orgCode = orgGroup.getOrgType().getOrgTypeCode();
				if (!orgCode.equals(PIConstants.ORG_GRP_BILATERAL)
						&& orgCode.equals(PIConstants.ORG_GRP_MULTILATERAL)) {
					return value;
				}
				else{
					filteredOrgs.addAll(DbUtil.getOrgByGroup(groupId));
				}
			}
		}
		for (NodeWrapper nextWrapper : nodeWrappers) {
				String yearOfPublication = nextWrapper.getYearOfPublication();
				if (yearOfPublication==null||!year.equals(Long.parseLong(yearOfPublication))) {
					continue;
				}
				// Check document type.
				AmpCategoryValue docType = CategoryManagerUtil
						.getAmpCategoryValueFromDb(
								nextWrapper.getCmDocTypeId(), true);
				if (docType != null) {
					if (docType.getValue().equalsIgnoreCase(
									CategoryConstants.RESOURCE_TYPE_COUNTRY_ANALYTIC_REPORT_KEY)) {

						Collection<AmpOrganisation> auxOrganizations = DocToOrgDAO
								.getOrgsObjByUuid(nextWrapper.getUuid());
						Iterator<AmpOrganisation> iterOrgs = auxOrganizations
								.iterator();
						if(filteredOrgs.size()>0){
							while (iterOrgs.hasNext()) {
								AmpOrganisation auxOrganisation = iterOrgs.next();
								if(filteredOrgs.contains(auxOrganisation)){
									donorCount++;
									if(auxOrganizations.size()>1){
										jointDonorCount++;
									}
									break;
								}
							}	
						}
						else{
							donorCount++;
							if(auxOrganizations.size()>1){
								jointDonorCount++;
							}
						}
					}
				}
			}

		if(donorCount!=0){
			value=Math.round(jointDonorCount*1.0/donorCount*100);
		}

		return value;

	}

	public static List<NodeWrapper> getNodeWrappers(HttpServletRequest request,
			Long teamId, boolean fromPublicView) throws DgException {
		List<NodeWrapper> nodeWrappers = new ArrayList<NodeWrapper>();
		if (fromPublicView) {
			// get public documents
			HashMap<String, CrDocumentNodeAttributes> uuidMap = CrDocumentNodeAttributes
					.getPublicDocumentsMap(false);
			Collection<String> uuidKeys = uuidMap.keySet();
			for (String uuidKey : uuidKeys) {
				NodeWrapper nextWrapper = DocumentManagerUtil
						.getReadNodeWrapper(uuidKey, request);
				if (nextWrapper != null) {
					nodeWrappers.add(nextWrapper);
				}
			}
		} else {
			List<Node> teamNodes = new ArrayList<Node>();
			javax.jcr.Session jcrReadSession = DocumentManagerUtil
					.getReadSession(request);

			if (teamId != null) {
				// Get the main team node for this team.
				Node teamNode = DocumentManagerUtil.getTeamNode(jcrReadSession,
						teamId);
				teamNodes.add(teamNode);
			} else {
				Collection<AmpTeam> teams = TeamUtil.getAllTeams();
				Iterator<AmpTeam> teamIter = teams.iterator();
				while (teamIter.hasNext()) {
					AmpTeam team = teamIter.next();
					// Get the main team node for each team .
					Node teamNode = DocumentManagerUtil.getTeamNode(
							jcrReadSession, team.getAmpTeamId());
					teamNodes.add(teamNode);

				}
			}
			for (Node teamNode : teamNodes) {
				Iterator<Node> iterNode;
				try {
					iterNode = teamNode.getNodes();
				} catch (RepositoryException e) {
					logger.error("RepositoryException ", e);
					throw new DgException(e);
				}
				while (iterNode.hasNext()) {
					Node nextNode = (Node) iterNode.next();
					NodeWrapper nextWrapper = new NodeWrapper(nextNode);
					nodeWrappers.add(nextWrapper);
				}
			}
		}
		DocumentManagerUtil.logoutJcrSessions(request.getSession());
		return nodeWrappers;
	}
}
