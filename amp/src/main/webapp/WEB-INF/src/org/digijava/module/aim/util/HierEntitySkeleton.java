package org.digijava.module.aim.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.hibernate.jdbc.Work;

/*
 * skeleton class for amp_category_value_location
 * 
 * */
public abstract class HierEntitySkeleton<K extends HierEntitySkeleton<?>> implements Comparable<K>, HierarchyListable, Identifiable {
    protected final Long id;    
    protected final String name;
    protected final Long parentId;
    protected boolean translatable = false; 
    protected final String code; 
    protected final Set<K> childLocations = new TreeSet<>();

    public HierEntitySkeleton(Long id, String locName, String code, Long parentId) {
        this.id = id;
        this.name = locName;
        this.code = code;       
        this.parentId = parentId;
    }
    
    @Override
    /**
     * hashcode based on id
     */ 
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    /**
     * equals based on id
     */
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) return false;
        K other = (K) o;
        return this.id.equals(other.id);
    }
    
    public String getCode() {
        return code;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return this.name;
    }
    

    public Long getParentId() {
        return this.parentId;
    }
    
    public void addChild(K child) {
        this.childLocations.add(child);
    }
    
    public Set<K> getChildLocations() {
        return this.childLocations;
    }

    @Override public Long getIdentifier() {
        return this.id; 
    }
    
    @Override
    /**
     * compareTo based on id
     */
    public int compareTo(K o) {
        int d = this.name.trim().compareToIgnoreCase(o.name.trim());
        if (d != 0) return d;
        return this.id.compareTo(o.id);
    }
    
    @Override
    public String toString() { 
        return String.format("%s (id: %d, parent: %d)", this.getName(), this.getId(), this.getParentId());
    }
    
    @Override
    public String getLabel() {
        return this.name;
    }
    
    @Override
    /**
     * return toString of id
     */
    public String getUniqueId() {
 
        return String.valueOf(this.getId());
    }
    
    @Override
    public String getAdditionalSearchString() {
        return code;
    }
    
    @Override
    public boolean getTranslateable() {
        return translatable;
    }

    @Override
    public void setTranslateable(boolean translatable) {
        this.translatable = translatable;
    }
    
    @Override
    public Collection<? extends HierarchyListable> getChildren() {
        return this.childLocations;
    }
    
    @Override
    public int getCountDescendants() {
        int ret = 1;
        if (this.getChildren() != null) {
            for (HierarchyListable hl : this.getChildren())
                ret += hl.getCountDescendants();
        }
        return ret;
    }

    protected static Double nullInsteadOfZero(double val) {
        if (val == 0D) {
            return null;
        } else {
            return val;
        }
    }

    protected static Long nullInsteadOfZero(long val) {
        if (val == 0) {
            return null;
        }
        else return val;
    }
    
    public static<K extends HierEntitySkeleton<K>> void setParentChildRelationships(Map<Long, K>... entitiesArray) {
        Map<Long, K> entities = new TreeMap<>();
        for(Map<Long, K> a:entitiesArray)
            entities.putAll(a);
        for (K entity : entities.values()) {
            if (entity.getParentId() != null) {
                if (entities.get(entity.getParentId()) != null)
                    entities.get(entity.getParentId()).addChild(entity);
            }
        }
    }
    
    protected static<K extends HierEntitySkeleton<K>> Map<Long, K> fetchTree(final String tableName, final String where, final EntityFetcher<K> fetcher) {
        final Map<Long, K> entities = new HashMap<>();
        final String[] columnNames = fetcher.getNeededColumnNames();
        PersistenceManager.getSession().doWork(new Work(){
                public void execute(Connection conn) throws SQLException {
                    ViewFetcher v = DatabaseViewFetcher.getFetcherForView(tableName, where, TLSUtils.getEffectiveLangCode(), new HashMap<PropertyDescription, ColumnValuesCacher>(), conn, columnNames);        
                    try(RsInfo rs = v.fetch(null)) {
                        while (rs.rs.next()) {
                            K entity = fetcher.fetch(rs.rs);
                            entities.put(entity.id, entity);
                        }
                    }
                    setParentChildRelationships(entities);              
                }
            });
        return entities;
    }
}
