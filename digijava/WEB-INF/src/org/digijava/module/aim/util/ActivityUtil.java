/*
 * ActivityUtil.java
 * Created: 14 Feb, 2005
 */

package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityClosingDates;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpClosingDateHistory;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMECurrValHistory;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpNotes;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalComponentReport;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpReportCache;
import org.digijava.module.aim.dbentity.AmpReportLocation;
import org.digijava.module.aim.dbentity.AmpReportPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpReportSector;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AmpProjectDonor;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.Issues;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.Measures;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.helper.RelOrganization;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.cms.dbentity.CMSContentItem;

/**
 * ActivityUtil is the persister class for all activity related
 * entities
 *
 * @author Priyajith
 */
public class ActivityUtil {

	private static Logger logger = Logger.getLogger(ActivityUtil.class);

	/**
	 * Persists an AmpActivity object to the database
	 * This function is used to create a new activity
	 * @param activity The activity to be persisted
	 */
	public static Long saveActivity(AmpActivity activity, ArrayList commentsCol,
			boolean serializeFlag, Long field,Collection relatedLinks,Long memberId) {
		/*
		 * calls saveActivity(AmpActivity activity,Long oldActivityId,boolean edit)
		 * by passing null and false to the parameters oldActivityId and edit respectively
		 * since this is creating a new activity
		 */
		return saveActivity(activity,null,false,commentsCol,serializeFlag,field,relatedLinks,memberId,null);
	}

