package org.digijava.module.contentrepository.dbentity.template;

public class CheckboxField extends TemplateField {
    
    public String getType(){
        return "chf"; //checkbox field
    }
    
    public boolean getCanHaveMultipleValues() {
        return false;
    }
    
    @Override
    public String getRendered() {
        String retVal=null;
        if(getPossibleValuesList()!=null){
            for (PossibleValue posVal : getPossibleValuesList()) {
            //submits in request parameter "doc_checkbox_5" if checkbox  for this template is 5th field.            
            retVal="<input type=\"checkbox\" name=\"doc_checkbox_"+getOrdinalNumber().intValue()+"\" value=\""+posVal.getValue()+"\">";         
                retVal+="<span class=\"t_sm\">"+posVal.getValue()+"</span>";
            }
            retVal+="</input>";
        }       
        return retVal;
    }
}
