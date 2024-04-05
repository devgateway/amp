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

package org.digijava.kernel.translator.form;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 *
 * @author shamanth.murthy@mphasis.com
 * @version
 */

public final class TranslatorNavForm extends ActionForm {
    private static final long serialVersionUID = 1L;

    private Collection navigation = null;

    private String strSelectedData = null;

    /**
    * Default Constructor
    *
    */
    public TranslatorNavForm() {
    }
    public Collection getLocales() {
        return navigation;
    }

    public void setLocales(Collection col) {
        this.navigation = col;
    }

    public String getLocalesSelected() {
        return strSelectedData;
    }

    public void setLocalesSelected(String strSelectedData) {
        this.strSelectedData = strSelectedData;
    }

    /**
    * Reset all properties to their default values.
    *
    * @param mapping The mapping used to select this instance
    * @param request The servlet request we are processing
    */

    public void reset(ActionMapping mapping, HttpServletRequest request) {

    }

    /**
    * Validate inputs
    *
    * @param mapping
    * @param request
    * @return
    */

    public ActionErrors validate(
        ActionMapping mapping,
        HttpServletRequest request) {
        return null;

    }

}
