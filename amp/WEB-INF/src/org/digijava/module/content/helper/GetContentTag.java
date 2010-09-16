package org.digijava.module.content.helper;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class GetContentTag  extends BodyTagSupport {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private String pageCode;
	private String attribute;
	
	@Override
	public int doStartTag() throws JspException {
		try {

			JspWriter out = pageContext.getOut();
			String result = "";
			if (this.pageCode != null)
			{
				result = ContentRender.getRenderedContent(pageCode, attribute);
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

	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}

	public String getPageCode() {
		return pageCode;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getAttribute() {
		return attribute;
	}
}
