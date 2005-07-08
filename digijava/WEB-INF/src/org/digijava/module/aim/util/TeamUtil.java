/*
 * TeamUtil.java Created: 07-Apr-2005
 */

package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.UpdateDB;
import org.digijava.module.aim.helper.Workspace;

/**
 * Persister class for all Team/Workspaces related Objects
 * 
 * @author priyajith
 */
public class TeamUtil {

	private static Logger logger = Logger.getLogger(TeamUtil.class);

	/**
	 * The function will get all the teams/workspaces which is unassociated with
	 * a parent team. It will also filter the teams based on the 'workspace
	 * type' and 'team category'
	 * 
	 * @param workspaceType
	 * @param teamCategory
	 * @return The collection of all teams
	 */
	public static Collection getUnassignedWorkspaces(String workspaceType,
			String teamCategory) {

		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();

			// get all teams whose 'parent id' is set to null
			String qryStr = "select t from " + AmpTeam.class.getName() + " t"
					+ " where t.parentTeamId is null";

			boolean wTypeFlag = false;
			boolean tCatFlag = false;

			// if user has specified a workspaceType filter
			if (workspaceType != null && workspaceType.trim().length() != 0) {
				qryStr += " and (t.accessType=:wType)";
				wTypeFlag = true;
			}
			// if user has specified team category filter
			if (teamCategory != null && teamCategory.trim().length() != 0) {
				qryStr += " and (t.type=:tCat)";
				tCatFlag = true;
			}

			Query qry = session.createQuery(qryStr);
			if (wTypeFlag) {
				qry.setParameter("wType", workspaceType, Hibernate.STRING);
			}
			if (tCatFlag) {
				qry.setParameter("tCat", teamCategory, Hibernate.STRING);
			}

			col = qry.list();

		} catch (Exception e) {
			logger.error("Exception from getUnassignedWorkspcaes");
			logger.error(e.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release Session failed");
				}
			}
		}
		return col;
	}

	public static Collection getAllTeams(Long teamId[]) {
		Session session = null;
		Collection col = new ArrayList();

		try {
			if (teamId != null && teamId.length > 0) {
				session = PersistenceManager.getSession();
				StringBuffer qryBuf = new StringBuffer();
				qryBuf.append("select t from ");
				qryBuf.append(AmpTeam.class.getName());
				qryBuf.append(" t where t.ampTeamId in (");
				for (int i = 0; i < teamId.length; i++) {
					if (teamId[i] != null) {
						qryBuf.append(teamId[i]);
						if ((i + 1) != teamId.length) {
							qryBuf.append(",");
						}
					}
				}
				qryBuf.append(")");
				Query qry = session.createQuery(qryBuf.toString());
				col = qry.list();
			}
		} catch (Exception e) {
			logger.error("Execption from getAllTeams");
			logger.error(e.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
		return col;
	}

	/**
	 * Creates a new team
	 * 
	 * @param team
	 *            The team object which is to be persisted
	 * @param childTeams
	 *            The collection of child teams which is to be associated with
	 *            the parent team
	 * @return true if the team is successfully created else returns false
	 */
	public static boolean createTeam(AmpTeam team, Collection childTeams) {
		boolean teamExist = false;
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();

			// check whether a team with the same name already exist
			String qryStr = "select t from " + AmpTeam.class.getName() + " t "
					+ "where (t.name=:name)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("name", team.getName(), Hibernate.STRING);
			Collection col = qry.list();
			if (col.size() > 0) {
				throw new AimException("Cannot create team: The team name "
						+ team.getName() + " already exist");
			} else {
				// save the new team
				session.save(team);

				qryStr = "select fiscal from "
						+ AmpFiscalCalendar.class.getName() + " fiscal "
						+ "where (fiscal.name=:cal)";
				qry = session.createQuery(qryStr);
				qry.setParameter("cal", "Ethiopian Fiscal Calendar", Hibernate.STRING);
				col = qry.list();
				AmpFiscalCalendar fiscal = null;
				if (col.size() > 0) {
					Iterator itr = col.iterator();
					if (itr.hasNext()) {
						fiscal = (AmpFiscalCalendar) itr.next();
					}
				}
				qryStr = "select curr from " + AmpCurrency.class.getName()
						+ " curr " + "where (curr.currencyCode=:code)";
				qry = session.createQuery(qryStr);
				qry.setParameter("code", "USD", Hibernate.STRING);
				col = qry.list();
				AmpCurrency curr = null;
				if (col.size() > 0) {
					Iterator itr = col.iterator();
					if (itr.hasNext()) {
						curr = (AmpCurrency) itr.next();
					}
				}

				// create application settings for the team and save
				AmpApplicationSettings ampAppSettings = new AmpApplicationSettings();
				ampAppSettings.setTeam(team);
				ampAppSettings.setMember(null);
				ampAppSettings.setDefaultRecordsPerPage(new Integer(10));
				ampAppSettings.setCurrency(curr);
				ampAppSettings.setFiscalCalendar(fiscal);
				ampAppSettings.setLanguage("English");
				ampAppSettings.setDefaultPerspective("MOFED");
				session.save(ampAppSettings);

				// update all child workspaces parent team
				if (childTeams != null && childTeams.size() > 0) {
					Iterator itr = childTeams.iterator();
					while (itr.hasNext()) {
						AmpTeam childTeam = (AmpTeam) itr.next();
						childTeam.setParentTeamId(team);
						session.update(childTeam);
					}
				}

				// commit the changes
				tx.commit();
			}
		} catch (AimException ae) {
			teamExist = true;
			logger.error("Execption from createTeam()");
			logger.error(ae.getMessage());
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} catch (Exception e) {
			logger.error("Execption from createTeam()");
			logger.error(e.getMessage());
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

		return teamExist;
	}

	/**
	 * Retreives the team/workspace information
	 * 
	 * @param id
	 *            The teamId
	 * @return The Workspace object
	 */
	public static Workspace getWorkspace(Long id) {
		Workspace workspace = null;
		Session session = null;

		try {
			session = PersistenceManager.getSession();
			String qryStr = "select t from " + AmpTeam.class.getName() + " t "
					+ "where (t.ampTeamId=:id)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				AmpTeam team = (AmpTeam) itr.next();
				workspace = new Workspace();
				workspace.setDescription(team.getDescription().trim());
				workspace.setId(team.getAmpTeamId().toString());
				workspace.setName(team.getName());
				workspace.setTeamCategory(team.getType());
				workspace.setWorkspaceType(team.getAccessType());
				qryStr = "select count(*) from "
						+ AmpTeamMember.class.getName() + " t "
						+ "where (t.ampTeam=:teamId)";
				qry = session.createQuery(qryStr);
				qry.setParameter("teamId", team.getAmpTeamId(), Hibernate.LONG);
				Iterator itr1 = qry.list().iterator();
				int numMem = 0;
				if (itr1.hasNext()) {
					Integer num = (Integer) itr1.next();
					numMem = num.intValue();
				}
				if (numMem == 0) {
					workspace.setHasMembers(false);
				} else {
					workspace.setHasMembers(true);
				}
				qryStr = "select count(*) from " + AmpActivity.class.getName()
						+ " act " + "where (act.team=:teamId)";
				qry = session.createQuery(qryStr);
				qry.setParameter("teamId", team.getAmpTeamId(), Hibernate.LONG);
				itr1 = qry.list().iterator();
				int numAct = 0;
				if (itr1.hasNext()) {
					Integer num = (Integer) itr1.next();
					numAct = num.intValue();
				}
				if (numAct == 0) {
					workspace.setHasActivities(false);
				} else {
					workspace.setHasActivities(true);
				}
				qryStr = "select t from " + AmpTeam.class.getName() + " t "
						+ "where (t.parentTeamId=:teamId)";
				qry = session.createQuery(qryStr);
				qry.setParameter("teamId", team.getAmpTeamId(), Hibernate.LONG);
				itr1 = qry.list().iterator();
				Collection childWorkspaces = new ArrayList();
				while (itr1.hasNext()) {
					AmpTeam childTeam = (AmpTeam) itr1.next();
					childWorkspaces.add(childTeam);
				}
				workspace.setChildWorkspaces(childWorkspaces);
			}
		} catch (Exception e) {
			logger.error("Exception from getWorkspace()");
			logger.error(e.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}

		return workspace;
	}

	/**
	 * Updates a team/workspace
	 * 
	 * @param team
	 * @param childTeams
	 * @return
	 */
	public static boolean updateTeam(AmpTeam team, Collection childTeams) {
		boolean teamExist = false;
		Session session = null;
		Transaction tx = null;

		try {

			session = PersistenceManager.getSession();
			tx = session.beginTransaction();

			// check whether a team with the same name already exist
			String qryStr = "select t from " + AmpTeam.class.getName() + " t "
					+ "where (t.name=:name)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("name", team.getName(), Hibernate.STRING);
			Iterator tempItr = qry.list().iterator();
			if (tempItr.hasNext()) {
				AmpTeam tempTeam = (AmpTeam) tempItr.next();
				if (!(tempTeam.getAmpTeamId().equals(team.getAmpTeamId()))) {
					throw new AimException("Cannot create team: The team name "
							+ team.getName() + " already exist");
				}
			}

			logger.debug("Updating team....");

			qryStr = "select t from " + AmpTeam.class.getName() + " t "
					+ "where (t.ampTeamId=:id)";
			qry = session.createQuery(qryStr);
			qry.setParameter("id", team.getAmpTeamId(), Hibernate.LONG);
			logger.debug("Getting the team with id " + team.getAmpTeamId());
			tempItr = qry.list().iterator();
			if (tempItr.hasNext()) {
				logger.debug("Before update....");
				AmpTeam updTeam = (AmpTeam) tempItr.next();
				updTeam.setAccessType(team.getAccessType());
				updTeam.setDescription(team.getDescription());
				updTeam.setName(team.getName());
				updTeam.setType(team.getType());
				session.saveOrUpdate(updTeam);

				qryStr = "select t from " + AmpTeam.class.getName() + " t "
						+ "where (t.parentTeamId=:parId)";
				qry = session.createQuery(qryStr);
				qry.setParameter("parId", updTeam.getAmpTeamId(),
						Hibernate.LONG);

				Iterator itr = qry.list().iterator();
				while (itr.hasNext()) {
					AmpTeam child = (AmpTeam) itr.next();
					child.setParentTeamId(null);
					session.saveOrUpdate(child);
				}

				logger.debug("Team updated");

				if (childTeams != null && childTeams.size() > 0) {
					itr = childTeams.iterator();
					logger.info("Size " + childTeams.size());
					while (itr.hasNext()) {
						AmpTeam childTeam = (AmpTeam) itr.next();
						AmpTeam upChildTeam = (AmpTeam) session.load(
								AmpTeam.class, childTeam.getAmpTeamId());
						upChildTeam.setParentTeamId(updTeam);
						session.saveOrUpdate(upChildTeam);
					}
				}
				// commit the changes
				tx.commit();
			}

		} catch (AimException ae) {
			teamExist = true;
			logger.error("Execption from updateTeam() :" + ae.getMessage());
			ae.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} catch (Exception e) {
			logger.error("Execption from updateTeam() :" + e.getMessage());
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
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
		return teamExist;
	}

	/**
	 * Removes a team
	 * 
	 * @param teamId 
	 * 				The team id of the team to be removed
	 */
	public static void removeTeam(Long teamId) {
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();

			String qryStr = "select t from " + AmpTeam.class.getName() + " t "
					+ "where (t.ampTeamId=:id)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("id", teamId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();

			AmpTeam delTeam = null;
			if (itr.hasNext()) {
				delTeam = (AmpTeam) itr.next();
			}

			if (delTeam != null) {
				qryStr = "select t from " + AmpTeam.class.getName() + " t "
						+ "where (t.parentTeamId=:id)";
				qry = session.createQuery(qryStr);
				qry.setParameter("id", teamId, Hibernate.LONG);
				itr = qry.list().iterator();
				while (itr.hasNext()) {
					AmpTeam childTeam = (AmpTeam) itr.next();
					childTeam.setParentTeamId(null);
					session.update(childTeam);
				}

				qryStr = "select app from "
						+ AmpApplicationSettings.class.getName() + " app "
						+ "where (app.team=:id) and app.member is null";
				qry = session.createQuery(qryStr);
				qry.setParameter("id", delTeam.getAmpTeamId(), Hibernate.LONG);
				itr = qry.list().iterator();

				if (itr.hasNext()) {
					AmpApplicationSettings appSet = (AmpApplicationSettings) itr
							.next();
					session.delete(appSet);
				}
				session.delete(delTeam);
			}
			tx.commit();
		} catch (Exception e) {
			logger.error("Execption from removeTeam() :" + e.getMessage());
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
	}

	/**
	 * Return an AmpTeam object corresponding to the id
	 * @param id The id of the team whose object is to be returned.
	 * @return The AmpTeam object
	 */
	public static AmpTeam getAmpTeam(Long id) {
		Session session = null;
		Query qry = null;
		AmpTeam team = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select t from " + AmpTeam.class.getName()
					+ " t where (t.ampTeamId=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				team = (AmpTeam) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team" + e.getMessage());
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return team;
	}
	
	public static void addTeamMember(AmpTeamMember member,AmpApplicationSettings appSettings) {
		Session session = null;
		Transaction tx = null;
		
		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			session.save(member);
			session.save(appSettings);
			logger.debug("Team Head = " + member.getAmpMemberRole().getTeamHead().booleanValue());
			logger.debug("Role Name = " + member.getAmpMemberRole().getRole());
			if (member.getAmpMemberRole().getTeamHead().booleanValue() == true) {
				AmpTeam team = member.getAmpTeam();
				team.setTeamLead(member);
				session.update(team);
			}
			tx.commit();				
		} catch (Exception e) {
			logger.error("Exception from addTeamMember :" + e);
			if (tx != null) {
				try {
					tx.rollback();	
				} catch (Exception rbf) {
					logger.error("Rollback failed :" + rbf);
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf);
				}
			}
		}
	}
	
	public static boolean isMemberExisting(Long teamId,String email) {
		Session session = null;
		Query qry = null;
		String qryStr = null;
		Iterator itr = null;
		boolean memberExist = false;

		try {
			session = PersistenceManager.getSession();
			qryStr = "select u.id from " + User.class.getName() + " u " +
					"where (u.email=:email)";
			qry = session.createQuery(qryStr);
			qry.setParameter("email",email,Hibernate.STRING);
			itr = qry.list().iterator();
			logger.debug("Here #1");
			if (itr.hasNext()) {
				Long id = (Long) itr.next();
				logger.debug("Id is " + id);
				qryStr = "select tm.ampTeam from " + AmpTeamMember.class.getName() + "" +
						" tm where (tm.user=:id)";
				qry = session.createQuery(qryStr);
				qry.setParameter("id",id,Hibernate.LONG);
				Iterator tempItr = qry.list().iterator();
				logger.debug("Here #2");
				if (tempItr.hasNext()) {
					AmpTeam team = (AmpTeam) tempItr.next();
					logger.debug("Got team " + team.getAmpTeamId());
					logger.debug("Checking " + team.getAmpTeamId() + " and " + 
							teamId);
					if (team.getAmpTeamId().equals(teamId)) {
						logger.debug("member already exist for the team");
						memberExist = true;
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception from isMemberExisting()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
		
		return memberExist;
	}
	
	public static Collection getTeamMembers(String email) {
		 User user = DbUtil.getUser(email);
		 if (user == null) return null;
		
		Session session = null;
		Query qry = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from " + AmpTeamMember.class.getName() + 
			  " tm where (tm.user=:user)";
			qry = session.createQuery(queryString);
			qry.setParameter("user",user.getId(),Hibernate.LONG);
			col = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get TeamMembers" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;		
	}
	
	public static void assignActivitiesToMember(Long memberId,Long activities[]) {
		Session session = null;
		Transaction tx = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
				tx = session.beginTransaction();
				for (int i = 0;i < activities.length;i ++) {
					if (activities[i] != null) {
						AmpActivity activity = (AmpActivity)session.load(
								AmpActivity.class,activities[i]);
						member.getActivities().add(activity);
					}
				}
				session.update(member);
				tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to assign activities" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Roll back failed");
				}
			}
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
	}
	
	public static void removeActivitiesFromMember(Long memberId,Long activities[]) {
		Session session = null;
		Transaction tx = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
				tx = session.beginTransaction();
				for (int i = 0;i < activities.length;i ++) {
					if (activities[i] != null) {
						AmpActivity activity = (AmpActivity)session.load(
								AmpActivity.class,activities[i]);
						member.getActivities().remove(activity);
					}
				}
				session.update(member);
				tx.commit();				
			}
		} catch (Exception e) {
			logger.error("Unable to remove activities" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Roll back failed");
				}
			}
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}		
	}
	
	public static Collection getUnassignedMemberActivities(Long teamId,Long memberId) {
		Collection col = null;
		Collection col1 = null;
		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			
			String queryString = "select act from " + AmpActivity.class.getName() + 
			  " act where (act.team=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id",teamId,Hibernate.LONG);
			col = qry.list();
			
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			
			col.removeAll(member.getActivities());
			logger.debug("Collection size after remove all:" + col.size());
			col1 = new ArrayList();
			Iterator itr = col.iterator();
			while (itr.hasNext()) {
				AmpActivity activity = (AmpActivity) itr.next();
				Iterator orgItr = activity.getOrgrole().iterator();
				Activity act = new Activity();
				act.setActivityId(activity.getAmpActivityId());
				act.setName(activity.getName());
				String donors = "";

				while (orgItr.hasNext()) {
					AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
					if (orgRole.getRole().getRoleCode().equals(Constants.DONOR)) {
						donors += orgRole.getOrganisation().getOrgCode();
					}
				}
				act.setDonors(donors);
				col1.add(act);				
			}			
			
		} catch (Exception e) {
			logger.error("Unable to remove activities" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col1;
	}
	
	public static Collection getUnassignedMemberReports(Long teamId,Long memberId) {
		Collection col = new ArrayList();
		
		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			
			String queryString = "select rep.report from " + AmpTeamReports.class.getName() + 
			  " rep where (rep.team=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id",teamId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpReports rep = (AmpReports) itr.next();
				col.add(rep);
			}
			
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			col.removeAll(member.getReports());
		} catch (Exception e) {
			logger.error("Unable to remove activities" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return col;
	}	
	
	public static void assignReportsToMember(Long memberId,Long reports[]) {
		Session session = null;
		Transaction tx = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
				tx = session.beginTransaction();
				for (int i = 0;i < reports.length;i ++) {
					if (reports[i] != null) {
						AmpReports report = (AmpReports)session.load(
								AmpReports.class,reports[i]);
						member.getReports().add(report);
					}
				}
				session.update(member);
				tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to assign reports" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Roll back failed");
				}
			}
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
	}	
	
	public static void removeReportsFromMember(Long memberId,Long reports[]) {
		Session session = null;
		Transaction tx = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
				tx = session.beginTransaction();
				for (int i = 0;i < reports.length;i ++) {
					if (reports[i] != null) {
						AmpReports report = (AmpReports)session.load(
								AmpReports.class,reports[i]);
						member.getReports().remove(report);
					}
				}
				session.update(member);
				tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to remove reports" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Roll back failed");
				}
			}
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
	}		
	
	public static void removeActivitiesFromTeam(Long activities[]) {
		Session session = null;
		Transaction tx = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			
			for (int i = 0;i < activities.length;i ++) {
			    AmpActivity activity = (AmpActivity) session.load(AmpActivity.class,
			            activities[i]);
			    activity.setTeam(null);
			    Iterator membersItr = activity.getMember().iterator();
			    while (membersItr.hasNext()) {
			        member = (AmpTeamMember) membersItr.next();
			        member.getActivities().remove(activity);
			        session.update(member);
			    }
			    activity.setMember(null); 
			    session.update(activity);
			    session.flush();
			    UpdateDB.updateReportCache(activities[i]);
			}
			
			tx.commit();
		} catch (Exception e) {
			logger.error("Unable to remove activities" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Roll back failed");
				}
			}
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}	    
	}
}
