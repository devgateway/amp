/**
 * Exporter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 *
 */
public abstract class Exporter {

	
	protected Exporter parent;
	protected Viewable item;
	
	/**
	 * 
	 */
	public Exporter(Exporter parent, Viewable item) {
		this.parent=parent;
		this.item=item;
	}

	public Exporter(){}
	
	public abstract void generate();
	
}
