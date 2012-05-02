package org.digijava.module.search.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.admin.helper.AmpActivityFake;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.time.StopWatch;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.search.helper.Resource;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

public class SearchUtil {

	private static Logger logger = Logger.getLogger(SearchUtil.class);

	public static final int QUERY_ALL = -1;
	public static final int ACTIVITIES = 0;
	public static final int REPORTS = 1;
	public static final int TABS = 2;
	public static final int RESOURCES = 3;
    public static final int RESPONSIBLE_ORGANIZATION = 4;
    public static final int EXECUTING_AGENCY = 5;
    public static final int IMPLEMENTING_AGENCY = 6;

	public static Collection<LoggerIdentifiable> getReports(TeamMember tm,
			String string) {
		// TODO: Unify this with getTabs()
		ArrayList<LoggerIdentifiable> resultList = new ArrayList<LoggerIdentifiable>();
		List<AmpReports> col = new ArrayList<AmpReports>();

		Session session = null;
		try {

			session = PersistenceManager.getRequestDBSession();
			AmpTeam team = (AmpTeam) session
					.load(AmpTeam.class, tm.getTeamId());

			AmpTeamMember ampteammember = TeamMemberUtil.getAmpTeamMember(tm
					.getMemberId());
			String queryString = null;
			Query qry = null;

			if (team.getAccessType().equalsIgnoreCase(
					Constants.ACCESS_TYPE_MNGMT)) {
				queryString = "select DISTINCT r from "
						+ AmpReports.class.getName()
						+ " r where r.drilldownTab=false AND (lower(r.name) LIKE lower(:keyword) OR r.reportDescription LIKE :keyword) AND (r.ownerId.ampTeamMemId = :memberid or r.ampReportId IN (select r2.report from "
						+ AmpTeamReports.class.getName()
						+ " r2 where r2.team.ampTeamId = :teamid and r2.teamView = true)) order by r.name";
				qry = session.createQuery(queryString);

				qry.setParameter("memberid", ampteammember.getAmpTeamMemId());
				qry.setParameter("teamid", tm.getTeamId());
				qry.setParameter("keyword", "%" + string + "%");
				col = qry.list();
			} else {
				queryString = "select distinct r from "
						+ AmpReports.class.getName()
						+ "  r left join r.members m where "
						+ " r.drilldownTab=false AND (lower(r.name) LIKE lower(:keyword) OR r.reportDescription LIKE :keyword) AND "
						+ " ((m.ampTeamMemId is not null and m.ampTeamMemId=:ampTeamMemId)"
						+ " or r.id in (select r2.id from "
						+ AmpTeamReports.class.getName()
						+ " tr inner join  tr.report r2 where tr.team=:teamId and tr.teamView = true))";
				qry = session.createQuery(queryString);
				qry.setLong("ampTeamMemId", tm.getMemberId());
				qry.setLong("teamId", tm.getTeamId());
				qry.setParameter("keyword", "%" + string + "%");
				col = qry.list();

			}

		} catch (Exception e) {
			logger.error("Exception from getReports()", e);
			throw new RuntimeException(e);
		}

		resultList.addAll(col);

		return resultList;

	}

	public static Collection<LoggerIdentifiable> getTabs(TeamMember tm,
			String string) {
		ArrayList<LoggerIdentifiable> resultList = new ArrayList<LoggerIdentifiable>();

		List<AmpReports> col = new ArrayList<AmpReports>();

		Session session = null;
		try {

			session = PersistenceManager.getRequestDBSession();
			AmpTeam team = (AmpTeam) session
					.load(AmpTeam.class, tm.getTeamId());

			AmpTeamMember ampteammember = TeamMemberUtil.getAmpTeamMember(tm
					.getMemberId());
			String queryString = null;
			Query qry = null;

			if (team.getAccessType().equalsIgnoreCase(
					Constants.ACCESS_TYPE_MNGMT)) {
				queryString = "select DISTINCT r from "
						+ AmpReports.class.getName()
						+ " r where r.drilldownTab=true AND (lower(r.name) LIKE lower(:keyword) OR r.reportDescription LIKE :keyword) AND (r.ownerId.ampTeamMemId = :memberid or r.ampReportId IN (select r2.report from "
						+ AmpTeamReports.class.getName()
						+ " r2 where r2.team.ampTeamId = :teamid and r2.teamView = true)) order by r.name";
				qry = session.createQuery(queryString);

				qry.setParameter("memberid", ampteammember.getAmpTeamMemId());
				qry.setParameter("teamid", tm.getTeamId());
				qry.setParameter("keyword", "%" + string + "%");
				col = qry.list();
			} else {
				queryString = "select distinct r from "
						+ AmpReports.class.getName()
						+ "  r left join r.members m where "
						+ " r.drilldownTab=true AND (lower(r.name) LIKE lower(:keyword) OR r.reportDescription LIKE :keyword) AND "
						+ " ((m.ampTeamMemId is not null and m.ampTeamMemId=:ampTeamMemId)"
						+ " or r.id in (select r2.id from "
						+ AmpTeamReports.class.getName()
						+ " tr inner join  tr.report r2 where tr.team=:teamId and tr.teamView = true))";
				qry = session.createQuery(queryString);
				qry.setLong("ampTeamMemId", tm.getMemberId());
				qry.setLong("teamId", tm.getTeamId());
				qry.setParameter("keyword", "%" + string + "%");
				col = qry.list();

			}

		} catch (Exception e) {
			logger.error("Exception from getTabs()", e);
			throw new RuntimeException(e);
		}

		resultList.addAll(col);
		return resultList;

	}

