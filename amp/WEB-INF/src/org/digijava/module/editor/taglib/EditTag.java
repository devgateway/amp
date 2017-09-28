/*
 *   EditTag.java
 *   @Author Maka Kharalashvili maka@digijava.org
 *   Created: Dec 17, 2003
 *   CVS-ID: $Id$
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
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;

public class EditTag
    extends javax.servlet.jsp.tagext.BodyTagSupport {

    private static Logger logger = Logger.getLogger(EditTag.class);

    private String key;
    private String editorBody;
    private String displayText = "E";
    private Integer maxLength;
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
            getKey().matches("[-a-zA-Z_0-9_{:_} ]++")) {
            try {
                Site site = RequestUtils.getSite(request);
                if( !showOnlyTitle ) {
                    editorBody = DbUtil.getEditorBody(site,
                        getKey(),
                        RequestUtils.
                        getNavigationLanguage(request).
                        getCode());

                    if (editorBody == null) {
                        editorBody = DbUtil.getEditorBody(site,
                            getKey(),
                            SiteUtils.getDefaultLanguages(site).getCode());
                    }
                } else {
                    editorBody = DbUtil.getEditorTitle(site,
                        getKey(),
                        RequestUtils.
                        getNavigationLanguage(request).
                        getCode());

                    if (editorBody == null) {
                        editorBody = DbUtil.getEditorTitle(site,
                            getKey(),
                            SiteUtils.getDefaultLanguages(site).getCode());
                    }
                }
                if(editorBody==null) return SKIP_BODY;
                if (maxLength==null || maxLength==0 || (editorBody!=null && editorBody.length()<maxLength)) {
                    setEditorBody(editorBody);
                } else {
                    setEditorBody(formatLongText(editorBody, request));
                }
                
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
                    "/editor/showEditText.do?id=" + getKey() +"&lang="+RequestUtils.
                        getNavigationLanguage(request).
                        getCode()+
                    "&referrer=" + refUrl +
                    "\">"+displayText+"</a>";
            }
            else {
              editTag = "<a href=\"" +
                    "/editor/showEditText.do?id=" + getKey() +"&lang="+RequestUtils.
                        getNavigationLanguage(request).
                        getCode()+"&referrer=" + refUrl +
                    "\">"+displayText+"</a>";
            
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

    private String formatLongText(String text, HttpServletRequest request) {
        String readMore = "Read More";
        String back = "Back";
        readMore = TranslatorWorker.translateText(readMore);
        back = TranslatorWorker.translateText(back);
        //Avoids cutting HTML in half
        String cutText = text;
        cutText = cutText.substring(0, maxLength);
        int openMark = cutText.lastIndexOf('<');
        if (openMark != -1)
        {
            int closeMark = cutText.lastIndexOf('>');
            if (openMark > closeMark)
            {
                cutText = cutText.substring(0, openMark);
            }
        }
        
        String ret = "<div id='fullTextDiv' style='display: none'> " + text + " </div>";
        ret += "<div id='showPartTextDiv' style='display: none'><a href='javascript:' onClick='showPartText()'> << " + back + "</a></div>";
        ret += "<div id='partTextDiv'>" + cutText + "</div>";
        ret += "<div id='showFullTextDiv'><a href='javascript:' onClick='showFullText()'>" + readMore + " >> </a></div>";
        ret += "<script language='javascript' type='text/javascript'>";
    
        ret += "function showFullText() {";
        ret += "document.getElementById('fullTextDiv').style.display = 'block';";
        ret += "document.getElementById('showPartTextDiv').style.display = 'block';";
        ret += "document.getElementById('partTextDiv').style.display = 'none';";
        ret += "document.getElementById('showFullTextDiv').style.display = 'none';";    
        ret += "}";

        ret += "function showPartText() {";

        ret += "document.getElementById('fullTextDiv').style.display = 'none';";
        ret += "document.getElementById('showPartTextDiv').style.display = 'none';";
        ret += "document.getElementById('partTextDiv').style.display = 'block';";
        ret += "document.getElementById('showFullTextDiv').style.display = 'block';";
        ret += "}";

        ret += "</script>";
                        
        return ret;
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
    
    public String getDisplayText() {
        return displayText;
    }
    
    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    /**
     * @return the maxLength
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * @param maxLength the maxLength to set
     */
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
}
