package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Map;
/**
 * 
 * @author acartaleanu
 *
 * abstract class as a scaffold for custom classes that would 
 * employ special procedures in obtaining possible values and 
 * specific activity values in for a full project information
 */
public abstract class FieldsDiscriminator {
	public abstract Map<Object, Object> getPossibleValues();
	/**
	 * Generates a JSON-friendly representation of the object provided, 
	 * with an implied logical conversion for correspondence to AF
	 * See {@link org.digijava.kernel.ampapi.endpoints.activity.InterchangeableClassMapper#classToCustomType(Object)} for details
	 * (example: a field logically represents a type, thus is expected
	 * to be output as a String in JSON, but is internally stored 
	 * as an Integer, encoded through hardcoded constants)
	 * 
	 * @param obj the object to be converted
	 */

	protected Object value;
	
//	public FieldsDiscriminator(Object value) {
//		this.value = value;
//	}
//	
	public abstract Object toJsonOutput(Object value);
	
	public abstract Long getIdOf(Object value);

	public abstract Object toAmpFormat(Object obj);	
//	public abstract Object toAmpFormat();
	
//	public abstract Class getJsonOutputType();
}
