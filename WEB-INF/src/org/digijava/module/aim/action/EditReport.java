package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import org.digijava.module.aim.annotations.reports.ColumnLike;
import org.digijava.module.aim.annotations.reports.Identificator;
import org.digijava.module.aim.annotations.reports.Level;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.exception.reports.ReportException;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.FeaturesUtil;

public class EditReport extends Action {
	private static Logger logger 			= Logger.getLogger(EditReport.class);
	private static String [] reportTypes	= {"donor","component","regional","contribution"};
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
			boolean isXLevelEnabled		= Boolean.parseBoolean( FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ACTIVITY_LEVEL) );
			AdvancedReportForm formBean = (AdvancedReportForm) form;
			formBean.setMaxStep(new Integer(6));
			String strReportId	= request.getParameter("rid");
			
			
			
			if (strReportId.compareTo("") != 0) {
				Session session = PersistenceManager.getSession();
				AmpReports ampreport	= (AmpReports) session.get(AmpReports.class, new Long(strReportId));
				//AmpReports ampreport	= this.getAmpReport( request, new Long(strReportId).longValue() );
				
//				 Must be removed 
//				logger.info( "Ordered columns are:" + ampreport.getOrderedColumns() );
//				logger.info( "Hierarchies are:" + ampreport.getHierarchies() );
				//logger.info( "members are:" + ampreport.getMembers() );
//				 Must be removed 
				
				
				
				if ( ampreport !=null ) {
					formBean.setAmpColumns(AdvancedReportUtil.getColumnList());
					formBean.setAmpMeasures(AdvancedReportUtil.getMeasureList());
					formBean.setReportTitle( ampreport.getName() );
					formBean.setReportDescription ( ampreport.getReportDescription() );
					
					//formBean.setAddedColumns( ampreport.getColumns() );
					if ( isXLevelEnabled ) {
						formBean.setColumnsSelection( new ArrayList<AmpReportColumn>( ampreport.getColumns() ) );
						Collections.sort( formBean.getColumnsSelection() );
					}
					else {
						Set addedColumns								= ampreport.getColumns();
						Iterator iterator								= addedColumns.iterator();
						Collection collColumns							= new ArrayList();
						ArrayList<AmpReportColumn> reportColumnsOrdered	= new ArrayList<AmpReportColumn>();
						
						reportColumnsOrdered.addAll(addedColumns);
						Collections.sort(reportColumnsOrdered);
						iterator	= reportColumnsOrdered.iterator();
						while ( iterator.hasNext() ) {
							AmpReportColumn ampReportColumn		= (AmpReportColumn) iterator.next();
							AmpColumns ampColumns				= ampReportColumn.getColumn();
							collColumns.add( ampColumns );
						}
						
						formBean.setAddedColumns( collColumns );
					}
					
					//formBean.setAddedMeasures( ampreport.getMeasures() );
					if ( isXLevelEnabled ) {
						formBean.setMeasuresSelection( new ArrayList<AmpReportMeasures>( ampreport.getReportMeasures() ) );
						Collections.sort( formBean.getMeasuresSelection() );
					}
					else {
						Set addedMeasures			= ampreport.getMeasures();
						Iterator iterator			= addedMeasures.iterator();
						ArrayList collMeasures		= new ArrayList();
						while ( iterator.hasNext() ) {
						    	AmpReportMeasures ampMeasures		= (AmpReportMeasures) iterator.next();
							collMeasures.add( ampMeasures );
						}
						Collections.sort(collMeasures);
						formBean.setAddedMeasures( collMeasures);
						
					}
					
					Long longReportType	= ampreport.getType();
					if (longReportType.longValue() > reportTypes.length || longReportType.longValue() < 1) {
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
					System.out.println("2.........."+formBean.getHideActivities());
					formBean.setReportOption( ampreport.getOptions() );
					formBean.setPublicReport( ampreport.getPublicReport() );
					formBean.setDrilldownTab( ampreport.getDrilldownTab() );
					
//					 Getting Column Hierarchies
					if ( isXLevelEnabled ) {
						formBean.setHierarchiesSelection( new ArrayList<AmpReportHierarchy>(ampreport.getHierarchies()) );
						Collections.sort( formBean.getHierarchiesSelection() );
					}
					else {
						Set<AmpReportHierarchy> hierarchies	= ampreport.getHierarchies();
						List<AmpReportHierarchy> sortedCollHierarchies 		= new ArrayList<AmpReportHierarchy>();
						sortedCollHierarchies.addAll(hierarchies);
						Collections.sort(sortedCollHierarchies);
						Iterator<AmpReportHierarchy> hierarchiesIterator	= sortedCollHierarchies.iterator();
						List<AmpColumns> collHierarchies 		= new ArrayList<AmpColumns>();
						while ( hierarchiesIterator.hasNext() ) {
							AmpReportHierarchy ampReportHierarchie	= hierarchiesIterator.next();
							collHierarchies.add ( ampReportHierarchie.getColumn() );
						}						
						formBean.setColumnHierarchie( collHierarchies );
					}
//				    END - Getting Column Hierarchies 
					if (isXLevelEnabled){
						formBean.setActivityLevel( ampreport.getActivityLevel().getId() );
						
						formBean.setColumnToLevel( new HashMap<Long, Collection<AmpCategoryValue>>() );
						formBean.setMeasureToLevel( new HashMap<Long, Collection<AmpCategoryValue>>() );
						AdvancedReport.populateLevelHashMap( formBean.getAmpColumns(), formBean.getColumnToLevel(), false );
						AdvancedReport.populateLevelHashMap( formBean.getAmpMeasures(), formBean.getMeasureToLevel(), false );
						
						removeSelectedLevels(formBean.getColumnToLevel(), formBean.getColumnsSelection() );
						removeSelectedLevels(formBean.getColumnToLevel(), formBean.getHierarchiesSelection() );
						removeSelectedLevels(formBean.getMeasureToLevel(), formBean.getMeasuresSelection() );
					}
					else{
						this.removeAddedColumnsFromPossibleColumns(formBean);
						this.removeAddedMeasuresFromPossibleMeasures(formBean);
					}
					
					formBean.setInEditingMode( true );
					
			
					formBean.setDbReportId( new Long(strReportId).longValue() );
				}
				else 
					logger.error("Couldn't find the AmpReport with rid " + strReportId);
				PersistenceManager.releaseSession(session);
			}
			else{
				logger.error("Couldn't find rid" + strReportId + "parameter in request.");
			}
			formBean.setCurrentTabName("forward"); //this is for highlighting tabs
			return mapping.findForward("forward");
	}
	
	private AmpReports getAmpReport(HttpServletRequest request, long rid){
		HttpSession session 	= request.getSession();
		Collection myreports	= (Collection)session.getAttribute(Constants.MY_REPORTS);

		Session pmsession = null;
		try {
			pmsession = PersistenceManager.getSession();
			Iterator iterator		= myreports.iterator();
			while ( iterator.hasNext() ) {
				AmpReports report	= (AmpReports)iterator.next();
				if ( report.getAmpReportId().longValue() == rid ) {
					return (AmpReports) pmsession.get(AmpReports.class, new Long(rid));
				}
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(pmsession);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private void removeAddedColumnsFromPossibleColumns(AdvancedReportForm formBean) {
		Collection possibleColumns			= formBean.getAmpColumns();
		Collection addedColumns				= formBean.getAddedColumns();
		Collection hierarchieColumns		= formBean.getColumnHierarchie();
		
//		 allAddedColumns will contain all elements from both possibleColumns and addedColumns 
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
			AmpMeasures addedAmpMeasure		= ((AmpReportMeasures)addedMeasuresIterator.next()).getMeasure();
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
	public static void removeSelectedLevels (HashMap<Long, Collection<AmpCategoryValue>> columnToLevel, List reportToColLikeList) {
		try{
			if (columnToLevel == null) {
				throw new ReportException("Could not remove already added levels because level container is null");
			}
			if (reportToColLikeList == null) {
				throw new ReportException("Could not remove already added levels because added level container is null");
			}
		
			Iterator iter	= reportToColLikeList.iterator();
			
			while ( iter.hasNext() ) {
				Object reportToColLike	= iter.next();
				Object level			= AdvancedReport.invokeGetterForBeanPropertyWithAnnotation(reportToColLike, Level.class, new Object[0]);
				Object colLikeObj		= AdvancedReport.invokeGetterForBeanPropertyWithAnnotation(reportToColLike, ColumnLike.class, new Object[0]);
				Long colLikeObjId		= (Long)AdvancedReport.invokeGetterForBeanPropertyWithAnnotation(colLikeObj, Identificator.class, new Object[0]);
				
				Collection<AmpCategoryValue> levelCollection	= columnToLevel.get(colLikeObjId);
				levelCollection.remove(level);
			}
		}
		catch(Exception E) {
			E.printStackTrace();
			return;
		}
	}
	
}