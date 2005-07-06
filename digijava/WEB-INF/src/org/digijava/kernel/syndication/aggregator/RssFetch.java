/*
 *   RssFetch.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created:
 * 	 CVS-ID: $Id: RssFetch.java,v 1.1 2005-07-06 10:34:25 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/
package org.digijava.kernel.syndication.aggregator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.digijava.kernel.syndication.digester.Rss;
import org.digijava.kernel.syndication.digester.RssChannel;
import org.digijava.kernel.syndication.digester.RssItem;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.module.news.dbentity.News;
import org.digijava.module.news.dbentity.NewsSettings;
import org.digijava.module.news.exception.NewsException;
import org.digijava.module.syndication.dbentity.CollectorFeedItem;
import org.digijava.module.syndication.exception.SyndicationException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.xml.sax.SAXException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class RssFetch
    implements Job {

    private static Logger logger = Logger.getLogger(RssFetch.class);

    private JobExecutionContext currentContext;
    HttpURLConnection connection;

    public void execute(JobExecutionContext cntxt) throws JobExecutionException {
        currentContext = cntxt;
        InputStream is = null;
        Rss rss = null;

        try {

            is = openInputStream();
            if( is == null ) {
                return ;
            }
            Rss2Impl rss2 = new Rss2Impl();
            rss = rss2.parseXml(is);
            connection.disconnect();
            if( rss == null ) {
                logger.info("feed " + getFeedUrl() + " is not 0.90,0.91,0.92,2.0 try 1.0");
                Rss1Impl rss1 = new Rss1Impl();
                is = openInputStream();
                if( is == null ) {
                    return ;
                }
                rss = rss1.parseXml(is);
                connection.disconnect();
            }

            if( rss == null ) {
                logger.error("XML parse error, feed: " + getFeedUrl());
            } else {
                    RssChannel rssChannel = rss.getChannel();
                    if( rssChannel == null ) {
                        logger.error("Rss channel is null, feed: " + getFeedUrl());
                    } else {
                            // news category
                            if( getModuleName().equalsIgnoreCase("news") ) {
                                syndicateNews(rssChannel.getItems(), rssChannel);
                                return ;
                            }
                            // calendar category
                            if( getModuleName().equalsIgnoreCase("calendar") ) {
                                syndicateCalendar(rssChannel.getItems(), rssChannel);
                                return ;
                            }
                            // cms category
                            if( getModuleName().equalsIgnoreCase("cms") ) {
                                syndicateCms(rssChannel.getItems(), rssChannel);
                                return ;
                            }
                    }
            }
        }
        catch (IOException ioe) {
            logger.error(ioe.getMessage() + ", feed: " + getFeedUrl());
        }
        catch(SAXException ex){
            logger.error(ex.getMessage() + ", feed: " + getFeedUrl());
        }
    }

    /**
     *
     * @param items
     */
    public void syndicateCalendar(Vector items, RssChannel channel) {
        logger.error("Calendar category not support yet !, feed: " + getFeedUrl());
    }

    /**
     *
     * @param items
     */
    public void syndicateCms(Vector items, RssChannel channel) {
        logger.error("CMS category not support yet !, feed: " + getFeedUrl());
    }

    /**
     *
     * @param items
     */
    public void syndicateNews(Vector items, RssChannel channel) {

        CollectorFeedItem feedItem = null;
        NewsSettings settings = null;

        if( items == null ) {
            logger.error("RSS items is null, feed: " + getFeedUrl());
            return ;
        }

        logger.info("RSS item size: " + items.size() + " , feed: " +
                     getFeedUrl());


        try {
           feedItem = org.digijava.module.syndication.util.
                DbUtil.getCollectorFeedItem(
                getItemId());

            if( feedItem == null  ) {
                logger.error("collector item is null, feed: " + getFeedUrl());
                return;
            }
        }
        catch (SyndicationException ex1) {
            logger.error("can't get collector item, feed: " + getFeedUrl());
            return;
        }


        try {
            settings = org.digijava.module.news.util.
                DbUtil.getNewsSettings(feedItem.getSiteId(),
                                       feedItem.getInstanceId());
        }
        catch (NewsException ex3) {
            logger.error("can't get news setting, feed: " + getFeedUrl());
            return;
        }

        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            RssItem item = (RssItem)iter.next();

                    News news = null;
                    String link = item.getLink();
                    Date pubDate = null;
                    String title = item.getTitle();
                    String description = item.getDescription();

                    if (item.getPubDate() == null ||
                        item.getPubDate().trim().length() == 0) {
                        logger.debug("Date not set for item, " + item.getLink() +
                                     ", feed: " + getFeedUrl() + " use current date");
                        pubDate = new Date();
                    }
                    else {
                        try {
                            SimpleDateFormat formatter
                                = new SimpleDateFormat ("EEE, d MMM yyyy HH:mm:ss Z");

                            pubDate = formatter.parse(item.getPubDate());
                        }
                        catch (ParseException ex) {
                            logger.error("error parsing date: " + ex.getMessage() + ", " + item.getLink() +
                                         ", feed: " + getFeedUrl());
                            pubDate = new Date();
                        }
                    }

                    try {
                        String status = ItemStatus.PUBLISHED;
                        news = org.digijava.module.news.util.DbUtil.getNewsBySourceUrl(
                            feedItem.getSiteId(), feedItem.getInstanceId(),
                            link);

                        if( news != null ) {
                            continue;
                        }

                        if( settings.isModerated() )
                            status = ItemStatus.PENDING;

                        news = org.digijava.module.news.util.DbUtil.createNews(new Long(0),
                                                            status,
                                                            channel.getLanguage(),
                                                            title,
                                                            description);

                        news.setSiteId(feedItem.getSiteId());
                        news.setInstanceId(feedItem.getInstanceId());

                        news.setEnableHTML(false);
                        news.setEnableSmiles(false);
                        news.setCountry(null);

                        GregorianCalendar releaseDate = new GregorianCalendar();
                        releaseDate.setTime(pubDate);
                        releaseDate.set(java.util.Calendar.MINUTE, 0);
                        releaseDate.set(java.util.Calendar.HOUR_OF_DAY, 0);
                        releaseDate.set(java.util.Calendar.SECOND, 0);
                        news.setReleaseDate(releaseDate.getTime());
                        news.setSyndication(true);
                        news.setSourceName(channel.getTitle());
                        news.setSourceUrl(link);
                        news.setArchiveDate(null);
                        news.getFirstNewsItem().setCreationDate(new Date());


                        org.digijava.module.news.util.DbUtil.updateNews(news);

                        if( feedItem.getFirstRun() == null ) {
                            feedItem.setFirstRun(new Date());
                        }

                        feedItem.setLastRun(new Date());

                        org.digijava.module.syndication.util.DbUtil.updateFeedCollector(feedItem);

                   }
                    catch (NewsException ex2) {
                        logger.error("news database error: " + ex2.getMessage() + ", " + item.getLink() +
                                     ", feed: " + getFeedUrl());
                    }
                    catch (SyndicationException ex2) {
                        logger.error("collector feed database error: " + ex2.getMessage() + ", " + item.getLink() +
                                     ", feed: " + getFeedUrl());
                    }


//                    org.digijava.module.news.util.DbUtil.createNews(new Long(0),
//                }
        }
    }


    public Long getFeedId() {
        return (Long) currentContext.getJobDetail().getJobDataMap().get(
            "feedId");
    }

    public String getFeedUrl() {
        return (String) currentContext.getJobDetail().getJobDataMap().get(
            "feedUrl");
    }

    public Long getItemId() {
        return (Long) currentContext.getJobDetail().getJobDataMap().get(
            "itemId");
    }

    public String getSiteId() {
        return (String) currentContext.getJobDetail().getJobDataMap().get(
            "siteId");
    }

    public String getModuleName() {
        return (String) currentContext.getJobDetail().getJobDataMap().get(
            "moduleName");
    }

    public String getInstanceName() {
        return (String) currentContext.getJobDetail().getJobDataMap().get(
            "moduleInstance");
    }

    /**
     *
     * @return InputStream
     * @throws Exception
     */
    public InputStream openInputStream() {

        InputStream stream = null;
        try {
            URL u = new URL(getFeedUrl());
            this.connection = (HttpURLConnection) u.openConnection();
            this.connection.setRequestProperty("User-Agent", "DIGIAggregation");
            this.connection.connect();
            stream = this.connection.getInputStream();
        }
        catch (Exception ex1) {
            logger.error(ex1.getMessage() + ", feed: " + getFeedUrl());
        }

        return stream;
    }
}
