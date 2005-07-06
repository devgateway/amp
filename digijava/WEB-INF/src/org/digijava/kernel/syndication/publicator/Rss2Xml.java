/*
 *   Rss2.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: Rss2Xml.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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
package org.digijava.kernel.syndication.publicator;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;

import org.digijava.kernel.syndication.digester.Rss;
import org.digijava.kernel.syndication.digester.RssChannel;
import org.digijava.kernel.syndication.digester.RssItem;

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

            rssData.append("<?xml version=\"1.0\" ?>");
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
                addNode(rssData, "description", item.getDescription(), true);
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
        addNode(buffer, name, value, false);
    }

    /**
     *
     * @param buffer StringBuffer
     * @param name String
     * @param value String
     * @param cdata boolean
     */
    private void addNode(StringBuffer buffer, String name, String value,
                         boolean cdata) {

        if (value != null && value.length() > 0) {
            if (cdata) {
                buffer.append("<" + name + "><![CDATA[" + value + "]]></" +
                              name + ">");
            }
            else {
                buffer.append("<" + name + ">" + value + "</" + name + ">");
            }
        }
    }

}
