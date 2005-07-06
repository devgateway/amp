/*
*   InstanceTag.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: InstanceTag.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
*
*   This file is part of DiGi project (www.digijava.org).
*   DiGi is a multi-site portal system written in Java/J2EE.
*
*   Confidential and Proprietary, Subject to the Non-Disclosure
*   Agreement, Version 1.0, between the Development Gateway
*   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
*   Gateway Foundation, Inc.
*
*   Unauthorized Disclosure Prohibited.
*
*************************************************************************/

package org.digijava.kernel.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.taglib.util.TagUtil;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import java.util.Iterator;
import java.util.Enumeration;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class InstanceTag
    extends TagSupport {

  public static final String PAGE_MODULE_INSTANCE = "page_moduleInstance";

  /**
   * Property key
   */
  private String property = null;

  /**
   * Get property key
   *
   * @return property key
   */
  public String getProperty() {
    return property;
  }

  /**
   * Set Property key
   *
   * @param property property key
   */
  public void setProperty(String property) {
    this.property = property;
  }

  // --------------------------------------------------------- Public Methods

  /**
   * Process the start tag.
   *
   * @return value
   * @throws JspException
   */
  public int doStartTag() throws JspException {


    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    ComponentContext context = ComponentContext.getContext(request);

    String property = getProperty();

    if( property == null || property.length() == 0 ) {

        ActionMapping mapping = (ActionMapping) request.getAttribute(Globals.MAPPING_KEY);
        if (mapping != null) {
            property = mapping.getName();
        }
    }


    Object objectForm = TagUtil.getForm( pageContext, property );

    if (objectForm == null) {
        throw new JspException("Custom tag instance: Form " + property +
                               " not found in any scope");
    }

    /**
     * Set form object in page scope
     */
    if( getId() != null ) {
        pageContext.setAttribute(getId(), objectForm, PageContext.PAGE_SCOPE);
    } else {

        pageContext.setAttribute(property, objectForm);

        pageContext.setAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY, objectForm);
    }


    // Continue processing this page
    return (SKIP_BODY);
  }

  /**
   * Release any acquired resources.
   */
  public void release() {

    super.release();

  }

}