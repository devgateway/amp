package org.digijava.module.gis.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.digijava.module.gis.dbentity.GisMap;
import org.digijava.module.gis.dbentity.GisMapPoint;
import org.digijava.module.gis.dbentity.GisMapSegment;
import org.digijava.module.gis.dbentity.GisMapShape;
import org.apache.ecs.xml.XMLDocument;
import org.apache.ecs.xml.XML;
import java.awt.font.LineMetrics;
import java.awt.Font;
import java.awt.font.GlyphVector;
import org.digijava.module.gis.util.HilightData;

/**
 * 
 * @author George Kvizhinadze
 *
 */
public class GisUtil {

    private static Map loadedMaps = null;

    static {
        loadedMaps = new HashMap();
    }


    public static GisMap getMap(String mapCode) {
        GisMap retVal = null;
        if (loadedMaps.containsKey(mapCode)) {
            retVal = (GisMap) loadedMaps.get(mapCode);
        } else {
            GisMap dbMap = DbUtil.getMapByCode(mapCode);
            if (dbMap != null) {
                retVal = dbMap;
                loadedMaps.put(mapCode, dbMap);
            }
        }
        return retVal;
    }

    public void addDataToImage(Graphics2D g2d, List mapData, int segmentNo,
                               int canvasWidth, int canvasHeight,
                               float mapLeftX,
                               float mapRightX, float mapTopY, float mapLowY,
                               boolean fill) {
        List hilightList = null;
        if (segmentNo > 0) {
            hilightList = new ArrayList();
            HilightData hData = new HilightData(segmentNo,
                                                new ColorRGB(249, 237, 213));
            hilightList.add(hData);
        }
        addDataToImage(g2d, mapData, hilightList,  canvasWidth, canvasHeight,
                       mapLeftX, mapRightX, mapTopY, mapLowY, fill);


    }



    public void addDataToImage(Graphics2D g2d, List mapData, List hDataList,
                               int canvasWidth, int canvasHeight,
                               float mapLeftX,
                               float mapRightX, float mapTopY, float mapLowY,
                               boolean fill) {

        float scale = 1f; //Pixels per degree

        int border = 10;

        float scaleX = (float) (canvasWidth - border * 2) / (mapRightX - mapLeftX);
        float scaleY = (float) (canvasHeight - border * 2) / (mapTopY - mapLowY);

        if (scaleX < scaleY) {
            scale = scaleX;
        } else {
            scale = scaleY;
        }
        int xOffset = (int) ( -mapLeftX * scale) + border;
        int yOffset = (int) ( -mapLowY * scale);

        try {

            //Color paintColor = new Color(39, 39, 119);
            Color paintColor = new Color(201, 153, 113);
            Color fillColor = null;

            for (int segmentId = 0; segmentId < mapData.size();
                                 segmentId++) {

                GisMapSegment gms = (GisMapSegment) mapData.get(segmentId);

                if (hDataList != null && hDataList.size() > 0) {
                    ColorRGB cRGB = getColorForSegment((int)gms.getSegmentId(), hDataList);
                    if (cRGB != null) {
                        fillColor = new Color(cRGB.getRed(), cRGB.getGreen(),
                                              cRGB.getBlue());
                    } else {
                        fillColor = paintColor;
                    }
                } else if (fill && fillColor == null) {
                    fillColor = paintColor;
                }



                    for (int shapeId = 0; shapeId < gms.getShapes().size(); shapeId++) {

                        GisMapShape shape = (GisMapShape) gms.getShapes().get(shapeId);
                        int[] xCoords = new int[shape.getShapePoints().size()];
                        int[] yCoords = new int[shape.getShapePoints().size()];

                        for (int mapPointId = 0;
                                              mapPointId < shape.getShapePoints().size();
                                              mapPointId++) {
                            GisMapPoint gmp = (GisMapPoint) shape.getShapePoints().get(
                                    mapPointId);

                            int xCoord = xOffset +
                                         (int) ((gmp.getLongatude()) *
                                                scale);
                            int yCoord = canvasHeight - border - (yOffset +
                                    (int) ((gmp.getLatitude()) *
                                           scale));
                            xCoords[mapPointId] = xCoord;
                            yCoords[mapPointId] = yCoord;
                        }

                        if (fill) {
                            Color gg = fillColor;
                            g2d.setColor(gg);

                            g2d.fillPolygon(xCoords, yCoords,
                                            shape.getShapePoints().size());

                            g2d.setColor(new Color(0, 0, 0, 70));
                            g2d.drawPolygon(xCoords, yCoords,
                                            shape.getShapePoints().size());

                        } else {
                            /*
                            g2d.setColor(new Color(39, 39, 119));
                            g2d.fillPolygon(xCoords, yCoords,
                                            shape.getShapePoints().size());
*/

                             g2d.setColor(new Color(0, 0, 0, 50));
                            g2d.drawPolygon(xCoords, yCoords,
                                            shape.getShapePoints().size());

                        }
                    }



                    GlyphVector glv = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), gms.getSegmentName());


                                 CoordinateRect ccRect = getMapRectForSegment(gms);

                                 g2d.setColor(new Color(0, 0, 0));
                                 g2d.drawString(gms.getSegmentName(),
                                                xOffset +
                                                (ccRect.getLeft() +
                                                 (ccRect.getRight() - ccRect.getLeft()) / 2) * scale -
                                                (int) (glv.getVisualBounds().getWidth() / 2) + 1,
                                                canvasHeight -
                                                (yOffset +
                                                 (ccRect.getBottom() +
                                                  (ccRect.getTop() - ccRect.getBottom()) / 2) * scale) + 1);

                                 g2d.setColor(new Color(255, 255, 255));
                                 g2d.drawString(gms.getSegmentName(),
                                                xOffset +
                                                (ccRect.getLeft() +
                                                 (ccRect.getRight() - ccRect.getLeft()) / 2) * scale -
                                                (int) (glv.getVisualBounds().getWidth() / 2),
                                                canvasHeight -
                                                (yOffset +
                                                 (ccRect.getBottom() +
                                                  (ccRect.getTop() - ccRect.getBottom()) / 2) * scale));





            }

