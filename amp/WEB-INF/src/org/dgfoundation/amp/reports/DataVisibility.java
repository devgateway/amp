/**
 * 
 */
package org.dgfoundation.amp.reports;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Provides common support for visibility detection, 
 * at the moment for report columns and measures.
 * 
 * @author Nadejda Mandrescu
 */
public class DataVisibility {
	protected static final Logger logger = Logger.getLogger(DataVisibility.class);
	
	public static Map<String, String> getConstantsValueToNameMap(Class clazz) {
		Field[] fields = clazz.getFields();
		Map<String, String> constants = new HashMap<String, String>();
		for (Field field : fields) {
			try {
				String value = String.valueOf(field.get(ColumnConstants.class.getClass()));
				constants.put(value, field.getName());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error(e);
			}
		}
		return constants;
	}

	protected <T extends AmpObjectVisibility> void  processVisbleObjects(List<T> visibilityList, 
			Map<String, String> nameToColumnMap,
			Set<String> visibleColumns, Set<String> invisibleColumns) {
		Set<AmpObjectVisibility> visibleParents = new HashSet<AmpObjectVisibility>();
		Set<AmpObjectVisibility> invisibleParents = new HashSet<AmpObjectVisibility>();
		
		for (ListIterator<T> iter = visibilityList.listIterator(); iter.hasNext(); ) {
			AmpObjectVisibility o = iter.next();
			
			//check if all ancestors are visible
			AmpObjectVisibility parent = o.getParent();
			boolean visible = true;
			while (parent != null && visible) {
				if (invisibleParents.contains(parent))
					visible = false;
				else if (!visibleParents.contains(parent)) {
					visible = FeaturesUtil.isVisible(parent);
					if (visible)
						visibleParents.add(parent);
				}
				parent = parent.getParent();
			}
			
			if (visible) {
				String columnName = nameToColumnMap.get(o.getName());
				invisibleColumns.remove(columnName);
				visibleColumns.add(columnName);
			} else {
				//if current parent is invisible, then place all children to invisible
				while (o != null && (parent == null || !o.equals(parent))) {
					invisibleParents.add(o);
					o = o.getParent();
				}
			}
		}
	}
}
