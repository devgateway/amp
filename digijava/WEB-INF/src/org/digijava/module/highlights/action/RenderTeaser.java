/*
 *   RenderTeaser.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 10, 2003
 * 	 CVS-ID: $Id: RenderTeaser.java,v 1.1 2005-07-06 10:34:04 rahul Exp $
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

package org.digijava.module.highlights.action;

import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.highlights.dbentity.HighlightLinks;
import org.digijava.module.highlights.form.HighlightForm;
import org.digijava.module.highlights.form.HighlightItemForm;
import org.digijava.module.highlights.form.HighlightTeaserItem;
import org.digijava.module.highlights.util.DbUtil;

/**
 * Renders Highlight Teaser
 */
public class RenderTeaser
    extends TilesAction {

    public ActionForward execute(ComponentContext context,
                                 ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        HighlightForm formBean = (HighlightForm) form;
        HighlightTeaserItem teaserItem = null;

        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

        formBean.setArchive(DbUtil.isArchiveHighlightList(moduleInstance.
            getSite().
            getSiteId(),
            moduleInstance.getInstanceName()));

        teaserItem = DbUtil.getTeaserItem(moduleInstance.getSite().
                                         getSiteId(),
                                         moduleInstance.getInstanceName());

        if (teaserItem == null) {
            formBean.setActiveHighlight(null);
            return null;
        }

        HighlightForm.HighlightInfo hi = new HighlightForm.HighlightInfo();

        hi.setTitle(teaserItem.getTitle());
        //
        if (teaserItem.getShortTopicLength() < teaserItem.getTopicText().length()) {
            hi.setMore(true);
            hi.setTopic(teaserItem.getShortenedTopic());
        }
        else {
            hi.setTopic(teaserItem.getTopicText());
            hi.setMore(false);
        }

        hi.setDescription(teaserItem.getDescription());

        switch (teaserItem.getLayout()) {
            case 1: {
                formBean.setLayout1(true);
                formBean.setLayout2(false);
                formBean.setLayout3(false);

                    hi.setShowImage(teaserItem.isShowImage());

                    if ( (teaserItem.getImageHeight() == 0) ||
                        (teaserItem.getImageWidth() == 0)) {
                        hi.setHaveImageSizes(false);
                    }
                    else {
                        hi.setImageHeight(Integer.toString(teaserItem.
                            getImageHeight()));
                        hi.setImageWidth(Integer.toString(teaserItem.
                            getImageWidth()));
                        hi.setHaveImageSizes(true);
                    }

                break;
            }
            case 2: {
                formBean.setLayout2(true);
                formBean.setLayout1(false);
                formBean.setLayout3(false);

                    hi.setShowImage(teaserItem.isShowImage());

                    if ( (teaserItem.getImageHeight() == 0) ||
                        (teaserItem.getImageWidth() == 0)) {
                        hi.setHaveImageSizes(false);
                    }
                    else {
                        hi.setImageHeight(Integer.toString(teaserItem.
                            getImageHeight()));
                        hi.setImageWidth(Integer.toString(teaserItem.
                            getImageWidth()));
                        hi.setHaveImageSizes(true);
                    }

                break;
            }
            case 3: {

                formBean.setLayout1(false);
                formBean.setLayout2(false);
                formBean.setLayout3(true);

                hi.setShowImage(false);

                break;
            }
            default: {
                break;
            }
        }
        if (!hi.isShowImage()) {
            formBean.setLayout1(false);
            formBean.setLayout2(false);
            formBean.setLayout3(true);
        }

        /*        //--
                if (highlight.getCreationDate() != null) {
                    GregorianCalendar date = new GregorianCalendar();
                    date.setTime(highlight.getCreationDate());
                    String creationDate = DgUtil.formatDate(date.getTime());
                    hi.setCreationDate(creationDate);
                }
             UserInfo author = DgUtil.getUserInfo(highlight.getAuthorUserId());
                hi.setAuthorFirstName(author.getFirstNames());
                hi.setAuthorLastName(author.getLastName());
                //--------------------------
                if (highlight.getUpdaterUserId() != null) {
                    hi.setUpdaterUserId(highlight.getUpdaterUserId());
                    if (highlight.getUpdationDate() != null) {
                        GregorianCalendar date = new GregorianCalendar();
                        date.setTime(highlight.getUpdationDate());
             String updationDate = DgUtil.formatDate(date.getTime());
                        hi.setUpdationDate(updationDate);
                    }
             UserInfo updater = DgUtil.getUserInfo(highlight.getUpdaterUserId());
                    hi.setUpdaterFirstName(updater.getFirstNames());
                    hi.setUpdaterLastName(updater.getLastName());
                }
                else {
                    hi.setUpdaterUserId(null);
                }
         */
        hi.setLinks(new ArrayList());
        Iterator iterator = teaserItem.getLinksArray().iterator();
        while (iterator.hasNext()) {
            HighlightItemForm.LinkInfo li = new HighlightItemForm.LinkInfo();
            HighlightLinks currentLink = (HighlightLinks) iterator.next();

            li.setName(currentLink.getName());
            li.setUrl(currentLink.getUrl());
            li.setIndex(currentLink.getOffset());

            hi.getLinks().add(li);
        }
        hi.setId(teaserItem.getId());

        //-more-------
        if (formBean.isMore()) {
            hi.setTopic(teaserItem.getTopicText());
            hi.setMore(false);
            formBean.setMore(false);
        }

        formBean.setActiveHighlight(hi);
        formBean.setActiveHighlightId(hi.getId());

        RequestUtils.setTranslationAttribute(request,
                                             "highlightForm.activeHighlightId",
                                             String.valueOf(formBean.
            getActiveHighlightId()));

        return null;
    }
}