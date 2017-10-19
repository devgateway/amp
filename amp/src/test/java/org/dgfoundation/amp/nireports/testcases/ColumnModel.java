package org.dgfoundation.amp.nireports.testcases;

import org.dgfoundation.amp.ar.Column;

public abstract class ColumnModel extends ReportModel {
    protected ColumnModel(String name)
    {
        super(name);
    }
    
    public abstract String matches(Column column);
}
