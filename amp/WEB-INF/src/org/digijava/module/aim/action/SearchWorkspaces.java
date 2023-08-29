package org.digijava.module.aim.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.WorkspaceForm;
import org.digijava.module.aim.util.TeamUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStreamWriter;
import java.util.*;

public class SearchWorkspaces extends Action {
    private static Logger logger = Logger.getLogger(SearchWorkspaces.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }

        logger.debug("In User manager action");

        WorkspaceForm vwForm = (WorkspaceForm) form;

        int page = 0;

        JSONObject json = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        String startIndex = request.getParameter("startIndex");
        String results = request.getParameter("results");
        int startIndexInt = 0;
        int resultsInt = 10;
        if (startIndex != null){
            startIndexInt = Integer.parseInt(startIndex);
        }
        if (results != null){
            resultsInt = Integer.parseInt(results);
        }

        sortWorkspaces(request, vwForm);
        
        Collection<AmpTeam> ampWorkspaces = (Collection<AmpTeam>) vwForm.getWorkspaces();
        int stIndex = startIndexInt;
        int edIndex = (startIndexInt + resultsInt)>ampWorkspaces.size()?ampWorkspaces.size():(startIndexInt + resultsInt);

        List list = new ArrayList();

        list.addAll(ampWorkspaces);

        Collection tempCol = new ArrayList();

        for (int i = stIndex; i < edIndex; i++) {
            tempCol.add(list.get(i));
        }

        for (Iterator it = tempCol.iterator(); it.hasNext();) {
            AmpTeam team = (AmpTeam) it.next();
            JSONObject jteam = new JSONObject();
            jteam.put("ID", team.getAmpTeamId());
            jteam.put("name", team.getName());
            jteam.put("accessType", team.getAccessType());

            if (team.getComputation() != null && team.getComputation()) {
                 jteam.put("computation", "yes");
            }
            else {
                 jteam.put("computation", "no");
            }

            JSONArray jsonOrganizationArray = new JSONArray();
            Set<AmpOrganisation> organizations = team.getOrganizations();
            if (organizations != null) {
               for (AmpOrganisation childOrg : organizations) {
                   JSONObject jChildOrg = new JSONObject();
                   jChildOrg.put("name", childOrg.getName());
                   jsonOrganizationArray.add( jChildOrg);
               }
            }

           JSONArray jsonChildrenTeamArray = new JSONArray();
           Collection<AmpTeam> childrenTeams = TeamUtil.getAllChildrenWorkspaces(team.getAmpTeamId());
            if (childrenTeams != null) {
               for(AmpTeam childteam : childrenTeams) {
                   JSONObject jChildTeam = new JSONObject();
                   jChildTeam.put("name", childteam.getName());
                   jsonChildrenTeamArray.add(jChildTeam);
               }
            }

            jteam.put("childrenOrganizations", jsonOrganizationArray);
            jteam.put("childrenWorkspaces", jsonChildrenTeamArray);
            jsonArray.add(jteam);

        }

        Integer totalRecords = ampWorkspaces.size();
        json.put("recordsReturned", tempCol.size());
        json.put("totalRecords", totalRecords);
        json.put("startIndex", startIndexInt);
        json.put("sort", null);
        json.put("dir", "asc");
        json.put("pageSize", 10);
        json.put("rowsPerPage", 10);

        json.put("workspaces", jsonArray);

        response.setContentType("text/json-comment-filtered");
        OutputStreamWriter outputStream = null;

        try {
            outputStream = new OutputStreamWriter(response.getOutputStream(),
                    "UTF-8");
            outputStream.write(json.toString());
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return null;
    }

    private void sortWorkspaces(HttpServletRequest request, WorkspaceForm vwForm) {
        Collection<AmpTeam> ampWorkspaces = (Collection<AmpTeam>) vwForm.getWorkspaces();
        if (ampWorkspaces != null) {
            List<AmpTeam> ampTeams = new ArrayList(ampWorkspaces);

            String sortBy = request.getParameter("sort");
            String sortDir = request.getParameter("dir");

            if (sortBy != null && sortDir != null) {
                if (sortBy.equals("name") && sortDir.equals("asc")) {
                    Collections.sort(ampTeams, new TeamUtil.HelperAmpTeamNameComparatorTrimmed());
                } else if (sortBy.equals("name") && sortDir.equals("desc")) {
                    Collections.sort(ampTeams, new TeamUtil.HelperAmpTeamNameComparatorDescTrimmed());
                }
            }
            vwForm.setWorkspaces(ampTeams);
        }
    }

}
