package org.digijava.module.search.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.admin.helper.AmpActivityFake;
import org.digijava.module.admin.helper.AmpPledgeFake;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrganisation;
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
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.search.helper.Resource;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.LongType;

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
    public static final int PLEDGE = 7;

    public static Collection<? extends LoggerIdentifiable> getReports(TeamMember tm,
            String string) {
        // TODO: Unify this with getTabs()
        string  = string.replace("*", "").toLowerCase();
        
        List<AmpReports> col;

        Session session = null;
        try {

            session = PersistenceManager.getRequestDBSession();
            AmpTeam team = (AmpTeam) session
                    .load(AmpTeam.class, tm.getTeamId());

            AmpTeamMember ampteammember = TeamMemberUtil.getAmpTeamMember(tm
                    .getMemberId());
            String queryString = null;
            Query qry = null;
            
            String reportNameHql = AmpReports.hqlStringForName("r");
            String reportDescriptionHql = AmpReports.hqlStringForDescription("r");
                
            if (team.getAccessType().equalsIgnoreCase(
                    Constants.ACCESS_TYPE_MNGMT)) {
                queryString = "select DISTINCT r from "
                        + AmpReports.class.getName()
                        + " r where r.drilldownTab=false AND (" + buildLike(string,reportNameHql) + " OR " + buildLike(string,reportDescriptionHql) + " ) AND (r.ownerId.ampTeamMemId = :memberid or r.ampReportId IN (select r2.report from "
                        + AmpTeamReports.class.getName()
                        + " r2 where r2.team.ampTeamId = :teamid and r2.teamView = true)) ";
                qry = session.createQuery(queryString);

                qry.setParameter("memberid", ampteammember.getAmpTeamMemId());
                qry.setParameter("teamid", tm.getTeamId());
                addParameter(qry,string);
                
                col = qry.list();
            } else {
                queryString = "select distinct r from "
                        + AmpReports.class.getName()
                        + "  r left join r.members m where "
                        + " r.drilldownTab=false AND (" + buildLike(string,reportNameHql) + " OR " + buildLike(string,reportDescriptionHql) + " ) AND "
                        + " ((m.ampTeamMemId is not null and m.ampTeamMemId=:ampTeamMemId)"
                        + " or r.id in (select r2.id from "
                        + AmpTeamReports.class.getName()
                        + " tr inner join  tr.report r2 where tr.team=:teamId and tr.teamView = true))";
                qry = session.createQuery(queryString);
                qry.setLong("ampTeamMemId", tm.getMemberId());
                qry.setLong("teamId", tm.getTeamId());
                addParameter(qry,string);
                col = qry.list();

            }

        } catch (Exception e) {
            logger.error("Exception from getReports()", e);
            throw new RuntimeException(e);
        }
        ArrayList<AmpReports> resultList = new ArrayList<AmpReports>(col);
        Collections.sort(resultList);
        return resultList;
    }
    
    public static String buildLike(String string, String field) {
        String query = "";
        String[] str = string.split(" ");
        int index = 1;
        for (String s : str) {
            query += (query != "" ? " OR " : "") + " lower(" + field + ") LIKE :keyword" + index++ + " ";
        }
        query = "( " + query + ")";

        return query;
    }
    
    public static void addParameter(Query qry, String string) {
        int index = 1;
        String[] str = string.split(" ");
        for (String s : str) {
            qry.setParameter("keyword" + index++, "%" + s + "%");
        }

    }

    public static Collection<? extends LoggerIdentifiable> getTabs(TeamMember tm, String string) {

        string = string.replace("*", "").toLowerCase();

        List<AmpReports> col;

        Session session = null;
        try {

            session = PersistenceManager.getRequestDBSession();
            AmpTeam team = (AmpTeam) session.load(AmpTeam.class, tm.getTeamId());

            AmpTeamMember ampteammember = TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
            String queryString = null;
            Query qry = null;

            String reportNameHql = AmpReports.hqlStringForName("r");
            String reportDescriptionHql = AmpReports.hqlStringForDescription("r");

            if (team.getAccessType().equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
                queryString = "select DISTINCT r from "
                        + AmpReports.class.getName()
                        + " r where r.drilldownTab=true AND (" + buildLike(string,reportNameHql) + " OR "
                        + buildLike(string,reportDescriptionHql)
                        + " ) AND (r.ownerId.ampTeamMemId = :memberid or r.ampReportId IN (select r2.report from "
                        + AmpTeamReports.class.getName() + " r2 where r2.team.ampTeamId = :teamid and r2.teamView = true)) ";
                qry = session.createQuery(queryString);

                qry.setParameter("memberid", ampteammember.getAmpTeamMemId());
                qry.setParameter("teamid", tm.getTeamId());
                addParameter(qry,string);
                col = qry.list();
            } else {
                queryString = "select distinct r from " + AmpReports.class.getName() + "  r left join r.members m where "
                        + " r.drilldownTab=true AND (" + buildLike(string,reportNameHql) + " OR "
                        + buildLike(string,reportDescriptionHql) + ") AND "
                        + " ((m.ampTeamMemId is not null and m.ampTeamMemId=:ampTeamMemId)"
                        + " or r.id in (select r2.id from " + AmpTeamReports.class.getName()
                        + " tr inner join  tr.report r2 where tr.team=:teamId and tr.teamView = true))";
                qry = session.createQuery(queryString);
                qry.setLong("ampTeamMemId", tm.getMemberId());
                qry.setLong("teamId", tm.getTeamId());
                addParameter(qry,string);
                col = qry.list();

            }

        } catch (Exception e) {
            logger.error("Exception from getTabs()", e);
            throw new RuntimeException(e);
        }

        ArrayList<AmpReports> resultList = new ArrayList<AmpReports>(col);
        Collections.sort(resultList);
        return resultList;

    }

    public static Collection<LoggerIdentifiable> getPledges(String keyword, HttpServletRequest request) {
        Collection<LoggerIdentifiable> resultList = new ArrayList<LoggerIdentifiable>();
        StopWatch.reset("Search");

        AmpARFilter filter = new AmpARFilter();

        /**
         * AmpARFilter.FILTER_SECTION_ALL, null - parameters were added on
         * merge, might not be right
         */
        Session session = null;
        List<FundingPledges> col = new ArrayList<FundingPledges>();

        session = PersistenceManager.getSession();

        // not a very nice solution, but I kept the old code and idea and just
        // added some speed
        // String newQueryString = "SELECT f.id " +
        // AmpActivity.sqlStringForName("f.id") +
        // " AS name, FROM amp_funding_pledges f";

        // keyword.replaceAll is done for single-quote escaping;
        // normally, extended by
        // org.apache.commons.lang.StringEscapeUtils.escapeSql should be used,
        // but it does exactly the same thing
        String newQueryString = "SELECT f.pledge_id, f.title FROM v_pledges_titles f WHERE lower(f.title) LIKE  lower('%"
                + keyword.replaceAll("'", "''") + "%')";
        SQLQuery newQuery = session.createSQLQuery(newQueryString).addScalar("pledge_id",
                org.hibernate.type.StandardBasicTypes.LONG);
        newQuery = newQuery.addScalar("title", org.hibernate.type.StandardBasicTypes.STRING);

        List<Object[]> items = newQuery.list();
        for (Object[] item : items) {
            Long ampId = (Long) item[0];
            String name = (String) item[1];
            AmpPledgeFake pledge = new AmpPledgeFake(name, ampId);
            resultList.add(pledge);
        }
        // StopWatch.next("Search", true,"mycomment 3");
        return resultList;
    }

    public static Collection<LoggerIdentifiable> getActivities(String keyword, HttpServletRequest request, TeamMember tm) {

        StopWatch.reset("Search");

        String workspaceQuery = WorkspaceFilter.generateWorkspaceFilterQuery(tm);

        AmpARFilter filter = new AmpARFilter();

        /**
         * AmpARFilter.FILTER_SECTION_ALL, null - parameters were added on
         * merge, might not be right
         */
        filter.readRequestData(request, AmpARFilter.FILTER_SECTION_ALL, null); // init
                                                                                // teamAO
                                                                                // and
                                                                                // other
                                                                                // auxiliary
                                                                                // info
        filter.setIndexText(keyword);
        filter.generateFilterQuery(request, false, true);

        // String hsqlQuery = filter.getGeneratedFilterQuery().replaceAll(
        // "FROM amp_activity", "FROM " + AmpActivity.class.getName());
        // hsqlQuery.replaceAll("FROM amp_team_activities", "FROM "
        // + AmpActivity.class.getName());
        // hsqlQuery isn't actually used anywhere
        Session session = null;
        List<AmpActivity> col = new ArrayList<AmpActivity>();

        session = PersistenceManager.getSession();

        // not a very nice solution, but I kept the old code and idea and just
        // added some speed
        String newQueryString = "SELECT f.amp_activity_id, f.amp_id, "
                + AmpActivityVersion.sqlStringForName("f.amp_activity_id")
                + " AS name, f.approval_status, f.draft FROM amp_activity f WHERE f.amp_activity_id in ("
                + filter.getGeneratedFilterQuery() + ""
                /* + "INTERSECT "+ workspaceQuery */+ ")";
        SQLQuery newQuery = session.createSQLQuery(newQueryString).addScalar("amp_activity_id", LongType.INSTANCE);
        newQuery = newQuery.addScalar("amp_id", org.hibernate.type.StandardBasicTypes.STRING);
        newQuery = newQuery.addScalar("name", org.hibernate.type.StandardBasicTypes.STRING);
        newQuery = newQuery.addScalar("approval_status", org.hibernate.type.StandardBasicTypes.STRING);
        newQuery = newQuery.addScalar("draft", org.hibernate.type.StandardBasicTypes.BOOLEAN);

        LinkedHashMap<Long, LoggerIdentifiable> sortingActivities = new LinkedHashMap<>();
        for (Long i : filter.getAmpActivityIdOrder()) {
            sortingActivities.put(i,null);
        }

        // StopWatch.next("Search", true,"mycomment 2");
        // ignore the warning
        List<Object[]> items = newQuery.list();
        for (Object[] item : items) {
            Long ampActivityId = (Long) item[0];
            String ampId = (String) item[1];
            String name = (String) item[2];
            String status = (String) item[3];
            Boolean draft = (Boolean) item[4];
            if (draft == null)
                draft = false;
            if (status == null)
                status = "";
            AmpActivityFake activity = new AmpActivityFake(name, ampId, ampActivityId);
            activity.setDraft(draft);
            activity.setStatus(status);
            sortingActivities.put(activity.getAmpActivityId(), activity);
        }
        // StopWatch.next("Search", true,"mycomment 3");
        sortingActivities.values().removeIf(Objects::isNull);
        return sortingActivities.values();
    }

    public static Collection<LoggerIdentifiable> getResources(String keyword, HttpServletRequest request, TeamMember tm) {

        keyword = keyword.replace("*", "");

        javax.jcr.Session jcrWriteSession = DocumentManagerUtil.getWriteSession(request);

        Collection<LoggerIdentifiable> resultList = new ArrayList<LoggerIdentifiable>();

        try {
            Node folderNode = jcrWriteSession.getRootNode();
            try {
                javax.jcr.query.QueryManager qm = folderNode.getSession().getWorkspace().getQueryManager();
                javax.jcr.query.Query q = qm.createQuery(
                        "SELECT * FROM nt:base WHERE jcr:path LIKE '/team/" + tm.getTeamId() + "/%' ",
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
                        "SELECT * FROM nt:base WHERE jcr:path LIKE '/private/" + tm.getTeamId() + "/" + tm.getEmail()
                                + "/%' " + "", javax.jcr.query.Query.SQL);

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
                for (String uuid : keySet) {
                    Node lastVersion = DocumentManagerUtil.getReadNode(uuid, request);
                    if (lastVersion != null) {
                        NodeWrapper nw = new NodeWrapper(lastVersion);
                        if (keywordMatches(nw, keyword)) {
                            Resource resource = new Resource();
                            resource.setName(nw.getTitle());
                            resource.setUuid(nw.getUuid());
                            resource.setWebLink(nw.getWebLink());
                            if (!resultList.contains(resource)) {
                                resultList.add(resource);
                            }
                        }
                    } else {
                        logger.warn("Missing resource in the JCR repository: " + uuid);
                    }
                }

            } catch (Exception e) {
                logger.error("Exception reading resources", e);
                return Collections.emptyList();
            }

        } catch (RepositoryException e) {
            logger.error("Exception reading resources from JCR repository", e);
            return Collections.emptyList();
        }
        DocumentManagerUtil.logoutJcrSessions(request);
        return resultList;
    }

    private static boolean keywordMatches(NodeWrapper n, String keyword) {
        String title = n.getTitle();
        String description = n.getDescription();
        String link = n.getWebLink();
        String name = n.getName();

        if (title == null)
            return false;

        if (title != null && title.toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
            return true;
        }

        if (description != null && description.toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
            return true;
        }

        if (link != null) // it's a link
        {
            if (link.toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
                return true;
            }
        } else // It's a doc
        {
            if (name != null && name.toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
                return true;
            }
        }
        return false;
    }

    public static Collection<LoggerIdentifiable> getActivitiesUsingRelatedOrgs(String keyword, TeamMember tm, String roleCode)
            throws DgException {
        Collection<LoggerIdentifiable> activities = new ArrayList<LoggerIdentifiable>();
        Set<AmpTeam> teams = TeamUtil.getRelatedTeamsForMember(tm);
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
        query.append(Util.toCSStringForIN(teams));
        query.append(")");
        String orgNameHql = AmpOrganisation.hqlStringForName("org");
        if (hasComputedOrgs) {
            query.append(" or org.ampOrgId in (");
            query.append(Util.toCSStringForIN(teamAO));
            query.append(")");
        }
        query.append(")");
        query.append(" and roleCode.roleCode=:roleCode ");
        if (!hasComputedOrgs) {
            query.append(" and " + buildLike(keyword,orgNameHql) + " ");
            if (tm.getTeamAccessType().equals("Management")) {
                query.append(String.format(
                        " and (act.draft=false or act.draft is null) and act.approvalStatus in ('%s', '%s') ",
                        Constants.STARTED_APPROVED_STATUS, Constants.APPROVED_STATUS));
            }
        }

        Session session = PersistenceManager.getRequestDBSession();
        Query qry = session.createQuery(query.toString());

        if (hasComputedOrgs) {
            qry.setString("roleCode", "DN");
        } else {
            qry.setString("roleCode", roleCode);
            addParameter(qry,keyword.toLowerCase());
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
                queryString.append(Util.toCSStringForIN(result));
                queryString.append(")");
                queryString.append(" and " + orgNameHql + " like :name");
                qry = session.createQuery(queryString.toString());
                qry.setString("roleCode", roleCode);
                qry.setString("name", '%' + keyword.toLowerCase() + '%');
                result = qry.list();
                if (result != null && !result.isEmpty()) {
                    activities.addAll(result);
                }

            }

        }
        return activities;
    }

    public static boolean TeamContainsKeyword(AmpTeam team, String keyword, java.util.Locale locale) {
        return ((team.getDescription() != null) && SearchUtil.stringContainsKeyword(team.getDescription(), keyword, locale))
                || ((team.getName() != null) && SearchUtil.stringContainsKeyword(team.getName(), keyword, locale));

    }

    /* Unicode-friendly function for search, ignores diacritics via Normalizer */
    public static boolean stringContainsKeyword(String source, String keyword, java.util.Locale locale) {
        String normSource = Normalizer.normalize(source.toLowerCase(), Normalizer.Form.NFD);
        String normKeyword = Normalizer.normalize(keyword, Normalizer.Form.NFD);

        String remSource = normSource.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String remKeyword = normKeyword.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return remSource.toLowerCase(locale).contains(remKeyword.toLowerCase(locale));

    }

}