	/**
	 * Persist an AmpActivity object to the database
	 * This function is used to either update an existing activity
	 * or creating a new activity. If the parameter 'edit' is set to
	 * true the function will update an existing activity with id
	 * given by the parameter 'oldActivityId'. If the 'edit' parameter
	 * is false, the function will create a new activity
	 *
	 * @param activity The AmpActivity object to be persisted
	 * @param oldActivityId The id of the AmpActivity object which is to be updated
	 * @param edit This boolean variable represents whether to create a new
	 * activity object or to update the existing activity object
	 */
	public static Long saveActivity(AmpActivity activity,Long oldActivityId,boolean edit,
			ArrayList commentsCol,boolean serializeFlag, Long field,
			Collection relatedLinks,Long memberId,Collection indicators) {
		logger.debug("In save activity " + activity.getName());
		Session session = null;
		Transaction tx = null;
		AmpActivity oldActivity = null;
		
		Long activityId = null;

		try {
			session = PersistenceManager.getSession();
			AmpTeamMember member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			tx = session.beginTransaction();
	
			
			if (edit) { /* edit an existing activity */
			    oldActivity = (AmpActivity) session.load(AmpActivity.class,oldActivityId);

			    activityId = oldActivityId;

			    activity.setAmpActivityId(oldActivityId);


				if (oldActivity == null) {
					logger.debug("Previous Activity is null");
				}

				// delete previos fundings and funding details
				Set fundSet = oldActivity.getFunding();
				if (fundSet != null) {
					Iterator fundSetItr = fundSet.iterator();
					while (fundSetItr.hasNext()) {
						AmpFunding fund = (AmpFunding) fundSetItr.next();
						/*
						Set fundDetSet = fund.getFundingDetails();
						if (fundDetSet != null) {
							Iterator fundDetItr = fundDetSet.iterator();
							while (fundDetItr.hasNext()) {
								AmpFundingDetail ampFundingDetail = (AmpFundingDetail) fundDetItr.next();
								session.delete(ampFundingDetail);
							}
						}*/
						session.delete(fund);
					}
				}

				// delete previous regional fundings
				fundSet = oldActivity.getRegionalFundings();
				if (fundSet != null) {
					Iterator fundSetItr = fundSet.iterator();
					while (fundSetItr.hasNext()) {
						AmpRegionalFunding regFund = (AmpRegionalFunding) fundSetItr.next();
						session.delete(regFund);
					}
				}

				// delete all previous components
				/*Set comp = oldActivity.getComponents();
				if (comp != null) {
					Iterator compItr = comp.iterator();
					while (compItr.hasNext()) {
						AmpComponent ampComp = (AmpComponent) compItr.next();
						session.delete(ampComp);
					}
				}*/

				// delete all previous org roles
				Set orgrole = oldActivity.getOrgrole();
				if (orgrole != null) {
					Iterator orgroleItr = orgrole.iterator();
					while (orgroleItr.hasNext()) {
						AmpOrgRole ampOrgrole = (AmpOrgRole) orgroleItr.next();
						session.delete(ampOrgrole);
					}
				}

				// delete all previous closing dates
				Set closeDates = oldActivity.getClosingDates();
				if (closeDates != null) {
					Iterator dtItr = closeDates.iterator();
					while (dtItr.hasNext()) {
						AmpActivityClosingDates date = (AmpActivityClosingDates) dtItr.next();
						session.delete(date);
					}
				}

				// delete all previous issues
				Set issues = oldActivity.getIssues();
				if (issues != null) {
					Iterator iItr = issues.iterator();
					while (iItr.hasNext()) {
						AmpIssues issue = (AmpIssues) iItr.next();
						session.delete(issue);
					}
				}

				// delete all previous sectors
				if (oldActivity.getSectors() != null) {
					Iterator iItr = oldActivity.getSectors().iterator();
					while (iItr.hasNext()) {
						AmpActivitySector sec = (AmpActivitySector) iItr.next();
						session.delete(sec);
					}
				}

				// delete all previous comments
				if (!commentsCol.isEmpty()) {
					ArrayList col = org.digijava.module.aim.util.DbUtil.getAllCommentsByField(field,oldActivity.getAmpActivityId());
					logger.debug("col.size() [Inside deleting]: " + col.size());
						if (col != null) {
							Iterator itr = col.iterator();
							while (itr.hasNext()) {
								AmpComments comObj = (AmpComments) itr.next();
								session.delete(comObj);
							}
						}
					logger.debug("comments deleted");
				}
				else
					logger.debug("commentsCol is empty");


				oldActivity.getClosingDates().clear();
				oldActivity.getComponents().clear();
				oldActivity.getDocuments().clear();
				oldActivity.getFunding().clear();
				oldActivity.getInternalIds().clear();
				oldActivity.getLocations().clear();
				oldActivity.getOrgrole().clear();
				oldActivity.getSectors().clear();

				oldActivity.setLineMinRank(activity.getLineMinRank());
				oldActivity.setPlanMinRank(activity.getPlanMinRank());
				oldActivity.setActualApprovalDate(activity.getActualApprovalDate());
				oldActivity.setActualCompletionDate(activity.getActualCompletionDate());
				oldActivity.setActualStartDate(activity.getActualStartDate());
				oldActivity.setAmpId(activity.getAmpId());
				oldActivity.setCalType(activity.getCalType());
				oldActivity.setCondition(activity.getCondition());
				oldActivity.setContFirstName(activity.getContFirstName());
				oldActivity.setContLastName(activity.getContLastName());
				oldActivity.setContractors(activity.getContractors());
				oldActivity.setCountry(activity.getCountry());
				oldActivity.setDescription(activity.getDescription());
                                oldActivity.setDocumentSpace(activity.getDocumentSpace());
				oldActivity.setEmail(activity.getEmail());
				oldActivity.setLanguage(activity.getLanguage());
				oldActivity.setLevel(activity.getLevel());
				oldActivity.setModality(activity.getModality());
				oldActivity.setMofedCntEmail(activity.getMofedCntEmail());
				oldActivity.setMofedCntFirstName(activity.getMofedCntFirstName());
				oldActivity.setMofedCntLastName(activity.getMofedCntLastName());
				oldActivity.setName(activity.getName());
				oldActivity.setBudget(activity.getBudget());
				oldActivity.setObjective(activity.getObjective());
				oldActivity.setProgramDescription(activity.getProgramDescription());
				oldActivity.setProposedApprovalDate(activity.getProposedApprovalDate());
				oldActivity.setProposedStartDate(activity.getProposedStartDate());
				oldActivity.setStatus(activity.getStatus());
				oldActivity.setStatusReason(activity.getStatusReason());
				oldActivity.setThemeId(activity.getThemeId());
				oldActivity.setUpdatedDate(activity.getUpdatedDate());
				oldActivity.setClosingDates(activity.getClosingDates());
				oldActivity.setComponents(activity.getComponents());
				//oldActivity.setDocuments(activity.getDocuments());
				oldActivity.setFunding(activity.getFunding());
				oldActivity.setRegionalFundings(activity.getRegionalFundings());

				oldActivity.setInternalIds(activity.getInternalIds());
				oldActivity.setLocations(activity.getLocations());
				oldActivity.setOrgrole(activity.getOrgrole());
				oldActivity.setSectors(activity.getSectors());
				oldActivity.setIssues(activity.getIssues());


                oldActivity.setActivityPrograms(activity.getActivityPrograms());
                oldActivity.setFunAmount(activity.getFunAmount());
                oldActivity.setCurrencyCode(activity.getCurrencyCode());
                oldActivity.setFunDate(activity.getFunDate());


				oldActivity.setApprovalStatus(activity.getApprovalStatus());
			}

			Iterator itr = relatedLinks.iterator();
			
			while (itr.hasNext()) {
				RelatedLinks rl = (RelatedLinks) itr.next();
				CMSContentItem temp = (CMSContentItem) session.get(CMSContentItem.class, new Long(rl.getRelLink().getId()));
				if (temp == null) {
					logger.debug("Item doesn't exist. Creating the CMS item");
					temp = rl.getRelLink();
					session.save(temp);
				}
				logger.debug("CMS item = " + temp.getId());
				if (rl.isShowInHomePage()) {
					if (member.getLinks() == null)
						member.setLinks(new HashSet());
					member.getLinks().add(temp);
				}

				if (edit) {
					if (oldActivity.getDocuments() == null) {
						oldActivity.setDocuments(new HashSet());
					}
					oldActivity.getDocuments().add(temp);
				} else {
					if (activity.getDocuments() == null) {
						activity.setDocuments(new HashSet());
					}
					activity.getDocuments().add(temp);
				}
			}
			session.saveOrUpdate(member);


			/* Persists the activity */
			if (edit) {
				// update the activity
			    logger.debug("updating ....");
			    oldActivity.setUpdatedDate(new Date(System.currentTimeMillis()));
			    oldActivity.setUpdatedBy(member);
			    session.saveOrUpdate(oldActivity);
			    activity = oldActivity;
			    /*
			    // added by Akash
			    // desc: Saving team members in amp_member_activity table in case activity is Approved
			    // start
				if ("approved".equals(oldActivity.getApprovalStatus())) {
					Long teamId = oldActivity.getTeam().getAmpTeamId();
					Query qry = null;
					String queryString = "select tm from " + AmpTeamMember.class.getName()
					 					 + " tm where (tm.ampTeam=:teamId)";
					qry = session.createQuery(queryString);
					qry.setParameter("teamId", teamId, Hibernate.LONG);
					Iterator tmItr = qry.list().iterator();
					member = new AmpTeamMember();
					while (tmItr.hasNext()) {
						member = (AmpTeamMember) tmItr.next();
						if (!member.getAmpMemberRole().getTeamHead().booleanValue()) {
							if (member.getActivities() == null)
								member.setActivities(new HashSet());
						}
						member.getActivities().add(oldActivity);
						session.saveOrUpdate(member);
					}
				}
				// end	*/

			} else {
				// create the activity
			    logger.debug("creating ....");
			    if (activity.getMember() == null) {
					activity.setMember(new HashSet());
			    }

				activity.getMember().add(activity.getActivityCreator());
				/*
				member = (AmpTeamMember) session.load(AmpTeamMember.class,
				        activity.getActivityCreator().getAmpTeamMemId());
				if (member.getActivities() == null) {
				    member.setActivities(new HashSet());
				}
				member.getActivities().add(activity);
				*/
				session.save(activity);
				activityId = activity.getAmpActivityId();
				//session.saveOrUpdate(member);
			}



			/* Persists comments, of type AmpComments, related to the activity */
			if (!commentsCol.isEmpty()) {
				logger.debug("commentsCol.size() [Inside Persisting]: " + commentsCol.size());
				boolean flag = true;
				/*
				if (edit && serializeFlag)
					flag = false; */
				logger.debug("flag [Inside Persisting comments]: " + flag);
				itr = commentsCol.iterator();
				while (itr.hasNext()) {
					AmpComments comObj = (AmpComments) itr.next();
					comObj.setAmpActivityId(activity);
					session.save(comObj);
					logger.debug("Comment Saved [AmpCommentId] : " + comObj.getAmpCommentId());
				}
			} else
				logger.debug("commentsCol is empty");

			if (indicators != null && indicators.size() > 0) {
				itr = indicators.iterator();
				while (itr.hasNext()) {
					ActivityIndicator actInd = (ActivityIndicator) itr.next();

					AmpMEIndicatorValue indVal = null;
					if (actInd.getIndicatorValId() != null &&
							actInd.getIndicatorValId().longValue() > 0) {
						indVal = (AmpMEIndicatorValue) session.load(
								AmpMEIndicatorValue.class,actInd.getIndicatorValId());
					} else {
						indVal = new AmpMEIndicatorValue();
						indVal.setActivityId(activity);
						AmpMEIndicators meInd = (AmpMEIndicators) session.load(AmpMEIndicators.class,
								actInd.getIndicatorId());
						indVal.setMeIndicatorId(meInd);
					}

					if (actInd.getBaseValDate() != null &&
							actInd.getTargetValDate() != null &&
							actInd.getRevisedTargetValDate() != null) {

						indVal.setBaseVal(actInd.getBaseVal());
						indVal.setBaseValDate(DateConversion.getDate(actInd.getBaseValDate()));
						indVal.setBaseValComments(actInd.getBaseValComments());

						indVal.setTargetVal(actInd.getTargetVal());
						indVal.setTargetValDate(DateConversion.getDate(actInd.getTargetValDate()));
						indVal.setTargetValComments(actInd.getTargetValComments());

						indVal.setRevisedTargetVal(actInd.getRevisedTargetVal());
						indVal.setRevisedTargetValDate(DateConversion.getDate(actInd.getRevisedTargetValDate()));
						indVal.setRevisedTargetValComments(actInd.getRevisedTargetValComments());

						if (actInd.getCurrentValDate() != null &&
								actInd.getCurrentValDate().trim().length() > 0) {
							logger.info("Here 1");
							if (actInd.getActualValDate() != null &&
									actInd.getActualValDate().trim().length() > 0
									&& (actInd.getActualVal() != actInd.getCurrentVal() ||
											!actInd.getActualValDate().equals(
													actInd.getCurrentValDate()))) {
								logger.info("Here 2");
								AmpMECurrValHistory currValHist = new AmpMECurrValHistory();
								currValHist.setCurrValue(actInd.getActualVal());
								currValHist.setCurrValueDate(DateConversion.getDate(actInd.getActualValDate()));
								currValHist.setComments(actInd.getActualValComments());
								currValHist.setMeIndValue(indVal);
								session.save(currValHist);
							}
							logger.info("Here 3");
							indVal.setActualVal(actInd.getCurrentVal());
							indVal.setActualValDate(DateConversion.getDate(actInd.getCurrentValDate()));
							indVal.setActualValComments(actInd.getCurrentValComments());
							logger.info("Here 4");
						}

						AmpIndicatorRiskRatings risk = null;
						if (actInd.getRisk() != null &&
								actInd.getRisk().longValue() > 0) {
							risk = (AmpIndicatorRiskRatings) session.load(AmpIndicatorRiskRatings.class,actInd.getRisk());
						}
						indVal.setRisk(risk);
						session.saveOrUpdate(indVal);
					}
				}
			}

			tx.commit(); // commit the transcation
			logger.debug("Activity saved");
		} catch (Exception ex) {
			logger.error("Exception from saveActivity()  " + ex.getMessage());
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
					logger.debug("Transaction Rollbacked");
				}
				catch (HibernateException e) {
					logger.error("Rollback failed :" + e);
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception e) {
					logger.error("Release session faliled :" + e);
				}
			}
		}

		return activityId;
	}

	public static Collection getComponents(Long actId) {
		 Session session = null;
		 Collection col = new ArrayList();
		 logger.info(" this is the other components getting called....");
		 try {
			session = PersistenceManager.getSession();
			String queryString = "select comp from " + AmpComponent.class.getName() +
			" comp where (comp.activity=:actId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId",actId,Hibernate.LONG);
			col = qry.list();
		 } catch (Exception e) {
		 	logger.error("Unable to get all components");
		 	logger.error(e.getMessage());
		 } finally {
		 	try {
		 		PersistenceManager.releaseSession(session);
		 	} catch (Exception  ex) {
		 		logger.error("Release Session failed :" + ex);
		 	}
		 }
		 return col;
	}


        public static Collection searchActivities(Long ampThemeId,
                                                  String statusCode,
                                                  Long donorOrgId,
                                                  Date fromDate,
                                                  Date toDate,
                                                  Long locationId) {
            Collection result = null;
            try {
                Session session = PersistenceManager.getRequestDBSession();
                String oql = "select act from " + AmpActivity.class.getName() +
                        " act  where 1=1 ";
                //this is changed cos now we have many-to-many here.
                if (ampThemeId != null) {
                	oql+=" and ( :ampThemeId in elements(act.activityPrograms )) ";
//                	oql+=" and act.activityPrograms in ( from "+AmpTheme.class.getName()+" thm where thm.ampThemeId=:ampThemeId )";
//                    oql += " and act.themeId.ampThemeId=:ampThemeId ";
                }
                if (donorOrgId != null) {
                    String s = " and act in (select rol.activity from " +
                            AmpOrgRole.class.getName() + " rol, "+
                            "where rol.organisation.ampOrgId=:DonorId  )";
                    oql += s;
                }
                if (statusCode != null) {
                    oql += " and act.status.statusCode=:statusCode ";
                }
                if (fromDate != null) {
                    oql += " and act.createdDate >= :FromDate";
                }
                if (toDate != null) {
                    oql += " and act.createdDate <= :ToDate";
                }
                if (locationId != null) {
                    oql += " and act.locations in (from "+AmpLocation.class.getName()+" loc where loc.id=:LocationID)";
//                    oql += " and act in (select a from AmpLocation.aidlocation as a, AmpLocation loc where loc.id=20)";
                }
                oql += " order by act.name";

                //==query
                Query query = session.createQuery(oql);
                if (ampThemeId != null) {
                    query.setLong("ampThemeId", ampThemeId.longValue());
                }
                if (donorOrgId != null) {
                    query.setLong("DonorId", donorOrgId.longValue());
                }
                if (statusCode != null) {
                    query.setString("statusCode", statusCode);
                }
                if (fromDate != null) {
                    query.setDate("FromDate", fromDate);
                }
                if (toDate != null) {
                    query.setDate("ToDate", toDate);
                }
                if (locationId != null) {
                    query.setLong("LocationID",locationId.longValue());
                }

                result = query.list();
            }
            catch (Exception ex) {
                logger.error(ex);
            }

            return result;
        }


	public static Collection getActivityCloseDates(Long activityId) {
		 Session session = null;
		 Collection col = new ArrayList();

		 try {
			session = PersistenceManager.getSession();
			String queryString = "select date from " + AmpActivityClosingDates.class.getName() +
			" date where (date.ampActivityId=:actId) and type in (0,1) order by date.ampActivityClosingDateId";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId",activityId,Hibernate.LONG);
			col = qry.list();
		 } catch (Exception e) {
		 	logger.error("Unable to get activity close dates");
		 	logger.error(e.getMessage());
		 } finally {
		 	try {
		 		PersistenceManager.releaseSession(session);
		 	} catch (Exception  ex) {
		 		logger.error("Release Session failed :" + ex);
		 	}
		 }
		 return col;
	}

	public static Collection getOrganizationWithRole(Long actId,String roleCode) {
		Session session = null;
		Collection col = new ArrayList();
		try {
			session = PersistenceManager.getSession();
			String qryStr = "select aor from " + AmpOrgRole.class.getName() + " aor " +
					"where (aor.activity=actId)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("actId",actId,Hibernate.LONG);
			Collection orgRoles = qry.list();
			Collection temp = new ArrayList();

			Iterator orgItr = orgRoles.iterator();
			while (orgItr.hasNext()) {
				AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
				if (orgRole.getRole().getRoleCode().equalsIgnoreCase(roleCode)) {
					if (!temp.contains(orgRole.getOrganisation())) {
						temp.add(orgRole.getOrganisation());
					}
				}
			}

			orgItr = temp.iterator();
			while (orgItr.hasNext()) {
				AmpOrganisation org = (AmpOrganisation) orgItr.next();
				col.add(org.getName());
			}

			AmpActivity act = (AmpActivity) session.load(AmpActivity.class,actId);

			if (act.getOrgrole() != null) {

			}
		} catch (Exception e) {
			logger.error("Unable to get Organization with role " + roleCode);
			logger.error(e.getMessage());
		} finally {
		 	try {
		 		PersistenceManager.releaseSession(session);
		 	} catch (Exception  ex) {
		 		logger.error("Release Session failed :" + ex);
		 	}
		}
		return col;
	}

	public static AmpActivity getAmpActivity(Long id) {
	    Session session = null;
	    AmpActivity activity = null;

	    try {
			session = PersistenceManager.getRequestDBSession();

			AmpActivity ampActivity = (AmpActivity) session.load(AmpActivity.class,id);

			if (ampActivity != null) {
			    activity = new AmpActivity();
			    activity.setLineMinRank(ampActivity.getLineMinRank());
			    activity.setPlanMinRank(ampActivity.getPlanMinRank());
			    activity.setActivityApprovalDate(ampActivity.getActivityApprovalDate());
			    activity.setActivityCloseDate(ampActivity.getActivityCloseDate());
			    activity.setActivityCreator(ampActivity.getActivityCreator());
			    activity.setActivityStartDate(ampActivity.getActivityStartDate());
			    activity.setActualApprovalDate(ampActivity.getActualApprovalDate());
			    activity.setActualCompletionDate(ampActivity.getActualCompletionDate());
			    activity.setActualStartDate(ampActivity.getActualStartDate());
			    activity.setAmpActivityId(ampActivity.getAmpActivityId());
			    activity.setAmpId(ampActivity.getAmpId());
			    activity.setCalType(ampActivity.getCalType());
			    activity.setComments(ampActivity.getComments());
			    activity.setCondition(ampActivity.getCondition());
			    activity.setContactName(ampActivity.getContactName());
			    activity.setContFirstName(ampActivity.getContFirstName());
			    activity.setContLastName(ampActivity.getContLastName());
			    activity.setContractors(ampActivity.getContractors());
			    activity.setCountry(ampActivity.getCountry());
			    activity.setCreatedDate(ampActivity.getCreatedDate());
			    activity.setDescription(ampActivity.getDescription());
                activity.setDocumentSpace(ampActivity.getDocumentSpace());
                activity.setBudget(ampActivity.getBudget());
                activity.setUpdatedBy(ampActivity.getUpdatedBy());

                activity.setEmail(ampActivity.getEmail());
			    activity.setLanguage(ampActivity.getLanguage());
			    activity.setLevel(ampActivity.getLevel());
			    activity.setModality(ampActivity.getModality());
			    activity.setMofedCntEmail(ampActivity.getMofedCntEmail());
			    activity.setMofedCntFirstName(ampActivity.getMofedCntFirstName());
			    activity.setMofedCntLastName(ampActivity.getMofedCntLastName());
			    activity.setName(ampActivity.getName());
			    activity.setObjective(ampActivity.getObjective());
			    activity.setOriginalCompDate(ampActivity.getOriginalCompDate());
			    activity.setProgramDescription(ampActivity.getProgramDescription());
			    activity.setProposedApprovalDate(ampActivity.getProposedApprovalDate());
			    activity.setProposedStartDate(ampActivity.getProposedStartDate());
			    activity.setStatus(ampActivity.getStatus());
			    activity.setStatusReason(ampActivity.getStatusReason());
			    activity.setTeam(ampActivity.getTeam());
			    activity.setThemeId(ampActivity.getThemeId());
			    activity.setUpdatedDate(ampActivity.getUpdatedDate());
			    activity.setVersion(ampActivity.getVersion());

                activity.setActivityPrograms(ampActivity.getActivityPrograms());
                activity.setFunAmount(ampActivity.getFunAmount());
                activity.setFunDate(ampActivity.getFunDate());
                activity.setCurrencyCode(ampActivity.getCurrencyCode());

			    activity.setClosingDates(new HashSet(ampActivity.getClosingDates()));
			    activity.setComponents(new HashSet(ampActivity.getComponents()));
			    activity.setDocuments(new HashSet(ampActivity.getDocuments()));
			    activity.setFunding(new HashSet(ampActivity.getFunding()));
			    activity.setRegionalFundings(new HashSet(ampActivity.getRegionalFundings()));
			    activity.setInternalIds(new HashSet(ampActivity.getInternalIds()));
			    activity.setLocations(new HashSet(ampActivity.getLocations()));
			    activity.setSectors(new HashSet(ampActivity.getSectors()));
			    activity.setOrgrole(new HashSet(ampActivity.getOrgrole()));
			    activity.setIssues(new HashSet(ampActivity.getIssues()));

			}
		} catch (Exception e) {
		 	logger.error("Unable to getAmpActivity");
		 	e.printStackTrace(System.out);
		}
	    return activity;
	}

	public static Activity getChannelOverview(Long actId) {
		Session session = null;
		Activity activity = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select act from " + AmpActivity.class.getName() +
			" act where (act.ampActivityId=:actId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId",actId,Hibernate.LONG);
			Collection act = qry.list();
			Iterator actItr = act.iterator();
			if (actItr.hasNext()) {
				activity = new Activity();
				AmpActivity ampAct = (AmpActivity) actItr.next();
				activity.setActivityId(ampAct.getAmpActivityId());

				activity.setStatus(ampAct.getStatus().getName());
				activity.setStatusReason(ampAct.getStatusReason().trim());
				activity.setBudget(ampAct.getBudget());

				activity.setObjective(ampAct.getObjective());

				activity.setCurrCompDate(DateConversion.
						ConvertDateToString(ampAct.getActualCompletionDate()));
				activity.setOrigAppDate(DateConversion.
						ConvertDateToString(ampAct.getProposedApprovalDate()));
				activity.setOrigStartDate(DateConversion.
						ConvertDateToString(ampAct.getProposedStartDate()));
				activity.setRevAppDate(DateConversion.
						ConvertDateToString(ampAct.getActualApprovalDate()));
				activity.setRevStartDate(DateConversion.
						ConvertDateToString(ampAct.getActualStartDate()));

				Collection col = ampAct.getClosingDates();
				List dates = new ArrayList();
				if (col != null && col.size() > 0) {
					Iterator itr = col.iterator();
					while (itr.hasNext()) {
						AmpActivityClosingDates cDate = (AmpActivityClosingDates) itr
								.next();
						if (cDate.getType().intValue() == Constants.REVISED.intValue()) {
							dates.add(DateConversion.ConvertDateToString(cDate
									.getClosingDate()));
						}
					}
				}
				Collections.sort(dates,DateConversion.dtComp);
				activity.setRevCompDates(dates);


				if (ampAct.getLevel() != null) {
					activity.setImpLevel(ampAct.getLevel().getName());
				}

				if (ampAct.getThemeId() != null) {
					activity.setProgram(ampAct.getThemeId().getName());
					activity.setProgramDescription(ampAct.getProgramDescription());
				}

				activity.setContractors(ampAct.getContractors());

				activity.setContFirstName(ampAct.getContFirstName());
				activity.setContLastName(ampAct.getContLastName());
				activity.setEmail(ampAct.getEmail());

				activity.setMfdContFirstName(ampAct.getMofedCntFirstName());
				activity.setMfdContLastName(ampAct.getMofedCntLastName());
				activity.setMfdContEmail(ampAct.getMofedCntEmail());

				if (ampAct.getCreatedDate() != null) {
					activity.setCreatedDate(
							DateConversion.ConvertDateToString(ampAct.getCreatedDate()));
				}


				if (ampAct.getActivityCreator() != null) {
					User usr = ampAct.getActivityCreator().getUser();
					if (usr != null) {
						activity.setActAthFirstName(usr.getFirstNames());
						activity.setActAthLastName(usr.getLastName());
						activity.setActAthEmail(usr.getEmail());
					}
				}

				if (ampAct.getModality() != null) {
					activity.setModality(ampAct.getModality().getName());
					activity.setModalityCode(ampAct.getModality().getModalityCode());
				}

				queryString = "select distinct f.ampTermsAssistId.termsAssistName from " +
				AmpFunding.class.getName() + " f where f.ampActivityId=:actId";

				qry = session.createQuery(queryString);
				qry.setParameter("actId",actId,Hibernate.LONG);

				Collection temp = new ArrayList();
				Iterator typesItr = qry.list().iterator();
				while (typesItr.hasNext()) {
					String code = (String) typesItr.next();
					temp.add(code);
				}
				activity.setAssistanceType(temp);

				Collection relOrgs = new ArrayList();
				if (ampAct.getOrgrole() != null) {
					Iterator orgItr = ampAct.getOrgrole().iterator();
					while (orgItr.hasNext()) {
						AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
						RelOrganization relOrg = new RelOrganization();
						relOrg.setOrgName(orgRole.getOrganisation().getName());
						relOrg.setRole(orgRole.getRole().getRoleCode());
						if (!relOrgs.contains(relOrg)) {
							relOrgs.add(relOrg);
						}
					}
				}
				activity.setRelOrgs(relOrgs);

				Collection sectors = new ArrayList();
				if (ampAct.getSectors() != null) {
					Iterator sectItr = ampAct.getSectors().iterator();
					while (sectItr.hasNext()) {
						//AmpSector sec = (AmpSector) sectItr.next();
						AmpSector sec = ((AmpActivitySector) sectItr.next()).getSectorId();
						ActivitySector actSect = new ActivitySector();
						if (sec.getParentSectorId() == null) {
							actSect.setSectorName(sec.getName());
						} else if (sec.getParentSectorId().getParentSectorId() == null) {
							actSect.setSectorName(sec.getParentSectorId().getName());
							actSect.setSubsectorLevel1Name(sec.getName());
						} else {
							actSect.setSectorName(sec.getParentSectorId().getParentSectorId().getName());
							actSect.setSubsectorLevel1Name(sec.getParentSectorId().getName());
							actSect.setSubsectorLevel2Name(sec.getName());
						}
						sectors.add(actSect);
					}
				}
				activity.setSectors(sectors);

				if (ampAct.getLevel() != null) {
					activity.setImpLevel(ampAct.getLevel().getName());
				}

				Collection locColl = new ArrayList();
				if (ampAct.getLocations() != null) {
					Iterator locItr = ampAct.getLocations().iterator();
					while (locItr.hasNext()) {
						AmpLocation ampLoc = (AmpLocation) locItr.next();
						Location loc = new Location();
						if (ampLoc.getAmpRegion() != null) {
							loc.setRegion(ampLoc.getAmpRegion().getName());
						}
						if (ampLoc.getAmpZone() != null) {
							loc.setZone(ampLoc.getAmpZone().getName());
						}
						if (ampLoc.getAmpWoreda() != null) {
							loc.setWoreda(ampLoc.getAmpWoreda().getName());
						}
						locColl.add(loc);
					}
				}
				activity.setLocations(locColl);

				activity.setProjectIds(ampAct.getInternalIds());

				Collection modalities = new ArrayList();
				queryString = "select fund from " + AmpFunding.class.getName() + " fund " +
						"where (fund.ampActivityId=:actId)";
				qry = session.createQuery(queryString);
				qry.setParameter("actId",actId,Hibernate.LONG);
				Iterator itr = qry.list().iterator();
				while (itr.hasNext()) {
					AmpFunding fund = (AmpFunding) itr.next();
					modalities.add(fund.getModalityId());
				}
				activity.setModalities(modalities);
				activity.setUniqueModalities(new TreeSet(modalities));
			}
		} catch (Exception e) {
		 	logger.error("Unable to get channnel overview");
		 	e.printStackTrace(System.out);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception  ex) {
		 		logger.error("Release Session failed :" + ex);
		 	}
		}
		return activity;
	}

	public static Collection getActivitySectors(Long actId) {
		Session session = null;
		Collection sectors = new ArrayList();

		try {
			session = PersistenceManager.getSession() ;
			String queryString = "select a from " + AmpActivity.class.getName() +
 			 " a " +  "where (a.ampActivityId=:actId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId",actId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				AmpActivity act = (AmpActivity) itr.next();
				Set set = act.getSectors();
				if (set != null) {
					Iterator sectItr = set.iterator();
					while (sectItr.hasNext()) {
						AmpSector sec = (AmpSector) sectItr.next();
						sectors.add(sec);
					}
				}
			}
		} catch(Exception ex) {
			logger.error("Unable to get activity sectors :" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return sectors;
	}

	public static Collection getOrgRole(Long id) {
	    Session session = null;
	    Collection orgroles = new ArrayList();
		try {
			session = PersistenceManager.getSession() ;
			String queryString = "select aor from " + AmpOrgRole.class.getName() +
 			 " aor " +  "where (aor.activity=:actId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("actId",id,Hibernate.LONG);
			orgroles = qry.list();
		} catch(Exception ex) {
			logger.error("Unable to get activity sectors :" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return orgroles;
	}

	public static Collection getAllComponents(Long id) {
	    Collection col = new ArrayList();

	    Session session = null;

	    try {
	        session = PersistenceManager.getSession();
	        AmpActivity activity = (AmpActivity) session.load(AmpActivity.class,id);
	        Set comp = activity.getComponents();
	        if (comp != null && comp.size() > 0) {
	            Iterator itr1 = comp.iterator();
	            while (itr1.hasNext()) {
	                AmpComponent ampComp = (AmpComponent) itr1.next();
	                Components components = new Components();
	                components.setComponentId(ampComp.getAmpComponentId());
	                components.setDescription(ampComp.getDescription());
	                components.setTitle(ampComp.getTitle());
	                components.setCommitments(new ArrayList());
	                components.setDisbursements(new ArrayList());
	                components.setExpenditures(new ArrayList());
	                components.setPhyProgress(new ArrayList());

	                Collection col1 = ActivityUtil.getFundingComponentActivity(ampComp.getAmpComponentId(),activity.getAmpActivityId());
	              //  Iterator itr2 = ampComp.getComponentFundings().iterator();
	                Iterator itr2 = col.iterator();
	                while (itr2.hasNext()) {
	                    AmpComponentFunding cf = (AmpComponentFunding) itr2.next();
	                    FundingDetail fd = new FundingDetail();
	                    fd.setAdjustmentType(cf.getAdjustmentType().intValue());
	                    if (fd.getAdjustmentType() == Constants.PLANNED) {
	                        fd.setAdjustmentTypeName("Planned");
	                    } else {
	                        fd.setAdjustmentTypeName("Actual");
	                    }
	                    fd.setCurrencyCode(cf.getCurrency().getCurrencyCode());
	                    fd.setCurrencyName(cf.getCurrency().getCurrencyName());
	                    fd.setPerspectiveCode(cf.getPerspective().getCode());
	                    fd.setPerspectiveName(cf.getPerspective().getName());
	                    fd.setTransactionAmount(
	                            DecimalToText.ConvertDecimalToText(
	                                    cf.getTransactionAmount().doubleValue()));
	                    fd.setTransactionDate(
	                            DateConversion.ConvertDateToString(
	                                    cf.getTransactionDate()));
	                    fd.setTransactionType(cf.getTransactionType().intValue());
	                    if (fd.getTransactionType() == Constants.COMMITMENT) {
	                        components.getCommitments().add(fd);
	                    } else if (fd.getTransactionType() == Constants.DISBURSEMENT) {
	                        components.getDisbursements().add(fd);
	                    } else if (fd.getTransactionType() == Constants.EXPENDITURE) {
	                        components.getExpenditures().add(fd);
	                    }
	                }
	                Collection col2 = ActivityUtil.getPhysicalProgressComponentActivity(ampComp.getAmpComponentId(),activity.getAmpActivityId());
	                itr2 = col2.iterator();
	                //itr2 = ampComp.getPhysicalProgress().iterator();
	                while (itr2.hasNext()) {
	                    AmpPhysicalPerformance ampPhyPerf = (AmpPhysicalPerformance) itr2.next();
	                    PhysicalProgress pp = new PhysicalProgress();
	                    pp.setDescription(ampPhyPerf.getDescription());
	                    pp.setPid(ampPhyPerf.getAmpPpId());
	                    pp.setReportingDate(
	                            DateConversion.ConvertDateToString(
	                                    ampPhyPerf.getReportingDate()));
	                    pp.setTitle(ampPhyPerf.getTitle());
	                    components.getPhyProgress().add(pp);
	                }
	    			List list = null;
	    			if (components.getCommitments() != null) {
	    				list = new ArrayList(components.getCommitments());
	    				Collections.sort(list,FundingValidator.dateComp);
	    			}
	    			components.setCommitments(list);
	    			list = null;
	    			if (components.getDisbursements() != null) {
	    				list = new ArrayList(components.getDisbursements());
	    				Collections.sort(list,FundingValidator.dateComp);
	    			}
	    			components.setDisbursements(list);
	    			list = null;
	    			if (components.getExpenditures() != null) {
	    				list = new ArrayList(components.getExpenditures());
	    				Collections.sort(list,FundingValidator.dateComp);
	    			}
	    			components.setExpenditures(list);
	                col.add(components);
	            }
	        }

	    } catch (Exception e) {
			logger.debug("Exception in getAmpComponents() " + e.getMessage());
			e.printStackTrace(System.out);
	    } finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.debug("Exception while releasing session " + ex.getMessage());
				}
			}
	    }
	    return col;
	}
	/*
	 * edited by Govind Dalwani
	 */
	// this function is to get the fundings for the components along with the activity Id

	public static Collection getFundingComponentActivity(Long id,Long actId) {
	    Collection col = null;
	    logger.info(" inside getting the funding.....");
	    Session session = null;

	    try {
	        session = PersistenceManager.getSession();
	        String qryStr = "select a from " + AmpComponentFunding.class.getName() + " a " +
			"where amp_component_id = '" + id + "' and activity_id = '"+ actId +"'";
	        Query qry = session.createQuery(qryStr);
	        col=qry.list();
	        //Iterator itr = qry.list().iterator();
	      /*  if (itr.hasNext()) {
	        	AmpComponentFunding ampf =

	        }*/


	    } catch (Exception e) {
			logger.debug("Exception in getAmpComponents() " + e.getMessage());
			e.printStackTrace(System.out);
	    } finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.debug("Exception while releasing session " + ex.getMessage());
				}
			}
	    }
	    //getComponents();
	    return col;
	}

	// function for getting fundings for components and ids ends here

	//function for physical progress

	public static Collection getPhysicalProgressComponentActivity(Long id,Long actId) {
	    Collection col = null;
	    logger.info(" inside getting the Physical Progress.....");
	    Session session = null;

	    try {
	        session = PersistenceManager.getSession();
	        String qryStr = "select a from " + AmpPhysicalPerformance.class.getName() + " a " +
			"where amp_component_id = '" + id + "' and amp_activity_id = '"+ actId +"'";
	        Query qry = session.createQuery(qryStr);
	        //Iterator itr = qry.list().iterator();
	        col=qry.list();

	    } catch (Exception e) {
			logger.debug("Exception in getAmpComponents() " + e.getMessage());
			e.printStackTrace(System.out);
	    } finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.debug("Exception while releasing session " + ex.getMessage());
				}
			}
	    }
	    return col;
	}
	//function end physical progress
