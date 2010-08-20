package org.digijava.module.contentrepository.dbentity.template;

public class MultipleSelect extends TemplateField {
	
	public String getType(){
		return "msf"; //multiple select field
	}

	@Override
	public String getRendered() {
		String retVal="<select name=\"doc_multiselect_"+getOrdinalNumber().intValue()+"\" styleClass=\"inp-text\" multiple=\"multiple\">";
		retVal+="<option value=\"Select\">" + "<digi:trn>Please Select</digi:trn>" + "</option>";
		if(getPossibleValues()!=null && getPossibleValues().size()>0){
			for (PossibleValue pv : getPossibleValues()) {
				retVal+="<option value=\""+pv.getId()+"\">" + "<digi:trn>"+pv.getValue()+"</digi:trn>" + "</option>";
			}
		}
		retVal+="</select>";
		return retVal;
	}

}
