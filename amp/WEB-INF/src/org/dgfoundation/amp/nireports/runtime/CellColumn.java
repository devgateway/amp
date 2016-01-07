package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.schema.Behaviour;

/**
 * a leaf column
 * @author Dolghier Constantin
 *
 */
public class CellColumn<K extends Cell> extends Column {
	
	final ColumnContents<K> contents;
	final Behaviour behaviour;
	
	public CellColumn(String name, ColumnContents<K> contents, GroupColumn parent, Behaviour behaviour) {
		super(name, parent);
		NiUtils.failIf(contents == null, "CellColumn should have a non-null contents");
		this.contents = contents;
		this.behaviour = behaviour;
	}

	@Override
	public Set<Long> getIds() {
		return contents.data.keySet();
	}

	@Override
	public void forEachCell(Consumer<Cell> acceptor) {
		contents.data.values().forEach(list -> list.forEach(cell -> acceptor.accept(cell)));
	}

	@Override
	public GroupColumn verticallySplitByCategory(Function<Cell, ComparableValue<String>> categorizer) {
		SortedMap<ComparableValue<String>, List<K>> values = new TreeMap<>();
		this.forEachCell(cell -> values.computeIfAbsent(categorizer.apply(cell), z -> new ArrayList<>()).add((K) cell));
		GroupColumn res = new GroupColumn(this.name, null, this.parent);
		values.forEach((key, cells) -> res.addColumn(new CellColumn<>(key.getValue(), new ColumnContents<>(cells), res, behaviour)));
		return res;
	}

	@Override
	public String debugDigest(boolean withContents) {
		String shortDigest = String.format("<%s, %d cells, behaviour %s>", this.name, this.contents.countCells(), behaviour.getDebugDigest());
		if (withContents)
			return String.format("%s with contents: %s", shortDigest, contents.toString());
		else
			return shortDigest;
	}
}
