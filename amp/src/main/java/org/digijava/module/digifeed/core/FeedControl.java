/**
 * FeedControl.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.digifeed.core;

import org.apache.log4j.Logger;
import org.digijava.module.digifeed.exception.UninitializedDigifeedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.zip.GZIPOutputStream;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 20, 2005
 * Class that provides init and access to the digiFeed system.
 */
public class FeedControl {

public FeedControl() {
        feeds = new ArrayList();
    }

    private static Logger logger = Logger.getLogger(FeedControl.class);
    /**
     * this list is initialized at Digi startup by the initFeeds() method and holds
     * FeedAccess objects, one for each successfully initialized feed. 
     * @see FeedAccess
     */
    private static List feeds;

    /**
     * prefix for the plugin packages
     */
    private static final String modulePackage="org.digijava.module.digifeed.feeds.";

    /**
     * By using reflection, this method automatically initializes the feeds
     * available inside the modulePackage package
     * 
     */
    public static void initFeeds(String abstractRootPath) {
        // first get a view of the feeds directories:

        File dir = new File(abstractRootPath+FeedConstants.pluginPath);
        feeds=new ArrayList();
        FileFilter filter = new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory();
            }
        };

        File feedFiles[] = dir.listFiles(filter);

        
        for (int i = 0; feedFiles!=null && i <  feedFiles.length; i++) {
            String feedName=feedFiles[i].getName();
            
            try {
                Class queryClass = Class.forName(modulePackage
                        + feedName + ".FeedQuery");
                Constructor conQ = queryClass.getConstructors()[0];
                GenericFeedQuery feedQuery = (GenericFeedQuery) conQ
                        .newInstance((Object[]) null);
                
                
                Class  binderClass = Class.forName(modulePackage
                        + feedName + ".FeedBinder");
                
                Constructor conB = binderClass.getConstructors()[0];
                GenericFeedBinder binder= (GenericFeedBinder)conB.newInstance((Object[])null);
    
                Class  requestClass = Class.forName(modulePackage
                        + feedName + ".FeedRequest");
                
                Constructor conR = requestClass.getConstructors()[0];
                GenericFeedRequest requestSample= (GenericFeedRequest)conR.newInstance((Object[])null);
    
                
                FeedInfo feedInfo = loadFeedInfo(abstractRootPath+FeedConstants.pluginPath + "/"
                        + feedName);
    
                feedInfo.setJaxbPackage(modulePackage+feedName+".schema");
                
                FeedAccess feedAccess = new FeedAccess(feedInfo, feedQuery, binder,requestSample);
                feeds.add(feedAccess);
                logger.info("Feed "+feedInfo.getName()+ "("+feedName+") initialized.");

            } catch (Exception e) {
                logger.info("Cannot initialize feed "+feedName+ " !!");
                logger.info(e.getMessage(), e);
            }
        }

    }

    /**
     * Loads regular info about the feed, from a file named <b>schema.properties</b> 
     * @param feedPath local system path to feed's directory
     * @return a bean with the information from the properties file
     */
    private static FeedInfo loadFeedInfo(String feedPath) {
        Properties p = new Properties();
        FeedInfo feedInfo = null;
        try {
            p.load(new FileInputStream(feedPath + "/schema.properties"));

            feedInfo = new FeedInfo();

            // initialize the feedinfo bean:
            feedInfo.setId(p.getProperty("id"));
            feedInfo.setName(p.getProperty("name"));
            feedInfo.setVersion(p.getProperty("version"));
            feedInfo.setUrl(p.getProperty("url"));
            feedInfo.setDescription(p.getProperty("description"));
            feedInfo.setAuthorName(p.getProperty("author.name"));
            feedInfo.setAuthorEmail(p.getProperty("author.email"));
            feedInfo.setGzip(p.getProperty("gzip").equals("yes")?true:false);
            feedInfo.setJaxbPackage(modulePackage+feedInfo.getId()+".schema");
            feedInfo.setEncoding(p.getProperty("encoding"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info(feedInfo.toString());
        
        return feedInfo;

    }
    
/**
 * exception management
 * @param out the output stream - to write the exception stacktrace
 * @param message
 */  
    public static void errorResponse(OutputStream out, String message) {
        PrintStream ps = new PrintStream( out );
        ps.println("--- ERROR! ---");
        ps.println();
        ps.println("Error message:");
        ps.println();
        ps.println(message);
        ps.println();
        ps.println("--------------");
        ps.close(); }
    
    
    public static void errorResponse(OutputStream out, Exception e) {
        PrintStream ps = new PrintStream( out );
        ps.println("--- ERROR! ---");
        ps.println();
        ps.println("Error message:");
        ps.println();
        e.printStackTrace( ps );
        ps.println();
        ps.println("--------------");
        ps.close();
    }

    
    public static boolean isGZipped(Map params) throws UninitializedDigifeedException {
        String[] feedid=(String[])params.get("feed");
        
        FeedAccess fa = getFeedAccess(feedid[0]);
        return fa.getInfo().isGzip();
    }
    

    
    /**
     * Generates XML output based on the request parameter map
     * @param params the request parameter map
     * @return
     */
    public static void fetchXML(Map params,HttpServletRequest request,
            HttpServletResponse response) {
        
        
        try {
            OutputStream normalOut=response.getOutputStream();
            
            
            //first get the feedaccess object for the requested feed:

            
            if (!params.containsKey("feed")) {
                errorResponse(normalOut,
                        "Please specify the feed id! (parameter 'feed')");
                return;
            }
            
            String[] feedid=(String[])params.get("feed");
            
                FeedAccess fa = getFeedAccess(feedid[0]);
            if (fa == null) {
                errorResponse(normalOut, "Unknown feed id! ");
                return;
            }
            
            
            //we now have a feed access, let's generate the request object
            GZIPOutputStream gzout=new GZIPOutputStream(normalOut);
            
            OutputStream out=null;
            
            if(fa.getInfo().isGzip()) out=gzout;else out=normalOut; 
            
            GenericFeedRequest freq=(GenericFeedRequest)fa.getSampleRequest().clone();
            freq.initialize(params);
            logger.info("freq.maxblock="+freq.getMaxBlockSize());
            freq.setServerPath(request.getServletPath());
            
            
            //fetch the objects from the database
            GenericFeedQuery q=(GenericFeedQuery) fa.getQuery().clone();
            
            //the bObj can be either a count result (Long) or a List. Output treatment is different
            Object businessObj=q.query(freq);
            
            if (businessObj instanceof List) {
    
            //generating binder object
            GenericFeedBinder binder=(GenericFeedBinder)fa.getSampleBinder().clone();

            binder.setRequest(freq);
            binder.setLast(q.isLast());
            binder.setNextStartWidth(q.getNextStartWidth());
            
            //create xml-aware beans
            binder.setSrc((List)businessObj);
            binder.bind();
            
            //generate the XML stream
            FeedGenerator fg=new FeedGenerator(fa.getInfo());
            fg.generateXML(binder.getFinalTree(), out);
            
            out.close();
            
            } else if (businessObj instanceof Integer) {
                PrintWriter pr=new PrintWriter(out);
                pr.println("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>");
                pr.println("<count query=\""+freq.toString()+"\">");
                pr.println(((Integer)businessObj).intValue());
                pr.println("</count>");
                pr.close();
            }
            
            
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            
        }
    }
    
    /**
     * searches the initialized feed data for a feed with the given id.
     * @param id
     * @return the identified FeedAccess object
     * @throws UninitializedDigifeedException if no FeedAccess object is available for that feed id
     */
    private static FeedAccess getFeedAccess(String id) throws UninitializedDigifeedException {
        if (feeds==null) throw new UninitializedDigifeedException();
        Iterator i=feeds.iterator();
        while (i.hasNext()) {
            FeedAccess element = (FeedAccess) i.next();
            if (element.getInfo().getId().equalsIgnoreCase(id)) return element;
        }
        return null;
    }


    /**
     * @return Returns the feeds.
     */
    public static List getFeeds() {
        return feeds;
    }

     

}
