package org.dgfoundation.amp.nireports.output;

/**
 * a cell which holds a text
 * @author Dolghier Constantin
 *
 */
public class NiTextCell extends NiOutCell {
	public final String text;
	public final long entityId;
	
	public NiTextCell(String text, long entityId) {
		this.text = text;
		this.entityId = entityId;
	}
	
	@Override
	public int compareTo(Object o) {
		NiTextCell ntc = (NiTextCell) o;
		return this.text.compareTo(ntc.text);
	}

	@Override
	public String getDisplayedValue() {
		return text;
	}

	@Override
	public <K> K accept(CellVisitor<K> visitor) {
		return visitor.visit(this);
	}

}
