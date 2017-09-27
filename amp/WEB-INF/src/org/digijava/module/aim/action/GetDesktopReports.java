package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpDesktopTabSelection;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class GetDesktopReports extends TilesAction {
    private static Logger logger    = Logger.getLogger( GetDesktopReports.class );
    public ActionForward execute(ComponentContext context,
            ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
        
        if (tm != null) {
                List<AmpReports> reports = new ArrayList<AmpReports>();
                //Adding the default team report
                AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
                
                AmpReports defaultTeamReport = ampAppSettings.getDefaultTeamReport();
                //ArrayList userReports = TeamMemberUtil.getAllMemberReports(tm.getMemberId());
                
                // The userReports are shown in the upper left corner widget on My Desktop. 
                // It has nothing to do with the tabs
                List<AmpReports> userReports = TeamUtil.getLastShownReports(tm.getTeamId(),tm.getMemberId(), false, true);
                
                List<AmpReports> userActiveTabs = TeamUtil.getAllTeamReports(tm.getTeamId(), true, null, null, true, tm.getMemberId(),null,null);
                if (defaultTeamReport != null){
                    boolean found = false;
                    for(AmpReports el:userActiveTabs) {
                        if (el.compareTo(defaultTeamReport) == 0){
                            found = true;
                            break;
                        }
                    }

                    if ( (userActiveTabs != null) && (!found) )
                        reports.add(defaultTeamReport);
                }
                
                //update currentTabReport reference to reflect the locale change
                AmpReports currentTabReport = (AmpReports)session.getAttribute(Constants.CURRENT_TAB_REPORT);
                Long currentTabReportId = currentTabReport==null? null: currentTabReport.getId();
                if( currentTabReportId!=null ) {
                    for(AmpReports r:userActiveTabs) {
                        if( r.getAmpReportId().equals(currentTabReportId) ) {
                            currentTabReport = r;
                            break;
                        }
                    }
                    session.setAttribute(Constants.CURRENT_TAB_REPORT, currentTabReport);
                }
                
                //After Tanzania: Team Leaders should see all
//              if (tm.getTeamHead() == true) {
//                  reports.addAll(TeamUtil.getAllTeamReports(userReports));
//              } else {
                    reports.addAll(userActiveTabs);
//              }
                    
                    
                Integer reportsPerPage=0;
                
                TreeSet<AmpReports> sortedActiveTabs    = new TreeSet<AmpReports>(AmpReports.lexicographicComparator);
                if ( userActiveTabs != null )
                        sortedActiveTabs.addAll( userActiveTabs );
                //Collections.sort((List<AmpReports>) reports, AmpReports.UpdatedDateComparator );
                //Collections.reverse((List<AmpReports>) reports);
                                
                session.setAttribute(Constants.MY_REPORTS, userReports);
                session.setAttribute(Constants.MY_ACTIVE_TABS, sortedActiveTabs);
                session.setAttribute(Constants.TEAM_ID,tm.getTeamId());
                session.setAttribute(Constants.MY_REPORTS_PER_PAGE,reportsPerPage);

                /* Setting Team Members tabs */
                AmpTeamMember ampTeamMember             = TeamUtil.getAmpTeamMember(tm.getMemberId());
                Collection<AmpReports> tabs             = new ArrayList<AmpReports>();
                boolean defaultTeamReportAdded          = false;
                if ( ampTeamMember.getDesktopTabSelections()!=null && ampTeamMember.getDesktopTabSelections().size() > 0 ) {
                        TreeSet<AmpDesktopTabSelection> sortedSelection = 
                            new TreeSet<AmpDesktopTabSelection>(AmpDesktopTabSelection.tabOrderComparator);
                        sortedSelection.addAll( ampTeamMember.getDesktopTabSelections() );
                        Iterator<AmpDesktopTabSelection> iter   = sortedSelection.iterator();
                        while ( iter.hasNext() ) {
                            AmpReports rep  = iter.next().getReport();
                            tabs.add( rep );
                            if ( defaultTeamReport!=null && defaultTeamReport.getAmpReportId().equals(rep.getAmpReportId()) )
                                defaultTeamReportAdded  = true;
                        }
                }
                if ( defaultTeamReport!=null && !defaultTeamReportAdded )
                    tabs.add( defaultTeamReport );
                session.setAttribute( Constants.MY_TABS , tabs);

                /* Setting default_team_report in session */
                ApplicationSettings appSettings = tm.getAppSettings();
                if ( appSettings != null ) {
                    if ( appSettings.getDefaultAmpReport() != null ) {
                        
                        AmpReports default_report   =  null;//appSettings.getDefaultAmpReport();
                        if(session.getAttribute(Constants.DEFAULT_TEAM_REPORT)!=null)
                            default_report=(AmpReports) session.getAttribute(Constants.DEFAULT_TEAM_REPORT);
                        else {
                            default_report  =  appSettings.getDefaultAmpReport();
                            session.setAttribute(Constants.DEFAULT_TEAM_REPORT, default_report);
                        }
                        if (reports == null)
                            reports = new ArrayList<AmpReports>();
                        this.addReportToCollection(default_report, reports);
                    }
                    else
                        if(session.getAttribute(Constants.DEFAULT_TEAM_REPORT)!=null)
                        {
                            AmpReports default_report = (AmpReports) session.getAttribute(Constants.DEFAULT_TEAM_REPORT);
                            if (reports == null)
                                reports = new ArrayList<AmpReports>();
                            this.addReportToCollection(default_report, reports);
                        }
                        else{
                            
                        
//                      logger.info("!!!!!!!!!!!! The default team report is null");
                            reportsPerPage = appSettings.getDefReportsPerPage();
                            if (reportsPerPage == null){
                                reportsPerPage=0;
                            }
                            session.setAttribute(Constants.MY_REPORTS_PER_PAGE,reportsPerPage);
                        }
                }
                else
                    logger.info("Application settings is null");
                /*END - Setting default_team_report in session*/

                if(tm.getTeamHead()) session.setAttribute(Constants.TEAM_Head,"yes");
                    else session.setAttribute(Constants.TEAM_Head,"no");
        } else {
            Collection<AmpReports> reports = ARUtil.getAllPublicReports(null, null, null);
            session.setAttribute(Constants.MY_REPORTS,reports);
        }
        return null;
    }

    private void addReportToCollection(AmpReports report, Collection<AmpReports> col) {
        for(AmpReports colReport:col) {
            if ( colReport.getAmpReportId() == report.getAmpReportId() )
                return;
        }

        col.add(report);
    }
}
