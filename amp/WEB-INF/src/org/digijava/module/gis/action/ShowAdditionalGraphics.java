package org.digijava.module.gis.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.gis.form.AdditionalGraphicsForm;
import org.digijava.module.gis.util.GisUtil;
import org.digijava.module.gis.util.MapColorScheme;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 9/30/11
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShowAdditionalGraphics extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        AdditionalGraphicsForm additionalGraphicsForm = (AdditionalGraphicsForm) form;
        String action = (additionalGraphicsForm.getActionType() != null && !additionalGraphicsForm.getActionType().isEmpty()) ?
                additionalGraphicsForm.getActionType() : "unspecified";


        if (action.equalsIgnoreCase("legendGradient")) {
            response.setContentType("image/png");

            int width = additionalGraphicsForm.getWidth();
            int height = additionalGraphicsForm.getHeight();
            response.getOutputStream().write(GisUtil.getDefaultGradienTegendBytes(request, width, height));
            response.getOutputStream().close();
        }

        return null;
    }
}
