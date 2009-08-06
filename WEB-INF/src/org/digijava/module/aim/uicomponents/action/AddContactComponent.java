

package org.digijava.module.aim.uicomponents.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.uicomponents.AddContact;
import org.digijava.module.aim.uicomponents.form.AddContactComponentForm;
import org.digijava.module.aim.util.ContactInfoUtil;


public class AddContactComponent extends DispatchAction{
     private static Logger logger = Logger.getLogger(AddContactComponent.class);
     public static final String ROOT_TAG = "CONTACTS";

    @Override
     protected  ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return create(mapping, form, request, response);
    }

     public  ActionForward create(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         AddContactComponentForm createForm = (AddContactComponentForm) form;
         HttpSession session = request.getSession();
         createForm.setKeyword(null);
         createForm.setContacts(null);
         createForm.setEmail(null);
         createForm.setFax(null);
         createForm.setLastname(null);
         createForm.setPhone(null);
         createForm.setName(null);
         createForm.setOrganisationName(null);
         createForm.setSelContactIds(null);
         createForm.setTitle(null);
         Object targetForm = session.getAttribute(AddContact.PARAM_PARAM_FORM_NAME);
         createForm.setTargetForm(targetForm);
         String collection = request.getParameter(AddContact.PARAM_COLLECTION_NAME);
         createForm.setTargetCollection(collection);
         return mapping.findForward("forward");
    }
    public ActionForward search(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AddContactComponentForm createForm = (AddContactComponentForm) form;
        String keyword = createForm.getKeyword();

        if (keyword != null) {
            List<AmpContact> foundContacts = ContactInfoUtil.searchContacts(keyword);
            createForm.setContacts(foundContacts);
        }
        return mapping.findForward("forward");

    }
    public ActionForward checkDulicateEmail(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AddContactComponentForm createForm = (AddContactComponentForm) form;
        String email = createForm.getEmail();
        int emailCount = ContactInfoUtil.getContactsCount(email);
        String contactEmail = null;
        if (emailCount > 0) {
            contactEmail = "exists";
        } else {
            contactEmail = "notExists";
        }
        //creating xml that will be returned
        response.setContentType("text/xml");
        OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
        PrintWriter out = new PrintWriter(outputStream, true);
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xml += "<" + ROOT_TAG + ">";
        xml += "<" + "contact email=\"" + contactEmail + "\" />";
        xml += "</" + ROOT_TAG + ">";
        out.println(xml);
        out.close();
        // return xml
        outputStream.close();
        return null;

    }
    public ActionForward addSelectedConts(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AddContactComponentForm createForm = (AddContactComponentForm) form;
        Field target = createForm.getTargetForm().getClass().getDeclaredField(createForm.getTargetCollection());
        target.setAccessible(true);
        Collection<AmpContact> targetCollecion = (Collection<AmpContact>) target.get(createForm.getTargetForm());

        if (targetCollecion == null) {
            targetCollecion = new ArrayList<AmpContact>();
        }

        Long[] contIds = createForm.getSelContactIds();

        if (contIds != null && contIds.length > 0) {
            for (int i = 0; i < contIds.length; i++) {
                AmpContact contact = ContactInfoUtil.getContact(contIds[i]);
                if (!targetCollecion.contains(contact)) {
                    targetCollecion.add(contact);
                }
            }

        }

        target.set(createForm.getTargetForm(), targetCollecion);
   
        return mapping.findForward("forward");

    }
     public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
          AddContactComponentForm createForm = (AddContactComponentForm) form;
         AmpContact contact=new AmpContact();
		contact.setName(createForm.getName().trim());
		contact.setLastname(createForm.getLastname().trim());
		contact.setEmail(createForm.getEmail().trim());
		contact.setTitle(createForm.getTitle().trim());

		if(createForm.getOrganisationName()!=null){
			contact.setOrganisationName(createForm.getOrganisationName().trim());
		}else{
			contact.setOrganisationName(null);
		}
		if(createForm.getPhone()!=null){
			contact.setPhone(createForm.getPhone().trim());
		}else{
			contact.setPhone(null);
		}
		if(createForm.getFax()!=null){
			contact.setFax(createForm.getFax().trim());
		}else{
			contact.setFax(null);
		}
                ContactInfoUtil.saveOrUpdateContact(contact);
                createForm.setSelContactIds(new Long[]{contact.getId()});
               return addSelectedConts(mapping, form, request, response);

     }


}
