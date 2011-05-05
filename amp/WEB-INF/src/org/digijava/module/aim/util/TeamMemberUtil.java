/*
 * TeamMemberUtil.java
 * Created : 17-Feb-2006
 */
package org.digijava.module.aim.util;


import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.mondrian.dbentity.EntityHelper;
import org.digijava.module.mondrian.dbentity.OffLineReports;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TeamMemberUtil {

	private static Logger logger = Logger.getLogger(TeamMemberUtil.class);

	public static Long getFundOrgOfUser(Long id) {
		Long orgId = null;
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			AmpTeamMember tm = (AmpTeamMember) session.load(AmpTeamMember.class,
					id);
			User user = tm.getUser();

			String qryStr = "select o.ampOrgId from " + AmpOrganisation.class.getName() + " o " +
					"where (o.name=:name)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("name",user.getOrganizationName(),Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				orgId = (Long) itr.next();
			}
		} catch (Exception e) {
			logger.error("Exception from getFundOrgOfUser()");
			e.printStackTrace(System.out);
		}
//		finally {
//			if (session != null) {
//				try {
//					PersistenceManager.releaseSession(session);
//				} catch (Exception rsf) {
//					logger.error("Release session failed");
//				}
//			}
//		}

		logger.info("OrgId is " + orgId);
		return orgId;
	}

	public static Collection getAllTMExceptTL(Long teamId) {
		Session session = null;
		Query qry = null;
		Collection members = new ArrayList();

			try {
				session = PersistenceManager.getSession();
				String queryString = "select tm from " + AmpTeamMember.class.getName()
									 + " tm where (tm.ampTeam=:teamId)";

				qry = session.createQuery(queryString);
				qry.setParameter("teamId", teamId, Hibernate.LONG);
				Iterator itr = qry.list().iterator();

				while (itr.hasNext()) {
					AmpTeamMember ampMem = (AmpTeamMember) itr.next();
					Long id = ampMem.getAmpTeamMemId();
					User user = UserUtils.getUser(ampMem.getUser().getId());
					String name = user.getName();
					String role = ampMem.getAmpMemberRole().getRole();
					AmpTeamMemberRoles ampRole = ampMem.getAmpMemberRole();
					AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
					TeamMember tm = new TeamMember();
					tm.setMemberId(id);
					tm.setMemberName(name);
					tm.setRoleName(role);
					tm.setEmail(user.getEmail());
					if (ampRole.getAmpTeamMemRoleId().equals(
							headRole.getAmpTeamMemRoleId())) {
						tm.setTeamHead(true);
					} else {
						tm.setTeamHead(false);
						if (ampMem.getActivities() == null) {
							tm.setActivities(new HashSet());
						}
						else
							tm.setActivities(ampMem.getActivities());
					}
					if (!tm.getTeamHead())
						members.add(tm);
				}
			} catch (Exception e) {
				logger.error("Unable to get all team members [getAllTMExceptTL()]", e);
			} finally {
				try {
					if (session != null) {
						PersistenceManager.releaseSession(session);
					}
				} catch (Exception ex) {
					logger.error("releaseSession() failed");
				}
			}
			logger.debug("returning members");
			return members;
	}

	public static AmpTeamMember getAmpTeamMember(Long id) {
		AmpTeamMember ampMember = null;
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select t from "
					+ AmpTeamMember.class.getName() + " t "
					+ "where (t.ampTeamMemId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			qry.setCacheable(true);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				ampMember = (AmpTeamMember) itr.next();
			}
			// end
		} catch (Exception ex) {
			logger.error("Unable to get team member ", ex);
		} 
		return ampMember;
	}

	public static AmpTeamMember getMember(String email) {

		User user = DbUtil.getUser(email);
		if (user == null)
			return null;

		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.user=:user)";
			qry = session.createQuery(queryString);
			qry.setParameter("user", user.getId(), Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				member = (AmpTeamMember) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team member", e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return member;

	}

    public static Collection<AmpTeamMember> getAllTeamMembers() {
        Session session = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = "select tm from " + AmpTeamMember.class.getName() + " tm";
            qry = session.createQuery(queryString);
            return qry.list();
        } catch (Exception e) {
            logger.error("Unable to get team members", e);
        }
        return null;
	}

	public static TeamMember getTMTeamHead(Long teamId) {
		TeamMember tm = null;
		AmpTeamMember ampMem	= getTeamHead(teamId);
		if(ampMem!=null){
			Long id 	= ampMem.getAmpTeamMemId();
			User usr 	= UserUtils.getUser(ampMem.getUser().getId());
			String name = usr.getName();
			String role = ampMem.getAmpMemberRole().getRole();
			AmpTeamMemberRoles ampRole = ampMem.getAmpMemberRole();
			AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
			tm = new TeamMember();
			tm.setMemberId(id);
			tm.setTeamId(teamId);
			tm.setMemberName(name);
			tm.setRoleName(role);
			tm.setEmail(usr.getEmail());
			tm.setPublishDocuments(ampMem.getPublishDocPermission());
			if (headRole!=null && ampRole.getAmpTeamMemRoleId().equals(headRole.getAmpTeamMemRoleId())) {
				tm.setTeamHead(true);
			} else {
				tm.setTeamHead(false);
			}
		}	

		return tm;

	}
	
	public static TeamMember getTeamMember(Long ampTeamMemberId) {
		AmpTeamMember ampMem	= getAmpTeamMember(ampTeamMemberId);
		Long id 	= ampMem.getAmpTeamMemId();
		User usr 	= UserUtils.getUser(ampMem.getUser().getId());
		String name = usr.getName();
		String role = ampMem.getAmpMemberRole().getRole();
		AmpTeamMemberRoles ampRole = ampMem.getAmpMemberRole();
		AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
		TeamMember tm = new TeamMember();
		tm.setMemberId(id);
		tm.setTeamId(ampMem.getAmpTeam().getAmpTeamId());
		tm.setMemberName(name);
		tm.setRoleName(role);
		tm.setEmail(usr.getEmail());
		tm.setPublishDocuments(ampMem.getPublishDocPermission());
		if (headRole!=null && ampRole.getAmpTeamMemRoleId().equals(headRole.getAmpTeamMemRoleId())) {
			tm.setTeamHead(true);
		} else {
			tm.setTeamHead(false);
		}
		return tm;

	}

	public static AmpTeamMember getTeamHead(Long teamId) {

		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {

			AmpTeamMemberRoles ampRole = getAmpTeamHeadRole();
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.ampTeam=:teamId) and (tm.ampMemberRole=:role)";
			qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			qry.setParameter("role", ampRole.getAmpTeamMemRoleId(),
					Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				member = (AmpTeamMember) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team member", e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return member;
	}
	public static List<AmpTeamMember> getTeamHeadAndApprovers(Long teamId) {

		Session session = null;
		Query qry = null;
		List<AmpTeamMember> members = new ArrayList<AmpTeamMember>();
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select teamMember from "
					+ AmpTeamMember.class.getName()
					+ " teamMember inner join teamMember.ampTeam  tm" +
					" inner join teamMember.ampMemberRole role "+
							" where tm.ampTeamId=:teamId and (role.teamHead=true or role.approver=true)";
			qry = session.createQuery(queryString);
			qry.setLong("teamId", teamId);
			@SuppressWarnings("unchecked")
			List<AmpTeamMember> list =(List<AmpTeamMember>) qry.list();
			if(list!=null){
				members.addAll(list);
			}
		} catch (Exception e) {
			logger.error("Unable to get team member", e);
		} 
		return members;
	}
	

	public static Collection getMembersUsingRole(Long roleId) {
		Session session = null;
		Query qry = null;
		Collection members = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.ampMemberRole=:roleId)";

			qry = session.createQuery(queryString);
			qry.setParameter("roleId", roleId, Hibernate.LONG);
			members = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all team members", e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("returning members");
		return members;
	}

	public static Collection<TeamMember> getAllTeamMembers(Long teamId) {
		Session session = null;
		Query qry = null;
		Collection<TeamMember> members = new ArrayList<TeamMember>();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()+" tm ";
                          if(teamId!=null){
                              queryString+="  where (tm.ampTeam=:teamId)";
                          }

			qry = session.createQuery(queryString);
                        if(teamId!=null){
			qry.setParameter("teamId", teamId, Hibernate.LONG);
                        }
			Iterator<AmpTeamMember> itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpTeamMember ampMem = (AmpTeamMember) itr.next();
				Long id = ampMem.getAmpTeamMemId();
				User user = UserUtils.getUser(ampMem.getUser().getId());
				String name = user.getName();
				String role = ampMem.getAmpMemberRole().getRole();
				AmpTeamMemberRoles ampRole = ampMem.getAmpMemberRole();
				AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
				TeamMember tm = new TeamMember();
				tm.setMemberId(id);
				tm.setMemberName(name);
				tm.setTeamName(ampMem.getAmpTeam().getName());
				tm.setRoleName(role);
				tm.setEmail(user.getEmail());
				tm.setTeamId(teamId);
				tm.setPublishDocuments(ampMem.getPublishDocPermission());
				if (headRole!=null && ampRole.getAmpTeamMemRoleId().equals(headRole.getAmpTeamMemRoleId())) {
					tm.setTeamHead(true);
					//System.out.println("[team member util] "+ tm.getMemberName() + " is team leader of team with id " +tm.getTeamId());
					logger.info("[logger] "+ tm.getMemberName() + " is team leader of team with id " +tm.getTeamId());

				} else {
					tm.setTeamHead(false);
				}
				members.add(tm);
			}
		} catch (Exception e) {
			logger.error("Unable to get all team members", e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		logger.debug("returning members");
		Collections.sort((List<TeamMember>)members, new TeamMemberUtil.TeamMemberComparator());
		return members;
	}
	
	public static List<TeamMember> getAllMembersExcludingTL (Long teamId){
		Session session = null;
		Query qry = null;
		List<TeamMember> members = null;
		AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
		try {
			session= PersistenceManager.getRequestDBSession();
			String queryString="select tm from " + AmpTeamMember.class.getName() +" tm where tm.ampTeam=:teamId and tm.ampMemberRole!="+headRole.getAmpTeamMemRoleId()+" order by tm.user.firstNames,tm.user.lastName";
			qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId,Hibernate.LONG);
			List<AmpTeamMember> ampTeamMembers = qry.list();
			if(ampTeamMembers!=null){
				Iterator<AmpTeamMember> itr = ampTeamMembers.iterator();
				while (itr.hasNext()) {
					AmpTeamMember ampMem = (AmpTeamMember) itr.next();
					Long id = ampMem.getAmpTeamMemId();
					User user = UserUtils.getUser(ampMem.getUser().getId());
					String name = user.getName();
					String role = ampMem.getAmpMemberRole().getRole();
					TeamMember tm = new TeamMember();
					tm.setMemberId(id);
					tm.setMemberName(name);
					tm.setTeamName(ampMem.getAmpTeam().getName());
					tm.setRoleName(role);
					tm.setEmail(user.getEmail());
					tm.setTeamId(teamId);
					tm.setPublishDocuments(ampMem.getPublishDocPermission());
					tm.setTeamHead(false);
					if(members==null){
						members=new ArrayList<TeamMember>();
					}
					members.add(tm);
				}
			}
		} catch (Exception e) {
			logger.error("Unable to get all team members", e);
		}
		return members;
	}
	
	/**
	 * checks whether some tm's have right to make document public and if they shouldn't have this right, changes them
	 * @param teamId
	 * @param memberIds
	 */
	public static void removeTeamMembersResourcePublishingRights(Long teamId, List<Long> memberIds){
		Session session = null;
		Query qry = null;
		AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
		try {
			session= PersistenceManager.getRequestDBSession();
			String qhl="update " + AmpTeamMember.class.getName()+" tm set tm.publishDocPermission=false where tm.ampTeam="+teamId+" and tm.publishDocPermission=true and tm.ampMemberRole!="+headRole.getAmpTeamMemRoleId();
			if(memberIds!=null && memberIds.size()>0){
				qhl+=" and tm.ampTeamMemId not in (:memberIds)" ;
			}
			qry=session.createQuery(qhl);
			if(memberIds!=null && memberIds.size()>0){
				qry.setParameterList("memberIds", memberIds);
			}
			qry.executeUpdate();
		} catch (Exception e) {
			logger.error("couldn't update team members", e);
		}
	}
	
	/**
	 * checks whether some tm's have no rights to make document public and if they should have this right, changes them
	 * @param teamId
	 * @param selectedMemberIds
	 */
	public static void grantMembersResourcePublishingRights (Long teamId, List<Long> selectedMemberIds){
		Session session = null;
		Query qry = null;
		try {
			session= PersistenceManager.getRequestDBSession();
			String qhl="update " + AmpTeamMember.class.getName()+" tm set tm.publishDocPermission=true where tm.ampTeam="+teamId+" and (tm.publishDocPermission is null or tm.publishDocPermission=false)";
			if(selectedMemberIds!=null && selectedMemberIds.size()>0){
				qhl+=" and tm.ampTeamMemId in (:memberIds)" ;
			}
			qry=session.createQuery(qhl);
			if(selectedMemberIds!=null && selectedMemberIds.size()>0){
				qry.setParameterList("memberIds", selectedMemberIds);
			}
			qry.executeUpdate();
		} catch (Exception e) {
			logger.error("couldn't update team members", e);
		}
	}
	
	private static class TeamMemberComparator implements Comparator<TeamMember> {
    	Locale locale;
        Collator collator;

        public TeamMemberComparator(){
            this.locale=new Locale("en", "EN");
        }

        public TeamMemberComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(TeamMember o1, TeamMember o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            
            int result = (o1.getMemberName()==null || o2.getMemberName()==null)?0:collator.compare(o1.getMemberName().toLowerCase(), o2.getMemberName().toLowerCase());
            return result;
        }
    }


    public static Collection<User> getAllTeamMemberUsers() {
        Session session = null;
        Query qry = null;
        Collection<User> users = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select tm from "
                    + AmpTeamMember.class.getName()
                    + " tm";

            qry = session.createQuery(queryString);

            Collection teamMembers=qry.list();
            if(teamMembers!=null){
                users=new ArrayList();
                Iterator itr = teamMembers.iterator();
                while(itr.hasNext()) {
                    AmpTeamMember ampMem = (AmpTeamMember) itr.next();
                    users.add(ampMem.getUser());
                }
            }
        } catch (Exception e) {
            logger.error("Unable to get all team members", e);
        }
        logger.debug("returning members");
        return users;
	}

	public static Collection getAllMembersUsingActivity(Long activityId) {
		Session session = null;
		Collection col = null;

		try {

			session = PersistenceManager.getSession();
			// modified by Priyajith
			// desc:used select query instead of
			// session.load
			// start
			String queryString = "select a from " + AmpActivityVersion.class.getName()
					+ " a " + "where (a.ampActivityId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", activityId, Hibernate.LONG);
			Iterator itrTemp = qry.list().iterator();
			AmpActivityVersion ampActivity = null;
			while (itrTemp.hasNext()) {
				ampActivity = (AmpActivityVersion) itrTemp.next();
			}
			// end

			Iterator itr = ampActivity.getMember().iterator();
			col = new ArrayList();
			while (itr.hasNext()) {
				col.add(itr.next());
			}
		} catch (Exception e) {
			logger.error("Exception from getAllMembersUsingActivity()", e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed", ex);
			}
		}
		return col;
	}

	public static Collection<AmpActivityVersion> getAllMemberActivities(Long memberId) {
		Session session = null;
		Collection<AmpActivityVersion> col = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			AmpTeamMember ampMember = (AmpTeamMember) session.load(AmpTeamMember.class, memberId);

			Iterator<AmpActivityVersion> itr = ampMember.getActivities().iterator();
			col = new ArrayList<AmpActivityVersion>();
			while (itr.hasNext()) {
				AmpActivityVersion activity = itr.next();
				Iterator<AmpOrgRole> orgItr = activity.getOrgrole().iterator();
				String donors = "";
				while (orgItr.hasNext()) {
					AmpOrgRole orgRole = orgItr.next();
					if(orgRole.getRole()!=null){
						if (orgRole.getRole().getRoleCode().equals(Constants.FUNDING_AGENCY)) {
							if (donors.trim().length() > 0)
								donors += ", ";
							donors += orgRole.getOrganisation().getName();
						}
					}

				}
				activity.setDonors(donors);
				col.add(activity);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return col;
	}


	public static Collection getAllMemberAmpActivities(Long memberId) {
		Session session = null;
		Collection col = new ArrayList();

		try {

			session = PersistenceManager.getSession();
			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
			// start
			String queryString = "select t from "
					+ AmpTeamMember.class.getName() + " t "
					+ "where (t.ampTeamMemId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", memberId, Hibernate.LONG);

			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				AmpTeamMember member = (AmpTeamMember) itr.next();
				col.addAll(member.getActivities());
			}

		} catch (Exception e) {
			logger.error("Exception from getAllMemberActivities()", e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed", ex);
			}
		}
		return col;
	}

	public static ArrayList getAllMemberReports(Long id) {
		Session session = null;
		ArrayList col = new ArrayList();

		try {

			session = PersistenceManager.getSession();
			// modified by Priyajith
			// Desc: removed the usage of session.load and used the select query
			// start
			String queryString = "select t from "
					+ AmpTeamMember.class.getName() + " t "
					+ "where (t.ampTeamMemId=:id)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);

			Iterator itrTemp = qry.list().iterator();
			AmpTeamMember ampTeamMember = null;
			while (itrTemp.hasNext()) {
				ampTeamMember = (AmpTeamMember) itrTemp.next();
			}
			// end

			Iterator itr = ampTeamMember.getReports().iterator();
			while (itr.hasNext()) {
				AmpReports ampReports = (AmpReports) itr.next();
				//if (!(ampReports.getAmpReportId().equals(new Long(7))))
				{
					logger.debug("inside iter");
					col.add(ampReports);
				}
			}
			Collections.sort(col);
		} catch (Exception e) {
			logger.error("Exception from getAllMemberReports()");
			logger.error(e.toString());
			e.printStackTrace(System.out);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed", ex);
			}
		}
		return col;
	}

        public static List getAllMemberReports(Long id, int currentPage, int reportPerPage) {
                Session session = null;
                List col = new ArrayList();

                try {

                        session = PersistenceManager.getRequestDBSession();
                        // modified by Priyajith
                        // Desc: removed the usage of session.load and used the select query
                        // start

                        String queryString = "select r from "
					+ AmpTeamMember.class.getName() + " t  inner join  t.reports r "
                            + "  where (t.ampTeamMemId=:teamId) order by r.name limit " + currentPage + ", " +
                            reportPerPage;
                        Query qry = session.createQuery(queryString);
                        qry.setLong("teamId", id);
                        col = qry.list();
                } catch (Exception e) {
                        logger.error("Exception from getAllMemberReports()");
                        logger.error(e.toString());
                        e.printStackTrace(System.out);
                }
                return col;
        }

        public static List<AmpReports> getAllTeamMembersReports(Long teamId,Integer currentPage,Integer reportPerPage){
    		Session session=null;
    		Query qry=null;
    		List<AmpReports> result=null;
    		try {
    			session = PersistenceManager.getRequestDBSession();
    			result=new ArrayList<AmpReports>();
    			String oql= "select r from "
                    + AmpTeamMember.class.getName() + " t  inner join  t.reports r "
                    + "  where (t.ampTeam=:id)";
				qry=session.createQuery(oql);
				qry.setParameter("id", teamId,Hibernate.LONG);
				if (currentPage !=null){
	            	   qry.setFirstResult(currentPage);
	               }
	               if(reportPerPage!=null){
	            	   qry.setMaxResults(reportPerPage);
	               }
				result=qry.list();

    		} catch (Exception e) {
    			logger.error("Exception from getAllMemberReports()");
                logger.error(e.toString());
                e.printStackTrace(System.out);
    		}
    		return result;
    	}


        public static Integer getAllMemberReportsCount(Long id) {
             Session session = null;
             Integer count = 0;

             try {

                     session = PersistenceManager.getRequestDBSession();
                     String queryString = "select r from "
                                     + AmpTeamMember.class.getName() + " t  inner join  t.reports r "
                         + "  where (t.ampTeamMemId=:id)";
                     Query qry = session.createQuery(queryString);
                     qry.setLong("id", id);
                     count=qry.list().size();
             } catch (Exception e) {
                     logger.error("Exception from getAllMemberReports()");
                     logger.error(e.toString());
                     e.printStackTrace(System.out);
             }
             return  count;
     }



	public static AmpTeamMember getAmpTeamMember(User user) {
		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;
		//if(user == null) return null;
		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.user=:user)";
			qry = session.createQuery(queryString);
			qry.setParameter("user", user.getId(), Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				member = (AmpTeamMember) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team Member", e);
		}
		return member;
	}

	public static Collection getTeamMembers(Long teamId) {
		Session session = null;
		Collection col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			AmpTeamMember mem = getTeamHead(teamId);
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.ampTeam=:teamId) and (tm.ampTeamMemId!=:memId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			qry.setParameter("memId", mem.getAmpTeamMemId(), Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpTeamMember ampMem = (AmpTeamMember) itr.next();
				Long id = ampMem.getAmpTeamMemId();
				User user = UserUtils.getUser(ampMem.getUser().getId());
				String name = user.getName();
				String role = ampMem.getAmpMemberRole().getRole();
				String email=user.getEmail();
				String teamName=ampMem.getAmpTeam().getName();
				TeamMember tm = new TeamMember();
				tm.setMemberId(id);
				tm.setMemberName(name);
				tm.setRoleName(role);
				tm.setEmail(email);
				tm.setTeamName(teamName);
				col.add(tm);
			}
		} catch (Exception e) {
			logger.error("Exception from getTeamMembers()", e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed", ex);
			}
		}
		return col;
	}

	public static String[] getMemberInformation(Long userId) {
		logger.debug("In getMemberInformation() : " + userId);
		Session session = null;
		String query = " ";
		Iterator iter = null;
		AmpTeamMember tm = null;
		Vector vect = new Vector();
		String[] info = null;

		Collection memCollInfo = new ArrayList();
		try {
			Query q = null;
			session = PersistenceManager.getRequestDBSession();
			query = "select m from " + AmpTeamMember.class.getName()+" m where m.user=:memberId";
			q = session.createQuery(query);
			q.setParameter("memberId", userId, Hibernate.LONG);
			if (q != null) {
				iter = q.list().iterator();
				while (iter.hasNext()) {
					tm = (AmpTeamMember) iter.next();
					vect.add(tm.getAmpTeam().getName());
					vect.add(tm.getAmpMemberRole().getRole());
				}
			}
			info = new String[vect.size()];
			vect.toArray(info);
		} catch (Exception e) {
			logger.error("Exception in getTeamMemberInformation() : ", e);
			e.printStackTrace(System.out);
		} 

		return info;
	}

	public static AmpTeamMemberRoles getAmpTeamHeadRole() {
		Session session = null;
		Query qry = null;
		AmpTeamMemberRoles ampRole = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select r from "
					+ AmpTeamMemberRoles.class.getName()
					+ " r where r.teamHead = true";
			qry = session.createQuery(queryString);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampRole = (AmpTeamMemberRoles) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get role", e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return ampRole;
	}

	public static Collection<AmpTeamMemberRoles> getAllTeamMemberRoles() {
		return getAllTeamMemberRoles(true);
	}
	public static Collection<AmpTeamMemberRoles> getAllTeamMemberRoles(boolean includeApprover) {
		Session session = null;
		Query qry = null;
		Collection<AmpTeamMemberRoles> roles = new ArrayList<AmpTeamMemberRoles>();

		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select r from "
					+ AmpTeamMemberRoles.class.getName() + " r ";
			if(!includeApprover){
				queryString+="where r.approver=false";
			}
			qry = session.createQuery(queryString);
			roles = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all roles", e);
		}
		return roles;
	}
	

	public static AmpTeamMemberRoles getAmpTeamMemberRole(Long id) {
		Session session = null;
		Query qry = null;
		AmpTeamMemberRoles role = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select r from "
					+ AmpTeamMemberRoles.class.getName()
					+ " r where (r.ampTeamMemRoleId=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				role = (AmpTeamMemberRoles) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get role", e);
		}
		finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return role;
	}

	public static AmpTeamMemberRoles getAmpRoleByName(String name) {
		Session session = null;
		Query qry = null;
		AmpTeamMemberRoles role = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select r from "
					+ AmpTeamMemberRoles.class.getName()
					+ " r where (r.role=:name)";
			qry = session.createQuery(queryString);
			qry.setParameter("name", name, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				role = (AmpTeamMemberRoles) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get role", e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return role;
	}

	public static Collection getTeamMembers(String email) {
		 User user = org.digijava.module.aim.util.DbUtil.getUser(email);
		 if (user == null) return null;

		Session session = null;
		Query qry = null;
		List col = new ArrayList<AmpTeamMember>();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from " + AmpTeamMember.class.getName() +
			  " tm where (tm.user.id=:user)";
			qry = session.createQuery(queryString);
			qry.setCacheable(true);
			qry.setLong("user",user.getId());
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
		Collections.sort(col, alphabeticalTeamComp);
		return col;
	}
	
	public static AmpTeamMember getAmpTeamMemberByEmailAndTeam(String email, Long teamId){
		AmpTeamMember retVal= null;
		Session session = null;
		Query qry = null;
		String queryString=null;
		try {
			session= PersistenceManager.getRequestDBSession();
			queryString="select tm from " +AmpTeamMember.class.getName() + " tm where tm.user.email=:usermail and tm.ampTeam="+teamId;
			qry=session.createQuery(queryString);
			qry.setString("usermail",email);
			retVal=(AmpTeamMember)qry.uniqueResult();
		} catch (Exception e) {
			logger.error("Unable to get TeamMember" + e.getMessage());
			e.printStackTrace(System.out);
		}
		return retVal;
	}
	
	public static Comparator<AmpTeamMember> alphabeticalTeamComp		=
		new Comparator<AmpTeamMember>() {
			public int compare(AmpTeamMember o1,
					AmpTeamMember o2) {
				return o1.getAmpTeam().getName().compareTo(o2.getAmpTeam().getName());
			}
		};  

	
	public static Collection getTMTeamMembers(String email) {
		 User user = org.digijava.module.aim.util.DbUtil.getUser(email);
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
			Collection results	= qry.list();
			Iterator itr		= results.iterator();

			while ( itr.hasNext() ) {
				AmpTeamMember ampMem = (AmpTeamMember) itr.next();
				Long id 	= ampMem.getAmpTeamMemId();
				Long teamId	= ampMem.getAmpTeam().getAmpTeamId();
				User usr 	= UserUtils.getUser(ampMem.getUser().getId());
				String name = usr.getName();
				String role = ampMem.getAmpMemberRole().getRole();
				AmpTeamMemberRoles ampRole = ampMem.getAmpMemberRole();
				AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
				TeamMember tm = new TeamMember();
				tm.setMemberId(id);
				tm.setTeamId(teamId);
				tm.setMemberName(name);
				tm.setRoleName(role);
				tm.setEmail(usr.getEmail());
				if (headRole!=null && ampRole.getAmpTeamMemRoleId().equals(
						headRole.getAmpTeamMemRoleId())) {
					tm.setTeamHead(true);
				} else {
					tm.setTeamHead(false);
				}
				col.add( tm );
			}
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
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
				tx = session.beginTransaction();
				for (int i = 0;i < activities.length;i ++) {
					if (activities[i] != null) {
						AmpActivityVersion activity = (AmpActivityVersion)session.load(
								AmpActivityVersion.class,activities[i]);
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
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
				tx = session.beginTransaction();
				for (int i = 0;i < activities.length;i ++) {
					if (activities[i] != null) {
						AmpActivityVersion activity = (AmpActivityVersion)session.load(
								AmpActivityVersion.class,activities[i]);
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

    public static void removeActivitiesFromMember(Long memberId) {
        Session session = null;
        Transaction tx = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
            if (member != null) {
                tx = session.beginTransaction();
                member.getActivities().clear();
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
        }
	}
	public static Collection getUnassignedMemberActivities(Long teamId,Long memberId) {
		Collection col = null;
		Collection col1 = new ArrayList();
		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();

			String queryString = "select act from " + AmpActivity.class.getName() +
			  " act where (act.team=:id) and (act.approvalStatus!=:status)";
			qry = session.createQuery(queryString);
			qry.setParameter("id",teamId,Hibernate.LONG);
			qry.setParameter("status","started",Hibernate.STRING);
			col = qry.list();

			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);

			col.removeAll(member.getActivities());
			logger.debug("Collection size after remove all:" + col.size());
			col1 = new ArrayList();
			Iterator itr = col.iterator();
			while (itr.hasNext()) {
				AmpActivity activity = (AmpActivity) itr.next();
				Iterator orgItr = activity.getOrgrole().iterator();
				String donors = "";

				while (orgItr.hasNext()) {
					AmpOrgRole orgRole = (AmpOrgRole) orgItr.next();
					if (orgRole.getRole().getRoleCode().equals(Constants.FUNDING_AGENCY)) {
						if (donors.trim().length() > 0)
							donors += ", ";
						donors += orgRole.getOrganisation().getName();
					}
				}
				activity.setDonors(donors);
				col1.add(activity);
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

    public static Collection getMemberLinks(Long memberId) {
		Collection col = new ArrayList();

		Session session = null;

		try {
			session = PersistenceManager.getSession();
			AmpTeamMember tm = (AmpTeamMember) session.load(AmpTeamMember.class,
					memberId);
			Iterator itr = tm.getLinks().iterator();
			while (itr.hasNext()) {
				CMSContentItem cmsItem = (CMSContentItem) itr.next();
				Documents document = new Documents();
				document.setDocId(new Long(cmsItem.getId()));
				document.setTitle(cmsItem.getTitle());
				document.setIsFile(cmsItem.getIsFile());
				document.setFileName(cmsItem.getFileName());
				document.setUrl(cmsItem.getUrl());
				document.setDocDescription(cmsItem.getDescription());
				document.setDate(cmsItem.getDate());
				col.add(document);
			}
		} catch (Exception e) {
			logger.error("Unable to get Member links" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return col;
	}

    public static CMSContentItem getMemberLink(Long memberLinkId) {
        CMSContentItem cmsItem=null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            cmsItem = (CMSContentItem) session.load(CMSContentItem.class,memberLinkId);
        } catch (Exception e) {
            logger.error("Unable to get Member links" + e.getMessage());
            e.printStackTrace(System.out);
        }
        return cmsItem;
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


	public static void removeMemberLinks(Long memberId,Long links[]) {
		Session session = null;
		Transaction tx = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
			    Collection col = new ArrayList();
				tx = session.beginTransaction();

				Iterator itr = member.getLinks().iterator();
				while (itr.hasNext()) {
				    CMSContentItem cmsItem = (CMSContentItem) itr.next();
				    boolean flag = false;
				    for (int i = 0;i < links.length;i ++) {
				        if (cmsItem.getId() == links[i].longValue()) {
				            flag = true;
				            session.delete(cmsItem);
				            break;
				        }
				    }
				    if (!flag) {
				        col.add(cmsItem);
				    }
				}
				member.setLinks(new HashSet(col));
				session.update(member);
				tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to remove members link" + e.getMessage());
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

	public static void addLinkToMember(Long memberId,CMSContentItem cmsItem) {
		Session session = null;
		Transaction tx = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
				tx = session.beginTransaction();
				if (member.getLinks() == null)
				    member.setLinks(new HashSet());
				member.getLinks().add(cmsItem);
				session.update(member);
				tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to add Links to members" + e.getMessage());
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

	public static void assignReportsToMember(Long memberId,Long reports[]) {
		Session session = null;
		Transaction tx = null;
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

	public static Collection getAllTeamMembersToDesktop(Long teamId) {
		Session session = null;
		Query qry = null;
		Collection members = new ArrayList();

		try {
			session = PersistenceManager.getSession();

			// get the team leader of the team
			String queryString = "select t.ampTeamMemId from " + AmpTeamMember.class.getName() +
				" t where (t.ampTeam=:teamid AND t.ampMemberRole=:roleid)";
			qry = session.createQuery(queryString);
			qry.setParameter("teamid", teamId, Hibernate.LONG);
			qry.setParameter("roleid", 1, Hibernate.INTEGER);
			Iterator itr = qry.list().iterator();
			Long id = new Long(-1);
			if (itr.hasNext()) {
			    Object idObj = itr.next();
			    id = (Long) idObj;
			}
			logger.debug("Got team leader " + id);

			// get all members of the team and also set the team leader
			// flag of the member who is the team leader
			queryString = "select tm.ampTeamMemId,usr.firstNames," +
					"usr.lastName from " + AmpTeamMember.class.getName()
					+ " tm, " + User.class.getName() + " usr " +
							"where tm.user=usr.id and (tm.ampTeam=:teamId)";
			qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			itr = qry.list().iterator();

			Object temp[] = null;
			while (itr.hasNext()) {
			    temp = (Object[]) itr.next();
				TeamMember tm = new TeamMember();
				Long memId = (Long)temp[0];
				tm.setMemberId(memId);
				tm.setMemberName((String)temp[1] + " " + (String)temp[2]);
				if (memId.equals(id)) {
				    {	//System.out.println("!!!this team member: "+tm.getMemberName()+" is the TEAM LEADER!!!");
				    	tm.setTeamHead(true);
				    }
				}
				members.add(tm);
			}
		} catch (Exception e) {
			logger.error("Unable to get all team members", e);
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
		logger.debug("returning members");
		return members;
	}


	public static void updateAppSettingDefReport(Long memId) {
		Session session = null;
		Transaction tx = null;

		try {
				session = PersistenceManager.getSession();
				tx = session.beginTransaction();

				String queryString = "select app from "
					+ AmpApplicationSettings.class.getName()
					+ " app where (app.member.ampTeamMemId=:memId)";

				Query qry = session.createQuery(queryString);
				qry.setParameter("memId", memId, Hibernate.LONG);
				Iterator itr = qry.list().iterator();
				if(itr.hasNext()){
					AmpApplicationSettings app=(AmpApplicationSettings) itr.next();
					app.setDefaultTeamReport(null);
					session.saveOrUpdate(app);
				}
				tx.commit();
			}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}

    public static void removeTeamMembers(Long id[]) {
        Session session = null;
        Transaction tx = null;
        String qryStr = null;
        Query qry = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            session.setFlushMode(FlushMode.COMMIT);

            tx = session.beginTransaction();
            for (int i = 0; i < id.length; i++) {
                if (id[i] != null) {
                    AmpTeamMember ampMember = (AmpTeamMember) session.load(AmpTeamMember.class, id[i]);
                    if (isTeamLead(ampMember)) {
                        AmpTeam team = ampMember.getAmpTeam();
                        team.setTeamLead(null);
                        session.update(team);
                    }
                    
                    AmpTeamMember teamHead = getTeamHead(ampMember.getAmpTeam().getAmpTeamId());
                    
                    Collection relatedActivities = ActivityUtil.getActivitiesRelatedToAmpTeamMember(session, ampMember.getAmpTeamMemId());
                    removeLinksFromATMToActivity(session,relatedActivities, ampMember, teamHead);                    
                   
                    String queryString = "select calatt from " + AmpCalendarAttendee.class.getName() + " calatt " + "where calatt.member.ampTeamMemId=:Id ";
                    qry = session.createQuery(queryString);
                    qry.setParameter("Id", ampMember.getAmpTeamMemId(), Hibernate.LONG);
                    Collection memevents=qry.list();
                    if (memevents != null && !memevents.isEmpty()) {
                    	for (Iterator iterator = memevents.iterator(); iterator
								.hasNext();) {
							AmpCalendarAttendee callatt = (AmpCalendarAttendee) iterator.next();
							session.delete(callatt);
							
						}
                    }                    
                   
                    // Verify for reports that are owned by this user and delete them
                    //DbUtil.deleteReportsForOwner(ampMember.getAmpTeamMemId());
                    queryString = "select rep from " + AmpReports.class.getName() + " rep " + "where rep.ownerId=:oId ";
                    qry = session.createQuery(queryString);
                    qry.setParameter("oId", ampMember.getAmpTeamMemId(), Hibernate.LONG);
                    
                    Collection memReports = qry.list();
                    if (memReports != null && !memReports.isEmpty()) {
                        for (Iterator rpIter = memReports.iterator(); rpIter.hasNext(); ) {
                            AmpReports rep = (AmpReports) rpIter.next();
                            //verify Default Report in App Settings
                            queryString = "select app from " + AmpApplicationSettings.class.getName() + " app " + "where app.defaultTeamReport=:rId ";
                            qry = session.createQuery(queryString);
                            qry.setParameter("rId", rep.getAmpReportId(), Hibernate.LONG);

                            Collection memAppSettings = qry.list();
                            if (memAppSettings != null && !memAppSettings.isEmpty()) {
                                for (Iterator appSettIter = memAppSettings.iterator(); appSettIter.hasNext(); ) {
                                    AmpApplicationSettings set = (AmpApplicationSettings) appSettIter.next();
                                    set.setDefaultTeamReport(null);
                                    session.update(set);
                                }
                            }
                            		
                            // delete related information before we delete the report
                            String deleteTeamReports = " select tr from " + AmpTeamReports.class.getName() + " tr where (tr.report=:ampReportId)";
                            Query qryaux = session.createQuery(deleteTeamReports);
                            qryaux.setParameter("ampReportId", rep.getAmpReportId(), Hibernate.LONG);

                            Collection tmReports= qryaux.list();
                            if(tmReports!=null && !tmReports.isEmpty()){
                            	for (Iterator itReports = tmReports.iterator(); itReports.hasNext();) {
                            		AmpTeamReports atr = (AmpTeamReports) itReports.next();
                            		session.delete(atr);
								}
                            }
                            
                            // session.delete(deleteTeamReports);
                            session.delete(rep);
                        }
                    }

                    qryStr = "select com from " + AmpComments.class.getName() +
                        " com where (com.memberId.ampTeamMemId=:memberId)";
                    qry = session.createQuery(qryStr);
                    qry.setParameter("memberId", id[i], Hibernate.LONG);
                    List<AmpComments> memComments = qry.list();
                    if(memComments!=null&&memComments.size()>0){
                        Iterator<AmpComments> commIter=memComments.iterator();
                        while(commIter.hasNext()){
                            AmpComments comm=commIter.next();
                            comm.setMemberId(null);
                            session.saveOrUpdate(comm);
                        }
                    }
                    qryStr = "select cal   from "+AmpCalendar.class.getName() +" cal where (cal.member.ampTeamMemId=:memberId) ";
                    qry = session.createQuery(qryStr);
                    qry.setLong("memberId", id[i]);
                    List<AmpCalendar> ampCalendarEvents = qry.list();
                    if(ampCalendarEvents!=null&&ampCalendarEvents.size()>0){
                        Iterator<AmpCalendar> calIter=ampCalendarEvents.iterator();
                        while (calIter.hasNext()) {
                            AmpCalendar ampCal = calIter.next();
                            Calendar cal=ampCal.getCalendarPK().getCalendar();
                            session.delete(ampCal);
                            session.delete(cal);
                        }
                    }
                   

                    qryStr = "select a from " + AmpApplicationSettings.class.getName() +
                        " a where (a.member.ampTeamMemId=:memberId)";
                    qry = session.createQuery(qryStr);
                    qry.setParameter("memberId", id[i], Hibernate.LONG);

                    Collection memAppSettings = qry.list();
                    if (memAppSettings != null && !memAppSettings.isEmpty()) {
                        Iterator itr = memAppSettings.iterator();
                        if (itr.hasNext()) {
                            logger.info("Got the app settings..");
                            AmpApplicationSettings ampAppSettings = (AmpApplicationSettings) itr.next();
                            ampAppSettings.setDefaultTeamReport(null);                            
                            session.delete(ampAppSettings);
                            logger.info("deleted the app settings..");
                        }
                    }
                    
                    ArrayList<OffLineReports> reports = new ArrayList<OffLineReports>();
            		reports = (ArrayList<OffLineReports>) EntityHelper.getReports(ampMember);
            		for (OffLineReports report : reports) {
            			EntityHelper.DeleteReport(report);
					}
                    session.delete(ampMember);
                    
                   
                }
            }
            tx.commit();
        } catch (Exception e) {
            logger.error("Unable to removeTeamMembers " + e.getMessage());
            e.printStackTrace();
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception rbf) {
                    logger.error("Roll back failed");
                }
            }
            throw new RuntimeException(e);
        }
    }

	private static boolean isTeamLead(AmpTeamMember member) {
		Session session = null;

		try {
			session = PersistenceManager.getSession();
			AmpTeam ampTeam = (AmpTeam) session.load(AmpTeam.class,
					member.getAmpTeam().getAmpTeamId());
			if(ampTeam!=null)
				if(ampTeam.getTeamLead()!=null)
					if(ampTeam.getTeamLead().getAmpTeamMemId()!=null)
						if (ampTeam.getTeamLead().getAmpTeamMemId().
									equals(member.getAmpTeamMemId())) {
								return true;
			}
		} catch (Exception e) {
			logger.error("Unable to update team page filters" + e.getMessage());
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
		return false;
	}
	/**
	 * Retrieves current TeamMember from request
	 * @param HttpServletRequest request
	 * @return AmpTeamMember currentAmpTeamMember
	 */
	public static AmpTeamMember getCurrentAmpTeamMember(HttpServletRequest request){
		TeamMember currentTeamMember = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
		AmpTeamMember currentAmpTeamMember=getAmpTeamMember(currentTeamMember.getMemberId());
		return currentAmpTeamMember;
	}
        public static List getTeamMemberbyUserId(Long userId) throws Exception{


            Session session = null;
            Query qry = null;
            List teamMembers = null;

            try {
                    session = PersistenceManager.getRequestDBSession();
                    String queryString = "select tm.ampTeamMemId from "
                                    + AmpTeamMember.class.getName()
                                    + " tm where (tm.user.id=:user)";
                    qry = session.createQuery(queryString);
                    qry.setParameter("user",userId, Hibernate.LONG);
                    teamMembers= qry.list();
            }
            catch (HibernateException ex) {
                    logger.error("Unable to get team member", ex);
                    throw ex;
            }

            return teamMembers;

    }

        public static List getAmpTeamMembersbyDgUserId(Long userId) throws Exception{


            Session session = null;
            Query qry = null;
            List teamMembers = null;

            try {
                    session = PersistenceManager.getRequestDBSession();
                    String queryString = "select tm from "
                                    + AmpTeamMember.class.getName()
                                    + " tm where (tm.user.id=:user)";
                    qry = session.createQuery(queryString);
                    qry.setParameter("user",userId, Hibernate.LONG);
                    teamMembers= qry.list();
            }
            catch (HibernateException ex) {
                    logger.error("Unable to get team member", ex);
                    throw ex;
            }

            return teamMembers;

        }

        public static List<AmpTeamMember> getAmpTeamMembersbyDgUser(User user) {
            try {
                return getAmpTeamMembersbyDgUserId(user.getId());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

    private static void removeLinksFromATMToActivity (Session session,Collection activities, AmpTeamMember atm, AmpTeamMember teamHead) {
    	if (activities == null || atm == null) {
    		return;
    	}
    	Iterator iter 	= activities.iterator();
    	while ( iter.hasNext() ) {
    		AmpActivityVersion act	= (AmpActivityVersion) iter.next();
    		if ( act.getModifiedBy() != null && act.getModifiedBy().getAmpTeamMemId().equals(atm.getAmpTeamMemId()) ) {
    			act.setModifiedBy(null);
    		}
    		if ( act.getUpdatedBy() != null && act.getUpdatedBy().getAmpTeamMemId().equals(atm.getAmpTeamMemId()) ) {
    			act.setUpdatedBy(null);
    		}
    		if ( act.getActivityCreator() != null && act.getActivityCreator().getAmpTeamMemId().equals(atm.getAmpTeamMemId()) ) {
    			act.setActivityCreator(null);
    		}
    		if ( act.getApprovedBy() != null && act.getApprovedBy().getAmpTeamMemId().equals(atm.getAmpTeamMemId()) ) {
    			//if we are deleting the team leader we shouldn't set him as TL
    			if ((teamHead!=null) && (!atm.equals(teamHead))){
    				act.setApprovedBy(teamHead);
    			}else{
    				act.setApprovedBy(null);
    			}
    		}
    		if ( act.getMember() != null ) {
    			Iterator iterMem	= act.getMember().iterator();
    			while ( iterMem.hasNext() ) {
    				AmpTeamMember mem	= (AmpTeamMember) iterMem.next();
    				if ( mem.getAmpTeamMemId().equals(atm.getAmpTeamMemId()) ) {
    					//logger.info(act.getAmpActivityId());
    					iterMem.remove();
    				}
    			}
    		}
    		session.update(act);
    	}
    }

}
