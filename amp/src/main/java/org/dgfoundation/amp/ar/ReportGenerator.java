/**
 * ReportGenerator.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.filtercacher.FastFilterCacher;
import org.dgfoundation.amp.ar.filtercacher.FilterCacher;
import org.dgfoundation.amp.ar.filtercacher.NopFilterCacher;
import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 23, 2006
 * The report generator class. This class gathers data from the db, performs
 * processing and creates the structures that need to be displayed.
 *
 */
public abstract class ReportGenerator {

    protected GroupColumn rawColumns;
    protected Map<String, CellColumn> rawColumnsByName = new HashMap<String,CellColumn>();
    
    protected GroupReportData report;
    
    protected AmpARFilter filter;
    protected boolean pledgereport = false;
    
    protected final AmpReports reportMetadata;
    protected FilterCacher filterCacher;
    protected java.util.Map<PropertyDescription, ColumnValuesCacher> columnCachers;
    
    protected static Logger logger = Logger.getLogger(ReportGenerator.class);
    
    /**
     * retrieves data from the DB (using views) and creates the basic structures
     */
    protected abstract void retrieveData();
    
    /**
     * prepares the extracted data for display
     */
    protected abstract void prepareData();
    
    protected ReportGenerator(AmpReports reportMetadata) {
        this.reportMetadata = reportMetadata;
    }
    
    /**
     * true = use FastFilterCachier, false = use NopFilterCacher
     */
    public final static boolean USE_FILTER_CACHING = true;
    
    /**
     * @deprecated
     * hacky workaround for codegen
     * @return
     */
    protected String getBaseEntityNameById(long actId){
        return this.pledgereport || actId > AmpReportGenerator.PLEDGES_IDS_START ? 
                PledgesEntityHelper.getPledgesById(actId % AmpReportGenerator.PLEDGES_IDS_START).getEffectiveName() 
                : ActivityUtil.getActivityName(actId);
    }
    
    /**
     * retrieve the categories that will serve to create subsets (subColumnS) of
     * Categorizable CellS
     * @param columnName the name of the column, to customize the returned category list
     * in order to suit that particular column
     * @return the list of category names
     */
    protected abstract List getColumnSubCategories(String columnName);
    
    /**
     * the main method of this class. it generates a displayable report object
     */
    public void generate() {
                
        //GroupReportData.totalMergedCells = 0;
        long startTS = System.currentTimeMillis();
        this.filterCacher = USE_FILTER_CACHING ? new FastFilterCacher(filter) : new NopFilterCacher(filter);
        try {
            columnCachers = new java.util.HashMap<PropertyDescription, ColumnValuesCacher>();
            retrieveData();
        }
        finally {
            if (this.filterCacher != null)
                this.filterCacher.closeConnection();
            this.filterCacher = null;
        }
        long retrTS = System.currentTimeMillis();
        
//      String jopa = this.rawColumns.prettyPrint();
        prepareData();
//      String popa = this.report.prettyPrint();
        //logger.error("the report is " + report.prettyPrint());
//      if (report.getName().startsWith("AMP-22234"))
//          System.err.println("report is, in code, " + describeReportInCode(report, 0, true));
        long endTS = System.currentTimeMillis();
        columnCachers.clear(); // cleanup memory used for holding columns
        logger.info("Report "+getReport().getName()+" generated in "+(endTS-startTS)/1000.0+" seconds. Data retrieval completed in "+(retrTS-startTS)/1000.0+" seconds");
        //logger.info("\tTotal merged cells while generating report: " + GroupReportData.totalMergedCells);
    }
    
    

    /**
     * @return Returns the report.
     */
    public GroupReportData getReport() {
        return report;
    }

    /**
     * @param report The report to set.
     */
    public void setReport(GroupReportData report) {
        this.report = report;
    }

    /**
     * @return Returns the filter.
     */
    public AmpARFilter getFilter() {
        return filter;
    }

    /**
     * @param filter The filter to set.
     */
    public void setFilter(AmpARFilter filter) {
        this.filter = filter;
    }

    /**
     * @return Returns the reportMetadata.
     */
    public AmpReports getReportMetadata() {
        return reportMetadata;
    }

    public FilterCacher getFilterCacher()
    {
        return this.filterCacher;
    }
    
    public java.util.Map<PropertyDescription, ColumnValuesCacher> getColumnCachers()
    {
        return columnCachers;
    }
    
    //  --------- CODE BELOW USED FOR GENERATING TESTCASES -----------
    
    /**
     * returns true iff this is a GroupReportData containing other GroupReportDatas
     * @param report
     * @return
     */
    public static boolean containsGroupReports(GroupReportData report)
    {
        if (report.getItems().isEmpty())
            return false;
        return report.getItems().get(0) instanceof GroupReportData;
    }
    
    /**
     * what the name says
     * @param trailCells
     * @param depth
     * @return
     */
    public static String buildTrailCellsDescription(List<AmountCell> trailCells, int depth)
    {
        StringBuffer buf = new StringBuffer(prefixString(depth) + ".withTrailCells(");
        for(int i = 0; i < trailCells.size(); i++)
        {
            if (i > 0)
                buf.append(", ");
            AmountCell tc = trailCells.get(i);
            if (tc == null)
                buf.append("null");
            else
                buf.append(String.format("\"%s\"", tc.toString()));
        }
        buf.append(")");
        return buf.toString();
    }
    
