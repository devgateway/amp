package org.dgfoundation.amp.nireports.testcases;

import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.cell.AmountCell;

import java.util.Arrays;

public abstract class ReportModel implements Comparable<ReportModel>{
    
    protected String name;
    protected String[] trailCells;
    
    protected ReportModel(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public ReportModel[] sort(ReportModel[] src)
    {
        if( src==null ) {
            return null;
        }
        ReportModel[] dest = new ReportModel[src.length];
        for(int i = 0; i < src.length; i++)
            dest[i] = src[i];
        Arrays.sort(dest);
        return dest;
    }
    
    protected String matches_trail_cells(ReportData<?> rd) {
        java.util.List<AmountCell> cells = rd.getTrailCells();
        if (cells == null)
            return String.format("RD %s has no trail cells, but should have %d", this.getName(), trailCells.length);
        
        if (cells.size() != trailCells.length)
            return String.format("RD %s has %d trail cells, but should have %d", this.getName(), cells.size(), trailCells.length);
            
        for(int i = 0; i < trailCells.length; i++)      
        {
            AmountCell cell = cells.get(i);
            
            String cellContents = cell == null ? null : cell.toString();            
            String correctCell = trailCells[i];

            String rs = compareCells(cellContents, correctCell, i);
            if (rs != null)
                return rs;
        }
        return null;
    }
    
    protected String compareCells(String cellContents, String correctCell, int i) {
        if (cellContents == null)
            cellContents = "<null>";
        
        if (correctCell == null)
            correctCell = "<null>";
        
        if (!correctCell.equals(cellContents))
            return String.format("CRD %s has trail cell %d equal %s instead of %s", this.getName(), i, cellContents, correctCell);
        
        return null;
    }

    @Override
    public int compareTo(ReportModel other) {
        int bla = this.getName().compareTo(other.getName()); 
        
        if (bla != 0)
            return bla;
        
        return this.getClass().getName().compareTo(other.getClass().getName());
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s", this.getClass().getSimpleName(), this.getName());
    }
}
