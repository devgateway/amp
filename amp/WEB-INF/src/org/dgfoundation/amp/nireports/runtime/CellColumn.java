package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;

import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.ReportHeadingCell;
import org.dgfoundation.amp.nireports.output.NiOutCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;

/**
 * a leaf column
 * @author Dolghier Constantin
 *
 */
public class CellColumn extends Column {
	
	final ColumnContents contents;
	final Behaviour<?> behaviour;
	final NiReportedEntity<?> entity;
		
	public CellColumn(String name, ColumnContents contents, GroupColumn parent, NiReportedEntity<?> entity) {
		this(name, contents, parent, entity, entity.getBehaviour());
	}
	
	public CellColumn(String name, ColumnContents contents, GroupColumn parent, NiReportedEntity<?> entity, Behaviour<?> behaviour) {
		super(name, parent);
		NiUtils.failIf(contents == null, "CellColumn should have a non-null contents");
		this.contents = contents;
		this.behaviour = behaviour;
		this.entity = entity;
	}

	@Override
	public void forEachCell(Consumer<NiCell> acceptor) {
		contents.data.values().forEach(list -> list.forEach(cell -> acceptor.accept(cell)));
	}

	@Override
	public GroupColumn verticallySplitByCategory(VSplitStrategy strategy, GroupColumn newParent) {
		SortedMap<ComparableValue<String>, List<NiCell>> values = new TreeMap<>();
		this.forEachCell(cell -> values.computeIfAbsent(strategy.categorize(cell), z -> new ArrayList<>()).add(cell));
		GroupColumn res = new GroupColumn(this.name, null, newParent);
		List<ComparableValue<String>> subColumnNames = strategy.getSubcolumnsNames(values.keySet());
		subColumnNames.forEach(key -> res.addColumn(new CellColumn(key.getValue(), new ColumnContents(Optional.ofNullable(values.get(key)).orElse(Collections.emptyList())), res, this.entity, strategy.getBehaviour(key, this))));
		return res;
	}

	public ColumnContents getContents() {
		return contents;
	}

	public Behaviour<NiOutCell> getBehaviour() {
		return (Behaviour<NiOutCell>) behaviour; // code ugly as sin because CellColumn has not been parametrized
	}
	
	@Override
	public List<CellColumn> getLeafColumns() {
		return Arrays.asList(this);
	}

	@Override
	public String debugDigest(boolean withContents) {
		String shortDigest = String.format("%s(%d, %s)", this.name, this.contents.countCells(), behaviour == null ? "(no behaviour)" : behaviour.getDebugDigest());
		if (withContents)
			return String.format("%s with contents: %s", shortDigest, contents.toString());
		else
			return shortDigest;
	}

	@Override
	public void calculatePositionInHeadingLayout(int totalRowSpan, int startingDepth, int startingColumn) {
		this.reportHeaderCell = new ReportHeadingCell(startingDepth, totalRowSpan, totalRowSpan, startingColumn, this.getWidth(), this.name);
	}

	@Override
	public int calculateTotalRowSpan() {
		return 1;
	}

	@Override
	public int getWidth() {
		return 1;
	}
	
	@Override
	public List<Column> getChildrenStartingAtDepth(int depth) {
		if (reportHeaderCell.getStartRow() == depth)
			return Arrays.asList(this);
		else
			return Collections.emptyList();
	}
}
