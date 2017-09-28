
/*
 *   CountryCallBackHandler.java
 *   Created by Shamanth Murthy shamanth.murthy@mphasis.com
 *   Date: Mar 18, 2004
 *
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Copyright (c) 2003 by Development Gateway Foundation, Digi developer
 *   team and other contributors..
 *
 *   DiGi is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 *
 *   DiGi is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 ********************************************************************************/

package org.digijava.module.common.util;

import org.digijava.kernel.translator.util.TranslationCallback;

public class CountryCallBackHandler implements TranslationCallback{

    private final String siteId = "0";

    public String getSiteId(Object o){
        return siteId;
    }

    public String getTranslationKey(Object o){

        if(o != null)
            return ("cn:" + o.toString());

        return null;

    }
    public String getDefaultTranslation(Object o){

        return "";
    }
}
