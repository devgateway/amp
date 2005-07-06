package org.digijava.kernel.syndication.aggregator;

import java.io.IOException;
import java.io.InputStream;

import org.digijava.kernel.syndication.digester.Rss;
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
abstract class RssData {
    private Rss rss;

    abstract Rss parseXml(InputStream stream) throws SAXException, IOException;

    public Rss getRss() {
        return rss;
    }

    public void setRss(Rss rss) {
        this.rss = rss;
    }

}
