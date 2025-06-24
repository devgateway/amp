package org.digijava.module.parisindicator.helper;

import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;

import java.util.Collection;

public interface PIOperationsFor5 {

    public abstract int[][] createMiniTable(Collection<PIReportAbstractRow> collection, int startYear, int endYear);

}
