package org.dgfoundation.amp.annotations.checkstyle;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, PARAMETER, FIELD })
/**
 * @author Nadejda Mandrescu
 */
public @interface IgnoreCanonicalNames {

}
