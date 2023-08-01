package org.digijava.module.contentrepository.action;

import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.contentrepository.dbentity.template.PossibleValue;
import org.digijava.module.contentrepository.dbentity.template.TemplateDoc;
import org.digijava.module.contentrepository.dbentity.template.TemplateField;
import org.digijava.module.contentrepository.form.TemplateDocManagerForm;
import org.digijava.module.contentrepository.helper.template.PossibleValueHelper;
import org.digijava.module.contentrepository.helper.template.TemplateConstants;
import org.digijava.module.contentrepository.helper.template.TemplateDocumentHelper;
import org.digijava.module.contentrepository.helper.template.TemplateFieldHelper;
import org.digijava.module.contentrepository.util.TemplateDocsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

public class TemplateDocActions extends DispatchAction {
    
    public ActionForward viewTemplateDocuments (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        TemplateDocManagerForm myForm=(TemplateDocManagerForm)form;
        List<TemplateDoc> tempDocs=TemplateDocsUtil.getTemplateDocs();
        if(tempDocs!=null && tempDocs.size()>0){
            Collections.sort(tempDocs, new TemplateDocsUtil.HelperTempDocNameComparator());         
        }
        myForm.setTemplates(tempDocs);
        //clear session if needed
        HttpSession session = request.getSession();
        if(session.getAttribute("fields")!=null){
            session.removeAttribute("fields");
        }
        return mapping.findForward("list");
    }
    
    public ActionForward addTemplateDocument (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        TemplateDocManagerForm myForm=(TemplateDocManagerForm)form;
        cleanForm(myForm);
        //clear session if needed
        HttpSession session = request.getSession();
        if(session.getAttribute("fields")!=null){
            session.removeAttribute("fields");
        }
        return mapping.findForward("createEdit");
    }
    
    public ActionForward editTemplateDocument (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        TemplateDocManagerForm myForm=(TemplateDocManagerForm)form;
        //fill possible field types
        List<LabelValueBean> availableFields= new ArrayList<LabelValueBean>();     
         for (int i=0;i<TemplateConstants.availableFieldClasses.length;i++) {
            LabelValueBean lvb=new LabelValueBean(TemplateConstants.availableFieldTypes[i],TemplateConstants.availableFieldClasses[i].getName());
            availableFields.add(lvb);
        }
         myForm.setAvailableFields(availableFields);
        TemplateDoc templateDocument= TemplateDocsUtil.getTemplateDoc(myForm.getTemplateId());
        myForm.setTemplateName(templateDocument.getName());
        //fields of template
        List<TemplateFieldHelper> pendingFields=new ArrayList<TemplateFieldHelper>();       
        Set<TemplateField> fields=templateDocument.getFields(); //template should have at least one field       
        for (TemplateField dbField : fields) {
            TemplateFieldHelper tfHelper=new TemplateFieldHelper();
            tfHelper.setDbId(dbField.getId());
            tfHelper.setFieldTemporaryId("db_"+dbField.getId()) ; //"db_" means this field is saved in db, "temp_" means it's a new field for this template
            tfHelper.setOrdinalNumber(dbField.getOrdinalNumber());
            tfHelper.setFieldType(dbField.getClass().getName());
            //tfhelper pre-defined values
            Set<PossibleValue> dbFieldPreDefinedVals=dbField.getPossibleValues();           
            if(dbFieldPreDefinedVals!=null && dbFieldPreDefinedVals.size()>0){
                tfHelper.setValues(new ArrayList<PossibleValueHelper>());
                for (PossibleValue dbPosVal : dbFieldPreDefinedVals) {
                    PossibleValueHelper pvHelper=new PossibleValueHelper();
                    pvHelper.setDbId(dbPosVal.getId());
                    pvHelper.setTempId("dbpv_"+dbPosVal.getId());
                    pvHelper.setPreDefinedValue(dbPosVal.getValue());
                    tfHelper.getValues().add(pvHelper);
                }
            }
            pendingFields.add(tfHelper);
        }
        myForm.setPendingFields(pendingFields);
        //put fields in session
        HttpSession session = request.getSession();
        session.setAttribute("fields", pendingFields);
        
        //sort with ordinary number
        Collections.sort(myForm.getPendingFields(), new TemplateDocsUtil.HelperTempDocFieldOrdinaryNumberComparator());
        return mapping.findForward("createEdit");
    }
    
