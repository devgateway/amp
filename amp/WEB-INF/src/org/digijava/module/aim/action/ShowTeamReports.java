package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.form.ReportsForm.ReportSortBy;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.Collator;
import java.util.*;

public class ShowTeamReports extends Action {

    private static Logger logger = Logger.getLogger(ShowTeamReports.class);
    private final static int FIRST_PAGE = 1;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {

        String forwardName  = "forward";
        
        List dbReturnSet = null;
        HttpSession session = request.getSession();
        
        boolean appSettingSet = false;

        ReportsForm rf = (ReportsForm) form;
        String action=rf.getAction();
        if (rf.isReset()
                || (action != null && action.equalsIgnoreCase("clear"))) {
            rf.setKeyword(null);
            rf.setAction(null);
            rf.setSortBy(ReportSortBy.NONE.getSortBy());
            rf.setCurrentPage(0);
            rf.setReset(false);
            rf.setPage(0);
            rf.setSelectedReportCategory(new Long(0));
        }

        rf.setShowTabs(null);
            if ( rf.getTabs()) {
                rf.setShowTabs(true);
                forwardName = "forwardTabs";
                if (!RequestUtils.isLoggued(response, request.getSession(), request)) {
                    return null;
                }
            }
            else{
                rf.setShowTabs(false);
        }
        
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        if ( tm != null )
            rf.setCurrentMemberId(tm.getMemberId());
        
                                                            
        getAllReports(appSettingSet, rf, tm, request);
        rf.setCurrentPage(rf.getPage());
//      rf.setPagesToShow(10);
        if (action != null && action.equalsIgnoreCase("search")) {
            rf.setCurrentPage(0);
            rf.setPage(0);
        }
        
        doPagination(rf, request);

        if(tm == null){
            //Prepare filter for Public View export
//          this code makes no sense as report publicness is saved on report-by-report basis            
//          AmpARFilter arf = (AmpARFilter) session.getAttribute(ArConstants.REPORTS_Z_FILTER);
//          if(arf==null) arf=new AmpARFilter();        
//          arf.setPublicView(true);
//          session.setAttribute(ArConstants.REPORTS_Z_FILTER, arf);
            if(!FeaturesUtil.isVisibleModule("Public Reports")) {
                return mapping.findForward("index");
            } else {
                return mapping.findForward("forwardPublic");
            }
        }
        else
            return mapping.findForward( forwardName );
    }
    private void doPagination(ReportsForm rf, HttpServletRequest request) {
        Collection allReports = rf.getReports();
        Collection pageList = rf.getReportsList();
        int pageSize = rf.getTempNumResults();
        if (pageList == null) {
            pageList = new ArrayList<AmpReports>();
            rf.setReportsList(pageList);
        }
        rf.setPageSize(rf.getTempNumResults()); 
        pageList.clear();
        int i = 0;


        int idx = 0;

        if(pageSize > 0 && rf.getPage() * rf.getPageSize() < allReports.size()){
            idx =  rf.getPage() * rf.getPageSize();
        }else{
            idx = 0;
            rf.setPage(0);
        }

        Double totalPages = 0.0;
        if(pageSize > 0){
            Iterator iterator = allReports.iterator();
            while(i<idx){
                iterator.next();
                i++;
            }
            for (i=0; iterator.hasNext() && i < pageSize; i++) {
                pageList.add(iterator.next());
            }
            totalPages=Math.ceil(1.0*allReports.size() / rf.getPageSize());
        }
        else{
            Iterator iterator = allReports.iterator();
            for (i=0;iterator.hasNext(); i++) {
                pageList.add(iterator.next());
           }
            totalPages=1.0;         
        }

        AmpApplicationSettings ampAppSettings = AmpARFilter.getEffectiveSettings();

        int numberOfPagesToDisplay = Integer.MAX_VALUE;

        if (ampAppSettings != null){
            Integer defRecordsPerPage = ampAppSettings.getDefaultRecordsPerPage();
            if(defRecordsPerPage != null && defRecordsPerPage > 0 && (ampAppSettings.getNumberOfPagesToDisplay() != null)) {
                numberOfPagesToDisplay = ampAppSettings.getNumberOfPagesToDisplay();
            }
        }

        numberOfPagesToDisplay = Math.min(numberOfPagesToDisplay, totalPages.intValue());

        request.setAttribute("pagesToDisplay", numberOfPagesToDisplay);
        rf.setPagesToShow(numberOfPagesToDisplay);
        rf.setTotalPages(totalPages.intValue());
    }

