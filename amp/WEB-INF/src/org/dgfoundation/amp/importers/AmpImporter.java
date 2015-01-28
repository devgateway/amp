/**
 * 
 */
package org.dgfoundation.amp.importers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.kernel.persistence.PersistenceManager;

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
		
		session = PersistenceManager.getSession();
		
		Map<String, String> o = null;
		do {
			try {
				o = parseNextLine();
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
			if (o != null)
				try {
					saveToDB(o);
				} catch (HibernateException e) {
					logger.error(e);
					e.printStackTrace();
				}
		} while (o != null);
		try {
			fr.close();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
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
