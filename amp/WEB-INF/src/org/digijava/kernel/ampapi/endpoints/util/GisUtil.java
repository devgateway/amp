package org.digijava.kernel.ampapi.endpoints.util;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class GisUtil {
	private static final Logger logger = Logger.getLogger(GisUtil.class);

	public static String formatDate(Date d) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		TimeZone tz = TimeZone.getTimeZone("UTC");

		df.setTimeZone(tz);
		return df.format(d);
	}
	public static List<AvailableMethod> getAvailableMethods(String className){
		List<AvailableMethod> availableFilters=new ArrayList<AvailableMethod>(); 
		try {
			Class<?> c = Class.forName(className);
			javax.ws.rs.Path p=c.getAnnotation(javax.ws.rs.Path.class);
			String path="/rest/"+p.value();
			Member[] mbrs=c.getMethods();
			for (Member mbr : mbrs) {
				ApiMethod apiAnnotation=
		    			((Method) mbr).getAnnotation(ApiMethod.class);
				if(apiAnnotation!=null){
					//then we have to add it to the filters list
					javax.ws.rs.Path methodPath=((Method) mbr).getAnnotation(javax.ws.rs.Path.class);
					AvailableMethod filter = new AvailableMethod();
					filter.setName(apiAnnotation.name());
					String endpoint="/rest/"+ p.value();
					
					if(methodPath!=null){
						endpoint+=methodPath.value();
					}
					filter.setEndpoint(endpoint);
					filter.setUi(apiAnnotation.ui());
					//we check the method exposed
					if(((Method) mbr).getAnnotation(javax.ws.rs.POST.class)!=null){
						filter.setMethod("POST");
					}else{
						if(((Method) mbr).getAnnotation(javax.ws.rs.GET.class)!=null){
							filter.setMethod("GET");
						}else{
							if(((Method) mbr).getAnnotation(javax.ws.rs.PUT.class)!=null){
								filter.setMethod("PUT");
							}else{
								if(((Method) mbr).getAnnotation(javax.ws.rs.DELETE.class)!=null){
									filter.setMethod("DELETE");
								}
							}
						}
					}
					availableFilters.add(filter);

				}
			}
		}
		 catch (ClassNotFoundException e) {
			logger.error("cannot retrieve filters list",e);
			return null;
		}
		return availableFilters;
	}	
}
