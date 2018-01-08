package org.digijava.module.aim.uicomponents;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.digijava.kernel.translator.TranslatorWorker;

public class RenderFormSubsectionTag extends BodyTagSupport {
    private static final long serialVersionUID = 1L;
    private String title = "";
    private String styleId = "";
    private String styleClass = "";
    
    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }


    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            StringBuffer html = new StringBuffer();
            html.append(String.format("<div %s class='fields_group content-dir %s'>\n", 
                    this.getStyleId().isEmpty() ? "" : "id = '" + getStyleId() + "'", this.getStyleClass()));
            html.append(String.format("<div class='fields_group_title'>%s</div>\n", TranslatorWorker.translateText(this.getTitle())));
            html.append("<div class='fields_group_contents'>\n");

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
            JspWriter out = pageContext.getOut();
            out.print(bodyContent.getString());
            out.print("</div>\n"); // closing fields_grou_contents
            out.print("</div>\n"); // closing fields_group
        } catch (Exception e) {}

        return EVAL_PAGE;
    }
}
