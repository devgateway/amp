package org.digijava.module.aim.helper;

import java.io.IOException;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class FormatDateHelperTag extends BodyTagSupport {

    /**
     * 
     */
    
    private static final long serialVersionUID = 1L;
    private Date value;

    @Override
    public int doStartTag() throws JspException {
        try {

            JspWriter out = pageContext.getOut();
            String result = "";
            if (value != null) {
                result = DateConversion.convertDateToLocalizedString(value);
            }

            out.write(result);
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

        return EVAL_PAGE;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    public Date getValue() {
        return value;
    }

}
