/*
 *   Publish.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id$
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


package org.digijava.module.news.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletOutputStream;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.news.form.NewsTeaserItem;
import org.digijava.module.news.util.DbUtil;
import org.digijava.module.syndication.dbentity.PublicationFeed;
import org.digijava.kernel.syndication.digester.Rss;
import org.digijava.kernel.syndication.publicator.action.PublicatorAction;
import org.digijava.module.common.util.ModuleUtil;
import org.digijava.module.common.util.BBCodeParser;
import org.digijava.kernel.syndication.digester.RssItem;
import java.util.Vector;

/**
 * Action displayes list of all news
 */

public class Publish
    extends PublicatorAction {

    public boolean publication(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse response,
                               Rss rssData, PublicationFeed feed) throws java.
        lang.Exception {

        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);
        String countryIso = null;

        if (feed.getCountry() != null)
            countryIso = feed.getCountry().getIso();

        List list = DbUtil.getNewsListForPublishing(moduleInstance.getSite().
            getSiteId(),
            moduleInstance.getInstanceName(),
            countryIso,
            feed.getLanguage().getCode(),
            new Long(30));

        if (list != null && list.size() > 0) {

            // create rss items
            Vector feeds = new Vector ();
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                NewsTeaserItem item = (NewsTeaserItem) iter.
                    next();
                String description = null;

                String delimiter = DbUtil.
                    getShortVersionDelimiter(moduleInstance.
                                             getSite().
                                             getSiteId(),
                                             moduleInstance.getInstanceName());

                description = ModuleUtil.
                    extractShortDescription(item.
                                            getDescription(), delimiter);

                description = BBCodeParser.parse(
                    description,
                    item.isEnableSmiles(),
                    item.isEnableHTML(), request);

                item.setNumOfCharsInTitle(30);

                feeds.add(new RssItem(item.getTitle(),
                                      item.getSourceUrl(),
                                      description,
                                      item.getReleaseDate()));
            }

            rssData.setItems(feeds);
        }

        return true;
    }

}
