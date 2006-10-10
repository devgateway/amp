/**
 * Exporter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import org.digijava.module.aim.dbentity.AmpReports;

/**
 * Abstract class subclassed by any Exporter classes that provide alternate
 * output schemas (PDF,XLS,CSV...) for a report tree. In AMP, for creating the
 * alternate output
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 */
public abstract class Exporter {

	protected Exporter parent;

	protected Viewable item;
	
	protected AmpReports metadata; 

	/**
	 * @return Returns the metadata.
	 */
	public AmpReports getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata The metadata to set.
	 */
	public void setMetadata(AmpReports metadata) {
		this.metadata = metadata;
	}

	/**
	 * Constructs a new exporter object as a child of an already instantiated
	 * Exporter object.
	 * 
	 * @param parent
	 *            the parent of this Exporter
	 * @param item
	 */
	public Exporter(Exporter parent, Viewable item) {
		this.parent = parent;
		this.item = item;
		if(parent!=null) this.metadata=parent.getMetadata();
	}

	public Exporter() {
	}

	public abstract void generate();

}
