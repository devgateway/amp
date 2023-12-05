package org.digijava.module.contentrepository.dbentity.template;

public class MultipleSelect extends TemplateField {
    
    public String getType(){
        return "msf"; //multiple select field
    }

    @Override
    public String getRendered() {
        String retVal="<select name=\"doc_multiselect_"+getOrdinalNumber().intValue()+"\" styleClass=\"inputx\" multiple=\"multiple\">";
        if(getPossibleValuesList()!=null && getPossibleValuesList().size()>0){
            for (PossibleValue pv : getPossibleValuesList()) {
                retVal+="<option value=\""+pv.getValue()+"\">" + "<digi:trn>"+pv.getValue()+"</digi:trn>" + "</option>";
            }
        }
        retVal+="</select>";
        return retVal;
    }

}