	public static Collection<LoggerIdentifiable> getActivities(String keyword, HttpServletRequest request, TeamMember tm) {
		Collection<LoggerIdentifiable> resultList = new ArrayList<LoggerIdentifiable>();
		StopWatch.reset("Search");
		AmpARFilter filter = new AmpARFilter();
//		StopWatch.next("Search", true,"mycomment 1");
		filter.setAmpTeams(new TreeSet());

		if (tm != null) {
			filter.setAccessType(tm.getTeamAccessType());
			filter.setAmpTeams(TeamUtil.getRelatedTeamsForMember(tm));
			Set teamAO = TeamUtil.getComputedOrgs(filter.getAmpTeams());

			if (teamAO != null && teamAO.size() > 0)
				filter.setTeamAssignedOrgs(teamAO);
		}
		filter.setIndexText(keyword);

		filter.generateFilterQuery(request);

		String hsqlQuery = filter.getGeneratedFilterQuery().replaceAll(
				"FROM amp_activity", "FROM " + AmpActivity.class.getName());
		hsqlQuery.replaceAll("FROM amp_team_activities", "FROM "
				+ AmpActivity.class.getName());

		Session session = null;
		List<AmpActivity> col = new ArrayList<AmpActivity>();
		try {
			session = PersistenceManager.getRequestDBSession();

			//not a very nice solution, but I kept the old code and idea and just added some speed
			String newQueryString = "select f.amp_activity_id, f.amp_id,  f.name from amp_activity f where f.amp_activity_id in ("+filter.getGeneratedFilterQuery()+")";
			SQLQuery newQuery = session.createSQLQuery(newQueryString).addScalar("amp_activity_id", org.hibernate.Hibernate.LONG);
			newQuery		  = newQuery.addScalar("amp_id", org.hibernate.Hibernate.STRING);
			newQuery		  = newQuery.addScalar("name", org.hibernate.Hibernate.STRING);
			
//			StopWatch.next("Search", true,"mycomment 2");
			Iterator iter = newQuery.list().iterator();
            while (iter.hasNext()) {
                Object[] item = (Object[])iter.next();
                Long ampActivityId = (Long) item[0];
                String ampId = (String) item[1];
                String name = (String) item[2];
                AmpActivityFake activity = new AmpActivityFake(name,ampId,ampActivityId);
                resultList.add(activity);
            }
//			StopWatch.next("Search", true,"mycomment 3");

		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultList;
	}

	public static Collection<LoggerIdentifiable> getResources(String keyword,
			HttpServletRequest request, TeamMember tm) {

		javax.jcr.Session jcrWriteSession = DocumentManagerUtil
				.getWriteSession(request);

		Collection<LoggerIdentifiable> resultList = new ArrayList<LoggerIdentifiable>();

		try {
			Node folderNode = jcrWriteSession.getRootNode();
			try {
				javax.jcr.query.QueryManager qm = folderNode.getSession()
						.getWorkspace().getQueryManager();
				javax.jcr.query.Query q = qm.createQuery(
						"SELECT * FROM nt:base WHERE jcr:path LIKE '/team/"
								+ tm.getTeamId() + "/%' ",
						javax.jcr.query.Query.SQL);

				javax.jcr.query.QueryResult result = q.execute();
				NodeIterator it = result.getNodes();
				while (it.hasNext()) {
					Node n = it.nextNode();
					NodeWrapper nw = new NodeWrapper(n);

					if (keywordMatches(nw, keyword)) {
						Resource resource = new Resource();
						resource.setName(nw.getTitle());
						resource.setUuid(nw.getUuid());
						resource.setWebLink(nw.getWebLink());
						resultList.add(resource);
					}
				}
				q = qm.createQuery(
						"SELECT * FROM nt:base WHERE jcr:path LIKE '/private/"
								+ tm.getTeamId() + "/" + tm.getEmail() + "/%' "
								+ "", javax.jcr.query.Query.SQL);

				result = q.execute();
				it = result.getNodes();
				while (it.hasNext()) {
					Node n = it.nextNode();
					NodeWrapper nw = new NodeWrapper(n);
					if (keywordMatches(nw, keyword)) {
						Resource resource = new Resource();
						resource.setName(nw.getTitle());
						resource.setUuid(nw.getUuid());
						resource.setWebLink(nw.getWebLink());
						resultList.add(resource);
					}
				}
				
				HashMap<String, CrDocumentNodeAttributes> pd = CrDocumentNodeAttributes.getPublicDocumentsMap(false);
				Set<String> keySet = pd.keySet();
				for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
					String uuid = (String) iterator.next();
					Node lastVersion = DocumentManagerUtil.getReadNode(uuid, request);
					NodeWrapper nw = new NodeWrapper(lastVersion);
					if (nw!=null&&keywordMatches(nw, keyword)) {
						Resource resource = new Resource();
						resource.setName(nw.getTitle());
						resource.setUuid(nw.getUuid());
						resource.setWebLink(nw.getWebLink());
						if(!resultList.contains(resource)){
							resultList.add(resource);
						}
					}					
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DocumentManagerUtil.logoutJcrSessions(request.getSession());
		return resultList;
	}

	private static boolean keywordMatches(NodeWrapper n, String keyword) {
		String title = n.getTitle();
		String description = n.getDescription();
		String link = n.getWebLink();
		String name = n.getName();

		if (title == null)
			return false;
		
		if (title!=null&&title.toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
			return true;
		}

		if (description!=null&&description.toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
			return true;
		}

		if (link != null) // it's a link
		{
			if (link.toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
				return true;
			}
		} else // It's a doc
		{
			if (name!=null&&name.toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
				return true;
			}
		}
		return false;
	}
	public static Collection<LoggerIdentifiable> getActivitiesUsingRelatedOrgs(String keyword, TeamMember tm, String roleCode) throws DgException {
        Collection<LoggerIdentifiable> activities = new ArrayList<LoggerIdentifiable>();
        Set<AmpTeam> teams=TeamUtil.getRelatedTeamsForMember(tm);
        Set teamAO = TeamUtil.getComputedOrgs(teams);
        boolean hasComputedOrgs = teamAO != null && !teamAO.isEmpty();
        StringBuilder query = new StringBuilder();
        query.append(" select act from ");
        query.append(AmpActivity.class.getName());
        query.append(" act inner join act.team team ");
        query.append(" inner join act.orgrole role ");
        query.append(" inner join role.organisation org ");
        query.append(" inner join role.role roleCode ");
        query.append("where (team.ampTeamId in (");
        query.append(Util.toCSString(teams));
        query.append( ")");
        if(hasComputedOrgs){
              query.append( " or org.ampOrgId in (");
              query.append( Util.toCSString(teamAO));
              query.append( ")");
        }
        query.append(")");
        query.append(" and roleCode.roleCode=:roleCode ");
        if (!hasComputedOrgs) {
            query.append(" and org.name like :name");
            if (tm.getTeamAccessType().equals("Management")) {
                query.append(" and act.draft=false and act.approvalStatus ='approved' ");
            }
        }
       
        Session session = PersistenceManager.getRequestDBSession();
        Query qry = session.createQuery(query.toString());
       
        if (hasComputedOrgs) {
            qry.setString("roleCode", "DN");
        }
        else{
            qry.setString("roleCode", roleCode);
            qry.setString("name", '%' + keyword + '%');
        }
        
        List<AmpActivity> result = qry.list();
        if (result != null && !result.isEmpty()) {
            if (!hasComputedOrgs) {
                activities.addAll(result);
            } else {
                StringBuilder queryString = new StringBuilder();
                queryString.append(" select act from ");
                queryString.append(AmpActivity.class.getName());
                queryString.append(" act ");
                queryString.append(" inner join act.orgrole role ");
                queryString.append(" inner join role.organisation org ");
                queryString.append(" inner join role.role roleCode ");
                queryString.append(" where roleCode.roleCode=:roleCode ");
                queryString.append(" and act.ampActivityId in (");
                queryString.append(Util.toCSString(result));
                queryString.append(")");
                queryString.append(" and org.name like :name");
                qry = session.createQuery(queryString.toString());
                qry.setString("roleCode", roleCode);
                qry.setString("name", '%' + keyword + '%');
                result = qry.list();
                if (result != null && !result.isEmpty()) {
                    activities.addAll(result);
                }
                
            }

        }
        return activities;
    }


}
