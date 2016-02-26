package org.dgfoundation.amp.testmodels;

import java.util.List;

/**
 * a fully-rendered-to-text textual representation of a report NiReportData
 * @author Dolghier Constantin
 *
 */
public class NiReportModel {
	
	public final String name;
	public List<String> headers;
	public List<String> warnings;
	public List<String> body;
	
	protected NiReportModel(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
		
	public NiReportModel withHeaders(List<String> headers) {
		this.headers = headers;
		return this;
	}

	public NiReportModel withWarnings(List<String> warnings) {
		this.warnings = warnings;
		return this;
	}

	public NiReportModel withBody(List<String> body) {
		this.body = body;
		return this;
	}

	public String compare(NiReportModel other) {
		String h = compare_list("headers", this.headers, other.headers);
		if (h != null) return h;
		
		String w = compare_list("warnings", this.warnings, other.warnings);
		if (w != null) return w;
		
		String b = compare_list("body", this.body, other.body);
		if (b != null) return b;
		
		return null;
	}
	
	String compare_list(String tag, List<String> cor, List<String> out) {
		if (cor == null)
			return null; // cor not specified -> not checking
		for(int i = 0; i < Math.min(cor.size(), out.size()); i++) {
			String comp = compareCells(cor.get(i), out.get(i));
			if (comp != null)
				return String.format("%s elem %d: %s", tag, i, comp);
		}
		if (cor.size() != out.size())
			return String.format("%s has %d elems instead of %d", tag, out.size(), cor.size());
		return null;
	}
	
	protected String compareCells(String cellContents, String correctCell) {
		if (cellContents == null)
			cellContents = "<null>";
		
		if (correctCell == null)
			correctCell = "<null>";
		
		if (!correctCell.equals(cellContents))
			return String.format("%s instead of %s", cellContents, correctCell);
		
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("%s -> %s%s%s", this.getName(),
			"\n" + String.join("\n", this.headers),
			"\n" + String.join("\n", this.warnings),
			"\n" + String.join("\n", this.body));
	}
}
