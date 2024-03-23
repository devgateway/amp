package org.digijava.module.um.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.UserBean;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DbUtil.UserManagerSorting;
import org.digijava.module.aim.util.RepairDbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.calendar.util.AmpUtil;
import org.digijava.module.um.form.ViewAllUsersForm;
import org.digijava.module.um.util.AmpUserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
    
    public class ViewAllUsers
        extends Action {
        public ActionForward execute(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
            
            ViewAllUsersForm vwForm = (ViewAllUsersForm) form;
            
            if(request.getParameter("reset")!=null && request.getParameter("reset").equals("true")){
                vwForm.reset(mapping, request);         
            }
            
            HttpSession session = request.getSession();
            if (!RequestUtils.isAdmin(response, session, request)) {
                return null;
            }
            
            RepairDbUtil.repairBannedUsersAreStillInATeam();
            
            if ( request.getParameter("showBanned")!=null ){
                if(request.getParameter("showBanned").equals("true") ) {
                    vwForm.setShowBanned(true);
                    vwForm.setType(-1);
                }
                else{
                    vwForm.setShowBanned(false);    
                }
            }
            
            vwForm.setPagesToShow(10);
            vwForm.setReset("false");
            vwForm.setNumResults(vwForm.getTempNumResults());
            Collection<UserBean> ubCol = getUsers(vwForm, request);
            vwForm.setSelectedNoLetter(true);
            String alpha=vwForm.getCurrentAlpha();
    
            if(ubCol == null) {
                return mapping.findForward("forward");
            }
            
            if (!RequestUtils.isAdmin(response, session, request)) {
                return null;
            }
    
            if(vwForm.getType() == 0) {
                for(Iterator ubIter = ubCol.iterator(); ubIter.hasNext(); ) {
                    UserBean ub = (UserBean) ubIter.next();
                    if(ub.getTeamMembers() != null && ! ub.getTeamMembers().isEmpty()) {
                        ubIter.remove();
                    }
                }
    
            } else if(vwForm.getType() == 1) {
                for(Iterator ubIter = ubCol.iterator(); ubIter.hasNext(); ) {
                    UserBean user = (UserBean) ubIter.next();
                    if(user.getTeamMembers() == null || user.getTeamMembers().isEmpty()) {
                        ubIter.remove();
                    }
                }
            }
    
            if(vwForm.getKeyword() != null && vwForm.getKeyword().length() >0 && ubCol != null) {
                for(Iterator ubIter = ubCol.iterator(); ubIter.hasNext(); ) {
                    UserBean ub = (UserBean) ubIter.next();
    
                    String firstAndLastName = ub.getFirstNames() + ub.getLastName();
                    if(ub.getEmail().toLowerCase().indexOf(vwForm.getKeyword().toLowerCase()) == -1 &&
                       firstAndLastName.toLowerCase().indexOf(vwForm.getKeyword().toLowerCase()) == -1 ) {
                        
                        ubIter.remove();
                    }
                }
            }
            
            
            if (ubCol != null && ubCol.size() > 0) {
                if(alpha == null || alpha.trim().length() == 0){
                  if (vwForm.getCurrentAlpha() != null) {
                        vwForm.setCurrentAlpha(null);
                      } 
                }else {
                  vwForm.setCurrentAlpha(alpha);
                }            
    
                collectAlphaArray(vwForm, ubCol);
              }
              else {
                vwForm.setAlphaPages(null);
              }
            
            vwForm.setNumResults(vwForm.getTempNumResults());
            
            if (vwForm.getNumResults() == 0) {
                  vwForm.setNumResults(10);
            }
            int stIndex = 1;
            int edIndex = vwForm.getNumResults();
    
            //If ALL was selected in pagination dropdown
            if (edIndex < 0) {
              edIndex = ubCol.size();
            }
    
            Vector vect = new Vector();
            int numPages;
    
            if (alpha == null || alpha.trim().length() == 0 || alpha.equals("viewAll")) {
              if (edIndex > ubCol.size()) {
                edIndex = ubCol.size();
              }
              vect.addAll(ubCol);
              numPages = ubCol.size() / vwForm.getNumResults();
              numPages += (ubCol.size() % vwForm.getNumResults() != 0) ? 1 : 0;
            }
            else {
              if (edIndex > vwForm.getAlphaUsers().size()) {
                edIndex = vwForm.getAlphaUsers().size();
              }
              vect.addAll(vwForm.getAlphaUsers());
              numPages = vwForm.getAlphaUsers().size() / vwForm.getNumResults();
              numPages += (vwForm.getAlphaUsers().size() % vwForm.getNumResults() != 0) ? 1 : 0;
            }
    
            Collection tempCol = new ArrayList();
            for (int i = (stIndex - 1); i < edIndex; i++) {
              tempCol.add(vect.get(i));
            }
    
            Collection pages = null;
    
            if (numPages > 1) {
              pages = new ArrayList();
              for (int i = 0; i < numPages; i++) {
                Integer pageNum = new Integer(i + 1);
                pages.add(pageNum);
              }
            }     
            
           
            
            vwForm.setUsers(ubCol);
            vwForm.setPagedUsers(tempCol);
            vwForm.setPages(pages);
            vwForm.setCurrentPage(new Integer(1));
              
            
            return mapping.findForward("forward");
        }
    
        private Collection<UserBean> getUsers(ViewAllUsersForm vwForm,HttpServletRequest request) {
            
            vwForm.setAlphaUsers(new ArrayList<UserBean>());
            Collection<User> users = null;
             String alpha = request.getParameter("currentAlpha"); //vwForm.getCurrentAlpha();
                if (alpha == null || alpha.trim().length() == 0 || alpha.equals("viewAll")) {
                    users = AmpUserUtil.getAllUsers(vwForm.getShowBanned());
                    vwForm.setSelectedNoLetter(true);
                } else if (alpha != null && !alpha.equals("viewAll")) {
                    users = new ArrayList<User>();
                    for(User us:AmpUserUtil.getAllUsers(vwForm.getShowBanned())) {
                        if (us.getFirstNames().toUpperCase().startsWith(alpha)) {
                            users.add(us);
                        }
                    }
                    vwForm.setSelectedNoLetter(false);
                }
            
                if (users != null) {
                    List<User> sortedUser = new ArrayList<User>(users);
                    String sortBy = vwForm.getSortBy();
                    if (sortBy == null) {
                        sortBy = "nameAscending";
                        vwForm.setSortBy(sortBy);
                    }               
                
                    Collections.sort(sortedUser, DbUtil.sortUsers(UserManagerSorting.valueOf(sortBy.toUpperCase())));
              
               
    
                List<UserBean> userBeans = buildUserBeans(sortedUser);
                
                if (alpha != null && !alpha.equals("viewAll")) {
                    vwForm.setAlphaUsers(userBeans);
                }
                            
                return userBeans;
            } else {
                return null;
            }
        }
    
        /**
         * maps each User instance of the input to a UserBean instance
         * @param sortedUsers
         * @return
         */
        protected List<UserBean> buildUserBeans(Collection<User> sortedUsers) {
            Set<Long> userIds = new HashSet<>();
            for (User user:sortedUsers)
                userIds.add(user.getId());
            
            Map<Long, List<AmpTeamMember>> membersByUserId = TeamMemberUtil.getTeamMembersByUserId(userIds);
            List<UserBean> res = new ArrayList<>();
            
            for(User user:sortedUsers) {
                if (user != null) {
                    UserBean ub = new UserBean();
                    ub.setId(user.getId());
                    ub.setEmail(user.getEmail());
                    ub.setFirstNames(user.getFirstNames());
                    ub.setLastName(user.getLastName());
                    ub.setBan(user.isBanned());

                    List<AmpTeamMember> members = membersByUserId.get(user.getId());
                    ub.setTeamMembers(members);
                    res.add(ub);
                }
            }
            return res;
        }
        
        public ViewAllUsers() {
        }

        private void collectAlphaArray(ViewAllUsersForm eaForm, Collection<UserBean> ubCol) {
            SortedSet<String> chars = new TreeSet<String>(AmpUtil.CharUnicodeComparator);
            SortedSet<String> digits = new TreeSet<String>(AmpUtil.CharUnicodeComparator);
            for (UserBean user : ubCol) {
                if (user.getFirstNames() != null && user.getFirstNames().length() > 0) {
                    Character firstLetter = user.getFirstNames().toUpperCase().charAt(0);
                    if (Character.isLetter(firstLetter)) {
                        chars.add(String.valueOf(firstLetter));
                    } else if ( Character.isDigit(firstLetter)) {
                        digits.add(String.valueOf(firstLetter));
                    }
                }
            }
            eaForm.setAlphaPages(chars.toArray(new String[0]));
            eaForm.setDigitPages(digits.toArray(new String[0]));
        }


}