            //make grid

                g2d.setColor(new Color(57, 57, 128, 80));
                for (float paralels = -90; paralels <= 90; paralels += 10) {
                    g2d.drawLine(border,
                                 canvasHeight - border -
                                 (yOffset + (int) ((paralels) * scale)),
                                 canvasWidth - border,
                                 canvasHeight - border -
                                 (yOffset + (int) ((paralels) * scale)));
                }

                for (float meridians = -180; meridians <= 180; meridians += 10) {
                    g2d.drawLine(xOffset + (int) (meridians * scale),
                                 border,
                                 xOffset + (int) (meridians * scale),
                                 canvasHeight - border);
                }

                g2d.setColor(new Color(130, 130, 164, 80));

                g2d.drawLine(border,
                             canvasHeight - border - yOffset,
                             canvasWidth - border,
                             canvasHeight - border - yOffset);

                g2d.drawLine(xOffset,
                             border,
                             xOffset,
                             canvasHeight - border);


            //make border

            g2d.setColor(new Color(0, 0, 100));

            g2d.fillRect(0, 0, canvasWidth, border - 1);
            g2d.fillRect(0, 0, border - 1, canvasHeight);
            g2d.fillRect(canvasWidth - border + 1, 0, canvasWidth, canvasHeight);
            g2d.fillRect(0, canvasHeight - border + 1, canvasWidth, canvasHeight);

