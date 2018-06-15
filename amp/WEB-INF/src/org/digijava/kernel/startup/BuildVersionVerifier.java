package org.digijava.kernel.startup;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dgfoundation.amp.ar.FilterParam;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class BuildVersionVerifier {
    
    static private BuildVersionVerifier instance = null;
    private final AmpVersion codeVersion;
    private final AmpVersion dbVersion;
    private final String path;
    
    static public BuildVersionVerifier getInstance(String path) throws Exception {
        if (instance == null)
            instance = new BuildVersionVerifier(path);
        return instance;
    }
    
    private BuildVersionVerifier(String path) throws SAXException, IOException, ParserConfigurationException {
        this.path = path;
        this.codeVersion = getCodeBuildVersion();
        this.dbVersion = getLatestAmpStartupVersion();
    }
    
    private class AmpVersion implements Comparable<AmpVersion>{
        final String version;
        final Long encodedVersion;
        /**
         * Obtains the encoded value of an amp version -- x[.yy][.zz][.yy], encoded
         * 
         * 2.22.22 would become   2222200
         * 2.2.1   would become   2020100
         * 2.2     would become   2020000
         * 2.2.1.1 would become   2020101 */
        private Long encodeVersion(String version) {
            version = version.replace("-SNAPSHOT", "");
            String[] tokens = version.split("\\.");
            Long result = 0L;
            /* 
             * this part assumes the longest form is y.xx.xx.xx, but it's usually y.xx.xx
             * therefore, padding the non-existing digits (for cases like y.xx, y.xx.xx, or even just y)
             */
            for (int i = 0; i < 4; i++) {
                if (i < tokens.length)
                    result += Long.parseLong(tokens[i]);
                result *= 100;
            }
            return result;
        }
        public AmpVersion(String version, Long encodedVersion) {
            this.version = version;
            this.encodedVersion = encodedVersion;
        }
        public AmpVersion(String version) {
            this.version = version;
            this.encodedVersion = encodeVersion(version);
        }
        
        @Override
        public int compareTo(AmpVersion arg0) {
            return this.encodedVersion.compareTo(arg0.encodedVersion);
        }
    }
    
    private AmpVersion getCodeBuildVersion() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = docFactory.newDocumentBuilder();
        Document doc;
        doc = builder.parse(path);
        String version = doc.getDoctype().getEntities().getNamedItem("ampVersion").getTextContent();
        return new AmpVersion(version);
    }

    
    
    /**
     * writes to amp_global_event_log a new event and a message, accompanied by the version it gets started on
     * @param eventName
     * @param message
     */
    public void writeVersionToStartupLog(final String eventName, final String message, String xmlPath) {
        //if it's the first run since version tracking was added, just wait for the XML patcher to add the initial startup afterwards
        if (!SQLUtils.tableExists("amp_global_event_log"))
            return; 
        PersistenceManager.doWorkInTransaction(conn -> {
            List<String> columnNames = Arrays.asList("event_name", "message", "amp_version", "amp_version_encoded");
            List<Object> values = new ArrayList<Object>(Arrays.asList(eventName, message, codeVersion.version, codeVersion.encodedVersion));
            SQLUtils.insert(conn, "amp_global_event_log", "id", "amp_global_event_log_id_seq", columnNames, Arrays.asList(values));
        });
    }
    
    private AmpVersion getLatestAmpStartupVersion() {
        if (!SQLUtils.tableExists("amp_global_event_log"))
            return new AmpVersion(null, null);
        final String query = "SELECT amp_version, amp_version_encoded FROM amp_global_event_log "
                + "WHERE amp_version_encoded IN (SELECT max(amp_version_encoded) FROM amp_global_event_log) LIMIT 1";
        return PersistenceManager.doReturningWorkInTransaction(conn -> {
            RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null);
            String version = null;
            Long versionEncoded = null;
            if (rsi.rs.next()) {
                version = rsi.rs.getString(1);
                versionEncoded = rsi.rs.getLong(2);
            }
            return new AmpVersion(version, versionEncoded);
        });
    }
    
    public void checkAmpVersionCompatibility() {
        if (this.dbVersion.encodedVersion == null) //no table yet, this is the first startup, it's fine
            return;
        if (this.dbVersion.encodedVersion > this.codeVersion.encodedVersion)
            throw new RuntimeException(String.format("AMP was last started on %s, code version is %s, crashing startup on purpose!", this.dbVersion.version, this.codeVersion.version));
    }
}
