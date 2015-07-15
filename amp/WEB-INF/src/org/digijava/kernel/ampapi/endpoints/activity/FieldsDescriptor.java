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
 * 
 * @author Nadejda Mandrescu
 */
public class FieldsDescriptor {
	public enum TranslationType {
		STRING,
		TEXT,
		NONE
	};

	/**
	 * Detects if a field is translatable
	 * 
	 * @param field
	 * @return true if this field is translatable
	 * Note: can be reused during Import / Export processing
	 */
	public static boolean isTranslatable(Field field, boolean multilingual) {
		return multilingual && field.isAnnotationPresent(TranslatableField.class) 
				&& field.getDeclaringClass().isAnnotationPresent(TranslatableClass.class)
				|| field.isAnnotationPresent(VersionableFieldTextEditor.class);
	}
	
	public static TranslationType getTranslatableType(Field field) {
		if (field.isAnnotationPresent(TranslatableField.class) 
				&& field.getDeclaringClass().isAnnotationPresent(TranslatableClass.class)) {
			return TranslationType.STRING;
		}
		if (field.isAnnotationPresent(VersionableFieldTextEditor.class)) {
			return TranslationType.TEXT;
		}
		return TranslationType.NONE;
	}
}
