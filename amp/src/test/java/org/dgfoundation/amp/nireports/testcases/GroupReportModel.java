package org.dgfoundation.amp.nireports.testcases;

import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.testutils.TestCaseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GroupReportModel extends ReportModel
{
    private ReportModel[] childModels;
    //private String[] trailCells;
    private List<String> positionDigest;
    protected boolean positionDigestIsTotal;
    
    private enum GRModelType {GROUP_REPORTS, COLUMN_REPORTS};
    
    GRModelType type;
    
    private GroupReportModel(String name, ReportModel[] models, GRModelType type)
    {
        super(name);
        this.childModels = sort(models);
        this.type = type;
    }
    
    public static GroupReportModel withGroupReports(String name, GroupReportModel...groupReportModels)
    {
        return new GroupReportModel(name, groupReportModels, GRModelType.GROUP_REPORTS);
    }
    
    public static GroupReportModel withColumnReports(String name, ColumnReportDataModel...columnReportDataModels)
    {
        return new GroupReportModel(name, columnReportDataModels, GRModelType.COLUMN_REPORTS);
    }
    
    public static GroupReportModel empty(String name)
    {
        return new GroupReportModel(name, new ColumnReportDataModel[0], GRModelType.COLUMN_REPORTS);
    }
    
    /**
     * equivalent to calling {@link #withPositionDigest(false, lines)}
     * @param lines
     * @return
     */
    public GroupReportModel withPositionDigest(String... lines)
    {
        return withPositionDigest(false, lines);
    }
    
    /**
     * sets the "correct" positionDigest for the GRD
     * @param total - whether the digest should be complete (including start positions). Only call with total = false for legacy code <br />
     * @param lines
     * @return
     */
    public GroupReportModel withPositionDigest(boolean total, String... lines)
    {
        this.positionDigest = new ArrayList<String>();
        if( lines!=null ) {     
            for(String lineDigest:lines)
                positionDigest.add(lineDigest);
            this.positionDigestIsTotal = total;
        }
        return this;
    }
    
    public GroupReportModel withTrailCells(String... trailCells)
    {
        this.trailCells = trailCells;
        return this;
    }
    
    public String matches(GroupReportData grd)
    {
        if (this.getName().compareTo(grd.getName()) != 0)
            return String.format("name mismatch: %s vs %s", grd.getName(), this.getName());

        if (trailCells != null)
        {
            String trailCellComparisonResult = matches_trail_cells(grd);
            if (trailCellComparisonResult != null)
                return trailCellComparisonResult;
        }
        
        if (positionDigest != null)
        {
            String positionDigestComparisonResult = matches_position_digest(grd);
            if (positionDigestComparisonResult != null)
                return positionDigestComparisonResult;
        }
        
        if (childModels != null)
        {
            // comparison is relevant iff childModels is specified
            if (grd.getItems().size() != childModels.length)
                return String.format("GRD %s has %d children in lieu of %d", this.getName(), grd.getItems().size(), childModels.length);
        }

        switch(type)
        {
            case GROUP_REPORTS:
                return matches_grd(grd);
                
            case COLUMN_REPORTS:
                return matches_crd(grd);
                
            default:
                throw new RuntimeException("unknown GRModelType: " + type);
        }
    }
    
    protected String matches_position_digest(GroupReportData grd)
    {
        List<String> digest = grd.digestReportHeadingData(this.positionDigestIsTotal);
        if (digest.size() != positionDigest.size())
            return String.format("GRD %s has %d rows in report-headings, but should have %d", this.getName(), digest.size(), positionDigest.size());
        
        for(int i = 0; i < digest.size(); i++)
        {
            String lineDigest = digest.get(i);
            String corLineDigest = positionDigest.get(i);
            if (!lineDigest.equals(corLineDigest))
                return String.format("GRD %s has the report-headings-line-digest #%d different:\n\t%s instead of \n\t%s", this.getName(), i, lineDigest, corLineDigest);
        }
        return null;
    }
    
    /**
     * grd should have children of type GroupReportData
     * @param grd
     * @return
     */
    protected String matches_grd(GroupReportData grd)
    {       
        try
        {           
            GroupReportData[] children = getAndSortGRDChildren(grd);
            
            for(int i = 0; i < children.length; i++)
            {
                GroupReportModel childModel = (GroupReportModel) childModels[i];
                String compareResult = childModel.matches(children[i]);
                if (compareResult != null)
                    return compareResult;
            }
            return null;

        }
        catch(TestCaseException e)
        {
            return e.getMessage();
        }
    }
    
    /**
     * grd should have children of type GroupReportData
     * @param grd
     * @return
     */
    protected String matches_crd(GroupReportData grd)
    {       
        try
        {           
            ColumnReportData[] children = getAndSortCRDChildren(grd);
            
            for(int i = 0; i < children.length; i++)
            {
                ColumnReportDataModel childModel = (ColumnReportDataModel) childModels[i];
                String compareResult = childModel.matches(children[i]);
                if (compareResult != null)
                    return compareResult;
            }
            return null;

        }
        catch(TestCaseException e)
        {
            return e.getMessage();
        }
    }
    
    /**
     * returns list of children sorted by name
     * @param grd
     * @return
     */
    protected GroupReportData[] getAndSortGRDChildren(GroupReportData grd)
    {
        GroupReportData[] res = new GroupReportData[grd.getItems().size()];
        for(int i = 0; i < res.length; i++)
        {
            if (!(grd.getItems().get(i) instanceof GroupReportData))
                throw new TestCaseException(String.format("GroupReportData %s child #%d !instanceof GroupReportData", grd.getName(), i));
            
            res[i] = (GroupReportData) grd.getItems().get(i);           
        }
        Arrays.sort(res, 0, res.length, new ReportDataComparator());
        return res;
    }
    
    /**
     * returns list of children sorted by name
     * @param grd
     * @return
     */
    protected ColumnReportData[] getAndSortCRDChildren(GroupReportData grd)
    {
        ColumnReportData[] res = new ColumnReportData[grd.getItems().size()];
        for(int i = 0; i < res.length; i++)
        {
            if (!(grd.getItems().get(i) instanceof ColumnReportData))
                throw new TestCaseException(String.format("GroupReportData %s child #%d !instanceof ColumnReportData", grd.getName(), i));
            
            res[i] = (ColumnReportData) grd.getItems().get(i);          
        }
        Arrays.sort(res, 0, res.length, new ReportDataComparator());
        return res;
    }
    
    public class ReportDataComparator implements Comparator<ReportData>
    {
        public int compare(ReportData a, ReportData b)
        {
            return a.getName().compareTo(b.getName());
        }
    }
    
}
