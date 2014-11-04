/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.util.List;
import java.util.Map;

/**
 * Holds report output content as map of columns and cells, or as a parent of {@link ReportArea} children.
 * @see ReportArea
 * @author Nadejda Mandrescu
 */
public class ReportAreaImpl implements ReportArea {
	protected NamedTypedEntity owner;
	protected Map<ReportOutputColumn, ReportCell> contents;
	protected List<ReportArea> children;
	
	public ReportAreaImpl() {
	}
	
	@Override
	public NamedTypedEntity getOwner() {
		return owner;
	}
	
	public void setOwner(NamedTypedEntity owner) {
		this.owner = owner;
	}

	@Override
	public Map<ReportOutputColumn, ReportCell> getContents() {
		return contents;
	}
	
	public void setContents(Map<ReportOutputColumn, ReportCell> contents) {
		this.contents = contents;
	}

	@Override
	public List<ReportArea> getChildren() {
		return children;
	}

	public void setChildren(List<ReportArea> children) {
		this.children = children;
	}
	
	public String toString() {
		return String.format("{contents: %s, children: %s}", contents, children);
	}

}
