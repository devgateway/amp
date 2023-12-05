package org.dgfoundation.amp.nireports.testcases;

import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.testutils.ColumnComparator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * a model (sketch) of a GroupColumn
 * @author Dolghier Constantin
 *
 */
public class GroupColumnModel extends ColumnModel{
    List<ColumnModel> subColumns;
    
    private GroupColumnModel(String name, List<ColumnModel> subColumns)
    {
        super(name);
        this.subColumns = subColumns;
        Collections.sort(subColumns);
    }
    
    /**
     * generates a sketch of a GroupColumn defined by its name and (optionally) subcolumns
     * @param name
     * @param subColumns
     * @return
     */
    public static GroupColumnModel withSubColumns(String name, ColumnModel...subColumns)
    {
        return new GroupColumnModel(name, Arrays.asList(subColumns));
    }
    
    @Override
    public String matches(Column column)
    {
        if (this.getName().compareTo(column.getName()) != 0)
            return String.format("GroupColumnModel name mismatch: %s vs %s", column.getName(), this.getName());

        if (!(column instanceof GroupColumn))
            return String.format("GroupColumnModel %s not matched by a GroupColumn %s", this.getName(), column.getName());
        
        if (subColumns != null)
            return matchContents((GroupColumn) column);
        
        return null;
    }
    
    protected String matchContents(GroupColumn column)
    {
        Column[] sortedColumns = getAndSortColumns(column);
        
        if (sortedColumns.length != subColumns.size())
            return String.format("GroupColumnModel %s: nr of subcolumns is %d instead of %d", this.getName(), sortedColumns.length, subColumns.size());
        
        for(int i = 0; i < sortedColumns.length; i++)
        {
            Column subColumn = sortedColumns[i];
            ColumnModel subcolumnModel = subColumns.get(i);
            
            String compareResult = subcolumnModel.matches(subColumn);
            if (compareResult != null)
                return compareResult;
        }
        return null;
    }
    
    
    /**
     * returns list of children sorted by name
     * @param grd
     * @return
     */
    protected Column[] getAndSortColumns(GroupColumn grd)
    {
        Column[] res = new Column[grd.getItems().size()];
        for(int i = 0; i < res.length; i++)
        {
            res[i] = grd.getItems().get(i);     
        }
        Arrays.sort(res, 0, res.length, new ColumnComparator());
        return res;
    }
}