    public ActionForward saveTemplateDocument (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        TemplateDocManagerForm myForm=(TemplateDocManagerForm)form;
        //validations
        ActionErrors errors= new ActionErrors();
        String tempName=myForm.getTemplateName(); //name should be unique
        TemplateDoc tempDoc=TemplateDocsUtil.getTemplateDocByName(tempName);
        if(tempDoc!=null || tempName==null){
            if (myForm.getTemplateId()==null && myForm.getTemplateName()==null){
                return viewTemplateDocuments(mapping, myForm, request, response);
            }else{
                if(!tempDoc.getId().equals(myForm.getTemplateId())){    
                    errors.add("name not unique", new ActionMessage("cr.templateName.exists",TranslatorWorker.translateText("Template With Given Name Already Exists")));
                }           
            }
        }
        
        //for each field check whether it's allowed not to have pre-defined values
        List<TemplateFieldHelper> fields= myForm.getPendingFields();
        if(fields!=null){
            for (TemplateFieldHelper fieldHelper : fields) {
                if (fieldHelper.getValues()==null || fieldHelper.getValues().size()==0) {
                    Class clazz = Class.forName(fieldHelper.getFieldType());
                    TemplateField templateField=(TemplateField) clazz.newInstance();
                    if(!templateField.getHasEmptyPossibleValsRights()){
                        String fieldTypeDisplayName=getFieldDisplayName(templateField.getClass());
                        errors.add("predefined values must exist", new ActionMessage("cr.templateField.predefinedValDoesnotExist",fieldTypeDisplayName+" "+TranslatorWorker.translateText("field must have pre-defined values")));
                    }
                }
            }
        }
        
        if (errors.size() > 0){
            //we have all the errors for this step saved and we must throw the amp error
            saveErrors(request, errors);
            return mapping.findForward("createEdit");
        }

        fillFieldTypes(myForm); //fill ceckbox submitted values
        TemplateDocumentHelper tempDocHelper=new TemplateDocumentHelper(myForm.getTemplateName());
        tempDocHelper.setId(myForm.getTemplateId()); //to be removed ?
        tempDocHelper.setFields(fields);
        TemplateDocsUtil.saveTemplateDoc(tempDocHelper);
        
        //remove fields from map
        HttpSession session = request.getSession();
        session.removeAttribute("fields");
        myForm.setTemplateId(null);
        myForm.setTemplateName(null);
        return viewTemplateDocuments(mapping, myForm, request, response);
    }
    
    public ActionForward deleteTemplateDocument (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        TemplateDocManagerForm myForm=(TemplateDocManagerForm)form;
        Long templateId=myForm.getTemplateId();
        TemplateDoc doc=TemplateDocsUtil.getTemplateDoc(templateId);
        DbUtil.delete(doc);
        //clear session if needed
        HttpSession session = request.getSession();
        if(session.getAttribute("fields")!=null){
            session.removeAttribute("fields");
        }
        return viewTemplateDocuments(mapping, myForm, request, response);
    }
    
    public ActionForward addTemplateDocumentField (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        TemplateDocManagerForm myForm=(TemplateDocManagerForm)form;
            if(myForm.getPendingFields()==null){
                 myForm.setPendingFields(new ArrayList<TemplateFieldHelper>());
             }else{
                 String[] submittedFieldTypes=myForm.getFieldType();
                 for(ListIterator iter = myForm.getPendingFields().listIterator(); iter.hasNext(); ) {
                     TemplateFieldHelper tfh = (TemplateFieldHelper) iter.next();                       
                     tfh.setFieldType(submittedFieldTypes[iter.nextIndex() - 1]);
                 }
            }
            
            TemplateFieldHelper tf= new TemplateFieldHelper();
            tf.setFieldTemporaryId("temp_"+new Date().getTime());
            tf.setOrdinalNumber(myForm.getPendingFields().size());
            //fill possible field types
            List<LabelValueBean> availableFields= new ArrayList<LabelValueBean>();     
             for (int i=0;i<TemplateConstants.availableFieldClasses.length;i++) {
                LabelValueBean lvb=new LabelValueBean(TemplateConstants.availableFieldTypes[i],TemplateConstants.availableFieldClasses[i].getName());
                availableFields.add(lvb);
            }
             myForm.setAvailableFields(availableFields);
             myForm.setPreDefinedValue(null);            
             myForm.getPendingFields().add(tf);
        //put fields in session
        HttpSession session = request.getSession();
        session.setAttribute("fields", myForm.getPendingFields());
        return mapping.findForward("createEdit");
    }
    
