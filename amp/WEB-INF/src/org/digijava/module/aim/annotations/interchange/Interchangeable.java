package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Interchangeable {
	String fieldTitle();
	String fmPath() default "";
	boolean multipleValues() default false;
	boolean importable() default true;
	String required () default "_NONE_";
	/**
	 * to be set 'true' for the fields whose type or generic subtype is 
	 * the same as of one of the containing classes above 
	 * 
	 * basically implemented to avoid endless loops
	 * 
	 * example: AmpActivityFields -> Set\<AmpSector\> sectors;
	 * 			AmpSector -> AmpSector parentSector; //descending into AmpSector here would build up a recursion
	 * 
	 * another example: 
	 * AmpActivityFields -> Set\<AmpSector\> sectors;
	 * AmpSector -> Set\<AmpOrganization\> orgs;
	 * AmpOrganization-> Set\<AmpActivityVersion\> acts; //AmpActivityVersion extends AmpActivityFields, therefore, loop again
	 * 
	 */
	boolean recursive() default false;
	
	/** configured with option value, like "Primary Sector" */
    String discriminatorOption() default "";
}
