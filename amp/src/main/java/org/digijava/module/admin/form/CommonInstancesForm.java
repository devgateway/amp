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

package org.digijava.module.admin.form;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.digijava.kernel.entity.ModuleInstance;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class CommonInstancesForm
      extends ActionForm {

    private ArrayList commonInstances;
    private Set modules;
    private ModuleInstance oneInstance;
    public int index;
    private Collection numOfItemsInTeaser;

    public static class Item {

    private String value;

    public Item() {
    }

    public Item(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    }

    public static class CommonInstanceInfo {
    private Long id;
    private String module;
    private String instance;
    private Long selectedNumOfItemsInTeaser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public Long getSelectedNumOfItemsInTeaser() {
        return this.selectedNumOfItemsInTeaser;
    }

    public void setSelectedNumOfItemsInTeaser(Long
                          selectedNumOfItemsInTeaser) {
        this.selectedNumOfItemsInTeaser = selectedNumOfItemsInTeaser;
    }
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
    commonInstances = new ArrayList();
    }

    public ActionErrors validate(ActionMapping actionMapping,
                 HttpServletRequest httpServletRequest) {

    ActionErrors errors = new ActionErrors();
    Iterator iter = commonInstances.iterator();
    while (iter.hasNext()) {
        CommonInstanceInfo item = (CommonInstanceInfo) iter.next();
        if ( (item.getModule() == null) ||
        (item.getModule().trim().length() == 0)) {
        errors.add(null,
               new ActionMessage("error.admin.moduleNameEmpty"));
        break;
        }
        if ( (item.getInstance() == null) ||
        (item.getInstance().trim().length() == 0)) {
        errors.add(null,
               new ActionMessage("error.admin.instanceNameEmpty"));
        break;
        }

    }

    for (int i = 0; i < commonInstances.size(); i++) {
        CommonInstanceInfo item = (CommonInstanceInfo) commonInstances.get(
          i);
        for (int n = 0; n < commonInstances.size(); n++) {
        CommonInstanceInfo item2 = (CommonInstanceInfo) commonInstances.
              get(n);
        if (item.getInstance().equalsIgnoreCase(item2.getInstance()) &&
            item.getModule().equalsIgnoreCase(item2.getModule()) &&
            n != i) {
            Object[] param = {
              item.getInstance(), item.getModule()};
            errors.add(null,
                   new ActionMessage(
              "error.admin.instanceAlreadyExists",
              param));
            break;
        }
        }
    }

    return errors.isEmpty() ? null : errors;
    }

    public CommonInstanceInfo getCommonInstance(int index) {
    CommonInstanceInfo info = null;
    int actualSize = commonInstances.size();
    if (index >= actualSize) {
        // Expand the list
        for (int i = 0; i <= index - actualSize; i++) {
        commonInstances.add(new CommonInstanceInfo());
        }
    }

    return (CommonInstanceInfo) commonInstances.get(index);
    }

    public Set getModules() {
    return modules;
    }

    public void setModules(Set modules) {
    this.modules = modules;
    }

    public ModuleInstance getOneInstance() {
    return oneInstance;
    }

    public void setOneInstance(ModuleInstance oneInstance) {
    this.oneInstance = oneInstance;
    }

    public int getIndex() {
    return index;
    }

    public void setIndex(int index) {
    this.index = index;
    }

    public Collection getNumOfItemsInTeaser() {
    return this.numOfItemsInTeaser;
    }

    public void setNumOfItemsInTeaser(Collection numOfItemsInTeaser) {
    this.numOfItemsInTeaser = numOfItemsInTeaser;
    }

    public ArrayList getCommonInstances() {
    return commonInstances;
    }

    public void setCommonInstances(ArrayList commonInstances) {
    this.commonInstances = commonInstances;
    }

}
