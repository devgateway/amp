/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.security;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.sun.jersey.spi.container.ContainerRequest;


/**
 * Authorizes API request 
 * 
 * @author Nadejda Mandrescu
 */
public class ApiAuthorization {
	protected static final Logger logger = Logger.getLogger(ApiAuthorization.class);
	private final Map<String, ApiMethod> paths = new HashMap<String, ApiMethod>();
	private final Map<ApiMethod, Method> apiMethodToClassMethod = new HashMap<ApiMethod, Method>();
	
	private static ApiAuthorization apiAuthorization = new ApiAuthorization();
	private ApiAuthorization() {
		addApiMethods("org.digijava.kernel.ampapi.endpoints");
	}
	
	private void addApiMethods(String packageName) {
		// initialize reflections to scan through the given package for method annotations
		Reflections reflections = new Reflections(
				new ConfigurationBuilder().setScanners(new MethodAnnotationsScanner())
				.setUrls(ClasspathHelper.forPackage(packageName)));
		Set<Method> apiMethods = reflections.getMethodsAnnotatedWith(ApiMethod.class);
		for (Method m : apiMethods) {
			ApiMethod apiMethod = m.getAnnotation(ApiMethod.class);
			if (apiMethod.authTypes().length == 0 || 
					apiMethod.authTypes().length == 1 && AuthRule.NONE.equals(apiMethod.authTypes()[0])) {
				continue;
			}
			Class<?> clazz = m.getDeclaringClass();
			String classPath = clazz.isAnnotationPresent(Path.class) ? clazz.getAnnotation(Path.class).value() : "";
			String currentPath = m.getAnnotation(Path.class).value();
			String methodRef = getMethodReference(getMethodType(m), classPath, currentPath);
			// store the regex for parameterized queries
			methodRef = methodRef.replaceAll("\\{.*\\}", ".*");
			paths.put(methodRef, apiMethod);
			apiMethodToClassMethod.put(apiMethod, m);
		}
	}
	
	public static void authorize(ContainerRequest containerReq) {
		//apiAuthorization = new ApiAuthorization();
		String pathRef = getMethodReference(containerReq.getMethod(), containerReq.getPath());
		ApiMethod apiMethod = apiAuthorization.paths.get(pathRef);
		// if no direct match found, then try to find by regex
		if (apiMethod == null) {
			for (String path : apiAuthorization.paths.keySet()) {
				if (pathRef.matches(path)) {
					apiMethod = apiAuthorization.paths.get(path);
					break;
				}
			}
		}
		// if API method with authorization settings was detected previously, then let's authorize this request
		if (apiMethod != null) {
			ActionAuthorizer.authorize(apiAuthorization.apiMethodToClassMethod.get(apiMethod), apiMethod, containerReq);
		}
	}
	
	private static String getMethodType(Method m) {
		if (m.isAnnotationPresent(GET.class)) {
			return HttpMethod.GET;
		} else if (m.isAnnotationPresent(POST.class)) {
			return HttpMethod.POST;
		} else if (m.isAnnotationPresent(PUT.class)) {
			return HttpMethod.PUT;
		// the below methods are not used, but adding in case will be
		} else if (m.isAnnotationPresent(DELETE.class)) {
			return HttpMethod.DELETE;
		} else if (m.isAnnotationPresent(HEAD.class)) {
			return HttpMethod.HEAD;
		} else if (m.isAnnotationPresent(OPTIONS.class)) {
			return HttpMethod.OPTIONS;
		}
		return null;
	}
	
	private static String getMethodReference(String... args) {
		StringBuilder sb = new StringBuilder();
		for (String arg : args) {
			arg = StringUtils.strip(arg, "/");
			if (StringUtils.isNotBlank(arg))
				sb.append("/").append(arg);
		}
		return sb.toString();
	}
}
