/**
 * ARDimension.java
 * (c) 2007 Development Gateway Foundation
 */
package org.dgfoundation.amp.ar.dimension;

import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.hibernate.HibernateException;

import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

public abstract class ARDimension {

    public static final Map<String,Class> COLUMN_DB_TYPE=new Hashtable<String, Class>();
    public static final Map<Class,ARDimension> DIMENSIONS=new Hashtable<Class, ARDimension>();
    public final static Map<String, String> columnParent = Collections.unmodifiableMap(new HashMap<String, String>() // Map<childColumn, ParentColumn
            {{
                put("Beneficiary Agency", "Beneficiary Agency Groups");
                put("Contracting Agency", "Contracting Agency Groups");
                
                put("Responsible Organization", "Responsible Organization Groups");
                put("Donor Agency", "Donor Group");
                put("Donor Group", "Donor Type");
                
                put("Executing Agency", "Executing Agency Groups");
                put("Executing Agency Groups", "Executing Agency Type");
                
                put("Implementing Agency", "Implementing Agency Groups");
                put("Implementing Agency Groups", "Implementing Agency Type");
                
                for(int i = 1; i <= 7; i++)
                    put("National Planning Objectives Level " + (i + 1), "National Planning Objectives Level " + i);

                for(int i = 1; i <= 7; i++)
                    put("Primary Program Level " + (i + 1), "Primary Program Level " + i);

                for(int i = 1; i <= 7; i++)
                    put("Secondary Program Level " + (i + 1), "Secondary Program Level " + i);
                
                for(int i = 1; i <= 7; i++)
                    put("Tertiary Program Level " + (i + 1), "Tertiary Program Level " + i);                

                put("Primary Sector Sub-Sector", "Primary Sector");
                put("Primary Sector Sub-Sub-Sector", "Primary Sector Sub-Sector");

                put("Secondary Sector Sub-Sector", "Secondary Sector");
                put("Secondary Sector Sub-Sub-Sector", "Secondary Sector Sub-Sector");

                put("Sector Tag Sub-Sector", "Sector Tag");
                put("Sector Tag Sub-Sub-Sector", "Sector Tag Sub-Sector");
                
                put(ArConstants.COLUMN_LOC_ADM_LEVEL_1, ArConstants.COLUMN_LOC_ADM_LEVEL_0);
                put(ArConstants.COLUMN_LOC_ADM_LEVEL_2, ArConstants.COLUMN_LOC_ADM_LEVEL_1);
                put(ArConstants.COLUMN_LOC_ADM_LEVEL_3, ArConstants.COLUMN_LOC_ADM_LEVEL_2);
                
                put(ArConstants.COLUMN_PLEDGE_LOC_ADM_LEVEL_2, ArConstants.COLUMN_PLEDGE_LOC_ADM_LEVEL_1);
                put(ArConstants.COLUMN_PLEDGE_LOC_ADM_LEVEL_3, ArConstants.COLUMN_PLEDGE_LOC_ADM_LEVEL_2);
            }});

    /**
     * Map<columnName, Set<all-direct-and-indirect-ancestors>>, built at startup based off {@value #columnParent}
     */
    public static Map<String, Set<String>> columnAncestors = buildAndCheckColumnsHierarchy();
    
    /**
     * 1. checks that the columnParent is sane (no cycles, no references to non-existing columns)
     * 2. builds the 
     */
    public static Map<String, Set<String>> buildAndCheckColumnsHierarchy()
    {
        Map<String, Set<String>> res = new HashMap<String, Set<String>>();
        Set<String> allColumnsWhichShouldExist = new java.util.HashSet<String>();
        
        for(String childColumnName:columnParent.keySet())
        {
            res.put(childColumnName, getAllAncestors(childColumnName));
            allColumnsWhichShouldExist.add(childColumnName);
            allColumnsWhichShouldExist.addAll(res.get(childColumnName));
        }
        Set<String> allColumnsWhichExist = new java.util.HashSet<String>(AdvancedReportUtil.getColumnNamesList());
        for(String colName:allColumnsWhichShouldExist)
            if (!allColumnsWhichExist.contains(colName))
                throw new RuntimeException("ARDimension::columnParent references unexistant column " + colName + ". AMP reports state is inconsistent, halting");
        return Collections.unmodifiableMap(res);
    }
    
    /**
     * recursively gets all the ancestors of a column
     * @param colName
     * @param in
     * @return
     */
    private static Set<String> getAllAncestors(String colName)
    {
        Set<String> res = new java.util.HashSet<String>();
        
        while (columnParent.containsKey(colName))
        {
            String parent = columnParent.get(colName);
            if (res.contains(parent))
                throw new RuntimeException("found a loop in the columnParents map around column " + colName + ", please fix the columnParent column!");
            res.add(parent);
            colName = parent;
        }
        return res;
    }
    
