package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.ReportHeadingCell;

/**
 * a column with subcolumns. The column has no cells of its own.
 * An instance can be either mutable or immutable, depending on the used constructor. Once frozen, an instance cannot be unfrozen
 * 
 * @author Dolghier Constantin
 *
 */
public class GroupColumn extends Column {
	protected List<Column> subColumns;
	protected boolean mutable;	
	
	/**
	 * constructs a mutable or immutable instance of the class, depending on whether a list of subcolumns has been supplied
	 * @param name
	 * @param subColumns if null, then instance is mutable, else immutable
	 * @param parent
	 */
	public GroupColumn(String name, List<Column> subColumns, GroupColumn parent, NiColSplitCell splitCell) {
		super(name, parent, splitCell);
		if (subColumns != null) {
			subColumns.forEach(z -> NiUtils.failIf(z.getParent() != this, String.format("trying to add a foreing child %s to %s", z.getHierName(), this.getHierName())));
		}
		this.mutable = true;
		this.subColumns = subColumns == null ?
				new ArrayList<>() : new ArrayList<>(subColumns); 
	}
	
	/** adds a column, if it is not null */
	public void maybeAddColumn(Column subColumn) {
		if (subColumn != null)
			addColumn(subColumn);
	}
	
	/**
	 * adds a subcolumns to this instance, <strong>if it has not been frozen</strong>. <br />
	 * Will crash if the instance is frozen
	 * 
	 * @param subColumn
	 */
	public void addColumn(Column subColumn) {
		NiUtils.failIf(!mutable, () -> String.format("%s is immutable, you are not allowed to add columns here"));
		subColumns.add(subColumn);
		NiUtils.failIf(subColumn.getParent() != this, () -> String.format("you attempted to add subcolumn <%s> to GroupColumn<%s> with a different parent", subColumn.debugDigest(false), this.debugDigest(false)));
	}

	/**
	 * returns a read-only view of the subcolumns
	 * @return
	 */
	public List<Column> getSubColumns() {
		return Collections.unmodifiableList(this.subColumns);
	}
	
	public void replaceChildren(List<Column> children) {
		if (!mutable)
			throw new RuntimeException("not allowed to mutate a frozen instance");
		this.subColumns.clear();
		this.subColumns.addAll(children);
	}
	
	/**
	 * finds a direct child by name
	 * @param childName
	 * @return null if nothing found
	 */
	public Column findChildByName(String childName) {
		for(Column subCol:subColumns)
			if (subCol.name.equals(childName))
				return subCol;
		return null;
	}

	@Override
	public void forEachCell(Consumer<NiCell> acceptor) {
		for(Column col:subColumns)
			col.forEachCell(acceptor);
	}

	@Override
	public GroupColumn verticallySplitByCategory(VSplitStrategy strategy, GroupColumn newParent) {
		GroupColumn res = new GroupColumn(name, null, newParent, splitCell);
		for(Column col:getSubColumns())
			res.addColumn(col.verticallySplitByCategory(strategy, res));
		
		return res;
	}

	@Override
	public List<CellColumn> getLeafColumns() {
		return subColumns.stream().flatMap(z -> z.getLeafColumns().stream()).collect(Collectors.toList());
	}

	@Override
	public String debugDigest(boolean withContents) {
		return String.format("[%s -> %s]", name, getSubColumns().stream().map(z -> z.debugDigest(withContents)).collect(Collectors.toList()));
	}

	public void calculateHeaders() {
		calculatePositionInHeadingLayout(calculateTotalRowSpan(), 0, 0);
	}
	
	@Override
	public void calculatePositionInHeadingLayout(int totalRowSpan, int startingDepth, int startColumn) {
		int selfRowSpan = /*this.isTotalColumn()*/ this.parent == null ? totalRowSpan - calculateChildrenMaxRowSpan() : 1;
		//selfRowSpan = getRowSpanInHeading_internal();
		if (selfRowSpan <= 0)
			throw new RuntimeException("selfRowSpan should be >= 1!");
		this.reportHeaderCell = new ReportHeadingCell(startingDepth, totalRowSpan, selfRowSpan, startColumn, this.getWidth(), this.name);
		int startColumnSum = 0;
		for(Column item:this.getSubColumns()) {
			item.calculatePositionInHeadingLayout(totalRowSpan - this.reportHeaderCell.getRowSpan(), startingDepth + this.reportHeaderCell.getRowSpan(), startColumn + startColumnSum);
			startColumnSum += item.getWidth();
		}
	}
	
	@Override
	public int getWidth() {
		int ret = 0;
		for(Column column:getSubColumns()){			
			ret += column.getWidth();
		}
		return Math.max(1, ret); // at least the column title
	}

    public int calculateChildrenMaxRowSpan() {    	
		int maxColSpan = 0;
		for(Column c:this.getSubColumns()) {
			maxColSpan = Math.max(maxColSpan, c.calculateTotalRowSpan());
		}
		return maxColSpan;
    }

	@Override
	public int calculateTotalRowSpan() {
		return calculateChildrenMaxRowSpan() + 1; // self + max of children
	}
	
	@Override
	public List<Column> getChildrenStartingAtDepth(int depth) {
		if (reportHeaderCell.getStartRow() == depth)
			return Arrays.asList(this);
		
		if (reportHeaderCell.getStartRow() > depth)
			return Collections.emptyList();
		
		return subColumns.stream().flatMap(z -> z.getChildrenStartingAtDepth(depth).stream()).collect(Collectors.toList());
	}

	@Override
	public <K> K accept(ColumnVisitor<K> cv) {
		return cv.visit(this);
	}
}
