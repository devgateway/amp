package org.digijava.module.gis.action;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.gis.dbentity.GisMapPoint;
import org.digijava.module.gis.dbentity.GisMapSegment;
import org.digijava.module.gis.form.GisMapLoaderForm;
import java.awt.Color;
import org.digijava.module.gis.dbentity.GisMapShape;
import java.awt.Rectangle;
import org.digijava.module.gis.dbentity.GisMap;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.gis.util.GisUtil;
import org.digijava.module.gis.util.CoordinateRect;


/**
 * GIS Dashboard renderer action.
 * @author Irakli Kobiashvili
 *
 */
public class MapLoader extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        response.setContentType("image/png");
        GisMapLoaderForm gisForm = (GisMapLoaderForm) form;

        GisUtil gisUtil = new GisUtil();

        int canvasWidth = 1000;
        int canvasHeight = 500;
        float mapLeftX = -180f;
        float mapRightX = 180f;
        float mapTopY = 90f;
        float mapLowY = -90f;

        String canvasWidthStr = request.getParameter("canvasWidth");
        if (canvasWidthStr != null) {
            canvasWidth = Integer.parseInt(canvasWidthStr);
        }

        String canvasHeightStr = request.getParameter("canvasHeight");
        if (canvasHeightStr != null) {
            canvasHeight = Integer.parseInt(canvasHeightStr);
        }

        String mapLeftXStr = request.getParameter("mapLeftX");
        if (mapLeftXStr != null) {
            mapLeftX = Float.parseFloat(mapLeftXStr);
        }

        String mapRightXStr = request.getParameter("mapRightX");
        if (mapRightXStr != null) {
            mapRightX = Float.parseFloat(mapRightXStr);
        }

        String mapTopYStr = request.getParameter("mapTopY");
        if (mapTopYStr != null) {
            mapTopY = Float.parseFloat(mapTopYStr);
        }

        String mapLowYStr = request.getParameter("mapLowY");
        if (mapLowYStr != null) {
            mapLowY = Float.parseFloat(mapLowYStr);
        }

        String mapCode = request.getParameter("mapCode");

        BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
                                                BufferedImage.TYPE_INT_ARGB);

        /*
        List mapDataSegments = readMapDataFromCSV("D:\\maps\\testdata\\gg.dat");
*/


//        GisMap map = DbUtil.getMap(1);

    GisMap map = null;
    if (mapCode != null && mapCode.trim().length() > 0) {
        map = GisUtil.getMap(mapCode);
    } else {
        map = GisUtil.getMap("TZA");
    }

        List mapDataSegments = map.getSegments();

        String autoRect = request.getParameter("autoRect");
        if (autoRect != null && autoRect.trim().equalsIgnoreCase("true")) {
            CoordinateRect rect = gisUtil.getMapRect(map);
            mapLeftX = rect.getLeft();
            mapRightX = rect.getRight();
            mapTopY = rect.getTop();
            mapLowY = rect.getBottom();
        }



//        List mapDataSegments1 = readMapDataFromCSV("D:\\maps\\TZ_Regions_Poly\\tanzania.dat");

        Graphics2D g2d = graph.createGraphics();

        g2d.setBackground(new Color(0, 0, 100, 255));
        g2d.clearRect(0, 0, canvasWidth, canvasHeight);


        gisUtil.addDataToImage(g2d, mapDataSegments, -1, canvasWidth, canvasHeight, mapLeftX, mapRightX, mapTopY,
                       mapLowY, false);


/*
        addDataToImage(g2d, mapDataSegments1, -1, canvasWidth, canvasHeight, mapLeftX, mapRightX, mapTopY,
                       mapLowY, true);
 */


        g2d.dispose();

        RenderedImage ri = graph;

        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(ri, "png", sos);

        graph.flush();

        sos.close();

        return null;
    }







}
