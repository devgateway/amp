/*
 * TeamMemberUtil.java
 * Created : 17-Feb-2006
 */
package org.digijava.module.aim.util;


import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpDesktopTabSelection;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.dbentity.Calendar;
import org.hibernate.*;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

public class TeamMemberUtil {

	private static Logger logger = Logger.getLogger(TeamMemberUtil.class);
	
	public static Hashtable<Long,User> users = new Hashtable<Long,User>();
	public static Hashtable<Long,AmpTeamMember> atmUsers = new Hashtable<Long,AmpTeamMember>();
	
	public static AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
	
	public static User getUserEntityByTMId(Long teamMemberId) {
		User u	= users.get( teamMemberId );
		if ( u == null ) {
			AmpTeamMember atm	= getAmpTeamMemberCached(teamMemberId);
			u					= atm.getUser();
			if (teamMemberId != null && u != null)
				users.put(teamMemberId, u);
		}
		return u;
			
	}

	public static Long getFundOrgOfUser(Long id) {
		Long orgId = null;
		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			AmpTeamMember tm = (AmpTeamMember) session.load(AmpTeamMember.class,
					id);
			User user = tm.getUser();

			String qryStr = "select o.ampOrgId from " + AmpOrganisation.class.getName() + " o " +
					"where (" + AmpOrganisation.hqlStringForName("o") + "=:name) and (o.deleted is null or o.deleted = false) ";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("name",user.getOrganizationName(),StringType.INSTANCE);
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
				qry.setParameter("teamId", teamId, LongType.INSTANCE);
				Iterator itr = qry.list().iterator();

				while (itr.hasNext()) {
					AmpTeamMember ampMem = (AmpTeamMember) itr.next();
					TeamMember tm = new TeamMember(ampMem);
					if ( !tm.getTeamHead()) {
						if (ampMem.getActivities() == null) {
							tm.setActivities(new HashSet());
						}
						else
							tm.setActivities(ampMem.getActivities());
						members.add(tm);
					}	
				}
			} catch (Exception e) {
				logger.error("Unable to get all team members [getAllTMExceptTL()]", e);
			}
			logger.debug("returning members");
			return members;
	}

	public static AmpTeamMember getAmpTeamMemberCached(Long id) {
		
		AmpTeamMember ampMember		= TeamMemberUtil.atmUsers.get(id);
//		if ( ampMember != null )
//			return ampMember;
//		
//		else 
		{
			ampMember				= getAmpTeamMember(id);
			if (id != null && ampMember != null)
				atmUsers.put(id, ampMember);
			return ampMember;
		}
	}
	//this returns the last one -- it should return a list of all team members associated to a userID
	public static AmpTeamMember getAmpTeamMemberByUserId(Long userId) {
		AmpTeamMember ampMember = null;
		Session session = null;
		
		try {
			session = PersistenceManager.getRequestDBSession();
			// modified by Priyajith
			// desc:used select query instead of session.load
			// start
			String queryString = "select t from "
					+ AmpTeamMember.class.getName() + " t "
					+ "where (t.user=:userId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("userId", userId, LongType.INSTANCE);
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
			qry.setParameter("id", id, LongType.INSTANCE);
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
			qry.setParameter("user", user.getId(), LongType.INSTANCE);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				member = (AmpTeamMember) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team member", e);
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
			tm = new TeamMember(ampMem);
		}
		return tm;

	}
	
	public static TeamMember getTeamMember(Long ampTeamMemberId) {
		return new TeamMember(getAmpTeamMember(ampTeamMemberId));
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
			qry.setParameter("teamId", teamId, LongType.INSTANCE);
			qry.setParameter("role", ampRole.getAmpTeamMemRoleId(),
					LongType.INSTANCE);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				member = (AmpTeamMember) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team member", e);
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
			qry.setParameter("roleId", roleId, LongType.INSTANCE);
			members = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all team members", e);
		}
		logger.debug("returning members");
		return members;
	}

	public static List<TeamMember> getAllTeamMembers(Long teamId) {
		try {
			Session session = PersistenceManager.getSession();
			String queryString = "select distinct tm from " + AmpTeamMember.class.getName() + " tm "
					+ "inner join fetch tm.user as usr "
					+ "inner join fetch tm.ampMemberRole "
					+ "inner join fetch tm.ampTeam "
					+ "inner join fetch usr.groups";
		
			if (teamId != null) {
				queryString += "  where (tm.ampTeam=:teamId)";
			}

			Query qry = session.createQuery(queryString);
			
			if (teamId != null) {
				qry.setParameter("teamId", teamId, LongType.INSTANCE);
			}
			
			List<AmpTeamMember> atms = qry.list();
            List<TeamMember> members = new ArrayList<>();
            
			for(AmpTeamMember atm:atms) {
				members.add(new TeamMember(atm));
			}
			
            Collections.sort((List<TeamMember>)members, new TeamMemberUtil.TeamMemberComparator());
            
            return members;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	public static Map<Long, List<TeamMember>> getAllTeamsWithMembers() {
		Map<Long, List<TeamMember>> result = new HashMap<>();
		List<TeamMember> teamMembers = getAllTeamMembers(null);
			
		for(TeamMember atm : teamMembers) {
				Long teamId = atm.getTeamId();
				if (result.get(teamId) == null) {
					result.put(teamId, new ArrayList<TeamMember>());
				}
				
				result.get(teamId).add(atm);
			}
            
            Iterator<Long> iterator = result.keySet().iterator();
			
            return result;
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
			qry.setParameter("teamId", teamId,LongType.INSTANCE);
			List<AmpTeamMember> ampTeamMembers = qry.list();
			if(ampTeamMembers!=null){
				Iterator<AmpTeamMember> itr = ampTeamMembers.iterator();
				while (itr.hasNext()) {
					TeamMember tm = new TeamMember((AmpTeamMember) itr.next());
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
			qry.setParameter("id", activityId, LongType.INSTANCE);
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
		}
		return col;
	}

	public static Collection<AmpActivityVersion> getAllMemberActivities(Long memberId) {
		Session session = null;
		Collection<AmpActivityVersion> col = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			AmpTeamMember ampMember = (AmpTeamMember) session.load(AmpTeamMember.class, memberId);
			col=ampMember.getActivities();	

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
			qry.setParameter("id", memberId, LongType.INSTANCE);

			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				AmpTeamMember member = (AmpTeamMember) itr.next();
				col.addAll(member.getActivities());
			}

		} catch (Exception e) {
			logger.error("Exception from getAllMemberActivities()", e);
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
			qry.setParameter("id", id, LongType.INSTANCE);

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
											+ "  where (t.ampTeamMemId=:teamId) order by " + AmpReports.hqlStringForName("r") +" limit " + currentPage + ", " +
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
				qry.setParameter("id", teamId,LongType.INSTANCE);
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
			qry.setParameter("user", user.getId(), LongType.INSTANCE);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				member = (AmpTeamMember) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team Member", e);
		}
		return member;
	}

	public static Collection<AmpTeamMember> getAllAmpTeamMembersByUser(User user) {
		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;
		Collection<AmpTeamMember> result= null;
		//if(user == null) return null;
		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.user=:user)";
			qry = session.createQuery(queryString);
			qry.setParameter("user", user.getId(), LongType.INSTANCE);
			result	=	qry.list();
		} catch (Exception e) {
			logger.error("Unable to get team Member", e);
		}
		return result;
	}
	
	public static AmpTeamMember getAmpTeamMemberByUserByTeam(User user,AmpTeam ampTeam) {
		Session session = null;
		Query qry = null;
		AmpTeamMember member = null;
		//if(user == null) return null;
		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.user=:user) and  (tm.ampTeam=:ampTeam) ";
			qry = session.createQuery(queryString);
			qry.setLong("user", user.getId());
			qry.setLong("ampTeam", ampTeam.getAmpTeamId());
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				member = (AmpTeamMember) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get team Member", e);
		}
		return member;
	}
	
	public static Collection<TeamMember> getTeamMembers(Long teamId) {
		Session session = null;
		Collection<TeamMember> col = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			AmpTeamMember mem = getTeamHead(teamId);
			String queryString = "select tm from "
					+ AmpTeamMember.class.getName()
					+ " tm where (tm.ampTeam=:teamId) and (tm.ampTeamMemId!=:memId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, LongType.INSTANCE);
			qry.setParameter("memId", mem.getAmpTeamMemId(), LongType.INSTANCE);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				TeamMember tm = new TeamMember((AmpTeamMember) itr.next());
				col.add(tm);
			}
		} catch (Exception e) {
			logger.error("Exception from getTeamMembers()", e);
		}
		return col;
	}

	public static List<TeamMember> getMemberInformation(Long userId) {
		logger.debug("In getMemberInformation() : " + userId);
		Session session = null;
		List<TeamMember>  helpers=null;
		try {
			Query q = null;
			session = PersistenceManager.getRequestDBSession();
			String query = "select new org.digijava.module.aim.helper.TeamMember(team.name,role.role) from " + AmpTeamMember.class.getName()+" m"
                                +" inner join m.ampTeam team inner join m.ampMemberRole role"
                                + " where m.user=:memberId";
			q = session.createQuery(query);
			q.setLong("memberId", userId);
			helpers=q.list();
			
		} catch (Exception e) {
			logger.error("Exception in getTeamMemberInformation() : ", e);
			e.printStackTrace(System.out);
		} 

		return helpers;
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
		}
		return ampRole;
	}

	
	public static Collection<AmpTeamMemberRoles> getAllTeamMemberRoles() {
		return getAllTeamMemberRoles(true);
	}
	
	/**
	 * searches for a "workspace member" role indirectly, through his capabilities of not being a team head and not being an approver. Does not search by name, as those can be i18n-ed
	 * @return
	 */
	public static AmpTeamMemberRoles getWorkspaceMemberTeamMemberRole()
	{
		for(AmpTeamMemberRoles role:getAllTeamMemberRoles())
		{
			if ((!role.getTeamHead()) && (!role.isApprover()))
				return role;
		}
		// oopsie, shouldn't happen
		throw new RuntimeException("could not find a non-team-head non-approver workspace member role!");
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
				queryString+="where r.approver=false or r.teamHead=true";
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
			qry.setParameter("id", id, LongType.INSTANCE);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				role = (AmpTeamMemberRoles) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get role", e);
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
			qry.setParameter("name", name, StringType.INSTANCE);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				role = (AmpTeamMemberRoles) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get role", e);
		}
		return role;
	}
	
	/**
	 * Map<User.id, List<AmpTeamMember>>
	 * @return
	 */
	public static Map<Long, List<AmpTeamMember>> getTeamMembersByUserId(Set<Long> userIds) {
		Map<Long, List<AmpTeamMember>> res = new HashMap<>();
		for(long userId:userIds)
			res.put(userId, new ArrayList<AmpTeamMember>());
		List<Object[]> z = PersistenceManager.getSession()
				.createQuery("select tm, tm.user.id from " + AmpTeamMember.class.getName() + " tm where tm.user.id in :userIds")
				.setParameterList("userIds", userIds)
				.list();
		for(Object[] elem:z) {
			long userId = PersistenceManager.getLong(elem[1]);
			res.get(userId).add((AmpTeamMember) elem[0]);
		}
		return res;
	}
	
	public static Collection<AmpTeamMember> getTeamMembers(String email) {
		 User user = org.digijava.module.aim.util.DbUtil.getUser(email);
		 if (user == null) return null;

		Session session = null;
		Query qry = null;
		List<AmpTeamMember> col = new ArrayList<AmpTeamMember>();

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
	
	/**
	 * null is the smallest number
	 * @param a
	 * @param b
	 * @return
	 */
	public static int nullCompare(Object a, Object b)
	{
		if (a == null)
		{
			if (b == null)
				return -1; // null <= null (avoid crash)
			return -1; // null < [non-null]
		}
		// gone till here -> a != null
		if (b == null)
			return 1; // [non-null] > null
		
		return 0; // [non-null] == [non-null]
	}
	
	public static Comparator<AmpTeamMember> alphabeticalTeamComp		=
		new Comparator<AmpTeamMember>() {
			public int compare(AmpTeamMember o1,
					AmpTeamMember o2) {
				
				int delta = nullCompare(o1, o2);
				if (delta != 0)
					return delta;
				
				delta = nullCompare(o1.getAmpTeam(), o2.getAmpTeam());
				if (delta != 0)
					return delta;
				
				delta = nullCompare(o1.getAmpTeam().getName(), o2.getAmpTeam().getName());
				if (delta != 0)
					return delta;
				
				return o1.getAmpTeam().getName().toLowerCase().compareTo(o2.getAmpTeam().getName().toLowerCase());
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
			qry.setParameter("user",user.getId(),LongType.INSTANCE);
			Collection results	= qry.list();
			Iterator itr		= results.iterator();

			while ( itr.hasNext() ) {
				TeamMember tm = new TeamMember((AmpTeamMember) itr.next());
				col.add( tm );
			}
		} catch (Exception e) {
			logger.error("Unable to get TeamMembers" + e.getMessage());
			e.printStackTrace(System.out);
		}
		return col;
	}

	public static void assignActivitiesToMember(Long memberId,Long activities[]) {
		Session session = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
//beginTransaction();
				for (int i = 0;i < activities.length;i ++) {
					if (activities[i] != null) {
						AmpActivityVersion activity = (AmpActivityVersion)session.load(
								AmpActivityVersion.class,activities[i]);
						member.getActivities().add(activity);
					}
				}
				session.update(member);
				//tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to assign activities" + e.getMessage());
			e.printStackTrace(System.out);
		}
	}

	public static void removeActivitiesFromMember(Long memberId,Long activities[]) {
		Session session = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
//beginTransaction();
				for (int i = 0;i < activities.length;i ++) {
					if (activities[i] != null) {
						AmpActivityVersion activity = (AmpActivityVersion)session.load(
								AmpActivityVersion.class,activities[i]);
						member.getActivities().remove(activity);
					}
				}
				session.update(member);
				//tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to remove activities" + e.getMessage());
			e.printStackTrace(System.out);
		}
	}

    public static void removeActivitiesFromMember(Long memberId) {
        Session session = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
            if (member != null) {
//beginTransaction();
                member.getActivities().clear();
                session.update(member);
                //tx.commit();
            }
        } catch (Exception e) {
            logger.error("Unable to remove activities" + e.getMessage());
            e.printStackTrace(System.out);
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
			  " act where (act.team=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id",teamId,LongType.INSTANCE);
			//qry.setParameter("status","started",StringType.INSTANCE);
			col1 = qry.list();

			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);

			col1.removeAll(member.getActivities());
			

		} catch (Exception e) {
			logger.error("Unable to remove activities" + e.getMessage());
			e.printStackTrace(System.out);
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
			qry.setParameter("id",teamId,LongType.INSTANCE);
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
		}
		return col;
	}

	public static void assignReportsToMember(Long memberId,Long reports[]) {
		Session session = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
//beginTransaction();
				for (int i = 0;i < reports.length;i ++) {
					if (reports[i] != null) {
						AmpReports report = (AmpReports)session.load(
								AmpReports.class,reports[i]);
						member.getReports().add(report);
					}
				}
				session.update(member);
				//tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to assign reports" + e.getMessage());
			e.printStackTrace(System.out);
		} 
	}

	public static void removeReportsFromMember(Long memberId,Long reports[]) {
		Session session = null;
		AmpTeamMember member = null;

		try {
			session = PersistenceManager.getSession();
			member = (AmpTeamMember) session.load(AmpTeamMember.class,memberId);
			if (member != null) {
//beginTransaction();
				for (int i = 0;i < reports.length;i ++) {
					if (reports[i] != null) {
						AmpReports report = (AmpReports)session.load(
								AmpReports.class,reports[i]);
						member.getReports().remove(report);
					}
				}
				session.update(member);
				//tx.commit();
			}
		} catch (Exception e) {
			logger.error("Unable to remove reports" + e.getMessage());
			e.printStackTrace(System.out);
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
			qry.setParameter("teamid", teamId, LongType.INSTANCE);
			qry.setParameter("roleid", 1, IntegerType.INSTANCE);
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
			qry.setParameter("teamId", teamId, LongType.INSTANCE);
			itr = qry.list().iterator();

			Object temp[] = null;
			while (itr.hasNext()) {
			    temp = (Object[]) itr.next();
				TeamMember tm = new TeamMember();
				Long memId = (Long)temp[0];
				tm.setMemberId(memId);
				tm.setMemberName((String)temp[1] + " " + (String)temp[2]);
				if (memId.equals(id)) {
				    {	////System.out.println("!!!this team member: "+tm.getMemberName()+" is the TEAM LEADER!!!");
				    	tm.setTeamHead(true);
				    }
				}
				members.add(tm);
			}
		} catch (Exception e) {
			logger.error("Unable to get all team members", e);
			e.printStackTrace(System.out);
		}
		logger.debug("returning members");
		return members;
	}



    public static void removeTeamMembers(Long id[]) {

    	Session session = PersistenceManager.getSession();

		for (Long anId : id) {
			if (anId != null) {
				AmpTeamMember ampMember = (AmpTeamMember) session.load(AmpTeamMember.class, anId);
				if (isTeamLead(ampMember)) {
					AmpTeam team = ampMember.getAmpTeam();
					team.setTeamLead(null);
					session.update(team);
				}

				AmpTeamMember teamHead = getTeamHead(ampMember.getAmpTeam().getAmpTeamId());

				Collection<AmpActivityVersion> relatedActivities = ActivityUtil.getActivitiesRelatedToAmpTeamMember(session,
						ampMember.getAmpTeamMemId());
				removeLinksFromATMToActivity(session, relatedActivities, ampMember, teamHead);

				String queryString = "select calatt from " + AmpCalendarAttendee.class.getName() + " calatt "
						+ "where calatt.member.ampTeamMemId=:Id ";
				Query qry = session.createQuery(queryString);
				qry.setParameter("Id", ampMember.getAmpTeamMemId(), LongType.INSTANCE);

				Collection<AmpCalendarAttendee> memevents = qry.list();
				for (AmpCalendarAttendee callatt : memevents) {
					session.delete(callatt);
				}
				Set<AmpDesktopTabSelection> desktopTabSelections = ampMember.getDesktopTabSelections();
				if (desktopTabSelections != null) {
					for (Iterator<AmpDesktopTabSelection> iter = desktopTabSelections.iterator(); iter.hasNext();) {
						AmpDesktopTabSelection desktopTabSelection = iter.next();
						AmpReports report = desktopTabSelection.getReport();
						report.getDesktopTabSelections().remove(desktopTabSelection);
						iter.remove();
						session.delete(desktopTabSelection);
					}
				}

				// Remove references to AMP_CONTACT (creator_id field)
				StringBuilder queryStr = new StringBuilder("update ");
				queryStr.append(AmpContact.class.getName());
				queryStr.append(" as cont set cont.creator=null where cont.creator = :SEL_USER");
				Query contDetQuery = session.createQuery(queryStr.toString());
				contDetQuery.setLong("SEL_USER", anId);
				int success = contDetQuery.executeUpdate();

				// Verify for reports that are owned by this user and delete
				// them
				// DbUtil.deleteReportsForOwner(ampMember.getAmpTeamMemId());
				queryString = "select rep from " + AmpReports.class.getName() + " rep " + "where rep.ownerId=:oId ";
				qry = session.createQuery(queryString);
				qry.setParameter("oId", ampMember.getAmpTeamMemId(), LongType.INSTANCE);

				Collection<AmpReports> memReports = qry.list();
				for (AmpReports rep : memReports) {
					// verify Default Report in App Settings
					queryString = "select app from " + AmpApplicationSettings.class.getName() + " app "
							+ "where app.defaultTeamReport=:rId ";
					qry = session.createQuery(queryString);
					qry.setLong("rId", rep.getAmpReportId());

					Collection<AmpApplicationSettings> memAppSettings = qry.list();
					for (AmpApplicationSettings set : memAppSettings) {
						set.setDefaultTeamReport(null);
						session.update(set);
					}

					// delete related information before we delete the report
					String deleteTeamReports = " select tr from " + AmpTeamReports.class.getName()
							+ " tr where (tr.report=:ampReportId)";
					Query qryaux = session.createQuery(deleteTeamReports);
					qryaux.setLong("ampReportId", rep.getAmpReportId());

					Collection tmReports = qryaux.list();
					if (tmReports != null && !tmReports.isEmpty()) {
						for (Object tmReport : tmReports) {
							AmpTeamReports atr = (AmpTeamReports) tmReport;
							session.delete(atr);
						}
					}

					// session.delete(deleteTeamReports);
					session.delete(rep);
				}

				String qryStr = "select com from " + AmpComments.class.getName()
						+ " com where (com.memberId.ampTeamMemId=:memberId)";
				qry = session.createQuery(qryStr).setParameter("memberId", anId, LongType.INSTANCE);
				List<AmpComments> memComments = qry.list();
				for (AmpComments comm : memComments) {
					comm.setMemberId(null);
					session.saveOrUpdate(comm);
				}
				qryStr = "select cal   from " + AmpCalendar.class.getName() + " cal where (cal.member.ampTeamMemId=:memberId) ";
				qry = session.createQuery(qryStr).setLong("memberId", anId);
				List<AmpCalendar> ampCalendarEvents = qry.list();
				for (AmpCalendar ampCal : ampCalendarEvents) {
					Calendar cal = ampCal.getCalendarPK().getCalendar();
					session.delete(ampCal);
					session.delete(cal);
				}

				qryStr = "select cont from " + AmpContact.class.getName() + " cont where (cont.creator=:memberId) ";
				qry = session.createQuery(qryStr).setLong("memberId", anId);
				List<AmpContact> ampContacts = qry.list();
				if (ampContacts != null && ampContacts.size() > 0) {
					for (AmpContact ampCont : ampContacts) {
						session.delete(ampCont);
					}
				}

				session.delete(ampMember);

			}
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
                    qry.setParameter("user",userId, LongType.INSTANCE);
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
                    qry.setParameter("user",userId, LongType.INSTANCE);
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

    private static void removeLinksFromATMToActivity (Session session, Collection activities, AmpTeamMember atm, AmpTeamMember teamHead) {
    	if (activities == null || atm == null) {
    		return;
    	}
        Query qry = null;

        for (Object activity : activities) {
            AmpActivityVersion act = (AmpActivityVersion) activity;

            if (act.getModifiedBy() != null && act.getModifiedBy().getAmpTeamMemId().equals(atm.getAmpTeamMemId())) {
                String queryString = "update " + AmpActivityVersion.class.getName() +
                        " v set v.modifiedBy = null where v.ampActivityId=" + act.getAmpActivityId();
                qry = session.createQuery(queryString);
                qry.executeUpdate();
            }

            if (act.getActivityCreator() != null && act.getActivityCreator().getAmpTeamMemId().equals(atm.getAmpTeamMemId())) {
                String queryString = "update " + AmpActivityVersion.class.getName() +
                        " v set v.activityCreator = null where v.ampActivityId=" + act.getAmpActivityId();
                qry = session.createQuery(queryString);
                qry.executeUpdate();
            }
            if (act.getApprovedBy() != null && act.getApprovedBy().getAmpTeamMemId().equals(atm.getAmpTeamMemId())) {
                //if we are deleting the team leader we shouldn't set him as TL
                if ((teamHead != null) && (!atm.getAmpTeamMemId().equals(teamHead.getAmpTeamMemId()))) {
                    //act.setApprovedBy(teamHead);
                    String queryString = "update " + AmpActivityVersion.class.getName() +
                            " v set v.approvedBy.ampTeamMemId = "+ teamHead.getAmpTeamMemId()
                            +" where v.ampActivityId=" + act.getAmpActivityId();
                    qry = session.createQuery(queryString);
                    qry.executeUpdate();
                } else {
                    String queryString = "update " + AmpActivityVersion.class.getName() +
                            " v set v.approvedBy = null where v.ampActivityId=" + act.getAmpActivityId();
                    qry = session.createQuery(queryString);
                    qry.executeUpdate();
                }
            }

            if (act.getMember() != null) {
    			act.getMember().remove(atm);
            }
            session.update(act);
        }
        //session.clear();

    }

    
	public static List<AmpTeam> getAllTeamsForUser(String email) {

		User user = DbUtil.getUser(email);
		if (user == null) return null;

		Session session = null;
		Query qry = null;
		

		ArrayList<AmpTeam> result = new ArrayList<AmpTeam>();
		
		try {
			session = PersistenceManager.getSession();
			String queryString = "select tm from " + AmpTeamMember.class.getName() + " tm where (tm.user=:user)";
			qry = session.createQuery(queryString);
			qry.setParameter("user", user.getId(), LongType.INSTANCE);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpTeamMember member = (AmpTeamMember) itr.next();
				result.add(member.getAmpTeam());
			}
		} catch (Exception e) {
			logger.error("Unable to get team member", e);
		}
		return result;

	}
	public static  void addDesktopTab(Long reportId, Long teamMemberId,Integer position) {
		//Transaction tr=null;
		Session dbSession =null;
		AmpDesktopTabSelection sel=null;
		try{
			dbSession 	= PersistenceManager.getRequestDBSession();
//beginTransaction();
			AmpTeamMember atm	= (AmpTeamMember)dbSession.load(AmpTeamMember.class, teamMemberId );
			sel	= new AmpDesktopTabSelection();
			sel.setIndex(position);
			sel.setOwner(atm);
			AmpReports report = (AmpReports)dbSession.load(AmpReports.class, reportId );
			sel.setReport(report);
			if ( atm.getDesktopTabSelections() == null ) {
				atm.setDesktopTabSelections( new TreeSet<AmpDesktopTabSelection>(AmpDesktopTabSelection.tabOrderComparator) );	
			}
			Set<AmpDesktopTabSelection>	tabs=atm.getDesktopTabSelections();
			Iterator<AmpDesktopTabSelection> iter=tabs.iterator();
			while(iter.hasNext()){
				AmpDesktopTabSelection tab=iter.next();
				AmpReports rep = tab.getReport();
				if(rep.getAmpReportId().equals(reportId)){
					iter.remove();
					report.getDesktopTabSelections().remove(tab);
					dbSession.delete(tab);
					break;
				}
			}
			tabs.add(sel);
			report.getDesktopTabSelections().add(sel);
			dbSession.save(sel);
			//tr.commit();
//session.flush();
			
		}
		catch (Exception e) {
			logger.error("unable to save tab", e);
			//tr.rollback();
		}
	}
	public static void removeDesktopTab(Long reportId, Long teamMemberId,Integer position) {
		//Transaction tr=null;
		Session dbSession =null;
		AmpDesktopTabSelection sel=null;
		try{
			dbSession 	= PersistenceManager.getRequestDBSession();
//beginTransaction();
			AmpTeamMember atm	= (AmpTeamMember)dbSession.load(AmpTeamMember.class, teamMemberId );
			Set<AmpDesktopTabSelection>	tabs=atm.getDesktopTabSelections();
			AmpDesktopTabSelection tabToRemove=null;
			Iterator<AmpDesktopTabSelection> iter=tabs.iterator();
			while(iter.hasNext()){
				AmpDesktopTabSelection tab=iter.next();
				AmpReports report = tab.getReport();
				if(report.getAmpReportId().equals(reportId)){
					iter.remove();
					report.getDesktopTabSelections().remove(tab);
					dbSession.delete(tab);
				}
			}
			//tr.commit();
//session.flush();
		}
		catch (Exception e) {
			logger.error("unable to save tab", e);
			//tr.rollback();
		}
	}
	
	/**
	 * TODO: find all 104 reimplementations of the same thing in AMP and change them to calls of this function
	 * @return
	 */
	public static TeamMember getLoggedInTeamMember(){
		TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
		return tm;
	}
	
	public static boolean isHeadRole(AmpTeamMemberRoles role){
		return (headRole==null || role==null) ? false: headRole.getAmpTeamMemRoleId().equals(role.getAmpTeamMemRoleId());
	}
	
	public static boolean isManagementWorkspace(TeamMember tm) {
		return tm != null && tm.getTeamAccessType() != null
				&& "Management".equalsIgnoreCase(tm.getTeamAccessType());

	}
}
