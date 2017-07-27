package org.digijava.kernel.util;

import javax.servlet.ServletContext;

import org.digijava.kernel.request.TLSUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Octavian Ciubotaru
 */
public class SpringUtil {

    /**
     * Returns a spring bean by its class.
     * This is a shortcut for accessing spring beans from a static method. Can only be called in http request scope.
     *
     * @param requiredType type the bean must match
     * @return an instance of the single bean matching the required type
     */
    public static <T> T getBean(Class<T> requiredType) {
        ServletContext sc = TLSUtils.getRequest().getServletContext();
        return getBean(sc, requiredType);
    }

    public static <T> T getBean(ServletContext sc, Class<T> requiredType) {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        return webApplicationContext.getBean(requiredType);
    }
}
