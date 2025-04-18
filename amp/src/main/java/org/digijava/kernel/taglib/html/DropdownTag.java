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

import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.List;

public class DropdownTag
    extends TagSupport {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(DropdownTag.class);

    protected String name;
    protected String property;
    protected String scope;
    protected List items;

    public static class DropdownItem {
        protected String key;
        protected String value;

        protected DropdownItem() {}

        public DropdownItem(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public int hashCode() {
            return key.hashCode();
        }

        public boolean equals(Object another) {
            if (another instanceof DropdownItem) {
                return key.equals( ( (DropdownItem) another).key);
            }
            else {
                return false;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int doStartTag() throws JspException {
        // Look up the requested property value
        Object value =
            org.apache.struts.taglib.TagUtils.getInstance().lookup(pageContext, name,
            property, scope);
        StringBuffer results = new StringBuffer("<select");

        results.append(" name=\"").append(property).append("\"").append(">");

        results.append("</select>");


        if (value == null) {
            logger.debug("No value defined for bean name: " + name +
                         " property: " + property + " scope: " + scope);
            return SKIP_BODY;
        }

        return SKIP_BODY;
    }


}
