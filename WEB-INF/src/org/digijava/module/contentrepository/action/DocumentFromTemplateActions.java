package org.digijava.module.contentrepository.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.contentrepository.dbentity.template.PossibleValue;
import org.digijava.module.contentrepository.dbentity.template.StaticTextField;
import org.digijava.module.contentrepository.dbentity.template.TemplateDoc;
import org.digijava.module.contentrepository.dbentity.template.TemplateField;
import org.digijava.module.contentrepository.form.CreateDocFromTemplateForm;
import org.digijava.module.contentrepository.util.TemplateDocsUtil;

import com.rc.retroweaver.runtime.Collections;

public class DocumentFromTemplateActions extends DispatchAction {
	
	public ActionForward loadTemplates(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		CreateDocFromTemplateForm myForm=(CreateDocFromTemplateForm)form;
		clearForm(myForm);
		List<TemplateDoc> tempDocs=TemplateDocsUtil.getTemplateDocs();
		if(tempDocs!=null && tempDocs.size()>0){
			Collections.sort(tempDocs, new TemplateDocsUtil.HelperTempDocNameComparator());			
		}
		myForm.setTemplates(tempDocs);
		return mapping.findForward("forward");
	}
	
	public ActionForward getTemplate(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		CreateDocFromTemplateForm myForm=(CreateDocFromTemplateForm)form;		
		TemplateDoc tempDoc= TemplateDocsUtil.getTemplateDoc(myForm.getTemplateId());
		myForm.setSelectedTemplate(tempDoc);
		List<TemplateField> fields= new ArrayList<TemplateField>(tempDoc.getFields());
		Collections.sort(fields, new TemplateDocsUtil.TempDocFieldOrdinaryNumberComparator());
		myForm.setFields(fields);
		tempDoc.setFields(new HashSet<TemplateField>(fields) );		
		return mapping.findForward("forward");
	}
	
	public ActionForward saveDocument(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		CreateDocFromTemplateForm myForm=(CreateDocFromTemplateForm)form;
		Map<Integer, String> submittedValuesHolder=new HashMap<Integer, String>();
		List<TemplateField> fields=myForm.getFields();
		//String[] submittedValuesArray=new String[fields.size()];
		for (TemplateField field : fields) {
			if(field instanceof StaticTextField){
				PossibleValue posVal=(PossibleValue)field.getPossibleValues().toArray()[0];
				submittedValuesHolder.put(field.getOrdinalNumber(), posVal.getValue());
				//submittedValuesArray[field.getOrdinalNumber()] = posVal.getValue();
			}
		}
		List<String> requestParameterNames = Collections.list((Enumeration<String>)request.getParameterNames());
		for (String parameter : requestParameterNames) {
			if(parameter.startsWith("doc_")){
				//in case it's multibox and multiple select, then submitted values can be array
			}
		}
		
		clearForm(myForm);
		return null;
	}
	
	private void clearForm(CreateDocFromTemplateForm form){
		form.setSelectedTemplate(null);
		form.setTemplateId(null);
		form.setTemplates(null);
		form.setFields(null);
	}
}
