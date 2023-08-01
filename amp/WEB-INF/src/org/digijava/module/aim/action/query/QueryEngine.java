package org.digijava.module.aim.action.query;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ReportContextData;
import org.digijava.module.aim.action.ViewNewAdvancedReport;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.translation.util.MultilingualInputFieldValues;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class QueryEngine extends Action{
    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        request.setAttribute(ReportContextData.BACKUP_REPORT_ID_KEY, ReportContextData.REPORT_ID_QUERY_ENGINE);
        request.getSession().setAttribute("report_wizard_current_id", ReportContextData.REPORT_ID_QUERY_ENGINE);
        ReportContextData.createWithId(ReportContextData.REPORT_ID_QUERY_ENGINE, true); // forget everything about previous filter
        request.setAttribute(ViewNewAdvancedReport.MULTILINGUAL_TAB_PREFIX + "_title", new MultilingualInputFieldValues(AmpReports.class, null, "name", null, null));
        //ReportsFilterPickerForm rfpForm   = (ReportsFilterPickerForm) form;
        //HttpSession httpSession = request.getSession();
        //httpSession.removeAttribute(ArConstants.REPORTS_Z_FILTER);
        
        //rfpForm.setIsnewreport(true);
        //rfpForm.setAmpReportId(null);
            
        AmpReports reportMeta   = new AmpReports();
        
        reportMeta.setColumns( new HashSet<AmpReportColumn>() );
        reportMeta.setHierarchies( new HashSet<AmpReportHierarchy>() );
        reportMeta.setMeasures( new HashSet<AmpReportMeasures>() );
        
        Collection<AmpColumns> availableCols    = AdvancedReportUtil.getColumnList();
        Collection<AmpMeasures> availableMeas   = AdvancedReportUtil.getMeasureList();
        AmpCategoryValue level1     = CategoryManagerUtil.getAmpCategoryValueFromDb( CategoryConstants.ACTIVITY_LEVEL_KEY , 0L);
        
        AmpColumns projTitleCol                 = null;
        
        for ( AmpColumns tempCol: availableCols ) {
            if ( ArConstants.COLUMN_PROJECT_TITLE.equals(tempCol.getColumnName()) ){
                projTitleCol    = tempCol;
                //System.out.println( projTitleCol.getFilters().size() );
                break;
            }
        }
        
        AmpReportColumn arc         = new AmpReportColumn();
        arc.setColumn(projTitleCol);
        arc.setLevel(level1);
        arc.setOrderId(1L);
        
        for ( AmpMeasures tempMeas: availableMeas ) {
            if ( "Actual Commitments".equals(tempMeas.getMeasureName()) ){
                AmpMeasures actualCommMeas      = tempMeas;
                AmpReportMeasures arm       = new AmpReportMeasures();
                arm.setMeasure(actualCommMeas);
                arm.setLevel(level1);
                arm.setOrderId(1L);
                reportMeta.getMeasures().add(arm);
            }
            if ( "Actual Disbursements".equals(tempMeas.getMeasureName()) ){
                AmpMeasures actualDisbMeas      = tempMeas;
                AmpReportMeasures arm       = new AmpReportMeasures();
                arm.setMeasure(actualDisbMeas);
                arm.setLevel(level1);
                arm.setOrderId(2L);
                reportMeta.getMeasures().add(arm);
            }
        }
        
        reportMeta.setType( new Long(ArConstants.DONOR_TYPE) );
        reportMeta.setUpdatedDate( new Date(System.currentTimeMillis()) );
        reportMeta.setHideActivities( false );
        reportMeta.setOptions( "A" );
        reportMeta.setReportDescription( "Displaying activities matching your selected query" );
        reportMeta.setName( "Query result tab" );
        reportMeta.setDrilldownTab( true );
        reportMeta.setPublicReport( false );
        reportMeta.setAllowEmptyFundingColumns( false );        
        
        reportMeta.getColumns().add(arc);
        
        reportMeta.setAmpReportId(ReportContextData.QUERY_ENGINE_REPORT_ID);
        
        ReportContextData.getFromRequest().setReportMeta(reportMeta);
        
        return mapping.findForward("forward");
    }
}
