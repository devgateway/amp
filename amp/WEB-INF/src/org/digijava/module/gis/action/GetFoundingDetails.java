package org.digijava.module.gis.action;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ecs.xml.XML;
import org.apache.ecs.xml.XMLDocument;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.module.aim.ar.util.ReportsUtil;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.fundingpledges.dbentity.*;
import org.digijava.module.gis.dbentity.GisMap;
import org.digijava.module.gis.dbentity.GisMapSegment;
import org.digijava.module.gis.util.*;

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

            //Funding date range
            String fromYear = request.getParameter("fromYear");
            Calendar fStartDate = null;
            if (fromYear != null) {
                fStartDate = Calendar.getInstance();
                fStartDate.set(Integer.parseInt(fromYear), 0, 1, 0, 0, 0);

            }

            String toYear = request.getParameter("toYear");
            Calendar fEndDate = null;
            if (toYear != null) {
                fEndDate = Calendar.getInstance();
                fEndDate.set(Integer.parseInt(toYear), 11, 31, 23, 59, 59);

            }
            
            String selectedDonorGroupsStr = request.getParameter("selectedDonorGroups");
            String [] selectedDonorGroup =null;
            if(selectedDonorGroupsStr !=null && !selectedDonorGroupsStr.equalsIgnoreCase("undefined")){
            	selectedDonorGroup = selectedDonorGroupsStr.split(",");
            }
            
            String selectedDonorTypesStr = request.getParameter("selectedDonorTypes");
            String [] selectedDonorTypes =null;
            if(selectedDonorTypesStr !=null && !selectedDonorTypesStr.equalsIgnoreCase("undefined")){
            	selectedDonorTypes = selectedDonorTypesStr.split(",");
            }
            
            String selectedDonnorAgencyStr = request.getParameter("selectedDonnorAgency");
            String [] selectedDonnorAgency =null;
            if(selectedDonnorAgencyStr !=null && !selectedDonnorAgencyStr.equalsIgnoreCase("undefined")){
            	selectedDonnorAgency = selectedDonnorAgencyStr.split(",");
            }
            
            String selectedNatPlanObjStr = request.getParameter("selectedNatPlanObj");
            String [] selectedNatPlanObj =null;
            if(selectedNatPlanObjStr !=null && !selectedNatPlanObjStr.equalsIgnoreCase("undefined")){
            	selectedNatPlanObj = selectedNatPlanObjStr.split(",");
            }
            
            String selectedPrimaryProgramsStr = request.getParameter("selectedPrimaryPrograms");
            String [] selectedPrimaryPrograms =null;
            if(selectedPrimaryProgramsStr !=null && !selectedPrimaryProgramsStr.equalsIgnoreCase("undefined")){
            	selectedPrimaryPrograms = selectedPrimaryProgramsStr.split(",");
            }
            
            String selectedSecondaryProgramsStr = request.getParameter("selectedSecondaryPrograms");
            String [] selectedSecondaryPrograms =null;
            if(selectedSecondaryProgramsStr !=null && !selectedSecondaryProgramsStr.equalsIgnoreCase("undefined")){
            	selectedSecondaryPrograms = selectedSecondaryProgramsStr.split(",");
            }
            
            String selectedSectorsStr = request.getParameter("selectedSectors");
            String [] selectedSectors =null;
            if(selectedSectorsStr !=null && !selectedSectorsStr.equalsIgnoreCase("undefined")){
            	selectedSectors = selectedSectorsStr.split(",");
            }
            
            String selectedSecondarySectorsStr = request.getParameter("selectedSecondarySectors");
            String [] selectedSecondarySectors =null;
            if(selectedSecondarySectorsStr !=null && !selectedSecondarySectorsStr.equalsIgnoreCase("undefined")){
            	selectedSecondarySectors = selectedSecondarySectorsStr.split(",");
            }
            
            String selectedTertiarySectorsStr = request.getParameter("selectedTertiarySectors");
            String [] selectedTertiarySectors =null;
            if(selectedTertiarySectorsStr !=null && !selectedTertiarySectorsStr.equalsIgnoreCase("undefined")){
            	selectedTertiarySectors = selectedTertiarySectorsStr.split(",");
            }

            CoordinateRect rect = gisUtil.getMapRect(map);

            if (rect != null) {
                MapColorScheme colorScheme = GisUtil.getActiveColorScheme(request);
                if (action.equalsIgnoreCase(GisUtil.ACTION_PAINT_MAP)) {
                    response.setContentType("image/png");

                    BufferedImage graph = new BufferedImage(canvasWidth,
                            canvasHeight,
                            BufferedImage.TYPE_INT_ARGB);

                    Graphics2D g2d = graph.createGraphics();

                    g2d.setBackground(new Color(0, 0, 100, 255));

                    g2d.clearRect(0, 0, canvasWidth, canvasHeight);

                    boolean fill = true;
                    if (request.getParameter("noFill") != null) {
                        fill = false;
                    }

                    if (map != null) {

	                    gisUtil.addDataToImage(g2d,
	                                           map.getSegments(),
	                                           -1,
	                                           canvasWidth, canvasHeight,
	                                           rect.getLeft(), rect.getRight(),
	                                           rect.getTop(), rect.getBottom(),
	                                           fill, false, colorScheme);
	
	                    if (request.getParameter("noCapt") == null) {
	
	                        gisUtil.addCaptionsToImage(g2d,
	                                map.getSegments(),
	                                canvasWidth, canvasHeight,
	                                rect.getLeft(), rect.getRight(),
	                                rect.getTop(), rect.getBottom());
	                    }
                    } else {
                    	gisUtil.getNoDataImage(g2d, canvasWidth, canvasHeight, "No map data in the database");
                    }
                    g2d.dispose();

                    RenderedImage ri = graph;

                    ImageIO.write(ri, "png", sos);

                    graph.flush();

                } else if (action.equalsIgnoreCase(GisUtil.
                        ACTION_GET_IMAGE_MAP)) {
                    response.setContentType("text/xml");

                    List mapDataSegments = map.getSegments();

                    XMLDocument xml = gisUtil.getImageMap(mapDataSegments,
                            Integer.parseInt(mapLevel),
                            20,
                            canvasWidth,
                            canvasHeight, rect.getLeft(),
                            rect.getRight(), rect.getTop(),
                            rect.getBottom());

//                    String imageMapCode = xml.toString();

                    xml.output(sos);
                } else if (action.equalsIgnoreCase("getSelectedFilterMap")) {
                        Object[] filteredData = (Object[]) request.getSession().getAttribute("GIS_FILTER_RESULTS");
                        Object[] fundingList = (Object[])filteredData[1];
                        GisFilterForm filterForm = (GisFilterForm)filteredData[0];

                        List segmentDataList = new ArrayList();
                        BigDecimal min = null;
                        BigDecimal max = null;
                        Map fundingLocationMap = (Map) fundingList[0];
                        Iterator locFoundingMapIt = fundingLocationMap.keySet().iterator();
                        while (locFoundingMapIt.hasNext()) {
                            String key = (String) locFoundingMapIt.next();
                            if (RMMapCalculationUtil.isRegion(map, key)) {
                                FundingData fData = (FundingData) fundingLocationMap.get(key);
                                SegmentData segmentData = new SegmentData();
                                segmentData.setSegmentCode(key);

                                BigDecimal selValue = null;

                                String fundingType = filterForm.getFundingType() != null ? filterForm.getFundingType() : "commitment";
                                if (fundingType.equals("commitment")) {
                                    selValue = fData.getCommitment();
                                } else if (fundingType.equals("disbursement")) {
                                    selValue = fData.getDisbursement();
                                } else if (fundingType.equals("expenditure")) {
                                    selValue = fData.getExpenditure();
                                }else{
                                    selValue = new BigDecimal("0");
                                }


                                segmentData.setSegmentValue(selValue.toString());

                                //Hilight only if value is not 0
                                if (selValue.longValue() != 0l) {
                                    if (min == null) {
                                        min = selValue;
                                        max = selValue;
                                    }

                                    if (selValue.compareTo(min) < 0) {
                                        min = selValue;
                                    }

                                    if (selValue.compareTo(max) > 0) {
                                        max = selValue;
                                    }

                                    segmentDataList.add(segmentData);
                                }
                            }
                        }

                        if (min == null) {
                            min = new BigDecimal(0);
                            max = new BigDecimal(0);
                        }




                    List hilightData = RMMapCalculationUtil.prepareHilightSegments(segmentDataList,map, new Double(min.doubleValue()), new Double(max.doubleValue()),colorScheme);

                    BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,BufferedImage.TYPE_INT_ARGB);

                    Graphics2D g2d = graph.createGraphics();

                    g2d.setBackground(new Color(0, 0, 100, 255));

                    g2d.clearRect(0, 0, canvasWidth, canvasHeight);

                    gisUtil.addDataToImage(g2d,map.getSegments(), hilightData,null,canvasWidth, canvasHeight,rect.getLeft(), rect.getRight(),
                            rect.getTop(), rect.getBottom(), true, false, colorScheme);

                    gisUtil.addCaptionsToImage(g2d,
                            map.getSegments(),
                            canvasWidth, canvasHeight,
                            rect.getLeft(), rect.getRight(),
                            rect.getTop(), rect.getBottom());

                    g2d.dispose();

                    RenderedImage ri = graph;

                    ImageIO.write(ri, "png", sos);

                    graph.flush();

                } else if (action.equalsIgnoreCase("getDataForSectorFin")) {
                	response.setContentType("image/png");
                	
                	/**
                	 * Dare version start
                	 */
                	Set primarySectors = Util.getSelectedObjects(AmpSector.class, selectedSectors);
            		Set secondarySectors = Util.getSelectedObjects(AmpSector.class, selectedSecondarySectors);
                    Set tertiarySectors = Util.getSelectedObjects(AmpSector.class, selectedTertiarySectors);
                    
                    Set natPlanObj = Util.getSelectedObjects(AmpTheme.class, selectedNatPlanObj);
            		Set primaryPrograms = Util.getSelectedObjects(AmpTheme.class, selectedPrimaryPrograms);
            		Set secondaryPrograms = Util.getSelectedObjects(AmpTheme.class, selectedSecondaryPrograms);
            		
            		// Calendar part is missing, should be included
            		
            		Set donorTypes = null;
            		if (selectedDonorTypes != null && selectedDonorTypes.length > 0) {
            			donorTypes=new HashSet();
            			for (int i = 0; i < selectedDonorTypes.length; i++) {
            				Long id = Long.parseLong("" + selectedDonorTypes[i]);
            				AmpOrgType type = org.digijava.module.aim.util.DbUtil.getAmpOrgType(id);
            				if (type != null){
            					donorTypes.add(type);
            				}            					
            			}
            		}
            		
            		Set donorGroups = null;
            		if (selectedDonorGroup != null && selectedDonorGroup.length > 0) {
            			donorGroups=new HashSet();
            			for (int i = 0; i < selectedDonorGroup.length; i++) {
            				Long id = Long.parseLong("" + selectedDonorGroup[i]);
            				AmpOrgGroup grp = org.digijava.module.aim.util.DbUtil.getAmpOrgGroup(id);
            				if (grp != null)
            					donorGroups.add(grp);
            			}
            		}
            		
            		//Set donorAgencies = ReportsUtil.processSelectedFilters(selectedDonnorAgency, AmpOrganisation.class);
            		
            		Collection<AmpSector> sectors = null; // with current GIS filter sectors are not differentiated, I think they should be re-made
                    if(primarySectors!=null || secondarySectors!=null || tertiarySectors !=null){
                    	sectors = new HashSet<AmpSector>();
                    }
                    if(primarySectors!=null){
                    	sectors.addAll(primarySectors);
                    }
                    if(secondarySectors!= null ){
                    	sectors.addAll(secondarySectors);
                    }
                    if(tertiarySectors!=null){
                    	sectors.addAll(tertiarySectors);
                    }
                    

            		Collection<AmpTheme> programs = null; // with current GIS filter themes are not differentiated, I think they should be re-made
                    if(natPlanObj!=null || primaryPrograms!=null || secondaryPrograms !=null){
                    	programs = new HashSet<AmpTheme>();
                    }
                    if(natPlanObj!=null){
                    	programs.addAll(natPlanObj);
                    }
                    if(primaryPrograms!= null ){
                    	programs.addAll(primaryPrograms);
                    }
                    if(secondaryPrograms!=null){
                    	programs.addAll(secondaryPrograms);
                    }
            		
            		TeamMember tm = null;
                    Long teamId = null;

                    boolean curWorkspaceOnly = request.getParameter("curWorkspaceOnly") != null && request.getParameter("curWorkspaceOnly").equalsIgnoreCase("true");
                    boolean includeNatProjects = request.getParameter("natProjects") != null &&
                            request.getParameter("natProjects").equalsIgnoreCase("true") ? true : false;

                    if (curWorkspaceOnly) {
                        tm = (TeamMember)request.getSession().getAttribute("currentMember");
                        if (tm != null) {
                            AmpTeam team = TeamUtil.getTeamByName(tm.getTeamName());
                            teamId = team.getAmpTeamId();
                        }
                    }
                    
                    int mapMode =  (request.getParameter("mapMode") != null && request.getParameter("mapMode").
                            equalsIgnoreCase("pledgesData")) ? DbUtil.MAP_MODE_PLEDGES : DbUtil.MAP_MODE_ACTIVITIES;
                    
                    
                    String defaultCountryISO = FeaturesUtil.getDefaultCountryIso();
                    Collection <AmpCategoryValueLocations> locations = DbUtil.getSelectedLocations(defaultCountryISO, Integer.parseInt(mapLevel));


                    List segmentDataList = new ArrayList();
                    BigDecimal min = null;
                    BigDecimal max = null;
                    if (mapMode == DbUtil.MAP_MODE_PLEDGES) {

                        //getPledgesByLocationsForPrograms
                    	
                        //Object[] pledgeFundings = DbUtil.getPledgeFundings(sectors, programs, null, locations, null, fStartDate.getTime(), fEndDate.getTime());

                        Object[] fundingList = null;

//                        if (sectorQueryType != DbUtil.SELECT_PROGRAM) {
//                            List <FundingPledgesSector> selPledges = DbUtil.getPledgesBySector(secId, sectorQueryType);
//                            fundingList = getPledgesByLocations (selPledges,
//                                Integer.parseInt(mapLevel),
//                                fStartDate.getTime(),
//                                fEndDate.getTime(), donorId);
//                        } else {
////                            List <FundingPledgesProgram> selPledges = DbUtil.getPledgesByProgram(prgId);
////                            fundingList = getPledgesByLocationsForPrograms (selPledges,Integer.parseInt(mapLevel),fStartDate.getTime(),fEndDate.getTime(), donorId);
//
//                        }

                        request.getSession().setAttribute("AMP_FUNDING_DATA", fundingList);

                        Map fundingLocationMap = (Map) fundingList[0];
                        
                        Iterator locFoundingMapIt = fundingLocationMap.keySet().iterator();


                        while (locFoundingMapIt.hasNext()) {
                            String key = (String) locFoundingMapIt.next();
                            FundingData fData = (FundingData) fundingLocationMap.get(key);
                            SegmentData segmentData = new SegmentData();
                            segmentData.setSegmentCode(key);

                            BigDecimal selValue = null;
                            selValue = fData.getCommitment();


                            segmentData.setSegmentValue(selValue.toString());

                            if (min == null) {
                                min = selValue;
                                max = selValue;
                            }

                            if (selValue.compareTo(min) < 0) {
                                min = selValue;
                            }

                            if (selValue.compareTo(max) > 0) {
                                max = selValue;
                            }

                            segmentDataList.add(segmentData);
                        }

                        if (min == null) {
                            min = new BigDecimal(0);
                            max = new BigDecimal(0);
                        }
                    } else {

                        String fundingType = request.getParameter("fundingType");
                        Object[] fundingList = (Object[]) request.getSession().getAttribute("GIS_FILTER_RESULTS");

                        Map fundingLocationMap = (Map) fundingList[0];

                        Iterator locFoundingMapIt = fundingLocationMap.keySet().iterator();


                        while (locFoundingMapIt.hasNext()) {
                            String key = (String) locFoundingMapIt.next();
                            FundingData fData = (FundingData) fundingLocationMap.get(key);
                            SegmentData segmentData = new SegmentData();
                            segmentData.setSegmentCode(key);

                            BigDecimal selValue = null;

                            if (fundingType.equals("commitment")) {
                                selValue = fData.getCommitment();
                            } else if (fundingType.equals("disbursement")) {
                                selValue = fData.getDisbursement();
                            } else if (fundingType.equals("expenditure")) {
                                selValue = fData.getExpenditure();
                            }else{
                                selValue = new BigDecimal("0");
                            }


                            segmentData.setSegmentValue(selValue.toString());

                            //Hilight only if value is not 0
                            if (selValue.longValue() != 0l) {
                                if (min == null) {
                                    min = selValue;
                                    max = selValue;
                                }

                                if (selValue.compareTo(min) < 0) {
                                    min = selValue;
                                }

                                if (selValue.compareTo(max) > 0) {
                                    max = selValue;
                                }

                                segmentDataList.add(segmentData);
                            }
                        }

                        if (min == null) {
                            min = new BigDecimal(0);
                            max = new BigDecimal(0);
                        }
                    }
                    
                    List hilightData = RMMapCalculationUtil.prepareHilightSegments(segmentDataList,map, new Double(min.doubleValue()), new Double(max.doubleValue()),colorScheme);

                    BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight,BufferedImage.TYPE_INT_ARGB);

                    Graphics2D g2d = graph.createGraphics();

                    g2d.setBackground(new Color(0, 0, 100, 255));

                    g2d.clearRect(0, 0, canvasWidth, canvasHeight);

                    gisUtil.addDataToImage(g2d,map.getSegments(), hilightData,null,canvasWidth, canvasHeight,rect.getLeft(), rect.getRight(),
                            rect.getTop(), rect.getBottom(), true, false, colorScheme);

                    gisUtil.addCaptionsToImage(g2d,
                            map.getSegments(),
                            canvasWidth, canvasHeight,
                            rect.getLeft(), rect.getRight(),
                            rect.getTop(), rect.getBottom());

                    g2d.dispose();

                    RenderedImage ri = graph;

                    ImageIO.write(ri, "png", sos);

                    graph.flush();


                } else if (action.equalsIgnoreCase("getFinansialDataXML")) {
                    response.setCharacterEncoding("UTF-8");
                        response.setContentType("text/xml");
                        Object[] filteredData = (Object[]) request.getSession().getAttribute("GIS_FILTER_RESULTS");
                        Object[] fundingList = (Object[])filteredData[1];
//                        request.getSession().removeAttribute("GIS_FILTER_RESULTS");
                        Map fundingLocationMap = (Map) fundingList[0];
                        FundingData totalFunding = null;
                        if (fundingList.length>1){
	                        totalFunding = (FundingData) fundingList[1];
                        }
                        XMLDocument segmendDataInfo = new XMLDocument();
                        segmendDataInfo.setCodeset("UTF-8");

                        String numberFormat = FeaturesUtil.getGlobalSettingValue(
                                GlobalSettingsConstants.NUMBER_FORMAT);
                        //NumberFormat formatter = new DecimalFormat(numberFormat);

                        XML root = new XML("funding");
                        
                        if (totalFunding==null) {
                        	root.addAttribute("totalCommitment",FormatHelper.formatNumber(0));
                            root.addAttribute("totalDisbursement",FormatHelper.formatNumber(0));
                            root.addAttribute("totalExpenditure",FormatHelper.formatNumber(0));
                            root.addAttribute("totalPlannedDisbursement",FormatHelper.formatNumber(0));
						} else {
							root.addAttribute("totalCommitment",FormatHelper.formatNumber(totalFunding.getCommitment().doubleValue()));
	                        root.addAttribute("totalDisbursement",FormatHelper.formatNumber(totalFunding.getDisbursement().doubleValue()));
	                        root.addAttribute("totalExpenditure",FormatHelper.formatNumber(totalFunding.getExpenditure().doubleValue()));
	                        root.addAttribute("totalPlannedDisbursement",FormatHelper.formatNumber(totalFunding.getPlannedDisbursement().doubleValue()));
						}
                        
                        segmendDataInfo.addElement(root);
                        Iterator locFoundingMapIt = fundingLocationMap.keySet().iterator();
                        while (locFoundingMapIt.hasNext()) {
                            String key = (String) locFoundingMapIt.next();
                            FundingData ammount = (FundingData) fundingLocationMap.
                                                  get(key);
                            XML regionData = new XML("region");
                            regionData.addAttribute("reg-code", key);
                            regionData.addAttribute("fundingCommitment",FormatHelper.formatNumber(ammount.getCommitment().doubleValue()));
                            regionData.addAttribute("fundingDisbursement",FormatHelper.formatNumber(ammount.getDisbursement().doubleValue()));
                            regionData.addAttribute("fundingExpenditure",FormatHelper.formatNumber(ammount.getExpenditure().doubleValue()));
                            regionData.addAttribute("fundingPlannedDisbursement",FormatHelper.formatNumber(ammount.getPlannedDisbursement().doubleValue()));
                            root.addElement(regionData);
                        }
                        segmendDataInfo.output(sos);
                } else if (action.equalsIgnoreCase("getSectorDonorsXML")) {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("text/xml");
                    String secIdStr = request.getParameter("sectorId");
                    Long secId = new Long(secIdStr);

                    XMLDocument donorsDoc = new XMLDocument();
                    donorsDoc.setCodeset("UTF-8");

                    XML root = new XML("donors");
                    donorsDoc.addElement(root);


                    List secDonorList = DbUtil.getSectorFoundingDonors(secId);
                    Iterator donorNameIterator = secDonorList.iterator();

                    while (donorNameIterator.hasNext()) {
                        Object[] donorName = (Object[]) donorNameIterator.next();
                        XML don = new XML("donor");
                        don.addAttribute("name", (String) donorName[0]);
                        don.addAttribute("id", (Long) donorName[1]);
                        root.addElement(don);

                    }
                    donorsDoc.output(sos);
                        
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
                        Double activityFound = DbUtil.getActivityFoundings(
                                ampActivity.
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

                            AmpActivityLocation loc = (AmpActivityLocation)
                                    locIt.next();

                            locationIdObjectMap.put(loc.getLocation().
                                    getAmpLocationId(),
                                    loc.getLocation());

                            if (locationFoundMap.containsKey(loc.getLocation().
                                    getAmpLocationId())) {
                                Double existingVal = (Double) locationFoundMap.
                                        get(loc.
                                            getLocation().getAmpLocationId());
                                locationFoundMap.put(loc.getLocation().
                                        getAmpLocationId(),
                                        existingVal +
                                        addAmmount *
                                        loc.getLocationPercentage() /
                                        100);
                            } else {
                                locationFoundMap.put(loc.getLocation().
                                        getAmpLocationId(),
                                        addAmmount *
                                        loc.getLocationPercentage() /
                                        100);
                            }
                        }

                    }

                    List segmentDataList = new ArrayList();
                    Iterator locFoundingMapIt = locationFoundMap.keySet().
                                                iterator();
                    while (locFoundingMapIt.hasNext()) {
                        Long key = (Long) locFoundingMapIt.next();
                        AmpLocation loc = (AmpLocation) locationIdObjectMap.get(
                                key);
                        Double ammount = (Double) locationFoundMap.get(key);
                        Object[] dataStore = new Object[2];
                        dataStore[0] = loc;
                        dataStore[1] = ammount;

                        //                        locDataList.add(dataStore);


                        if (loc.getRegion() != null) {
                            SegmentData segmentData = new SegmentData();
                            segmentData.setSegmentCode(loc.getRegion());
                            Double PercentFromTotal = ammount / totalFund * 100;
                            segmentData.setSegmentValue(PercentFromTotal.
                                    toString());
                            segmentDataList.add(segmentData);
                        }
                    }

                    List hilightData = RMMapCalculationUtil.prepareHilightSegments(segmentDataList,
                            map, new Double(0), new Double(100), colorScheme);

                    BufferedImage graph = new BufferedImage(canvasWidth,
                            canvasHeight,
                            BufferedImage.TYPE_INT_ARGB);

                    Graphics2D g2d = graph.createGraphics();

                    g2d.setBackground(new Color(0, 0, 100, 255));

                    g2d.clearRect(0, 0, canvasWidth, canvasHeight);

                    if (map != null) {
                    gisUtil.addDataToImage(g2d,
                                           map.getSegments(),
                                           hilightData, null,
                                           canvasWidth, canvasHeight,
                                           rect.getLeft(), rect.getRight(),
                                           rect.getTop(), rect.getBottom(), true, false, colorScheme);

                    gisUtil.addCaptionsToImage(g2d,
                                               map.getSegments(),
                                               canvasWidth, canvasHeight,
                                               rect.getLeft(), rect.getRight(),
                                               rect.getTop(), rect.getBottom());
                    } else {
                    	gisUtil.getNoDataImage(g2d, canvasWidth, canvasHeight, "No map data in the database");
                    }

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
                    if (indId.intValue() > 0) {
                        selIndicator = IndicatorUtil.getIndicator(indId);
                    }
                    //Get segments with funding for dashed paint map
                    List secFundings = null;

                    if (secId.longValue() > 0l) {
                        secFundings = DbUtil.getSectorFoundings(secId);
                    } else {
                        secFundings = new ArrayList();
                    }

                    Object[] fundingList = getFundingsByLocations(secFundings,
                            Integer.parseInt(mapLevel),
                            fStartDate.getTime(),
                            fEndDate.getTime());
                    Map fundingLocationMap = (Map) fundingList[0];

                    List segmentDataDasheList = new ArrayList();

                    Iterator locFoundingMapIt = fundingLocationMap.keySet().
                                                iterator();
                    while (locFoundingMapIt.hasNext()) {
                        String key = (String) locFoundingMapIt.next();
                        SegmentData segmentData = new SegmentData();
                        segmentData.setSegmentCode(key);
                        segmentData.setSegmentValue("100");
                        segmentDataDasheList.add(segmentData);
                    }

                    List hilightDashData = prepareDashSegments(
                            segmentDataDasheList,
                            new ColorRGB(0, 0, 0), map);

                    List inds = null;
                    if (indYear != null && !indYear.equals("-1")) {

                        String startDateStr = null;
                        String endDateStr = null;

                        if (indYear.startsWith("-")) {
                            startDateStr = indYear.substring(0,
                                    indYear.indexOf("-", 2));
                        } else {
                            startDateStr = indYear.substring(0,
                                    indYear.indexOf("-"));
                        }

                        if (indYear.indexOf("--") > -1) {
                            endDateStr = indYear.substring(indYear.indexOf("--") +
                                    1);
                        } else {
                            endDateStr = indYear.substring(indYear.indexOf("-") +
                                    1);
                        }

                        DateInterval datInt = new DateInterval(new Date(new
                                Long(startDateStr).longValue()),
                                new Date(new Long(endDateStr).longValue()));

                        inds = DbUtil.getIndicatorValuesForSectorIndicator(
                                secId, indId,
                                datInt, subgroupId, Integer.parseInt(mapLevel));

                        /*
                         inds = DbUtil.getIndicatorValuesForSectorIndicator(secId, indId,
                         indicatorYear, subgroupId, Integer.parseInt(mapLevel));*/
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

                        //.sourceName

                        AmpIndicatorSource srcObj = (AmpIndicatorSource)indData[2];
                        String srcStr = (String)indData[3];
                        String src = null;

                        if (srcStr != null && srcStr.trim().length() > 0){
                            src = srcStr;
                        }


                        if (RMMapCalculationUtil.isRegion(map, segmentCode) &&
                            !regSet.contains(segmentCode)) {

                            SegmentData indHilightData = new SegmentData();
                            indHilightData.setSegmentCode(segmentCode);
                            indHilightData.setSegmentValue(indValue.toString());
                            indHilightData.setIndicatorSrcName(src);

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

                        if (selIndicator.getUnit() != null &&
                            selIndicator.getUnit().trim().length() > 0) {
                            indUnit = selIndicator.getUnit();
                        } else {
                            indUnit = "N/A";
                        }

                        request.getSession().setAttribute("AMP_INDICATOR_UNIT",indUnit);
                        request.getSession().setAttribute("AMP_INDICATOR_VALUES", segmentDataList);
                    }

                    List hilightData = RMMapCalculationUtil.prepareHilightSegments(segmentDataList,
                            map, min, max, colorScheme);

                    BufferedImage graph = new BufferedImage(canvasWidth,
                            canvasHeight,
                            BufferedImage.TYPE_INT_ARGB);

                    Graphics2D g2d = graph.createGraphics();

                    g2d.setBackground(new Color(0, 0, 100, 255));

                    g2d.clearRect(0, 0, canvasWidth, canvasHeight);

                    if (map != null) {
                    gisUtil.addDataToImage(g2d,
                                           map.getSegments(),
                                           hilightData,
                                           hilightDashData,
                                           canvasWidth, canvasHeight,
                                           rect.getLeft(), rect.getRight(),
                                           rect.getTop(), rect.getBottom(), true, false, colorScheme);

                    gisUtil.addCaptionsToImage(g2d,
                                               map.getSegments(),
                                               canvasWidth, canvasHeight,
                                               rect.getLeft(), rect.getRight(),
                                               rect.getTop(), rect.getBottom());
                    } else {
                    	gisUtil.getNoDataImage(g2d, canvasWidth, canvasHeight, "No map data in the database");
                    }

                    g2d.dispose();

                    RenderedImage ri = graph;

                    ImageIO.write(ri, "png", sos);

                    graph.flush();

                } else if (action.equalsIgnoreCase("getSectorDataXML")) {
                    response.setContentType("text/xml");
                    String secIdStr = request.getParameter("sectorId");

                    Long secId = new Long(secIdStr);

                    List secFundings = DbUtil.getSectorFoundings(secId);

                    Object[] fundingList = getFundingsByLocations(secFundings,
                            Integer.parseInt(mapLevel),
                            fStartDate.getTime(),
                            fEndDate.getTime());

                    Map fundingLocationMap = (Map) fundingList[0];
                    FundingData totalFunding = (FundingData) fundingList[1];

                    XMLDocument segmendDataInfo = new XMLDocument();

                    String numberFormat = FeaturesUtil.getGlobalSettingValue(
                            GlobalSettingsConstants.NUMBER_FORMAT);
                    //NumberFormat formatter = new DecimalFormat(numberFormat);

                    XML root = new XML("funding");

                    root.addAttribute("totalCommitment",FormatHelper.formatNumber(totalFunding.getCommitment().doubleValue()));
                    root.addAttribute("totalDisbursement",FormatHelper.formatNumber(totalFunding.getDisbursement().doubleValue()));
                    root.addAttribute("totalExpenditure",FormatHelper.formatNumber(totalFunding.getExpenditure().doubleValue()));
                    root.addAttribute("totalPlannedDisbursement",FormatHelper.formatNumber(totalFunding.getPlannedDisbursement().doubleValue()));

                    segmendDataInfo.addElement(root);
                    Iterator locFoundingMapIt = fundingLocationMap.keySet().iterator();
                    while (locFoundingMapIt.hasNext()) {
                        String key = (String) locFoundingMapIt.next();
                        FundingData ammount = (FundingData) fundingLocationMap.get(key);
                        XML regionData = new XML("region");
                        regionData.addAttribute("reg-code", key);
                        regionData.addAttribute("fundingCommitment",FormatHelper.formatNumber(ammount.getCommitment().doubleValue()));
                        regionData.addAttribute("fundingDisbursement", FormatHelper.formatNumber(ammount.getDisbursement().doubleValue()));
                        regionData.addAttribute("fundingExpenditure",FormatHelper.formatNumber(ammount.getExpenditure().doubleValue()));
                        regionData.addAttribute("fundingPlannedDisbursement",FormatHelper.formatNumber(ammount.getPlannedDisbursement().doubleValue()));
                        root.addElement(regionData);
                    }
                    segmendDataInfo.output(sos);
                } else if (action.equalsIgnoreCase("getAvailIndicatorSubgroups")) {
                    response.setContentType("text/xml");

                    String secIdStr = request.getParameter("sectorId");
                    Long secId = new Long(secIdStr);

                    String indIdStr = request.getParameter("indicatorId");
                    Long indId = new Long(indIdStr);

                    List availSubgroups = DbUtil.
                                          getAvailSubgroupsForSectorIndicator(
                            secId, indId, Integer.parseInt(mapLevel));

                    XMLDocument availSubGroupXmlDoc = new XMLDocument();
                    XML root = new XML("availSubgroups");

                    availSubGroupXmlDoc.addElement(root);

                    Iterator subIt = availSubgroups.iterator();

                    while (subIt.hasNext()) {
                        Object[] subData = (Object[]) subIt.next();
                        Long subId = (Long) subData[0];
                        String subName = (String) subData[1];

                        subName = subName.replace("<", "&lt;");
                        subName = subName.replace(">", "&gt;");

                        XML subNode = new XML("subgroup");
                        subNode.addAttribute("id", subId);
                        subNode.addAttribute("name", subName);
                        root.addElement(subNode);
                    }
                    availSubGroupXmlDoc.output(sos);
                } else if (action.equalsIgnoreCase("getAvailIndicatorYears")) {
                    response.setContentType("text/xml");

                    String secIdStr = request.getParameter("sectorId");
                    Long secId = new Long(secIdStr);

                    String indIdStr = request.getParameter("indicatorId");
                    Long indId = new Long(indIdStr);

                    //List availYears = DbUtil.getAvailYearsForSectorIndicator(secId, indId, Integer.parseInt(mapLevel), subgroupId);

                    List availRanges = DbUtil.
                                       getAvailDateIntervalsForSectorIndicator(
                            secId, indId, Integer.parseInt(mapLevel),
                            subgroupId);

                    List availRangeObjects = new ArrayList();
                    if (availRanges != null && !availRanges.isEmpty()) {
                        Iterator rngIt = availRanges.iterator();
                        while (rngIt.hasNext()) {
                            Object[] obj = (Object[]) rngIt.next();
                            Date startDat = (Date) obj[0];
                            Date endDat = (Date) obj[1];
                            DateInterval dateInterval = new DateInterval(
                                    startDat, endDat);
                            availRangeObjects.add(dateInterval);
                        }
                    }

                    XMLDocument availYearsXmlDoc = new XMLDocument();

                    XML root = new XML("availIntervals");
                    availYearsXmlDoc.addElement(root);

                    Iterator yearIt = availRangeObjects.iterator();

                    while (yearIt.hasNext()) {
                        DateInterval year = (DateInterval) yearIt.next();
                        XML yearNode = new XML("interval");
                        yearNode.addAttribute("start-value", year.getStartTime());
                        yearNode.addAttribute("start-caption",year.getFormatedStartTime());
                        yearNode.addAttribute("end-value", year.getEndTime());
                        yearNode.addAttribute("end-caption",year.getFormatedEndTime());
                        root.addElement(yearNode);
                    }
                    availYearsXmlDoc.output(sos);
                } else if (action.equalsIgnoreCase("getIndicatorNamesXML")) {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("text/xml");
                    String secIdStr = request.getParameter("sectorId");
                    Long secId = new Long(secIdStr);

                    XMLDocument sectorIndicators = new XMLDocument();
                    sectorIndicators.setCodeset("UTF-8");

                    XML root = new XML("indicators");
                    sectorIndicators.addElement(root);

                    //Add indicators
                    List secIndicatorList = DbUtil.getIndicatorsForSector(secId,
                            Integer.parseInt(mapLevel));
                    Iterator indNameIterator = secIndicatorList.iterator();

                    while (indNameIterator.hasNext()) {
                        Object[] indName = (Object[]) indNameIterator.next();
                        XML ind = new XML("indicator");
                        ind.addAttribute("name", (String) indName[1]);
                        ind.addAttribute("id", (Long) indName[0]);
                        ind.addAttribute("enbl", ((Boolean) indName[2]));
                        root.addElement(ind);

                    }
                    sectorIndicators.output(sos);
                } else if (action.equalsIgnoreCase("getIndicatorValues")) {

                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("text/xml");

                    String indicatorUnit = (String) request.getSession().
                                           getAttribute("AMP_INDICATOR_UNIT");
                    request.getSession().removeAttribute("AMP_INDICATOR_UNIT");

                    List indicatorVals = (List) request.getSession().
                                         getAttribute("AMP_INDICATOR_VALUES");

                    XMLDocument indicators = new XMLDocument();
                    XML root = new XML("indicatorData");
                    root.addAttribute("indUnit", indicatorUnit);
                    indicators.addElement(root);

                    if (indicatorVals != null && !indicatorVals.isEmpty()) {
                        request.getSession().removeAttribute(
                                "AMP_INDICATOR_VALUES");
                        Iterator<SegmentData> it = indicatorVals.iterator();
                        while (it.hasNext()) {
                            SegmentData sd = it.next();
                            XML indVal = new XML("indVal");
                            indVal.addAttribute("val", sd.getSegmentValue());
                            indVal.addAttribute("reg", sd.getSegmentCode());
                            if (sd.getIndicatorSrcName() != null) {
                                indVal.addAttribute("src",
                                        sd.getIndicatorSrcName());
                            } else {
                                indVal.addAttribute("src","Unknown");
                            }
                            root.addElement(indVal);
                        }
                    }

                    indicators.output(sos);
                }  else if (action.equalsIgnoreCase("getSectorTree")) {
                    response.setContentType("text/xml");
                    XMLDocument tree = null;
                    int treeMode = (request.getParameter("mode") != null && request.getParameter("mode").
                            equalsIgnoreCase("pledgesData")) ? DbUtil.SECTORS_FOR_PLEDGES : DbUtil.SECTORS_FOR_ACTIVITIES;

                    ServletContext ampContext = getServlet().getServletContext();
                    AmpTreeVisibility ampTreeVisibility = (AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");

                    boolean showSecondaryScheme = FeaturesUtil.getFieldVisibility("Secondary Sector").isFieldActive(ampTreeVisibility);

                    tree = DbUtil.getAllSectorsAsHierarchyXML(treeMode, showSecondaryScheme, DbUtil.getGisSettings(request).getSectorSchemeFilterMode());
                    tree.output(sos);
                } else if (action.equalsIgnoreCase("filter")) {

                    String isRegSetStr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GIS_FUNDING_TYPE);
                    boolean isRegional = (isRegSetStr == null || isRegSetStr.trim().equalsIgnoreCase("donor"))?GisUtil.GIS_DONOR_FUNDINGS:GisUtil.GIS_REGIONAL_FUNDINGS;
                    boolean isPublic = request.getParameter("isPublic") == null?false:true;

                    if (!isPublic && request.getSession().getAttribute("currentMember") == null) {
                        isPublic = true;
                    }

                    GisFilterForm filterForm = GisUtil.parseFilterRequest (request);
                    Object[] filterResults = RMMapCalculationUtil.getAllFundingsFiltered(filterForm, isRegional, isPublic);
                    request.getSession().setAttribute("GIS_FILTER_RESULTS", new Object[] {filterForm, filterResults});

                }
            }  else if (map == null) {
            	response.setContentType("image/png");
            	BufferedImage graph = new BufferedImage(canvasWidth,
                        canvasHeight,
                        BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = graph.createGraphics();
                gisUtil.getNoDataImage(g2d, canvasWidth, canvasHeight, "No map data in the database");
                
                g2d.dispose();

                RenderedImage ri = graph;

                ImageIO.write(ri, "png", sos);

                graph.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sos.flush();
            sos.close();
        }
        return null;

    }



    private List prepareDashSegments(List segmentData, ColorRGB dashColor,
                                     GisMap map) {
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



    public static Object[] getFundingsByLocations(List activityList, int level,
            Date start, Date end) throws Exception {
    	return getFundingsByLocations(activityList, level, start, end, null);
    }

    public static Object[] getFundingsByLocations(List activityList, int level,
                                            Date start, Date end, Long donorId) throws
            Exception {

        Map locationFundingMap = new HashMap();
        FundingData totalFundingForSector = new FundingData();
        //Calculate total funding
        if (activityList != null) {
            Iterator<Object[]> actIt = activityList.iterator();
            while (actIt.hasNext()) {
                Object[] actData = actIt.next();
                AmpActivityVersion activity = (AmpActivityVersion) actData[0];
                Float percentsForSectorSelected = (Float) actData[1];

                if (percentsForSectorSelected != null) {

                FundingData totalFunding = getActivityTotalFundingInBaseCurrency(
                        activity, start, end, donorId);

                totalFundingForSector.setCommitment(totalFundingForSector.getCommitment().add(totalFunding.getCommitment().multiply(new BigDecimal((percentsForSectorSelected / 100f)))))  ;
                totalFundingForSector.setDisbursement(totalFundingForSector.getDisbursement().add(totalFunding.getDisbursement().multiply(new BigDecimal (percentsForSectorSelected/ 100f))));
                totalFundingForSector.setExpenditure(totalFundingForSector.getExpenditure().add(totalFunding.getExpenditure().multiply(new BigDecimal(percentsForSectorSelected / 100f))));

                FundingData fundingForSector = new FundingData();
                fundingForSector.setCommitment(totalFunding.getCommitment().multiply(new BigDecimal(percentsForSectorSelected / 100f)));
                fundingForSector.setDisbursement(totalFunding.getDisbursement().multiply(new BigDecimal(percentsForSectorSelected / 100f)));
                fundingForSector.setExpenditure(totalFunding.getExpenditure().multiply(new BigDecimal(percentsForSectorSelected / 100f)));

                Set locations = activity.getLocations();
                Iterator<AmpActivityLocation> locIt = locations.iterator();

                while (locIt.hasNext()) {
                    AmpActivityLocation loc = locIt.next();


                    //Region level
                    //if (loc.getLocation().getAmpRegion() != null && loc.getLocation().getZone()==null && loc.getLocationPercentage().floatValue() > 0.0f) {

                    if (loc.getLocationPercentage() != null &&
                        loc.getLocationPercentage().floatValue() > 0.0f &&
                        loc.getLocation().getRegionLocation() != null) {
                        if (level == GisMap.MAP_LEVEL_REGION &&
                            loc.getLocation().getRegionLocation().getParentCategoryValue().getEncodedValue().equals("Region")) {

                                String regCode = loc.getLocation().getRegionLocation().getName().trim();

                            if (locationFundingMap.containsKey(regCode)) {
                                FundingData existingVal = (FundingData) locationFundingMap.get(regCode);

                                FundingData newVal = new FundingData();
                                newVal.setCommitment(existingVal.getCommitment().add(fundingForSector.getCommitment().multiply(new BigDecimal(loc.getLocationPercentage() / 100f))));
                                newVal.setDisbursement(existingVal.getDisbursement().add(fundingForSector.getDisbursement().multiply(new BigDecimal(loc.getLocationPercentage() / 100f))));
                                newVal.setExpenditure(existingVal.getExpenditure().add(fundingForSector.getExpenditure().multiply(new BigDecimal(loc.getLocationPercentage() / 100f))));
                                locationFundingMap.put(regCode, newVal);
                            } else {
                                if (fundingForSector.getCommitment().floatValue() !=0f || fundingForSector.getDisbursement().floatValue() != 0f || fundingForSector.getExpenditure().floatValue() != 0f) {
                                    FundingData newVal = new FundingData();
                                    newVal.setCommitment(fundingForSector.getCommitment().multiply(new BigDecimal(loc.getLocationPercentage()/ 100f)));
                                    newVal.setDisbursement(fundingForSector.getDisbursement().multiply(new BigDecimal(loc.getLocationPercentage() / 100f)));
                                    newVal.setExpenditure(fundingForSector.getExpenditure().multiply(new BigDecimal(loc.getLocationPercentage()/ 100f)));
                                    locationFundingMap.put(regCode, newVal);
                                }
                            }
                        } else if (level == GisMap.MAP_LEVEL_DISTRICT &&
                                   loc.getLocation().getRegionLocation().getParentCategoryValue().getEncodedValue().equals("Zone")) {

                            //District level
                            //if (loc.getLocation().getAmpZone()!=null && loc.getLocationPercentage().floatValue() > 0.0f) {

                                String regCode = loc.getLocation().getRegionLocation().getName().trim();

                            if (locationFundingMap.containsKey(regCode)) {
                                FundingData existingVal = (FundingData)
                                        locationFundingMap.get(
                                                regCode);

                                FundingData newVal = new FundingData();
                                newVal.setCommitment(existingVal.getCommitment().add(fundingForSector.getCommitment().multiply(new BigDecimal(loc.getLocationPercentage()/100f))));
                                newVal.setDisbursement(existingVal.getDisbursement().add(fundingForSector.getDisbursement().multiply(new BigDecimal(loc.getLocationPercentage() / 100f))));
                                newVal.setExpenditure(existingVal.getExpenditure().add(fundingForSector.getExpenditure().multiply(new BigDecimal(loc.getLocationPercentage() / 100f))));
                                locationFundingMap.put(regCode, newVal);
                            } else {
                                if (fundingForSector.getCommitment().floatValue() !=
                                    0f ||
                                    fundingForSector.getDisbursement().
                                    floatValue() != 0f ||
                                    fundingForSector.getExpenditure().
                                    floatValue() != 0f) {

                                    FundingData newVal = new FundingData();
                                    newVal.setCommitment(fundingForSector.getCommitment().multiply(new BigDecimal(loc.getLocationPercentage()/ 100f)));
                                    newVal.setDisbursement(fundingForSector.getDisbursement().multiply(new BigDecimal(loc.getLocationPercentage()/100f)));
                                    newVal.setExpenditure(fundingForSector.getExpenditure().multiply(new BigDecimal(loc.getLocationPercentage()/100f)));
                                 locationFundingMap.put(regCode, newVal);
                                }
                            }
                        }
                    }

                }

                //    Set activiactivity.getFunding();
            }

        }

        }
        Object[] retVal = new Object[2];
        retVal[0] = locationFundingMap;
        retVal[1] = totalFundingForSector;
        return retVal;
    }

    public static Object[] getPledgesByLocations(List <FundingPledgesSector> pledgeSectorList, int level, Date start, Date end, Long donorId) throws
            Exception {

        Map locationFundingMap = new HashMap();
        FundingData totalFundingForSector = new FundingData();
        //Calculate total funding
        if (pledgeSectorList != null) {
            Iterator<FundingPledgesSector> pledgeIt = pledgeSectorList.iterator();
            while (pledgeIt.hasNext()) {
                FundingPledgesSector pledgeSec = pledgeIt.next();

                Float percentsForSectorSelected = pledgeSec.getSectorpercentage();

                if (percentsForSectorSelected != null) {

                    FundingData totalFunding = getActivityTotalFundingInBaseCurrencyForPledge(
                            pledgeSec.getPledgeid(), start, end, donorId);

                    totalFundingForSector.setCommitment(totalFundingForSector.getCommitment().add(totalFunding.getCommitment().multiply(new BigDecimal((percentsForSectorSelected / 100f)))))  ;
                    totalFundingForSector.setDisbursement(totalFundingForSector.getDisbursement().add(totalFunding.getDisbursement().multiply(new BigDecimal (percentsForSectorSelected/ 100f))));
                    totalFundingForSector.setExpenditure(totalFundingForSector.getExpenditure().add(totalFunding.getExpenditure().multiply(new BigDecimal(percentsForSectorSelected / 100f))));

                    FundingData fundingForSector = new FundingData();
                    fundingForSector.setCommitment(totalFunding.getCommitment().multiply(new BigDecimal(percentsForSectorSelected / 100f)));
                    fundingForSector.setDisbursement(totalFunding.getDisbursement().multiply(new BigDecimal(percentsForSectorSelected / 100f)));
                    fundingForSector.setExpenditure(totalFunding.getExpenditure().multiply(new BigDecimal(percentsForSectorSelected / 100f)));

                    List <FundingPledgesLocation>locations = PledgesEntityHelper.getPledgesLocations(pledgeSec.getPledgeid().getId());

                    Iterator<FundingPledgesLocation> locIt = locations.iterator();

                    while (locIt.hasNext()) {
                        FundingPledgesLocation loc = locIt.next();


                        //Region level
                        //if (loc.getLocation().getAmpRegion() != null && loc.getLocation().getZone()==null && loc.getLocationPercentage().floatValue() > 0.0f) {

                        if (loc.getLocationpercentage() != null &&
                                loc.getLocationpercentage().floatValue() > 0.0f &&
                                loc.getLocation() != null) {
                            if (level == GisMap.MAP_LEVEL_REGION &&
                                    loc.getLocation().getParentCategoryValue().getEncodedValue().equals("Region")) {

                                String regCode = loc.getLocation().getName().trim();

                                if (locationFundingMap.containsKey(regCode)) {
                                    FundingData existingVal = (FundingData) locationFundingMap.get(regCode);

                                    FundingData newVal = new FundingData();
                                    newVal.setCommitment(existingVal.getCommitment().add(fundingForSector.getCommitment().multiply(new BigDecimal(loc.getLocationpercentage() / 100f))));
                                    newVal.setDisbursement(existingVal.getDisbursement().add(fundingForSector.getDisbursement().multiply(new BigDecimal(loc.getLocationpercentage() / 100f))));
                                    newVal.setExpenditure(existingVal.getExpenditure().add(fundingForSector.getExpenditure().multiply(new BigDecimal(loc.getLocationpercentage() / 100f))));
                                    locationFundingMap.put(regCode, newVal);
                                } else {
                                    if (fundingForSector.getCommitment().floatValue() !=0f || fundingForSector.getDisbursement().floatValue() != 0f || fundingForSector.getExpenditure().floatValue() != 0f) {
                                        FundingData newVal = new FundingData();
                                        newVal.setCommitment(fundingForSector.getCommitment().multiply(new BigDecimal(loc.getLocationpercentage()/ 100f)));
                                        newVal.setDisbursement(fundingForSector.getDisbursement().multiply(new BigDecimal(loc.getLocationpercentage() / 100f)));
                                        newVal.setExpenditure(fundingForSector.getExpenditure().multiply(new BigDecimal(loc.getLocationpercentage()/ 100f)));
                                        locationFundingMap.put(regCode, newVal);
                                    }
                                }
                            } else if (level == GisMap.MAP_LEVEL_DISTRICT &&
                                    loc.getLocation().getParentCategoryValue().getEncodedValue().equals("Zone")) {

                                //District level
                                //if (loc.getLocation().getAmpZone()!=null && loc.getLocationPercentage().floatValue() > 0.0f) {

                                String regCode = loc.getLocation().getName().trim();

                                if (locationFundingMap.containsKey(regCode)) {
                                    FundingData existingVal = (FundingData)
                                            locationFundingMap.get(
                                                    regCode);

                                    FundingData newVal = new FundingData();
                                    newVal.setCommitment(existingVal.getCommitment().add(fundingForSector.getCommitment().multiply(new BigDecimal(loc.getLocationpercentage()/100f))));
                                    newVal.setDisbursement(existingVal.getDisbursement().add(fundingForSector.getDisbursement().multiply(new BigDecimal(loc.getLocationpercentage() / 100f))));
                                    newVal.setExpenditure(existingVal.getExpenditure().add(fundingForSector.getExpenditure().multiply(new BigDecimal(loc.getLocationpercentage() / 100f))));
                                    locationFundingMap.put(regCode, newVal);
                                } else {
                                    if (fundingForSector.getCommitment().floatValue() !=
                                            0f ||
                                            fundingForSector.getDisbursement().
                                                    floatValue() != 0f ||
                                            fundingForSector.getExpenditure().
                                                    floatValue() != 0f) {

                                        FundingData newVal = new FundingData();
                                        newVal.setCommitment(fundingForSector.getCommitment().multiply(new BigDecimal(loc.getLocationpercentage()/ 100f)));
                                        newVal.setDisbursement(fundingForSector.getDisbursement().multiply(new BigDecimal(loc.getLocationpercentage()/100f)));
                                        newVal.setExpenditure(fundingForSector.getExpenditure().multiply(new BigDecimal(loc.getLocationpercentage()/100f)));
                                        locationFundingMap.put(regCode, newVal);
                                    }
                                }
                            }
                        }

                    }

                    //    Set activiactivity.getFunding();
                }

            }

        }
        Object[] retVal = new Object[2];
        retVal[0] = locationFundingMap;
        retVal[1] = totalFundingForSector;
        return retVal;
    }

    public static FundingData getActivityTotalFundingInBaseCurrency(AmpActivityVersion activity,
            Date start, Date end) {
    	return getActivityTotalFundingInBaseCurrency(activity, start, end, null);
    }

    public static FundingData getActivityTotalFundingInBaseCurrency(AmpActivityVersion activity,
            Date start, Date end, Long donorId) {
        FundingData retVal = null;
        Set fundSet = activity.getFunding();
        Iterator<AmpFunding> fundIt = fundSet.iterator();

        BigDecimal commitment = null;
        BigDecimal disbursement = null;
        BigDecimal expenditure = null;
        BigDecimal planedDisbursement = null;

        Timestamp startTs = new Timestamp(start.getTime());
        startTs.setNanos(0);
        Timestamp endTs = new Timestamp(end.getTime());
        endTs.setNanos(0);

        FundingCalculationsHelper fch = new FundingCalculationsHelper();
//        fch.doCalculations();

        Set fundDetSet = new HashSet();


        try {
            while (fundIt.hasNext()) {
                AmpFunding fund = fundIt.next();

                if (donorId == null || donorId < 0 || donorId.equals(fund.getAmpDonorOrgId().getAmpOrgId())){

                Set fundDetails = fund.getFundingDetails();

                Iterator fdIt = fundDetails.iterator();
                while (fdIt.hasNext()) {
                    AmpFundingDetail fd = (AmpFundingDetail) fdIt.next();
                    if ((fd.getTransactionDate().after(startTs) || fd.getTransactionDate().equals(startTs)) &&
                        (fd.getTransactionDate().before(endTs)) || fd.getTransactionDate().equals(endTs)) {
                        fundDetSet.add(fd);
                    }
                }
            }
        }

           String baseCurr	= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        	if ( baseCurr == null ){
        		baseCurr	= "USD";
            }
            fch.doCalculations(fundDetSet, baseCurr);

            commitment = fch.getTotActualComm().getValue();
            disbursement = fch.getTotActualDisb().getValue();
            expenditure = fch.getTotActualExp().getValue();
            planedDisbursement = fch.getTotPlanDisb().getValue();

        } catch (Exception ex1) {
            ex1.printStackTrace();
            //Add exception reporting
        }

        retVal = new FundingData(commitment, disbursement, expenditure, planedDisbursement);

        return retVal;
    }

    public static FundingData getActivityTotalFundingInBaseCurrencyForPledge(FundingPledges pledge,
                                                                             Date start, Date end, Long donorId) {
        FundingData retVal = null;
        List <FundingPledgesDetails> fundSet = PledgesEntityHelper.getPledgesDetails(pledge.getId());

        Iterator<FundingPledgesDetails> fundIt = fundSet.iterator();

        BigDecimal commitment = null;
        BigDecimal disbursement = null;
        BigDecimal expenditure = null;
        BigDecimal plannedDisbursement = null;

        Timestamp startTs = new Timestamp(start.getTime());
        startTs.setNanos(0);
        Timestamp endTs = new Timestamp(end.getTime());
        endTs.setNanos(0);

        FundingCalculationsHelper fch = new FundingCalculationsHelper();
//        fch.doCalculations();

        Set fundDetSet = new HashSet();


        try {
            while (fundIt.hasNext()) {
                FundingPledgesDetails fund = fundIt.next();

                if (donorId == null || donorId < 0 || donorId.equals(pledge.getOrganization().getAmpOrgId())) {
                    if ((fund.getFunding_date().after(startTs) || fund.getFunding_date().equals(startTs)) &&
                            (fund.getFunding_date().before(endTs)) || fund.getFunding_date().equals(endTs)) {

                        AmpFundingDetail forCalculations = new AmpFundingDetail();
                        forCalculations.setAmpCurrencyId(fund.getCurrency());
                        forCalculations.setTransactionAmount(fund.getAmount());
                        forCalculations.setTransactionDate(fund.getFunding_date());
                        forCalculations.setTransactionType(Constants.COMMITMENT);


                        fundDetSet.add(forCalculations);
                    }
                }
            }


            String baseCurr	= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
            if ( baseCurr == null ){
                baseCurr	= "USD";
            }
            fch.doCalculations(fundDetSet, baseCurr);

            commitment = fch.getTotActualComm().getValue();
            disbursement = fch.getTotActualDisb().getValue();
            expenditure = fch.getTotActualExp().getValue();
            plannedDisbursement = fch.getTotPlanDisb().getValue();
        } catch (Exception ex1) {
            ex1.printStackTrace();
            //Add exception reporting
        }

        retVal = new FundingData(commitment, disbursement, expenditure, plannedDisbursement);

        return retVal;
    }

}
