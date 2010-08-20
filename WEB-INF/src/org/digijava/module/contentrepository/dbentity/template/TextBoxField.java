package org.digijava.module.contentrepository.dbentity.template;

public class TextBoxField extends TemplateField {
	
	public String getType(){
		return "tbf"; //text box field
	}	

	@Override
	public String getRendered() {
		String retVal="<input type=\"text\" name=\"doc_textBox_"+getOrdinalNumber().intValue()+"\"" +
				"size=\"30\" />";		
		return retVal;
	}

}
