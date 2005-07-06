/*
 *   ModuleInstance.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 1, 2003
 * 	 CVS-ID: $Id: ModuleInstance.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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
package org.digijava.kernel.entity;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.Constants;
import java.io.Serializable;

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
