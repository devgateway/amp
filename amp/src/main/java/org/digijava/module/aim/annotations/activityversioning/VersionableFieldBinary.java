package org.digijava.module.aim.annotations.activityversioning;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a binary (e.g., image, file) field as versionable.
 * Intended for use with byte[] or Blob fields.
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface VersionableFieldBinary {

    /**
     * Human-readable label for the field (used in forms and logs).
     */
    String fieldTitle();

    /**
     * Optional help text or tooltip.
     */
    String description() default "";

    /**
     * Whether the field is tracked for version history (default is true).
     */
    boolean versionable() default true;

    /**
     * Optional path in the Field Manager config (for FM integration).
     */
    String fmPath() default "";
}
