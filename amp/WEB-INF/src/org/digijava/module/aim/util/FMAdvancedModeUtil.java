package org.digijava.module.aim.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FMAdvancedModeUtil {

	public static String ADVANCED_PARAMETER = "FMAdvanced"; 
	
	public static String addModuleAdvancedMarkUp(String body){
		return addAdvancedMarkUp(body.trim(), "[M] ");
	}

	public static String addFeatureAdvancedMarkUp(String body){
		return addAdvancedMarkUp(body.trim(), "[F] ");
	}

	public static String addFieldAdvancedMarkUp(String body){
		return addAdvancedMarkUp(body.trim(), "[f] ");
	}
	
	private static String addAdvancedMarkUp(String body, String suffix){
		StringBuffer retValue = new StringBuffer();

		// <(\w+).*?>\s*([^<>]*?)\s*<\/\1>
//		Pattern pattern =  Pattern.compile("<(\\w+).*?>\\s*([^<>]*?)\\s*<\\/\\1>");
		
		//(?:(^\s*)(\w*))|(?:<(\w+).*?>\s*([^<>]*?)\s*<\/\3>)
//		Pattern pattern =  Pattern.compile("(?:(^\\s*)(\\w*))|(?:<(\\w+).*?>\\s*([^<>]*?)\\s*<\\/\\3>)");

		//(?:^\s*(?:\w*))|(?:>\s*([^<>]*?)\s*</)
//		Pattern pattern =  Pattern.compile("(?:^\\s*(?:\\w*))|(?:>\\s*([^<>]*?)\\s*</)");

		//(?:^\s*(?:\w*))|(?:<img\s*.*?\s*)/>|(?:[^/]>\s*([^<>]*?)\s*</)
		Pattern pattern =  Pattern.compile("(?:^\\s*(?:\\w*))|(<img\\s*.*?\\s*)/>|(?:[^/]>\\s*([^<>]*?)\\s*</)");
		
		Matcher matcher = pattern.matcher(body);
		boolean fFind = matcher.find(); 
		
		if (fFind) {
			boolean rFind = false;
			String teststr = matcher.group().trim();
			int index = matcher.start(1) >= 0 ? matcher.start(1) : matcher.start(2);
			if (!teststr.isEmpty() && index <0){
				index = body.indexOf(teststr);
				retValue.append(injectionSuffix(body, index, suffix));
			} else			
			while (fFind){
				index = matcher.start(1) >= 0 ? matcher.start(1) : matcher.start(2);
				if (index >= 0){
					retValue.append(injectionSuffix(body, index, suffix));
					rFind = true;
					break;
				}
				fFind = matcher.find();
			}
			
			if (!rFind){
				retValue.append(body);
			}
			
		} else {
			retValue.append(body);
		}
		
		return retValue.toString();
	}
	

	private static StringBuffer injectionSuffix(String body, int index,  String suffix){
		StringBuffer retValue = new StringBuffer();
		retValue.append(body.substring(0, index).trim());
		retValue.append(suffix);
		retValue.append(body.substring(index, body.length()).trim());

		return retValue;
	}
}
