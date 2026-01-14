package org.digijava.module.aim.helper.donorReport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OrganizationReportColumn {
    public String columnName();
    public PropertyType propertyType() default PropertyType.PLAIN;
    public Class<?> returnedClass() default Object.class;
}

