package org.digijava.module.exception.util;

import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import javax.servlet.http.HttpServletRequest;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import java.util.HashMap;
import org.digijava.kernel.util.DgUtil;
import java.util.Date;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ModuleErrorStack {

    public String comment;
    public String name;
    public String email;

    public String stackTrace;
    public Integer statusCode;
    public String message;
    public String url;
    public Date timeStamp;
    public String siteId;
    public String moduleInstance;

    public ModuleErrorStack() {
    }

    public String getEmailSubject(HttpServletRequest request) throws
        WorkerException {

        TranslatorWorker worker;

        Site site = RequestUtils.getSite(request);
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
            "{url} \n\n" +
            "Site name: {siteName}\n" +
            "Status code: {statusCode}\n" +
            "Time: {timeStamp}\n" +
            "Url: {url}\n" +
            "Message: {exceptionMessage}\n\n\n" +
            "{stackTrace}").getMessage();

        return fillPattern(body);
    }

    public String fillPattern(String body) {

        HashMap hMap = new HashMap();
        hMap.put("siteName", getSiteId());
        hMap.put("exceptionMessage", getMessage());
        hMap.put("timeStamp", getTimeStamp());
        hMap.put("statusCode", getStatusCode());
        hMap.put("url", getUrl());
        hMap.put("stackTrace", getStackTrace());
        hMap.put("comment", getComment());
        hMap.put("name", getName());
        hMap.put("email", getEmail());

        return DgUtil.fillPattern(body, hMap);

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Date getTimeStamp() {
        return new Date();
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getModuleInstance() {
        return moduleInstance;
    }
    public void setModuleInstance(String moduleInstance) {
        this.moduleInstance = moduleInstance;
    }
}