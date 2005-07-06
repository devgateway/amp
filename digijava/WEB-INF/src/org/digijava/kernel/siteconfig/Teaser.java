/*
 *   Teaser.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 22, 2003
 * 	 CVS-ID: $Id: Teaser.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

package org.digijava.kernel.siteconfig;

public class Teaser
    extends ConfigurationItem {
    private String name;
    private String value;
    private String action;
    private boolean renderJSP;

    public Teaser() {
        renderJSP = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "<teaser name=\"" + name + "\" value=\"" + value +
            "\" action=\"" + action + "\" renderJSP=\"" + renderJSP +
            "\"/>";
    }

    public void merge(ConfigurationItem configurationItem) {
        Teaser secondTeaser = (Teaser) configurationItem;

        if (secondTeaser.getValue() != null) {
            value = secondTeaser.getValue();
        }
        if (secondTeaser.getAction() != null) {
            action = secondTeaser.getAction();
        }
    }

    public void validate() throws Exception {};

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public boolean isRenderJSP() {
        return renderJSP;
    }

    public void setRenderJSP(boolean renderJSP) {
        this.renderJSP = renderJSP;
    }

}