package org.dgfoundation.amp.test.customfields;

import junit.framework.TestCase;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.List;


import org.digijava.module.aim.helper.CategoryCustomField;
import org.digijava.module.aim.helper.ComboBoxCustomField;
import org.digijava.module.aim.helper.CustomField;
import org.digijava.module.aim.helper.DateCustomField;
import org.digijava.module.aim.helper.XMLCustomFieldParser;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;


public class XMLCustomFieldParserTest extends TestCase {

	XMLReader xr;
	XMLCustomFieldParser handler;
	StringBuffer sb;
	
	@Override
	protected void setUp() throws Exception {
    	xr = new org.apache.xerces.parsers.SAXParser();
    	handler = new XMLCustomFieldParser();
    	xr.setContentHandler(handler);
    	xr.setErrorHandler(handler);
    	sb = new StringBuffer();
	}
	
	
    public void testMultipleFields() throws SAXException, IOException{
    	XMLReader xr = new org.apache.xerces.parsers.SAXParser();
    	XMLCustomFieldParser handler = new XMLCustomFieldParser();
    	xr.setContentHandler(handler);
    	xr.setErrorHandler(handler);

    	FileReader r = new FileReader("WEB-INF/customFields.xml");
    	xr.parse(new InputSource(r));
    	List<CustomField> cf = handler.getCustomFields();

    	assertEquals(4, cf.size());
    }
   

    public void testTextCustomField() throws SAXException, IOException{    	

    	sb.append("<?xml version=\"1.0\"?>");
    	sb.append("<fields>");
    	sb.append("	<step number=\"1\">");
    	sb.append("		<text name=\"Field1\" description=\"Description1\" property=\"field1\" field=\"Custom Field1\"/>");
    	sb.append("	</step>");
    	sb.append("</fields>");
    	
    	StringReader sr = new StringReader(sb.toString());
    	
    	xr.parse(new InputSource(sr));
    	
    	List<CustomField> customFields = handler.getCustomFields();
    	
    	assertEquals(1, customFields.size());
    	
    	CustomField cf = customFields.get(0);
    	
    	assertEquals(cf.getStep(), 1);
    	assertEquals(cf.getName(), "Field1");
    	assertEquals(cf.getDescription(), "Description1");
    	assertEquals(cf.getAmpActivityPropertyName(), "field1");
    	assertEquals(cf.getFM_field(), "Custom Field1");
    }
    
    public void testComboCustomField() throws SAXException, IOException{    	

    	sb.append("<?xml version=\"1.0\"?>");
    	sb.append("<fields>");
    	sb.append("	<step number=\"1\">");
    	sb.append("  <combo name=\"Field2\" description=\"Description2\" property=\"field2\" field=\"Custom Field2\">");
    	sb.append("   <option value=\"1\">Option1</option>");
    	sb.append("   <option value=\"2\">Option2</option>");
    	sb.append("   <option value=\"3\">Option3</option>");
    	sb.append("  </combo>");
    	sb.append("	</step>");
    	sb.append("</fields>");
    	
    	StringReader sr = new StringReader(sb.toString());
    	
    	xr.parse(new InputSource(sr));
    	
    	List<CustomField> customFields = handler.getCustomFields();
    	
    	assertEquals(1, customFields.size());
    	
    	CustomField cf = customFields.get(0);
    	
    	assertEquals(cf.getStep(), 1);
    	
    	assertTrue(cf instanceof ComboBoxCustomField);
    	
    	ComboBoxCustomField combo = (ComboBoxCustomField) cf;
    	
    	assertEquals(1,combo.getStep());
    	assertEquals("Field2", combo.getName());
    	assertEquals("Description2", combo.getDescription());
    	assertEquals("field2",combo.getAmpActivityPropertyName());
    	assertEquals("Custom Field2", combo.getFM_field());
    	
    	
    	assertEquals(3, combo.getOptions().size());
    	
    	LinkedHashMap<String, String> options = combo.getOptions();
    	
    	assertTrue(options.containsKey("1"));
    	assertTrue(options.containsKey("2"));
    	assertTrue(options.containsKey("3"));
    	
    	assertEquals("Option1", options.get("1"));
    	assertEquals("Option2", options.get("2"));
    	assertEquals("Option3", options.get("3"));
    }    
    
    public void testCategoryCustomField() throws SAXException, IOException{    	

    	sb.append("<?xml version=\"1.0\"?>");
    	sb.append("<fields>");
    	sb.append("	<step number=\"1\">");
    	sb.append("		<category name=\"Field3\" description=\"Description3\" property=\"field3\" field=\"Custom Field3\" category=\"categoryName\"/>");
    	sb.append("	</step>");
    	sb.append("</fields>");
    	
    	StringReader sr = new StringReader(sb.toString());
    	
    	xr.parse(new InputSource(sr));
    	
    	List<CustomField> customFields = handler.getCustomFields();
    	
    	assertEquals(1, customFields.size());
    	
    	assertTrue(customFields.get(0) instanceof CategoryCustomField);
    	
    	CategoryCustomField cf = (CategoryCustomField) customFields.get(0);
    	
    	assertEquals(cf.getStep(), 1);
    	assertEquals(cf.getName(), "Field3");
    	assertEquals(cf.getDescription(), "Description3");
    	assertEquals(cf.getAmpActivityPropertyName(), "field3");
    	assertEquals(cf.getFM_field(), "Custom Field3");
    }    
    
    public void testDateCustomField() throws SAXException, IOException{    	

    	sb.append("<?xml version=\"1.0\"?>");
    	sb.append("<fields>");
    	sb.append("	<step number=\"1\">");
    	sb.append("		<date name=\"Field4\" description=\"Description4\" property=\"field4\" field=\"Custom Field4\"/>");
    	sb.append("	</step>");
    	sb.append("</fields>");
    	
    	StringReader sr = new StringReader(sb.toString());
    	
    	xr.parse(new InputSource(sr));
    	
    	List<CustomField> customFields = handler.getCustomFields();
    	
    	assertEquals(1, customFields.size());
    	
    	assertTrue(customFields.get(0) instanceof DateCustomField);
    	
    	DateCustomField dcf = (DateCustomField) customFields.get(0);
    	
    	assertEquals(dcf.getStep(), 1);
    	assertEquals(dcf.getName(), "Field4");
    	assertEquals(dcf.getDescription(), "Description4");
    	assertEquals(dcf.getAmpActivityPropertyName(), "field4");
    	assertEquals(dcf.getFM_field(), "Custom Field4");
    }    
}
