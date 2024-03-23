package org.digijava.module.aim.uicomponents;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;


public class EditContactLink extends BodyTagSupport {
    private static final long serialVersionUID = 1L;
    public static final String PARAM_EDIT_CONTACT_FORM_NAME = "PARAM_EDIT_CONTACT_FORM_NAME";
    public static final String PARAM_COLLECTION_NAME = "PARAM_COLLECTION_NAME";
    public static final String PARAM_CONTACT_ID = "PARAM_CONTACT_ID";
    public static final String PARAM_CONTACT_TYPE="CONTACT_TYPE";
    public static final String ADD_ORG_BUTTON="ADD_ORGANIZATION_BUTTON"; // add/remove organization buttons should be visible or not
    private String collection = "";
    private Object form;
    private String contactId;
    private String contactType="";
    private String addOrgBtn="";

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }


    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public Object getForm() {
        return form;
    }

    public void setForm(Object form) {
        this.form = form;
    }
    
    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getAddOrgBtn() {
        return addOrgBtn;
    }

    public void setAddOrgBtn(String addOrgBtn) {
        this.addOrgBtn = addOrgBtn;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            pageContext.getSession().setAttribute(PARAM_EDIT_CONTACT_FORM_NAME, form);
            JspWriter out = pageContext.getOut();
            StringBuffer html = new StringBuffer();

            html.append("<a href=\"javascript:selectContact('/aim/addAmpContactInfo.do~reset=true~action=edit~");
            if (!"".equalsIgnoreCase(collection)) {

                html.append(PARAM_COLLECTION_NAME);
                html.append("=");
                html.append(collection);
                html.append("~");
            }
            if (contactId!=null&&!"".equalsIgnoreCase(contactId)) {
                html.append(PARAM_CONTACT_ID);
                html.append("=");
                html.append(contactId);
                html.append("~");
            }
            if(!"".equalsIgnoreCase(contactType)){
                html.append(PARAM_CONTACT_TYPE);
                html.append("=");
                html.append(contactType);
                html.append("~");
            }
            
            if(!"".equalsIgnoreCase(addOrgBtn)){
                html.append(ADD_ORG_BUTTON);
                html.append("=");
                html.append(addOrgBtn);
            }           
            html.append("','addContactWindows','height=400,width=600,scrollbars=yes,resizable=yes')\" ");

            out.write(html.toString());
            return super.doStartTag();
        } catch (IOException ioe) {

        }
        return (EVAL_BODY_INCLUDE);

    }

    @Override
    public int doAfterBody() throws JspException {
        // TODO Auto-generated method stub
        return super.doAfterBody();
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print(">" + bodyContent.getString() + "</a>");
        } catch (Exception e) {
                    throw new JspException(e);

        }

        return EVAL_PAGE;
    }

}
