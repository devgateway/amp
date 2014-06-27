package org.digijava.module.aim.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.digijava.module.aim.helper.CustomField;
import org.digijava.module.aim.helper.CustomFieldStep;
import org.digijava.module.aim.helper.XMLCustomFieldParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class CustomFieldsUtil {
	private static List<CustomField<?>> customFields;
	private static List<CustomFieldStep> customFieldsSteps;
	
    static public void parseXMLFile(InputStream inputStreamString ){    	
    	XMLReader xr = new org.apache.xerces.parsers.SAXParser();
    	XMLCustomFieldParser handler = new XMLCustomFieldParser();
    	xr.setContentHandler(handler);
    	xr.setErrorHandler(handler);

    	try {
			xr.parse(new InputSource(inputStreamString));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customFields = handler.getCustomFields();
		customFieldsSteps = handler.getCustomFieldsSteps();
    }
    
    static public List<CustomField<?>> getCustomFields(){
    	return customFields;
    }
    static public List<CustomFieldStep> getCustomFieldsSteps(){
    	return customFieldsSteps;
    }    
}
