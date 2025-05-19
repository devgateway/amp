package org.digijava.module.aim.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.form.TeamMemberForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.um.util.AmpUserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStreamWriter;
import java.util.*;

public class GetAvailableUsersForWorkspaceJSON extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {

        HttpSession session = request.getSession();
//      if (session.getAttribute("ampAdmin") == null) {
//          return mapping.findForward("index");
//      } else {
//          String str = (String) session.getAttribute("ampAdmin");
//          if (str.equals("no")) {
//              return mapping.findForward("index");
//          }
//      }
        
        Site site = RequestUtils.getSite(request);
        Long siteId=site.getId();
        
        Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
        String locale=navigationLanguage.getCode();

        TeamMemberForm tmForm = (TeamMemberForm) form;
        if(tmForm.getTeamId()!=null){
            Collection<User> allUsers= AmpUserUtil.getAllUsersNotBelongingToTeam(tmForm.getTeamId(),tmForm.getFullname());
            tmForm.setallUser(allUsers);
        }

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

        sortUsers(request, tmForm);
        
        Collection<User> availableUsers = (Collection<User>) tmForm.getallUser();
        int stIndex = startIndexInt;
        int edIndex = (startIndexInt + resultsInt)>=availableUsers.size()?availableUsers.size()-1:(startIndexInt + resultsInt);

        Vector<User> vect = new Vector<User>();

        vect.addAll(availableUsers);

        Collection<User> tempCol = new ArrayList<User>();

        for (int i = stIndex; i <= edIndex; i++) {
            tempCol.add(vect.get(i));
        }
        for (Iterator<User> it = tempCol.iterator(); it.hasNext();) {
            User user = (User) it.next();
            JSONObject juser = new JSONObject();
            juser.put("ID", user.getId());
            juser.put("name", user.getName());
            juser.put("email", user.getEmail());
            String orgs="";
            if(user.getAssignedOrgs()!=null && user.getAssignedOrgs().size()>0){
                orgs+="<ul>";
                for (AmpOrganisation userOrg : user.getAssignedOrgs()) {
                    orgs+="<li>"+userOrg.getName()+"</li>";
                }
                orgs+="</ul>";
            }
            juser.put("organizations", orgs);
            String roles = "<select class=\"inp-text\" id=\"role_" + user.getId()
                    + "\" onchange=\"updateUserRole(this)\" >";
            roles += "<option value=\"-1\" >-----" + TranslatorWorker.translateText("Select Role", locale, siteId)
                    + "-----</option>";
            Collection<AmpTeamMemberRoles> allRoles= TeamMemberUtil.getAllTeamMemberRoles();
            if(allRoles!=null && allRoles.size()>0){
                for (AmpTeamMemberRoles ampTmRole : allRoles) {
                    roles+="<option value=\""+user.getId()+"_"+ampTmRole.getAmpTeamMemRoleId()+"\" >";
                    roles+=TranslatorWorker.translateText(ampTmRole.getRole(), locale, siteId);
                    roles+="</option>";
                }
            }
            roles+="</select>";
            juser.put("role", roles);
            //checkbox
            String chkBox="<input type=\"checkbox\" value=\""+user.getId()+"\" id=\"chk_"+user.getId()+"\" class=\"selectedUsers\" onclick=\"pickUsersForTeam(this)\">";
            //String chkBox="<input type=\"checkbox\" name=\"userIdsWithRoles\" value=\""+user.getId()+"\" id=\"chk_"+user.getId()+"\" class=\"selectedUsers\">";
            juser.put("chkBox", chkBox);
            jsonArray.add(juser);

        }
        Integer totalRecords = availableUsers.size();
        json.put("recordsReturned", tempCol.size());
        json.put("totalRecords", totalRecords);
        json.put("startIndex", startIndexInt);
        json.put("sort", null);
        json.put("dir", "asc");
        json.put("pageSize", 10);
        json.put("rowsPerPage", 10);

        json.put("users", jsonArray);

        response.setContentType("text/json-comment-filtered");
        OutputStreamWriter outputStream = null;

        try {
            outputStream = new OutputStreamWriter(response.getOutputStream(),"UTF-8");
            outputStream.write(json.toString());
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }   
        
        return null;
    }
    
    private void sortUsers(HttpServletRequest request, TeamMemberForm tmForm) {
        Collection<User> availableUsers = (Collection<User>) tmForm.getallUser();
        if (availableUsers != null) {
            List<User> ampUsers = new ArrayList<User>(availableUsers);

            String sortBy = request.getParameter("sort");
            String sortDir = request.getParameter("dir");

            if (sortBy != null && sortDir != null) {
                if (sortBy.equals("name") && sortDir.equals("asc")) {
                    Collections.sort(ampUsers, new DbUtil.HelperUserNameComparatorAsc());
                } else if (sortBy.equals("name") && sortDir.equals("desc")) {
                    Collections.sort(ampUsers, new DbUtil.HelperUserNameComparatorDesc());
                }else if(sortBy.equals("email") && sortDir.equals("asc")){
                    Collections.sort(ampUsers, new DbUtil.HelperEmailComparatorAsc());
                }else if(sortBy.equals("email") && sortDir.equals("desc")){
                    Collections.sort(ampUsers, new DbUtil.HelperEmailComparatorDesc());
                }
            }
            tmForm.setallUser(ampUsers);
        }
    }
}
