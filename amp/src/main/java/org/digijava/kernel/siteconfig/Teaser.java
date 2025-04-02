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
