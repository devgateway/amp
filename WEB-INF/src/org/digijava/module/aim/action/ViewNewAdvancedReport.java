/**
 * ViewNewAdvancedReport.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.action;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.GenericViews;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.Constants;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 15, 2006
 * 
 */
public class ViewNewAdvancedReport extends Action {

	
	private static Logger logger = Logger.getLogger(ViewNewAdvancedReport.class) ;
	
	/**
	 * 
	 */
	public ViewNewAdvancedReport() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
			{
		AdvancedReportForm arf=(AdvancedReportForm) form;
		HttpSession hs = request.getSession();

		String widget=request.getParameter("widget");
		request.setAttribute("widget",widget);
		
		//check currency code:
		if(hs.getAttribute("reportCurrencyCode")==null) hs.setAttribute("reportCurrencyCode",Constants.DEFAULT_CURRENCY);
		
		if(hs.getAttribute("reportSorters")==null) hs.setAttribute("reportSorters",new HashMap());
		Map sorters=(Map) hs.getAttribute("reportSorters");
		String ampReportId = request.getParameter("ampReportId");
		
		GroupReportData rd=(GroupReportData) hs.getAttribute("report");
		AmpReports ar=(AmpReports) hs.getAttribute("reportMeta");
		Session session = PersistenceManager.getSession();
		String sortBy=request.getParameter("sortBy");
		String applySorter = request.getParameter("applySorter");
		if(ampReportId==null) ampReportId=ar.getAmpReportId().toString();
		
		if( (applySorter==null && sortBy==null ) || ar==null || (arf.getCreatedReportId() == null) || (ampReportId!=null && !ampReportId.equals(arf.getCreatedReportId().toString())))
		{
			rd=ARUtil.generateReport(mapping,form,request,response);
	
			ar = (AmpReports) session.get(AmpReports.class, new Long(ampReportId));	
		}
		
		//test if the request was for hierarchy sorting purposes:
		if(applySorter!=null) {
			if(request.getParameter("levelPicked")!=null && request.getParameter("levelSorter")!=null)
				sorters.put(request.getParameter("levelPicked"),new MetaInfo(request.getParameter("levelSorter"),request.getParameter("levelSortOrder")));
			else 	
			sorters.put(arf.getLevelPicked(),new MetaInfo(arf.getLevelSorter(),arf.getLevelSortOrder()));
			
		
			
			rd.importLevelSorters(sorters,ar.getHierarchies().size());
			rd.applyLevelSorter();
						
		}
		
		// test if the request was for column sorting purposes:
		
		if(sortBy!=null) {
			hs.setAttribute("sortBy",sortBy);
			rd.setSorterColumn(sortBy);
		
		}
		
				if (rd==null) return mapping.findForward("index");
				rd.setGlobalHeadingsDisplayed(new Boolean(false));
				
				String viewFormat=request.getParameter("viewFormat");
				if(viewFormat==null) viewFormat=GenericViews.HTML;
				request.setAttribute("viewFormat",viewFormat);
			
				
			
				request.setAttribute("extraTitle",ar.getName());
				request.setAttribute("ampReportId",ampReportId);				
				rd.setCurrentView(viewFormat);
				hs.setAttribute("report",rd);
				hs.setAttribute("reportMeta",ar);
				
				session.close();
				
				return mapping.findForward("forward");
			}
	
}
