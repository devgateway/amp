/*
 *   PublicatorAction.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: PublicatorAction.java,v 1.1 2005-07-06 10:34:18 rahul Exp $
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


package org.digijava.kernel.syndication.publicator.action;

import java.util.Date;
import javax.servlet.ServletOutputStream;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.syndication.digester.Rss;
import org.digijava.kernel.syndication.digester.RssChannel;
import org.digijava.kernel.syndication.publicator.Rss2Xml;
import org.digijava.module.syndication.dbentity.PublicationFeed;

/**
 * Action displayes list of all news
 */

public abstract class PublicatorAction
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {


        String id = request.getParameter("id");
        Long itemId = null;
        String countryIso = null;
        if( id != null ) {
            itemId = new Long(id);
        }

        if( itemId != null ) {
            PublicationFeed feed = org.digijava.module.syndication.util.DbUtil.getPublishedFeed(itemId);
            if( feed.isStatus() ) {

                // create rss global fields
                Rss2Xml rssData = new Rss2Xml();

                RssChannel channel = new RssChannel();
                channel.setDescription(feed.getFeedDescription());
                channel.setTitle(feed.getFeedTitle());
                channel.setLanguage(feed.getLanguage().getCode());
                channel.setDate(new Date());
                channel.setLink(feed.getFeedUrl());
                rssData.setChannel(channel);

                if (publication(mapping, form, request, response, rssData, feed)) {
                    StringBuffer buffer = rssData.writeXml();
                    if( buffer != null ) {
                        ServletOutputStream output = response.getOutputStream();
                        response.setContentType("text/xml");
                        output.write(buffer.toString().getBytes("UTF-8"));
                        output.flush();
                    }
                }
            }
        }
        return null;
    }

    public abstract boolean publication(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response, Rss rssData, PublicationFeed feed ) throws java.lang.Exception;
}
