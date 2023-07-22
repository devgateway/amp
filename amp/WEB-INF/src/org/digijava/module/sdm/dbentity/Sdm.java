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

package org.digijava.module.sdm.dbentity;

import java.util.Iterator;
import java.util.Set;

import org.digijava.module.sdm.exception.SDMException;
import org.digijava.module.sdm.util.DbUtil;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "DG_SDM")

public class Sdm {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dg_sdm_seq")
    @SequenceGenerator(name = "dg_sdm_seq", sequenceName = "dg_sdm_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SITE_ID")
    private String siteId;

    @Column(name = "INSTANCE_ID")
    private String instanceId;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "sdm", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("paragraph_order ASC")
    private Set<SdmItem> items;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<SdmItem> getItems() {
        return items;
    }

    public void setItems(Set<SdmItem> items) {
        this.items = items;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    public SdmItem getItemByIndex(Long index) {
        Set<SdmItem> sdmItems = this.getItems();
        SdmItem  result = null;

        Iterator<SdmItem> iter = sdmItems.iterator();
        while(iter.hasNext()) {
            SdmItem item = iter.next();

            if (item.getParagraphOrder().equals(index)) {
                result = item;
                break;
            }
        }
        return result;
    }
}
