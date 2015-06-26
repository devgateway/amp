/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;

import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;

/**
 * Provides fields description
 * TODO: we should store here "Fields Enumeraiton EP" logic
 * 
 * @author Nadejda Mandrescu
 */
public class FieldsDescriptor {

	/**
	 * Detects if a field is translatable
	 * 
	 * @param field
	 * @return true if this field is translatable
	 * Note: can be reused during Import / Export processing
	 */
	public static boolean isTranslatable(Field field) {
		return field.isAnnotationPresent(TranslatableField.class) 
				&& field.getDeclaringClass().isAnnotationPresent(TranslatableClass.class)
				|| field.isAnnotationPresent(VersionableFieldTextEditor.class);
	}
}