    private void getAllReports(boolean appSettingSet, ReportsForm rf, TeamMember tm, HttpServletRequest request) {
        Locale currentLocale = new Locale(TLSUtils.getLangCode());
        Collator collator = Collator.getInstance(currentLocale);
        collator.setStrength(Collator.PRIMARY);

//      AmpReports rep = (AmpReports) PersistenceManager.getSession().load(AmpReports.class, 6073L);
//      logger.error("report #" + rep.getAmpReportId() + " has name = " + rep.getName() + " in locale " + TLSUtils.getEffectiveLangCode());
        
        if (rf.getCurrentPage() == 0) {
            rf.setCurrentPage(FIRST_PAGE);
        }
        ReportSortBy col = ReportSortBy.NONE;
        Comparator sort=null;
        for (ReportSortBy column : ReportSortBy.values()) {
            if (column.getSortBy() == rf.getSortBy()) {
                col = column;
                break;
            }
        }
        
        
        Boolean favourites = rf.getOnlyFavourites();
        if (favourites != null && favourites)
            request.setAttribute("onlyFavourites", true);
        
        if (tm == null) {
            List<AmpReports> reports = null;
            if(favourites !=null && favourites){
                reports = ARUtil.getAllPublicReports(false, rf.getKeyword(),rf.getSelectedReportCategory(),favourites);
            }else{
                reports = ARUtil.getAllPublicReports(false, rf.getKeyword(),rf.getSelectedReportCategory());
            }
            
            
            if (reports != null) {
                switch (col) {
                    case NAME_ASC:
                        sort = new AdvancedReportUtil.AmpReportTitleComparator(AdvancedReportUtil.SortOrder.ASC, collator);
                        Collections.sort(reports, sort);
                        break;
                    case NAME_DESC:
                        sort = new AdvancedReportUtil.AmpReportTitleComparator(AdvancedReportUtil.SortOrder.DESC, collator);
                        Collections.sort(reports, sort);
                        break;
                        
                       default:
                           //TODO: not sorting by other fields?
                           break;
                }
               
            }
            rf.setReports(reports);
            rf.setTotalPages(FIRST_PAGE);
        } else {
            rf.setCurrentMemberId(tm.getMemberId());
            ApplicationSettings appSettings = tm.getAppSettings();
            if (appSettings != null) {
                appSettingSet = true;
                if (appSettings.getDefReportsPerPage() == 0) {
                    rf.setTotalPages(FIRST_PAGE);
                }

            }

            switch(col) {
                case NAME_ASC: sort=new AdvancedReportUtil.AmpReportTitleComparator(AdvancedReportUtil.SortOrder.ASC,collator);break;
                case NAME_DESC: sort=new AdvancedReportUtil.AmpReportTitleComparator(AdvancedReportUtil.SortOrder.DESC,collator); break;
                case OWNER_ASC: sort=new AdvancedReportUtil.AmpReportOwnerComparator(AdvancedReportUtil.SortOrder.ASC,collator);break;
                case OWNER_DESC: sort=new AdvancedReportUtil.AmpReportOwnerComparator(AdvancedReportUtil.SortOrder.DESC,collator);break;
                case DATE_ASC: sort=new AdvancedReportUtil.AmpReportCreationDateComparator(AdvancedReportUtil.SortOrder.ASC);break;
                case DATE_DESC: sort=new AdvancedReportUtil.AmpReportCreationDateComparator(AdvancedReportUtil.SortOrder.DESC);break;
                default: sort=new AdvancedReportUtil.AmpReportTitleComparator(AdvancedReportUtil.SortOrder.ASC, collator);break;
            }

            List<AmpReports> teamResults = null;
            //Collection teamMemberResults = null;
            AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
            AmpReports defaultTeamReport = ampAppSettings.getDefaultTeamReport();
            if (appSettingSet) {
                if(favourites !=null && favourites){
                    teamResults = TeamUtil.getAllTeamReports(tm.getTeamId(), rf.getShowTabs(), 0, 0,true,tm.getMemberId(), rf.getKeyword(),rf.getSelectedReportCategory(),favourites);
                }else{
                    teamResults = TeamUtil.getAllTeamReports(tm.getTeamId(), rf.getShowTabs(), 0, 0,true,tm.getMemberId(), rf.getKeyword(),rf.getSelectedReportCategory());
                }

                Double totalPages = Math.ceil(1.0* TeamUtil.getAllTeamReportsCount(tm.getTeamId(), rf.getShowTabs(), true,tm.getMemberId()) / appSettings.getDefReportsPerPage());
                rf.setTotalPages(totalPages.intValue());
                rf.setTempNumResults(appSettings.getDefReportsPerPage());
                //rf.setTempNumResults(100);
            }else{
                if(favourites !=null && favourites){
                    teamResults = TeamUtil.getAllTeamReports(tm.getTeamId(), rf.getShowTabs(), null, null,true,tm.getMemberId(),rf.getKeyword(),rf.getSelectedReportCategory(),favourites);
                }else{
                    teamResults = TeamUtil.getAllTeamReports(tm.getTeamId(), rf.getShowTabs(), null, null,true,tm.getMemberId(),rf.getKeyword(),rf.getSelectedReportCategory());
                }

                }
            boolean found = false;
            if (defaultTeamReport != null){
                Iterator iter = teamResults.iterator();
                while (iter.hasNext()) {
                    AmpReports el = (AmpReports) iter.next();
                    if (el.compareTo(defaultTeamReport) == 0){
                        found = true;
                        break;
                    }
                }
            }
            if (!found && defaultTeamReport!=null && rf.getShowTabs()){
                teamResults.add(defaultTeamReport);
            }

            List<AmpReports> sortedReports = new ArrayList<AmpReports>(teamResults);

            //
            //do not add this in ArrayList constructor.
            Collections.sort(sortedReports,sort);
            rf.setReports(sortedReports);
            //rf.setPage(0);
        }
    }

}
