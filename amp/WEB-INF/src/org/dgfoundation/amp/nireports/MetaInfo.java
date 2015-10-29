package org.dgfoundation.amp.nireports;

/**
 * <b> Immutable</b>class describing a piece of metadata.
 * The enclosed value should be immutable (or at least <strong>never</strong> changed), or you will be in a world of pain
 * 
 * @author Dolghier Constantin
 * 
 */
public class MetaInfo implements Comparable<MetaInfo> {
	public final String category;
	public final Comparable<?> value;
	
	public MetaInfo(String category, Comparable<?> value) {
		if (this.category == null)
			throw new RuntimeException("metaInfo category is null");
		if (this.value == null)
			throw new RuntimeException("metaInfo value is null");
		this.category = category;
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		MetaInfo oth = (MetaInfo) obj;
		return this.category.equals(oth.category) && this.value.equals(oth.value);
	}

	@Override
	public int hashCode() {		
		return this.category.hashCode() ^ (19 * this.value.hashCode());
	}
	
	@Override
	public int compareTo(MetaInfo oth) {
		int delta = this.category.compareTo(oth.category);
		if (delta != 0)
			return delta;
		return ((Comparable)this.value).compareTo(oth.value);
	}

}
