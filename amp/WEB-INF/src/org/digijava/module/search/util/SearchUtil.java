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
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.search.helper.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.digijava.module.contentrepository.dbentity.*;

public class SearchUtil {

	private static Logger logger = Logger.getLogger(SearchUtil.class);

	public static final int QUERY_ALL = -1;
	public static final int ACTIVITIES = 0;
	public static final int REPORTS = 1;
	public static final int TABS = 2;
	public static final int RESOURCES = 3;

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

	public static Collection<LoggerIdentifiable> getActivities(String keyword,
			HttpServletRequest request, TeamMember tm) {
		Collection<LoggerIdentifiable> resultList = new ArrayList<LoggerIdentifiable>();

		AmpARFilter filter = new AmpARFilter();

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
			List result = session.createSQLQuery(
					filter.getGeneratedFilterQuery()).addScalar(
					"amp_activity_id", org.hibernate.Hibernate.LONG).list();

			Iterator it = result.iterator();
			while (it.hasNext()) {
				Long activityId = (Long) it.next();

				AmpActivityVersion act = ActivityUtil.loadActivity(activityId);
				resultList.add(act);

			}

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

}
