/**
 * Exporter.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import org.digijava.module.aim.dbentity.AmpReports;

/**
 * Abstract class subclassed by any Exporter that provides alternate
 * output schemas (PDF,XLS,CSV...) for Viewable entities. 
 * Exporters are java based viewers for Viewable items. They behave in the same way as jsp includes for html. Thus,
 * an exporter for an object that has childen (like Reports or Columns) will always call any child exporters before exiting.
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 */
public abstract class Exporter {
    /**
     * the parent of this exporter, if any. This is used to provide access to parent's properties.
     */
    protected Exporter parent;

    /**
     * the item that is required to export
     */
    protected Viewable item;
    
    public Viewable getItem() {
        return item;
    }

    protected AmpReports metadata; 
    protected AmpARFilter filter;

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

    public AmpARFilter getFilter()
    {
        return this.filter;
    }
    
    public void setFilter(AmpARFilter filter)
    {
        this.filter = filter;
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
        if (parent!=null)
        {
            this.metadata = parent.getMetadata();
            this.filter = parent.getFilter();
        }
    }

    public Exporter() {
    }
    
    

    /**
     * @return the parent
     */
    public Exporter getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Exporter parent) {
        this.parent = parent;
    }
    
    /**
     * cached result of {@link #getArchParent()}
     */
    private Exporter _arch_parent = null;
    
    /**
     * iterates upto the top of the hier
     * @return
     */
    public Exporter getArchParent(){
        if (this.parent == null)
            return this;
        if (_arch_parent == null)
            _arch_parent = this.parent.getArchParent();
        return _arch_parent;
    }

    /**
     * method implemented by any exporter, that creates the required objects that can be displayed in output. 
     * If the Viewable item also holds children, Viewable.invokeExporter will be called for any of them 
     *
     */
    public abstract void generate();

}
