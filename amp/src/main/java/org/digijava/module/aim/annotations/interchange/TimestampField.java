package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used for marking date fields that should be serialized/deserialized by using
 * {@href EPConstants.ISO8601_DATE_AND_TIME_FORMAT} format
 *
 * By default, all Date fields are serialized/deserialized in {@href EPConstants.ISO8601_DATE_FORMAT} format
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TimestampField {

}
