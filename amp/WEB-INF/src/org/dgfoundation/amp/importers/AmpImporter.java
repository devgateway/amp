/**
 * 
 */
package org.dgfoundation.amp.importers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.services.sync.model.SyncConstants;
import org.digijava.module.aim.dbentity.AmpOfflineChangelog;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * @author mihai
 * 
 */
public abstract class AmpImporter {

    private static Logger logger = Logger.getLogger(AmpImporter.class);

    protected String importFileName;
    protected String[] columnNames;

    protected abstract Class[] getImportedTypes();

    protected abstract Map<String, String> parseNextLine() throws IOException;

    protected abstract void saveToDB(Map<String, String> o) throws HibernateException;

    protected abstract void initializeReader(Reader source);

    protected Session session;

    
    
    protected Reader reader;

    public void performImport() {

        FileReader fr = null;
        try {
            fr = new FileReader(importFileName);
            initializeReader(fr);
        } catch (FileNotFoundException e) {
            logger.error(e);
            //e.printStackTrace();
        }
        
        session = PersistenceManager.getSession(); // ensure a clean Session exists
        try {
            boolean locatorsUpdated = false;
            while (true) {          
                Map<String, String> o = parseNextLine();
                if (o == null) break;
                saveToDB(o);
                locatorsUpdated = true;
            };
            if (locatorsUpdated) {
                addLocatorsAmpOfflineChangelog();
            }
        } catch (Exception e) {
            logger.error("error while running import on " + this.getClass().getName(), e);
        }
        PersistenceManager.closeQuietly(fr);
        PersistenceManager.endSessionLifecycle();
    }

    private void addLocatorsAmpOfflineChangelog() {
        AmpOfflineChangelog changelog = new AmpOfflineChangelog();
        changelog.setEntityName(SyncConstants.Entities.LOCATORS);
        changelog.setOperationName(SyncConstants.Ops.UPDATED);
        changelog.setOperationTime(new Date());
        session.save(changelog);
    }

    protected abstract String getFileType();

    public AmpImporter(String importFileName, String[] columnNames2) {
        this.importFileName = importFileName;
        this.columnNames = columnNames2;
    }

    public String getImportFileName() {
        return importFileName;
    }

    public void setImportFileName(String importFileName) {
        this.importFileName = importFileName;
    }
}
