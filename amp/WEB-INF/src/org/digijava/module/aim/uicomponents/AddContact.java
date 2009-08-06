

package org.digijava.module.aim.uicomponents;
import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class AddContact extends BodyTagSupport {
    private static final long serialVersionUID = 1L;
    public static final String PARAM_PARAM_FORM_NAME = "PARAM_PARAM_FORM_NAME";
    public static final String PARAM_COLLECTION_NAME = "PARAM_COLLECTION_NAME";
    public static final String STYLE_CLASS_NAME = "CLASS"; //buttons standard class property
    private String collection = "";
    private Object form;
    private String styleClass="";

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
    public int doStartTag() throws JspException {
		try {
                        pageContext.getSession().setAttribute(PARAM_PARAM_FORM_NAME, form);
			JspWriter out = pageContext.getOut();
			StringBuffer html = new StringBuffer();
			html.append("<input type=\"button\" ");
			if(!"".equalsIgnoreCase(styleClass)){
				html.append(STYLE_CLASS_NAME);
				html.append("=");
				html.append("\""+styleClass+"\"");
			}
	
			html.append(" onclick=\"javascript:selectContact('/aim/addAmpContactInfo.do~reset=true~action=create~");
			if (!"".equalsIgnoreCase(collection)) {

				html.append(PARAM_COLLECTION_NAME);
				html.append("=");
				html.append(collection);
				html.append("~");
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
