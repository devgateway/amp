/**
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alex Gartner
 *
 */
public class PercentageHelperMap extends HashMap<String, List<PercentageHelperData>> {

	
	public PercentageHelperMap() {
	}
	
	public PercentageHelperMap(PercentageHelperMap phm) {
		super( phm );
	}
	
	/**
	 * 
	 */
	public static final long serialVersionUID = 5532320697152034034L;
	
	
	public void put (String colType, String item, Long itemId, double percentage, Class dimensionClass) {
		List<PercentageHelperData> dataList		= this.get(colType);
		if ( dataList == null ) {
			dataList	= new ArrayList<PercentageHelperData>();
			PercentageHelperData phd	= new PercentageHelperData(colType,item, itemId, percentage, dimensionClass);
			dataList.add(phd);
			this.put(colType, dataList);
		}
		else {
			PercentageHelperData phd	= new PercentageHelperData(colType,item, itemId, percentage, dimensionClass);
			
			Iterator<PercentageHelperData> it	= dataList.iterator();
			while (it.hasNext()) {
				PercentageHelperData temp	= it.next();
				if ( temp.item.equals(item) ) {
					// never apply same percentage of same value again
					return;
				}
				if ( temp.isDescendent(itemId) ) {
					temp.setItem(item);
					temp.setItemId(itemId);
					temp.setPercentage(percentage);
					return;
				}
				if (phd.isDescendent(temp)) {
					return;
					
				}
			}
			
			
			dataList.add(phd);
			
		}
	}
	
	public double getPercentageSum(String colType) {
		List<PercentageHelperData> dataList		= this.get(colType);
		if ( dataList == null || dataList.size() == 0 ) 
			return -1;
		
		else {
			double ret = 0;
			for (PercentageHelperData percentageHelperData : dataList) {
				ret += percentageHelperData.getPercentage();
			}
			return ret;
		}
	}
	
}
