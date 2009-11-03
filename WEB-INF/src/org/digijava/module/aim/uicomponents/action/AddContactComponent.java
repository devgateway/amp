

package org.digijava.module.aim.uicomponents.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.uicomponents.AddContact;
import org.digijava.module.aim.uicomponents.EditContactLink;
import org.digijava.module.aim.uicomponents.form.AddContactComponentForm;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;


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
         createForm.setFunction(null);
         createForm.setMobilephone(null);
         createForm.setOfficeaddress(null);
         createForm.setContactId(null);
         createForm.setTemporaryId(null);
         createForm.setOrganizations(null);
         Object targetForm = session.getAttribute(AddContact.PARAM_PARAM_FORM_NAME);
         createForm.setTargetForm(targetForm);
         String collection = request.getParameter(AddContact.PARAM_COLLECTION_NAME);
         createForm.setTargetCollection(collection);
         return mapping.findForward("forward");
    }

      public  ActionForward addOrganizations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         AddContactComponentForm createForm = (AddContactComponentForm) form;
         return setAction(createForm, mapping);
    }

     public  ActionForward removeOrganizations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         AddContactComponentForm createForm = (AddContactComponentForm) form;
         Long[] ids=createForm.getSelContactOrgs();
         if(ids!=null){
             for(Long id :ids){
                 AmpOrganisation organization=DbUtil.getOrganisation(id);
                 createForm.getOrganizations().remove(organization);
             }
         }
        return setAction(createForm, mapping);
    }

      public ActionForward edit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AddContactComponentForm createForm = (AddContactComponentForm) form;
        String contId = request.getParameter(EditContactLink.PARAM_CONTACT_ID);
        //AmpContact contact= ContactInfoUtil.getContact(contactId);
        HttpSession session = request.getSession();
        Object targetForm = session.getAttribute(EditContactLink.PARAM_PARAM_FORM_NAME);
        createForm.setTargetForm(targetForm);
        String collection = request.getParameter(EditContactLink.PARAM_COLLECTION_NAME);
        createForm.setTargetCollection(collection);
        Field target = createForm.getTargetForm().getClass().getDeclaredField(createForm.getTargetCollection());
        target.setAccessible(true);
        Collection<AmpContact> targetCollecion = (Collection<AmpContact>) target.get(createForm.getTargetForm());
        Iterator<AmpContact> contcatIter = targetCollecion.iterator();
        while (contcatIter.hasNext()) {
            AmpContact contact = contcatIter.next();
            boolean compareIds=false;
            try{
                  compareIds=contact.getId() != null &&contact.getId().equals(Long.valueOf(contId));
                  if(!compareIds){
                       compareIds=contact.getTemporaryId()!=null&&contact.getTemporaryId().equals(contId);
                  }
            }
            catch(NumberFormatException ex){
                compareIds=contact.getTemporaryId()!=null&&contact.getTemporaryId().equals(contId);
            }

            if(compareIds){
                createForm.setKeyword(null);
                createForm.setContacts(null);
                createForm.setContactId(contact.getId());
                createForm.setTemporaryId(contact.getTemporaryId());
                createForm.setEmail(contact.getEmail());
                createForm.setFax(contact.getFax());
                createForm.setLastname(contact.getLastname());
                createForm.setPhone(contact.getPhone());
                createForm.setName(contact.getName());
                createForm.setOrganisationName(contact.getOrganisationName());
                createForm.setSelContactIds(null);
                if (contact.getTitle() != null) {
                    createForm.setTitle(contact.getTitle().getId());
                }
                createForm.setOrganizations(new ArrayList<AmpOrganisation>());
                if (contact.getOrganizations() != null) {
                    createForm.getOrganizations().addAll(contact.getOrganizations());
                }
                createForm.setFunction(contact.getFunction());
                createForm.setMobilephone(contact.getMobilephone());
                createForm.setOfficeaddress(contact.getOfficeaddress());

            }

        }
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
        int emailCount = ContactInfoUtil.getContactsCount(email,createForm.getContactId());
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
        Collection<AmpContact> sortedtargetCollecion = new TreeSet<AmpContact>(new AmpContactCompare());
        Collection<AmpContact> targetCollecion = (Collection<AmpContact>) target.get(createForm.getTargetForm());
        if (targetCollecion != null) {
            sortedtargetCollecion.addAll(targetCollecion);
        }
        else{
            targetCollecion = new ArrayList<AmpContact>();
        }
        Long[] contIds = createForm.getSelContactIds();
        if (contIds != null && contIds.length > 0) {
            for (int i = 0; i < contIds.length; i++) {
                AmpContact contact = ContactInfoUtil.getContact(contIds[i]);
                if (!sortedtargetCollecion.contains(contact)) {
                    sortedtargetCollecion.add(contact);
                } else {
                    sortedtargetCollecion.remove(contact);
                    sortedtargetCollecion.add(contact);
                }
            }

        }
        targetCollecion.clear();
        targetCollecion.addAll(sortedtargetCollecion);
        target.set(createForm.getTargetForm(), targetCollecion);

        return mapping.findForward("forward");

    }
     public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
          AddContactComponentForm createForm = (AddContactComponentForm) form;
          AmpContact contact=null;
          if(createForm.getContactId()==null||createForm.getContactId()==0){
               contact=new AmpContact();
               if(createForm.getTemporaryId()==null||createForm.getTemporaryId().length()==0){
                   contact.setTemporaryId("_"+new Date().getTime());
               }
               else{
                   contact.setTemporaryId(createForm.getTemporaryId());
               }
            
          }
          else{
               contact=ContactInfoUtil.getContact(createForm.getContactId());
          }
		contact.setName(createForm.getName().trim());
		contact.setLastname(createForm.getLastname().trim());
		contact.setEmail(createForm.getEmail().trim());
                if(createForm.getTitle()!=null&&createForm.getTitle()!=-1){
                    contact.setTitle(CategoryManagerUtil.getAmpCategoryValueFromDb(createForm.getTitle()));
                }
		
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
		if(createForm.getFunction()!=null){
			contact.setFunction(createForm.getFunction().trim());
		}else{
			contact.setFunction(null);
		}
		if(createForm.getMobilephone()!=null){
			contact.setMobilephone(createForm.getMobilephone().trim());
		}else{
			contact.setMobilephone(null);
		}
		if(createForm.getOfficeaddress()!=null){
			contact.setOfficeaddress(createForm.getOfficeaddress().trim());
		}else{
			contact.setOfficeaddress(null);
		}
                if (contact.getOrganizations() == null) {
                     contact.setOrganizations(new HashSet<AmpOrganisation>());
                }
                if (createForm.getOrganizations() != null) {
                    contact.getOrganizations().clear();
                 contact.getOrganizations().addAll(createForm.getOrganizations());
                }
                 Field target = createForm.getTargetForm().getClass().getDeclaredField(createForm.getTargetCollection());
                 target.setAccessible(true);
                 Collection<AmpContact> sortedtargetCollecion = new TreeSet<AmpContact>(new AmpContactCompare());
                 Collection<AmpContact> targetCollecion = (Collection<AmpContact>) target.get(createForm.getTargetForm());
                 if (targetCollecion != null) {
                     sortedtargetCollecion.addAll(targetCollecion);
                 } else {
                     targetCollecion = new ArrayList<AmpContact>();
                 }
                  if (!sortedtargetCollecion.contains(contact)) {
                     sortedtargetCollecion.add(contact);
                 } else {
                     //removing because properties may be changed...
                     sortedtargetCollecion.remove(contact);
                     sortedtargetCollecion.add(contact);
                 }
                    targetCollecion.clear();
                    targetCollecion.addAll(sortedtargetCollecion);

                  target.set(createForm.getTargetForm(), targetCollecion);
                  return mapping.findForward("forward");
               /* ContactInfoUtil.saveOrUpdateContact(contact);
                createForm.setSelContactIds(new Long[]{contact.getId()});
               return addSelectedConts(mapping, form, request, response);*/

     }

    private ActionForward setAction(AddContactComponentForm createForm, ActionMapping mapping) {
        if (createForm.getContactId() == null || createForm.getContactId() == 0) {
            createForm.setAction("create");
        } else {
            createForm.setAction("edit");
        }
        return mapping.findForward("forward");
    }

    class AmpContactCompare implements Comparator<AmpContact> {
        public int compare(AmpContact cont1, AmpContact cont2) {
            if(cont1.getId()!=null&&cont2.getId()!=null){
                return cont1.getId().compareTo(cont2.getId());
            }
            else{
                 if(cont1.getId()==null&&cont2.getId()==null){
                       return cont1.getTemporaryId().compareTo(cont2.getTemporaryId());
                 }
                 else{
                     if(cont1.getId()==null){
                         return -1;
                     }
                     else{
                         return 1;
                     }
                 }
            }
            
        }
    }
}
