/*
 *   CmsUtil.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created:
 *   CVS-ID: $Id: CmsUtil.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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

package org.digijava.module.cms.util;

import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.util.RequestUtils;
import javax.servlet.http.HttpServletRequest;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DgUtil;
import java.util.HashMap;
import org.digijava.kernel.entity.Message;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class CmsUtil {
    public CmsUtil() {
    }

    public static String getEmailSubject(HttpServletRequest request) throws
        WorkerException {

        TranslatorWorker worker;

        Site site = RequestUtils.getSite(request);
        Locale currentLocale = RequestUtils.getNavigationLanguage(request);

        // get newpassword body
        worker = TranslatorWorker.getInstance(
            "alerts:cmsinterestingresource:subject");
        String subject = worker.getFromGroup(
            "alerts:cmsinterestingresource:subject",
            currentLocale.getCode(), site,
            "Interesting resource").getMessage();

        return subject;
    }


    public static String getEmailMessage(HttpServletRequest request, String name, String link) throws
        WorkerException {

        TranslatorWorker worker;
        String siteName = null;
        Message message;
        Site site = RequestUtils.getSite(request);
        Locale currentLocale = RequestUtils.getNavigationLanguage(request);


        worker = TranslatorWorker.getInstance("param:SiteName");
        message = worker.get("param:SiteName",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            siteName = site.getName();
        }
        else {
            siteName = message.getMessage();
        }

        HashMap hMap = new HashMap();
        hMap.put("siteName", siteName);
        hMap.put("name", name);
        hMap.put("link", link);

        // get newpassword body
        worker = TranslatorWorker.getInstance(
            "alerts:cmsinterestingresource:message");
        String body = worker.getFromGroup(
            "alerts:cmsinterestingresource:message",
            currentLocale.getCode(), site,
            "Hi,\n" +
            "I found this resource on the {siteName}\n\n" +
            "{link}\n\n" +
            "{name}").getMessage();

        body = DgUtil.fillPattern(body, hMap);

        return body;
    }


}