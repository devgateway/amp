package org.dgfoundation.amp.mondrian;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dgfoundation.amp.newreports.NamedTypedEntity;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.TextCell;

public class ReportAreaForTests extends ReportAreaImpl {
	
	public ReportAreaForTests withOwner(NamedTypedEntity owner) {
		this.owner = owner;
		return this;
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
		if (contents.length % 2 != 0)
			throw new RuntimeException("please supply an even number of arguments, you supplied: " + contents.length);
		
		LinkedHashMap<ReportOutputColumn, ReportCell> cont = new LinkedHashMap<>();
		for(int i = 0; i < contents.length / 2; i++) {
			String columnName = contents[2 * i];
			String colCont = contents[2 * i + 1];
			cont.put(new ReportOutputColumn(columnName, null, columnName), new TextCell(colCont));
		}
		return withContents(cont);
	}
	
	// testcases functionality below: DO NOT USE IN PRODUCTION CODE (does incomplete comparisons, not production-ready
		
	/**
	 * stops at first error
	 * @param oth
	 * @return
	 */
	public String getDifferenceAgainst(ReportArea output) {
		if (!nullGuardEqual(this.owner, output.getOwner()))
			return String.format("different owners: %s vs correct %s", output.getOwner(), this.owner);
				
		String contentsRes = compareContents(output.getContents());
		if (contentsRes != null)
			return contentsRes;
		
		String childrenRes = compareChildren(output.getChildren());
		if (childrenRes != null)
			return childrenRes;
		
		return null;
	}
	
	public static boolean nullGuardEqual(Object obj, Object cor) {
		if (obj == null) return cor == null;
		if (cor == null) return obj == null;
		return obj.equals(cor);
	}
	
	public String compareContents(Map<ReportOutputColumn, ReportCell> outputContents) {
		
		if ((this.contents == null) ^ (outputContents == null))
			return String.format("different nullness of contents: %s vs correct %s", outputContents == null, this.getContents() == null);
		
		if (contents == null)
			return null; // both are null -> ok
		
		if (contents.size() != outputContents.size())
			return String.format("different contents sizes: %d vs correct %d", outputContents.size(), contents.size());
		
		SortedMap<ReportOutputColumn, ReportCell> sortedContents = new TreeMap<>(new SimplifiedROCComparator());
		sortedContents.putAll(outputContents);
		
		SortedMap<ReportOutputColumn, ReportCell> sortedCorrectContents = new TreeMap<>(new SimplifiedROCComparator());
		sortedCorrectContents.putAll(contents);
		
		for(ReportOutputColumn column:sortedContents.keySet()) {
			ReportCell outputCell = sortedContents.get(column);
			
			if (!sortedCorrectContents.containsKey(column))
				return String.format("unneeded entry in the output: %s", column);
			
			ReportCell corCell = sortedCorrectContents.get(column);
			if (!outputCell.displayedValue.equals(corCell.displayedValue))
				return String.format("incorrect output for the cell %s: %s instead of the correct %s", column, outputCell, corCell);
		}
		
		return null;
	}
	
	public String compareChildren(List<ReportArea> outputChildren) {
		if ((this.children == null) ^ (outputChildren == null))
			return String.format("different nullness of children: %s vs correct %s", outputChildren == null, this.getChildren() == null);
		
		if (this.children == null)
			return null;
		
		if (this.children.size() != outputChildren.size())
			return String.format("different children sizes: %s vs correct %d", outputChildren.size(), children.size());
		
		for(int i = 0; i < this.children.size(); i++) {
			ReportAreaForTests child = (ReportAreaForTests) this.children.get(i);
			ReportArea outputChild = outputChildren.get(i);
			
			String delta = child.getDifferenceAgainst(outputChild);
			if (delta != null)
				return delta;
		}
		return null;
	}
}
