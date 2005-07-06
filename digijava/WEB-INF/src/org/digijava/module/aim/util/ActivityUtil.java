/*
 * ActivityUtil.java
 * Created: 14 Feb, 2005 
 */

package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.RelOrganization;
import org.digijava.module.cms.dbentity.CMSContentItem;
import org.digijava.module.aim.util.DbUtil;

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
	public static void saveActivity(AmpActivity activity, ArrayList commentsCol,boolean serializeFlag, Long field) {
		/*
		 * calls saveActivity(AmpActivity activity,Long oldActivityId,boolean edit)
		 * by passing null and false to the parameters oldActivityId and edit respectively
		 * since this is creating a new activity
		 */
		saveActivity(activity,null,false,commentsCol,serializeFlag,field);
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
	public static void saveActivity(AmpActivity activity,Long oldActivityId,boolean edit,ArrayList commentsCol,boolean serializeFlag, Long field) {
		logger.debug("In save activity " + activity.getName());
		Session session = null;
		Transaction tx = null;
		AmpActivity oldActivity = null;
		
		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			
			if (edit) { /* edit an existing activity */
				oldActivity = DbUtil.getProjectChannelOverview(oldActivityId);
				
				if (oldActivity == null) {
					logger.debug("Previous Activity is null");
				}
				
				/* delete previos fundings and funding details */
				Set fundSet = oldActivity.getFunding();
				if (fundSet != null) {
					Iterator fundSetItr = fundSet.iterator();
					while (fundSetItr.hasNext()) {
						AmpFunding fund = (AmpFunding) fundSetItr.next();
						Set fundDetSet = fund.getFundingDetails();
						if (fundDetSet != null) {
							Iterator fundDetItr = fundDetSet.iterator();
							while (fundDetItr.hasNext()) {
								AmpFundingDetail ampFundingDetail = (AmpFundingDetail) fundDetItr.next();
								session.delete(ampFundingDetail);
							}
						}
						session.delete(fund);
					}
				}
				
				/* delete all previous components */
				Set comp = oldActivity.getComponents();
				if (comp != null) {
					Iterator compItr = comp.iterator();
					while (compItr.hasNext()) {
						AmpComponent ampComp = (AmpComponent) compItr.next();
						
						/* delete physical progress items */
						Set phyProg = ampComp.getPhysicalProgress();
						if (phyProg != null) {
							Iterator phyProgItr = phyProg.iterator();
							while (phyProgItr.hasNext()) {
								AmpPhysicalPerformance phyProf = (AmpPhysicalPerformance) phyProgItr.next();
								session.delete(phyProf);
							}
						}
						session.delete(ampComp);
					}
				}
				
				/* delete all activity internal Ids*/
				Set intIds = oldActivity.getInternalIds();
				if (intIds != null) {
					Iterator intIdItr = intIds.iterator();
					while (intIdItr.hasNext()) {
						AmpActivityInternalId intId = (AmpActivityInternalId) intIdItr.next();
						session.delete(intId);
					}
				}
				
				/* delete all previous closing dates */
				Set closeDates = oldActivity.getClosingDates();
				if (closeDates != null) {
					Iterator dtItr = closeDates.iterator();
					while (dtItr.hasNext()) {
						AmpActivityClosingDates date = (AmpActivityClosingDates) dtItr.next();
						session.delete(date);
					}
				}
				
				/* delete all previous comments */
				ArrayList col = DbUtil.getAllCommentsByField(field,oldActivity.getAmpActivityId());
				logger.debug("col.size() [Inside deleting]: " + col.size());
					if (col != null) {
						Iterator itr = col.iterator();
						while (itr.hasNext()) {
							AmpComments comObj = (AmpComments) itr.next();
							session.delete(comObj);
						}
					}
				logger.debug("comments deleted");
				
				activity.setAmpActivityId(oldActivityId);
			}
			
			/* Persists document objects, of type CMSContentItem, related to the activity */
			Set docsSet = activity.getDocuments();
			if (docsSet != null) {
				Iterator docItr = docsSet.iterator();
				while (docItr.hasNext()) {
					CMSContentItem cmsItem = (CMSContentItem) docItr.next();
					session.save(cmsItem);
				}
			}
			
			/* Persists the activity */
			if (edit) {
				// update the activity
				activity.setActivityCreator(oldActivity.getActivityCreator());
				activity.setCreatedDate(oldActivity.getCreatedDate());
				session.update(activity);
			} else {
				// create the activity
				//activity.setMember(new HashSet());
				//activity.getMember().add(activity.getActivityCreator());
				session.save(activity);
				
				String qryStr = "select tm from " + AmpTeamMember.class.getName() + " tm " +
					"where (tm.ampTeamMemId=:id)";
				Query qry = session.createQuery(qryStr);
				qry.setParameter("id",activity.getActivityCreator().getAmpTeamMemId(),Hibernate.LONG);
				Iterator tmpItr = qry.list().iterator();
				AmpTeamMember member = null;
				if (tmpItr.hasNext()) {
					member = (AmpTeamMember) tmpItr.next();
					logger.debug("Number of member activities is " + member.getActivities().size());
					Collection memActivites = member.getActivities();
					if (memActivites  == null) {
						memActivites = new ArrayList();
					}
					boolean flag = false;
					Iterator tmp = memActivites.iterator();
					while (tmp.hasNext()) {
						AmpActivity act = (AmpActivity) tmp.next();
						if (act.getAmpActivityId() != null &&
								act.getAmpActivityId().equals(activity.getAmpActivityId())) {
							flag = true;
							break;
						}
					}
					if (!flag) {
						memActivites.add(activity);
					}
					member.setActivities(new HashSet(memActivites));
					session.update(member);
				}				
			}
			
		
			/* Persists comments, of type AmpComments, related to the activity */
			if (!commentsCol.isEmpty()) {
				logger.debug("commentsCol.size() [Inside Persisting]: " + commentsCol.size());
				boolean flag = true;
				/*
				if (edit && serializeFlag)
					flag = false; */
				logger.debug("flag [Inside Persisting comments]: " + flag);
				/*if (flag) {*/
					Iterator itr = commentsCol.iterator();
					while (itr.hasNext()) {
						AmpComments comObj = (AmpComments) itr.next();
						comObj.setAmpActivityId(activity);
						session.save(comObj);
						logger.debug("Comment Saved [AmpCommentId] : " + comObj.getAmpCommentId());
					}
			} else
				logger.debug("commentsCol is empty !");
			
			/*
			 * Persists the activity closing dates 
			 */
			Set closeDates = activity.getClosingDates();
			if (closeDates != null && closeDates.size() > 0) {
				Iterator datesItr = closeDates.iterator();
				while (datesItr.hasNext()) {
					AmpActivityClosingDates date = (AmpActivityClosingDates) datesItr.next();
					session.save(date);
				}
			}
			
			/* 
			 * Persists the activity internal ids
			 * This is done after saving activity since this requires the activity id
			 * as one of the key, which will be got only after saving the activity object
			 */ 
			
			Set intIdSet = activity.getInternalIds();
			if (intIdSet != null) {
				Iterator intIdItr = intIdSet.iterator();
				while (intIdItr.hasNext()) {
					AmpActivityInternalId intId = (AmpActivityInternalId) intIdItr.next();
					intId.setAmpActivityId(activity.getAmpActivityId());
					session.save(intId);
				}
			}
			
			/* Persists components physical performance object */
			logger.info("Here 1");
			Set compSet = activity.getComponents();
			if (compSet != null || compSet.size() > 0) {
				logger.info("Inside if");
				Iterator compItr = compSet.iterator();
				logger.info("Got iterator");
				while (compItr.hasNext()) {
					AmpComponent comp = (AmpComponent) compItr.next();
					logger.info("Saving component " + comp.getTitle());
					session.save(comp);
					Set phyProgSet = comp.getPhysicalProgress();
					if (phyProgSet != null) {
						Iterator phyProgItr = phyProgSet.iterator();
						while (phyProgItr.hasNext()) {
							AmpPhysicalPerformance phyPerf = (AmpPhysicalPerformance) phyProgItr.next();
							logger.info("Saving phy perf " + phyPerf.getTitle());
							session.save(phyPerf);
						}
					}					
				}
				logger.info("end while");
			} else {
				logger.info("Component is either null or size is equal to 0");
			}
			
			/* Persists the funding,funding details and closing date history objects */
			Set fundSet = activity.getFunding();
			if (fundSet != null) {
				logger.debug("Fundings present");
				Iterator fundItr = fundSet.iterator();
				while (fundItr.hasNext()) {
					AmpFunding funding = (AmpFunding) fundItr.next();
					session.save(funding); // saves funding
					
					Set fundDetSet = funding.getFundingDetails();
					if (fundDetSet != null) {
						Iterator fundDetItr = fundDetSet.iterator();
						while (fundDetItr.hasNext()) {
							AmpFundingDetail funDet = (AmpFundingDetail) fundDetItr.next();
							session.save(funDet); // saves funding details
						}
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
	}
	
	public static Collection getComponents(Long actId) {
		 Session session = null;
		 Collection col = new ArrayList();

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
			AmpActivity act = (AmpActivity) session.load(AmpActivity.class,actId);
				
			if (act.getOrgrole() != null) {
				Collection orgRoles = act.getOrgrole();
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
				
				if (ampAct.getObjective().length() > 200) {
					activity.setObjective(ampAct.getObjective().substring(0,200));
					activity.setObjMore(true);
				} else {
					activity.setObjective(ampAct.getObjective());
					activity.setObjMore(false);					
				}
				
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
						AmpSector sec = (AmpSector) sectItr.next();
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

} // End
