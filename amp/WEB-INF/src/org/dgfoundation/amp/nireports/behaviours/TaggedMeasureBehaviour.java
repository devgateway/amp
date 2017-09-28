package org.dgfoundation.amp.nireports.behaviours;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.apache.wicket.behavior.Behavior;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.meta.MetaInfo;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.VSplitStrategy;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;

/**
 * the {@link Behavior} of a Tagged entity (like Classified Actual Expenditures -- expenditures vertically divided by an ACV)
 * @author acartaleanu
 *
 */
public class TaggedMeasureBehaviour extends TrivialMeasureBehaviour {
        
    /**
     * the category to insert when the cell lacks the metadata 
     */
    public final static String UNDEFINED_CATEGORY = "Unassigned";

    protected final String totalColumnName;
    protected final String tagCategory;
    protected final String pseudoColumnName;
    protected final Supplier<String> totalSubcolumn;
    
    public TaggedMeasureBehaviour(String totalColumnName, String tagCategory, String pseudocolumnName) {
        this(totalColumnName, tagCategory, pseudocolumnName, null);
    }
    
    public TaggedMeasureBehaviour(String totalColumnName, String tagCategory, String pseudocolumnName, Supplier<String> totalSubcolumn) {
        this.totalColumnName = totalColumnName;
        this.tagCategory = tagCategory;
        this.pseudoColumnName = pseudocolumnName;
        this.totalSubcolumn = totalSubcolumn;
    }
    
    @Override
    public List<VSplitStrategy> getSubMeasureHierarchies(NiReportsEngine context) {
        VSplitStrategy byTaggedCategory = getSplittingStrategy(tagCategory, pseudoColumnName, totalSubcolumn);
        return Arrays.asList(byTaggedCategory);
    }

    public static String getTagName(Cell cell, String tagCategory) {
        MetaInfo metaInfo = cell.getMetaInfo().getMetaInfo(tagCategory);
        return metaInfo == null ? UNDEFINED_CATEGORY : metaInfo.v.toString();
    }
    
    @Override
    public ImmutablePair<String, ColumnContents> getTotalCells(NiReportsEngine context, NiReportedEntity<?> entity, ColumnContents fetchedContents) {
        if (totalColumnName == null)
            return super.getTotalCells(context, entity, fetchedContents);
        return new ImmutablePair<String, ColumnContents>(totalColumnName, fetchedContents);
    }
    
    /**
     * builds a splitting strategy which splits by 
     * @param tagCategory
     * @param pseudoColumnName
     * @return
     */
    public static VSplitStrategy getSplittingStrategy(String tagCategory, String pseudoColumnName, Supplier<String> subtotalColumn) {
        VSplitStrategy byTaggedCategory = VSplitStrategy.build(
                cell -> 
                    new ComparableValue<String>(
                        getTagName(cell.getCell(), tagCategory),
                        getTagName(cell.getCell(), tagCategory)), 
                pseudoColumnName,
                subtotalColumn == null ? null : () -> new ComparableValue<String>(subtotalColumn.get(), "~~~~"));
        return byTaggedCategory;
    }
    
    @Override
    public boolean shouldDeleteLeafIfEmpty(CellColumn column) {
        return true;
    }
    
}
