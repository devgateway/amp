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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import java.util.Collection;
import java.util.TreeSet;

public class SiteInstancesForm
    extends ActionForm {

    private ArrayList instances;
    private TreeSet modules;
    private ModuleInstance oneInstance;
    private List masterInstances;
    private String[] permittedInstances;
    private ArrayList refInstances;
    public int index;
    private Long mapId;
    private Collection numOfItemsInTeaser;
    private String defaultModeuleIndex;

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

    public static class InstanceInfo {
        private Long id;
        private String module;
        private String instance;
        private Long mappingId;
        private String mappingSite;
        private String mappingInstance;
        private Long selectedNumOfItemsInTeaser;
        private boolean permitted;

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

        public Long getMappingId() {
            return mappingId;
        }

        public void setMappingId(Long mappingId) {
            this.mappingId = mappingId;
        }

        public void setMappingSite(String mappingSite) {
            this.mappingSite = mappingSite;
        }

        public String getMappingSite() {
            return mappingSite;
        }

        public void setMappingInstance(String mappingInstance) {
            this.mappingInstance = mappingInstance;
        }

        public String getMappingInstance() {
            return mappingInstance;
        }

        public void setPermitted(boolean permitted) {
            this.permitted = permitted;
        }

        public boolean isPermitted() {
            return permitted;
        }

        public Long getSelectedNumOfItemsInTeaser() {
            return this.selectedNumOfItemsInTeaser;
        }

        public void setSelectedNumOfItemsInTeaser(Long selectedNumOfItemsInTeaser) {
            this.selectedNumOfItemsInTeaser = selectedNumOfItemsInTeaser;
        }
        
        @Override
        public String toString() {
            return this.getModule() + "/" + this.getInstance() + "/" + this.getId() ;
        }
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        instances = new ArrayList();
        permittedInstances = null;
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {

        ActionErrors errors = new ActionErrors();
        Iterator iter = instances.iterator();
        while (iter.hasNext()) {
            InstanceInfo item = (InstanceInfo) iter.next();
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


        for( int i = 0; i < instances.size(); i++ ) {
            InstanceInfo item = (InstanceInfo)instances.get(i);
            for( int n = 0; n < instances.size(); n++ ) {
                InstanceInfo item2 = (InstanceInfo)instances.get(n);
                if( item.getInstance().equalsIgnoreCase(item2.getInstance()) &&
                    item.getModule().equalsIgnoreCase(item2.getModule()) && n != i ) {
                    Object[] param = {item.getInstance(), item.getModule()};
                    errors.add(null,
                               new ActionMessage("error.admin.instanceAlreadyExists",
                                               param));
                    break;
                }
            }
        }

        return errors.isEmpty() ? null : errors;
    }

    public ArrayList getInstances() {
        return instances;
    }

    public void setInstances(ArrayList instances) {
        this.instances = instances;
    }

    public InstanceInfo getInstance(int index) {
        InstanceInfo info = null;
        int actualSize = instances.size();
        if (index >= actualSize) {
            // Expand the list
            for (int i = 0; i <= index - actualSize; i++) {
                instances.add(new InstanceInfo());
            }
        }

        return (InstanceInfo) instances.get(index);
    }

    public TreeSet getModules() {
        return modules;
    }

    public void setModules(TreeSet modules) {
        this.modules = modules;
    }

    public ModuleInstance getOneInstance() {
        return oneInstance;
    }

    public void setOneInstance(ModuleInstance oneInstance) {
        this.oneInstance = oneInstance;
    }

    public List getMasterInstances() {
        return masterInstances;
    }

    public void setMasterInstances(List masterInstances) {
        this.masterInstances = masterInstances;
    }

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public String[] getPermittedInstances() {
        return permittedInstances;
    }

    public void setPermittedInstances(String[] permittedInstances) {
        this.permittedInstances = permittedInstances;
    }

    public ArrayList getRefInstances() {
        return refInstances;
    }

    public void setRefInstances(ArrayList refInstances) {
        this.refInstances = refInstances;
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
  public String getDefaultModeuleIndex() {
    return defaultModeuleIndex;
  }
  public void setDefaultModeuleIndex(String defaultModeuleIndex) {
    this.defaultModeuleIndex = defaultModeuleIndex;
  }

}
