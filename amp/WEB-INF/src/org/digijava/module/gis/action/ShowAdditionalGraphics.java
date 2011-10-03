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
            MapColorScheme colorScheme = GisUtil.getActiveColorScheme(request);
            BufferedImage graph = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = graph.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradient = new GradientPaint(0,0,colorScheme.getGradientMinColor().getAsAWTColor(),
                                                                   width - 1,height - 1,colorScheme.getGradientMaxColor().getAsAWTColor(),true);



            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, width, height);

            g2d.setPaint(Color.black);
            g2d.drawRect(0, 0, width - 1, height - 1);

            java.awt.Font f = new java.awt.Font("Helvetica", java.awt.Font.BOLD, 12);
            g2d.setFont(f);

            String minText = "MIN";
            GlyphVector minVector = g2d.getFont().createGlyphVector(g2d.
                    getFontRenderContext(), minText);
            int minCaptionWidth = (int)minVector.getVisualBounds().getWidth();
            int minCaptionHeight = (int)minVector.getVisualBounds().getHeight();

            String maxText = "MAX";
            GlyphVector maxVector = g2d.getFont().createGlyphVector(g2d.
                    getFontRenderContext(), maxText);
            int maxCaptionWidth = (int)maxVector.getVisualBounds().getWidth();
            int maxCaptionHeight = (int)maxVector.getVisualBounds().getHeight();


            g2d.drawString(minText, 4 , height - (height - minCaptionHeight)/2);
            g2d.drawString(maxText, width - 4 - maxCaptionWidth, height - (height - maxCaptionHeight)/2);

            //g2d.drawString(minText, 0, 20);


            g2d.dispose();
            ImageIO.write(graph, "png", response.getOutputStream());
            graph.flush();
            response.getOutputStream().close();

        }

        return null;
    }
}
