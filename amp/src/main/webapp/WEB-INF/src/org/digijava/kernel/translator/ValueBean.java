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

package org.digijava.kernel.translator;

import java.io.Serializable;
public class ValueBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String keyId;
    private String source;
    private String target;
    private String targetSiteId;
    private String sourceSiteId;
    boolean needsUpdate = false;

    public ValueBean() {

    }
    public ValueBean(String key,String message_source,String message_target,String sourceSiteId,String targetSiteId,boolean update) {
        this.keyId = key;
        this.source = message_source;
        this.target = message_target;
        this.needsUpdate = update;
        this.targetSiteId = targetSiteId;
        this.sourceSiteId = sourceSiteId;
    }

    public String getKeyId() {
        return this.keyId;
    }

    public String getSource() {
            return this.source;
    }

    public String getTarget() {
        return this.target;
    }

    public void setKeyId(String key) {
        this.keyId = key;
    }

    public void setSource(String message_source) {
        this.source = message_source;
    }

    public void setTarget(String message_target) {
        this.target = message_target;
    }

    public void setTargetSiteId(String siteId) {
        this.targetSiteId = siteId;
    }

    public String getTargetSiteId() {
        return this.targetSiteId;
    }
    public void setSourceSiteId(String siteId) {
        this.sourceSiteId = siteId;
    }

    public String getSourceSiteId() {
        return this.sourceSiteId;
    }
    public boolean isNeedsUpdate() {
            return this.needsUpdate;
    }

    public void setNeedsUpdate(boolean b) {
            needsUpdate = b;
    }


}
