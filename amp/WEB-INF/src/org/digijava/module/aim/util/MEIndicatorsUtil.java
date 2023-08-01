package org.digijava.module.aim.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.AllActivities;
import org.digijava.module.aim.helper.AllMEIndicators;
import org.digijava.module.aim.helper.Constants;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Deprecated
public class MEIndicatorsUtil
{
    private static Logger logger = Logger.getLogger(MEIndicatorsUtil.class);

    public static Collection getAllActivityIds()
    {
        Session session = null;
        Query qry = null;
        Collection actIdCol = null;
        Collection<AllActivities> actInds = new ArrayList<AllActivities>();
        try
        {
            session = PersistenceManager.getSession();
            String queryString = "select distinct actInd.activityId from "
                                + AmpMEIndicatorValue.class.getName() + " actInd";
            qry = session.createQuery(queryString);
            actIdCol = qry.list();
            Iterator itrIds = actIdCol.iterator();
            while(itrIds.hasNext())
            {
                AmpActivity ampAct = (AmpActivity) itrIds.next();
                AllActivities actList = new AllActivities();
                actList.setActivityId(ampAct.getAmpActivityId());
                actList.setActivityName(ampAct.getName());
                actInds.add(getIndicatorsActivity(actList));
            }
        }
        catch(Exception ex)
        {
            logger.error("Unable to get all Indicators of Activities");
            logger.debug("Exception " + ex);
        }
        return actInds;
    }

    public static AllActivities getIndicatorsActivity(AllActivities actList)
    {
        Session session = null;
        Collection tempCol = new ArrayList();
        Long actId = actList.getActivityId();
        try
        {
            session = PersistenceManager.getSession();
            String qryStr = "select indVal from " + AmpMEIndicatorValue.class.getName() +
                    " indVal where (indVal.activityId=:actId)" ;
            Query qry = session.createQuery(qryStr);
            qry.setParameter("actId",actId,LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext())
            {
                AmpMEIndicatorValue meIndValue = (AmpMEIndicatorValue) itr.next();
                AmpMEIndicators ampMEInd = (AmpMEIndicators) session.load(AmpMEIndicators.class,meIndValue.getMeIndicatorId().getAmpMEIndId());
                AllMEIndicators allMEInd = new AllMEIndicators();
                allMEInd.setAmpMEIndId(ampMEInd.getAmpMEIndId());
                allMEInd.setName(ampMEInd.getName());
                allMEInd.setCode(ampMEInd.getCode());
                allMEInd.setDefaultInd(ampMEInd.isDefaultInd());
                allMEInd.setAscendingInd(ampMEInd.isAscendingInd());
                tempCol.add(allMEInd);
            }
            actList.setAllMEIndicators(tempCol);
        }
        catch (Exception e)
        {
            logger.error("Exception from getIndicatorsActivity() :" + e.getMessage());
            e.printStackTrace(System.out);
        }
        return actList;
    }



    public static Collection getMeIndValIds(Long meIndicatorId)
    {
        Session session = null;
        Collection col = null;
        try
        {
            session = PersistenceManager.getSession();
            String queryString = "select ampMeIndValId from "
                + AmpMEIndicatorValue.class.getName()
                + " ampMeIndValId where (ampMeIndValId.meIndicatorId=:meIndicatorId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("meIndicatorId",meIndicatorId,LongType.INSTANCE);
            col = qry.list();
        }
        catch(Exception exp)
        {
            logger.debug("UNABLE to find meIndicatorValueIds for given meIndicatorId.", exp);
        }
        return col;
    }

    public static Collection getMeCurrValIds(Long meIndValue)
    {
        Session session = null;
        Collection col = null;

        try
        {
            session = PersistenceManager.getSession();
            String queryString = "select ampMECurrValHistoryId from "
                                +AmpMECurrValHistory.class.getName()
                                +" ampMECurrValHistoryId where (ampMECurrValHistoryId.meIndValue=:meIndValue)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("meIndValue",meIndValue,LongType.INSTANCE);
            col = qry.list();
        }
        catch(Exception e1)
        {
            logger.debug("UNABLE to find meIndicatorCurrValIds for given meIndValId", e1);
        }
        return col;
    }

        public static Long getIndicatorsForActivity(Long actId,String name)
{
        Session session = null;
        Long id =null;
        String qryStr = null;
        Query qry = null;

        try {
                session = PersistenceManager.getRequestDBSession();
                if (actId != null) {
                        qryStr = "select me.indicatorId from " +
                            AmpMEIndicatorValue.class.getName() + " " +
                            "indVal inner join indVal.indicator me where (indVal.activityId=:actId) and me.name=:name" ;
                        qry = session.createQuery(qryStr);
                        qry.setLong("actId",actId);
                        qry.setString("name",name);
                        if(qry!=null){
                                Iterator <Long> meIter= qry.list().iterator();
                                if(meIter.hasNext()){
                                     id=meIter.next();
                                }
                        }

                }
        }
        catch (Exception e) {
                        logger.error("Exception from getActivityIndicators() :" + e.getMessage());
                        e.printStackTrace(System.out);
                }
                return id;
        }


