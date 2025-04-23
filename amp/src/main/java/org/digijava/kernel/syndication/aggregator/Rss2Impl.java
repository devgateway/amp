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

package org.digijava.kernel.syndication.aggregator;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.digijava.kernel.syndication.digester.Rss;
import org.digijava.kernel.syndication.digester.RssChannel;
import org.digijava.kernel.syndication.digester.RssItem;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Rss2Impl
    extends RssData {

    private static Logger logger = Logger.getLogger(Rss2Impl.class);

    public Rss2Impl() {
    }

    /**
     * Parse RSS 2.0 xml file
     *
     * @param stream InputStream
     * @todo Implement this org.digijava.kernel.aggregator.RssData method
     */
    public Rss parseXml(InputStream stream) throws SAXException, IOException {

        Digester digester = createDigester();

        Rss rss = null;
        rss = (Rss) digester.parse(stream);
        setRss(rss);

        return rss;
    }

    /**
     *
     * @param data String
     * @return Rss
     * @throws SAXException
     * @throws IOException
     */
    public Rss parseXml(Reader data) throws SAXException, IOException {

        Digester digester = createDigester();

        Rss rss = null;
        rss = (Rss) digester.parse(data);
        setRss(rss);

        return rss;
    }

    /**
     *
     * @return Digester
     */
    public Digester createDigester() {
        Digester digester = new Digester();

        digester.clear();
        digester.setValidating(false);
        digester.setUseContextClassLoader(true);

        digester.addObjectCreate("rss", Rss.class);
        digester.addObjectCreate("rss/channel", RssChannel.class);
        digester.addObjectCreate("rss/channel/item", RssItem.class);

        digester.addSetProperties("rss", "version", "version");

        digester.addBeanPropertySetter("rss/channel/title");
        digester.addBeanPropertySetter("rss/channel/link");
        digester.addBeanPropertySetter("rss/channel/description");
        digester.addBeanPropertySetter("rss/channel/language");
        digester.addBeanPropertySetter("rss/channel/pubDate");
        digester.addBeanPropertySetter("rss/channel/category", "category");

        digester.addSetNext("rss/channel", "setChannel");
        digester.addSetNext("rss/channel/item", "addItem");

        digester.addBeanPropertySetter("rss/channel/item/title");
        digester.addBeanPropertySetter("rss/channel/item/link");
        digester.addBeanPropertySetter("rss/channel/item/description");
        digester.addBeanPropertySetter("rss/channel/item/pubDate");

        return digester;

    }
}
