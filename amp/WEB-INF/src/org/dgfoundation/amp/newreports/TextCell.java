/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 * Text cell of a report
 * @author Nadejda Mandrescu
 */
public class TextCell extends ReportCell {
	
	public TextCell(Comparable<?> value) {
		super(value, value == null ? "" : String.valueOf(value));
	}
	
	//TODO: we need to decide how to compare Unicode strings
	@Override public int compareTo(ReportCell oth) {
		return Normalizer.normalize(this.displayedValue, Form.NFD)
				.compareToIgnoreCase(
				Normalizer.normalize(oth.displayedValue, Form.NFD));
	}
	
}
