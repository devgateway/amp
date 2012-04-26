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

package org.digijava.kernel.security;

import java.io.Serializable;

import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.security.permission.ObjectPermission;
import org.digijava.kernel.util.ModuleUtils;

public class ModuleInstancePermission
    extends ObjectPermission implements Serializable {

    private static final long serialVersionUID = 1;
    private static ModuleInstanceSecurityManager instance = new ModuleInstanceSecurityManager();

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

    public AbstractObjectSecurityManager getSecurityManager() {
        return instance;
    }

    protected int modifySecurityAction(int oldAction) {
        if (oldAction == 0x7) {
            return INT_ADMIN;
        }
        return oldAction;
    }
}
