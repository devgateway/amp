package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.WorkspaceForm;
import org.digijava.module.aim.form.helpers.WorkspaceDataSelection;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.search.util.SearchUtil;
import org.hibernate.Hibernate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

public class WorkspaceManager extends Action {

    private static Logger logger = Logger.getLogger(WorkspaceManager.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }

        int NUM_RECORDS = 10000;
        Collection<AmpTeam> workspaces = new ArrayList<AmpTeam>();
        WorkspaceForm wsForm = (WorkspaceForm) form;

        ArrayList<WorkspaceDataSelection> workspaceDataSelections = new ArrayList<>();

        WorkspaceDataSelection members = new WorkspaceDataSelection();
        members.setLabel("Members");
        members.setValue("0");
        workspaceDataSelections.add(members);

        WorkspaceDataSelection activities = new WorkspaceDataSelection();
        activities.setLabel("Activities");
        activities.setValue("1");
        workspaceDataSelections.add(activities);

        wsForm.setWorkspaceDataSelections(workspaceDataSelections);


        if (request.getParameter("reset") != null && request.getParameter("reset").equalsIgnoreCase("true")) {
            wsForm.setKeyword(null);
            wsForm.setWorkspaceType("all");
            wsForm.setNumPerPage(-1);
        }

        if (session.getAttribute("fromPage") != null) {
            wsForm.setCurrentPage((Integer)session.getAttribute("fromPage"));
            session.removeAttribute("fromPage");
        }

        if (session.getAttribute("selectedRow") != null) {
            wsForm.setCurrentRow((Integer)session.getAttribute("selectedRow"));
            session.removeAttribute("selectedRow");
        }

        if (session.getAttribute("selectedWs") != null) {
            wsForm.setSelectedWs((Long)session.getAttribute("selectedWs"));
            session.removeAttribute("selectedWs");
        }

        String keyword = wsForm.getKeyword();
        Collection<String> keywords=new ArrayList<String>();
        StringTokenizer st = new StringTokenizer("");
        if (keyword != null) {
            st = new StringTokenizer(keyword);
        }

        while (st.hasMoreTokens()) {
            keywords.add(st.nextToken().toLowerCase());
        }

        String temp = request.getParameter("page");
        if (temp != null) {
            wsForm.setPage(Integer.parseInt(temp));
        } else if (wsForm.getPage() <= 0) {
            wsForm.setPage(1);
        }

        //all teams need to be refreshed after any change, AMP-17855
        Collection<AmpTeam> ampWorkspaces = null;
        ampWorkspaces = TeamUtil.getAllTeams();
        session.setAttribute("ampWorkspaces", ampWorkspaces);


        if (wsForm.getNumPerPage() != -1) {
            NUM_RECORDS = wsForm.getNumPerPage();
        } else {
            NUM_RECORDS = ampWorkspaces.size();
        }

        /*
         * check whether the numPages is less than the page . if yes return
         * error.
         */
        int currPage = wsForm.getPage();
        int stIndex = ((currPage - 1) * NUM_RECORDS);
        int edIndex = stIndex + NUM_RECORDS;

        String workspaceType = wsForm.getWorkspaceType();
        Long workspaceGroup = wsForm.getWorkspaceGroup();
        for (AmpTeam ampTeam : ampWorkspaces) {

            Boolean addTeam = false;
            if (workspaceType != null) {
                if ("all".equals(workspaceType)) {
                    addTeam = true;
                } else if("team".equals(workspaceType) && "Team".equals(ampTeam.getAccessType())) {
                    addTeam = true;
                } else if("management".equals(workspaceType) && "Management".equals(ampTeam.getAccessType())) {
                    addTeam = true;
                } else if("computed".equals(workspaceType) && ampTeam.getComputation()!=null && ampTeam.getComputation()) {
                    addTeam = true;
                }
            }

            if (workspaceGroup != null && workspaceGroup != 0 && addTeam) {
                addTeam = ampTeam.getWorkspaceGroup() != null && (ampTeam.getWorkspaceGroup().getId().compareTo(workspaceGroup) == 0);
            }

            if (addTeam) {
                ampTeam.setChildrenWorkspaces(TeamUtil.getAllChildrenWorkspaces(ampTeam.getAmpTeamId()));
                workspaces.add(ampTeam);
            }
        }

        //pages
        int numPages = 0;
        if (NUM_RECORDS != 0) {
            numPages=workspaces.size() / NUM_RECORDS;
            numPages += (workspaces.size() % NUM_RECORDS != 0) ? 1 : 0;
        }

        //workspaces for current page
        if (edIndex > workspaces.size()) {
            edIndex = workspaces.size();
        }

        workspaces = ((List<AmpTeam>)workspaces).subList(stIndex, edIndex);

        Collection<Integer> pages = null;
        if (numPages > 1) {
            pages = new ArrayList<Integer>();
            for (int i = 0; i < numPages; i++) {
                Integer pageNum = i + 1;
                pages.add(pageNum);
            }
        }

        Collection<AmpTeam> workspacesFiltered = new ArrayList<AmpTeam>();
        if (! workspaces.isEmpty()) {
            for (AmpTeam team : workspaces) {

                // we need this initialization, because this collection will be used
                // in the next Hibernate transaction, in SearchWorkspaces.java
                // The explicit initialization was removed in scope of AMP-20228
                Hibernate.initialize(team.getOrganizations());

                boolean found = false;
                for (String keyw : keywords) {
                    java.util.Locale currentLocale = new java.util.Locale(TLSUtils.getEffectiveLangCode());
                    found |= "".equals(keyw) || SearchUtil.TeamContainsKeyword(team, keyw, currentLocale);
                }
                if (found) {
                    workspacesFiltered.add(team);
                }
            }
        }

        if (workspacesFiltered.isEmpty() && keywords.isEmpty()) {
            wsForm.setWorkspaces(workspaces);
        } else {
            wsForm.setWorkspaces(workspacesFiltered);
        }

        wsForm.setPages(pages);

        return mapping.findForward("forward");
    }
}
