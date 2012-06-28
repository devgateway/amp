package org.digijava.module.aim.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.AmpProject;
import org.digijava.module.aim.helper.AmpProjectDonor;
import org.digijava.module.aim.helper.Commitments;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.Sector;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

public class DesktopUtil {

	private static Logger logger = Logger.getLogger(DesktopUtil.class);


	public static Collection getAllChildrenIds(Long teamId) {
		Session session = null;
		Collection childTeamId = new ArrayList();
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String qryStr = "select t.ampTeamId from " + AmpTeam.class.getName() + " t where" +
					" (t.parentTeamId=:id)";
			logger.info("GetAllChildrens 1= " + qryStr);
			Query qry = session.createQuery(qryStr);
			qry.setParameter("id",teamId,Hibernate.LONG);
			col = qry.list();
			while (col != null && col.size() > 0) {
				String qryParam = "";
				Iterator itr = col.iterator();
				while (itr.hasNext()) {
					Long tId = (Long) itr.next();
					childTeamId.add(tId);
					if (qryParam.length() != 0)
						qryParam += ",";
					qryParam += tId;
				}
				qryStr = "select t.ampTeamId from " + AmpTeam.class.getName() + " t where" +
					" t.parentTeamId in (" + qryParam + ")";
				logger.info("GetAllChildrens 2= " + qryStr);
				qry = session.createQuery(qryStr);
				col = qry.list();
			}
		} catch (Exception e) {
			logger.error("Exception from getAllChildrenIds " + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed " +
							rsf.getMessage());
				}
			}
		}
		return childTeamId;
	}

	public static Collection getActivities(Collection teamIds,boolean team,Long memberId) {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getSession();
			if (teamIds != null) {
				Iterator itr = teamIds.iterator();
				String param = "";
				while (itr.hasNext()) {
					Long teamId = (Long) itr.next();
					if (param.length() > 0) {
						param += ",";
					}
					param += teamId;
				}


				if (team) {
					qryStr = "select a from " + AmpActivity.class.getName() + " a" +
							" where a.team in (" + param + ") and (a.activityCreator=:mId " +
									"or a.approvalStatus like 'approved' or a.approvalStatus like 'edited')";
					qry = session.createQuery(qryStr);
					qry.setParameter("mId",memberId,Hibernate.LONG);
				} else {
					qryStr = "select a from " + AmpActivity.class.getName() + " a " +
							"where a.team in (" + param + ") and a.approvalStatus like 'approved'";
					qry = session.createQuery(qryStr);
				}

				Collection temp=null;
				try {
					temp = qry.list();
				} catch (Exception e) {
					// TODO this is warkaround for short time, problem is described in AMP-1059
					e.printStackTrace();
					logger.error(e);
				}
				if (temp != null && temp.size() > 0) {
					itr = temp.iterator();
					while (itr.hasNext()) {
						AmpActivity act = (AmpActivity) itr.next();
						AmpProject project = new AmpProject();
						project.setAmpActivityId(act.getAmpActivityId());
						project.setAmpId(act.getAmpId());
						project.setName(act.getName());
						project.setApprovalStatus(act.getApprovalStatus());
//						AmpStatus status = act.getStatus(); // TO BE DELETE
						AmpCategoryValue statusValue	= CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, act.getCategories());
						if (statusValue != null)
							project.setStatusId(statusValue.getId());
						project.setActivityRisk(MEIndicatorsUtil.getOverallRisk(act.getAmpActivityId()));
						project.setLineMinRank(act.getLineMinRank());
						project.setPlanMinRank(act.getPlanMinRank());

						project.setSector(new ArrayList());
						Set sectSect = act.getSectors();
						if (sectSect != null) {
							Iterator sItr = sectSect.iterator();
							while (sItr.hasNext()) {
								Object x=sItr.next();
								if(x==null) continue;
								AmpSector sect = ((AmpActivitySector)x).getSectorId();
								Sector sector = new Sector();
								sector.setSectorId(sect.getAmpSectorId());
								sector.setSectorName(sect.getName());
								project.getSector().add(sector);
							}
						}

						project.setCommitmentList(new ArrayList());
						project.setDonor(new ArrayList());

						Set fundings = act.getFunding();
						if (fundings != null) {
							Iterator fItr = fundings.iterator();
							while (fItr.hasNext()) {
								AmpFunding funding = (AmpFunding) fItr.next();
								AmpProjectDonor projDonor = new AmpProjectDonor();
								if(funding.getAmpDonorOrgId()==null)
									continue;
									
								projDonor.setAmpDonorId(funding.getAmpDonorOrgId().getAmpOrgId());
								projDonor.setDonorName(funding.getAmpDonorOrgId().getName());
								if (project.getDonor().contains(projDonor) == false)
									project.getDonor().add(projDonor);
									
								Iterator fdItr = funding.getFundingDetails().iterator();
								while (fdItr.hasNext()) {
									AmpFundingDetail fd = (AmpFundingDetail) fdItr.next();
									if (fd.getAdjustmentType().intValue() == Constants.ACTUAL &&
											fd.getTransactionType().intValue() == Constants.COMMITMENT) {
										Commitments comm = new Commitments();
										comm.setDonorId(funding.getAmpDonorOrgId().getAmpOrgId());
										comm.setAmount(fd.getTransactionAmount().doubleValue());
										comm.setCurrencyCode(fd.getAmpCurrencyId().getCurrencyCode());
										comm.setTransactionDate(fd.getTransactionDate());
										project.getCommitmentList().add(comm);
									}
								}
							}
						}
						project.setTotalCommited("");
						col.add(project);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
            throw new RuntimeException("Exception from getActivities()", e);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed " +
							rsf.getMessage());
				}
			}
		}
		return col;
	}

	public static Collection getAmpProjects(Collection actIds) {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			if (actIds != null && actIds.size() > 0) {
				Iterator itr = actIds.iterator();
				String params = "";
				while (itr.hasNext()) {
					Long actId = (Long) itr.next();
					if (params.length() > 0) {
						params += ",";
					}
					params += actId;
				}

				String qryStr = "select a from " + AmpActivity.class.getName() + " a" +
						" where a.ampActivityId in (" + params + ")";

				Query qry = session.createQuery(qryStr);

				Collection temp = qry.list();
				if (temp != null && temp.size() > 0) {
					itr = temp.iterator();
					while (itr.hasNext()) {
						AmpActivity act = (AmpActivity) itr.next();
						AmpProject project = new AmpProject();
						project.setAmpActivityId(act.getAmpActivityId());
						project.setAmpId(act.getAmpId());
						project.setName(act.getName());
						project.setApprovalStatus(act.getApprovalStatus());
//						AmpStatus status = act.getStatus(); // TO BE DELETED
						AmpCategoryValue statusValue	= CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, act.getCategories());
						if (statusValue != null)
							project.setStatusId(statusValue.getId());
						project.setActivityRisk(MEIndicatorsUtil.getOverallRisk(act.getAmpActivityId()));

						project.setSector(new ArrayList());
						Set sectSect = act.getSectors();
						if (sectSect != null || !sectSect.isEmpty() || sectSect.iterator()!=null) {
							
							Iterator sItr = sectSect.iterator();
							//////System.out.println(sItr.toString()+" -----------------------");
							while (sItr.hasNext()) {
								AmpActivitySector asect = (AmpActivitySector) sItr.next();
								
								if(asect!=null)
									if (asect.getSectorId()!=null) {
  								//AmpSector sect = ((AmpActivitySector) sItr.next()).getSectorId();
										AmpSector sect = asect.getSectorId();
										Sector sector = new Sector();
										sector.setSectorId(sect.getAmpSectorId());
										sector.setSectorName(sect.getName());
										project.getSector().add(sector);
									}
							}
						}

						project.setCommitmentList(new ArrayList());
						project.setDonor(new ArrayList());

						Set fundings = act.getFunding();
						if (fundings != null) {
							Iterator fItr = fundings.iterator();
							while (fItr.hasNext()) {
								AmpFunding funding = (AmpFunding) fItr.next();
								AmpProjectDonor projDonor = new AmpProjectDonor();
								projDonor.setAmpDonorId(funding.getAmpDonorOrgId().getAmpOrgId());
								projDonor.setDonorName(funding.getAmpDonorOrgId().getName());
								if (project.getDonor().contains(projDonor) == false)
									project.getDonor().add(projDonor);

								Iterator fdItr = funding.getFundingDetails().iterator();
								while (fdItr.hasNext()) {
									AmpFundingDetail fd = (AmpFundingDetail) fdItr.next();
									if (fd.getAdjustmentType().intValue() == Constants.ACTUAL &&
											fd.getTransactionType().intValue() == Constants.COMMITMENT) {
										Commitments comm = new Commitments();
										comm.setDonorId(funding.getAmpDonorOrgId().getAmpOrgId());
										comm.setAmount(fd.getTransactionAmount().doubleValue());
										comm.setCurrencyCode(fd.getAmpCurrencyId().getCurrencyCode());
										comm.setTransactionDate(fd.getTransactionDate());
										project.getCommitmentList().add(comm);
									}
								}
							}
						}
						project.setTotalCommited("");
						col.add(project);
					}
				}
			}
		} catch (Exception e) {
            throw new RuntimeException("Exception from getAmpProjects", e);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed " +
							rsf.getMessage());
				}
			}
		}
		return col;
	}

	public static Collection getDesktopActivities(Long teamId,
			Long memberId,boolean teamLead) {

		Session session = null;
		Collection projects = new ArrayList();

		try {
			session = PersistenceManager.getSession();

			if (teamLead) {
				// Load activities for the team leader
				AmpTeam team = (AmpTeam) session.load(AmpTeam.class,teamId);

				/*	AMP-3476
				if (team.getTeamCategory() != null && team.getTeamCategory().
						equalsIgnoreCase(Constants.DEF_DNR_PERSPECTIVE)) {
					// Donor team
					if (team.getAccessType() != null && team.getAccessType().
							equalsIgnoreCase(Constants.TYPE_MNGMT)) {
						// Management team
						logger.info("Loading activities for Donor managemnet team leader");
						Collection childTeamId = getAllChildrenIds(teamId);

						Iterator temp = childTeamId.iterator();
						while (temp.hasNext()) {
							Long id = (Long) temp.next();
							logger.info("id = " + id);
						}

						projects = getActivities(childTeamId,false,memberId);
					} else {
						// Working team
						Set act = team.getActivityList();
						if (act != null) {
							Iterator actItr = act.iterator();
							Collection actIds = new ArrayList();
							while (actItr.hasNext()) {
								AmpActivity tmpAct = (AmpActivity) actItr.next();
								actIds.add(tmpAct.getAmpActivityId());
							}
							projects = getAmpProjects(actIds);
						}
					}
				} else {
				*/
					// Mofed team
					
					Collection temp = new ArrayList();
					if (team.getAccessType() != null && team.getAccessType().
							equalsIgnoreCase(Constants.TYPE_MNGMT)) {
						temp = getAllChildrenIds(teamId);
						projects = getActivities(temp,false,memberId);
					} else {
						temp.add(teamId);
						projects = getActivities(temp,true,memberId);
					}
				//}
			} else {
				// Load the activities for the team member
				AmpTeamMember tm = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
				Set act = tm.getActivities();
				if (act != null) {
					Iterator actItr = act.iterator();
					Collection actIds = new ArrayList();
					while (actItr.hasNext()) {
						AmpActivity tmpAct = (AmpActivity) actItr.next();
						actIds.add(tmpAct.getAmpActivityId());
					}
					projects = getAmpProjects(actIds);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
            throw new RuntimeException("Exception from getDesktopActivties", e);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed " +
							rsf.getMessage());
				}
			}
		}

		return projects;
	}

	public static Collection getPaginatedActivityList(ArrayList actList,int numRecords,int pageNo) {
		Collection col = new ArrayList();
		int stIndex = (pageNo - 1) * numRecords;
		int edIndex = stIndex + numRecords;
		if (edIndex > actList.size()) edIndex = actList.size();
		for (int i = stIndex;i < edIndex;i ++) {
			col.add(actList.get(i));
		}

		return col;
	}

	public static double updateProjectTotals(Collection activities,String currCode) {

		logger.info("updateProjectTotals called with currcode =" + currCode);

		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		double grandTotal = 0;
		if (activities != null) {
			Iterator itr = activities.iterator();
			while (itr.hasNext()) {
				AmpProject project = (AmpProject) itr.next();
				double totAmount = 0;
				if (project.getCommitmentList() != null &&
						project.getCommitmentList().size() > 0) {
					Iterator cItr = project.getCommitmentList().iterator();
					while (cItr.hasNext()) {
						Commitments comm = (Commitments) cItr.next();
						double toCurrency = Util.getExchange(currCode, new java.sql.Date( comm.getTransactionDate().getTime()));
						double fromCurrency = Util.getExchange(comm.getCurrencyCode(),new java.sql.Date(comm.getTransactionDate().getTime()));
						totAmount += CurrencyWorker.convert1(comm.getAmount(),fromCurrency, toCurrency);
					}
				}
				grandTotal += totAmount;
				if (totAmount <= 0) {
					project.setTotalCommited("");
				} else {
					project.setTotalCommited(mf.format(totAmount));
				}
			}
		}
		return grandTotal;
	}

	public static ArrayList searchActivities(Collection actIds,String searchKey) {
		Session session = null;
		ArrayList col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		Collection act = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String params = "";
			if (actIds != null && actIds.size() > 0) {
				Iterator itr = actIds.iterator();
				while (itr.hasNext()) {
					Long id = (Long) itr.next();
					if (params.length() > 0) {
						params += ",";
					}
					params += id;
				}

				qryStr = "select a.ampActivityId,a.description,a.objective from " +
						"" + AmpActivity.class.getName() + " a where a.ampActivityId in (" +
								"" + params + ")";
				qry = session.createQuery(qryStr);
				Iterator tmp = qry.list().iterator();
				Editor ed = null;
				while (tmp.hasNext()) {
					Object[] obj = (Object[]) tmp.next();
					Long actId = (Long) obj[0];
					String descKey = (String) obj[1];
					String objKey = (String) obj[2];
					ed = org.digijava.module.editor.util.DbUtil.getEditor(descKey,"en");
					if (ed != null && ed.getBody() != null && ed.getBody().trim().length() > 0) {
						String temp = DbUtil.getDescParsed(ed.getBody()).toLowerCase();
						if (temp.indexOf(searchKey) > -1) {
							act.add(actId);
							continue;
						}
					}
					ed = org.digijava.module.editor.util.DbUtil.getEditor(objKey,"en");
					if (ed != null && ed.getBody() != null && ed.getBody().trim().length() > 0) {
						String temp = DbUtil.getDescParsed(ed.getBody()).toLowerCase();
						if (temp.indexOf(searchKey) > -1) {
							act.add(actId);
						}
					}
				}
				if (act.size() > 0)
					col = (ArrayList) getAmpProjects(act);
			}

		} catch (Exception e) {
			logger.error("Exception from searchActivities " + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed " +
							rsf.getMessage());
				}
			}
		}
		return col;
	}
	public static Collection getActivitiesTobeClosed(Long ampTeamId)
	{
		Collection actList = new ArrayList();
		Session session = null;
		Query q = null;
        Date currentDate = new Date();
		try {
			session = PersistenceManager.getSession();
			String queryString = "select act from " + AmpActivity.class.getName()
									+ " act where (act.team=:ampTeamId)" 
									+ " and (act.actualCompletionDate is not null)"  
									+ " and (act.actualCompletionDate>=:currentDate)";
			q = session.createQuery(queryString);
			q.setParameter("ampTeamId", ampTeamId, Hibernate.LONG);
			q.setParameter("currentDate", currentDate, Hibernate.DATE);
			actList = q.list();
		} catch (Exception ex) {
			logger.error("Unable to get AmpActivity [getCreatedOrEditedActivities()]", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		logger.debug("Getting CreatedOrEdited activities Executed successfully ");
		return actList;
	}	
}
