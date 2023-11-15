/**
 * 
 */
package org.dgfoundation.amp.ar.cell;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.helper.HierarchycalItem;
import org.dgfoundation.amp.ar.workers.XmlHierarchyColWorker;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex
 *
 */
public class XmlHierarchyCell extends Cell {
    private static Logger logger = Logger.getLogger(XmlHierarchyCell.class);
    
    private List<HierarchycalItem> rootItems;
    
    public XmlHierarchyCell() {
        super();
    }
    
    public XmlHierarchyCell(Long id) {
        super(id);
    }
    
    
    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.cell.Cell#getWorker()
     */
    @Override
    public Class<XmlHierarchyColWorker> getWorker() {
        
        return XmlHierarchyColWorker.class;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.cell.Cell#comparableToken()
     */
    @Override
    public Comparable comparableToken() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String getWrapDirective() {
        return "";
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.cell.Cell#getValue()
     */
    @Override
    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.cell.Cell#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object value) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.cell.Cell#merge(org.dgfoundation.amp.ar.cell.Cell)
     */
    @Override
    public Cell merge(Cell c) {
        logger.warn("Merging cells not implemented");
        return null;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.cell.Cell#merge(org.dgfoundation.amp.ar.cell.Cell, org.dgfoundation.amp.ar.cell.Cell)
     */
    @Override
    public void merge(Cell c, Cell dest) {
        logger.warn("Merging cells not implemented");

    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.cell.Cell#newInstance()
     */
    @Override
    public Cell newInstance() {
        XmlHierarchyCell cell   = new XmlHierarchyCell();
        cell.setRootItems(new ArrayList<HierarchycalItem>());
        return cell;
    }

    /**
     * @return the rootItems
     */
    public List<HierarchycalItem> getRootItems() {
        return rootItems;
    }

    /**
     * @param rootItems the rootItems to set
     */
    public void setRootItems(List<HierarchycalItem> rootItems) {
        this.rootItems = rootItems;
    }


    

}
