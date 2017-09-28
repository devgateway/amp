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

package org.digijava.kernel.taglib.html;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;
import org.digijava.kernel.taglib.util.TagUtil;

/**
 * Created by IntelliJ IDEA.
 * User: lasha
 * Date: Feb 28, 2005
 * Time: 4:44:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchTag
    extends BodyTagSupport {

    private static final long serialVersionUID = 1L;
    private String module;
    private String instance;
    private String name;

    /**
     * Generate the required input tag.
     * <p>
     * Support for indexed property since Struts 1.1
     *
     * @exception javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public int doStartTag() throws javax.servlet.jsp.JspException {

        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();

        // Create an appropriate "input" element based on our parameters
        StringBuffer results = new StringBuffer(1024);
        StringBuffer context = new StringBuffer(1024);

        context.append(TagUtil.generateContext(request, pageContext,
                                                 "context/search/default/viewResults.do"));

        results.append("<form action=\"" + context + "\">");

        if (module != null && module.length() > 0) {
             results.append("<input type=\"hidden\" name=\"module\" value=\"" + module + "\">");
        }


        // Print this field to our output writer
        TagUtils.getInstance().write(pageContext, results.toString());

        // Continue processing this page
        return (EVAL_PAGE);
    }

    public int doEndTag() throws JspException {

      // Print this field to our output writer
      BodyContent content = getBodyContent();
      TagUtils.getInstance().write(pageContext, content.getString() + "</form>");
      return EVAL_PAGE;
    }


    /**
     * Release any acquired resources.
     */
    public void release() {

    }

    public String getModule() {
        return module;
    }

    public String getInstance() {
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public void setName(String name) {
        this.name = name;
    }

}
