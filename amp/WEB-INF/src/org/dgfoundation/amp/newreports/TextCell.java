/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Map;

/**
 * Text cell of a report
 * @author Nadejda Mandrescu
 */
public class TextCell extends ReportCell {
	
	public TextCell(Comparable<?> value, long entityId, Map<Long, String> entityIdsValues) {
		super(value, value == null ? "" : String.valueOf(value), entityId, entityIdsValues);
	}
	
	public TextCell(Comparable<?> value) {
		super(value, value == null ? "" : String.valueOf(value), -1, null);
	}
	
	//TODO: we need to decide how to compare Unicode strings
	@Override public int compareTo(ReportCell oth) {
		return Normalizer.normalize(this.displayedValue, Form.NFD)
				.compareToIgnoreCase(
				Normalizer.normalize(oth.displayedValue, Form.NFD));
	}
	
}
