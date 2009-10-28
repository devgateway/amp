package org.digijava.module.aim.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;

public class FMAdvancedModeUtil {

	public static String ADVANCED_PARAMETER = "FMAdvanced"; 
	
	public static String addModuleAdvancedMarkUp(String body, String name){
		return addAdvancedMarkUp(body.trim(), "[M] ", "showModule&fmName="+name);
	}

	public static String addFeatureAdvancedMarkUp(String body, String name){
		return addAdvancedMarkUp(body.trim(), "[F] ", "showFeature&fmName="+name);
	}

	public static String addFieldAdvancedMarkUp(String body, String name){
		return addAdvancedMarkUp(body.trim(), "[f] ", "showField&fmName="+name);
	}
	
	private static String addAdvancedMarkUp(String body, String suffix, String method){
		StringBuffer retValue = new StringBuffer();

		// <(\w+).*?>\s*([^<>]*?)\s*<\/\1>
//		Pattern pattern =  Pattern.compile("<(\\w+).*?>\\s*([^<>]*?)\\s*<\\/\\1>");
		
		//(?:(^\s*)(\w*))|(?:<(\w+).*?>\s*([^<>]*?)\s*<\/\3>)
//		Pattern pattern =  Pattern.compile("(?:(^\\s*)(\\w*))|(?:<(\\w+).*?>\\s*([^<>]*?)\\s*<\\/\\3>)");

		//(?:^\s*(?:\w*))|(?:>\s*([^<>]*?)\s*</)
//		Pattern pattern =  Pattern.compile("(?:^\\s*(?:\\w*))|(?:>\\s*([^<>]*?)\\s*</)");

		//(?:^\s*(?:\w*))|(?:<img\s*.*?\s*)/>|(?:[^/]>\s*([^<>]*?)\s*</)
//		Pattern pattern =  Pattern.compile("(?:^\\s*(?:\\w*))|(<img\\s*.*?\\s*)/>|(?:[^/]>\\s*([^<>]*?)\\s*</)");
	
		
		//(?:<[iI][mM][gG].*?>)|(<[aA].*?</[aA]>)|(?:[^/]>\s*([^<>]*?)\s*</)|(?:^\s*(?:\w*))
		Pattern pattern =  Pattern.compile("(?:<[iI][mM][gG].*?>)|(<[aA].*?</[aA]>)|(?:[^/]>\\s*([^<>]*?)\\s*</)|(?:^\\s*(?:\\w*))");

//		body = "<br/> " + body;
		Matcher matcher = pattern.matcher(body);
		boolean fFind = matcher.find(); 
		
		if (fFind) {
			boolean rFind = false;
			String teststr = matcher.group().trim();
			int index = matcher.start(1) >= 0 ? matcher.start(1) : matcher.start(2);
			if (!teststr.isEmpty() && index <0){
				index = body.indexOf(teststr);
				retValue.append(injectionSuffix(body, index, suffix, method));
			} else			
			while (fFind){
				teststr = matcher.group().trim();
				if (!teststr.isEmpty()){
					index = matcher.start(1) >= 0 ? matcher.start(1) : matcher.start(2) >= 0 ? matcher.start(2): matcher.start();
				}
					
				if (index >= 0){
					retValue.append(injectionSuffix(body, index, suffix, method));
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
	

	private static StringBuffer injectionSuffix(String body, int index,  String suffix, String method){
		StringBuffer retValue = new StringBuffer();
		retValue.append(body.substring(0, index).trim());
		
//		retValue.append("<span title=\"Click here for more details\" onclick=\"document.getElementById('"+id+"').style.display='block'\">");
		retValue.append("<span title=\"Click here for more details\" onclick='window.open(\"/aim/fmAdvancedPopup.do?method="+method+"\",\"FMAdvanced\",\"status=1, width=600, height=200\");'>");

		retValue.append(suffix);
		
		retValue.append("</span>");
/*
//		retValue.append("<div title=\"close\" id=\""+id+"\" onClick=\"this.style.display='none'\" style=\"border:1px solid black; position:absolute;z-index:10;display:none;\" align=\"right\">");
		retValue.append("<div align=\"left\" id=\""+id+"\" style=\"border: 1px solid black; position: absolute; z-index: 10; display: none; background-color: #E0FFFF; \" >");
		retValue.append("<div align=\"left\" style=\"background-color: Blue; color: White;\" title=\"close\" onClick=\"this.parentNode.style.display='none'\">Parent Tree</div>");
		
		retValue.append(divText);
		retValue.append("</div>");
*/		
		retValue.append(body.substring(index, body.length()).trim());
		return retValue;
	}
	
/*	
	
	private static StringBuffer getDivText(AmpModulesVisibility module, Integer offset){
		StringBuffer retValue = new StringBuffer();

		if (module.getParent() != null){
			retValue.append(getDivText((AmpModulesVisibility)module.getParent(), offset++));
		}
		retValue.append(getElementTest(module.getName(), offset));
	
		return retValue;
	}
	
	private static StringBuffer getDivText(AmpFeaturesVisibility feature, Integer offset){
		StringBuffer retValue = new StringBuffer();

		if (feature.getParent() != null){
			retValue.append(getDivText((AmpModulesVisibility)feature.getParent(), offset++));
		}
		retValue.append(getElementTest(feature.getName(), offset));

		
		return retValue;
	}

	private static StringBuffer getDivText(AmpFieldsVisibility field, Integer offset){
		StringBuffer retValue = new StringBuffer();

		if (field.getParent() != null){
			retValue.append(getDivText((AmpFeaturesVisibility)field.getParent(), offset++));
		}
		retValue.append(getElementTest(field.getName(), offset));
		
		return retValue;
	}
	
	private static StringBuffer getElementTest(String name, Integer offset){
		StringBuffer retValue = new StringBuffer();

		retValue.append("<div nowrap=\"true\">");
		for (int i = 0; i < offset; i++) {
			retValue.append("&nbsp;");
		}
		retValue.append("<img src=\"/TEMPLATE/ampTemplate/images/yui/check2.gif\" width=\"16\" height=\"22\" border=\"0\">");
		retValue.append(name);
		retValue.append("</div>");
		
		return retValue;	
	}
*/	
	
}
