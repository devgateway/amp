package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Interchangeable {
	/**
	 * field title -- a capitalized, preferrably unabbreviated name for the field
	 * example: "Sectors"; "AMP Internal ID"
	 */
	String fieldTitle();
	
	/**
	 * Path in the Feature Manager, corresponding to enabling / disabling said field in AF
	 */
	String fmPath() default "";
	
	/**
	 * Whether the collection field in AF supports several values. It will be used just for collection fields
	 * (like: one activity might have several sectors assigned, or only one)
	 */
	boolean multipleValues() default true;
	
	/**
	 * whether this field is to be imported on the target system
	 * example: ID fields are usually not imported, since are assigned by the 
	 * importing DB automatically
	 */
	boolean importable() default false;
	
	/**
	 *Whether the field is always required, required for non-draft saves, or not required. 
	 */
	String required () default "_NONE_";
	
	/**
	 * Set to true if underlying field value can be obtained from the 
	 * Possible Values endpoint -- meaning it can be identified by an ID
	 * and picked from a list instead of being computed by AMP
	 * or input by the user
	 * Example: any AmpCategoryValue is picked by ID, not by its string value,
	 * since it's picked from a list and never customly edited
	 * Another example: Sector ID from AmpActivitySector. A sector is picked from
	 * a predefined list, not added in the AF. 
	 */
	boolean pickIdOnly() default false;
	
	/*ATTENTION: A FIELD MIGHT BE BOTH AN ID AND A VALUE (UNDERLYING)*/
	/**
	 * Whether this field is an ID for the db entity where it takes residence.
	 * The ID itself is picked from the getIdentifier() method of the entity, 
	 * which has to be marked as "implements <Identifiable>".
	 */
	boolean id() default false;
	
	/**
	 * Whether this field is the value shown in Activity Form -- for instance, AmpSector.name for sectors.
	 */
	boolean value() default false;
	
	/**
	 * Used in PossibleValues EP. (Designed for the AmpActivityLocation->AmpLocation->AmpCategoryValueLocations)
	 * 
	 * Marks a field that contains information useful for describing a possible value, 
	 * yet not contained within the class itself. 
	 * Said info is to be specified under the tag "Extra info" and grouped in a JSON. 
	 * Said JSON will have the structure expected from a 'Possible Value' item.  
	 *  
	 */
	boolean extraInfo() default false;
	
		
	
	/** configured with option value, like "Primary Sector" */
    String discriminatorOption() default "";
    
    Validators validators() default @Validators;
    
    /* constraints for multi-level validators */
    boolean uniqueConstraint() default false;
    boolean percentageConstraint() default false;

}