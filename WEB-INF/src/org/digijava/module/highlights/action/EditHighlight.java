/*
 *   EditHighlight.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 13, 2003
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

package org.digijava.module.highlights.action;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.highlights.dbentity.Highlight;
import org.digijava.module.highlights.dbentity.HighlightLinks;
import org.digijava.module.highlights.form.HighlightItemForm;
import org.digijava.module.highlights.util.DbUtil;
import net.sf.swarmcache.ObjectCache;

/**
 * Action edits either active Highlight or Highlight identified by activeHighlightId identity, if the last is not null
 */

public class EditHighlight
    extends Action {

    private static Logger logger = Logger.getLogger(EditHighlight.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        HighlightItemForm formBean = (HighlightItemForm) form;

        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(
            request);
        String siteId = moduleInstance.getSite().
            getSiteId();
        String instanceName = moduleInstance.getInstanceName();


        User user = RequestUtils.getUser(request);

        if (user != null) {

            Highlight highlight = null;

            if ( (formBean.getActiveHighlightId() != null) &&
                (!formBean.getActiveHighlightId().equals(new Long(0)))) {
                highlight = DbUtil.getHighlight(formBean.getActiveHighlightId());
            }
            if (highlight == null) {

               highlight = DbUtil.getHighlight(siteId,instanceName);
            }

            if (highlight != null) {

                highlight.setTitle(formBean.getTitle());
                highlight.setDescription(formBean.getDescription());
                highlight.setTopic(formBean.getTopic());

                if (formBean.getLayout().equals("1")) {
                    highlight.setLayout(1);

                    if (formBean.getImageHeight1().trim().length() == 0 ||
                        formBean.getImageWidth1().trim().length() == 0) {

                        if ( (highlight.getImageHeight() != 0) &&
                            (highlight.getImageWidth() != 0)) {
                            formBean.setImageHeight1(Integer.toString(highlight.
                                getImageHeight()));
                            formBean.setImageWidth1(Integer.toString(highlight.
                                getImageWidth()));
                        }
                        else {
                            highlight.setImageHeight(0);
                            highlight.setImageWidth(0);
                        }
                    }
                    else {
                        highlight.setImageHeight(Integer.parseInt(formBean.
                            getImageHeight1()));
                        highlight.setImageWidth(Integer.parseInt(formBean.
                            getImageWidth1()));
                    }
                }
                //
                if (formBean.getLayout().equals("2")) {
                    highlight.setLayout(2);

                    if (formBean.getImageHeight2().trim().length() == 0 ||
                        formBean.getImageWidth2().trim().length() == 0) {

                        if ( (highlight.getImageHeight() != 0) &&
                            (highlight.getImageWidth() != 0)) {

                            formBean.setImageHeight2(Integer.toString(highlight.
                                getImageHeight()));
                            formBean.setImageWidth2(Integer.toString(highlight.
                                getImageWidth()));
                        }
                        else {
                            highlight.setImageHeight(0);
                            highlight.setImageWidth(0);
                        }

                    }
                    else {
                        highlight.setImageHeight(Integer.parseInt(formBean.
                            getImageHeight2()));
                        highlight.setImageWidth(Integer.parseInt(formBean.
                            getImageWidth2()));
                    }
                }
                //Image -------------
                FormFile formFile = formBean.getPhotoFile();
                if (formFile != null) {
                  if (formFile.getFileSize() != 0) {
                    byte[] image = formFile.getFileData();
                    highlight.setImage(image);
                    highlight.setContentType(formFile.getContentType());
                  }
                  else {
                    if (formBean.getImage() != null) {
                      highlight.setImage(formBean.getImage());
                      if (formBean.getContentType() != null) {
                        highlight.setContentType(formBean.getContentType());
                      }
                    }
                    else {
                      highlight.setImage(null);
                      highlight.setContentType(null);
                    }
                  }
                }
                //
                if (formBean.getLayout().equals("3")) {
                    highlight.setLayout(3);
                    highlight.setImage(null);
                    highlight.setImageHeight(0);
                    highlight.setImageWidth(0);

                    //
                    formBean.setImage(null);
                    formBean.setImageSize(0);
                }

                highlight.setShortTopicLength(formBean.getShortTopicLength());

                highlight.setUpdaterUserId(user.getId());

                GregorianCalendar date = new GregorianCalendar();
                highlight.setUpdationDate(date.getTime());

                //Assign links to highlight

                Set links = new HashSet();
                highlight.setLinks(links);
                Iterator iterator = formBean.getLinks().iterator();
                while (iterator.hasNext()) {
                    HighlightItemForm.LinkInfo linkItem = (HighlightItemForm.
                        LinkInfo)
                        iterator.next();

                    HighlightLinks link = new HighlightLinks();

                    if (linkItem.getName().trim().length() != 0 &&
                        linkItem.getUrl().trim().length() != 0 &&
                        ! linkItem.getUrl().trim().equals(request.getScheme()+"://")) {
                        link.setName(linkItem.getName());
                        link.setUrl(linkItem.getUrl());
                        link.setOffset(linkItem.getIndex());
                        link.setHighlight(highlight);

                        links.add(link);
                    }
                }
                if (links.size() != 0) {
                  highlight.setLinks(links);
                }

                //
                //clear image cache
                ObjectCache imgCache = DigiCacheManager.getInstance().getCache(
                    "org.digijava.module.highlights.shrinkedImgCache");
                imgCache.put(highlight.getId(), null);

                //  --------------------
                if (!formBean.isActive()) {
                  highlight.setActive(true);
                  DbUtil.ArchiveHighlights(siteId,instanceName);
                }

                DbUtil.updateHighlight(highlight);
                formBean.setUpdaterUserId(user.getId());
            }
        }
        else {
            ActionErrors errors = new ActionErrors();
            errors.add(null,
                       new ActionError("error.highlights.userEmpty"));
            saveErrors(request, errors);
        }

        return mapping.findForward("forward");
    }

}