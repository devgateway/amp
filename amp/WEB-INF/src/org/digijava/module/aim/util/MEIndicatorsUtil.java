package org.digijava.module.aim.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.dbentity.IndicatorConnection;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.MEIndicatorRisk;
import org.digijava.module.aim.helper.MEIndicatorValue;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Deprecated
public class MEIndicatorsUtil
{
	private static Logger logger = Logger.getLogger(MEIndicatorsUtil.class);
    public static boolean checkDuplicateNameCode(String name,String code,Long id) {
		Session session = null;
		Query qry = null;
		boolean duplicatesExist = false;
		String queryString = null;

		try
		{
			session = PersistenceManager.getRequestDBSession();
			if (id != null && id.longValue() > 0) {
				queryString = "select count(*) from "
					+ AmpIndicator.class.getName() + " meind "
					+ "where ( name=:name"
					+ " or code=:code) and " +
							"(meind.indicatorId !=:id)" ;
				qry = session.createQuery(queryString);
				qry.setParameter("id", id, Hibernate.LONG);
				qry.setParameter("code", code.trim(), Hibernate.STRING);
				qry.setParameter("name", name.trim(), Hibernate.STRING);
			} else {
				queryString = "select count(*) from "
					+ AmpIndicator.class.getName() + " meind "
					+ "where ( name=:name"
					+ " or code=:code)" ;
				qry = session.createQuery(queryString);
				qry.setParameter("code", code.trim(), Hibernate.STRING);
				qry.setParameter("name", name.trim(), Hibernate.STRING);

			}
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				Integer cnt = (Integer) itr.next();
				if (cnt.intValue() > 0)
					duplicatesExist = true;
			}
		}
		catch (Exception ex) {
			logger.error("UNABLE to find Indicators with duplicate name.", ex);
			ex.printStackTrace(System.out);
		}
		finally {
			try {
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2) {
				logger.debug("releaseSession() FAILED", ex2);
			}
		}
		return duplicatesExist;
	}
	public static Collection getActivityList()
	{
		Session session = null;
		Collection col = null;

		try
		{
			session = PersistenceManager.getRequestDBSession();

			String queryString = "select ampActivityId from "
								+ AmpActivity.class.getName() + " ampActivityId";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.debug("UNABLE to find activity ids from AmpActivity.");
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch(Exception exp)
			{
				logger.debug("releaseSession() FAILED", exp);
			}
		}
		return col;
	}
    /*TODO These indicators need refactoring.
     * Old code new code are mixed with each other...
     * Need clear specs*/
    public static Collection getActivityIndicators(Long actId) {
        Session session = null;
        Collection col = new ArrayList();
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            if (actId != null) {

                qryStr = "select con from " + IndicatorConnection.class.getName() + " " +
                        "con  where (con.activity=:actId)";

                qry = session.createQuery(qryStr);
                qry.setParameter("actId", actId, Hibernate.LONG);
                Iterator itr = qry.list().iterator();
                while (itr.hasNext()) {
                    IndicatorConnection actCon = (IndicatorConnection) itr.next();
                    Long conId=actCon.getId();
                    qryStr = "select indVal from " + AmpIndicatorValue.class.getName() + " " +
                            "indVal inner join indVal.indicatorConnection con where (con=:conId)";
                    qry = session.createQuery(qryStr);
                    qry.setLong("conId", conId);
                    ActivityIndicator actInd = new ActivityIndicator();
                    actInd.setConnectionId(conId);
                    AmpIndicator ind = IndicatorUtil.getConnectionToActivity(actCon.getId()).getIndicator();
                    actInd.setIndicatorName(ind.getName());
                    actInd.setIndicatorCode(ind.getCode());
                    actInd.setIndicatorId(ind.getIndicatorId());
                    if (qry.list().size() > 0) {
                        Iterator<AmpIndicatorValue> indValues = qry.list().iterator();
                        while (indValues.hasNext()) {
                            AmpIndicatorValue meIndValue = (AmpIndicatorValue) indValues.next();
                            actInd.setIndicatorValId(meIndValue.getIndValId());
                            float value = meIndValue.getValue().floatValue();
                            if (meIndValue.getValueType() == AmpIndicatorValue.BASE) {
                                actInd.setBaseVal(value);
                                if (meIndValue.getValueDate() != null) {
                                    actInd.setBaseValDate(DateConversion.ConvertDateToString(meIndValue.getValueDate()));
                                }
                                actInd.setBaseValComments(meIndValue.getComment());
                            } else {
                                if (meIndValue.getValueType() == AmpIndicatorValue.ACTUAL) {
                                    actInd.setActualVal(value);
                                    actInd.setCurrentVal(value);
                                    if (meIndValue.getValueDate() != null) {
                                        actInd.setActualValDate(DateConversion.ConvertDateToString(meIndValue.getValueDate()));
                                        actInd.setCurrentValDate(DateConversion.ConvertDateToString(meIndValue.getValueDate()));
                                        actInd.setActualValComments(meIndValue.getComment());
                                        actInd.setCurrentValComments(meIndValue.getComment());
                                    }
                                } else {
                                    if (meIndValue.getValueType() == AmpIndicatorValue.TARGET) {
                                        actInd.setTargetVal(value);
                                        if (meIndValue.getValueDate() != null) {
                                            actInd.setTargetValDate(DateConversion.ConvertDateToString(meIndValue.getValueDate()));
                                        }
                                        actInd.setTargetValComments(meIndValue.getComment());
                                    } else {
                                        actInd.setRevisedTargetVal(value);
                                        if (meIndValue.getValueDate() != null) {
                                            actInd.setRevisedTargetValDate(DateConversion.ConvertDateToString(meIndValue.getValueDate()));
                                        }
                                        actInd.setRevisedTargetValComments(meIndValue.getComment());
                                    }
                                }
                            }

                            //actInd.setLogframeValueId(meIndValue.getLogframeValueId());
                            actInd.setIndicatorsCategory(meIndValue.getLogFrame());

                            if (meIndValue.getRiskValue() != null) {
                                actInd.setRisk(meIndValue.getRiskValue().getId());
                            }
                            actInd.setDefaultInd(ind.isDefaultInd());
                        }
                    }
                     col.add(actInd);
                }
            }
            // load all global indicators which doesnt have an
            // entry in 'amp_me_indicator_value' table

            qryStr = "select meInd from " + AmpIndicator.class.getName() + " meInd " +
                    "where meInd.defaultInd = true";
            qry = session.createQuery(qryStr);
            Iterator itr = qry.list().iterator();
            long t = System.currentTimeMillis();
            while (itr.hasNext()) {
                AmpIndicator meInd = (AmpIndicator) itr.next();
                ActivityIndicator actInd = new ActivityIndicator();
                actInd.setIndicatorId(meInd.getIndicatorId());
                if (!col.contains(actInd)) {
                    actInd.setIndicatorName(meInd.getName());
                    actInd.setIndicatorCode(meInd.getCode());
                    actInd.setIndicatorValId(new Long(t * -1));
                    actInd.setActivityId(actId);
                    actInd.setDefaultInd(meInd.isDefaultInd());
                    ++t;
                    col.add(actInd);
                }
            }

        } catch (Exception e) {
            logger.error("Exception from getActivityIndicators() :" + e.getMessage());
        }
        return col;
    }
    // TODO refactor this method...
    public static void saveMEIndicatorValues(ActivityIndicator actInd) {
        Session session = null;
        Transaction tx = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            tx = session.beginTransaction();
            AmpIndicator ind = (AmpIndicator) session.get(AmpIndicator.class, actInd.getIndicatorId());
            AmpActivity activity = (AmpActivity) session.get(AmpActivity.class, actInd.getActivityId());
            //try to find connection of current activity with current indicator
            IndicatorActivity indConn = IndicatorUtil.findActivityIndicatorConnection(activity, ind, session);
            //if no connection found then create new one. Else clear old values for the connection.
            boolean newIndicator = false;
            if (indConn == null) {
                indConn = new IndicatorActivity();
                indConn.setActivity(activity);
                indConn.setIndicator(ind);
                indConn.setValues(new HashSet<AmpIndicatorValue>());
                newIndicator = true;
            } else {
                if ((indConn.getValues() != null) && (indConn.getValues().size() > 0)) {
                    for (AmpIndicatorValue value : indConn.getValues()) {
                        session.delete(value);
                    }
                    indConn.getValues().clear();
                }
            }

            //create each type of value and assign to connection

            AmpIndicatorValue indValTarget = null;
            if (actInd.getTargetVal() != null) {
                indValTarget = new AmpIndicatorValue();
                indValTarget.setValueType(AmpIndicatorValue.TARGET);
                indValTarget.setValue(new Double(actInd.getTargetVal()));
                indValTarget.setComment(actInd.getTargetValComments());
                indValTarget.setValueDate(DateConversion.getDate(actInd.getTargetValDate()));
                indValTarget.setIndicatorConnection(indConn);
                indConn.getValues().add(indValTarget);
            }
            AmpIndicatorValue indValBase = null;
            if (actInd.getBaseVal() != null) {
                indValBase = new AmpIndicatorValue();
                indValBase.setValueType(AmpIndicatorValue.BASE);
                indValBase.setValue(new Double(actInd.getBaseVal()));
                indValBase.setComment(actInd.getBaseValComments());
                indValBase.setValueDate(DateConversion.getDate(actInd.getBaseValDate()));
                indValBase.setIndicatorConnection(indConn);
                indConn.getValues().add(indValBase);
            }
            AmpIndicatorValue indValRevised = null;
            if (actInd.getRevisedTargetVal() != null) {
                indValRevised = new AmpIndicatorValue();
                indValRevised.setValueType(AmpIndicatorValue.REVISED);
                indValRevised.setValue(new Double(actInd.getRevisedTargetVal()));
                indValRevised.setComment(actInd.getRevisedTargetValComments());
                indValRevised.setValueDate(DateConversion.getDate(actInd.getRevisedTargetValDate()));
                indValRevised.setIndicatorConnection(indConn);
                indConn.getValues().add(indValRevised);
            }
            // save connection with its new values.
            if (newIndicator) {
                // Save the new indicator that is NOT present in the indicators collection from the Activity.
                //IndicatorUtil.saveConnectionToActivity(indConn, session);
                session.saveOrUpdate(indConn);
            } else {
                //They are loaded by different sessions!
                for (AmpIndicatorValue value : indConn.getValues()) {
                    session.save(value);
                }
                session.saveOrUpdate(activity);
            }
            tx.commit();
        } catch (Exception e) {
            logger.error("Exception from saveMEIndicatorValues() :" + e.getMessage());
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception trbf) {
                    logger.error("Transaction roll back failed " + trbf.getMessage());
                }
            }
        }
    }
    /* TODO this method is written for old indicator structure,
     * should be rewritten when dashboard will be rewritten...
     */
	public static Collection getPortfolioMEIndicatorValues(Collection actIds,
			Long indId,boolean includeBaseLine, HttpServletRequest request) {

		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getRequestDBSession();
			String qryStr = null;
			Iterator itr = null;

			if (actIds != null && actIds.size() > 0) {
				if (actIds.size() == 1) {
					itr = actIds.iterator();
					Long actId = (Long) itr.next();
					if (indId.longValue() > 0) {
						qryStr = "select mi.amp_me_indicator_id,iv.base_val,iv.actual_val,iv.revised_target_val," +
								"mi.name from amp_me_indicator_value iv inner join " +
								"amp_me_indicators mi on (iv.me_indicator_id=mi.amp_me_indicator_id)" +
								" where mi.default_ind = 1  and iv.activity_id=" + actId + " and " +
								"iv.me_indicator_id=" + indId + " order by iv.activity_id,mi.name";
					} else {
						qryStr = "select mi.amp_me_indicator_id,iv.base_val,iv.actual_val,iv.revised_target_val,mi.name " +
								"from amp_me_indicator_value iv inner join amp_me_indicators mi " +
								"on (iv.me_indicator_id=mi.amp_me_indicator_id) where mi.default_ind = 1 " +
								"and iv.activity_id=" + actId + " order by iv.activity_id,mi.name";
					}
				} else {
					itr = actIds.iterator();
					String params = "";
					while (itr.hasNext()) {
						Long actId = (Long) itr.next();
						if (params.length() > 0) params += ",";
						params += actId;
					}
					if (indId.longValue() > 0) {
						qryStr = "select mi.amp_me_indicator_id,iv.base_val,iv.actual_val,iv.revised_target_val,a.name from " +
								"amp_me_indicator_value iv inner join amp_me_indicators mi on " +
								"(iv.me_indicator_id=mi.amp_me_indicator_id) inner join amp_activity a on " +
								"(a.amp_activity_id=iv.activity_id) where mi.default_ind = 1  and " +
								"iv.activity_id in (" + params + ") and iv.me_indicator_id=" + indId + "" +
										" order by a.name";
					} else {
//						{
//						qryStr = "select mi.amp_me_indicator_id,sum(iv.base_val),sum(iv.actual_val),sum(iv.revised_target_val)," +
//								"mi.name from amp_me_indicator_value iv inner join amp_me_indicators mi on" +
//								" (iv.me_indicator_id=mi.amp_me_indicator_id) where mi.default_ind = 1 and " +
//								"iv.activity_id in ( " + params + ") group by iv.me_indicator_id " +
//										"order by iv.activity_id";
//					}
						qryStr = "select mi.indicator_id,sum(iv.base_val),sum(iv.actual_val),sum(iv.revised_target_val)," +
						         "mi.name from amp_me_indicator_value iv inner join amp_indicators mi on" +
						         " (iv.indicator_id=mi.indicator_id) where mi.default_ind = true and " +
						         "iv.activity_id in ( " + params + ") group by iv.indicator_id " +
								 "order by iv.activity_id";
			}
				}

				Connection con = session.connection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(qryStr);
				while (rs.next()) {
					long id = rs.getLong(1);
					double baseVal = rs.getDouble(2);
					Double actVal = rs.getDouble(3);
					double tarVal = rs.getDouble(4);
					String key = rs.getString(5);

					if (includeBaseLine) {
						MEIndicatorValue actIndVal = new MEIndicatorValue();
						actIndVal.setIndId(new Long(id));
						actIndVal.setIndicatorName(key);
						String trnKey = "aim:performance:"+(Constants.ME_IND_VAL_ACTUAL_ID).toLowerCase();
						trnKey = trnKey.replaceAll(" ", "");
						String msg = CategoryManagerUtil.translate(trnKey, request, Constants.ME_IND_VAL_ACTUAL_ID);
						actIndVal.setType(msg);
						if (actVal!=null&&tarVal > 0) {
							actIndVal.setValue((actVal - baseVal)/(tarVal - baseVal));
							col.add(actIndVal);
						}
					} else {
						MEIndicatorValue actIndVal = new MEIndicatorValue();
						actIndVal.setIndId(new Long(id));
						actIndVal.setIndicatorName(key);
						String trnKey = "aim:performance:"+(Constants.ME_IND_VAL_ACTUAL_ID).toLowerCase();
						trnKey = trnKey.replaceAll(" ", "");
						String msg = CategoryManagerUtil.translate(trnKey, request, Constants.ME_IND_VAL_ACTUAL_ID);
						actIndVal.setType(msg);
						if (actVal!=null&&tarVal > 0) {
							actIndVal.setValue(actVal/tarVal);
							col.add(actIndVal);
						}
					}

					MEIndicatorValue targetIndVal = new MEIndicatorValue();
					targetIndVal.setIndId(new Long(id));
					targetIndVal.setIndicatorName(key);
					String trnKey = "aim:performance:"+(Constants.ME_IND_VAL_TARGET_ID).toLowerCase();
					trnKey = trnKey.replaceAll(" ", "");
					String msg = CategoryManagerUtil.translate(trnKey, request, Constants.ME_IND_VAL_TARGET_ID);

					targetIndVal.setType(msg);
					if (tarVal > 0) {
						targetIndVal.setValue(1);
						col.add(targetIndVal);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception from getPortfolioMEIndicatorValues() :" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
		return col;
	}

	
	public static Collection getPortfolioMEIndicatorRisks(Collection actIds) {
		ArrayList col = new ArrayList();
		Iterator itr = null;
		Map riskMap = new HashMap();
		try {
            Collection<AmpCategoryValue> risks=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.INDICATOR_RISK_TYPE_KEY);
            itr=risks.iterator();
			while (itr.hasNext()) {
				AmpCategoryValue r = (AmpCategoryValue) itr.next();
				riskMap.put(r.getIndex(),r.getValue());
			}

			if (actIds != null && actIds.size() > 0) {
				itr = actIds.iterator();
				String name;
				while (itr.hasNext()) {
					Long id = (Long) itr.next();
					int value = IndicatorUtil.getOverallRisk(id);
					Integer key = new Integer(value);

					if (riskMap.containsKey(key)) {
						name = (String) riskMap.get(new Integer(value));
						MEIndicatorRisk meRisk = new MEIndicatorRisk();
						meRisk.setRisk(name);
						int index = -1;
						if (col != null && col.size() > 0) {
							index = col.indexOf(meRisk);
						}

						if (index >= 0) {
							meRisk = (MEIndicatorRisk) col.get(index);
							meRisk.setRiskCount(meRisk.getRiskCount()+1);
						} else {
							meRisk.setRiskCount(1);
							meRisk.setRiskRating((byte) value);
							col.add(meRisk);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception from getPortfolioMEIndicatorRisk() :" + e.getMessage());
			e.printStackTrace(System.out);
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

	public static int getOverallPortfolioRisk(Collection actIds) {
		int risk = 0;
		try {
		Collection col = getPortfolioMEIndicatorRisks(actIds);
		Iterator itr = col.iterator();
		float temp = 0;
		while (itr.hasNext()) {
			MEIndicatorRisk meRisk = (MEIndicatorRisk) itr.next();
			temp += meRisk.getRiskRating() * meRisk.getRiskCount();
		}

		if (col.size() > 0) {
			temp /= (float) col.size();
			temp = Math.round(temp);
			if (temp < 0)
				risk = (int) Math.floor(temp);
			else if(temp > 0)
				risk = (int) Math.ceil(temp);
			else
				risk = -1;
		}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return risk;
	}
	
	public static int getRiskRatingValue(String name) {
		int riskRating = 0;

		try {
            Collection<AmpCategoryValue> risks=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.INDICATOR_RISK_TYPE_KEY);
			Iterator<AmpCategoryValue> itr = risks.iterator();
			if (itr.hasNext()) {
				AmpCategoryValue temp =  itr.next();
                if(temp.getValue().equals(name)){
                    riskRating = temp.getIndex();
                }
			}
		}
		catch(Exception e) {
			logger.error(e);
		}
		return riskRating;
	}

	public static String getRiskRatingName(int riskValue) {
		String riskName = "";
		try {
            AmpCategoryValue risk=CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.INDICATOR_RISK_TYPE_KEY,new Long(riskValue));
            riskName=risk.getValue();
		}
		catch(Exception e) {
			logger.error(e);
		}
		return riskName;
	}
}
