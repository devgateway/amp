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

package org.digijava.kernel.util;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Wrapper class around the Digester that hide Digester's initialization details
 * @author Jean-Francois Arcand
 * @author Mikheil Kapanadze
 */

public class DigesterFactory{
    /**
     * The log.
     */
   protected static org.apache.commons.logging.Log log =
       org.apache.commons.logging.LogFactory.getLog(DigesterFactory.class);

   protected static SAXParserFactoryImpl parserFactory = new org.apache.xerces.jaxp.
       SAXParserFactoryImpl();

    public static final String digiConfigPublicId_10 = "digi-config_1_0.xsd";
    public static final String digiConfigPath_10 =
        "org/digijava/kernel/util/resource/digi-config_1_0.xsd";

    public static final String LocaleConfigPublicId_10 =
        "-//Digijava Project//DTD Locale Configuration 1.0//EN";
    public static final String LocaleConfigPath_10 =
        "/org/digijava/kernel/text/data/LocaleData_1_0.dtd";


    /**
     * The XML entiry resolver used by the Digester.
     */
    private static SchemaResolver schemaResolver;


    /**
     * Create a <code>Digester</code> parser with no <code>Rule</code>
     * associated and XML validation turned off.
     */
    public static Digester newDigester() throws SAXException,
        ParserConfigurationException {
        return newDigester(false, false, null);
    }


    /**
     * Create a <code>Digester</code> parser with XML validation turned off.
     * @param rule an instance of <code>RuleSet</code> used for parsing the xml.
     */
    public static Digester newDigester(RuleSet rule) throws SAXException,
        ParserConfigurationException {
        return newDigester(false,false,rule);
    }


    /**
     * Create a <code>Digester</code> parser.
     * @param xmlValidation turn on/off xml validation
     * @param xmlNamespaceAware turn on/off namespace validation
     * @param rule an instance of <code>RuleSet</code> used for parsing the xml.
     */
    public static Digester newDigester(boolean xmlValidation,
                                       boolean xmlNamespaceAware,
                                       RuleSet rule) throws SAXException,
        ParserConfigurationException {

        SAXParser saxParser =  new SAXParser();

        if (xmlValidation) {
            saxParser.setFeature("http://xml.org/sax/features/validation", true);
            saxParser.setFeature(
                "http://apache.org/xml/features/validation/dynamic", true);
            saxParser.setFeature("http://apache.org/xml/features/validation/schema", true);
            saxParser.setFeature(
                "http://apache.org/xml/features/validation/schema-full-checking",
                true);
            schemaResolver = new SchemaResolver(DigesterFactory.class.
                                                getClassLoader());
            registerLocalSchema();
            saxParser.setEntityResolver(schemaResolver);
        }
        else {
            schemaResolver = null;
        }
        if (xmlNamespaceAware) {
            saxParser.setFeature("http://xml.org/sax/features/namespaces", true);
            saxParser.setFeature(
                "http://xml.org/sax/features/namespace-prefixes", true);
        }

        Digester digester = new Digester();
        digester.setValidating(false);
        digester.setNamespaceAware(false);
        digester.setUseContextClassLoader(true);
        if (schemaResolver != null) {
            digester.setEntityResolver(schemaResolver);
        }
        if (rule != null) {
            digester.addRuleSet(rule);
        }

        return (digester);
    }


    /**
     * Utilities used to force the parser to use local schema, when available,
     * instead of the <code>schemaLocation</code> XML element.
     */
    protected static void registerLocalSchema(){
        // digi.xml
        register(digiConfigPath_10,
                 digiConfigPublicId_10);

        // locale configuration
        register(LocaleConfigPath_10,
                 LocaleConfigPublicId_10);
    }


    /**
     * Load the resource and add it to the resolver.
     */
    protected static void register(String resourceURL, String resourcePublicId) {
        schemaResolver.register(resourcePublicId, resourceURL);
    }

}
