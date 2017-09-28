/**
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.dgfoundation.amp.ar.PercentageHelperMap.PercentageHelperList;

/**
 * @author Alex Gartner
 *
 */
public class PercentageHelperMap extends HashMap<String, PercentageHelperList> {

    
    public PercentageHelperMap() {
    }
    
    public PercentageHelperMap(PercentageHelperMap phm) {
        super( phm );
    }
    
    /**
     * 
     */
    public static final long serialVersionUID = 5532320697152034034L;
    
    
    public void put (String colType, String item, Long itemId, double percentage, Class dimensionClass, boolean hierarchyPurpose) {
        PercentageHelperList dataList       = this.get(colType);
        if ( dataList == null ) {
            dataList    = new PercentageHelperList();
            PercentageHelperData newPhd = new PercentageHelperData(colType,item, itemId, percentage, dimensionClass, hierarchyPurpose);
            if ( newPhd.getHierarchyPurpoase() )
                dataList.setHierarchy(newPhd);
            else
                dataList.add(newPhd);
            this.put(colType, dataList);
        }
        else {
            PercentageHelperData newPhd = new PercentageHelperData(colType,item, itemId, percentage, dimensionClass, hierarchyPurpose);
            
            /* If a hierarchy percentage comes we only keep those percentages that are its descendants  */
            if ( newPhd.getHierarchyPurpoase() ) {
                //if ( dataList.getHierarchy() == null || dataList.getHierarchy().hasAsDescendent(newPhd) ) {
                    Iterator<PercentageHelperData> it   = dataList.iterator();
                    while (it.hasNext()) {
                        PercentageHelperData temp   = it.next();
                        if ( newPhd.hasAsDescendent(temp) ) {
                            //do nothing
                        }
                        else {
                            it.remove();
                        }
                    }
                    dataList.setHierarchy(newPhd);
                //}
            }
            else { //the percentage comes from a filter
                if ( dataList.getHierarchy() == null || dataList.getHierarchy().hasAsDescendent(newPhd) ) {
                    Iterator<PercentageHelperData> it   = dataList.iterator();
                    while (it.hasNext()) {
                        PercentageHelperData temp   = it.next();
                        if ( temp.item.equals(item) ) {
                            // never apply same percentage of same value again
                            return;
                        }
                        
                        if ( temp.hasAsDescendent(itemId) ) {
//                          temp.setItem(item);
//                          temp.setItemId(itemId);
//                          temp.setPercentage(percentage);
                            return;
                        }
                        if (newPhd.hasAsDescendent(temp)) {
                            temp.setItem(item);
                            temp.setItemId(itemId);
                            temp.setPercentage(percentage);
                            return;
                            
                        }
                    }
                    dataList.add(newPhd);
                }
            }
            
//          Iterator<PercentageHelperData> it   = dataList.iterator();
//          while (it.hasNext()) {
//              PercentageHelperData temp   = it.next();
//              if ( temp.item.equals(item) ) {
//                  // never apply same percentage of same value again
//                  return;
//              }
//              
//              if ( temp.hasAsDescendent(itemId) ) {
//                  temp.setItem(item);
//                  temp.setItemId(itemId);
//                  temp.setPercentage(percentage);
//                  return;
//              }
//              if (newPhd.hasAsDescendent(temp)) {
//                  return;
//                  
//              }
//          }
//          
//          
//          dataList.add(newPhd);
            
        }
    }
    
    public double getPercentageSum(String colType) {
        PercentageHelperList dataList       = this.get(colType);
        if ( dataList == null || (dataList.size() == 0 && dataList.getHierarchy() == null )) 
            return -1;
        
        else {
            double ret = 0;
            if ( dataList.size() > 0 ) {
                for (PercentageHelperData percentageHelperData : dataList) {
                    ret += percentageHelperData.getPercentage();
                }
                return ret;
            }
            else
                return dataList.getHierarchy().getPercentage();
        }
    }
    
    public class PercentageHelperList extends ArrayList<PercentageHelperData> {
        
        PercentageHelperData hierarchy  = null;

        public PercentageHelperList() {
            super();
        }

        public PercentageHelperList(Collection<? extends PercentageHelperData> c) {
            super(c);
        }

        public PercentageHelperList(int initialCapacity) {
            super(initialCapacity);
        }

        /**
         * @return the hierarchy
         */
        public PercentageHelperData getHierarchy() {
            return hierarchy;
        }

        /**
         * @param hierarchy the hierarchy to set
         */
        public void setHierarchy(PercentageHelperData hierarchy) {
            this.hierarchy = hierarchy;
        }
        
        @Override
        public String toString() {
            return "H: " + hierarchy + " F: " + super.toString();
        }
        
        
    }
    
}
