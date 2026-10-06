/**
 * TextCell.java
 * (c) 2007 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.cell;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.MetaInfoSet;
import org.dgfoundation.amp.ar.workers.MetaTextColWorker;

/**
 * @author mihai
 * a text cell that also adds a percentage property. this percentage applied to amounts in hierarchies created by the text cell here 
 */
public class MetaTextCell extends TextCell {

    private MetaInfoSet metaData;
    
    public MetaInfoSet getMetaData() {
        return metaData;
    }
    
//  public MetaInfo getFirstMetaInfo() {
//      return (MetaInfo) metaData.iterator().next();
//  }
    
    public boolean getDraftFlag() {
        MetaInfo metaInfo = getMetaInfo(ArConstants.DRAFT);
        if(metaInfo!=null) return (Boolean) metaInfo.getValue();
        return false;
    }
    
    public String getStatusFlag() {
        MetaInfo metaInfo = getMetaInfo(ArConstants.STATUS);
        if (metaInfo!=null) return (String) metaInfo.getValue();
        return "";
    }
    
    public String getColour(){
        if (this.isDisquisedPledgeCellWhichShouldBeHighlited())
            return "#6A6A00"; // pledge disguising as an activity in a mixed report for AMP-17746
        
        String statusFlag = getStatusFlag() == null ? "" : getStatusFlag();
            
        if (getDraftFlag())
            return "RED";
            
        // not a draft
        if (AmpARFilter.VALIDATED_ACTIVITY_STATUS.contains(statusFlag)) {
            return "#05528B";
        }
            
        if (AmpARFilter.UNVALIDATED_ACTIVITY_STATUS.contains(statusFlag)) {
            return "GREEN";
        }
        
        return "";      
    }
    
    
    public MetaInfo getMetaInfo(String category) {
        return metaData.getMetaInfo(category);
    }

    public Class getWorker() {
        return MetaTextColWorker.class;
    }
    
    public void setMetaData(MetaInfoSet metaData) {
        this.metaData = metaData;
    }

    /**
     * 
     */
    public MetaTextCell() {
        super();
        metaData = new MetaInfoSet();
    }
    
    public MetaTextCell(TextCell c) {
        super();
        this.setId(c.getId());
        this.setOwnerId(c.getOwnerId());
        this.setShow(c.isShow());
        this.setValue(c.getValue());
        metaData = new MetaInfoSet();
    }

    /**
     * @param id
     */
    public MetaTextCell(Long id) {
        super(id);
        metaData = new MetaInfoSet();
    }

}
