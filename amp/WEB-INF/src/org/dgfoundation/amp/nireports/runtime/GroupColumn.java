package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.NiUtils;

/**
 * a column with subcolumns. The column has no cells of its own.
 * An instance can be either mutable or immutable, depending on the used constructor. Once frozen, an instance cannot be unfrozen
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
	public GroupColumn(String name, List<Column> subColumns, GroupColumn parent) {
		super(name, parent);
		this.mutable = subColumns == null;
		this.subColumns = mutable ?
				new ArrayList<>() : Collections.unmodifiableList(new ArrayList<>(subColumns)); 
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
		subColumn.setParent(this);
	}
	
	public void freeze() {
		if (!mutable)
			return;
		subColumns = Collections.unmodifiableList(subColumns);
		mutable = true;
	}

	/**
	 * returns a read-only view of the subcolumns
	 * @return
	 */
	public List<Column> getSubColumns() {
		return Collections.unmodifiableList(this.subColumns);
	}
	
	@Override
	public Set<Long> getIds() {
		Set<Long> res = new HashSet<>();
		for(Column col:subColumns)
			res.addAll(col.getIds());
		return res;
	}

	@Override
	public void forEachCell(Consumer<NiCell> acceptor) {
		for(Column col:subColumns)
			col.forEachCell(acceptor);
	}

	@Override
	public GroupColumn verticallySplitByCategory(VSplitStrategy strategy) {
		List<Column> newSubs = new ArrayList<>();
		for(Column col:getSubColumns())
			newSubs.add(col.verticallySplitByCategory(strategy));
		GroupColumn res = new GroupColumn(name, newSubs, parent);
		return res;
	}

	@Override
	public String debugDigest(boolean withContents) {
		return String.format("[%s -> %s]", name, getSubColumns().stream().map(z -> z.debugDigest(withContents)).collect(Collectors.toList()));
	}
}
