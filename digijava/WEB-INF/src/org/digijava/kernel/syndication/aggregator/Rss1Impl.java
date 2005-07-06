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
public class Rss1Impl
    extends RssData {

    private static Logger logger = Logger.getLogger(Rss2Impl.class);

    public Rss1Impl() {
    }

    /**
     * parseXml
     *
     * @param stream InputStream
     * @throws Exception
     * @todo Implement this org.digijava.kernel.aggregator.RssData method
     */
    public Rss parseXml(InputStream stream) throws SAXException, IOException{

        Digester digester = new Digester();

        digester.clear();
        digester.setValidating(false);
        digester.setUseContextClassLoader(true);

        digester.addObjectCreate("rdf:RDF", Rss.class);
        digester.addObjectCreate("rdf:RDF/channel", RssChannel.class);
        digester.addObjectCreate("rdf:RDF/item", RssItem.class);

        digester.addBeanPropertySetter("rdf:RDF/channel/title");
        digester.addBeanPropertySetter("rdf:RDF/channel/link");
        digester.addBeanPropertySetter("rdf:RDF/channel/description");
        digester.addBeanPropertySetter("rdf:RDF/channel/dc:language","language");
        digester.addBeanPropertySetter("rdf:RDF/channel/dc:date", "pubDate");

        digester.addSetNext("rdf:RDF/channel", "setChannel");
        digester.addSetNext("rdf:RDF/item", "addItem");

        digester.addBeanPropertySetter("rdf:RDF/item/title");
        digester.addBeanPropertySetter("rdf:RDF/item/link");
        digester.addBeanPropertySetter("rdf:RDF/item/description");
        digester.addBeanPropertySetter("rdf:RDF/item/dc:date","pubDate");


        Rss rss = null;
        rss = (Rss)digester.parse(stream);
        rss.getChannel().setItems(rss.getItems());
        setRss(rss);

        return rss;

    }
}
