/*
 *   RenderTeaser.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: RenderTeaser.java,v 1.1 2005-07-06 10:34:12 rahul Exp $
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.news.dbentity.NewsSettings;
import org.digijava.module.news.exception.NewsException;
import org.digijava.module.news.form.NewsForm;
import org.digijava.module.news.form.NewsTeaserItem;
import org.digijava.module.news.util.DbUtil;

/**
 * Renders News Teaser
 */
public class RenderTeaser
    extends TilesAction {

  private static Logger logger = Logger.getLogger(RenderTeaser.class);

  public ActionForward execute(ComponentContext context,
                               ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
      ServletException {

    NewsForm newsForm = (NewsForm) form;

    ArrayList newsList = new ArrayList();
    List dbNewsList = null;

    NewsSettings setting = null;

    Long numOfItemsInTeaser = null;
    int numOfCharsInTitle = 0;

    User user = RequestUtils.getUser(request);

    try {

        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

        // set number of items in page
        numOfItemsInTeaser = moduleInstance.getNumberOfItemsInTeaser();

        // get Setting
        setting = DbUtil.getNewsSettings(request);

        // set number of characters in title
	Long numOfCharsInTitleL = setting.getNumberOfCharsInTitle();
        numOfCharsInTitle = (numOfCharsInTitleL != null) ? numOfCharsInTitleL.intValue() : 200;


        // Get Current news
        dbNewsList = DbUtil.getTeaserNewsList (moduleInstance.getSite().getSiteId(),
                                    moduleInstance.getInstanceName(),
                                    numOfItemsInTeaser);

      if (dbNewsList != null) {
        Iterator it = dbNewsList.iterator();
        while (it.hasNext()) {
          NewsTeaserItem item = (NewsTeaserItem) it.next();
          item.setNumOfCharsInTitle(numOfCharsInTitle);
        }
      }
        logger.debug("NUMBER OF RETRIEVED EVENTS:"+dbNewsList.size());
    }

    catch (NewsException ex) {
       logger.debug("Unable to get information from Database ",ex);
       throw new ServletException(ex.getMessage(), ex );
    }


    int index = 0;

    if (numOfItemsInTeaser != null) {
      newsForm.setNumOfItemsInTeaser(numOfItemsInTeaser.intValue());
    } else {
      newsForm.setNumOfItemsInTeaser(0);
    }

    newsForm.setNewsList(dbNewsList);

    return null;
  }
}