    /**
     * returns true IFF a splitter cell belongs to a column which is an ancestor of a column and is not unallocated
     * @param splitterCell
     * @param keyCol
     * @return
     */
    public static boolean isAncestor(Column predecessorColumn, CellColumn keyCol)
    {
        String selfColName = keyCol.getName();
        Set<String> ancestorColumns = columnAncestors.get(selfColName);
        if (ancestorColumns == null)
            return false;
        
        return ancestorColumns.contains(predecessorColumn.getName());
    }
    
    protected HashMap<Class,HashMap<Long,Long>> links;
    
    public HashMap getLinksForClass(Class c) {
    return links.get(c);
    }
        
    public ARDimension()
    { 
        links = new HashMap<Class, HashMap<Long, Long>>();
        try {
            initialize();
        }
        catch (Exception e) {
            throw new RuntimeException("Exception encountered", e);
        }
    }
    

    /**
     * returns false IFF the two cells are of the same "grand category" (like locations) and the child is NOT under the splitter (like subsector under sector)
     * @param allSplitterCells
     * @param childCell
     * @return
     */
    public static boolean isLinkedWith(Set<Cell> allSplitterCells, Cell childCell)
    {
        //we get the dimension worker       
        //See AMP-9522
        if (childCell.isUnallocatedCell())
        {
            // Unallocated cells are fake cells. If this cell has a database ID then this is a real 
            // cell not a normal "Unallocated" cell
            return true;
        }
    
        Class relatedContentPersisterClass = childCell.getColumn().getRelatedContentPersisterClass();
        if(relatedContentPersisterClass==null)
            return true; // default behavior is to accept anything we have no information about
        
        Class dimensionClass = childCell.getColumn().getDimensionClass();
        if (dimensionClass == null)
            return true;
    
        ARDimension d = DIMENSIONS.get(dimensionClass);
        if (d == null)
        {
            Constructor dimensionCons = ARUtil.getConstrByParamNo(dimensionClass,0);
            try {
                d = (ARDimension) dimensionCons.newInstance();
                DIMENSIONS.put(dimensionClass, d);
            } 
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return d.internalIsLinkedWith(allSplitterCells, childCell); 
    }
   
    public abstract void initialize() throws HibernateException, SQLException;
    
    /**
     * 
     * @param parentId
     * @param relatedContentPersisterClass
     * @return 
     * if the parent and child of the hierarchy are of the same type (like sectors or NPO) we have to get all the parents and grandparents
     * if the parent and child are different types like donor group and donor agency, we do not have to get the grandparents.
     */
    public abstract Long getParentObject(Long parentId, Class relatedContentPersisterClass);
    
    public boolean internalIsLinkedWith(Set<Cell> allSplitterCells, Cell childCell)
    {
        if (allSplitterCells.size() == 0)
            return true;

        Long childId = childCell.getId();
        for(Cell c:allSplitterCells)
        {
//          if (!isAncestor(c.getColumn(), (CellColumn) childCell.getColumn())) // it is safe to typecast, because we only split CellColumns non-recursively
//              continue; // we only filter-by-parent-relationships for related columns
            
            Class relatedContentPersisterClass = c.getColumn().getRelatedContentPersisterClass();
            // this is the class of the parent object responsible for the hierarchy
            // we check if we have a mapping for that class. the mapping will hold its children.
            Map<Long, Long> m = links.get(relatedContentPersisterClass);
            if (m != null)
            {
                Long parentId = null;
                Long newParent = null;
                do {
            
                    parentId = m.get(childId);
                    newParent = getParentObject(childId, relatedContentPersisterClass);
                    if (newParent != null)
                        childId = newParent;
                    if (parentId != null && parentId.equals(c.getId()))
                        break;
            
                } while (newParent!=null);
        
                if (parentId == null)
                    return false;
                
                if (!parentId.equals(c.getId()))
                    return false;
            }
        }
        return true;
    }

    public static boolean areItemsLinked(Long parentId, Long childId, Class dimensionClass) {
        ARDimension d=DIMENSIONS.get(dimensionClass);
        if (d == null) {
            Constructor dimensionCons = ARUtil.getConstrByParamNo(dimensionClass,0);
            try {
             d = (ARDimension) dimensionCons.newInstance();
            DIMENSIONS.put(dimensionClass, d);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return d.areItemsLinked(parentId, childId);         
    }
    
    public boolean areItemsLinked(Long parentId, Long childId) {
        Set<Entry<Class, HashMap<Long,Long>>> entrySet  =  this.links.entrySet();
        if ( entrySet.size() == 1 ) // All the ones with percentages have just one set
        {
            HashMap<Long, Long> map = entrySet.iterator().next().getValue();
            Long tempId             = childId;
            while ( tempId != null) {
                if ( tempId.equals(parentId) )
                    return true;
                tempId      = map.get(tempId);
            }
        }
        return false;
    }
}
