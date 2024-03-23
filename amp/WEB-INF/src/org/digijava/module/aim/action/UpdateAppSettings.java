package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.UpdateAppSettingsForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.digijava.module.contentrepository.helper.CrConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

public class UpdateAppSettings extends Action {

    private static Logger logger = Logger.getLogger(UpdateAppSettings.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws java.lang.Exception {

        UpdateAppSettingsForm uForm = (UpdateAppSettingsForm) form;
        AmpCaching.getInstance().applicationSettingsRetrieved = false; //invalidate app settings cache
        
        String shareResAction=request.getParameter("shareResAction");
        
        this.populatePossibleValsAddTR(uForm);
        
        this.populatePublishResourcesPossibleVals(uForm);
        
        this.populateShareResAmongWorkspacesPossibleVals(uForm);
        
        logger.debug("In updtate app settings");
        HttpSession session = request.getSession();

        if (session.getAttribute("currentMember") == null) {
            return mapping.findForward("index");
        }

        TeamMember tm = (TeamMember) session.getAttribute("currentMember");

        if (request.getParameter("updated") != null
                && request.getParameter("updated").equals("true")) {
            uForm.setUpdated(true);
        } else {
            uForm.setUpdated(false);
        }

        if (request.getParameter("errors") != null  && request.getParameter("errors").equals("true")) {
            uForm.setErrors(true);
        } else {
            uForm.setErrors(false);
        }
        
        if (uForm.getType() == null || uForm.getType().trim().equals("") || (shareResAction!=null && shareResAction.equalsIgnoreCase("getOptions"))) {
            String path = mapping.getPath();
            logger.debug("path = " + path);
            AmpApplicationSettings ampAppSettings = null;
            boolean loadValues = false;
            if (uForm.getType() == null || uForm.getType().trim().equals("")) {
                loadValues = true;
            }
            if (path != null
                    && (path.trim().equals("/aim/defaultSettings") || path
                            .trim().equals("/defaultSettings"))) {
                if (tm.getTeamHead() == false) {
                    return mapping.findForward("viewMyDesktop");
                }
                uForm.setType("default");
                uForm.setTeamName(tm.getTeamName());
                ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
            } else if (path != null
                    && (path.trim().equals("/aim/customizeSettings") || path
                            .trim().equals("/customizeSettings"))) {
                throw new RuntimeException("Unsupported: userSpecific settings");
            }

            if (ampAppSettings != null && loadValues) {
                uForm.setAppSettingsId(ampAppSettings.getAmpAppSettingsId());
                uForm.setDefRecsPerPage(ampAppSettings.getDefaultRecordsPerPage());
                uForm.setNumberOfPagesToDisplay(String.valueOf(ampAppSettings.getNumberOfPagesToDisplay()));

                Integer reportsPerPage = ampAppSettings.getDefaultReportsPerPage();
                if (reportsPerPage == null) {
                    reportsPerPage = 0;
                }
                Integer reportStartYear = ampAppSettings.getReportStartYear();
                if (reportStartYear == null) {
                    reportStartYear = 0;
                }
                Integer reportEndYear = ampAppSettings.getReportEndYear();
                if (reportEndYear == null) {
                    reportEndYear = 0;
                }
                if(shareResAction==null){
                    uForm.setAllowAddTeamRes( ampAppSettings.getAllowAddTeamRes() );
                    this.populateShareResAmongWorkspacesPossibleVals(uForm);
                }
                uForm.setAllowShareAccrossWRK(ampAppSettings.getAllowShareTeamRes());
                uForm.setAllowPublishingResources(ampAppSettings.getAllowPublishingResources());
                
                uForm.setDefReportsPerPage(reportsPerPage);
                uForm.setReportStartYear(reportStartYear);
                uForm.setReportEndYear(reportEndYear);
                uForm.setLanguage(ampAppSettings.getLanguage());
                uForm.setValidation(ampAppSettings.getValidation());
                uForm.setShowAllCountries(ampAppSettings.getShowAllCountries());

                uForm.setCurrencyId(EndpointUtils.getDefaultCurrencyId(ampAppSettings));

                if(ampAppSettings.getFiscalCalendar()!=null){
                uForm.setFisCalendarId(ampAppSettings.getFiscalCalendar().getAmpFiscalCalId());
                }

                if (ampAppSettings.getDefaultTeamReport() != null){
                    uForm.setDefaultReportForTeamId(ampAppSettings.getDefaultTeamReport().getAmpReportId());
                }else{
                    uForm.setDefaultReportForTeamId(new Long(0));
                }
                    
            }
            //Set workspaceType added by Armen 18/01/12
            Long currentTeamId=tm.getTeamId();
            uForm.setWorkspaceType(TeamUtil.getWorkspace(currentTeamId).getWorkspaceType());
            
            //get team members
            
            List<TeamMember> members =TeamMemberUtil.getAllMembersExcludingTL(currentTeamId);
            uForm.setTeamMembers(members);
            if(uForm.getAllowPublishingResources()!=null && uForm.getAllowPublishingResources().equals(CrConstants.PUBLISHING_RESOURCES_ALLOWED_SPECIFIC_USERS)){
                //Long currentTeamId=tm.getTeamId();
                //List<TeamMember> members =TeamMemberUtil.getAllMembersExcludingTL(currentTeamId);
                //uForm.setTeamMembers(members);
                if(members!=null){
                    List<Long> selMembersIds= null; 
                    for (TeamMember teamMember : members) {
                        if(teamMember.getPublishDocuments()!=null && teamMember.getPublishDocuments()){
                            if(selMembersIds==null){
                                selMembersIds=new ArrayList<Long>();
                            }
                            selMembersIds.add(teamMember.getMemberId());
                        }
                    }
                    if(selMembersIds!=null && selMembersIds.size()>0){
                        uForm.setSelTeamMembers(selMembersIds.toArray(new Long[selMembersIds.size()] ));
                    }
                }
            }
            
            /* Select only the reports that are shown as tabs */
            List<AmpReports> reports = TeamUtil.getAllTeamReports(tm.getTeamId(), null,null, null, true, tm.getMemberId(),null,null);
            if (reports != null) {
                Iterator iterator = reports.iterator();
                while (iterator.hasNext()) {
                    AmpReports ampreport = (AmpReports) iterator.next();
                    if (ampreport.getDrilldownTab() == null
                            || !ampreport.getDrilldownTab().booleanValue()) {
                        iterator.remove();
                    }
                }
            }
            
            Collections.sort(reports, 
                    new Comparator<AmpReports> () {
                        public int compare(AmpReports o1, AmpReports o2) {
                            return o1.getName().compareTo( o2.getName() );
                        }
                    }
                );
            
            uForm.setReports(reports);
            uForm.setCurrencies(CurrencyUtil.getUsableCurrencies());
            uForm.setFisCalendars(DbUtil.getAllFisCalenders());

            // set Navigation languages
            Set languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));

            HashMap translations = new HashMap();
            Iterator iterator = TrnUtil.getLanguages(RequestUtils.getNavigationLanguage(request).getCode()).iterator();
            while (iterator.hasNext()) {
                TrnLocale item = (TrnLocale) iterator.next();
                translations.put(item.getCode(), item);
            }
            // sort languages
            List sortedLanguages = new ArrayList();
            iterator = languages.iterator();
            while (iterator.hasNext()) {
                Locale item = (Locale) iterator.next();
                sortedLanguages.add(translations.get(item.getCode()));
            }
            Collections.sort(sortedLanguages, TrnUtil.localeNameComparator);
            uForm.setLanguages(sortedLanguages);

            logger.info("TYpe =" + uForm.getType());
            return mapping.findForward("showDefaultSettings");
        } else {
            logger.debug("In saving");
            SiteDomain currentDomain = RequestUtils.getSiteDomain(request);
            
            String context = SiteUtils.getSiteURL(currentDomain, request
                    .getScheme(), request.getServerPort(), request
                    .getContextPath());
            if(uForm.getDefRecsPerPage() == null || uForm.getDefRecsPerPage() < 2)
                {
                    ActionMessages errors = new ActionMessages();
                    errors.add("title", new ActionMessage("error.aim.addActivity.wrongNrActsPerPage", TranslatorWorker.translateText("Please enter the title")));
                    if (errors.size() > 0)
                        saveErrors(request, errors);
                    
                    if (uForm.getType().equals("default")) {
                        uForm.setType(null);
                        String url = context + "/translation/switchLanguage.do?code="
                                + uForm.getLanguage() + "&rfr=" + context
                                + "/aim/defaultSettings.do~errors=true~updated="
                                + uForm.getUpdated();
                        response.sendRedirect(url);
                        logger.debug("redirecting " + url + " ....");
                        uForm.setErrors(true);
                        return null;
                    }
                }
            AmpApplicationSettings ampAppSettings = null;
            if ("save".equals(uForm.getSave())) {
                ampAppSettings = new AmpApplicationSettings();
                ampAppSettings.setAmpAppSettingsId(uForm.getAppSettingsId());
                ampAppSettings.setDefaultRecordsPerPage(new Integer(uForm.getDefRecsPerPage()));
                
                Integer numberOfPagesToDisplay = null;
                try {
                    numberOfPagesToDisplay = Integer.parseInt(uForm.getNumberOfPagesToDisplay());
                } catch (NumberFormatException e) {
                    
                }
                ampAppSettings.setNumberOfPagesToDisplay(numberOfPagesToDisplay);
                
                ampAppSettings.setReportStartYear((new Integer(uForm.getReportStartYear())));
                ampAppSettings.setReportEndYear((new Integer(uForm.getReportEndYear())));
                ampAppSettings.setDefaultReportsPerPage(uForm.getDefReportsPerPage());
                ampAppSettings.setCurrency(CurrencyUtil.getAmpcurrency(uForm.getCurrencyId()));
                ampAppSettings.setFiscalCalendar(DbUtil.getAmpFiscalCalendar(uForm.getFisCalendarId()));
                ampAppSettings.setLanguage(uForm.getLanguage());
                ampAppSettings.setValidation(uForm.getValidation());
                ampAppSettings.setTeam(TeamUtil.getAmpTeam(tm.getTeamId()));
                ampAppSettings.setAllowAddTeamRes( uForm.getAllowAddTeamRes() );
                ampAppSettings.setAllowShareTeamRes(uForm.getAllowShareAccrossWRK());
                ampAppSettings.setAllowPublishingResources(uForm.getAllowPublishingResources());
                ampAppSettings.setShowAllCountries(uForm.getShowAllCountries());
                AmpReports ampReport = DbUtil.getAmpReports(uForm.getDefaultReportForTeamId());
                ampAppSettings.setDefaultTeamReport(ampReport);

                session.setAttribute(Constants.DEFAULT_TEAM_REPORT, ampAppSettings.getDefaultTeamReport());
                session.setAttribute(Constants.CURRENT_TAB_REPORT, ampAppSettings.getDefaultTeamReport());
            
                try {
                    DbUtil.update(ampAppSettings);
                    //update team members
                    if(uForm.getResetTeamMembers()!=null && uForm.getResetTeamMembers()){
                        uForm.setSelTeamMembers(null);
                    }
                    if(uForm.getAllowPublishingResources().equals(CrConstants.PUBLISHING_RESOURCES_ALLOWED_ONLY_TL)){//allowed only to WM
                        //remove rights to other tms
                        TeamMemberUtil.removeTeamMembersResourcePublishingRights(tm.getTeamId(),null);
                        //give permissions to TL
                        AmpTeamMember teamHead= TeamMemberUtil.getTeamHead(tm.getTeamId());
                        if(teamHead.getPublishDocPermission()==null){
                            teamHead.setPublishDocPermission(true);
                            DbUtil.saveOrUpdateObject(teamHead);
                        }
                    }else if (uForm.getAllowPublishingResources().equals(CrConstants.PUBLISHING_RESOURCES_ALLOWED_SPECIFIC_USERS)){//allowed specific users
                        if(uForm.getSelTeamMembers()!=null && uForm.getSelTeamMembers().length!=0){                         
                            List<Long> selTMs=Arrays.asList(uForm.getSelTeamMembers());
                            //remove rights to other tms
                            TeamMemberUtil.removeTeamMembersResourcePublishingRights(tm.getTeamId(),selTMs);
                            //grant publishing rights to selected tms
                            TeamMemberUtil.grantMembersResourcePublishingRights(tm.getTeamId(), selTMs);
                        }else{
                            TeamMemberUtil.removeTeamMembersResourcePublishingRights(tm.getTeamId(),null); //in case every user was unselected
                        }
                    }else if (uForm.getAllowPublishingResources().equals(CrConstants.PUBLISHING_RESOURCES_ALLOWED_TM)){//allowed to all users
                        //grant publishing rights to everyone
                        TeamMemberUtil.grantMembersResourcePublishingRights(tm.getTeamId(), null);
                    }                   
                    
                    uForm.setUpdated(true);
                } catch (Exception e) {
                    uForm.setUpdated(false);
                }
            } else if (uForm.getRestore() != null) {
                throw new RuntimeException("unimplemented functionality: restoring workspace settings");
            }
            AmpApplicationSettings tempSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
            if(tempSettings!=null){
                ApplicationSettings applicationSettings = getReloadedAppSettings(tempSettings);
                tm.setAppSettings(applicationSettings);
            }           
            if (session.getAttribute(Constants.CURRENT_MEMBER) != null) {
                session.removeAttribute(Constants.CURRENT_MEMBER);
                session.setAttribute(Constants.CURRENT_MEMBER, tm);
            }
            session.setAttribute(Constants.DESKTOP_SETTINGS_CHANGED, new Boolean(true));
            //
            uForm.setUpdateFlag(false);
            //
            //SiteDomain 
            currentDomain = RequestUtils.getSiteDomain(request);

            //String 
            context = SiteUtils.getSiteURL(currentDomain, request
                    .getScheme(), request.getServerPort(), request
                    .getContextPath());
            if (uForm.getType().equals("default")) {
                uForm.setType(null);
                String url = context + "/translation/switchLanguage.do?code="
                        + ampAppSettings.getLanguage() + "&rfr=" + context
                        + "/aim/defaultSettings.do~updated="
                        + uForm.getUpdated();
                response.sendRedirect(url);
                logger.debug("redirecting " + url + " ....");
                return null;
            } else 
            {
                return mapping.findForward("index");
            }
        }
    }

    public ApplicationSettings getReloadedAppSettings(AmpApplicationSettings ampAppSettings) {
        ApplicationSettings appSettings = new ApplicationSettings(ampAppSettings);
        return appSettings;
    }

    private void populatePossibleValsAddTR (UpdateAppSettingsForm uForm) {
        if (uForm.getPossibleValsAddTR() == null || uForm.getPossibleValsAddTR().size() == 0 ) {
            KeyValue elem1  = new KeyValue(CrConstants.TEAM_RESOURCES_ADD_ONLY_WORKSP_MANAGER.toString(), "Managed by Workspace Manager");
            KeyValue elem2  = new KeyValue(CrConstants.TEAM_RESOURCES_ADD_ALLOWED_WORKSP_MEMBER.toString(), "Workspace Members Allowed to Add New Resources");
            KeyValue elem3  = new KeyValue(CrConstants.TEAM_RESOURCES_VERSIONING_ALLOWED_WORKSP_MEMBER.toString(), "Workspace Members Allowed to Add New Resources and Versions");
            
            uForm.setPossibleValsAddTR(new ArrayList<KeyValue>() );
            uForm.getPossibleValsAddTR().add(elem1);
            uForm.getPossibleValsAddTR().add(elem2);
            uForm.getPossibleValsAddTR().add(elem3);
        }
    }
    
    private void populateShareResAmongWorkspacesPossibleVals (UpdateAppSettingsForm uForm) {
        Integer selectedTeamResourceRight=uForm.getAllowAddTeamRes();
        uForm.setShareResAmongWorkspacesPossibleVals(new ArrayList<KeyValue>() );
            
        KeyValue elem1  = new KeyValue(CrConstants.SHARE_AMONG_WRKSPACES_ALLOWED_WM.toString(), "Managed by Workspace Manager");
        uForm.getShareResAmongWorkspacesPossibleVals().add(elem1);
            
        if(selectedTeamResourceRight!=null && selectedTeamResourceRight.equals(CrConstants.TEAM_RESOURCES_VERSIONING_ALLOWED_WORKSP_MEMBER)){
            KeyValue elem2  = new KeyValue(CrConstants.SHARE_AMONG_WRKSPACES_ALLOWED_TM.toString(), "Workspace Members Allowed to Share Resources Across Workspaces");
            uForm.getShareResAmongWorkspacesPossibleVals().add(elem2);
        }
    }
    
    private void populatePublishResourcesPossibleVals(UpdateAppSettingsForm uForm){
        if(uForm.getPublishResourcesPossibleVals()==null || uForm.getPublishResourcesPossibleVals().size() ==0){
            KeyValue elem1  = new KeyValue(CrConstants.PUBLISHING_RESOURCES_ALLOWED_ONLY_TL.toString(), "Managed by Workspace Manager");
            KeyValue elem2  = new KeyValue(CrConstants.PUBLISHING_RESOURCES_ALLOWED_TM.toString(), "All Team Members are allowed to publish documents");
            KeyValue elem3  = new KeyValue(CrConstants.PUBLISHING_RESOURCES_ALLOWED_SPECIFIC_USERS.toString(), "Only selected members are allowed to publish documents");
            
            uForm.setPublishResourcesPossibleVals(new ArrayList<KeyValue>() );
            uForm.getPublishResourcesPossibleVals().add(elem1);
            uForm.getPublishResourcesPossibleVals().add(elem2);
            uForm.getPublishResourcesPossibleVals().add(elem3);
        }
    }
}
