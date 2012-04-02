package org.digijava.module.aim.action;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.form.ReportsForm.ReportSortBy;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class ShowTeamReports extends Action {

	private static Logger logger = Logger.getLogger(ShowTeamReports.class);
	private final static int FIRST_PAGE = 1;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		String forwardName	= "forward";
		
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
        }

		rf.setShowTabs(null);
			if ( rf.getTabs()) {
				rf.setShowTabs(true);
				forwardName	= "forwardTabs";
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
		rf.setPagesToShow(10);
        if (action != null && action.equalsIgnoreCase("search")) {
            rf.setCurrentPage(0);
            rf.setPage(0);
        }
		
		doPagination(rf, request);
		
		if(tm == null){
			//Prepare filter for Public View export
			AmpARFilter arf = (AmpARFilter) session.getAttribute(ArConstants.REPORTS_FILTER);
			if(arf==null) arf=new AmpARFilter();		
			arf.setPublicView(true);
			session.setAttribute(ArConstants.REPORTS_FILTER,arf);
			return mapping.findForward("forwardPublic");
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

		rf.setTotalPages(totalPages.intValue());
	}

	private void getAllReports(boolean appSettingSet, ReportsForm rf, TeamMember tm, HttpServletRequest request) {
        String siteId = RequestUtils.getSite(request).getSiteId();
		String locale = RequestUtils.getNavigationLanguage(request).getCode();
        Locale currentLocale = new Locale(locale);
        Collator collator = Collator.getInstance(currentLocale);
        collator.setStrength(Collator.PRIMARY);

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
        

		if (tm == null) {
		List reports = ARUtil.getAllPublicReports(false, rf.getKeyword());
            if (reports != null) {
                switch (col) {
                    case NAME_ASC:
                        sort = new AdvancedReportUtil.AmpReportTitleComparator(AdvancedReportUtil.SortOrder.ASC, collator);
                        break;
                    case NAME_DESC:
                        sort = new AdvancedReportUtil.AmpReportTitleComparator(AdvancedReportUtil.SortOrder.DESC, collator);
                        break;
                    default:
                        sort = new AdvancedReportUtil.AmpReportIdComparator();
                        break;
                }
                Collections.sort(reports, sort);
            }
			rf.setReports(reports);
			rf.setTotalPages(FIRST_PAGE);
		} else {
			rf.setCurrentMemberId(tm.getMemberId());
			ApplicationSettings appSettings = tm.getAppSettings();
			if ( appSettings != null && appSettings.getDefReportsPerPage() == 0)
				rf.setTotalPages(FIRST_PAGE);
			
			if (appSettings != null ) {
				appSettingSet = true;
			}

            
            switch(col){
                case NAME_ASC: sort=new AdvancedReportUtil.AmpReportTitleComparator(AdvancedReportUtil.SortOrder.ASC,collator);break;
                case NAME_DESC: sort=new AdvancedReportUtil.AmpReportTitleComparator(AdvancedReportUtil.SortOrder.DESC,collator); break;
                case OWNER_ASC: sort=new AdvancedReportUtil.AmpReportOwnerComparator(AdvancedReportUtil.SortOrder.ASC,collator);break;
                case OWNER_DESC: sort=new AdvancedReportUtil.AmpReportOwnerComparator(AdvancedReportUtil.SortOrder.DESC,collator);break;
                case DATE_ASC: sort=new AdvancedReportUtil.AmpReportCreationDateComparator(AdvancedReportUtil.SortOrder.ASC);break;
                case DATE_DESC: sort=new AdvancedReportUtil.AmpReportCreationDateComparator(AdvancedReportUtil.SortOrder.DESC);break;
                default: sort=new AdvancedReportUtil.AmpReportTitleComparator(AdvancedReportUtil.SortOrder.ASC, collator);break;
            }
           
			List<AmpReports> teamResults =null;
			//Collection teamMemberResults = null;
			AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
			AmpReports defaultTeamReport = ampAppSettings.getDefaultTeamReport();
			if (appSettingSet) {
				teamResults = (ArrayList)TeamUtil.getAllTeamReports(tm.getTeamId(), rf.getShowTabs(), 0, 0,true,tm.getMemberId(), rf.getKeyword());
				Double totalPages = Math.ceil(1.0* TeamUtil.getAllTeamReportsCount(tm.getTeamId(), rf.getShowTabs(), true,tm.getMemberId()) / appSettings.getDefReportsPerPage());
				rf.setTotalPages(totalPages.intValue());
				rf.setTempNumResults(appSettings.getDefReportsPerPage());
				//rf.setTempNumResults(100);
			}else{
				teamResults = (ArrayList)TeamUtil.getAllTeamReports(tm.getTeamId(), rf.getShowTabs(), null, null,true,tm.getMemberId(),rf.getKeyword());
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
			
			//
			// requirements for translation purposes of hierarchies
			AmpReports el = null;

			String text = null;
			String translatedText = null;
			//String prefix = "aim:reportbuilder:"; not used any more cos hash key translation.
			Iterator iterator = teamResults.iterator();
			List<AmpReports> sortedReports=new ArrayList<AmpReports>();
			while (iterator.hasNext()) {
				el = (AmpReports) iterator.next(); 
				if (el.getHierarchies() != null) {
					AmpReportHierarchy arh = null;
					Set h = new TreeSet<AmpReportHierarchy>();
					Iterator iterator2 = el.getHierarchies().iterator();					
					while (iterator2.hasNext()) {
						arh = (AmpReportHierarchy) iterator2.next();
						text = arh.getColumn().getColumnName();
						try {
							//translatedText = TranslatorWorker.translate(prefix + text.toLowerCase(), locale, siteId);
							translatedText = TranslatorWorker.translateText(text, locale, siteId);
						} catch (WorkerException e) {
							e.printStackTrace();
						}
						if (translatedText.compareTo("") == 0)
							translatedText = text;
						arh.getColumn().setColumnName(translatedText);
						//
						h.add(arh);
					}
					el.setHierarchies(h);					
				}
				sortedReports.add(el);
			}					
			//
			
			//do not add this in ArrayList constructor.
			Collections.sort(sortedReports,sort);
			rf.setReports(sortedReports);
			//rf.setPage(0);
		}
	}

}
