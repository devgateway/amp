/**
 * CategCell.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.cell;

import org.dgfoundation.amp.ar.Categorizable;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.MetaInfoSet;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 30, 2006
 * @deprecated
 * type of Cell that also holds meta-data for grouping 
 * (ex. amount items also have type of assistance information)
 */
public abstract class CategCell extends Cell implements Categorizable {

    public CategCell() {
        super();
        metaData = new MetaInfoSet();
    }
    
    protected MetaInfoSet metaData;
    
    /**
     * @deprecated
     * @param categoryName
     * @return
     */
    public boolean applyCategorization(String categoryName) {
/*      Iterator i=metaData.iterator();
        while (i.hasNext()) {
            MetaInfo element = (MetaInfo) i.next();
            if (element.getCategory().equals(categoryName)) {
                categorizedBy=element;
                return true;
            }   
        }
        categorizedBy=null;
*/
        return false;
        
    }
    

    public MetaInfo getMetaInfo(String category) {
        return metaData.getMetaInfo(category);
    }
    
    /**
     * @param ownerId
     * @param name
     * @param value
     */
    public CategCell(Long id) {
        super(id);
        metaData = new MetaInfoSet();
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Cell#toString()
     */
    public String toString() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return Returns the metaData.
     */
    public MetaInfoSet getMetaData() {
        return metaData;
    }

    /**
     * cleans all metadata which is safe to be deleted after the report has been generated and is only used for viewing
     * @param cell
     * @return
     */
    public int clearMetaData()
    {
        int sz = metaData.size();
        metaData.clear();
        return sz;
    }
}
