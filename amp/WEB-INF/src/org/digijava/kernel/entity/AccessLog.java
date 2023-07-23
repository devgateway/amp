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

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "DG_ACCESS_LOG")
public class AccessLog
    implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accessLogSeqGen")
    @SequenceGenerator(name = "accessLogSeqGen", sequenceName = "DG_ACCESS_LOG_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "SITE_ID")
    private String siteId;

    @Column(name = "MODULE_NAME")
    private String moduleName;

    @Column(name = "INSTANCE_NAME")
    private String instanceName;

    @Column(name = "REAL_SITE_ID")
    private String realSiteId;

    @Column(name = "REAL_INSTANCE_NAME")
    private String realInstanceName;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "USER_IP")
    private String userIp;

    @Column(name = "LOG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logDate;

    @Column(name = "URL")
    private String url;

    @Column(name = "LANG_CODE")
    private String languageCode;

    @Column(name = "IDENTITY")
    private String itemIdentity;

    @Column(name = "IDENTITY_TYPE")
    private String identityType;


    /*
     non-persistent properties
     */
    private String identityPattern;
    @Transient
//    @ElementCollection
//    @MapKeyColumn(name = "param_key") // Column name for map keys
//    @Column(name = "param_value") // Column name for map values
//    @CollectionTable(name = "DG_ACCESS_LOG_PARAMS", joinColumns = @JoinColumn(name = "access_log_id"))
    private Map<Object, Object> parameters;
    private String forwardedAddr;
    private String originalIp;

    public String toString() {
        return new ToStringBuilder(this).reflectionToString(this).toString();
    }

    /**
     * Clone object. Does not clone id and non-persistent properties
     * @return
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        AccessLog newInstance = new AccessLog();

        if (url != null) {
            newInstance.url = new String(url);
        }

        if (logDate != null) {
            newInstance.logDate = (Date) logDate.clone();
        }

        if (userIp != null) {
            newInstance.userIp = new String(userIp);
        }

        if (siteId != null) {
            newInstance.siteId = new String(siteId);
        }

        if (moduleName != null) {
            newInstance.moduleName = new String(moduleName);
        }

        if (instanceName != null) {
            newInstance.instanceName = new String(instanceName);
        }

        if (realSiteId != null) {
            newInstance.realSiteId = new String(realSiteId);
        }

        if (realInstanceName != null) {
            newInstance.realInstanceName = new String(realInstanceName);
        }

        if (userId != null) {
            newInstance.userId = new Long(userId.longValue());
        }

        if (languageCode != null) {
            newInstance.languageCode = new String(languageCode);
        }

        if (identityType != null) {
            newInstance.identityType = new String(identityType);
        }

        if (itemIdentity != null) {
            newInstance.itemIdentity = new String(itemIdentity);
        }

        return newInstance;
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getRealInstanceName() {
        return realInstanceName;
    }

    public void setRealInstanceName(String realInstanceName) {
        this.realInstanceName = realInstanceName;
    }

    public String getRealSiteId() {
        return realSiteId;
    }

    public void setRealSiteId(String realSiteId) {
        this.realSiteId = realSiteId;
    }

    public String getIdentityPattern() {
        return identityPattern;
    }

    public void setIdentityPattern(String identityPattern) {
        this.identityPattern = identityPattern;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getItemIdentity() {
        return itemIdentity;
    }

    public void setItemIdentity(String itemIdentity) {
        this.itemIdentity = itemIdentity;
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public String getForwardedAddr() {
        return forwardedAddr;
    }

    public void setForwardedAddr(String forwardedAddr) {
        this.forwardedAddr = forwardedAddr;
    }

    public String getOriginalIp() {
        return originalIp;
    }

    public void setOriginalIp(String originalIp) {
        this.originalIp = originalIp;
    }

}
