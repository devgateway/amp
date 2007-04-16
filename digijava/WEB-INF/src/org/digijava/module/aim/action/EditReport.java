package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ReportUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

public class EditReport extends Action {
	private static Logger logger 			= Logger.getLogger(EditReport.class);
	private static String [] reportTypes	= {"donor","component","regional"};
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		
			AdvancedReportForm formBean = (AdvancedReportForm) form;
			
			String strReportId	= request.getParameter("rid");
			if (strReportId.compareTo("") != 0) {
				//AmpReports ampreport	= (AmpReports) session.get(AmpReports.class, new Long(strReportId));
				AmpReports ampreport	= this.getAmpReport( request, new Long(strReportId).longValue() );
				
				/* Must be removed */
//				logger.info( "Ordered columns are:" + ampreport.getOrderedColumns() );
//				logger.info( "Hierarchies are:" + ampreport.getHierarchies() );
				//logger.info( "members are:" + ampreport.getMembers() );
				/* Must be removed */
				
				
				
				if ( ampreport !=null ) {
					formBean.setAmpColumns(ReportUtil.getColumnList());
					formBean.setAmpMeasures(ReportUtil.getMeasureList());
					formBean.setReportTitle( ampreport.getName() );
					formBean.setReportDescription ( ampreport.getReportDescription() );
					
					//formBean.setAddedColumns( ampreport.getColumns() );
					Set addedColumns			= ampreport.getColumns();
					Iterator iterator			= addedColumns.iterator();
					Collection collColumns		= new ArrayList();
					while ( iterator.hasNext() ) {
						AmpReportColumn ampReportColumn		= (AmpReportColumn) iterator.next();
						AmpColumns ampColumns				= ampReportColumn.getColumn();
						collColumns.add( ampColumns );
					}
					formBean.setAddedColumns( collColumns );
					
					//formBean.setAddedMeasures( ampreport.getMeasures() );
					Set addedMeasures			= ampreport.getMeasures();
					iterator					= addedMeasures.iterator();
					Collection collMeasures		= new ArrayList();
					while ( iterator.hasNext() ) {
						AmpMeasures ampMeasures		= (AmpMeasures) iterator.next();
						collMeasures.add( ampMeasures );
					}
					formBean.setAddedMeasures( collMeasures);
					
					Long longReportType	= ampreport.getType();
					if (longReportType.longValue() > 3 || longReportType.longValue() < 1) {
						logger.error("Wrong report type:" + longReportType);
					} 
					else{
						formBean.setReportType( reportTypes[longReportType.intValue()-1] );
						formBean.setArReportType( reportTypes[longReportType.intValue()-1] ); // This property should be analyzed more carefully						
//						logger.info("Report type is: " + formBean.getReportType() );
					}
					
					
					//formBean.setDescriptionLink( ampreport.getDescription() );
					formBean.setReportTitle( ampreport.getName() );
					formBean.setReportDescription ( ampreport.getReportDescription() );
					
					formBean.setHideActivities( ampreport.getHideActivities() );
					formBean.setReportOption( ampreport.getOptions() );
					
					/* Getting Column Hierarchies */
					Set dbHierarchies				= ampreport.getHierarchies();
					Iterator dbHierarchiesIterator	= dbHierarchies.iterator();
					Collection collHierarchies 		= new ArrayList();
					while ( dbHierarchiesIterator.hasNext() ) {
						Object next								= dbHierarchiesIterator.next();
//						logger.info( "Object in Hierarchie collection is:" + next.getClass() );
						AmpReportHierarchy ampReportHierarchie	= (AmpReportHierarchy) next;
						collHierarchies.add ( ampReportHierarchie.getColumn() );
					}
					formBean.setColumnHierarchie( collHierarchies );
					/* Getting Column Hierarchies */
					
					this.removeAddedColumnsFromPossibleColumns(formBean);
					this.removeAddedMeasuresFromPossibleMeasures(formBean);
					
					formBean.setInEditingMode( true );
					formBean.setDbReportId( new Long(strReportId).longValue() );
					
					
					
				}
				else 
					logger.error("Couldn't find the AmpReport with rid " + strReportId);
				
			
			}
			else{
				logger.error("Couldn't find rid" + strReportId + "parameter in request.");
			}
			
			return mapping.findForward("forward");
	}
	
	private AmpReports getAmpReport(HttpServletRequest request, long rid) throws java.lang.Exception {
		HttpSession session 	= request.getSession();
		Collection myreports	= (Collection)session.getAttribute(Constants.MY_REPORTS);

		Session pmsession 		= PersistenceManager.getSession();
		/* Must be removed */
//		logger.info("MY_REPORTS is:" + myreports);
		/* Must be removed */
		
		Iterator iterator		= myreports.iterator();
		while ( iterator.hasNext() ) {
			AmpReports report	= (AmpReports)iterator.next();
			if ( report.getAmpReportId().longValue() == rid ) {
				return (AmpReports) pmsession.get(AmpReports.class, new Long(rid));
			}
				
		}
		return null;
	}
	
	private void removeAddedColumnsFromPossibleColumns(AdvancedReportForm formBean) {
		Collection possibleColumns			= formBean.getAmpColumns();
		Collection addedColumns				= formBean.getAddedColumns();
		Collection hierarchieColumns		= formBean.getColumnHierarchie();
		
		/* allAddedColumns will contain all elements from both possibleColumns and addedColumns */
		Collection allAddedColumns			= new ArrayList ( addedColumns );
		allAddedColumns.addAll( hierarchieColumns );
				
		Iterator allAddedColumnsIterator	= allAddedColumns.iterator();
		
		while ( allAddedColumnsIterator.hasNext() ) {
			AmpColumns addedAmpColumn		= (AmpColumns)allAddedColumnsIterator.next();
			Set removableColumns			= new HashSet();
			
			Iterator possibleColumnsIterator	= possibleColumns.iterator();
			while( possibleColumnsIterator.hasNext() ) {
				AmpColumns possibleAmpColumn	= (AmpColumns)possibleColumnsIterator.next();
				if ( addedAmpColumn.getColumnId().longValue() == possibleAmpColumn.getColumnId().longValue() ) 
					removableColumns.add( possibleAmpColumn );
			}
			possibleColumns.removeAll( removableColumns );
		}
	}
	private void removeAddedMeasuresFromPossibleMeasures(AdvancedReportForm formBean) {
		Collection possibleMeasures				= formBean.getAmpMeasures();
		Collection addedMeasures				= formBean.getAddedMeasures();
				
		Iterator addedMeasuresIterator			= addedMeasures.iterator();
		
		while ( addedMeasuresIterator.hasNext() ) {
			AmpMeasures addedAmpMeasure		= (AmpMeasures)addedMeasuresIterator.next();
			Set removableMeasures				= new HashSet();
			
			Iterator possibleMeasuresIterator	= possibleMeasures.iterator();
			while( possibleMeasuresIterator.hasNext() ) {
				AmpMeasures possibleAmpMeasure	= (AmpMeasures)possibleMeasuresIterator.next();
				if ( addedAmpMeasure.getMeasureId().longValue() == possibleAmpMeasure.getMeasureId().longValue() ) 
					removableMeasures.add( possibleAmpMeasure );
			}
			possibleMeasures.removeAll( removableMeasures );
		}
	}
	
}
