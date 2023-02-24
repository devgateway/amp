package org.dgfoundation.amp.nireports.runtime;

import java.util.List;
import java.util.stream.Collectors;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;

/**
 * a {@link ColumnVisitor} which performs the post-measures splitting of the report.
 * Uses {@link NiReportsSchema#getSubMeasureHierarchies(NiReportsEngine, CellColumn)} as a source of strategies
 * for each separate {@link CellColumn}
 * 
 * @author Dolghier Constantin
 *
 */
public class PostMeasureVHiersVisitor implements ColumnVisitor<Column> {

    final NiReportsEngine engine;
    
    public PostMeasureVHiersVisitor(NiReportsEngine engine) {
        this.engine = engine;
    }
        
    @Override
    public Column visit(CellColumn cc) {
        List<VSplitStrategy> subMeasureStrategy = engine.schema.getSubMeasureHierarchies(engine, cc);
        if (subMeasureStrategy == null || subMeasureStrategy.isEmpty())
            return cc;
        
        GroupColumn res = cc.verticallySplitByCategory(subMeasureStrategy.get(0), cc.parent);
        for(int i = 1; i < subMeasureStrategy.size(); i++)
            res = res.verticallySplitByCategory(subMeasureStrategy.get(i), cc.parent);
        return res;
    }

    @Override
    public GroupColumn visit(GroupColumn gc) {
        List<Column> children =  gc.getSubColumns().stream().map(z -> z.accept(this)).collect(Collectors.toList());
        gc.replaceChildren(children);
        return gc;
    }

}
