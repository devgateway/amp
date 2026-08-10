package org.digijava.kernel.util;

import org.apache.commons.digester.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Helpers to lock down JAXB and Commons Digester XML parsing against XML External Entity (XXE) attacks
 * (AMP-SEC-025/026/061/062): disallow DOCTYPE declarations and external entity/DTD resolution.
 */
public final class XmlSecurityUtils {

    private static final String FEATURE_DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";
    private static final String FEATURE_EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
    private static final String FEATURE_EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
    private static final String FEATURE_LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

    private XmlSecurityUtils() {
    }

    /** A {@link SAXParserFactory} hardened against XXE, with DOCTYPE declarations disallowed entirely. */
    public static SAXParserFactory secureSaxParserFactory() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            factory.setFeature(FEATURE_DISALLOW_DOCTYPE, true);
            factory.setFeature(FEATURE_EXTERNAL_GENERAL_ENTITIES, false);
            factory.setFeature(FEATURE_EXTERNAL_PARAMETER_ENTITIES, false);
            factory.setXIncludeAware(false);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException("Unable to configure a secure SAXParserFactory", e);
        }
        return factory;
    }

    private static XMLReader secureXmlReader() {
        try {
            return secureSaxParserFactory().newSAXParser().getXMLReader();
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException("Unable to create a secure XMLReader", e);
        }
    }

    /** Wraps a stream so it can be safely passed to {@link Unmarshaller#unmarshal(Source)}. */
    public static Source secureSource(InputStream inputStream) {
        return new SAXSource(secureXmlReader(), new InputSource(inputStream));
    }

    /** Wraps a reader so it can be safely passed to {@link Unmarshaller#unmarshal(Source)}. */
    public static Source secureSource(Reader reader) {
        return new SAXSource(secureXmlReader(), new InputSource(reader));
    }

    /** Wraps a file so it can be safely passed to {@link Unmarshaller#unmarshal(Source)}. */
    public static Source secureSource(File file) throws FileNotFoundException {
        InputSource inputSource = new InputSource(new FileInputStream(file));
        inputSource.setSystemId(file.toURI().toString());
        return new SAXSource(secureXmlReader(), inputSource);
    }

    /** Wraps an existing {@link InputSource} so it can be safely passed to {@link Unmarshaller#unmarshal(Source)}. */
    public static Source secureSource(InputSource inputSource) {
        return new SAXSource(secureXmlReader(), inputSource);
    }

    /**
     * Hardens a Commons Digester instance against XXE. DOCTYPE declarations are disallowed entirely
     * unless {@code allowDoctypeDecl} is true, which is only needed when the digester validates
     * against a locally registered DTD.
     */
    public static void secureDigester(Digester digester, boolean allowDoctypeDecl) {
        try {
            if (!allowDoctypeDecl) {
                digester.setFeature(FEATURE_DISALLOW_DOCTYPE, true);
            }
            digester.setFeature(FEATURE_EXTERNAL_GENERAL_ENTITIES, false);
            digester.setFeature(FEATURE_EXTERNAL_PARAMETER_ENTITIES, false);
            digester.setFeature(FEATURE_LOAD_EXTERNAL_DTD, false);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException("Unable to configure a secure Digester", e);
        }
    }

    /** Hardens a Commons Digester instance against XXE, disallowing DOCTYPE declarations entirely. */
    public static void secureDigester(Digester digester) {
        secureDigester(digester, false);
    }
}
