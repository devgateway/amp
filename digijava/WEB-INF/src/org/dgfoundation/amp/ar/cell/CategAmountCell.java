/**
 * CategAmountCell.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.ar.cell;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.ar.Categorizable;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.workers.CategAmountColWorker;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 30, 2006
 * AmountCell that also holds metadata
 */
public class CategAmountCell extends AmountCell implements Categorizable {
	
	protected Set metaData;
	
	/**
	 * @return Returns the metaData.
	 */
	public Set getMetaData() {
		return metaData;
	}

	/**
	 * @param metaData The metaData to set.
	 */
	public void setMetaData(Set metaData) {
		this.metaData = metaData;
	}

	public MetaInfo getMetaInfo(String category) {
		Iterator i=metaData.iterator();
		while (i.hasNext()) {
			MetaInfo element = (MetaInfo) i.next();
			if(element.getCategory().equals(category)) return element;
		}
		return null;
	}
	
	public CategAmountCell() {
		super();
		metaData=new HashSet();
	}
	
	public Class getWorker() {
		return CategAmountColWorker.class;
	}
	
	/**
	 * @param ownerId
	 * @param name
	 * @param value
	 */
	public CategAmountCell(Long id) {
		super(id);
		metaData=new HashSet();
	}


	public void setValue(Object o) {
		this.amount=((Double) o).doubleValue();
	}
	
	public Object getValue() {
		return new Double(amount);
	}


	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Viewable#getViewArray()
	 */
	protected MetaInfo[] getViewArray() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Cell filter(Cell metaCell) {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Categorizable#hasMeta(org.dgfoundation.amp.ar.MetaInfo)
	 */
	public boolean hasMetaInfo(MetaInfo m) {
		MetaInfo internal=getMetaInfo(m.getCategory());
		if(internal==null) return false;
		if(internal.compareTo(m)==0) return true;else return false;
	}
}
