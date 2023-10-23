package org.digijava.module.um.action;

import org.apache.struts.action.*;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.entity.trubudget.TruBudgetIntent;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.PasswordPolicyValidator;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.CountryBean;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.um.form.ViewEditUserForm;
import org.digijava.module.um.util.AmpUserUtil;
import org.digijava.module.um.util.DbUtil;
import org.digijava.module.um.util.UmUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

import static org.digijava.module.um.util.DbUtil.*;

public class ViewEditUser extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        ViewEditUserForm uForm = (ViewEditUserForm) form;
        User user = null;
        HttpSession session = request.getSession();
        String isAmpAdmin = (String) session.getAttribute("ampAdmin");
        ActionMessages errors = new ActionMessages();
        Site curSite = RequestUtils.getSite(request);
        UserLangPreferences langPref = null;
        Long userId = uForm.getId();

        if (userId != null) {
            user = UserUtils.getUser(userId);
        } else if (uForm.getEmail() != null && user==null) {
            user = UserUtils.getUserByEmailAddress(uForm.getEmail());
        }else{
            return mapping.findForward("forward");
        }
        
        if (!RequestUtils.isAdmin(response, session, request)) {
            return null;
        }

        try {
            langPref = UserUtils.getUserLangPreferences(user, curSite);
        } catch (DgException ex) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ex.getMessage()));
            saveErrors(request, errors);
        }

        Boolean isBanned = uForm.getBan();
        
        
        if (isBanned != null) {         
            if (isAmpAdmin.equalsIgnoreCase("yes")) {               
                if (isBanned) {
                    //see if the user we want to ban is admin
                    Site site = RequestUtils.retreiveSiteDomain(request).getSite();        
                    boolean siteAdmin = UserUtils.isAdmin(user, site);
                    if(siteAdmin){ // should be impossible to ban admin
                        errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.um.errorBanningAdmin"));
                    }else{
                        List ampTeamMembers = TeamMemberUtil.getAmpTeamMembersbyDgUserId(userId);
                        if ( ampTeamMembers != null && ampTeamMembers.size() == 0 ) {
                            user.setBanned(true);
                            DbUtil.updateUser(user);
                        }
                        if ( ampTeamMembers != null && ampTeamMembers.size() > 0 ) {
                            StringBuilder teamNames    = new StringBuilder();
                            for (Object ampTeamMember : ampTeamMembers) {
                                AmpTeamMember atm = (AmpTeamMember) ampTeamMember;
                                AmpTeam team = atm.getAmpTeam();
                                if (team != null && team.getName() != null) {
                                    if (teamNames.length() == 0)
                                        teamNames.append("'").append(team.getName()).append("'");
                                    else
                                        teamNames.append(", '").append(team.getName()).append("'");
                                }
                            }
                            errors.add("title",
                                    new ActionMessage("error.um.userIsInTeams", teamNames.toString()));
                        }
                        if ( ampTeamMembers == null ) {
                            errors.add("title",new ActionMessage("error.um.errorBanning"));
                        }
                    }
                }
                else {
                    user.setBanned(false);
                    DbUtil.updateUser(user);
                }
                /*user.setBanned(isBanned);
                if (isBanned) {
                    Site site = RequestUtils.getSite(request);
                    List teamMembersId = TeamMemberUtil.getTeamMemberbyUserId(userId);
                    if (teamMembersId != null && teamMembersId.size() != 0) {
                        Long[] memberToRemove = new Long[teamMembersId.size()];
                        teamMembersId.toArray(memberToRemove);
                        TeamMemberUtil.removeTeamMembers(memberToRemove, site.getId());
                    }

                }
                DbUtil.updateUser(user);*/
            } else {
                errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.um.banUserInvalidPermission"));
            }
            resetViewEditUserForm(uForm);
            saveErrors(request, errors);
            return mapping.findForward("saved");
        }
        if (uForm.getEvent() == null) {

            Locale navLang = RequestUtils.getNavigationLanguage(request);
            if (navLang != null) {
                Collection<CountryBean> countrieCol = org.digijava.module.aim.util.DbUtil.getTranlatedCountries(request);
                if (countrieCol != null) {
                    uForm.setCountries(countrieCol);
                }

                Collection userLangs = TrnUtil.getSortedUserLanguages(request);
                uForm.setLanguages(userLangs);
            }

            uForm.setRegions(DynLocationManagerUtil.getLocationsOfTypeAdmLevel1OfDefCountry());
            Collection<AmpOrgType> orgTypeCol = DbUtil.getAllOrgTypes();
            if (orgTypeCol != null) {
                uForm.setOrgTypes(orgTypeCol);
            }


            uForm.setMailingAddress(null);
            uForm.setFirstNames(null);
            uForm.setLastName(null);
            uForm.setName(null);
            uForm.setUrl(null);
            uForm.setNotificationEmailEnabled(false);
            uForm.setNotificationEmail(null);
            uForm.setSelectedCountryIso(null);
            uForm.setSelectedRegionId(null);
            uForm.setSelectedLanguageCode(null);
            uForm.setSelectedOrgName(null);

            uForm.setSelectedOrgGroupId(null);
            uForm.setSelectedOrgTypeId(null);
            uForm.setSelectedCountryIso(null);
            uForm.setAssignedOrgId(null);
            uForm.setAssignedOrgs(new TreeSet<AmpOrganisation>());
            uForm.setPledger(false);
            uForm.setPledgeSuperUser(false);
            uForm.setId(null);
            uForm.setEmail(null);
            uForm.setConfirmNewPassword(null);
            uForm.setNewPassword(null);
            uForm.setDisplaySuccessMessage(null);
            uForm.setAddWorkspace(false);
            uForm.setEmailerror(false);
            uForm.setExemptFromDataFreezing(false);
            uForm.setNationalCoordinator(false);
            List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");
            if (getSettingValue(settings,"isEnabled").equalsIgnoreCase("true")) {

                Set<String> intentNames = user.getTruBudgetIntents().stream().map(TruBudgetIntent::getTruBudgetIntentName).collect(Collectors.toSet());

                uForm.setTruBudgetPassword(user.getTruBudgetPassword()!=null?UmUtil.decrypt(user.getTruBudgetPassword(), user.getTruBudgetKeyGen()):"");

                Collection<TruBudgetIntent> intents = getTruBudgetIntents();
                intents.forEach(intent ->
                {
                    if (intentNames.contains(intent.getTruBudgetIntentName())) {
                        intent.setUserHas(true);
                    }
                });
                uForm.setTruBudgetIntents(intents);
                uForm.setTruBudgetEnabled(getSettingValue(settings, "isEnabled"));
            }


            if (user != null) {
                uForm.setMailingAddress(user.getAddress());
                AmpUserExtension userExt = AmpUserUtil.getAmpUserExtension(user);

                if (user.getCountry() != null) {
                    uForm.setSelectedCountryIso(user.getCountry().getIso());
                }
                
                if (user.getRegion() != null) {
                    uForm.setSelectedRegionId(user.getRegion().getId());
                }

                uForm.setId(user.getId());
                uForm.setEmail(user.getEmail());
                uForm.setFirstNames(user.getFirstNames());
                uForm.setLastName(user.getLastName());
                uForm.setName(user.getName());
                uForm.setUrl(user.getUrl());
                uForm.getAssignedOrgs().addAll(user.getAssignedOrgs());
                uForm.setPledger(user.getPledger());
                uForm.setPledgeSuperUser(user.getPledgeSuperUser());
                uForm.setBanReadOnly(user.isBanned());
                uForm.setExemptFromDataFreezing(user.getExemptFromDataFreezing());
                
                uForm.setNotificationEmailEnabled(user.isNotificationEmailEnabled());
                uForm.setNotificationEmail(user.getNotificationEmail());

                Locale language = null;
                if (langPref == null) {
                    language = user.getRegisterLanguage();
                } else {
                    language = langPref.getAlertsLanguage();
                }

                uForm.setSelectedLanguageCode(language.getCode());
                uForm.setSelectedOrgName(user.getOrganizationName());

                if (userExt!=null){
                    if (userExt.getOrgGroup()!=null){
                        uForm.setSelectedOrgGroupId(userExt.getOrgGroup().getAmpOrgGrpId());
                    }
                    if (userExt.getOrgType()!=null){
                        uForm.setSelectedOrgTypeId(userExt.getOrgType().getAmpOrgTypeId().toString());
                    }
                    if (userExt.getOrganization()!=null){
                        uForm.setSelectedOrgName(userExt.getOrganization().getName());
                        uForm.setSelectedOrgId(userExt.getOrganization().getAmpOrgId());
                    }

                }

//                if (user.getOrganizationName() != null &&
//                    user.getOrganizationName().length() != 0) {
//                    Collection<AmpOrganisation> orgCol = org.digijava.module.aim.util.DbUtil.getAllOrganisation();
//
//                    AmpOrganisation orgnisation = null;
//                    if (orgCol != null) {
//                        for (Iterator iter = orgCol.iterator(); iter.hasNext(); ) {
//                            orgnisation = (AmpOrganisation) iter.next();
//                            if (orgnisation.getName().equals(uForm.getSelectedOrgName())) {
//                                break;
//                            }
//                        }
//                    }
//
//                    Collection<AmpOrgGroup> orgGrpCol = DbUtil.getAllOrgGroup();
//                    AmpOrgGroup orgGroup = null;
//                    if (orgGrpCol != null && orgnisation != null) {
//                        for (Iterator orgGroupIter = orgGrpCol.iterator();
//                             orgGroupIter.hasNext(); ) {
//                            orgGroup = (AmpOrgGroup) orgGroupIter.next();
//                            if (orgGroup != null && orgnisation.getOrgGrpId() != null &&
//                                orgGroup.getAmpOrgGrpId().equals(orgnisation.getOrgGrpId().getAmpOrgGrpId())) {
//
//                                uForm.setSelectedOrgGroupId(orgGroup.getAmpOrgGrpId());
//                                if (orgGroup.getOrgType() != null) {
//                                    uForm.setSelectedOrgTypeId(orgGroup.getOrgType().getAmpOrgTypeId().toString());
//                                }
//                                break;
//                            }
//                        }
//                    }
//
                    uForm.setOrgTypes(DbUtil.getAllOrgTypes());
                    if (uForm.getSelectedOrgTypeId() != null) {
                        uForm.setOrgGroups(DbUtil.getOrgGroupByType(Long.valueOf(uForm.getSelectedOrgTypeId())));
                    }
                    uForm.setOrgs(DbUtil.getOrgByGroup(uForm.getSelectedOrgGroupId()));
                    //workspaces
                    if(uForm.getWorkspaces() == null){
                        uForm.setWorkspaces(TeamUtil.getAllTeams());
                    }
                    uForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());

                    uForm.setNationalCoordinator(user.hasNationalCoordinatorGroup());

//                }
            }
        } else {
            if (uForm.getEvent().equalsIgnoreCase("save")) {
                if (user != null) {
                    uForm.setEmailerror(false);
                      if (!DbUtil.EmailExist(uForm.getEmail(), user.getId())){
                            uForm.setEmailerror(true);
                            uForm.setEvent(null);
                            return mapping.findForward("forward");
                      }
                    
                    // TODO ideally, user, userLangPreferences, and userExtension should be saved in one transaction.
                    AmpUserExtension userExt=AmpUserUtil.getAmpUserExtension(user);
                    if (userExt==null){
                        userExt=new AmpUserExtension(new AmpUserExtensionPK(user));
                    }

                    if (userExt!=null){
                        AmpOrgType orgType=org.digijava.module.aim.util.DbUtil.getAmpOrgType(new Long(uForm.getSelectedOrgTypeId()));
                        userExt.setOrgType(orgType);
                        AmpOrgGroup orgGroup=org.digijava.module.aim.util.DbUtil.getAmpOrgGroup(uForm.getSelectedOrgGroupId());
                        userExt.setOrgGroup(orgGroup);
                        AmpOrganisation organ = org.digijava.module.aim.util.DbUtil.getOrganisation(uForm.getSelectedOrgId());
                        userExt.setOrganization(organ);
                        AmpUserUtil.saveAmpUserExtension(userExt);
                    }
                    List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");

                    if (getSettingValue(settings,"isEnabled").equalsIgnoreCase("true")) {

                        String[] intents = uForm.getSelectedTruBudgetIntents();
                        List<TruBudgetIntent> truBudgetIntents = new ArrayList<>();
                        if (intents != null) {
                            truBudgetIntents = getTruBudgetIntentsByName(intents);
                        }
                        // TODO: 8/28/23 add for trubudget request

//                    user.getTruBudgetIntents().addAll(new HashSet<>(truBudgetIntents));
                        user.setInitialTruBudgetIntents(new HashSet<>(user.getTruBudgetIntents()));
                        user.setTruBudgetIntents(new HashSet<>(truBudgetIntents));
                        String keyGen = UmUtil.generateAESKey(128);
                        user.setTruBudgetKeyGen(keyGen);

                        String encryptedTruPassword = UmUtil.encrypt(uForm.getTruBudgetPassword()!=null? uForm.getTruBudgetPassword() : "amptrubudget", keyGen);
                        user.setTruBudgetPassword(encryptedTruPassword);
                    }

                    user.setCountry(org.digijava.module.aim.util.DbUtil.getDgCountry(uForm.getSelectedCountryIso()));
                    if(uForm.getSelectedRegionId()==-1){
                        user.setRegion(null);
                    }else
                        user.setRegion(LocationUtil.getAmpCategoryValueLocationById(uForm.getSelectedRegionId()));
                    user.setEmail(uForm.getEmail());
                    user.setFirstNames(uForm.getFirstNames());
                    user.setLastName(uForm.getLastName());
                    user.setAddress(uForm.getMailingAddress());
                    user.setOrganizationName(uForm.getSelectedOrgName());
                    
                    user.getAssignedOrgs().clear();
                    user.getAssignedOrgs().addAll(uForm.getAssignedOrgs());
                    
                    user.setUrl(uForm.getUrl());

                    SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.CURRENT_SITE);
                    UserLangPreferences userLangPreferences = new UserLangPreferences(user, DgUtil.getRootSite(siteDomain.getSite()));

                    Locale language = new Locale();
                    language.setCode(uForm.getSelectedLanguageCode());

                    userLangPreferences.setAlertsLanguage(language);
                    userLangPreferences.setNavigationLanguage(RequestUtils.getNavigationLanguage(request));

                    user.setUserLangPreferences(userLangPreferences);
                    user.setPledger(uForm.getPledger());
                    user.setPledgeSuperUser(uForm.getPledger() && uForm.getPledgeSuperUser());
                    user.setExemptFromDataFreezing(uForm.getExemptFromDataFreezing());
                    
                    user.setNotificationEmailEnabled(uForm.getNotificationEmailEnabled());
                    
                    if (uForm.getNotificationEmailEnabled()) {
                        user.setNotificationEmail(uForm.getNotificationEmail());
                    } else {
                        user.setNotificationEmail(null);
                    }
                    

                    if (uForm.getNationalCoordinator()) {
                        user.getGroups().add(org.digijava.module.admin.util.DbUtil.getGroupByKey(Group.NATIONAL_COORDINATORS));
                    } else {
                        user.getGroups().remove(org.digijava.module.admin.util.DbUtil.getGroupByKey(Group.NATIONAL_COORDINATORS));
                    }
                    DbUtil.updateUser(user);
                    //assign workspace place
                    if(uForm.isAddWorkspace()){
                        uForm.setAssignedWorkspaces(TeamMemberUtil.getAllAmpTeamMembersByUser(user));
                        return mapping.findForward("assignWorkspace");
                    }
                    
                    uForm.setEvent(null);
                    resetViewEditUserForm(uForm);
                    return mapping.findForward("saved");
                }

            }else if(uForm.getEvent().equalsIgnoreCase("assignWorkspaceToUser")){
                uForm.setEvent(null);
                AmpTeam ampTeam = TeamUtil.getAmpTeam(uForm.getTeamId());
                //User user = org.digijava.module.aim.util.DbUtil.getUser(uForm.getEmail());
                
                if (uForm.getRole() == null) {
                    uForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.addTeamMember.roleNotSelected"));
                    saveErrors(request, errors);
                    return mapping.findForward("forward");          
                }
                
                /* check if user have selected role as Team Lead when a team lead 
                 * already exist for the team */
                if (ampTeam.getTeamLead() != null &&
                        ampTeam.getTeamLead().getAmpMemberRole().getAmpTeamMemRoleId().equals(uForm.getRole())) {
                    uForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
                    errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.aim.addTeamMember.teamLeadAlreadyExist"));
                    saveErrors(request, errors);
                    return mapping.findForward("assignWorkspace");          
                }
                
                /* check if user is already part of the selected team */
                if (TeamUtil.isMemberExisting(uForm.getTeamId(),uForm.getEmail())) {
                    uForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
                    errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(
                            "error.aim.addTeamMember.teamMemberAlreadyExist"));
                    saveErrors(request, errors);
                    //logger.debug("Member is already existing");
                    return mapping.findForward("assignWorkspace");          
                }
                
                   
                addWorkSpace(UmUtil.assignWorkspaceToUser(request, uForm.getRole(), user, ampTeam), uForm);
                return mapping.findForward("assignWorkspace");
                
            } else if (uForm.getEvent().equalsIgnoreCase("deleteWS")){
                uForm.setEvent(null);
                Long wId = new Long(request.getParameter("wId"));
                TeamMemberUtil.removeTeamMembers(new Long[]{wId});
                Collection asWS = uForm.getAssignedWorkspaces();
                for (Object asW : asWS) {
                    AmpTeamMember newMember = (AmpTeamMember) asW;
                    if (newMember.getAmpTeamMemId().compareTo(wId) == 0) {
                        asWS.remove(newMember);
                        break;
                    }
                }
                return mapping.findForward("assignWorkspace");
            }else {
                if (uForm.getEvent().equalsIgnoreCase("changePassword")) {
                    if (!PasswordPolicyValidator.isValid(uForm.getNewPassword(), uForm.getEmail())) {
                        errors.add(ActionMessages.GLOBAL_MESSAGE,
                                new ActionMessage("error.strong.validation"));
                        saveErrors(request, errors);
                        request.setAttribute(PasswordPolicyValidator.SHOW_PASSWORD_POLICY_RULES, true);
                        return mapping.findForward("forward");
                    }

                    String newPassword = uForm.getNewPassword();
                    String confirmNewPassword = uForm.getConfirmNewPassword();
                    if (confirmNewPassword == null || newPassword == null ||
                        newPassword.trim().length() == 0 ||
                        confirmNewPassword.trim().length() == 0) {
                        errors.add(ActionMessages.GLOBAL_MESSAGE,
                                   new ActionMessage("error.update.blankFields"));
                        saveErrors(request, errors);
                    } else {
                        if (!newPassword.equals(confirmNewPassword)) {
                            errors.add(ActionMessages.GLOBAL_MESSAGE,
                                       new ActionMessage("error.update.noPasswordMatch"));
                            saveErrors(request, errors);

                        } else {
                            DbUtil.updatePassword(user.getEmail(), newPassword);
                            uForm.setDisplaySuccessMessage(true);
                        }
                    }
                }

                else if (uForm.getEvent().equalsIgnoreCase("typeSelected")) {
                    uForm.setOrgGroups(DbUtil.getOrgGroupByType(Long.valueOf(uForm.getSelectedOrgTypeId())));
                    if (uForm.getOrgs() != null && uForm.getOrgs().size() != 0) {
                        uForm.getOrgs().clear();
                    }
                } else if (uForm.getEvent().equalsIgnoreCase("groupSelected")) {
                    uForm.setOrgs(DbUtil.getOrgByGroup(uForm.getSelectedOrgGroupId()));
                } else if(uForm.getEvent().equalsIgnoreCase("addOrg")){
                             uForm.setEvent(null);
                             if(uForm.getAssignedOrgId()!=-1) {
                                 AmpOrganisation organisation = org.digijava.module.aim.util.DbUtil.getOrganisation(uForm.getAssignedOrgId());
                                 uForm.getAssignedOrgs().add(organisation);
                             }
                        } 
                        else if(uForm.getEvent().equalsIgnoreCase("delOrgs")){
                                uForm.setEvent(null);
                                for(int i=0;i<uForm.getSelAssignedOrgs().length;i++) {
                                    AmpOrganisation organisation = org.digijava.module.aim.util.DbUtil.getOrganisation(uForm.getSelAssignedOrgs()[i]);
                                    uForm.getAssignedOrgs().remove(organisation);
                                }
                            }
            }
        }
        resetViewEditUserForm(uForm);
        return mapping.findForward("forward");
    }

    

    public ViewEditUser() {
    }

    public void resetViewEditUserForm(ViewEditUserForm uForm) {
        if (uForm != null) {
            uForm.setBan(null);
            uForm.setEvent(null);
            uForm.setNewPassword(null);
            uForm.setConfirmNewPassword(null);
            uForm.setEmailerror(false);
        }
    }
    
    private void addWorkSpace(AmpTeamMember newMember, ViewEditUserForm upMemForm) {
        Collection assignedWS = upMemForm.getAssignedWorkspaces();
        if(assignedWS==null){
            ArrayList assWS = new ArrayList();
            upMemForm.setAssignedWorkspaces(assWS);
        }
        upMemForm.getAssignedWorkspaces().add(newMember);
        upMemForm.setTeamId(-1L);
        upMemForm.setRole(-1L);
    }
}
