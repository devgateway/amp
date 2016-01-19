package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.dgfoundation.amp.ar.helper.ReportHeadingLayoutCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.ReportHeadingCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;

/**
 * a leaf column
 * @author Dolghier Constantin
 *
 */
public class CellColumn extends Column {
	
	final ColumnContents contents;
	final Behaviour behaviour;
		
	public CellColumn(String name, ColumnContents contents, GroupColumn parent, Behaviour behaviour) {
		super(name, parent);
		NiUtils.failIf(contents == null, "CellColumn should have a non-null contents");
		this.contents = contents;
		this.behaviour = behaviour;
	}

	@Override
	public void forEachCell(Consumer<NiCell> acceptor) {
		contents.data.values().forEach(list -> list.forEach(cell -> acceptor.accept(cell)));
	}

	@Override
	public GroupColumn verticallySplitByCategory(VSplitStrategy strategy) {
		SortedMap<ComparableValue<String>, List<NiCell>> values = new TreeMap<>();
		this.forEachCell(cell -> values.computeIfAbsent(strategy.categorize(cell), z -> new ArrayList<>()).add(cell));
		GroupColumn res = new GroupColumn(this.name, null, this.parent);
		values.forEach((key, cells) -> res.addColumn(new CellColumn(key.getValue(), new ColumnContents(cells), res, strategy.getBehaviour(key, this))));
		return res;
	}

	public ColumnContents getContents() {
		return contents;
	}

	public Behaviour getBehaviour() {
		return behaviour;
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
