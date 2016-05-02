package org.dgfoundation.amp.nireports.amp;

import java.util.Arrays;
import java.util.List;


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
import org.dgfoundation.amp.nireports.schema.TrivialMeasureBehaviour;

/**
 * the {@link Behavior} of a Tagged entity (like Classified Actual Expenditures -- expenditures vertically divided by an ACV)
 * @author acartaleanu
 *
 */
public class TaggedMeasureBehaviour extends TrivialMeasureBehaviour {
		
	protected final String totalColumnName;
	protected final MetaCategory tagCategory;
	protected final String pseudoColumnName;
	
	
	public TaggedMeasureBehaviour(String totalColumnName, MetaCategory tagCategory, String pseudocolumnName) {
		this.totalColumnName = totalColumnName;
		this.tagCategory = tagCategory;
		this.pseudoColumnName = pseudocolumnName;
	}
	
	@Override
	public List<VSplitStrategy> getSubMeasureHierarchies(NiReportsEngine context) {
		VSplitStrategy byTaggedCategory= VSplitStrategy.build(cell -> new ComparableValue<String>(getTagName(cell.getCell()), getTagName(cell.getCell())), pseudoColumnName);
		return Arrays.asList(byTaggedCategory);
	}

	public String getTagName(Cell cell) {
		MetaInfo metaInfo = cell.getMetaInfo().getMetaInfo(tagCategory.category);
		return metaInfo == null ? AmpReportsSchema.UNDEFINED_CATEGORY : metaInfo.v.toString();
	}
	
	@Override
	public ImmutablePair<String, ColumnContents> getTotalCells(NiReportsEngine context, NiReportedEntity<?> entity, ColumnContents fetchedContents) {
		if (totalColumnName == null)
			return super.getTotalCells(context, entity, fetchedContents);
		return new ImmutablePair<String, ColumnContents>(totalColumnName, fetchedContents);
	}
	
	
	@Override
	public boolean shouldDeleteLeafIfEmpty(CellColumn column) {
		return true;
	}
}
