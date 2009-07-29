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

package org.digijava.module.exception.util;

import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.ExceptionInfo;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ModuleErrorStack {

    private String comment;
    private String name;
    private String email;
    private String issueId;
    private ExceptionInfo exceptionInfo;
    private Site site;

    public ModuleErrorStack() {
    }

    public ModuleErrorStack(ExceptionInfo exceptionInfo, String name, String email, String comment, String issueId, Site site) {
        this.exceptionInfo = exceptionInfo;
        this.name = name;
        this.email = email;
        this.comment = comment;
        this.issueId = issueId;
        this.site = site;
    }

    public String getEmailSubject() throws
        WorkerException {

        TranslatorWorker worker;

        Locale currentLocale = new Locale();
        currentLocale.setCode("en");

        // get newpassword body
        worker = TranslatorWorker.getInstance(
            "exception:subject");
        String subject = worker.getFromGroup(
            "exception:subject",
            currentLocale.getCode(), site,
            "Error {statusCode}, site {siteName}").getMessage();

        return fillPattern(subject);
    }

    public String getEmailMessage(HttpServletRequest request) throws
        WorkerException {

        TranslatorWorker worker;
        String siteName = null;
        Message message;

        Site site = RequestUtils.getSite(request);
        Locale currentLocale = new Locale();
        currentLocale.setCode("en");

        // get newpassword body
        worker = TranslatorWorker.getInstance(
            "exception:body");
        String body = worker.getFromGroup(
            "exception:body",
            currentLocale.getCode(), site,
            "Some error occured while server was processing page \n" +
            "{url} \n\nIssue id {issueId}\n\n{comment}\n\n" +
            "==================== Error Details ====================\n" +
            "Site name: {siteName}(#{siteId}/{siteKey}) \n" +
            "Status code: {statusCode}\n" +
            "Time: {timeStamp}\n" +
            "Url: {url}\n" +
            "Message: {exceptionMessage}\n\n\n" +
            "{stackTrace}").getMessage();

        return fillPattern(body);
    }

    public String fillPattern(String body) {

        HashMap hMap = new HashMap();
        hMap.put("siteName", exceptionInfo.getSiteName());
        hMap.put("siteId", String.valueOf(exceptionInfo.getSiteId()));
        hMap.put("siteKey", exceptionInfo.getSiteKey());
        hMap.put("exceptionMessage", exceptionInfo.getErrorMessage());
        hMap.put("timeStamp", new Date(exceptionInfo.getTimestamp()));
        hMap.put("statusCode", exceptionInfo.getExceptionCode() == null ? "none" : exceptionInfo.getExceptionCode().toString());
        hMap.put("url", exceptionInfo.getSourceURL());
        hMap.put("stackTrace", exceptionInfo.getStackTrace());
        hMap.put("comment", comment == null ? "" : comment);
        hMap.put("name", name);
        hMap.put("email", email);
        hMap.put("issueId", issueId == null ? "" : issueId);

        return DgUtil.fillPattern(body, hMap);

    }
}
