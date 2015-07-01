package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Interchangeable {
	
	
	
	
	/**
	 * field title -- a capitalized, preferrably unabbreviated name for the field
	 * example: "Sectors"; "AMP Internal ID"
	 * 
	 */
	String fieldTitle();
	/**
	 * Path in the Function Manager, corresponding to enabling / disabling said field in AF
	 * 
	 */
	String fmPath() default "";
	/**
	 * Whether the field in AF supports several values 
	 * (like: one activity might have several sectors assigned, or only one)
	 * 
	 */
	boolean multipleValues() default false;
	/**
	 * whether this field is to be imported on the target system
	 * example: ID fields are usually not imported, since are assigned by the 
	 * importing DB automatically
	 * 
	 * 
	 */
	boolean importable() default true;
	/**
	 *Whether the field is required.
	 * 
	 */
	String required () default "_NONE_";
	/**
	 * to be set 'true' for the fields whose type or generic subtype is 
	 * the same as of one of the containing classes above 
	 * 
	 * basically implemented to avoid endless loops when describing classes or 
	 * enumerating values
	 * 
	 * example: AmpActivityFields -> Set\<AmpActivitySector\> sectors;
	 * 			AmpActivitySector -> AmpSector sector;
	 * 			AmpSector -> AmpSector parentSector; //descending into AmpSector here would build up a recursion
	 * 
	 * another example: 
	 * AmpActivityFields -> Set\<AmpActivitySector\> sectors;
	 * AmpActivitySector -> AmpSector sector;
	 * AmpSector -> Set\<AmpOrganization\> orgs;
	 * AmpOrganization-> Set\<AmpActivityVersion\> acts; //AmpActivityVersion extends AmpActivityFields, therefore, loop again
	 * 
	 * a third example:
	 * 
	 * AmpActivityFields -> 
	 * 
	 */
	boolean pickIdOnly() default false;
	
	/*ATTENTION: A FIELD MIGHT BE BOTH AN ID AND A VALUE (UNDERLYING)*/
	/**
	 * Whether this field is an ID for the db entity where it takes residence.
	 */
	boolean id() default false;
	/**
	 * Whether this field is the value shown in Activity Form -- for instance, AmpSector.name for sectors.
	 */
	boolean value() default false;
	/**
	 * marks the fact that the Possible Values of the class are contained in the type class of the field marked with descend(), 
	 * instead of the class containing this very field
	 *  
	 */
//	boolean descend() default false;
	
	/** configured with option value, like "Primary Sector" */
    String discriminatorOption() default "";

}
