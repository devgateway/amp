package org.digijava.module.contentrepository.dbentity.template;

import org.digijava.module.contentrepository.dbentity.template.PossibleValue;

public class MultiboxField extends TemplateField {

    public String getType(){
        return "mbf"; //multibox field
    }
    
    @Override
    public String getRendered() {
        String retVal="";
        if(getPossibleValuesList()!=null && getPossibleValuesList().size()>0){
            for (PossibleValue pv : getPossibleValuesList()) {
                //submits in request parameter "doc_multibox_5" if multibox  for this template is 5th field.
                retVal+="<input type=\"checkbox\" name=\"doc_multibox_"+getOrdinalNumber().intValue()+"\"" +
                        " value=\""+pv.getValue()+"\">";
                retVal+=pv.getValue();
                retVal+="</input><br>";
            }
        }       
        return retVal;
    }

}
