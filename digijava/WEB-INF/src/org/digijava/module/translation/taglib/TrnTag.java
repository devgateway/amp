/*
 *   TrnTag.java
 *   @Author Shamanth Murthy shamanth.murthy@mphasis.com
 *   Created: Sep 24, 2003
 *   CVS-ID: $Id: TrnTag.java,v 1.1 2005-07-06 10:34:18 rahul Exp $
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

package org.digijava.module.translation.taglib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;

/**
 * Custom tag that retrieves internationalized message
 * based on the Key provided. This tag uses methods in DgUtil to determine
 * the language in which the message must be displayed.
 * If the key does not have a translation in the requested language then
 * the message in site default language is shown, else English
 * language message is shown.
 * This Tag also appends an Edit,Translate or CreateTag to the message
 * if a Translator has logged in.
 */
public class TrnTag
    extends BodyTagSupport {

    private static final Logger logger = Logger.getLogger(TrnTag.class);

    private static final int NUMBER_OF_PARAMETERS = 5;

    private static final String LOCAL_TRANSLATION = "local";
    private static final String GROUP_TRANSLATION = "group";

    private String key = null;
    private Boolean linkAlwaysVisible = null;
    private String type = null;
    private String siteId = null;
    private String bodyText = null;

    private String[] args;

    private boolean groupTranslator = false;
    private boolean localTranslator = false;
    private boolean globalAdmin = false;
    private boolean showLinks = false;
    private HttpServletRequest request = null;
    private String backUrl = null;

    private int max;


    public TrnTag() {
        args = new String[NUMBER_OF_PARAMETERS];
        linkAlwaysVisible = new Boolean(false);
        max = 0;
    }

    /**
     * Gets the translation key
     * @return
     */
    public String getKey() {
        return (this.key);
    }

    /**
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getLinkAlwaysVisible() {
        return linkAlwaysVisible;
    }

    public void setLinkAlwaysVisible(Boolean linkAlwaysVisible) {
        this.linkAlwaysVisible = linkAlwaysVisible;
    }

    /**
     * Gets the type of a message
     * Only Group and Local are allowed
     * @return
     */
    public String getType() {

        //Check if the type is null
        if (this.type == null) {
            this.type = GROUP_TRANSLATION;
        }

        return (this.type);
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public String getSiteId() {
        return siteId;
    }

    /**
     *
     * @param siteId
     */
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * First argument used for formatting
     */
    public String getArg1() {
        return (this.args[0]);
    }

    /**
     * First argument used for formatting
     * @param String
     */
    public void setArg1(String arg1) {
        this.args[0] = arg1;
    }

    /**
     * Second argument used for formatting args
     */
    public String getArg2() {
        return (this.args[1]);
    }

    /**
     * Second argument used for formatting
     * @param String
     */
    public void setArg2(String arg2) {
        this.args[1] = arg2;
    }

    /**
     * Third argument used for formatting
     */
    public String getArg3() {
        return (this.args[2]);
    }

    /**
     * Third argument used for formatting
     *
     * @param String
     */
    public void setArg3(String arg3) {
        this.args[2] = arg3;
    }

    /**
     * Fourth argument used for formatting
     */
    public String getArg4() {
        return (this.args[3]);
    }

    /**
     * Third argument used for formatting
     *
     * @param String
     */
    public void setArg4(String arg4) {
        this.args[3] = arg4;
    }

    /**
     * Third argument used for formatting
     */
    public String getArg5() {
        return (this.args[4]);
    }

    /**
     * Third argument used for formatting
     *
     * @param String
     */
    public void setArg5(String arg5) {
        this.args[4] = arg5;
    }

    /**
     *
     * @param bodyText
     */
    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String getBodyText() {
        return this.bodyText;
    }
    /**
     * maximal length of visible translation
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }
    public int getMax() {
        return max;
    }

    /**
     * Process the start of this tag.
     * @return int
     */
    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */

    public int doAfterBody() {

        BodyContent body = getBodyContent();
        request = (HttpServletRequest) pageContext.getRequest();

        boolean localeSupported = true;

        //Compute back url...
        backUrl = "";
        showLinks = TranslatorWorker.isTranslationMode(request);
        if( !showLinks ) {
            showLinks = linkAlwaysVisible.booleanValue();
        }

        // determine User permissions
        if (showLinks) {
            backUrl = RequestUtils.getRelativeSourceURL(request);

            if (siteId != null) {
                Site site = null;
                try {
                    site = SiteUtils.getSite(siteId);
                }
                catch (DgException ex) {
                    ex.printStackTrace();
                    return (SKIP_BODY);
                }
                groupTranslator = DgUtil.isGroupTranslatorForSite(request, site);
                localTranslator = DgUtil.isLocalTranslatorForSite(request, site);
            }
            else {
                groupTranslator = DgUtil.isGroupTranslatorForSite(request);
                localTranslator = DgUtil.isLocalTranslatorForSite(request);
            }
            User user = RequestUtils.getUser(request);
            if (user != null) {
                globalAdmin = user.isGlobalAdmin();

            }
        }

        //Determine Locale from dgutil
        org.digijava.kernel.entity.Locale language =
            RequestUtils.getNavigationLanguage(request);

        String actualType = getActualType();
        logger.debug("tag type is: " + getType() + " actual type is: " +
                     actualType);

        //logic to retreive appropriate message.
        //If siteId attribute is set for the tag, translation is always local
        if (LOCAL_TRANSLATION.equalsIgnoreCase(actualType)) {
            checkData(language.getCode(), true,
                      LOCAL_TRANSLATION, body);
        }
        else {
            //its group
            boolean displayed = checkData( language.getCode(),
                                           false, LOCAL_TRANSLATION, body);

            if (displayed) {
                logger.debug("Local translatin EXISTS");
            }
            else {
                logger.debug("Local translation DOES NOT EXIST");
                displayed = checkData(language.getCode(),
                                      true, GROUP_TRANSLATION, body);
                logger.debug("Displayed: " + displayed);
            }
        }
        // Skip the tag and continue processing this page
        return (SKIP_BODY);
    }

    /**
     * Formats message text, appends suffix and prints the result
     * @param message Message
     * @param suffix String
     * @param body BodyContent
     */
    private void writeData(Message message, String suffix, BodyContent body) {
        String newSuffix = suffix;
        if (suffix == null) {
            newSuffix = "";
        }
        if (message.getMessage() == null) {// ||
//            message.getMessage().trim().length() == 0) {
            writeData("key:" + message.getKey() + newSuffix, body);
        }
        else {
            writeData(format(message.getMessage()) + newSuffix, body);
        }
    }

    /**
     * Writes given string to the page output
     * @param localizedMsg
     * @param body
     */
    private void writeData(String localizedMsg, BodyContent body) {

        try {
            JspWriter out = body.getEnclosingWriter();
            out.print(localizedMsg);
        }
        catch (IOException ioe) {
            logger.error("IOException " + ioe, ioe);
        }

    }

    /**
     * Gets the appropriate message for the
     * passed key and locale
     * @param locale
     * @param flag
     * @param callType
     * @param body
     * @return
     */
    private boolean checkData(
        String localeKey,
        boolean registerKey,
        String callType,
        BodyContent body) {

        logger.debug("invoking checkData: " + localeKey + "," + registerKey + "," +
                     callType);

        boolean returnVal = false;
        String localizedMsg = "";

        try {

            Message msg = new Message();

            if (localeKey != null) {
                msg = getMsg(localeKey, callType);
            }

            if (msg == null) {
                if (DgUtil.getCurrSiteDefLang(request) == null) {
                    localeKey = null;
                }
                else {
                    localeKey =
                        DgUtil.getCurrSiteDefLang(request).getCode();
                }

                if (localeKey != null) {
                    msg = getMsg(localeKey, callType);
                }

                if (msg == null) {
                    localeKey = "en";

                    msg = getMsg(localeKey, callType);

                    if (msg == null) {

                        //Still text not found? update the text in the database and display the same
                        localizedMsg = getBodyText();
                        if( localizedMsg == null ) {
                            localizedMsg = body.getString();
                        }

                        if (localizedMsg != null) {
                           // && localizedMsg.trim().length() != 0) {

                            try {
                                if (registerKey) {

                                    updateMsg(localizedMsg, localeKey, callType);

                                    writeData(format(localizedMsg) +
                                              getMessageTranslate(),
                                              body);
                                    returnVal = true;
                                }

                            }
                            catch (java.lang.Exception e) {
                                logger.error("Error updating  message!", e);
                            }

                        }
                        else {

                            writeData("key:" + getKey() + getMessageCreate(),
                                      body);
                            returnVal = true;
                        }
                    }
                    else {

                        writeData(msg, getMessageTranslate(), body);
                        returnVal = true;
                    }
                }
                else {

                    writeData(msg, getMessageTranslate(), body);
                    returnVal = true;
                }

            }
            else {

                writeData(msg, getMessageEdit(), body);
                returnVal = true;
            }

        }
        catch (WorkerException we) {

            we.printStackTrace();
        }

        return returnVal;

    }

    /**
     * Returns a message for a given locale and type of the tag
     * Interacts with TranslatorWorker
     *
     * @param locale
     * @param callType
     * @return
     * @throws WorkerException
     */
    private Message getMsg(String localeKey, String callType) throws
        WorkerException {

        Message msg = new Message();

        TranslatorWorker worker = TranslatorWorker.getInstance(this.getKey());

        if (this.getKey().trim().startsWith("cn") ||
            this.getKey().trim().startsWith("ln")) {

            msg = worker.get(
                this.getKey(),
                localeKey,
                "0");

            return msg;
        }

        String localSiteId = getSiteId(callType);

        logger.debug("getMsg: " + this.getKey() + "," + localeKey +
                     "," + String.valueOf(localSiteId));
        msg = worker.get(this.getKey(), localeKey, localSiteId);

        return msg;
    }

    /**This method would format a passed message
     */
    private String format(String msg) {
        if (msg.indexOf('{') <0) {
            //truncate msg if needed
            if (getMax() > 0 ) {
                if (msg.length() > getMax()) {
                    msg = msg.substring(0, getMax()) + "...";
                }
            }

            return msg;
        }
        Map translationParams = (Map) RequestUtils.getDigiContextAttribute( (
            HttpServletRequest) pageContext.getRequest(),
            Constants.TRANSLATION_PARAMETERS);

        if (translationParams != null) {
            translationParams = new HashMap(translationParams);
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                if (translationParams == null) {
                    translationParams = new HashMap();
                }
                translationParams.put(Integer.toString(i), args[i]);
            }
        }

        String formattedString = msg;
        if (translationParams != null) {
            formattedString = DgUtil.fillPattern(formattedString,
                                                 translationParams);
        }

        //truncate if needed
        if (getMax() > 0 ) {
            if (formattedString.length() > getMax()) {
                formattedString = formattedString.substring(0, getMax()) + "...";
            }
        }

        return formattedString;
    }

    /**
     * Returns Edit string depending on the tag type and user priveleges
     *
     * @return
     */
    private String getMessageEdit() {
        //boolean groupTranslator = true;
        //boolean localTranslator = true;
        String translatorTag = "";

        if (showLinks) {
            if (this.getKey().trim().startsWith("cn") ||
                this.getKey().trim().startsWith("ln")) {
                if (!globalAdmin) {

                    return translatorTag;
                }

            }

            String actualType = getActualType();

            if (groupTranslator && actualType.equals(LOCAL_TRANSLATION)) {
                translatorTag = "";
            }

            if (groupTranslator && actualType.equals(GROUP_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=group\">&lt;E&gt;</a>";
            }

            if (localTranslator && actualType.equals(LOCAL_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=local\">&lt;EL&gt;</a>";
            }

            if (localTranslator && actualType.equals(GROUP_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=local\">&lt;EL&gt;</a>";
            }
            if (localTranslator && groupTranslator && actualType.equals(GROUP_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=group\">&lt;E&gt;</a>" +
                    "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=local\">&lt;EL&gt;</a>";
            }
            if (localTranslator && groupTranslator && actualType.equals(LOCAL_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=local\">&lt;EL&gt;</a>";
            }
            if (!localTranslator && !groupTranslator) {
                translatorTag = "";
            }
        }

        return translatorTag;
    }

    /**
     * Returns Create string depending on the tag type and user previledges
     *
     * @return
     */
    private String getMessageCreate() {

        String translatorTag = "";

        if (showLinks) {
            if (this.getKey().trim().startsWith("cn") ||
                this.getKey().trim().startsWith("ln")) {
                if (!globalAdmin) {
                    return translatorTag;
                }

            }
            String actualType = getActualType();

            if (groupTranslator && actualType.equals(LOCAL_TRANSLATION)) {
                translatorTag = "";
            }
            if (groupTranslator && actualType.equals(GROUP_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=group\">&lt;C&gt;</a>";
            }
            if (localTranslator && actualType.equals(LOCAL_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=local\">&lt;CL&gt;</a>";
            }
            if (localTranslator && actualType.equals(GROUP_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=local\">&lt;CL&gt;</a>";
            }
            if (localTranslator && groupTranslator && actualType.equals(GROUP_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=group\">&lt;C&gt;</a>";
            }
            if (localTranslator && groupTranslator && actualType.equals(LOCAL_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=local\">&lt;CL&gt;</a>";
            }
            if (!localTranslator && !groupTranslator) {
                translatorTag = "";
            }
        }
        return translatorTag;
    }

    /**
     * Returns Href string depending on the context path retrieved
     *
     * @return
     */
    private String getHref() {
        String returnPath = request.getContextPath();
        if (RequestUtils.getSiteDomain(request).getSitePath() != null) {
            returnPath = returnPath + RequestUtils.getSiteDomain(request).getSitePath();
        }
        return returnPath;
    }

    /**
     * Returns translate string depending on the tag type and user previledges
     *
     * @return
     */
    private String getMessageTranslate() {

        String translatorTag = "";

        if (showLinks) {
            if (this.getKey().trim().startsWith("cn") ||
                this.getKey().trim().startsWith("ln")) {
                if (!globalAdmin) {

                    return translatorTag;
                }
            }
            String actualType = getActualType();

            if (groupTranslator && actualType.equals(LOCAL_TRANSLATION)) {
                translatorTag = "";
            }
            if (groupTranslator && actualType.equals(GROUP_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=group\">&lt;T&gt;</a>";
            }
            if (localTranslator && actualType.equals(LOCAL_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=local\">&lt;TL&gt;</a>";
            }
            if (localTranslator && actualType.equals(GROUP_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=local\">&lt;TL&gt;</a>";
            }
            if (localTranslator && groupTranslator && actualType.equals(GROUP_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=group\">&lt;T&gt;</a>" +
                    "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=local\">&lt;TL&gt;</a>";

            }
            if (localTranslator && groupTranslator && actualType.equals(LOCAL_TRANSLATION)) {
                translatorTag = "<a href=\"" + getHref() +
                    "/showLayout.do?layout=TranslatorPortlet&key=" + getKey() +
                    "&back_url=" + backUrl + "&type=local\">&lt;TL&gt;</a>";
            }
            if (!localTranslator && !groupTranslator) {
                translatorTag = "";
            }
        }
        return translatorTag;
    }

    /**
     * Does an insert or update on TranslatorWorker
     *
     * @param strMessage
     * @param locale
     * @param type
     * @throws WorkerException
     */
    private void updateMsg(String strMessage, String localeKey, String type) throws
        WorkerException {

        if (this.getKey().trim().startsWith("cn") ||
            this.getKey().trim().startsWith("ln")) {
            return;
        }

        String siteId = getSiteId(type);

        TranslatorWorker worker = TranslatorWorker.getInstance(this.getKey());

        Message msg = worker.get(this.getKey(), localeKey, siteId);

        if (msg != null) {
            msg.setMessage(strMessage);
            msg.setKey(this.getKey());
            msg.setCreated(new java.sql.Timestamp(System.currentTimeMillis()));
            msg.setLocale(localeKey);

            msg.setSiteId(siteId);

            worker.update(msg);

        }
        else {

            Message message = new Message();
            message.setMessage(strMessage);

            message.setCreated(new java.sql.Timestamp(System.currentTimeMillis()));
            message.setKey(this.getKey());

            message.setSiteId(siteId);

            message.setLocale(localeKey);

            worker.save(message);

        }

    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();

    }

    private String getSiteId(String callType) throws WorkerException {
        Long localSiteId;
        if (siteId != null) {
            try {
                localSiteId = SiteUtils.getNumericSiteId(siteId);
            }
            catch (DgException ex) {
                throw new WorkerException(ex);
            }

        }
        else if (callType.equals(LOCAL_TRANSLATION)) {
            localSiteId = RequestUtils.getSite(request).getId();
        }
        else {
            localSiteId = DgUtil.getRootSite(RequestUtils.getSite(request)).getId();
        }
        return String.valueOf(localSiteId);
    }

    private String getActualType() {
        if (siteId != null) {
            return LOCAL_TRANSLATION;
        } else {
            if (LOCAL_TRANSLATION.equals(type)) {
                return LOCAL_TRANSLATION;
            } else {
                return GROUP_TRANSLATION;
            }
        }
    }
}
