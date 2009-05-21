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

package org.digijava.module.translation.entity;

public class AdvancedTrnItem implements Comparable {
    public final static int GLOBAL_TRN = 0;
    public final static int GROUP_TRN = 1;
    public final static int LOCAL_TRN = 2;

    private String key;
    private String sourceValue;
    private String targetValue;
    private boolean srcChanged;
    private int sourceType;
    private int targetType;

    public int compareTo(Object o) {
        if (!(o instanceof AdvancedTrnItem)) {
            throw new IllegalArgumentException("AdvancedTrnItem class object can be only compared against the same class instance");
        }
        AdvancedTrnItem other = (AdvancedTrnItem)o;
        int result;
        if (srcChanged) {
            result = other.srcChanged ? 0 : -1;
        } else {
            result = other.srcChanged ? 1 : 0;
        }
        if (result != 0) {
            return result;
        }
        return key.compareToIgnoreCase(other.key);
    }

    public String getKey() {
        return key;
    }

    public int getSourceType() {
        return sourceType;
    }

    public String getSourceValue() {
        return sourceValue;
    }

    public int getTargetType() {
        return targetType;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public boolean isSrcChanged() {

        return srcChanged;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public void setSourceValue(String sourceValue) {
        this.sourceValue = sourceValue;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public void setSrcChanged(boolean srcChanged) {
        this.srcChanged = srcChanged;
    }

}
