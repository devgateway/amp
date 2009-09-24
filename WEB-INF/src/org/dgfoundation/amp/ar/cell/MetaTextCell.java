/**
 * TextCell.java
 * (c) 2007 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.util.Set;
import java.util.TreeSet;

import org.apache.ecs.vxml.Return;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.workers.MetaTextColWorker;

/**
 * @author mihai
 * a text cell that also adds a percentage property. this percentage applied to amounts in hierarchies created by the text cell here 
 */
public class MetaTextCell extends TextCell {

	private Set metaData;
	
	public Set getMetaData() {
		return metaData;
	}
	
	public MetaInfo getFirstMetaInfo() {
		return (MetaInfo) metaData.iterator().next();
	}
	
	public boolean getDraftFlag() {
		MetaInfo metaInfo = getMetaInfo(ArConstants.DRAFT);
		if(metaInfo!=null) return (Boolean) metaInfo.getValue();
		return false;
	}
	
	public String getStatusFlag() {
		MetaInfo metaInfo = getMetaInfo(ArConstants.STATUS);
		if(metaInfo!=null) return (String) metaInfo.getValue();
		return "";
	}
	
	public String getColour(){
		try{
		if( getDraftFlag() && "started".equals(getStatusFlag()) ) return "RED";
		if( !getDraftFlag() && "started".equals(getStatusFlag()) ) return "GREEN";
		if( !getDraftFlag() && "edited".equals(getStatusFlag()) ) return "GREEN";
		if( getDraftFlag()) return "RED";
		if (!getDraftFlag() && "approved".equals(getStatusFlag())) return "#05528B";
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "";
	}
	
	public MetaInfo getMetaInfo(String category) {
		return MetaInfo.getMetaInfo(metaData, category);
	}

	public Class getWorker() {
		return MetaTextColWorker.class;
	}
	
	public void setMetaData(Set metaData) {
		this.metaData = metaData;
	}

	/**
	 * 
	 */
	public MetaTextCell() {
		super();
		metaData=new TreeSet();
	}
	
	public MetaTextCell(TextCell c) {
		super();
		this.setId(c.getId());
		this.setOwnerId(c.getOwnerId());
		this.setShow(c.isShow());
		this.setValue(c.getValue());
		metaData=new TreeSet();
	}

	/**
	 * @param id
	 */
	public MetaTextCell(Long id) {
		super(id);
		metaData=new TreeSet();
	}

}
