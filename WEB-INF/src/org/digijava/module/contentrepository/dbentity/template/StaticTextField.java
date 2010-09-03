package org.digijava.module.contentrepository.dbentity.template;

public class StaticTextField extends TemplateField {
	
	public String getType(){
		return "stf"; //static text field
	}
	
	public boolean getCanHaveMultipleValues() {
		return false;
	}

	@Override
	public String getRendered() {
		String retVal="<span>";
		retVal+=getValue();
		retVal="</span>";
		return retVal;
	}

}
