/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.queries.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a touple of MDX elements (it can be a mix of MDX lelves, attributes or measures)
 * @author Nadejda Mandrescu
 *
 */
public class MDXTuple {
	private List<MDXElement> tuple = new ArrayList<MDXElement>();
	
	public MDXTuple() {
	}
	
	public void add(MDXElement elem) {
		tuple.add(elem);
	}
	
	public List<MDXElement> getTuple() {
		return tuple;
	}
	
	public String toSortingString() {
		if (tuple.size() == 0) return "";
		StringBuilder sb = new StringBuilder(tuple.size() * 20); //aprox init
		sb.append("(");
		for (MDXElement elem: tuple) {
			sb.append(elem.getSortName()).append(",");
		}
		sb.deleteCharAt(sb.length()-1).append(")");
		return sb.toString(); 
	}

}
