/*
 *   ModuleInstancePermission.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 *   Created:
 *   CVS-ID: $Id: ModuleInstancePermission.java,v 1.1 2005-07-06 10:34:22 rahul Exp $
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

package org.digijava.kernel.security;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.security.permission.ObjectPermission;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.ModuleUtils;
import org.digijava.kernel.exception.*;
import java.io.Serializable;

public class ModuleInstancePermission
    extends ObjectPermission implements Serializable {

    private static final long serialVersionUID = 1;

    private Long siteId;

    public ModuleInstancePermission(ModuleInstance moduleInstance,
                                    String actions) {
        super(ModuleInstance.class.getName(),
              moduleInstance.getModuleInstanceId(), actions);
        this.siteId = moduleInstance.getSite() == null ? null :
            moduleInstance.getSite().getId();
    }

    public ModuleInstancePermission(ModuleInstance moduleInstance, int action) {
        super(ModuleInstance.class.getName(), moduleInstance.getModuleInstanceId(), action);
        this.siteId = moduleInstance.getSite() == null ? null :
            moduleInstance.getSite().getId();
    }

    public ModuleInstancePermission(Long moduleInstanceId, Integer action) {
        super(ModuleInstance.class.getName(), moduleInstanceId, action.intValue());
        ModuleInstance moduleInstance = null;
        try {
            moduleInstance = ModuleUtils.getModuleInstance(moduleInstanceId);
        }
        catch (DgException ex) {
            throw new RuntimeException(ex);
        }
        this.siteId =  moduleInstance.getSite() == null ? null :
            moduleInstance.getSite().getId();
    }

    public Long getModuleInstanceId() {
        return (Long) instanceId;
    }

    public Long getSiteId() {
        return siteId;
    }

    /**
     * Returns array of objects, which will be persisted into database and then
     * used to reconstruct this permission object. Values are sorted by types
     * and order of constructor's parameter
     * @return array of objects
     */
    public Object[] getParameters() {
        return new Object[] { getModuleInstanceId(), new Integer(actionMask)};
    }

}