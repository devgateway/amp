package org.digijava.module.gis.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.digijava.module.gis.util.GisUtil;
import org.digijava.module.gis.dbentity.GisMap;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import org.apache.ecs.xml.XMLDocument;
import java.util.List;
import org.digijava.module.gis.util.SegmentData;
import java.util.Iterator;
import java.awt.image.RenderedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.StringTokenizer;
import java.awt.image.BufferedImage;
import org.digijava.module.gis.dbentity.GisMapSegment;
import javax.servlet.ServletOutputStream;
import org.digijava.module.gis.util.CoordinateRect;
import org.digijava.module.gis.util.HilightData;
import org.digijava.module.gis.util.ColorRGB;

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

        GisUtil gisUtil = new GisUtil();
        ServletOutputStream sos = response.getOutputStream();

        String action = request.getParameter("action");


        String mapCode = request.getParameter("mapCode");
        GisMap map = null;

        if (mapCode != null && mapCode.trim().length() > 0) {
            map = GisUtil.getMap(mapCode);
        }

        GisMap districtMap = null;
        if (mapCode != null && mapCode.trim().length() > 0) {
            String districtMapCode = mapCode + "_DISTRICT";
            districtMap = GisUtil.getMap(districtMapCode);
        }


        //Process segment data
        String segmentDataStr = request.getParameter("segmentData");
        List segDataList = null;

        String parentCode = null;

        if (segmentDataStr != null) {
            segDataList = new ArrayList();
            StringTokenizer segmentTokenizer = new StringTokenizer(
                    segmentDataStr, "|");
            while (segmentTokenizer.hasMoreTokens()) {
                String segmentToken = segmentTokenizer.nextToken();
                StringTokenizer segmentDataTokenizer = new StringTokenizer(
                        segmentToken, "/");
                SegmentData segmentData = new SegmentData();
                segmentData.setParentCode(segmentDataTokenizer.nextToken());

                String segCode = segmentDataTokenizer.nextToken();
                if (segCode.endsWith("DC")) {
                    segCode = segCode.substring(0, segCode.length()-3);
                }
                segmentData.setSegmentCode(segCode);
                segmentData.setSegmentValue(segmentDataTokenizer.nextToken());
                segDataList.add(segmentData);

                parentCode = segmentData.getParentCode();
            }
        }

        List hilightData = prepareHilightSegments(segDataList, districtMap);
        List detailedMapSegments = getSegmentsForParent(parentCode, districtMap);

        int canvasWidth = 500;
        int canvasHeight = 500;

        //CoordinateRect rect = gisUtil.getMapRect(map);
        CoordinateRect rect = gisUtil.getMapRect(detailedMapSegments);

        if (action.equalsIgnoreCase(GisService.ACTION_PAINT_MAP)) {
                    response.setContentType("image/png");


                    BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
                                                            BufferedImage.TYPE_INT_ARGB);


                    Graphics2D g2d = graph.createGraphics();

                    g2d.setBackground(new Color(0, 0, 100, 255));

                    g2d.clearRect(0, 0, canvasWidth, canvasHeight);

                    gisUtil.addDataToImage(g2d,
                                            map.getSegments(),
                                           -1,
                                           canvasWidth, canvasHeight,
                                           rect.getLeft(), rect.getRight(),
                                           rect.getTop(), rect.getBottom(), true, false);

                    gisUtil.addDataToImage(g2d,
                                            detailedMapSegments,
                                           hilightData,
                                           canvasWidth, canvasHeight,
                                           rect.getLeft(), rect.getRight(),
                                           rect.getTop(), rect.getBottom(), true);




                    g2d.dispose();

                    RenderedImage ri = graph;


                    ImageIO.write(ri, "png", sos);

                    graph.flush();




                } else if (action.equalsIgnoreCase(GisService.ACTION_GET_IMAGE_MAP)) {
                    response.setContentType("text/xml");


                    List mapDataSegments = map.getSegments();

        /*
                    String imageMapCode = gisUtil.getImageMap(mapDataSegments, 10, canvasWidth,
                                           canvasHeight, mapLeft, mapRight, mapTop,
                                           mapBottom).toString();



                    sos.print(imageMapCode);
         */
                } else if (action.equalsIgnoreCase(GisService.ACTION_GET_SEGMENT_INFO)) {
                    response.setContentType("text/xml");
                    List mapDataSegments = map.getSegments();
                    String segmentInfoCode = gisUtil.getSegmentData(mapDataSegments).toString();
                    sos.print(segmentInfoCode);
                }

                sos.close();
                return null;

    }

    private List prepareHilightSegments (List segmentData, GisMap map) {
        List retVal = new ArrayList();
        if (map != null && map.getSegments() != null) {
            Iterator it = map.getSegments().iterator();

            while (it.hasNext()) {
                GisMapSegment segment = (GisMapSegment) it.next();
                for (int idx = (int) 0; idx < segmentData.size(); idx++) {
                    SegmentData sd = (SegmentData) segmentData.get(idx);
                    if (sd.getSegmentCode().equalsIgnoreCase(segment.getSegmentCode())) {
                        HilightData hData = new HilightData();
                        hData.setSegmentId((int) segment.getSegmentId());
                        float redColor = Float.parseFloat(sd.getSegmentValue()) * 2.55f;
                        hData.setColor(new ColorRGB((int) redColor, (int) (255f - redColor), 0));
                        retVal.add(hData);
                    }
                }
            }
        }
        return retVal;
    }

    private List getSegmentsForParent (String parentCode, GisMap map) {
        List retVal = new ArrayList();
        if (map != null && map.getSegments() != null) {
        Iterator it = map.getSegments().iterator();

        while (it.hasNext()) {
            GisMapSegment segment = (GisMapSegment) it.next();
            if (segment.getParentSegmentCode().equalsIgnoreCase(parentCode)) {
                retVal.add(segment);
            }
        }
        }
        return retVal;
       
    }

}
