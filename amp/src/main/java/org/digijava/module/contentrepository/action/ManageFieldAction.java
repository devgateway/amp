package org.digijava.module.contentrepository.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.contentrepository.dbentity.template.TemplateField;
import org.digijava.module.contentrepository.dbentity.template.TextAreaField;
import org.digijava.module.contentrepository.dbentity.template.TextBoxField;
import org.digijava.module.contentrepository.form.ManageTemplDocFieldForm;
import org.digijava.module.contentrepository.helper.template.PossibleValueHelper;
import org.digijava.module.contentrepository.helper.template.TemplateFieldHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class ManageFieldAction extends Action{
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        ManageTemplDocFieldForm myForm=(ManageTemplDocFieldForm)form;
        String action = request.getParameter("action");
        //get fields from session
        HttpSession session = request.getSession();
        List<TemplateFieldHelper> pendingFields=(List<TemplateFieldHelper>) session.getAttribute("fields");
        
        TemplateFieldHelper field = getTemplateFieldHelperFromList(pendingFields,myForm.getTemplateDocFieldTemporaryId());
        switch (action) {
            case "manage":  //user clicked on "Manage Field" button
                if (myForm.getTemplateDocFieldTemporaryId() != null) {
                    field.setFieldType(myForm.getSelectedFieldType());
                    // fill form possible values with field's values
                    myForm.setPossibleValuesForField(field.getValues());
                }
                break;
            case "deleteValue":  //user wants to remove already existing value of the field
                //fill possible values first
                fillFieldPossibleValues(myForm);
                //remove value
                String valueTempid = request.getParameter("valueId");
                PossibleValueHelper posValToRemove = getTemplateFieldHelperValue(myForm.getPossibleValuesForField(), valueTempid);
                myForm.getPossibleValuesForField().remove(posValToRemove);

                break;
            case "addNewValue":
                if (myForm.getPossibleValuesForField() == null) {
                    myForm.setPossibleValuesForField(new ArrayList<PossibleValueHelper>());
                    myForm.getPossibleValuesForField().add(new PossibleValueHelper("pv_" + new Date().getTime()));
                } else {
                    fillFieldPossibleValues(myForm);

                    //create new array of possible values
                    List<PossibleValueHelper> oldValues = myForm.getPossibleValuesForField();
                    List<PossibleValueHelper> newArray = new ArrayList<PossibleValueHelper>(oldValues.size() + 1);
                    newArray.addAll(oldValues);
                    newArray.add(new PossibleValueHelper("pv_" + new Date().getTime()));
                    myForm.setPossibleValuesForField(newArray);
                }
                break;
            case "saveValues":
                //if this field already has assigned some values etc. we should load it. Otherwise create first empty field
                if (field.getValues() == null) {
                    field.setValues(new ArrayList<PossibleValueHelper>());
                }
                fillFieldPossibleValues(myForm);
                field.setValues(myForm.getPossibleValuesForField());
                //put fields in session
                session.setAttribute("fields", pendingFields);
                return mapping.findForward("fill");
        }
        
        hasAddMoreValuesRight(myForm);
        return mapping.findForward("manageField");      
    }
    
    /**
     * gets TemplateFieldHelper from list, whose temporary_id matches given parameter @param fieldTempId
     */
    private TemplateFieldHelper getTemplateFieldHelperFromList(List<TemplateFieldHelper> fields , String fieldTempId){
        TemplateFieldHelper retVal=null;
        for (TemplateFieldHelper templateFieldHelper : fields) {
            if(templateFieldHelper.getFieldTemporaryId().equals(fieldTempId)){
                retVal=templateFieldHelper;
                break;
            }
        }
        return retVal;
    }
    
    /**
     * fill fields possible pre-defined values
     * @param myForm
     */
    private void fillFieldPossibleValues(ManageTemplDocFieldForm myForm) {
        String[] preDefinedValue=myForm.getPreDefinedValue();
         for(ListIterator iter = myForm.getPossibleValuesForField().listIterator(); iter.hasNext(); ) {
             PossibleValueHelper pfh = (PossibleValueHelper) iter.next();                       
             pfh.setPreDefinedValue(preDefinedValue[iter.nextIndex() - 1]);
        }
    }
    
    /**
     * check whether this field is allowed to have predefined values or have more values then it has now
     */
    private void hasAddMoreValuesRight(ManageTemplDocFieldForm myForm) throws ClassNotFoundException,InstantiationException, IllegalAccessException {
        Class clazz = Class.forName(myForm.getSelectedFieldType());
        TemplateField templateField=(TemplateField) clazz.newInstance();
        if(!templateField.getCanHaveMultipleValues()){
            if (templateField.getClass().getName().equals(TextBoxField.class.getName()) || templateField.getClass().getName().equals(TextAreaField.class.getName())
                    || (myForm.getPossibleValuesForField()!=null && myForm.getPossibleValuesForField().size()>0)){
                myForm.setHasAddModeValuesRight(false);
            }else{
                myForm.setHasAddModeValuesRight(true);
            }
        }else{
            myForm.setHasAddModeValuesRight(true);
        }
    }
    
    /**
     * gets PossibleValueHelper from list, whose temporary_id matches given parameter @param valueId
     */
    private PossibleValueHelper getTemplateFieldHelperValue(List<PossibleValueHelper> values , String valueId){
        PossibleValueHelper retVal=null;
        for (PossibleValueHelper val : values) {
            if(val.getTempId().equals(valueId)){
                retVal=val;
                break;
            }
        }
        return retVal;
    }
}
