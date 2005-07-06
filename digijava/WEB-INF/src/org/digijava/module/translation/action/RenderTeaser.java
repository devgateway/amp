/*
 *   RenderTeaser.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: jan 14, 2004
 * 	 CVS-ID: $Id: RenderTeaser.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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

package org.digijava.module.translation.action;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.translation.form.TranslationForm;
import org.digijava.module.translation.form.TranslationInfo;
import org.digijava.module.translation.util.DbUtil;
import org.digijava.module.translation.util.TranslationManager;

public class RenderTeaser
    extends TilesAction {

    private static Logger logger = Logger.getLogger(RenderTeaser.class);

    public ActionForward execute(ComponentContext context,
                                 ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        TranslationForm formBean = (TranslationForm) form;
/*
        Collection languages = null;
        String relativeUrlEncode = null;
        List formBeanLanguages = new ArrayList();

        Site site = RequestUtils.getSite(request);
        SiteDomain defaultDomain = SiteUtils.getDefaultSiteDomain(site);

        if (DgUtil.isLocalTranslatorForSite(request)) {
            logger.debug("Generating translation languages");
            languages = SiteUtils.getTransLanguages(site);
        }
        else {
            logger.debug("Generating user languages");
            languages = SiteUtils.getUserLanguages(site);
        }

        languages = DbUtil.getLanguages(languages, site.getId());
*/
        boolean generateRoots = false;
        String rootPathesParam = (String) context.getAttribute("redirectToRoot");
        if (rootPathesParam != null) {
            generateRoots = Boolean.valueOf(rootPathesParam).booleanValue();
        }

        long time = System.currentTimeMillis();
        TranslationManager.generateLanguages(generateRoots, request, formBean);
        //System.out.println("Fetched in " + (System.currentTimeMillis() - time) + "ms.");
/*
        if (languages != null) {

            String relativeUrl;
            if (generateRoots) {
                logger.debug("Generating root pathes in the language switch");
                relativeUrl = "/";
            }
            else {
                logger.debug("Generating full pathes in the language switch");
                String currentUrl = RequestUtils.getSourceURL(request);
                logger.debug("Current URL is: " + currentUrl);

                String sitetUrl = DgUtil.getSiteUrl(RequestUtils.getSiteDomain(
                    request), request);

                relativeUrl = currentUrl.substring(sitetUrl.length(),
                    currentUrl.length());
                if (relativeUrl.trim().length() == 0) {
                    relativeUrl = "/";
                }

            }

            relativeUrlEncode = URLEncoder.encode(relativeUrl, "UTF-8");

            Iterator iterator = languages.iterator();
            while (iterator.hasNext()) {

                TranslationInfo locale = (TranslationInfo) iterator.next();

                TranslationForm.TranslationInfo ti = new TranslationForm.
                    TranslationInfo();

                String referUrl;
                if (locale.getSiteDomain() != null) {
                    referUrl = SiteUtils.getSiteURL(locale.getSiteDomain(),
                        locale.getSitePath(), request.getScheme(),
                        request.getServerPort(), request.getContextPath()) +
                        relativeUrl;
                }
                else {
                    referUrl = DgUtil.getSiteUrl(defaultDomain, request) +
                        "/translation/switchLanguage.do?code=" +
                        locale.getLangCode() + "&rfr=" + relativeUrlEncode;
                }

                ti.setLangCode(locale.getLangCode());
                ti.setLangName(locale.getLangName());
                ti.setReferUrl(referUrl);
                ti.setKey("ln:" + locale.getLangCode());

                if (RequestUtils.getNavigationLanguage(request).getCode().
                    equals(ti.getLangCode())) {
                    formBean.setReferUrl(ti.getReferUrl());
                }

                formBeanLanguages.add(ti);
            }
        }
        Collections.sort(formBeanLanguages);

        if (formBeanLanguages.size() != 0) {
            formBean.setLanguages(formBeanLanguages);
        }
        else {
            formBean.setLanguages(null);
        }
*/
        return null;
    }
}
