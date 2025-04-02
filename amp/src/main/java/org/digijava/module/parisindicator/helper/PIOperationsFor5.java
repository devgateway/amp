package org.digijava.module.parisindicator.helper;

import java.util.Collection;

import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;

public interface PIOperationsFor5 {

    public abstract int[][] createMiniTable(Collection<PIReportAbstractRow> collection, int startYear, int endYear);

}