    public static void deleteMEIndicatorValues(Long indValId) {
        Session session = null;
//      Transaction tx = null;
        try {
            session = PersistenceManager.getSession();
            AmpMEIndicatorValue meIndVal = (AmpMEIndicatorValue) session.load(
                    AmpMEIndicatorValue.class,indValId);

            String qryStr = "select meCh from " + AmpMECurrValHistory.class.getName() + "" +
                    " meCh where (meCh.meIndValue=:indVal)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("indVal",indValId,LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
//beginTransaction();
            while (itr.hasNext()) {
                AmpMECurrValHistory currValHist = (AmpMECurrValHistory) itr.next();
                session.delete(currValHist);
            }
            session.delete(meIndVal);
            //tx.commit();

        } catch (Exception e) {
            logger.error("Exception from saveMEIndicatorValues() :" + e.getMessage());
            e.printStackTrace(System.out);
        }
    }


    public static List<AmpIndicatorRiskRatings> getAllIndicatorRisks()
    {
        Session session = null;
        Query qry = null;
        List<AmpIndicatorRiskRatings> col = null;
        try
        {
            session = PersistenceManager.getSession();
            String queryString = "select r from " + AmpIndicatorRiskRatings.class.getName() + " r";
            qry = session.createQuery(queryString);
            col = qry.list();
        }
        catch (Exception e)
        {
            logger.error("Unable to get the risk ratings");
            logger.debug("Exception : " + e);
        }
        return col;
    }


    public static String getRiskColor(int overallRisk) {
        String fntColor = "";
        int r = 0,g = 0,b = 0;
        switch (overallRisk) {
        case Constants.HIGHLY_SATISFACTORY:
            r = Constants.HIGHLY_SATISFACTORY_CLR.getRed();
            g = Constants.HIGHLY_SATISFACTORY_CLR.getGreen();
            b = Constants.HIGHLY_SATISFACTORY_CLR.getBlue();
            break;
        case Constants.VERY_SATISFACTORY:
            r = Constants.VERY_SATISFACTORY_CLR.getRed();
            g = Constants.VERY_SATISFACTORY_CLR.getGreen();
            b = Constants.VERY_SATISFACTORY_CLR.getBlue();
            break;
        case Constants.SATISFACTORY:
            r = Constants.SATISFACTORY_CLR.getRed();
            g = Constants.SATISFACTORY_CLR.getGreen();
            b = Constants.SATISFACTORY_CLR.getBlue();
            break;
        case Constants.UNSATISFACTORY:
            r = Constants.UNSATISFACTORY_CLR.getRed();
            g = Constants.UNSATISFACTORY_CLR.getGreen();
            b = Constants.UNSATISFACTORY_CLR.getBlue();
            break;
        case Constants.VERY_UNSATISFACTORY:
            r = Constants.VERY_UNSATISFACTORY_CLR.getRed();
            g = Constants.VERY_UNSATISFACTORY_CLR.getGreen();
            b = Constants.VERY_UNSATISFACTORY_CLR.getBlue();
            break;
        case Constants.HIGHLY_UNSATISFACTORY:
            r = Constants.HIGHLY_UNSATISFACTORY_CLR.getRed();
            g = Constants.HIGHLY_UNSATISFACTORY_CLR.getGreen();
            b = Constants.HIGHLY_UNSATISFACTORY_CLR.getBlue();
        }

        String hexR = Integer.toHexString(r);
        String hexG = Integer.toHexString(g);
        String hexB = Integer.toHexString(b);
        if (hexR.equals("0"))
            fntColor += "00";
        else
            fntColor += hexR;

        if (hexG.equals("0"))
            fntColor += "00";
        else
            fntColor += hexG;

        if (hexB.equals("0"))
            fntColor += "00";
        else
            fntColor += hexB;

        return fntColor;
    }


    public static String getRiskRatingName(int risk) {
        Session session = null;
        String riskName = "";

        try {
            session = PersistenceManager.getSession();
            String qryStr = "select r.ratingName from " + AmpIndicatorRiskRatings.class.getName() + "" +
                    " r where (r.ratingValue=:risk)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("risk",new Integer(risk),IntegerType.INSTANCE);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                riskName = (String) itr.next();
            }
        }
        catch(Exception e) {
            logger.error("Unable to get risk ratibg value");
            e.printStackTrace(System.out);
        }
        return riskName;
    }
    

    public static void deleteProjIndicator(Long indId)
    {
        Collection colMeIndValIds = null;
        Collection ampMECurrValIds = null;
        AmpMEIndicatorValue ampMEIndVal = null;

        AmpMEIndicators ampMEInd = new AmpMEIndicators();
        ampMEInd.setAmpMEIndId(indId);
        colMeIndValIds = MEIndicatorsUtil.getMeIndValIds(indId);
        Iterator itr = colMeIndValIds.iterator();
        while(itr.hasNext())
        {
            ampMEIndVal = (AmpMEIndicatorValue) itr.next();
            ampMECurrValIds = MEIndicatorsUtil.getMeCurrValIds(ampMEIndVal.getAmpMeIndValId());

            if(ampMECurrValIds != null)
            {
                AmpMECurrValHistory ampMECurrVal = null;
                Iterator itrCurrVal = ampMECurrValIds.iterator();
                while(itrCurrVal.hasNext())
                {
                    ampMECurrVal = (AmpMECurrValHistory) itrCurrVal.next();
                    try {
                    DbUtil.delete(ampMECurrVal);
                    } catch (JDBCException e) {
                        logger.error(e.getMessage(), e);
                }
            }
            }
            try {
            DbUtil.delete(ampMEIndVal);
            } catch (JDBCException e) {
                logger.error(e.getMessage(), e);
        }
        }
        try {
        DbUtil.delete(ampMEInd);
        } catch (JDBCException e) {
            logger.error(e.getMessage(), e);
    }
  }
}
