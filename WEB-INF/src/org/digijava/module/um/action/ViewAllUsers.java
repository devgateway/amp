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
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.UserBean;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.util.TeamUtil;

public class ViewAllUsers
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ViewAllUsersForm vwForm = (ViewAllUsersForm) form;
        Collection ubCol = getUsers();

        if(ubCol == null) {
            return mapping.findForward("forward");
        }

        if(vwForm.getType() == 0) {
            for(Iterator ubIter = ubCol.iterator(); ubIter.hasNext(); ) {
                UserBean ub = (UserBean) ubIter.next();
                if(ub.getTeams() != null) {
                    ubIter.remove();
                }
            }

        } else if(vwForm.getType() == 1) {
            for(Iterator ubIter = ubCol.iterator(); ubIter.hasNext(); ) {
                UserBean user = (UserBean) ubIter.next();
                if(user.getTeams() == null) {
                    ubIter.remove();
                }
            }
        }

        if(vwForm.getKeyword() != null && ubCol != null) {
            for(Iterator ubIter = ubCol.iterator(); ubIter.hasNext(); ) {
                UserBean ub = (UserBean) ubIter.next();

                String firstAndLastName = ub.getFirstNames() + ub.getLastName();
                if(ub.getEmail().indexOf(vwForm.getKeyword()) == -1 &&
                   firstAndLastName.indexOf(vwForm.getKeyword()) == -1) {
                    ubIter.remove();
                }
            }
        }
        vwForm.setUsers(ubCol);
        return mapping.findForward("forward");
    }

    private Collection<UserBean> getUsers() {
        Collection<User> users = AmpUserUtil.getAllUsers();
        if(users != null) {
            List<User> sortedUser = new ArrayList(users);

            Collections.sort(sortedUser, new DbUtil.HelperUserNameComparator());

            Collection<UserBean> ubCol = new ArrayList();

            for(Iterator userIter = sortedUser.iterator(); userIter.hasNext(); ) {
                User user = (User) userIter.next();
                if(user != null) {
                    UserBean ub = new UserBean();
                    ub.setId(user.getId());
                    ub.setEmail(user.getEmail());
                    ub.setFirstNames(user.getFirstNames());
                    ub.setLastName(user.getLastName());
                    ub.setBan(user.isBanned());

                    Collection members = TeamMemberUtil.getTeamMembers(user.getEmail());
                    if(members != null) {
                        List<AmpTeam> teams = new ArrayList<AmpTeam>();
                        for(Iterator teamMemberIter = members.iterator(); teamMemberIter.hasNext(); ) {
                            AmpTeamMember teamMember = (AmpTeamMember) teamMemberIter.next();
                            if(teamMember != null && teamMember.getAmpTeam() != null) {
                                teams.add(teamMember.getAmpTeam());
                            }
                        }
                        if(teams != null && teams.size() > 0) {
                            Collections.sort(teams, new TeamUtil.HelperAmpTeamNameComparator());
                            ub.setTeams(teams);
                        }
                    }
                    ubCol.add(ub);
                }
            }
            return ubCol;
        } else {
            return null;
        }
    }

    public ViewAllUsers() {
    }
}
