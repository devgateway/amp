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

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;

/**
 * Custom tag that retrieves internationalized message
 * based on the Key provided. This tag uses methods in DgUtil to determine
 * the language in which the message must be displayed.
 * If the key does not have a translation in the requested language then
 * the message in site default language is shown, else English
 * language message is shown.
 * This Tag also appends an Edit,Translate or CreateTag to the message
 * if a Translator has logged in.
 *
 *
 *
 */

public class ErrorsTag extends org.apache.struts.taglib.html.ErrorsTag {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(ErrorsTag.class);
    
    private static ResourceBundle bundleApplication = ResourceBundle.getBundle("java.resources.application");

    public ErrorsTag() {
    }

    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */

    public int doStartTag() throws JspException {

        int returnValue = EVAL_BODY_INCLUDE;
        ModuleInstance moduleInstanceContext = null;
        ModuleInstance moduleInstanceRequest = null;
        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();
        ComponentContext context = ComponentContext.getContext(request);
        Long umModuleId = (Long)request.getAttribute(Constants.DG_UM_MODULE_ID);

        logger.debug("Start Dump Error TAG");


        // get module name instance name
        if( context != null ) {
            moduleInstanceContext = (ModuleInstance) context.getAttribute(Constants.MODULE_INSTANCE_OBJECT);
            ActionMessages errors = (ActionMessages) context.getAttribute(Globals.MESSAGE_KEY);
            if( errors != null ) {
                logger.debug("set error for teaser");
                request.setAttribute(Globals.MESSAGE_KEY, errors);
                doErrors();
                return super.doStartTag();
            } else {
                logger.debug("no error for teaser");
            }
        } else {
            logger.debug("Sorry, Context is null");
        }

        moduleInstanceRequest = (ModuleInstance) request.getAttribute(Constants.MODULE_INSTANCE_OBJECT);
        // -------------------------

        if( moduleInstanceContext != null ) {
            logger.debug("Context, Module Name is: " +
                         moduleInstanceContext.getModuleName());
            logger.debug("Context, Module Instance is: " +
                         moduleInstanceContext.getInstanceName());
        } else {
            logger.debug("Context, Module is null");
        }
        if( moduleInstanceRequest != null ) {
            logger.debug("Request, Module Name is: " +
                         moduleInstanceRequest.getModuleName());
            logger.debug("Request, Module Instance is: " +
                         moduleInstanceRequest.getInstanceName());
        } else {
            logger.debug("Request, Module is null");
        }

        if( moduleInstanceContext != null && moduleInstanceRequest != null ) {
            if (moduleInstanceContext.getModuleInstanceId().equals(
                moduleInstanceRequest.getModuleInstanceId())) {
                logger.debug("Error tag for module: " + moduleInstanceContext.getModuleName() +
                             ", instance: " + moduleInstanceContext.getInstanceName());
                doErrors();
                return super.doStartTag();
            }
        }
        else if (moduleInstanceContext == null && moduleInstanceRequest != null &&
                 umModuleId == null) {
            logger.debug(" - 1 - ");
            doErrors();
            return super.doStartTag();
        }

        if (umModuleId != null && moduleInstanceContext != null &&
                 umModuleId.equals(moduleInstanceContext.getModuleInstanceId())) {
                 logger.debug(" - 2 - ");
            doErrors();
            return super.doStartTag();
        }

        logger.debug("End Dump Error TAG");

        // Skip the tag anf continue processing this page
        return returnValue;
    }

    /**
     *
     * @return
     * @throws JspException
     */
    public void doErrors() throws JspException {

        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();

        ActionMessages errors = null;
        ActionMessages newErrors = null;
//        Message message;

        try {
            errors = TagUtils.getInstance().getActionMessages(pageContext, name);
        } catch (JspException e) {
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }

        if( errors != null ) {

            Locale currentLocale = RequestUtils.getNavigationLanguage(request);
            Site site = RequestUtils.getSite(request);

            Iterator iter = (property == null) ? errors.get() : errors.get(property);
            newErrors = new ActionMessages();
            while (iter.hasNext()) {
                ActionMessage item = (ActionMessage)iter.next();

                /*newKey = "@" + currentLocale.getCode() + "." + site.getSiteId() + "." + item.getKey();
                logger.debug("New key for error is " + newKey);*/
                
                
                //Add the new string id if needed.
                try {
                    String body = bundleApplication.getString(item.getKey());
                    
                    /**
                     * Constantin: atrocious hack - do not translate html tags from application.properties
                     */
                    String eliminatedPrefix = "";
                    String eliminatedSuffix = "";
                    if (body.toLowerCase().startsWith("<font"))
                    {
                        eliminatedPrefix += body.substring(0, body.indexOf(">") + 1);
                        eliminatedSuffix = "</font>" + eliminatedSuffix;
                        body = body.substring(eliminatedPrefix.length()); // delete prefix
                        body = body.substring(0, body.length() - eliminatedSuffix.length()); // delete suffix
                        body = body.trim();
                    }
                    /**
                     * Constantin: copy paste all day long!
                     */
                    if (body.toLowerCase().startsWith("<li"))
                    {
                        eliminatedPrefix += body.substring(0, body.indexOf(">") + 1);
                        eliminatedSuffix = "</li>" + eliminatedSuffix;
                        body = body.substring(eliminatedPrefix.length()); // delete prefix
                        body = body.substring(0, body.length() - eliminatedSuffix.length()); // delete suffix
                        body = body.trim();
                    }
                    
                    String translatedBody = body;
                    // translate all parts and only afterwards format with additional arguments
                    for (String bodyPart : body.split("\\{[0-9]+\\}")) {
                        bodyPart = bodyPart.trim();
                        bodyPart = StringEscapeUtils.escapeHtml(bodyPart);
                        translatedBody = translatedBody.replace(bodyPart, TranslatorWorker.translateText(bodyPart));
                    }
                    
                    // formatting with custom arguments
                    if (item.getValues() != null) {
                        MessageFormat format = new MessageFormat(translatedBody);
                        translatedBody = format.format(item.getValues());
                    }
                    translatedBody = translatedBody.trim();

                    String errorMsg = eliminatedPrefix + translatedBody + eliminatedSuffix;
                    newErrors.add((property == null) ? Globals.MESSAGE_KEY : property, new ActionMessage(errorMsg, false));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

            if (!newErrors.isEmpty()) {
                request.setAttribute(Globals.ERROR_KEY, newErrors);
            }
        }
    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();

    }
}