//function to get all the components in the database
	public static Collection getAllComponentNames() {
	    Collection col = null;
	    logger.info(" inside getting the components.....");
	    Session session = null;

	    try {
	        session = PersistenceManager.getSession();
	        String qryStr = "select a from " + AmpComponent.class.getName() + " a " ;
	        Query qry = session.createQuery(qryStr);
	        col=qry.list();

	    } catch (Exception e) {
			logger.debug("Exception in getAmpComponents() " + e.getMessage());
			e.printStackTrace(System.out);
	    } finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.debug("Exception while releasing session " + ex.getMessage());
				}
			}
	    }
	   // EditActivityForm f = new EditActivityForm();
	    //f.setAllComps(col);

	    return col;
	}
//end functino to get components
	public static ArrayList getIssues(Long id) {
		ArrayList list = new ArrayList();

		Session  session = null;
		try {
			session = PersistenceManager.getSession();
			AmpActivity activity = (AmpActivity) session.load(AmpActivity.class,id);
			Set issues = activity.getIssues();
			if (issues != null && issues.size() > 0) {
				Iterator iItr = issues.iterator();
				while (iItr.hasNext()) {
					AmpIssues ampIssue = (AmpIssues) iItr.next();
					Issues issue = new Issues();
					issue.setId(ampIssue.getAmpIssueId());
					issue.setName(ampIssue.getName());
					ArrayList mList = new ArrayList();
					if (ampIssue.getMeasures() != null &&
							ampIssue.getMeasures().size() > 0) {
						Iterator mItr = ampIssue.getMeasures().iterator() ;
						while (mItr.hasNext()) {
							AmpMeasure ampMeasure = (AmpMeasure) mItr.next();
							Measures measure = new Measures();
							measure.setId(ampMeasure.getAmpMeasureId());
							measure.setName(ampMeasure.getName());
							ArrayList aList = new ArrayList();
							if (ampMeasure.getActors() != null &&
									ampMeasure.getActors().size() > 0) {
								Iterator aItr = ampMeasure.getActors().iterator();
								while (aItr.hasNext()) {
									AmpActor actor = (AmpActor) aItr.next();
									aList.add(actor);
								}
							}
							measure.setActors(aList);
							mList.add(measure);
						}
					}
					issue.setMeasures(mList);
					list.add(issue);
				}
			}
		} catch (Exception e) {
			logger.debug("Exception in getIssues() " + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.debug("Exception while releasing session " + ex.getMessage());
				}
			}
		}
		return list;
	}

	public static Collection getRegionalFundings(Long id) {
		Collection col = new ArrayList();

		Session  session = null;
		try {
			session = PersistenceManager.getSession();
			AmpActivity activity = (AmpActivity) session.load(AmpActivity.class,id);
			col = activity.getRegionalFundings();
		} catch (Exception e) {
			logger.debug("Exception in getRegionalFundings() " + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.debug("Exception while releasing session " + ex.getMessage());
				}
			}
		}
		return col;
	}

	public static Collection getRegionalFundings(Long id,Long regId) {
		Collection col = new ArrayList();

		Session  session = null;
		try {
			session = PersistenceManager.getSession();
			AmpActivity activity = (AmpActivity) session.load(AmpActivity.class,id);
			col = activity.getRegionalFundings();
			ArrayList temp = new ArrayList(col);
			Iterator itr = temp.iterator();
			AmpRegionalFunding regionFunding = new AmpRegionalFunding();
			regionFunding.setAmpRegionalFundingId(regId);
			while (itr.hasNext()) {
				AmpRegionalFunding regFund = (AmpRegionalFunding) itr.next();
				if (regionFunding.equals(regFund)) {
					col.remove(regFund);
				}
 			}
		} catch (Exception e) {
			logger.debug("Exception in getRegionalFundings() " + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.debug("Exception while releasing session " + ex.getMessage());
				}
			}
		}
		return col;
	}

	public static AmpActivity getActivityByName(String name) {
		AmpActivity activity = null;
		Session  session = null;
		try {
			session = PersistenceManager.getSession();
			String qryStr = "select a from " + AmpActivity.class.getName() + " a " +
					"where lower(a.name) = '" + name.toLowerCase() + "'";
			Query qry = session.createQuery(qryStr);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				activity = (AmpActivity) itr.next();
			}
		} catch (Exception e) {
			logger.debug("Exception in isActivityExisting() " + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.debug("Exception while releasing session " + ex.getMessage());
				}
			}
		}
		return activity;
	}

	public static void saveDonorFundingInfo(Long actId,Set fundings) {
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();

			//logger.info("Before iterating");
			Iterator itr = fundings.iterator();
			while (itr.hasNext()) {
				AmpFunding temp = (AmpFunding) itr.next();
				AmpFunding fund = (AmpFunding) session.load(AmpFunding.class,temp.getAmpFundingId());
				Iterator fItr = fund.getFundingDetails().iterator();
				while (fItr.hasNext()) {
					AmpFundingDetail fd = (AmpFundingDetail) fItr.next();
					session.delete(fd);
				}
				fund.getFundingDetails().clear();
				fund.setFundingDetails(temp.getFundingDetails());
				//logger.info("Updating " + fund.getAmpFundingId());
				session.update(fund);
				//logger.info("Updated...");
			}
			tx.commit();
			//logger.info("Donor info. saved");
		} catch (Exception e) {
			logger.error("Exception from saveDonorFundingInfo()");
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try{
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

	}

	public static boolean canViewActivity(Long actId,TeamMember tm) {
		boolean canView = false;
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			if (tm.getTeamHead()) {
				if (tm.getTeamType().equalsIgnoreCase("DONOR")) {
					// DONOR team leader
					AmpTeam team = (AmpTeam) session.load(AmpTeam.class,tm.getTeamId());
					AmpActivity act = new AmpActivity();
					act.setAmpActivityId(actId);
					if (team.getActivityList().contains(act)) canView = true;
				} else {
					// MOFED team leader
					//logger.info("Mofed team leader");
					//logger.info("loading activity " + actId);
					AmpActivity act = (AmpActivity) session.load(AmpActivity.class,actId);
					if (act.getTeam().getAmpTeamId().equals(tm.getTeamId())) {
						logger.debug("Can view " + actId + " , team " + tm.getTeamId());
						canView = true;
					} else {

					}
				}
			} else {
				AmpTeamMember ampTeamMem = (AmpTeamMember) session.load(AmpTeamMember.class,
						tm.getMemberId());
				AmpActivity act = new AmpActivity();
				act.setAmpActivityId(actId);
				if (ampTeamMem.getActivities().contains(act)) canView = true;
			}
		} catch (Exception e) {
			logger.error("Exception from canViewActivity()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try{
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
		//logger.info("Canview =" + canView);
		return canView;
	}

	public static Collection getDonors(Long actId) {
		Collection col = new ArrayList();
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			AmpActivity act = (AmpActivity) session.load(AmpActivity.class,actId);
			if (act.getFunding() != null) {
				Iterator itr = act.getFunding().iterator();
				while (itr.hasNext()) {
					AmpFunding fund = (AmpFunding) itr.next();
					AmpProjectDonor ampProjectDonor = new AmpProjectDonor();
					ampProjectDonor.setDonorName(fund.getAmpDonorOrgId().getName());
					ampProjectDonor.setAmpDonorId(fund.getAmpDonorOrgId().getAmpOrgId());
					col.add(ampProjectDonor);
				}
			}

		} catch (Exception e) {
			logger.error("Exception from getDonors()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try{
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
		return col;
	}

	public static long getActivityMaxId() {
		Session session = null;
		long maxId = 0;

		try {
			session = PersistenceManager.getSession();

			String queryString = "select max(act.ampActivityId) from "
					+ AmpActivity.class.getName() + " act";
			Query qry = session.createQuery(queryString);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				Long temp = (Long) itr.next();
				if (temp != null) {
					maxId = temp.longValue();
				}
			}

		} catch (Exception e) {
			logger.error("Uanble to max id :" + e);
		} finally {

			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex) {
				logger.error("releaseSession() failed " + ex);
			}
		}
		return maxId;
	}

	public static AmpActivity getProjectChannelOverview(Long id) {
		Session session = null;
		AmpActivity activity = null;

		try {
			logger.debug("Id is " + id);
			session = PersistenceManager.getSession();

			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
			// start
			String queryString = "select a from " + AmpActivity.class.getName()
					+ " a " + "where (a.ampActivityId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext())
				activity = (AmpActivity) itr.next();
			// end
		} catch (Exception ex) {
			logger
					.error("Unable to get Amp Activity getProjectChannelOverview() :"
							+ ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed ");
			}
		}
		return activity;
	}

	/*
	 * get the list of all the activities
	 * to display in the activity manager of Admin
	*/
	public static Collection getAllActivitiesList()
	{
		Collection col = null;
		Session session = null;
		Query qry = null;

		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select ampAct from " + AmpActivity.class.getName() + " ampAct";
			qry = session.createQuery(queryString);
			col = qry.list();
			logger.debug("the size of the ampActivity : "+col.size());
		}
		catch(Exception e1)
		{
			logger.error("Could not retrieve the activities list");
			e1.printStackTrace(System.out);
		}
		finally
		{
			if(session != null)
			{
				try
				{
					PersistenceManager.releaseSession(session);
				}
				catch(Exception e2)
				{
					logger.error("Release session failed");
				}
			}
		}
		return col;
	}

	/* functions to DELETE an activity by Admin start here.... */
	public static void deleteActivity(Long ampActId)
	{
		Session session = null;
		Transaction tx = null;

		try
		{
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();

			AmpActivity ampAct = (AmpActivity) session.load(
				AmpActivity.class,ampActId);

			if (ampAct == null)
				logger.debug("Activity is null. Hence no activity with id : "+ampActId);
			else
			{
				/* delete fundings and funding details */
				Set fundSet = ampAct.getFunding();
				if (fundSet != null)
				{
					Iterator fundSetItr = fundSet.iterator();
					while (fundSetItr.hasNext())
					{
						AmpFunding fund = (AmpFunding) fundSetItr.next();
						Set fundDetSet = fund.getFundingDetails();
						if (fundDetSet != null)
						{
							Iterator fundDetItr = fundDetSet.iterator();
							while (fundDetItr.hasNext())
							{
								AmpFundingDetail ampFundingDetail = (AmpFundingDetail) fundDetItr.next();
								session.delete(ampFundingDetail);
							}
						}
						Set closingDate = fund.getClosingDateHistory();
						if(closingDate != null)
						{
							Iterator closingDateItr = closingDate.iterator();
							while(closingDateItr.hasNext())
							{
								AmpClosingDateHistory closeHistory = (AmpClosingDateHistory) closingDateItr.next();
								session.delete(closeHistory);
							}
						}
						session.delete(fund);
					}
				}

				/* delete regional fundings */
				fundSet = ampAct.getRegionalFundings();
				if (fundSet != null)
				{
					Iterator fundSetItr = fundSet.iterator();
					while (fundSetItr.hasNext())
					{
						AmpRegionalFunding regFund = (AmpRegionalFunding) fundSetItr.next();
						session.delete(regFund);
					}
				}

				/* delete components */
				Set comp = ampAct.getComponents();
				if (comp != null)
				{
					Iterator compItr = comp.iterator();
					while (compItr.hasNext())
					{
						AmpComponent ampComp = (AmpComponent) compItr.next();
						/*Set compFund = ampComp.getComponentFundings();
						if(compFund != null)
						{
							Iterator compFundItr = compFund.iterator();
							while(compFundItr.hasNext())
							{
								AmpComponentFunding ampCompFund = (AmpComponentFunding) compFundItr.next();
								session.delete(ampCompFund);
							}
						}
						Set phyProgress = ampComp.getPhysicalProgress();
						if(phyProgress != null)
						{
							Iterator phyProgressItr = phyProgress.iterator();
							while(phyProgressItr.hasNext())
							{
								AmpPhysicalPerformance phyPerformance = (AmpPhysicalPerformance) phyProgressItr.next();
								session.delete(phyPerformance);
							}
						}*/
						session.delete(ampComp);
					}
				}

				/* delete org roles */
				Set orgrole = ampAct.getOrgrole();
				if (orgrole != null)
				{
					Iterator orgroleItr = orgrole.iterator();
					while (orgroleItr.hasNext())
					{
						AmpOrgRole ampOrgrole = (AmpOrgRole) orgroleItr.next();
						session.delete(ampOrgrole);
					}
				}

				/* delete closing dates */
				Set closeDates = ampAct.getClosingDates();
				if (closeDates != null)
				{
					Iterator dtItr = closeDates.iterator();
					while (dtItr.hasNext())
					{
						AmpActivityClosingDates date = (AmpActivityClosingDates) dtItr.next();
						session.delete(date);
					}
				}

				/* delete issues,measures,actors */
				Set issues = ampAct.getIssues();
				if (issues != null)
				{
					Iterator iItr = issues.iterator();
					while (iItr.hasNext())
					{
						AmpIssues issue = (AmpIssues) iItr.next();
						Set measure = issue.getMeasures();
						if(measure != null)
						{
							Iterator measureItr = measure.iterator();
							while(measureItr.hasNext())
							{
								AmpMeasure ampMeasure = (AmpMeasure) measureItr.next();
								Set actor = ampMeasure.getActors();
								if(actor != null)
								{
									Iterator actorItr = actor.iterator();
									while(actorItr.hasNext())
									{
										AmpActor ampActor = (AmpActor) actorItr.next();
										session.delete(ampActor);
									}
								}
								session.delete(ampMeasure);
							}
						}
						session.delete(issue);
					}
				}

				/* delete activity internal id
				Set internalIds = ampAct.getInternalIds();
				if(internalIds != null)
				{
					Iterator interIdItr = internalIds.iterator();
					while(interIdItr.hasNext())
					{
						AmpActivityInternalId ampInternalId = (AmpActivityInternalId) interIdItr.next();
						logger.info("internal id : "+ampInternalId.getInternalId());
						session.delete(ampInternalId);
					}
				}
				*/

				/* delete AMP activity Survey */
				Set ampSurvey = ampAct.getSurvey();
				if(ampSurvey != null)
				{
					Iterator surveyItr = ampSurvey.iterator();
					while(surveyItr.hasNext())
					{
						AmpAhsurvey ahSurvey = (AmpAhsurvey) surveyItr.next();
						Set ahAmpSurvey = ahSurvey.getResponses();
						if(ahSurvey != null)
						{
							Iterator ahSurveyItr = ahAmpSurvey.iterator();
							while(ahSurveyItr.hasNext())
							{
								AmpAhsurveyResponse surveyResp = (AmpAhsurveyResponse) ahSurveyItr.next();
								session.delete(surveyResp);
							}
						}
						session.delete(ahSurvey);
					}
				}

				/* delete the activity relevant notes */
				Set notesSet = ampAct.getNotes();
				if(notesSet != null)
				{
					Iterator notesItr = notesSet.iterator();
					while(notesItr.hasNext())
					{
						AmpNotes notesAmp = (AmpNotes) notesItr.next();
						session.delete(notesAmp);
					}
				}
			}
			session.delete(ampAct);
			tx.commit();
			session.flush();
		}
		catch(Exception e1)
		{
			logger.error("Could not delete the activity with id : "+ampActId);
			e1.printStackTrace(System.out);
		}
		finally
		{
			if(session != null)
			{
				try
				{
					PersistenceManager.releaseSession(session);
				}
				catch(Exception e2)
				{
					logger.error("Release session failed");
				}
			}
		}
	}

	public static void deleteActivityAmpComments(Collection commentId)
	{
		Session session = null;
		try
		{
			session = PersistenceManager.getSession();
			if(commentId != null)
			{
				Iterator commentItr = commentId.iterator();
				while(commentItr.hasNext())
				{
					AmpComments ampComment = (AmpComments) commentItr.next();
					AmpComments ampComm = (AmpComments) session.load
											(AmpComments.class,ampComment.getAmpCommentId());
					session.delete(ampComm);
				}
			}
		}
		catch(Exception e1)
		{
			logger.error("Could not delete/find the comments revelant to the activity");
			e1.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch(Exception e2)
			{
				logger.error("Release session failed");
			}
		}
	}

	public static void deleteActivityPhysicalComponentReport(Collection phyCompReport)
	{
		Session session = null;
		try
		{
			session = PersistenceManager.getSession();
			if(phyCompReport != null)
			{
				Iterator phyReportItr = phyCompReport.iterator();
				while(phyReportItr.hasNext())
				{
					AmpPhysicalComponentReport phyReport = (AmpPhysicalComponentReport) phyReportItr.next();
					AmpPhysicalComponentReport physicalReport = (AmpPhysicalComponentReport) session.load
																	(AmpPhysicalComponentReport.class,phyReport.getAmpReportId());
					session.delete(physicalReport);
				}
			}
		}
		catch(Exception e1)
		{
			logger.error("could not delete/find the physical component report activities");
			e1.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch(Exception e2)
			{
				logger.error("Release session failed");
			}
		}
	}

	public static void deleteActivityAmpReportCache(Collection repCache)
	{
		Session session = null;
		try
		{
			session = PersistenceManager.getSession();
			if(repCache != null)
			{
				Iterator repCacheItr = repCache.iterator();
				while(repCacheItr.hasNext())
				{
					AmpReportCache reportCache = (AmpReportCache) repCacheItr.next();
					AmpReportCache ampReportCache = (AmpReportCache) session.load
													(AmpReportCache.class,reportCache.getAmpReportId());
					session.delete(ampReportCache);
				}
			}
		}
		catch(Exception e1)
		{
			logger.error("could not delete/find the physical component report activities");
			e1.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch(Exception e2)
			{
				logger.error("Release session failed");
			}
		}
	}

	public static void deleteActivityReportLocation(Collection repLoc)
	{
		Session session = null;
		try
		{
			session = PersistenceManager.getSession();
			if(repLoc != null)
			{
				Iterator repLocItr = repLoc.iterator();
				while(repLocItr.hasNext())
				{
					AmpReportLocation repLocTemp = (AmpReportLocation) repLocItr.next();
					AmpReportLocation amprepLoc = (AmpReportLocation) session.load
													(AmpReportLocation.class,repLocTemp.getAmpReportId());
					session.delete(amprepLoc);
				}
			}
		}
		catch(Exception e1)
		{
			logger.error("could not delete/find the physical component report activities");
			e1.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch(Exception e2)
			{
				logger.error("Release session failed");
			}
		}
	}

	public static void deleteActivityReportPhyPerformance(Collection phyPerform)
	{
		Session session = null;
		try
		{
			session = PersistenceManager.getSession();
			if(phyPerform != null)
			{
				Iterator phyPerformItr = phyPerform.iterator();
				while(phyPerformItr.hasNext())
				{
					AmpReportPhysicalPerformance repPhyTemp = (AmpReportPhysicalPerformance) phyPerformItr.next();
					AmpReportPhysicalPerformance repPhyPerform = (AmpReportPhysicalPerformance) session.load
																	(AmpReportPhysicalPerformance.class,repPhyTemp.getAmpPpId());
					session.delete(repPhyPerform);
				}
			}
		}
		catch(Exception e1)
		{
			logger.error("could not delete/find the physical component report activities");
			e1.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch(Exception e2)
			{
				logger.error("Release session failed");
			}
		}
	}

	public static void deleteActivityReportSector(Collection repSector)
	{
		Session session = null;
		try
		{
			session = PersistenceManager.getSession();
			if(repSector != null)
			{
				Iterator repSectorItr = repSector.iterator();
				while(repSectorItr.hasNext())
				{
					AmpReportSector repSecTemp = (AmpReportSector) repSectorItr.next();
					AmpReportSector ampRepSector = (AmpReportSector) session.load
													(AmpReportSector.class,repSecTemp.getAmpReportId());
					session.delete(ampRepSector);
				}
			}
		}
		catch(Exception e1)
		{
			logger.error("could not delete/find the physical component report activities");
			e1.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch(Exception e2)
			{
				logger.error("Release session failed");
			}
		}
	}

	public static void deleteActivityIndicatorVal(Collection indVal)
	{
		Session session = null;
		try
		{
			session = PersistenceManager.getSession();
			if(indVal != null)
			{
				Iterator indValItr = indVal.iterator();
				while(indValItr.hasNext())
				{
					AmpMEIndicatorValue indValue = (AmpMEIndicatorValue) indValItr.next();
					AmpMEIndicatorValue indicatorVal = (AmpMEIndicatorValue) session.load
														(AmpMEIndicatorValue.class,indValue.getAmpMeIndValId());
					session.delete(indicatorVal);
				}
			}
		}
		catch(Exception e1)
		{
			logger.error("could not delete/find the physical component report activities");
			e1.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch(Exception e2)
			{
				logger.error("Release session failed");
			}
		}
	}
	/* functions to DELETE an activity by Admin end here.... */
} // End
