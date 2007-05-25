/*
*   ShowImage.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created:
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

package org.digijava.module.forum.action;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.dbentity.ForumAsset;
import org.digijava.module.forum.util.DbUtil;

public class GetFile extends Action {
    public ActionForward execute(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
//            response.setContentType();
            String assetId = request.getParameter("assetId");
            if (assetId != null) {
                ForumAsset asset =
                    DbUtil.getAssetItem(new Long(assetId).longValue());
                if (asset != null) {
                  response.setContentType(asset.getContentType());
                  response.setHeader("Content-Disposition",
                                     "attachment; filename=" +
                                     asset.getSrcFileName());

                }
                if (asset != null && asset.getFileContent() != null){
                    try {
                        ServletOutputStream ostr = response.getOutputStream();
                        ostr.write(asset.getFileContent());
                        ostr.close();
                    }
                    catch (IOException ex) {
                    }
                }
            }
            return null;
        }
}