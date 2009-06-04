package org.digijava.module.orgProfile.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import java.util.Set;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.orgProfile.helper.Project;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.widget.dbentity.AmpWidgetOrgProfile;
import org.digijava.module.aim.util.ActivityUtil;

/**
 *
 * @author medea
 */
public class OrgProfileUtil {

    private static Logger logger = Logger.getLogger(OrgProfileUtil.class);

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
    public static Long getValue(String indCode,  String currCode, Long orgId, Long orgGroupId, Date startDate, Date endDate, TeamMember teamMember) throws DgException {
        Long total = 0l;
        try {
            // questions that must be answered with 'yes' in the survey, nominator column
            int nominator[] = null;
             // questions that must be answered with 'yes' in the survey, denominator column
            int denominator[] = null;
            int adjustmentType=Constants.ACTUAL;
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
                                total = getPIUValue(orgId, orgGroupId, startDate, endDate, teamMember);
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
                                           total=getIndicator10aValue(startDate, endDate, orgId, orgGroupId);
                                           return total;
                                        } else {
                                            if (indCode.equals("5aii")) {
                                                nominator = new int[]{1, 5, 6, 7};
                                                total = getDonorsCount(nominator, orgId, orgGroupId, startDate, endDate, teamMember);
                                                return total;
                                            } else {
                                                if (indCode.equals("5bii")) {
                                                    nominator = new int[]{8, 1};
                                                    total = getDonorsCount(nominator, orgId, orgGroupId, startDate, endDate, teamMember);
                                                    return total;
                                                }
                                                else{
                                                     if (indCode.equals("10b")||indCode.equals("8")) {
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
                    nominatorValue = getValue(indCode, Constants.PLANNED, currCode, orgId, orgGroupId, startDate, endDate, teamMember, nominatorCondition);
                }
                else{
                    //calculating  actual disb.
                      nominatorValue = getValue(indCode, adjustmentType, currCode, orgId, orgGroupId, startDate, endDate, teamMember, nominatorCondition);
                }
            }
            if (denominatorCondition.length() > 0) {
                //calculating denominator value
                denominatorValue = getValue(indCode, adjustmentType, currCode, orgId, orgGroupId, startDate, endDate, teamMember, denominatorCondition);
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
    public static Long getDonorsCount(int questionNumber[], Long orgId, Long orgGroupId, Date startDate, Date endDate, TeamMember member) {
        long size = 0;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "";
            String whereCondition = getAmpAhsurveyCondition(questionNumber);
            Query qry = null;
            if (whereCondition.length() > 0) {
                queryString = "select  distinct f.ampDonorOrgId from " + AmpAhsurvey.class.getName() + " ah inner join ah.responses res  " + " inner join res.ampQuestionId  q  " +
                        " inner join ah.ampActivityId act   " + " inner join act.funding f   " + " inner join  f.fundingDetails fd   ";

                queryString += "  where fd.transactionType =1 and  fd.adjustmentType =1" +
                        " and fd.transactionDate>=:startDate and  fd.transactionDate<=:endDate ";
                queryString += " and ah.ampAHSurveyId in (" + whereCondition + ")";
                if (orgId == null || orgId == -1) {
                    if (orgGroupId != null && orgGroupId != -1) {
                        queryString += " and ah.ampDonorOrgId.orgGrpId=:orgGroupId ";
                    }
                } else {
                    queryString += " and ah.ampDonorOrgId=:orgId ";
                }
                queryString += ChartWidgetUtil.getTeamQuery(member);
                qry = session.createQuery(queryString);
                qry.setDate("startDate", startDate);
                qry.setDate("endDate", endDate);
                if (member != null) {
                    qry.setLong("teamId", member.getTeamId());

                }
                if (orgId == null || orgId == -1) {
                    if (orgGroupId != null && orgGroupId != -1) {
                        qry.setLong("orgGroupId", orgGroupId);
                    }
                } else {
                    qry.setLong("orgId", orgId);
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
    public static long getPIUValue(Long orgId, Long orgGroupId, Date startDate, Date endDate, TeamMember member) {
        long size = 0;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "select  distinct act ";

            queryString += " from " + AmpAhsurvey.class.getName() + " ah inner join ah.responses res  " + " inner join res.ampQuestionId  q  " +
                    " inner join ah.ampActivityId act   " + " inner join act.funding f   " + " inner join  f.fundingDetails fd   ";

            queryString += " where " + " fd.transactionType =1 and  fd.adjustmentType =1" +
                    " and act.actualStartDate>=:startDate and  act.actualCompletionDate<=:endDate  ";
            queryString += ChartWidgetUtil.getTeamQuery(member);
            queryString += " and res.response='Yes' and q.questionNumber=9";

            if (orgId == null || orgId == -1) {
                if (orgGroupId != null && orgGroupId != -1) {
                    queryString += " and ah.ampDonorOrgId.orgGrpId=:orgGroupId ";
                }
            } else {
                queryString += " and ah.ampDonorOrgId=:orgId ";
            }
            Query qry = session.createQuery(queryString);
            qry.setDate("startDate", startDate);
            qry.setDate("endDate", endDate);
            if (member != null) {
                qry.setLong("teamId", member.getTeamId());

            }
            if (orgId == null || orgId == -1) {
                if (orgGroupId != null && orgGroupId != -1) {
                    qry.setLong("orgGroupId", orgGroupId);
                }
            } else {
                qry.setLong("orgId", orgId);
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
    public static long getIndicator10aValue(Date startDate, Date endDate, Long orgId, Long orgGroupId) {
        long value = 0;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "select  distinct cal  from " + AmpCalendar.class.getName() + " cal inner join cal.eventType  type " + " left join cal.organisations org " +
                    " where (cal.calendarPK.calendar.startDate>=:startDate and cal.calendarPK.calendar.endDate<=:endDate) " + " and type.name='Mission' "; //I think we need made changes in db structure


            if (orgId != null && orgId != -1) {
                queryString += " and :orgId in elements(cal.organisations)";
            } else {
                if (orgGroupId != null && orgGroupId != -1) {
                    queryString += " and org.orgGrpId=:orgGroupId";
                }
            }
            Query qry = session.createQuery(queryString + " and size(cal.organisations)>1 "); //joint
            qry.setDate("startDate", startDate);
            qry.setDate("endDate", endDate);
            if (orgId != null && orgId != -1) {
                qry.setLong("orgId", orgId);
            } else {
                if (orgGroupId != null && orgGroupId != -1) {
                    qry.setLong("orgGroupId", orgGroupId);

                }
            }
            long jointMisssion = qry.list().size();
            qry = session.createQuery(queryString); // all missions
            qry.setDate("startDate", startDate);
            qry.setDate("endDate", endDate);
            if (orgId != null && orgId != -1) {
                qry.setLong("orgId", orgId);
            } else {
                if (orgGroupId != null && orgGroupId != -1) {
                    qry.setLong("orgGroupId", orgGroupId);

                }
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
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }
        year -= 1; // previous fiscal year
        Long currId = filter.getCurrId();
        String currCode;
        Long orgGroupId = filter.getOrgGroupId();
        if (currId == null) {
            currCode = "USD";
        } else {
            currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
        }
        Long orgID = filter.getOrgId();
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        // apply calendar filter
        Date startDate = getStartDate(fiscalCalendarId, year.intValue());
        Date endDate = getEndDate(fiscalCalendarId, year.intValue());
        try {
            session = PersistenceManager.getRequestDBSession();
            /* pick all activities of the organization in the selected year ordered
            by their amounts in USD
            alas that "Limit" does not work in the query...  */
            queryString = " select act.ampActivityId from " + AmpActivity.class.getName() + " act  ";

            queryString += " inner join act.funding f " +
                    " inner join f.fundingDetails fd ";

            queryString += "  where " +
                    " fd.transactionType = 0 and  fd.adjustmentType = 1";

            if (orgID == null || orgID == -1) {
                if (orgGroupId != null && orgGroupId != -1) {
                    queryString += ChartWidgetUtil.getOrganizationQuery(true);
                }
            } else {
                queryString += ChartWidgetUtil.getOrganizationQuery(false);
            }
            queryString += " and fd.transactionDate>=:startDate and  fd.transactionDate<=:endDate  ";
            queryString+=ChartWidgetUtil.getTeamQuery(teamMember);
            queryString +=" group by act.ampActivityId order by sum(fd.transactionAmountInUSD) desc ";

            Query query = session.createQuery(queryString);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (orgID != null && orgID != -1) {
                query.setLong("orgID", orgID);
            } else {
                if (orgGroupId != null && orgGroupId != -1) {
                    query.setLong("orgGroupId", orgGroupId);
                }
            }
            if (teamMember != null) {
                query.setLong("teamId", teamMember.getTeamId());

            }
            List result = query.list();
            if (result.size() > 5) {
                result = result.subList(0, 5);//pick 5 largest projects
            }


            Iterator<Long> activityIter = result.iterator();
            // converting funding to selected currency amount and creating projects
            while (activityIter.hasNext()) {
                Long activityId = activityIter.next();
                AmpActivity activity=ActivityUtil.getAmpActivity(activityId);
                queryString = "select fd from " + AmpFundingDetail.class.getName() + " fd  inner join fd.ampFundingId f ";
                queryString += "   inner join f.ampActivityId act  where   fd.transactionType = 0 and  fd.adjustmentType = 1  ";
                if (orgID == null || orgID == -1) {
                    if (orgGroupId != null && orgGroupId != -1) {
                        queryString += ChartWidgetUtil.getOrganizationQuery(true);
                    }
                } else {
                    queryString += ChartWidgetUtil.getOrganizationQuery(false);
                }
                queryString += " and fd.transactionDate>=:startDate and  fd.transactionDate<=:endDate  and act=" + activity.getAmpActivityId();
                query = session.createQuery(queryString);
                query.setDate("startDate", startDate);
                query.setDate("endDate", endDate);
                if (orgID != null && orgID != -1) {
                    query.setLong("orgID", orgID);
                } else {
                    if (orgGroupId != null && orgGroupId != -1) {
                        query.setLong("orgGroupId", filter.getOrgGroupId());
                    }
                }
                List<AmpFundingDetail> details = query.list();
                Project project = new Project();
                Set<AmpActivitySector> sectors = activity.getSectors();
                Iterator<AmpActivitySector> sectorIter = sectors.iterator();
                String sectorsName = "";
                while (sectorIter.hasNext()) {
                    sectorsName += " " + sectorIter.next().getSectorId().getName() + ",";
                }
                if (sectorsName.length() > 0) {
                    sectorsName = sectorsName.substring(0, sectorsName.length() - 1);
                }
                project.setSectorNames(sectorsName);
                FundingCalculationsHelper cal = new FundingCalculationsHelper();
                cal.doCalculations(details, currCode);

                Double amount = cal.getTotActualComm().doubleValue();

                project.setAmount(FormatHelper.formatNumber(amount));
                String title = activity.getName();
                if (title.length() > 15) {
                    project.setFullTitle(title);
                    title = title.substring(0, 14) + "...";
                }
                project.setTitle(title);
                project.setActivityId(activity.getAmpActivityId());
                projects.add(project);

            }
        } catch (Exception e) {
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }


        return projects;
    }

    public static Date getStartDate(Long fiscalCalendarId, int year) {
        Date startDate = null;
        if (fiscalCalendarId != null && fiscalCalendarId != -1) {
            startDate = FiscalCalendarUtil.getCalendarStartDate(fiscalCalendarId, year);

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
        Date startDate = null;
        if (fiscalCalendarId != null && fiscalCalendarId != -1) {
            startDate = FiscalCalendarUtil.getCalendarEndDate(fiscalCalendarId, year);

        } else {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, Calendar.DECEMBER);
            cal.set(Calendar.DAY_OF_MONTH, 31);
            cal.set(Calendar.YEAR, year);
            startDate = cal.getTime();
        }
        return startDate;
    }

    /**
     * validation method that only allows you to add a chart or table type once
     * @param type
     * @param widgetOrgId
     * @return
     */
    public static boolean widgetTypeExists(Long type, Long widgetOrgId) {
        boolean exists = true;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "select  widOrg from " + AmpWidgetOrgProfile.class.getName() + " widOrg where widOrg.type=:type ";
            if (widgetOrgId != null) {
                queryString += " and widOrg.id!=:widgetOrgId";
            }
            Query qry = session.createQuery(queryString);
            qry.setLong("type", type);
            if (widgetOrgId != null) {
                qry.setLong("widgetOrgId", widgetOrgId);
            }
            if (qry.list().size() == 0) {
                exists = false;
            }
        } catch (DgException ex) {
            logger.error("Unable get value ", ex);

        }


        return exists;
    }
    /**
     * Creates string consisting of surveys ids
     * @param questionNumbers
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    public static String getAmpAhsurveyCondition(int[] questionNumbers) throws DgException{
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "";
            String whereCondition = "";
            Query qry = null;
            List<AmpAhsurvey> surveys = new ArrayList<AmpAhsurvey>();
            List<AmpAhsurvey> selectedSurveys = new ArrayList<AmpAhsurvey>();
            for (int i = 0; i < questionNumbers.length; i++) {
                queryString = " select ah from " + AmpAhsurvey.class.getName()+" ah ";
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
            return whereCondition ;
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
    public static Double getValue(String indCode, int adjustmentType, String currCode, Long orgId, Long orgGroupId, Date startDate, Date endDate, TeamMember teamMember, String condition) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        String queryString = "";
        Double total = new Double(0);
        Query qry = null;
        queryString = "select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,";
        queryString += "fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,fd.fixedExchangeRate";
        if (indCode.equals("4")) {
            queryString += ", ah.ampAHSurveyId";
        }
        queryString += ") from " +
                AmpAhsurvey.class.getName() +
                " ah inner join ah.responses res  " + " inner join ah.ampActivityId act   " + " inner join act.funding f   " + " inner join  f.fundingDetails fd   ";
        queryString += " where  fd.transactionType =1 and  fd.adjustmentType =:adjustmentType" +
                "  and fd.transactionDate>=:startDate and  fd.transactionDate<=:endDate ";
        queryString += ChartWidgetUtil.getTeamQuery(teamMember);

        if (orgId == null || orgId == -1) {
            if (orgGroupId != null && orgGroupId != -1) {
                queryString += " and ah.ampDonorOrgId.orgGrpId=:orgGroupId ";
            }
        } else {
            queryString += " and ah.ampDonorOrgId=:orgId ";
        }

        // specified survyes
        queryString += " and ah.ampAHSurveyId in (" + condition + ")";

        qry = session.createQuery(queryString);
        qry.setDate("startDate", startDate);
        qry.setDate("endDate", endDate);
        if (teamMember != null) {
            qry.setLong("teamId", teamMember.getTeamId());

        }
        qry.setInteger("adjustmentType", adjustmentType);
        if (orgId == null || orgId == -1) {
            if (orgGroupId != null && orgGroupId != -1) {
                qry.setLong("orgGroupId", orgGroupId);
            }
        } else {
            qry.setLong("orgId", orgId);
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


}
