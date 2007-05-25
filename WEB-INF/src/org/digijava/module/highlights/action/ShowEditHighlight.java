/*
 *   ShowEditHighlight.java
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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.UserInfo;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.highlights.dbentity.Highlight;
import org.digijava.module.highlights.dbentity.HighlightLinks;
import org.digijava.module.highlights.form.HighlightItemForm;
import org.digijava.module.highlights.util.DbUtil;

/**
 * Action renders form, whith which existing Highlight can be updated
 */

public class ShowEditHighlight
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        HighlightItemForm formBean = (HighlightItemForm) form;
        Highlight highlight = null;

        if (request.getParameter("reset") == null) {

            if (formBean.getActiveHighlightId() != null &&
                formBean.getActiveHighlightId().longValue() != 0) {
                formBean.setActive(DbUtil.isHighlightActive(formBean.getActiveHighlightId()));
                highlight = DbUtil.getHighlight(formBean.getActiveHighlightId());
            }
            else {
                ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(
                    request);

                highlight = DbUtil.getHighlight(moduleInstance.getSite().
                                                getSiteId(),
                                                moduleInstance.getInstanceName());
                formBean.setActive(true);
            }
            if (highlight != null) {
                formBean.setTitle(highlight.getTitle());
                formBean.setDescription(highlight.getDescription());
                formBean.setTopic(highlight.getTopicText());

                formBean.setShortTopicLength(highlight.getShortTopicLength());

                switch (highlight.getLayout()) {
                    case 1: {
                        formBean.setLayout("1");

                        if ( (highlight.getImageHeight() != 0) &&
                            (highlight.getImageWidth() != 0)) {
                            formBean.setImageHeight1(Integer.toString(highlight.
                                getImageHeight()));
                            formBean.setImageWidth1(Integer.toString(highlight.
                                getImageWidth()));

                            formBean.setImageHeight(Integer.toString(highlight.
                                getImageHeight()));
                            formBean.setImageWidth(Integer.toString(highlight.
                                getImageWidth()));
                            formBean.setHaveImageSizes(true);
                        }
                        else {
                            formBean.setHaveImageSizes(false);
                        }

                        if (highlight.getImage() != null) {
                            formBean.setImage(highlight.getImage());
                            formBean.setHaveImage(true);
                        }
                        else {
                            formBean.setHaveImage(false);
                            formBean.setImage(null);
                        }

                        break;
                    }
                    case 2: {
                        formBean.setLayout("2");

                        if ( (highlight.getImageHeight() != 0) &&
                            (highlight.getImageWidth() != 0)) {
                            formBean.setImageHeight2(Integer.toString(highlight.
                                getImageHeight()));
                            formBean.setImageWidth2(Integer.toString(highlight.
                                getImageWidth()));

                            formBean.setImageHeight(Integer.toString(highlight.
                                getImageHeight()));
                            formBean.setImageWidth(Integer.toString(highlight.
                                getImageWidth()));

                            formBean.setHaveImageSizes(true);
                        }
                        else {
                            formBean.setHaveImageSizes(false);
                        }

                        if (highlight.getImage() != null) {
                            formBean.setImage(highlight.getImage());
                            formBean.setHaveImage(true);
                        }
                        else {
                            formBean.setHaveImage(false);
                            formBean.setImage(null);
                        }

                        break;
                    }
                    case 3: {
                        formBean.setLayout("3");
                        formBean.setImage(null);
                        formBean.setHaveImage(false);

                        break;
                    }
                    default: {
                        break;
                    }
                }

                if (highlight.getLinks() == null || highlight.getLinks().size() == 0) {
                  HighlightItemForm.LinkInfo link = new HighlightItemForm.LinkInfo();
                  formBean.setLinks(new ArrayList());
                  link.setUrl(request.getScheme() + "://");
                  formBean.getLinks().add(link);

                } else {
                  formBean.setLinks(new ArrayList());

                  Iterator iterator = highlight.getLinks().iterator();
                  while (iterator.hasNext()) {
                    HighlightItemForm.LinkInfo li = new HighlightItemForm.
                        LinkInfo();
                    HighlightLinks currentLink = (HighlightLinks) iterator.next();

                    li.setName(currentLink.getName());
                    li.setUrl(currentLink.getUrl());
                    li.setIndex(currentLink.getOffset());

                    formBean.getLinks().add(li);
                  }
                }

                UserInfo author = DgUtil.getUserInfo(highlight.getAuthorUserId());

                formBean.setAuthorFirstName(author.getFirstNames());
                formBean.setAuthorLastName(author.getLastName());

                if (highlight.getCreationDate() != null) {

                    GregorianCalendar date = new GregorianCalendar();
                    date.setTime(highlight.getCreationDate());

                    String creationDate = DgUtil.formatDate(date.getTime());
                    formBean.setCreationDate(creationDate);
                }

                if (highlight.getUpdaterUserId() != null) {

                    formBean.setUpdaterUserId(highlight.getUpdaterUserId());

                    if (highlight.getUpdationDate() != null) {

                        GregorianCalendar date = new GregorianCalendar();
                        date.setTime(highlight.getUpdationDate());

                        String updationDate = DgUtil.formatDate(date.getTime());
                        formBean.setUpdationDate(updationDate);
                    }

                    UserInfo updater = DgUtil.getUserInfo(highlight.
                        getUpdaterUserId());

                    formBean.setUpdaterFirstName(updater.getFirstNames());
                    formBean.setUpdaterLastName(updater.getLastName());
                }
                else {
                    formBean.setUpdaterUserId(null);
                }
            }

//            formBean.setFormReset(true);
        }

        return mapping.findForward("forward");
    }

}