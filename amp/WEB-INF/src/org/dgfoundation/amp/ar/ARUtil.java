/**
 * ARUtil.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ArConstants.SyntheticColumnsMeta;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ListCell;
import org.dgfoundation.amp.ar.cell.MetaTextCell;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.ar.dimension.ARDimension;
import org.dgfoundation.amp.ar.dimension.DonorDimension;
import org.dgfoundation.amp.ar.dimension.DonorGroupDimension;
import org.dgfoundation.amp.ar.dimension.DonorTypeDimension;
import org.dgfoundation.amp.ar.view.xls.ColumnReportDataXLS;
import org.dgfoundation.amp.ar.view.xls.GroupReportDataXLS;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.view.xls.PlainColumnReportDataXLS;
import org.dgfoundation.amp.ar.view.xls.PlainGroupReportDataXLS;
import org.dgfoundation.amp.ar.view.xls.RichColumnReportDataXLS;
import org.dgfoundation.amp.ar.view.xls.RichGroupReportDataXLS;
import org.dgfoundation.amp.ar.view.xls.XLSExportType;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.dgfoundation.amp.ar.workers.CategAmountColWorker;
import org.dgfoundation.amp.ar.workers.MetaTextColWorker;
import org.dgfoundation.amp.ar.workers.TextColWorker;
import org.dgfoundation.amp.ar.workers.TrnTextColWorker;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.action.reportwizard.ReportWizardAction;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.ComparableMonth;
import org.digijava.module.aim.helper.fiscalcalendar.EthiopianCalendar;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.budgetexport.reports.implementation.BudgetCategAmountColWorker;
import org.digijava.module.budgetexport.reports.implementation.BudgetColumnReportDataXLS;
import org.digijava.module.budgetexport.reports.implementation.BudgetGroupReportDataXLS;
import org.digijava.module.budgetexport.reports.implementation.BudgetMetaTextColWorker;
import org.digijava.module.budgetexport.reports.implementation.BudgetTextColWorker;
import org.digijava.module.budgetexport.util.BudgetExportConstants;
import org.digijava.module.budgetexport.util.MappingEncoder;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 * 
 */
public final class ARUtil {
    /**
     * 
     * @param getTabs if null gets both reports and tabs, on true only tabs, on false only reports
     * @return list of public reports
     */
    @SuppressWarnings("unchecked")
    public static List<AmpReports> getAllPublicReports(Boolean getTabs,String name,Long reportCategoryId) {
        return getAllPublicReports(getTabs, name, reportCategoryId, null);
    }
    