            g2d.setColor(new Color(200, 200, 200, 255));
            g2d.drawRect(border - 3, border - 3, canvasWidth - border * 2 + 5,
                         canvasHeight - border * 2 + 5);
        } catch (Exception ex) {
            String ggg = "ggg";
        }


    }

    private ColorRGB getColorForSegment (int segmentCode, List hilightData) {
        ColorRGB retVal = null;
        Iterator it = hilightData.iterator();
        while (it.hasNext()) {
            HilightData hData = (HilightData) it.next();
            if (hData.getSegmentId() == segmentCode) {
                retVal = hData.getColor();
                break;
            }
        }
        return retVal;
    }

    public List readMapDataFromCSV(String fileName) throws Exception {

        BufferedReader fis = new BufferedReader(new FileReader(fileName));

        int readByteCount = 0;
        char[] readBuff = new char[1024];
        StringBuffer fileContent = new StringBuffer();
        do {
            readByteCount = fis.read(readBuff);
            fileContent.append(String.valueOf(readBuff, 0, readByteCount));
        } while (readByteCount == 1024);

        fis.close();

        String fileContentStr = fileContent.toString();

        StringTokenizer fcspliter = new StringTokenizer(fileContentStr,
                "\n\r");

        List mapDataSegments = new ArrayList();

        long segmentNo = 0;
        long shapeNo = 0;

        GisMapSegment segment = null;
        GisMapShape shape = null;

        while (fcspliter.hasMoreTokens()) {
            String dataElement = (String) fcspliter.nextElement();

            StringTokenizer dataLinepliter = new StringTokenizer(dataElement,
                    ",");

            int segmentId = Integer.parseInt(dataLinepliter.nextToken());
            int shapeId = Integer.parseInt(dataLinepliter.nextToken());
            int pointId = Integer.parseInt(dataLinepliter.nextToken());
            float longatudeVal = Float.parseFloat(dataLinepliter.nextToken());
            float latitudeVal = Float.parseFloat(dataLinepliter.nextToken());

            if (segmentId > segmentNo) {
                segment = new GisMapSegment(segmentId);
                mapDataSegments.add(segment);
                shapeNo = 0;
            }

            if (shapeId > shapeNo) {
                shape = new GisMapShape(shapeId);
                segment.getShapes().add(shape);
            }

            GisMapPoint point = new GisMapPoint(pointId, longatudeVal,
                                                latitudeVal);
            shape.getShapePoints().add(point);

            segmentNo = segmentId;
            shapeNo = shapeId;
        }
        return mapDataSegments;
    }

    public CoordinateRect getMapRect(GisMap map) {
        return getMapRect(map.getSegments());
    }

    public CoordinateRect getMapRect(List segments) {
        CoordinateRect retVal = new CoordinateRect(180f, -180f, -90f, 90f);
        Iterator segmentIt = segments.iterator();
        while (segmentIt.hasNext()) {
            GisMapSegment segment = (GisMapSegment) segmentIt.next();
            Iterator shapeIt = segment.getShapes().iterator();
            while (shapeIt.hasNext()) {
                GisMapShape shape = (GisMapShape) shapeIt.next();
                Iterator pointIt = shape.getShapePoints().iterator();
                while (pointIt.hasNext()) {
                    GisMapPoint point = (GisMapPoint) pointIt.next();

                    if (retVal.getLeft() > point.getLongatude()) {
                        retVal.setLeft(point.getLongatude());
                    }

                    if (retVal.getRight() < point.getLongatude()) {
                        retVal.setRight(point.getLongatude());
                    }

                    if (retVal.getTop() < point.getLatitude()) {
                        retVal.setTop(point.getLatitude());
                    }

                    if (retVal.getBottom() > point.getLatitude()) {
                        retVal.setBottom(point.getLatitude());
                    }

                }
            }
        }

        return retVal;
    }

    public CoordinateRect getMapRectForSegment(GisMapSegment segment) {
        CoordinateRect retVal = new CoordinateRect(180f, -180f, -90f, 90f);
        Iterator shapeIt = segment.getShapes().iterator();
        while (shapeIt.hasNext()) {
            GisMapShape shape = (GisMapShape) shapeIt.next();
            Iterator pointIt = shape.getShapePoints().iterator();
            while (pointIt.hasNext()) {
                GisMapPoint point = (GisMapPoint) pointIt.next();

                if (retVal.getLeft() > point.getLongatude()) {
                    retVal.setLeft(point.getLongatude());
                }

                if (retVal.getRight() < point.getLongatude()) {
                    retVal.setRight(point.getLongatude());
                }

                if (retVal.getTop() < point.getLatitude()) {
                    retVal.setTop(point.getLatitude());
                }

                if (retVal.getBottom() > point.getLatitude()) {
                    retVal.setBottom(point.getLatitude());
                }

            }
        }
        return retVal;
    }

    public XMLDocument getImageMap(List mapData,
                                   int pointsPerShape,
                                   int canvasWidth,
                                   int canvasHeight,
                                   float mapLeftX,
                                   float mapRightX,
                                   float mapTopY,
                                   float mapLowY) {

        XMLDocument retVal = new XMLDocument();
        XML imageMapDefRoot = null;
        imageMapDefRoot = new XML("map");
        retVal.addElement(imageMapDefRoot);

        float scale = 1f; //Pixels per degree
        int border = 10;


        float scaleX = (float) (canvasWidth - border * 2) / (mapRightX - mapLeftX);
        float scaleY = (float) (canvasHeight - border * 2) / (mapTopY - mapLowY);

        if (scaleX < scaleY) {
            scale = scaleX;
        } else {
            scale = scaleY;
        }
        int xOffset = (int) ( -mapLeftX * scale) + border;
        int yOffset = (int) ( -mapLowY * scale);

        for (int segmentId = 0; segmentId < mapData.size();
                             segmentId++) {
            GisMapSegment gms = (GisMapSegment) mapData.get(segmentId);

            XML segmentNode = null;

            segmentNode = new XML("segment");
            segmentNode.addAttribute("name", gms.getSegmentName());
            segmentNode.addAttribute("code", gms.getSegmentCode());
            if (gms.getSegmentDescription() != null) {
                segmentNode.addAttribute("desc", gms.getSegmentDescription());
            }
            imageMapDefRoot.addElement(segmentNode);

            for (int shapeId = 0; shapeId < gms.getShapes().size(); shapeId++) {

                GisMapShape shape = (GisMapShape) gms.getShapes().get(shapeId);

                XML shapeNode = null;
                int skipPoints = 0;

                shapeNode = new XML("shape");
                segmentNode.addElement(shapeNode);
                skipPoints = shape.getShapePoints().size() / pointsPerShape;

                for (int mapPointId = 0;
                                      mapPointId < shape.getShapePoints().size();
                                      mapPointId += skipPoints) {

                    GisMapPoint gmp = (GisMapPoint) shape.getShapePoints().get(
                            mapPointId);

                    int xCoord = xOffset +
                                 (int) ((gmp.getLongatude()) *
                                        scale);
                    int yCoord = canvasHeight - border - (yOffset +
                                                 (int) ((gmp.getLatitude()) *
                            scale));

                    XML pointNode = new XML("point");
                    pointNode.addAttribute("x", xCoord);
                    pointNode.addAttribute("y", yCoord);
                    shapeNode.addElement(pointNode);
                }
            }
        }
        return retVal;
    }

    public XMLDocument getSegmentData(List mapData) {

        XMLDocument retVal = new XMLDocument();
        XML rootNode = null;
        rootNode = new XML("segments");
        retVal.addElement(rootNode);

        for (int segmentId = 0; segmentId < mapData.size();
                             segmentId++) {
            GisMapSegment gms = (GisMapSegment) mapData.get(segmentId);
            XML segmentNode = null;
            segmentNode = new XML("segment");
            segmentNode.addAttribute("name", gms.getSegmentName());
            segmentNode.addAttribute("code", gms.getSegmentCode());
            if (gms.getSegmentDescription() != null) {
                segmentNode.addAttribute("desc", gms.getSegmentDescription());
            }
            rootNode.addElement(segmentNode);
        }
        return retVal;
    }

    public List getSegmentsForParent (List mapData, String parentCode) {
        List retVal = new ArrayList();
        Iterator segmentIt = mapData.iterator();
        while (segmentIt.hasNext()) {
            GisMapSegment seg = (GisMapSegment) segmentIt.next();
            if (seg.getParentSegmentCode().equalsIgnoreCase(parentCode)) {
                retVal.add(seg);
            }
        }
        return retVal;
    }

}
