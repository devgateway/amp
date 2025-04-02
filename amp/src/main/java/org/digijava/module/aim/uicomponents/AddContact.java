package org.digijava.module.aim.uicomponents;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class AddContact extends BodyTagSupport {
    private static final long serialVersionUID = 1L;
    public static final String PARAM_ADD_CONTACT_FORM_NAME = "PARAM_ADD_CONTACT_FORM_NAME";
    public static final String PARAM_COLLECTION_NAME = "PARAM_COLLECTION_NAME";
    public static final String STYLE_CLASS_NAME = "CLASS"; //buttons standard class property
    public static final String PARAM_CONTACT_TYPE="CONTACT_TYPE";
    public static final String ADD_ORG_BUTTON="ADD_ORGANIZATION_BUTTON"; //add/remove organization buttons should be visible or not 
    private String collection = "";
    private Object form;
    private String styleClass="";
    private String contactType=""; //donor,mofed,sector ministry, Project Coordinator , Implementing/Executing Agency
    private String addOrgBtn="";
    
    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
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

    public int doStartTag() throws JspException {
        try {
            pageContext.getSession().setAttribute(PARAM_ADD_CONTACT_FORM_NAME, form);
            JspWriter out = pageContext.getOut();
            StringBuffer html = new StringBuffer();
            html.append("<input type=\"button\" ");
            if(!"".equalsIgnoreCase(styleClass)){
                html.append(STYLE_CLASS_NAME);
                html.append("=");
                html.append("\""+styleClass+"\"");
            }
    
            html.append(" onclick=\"javascript:selectContact('/aim/addAmpContactInfo.do~reset=true~action=checkDuplicationContacts~");
            if (!"".equalsIgnoreCase(collection)) {

                html.append(PARAM_COLLECTION_NAME);
                html.append("=");
                html.append(collection);
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
            pageContext.getOut().print("value=\"" + bodyContent.getString() + "\"/>");
        } catch (Exception e) {

        }

        return EVAL_PAGE;
    }


}
