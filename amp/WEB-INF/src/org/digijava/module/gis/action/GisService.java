package org.digijava.module.gis.action;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.gis.dbentity.GisMap;
import org.digijava.module.gis.form.GisMapLoaderForm;
import org.digijava.module.gis.util.CoordinateRect;
import org.digijava.module.gis.util.GisUtil;
import org.apache.ecs.xml.XMLDocument;
import java.util.Iterator;
import org.digijava.module.gis.dbentity.GisMapSegment;
import java.util.StringTokenizer;
import org.digijava.module.gis.util.SegmentData;
import java.util.ArrayList;


public class GisService extends Action {

    public static String DEF_MAP_CODE = "WORLD";
    public static int DEF_CANVAS_WIDTH = 1000;
    public static int DEF_CANVAS_HEIGHT = 500;
    public static float DEF_LEFT_POS = -180f;
    public static float DEF_RIGHT_POS = 180f;
    public static float DEF_TOP_POS = 90f;
    public static float DEF_BOTTOM_POS = 90f;

    public static String ACTION_PAINT_MAP = "paintMap";
    public static String ACTION_GET_IMAGE_MAP = "getImageMap";
    public static String ACTION_GET_SEGMENT_INFO = "getSegmentInfo";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        GisMapLoaderForm gisForm = (GisMapLoaderForm) form;
        GisUtil gisUtil = new GisUtil();
        ServletOutputStream sos = response.getOutputStream();

        String action = request.getParameter("action");

        int canvasWidth = DEF_CANVAS_WIDTH;
        int canvasHeight = DEF_CANVAS_HEIGHT;
        float mapLeft = DEF_LEFT_POS;
        float mapRight = DEF_RIGHT_POS;
        float mapTop = DEF_TOP_POS;
        float mapBottom = DEF_BOTTOM_POS;




        String canvasWidthStr = request.getParameter("canvasWidth");
        if (canvasWidthStr != null) {
            canvasWidth = Integer.parseInt(canvasWidthStr);
        }

        String canvasHeightStr = request.getParameter("canvasHeight");
        if (canvasHeightStr != null) {
            canvasHeight = Integer.parseInt(canvasHeightStr);
        }

        String mapLeftXStr = request.getParameter("left");
        if (mapLeftXStr != null) {
            mapLeft = Float.parseFloat(mapLeftXStr);
        }

        String mapRightXStr = request.getParameter("right");
        if (mapRightXStr != null) {
            mapRight = Float.parseFloat(mapRightXStr);
        }

        String mapTopYStr = request.getParameter("top");
        if (mapTopYStr != null) {
            mapTop = Float.parseFloat(mapTopYStr);
        }

        String mapLowYStr = request.getParameter("bottom");
        if (mapLowYStr != null) {
            mapBottom = Float.parseFloat(mapLowYStr);
        }

        String mapCode = request.getParameter("mapCode");


        String autoRect = request.getParameter("autoRect");

        GisMap map = null;
            if (mapCode != null && mapCode.trim().length() > 0) {
                map = GisUtil.getMap(mapCode);
            } else {
                map = GisUtil.getMap("TZA");
            }


        if (autoRect != null && autoRect.trim().equalsIgnoreCase("true")) {
            CoordinateRect rect = gisUtil.getMapRect(map);
            mapLeft = rect.getLeft();
            mapRight = rect.getRight();
            mapTop = rect.getTop();
            mapBottom = rect.getBottom();
        }


        if (action.equalsIgnoreCase(ACTION_PAINT_MAP)) {
            response.setContentType("image/png");

            BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
                                                    BufferedImage.TYPE_INT_ARGB);



            List mapDataSegments = map.getSegments();



            Graphics2D g2d = graph.createGraphics();

            g2d.setBackground(new Color(0, 0, 100, 255));
            g2d.clearRect(0, 0, canvasWidth, canvasHeight);

            XMLDocument imageMapDef = new XMLDocument();

            gisUtil.addDataToImage(g2d, mapDataSegments, -1, canvasWidth,
                                   canvasHeight, mapLeft, mapRight, mapTop,
                                   mapBottom, true, false);






            //Hilight segment
            String hilightSegmentCode = request.getParameter("hilight");

            if (hilightSegmentCode != null && hilightSegmentCode.trim().length() > 0) {
                Iterator segIt = mapDataSegments.iterator();
                long hsId = 0;
                while (segIt.hasNext()) {
                    GisMapSegment segment = (GisMapSegment) segIt.next();
                    if (segment.getSegmentCode().equals(hilightSegmentCode)) {
                        hsId = segment.getSegmentId();
                        break;
                    }
                }
                gisUtil.addDataToImage(g2d, mapDataSegments, (int)hsId, canvasWidth,
                                   canvasHeight, mapLeft, mapRight, mapTop,
                                   mapBottom, true, false);

            }

            g2d.dispose();

            RenderedImage ri = graph;


            ImageIO.write(ri, "png", sos);

            graph.flush();

           // request.getSession().setAttribute("LOADED_IMAGE_MAP", imageMapDef.toString());
        } else if (action.equalsIgnoreCase(ACTION_GET_IMAGE_MAP)) {
            response.setContentType("text/xml");


            List mapDataSegments = map.getSegments();


            String imageMapCode = gisUtil.getImageMap(mapDataSegments, 10, canvasWidth,
                                    2,
                                   canvasHeight, mapLeft, mapRight, mapTop,
                                   mapBottom).toString();


            sos.print(imageMapCode);
        } else if (action.equalsIgnoreCase(ACTION_GET_SEGMENT_INFO)) {
            response.setContentType("text/xml");
            List mapDataSegments = map.getSegments();
            String segmentInfoCode = gisUtil.getSegmentData(mapDataSegments).toString();
            sos.print(segmentInfoCode);
        }

        sos.close();
        return null;
    }


}
