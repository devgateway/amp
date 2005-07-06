/*
 *   DigiExceptionReportForm.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Jul 4, 2003
 * 	 CVS-ID: $Id: DigiExceptionReportForm.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
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
package org.digijava.module.exception.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import java.util.Date;
import org.digijava.module.exception.util.ModuleErrorStack;
import java.util.HashMap;

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

    public String  layout;
    public HashMap moduleStack;
    public ModuleErrorStack currentModuleStack;

    public DigiExceptionReportForm() {
        currentModuleStack = null;
        moduleStack = new HashMap();
    }

    public String getComment() {
        if( currentModuleStack == null ) return null;
        return currentModuleStack.getComment();
    }

    public void setComment(String comment) {
        if( currentModuleStack == null ) return ;
        currentModuleStack.setComment(comment);
    }

    public String getStackTrace() {
        if( currentModuleStack == null ) return null;
        return currentModuleStack.getStackTrace();
    }

    public void setStackTrace(String stackTrace) {
        if( currentModuleStack == null ) return;
        currentModuleStack.setStackTrace(stackTrace);
    }

    public String getName() {
        if( currentModuleStack == null ) return null;
        return currentModuleStack.getName();
    }

    public void setName(String name) {
        if( currentModuleStack == null ) return;
        currentModuleStack.setName(name);
    }

    public String getEmail() {
        if( currentModuleStack == null ) return null;
        return currentModuleStack.getEmail();
    }

    public void setEmail(String email) {
        if( currentModuleStack == null ) return;
        currentModuleStack.setEmail(email);
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

    }

    public String getMessage() {
        if( currentModuleStack == null ) return null;
        return currentModuleStack.getMessage();
    }

    public void setMessage(String message) {
        if( currentModuleStack == null ) return;
        currentModuleStack.setMessage(message);
    }

    public Integer getStatusCode() {
        if( currentModuleStack == null ) return null;
        return currentModuleStack.getStatusCode();
    }

    public void setStatusCode(Integer statusCode) {
        if( currentModuleStack == null ) return;
        currentModuleStack.setStatusCode(statusCode);
    }

    public Date getTime() {
        if( currentModuleStack == null ) return null;
        return currentModuleStack.getTimeStamp();
    }

    public void setTime(Date time) {
        if( currentModuleStack == null ) return;
        currentModuleStack.setTimeStamp(time);
    }

    public String getUrl() {
        if( currentModuleStack == null ) return null;
        return currentModuleStack.getUrl();
    }

    public void setUrl(String url) {
        if( currentModuleStack == null ) return;
        currentModuleStack.setUrl(url);
    }

    public String getSiteId() {
        if( currentModuleStack == null ) return null;
        return currentModuleStack.getSiteId();
    }

    public void setSiteId(String siteId) {
        if( currentModuleStack == null ) return;
        currentModuleStack.setSiteId(siteId);
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public ModuleErrorStack getExceptionTemplate() {
        return currentModuleStack;
    }

    public void setExceptionTemplate(ModuleErrorStack exceptionTemplate) {
        this.currentModuleStack = exceptionTemplate;
    }

    public ModuleErrorStack getModuleStack(String moduleInstance) {
        currentModuleStack = (ModuleErrorStack)moduleStack.get(moduleInstance);
        return currentModuleStack;
    }

    public HashMap getErrorStck() {
        return this.moduleStack;
    }

    public void setModuleStack(String moduleINstance, ModuleErrorStack moduleStack) {
        this.moduleStack.put(moduleINstance, moduleStack);
    }

}