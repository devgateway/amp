package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XMLCustomFieldParser extends DefaultHandler{
	private List<CustomField<?>> customFields = new ArrayList<CustomField<?>>();
	private List<CustomFieldStep> customFieldsSteps = new ArrayList<CustomFieldStep>();
	private CustomFieldStep step;
	private CustomField<?> cf;
	private LinkedHashMap<String,String> options;
	private boolean isOption;
	private String optionValue;
	private String optionText;	
	
   public void startElement (String uri, String name,
		      String qName, Attributes atts){
	   if ("text".equals(name)){
		   cf = new  TextCustomField();
		   processAtrributes(atts);
	   }else if("combo".equals(name)){
		   cf = new  ComboBoxCustomField();
		   options = new LinkedHashMap<String,String>();
		   processAtrributes(atts);
	   }else if("category".equals(name)){
		   cf = new  CategoryCustomField();
		   processAtrributes(atts);
		   CategoryCustomField ccf = (CategoryCustomField) cf;
		   ccf.setCategoryName(atts.getValue("category"));
	   }else if("date".equals(name)){
		   cf = new  DateCustomField();
		   processAtrributes(atts);
	   }else if("radio".equals(name)){
		   cf = new  RadioOptionCustomField();
		   options = new LinkedHashMap<String,String>();
		   processAtrributes(atts);
	   }else if("check".equals(name)){
		   cf = new  CheckCustomField();
		   CheckCustomField ccf = (CheckCustomField) cf;
		   processAtrributes(atts);
		   ccf.setLabelTrue(atts.getValue("label_true"));
		   ccf.setLabelFalse(atts.getValue("label_false"));
	   }else if("step".equals(name)){
		   int stepInt = Integer.parseInt(atts.getValue("number"));
		   step = new CustomFieldStep();
		   step.setStep(stepInt);
		   step.setName(atts.getValue("name"));
		   customFieldsSteps.add(step);
	   }else if("option".equals(name) && (cf instanceof  ComboBoxCustomField || cf instanceof RadioOptionCustomField) ){
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
		
		if(cf instanceof  ComboBoxCustomField || cf instanceof RadioOptionCustomField){
			if("combo".equals(name)){ 
				((ComboBoxCustomField)cf).setOptions(options);
				options = null;
			}else if("radio".equals(name)){				
				((RadioOptionCustomField)cf).setOptions(options);
				options = null;
			}else if("option".equals(name)){				
				options.put(optionValue, optionText);
				isOption = false;
			}
		}
	    if ("text".equals(name) || "combo".equals(name) ||  "category".equals(name) ||  "date".equals(name) || "radio".equals(name) || "check".equals(name) ){
		   if(cf != null){ 
			   customFields.add(cf);
			   step.addCustomField(cf);
		   }
		   cf = null;
	    }		
	}

	public List<CustomField<?>> getCustomFields() {
		return customFields;
	}

	public List<CustomFieldStep> getCustomFieldsSteps() {
		return customFieldsSteps;
	}
   
}
