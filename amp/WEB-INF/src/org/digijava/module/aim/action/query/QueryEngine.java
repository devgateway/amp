package org.digijava.module.aim.action.query;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class QueryEngine extends Action{
	@Override
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		ReportsFilterPickerForm rfpForm	= (ReportsFilterPickerForm) form;
		
		rfpForm.setIsnewreport(true);
		rfpForm.setAmpReportId(null);
		
		
		AmpReports reportMeta	= new AmpReports();
		
		Collection<AmpColumns> availableCols	= AdvancedReportUtil.getColumnListWithDbSession();
		Collection<AmpMeasures> availableMeas	= AdvancedReportUtil.getMeasureList();
		AmpCategoryValue level1		= CategoryManagerUtil.getAmpCategoryValueFromDb( CategoryConstants.ACTIVITY_LEVEL_KEY , 0L);
		
		AmpColumns projTitleCol					= null;
		AmpMeasures actualCommMeas				= null;
		
		for ( AmpColumns tempCol: availableCols ) {
			if ( ArConstants.COLUMN_PROJECT_TITLE.equals(tempCol.getColumnName()) ){
				projTitleCol	= tempCol;
				System.out.println( projTitleCol.getFilters().size() );
				break;
			}
		}
		
		AmpReportColumn arc			= new AmpReportColumn();
		arc.setColumn(projTitleCol);
		arc.setLevel(level1);
		arc.setOrderId(1L);
		
		for ( AmpMeasures tempMeas: availableMeas ) {
			if ( "Actual Commitments".equals(tempMeas.getMeasureName()) ){
				actualCommMeas	= tempMeas;
				break;
			}
		}
		AmpReportMeasures arm		= new AmpReportMeasures();
		arm.setMeasure(actualCommMeas);
		arm.setLevel(level1);
		arm.setOrderId(1L);
		
		
		reportMeta.setType( new Long(ArConstants.DONOR_TYPE) );
		reportMeta.setUpdatedDate( new Date(System.currentTimeMillis()) );
		reportMeta.setHideActivities( false );
		reportMeta.setOptions( "A" );
		reportMeta.setReportDescription( "Displaying activities matching your selected query" );
		reportMeta.setName( "Query result tab" );
		reportMeta.setDrilldownTab( true );
		reportMeta.setPublicReport( false );
		reportMeta.setAllowEmptyFundingColumns( false );
		
		reportMeta.setColumns( new HashSet<AmpReportColumn>() );
		reportMeta.setHierarchies( new HashSet<AmpReportHierarchy>() );
		reportMeta.setMeasures( new HashSet<AmpReportMeasures>() );
		
		reportMeta.getColumns().add(arc);
		reportMeta.getMeasures().add(arm);
		
		reportMeta.setAmpReportId(-7L);
		
		request.getSession().setAttribute("reportMeta", reportMeta);
		
		return mapping.findForward("forward");
	}
}