    public ActionForward editTemplateDocumentField (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        TemplateDocManagerForm myForm=(TemplateDocManagerForm)form;     
        if(myForm.getTemplateDocFieldTemporaryId()!=null){
            TemplateFieldHelper fieldToBeChanged=getTemplateFieldHelperFromList(myForm.getPendingFields(), myForm.getTemplateDocFieldTemporaryId());
            int fieldOrdinalNumber=fieldToBeChanged.getOrdinalNumber();
            //need to get element from fieldType array with ordinalNumber=fieldOrdinalNumber and replace fieldToBeChanged's type with fieldType[fieldOrdinalNumber]
            String selFieldType=request.getParameter("selFieldType");
            fieldToBeChanged.setFieldType(selFieldType);
            fieldToBeChanged.setValues(null);
        }
        //put fields in session
        HttpSession session = request.getSession();
        session.setAttribute("fields", myForm.getPendingFields());
        return mapping.findForward("createEdit");
    }
    
    public ActionForward deleteTemplateDocumentField (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        TemplateDocManagerForm myForm=(TemplateDocManagerForm)form;
        String [] fieldsIdsToRemove=myForm.getSelectedFieldsIds();
        if(fieldsIdsToRemove!=null && fieldsIdsToRemove.length>0){
            for (String fieldTempId : fieldsIdsToRemove) {
                List<TemplateFieldHelper> newFields= removeFieldFromTemplate(myForm.getPendingFields(), fieldTempId);
                myForm.setPendingFields(newFields);
            }
        }
        //put fields in session
        HttpSession session = request.getSession();
        session.setAttribute("fields", myForm.getPendingFields());
        return mapping.findForward("createEdit");
    }

    
    public ActionForward fillFormsPendingFieldsFromMap (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        TemplateDocManagerForm myForm=(TemplateDocManagerForm)form;
        HttpSession session = request.getSession();
        List<TemplateFieldHelper> pendingFields=(List<TemplateFieldHelper>) session.getAttribute("fields");
        myForm.setPendingFields(pendingFields);
        return mapping.findForward("createEdit");
    }
    
    /**
     * fill document's field's type
     * @param myForm
     */
    private void fillFieldTypes(TemplateDocManagerForm myForm) {
        String[] fieldTypes=myForm.getFieldType();
         for(ListIterator iter = myForm.getPendingFields().listIterator(); iter.hasNext(); ) {
             TemplateFieldHelper tfh = (TemplateFieldHelper) iter.next();                       
             tfh.setFieldType(fieldTypes[iter.nextIndex() - 1]);
        }
    }
    
    
    private void cleanForm(TemplateDocManagerForm myForm){
        myForm.setAvailableFields(null);
//      myForm.setFields(null);
//      myForm.setFieldToAddOrEdit(null);
        myForm.setPendingFields(null);
        myForm.setTemplateId(null);
        myForm.setTemplateName(null);
        myForm.setTemplates(null);
        myForm.setTemplateDocFieldTemporaryId(null);
    }
    
    private List<TemplateFieldHelper> removeFieldFromTemplate(List<TemplateFieldHelper> fields , String fieldTemporaryId){
        int fieldOrdinalNumber= -1;
        for (TemplateFieldHelper templateFieldHelper : fields) {
            if(templateFieldHelper.getFieldTemporaryId().equals(fieldTemporaryId)){
                fieldOrdinalNumber=templateFieldHelper.getOrdinalNumber();
                fields.remove(templateFieldHelper);
                break;
            }
        }
        //now we should update ordinal numbers of other fields, which came in the list after removed field . they should be decreased by 1.
        for (TemplateFieldHelper templateFieldHelper : fields) {
            if (templateFieldHelper.getOrdinalNumber()>fieldOrdinalNumber){
                templateFieldHelper.setOrdinalNumber(templateFieldHelper.getOrdinalNumber()-1);
            }
        }
        return fields;
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
     * returns field's fieldType display name, for example for the fieldType=org.digijava.module.contentrepository.dbentity.template.MultiboxField wil return multibox
     * @return
     */
    private String getFieldDisplayName(Class fieldClass){
        String retVal=null;
        Class [] availableFieldClasses =TemplateConstants.availableFieldClasses;
        String [] availableFieldTypes = TemplateConstants.availableFieldTypes;
        for (int i=0;i<availableFieldClasses.length;i++) {
            if(availableFieldClasses[i].getName().equals(fieldClass.getName())){
                retVal= availableFieldTypes[i];
                break;
            }
        }
        return retVal;
    }
}
