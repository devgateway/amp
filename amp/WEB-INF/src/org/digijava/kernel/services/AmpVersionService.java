package org.digijava.kernel.services;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

/**
 * @author Octavian Ciubotaru
 */
@Component
public class AmpVersionService {

    private final Logger logger = Logger.getLogger(AmpVersionService.class);

    private AmpVersionInfo versionInfo;

    @Autowired
    public AmpVersionService(WebApplicationContext webApplicationContext) {
        ServletContext servletContext = webApplicationContext.getServletContext();
        String filePath = servletContext.getRealPath("/") + Security.getSiteConfigPath();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            Document doc = builder.parse(filePath);
            NamedNodeMap entities = doc.getDoctype().getEntities();

            versionInfo = new AmpVersionInfo();
            versionInfo.setAmpVersion(entities.getNamedItem("ampVersion").getTextContent());
            versionInfo.setReleaseDate(entities.getNamedItem("releaseDate").getTextContent());
            versionInfo.setBuildSource(entities.getNamedItem("buildSource").getTextContent());
            versionInfo.setBuildDate(entities.getNamedItem("buildDate").getTextContent());
        } catch (IOException | ParserConfigurationException | SAXException e) {
            logger.error("Couldn't parse xml file " + filePath, e);
        }
    }

    public AmpVersionInfo getVersionInfo() {
        return versionInfo;
    }

    public boolean isAmpOfflineCompatible(String ampOfflineVersion) {
        return true;
    }
}
