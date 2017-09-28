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

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.BaseInputTag;
import org.apache.struts.taglib.html.Constants;

@Deprecated
public class HtmlEditorTag
    extends BaseInputTag {
    private static final long serialVersionUID = 1L;

//  private static Logger logger = Logger.getLogger(HtmlEditorTag.class);

    protected String name = Constants.BEAN_KEY;
    private Integer height = null;
    private Integer width = null;


    public int doStartTag() throws JspException {
        String textToEdit = renderData();

        String componentName = getComponentName();
        String componentId = getComponentId(componentName);
        StringBuffer results = new StringBuffer(512);

        results.append(
            "<script type=\"text/javascript\" src=\"/thirdparty/FCKeditor/fckeditor.js\"></script>");
        results.append("<script type=\"text/javascript\">\n");
        results.append("var " + componentId + " = new FCKeditor('" +
                       componentName + "');\n");
        results.append(componentId + ".BasePath = '/thirdparty/FCKeditor/';\n");
        results.append(componentId + ".Value = '" + textToEdit +
                       "';\n");

        if (height != null) {
            results.append(componentId + ".Height = " + height +
                           ";\n");
        }
        if (width != null) {
            results.append(componentId + ".Width = " + width + ";\n");
        }
        results.append(componentId + ".Create();\n");
        results.append("</script>\n");

        TagUtils.getInstance().write(pageContext, results.toString());

        return EVAL_PAGE;
    }

    private String getComponentId(String name) throws JspException {
        return name.replaceAll("\\W", "_");
    }

    private String getComponentName() throws JspException {
        StringBuffer componentName = new StringBuffer();
        if (indexed) {
            prepareIndex(componentName, name);
        }
        componentName.append(property);
        return componentName.toString();
    }

    public Integer getHeight() {
        return height;
    }
    protected String renderData() throws JspException {

        String data = this.value;

        if (data == null) {
            data = this.lookupProperty(this.name, this.property);
        }
        if (data == null){
            return "";
        }
        data = data.replaceAll("'", "\\\\'");
        data = data.replaceAll("\r\n", "");
        data = data.replaceAll("\n\r", "");
        data = data.replaceAll("\r", "");
        data = data.replaceAll("\n", "");

        return data;
    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        name = Constants.BEAN_KEY;

    }
    public String getName() {
        return name;
    }



    public Integer getWidth() {
        return width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setWidth(Integer width) {
        this.width = width;
    }
}
