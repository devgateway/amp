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

import org.apache.commons.lang.StringUtils;


public class Locale implements Serializable {
    
    private Long id;

    private String name;
    private String code;
    private boolean available;
    private String messageLangKey;
    private Boolean leftToRight;
    
    private String region;
    
    private String unicodeExtension;
    
    private java.util.Locale systemLocale;
    
    public Locale() {
    }

    public Locale(String code, String name) {
        this.code = code;
        this.name = name;
        this.leftToRight = Boolean.TRUE;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getMessageLangKey() {
        return messageLangKey;
    }

    public Boolean getLeftToRight() {

        return leftToRight;
    }

    public void setMessageLangKey(String messageLangKey) {
        this.messageLangKey = messageLangKey;
    }

    public void setLeftToRight(Boolean leftToRight) {
        this.leftToRight = leftToRight;
    }
    
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    public String getUnicodeExtension() {
        return unicodeExtension;
    }
    
    public void setUnicodeExtension(String unicodeExtension) {
        this.unicodeExtension = unicodeExtension;
    }
    
    public String getDirection() {
        if (leftToRight == null) {
            return "ltr";
        }
        return leftToRight.booleanValue() ? "ltr": "rtl";
    }
    
    public java.util.Locale getSystemLocale() {
        if (systemLocale == null) {
            java.util.Locale.Builder localeBuilder = new java.util.Locale.Builder().setLanguage(code);
            if (StringUtils.isNotBlank(region)) {
                localeBuilder.setRegion(region);
            }
            if (StringUtils.isNotBlank(unicodeExtension)) {
                localeBuilder.setExtension(java.util.Locale.UNICODE_LOCALE_EXTENSION, unicodeExtension);
            }
            systemLocale = localeBuilder.build();
        }
        
        return systemLocale;
    }
    
    @Override
    public String toString() {
        return this.code;
    }
}
