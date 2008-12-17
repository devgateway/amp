package org.digijava.module.gis.action;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ecs.xml.XML;
import org.apache.ecs.xml.XMLDocument;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.gis.dbentity.GisMap;
import org.digijava.module.gis.dbentity.GisMapSegment;
import org.digijava.module.gis.util.ColorRGB;
import org.digijava.module.gis.util.CoordinateRect;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.gis.util.FundingData;
import org.digijava.module.gis.util.GisUtil;
import org.digijava.module.gis.util.HilightData;
import org.digijava.module.gis.util.SegmentData;

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
        ServletOutputStream sos = null;

        try {

            GisUtil gisUtil = new GisUtil();
            sos = response.getOutputStream();

            String action = request.getParameter("action");

            String mapCode = request.getParameter("mapCode");
            GisMap map = null;

            String mapLevel = request.getParameter("mapLevel");
            if (mapLevel == null) {
                mapLevel = "2";
            }


            String indYear = request.getParameter("indYear");
            Integer indicatorYear = null;
            if (indYear == null) {
                indicatorYear = new Integer(-1);
            } else {
                indicatorYear = new Integer(indYear);
            }


            String subgroupIdStr = request.getParameter("subgroupId");
            Long subgroupId = null;
            if (subgroupIdStr != null) {
                subgroupId = new Long(subgroupIdStr);
            } else {
                subgroupId = Long.valueOf(0l);
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


            CoordinateRect rect = gisUtil.getMapRect(map);

            if (action.equalsIgnoreCase(GisService.ACTION_PAINT_MAP)) {
                response.setContentType("image/png");

                BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
                                                        BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = graph.createGraphics();

                g2d.setBackground(new Color(0, 0, 100, 255));

                g2d.clearRect(0, 0, canvasWidth, canvasHeight);

                boolean fill = true;
                 if (request.getParameter("noFill") != null) {
                     fill = false;
                 }

                gisUtil.addDataToImage(g2d,
                                       map.getSegments(),
                                       -1,
                                       canvasWidth, canvasHeight,
                                       rect.getLeft(), rect.getRight(),
                                       rect.getTop(), rect.getBottom(), fill);

                if (request.getParameter("noCapt") == null) {

                    gisUtil.addCaptionsToImage(g2d,
                                               map.getSegments(),
                                               canvasWidth, canvasHeight,
                                               rect.getLeft(), rect.getRight(),
                                               rect.getTop(), rect.getBottom());
                }
                g2d.dispose();

                RenderedImage ri = graph;

                ImageIO.write(ri, "png", sos);

                graph.flush();

            } else if (action.equalsIgnoreCase(GisService.ACTION_GET_IMAGE_MAP)) {
                response.setContentType("text/xml");

                List mapDataSegments = map.getSegments();

                String imageMapCode = gisUtil.getImageMap(mapDataSegments, 20,
                                                          canvasWidth,
                                                          canvasHeight, rect.getLeft(),
                                                          rect.getRight(), rect.getTop(),
                                                          rect.getBottom()).toString();

                sos.print(imageMapCode);

            } else if (action.equalsIgnoreCase("getDataForSector")) {

                response.setContentType("image/png");

                String secIdStr = request.getParameter("sectorId");

                Long secId = new Long(secIdStr);

                List secFundings = null;

                if (secId.longValue() > 0l) {
                    secFundings = DbUtil.getSectorFoundings(secId);
                } else {
                    secFundings = new ArrayList();
                }
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

                List hilightData = prepareHilightSegments(segmentDataList, map, new Double(0), new Double(100));

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

                AmpIndicator selIndicator = null;
                if (indId.intValue()>0) {
                    selIndicator = IndicatorUtil.getIndicator(indId);
                }
                //Get segments with funding for dashed paint map
                List secFundings = null;

                if (secId.longValue() > 0l) {
                    secFundings = DbUtil.getSectorFoundings(secId);
                } else {
                    secFundings = new ArrayList();
                }


                Object [] fundingList = getFundingsByLocations(secFundings, Integer.parseInt(mapLevel));
                Map fundingLocationMap = (Map) fundingList[0];

                List segmentDataDasheList = new ArrayList();

                Iterator locFoundingMapIt = fundingLocationMap.keySet().iterator();
                while (locFoundingMapIt.hasNext()) {
                    String key = (String) locFoundingMapIt.next();
                    SegmentData segmentData = new SegmentData();
                    segmentData.setSegmentCode(key);
                    segmentData.setSegmentValue("100");
                    segmentDataDasheList.add(segmentData);
                }

                List hilightDashData = prepareDashSegments(segmentDataDasheList,
                        new ColorRGB(0, 0, 0), map);

                List inds = null;
                if (indicatorYear.intValue()>0) {
                    inds = DbUtil.getIndicatorValuesForSectorIndicator(secId, indId,
                            indicatorYear, subgroupId, Integer.parseInt(mapLevel));
                } else {
                    inds = new ArrayList();
                }
                List segmentDataList = new ArrayList();
                Iterator indsIt = inds.iterator();
                Double min = null;
                Double max = null;

                Set regSet = new HashSet();

                while (indsIt.hasNext()) {
                    Object[] indData = (Object[]) indsIt.next();

                    String segmentCode = (String) indData[1];
                    Double indValue = (Double) indData[0];

                    if (isRegion(map,segmentCode) && !regSet.contains(segmentCode)) {

                    SegmentData indHilightData = new SegmentData();
                    indHilightData.setSegmentCode(segmentCode);
                    indHilightData.setSegmentValue(indValue.toString());

                    if (min == null) {
                        min = indValue;
                        max = indValue;
                    }

                    if (indValue < min) {
                        min = indValue;
                    }

                    if (indValue > max) {
                        max = indValue;
                    }

                    regSet.add(segmentCode);
                    segmentDataList.add(indHilightData);
                   }


                }

                if (min == null) {
                    min = new Double(0);
                    max = new Double(0);
                }

                //Save data in session for future use (next action should be get indicator values and unit)
                if (selIndicator != null) {
                    String indUnit = null;

                    if (selIndicator.getUnit() != null && selIndicator.getUnit().trim().length() > 0) {
                        indUnit = selIndicator.getUnit();
                    } else {
                        indUnit = "N/A";
                    }

                    request.getSession().setAttribute("AMP_INDICATOR_UNIT", indUnit);
                    request.getSession().setAttribute("AMP_INDICATOR_VALUES", segmentDataList);
                }

                List hilightData = prepareHilightSegments(segmentDataList, map, min, max);

                BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,
                                                        BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = graph.createGraphics();

                g2d.setBackground(new Color(0, 0, 100, 255));

                g2d.clearRect(0, 0, canvasWidth, canvasHeight);

                gisUtil.addDataToImage(g2d,
                                       map.getSegments(),
                                       hilightData,
                                       hilightDashData,
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


                Object [] fundingList = getFundingsByLocations(secFundings, Integer.parseInt(mapLevel));
                Map fundingLocationMap = (Map) fundingList[0];
                FundingData totalFunding = (FundingData) fundingList[1];

                XMLDocument segmendDataInfo = new XMLDocument();
                XML root = new XML("funding");
                root.addAttribute("totalCommitment", ((float) Math.round(totalFunding.getCommitment() / 10)) / 100f);
                root.addAttribute("totalDisbursement", ((float) Math.round(totalFunding.getDisbursement() / 10)) / 100f);
                root.addAttribute("totalExpenditure", ((float) Math.round(totalFunding.getExpenditure() / 10)) / 100f);

                segmendDataInfo.addElement(root);
                Iterator locFoundingMapIt = fundingLocationMap.keySet().iterator();
                while (locFoundingMapIt.hasNext()) {
                    String key = (String) locFoundingMapIt.next();
                    FundingData ammount = (FundingData) fundingLocationMap.get(key);
                        XML regionData = new XML("region");
                        regionData.addAttribute("reg-code", key);
                        regionData.addAttribute("fundingCommitment", ((float) Math.round(ammount.getCommitment().intValue() * 100)) / 100f);
                        regionData.addAttribute("fundingDisbursement", ((float) Math.round(ammount.getDisbursement().intValue() * 100)) / 100f);
                        regionData.addAttribute("fundingExpenditure", ((float) Math.round(ammount.getExpenditure().intValue() * 100)) / 100f);
                        root.addElement(regionData);
                }

                sos.print(segmendDataInfo.toString());
            } else if (action.equalsIgnoreCase("getAvailIndicatorSubgroups")) {
                response.setContentType("text/xml");

                String secIdStr = request.getParameter("sectorId");
                Long secId = new Long(secIdStr);

                String indIdStr = request.getParameter("indicatorId");
                Long indId = new Long(indIdStr);

                List availSubgroups = DbUtil.getAvailSubgroupsForSectorIndicator(secId, indId, Integer.parseInt(mapLevel));

                XMLDocument availSubGroupXmlDoc = new XMLDocument();
                XML root = new XML("availSubgroups");

                availSubGroupXmlDoc.addElement(root);

                Iterator subIt = availSubgroups.iterator();

                while (subIt.hasNext()) {
                    Object[] subData = (Object[]) subIt.next();
                    Long subId = (Long) subData[0];
                    String subName = (String) subData[1];

                    XML subNode = new XML("subgroup");
                    subNode.addAttribute("id",subId);
                    subNode.addAttribute("name",subName);
                    root.addElement(subNode);
                }

                sos.print(availSubGroupXmlDoc.toString());
            } else if (action.equalsIgnoreCase("getAvailIndicatorYears")) {
                response.setContentType("text/xml");

                String secIdStr = request.getParameter("sectorId");
                Long secId = new Long(secIdStr);

                String indIdStr = request.getParameter("indicatorId");
                Long indId = new Long(indIdStr);

                List availYears = DbUtil.getAvailYearsForSectorIndicator(secId, indId, Integer.parseInt(mapLevel), subgroupId);

                XMLDocument availYearsXmlDoc = new XMLDocument();
                XML root = new XML("availYears");

                availYearsXmlDoc.addElement(root);

                Iterator yearIt = availYears.iterator();

                while (yearIt.hasNext()) {
                    Integer year = (Integer) yearIt.next();
                    XML yearNode = new XML("year");
                    yearNode.addAttribute("value",year);
                    root.addElement(yearNode);
                }

                sos.print(availYearsXmlDoc.toString());
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
                    ind.addAttribute("name", (String) indName[1]);
                    ind.addAttribute("id", (Long) indName[0]);
                    root.addElement(ind);

                }
                sos.print(sectorIndicators.toString());
            } else if (action.equalsIgnoreCase("getIndicatorValues")) {

                response.setContentType("text/xml");

                String indicatorUnit = (String) request.getSession().getAttribute("AMP_INDICATOR_UNIT");
                request.getSession().removeAttribute("AMP_INDICATOR_UNIT");

                List indicatorVals = (List) request.getSession().getAttribute("AMP_INDICATOR_VALUES");

                XMLDocument indicators = new XMLDocument();
                XML root = new XML("indicatorData");
                root.addAttribute("indUnit", indicatorUnit);
                indicators.addElement(root);

                if (indicatorVals != null && !indicatorVals.isEmpty()) {
                    request.getSession().removeAttribute("AMP_INDICATOR_VALUES");
                    Iterator <SegmentData> it = indicatorVals.iterator();
                    while (it.hasNext()) {
                        SegmentData sd = it.next();
                        XML indVal = new XML("indVal");
                        indVal.addAttribute("val", sd.getSegmentValue());
                        indVal.addAttribute("reg", sd.getSegmentCode());
                        root.addElement(indVal);
                    }
                }

                sos.print(indicators.toString());

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sos.flush();
            sos.close();
        }
        return null;

    }

    private List prepareHilightSegments(List segmentData, GisMap map, Double min, Double max) {

        float delta = max.floatValue() - min.floatValue();
        float coeff = 205/delta;

        List retVal = new ArrayList();
        Iterator it = map.getSegments().iterator();

        while (it.hasNext()) {
            GisMapSegment segment = (GisMapSegment) it.next();
            for (int idx = (int) 0; idx < segmentData.size(); idx++) {
                SegmentData sd = (SegmentData) segmentData.get(idx);
                if (sd.getSegmentCode().equalsIgnoreCase(segment.getSegmentCode())) {
                    HilightData hData = new HilightData();
                    hData.setSegmentId((int) segment.getSegmentId());
                    float green = (Float.parseFloat(sd.getSegmentValue()) - min.floatValue()) * coeff;
                    hData.setColor(new ColorRGB((int) 0,
                                                (int) (green + 50f), 0));
                    retVal.add(hData);
                }
            }
        }
        return retVal;
    }

    private boolean isRegion (GisMap map, String regCode) {
        boolean retVal = false;
        Iterator it = map.getSegments().iterator();

        while (it.hasNext()) {
            GisMapSegment segment = (GisMapSegment) it.next();
            if (segment.getSegmentCode().equalsIgnoreCase(regCode)) {
                retVal = true;
                break;
            }
        }

        return retVal;
    }

    private List prepareDashSegments(List segmentData, ColorRGB dashColor, GisMap map) {
        List retVal = new ArrayList();
        Iterator it = map.getSegments().iterator();

        while (it.hasNext()) {
            GisMapSegment segment = (GisMapSegment) it.next();
            for (int idx = (int) 0; idx < segmentData.size(); idx++) {
                SegmentData sd = (SegmentData) segmentData.get(idx);
                if (sd.getSegmentCode().equalsIgnoreCase(segment.getSegmentCode())) {
                    HilightData hData = new HilightData();
                    hData.setSegmentId((int) segment.getSegmentId());
                    hData.setColor(dashColor);
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

    private Object[] getFundingsByLocations (List activityList, int level) throws Exception {

        Map locationFundingMap = new HashMap();
        FundingData totalFundingForSector = new FundingData();
        //Calculate total funding
        Iterator<Object[]> actIt = activityList.iterator();
        while (actIt.hasNext()) {
            Object[] actData = actIt.next();
            AmpActivity activity = (AmpActivity) actData[0];
            Float percentsForSectorSelected = (Float)actData[1];
            FundingData totalFunding = getActivityTotalFundingInUSD (activity);

            totalFundingForSector.setCommitment(totalFundingForSector.getCommitment() + totalFunding.getCommitment().floatValue()*percentsForSectorSelected.floatValue()/100f);
            totalFundingForSector.setDisbursement(totalFundingForSector.getDisbursement() + totalFunding.getDisbursement().floatValue()*percentsForSectorSelected.floatValue()/100f);
            totalFundingForSector.setExpenditure(totalFundingForSector.getExpenditure() + totalFunding.getExpenditure().floatValue()*percentsForSectorSelected.floatValue()/100f);


            FundingData fundingForSector = new FundingData();
            fundingForSector.setCommitment(new Double(totalFunding.getCommitment().floatValue()*percentsForSectorSelected.floatValue()/100f));
            fundingForSector.setDisbursement(new Double(totalFunding.getDisbursement().floatValue()*percentsForSectorSelected.floatValue()/100f));
            fundingForSector.setExpenditure(new Double(totalFunding.getExpenditure().floatValue()*percentsForSectorSelected.floatValue()/100f));

            Set locations = activity.getLocations();
            Iterator <AmpActivityLocation> locIt = locations.iterator();


            while (locIt.hasNext()) {
                AmpActivityLocation loc = locIt.next();

                //Region level
                //if (loc.getLocation().getAmpRegion() != null && loc.getLocation().getZone()==null && loc.getLocationPercentage().floatValue() > 0.0f) {

                if (loc.getLocationPercentage().floatValue() > 0.0f) {
                    if (level == GisMap.MAP_LEVEL_REGION) {

                        String regCode = loc.getLocation().getAmpRegion().getRegionCode();
                        if (locationFundingMap.containsKey(regCode)) {
                            FundingData existingVal = (FundingData) locationFundingMap.get(
                                    regCode);

                            FundingData newVal = new FundingData();
                            newVal.setCommitment(new Double(existingVal.getCommitment() +
                                                            fundingForSector.getCommitment().
                                                            floatValue() *
                                                            loc.getLocationPercentage().
                                                            floatValue() / 100f));
                            newVal.setDisbursement(new Double(existingVal.getDisbursement() +
                                                              fundingForSector.
                                                              getDisbursement().floatValue() *
                                                              loc.getLocationPercentage().
                                                              floatValue() / 100f));
                            newVal.setExpenditure(new Double(existingVal.getExpenditure() +
                                                             fundingForSector.
                                                             getExpenditure().floatValue() *
                                                             loc.getLocationPercentage().
                                                             floatValue() / 100f));

                            locationFundingMap.put(regCode, newVal);
                        } else {
                            FundingData newVal = new FundingData();
                            newVal.setCommitment(new Double(fundingForSector.getCommitment().
                                                            floatValue() *
                                                            loc.getLocationPercentage().
                                                            floatValue() / 100f));
                            newVal.setDisbursement(new Double(fundingForSector.
                                                              getDisbursement().floatValue() *
                                                              loc.getLocationPercentage().
                                                              floatValue() / 100f));
                            newVal.setExpenditure(new Double(fundingForSector.
                                                             getExpenditure().floatValue() *
                                                             loc.getLocationPercentage().
                                                             floatValue() / 100f));

                            locationFundingMap.put(regCode, newVal);
                        }
                    } else if (level == GisMap.MAP_LEVEL_DISTRICT && loc.getLocation().getAmpZone()!=null) {

                        //District level
                        //if (loc.getLocation().getAmpZone()!=null && loc.getLocationPercentage().floatValue() > 0.0f) {

                        String regCode = loc.getLocation().getAmpZone().getZoneCode();

                        if (locationFundingMap.containsKey(regCode)) {
                            FundingData existingVal = (FundingData)
                                                      locationFundingMap.get(
                                                              regCode);

                            FundingData newVal = new FundingData();
                            newVal.setCommitment(new Double(existingVal.
                                                            getCommitment() +
                                                            fundingForSector.getCommitment().
                                                            floatValue() *
                                                            loc.getLocationPercentage().
                                                            floatValue() /
                                                            100f));
                            newVal.setDisbursement(new Double(existingVal.
                                                              getDisbursement() +
                                                              fundingForSector.
                                                              getDisbursement().
                                                              floatValue() *
                                                              loc.getLocationPercentage().
                                                              floatValue() / 100f));
                            newVal.setExpenditure(new Double(existingVal.
                                                             getExpenditure() +
                                                             fundingForSector.
                                                             getExpenditure().
                                                             floatValue() *
                                                             loc.getLocationPercentage().
                                                             floatValue() /
                                                             100f));

                            locationFundingMap.put(regCode, newVal);
                        } else {
                            FundingData newVal = new FundingData();
                            newVal.setCommitment(new Double(fundingForSector.
                                                            getCommitment().floatValue() *
                                                            loc.getLocationPercentage().
                                                            floatValue() / 100f));
                            newVal.setDisbursement(new Double(fundingForSector.
                                                              getDisbursement().floatValue() *
                                                              loc.getLocationPercentage().
                                                              floatValue() / 100f));
                            newVal.setExpenditure(new Double(fundingForSector.
                                                             getExpenditure().floatValue() *
                                                             loc.getLocationPercentage().
                                                             floatValue() / 100f));

                            locationFundingMap.put(regCode, newVal);
                        }
                    }
                }


            }

        //    Set activiactivity.getFunding();
        }
        Object[] retVal = new Object[2];
        retVal[0] = locationFundingMap;
        retVal[1] = totalFundingForSector;
        return retVal;
    }

    private FundingData getActivityTotalFundingInUSD(AmpActivity activity) {
        FundingData retVal = null;
        Set fundSet = activity.getFunding();
        Iterator <AmpFunding> fundIt = fundSet.iterator();


        Double commitment = null;
        Double disbursement = null;
        Double expenditure = null;


        FundingCalculationsHelper fch = new FundingCalculationsHelper();
//        fch.doCalculations();

        Set fundDetSet = new HashSet();

        try {
            while (fundIt.hasNext()) {
                AmpFunding fund = fundIt.next();
                Set fundDetaiuls = fund.getFundingDetails();

                fundDetSet.addAll(fundDetaiuls);

                /*
                Iterator<AmpFundingDetail> fundDetIt = fundDetaiuls.iterator();
                while (fundDetIt.hasNext()) {
                    AmpFundingDetail fundDet = fundDetIt.next();
                    Double exchangeRate = null;
                    exchangeRate = fundDet.getFixedExchangeRate();
                   if (fundDet.getTransactionType().intValue() ==
                       Constants.COMMITMENT) {
                       commitment += fundDet.getTransactionAmount() /
                               exchangeRate;
                   } else if (fundDet.getTransactionType().intValue() ==
                              Constants.DISBURSEMENT) {
                       disbursement += fundDet.getTransactionAmount() /
                               exchangeRate;
                   } else if (fundDet.getTransactionType().intValue() ==
                              Constants.EXPENDITURE) {
                       expenditure += fundDet.getTransactionAmount() /
                               exchangeRate;
                   }

                }*/
            }

            fch.doCalculations(fundDetSet, "usd");

            commitment = fch.getTotActualComm().doubleValue();
            disbursement = fch.getTotActualDisb().doubleValue();
            expenditure = fch.getTotActualExp().doubleValue();


        } catch (Exception ex1) {
            String ggg="gadfg";
            //Add exception reporting
        }

        retVal = new FundingData(commitment, disbursement, expenditure);

        return retVal;
    }

}
