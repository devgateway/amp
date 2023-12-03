/**
 * 
 */
package org.dgfoundation.amp.importers;

import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author mihai
 * 
 */
public class ImporterWorker {

    private static Logger logger = Logger.getLogger(ImporterWorker.class);

    private ServletContext sc;
    private String importerHome;
    private List<Properties> importersConfigs;
    private static final String importerPackage="org.dgfoundation.amp.importers";
    

    public ImporterWorker(String importerHome) {
        this.importerHome = importerHome;
        importersConfigs=new ArrayList<Properties>();
    }
    
    private void loadImporters() {
        String realPath = importerHome;

        File dir = new File(realPath);
        FileFilter filter = new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith(".properties");
            }
        };
        
        if (!dir.isDirectory())
            throw new RuntimeException(
                    "Importer path is invalid! Should be a directory: "
                            + dir.getAbsolutePath());
        File[] files = dir.listFiles(filter);
        for (int i = 0; i < files.length; i++) {
            Properties p = new Properties();
            try {
                p.load(new FileInputStream(files[i]));
            } catch (FileNotFoundException e) {
                logger.error("Cannot find configuration file " + files[i]
                        + " for the importer " + this.getClass().getName());
                e.printStackTrace();
            } catch (IOException e) {
                logger.error("Cannot read configuration file " + files[i]
                        + " for the importer " + this.getClass().getName());
                e.printStackTrace();
            }
            importersConfigs.add(p);
        }
    }

    public void start() {
        loadImporters();
        performImports();
    }
    
    
    private void performImports() {
        Iterator<Properties> i=importersConfigs.iterator();
        while (i.hasNext()) {
            Properties properties = (Properties) i.next();
            String enabled = properties.getProperty("enabled");
            if(!("true".equals(enabled))) continue;
            try {
                Class c=Class.forName(importerPackage+"."+properties.getProperty("importerClassName"));
                Class []parameterTypes=new Class[] {String.class ,String[].class ,Properties.class};
                Constructor constructor = c.getConstructor(parameterTypes);
                
                //parse the names of columns that will be imported:
                StringTokenizer st=new StringTokenizer(properties.getProperty("columnNames"),",");
                List<String> columns=new ArrayList<String>();
                while(st.hasMoreTokens()) {
                    String col=st.nextToken();
                    columns.add(col);
                }
                
                AmpImporter ai = (AmpImporter) constructor.newInstance(properties.getProperty("importFileName"),columns.toArray(new String[0]),properties);
                ai.performImport();
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage(), e);
            } catch (SecurityException e) {
                logger.error(e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                logger.error(e.getMessage(), e);
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage(), e);
            } catch (InstantiationException e) {
                logger.error(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                logger.error(e.getMessage(), e);
            }
        }       
    }
    
    public ServletContext getSc() {
        return sc;
    }

    public void setSc(ServletContext sc) {
        this.sc = sc;
    }

    public String getImporterHome() {
        return importerHome;
    }

    public void setImporterHome(String importerHome) {
        this.importerHome = importerHome;
    }

}
