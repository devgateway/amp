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

package org.digijava.module.editor.dbentity;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.Versionable;
import org.digijava.module.aim.util.AmpMath;
import org.digijava.module.aim.util.Output;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Editor
    implements Serializable, Cloneable, Versionable {

    private static final long serialVersionUID = 1L;

//  private static Logger logger = Logger.getLogger(Editor.class);

    private String siteId;
    private String editorKey;
    private Date lastModDate;
    private String url;
    private String language;

    private String title;
    private String body;
    private String notice;

    private String creationIp;
    private User user;

    private int orderIndex;
    private String groupName;

    public Date getLastModDate() {
        return lastModDate;
    }

    public void setLastModDate(Date lastModDate) {
        this.lastModDate = lastModDate;
    }

    public String getSiteId() {
        return siteId;
    }

    /**
     * @deprecated - use {@link #setSite(Site)} instead
     * @param siteId
     */
    public void setSiteId(String siteId) {
        if ((siteId != null) && (AmpMath.isLong(siteId)))
        {
            Logger.getLogger(this.getClass()).error("numeric siteId: " + siteId, new RuntimeException());
            this.siteId = SiteCache.lookupById(Long.parseLong(siteId)).getSiteId();
        }
        this.siteId = siteId;
    }

    public void setSite(Site site)
    {
        setSiteId(site == null ? null : site.getSiteId());
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreationIp() {
        return creationIp;
    }

    public void setCreationIp(String creationIp) {
        this.creationIp = creationIp;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEditorKey() {
        return editorKey;
    }

    public void setEditorKey(String editorKey) {
        this.editorKey = editorKey;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if (! (other instanceof Editor))
            return false;
        Editor castOther = (Editor) other;
        return new EqualsBuilder()
            .append(this.getSiteId(), castOther.getSiteId())
            .append(this.getLanguage(), castOther.getLanguage())
            .append(this.getEditorKey(), castOther.getEditorKey())
            .isEquals();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getSiteId())
            .append(this.getLanguage())
            .append(this.getEditorKey())
            .toHashCode();
    }

    public int getOrderIndex() {
        return orderIndex;
    }
    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    @Override
    public boolean equalsForVersioning(Object obj) {
        Editor aux = (Editor) obj;
        if (this.body.compareTo(aux.getBody()) == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.getOutputs().add(
                new Output(null, new String[] { " Body:&nbsp;" },
                        new Object[] { this.body != null ? this.body : "" }));
        return out;
    }

    @Override
    public Object getValue() {
        return this.body != null ? this.body : "";
    }
    
    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        Editor aux = (Editor) clone(); 
        aux.editorKey = null;
        return aux;
    }

}
