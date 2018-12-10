package org.dgfoundation.amp.newreports;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Stack;
import java.util.function.Function;

import junit.framework.TestCase;

import org.dgfoundation.amp.algo.AmpCollections;

public class ReportAreaForTests extends ReportAreaImpl {
            
    public ReportAreaForTests withOwner(AreaOwner owner) {
        this.owner = owner;
        return this;
    }
    
    public ReportAreaForTests() {}
    
    public ReportAreaForTests(AreaOwner owner, String...contents) {
        this.owner = owner;
        this.contents = buildContents(contents);
    }
    
    public ReportAreaForTests withChildren(ReportArea...children) {
        this.children = Arrays.asList(children);
        return this;
    }

    public ReportAreaForTests withContents(Map<ReportOutputColumn, ReportCell> contents) {
        this.contents = contents;
        return this;
    }
    
    public ReportAreaForTests withContents(String...contents) {
        return withContents(buildContents(contents));
    }
    
    public LinkedHashMap<ReportOutputColumn, ReportCell> buildContents(String... contents) {
        if (contents.length % 2 != 0)
            throw new RuntimeException("please supply an even number of arguments, you supplied: " + contents.length);
        
        LinkedHashMap<ReportOutputColumn, ReportCell> cont = new LinkedHashMap<>();
        for(int i = 0; i < contents.length / 2; i++) {
            String columnName = contents[2 * i];
            String colCont = contents[2 * i + 1];
            ReportOutputColumn roc = new ReportOutputColumn(columnName, null, columnName, null, null);
            if (cont.containsKey(roc))
                throw new RuntimeException("you defined the same column in output twice: " + roc.getHierarchicalName());
            cont.put(roc, new TextCell(colCont));
        }
        return cont;
    }
    
    // testcases functionality below: DO NOT USE IN PRODUCTION CODE (does incomplete comparisons, not production-ready      
    public static Stack<ReportAreaForTests> deltaStack = new Stack<>();
    
    /**
     * stops at first error
     * @param oth
     * @return
     */
    public String getDifferenceAgainst(ReportArea output) {
        deltaStack.add(this);
//      System.err.format("START comparing %s against %s\n", this.owner, output.getOwner());
        
        if (!ownerOk(this.owner, output.getOwner()))
            report_error(String.format("different owners: %s vs correct %s", output.getOwner(), this.owner));
                
        String contentsRes = compareContents(output.getContents());
        if (contentsRes != null)
            return contentsRes;
        
        String childrenRes = compareChildren(output.getChildren());
        if (childrenRes != null)
            return childrenRes;
        
//      System.err.format("END comparing %s against %s\n", this.owner, output.getOwner());
        deltaStack.pop();
        
        return null;
    }
    
    public static boolean ownerOk(AreaOwner cor, AreaOwner out) {
        if ((out != null && out.id > 0) && (cor == null))
            return true; // if out is leaf while cor is null, we are ok
        if (out == null) return cor == null;
        if (cor == null) return out == null;
        
        if (cor.id != -1 && cor.id != out.id)
            return false;
        
        return cor.columnName.equals(out.columnName) && cor.debugString.equals(out.debugString);
//      return cor.equals(out);
    }
    
    public static boolean nullGuardEqual(Object obj, Object cor) {
        if (obj == null) return cor == null;
        if (cor == null) return obj == null;
        return obj.equals(cor);
    }
    
    public String compareContents(Map<ReportOutputColumn, ReportCell> outputContents) {
        
        if ((this.contents == null) ^ (outputContents == null))
            report_error(String.format("different nullness of contents in %s: %s vs correct %s", this.getOwner(), outputContents == null, this.getContents() == null));
        
        if (contents == null)
            return null; // both are null -> ok
        
        if (contents.size() != outputContents.size())
            report_error(String.format("different contents sizes in %s: %d vs correct %d", this.getOwner(), outputContents.size(), contents.size()));
        
        SortedMap<String, ReportCell> sortedContents = (SortedMap<String, ReportCell>) AmpCollections.remap(outputContents, col -> SimplifiedROCComparator.generateDisplayedName(col), Function.identity(), true);
        //Map<String, ReportCell> sortedContents = outputContents;
//      outputContents.forEach((col, val) -> 
//      sortedContents.putAll(outputContents);
        
        SortedMap<String, ReportCell> sortedCorrectContents = (SortedMap<String, ReportCell>) AmpCollections.remap(contents, col -> SimplifiedROCComparator.generateDisplayedName(col), Function.identity(), true);
        //Map<String, ReportCell> sortedCorrectContents = contents;
//      sortedCorrectContents.putAll(contents);
        
        for(String column:sortedContents.keySet()) {
            ReportCell outCell = sortedContents.get(column);
            
            if (!sortedCorrectContents.containsKey(column))
                report_error(String.format("unneeded entry %s in the output of %s: %s", outCell, this.getOwner(), column));
            
            ReportCell corCell = sortedCorrectContents.get(column);
            String deltaRes = equalCells(column, outCell, corCell);
            if (deltaRes != null)
                return deltaRes;
        }
        
        return null;
    }

    public String equalCells(String column, ReportCell outCell, ReportCell corCell) {
        if (outCell != null && outCell.displayedValue.isEmpty())
            outCell = null;

        if (corCell != null && corCell.displayedValue.isEmpty())
            corCell = null;
        
//      if (corCell != null && corCell.displayedValue.endsWith(" Totals"))
//          return null; //TODO-NiReports: to change later

        if (outCell == null && corCell == null)
            return null;
        
        if (outCell == null || corCell == null)
            return report_error(String.format("col %s of %s: different nullness: out %s vs cor %s", column, this.owner, outCell, corCell));
        
        TestCase.assertEquals(corCell.displayedValue, outCell.displayedValue);
//      if (!outCell.displayedValue.equals(corCell.displayedValue))
//          return report_error(String.format("col %s of %s: incorrect output: %s instead of the correct %s", column, this.owner, outCell, corCell));

        return null;
    }
    
    public String compareChildren(List<ReportArea> outputChildren) {
        if ((this.children == null) ^ (outputChildren == null))
            return report_error(String.format("different nullness of children of %s: %s vs correct %s", this.owner, outputChildren == null, this.getChildren() == null));
        
        if (this.children == null)
            return null;
        
        if (this.children.size() != outputChildren.size())
            return report_error(String.format("different children sizes of %s: %s vs correct %d", this.owner, outputChildren.size(), children.size()));
        
        for(int i = 0; i < this.children.size(); i++) {
            ReportAreaForTests child = (ReportAreaForTests) this.children.get(i);
            ReportArea outputChild = outputChildren.get(i);
            
            String delta = child.getDifferenceAgainst(outputChild);
            if (delta != null)
                return report_error(delta);
        }
        return null;
    }
    
    protected String report_error(String str) {
        String stackStr = AmpCollections.relist(deltaStack, ra -> ra.getOwner()).toString();
        throw new RuntimeException("for stack " + stackStr + ", " + str);
    }
}
