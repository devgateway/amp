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

package org.digijava.module.translation.security;

import org.digijava.kernel.security.AbstractObjectSecurityManager;
import org.digijava.kernel.security.permission.ObjectPermission;
import javax.servlet.http.HttpServletRequest;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.security.SitePermission;
import java.security.Principal;
import javax.security.auth.Subject;

public class TranslateSecurityManager
    extends AbstractObjectSecurityManager {

    private static TranslateSecurityManager instance = new
        TranslateSecurityManager();

    public ObjectPermission createPermission(Class clazz, Object key,
                                             int action) {
        return new TranslatePermission( (TranslateObject) key, action);
    }

    public static TranslateSecurityManager getInstance() {
        return instance;
    }

    public boolean checkPermission(Principal principal, Class targetClass,
                                   Object key, int action) {
        TranslatePermission perm = (TranslatePermission)
            createPermission(targetClass,
                             key, action);
        if (DigiSecurityManager.checkPermission(principal, perm)) {
            return true;
        }
        else {
            if (perm.getTranslateId() == null ||
                perm.getTranslateId().getSiteId() == null ||
                perm.getTranslateId().getSiteId().longValue() == 0) {
                return false;
            }
            else {
                return checkPermission(principal,
                                       new SitePermission(perm.getTranslateId().
                    getSiteId(), SitePermission.INT_TRANSLATE));
            }
        }
    }

    public boolean checkPermission(Subject subject, Class targetClass,
                                   Object key,
                                   int action) {
        TranslatePermission perm = (TranslatePermission)
            createPermission(targetClass,
                             key, action);
        if (DigiSecurityManager.checkPermission(subject, perm)) {
            return true;
        }
        else {
            if (perm.getTranslateId() == null ||
                perm.getTranslateId().getSiteId() == null ||
                perm.getTranslateId().getSiteId().longValue() == 0) {
                return false;
            }
            else {
                return checkPermission(subject,
                                       new SitePermission(perm.getTranslateId().
                    getSiteId(), SitePermission.INT_TRANSLATE));
            }
        }
    }

    public static boolean checkPermission(Subject subject, long siteId,
                                          String langCode) {
        return getInstance().checkPermission(subject, TranslateObject.class,
                                             new TranslateObject(new Long(
            siteId),
            langCode), TranslatePermission.INT_TRANSLATE);
    }

    public static boolean isTranslationPermittedForSite(HttpServletRequest
        request,
        Site site, Locale locale) {

        if (site != null && locale != null) {
            return getInstance().
                checkPermission(
                    RequestUtils.getSubject(request), TranslateObject.class,
                    new TranslateObject(site.getId(),
                                        locale.getCode()),
                    TranslatePermission.INT_TRANSLATE);
        }
        else {
            return false;
        }

    }

    public static boolean isTranslationPermittedForSite(HttpServletRequest
        request,
        Long siteId, String langCode) {

        if (siteId != null && langCode != null) {
            return getInstance().
                checkPermission(
                    RequestUtils.getSubject(request), TranslateObject.class,
                    new TranslateObject(siteId,
                                        langCode),
                    TranslatePermission.INT_TRANSLATE);
        }
        else {
            return false;
        }

    }

    public static boolean isGlobalTranslatorForLocale(HttpServletRequest
        request,
        Locale locale) {

        if (locale != null) {
            return getInstance().
                checkPermission(
                    RequestUtils.getSubject(request), TranslateObject.class,
                    new TranslateObject(new Long(0),
                                        locale.getCode()),
                    TranslatePermission.INT_TRANSLATE);
        }
        else {
            return false;
        }

    }

    public static boolean isGlobalTranslatorForLocale(HttpServletRequest
        request,
        String langCode) {

        if (langCode != null) {
            return getInstance().
                checkPermission(
                    RequestUtils.getSubject(request), TranslateObject.class,
                    new TranslateObject(new Long(0),
                                        langCode),
                    TranslatePermission.INT_TRANSLATE);
        }
        else {
            return false;
        }
    }

    public static boolean isGlobalTranslatorForLocale(HttpServletRequest
        request) {

        return isGlobalTranslatorForLocale(request,
                                           RequestUtils.getNavigationLanguage(
                                               request));
    }
}
