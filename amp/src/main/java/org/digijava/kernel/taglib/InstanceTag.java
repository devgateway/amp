/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.taglib.util.TagUtil;

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

    private static final long serialVersionUID = 1L;

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
//    ComponentContext context = ComponentContext.getContext(request);

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
