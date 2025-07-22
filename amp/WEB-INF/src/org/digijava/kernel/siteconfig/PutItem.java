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

package org.digijava.kernel.siteconfig;

/**
 * Data class, used by Digester to parse site-config.xml.
 * See org.digijava.kernel.util.ConfigurationManager for more details
 * @author Mikheil Kapanadze
 * @version 1.0
 */
public class PutItem
    extends ConfigurationItem {

    /*
     <put-item> tag in site-config.xml can be:
     0. invalid
     1. empty
     2. layout=
     3. file=
     4. module=, instance=
     5. module=, instance=, teaser=
     6. module=, page=
     */

    public static final byte INVALID_ITEM = -1;
    public static final int EMPTY_ITEM = 0;
    public static final int LAYOUT_ITEM = 0x10;
    public static final int FILE_ITEM = 0x8;
    public static final int MOD_INST_ITEM = 0x5;
    public static final int MOD_INST_TEASER_ITEM = 0x25;
    public static final int MOD_PAGE_ITEM = 0x3;

    private static final int[] itemTypes = {
        EMPTY_ITEM, LAYOUT_ITEM, FILE_ITEM, MOD_INST_ITEM, MOD_INST_TEASER_ITEM,
        MOD_PAGE_ITEM};

    private String name;
    private String value;
    private String module; // 0x01
    private String page; // 0x02
    private String instance; // 0x04
    private String file; // 0x08
    private String layout; // 0x10
    private String teaser; // 0x20

    public PutItem() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "<put-item name=\"" + name + "\" value=\"" + value +
            "\" layout=\"" + layout + "\" file=\"" + file +
            "\" module=\"" + module + "\" instance=\"" + instance +
            "\" page=\"" + page + "\" teaser=\"" + teaser +
            "\"/>";
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void merge(ConfigurationItem configurationItem) {
        PutItem secondPutItem = (PutItem) configurationItem;

        value = secondPutItem.getValue();
        module = secondPutItem.getModule();
        page = secondPutItem.getPage();
        instance = secondPutItem.getInstance();
        file = secondPutItem.getFile();
        layout = secondPutItem.getLayout();
        teaser = secondPutItem.getTeaser();
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void validate() throws Exception {
        if (getItemType() == EMPTY_ITEM) {
            throw new Exception("invalid site-config.xml entry " +
                                this.toString());
        }
    };

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public int getItemType() {
        if (name == null)
            return INVALID_ITEM;

        int mask =
            (module == null ? 0 : 1) |
            (page == null ? 0 : 2) |
            (instance == null ? 0 : 4) |
            (file == null ? 0 : 8) |
            (layout == null ? 0 : 16) |
            (teaser == null ? 0 : 32);

        for (int i = 0; i < itemTypes.length; i++) {
            if (itemTypes[i] == mask) {
                return itemTypes[i];
            }
        }
        return INVALID_ITEM;
    }

}
