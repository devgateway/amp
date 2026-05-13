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

package org.digijava.kernel.exception;

import java.io.Serializable;
import java.util.LinkedList;

public class ExceptionInfo implements Serializable {
    public static final String EXCEPTION_INFO = "exInfo";
    private Integer exceptionCode;
    private String stackTrace;
    private String errorMessage;
    private String userMessage;
    private String mainTag;
    private LinkedList<String> tags;
    private String backLink;
    private String sourceURL;
    private long siteId;
    private String siteKey;
    private String siteName;
    private String moduleName;
    private String instanceName;
    private long timestamp;
    private Throwable exception;

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Integer getExceptionCode() {
        return exceptionCode;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public long getSiteId() {
        return siteId;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getSourceURL() {
        return sourceURL;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setExceptionCode(Integer exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setSiteId(long siteId) {
        this.siteId = siteId;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getMainTag() {
        return mainTag;
    }

    public void setMainTag(String mainTag) {
        this.mainTag = mainTag;
    }

    public String getBackLink() {
        return backLink;
    }

    public void setBackLink(String backLink) {
        this.backLink = backLink;
    }

    public LinkedList<String> getTags() {
        return tags;
    }

    public void setTags(LinkedList<String> tags) {
        this.tags = tags;
    }
    
}
