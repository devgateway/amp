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

package org.digijava.kernel.entity;

import java.io.Serializable;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.Constants;

public class ModuleInstance implements Serializable {

    public static final int NUMBER_OF_ITEMS_IN_TEASER = 8;

    private Long moduleInstanceId;
    private Site site;
    private String moduleName;
    private String instanceName;
    private boolean common;
    private ModuleInstance realInstance;
    private boolean permitted;
    private Long numberOfItemsInTeaser;


    public ModuleInstance() {
        numberOfItemsInTeaser = null;
    }

    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Long getModuleInstanceId() {
        return moduleInstanceId;
    }

    public void setModuleInstanceId(Long moduleInstanceId) {
        this.moduleInstanceId = moduleInstanceId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public ModuleInstance getRealInstance() {
        return realInstance;
    }

    public void setRealInstance(ModuleInstance realInstance) {
        this.realInstance = realInstance;
    }

    public boolean isPermitted() {
        return permitted;
    }

    public void setPermitted(boolean permitted) {
        this.permitted = permitted;
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("ModuleInstance{").append("Module=").append(moduleName);
        buff.append(",").append("Instance=").append(instanceName);

        if (site != null) {
            buff.append(",").append("Site=").append(site.getSiteId());
        }

        if (realInstance != null) {
            buff.append(",").append("realInstance=").append(realInstance);
        }
        buff.append("}");
        return buff.toString();
    }

    public Long getNumberOfItemsInTeaser() {
        if (numberOfItemsInTeaser == null) {
            return new Long(Constants.NUMBER_OF_ITEMS_IN_TEASER);
        }
        else {
            return numberOfItemsInTeaser;
        }
    }

    public void setNumberOfItemsInTeaser(Long numberOfItemsInTeaser) {
        this.numberOfItemsInTeaser = numberOfItemsInTeaser;
    }
}
