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

package org.digijava.kernel.syndication.publicator;

import org.digijava.kernel.syndication.digester.Rss;
import org.digijava.kernel.syndication.digester.RssChannel;
import org.digijava.kernel.syndication.digester.RssItem;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Rss2Xml
    extends Rss {

    private final static String RSS_VERSION = "2.0";

    /**
     *
     * @return StringBuffer
     */
    public StringBuffer writeXml() {
        StringBuffer rssData = null;

        setVersion(RSS_VERSION);

        Vector items = getItems();
        if (items == null) {
            items = getChannel().getItems();
        }

        if (items != null && items.size() > 0) {

            rssData = new StringBuffer(8192);
            SimpleDateFormat formatter
                = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

            RssChannel channel = getChannel();

            rssData.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            rssData.append("<rss version=\"" + getVersion() + "\" >");
            rssData.append("<channel>");
            addNode(rssData, "title", channel.getTitle());
            addNode(rssData, "description", channel.getDescription());
            addNode(rssData, "link", channel.getLink());
            addNode(rssData, "language", channel.getLanguage());
            addNode(rssData, "category", channel.getCategory());
            addNode(rssData, "pubDate", formatter.format(channel.getDate()));
            addNode(rssData, "generator", channel.getGenerator());

            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                rssData.append("<item>");
                RssItem item = (RssItem) iter.next();

                addNode(rssData, "title", item.getTitle());
                addNode(rssData, "description", item.getDescription());
                addNode(rssData, "link", item.getLink());
                addNode(rssData, "pubDate", formatter.format(item.getDate()));

                rssData.append("</item>");
            }

            rssData.append("</channel>");
            rssData.append("</rss>");

        }

        return rssData;
    }


    /**
     *
     * @param buffer StringBuffer
     * @param name String
     * @param value String
     */
    private void addNode(StringBuffer buffer, String name, String value) {

        if (value != null && value.length() > 0) {
            buffer.append("<" + name + ">" + substitution(value) + "</" + name + ">");
        } else {
            buffer.append("<" + name + "></" + name + ">");
        }
    }


    /**
     *
     * @param text String
     * @return String
     */
    public String substitution(String text) {

        text = text.replaceAll("&", "&amp;");
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        text = text.replaceAll("\'", "&apos;");
        text = text.replaceAll("\"", "&quot;");

        return text;
    }

}
