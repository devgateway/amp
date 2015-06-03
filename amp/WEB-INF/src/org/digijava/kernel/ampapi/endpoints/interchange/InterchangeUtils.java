package org.digijava.kernel.ampapi.endpoints.interchange;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.dbentity.AmpActivityFields;

public class InterchangeUtils {

	
	
	@SuppressWarnings("serial")
	protected static final Map<Class<?>, String > classToCustomType = new HashMap<Class<?>, String>() {{
		put(java.lang.String.class, "string");
		put(java.util.Date.class, "date");
		put(java.lang.Double.class, "float");
		
		
	}};
	
	
	
	public static String getCustomFieldType(Field field) {
		if (classToCustomType.containsKey(field.getClass())) {
			return classToCustomType.get(field.getClass());
		}
		return "bred";
		
	}
	
	public static JsonBean getAllAvailableFields() {
		JsonBean result = new JsonBean();
		
		Set<String> visibleColumnNames = ColumnsVisibility.getVisibleColumns();
		Field[] fields = AmpActivityFields.class.getDeclaredFields();

		List<Field> exportableFields = new ArrayList<Field>();
		for (Field field : fields) {
            if (field.getAnnotation(Interchangeable.class) != null) {
            	exportableFields.add(field);
            }
		}
		

		for (Field field: exportableFields) {
			result.set(field.getName(), field.getType().toString());
		}
		
//		for (String col : visibleColumnNames) {
//			result.set(, value);
////			fieldSet.contains(arg0)
////			result.set(col, );
//		}
		return result;
	}

}
