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
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpActivity;
import java.util.Map;
import java.util.HashMap;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.apache.ecs.xml.XML;

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
public class GetFoundingDetails extends Action {
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

        int canvasWidth = 700;
        int canvasHeight = 700;

        CoordinateRect rect = gisUtil.getMapRect(map);

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
                                   rect.getTop(), rect.getBottom(), true);

            gisUtil.addCaptionsToImage(g2d,
                                       map.getSegments(),
                                       canvasWidth, canvasHeight,
                                       rect.getLeft(), rect.getRight(),
                                       rect.getTop(), rect.getBottom());


            g2d.dispose();

            RenderedImage ri = graph;

            ImageIO.write(ri, "png", sos);

            graph.flush();

        } else if (action.equalsIgnoreCase(GisService.ACTION_GET_IMAGE_MAP)) {
            response.setContentType("text/xml");


           List mapDataSegments = map.getSegments();


           String imageMapCode = gisUtil.getImageMap(mapDataSegments, 20, canvasWidth,
                                  canvasHeight, rect.getLeft(), rect.getLeft(), rect.getTop(),
                                  rect.getBottom()).toString();


           sos.print(imageMapCode);

        } else if (action.equalsIgnoreCase("getDataForSector")) {

            response.setContentType("image/png");

            String secIdStr = request.getParameter("sectorId");

            Long secId = new Long(secIdStr);

            List secFundings = DbUtil.getSectorFoundings(secId);

            Iterator it = secFundings.iterator();

            Map locationIdObjectMap = new HashMap();
            Map locationFoundMap = new HashMap();

            Double totalFund = 0d;
            while (it.hasNext()) {
                Object[] secFounding = (Object[]) it.next();
                AmpActivity ampActivity = (AmpActivity) secFounding[0];
                Double activityFound = DbUtil.getActivityFoundings(ampActivity.
                        getAmpActivityId());

                Double addAmmount = 0d;
                if (activityFound != null) {
                    float addPercent = (Float) secFounding[1];
                    addAmmount = activityFound * addPercent /
                                 100;
                    totalFund += addAmmount;
                }

                Iterator locIt = ampActivity.getLocations().iterator();
                while (locIt.hasNext()) {

                    AmpActivityLocation loc = (AmpActivityLocation) locIt.next();

                    locationIdObjectMap.put(loc.getLocation().getAmpLocationId(),
                                            loc.getLocation());

                    if (locationFoundMap.containsKey(loc.getLocation().
                            getAmpLocationId())) {
                        Double existingVal = (Double) locationFoundMap.get(loc.
                                getLocation().getAmpLocationId());
                        locationFoundMap.put(loc.getLocation().getAmpLocationId(),
                                             existingVal +
                                             addAmmount *
                                             loc.getLocationPercentage() /
                                             100);
                    } else {
                        locationFoundMap.put(loc.getLocation().getAmpLocationId(),
                                             addAmmount *
                                             loc.getLocationPercentage() /
                                             100);
                    }
                }

            }

            List segmentDataList = new ArrayList();
            Iterator locFoundingMapIt = locationFoundMap.keySet().iterator();
            while (locFoundingMapIt.hasNext()) {
                Long key = (Long) locFoundingMapIt.next();
                AmpLocation loc = (AmpLocation) locationIdObjectMap.get(key);
                Double ammount = (Double) locationFoundMap.get(key);
                Object[] dataStore = new Object[2];
                dataStore[0] = loc;
                dataStore[1] = ammount;

                //                        locDataList.add(dataStore);


                if (loc.getRegion() != null) {
                    SegmentData segmentData = new SegmentData();
                    segmentData.setSegmentCode(loc.getRegion());
                    Double PercentFromTotal = ammount / totalFund * 100;
                    segmentData.setSegmentValue(PercentFromTotal.toString());
                    segmentDataList.add(segmentData);
                }
            }

            List hilightData = prepareHilightSegments(segmentDataList, map);

            BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
                    BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = graph.createGraphics();

            g2d.setBackground(new Color(0, 0, 100, 255));

            g2d.clearRect(0, 0, canvasWidth, canvasHeight);

            gisUtil.addDataToImage(g2d,
                                   map.getSegments(),
                                   hilightData,
                                   canvasWidth, canvasHeight,
                                   rect.getLeft(), rect.getRight(),
                                   rect.getTop(), rect.getBottom(), true);

            gisUtil.addCaptionsToImage(g2d,
                                       map.getSegments(),
                                       canvasWidth, canvasHeight,
                                       rect.getLeft(), rect.getRight(),
                                       rect.getTop(), rect.getBottom());

            g2d.dispose();

            RenderedImage ri = graph;

            ImageIO.write(ri, "png", sos);

            graph.flush();

        } else if (action.equalsIgnoreCase("getDataForIndicator")) {

            response.setContentType("image/png");

            String secIdStr = request.getParameter("sectorId");
            Long secId = new Long(secIdStr);

            String indIdStr = request.getParameter("indicatorId");
            Long indId = new Long(indIdStr);

            List inds = DbUtil.getIndicatorValuesForSectorIndicator(secId, indId);

            List segmentDataList = new ArrayList();
            Iterator indsIt = inds.iterator();
            while (indsIt.hasNext()) {
                Object[] indData = (Object[]) indsIt.next();
                SegmentData indHilightData = new SegmentData();
                indHilightData.setSegmentCode((String)indData[1]);
                indHilightData.setSegmentValue(((Double)indData[0]).toString());
                segmentDataList.add(indHilightData);
            }


            List hilightData = prepareHilightSegments(segmentDataList, map);

            BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
                    BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = graph.createGraphics();

            g2d.setBackground(new Color(0, 0, 100, 255));

            g2d.clearRect(0, 0, canvasWidth, canvasHeight);

            gisUtil.addDataToImage(g2d,
                                   map.getSegments(),
                                   hilightData,
                                   canvasWidth, canvasHeight,
                                   rect.getLeft(), rect.getRight(),
                                   rect.getTop(), rect.getBottom(), true);

            gisUtil.addCaptionsToImage(g2d,
                                       map.getSegments(),
                                       canvasWidth, canvasHeight,
                                       rect.getLeft(), rect.getRight(),
                                       rect.getTop(), rect.getBottom());

            g2d.dispose();

            RenderedImage ri = graph;

            ImageIO.write(ri, "png", sos);

            graph.flush();


        } else if (action.equalsIgnoreCase("getSectorDataXML")) {
            response.setContentType("text/xml");
            String secIdStr = request.getParameter("sectorId");

            Long secId = new Long(secIdStr);

            List secFundings = DbUtil.getSectorFoundings(secId);

            Iterator it = secFundings.iterator();

            Map locationIdObjectMap = new HashMap();
            Map locationFoundMap = new HashMap();

            Double totalFund = 0d;
            while (it.hasNext()) {
                Object[] secFounding = (Object[]) it.next();
                AmpActivity ampActivity = (AmpActivity) secFounding[0];
                Double activityFound = DbUtil.getActivityFoundings(ampActivity.
                        getAmpActivityId());

                Double addAmmount = 0d;
                if (activityFound != null) {
                    float addPercent = (Float) secFounding[1];
                    addAmmount = activityFound * addPercent /
                                 100;
                    totalFund += addAmmount;
                }

                Iterator locIt = ampActivity.getLocations().iterator();
                while (locIt.hasNext()) {

                    AmpActivityLocation loc = (AmpActivityLocation) locIt.next();

                    locationIdObjectMap.put(loc.getLocation().getAmpLocationId(),
                                            loc.getLocation());

                    if (locationFoundMap.containsKey(loc.getLocation().
                            getAmpLocationId())) {
                        Double existingVal = (Double) locationFoundMap.get(loc.
                                getLocation().getAmpLocationId());
                        locationFoundMap.put(loc.getLocation().getAmpLocationId(),
                                             existingVal +
                                             addAmmount *
                                             loc.getLocationPercentage() /
                                             100);
                    } else {
                        locationFoundMap.put(loc.getLocation().getAmpLocationId(),
                                             addAmmount *
                                             loc.getLocationPercentage() /
                                             100);
                    }
                }

             }

            XMLDocument segmendDataInfo = new XMLDocument();
            XML root = new XML("funding");
            root.addAttribute("total", ((float)Math.round(totalFund/10))/100f);
            segmendDataInfo.addElement(root);
            Iterator locFoundingMapIt = locationFoundMap.keySet().iterator();
            while (locFoundingMapIt.hasNext()) {
                Long key = (Long) locFoundingMapIt.next();
                AmpLocation loc = (AmpLocation) locationIdObjectMap.get(key);
                Double ammount = (Double) locationFoundMap.get(key);
                if (loc.getRegion() != null) {
                    XML regionData = new XML("region");
                    regionData.addAttribute("reg-code", loc.getRegion());
                    regionData.addAttribute("funding", ammount);
                    root.addElement(regionData);
                }
            }



            sos.print(segmendDataInfo.toString());
        } else if (action.equalsIgnoreCase("getIndicatorNamesXML")) {

            response.setContentType("text/xml");
            String secIdStr = request.getParameter("sectorId");
            Long secId = new Long(secIdStr);


            XMLDocument sectorIndicators = new XMLDocument();
            XML root = new XML("indicators");
            sectorIndicators.addElement(root);

            //Add indicators
            List secIndicatorList = DbUtil.getIndicatorsForSector(secId);
            Iterator indNameIterator = secIndicatorList.iterator();



            while (indNameIterator.hasNext()) {
                Object[] indName = (Object[]) indNameIterator.next();
                XML ind = new XML("indicator");
                ind.addAttribute("name", (String)indName[1]);
                ind.addAttribute("id", (Long)indName[0]);
                root.addElement(ind);

            }
            sos.print(sectorIndicators.toString());
        }

        sos.flush();
        sos.close();
        return null;

    }

    private List prepareHilightSegments(List segmentData, GisMap map) {
        List retVal = new ArrayList();
        Iterator it = map.getSegments().iterator();

        while (it.hasNext()) {
            GisMapSegment segment = (GisMapSegment) it.next();
            for (int idx = (int) 0; idx < segmentData.size(); idx++) {
                SegmentData sd = (SegmentData) segmentData.get(idx);
                if (sd.getSegmentCode().equalsIgnoreCase(segment.getSegmentCode())) {
                    HilightData hData = new HilightData();
                    hData.setSegmentId((int) segment.getSegmentId());
                    float redColor = Float.parseFloat(sd.getSegmentValue()) *
                                     2.55f;
                    hData.setColor(new ColorRGB((int) redColor,
                                                (int) (255f - redColor), 0));
                    retVal.add(hData);
                }
            }
        }
        return retVal;
    }

    private List getSegmentsForParent(String parentCode, GisMap map) {
        List retVal = new ArrayList();
        Iterator it = map.getSegments().iterator();

        while (it.hasNext()) {
            GisMapSegment segment = (GisMapSegment) it.next();
            if (segment.getParentSegmentCode().equalsIgnoreCase(parentCode)) {
                retVal.add(segment);
            }
        }
        return retVal;
    }

}
