/*
 *   ShowImage.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 13, 2003
 * 	 CVS-ID: $Id: ShowImage.java,v 1.1 2005-07-06 10:34:04 rahul Exp $
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

import java.io.ByteArrayOutputStream;
import javax.media.jai.JAI;
import javax.media.jai.RenderedImageAdapter;
import javax.servlet.ServletOutputStream;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.module.highlights.dbentity.Highlight;
import org.digijava.module.highlights.form.HighlightForm;
import org.digijava.module.highlights.util.DbUtil;
import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.JPEGEncodeParam;
import com.sun.media.jai.codec.PNGEncodeParam;
import net.sf.swarmcache.ObjectCache;
import org.apache.log4j.Logger;

/**
 *<p>Action renders Highlight image</p>
 * also renders left upper corner of an image using Java Advanced Imaging API, when parameter "shrink" is specified
 */

public class ShowImage
    extends Action {

    private static Logger logger = Logger.getLogger(ShowImage.class);

    private ObjectCache imgCache;

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        HighlightForm formBean = (HighlightForm) form;
        Highlight highlight = null;

        if (formBean.getActiveHighlightId() != null) {

            if (request.getParameter("shrink") != null) {

                imgCache = DigiCacheManager.getInstance().getCache(
                    "org.digijava.module.highlights.shrinkedImgCache");
                byte[] outBuff = (byte[]) imgCache.get(formBean.
                    getActiveHighlightId());

                if (outBuff == null) {

                    byte[] image = DbUtil.getHighlightImage(formBean.
                        getActiveHighlightId());

                    ByteArraySeekableStream str = new ByteArraySeekableStream(
                        image);
                    ByteArrayOutputStream outStr = new ByteArrayOutputStream();

                    RenderedImage img = (RenderedImage) JAI.create("stream",
                        str);

                    RenderedImageAdapter ria = new RenderedImageAdapter(img);
                    BufferedImage bufImage = ria.getAsBufferedImage(new
                        Rectangle(0, 0,
                                  50, 50), img.getColorModel());

                    if (bufImage == null) {
                        bufImage = ria.getAsBufferedImage();
                    }

                    ImageEncoder encoder = null;
                    if (request.getParameter("png") != null) {
                        logger.debug("writing image in PNG format");
                        PNGEncodeParam pngEncodeParam = PNGEncodeParam.
                            getDefaultEncodeParam(img);
                        encoder = ImageCodec.createImageEncoder(
                            "PNG",
                            outStr, pngEncodeParam);
                    }
                    else {
                        logger.debug("writing image in JPEG format");

                        JPEGEncodeParam jpegEncodeParam = new JPEGEncodeParam();
                        jpegEncodeParam.setQuality(0.75F);
                        encoder = ImageCodec.createImageEncoder(
                            "JPEG",
                            outStr, jpegEncodeParam);
                    }

                    if (encoder != null) {
                        encoder.encode(bufImage);
                    }
                    outStr.flush();
                    outBuff = outStr.toByteArray();

                    imgCache.put(formBean.getActiveHighlightId(), outBuff);
                }

                if (outBuff != null) {
                    ServletOutputStream output = response.getOutputStream();
                    output.write(outBuff);
                    output.flush();
                }
                if (highlight.getContentType() != null ){
                  response.setContentType(highlight.getContentType());
                }
            }
            else {

                byte[] image = DbUtil.getHighlightImage(formBean.getActiveHighlightId());

                if (image != null) {
                    ServletOutputStream output = response.getOutputStream();
                    output.write(image);
                    output.flush();
                }

            }

        }

        return null;
    }

}