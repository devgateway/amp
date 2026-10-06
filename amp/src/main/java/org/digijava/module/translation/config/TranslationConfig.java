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

package org.digijava.module.translation.config;

import org.apache.commons.digester.Digester;
import org.digijava.kernel.config.KeyValuePair;
import org.digijava.kernel.util.DigesterFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TranslationConfig {
    private String dropdownConfig;
    private GroupsCfg cfg;
    private static Digester digester;

    public String getDropdownConfig() {
        return dropdownConfig;
    }

    public void setDropdownConfig(String dropdownConfig) {
        this.dropdownConfig = dropdownConfig;
        if (dropdownConfig == null || dropdownConfig.trim().length() == 0) {
            this.cfg = null;
        }
        else {
            synchronized (digester) {
                StringReader sr = new StringReader(dropdownConfig);
                try {
                    this.cfg = (GroupsCfg) digester.parse(sr);
                }
                catch (Exception ex) {
                    throw new RuntimeException(
                        "Unable to parse translation dropdown config[" +
                        dropdownConfig + "]", ex);
                }
                sr.close();
            }
        }
    }

    public List getKeys(String groupName) {
        if (cfg == null) {
            return null;
        };
        GroupCfg groupCfg = cfg.getGroup(groupName);
        if (groupCfg == null) {
            return null;
        }
        return new ArrayList(groupCfg.getKeys());
    }

    private static Digester getDigester() throws ParserConfigurationException,
        SAXException {
        Digester digester = DigesterFactory.newDigester();

        digester.addObjectCreate("groups", GroupsCfg.class);
        digester.addObjectCreate("groups/group", GroupCfg.class);
        digester.addSetProperties("groups/group", "id", "id");
        digester.addSetNext("groups/group", "addGroup");

        digester.addObjectCreate("groups/group/key", KeyValuePair.class);
        digester.addSetNext("groups/group/key", "addKey");
        digester.addSetProperties("groups/group/key", "id", "key");
        digester.addBeanPropertySetter("groups/group/key", "value");

        return digester;
    }

    static {
        try {
            digester = getDigester();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
