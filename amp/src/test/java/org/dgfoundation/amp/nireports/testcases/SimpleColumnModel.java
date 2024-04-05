package org.dgfoundation.amp.nireports.testcases;

import org.dgfoundation.amp.ar.AmpReportGenerator;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.testutils.ReportTestingUtils;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * a model (sketch) of a CellColumn
 * @author Dolghier Constantin
 *
 */
public class SimpleColumnModel extends ColumnModel {
    
    Map<String, String> correctContents;
    protected boolean isPledge = false;
    
    private SimpleColumnModel(String name, Map<String, String> contents)
    {
        super(name);
        
        if (contents != null)
            this.correctContents = new HashMap<String, String>(contents);
    }
    
    public SimpleColumnModel setIsPledge(boolean isPledge){
        this.isPledge = isPledge;
        return this;
    }
    /**
     * generates a sketch of a CellColumn defined by its name and (optionally) contents. If contents is not specified, the a check on it is simply not run
     * @param name [mandatory non-null] name 
     * @param contents [may be null, where null is placeholded by #ReportTestingUtils.NULL_PLACEHOLDER] - a linear-list encoded Map<String, String> of contents for the column<br />The list encodes the map as [activityName_1 activityValue_1] [activityName_2 activityValue_2... ]. "ActivityValue" should be equal to << cellColumn.getByOwnerId(AmpActivity.getByName[activityName].getAmpActivityId()).toString() >>
     * @return
     */
    public static SimpleColumnModel withContents(String name, Object...contents)
    {
        boolean mustBeEmpty = false;
        if (contents != null && contents.length == 1)
        {
            if (contents[0].equals(ReportTestingUtils.NULL_PLACEHOLDER))
                contents = null;
            else
                if (contents[0].equals(ReportTestingUtils.MUST_BE_EMPTY))
                {
                    mustBeEmpty = true;
                    contents = null;
                }
        }
        
        if (contents != null && contents.length % 2 == 1)
            throw new RuntimeException("odd length not supported!");
        
        Map<String, String> con;
        
        if (contents == null)
            con = null;
        else
        {
            con = new HashMap<String, String>();
            for(int i = 0; i < contents.length / 2; i++)
            {
                String activityName = (String) (contents[2 * i]);
                String activityAmm = (String) (contents[2 * i + 1]);
                con.put(activityName, activityAmm);
            }
        }
        
        if (mustBeEmpty)
            con = new HashMap<String, String>();
        
        return new SimpleColumnModel(name, con);
    }
    
    @Override
    public String matches(Column column)
    {
        if (this.getName().compareTo(column.getName()) != 0)
            return String.format("SimpleColumnModel name mismatch: %s vs %s", column.getName(), this.getName());

        if (!(column instanceof CellColumn))
            return String.format("SimpleColumnModel %s not matched by a CellColumn %s", this.getName(), column.getName());
        
        if (correctContents != null)
            return matchContents((CellColumn) column);
        
        return null;
    }
    
    /**
     * checks whether the contents of the column, as encoded in {@link #correctContents}, matches the one of the presented column
     * @param column
     * @return
     */
    protected String matchContents(CellColumn column)
    {
        Set<Long> activityIds = column.getOwnerIds();
        if (activityIds.size() != correctContents.size())
            return String.format("SimpleColumnModel %s: size mismatch: %d vs %d", this.getName(), activityIds.size(), correctContents.size());
        
        for(Long activityId: activityIds)
        {
            String activityName = (this.isPledge || activityId > AmpReportGenerator.PLEDGES_IDS_START) ? 
                    PledgesEntityHelper.getPledgesById(activityId % AmpReportGenerator.PLEDGES_IDS_START).getEffectiveName() : 
                        ReportTestingUtils.getActivityName(activityId);////System.out.println("da da");
            String activityCorOutput = correctContents.get(activityName);
            if (activityCorOutput == null)
                return String.format("SimpleColumnModel %s, activity %s should not exist in the output", this.getName(), activityName);
            
            Cell cell = column.getByOwner(activityId);
            String reportOutput = cell.toString();
            if (reportOutput.compareTo(activityCorOutput) != 0)
                return String.format("SimpleColumnModel %s, activity %s has output <%s> instead of <%s>", this.getName(), activityName, reportOutput, activityCorOutput);
        }
        return null;
    }
}

