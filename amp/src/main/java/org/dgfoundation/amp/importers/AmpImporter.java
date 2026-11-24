/**
 * 
 */
package org.dgfoundation.amp.importers;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * @author mihai
 * 
 */
public abstract class AmpImporter {

    private static Logger logger = Logger.getLogger(AmpImporter.class);
    private static final int IMPORT_BATCH_SIZE = 500;

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
            logger.error(e.getMessage(), e);
            return;
        }
        
        session = PersistenceManager.getSession(); // ensure a clean Session exists
        try {
            int importedRows = 0;
            while (true) {          
                Map<String, String> o = parseNextLine();
                if (o == null) break;
                saveToDB(o);
                importedRows++;
                if (importedRows % IMPORT_BATCH_SIZE == 0) {
                    session.flush();
                    session.getTransaction().commit();
                    session.clear();
                    session.beginTransaction();
                    logger.info("************************* Imported " + importedRows + " rows from " + importFileName+" *************************");
                }
            };
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            logger.error("error while running import on " + this.getClass().getName(), e);
        } finally {
            PersistenceManager.closeQuietly(fr);
            PersistenceManager.cleanupSession(session);
        }
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
