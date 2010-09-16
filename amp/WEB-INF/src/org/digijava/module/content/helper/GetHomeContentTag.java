package org.digijava.module.content.helper;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class GetHomeContentTag  extends BodyTagSupport {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	@Override
	public int doStartTag() throws JspException {
		try {

			JspWriter out = pageContext.getOut();
			String result = "";
			result = ContentRender.getHomePageCode();
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


}