    /**
     * generates code which generates a description of a GRD's subelements
     * @param items
     * @param depth
     * @return
     */
    public String describeElements(List<ReportData> items, int depth)
    {
        StringBuffer res = new StringBuffer();
        for(int i = 0; i < items.size(); i++)
        {
            ReportData item = items.get(i);
            //res.append(prefixString(depth));
            if (item instanceof GroupReportData)
                res.append(describeReportInCode((GroupReportData)item, depth));
            else
                res.append(describeCRDInCode((ColumnReportData) item, depth));
            if (i < items.size() - 1)
                res.append(",\n");
//          if (i > 0)
//              buf.append("")
        }
        return res.toString();
    }
    
    /**
     * returns the prefix of a string shifted right
     * @param depth
     * @return
     */
    public static String prefixString(int depth)
    {
        String res = "";
        for(int i = 0; i < depth; i++)
            res = res + "\t";
        return res;
    }
    
    /**
     * returns the code generating the description of a report-heading-layout 
     * @param headingDigests
     * @param depth
     * @return
     */
    public static String generateLayoutString(List<String> headingDigests, int depth)
    {
        StringBuffer res = new StringBuffer(String.format("%s.withPositionDigest(true,\n", prefixString(depth)));
        
        for(int i = 0; i < headingDigests.size(); i++)
        {
            String line = headingDigests.get(i);
            res.append(String.format("%s\"%s\"", prefixString(depth + 1), line));
            if (i != headingDigests.size() - 1)
            {
                res.append(",");
                res.append("\n");
            }
        }
        res.append(")");
        return res.toString();
    }
    
    /**
     * generates the code describing a report <b>without report-headings</b>
     * @param report
     * @param depth
     * @return
     */
    public String describeReportInCode(GroupReportData report, int depth)
    {
        return describeReportInCode(report, depth, false);
    }
    
    /**
     * generates the code describing a report with/without report-headings
     * @param report
     * @param depth
     * @param describeLayout
     * @return
     */
    public String describeReportInCode(GroupReportData report, int depth, boolean describeLayout)
    {
        String elements = describeElements(report.getItems(), depth + 1);
        ////System.out.println("elements: " + describeElements(report.getItems(), depth + 1));
        String withTrailCells = buildTrailCellsDescription(report.getTrailCells(), depth);
        String functionName = containsGroupReports(report) ? "withGroupReports" : "withColumnReports";
        String describeLayoutString = describeLayout ? String.format("\n%s%s", prefixString(depth), generateLayoutString(report.digestReportHeadingData(true), depth)) : "";
        return String.format("%sGroupReportModel.%s(\"%s\",\n%s)\n%s%s", prefixString(depth) ,functionName, report.getName(), elements, withTrailCells, describeLayoutString);
    }
    
    public String describeColumns(List<Column> columns, int depth)
    {
        StringBuffer res = new StringBuffer();
        for(int i = 0; i < columns.size(); i++)
        {
            Column col = columns.get(i);
            if (col instanceof CellColumn)
                res.append(describeColumn((CellColumn) col, depth));
            else
                res.append(describeGroupColumn((GroupColumn) col, depth));
            if (i < columns.size() - 1)
            {
                res.append(", ");
                res.append("\n");
            }
        }
        return res.toString();
    }
    
    public String describeColumn(CellColumn col, int depth)
    {
        try
        {
            Set<Long> ownerIds = col.getAllOwnerIds();
            StringBuffer values = new StringBuffer();
            int nr = 0;
            for(Long actId:ownerIds)
            {
                Cell value = col.getByOwner(actId);
                if (nr > 0)
                    values.append(", ");
                String actName = getBaseEntityNameById(actId);
                String cellValue = value.toString();
                values.append(String.format("\"%s\", \"%s\"", actName, cellValue));
                nr ++;
            }
            if (ownerIds.isEmpty())
                values.append("MUST_BE_EMPTY");
            StringBuffer res = new StringBuffer(String.format("%sSimpleColumnModel.withContents(\"%s\", %s).setIsPledge(%b)", prefixString(depth), col.getName(), values.toString(), this.pledgereport));
            return res.toString();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public String describeGroupColumn(GroupColumn col, int depth)
    {
        String columnsList = describeColumns(col.getItems(), depth + 1); 
        return String.format("%sGroupColumnModel.withSubColumns(\"%s\",\n%s)", prefixString(depth), col.getName(), columnsList);
    }
    
    public String describeCRDInCode(ColumnReportData crd, int depth)
    {
        String elements = describeColumns(crd.getItems(), depth + 1);
        String withTrailCells = buildTrailCellsDescription(crd.getTrailCells(), depth);
        return String.format("%sColumnReportDataModel.withColumns(\"%s\",\n%s)\n%s", prefixString(depth), crd.getName(), elements, withTrailCells);
        
    }   
}
