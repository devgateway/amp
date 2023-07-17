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
import javax.persistence.*;
import javax.persistence.Entity;


@Entity
@Table(name = "DG_LOCALE")
public class Locale implements Serializable {
    @Id
    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "AVAILABLE")
    private boolean available;

    @Column(name = "MESSAGE_LANG_KEY")
    private String messageLangKey;

    @Column(name = "LEFT_TO_RIGHT")
    private Boolean leftToRight;

    public Locale() {
    }

    public Locale(String code, String name) {
        this.code = code;
        this.name = name;
        this.leftToRight = Boolean.TRUE;
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

    public String getDirection() {
        if (leftToRight == null) {
            return "ltr";
        }
        return leftToRight.booleanValue() ? "ltr": "rtl";
    }
    
    @Override
    public String toString() {
        return this.code;
    }
}
