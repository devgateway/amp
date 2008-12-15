package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XMLCustomFieldParser extends DefaultHandler{
	private List<CustomField> customFields = new ArrayList<CustomField>();
	private int step;
	private CustomField cf;
	private LinkedHashMap<String,String> options;
	private boolean isOption;
	private String optionValue;
	private String optionText;	
   
   public void startElement (String uri, String name,
		      String qName, Attributes atts){
	   if ("text".equals(name)){
		   cf = new  CustomField<String>();
		   processAtrributes(atts);
	   }else if("combo".equals(name)){
		   cf = new  ComboBoxCustomField();
		   options = new LinkedHashMap<String,String>();
		   processAtrributes(atts);
	   }else if("category".equals(name)){
		   cf = new  CategoryCustomField();
		   processAtrributes(atts);
	   }else if("date".equals(name)){
		   cf = new  DateCustomField();
		   processAtrributes(atts);
	   }else if("step".equals(name)){
		   step = Integer.parseInt(atts.getValue("number"));
	   }else if("option".equals(name) && cf instanceof  ComboBoxCustomField){
			   isOption = true;
			   optionValue = atts.getValue("value");
	   }
	}
   
    @Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
    	if(isOption)
    		optionText = new String(ch,start,length);
	}
   
   private void processAtrributes(Attributes atts){
	   String nameValue = atts.getValue("name");
	   String description = atts.getValue("description");
	   String property = atts.getValue("property");
	   String field = atts.getValue("field");

	   cf.setStep(step);
	   cf.setName(nameValue);
	   cf.setDescription(description);
	   cf.setAmpActivityPropertyName(property);
	   cf.setFM_field(field);	   
   }
	
	public void endElement (String uri, String name, String qName)	{
		
		if(cf instanceof  ComboBoxCustomField){
			if("combo".equals(name)){ 
				((ComboBoxCustomField)cf).setOptions(options);
				options = null;
			}else if("option".equals(name)){				
				options.put(optionValue, optionText);
				isOption = false;
			}
		}
	    if ("text".equals(name) || "combo".equals(name) ||  "category".equals(name) ||  "date".equals(name)){
		   if(cf != null) 
			   customFields.add(cf);
		   cf = null;
	    }		
	}

	public void setCustomFields(List<CustomField> customFields) {
		this.customFields = customFields;
	}

	public List<CustomField> getCustomFields() {
		return customFields;
	}
   
}
