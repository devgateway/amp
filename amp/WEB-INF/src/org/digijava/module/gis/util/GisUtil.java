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

        float scale = 1f; //Pixels per degree

        float scaleX = (float) (canvasWidth) / (mapRightX - mapLeftX);
        float scaleY = (float) (canvasHeight) / (mapTopY - mapLowY);

        if (scaleX < scaleY) {
            scale = scaleX;
        } else {
            scale = scaleY;
        }
        int xOffset = (int) ( -mapLeftX * scale);
        int yOffset = (int) ( -mapLowY * scale);

        for (int segmentId = 0; segmentId < mapData.size();
                             segmentId++) {

            GisMapSegment gms = (GisMapSegment) mapData.get(segmentId);

            if (segmentNo < 0 || segmentNo == gms.getSegmentId()) {


                /*
            g2d.setColor(new Color(250, 250, 250));
            CoordinateRect ccRect = getMapRectForSegment(gms);
            g2d.drawString(gms.getSegmentName(),
                           xOffset + (ccRect.getLeft() + (ccRect.getRight()-ccRect.getLeft())/2) * scale,
                           canvasHeight - (yOffset + (ccRect.getBottom() + (ccRect.getTop() - ccRect.getBottom()) / 2) * scale));
*/



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
                    int yCoord = canvasHeight - (yOffset +
                                                 (int) ((gmp.getLatitude()) *
                                                        scale));
                    xCoords[mapPointId] = xCoord;
                    yCoords[mapPointId] = yCoord;
                }

                if (fill) {
                    Color gg = new Color(0, 0, 255);
                    g2d.setColor(gg);


                    g2d.fillPolygon(xCoords, yCoords,
                                    shape.getShapePoints().size());

                    g2d.setColor(new Color(255, 255, 255));
                    g2d.drawPolygon(xCoords, yCoords,
                                    shape.getShapePoints().size());


                } else {
                    g2d.setColor(new Color(200, 200, 200));
                    g2d.drawPolygon(xCoords, yCoords,
                                    shape.getShapePoints().size());
                    g2d.setColor(new Color(200, 200, 200, 50));
                    g2d.fillPolygon(xCoords, yCoords,
                                    shape.getShapePoints().size());
                }
            }
        }
        }

        //make grid
        g2d.setColor(new Color(200, 200, 200, 40));
        for (float paralels = -90; paralels < 90; paralels += 10) {
            g2d.drawLine(0, canvasHeight - yOffset + (int) (paralels * scale),
                         canvasWidth,
                         canvasHeight - yOffset + (int) (paralels * scale));
        }

        for (float meridians = -180; meridians < 180; meridians += 10) {
            g2d.drawLine(xOffset + (int) (meridians * scale), 0,
                         xOffset + (int) (meridians * scale), canvasHeight);
        }

        g2d.setColor(new Color(200, 200, 200, 77));
        g2d.drawLine(0, canvasHeight - yOffset, canvasWidth,
                     canvasHeight - yOffset);
        g2d.drawLine(xOffset, 0, xOffset, canvasHeight);

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

        float scaleX = (float) (canvasWidth) / (mapRightX - mapLeftX);
        float scaleY = (float) (canvasHeight) / (mapTopY - mapLowY);

        if (scaleX < scaleY) {
            scale = scaleX;
        } else {
            scale = scaleY;
        }
        int xOffset = (int) ( -mapLeftX * scale);
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
                    int yCoord = canvasHeight - (yOffset +
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


}