    public static List<AmpReports> getAllPublicReports(Boolean getTabs,String name,Long reportCategoryId,Boolean onlyCategorizied) {
        Session session = null;
        List<AmpReports> col = null;
        String tabFilter    = "";

        if ( getTabs!=null ) {
            tabFilter   = "r.drilldownTab=:getTabs AND";
        }
        try {
            session = PersistenceManager.getSession();
            String queryString = "select r from " + AmpReports.class.getName()+ " r " + "where ( " + tabFilter + " r.publicReport=true)";
            if (name != null) {
                //queryString += " and lower(r.name) like lower(:name) ";
                queryString += String.format(" and lower(%s) like lower(:name) ", AmpReports.hqlStringForName("r"));
            }
            if(onlyCategorizied!=null && onlyCategorizied){
                queryString += " and r.reportCategory is not null ";
            }
            if(reportCategoryId !=null && !reportCategoryId.equals(new Long(0))){
                 queryString += " and r.reportCategory=:repCat ";
            }
            queryString +=" order by r.publishedDate desc ";
            Query qry = session.createQuery(queryString);
            if ( getTabs!=null )
                qry.setBoolean("getTabs", getTabs);
             if (name != null) {
               qry.setString("name", '%' + name+ '%');
            }
             if(reportCategoryId !=null && !reportCategoryId.equals(new Long(0))){
                 qry.setLong("repCat", reportCategoryId);
             }

            col= qry.list();
            
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
        return col;
    }

    protected static Logger logger = Logger.getLogger(ARUtil.class);

    public static Constructor getConstrByParamNo(Class c, int paramNo, HttpSession session) {
        try {
            String budgetTypeReport = (String) session.getAttribute(BudgetExportConstants.BUDGET_EXPORT_TYPE );
            
            return getConstrByParamNo(c, paramNo, budgetTypeReport != null );
        }
        catch (Exception e) {
            e.printStackTrace();
            return ARUtil.getConstrByParamNo(c, paramNo);
        }
    }
    
    /**
     * hacks method for overriding the requested class in some cases
     * @param c
     * @param paramNo
     * @param useBudgetClasses
     * @param isPlainReport
     * @return
     */
    public static Constructor getConstrByParamNo(Class c, int paramNo, boolean useBudgetClasses, XLSExportType exportType) {
            if ( useBudgetClasses ) {
                if (TextColWorker.class.equals(c))
                    c   = BudgetTextColWorker.class;
                if (TrnTextColWorker.class.equals(c))
                    c   = BudgetTextColWorker.class;
                if (MetaTextColWorker.class.equals(c))
                    c   = BudgetMetaTextColWorker.class;
                if (CategAmountColWorker.class.equals(c) )
                    c   = BudgetCategAmountColWorker.class;
                if ( GroupReportDataXLS.class.equals(c) )
                    c   = BudgetGroupReportDataXLS.class;
                if ( ColumnReportDataXLS.class.equals(c) )
                    c   = BudgetColumnReportDataXLS.class;
            } else if (exportType == XLSExportType.PLAIN_XLS_EXPORT) {
                if ( GroupReportDataXLS.class.equals(c) )
                    c   = PlainGroupReportDataXLS.class;
                if ( ColumnReportDataXLS.class.equals(c) )
                    c   = PlainColumnReportDataXLS.class;
            } else if (exportType == XLSExportType.RICH_XLS_EXPORT){
                if (GroupReportDataXLS.class.equals(c))
                    c = RichGroupReportDataXLS.class;
                if (ColumnReportDataXLS.class.equals(c))
                    c = RichColumnReportDataXLS.class;
            }
            
            return ARUtil.getConstrByParamNo(c, paramNo);
    }
    
    public static Constructor getConstrByParamNo(Class c, int paramNo, boolean useBudgetClasses) {
        return getConstrByParamNo(c, paramNo, useBudgetClasses, XLSExportType.SIMPLE_XLS_EXPORT);
    }
    
    public static Constructor getConstrByParamNo(Class c, int paramNo) {
        Constructor[] clist = c.getConstructors();
        for (int j = 0; j < clist.length; j++) {
            if (clist[j].getParameterTypes().length == paramNo)
                return clist[j];
        }
        logger.error("Cannot find a constructor with " + paramNo
                + " parameters for class " + c.getName());
        return null;
    }

    /**
     * returns a reference to the currently used report, based on ampReportId parameter, attribute or RCD (in this order)<br />
     * also adds the instance to the RCD and sets the locales on it<br />
     * throws RuntimeException in case it is not possible to identify one
     * @return
     */
    public static AmpReports getReferenceToReport()
    {
        HttpServletRequest request = TLSUtils.getRequest();
        Site site = RequestUtils.getSite(request);
        Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
        Long siteId = site.getId();
        String locale = navigationLanguage.getCode();   
        
        String ampReportId = request.getParameter("ampReportId");
        AmpReports r = null;
        if (ampReportId == null || ampReportId.length() == 0 )
            ampReportId = (String) request.getAttribute("ampReportId");
        
        if ( ampReportId == null || ampReportId.length() == 0 || Long.parseLong(ampReportId) <= 0) {
            r = ReportContextData.getFromRequest().getReportMeta();
        }
        
        if (r == null) {
            Session session = PersistenceManager.getSession();
            r = (AmpReports) session.get(AmpReports.class, new Long(ampReportId));
            if (r == null)
                throw new RuntimeException("No report with ampReportId = " + ampReportId + " found!");
        }
        
        r.setSiteId(siteId);
        r.setLocale(locale);
        ReportContextData.getFromRequest().setReportMeta(r);
        
        return r;
    }
    
    /**
     * writes a plain text response. rethrows any exception
     * @param response
     * @param msg
     */
    public static void writeResponse(HttpServletResponse response, String msg)
    {
        try
        {
            response.setContentType("text/html");   
            OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
            PrintWriter out = new PrintWriter(outputStream, true);
            out.println(msg);
            out.close();    
            outputStream.close();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static void generateReportNotFoundPage(HttpServletResponse response){
        generateReportResponse(response, "Report with given id not found!");
    }
    
    public static void generateReportResponse(HttpServletResponse response, String messsage, Object... args){
        String url = "/";
        String alert = String.format(TranslatorWorker.translateText(messsage), args);
        String script = "<script>if ((typeof opener !== 'undefined') && (opener != null)) {opener.close();};\n" 
            + "alert('"+ alert +"');\n" 
            + "window.location=('"+ url +"');\n"
            + "</script>";
        writeResponse(response, script);
    }
    
    /**
     * generates a report given the data from an AmpReports instance and some filters
     * @param request - legacy, will be removed in the future
     * @param r - the report configuration data
     * @param filter - the filter to be used
     * @return
     */
    public static GroupReportData generateReport(AmpReports r, AmpARFilter filter, boolean regenerateFilterQuery, boolean cleanupMetadata)  {


        CellColumn.calls = CellColumn.iterations = MetaInfoSet.calls = MetaInfoSet.iterations = 0;

        HttpSession httpSession = TLSUtils.getRequest().getSession();
        TeamMember teamMember = (TeamMember) httpSession.getAttribute(Constants.CURRENT_MEMBER);

        if (teamMember != null)
            logger.info("Report '" + r.getName() + "' requested by user "
                    + teamMember.getEmail() + " from team "
                    + teamMember.getTeamName());

        //TODO: Check Constantin - code comming from merge
//      AmpARFilter af = (AmpARFilter) httpSession
//              .getAttribute(ArConstants.REPORTS_FILTER);
//      if (af == null)
//          af = new AmpARFilter();
//      af.readRequestData(request);
//      Object initFilter   = request.getAttribute(ArConstants.INITIALIZE_FILTER_FROM_DB);
//      if ( initFilter!=null && "true".equals(initFilter) ) {
//          FilterUtil.populateFilter(r, af);
//          /* The prepare function needs to have the filter (af) already populated */
//          FilterUtil.prepare(request, af, true);
//          httpSession.setAttribute( ReportWizardAction.EXISTING_SESSION_FILTER, af);
//      }
//      httpSession.setAttribute(ArConstants.REPORTS_FILTER, af);

        AmpReportGenerator arg = new AmpReportGenerator(r, filter, regenerateFilterQuery);
        arg.setCleanupMetadata(cleanupMetadata);
        arg.generate();
        
        logger.info(String.format("while generating report, had %d / %d CellColumn calls (%.2f waste) and %d / %d MetaInfo calls (%.2fx waste)", CellColumn.iterations, CellColumn.calls, 1.0 * CellColumn.iterations / (0.001 + CellColumn.calls), MetaInfoSet.iterations, MetaInfoSet.calls, 1.0 * MetaInfoSet.iterations / (0.001 + MetaInfoSet.calls)));
        GroupReportData result = arg.getReport();
        
        arg.setReport(null);
        r.setReportGenerator(null);
        
        return result;
    }


    public static Collection<AmpOrgGroup> filterDonorGroups(Collection donorGroups) {
        Collection<AmpOrgGroup> ret = new ArrayList<AmpOrgGroup>();
        if (donorGroups == null) {
            logger.error("Collection of AmpOrgGroup should NOT be null in filterDonorGroups");
            return ret;
        }
        Iterator iter = donorGroups.iterator();
        while (iter.hasNext()) {
            AmpOrgGroup grp = (AmpOrgGroup) iter.next();
            if (grp.getOrgType() != null
                    && grp.getOrgType().getOrgType() != null
                    && (grp.getOrgType().getOrgType().toLowerCase().contains(
                            "gov") || grp.getOrgType().getOrgType()
                            .toLowerCase().contains("gouv"))) {
                continue;
            }
            ret.add(grp);
        }

        Collections.sort((List)ret, new DbUtil.HelperAmpOrgGroupNameComparator());
        
        return ret;
    }

    /**
     * Checks if the hierarchy with the specified name as a parameter is present amount the given collection
     * @param hierarchies the collection of hierarchies that the report holds persisted
     * @param hierarchyName the name of the hierarchy to check
     * @return true if the hierarchy exists
     */
    public static boolean hasHierarchy(Collection<AmpReportHierarchy> hierarchies,String hierarchyName) {
        if (hierarchies != null) {
            for (AmpReportHierarchy h : hierarchies) {
                if(h.getColumn().getColumnName().equals(hierarchyName)) return true;
            }
        }
        return false;
    }
    
    public static boolean containsMeasure(String measureName, Collection<AmpReportMeasures> measures)
    {
        if (measureName == null)
            return false;
        
        for(AmpReportMeasures measure:measures){
            AmpMeasures element = measure.getMeasure();
            if (element.getMeasureName().equals(measureName))
                return true;
        }
        return false;
    }
    
    
    public static boolean hasHeaderValue(AmpColumns column){
        return (column.getTotalExpression()!=null )|| column.getColumnName().equalsIgnoreCase(ArConstants.COLUMN_FUNDING);
    }
    
    public static boolean containsColumn(String columName, Set columns) {
        if (columName == null)
            return false;
        Iterator i = columns.iterator();
        while (i.hasNext()) {
            AmpReportColumn element = (AmpReportColumn) i.next();
            if (element.getColumn().getColumnName().indexOf(columName) != -1)
                return true;
        }
        return false;
    }

    public static List<AmpReportHierarchy> createOrderedHierarchies(Collection columns,
            Collection<AmpReportHierarchy> hierarchies) {
        List<AmpReportHierarchy> orderedColumns = new ArrayList<AmpReportHierarchy>(hierarchies.size());
        for (int x = 0; x < hierarchies.size() + columns.size(); x++) {
            for(AmpReportHierarchy element:hierarchies) {
                int order = element.getLevelId().intValue();
                if (order - 1 == x)
                    orderedColumns.add(element);
            }
        }
        return orderedColumns;
    }

    /**
     * Creates a list in the index order of the report wizard selected column
     * order.
     * 
     * @param columns
     * @return
     */
    public static List<AmpReportColumn> createOrderedColumns(Collection<AmpReportColumn> columns) {
        ArrayList<AmpReportColumn> res = new ArrayList<>(columns);
        Collections.sort(res);
        return res;
    }
    
    public static void insertEmptyColumns (String type, CellColumn src, SortedSet<MetaInfo> destMetaSet, AmpARFilter filter) {
        Iterator iter                                   = src.iterator();
        TreeSet<Comparable<? extends Object>>   periods = new TreeSet<Comparable<? extends Object>>();
        try{
            ARUtil.initializePeriodValues(type, periods, filter);
            if (periods.isEmpty())
                return; //nothing to do
        
            while ( iter.hasNext() ) {
                Categorizable elem  = (Categorizable)iter.next();
                MetaInfo minfo      = elem.getMetaData().getMetaInfo(type);
                Comparable c        = (Comparable) minfo.getValue();
                if ( c.compareTo( periods.first() ) > 0 &&
                        c.compareTo( periods.last() ) < 0 && elem.isShow() )
                    periods.add((Comparable) minfo.getValue() );
            }
    
            if (periods!=null && periods.size()>0) {
                Object prevPeriod                   = null;
                Object first                            = periods.first();
                Object last                             = periods.last();
                Iterator periodIter                 = periods.iterator();
                while ( periodIter.hasNext() ) {
                    Object period           = periodIter.next();
                    ////System.out.println("Year found:" + period );
                    int difference          = 0;
                    if ( prevPeriod != null && 
                            (difference=ARUtil.periodDifference(type, prevPeriod, period)) > 1 ) {
                        for (int i=1; i< difference; i++) {
                            Comparable comparable   = ARUtil.getFuturePeriod(type, prevPeriod, i, first, last);
                            if (comparable != null)
                                destMetaSet.add( new MetaInfo(type, comparable) );
                        }
                        
                    }
                    prevPeriod      = period;
                }
            
            }
        }
        catch (Exception e) {
            logger.error( e.getMessage() );
            e.printStackTrace();
        }       
    }
    
    private static Comparable getFuturePeriod(String type, Object period, int step,  Object first, Object last) throws Exception{
        if ( ArConstants.YEAR.equals( type ) ) {
            Integer firstEl     = (Integer) first;
            Integer lastEl          = (Integer) last;
            ////System.out.println("Adding year:" + (((Integer)period) + step) );
            if ( firstEl != null && 
                    (((Integer)period) + step) < firstEl )
                return null;
            if ( lastEl != null && 
                    (((Integer)period) + step) > lastEl)
                return null;
            
            return ((Integer)period) + step;
        }
        
        if ( ArConstants.QUARTER.equals( type ) ) {
            String quarter          = ((String)period ).substring(1);
            Integer quarterId       = ( Integer.parseInt(quarter) );
            if ( quarterId >= 4 )
                throw new Exception( "There is no quarter greater than: " + quarterId );
            if ( quarterId + step > 4 )
                throw new Exception( "Max quarter is 4. Trying to generate quarter: " + (quarterId + step) );
            return "Q" + (quarterId + step); 
        }
        
        if ( ArConstants.MONTH.equals( type ) ) {
            ComparableMonth month                   = ((ComparableMonth) period);
            
            String gsCal                            = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
            AmpFiscalCalendar ampFiscalCalendar     = FiscalCalendarUtil.getAmpFiscalCalendar(Long.parseLong(gsCal));
            if ( ampFiscalCalendar == null )
                throw new Exception("Cannot obtain default AmpFiscalCalendar");
            
            if ( "GREG-CAL".equalsIgnoreCase( ampFiscalCalendar.getBaseCal() ) ) {
                if ( month.getMonthId() >= 11 )
                    throw new Exception("Calendar type is "+ ampFiscalCalendar.getBaseCal() +
                            ". There is no month greater than: " + month.getMonthId());
                Integer newMonthId          = month.getMonthId() + step;
                if ( newMonthId > 11 )
                    throw new Exception("Calendar type is "+ ampFiscalCalendar.getBaseCal() +
                            ". Max month is 11. Trying to generate month: " + newMonthId);
                DateFormatSymbols dfs       = new DateFormatSymbols();
                return new ComparableMonth(newMonthId, dfs.getMonths()[newMonthId]);
            }
            if ( "ETH-CAL".equalsIgnoreCase( ampFiscalCalendar.getBaseCal() ) ) {
                if ( month.getMonthId() >= 13 )
                    throw new Exception("Calendar type is "+ ampFiscalCalendar.getBaseCal() +
                            ". There is no month greater than: " + month.getMonthId());
                Integer newMonthId          = month.getMonthId() + step;
                if ( newMonthId > 13 )
                    throw new Exception("Calendar type is "+ ampFiscalCalendar.getBaseCal() +
                            ". Max month is 13. Trying to generate month: " + newMonthId);
                return new ComparableMonth( newMonthId, new EthiopianCalendar().ethMonths(newMonthId) );
            }
            throw new Exception("Unknown calendar type: " + ampFiscalCalendar.getBaseCal() );
        }
        
        throw new Exception("The specified type is neither YEAR, QUARTER nor MONTH: " + type);
    }
    private static int periodDifference(String type, Object period1, Object period2) throws Exception{
        if ( ArConstants.YEAR.equals( type ) ) {
            return ((Integer)period2) - ((Integer)period1);
        }
        
        if ( ArConstants.QUARTER.equals( type ) ) {
            String quarter1         = ((String)period1 ).substring(1);
            Integer quarterId1      = ( Integer.parseInt(quarter1) );
            
            String quarter2         = ((String)period2 ).substring(1);
            Integer quarterId2      = ( Integer.parseInt(quarter2) );
            
            return quarterId2 - quarterId1;
        }
        
        if ( ArConstants.MONTH.equals( type ) ) {
            ComparableMonth month1      = ((ComparableMonth) period1);
            ComparableMonth month2      = ((ComparableMonth) period2);
            
            return month2.getMonthId() - month1.getMonthId();
            
        }
        
        throw new Exception("The specified type is neither YEAR, QUARTER nor MONTH: " + type);
    }
    private static void initializePeriodValues(String type, Collection<Comparable<? extends Object>> periods, AmpARFilter filter) throws Exception {
//      if ( ArConstants.YEAR.equals(type) ) {
//          if ( filter == null  )
//              throw new Exception("Filter is null when adding empty years to report");
//          if ( filter.getRenderStartYear() != null && filter.getRenderStartYear() > 1800  && filter.getRenderStartYear() < 2200 )
//              periods.add(  filter.getRenderStartYear() -1 );
//          if ( filter.getRenderEndYear() != null && filter.getRenderStartYear() > 1800  && filter.getRenderEndYear() < 2200 )
//              periods.add( filter.getRenderEndYear() + 1 );
//      }
        if ( ArConstants.QUARTER.equals(type) ) {
            periods.add("Q0");
            periods.add("Q5");
        }
        if ( ArConstants.MONTH.equals(type) ) {
            periods.add( new ComparableMonth(-1, "Before 1st month") );
            periods.add( new ComparableMonth(12, "After last month") );
        }
    }
    
    //AMP-6541
    public static void clearDimension(Class c){
        logger.info("removing dimension: "+c.toString());
        ARDimension.DIMENSIONS.remove(c);
    }
    
    public static void clearOrgGroupTypeDimensions(){
        clearDimension(DonorDimension.class);
        clearDimension(DonorGroupDimension.class);
        clearDimension(DonorTypeDimension.class);
    }
    
    public static void cleanReportOfHtmlCodes(GroupReportData rd){
        for (Iterator it = rd.getItems().iterator(); it.hasNext();) {
            Object o  = it.next();
            if(o instanceof GroupReportData) cleanReportOfHtmlCodes((GroupReportData)o);
            if(o instanceof ColumnReportData) {
                ColumnReportData crd = (ColumnReportData) o;
                for (Iterator j = crd.getItems().iterator(); j.hasNext();) {
                    Object oo = j.next();
                    if(oo instanceof CellColumn){
                        CellColumn cellColumn  = (CellColumn)oo;
                        for (Iterator k = cellColumn.getItems().iterator(); k.hasNext();) {
                            Object cell = k.next();
                            ARUtil.cleanCell(cell);
                        }
                    }
                }
        
            }
    
        }
    }

    private static void cleanCell(Object cell) {
        if(cell instanceof TextCell) ARUtil.cleanTextCell((TextCell)cell);
        //if(cell instanceof MetaTextCell) ARUtil.cleanTextCell((TextCell)cell); // MetaTextCell extends TextCell
        if(cell instanceof ListCell) ARUtil.cleanListCell((ListCell)cell);
    }

    private static void cleanListCell(ListCell cell) {
        /* Donor commitment date some times is a ListCell of DateCell 
         * in such case it should not not execute the clean */
        boolean process = false;
        for (Object o : cell.getValue()) {
            if (o.getClass().equals(TextCell.class)){
                process = true;
            }
        }
        if (process){
            //Collection<TextCell> listCells = (Collection)cell.getValue();
            for (Cell tCell : cell.getValue()) {
                ARUtil.cleanTextCell((TextCell) tCell);
            }
        }
    }

    private static void cleanTextCell(TextCell cell) {
        if(cell.getFullTextVersion()!=null){
            cell.setValue(DataExchangeUtils.convertHTMLtoChar(cell.getFullTextVersion()));
        }else{
            cell.setValue("");
        }
    }
    
    public static double retrievePercentageFromCell ( MetaTextCell mtc ) throws Exception {
        MetaInfo<Double> mInfo  = mtc.getMetaInfo(ArConstants.PERCENTAGE);
        if ( mInfo != null && mInfo.getValue() > 0) { // ROTTEN: this "if" throws up on 0.0% cells masked as non-procented ones. See AmountCell::getAmount() for more comments of ways in which this is broken. AMP-13848
            Double percentage       = mInfo.getValue();
            return percentage;
        }
        if ( mInfo == null)
            throw new Exception("Percentage metainfo in MetaTextCell " + mtc + " is null");
        throw new Exception("Percentage found in MetaTextCell " + mtc + " is " + mInfo.getValue() );
    }
    
    public static double retrieveParentPercetage( Long ownerId, Cell splitterCell ) throws Exception {
        Column col              = splitterCell.getColumn();
        Iterator<TextCell> iter = col.getItems().iterator();
        while (iter.hasNext()) {
            TextCell    textCell        = iter.next();
            if ( textCell instanceof MetaTextCell ) {
                MetaTextCell metaTextCell   = (MetaTextCell) textCell;
                if ( splitterCell.getValue().equals(metaTextCell.getValue()) && ownerId.equals(metaTextCell.getOwnerId() ) ) {
                    Double percentage       = retrievePercentageFromCell(metaTextCell);
                    return percentage;
                }
            }
        }
        return 100.0 ;
    }
    
    public static List<SyntheticColumnsMeta> getSyntheticGeneratorList (HttpSession session) {
        String budgetTypeReport = (String) session.getAttribute(BudgetExportConstants.BUDGET_EXPORT_TYPE );
        if (budgetTypeReport != null ) {
            return BudgetExportConstants.syntheticColumns;
        }
        else 
            return
                ArConstants.syntheticColumns;
    }
    
//  public static GroupReportDataXLS instatiateGroupReportDataXLS (HttpSession session, HSSFWorkbook wb,HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
//          IntWrapper colId, Long ownerId, GroupReportData rd) {
//      return instatiateGroupReportDataXLS(session, wb, sheet, row, rowId, colId, ownerId, rd, false);
//  }

    public static GroupReportDataXLS instatiateGroupReportDataXLS (HttpSession session, HSSFWorkbook wb,HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
            IntWrapper colId, Long ownerId, GroupReportData rd, XLSExportType exportType) {
        if (session != null ) {
            String budgetTypeReport = (String) session.getAttribute(BudgetExportConstants.BUDGET_EXPORT_TYPE );
            if (budgetTypeReport != null ) {
                GroupReportDataXLS grd  = new BudgetGroupReportDataXLS(wb, sheet, row, rowId,
                        colId, null, rd);
                return grd;
            }
        }

        switch(exportType){
            case PLAIN_XLS_EXPORT:
                return new PlainGroupReportDataXLS(wb, sheet, row, rowId, colId, null, rd);
            
            case SIMPLE_XLS_EXPORT:
                return new GroupReportDataXLS(wb, sheet, row, rowId, colId, null, rd);
                
            case RICH_XLS_EXPORT:
                return new RichGroupReportDataXLS(wb, sheet, row, rowId, colId, null, rd);
                
            default:
                throw new RuntimeException("Unknown Excel export type: " + exportType);
        }
    }
    
    public static boolean hasCurrentUserAccessRight(AmpReports report) {
        HttpServletRequest request = TLSUtils.getRequest();
        if( report!=null ) {
            TeamMember tm = (TeamMember) request.getSession().getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
            if( tm==null || tm.getTeamId()==null ) { 
                if( report.getPublicReport() )
                    return true;
            } else {
                if( AmpARFilter.TEAM_MEMBER_ALL_MANAGEMENT_WORKSPACES.equals(tm.getTeamId()) || tm.getTeamId().equals(report.getOwnerId().getAmpTeam().getAmpTeamId()) )
                    return true;
            }
        }
        return false;
    }
    
    public static AmpColumns getColumnForView(String viewName) {
        String query = "select c from " + AmpColumns.class.getName() + " c WHERE c.extractorView = :ev ORDER BY c.columnId";
        List<AmpColumns> res = PersistenceManager.getSession().createQuery(query).setString("ev", viewName).list();
        if (res.isEmpty())
            return null;
        return res.get(0);
    }
}
