package org.digijava.module.um.action;

import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.module.um.form.ViewAllUsersForm;
import java.util.Collection;
import org.digijava.kernel.user.User;
import java.util.ArrayList;
import org.digijava.module.aim.util.DbUtil;
import java.util.*;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.um.util.AmpUserUtil;

public class ViewAllUsers
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        ViewAllUsersForm vwForm=(ViewAllUsersForm)form;

        Collection<User> users=new ArrayList<User>();
        if(users==null){
            return mapping.findForward("forward");
        }

        if(vwForm.getType()==-1){
            users=AmpUserUtil.getAllUsers();
        }else if(vwForm.getType()==0){
            users=TeamMemberUtil.getAllTeamMemberUsers();
        }else if(vwForm.getType()==1){
            users=AmpUserUtil.getAllUsers();
            if(users!=null){
                Collection teamMemberUsers = TeamMemberUtil.getAllTeamMemberUsers();
                if(teamMemberUsers != null) {
                    for(Iterator usersIter = users.iterator(); usersIter.hasNext(); ) {
                        User user = (User) usersIter.next();
                        for(Iterator teamMemberUserIter = teamMemberUsers.iterator(); teamMemberUserIter.hasNext(); ) {
                            User teamMemberUser = (User) teamMemberUserIter.next();
                            if(teamMemberUser.getId().equals(user.getId())){
                                usersIter.remove();
                                break;
                            }
                        }
                    }
                }
            }
        }

        if(vwForm.getKeyword()!=null && users!=null){
            for(Iterator userIter = users.iterator(); userIter.hasNext(); ) {
                User user = (User) userIter.next();
                if(user.getEmail().indexOf(vwForm.getKeyword())==-1){
                    userIter.remove();
                }
            }
        }

        if(users!=null){
            for(Iterator userIter = users.iterator(); userIter.hasNext(); ) {
                User user = (User) userIter.next();
                if(user.getOrganizationName()==null){
                    userIter.remove();
                }
            }
        }
        if(users!=null){
            List<User> sortedUser=new ArrayList(users);
            Collections.sort(sortedUser,new DbUtil.HelperUserNameComparator());
            vwForm.setUsers(sortedUser);
        }

        return mapping.findForward("forward");
    }

    public ViewAllUsers() {
    }
}
