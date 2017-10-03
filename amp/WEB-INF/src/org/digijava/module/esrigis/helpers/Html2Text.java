package org.digijava.module.esrigis.helpers;

import java.io.IOException;
import java.io.StringReader;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Take HTML and give back the text part while dropping the HTML tags.
 * 
 * There is some risk that using TagSoup means we'll permute non-HTML text.
 * However, it seems to work the best so far in test cases.
 * 
 * @author Diego
 * @see <a href="http://home.ccil.org/~cowan/XML/tagsoup/">TagSoup</a>
 */
public class Html2Text implements ContentHandler {
    private StringBuffer sb;

    public Html2Text() {
    }

    public void parse(String str) throws IOException, SAXException {
        XMLReader reader = new Parser();
        reader.setContentHandler(this);
        sb = new StringBuffer();
        reader.parse(new InputSource(new StringReader(str)));
    }

    public String getText() {
        return sb.toString();
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        for (int idx = 0; idx < length; idx++) {
            sb.append(ch[idx + start]);
        }
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
        sb.append(ch);
    }

    // The methods below do not contribute to the text
    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override
    public void processingInstruction(String target, String data)
            throws SAXException {
    }

    @Override
    public void setDocumentLocator(Locator locator) {
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
    }

    @Override
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
    }
}
