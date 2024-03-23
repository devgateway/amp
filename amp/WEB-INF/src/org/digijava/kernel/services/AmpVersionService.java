package org.digijava.kernel.services;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.security.Security;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOfflineCompatibleVersionRange;
import org.digijava.module.aim.dbentity.AmpOfflineRelease;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

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
    
    public boolean isAmpOfflineCompatible(AmpOfflineRelease release) {
        return release != null && isAmpOfflineCompatible(release.getVersion());
    }

    public boolean isAmpOfflineCompatible(String ampOfflineVersion) {
        AmpOfflineVersion version = new AmpOfflineVersion(ampOfflineVersion);

        List<AmpOfflineCompatibleVersionRange> ranges = getCompatibleVersionRanges();

        for (AmpOfflineCompatibleVersionRange range : ranges) {
            if (version.compareTo(range.getFromVersion()) >= 0
                    && version.compareTo(range.getToVersion()) <= 0) {
                return true;
            }
        }

        return false;
    }

    public List<AmpOfflineCompatibleVersionRange> getCompatibleVersionRanges() {
        Session session = PersistenceManager.getSession();
        return (List<AmpOfflineCompatibleVersionRange>) session.createCriteria(AmpOfflineCompatibleVersionRange.class)
                .setCacheable(true)
                .list();
    }

    public AmpOfflineCompatibleVersionRange addCompatibleVersionRange(AmpOfflineCompatibleVersionRange range) {
        range.setId(null);
        ensureIsValid(range);
        PersistenceManager.getSession().save(range);
        return range;
    }

    public void ensureIsValid(AmpOfflineCompatibleVersionRange range) {
        if (range.getFromVersion().compareTo(range.getToVersion()) > 0) {
            throw new IllegalArgumentException("To version must be greater than from.");
        }
    }

    public AmpOfflineCompatibleVersionRange updateCompatibleVersionRange(AmpOfflineCompatibleVersionRange range) {
        ensureIsValid(range);
        PersistenceManager.getSession().update(range);
        return range;
    }

    public AmpOfflineCompatibleVersionRange deleteCompatibleVersionRange(Long id) {
        Session session = PersistenceManager.getSession();
        AmpOfflineCompatibleVersionRange range =
                (AmpOfflineCompatibleVersionRange) session.load(AmpOfflineCompatibleVersionRange.class, id);
        session.delete(range);
        return range;
    }
}
