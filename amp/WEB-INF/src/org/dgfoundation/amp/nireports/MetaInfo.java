package org.dgfoundation.amp.nireports;

/**
 * <b> Immutable</b> class describing a piece of metadata.
 * The enclosed value should be immutable (or at least <b>never</b> changed), or you will be in a world of pain
 * 
 * @author Dolghier Constantin
 * 
 */
public class MetaInfo extends ImmutablePair<String, Comparable<?>> implements Comparable<MetaInfo> {
//	public final String category;
//	public final Comparable<?> value;
	
	public MetaInfo(String category, Comparable<?> value) {
		super(category, value);
	}
	
	public String getCategory() {
		return this.k;
	}
	
	public Comparable<?> getValue() {
		return this.v;
	}
	
	@Override
	public int compareTo(MetaInfo oth) {
		int delta = this.k.compareTo(oth.k);
		if (delta != 0)
			return delta;
		return ((Comparable)this.v).compareTo(oth.v);
	}

}
