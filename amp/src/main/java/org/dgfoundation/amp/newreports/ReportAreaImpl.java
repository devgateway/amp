/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Holds report output content as map of columns and cells, or as a parent of {@link ReportArea} children.
 * @see ReportArea
 * @author Nadejda Mandrescu
 */
public class ReportAreaImpl implements ReportArea {
    protected AreaOwner owner;
    protected Map<ReportOutputColumn, ReportCell> contents;
    protected List<ReportArea> children;
    protected int nrEntities = -1;
    
    public ReportAreaImpl() {
    }
    
    @Override
    public AreaOwner getOwner() {
        return owner;
    }
    
    public void setOwner(AreaOwner owner) {
        this.owner = owner;
    }

    @Override
    public Map<ReportOutputColumn, ReportCell> getContents() {
        return contents;
    }
    
    public void setContents(Map<ReportOutputColumn, ReportCell> contents) {
        this.contents = contents;
    }
    
    @Override
    public List<ReportArea> getChildren() {
        return children;
    }

    public void setChildren(List<ReportArea> children) {
        this.children = children;
    }
    
    public static<K extends ReportAreaImpl> Supplier<ReportAreaImpl> buildSupplier(Class<K> clazz) {
        return () -> {
            try {
                K ra = clazz.newInstance();
                ra.setContents(new LinkedHashMap<ReportOutputColumn, ReportCell>());
                return ra;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }
    
    @Override
    public int getNrEntities() {
        return this.nrEntities;
    }
    
    public void setNrEntities(int nrEntities) {
        this.nrEntities = nrEntities;
    }
    
    public String toString() {
        return String.format("{contents: %s, children: %s}", contents, children);
    }
}
