package org.dgfoundation.amp.nireports.runtime;

import java.util.List;
import java.util.stream.Collectors;
import org.dgfoundation.amp.nireports.NiReportsEngine;

public class PostMeasureVHiersVisitor implements ColumnVisitor<Column> {

	final NiReportsEngine engine;
	
	public PostMeasureVHiersVisitor(NiReportsEngine engine) {
		this.engine = engine;
	}
		
	@Override
	public Column visit(CellColumn cc) {
		List<VSplitStrategy> subMeasureStrategies = cc.behaviour.getSubMeasureHierarchies(engine);
		if (subMeasureStrategies == null || subMeasureStrategies.isEmpty())
			return cc;
		
		GroupColumn res = cc.verticallySplitByCategory(subMeasureStrategies.get(0), cc.parent);
		for(int i = 1; i < subMeasureStrategies.size(); i++)
			res = res.verticallySplitByCategory(subMeasureStrategies.get(i), cc.parent);
		return res;
	}

	@Override
	public GroupColumn visit(GroupColumn gc) {
		List<Column> children =  gc.getSubColumns().stream().map(z -> z.accept(this)).collect(Collectors.toList());
		gc.replaceChildren(children);
		return gc;
	}

}
