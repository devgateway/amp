package org.digijava.kernel.syndication.aggregator;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.digijava.kernel.syndication.digester.Rss;
import org.digijava.kernel.syndication.digester.RssChannel;
import org.digijava.kernel.syndication.digester.RssItem;
import org.xml.sax.SAXException;

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

        Rss rss = null;
        rss = (Rss) digester.parse(stream);
        setRss(rss);

        return rss;
    }
}
