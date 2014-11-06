/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.settings;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.digijava.kernel.translator.TranslatorWorker;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
/**
 * Setting Field that allows us to drill down & define custom settings structure 
 * 
 * @author Nadejda Mandrescu
 */
public class SettingField {
	public enum FieldType {
		/** The actual value */
		INT_VALUE,
		STR_VALUE,
		BOOL_VALUE,
		/** A list of Options */
		OPTIONS,
		/** A list of nested SettingField-s */
		FIELDS;
	};
	/** 
	 * Field type
	 */
	public final FieldType type;
	/** Internal id */
	public final String id;
	/** The groupId the field depends on, i.e. if that id is selected */
	public final String groupId;
	/** Name to display */
	public final String name;
	/** The value based on the field type */
	public final Object value;
	
	/**
	 * Builds single value field
	 * @param id
	 * @param name
	 * @param value
	 */
	public SettingField(String id, String groupId, String name, String value) {
		this(FieldType.STR_VALUE, id, groupId, name, value);
	}
	
	public SettingField(String id, String groupId, String name, int value) {
		this(FieldType.INT_VALUE, id, groupId, name, value);
	}
	
	public SettingField(String id, String groupId, String name, boolean value) {
		this(FieldType.BOOL_VALUE, id, groupId, name, value);
	}
	
	/**
	 * Builds a nested setting field that contains multiple other values/options 
	 * @param id
	 * @param name
	 * @param field
	 */
	public SettingField(String id, String groupId, String name, List<SettingField> field) {
		this(FieldType.FIELDS, id, groupId, name, field);
	}
	
	/**
	 * Setting field with multiple options 
	 * @param id internal field id/name
	 * @param name displayed field name
	 * @param options {@link SettingOptions}
	 */
	public SettingField(String id, String groupId, String name, SettingOptions options) {
		this(FieldType.OPTIONS, id, groupId, name, options);
	}
	
	private SettingField(FieldType type, String id, String groupId, String name, Object value) {
		this.type = type;
		this.id = id;
		this.groupId = groupId;
		this.name = TranslatorWorker.translateText(name);
		this.value = value;
	}
}
