package org.dgfoundation.amp.category;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.helper.CategoryManagerUtil;

public class CategoryValueTagClass extends TagSupport {
	private static Logger logger	= Logger.getLogger( CategoryValueTagClass.class );
	Long categoryValueId;
	
	public Long getCategoryValueId() {
		return categoryValueId;
	}
	public void setCategoryValueId(Long categoryValueId) {
		this.categoryValueId = categoryValueId;
	}
	public int doStartTag() {
		return SKIP_BODY;
	}
	public int doEndTag() throws JspException {
		HttpServletRequest request	= (HttpServletRequest) pageContext.getRequest(); 
			
		if (categoryValueId != null && categoryValueId.longValue() > 0 ) {
			AmpCategoryValue ampCategoryValue	= CategoryManagerUtil.getAmpCategoryValueFromDb( categoryValueId );
			if (ampCategoryValue != null) {
				try{
					JspWriter out					= pageContext.getOut();
					out.print( CategoryManagerUtil.translateAmpCategoryValue(ampCategoryValue, request, null) );
				}
				catch(Exception E){
					logger.info(E);
					E.printStackTrace();
				}
			}
			else
				logger.info("No AmpCategoryValue found with id " + categoryValueId);
		}
		return EVAL_PAGE;
	}
}
