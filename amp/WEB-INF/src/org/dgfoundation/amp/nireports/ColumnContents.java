package org.dgfoundation.amp.nireports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the contents of a column inside of a leaf-level ReportData.<br />
 * TODO: for now it is mutable, but it is under consideration
 * @author Dolghier Constantin
 *
 */
public class ColumnContents {
	public Map<Long, List<Cell>> data = new HashMap<>();
}
