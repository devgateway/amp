package org.digijava.module.aim.helper;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class FormatDateHelperTag extends BodyTagSupport {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private Timestamp value;
	
	@Override
	public int doStartTag() throws JspException {
		try {

			JspWriter out = pageContext.getOut();
			Timestamp time = getValue();
			String result="";
			if (value!=null)
				result = DateConversion.ConvertDateToString( new Date(value.getTime()) );
			
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

	public void setValue(Timestamp value) {
		this.value = value;
	}

	public Timestamp getValue() {
		return value;
	}
}
