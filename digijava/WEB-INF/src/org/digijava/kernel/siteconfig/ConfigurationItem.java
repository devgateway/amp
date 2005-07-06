/*
 *   ConfigurationItem.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 17, 2003
 * 	 CVS-ID: $Id: ConfigurationItem.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/

package org.digijava.kernel.siteconfig;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Configuration class form marging site-config.xml files
 * @author Mikheil Kapanadze
 * @version 1.0
 */
public abstract class ConfigurationItem {

    public abstract void setName(String name);

    public abstract String getName();

    public abstract void validate() throws Exception;

    public abstract void merge(ConfigurationItem configurationItem);

    protected void mergeHashMap(HashMap firstMap, HashMap secondMap) {
        if (firstMap.size() == 0) {
            firstMap.putAll(secondMap);
        }
        else {
            Iterator iter = secondMap.values().iterator();
            while (iter.hasNext()) {
                ConfigurationItem secondItem = (ConfigurationItem) iter.next();
                ConfigurationItem firstItem = (ConfigurationItem) firstMap.get(
                    secondItem.getName());

                if (firstItem != null) {
                    firstItem.merge(secondItem);
                }
                else {
                    firstMap.put(secondItem.getName(), secondItem);
                }
            }
        }
    }

}