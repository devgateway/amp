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

package org.digijava.module.exception.form;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.digijava.kernel.exception.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DigiExceptionReportForm
    extends ActionForm {

    private String name;
    private String email;
    private String message;
    private ExceptionInfo exceptionInfo;
    private String issueId;
    private boolean teaserView;
    private Long reportId;
    private Long rand; //random appended to a string - used in GetConfluenceDocs

    public Long getRand() {
        return rand;
    }

    public void setRand(Long rand) {
        this.rand = rand;
    }

    /**
     * Validate user input
     *
     * @param actionMapping
     * @param httpServletRequest
     * @return
     */
    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {

        ActionErrors errors = new ActionErrors();

        return errors.isEmpty() ? null : errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        exceptionInfo = new ExceptionInfo();
        reportId = null;
    }

    public String getEmail() {
        return email;
    }

    public ExceptionInfo getExceptionInfo() {
        return exceptionInfo;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getIssueId() {
        return issueId;
    }

    public boolean isTeaserView() {
        return teaserView;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setExceptionInfo(ExceptionInfo exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public void setTeaserView(boolean teaserView) {
        this.teaserView = teaserView;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

}
