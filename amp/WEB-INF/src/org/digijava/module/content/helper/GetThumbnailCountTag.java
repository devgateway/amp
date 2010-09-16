package org.digijava.module.content.helper;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class GetThumbnailCountTag  extends BodyTagSupport {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private String pageCode;
	
	@Override
	public int doStartTag() throws JspException {
		try {

			JspWriter out = pageContext.getOut();
			String result = "";
			if (this.pageCode != null)
			{
				result = ContentRender.getThumbnailCount(this.pageCode).toString();
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
}
