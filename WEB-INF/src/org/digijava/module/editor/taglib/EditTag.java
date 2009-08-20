/*
 *   EditTag.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Dec 17, 2003
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

package org.digijava.module.editor.taglib;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.request.Site;

public class EditTag extends BodyTagSupport {

    private static Logger logger = Logger.getLogger(EditTag.class);

    private String key;
    private String editorBody;
    private boolean showOnlyTitle = false;

    /**
     * Process the start of this tag.
     * @return int
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();

        if (getKey() != null && getKey().trim().length() > 0 &&
            getKey().matches("[-a-zA-Z_0-9_{:_}]++")) {
            try {
                Site site = RequestUtils.getSite(request);
                if( !showOnlyTitle ) {
                    editorBody = DbUtil.getEditorBody(site.getSiteId(),
                        getKey(),
                        RequestUtils.
                        getNavigationLanguage(request).
                        getCode());

                    if (editorBody == null) {
                        editorBody = DbUtil.getEditorBody(site.getSiteId(),
                            getKey(),
                            SiteUtils.getDefaultLanguages(site).getCode());
                    }
                } else {
                    editorBody = DbUtil.getEditorTitle(site.getSiteId(),
                        getKey(),
                        RequestUtils.
                        getNavigationLanguage(request).
                        getCode());

                    if (editorBody == null) {
                        editorBody = DbUtil.getEditorTitle(site.getSiteId(),
                            getKey(),
                            SiteUtils.getDefaultLanguages(site).getCode());
                    }
                }
                setEditorBody(editorBody);
            }
            catch (EditorException ex) {
                logger.warn("Unable to get editor object from database", ex);
            }
            return EVAL_BODY_BUFFERED;
        }
        else {
            try {
                pageContext.getOut().write("Unrecognized character found in key.Edit tag key can consist of characters: a-z;A-Z;0-9;{,}:,-,_.");
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return SKIP_BODY;
        }
    }

    public int doEndTag() throws JspException {

        HttpServletRequest request = (HttpServletRequest) pageContext.
            getRequest();

        User user = RequestUtils.getUser(request);
        BodyContent body = getBodyContent();
        String editTag = "";

        String refUrl = RequestUtils.getSourceURL(request);
        boolean admin = false;
        /*
                 try {
            refUrl = URLEncoder.encode(refUrl, "UTF-8");
            String key = URLDecoder.decode(getKey(), "UTF-8");
                 }
                 catch (UnsupportedEncodingException ex) {
             throw new JspException("Error decoding editor key or referer url", ex);
                 }*/

        if (RequestUtils.getRealModuleInstance(request) == null) {
            admin = DgUtil.isSiteAdministrator(request);
        }
        else {
            admin = DgUtil.isModuleInstanceAdministrator(request);
        }

        if (admin) {
            if (getEditorBody() != null) {
                editTag = getEditorBody() + "<a href=\"" +
                    DgUtil.getSiteUrl(RequestUtils.getSite(request), request) +
                    "/editor/showEditText.do?id=" + getKey() +"&lang="+RequestUtils.
                        getNavigationLanguage(request).
                        getCode()+
                    "&referrer=" + refUrl +
                    "\">E</a>";
            }
            else {
              editTag = "<a href=\"" +
                    DgUtil.getSiteUrl(RequestUtils.getSite(request), request) +
                    "/editor/showEditText.do?id=" + getKey() +"&lang="+RequestUtils.
                        getNavigationLanguage(request).
                        getCode()+"&referrer=" + refUrl +
                    "\">E</a>";
            
            }
        }
        else {
            if (getEditorBody() != null) {
                editTag = getEditorBody();
            }
            else {
                if(body!=null)
            	editTag = body.getString();
                
            }
        }

        try {
            JspWriter out = pageContext.getOut();
            out.write(editTag);
        }
        catch (IOException e) {
            logger.warn(" EditTag failed to render ", e);
        }
        return EVAL_PAGE;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEditorBody() {
        return editorBody;
    }

    public void setEditorBody(String editorBody) {
        this.editorBody = editorBody;
    }

    public boolean isShowOnlyTitle() {
        return showOnlyTitle;
    }

    public void setShowOnlyTitle(boolean showOnlyTitle) {
        this.showOnlyTitle = showOnlyTitle;
    }

}