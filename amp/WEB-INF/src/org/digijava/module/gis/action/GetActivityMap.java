package org.digijava.module.gis.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.gis.util.*;
import org.digijava.module.gis.dbentity.GisMap;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import org.apache.ecs.xml.XMLDocument;
import java.util.List;
import java.util.Iterator;
import java.awt.image.RenderedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.StringTokenizer;
import java.awt.image.BufferedImage;
import org.digijava.module.gis.dbentity.GisMapSegment;
import javax.servlet.ServletOutputStream;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class GetActivityMap extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

    ServletOutputStream sos = null;
    response.setContentType("image/png");

        try {

            MapColorScheme colorScheme = GisUtil.getActiveColorScheme(request);
            GisUtil gisUtil = new GisUtil();
            sos = response.getOutputStream();

            String action = request.getParameter("action");

            String mapCode = request.getParameter("mapCode");
            GisMap map = null;

            String mapLevel = request.getParameter("mapLevel");
            if (mapLevel == null) {
                mapLevel = "2";
            }

            if (mapCode != null && mapCode.trim().length() > 0) {
                map = GisUtil.getMap(mapCode, Integer.parseInt(mapLevel));
            }

            int canvasWidth = 700;
            int canvasHeight = 700;

            if (request.getParameter("width") != null) {
                canvasWidth = Integer.parseInt(request.getParameter("width"));
            }

            if (request.getParameter("height") != null) {
                canvasHeight = Integer.parseInt(request.getParameter("height"));
            }

            Long[] selRegIds = null;
            if (request.getParameter("selRegIDs") != null){
                String selRegIDsStr = request.getParameter("selRegIDs");
                if (selRegIDsStr.endsWith("|")) {
//                    selRegIDsStr = selRegIDsStr.substring(0, selRegIDsStr.length() - 1);
                    StringTokenizer selRegSplitter = new  StringTokenizer(selRegIDsStr, "|", false);
                    selRegIds = new Long[selRegSplitter.countTokens()];

                    String regIdStr = null;
                    for (int idx = 0; selRegSplitter.hasMoreTokens(); idx ++) {
                       regIdStr = selRegSplitter.nextToken();
                       selRegIds[idx] = Long.valueOf(regIdStr);
                    }
                }
            }


            List hilightData = getSelectedRegionHilight(selRegIds, map);

            CoordinateRect rect = gisUtil.getMapRect(map);

            BufferedImage graph = new BufferedImage(canvasWidth,
                            canvasHeight,
                            BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = graph.createGraphics();

            if (map != null && rect != null) {
                if (action.equalsIgnoreCase(GisUtil.ACTION_PAINT_MAP)) {
//                    g2d.setBackground(new Color(0, 0, 100, 255));

//                    g2d.clearRect(0, 0, canvasWidth, canvasHeight);

                    boolean fill = true;
                    if (request.getParameter("noFill") != null) {
                        fill = false;
                    }

                    colorScheme.setBackgroundColor(new ColorRGB(221, 221, 221));
                    colorScheme.setTerrainColor(new ColorRGB(120, 120, 120));
                    colorScheme.setBorderColor(new ColorRGB(139, 139, 139, 50));
                    colorScheme.setRegionBorderColor(new ColorRGB(50, 50, 50));

                    gisUtil.addDataToImage(g2d,
                                           map.getSegments(),
                                            hilightData,
                                           null,
                                           canvasWidth, canvasHeight,
                                           rect.getLeft(), rect.getRight(),
                                           rect.getTop(), rect.getBottom(),
                                           fill, false, colorScheme);

                    if (request.getParameter("noCapt") == null) {

                        gisUtil.addCaptionsToImage(g2d,
                                map.getSegments(),
                                canvasWidth, canvasHeight,
                                rect.getLeft(), rect.getRight(),
                                rect.getTop(), rect.getBottom(), new ColorRGB (255, 255, 255), new ColorRGB (0, 0, 0, 100));
                    }
                }
            } else {
                gisUtil.getNoDataImage(g2d, canvasWidth, canvasHeight, "No map data");
            }

            g2d.dispose();
            RenderedImage ri = graph;
            ImageIO.write(ri, "png", sos);
            graph.flush();

        } catch (Exception e) {
                e.printStackTrace();
        } finally {
            sos.flush();
            sos.close();
        }
        return null;

    }

    private List getSelectedRegionHilight (Long[] locationCodes, GisMap map) {
        List retVal = new ArrayList();
        if (map != null && map.getSegments() != null) {
            for (Long locId: locationCodes) {
                AmpLocation loc = LocationUtil.getAmpLocationByCVLocation(locId);
                if (loc != null) {
                    for (GisMapSegment segment :  map.getSegments()) {
                        if (segment.getSegmentCode().equals(loc.getLocation().getName())) {
                            HilightData hDataItem = new HilightData ((int) segment.getSegmentId(), new ColorRGB(163, 184, 188));
                            retVal.add(hDataItem);
                            break;
                        }
                    }
                }
            }
        }
        return retVal;
    }
}
