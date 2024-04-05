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

package org.digijava.module.translation.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.security.DigiSecurityManager;
import org.digijava.kernel.security.principal.UserPrincipal;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.translation.form.TranslationPermissionsForm;
import org.digijava.module.translation.security.TranslateObject;
import org.digijava.module.translation.security.TranslatePermission;

import java.util.ArrayList;
import java.util.Iterator;

public class SaveUserPermissions
      extends Action {

    public ActionForward execute(ActionMapping mapping,
                 ActionForm form,
                 javax.servlet.http.HttpServletRequest request,
                 javax.servlet.http.HttpServletResponse
                 response) throws java.lang.Exception {

    TranslationPermissionsForm formBean = (
          TranslationPermissionsForm) form;

    User user = null;
    if (formBean.getUserId() != null) {
        user = UserUtils.getUser(formBean.getUserId());
    }

    if (user != null) {
        ArrayList permissions = new ArrayList();
        Iterator iter = formBean.getPermissions().iterator();
        while (iter.hasNext()) {
            TranslationPermissionsForm.PermissionInfo pi = (
                TranslationPermissionsForm.
                PermissionInfo) iter.next();

            Long id = pi.getSiteId().longValue() >= 0 ? pi.getSiteId() : null;
            String locale = pi.getLocaleId().equals(TranslateObject.
                                                    LOCALE_CODE_ALL) ? null :
                pi.getLocaleId();

            permissions.add(new TranslatePermission(id, locale,
                                                    new Integer(TranslatePermission.
                INT_TRANSLATE)));
        }

        UserPrincipal userPrincipal = new UserPrincipal(user);
        DigiSecurityManager.setPrincipalPermissions(userPrincipal,
          permissions,new Class[] {TranslatePermission.class} );
    }

    return mapping.findForward("forward");

    }

}
