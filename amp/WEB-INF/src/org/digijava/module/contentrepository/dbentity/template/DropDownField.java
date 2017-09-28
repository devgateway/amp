package org.digijava.module.contentrepository.dbentity.template;

import org.digijava.module.contentrepository.dbentity.template.PossibleValue;

public class DropDownField extends TemplateField {

    public String getType(){
        return "ssf"; //single select field
    }   

    @Override
    public String getRendered() {       
        String retVal="<select name=\"doc_select_"+getOrdinalNumber().intValue()+"\" styleClass=\"inputx\" >";
        retVal+="<option value=\"-Select-\">" + "<digi:trn>Please Select</digi:trn>" + "</option>";
        if(getPossibleValuesList()!=null && getPossibleValuesList().size()>0){
            for (PossibleValue pv : getPossibleValuesList()) {
                retVal+="<option value=\""+pv.getValue()+"\">" + "<digi:trn>"+pv.getValue()+"</digi:trn>" + "</option>";
            }
        }
        retVal+="</select>";
        return retVal;
    }

}
