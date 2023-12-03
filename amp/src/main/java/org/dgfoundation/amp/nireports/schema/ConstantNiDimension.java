package org.dgfoundation.amp.nireports.schema;

import java.util.Collections;
import java.util.List;

/**
 * a NiDimension which uses a constant tabular bidimensional array as a source of data
 * @author Dolghier Constantin
 *
 */
public class ConstantNiDimension extends TabularSourcedNiDimension {
    
    public final List<List<Long>> data;
    
    public ConstantNiDimension(String name, int depth, List<List<Long>> data) {
        super(name, depth);
        this.data = Collections.unmodifiableList(data);
    }

    @Override
    protected List<List<Long>> getTabularData() {
        return data;
    }
    
}
