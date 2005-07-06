/*
 *   DateTag.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 *   Created: May 4, 2004
 *   CVS-ID: $Id: DropdownTag.java,v 1.1 2005-07-06 10:34:24 rahul Exp $
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
package org.digijava.kernel.taglib.html;

import javax.servlet.jsp.tagext.TagSupport;
import java.util.List;
import javax.servlet.jsp.JspException;
import org.apache.log4j.Logger;

public class DropdownTag
    extends TagSupport {

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
            org.apache.struts.util.RequestUtils.lookup(pageContext, name,
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