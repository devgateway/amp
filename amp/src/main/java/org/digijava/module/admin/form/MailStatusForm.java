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

package org.digijava.module.admin.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

public class MailStatusForm
    extends ActionForm {

    private boolean smtpAvailable;
    private int errorMails;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.smtpAvailable = false;
        this.errorMails = 0;
    }

    public int getErrorMails() {
        return errorMails;
    }

    public boolean isSmtpAvailable() {
        return smtpAvailable;
    }

    public void setErrorMails(int errorMails) {
        this.errorMails = errorMails;
    }

    public void setSmtpAvailable(boolean smtpAvailable) {
        this.smtpAvailable = smtpAvailable;
    }

}
