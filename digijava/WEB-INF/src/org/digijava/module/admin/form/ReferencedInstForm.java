/*
 *   ReferencedInstForm.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 5, 2003
 * 	 CVS-ID: $Id: ReferencedInstForm.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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
package org.digijava.module.admin.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.Collection;
import org.digijava.kernel.entity.Locale;
import java.util.ArrayList;
import java.util.List;
import org.digijava.kernel.util.SiteConfigUtils;
import java.util.HashMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Map;
import org.digijava.kernel.entity.ModuleInstance;
import java.util.Set;
import java.util.Iterator;

public class ReferencedInstForm
    extends ActionForm {

    private ArrayList sites;
    private ArrayList instances;
    private String module;
    private Long instanceId;
    private Long siteId;
    private int index;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        siteId = null;
        instanceId = null;
        module = null;
    }

    public ArrayList getInstances() {
        return instances;
    }

    public void setInstances(ArrayList instances) {
        this.instances = instances;
    }

    public ArrayList getSites() {
        return sites;
    }

    public void setSites(ArrayList sites) {
        this.sites = sites;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}