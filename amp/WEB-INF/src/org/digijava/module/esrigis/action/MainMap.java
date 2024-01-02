package org.digijava.module.esrigis.action;

/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 * @author Diego Dimunzio
 */

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.onepager.components.features.tables.AmpLocationFormTableFeature;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.exception.reports.ReportException;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.*;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.esrigis.dbentity.AmpMapConfig;
import org.digijava.module.esrigis.form.DataDispatcherForm;
import org.digijava.module.esrigis.helpers.*;
import org.hibernate.query.Query;
import org.hibernate.type.StringType;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.*;

public class MainMap extends Action {
    private static final Logger logger = Logger.getLogger(MainMap.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DataDispatcherForm dataDispatcherForm = (DataDispatcherForm) form;
        MapFilter filter = dataDispatcherForm.getFilter();
        if (request.getParameter("reset") != null
                && request.getParameter("reset").equals("true")) {
            filter = null;
        }

        if (request.getParameter("action") != null) {
            return displayIcon(mapping, form, request, response);
        }

        AmpMapConfig map = DbHelper.getMapByType(MapConstants.MapType.ARCGIS_API);
        if(map != null && map.getMapUrl() != null && !"".equals(map.getMapUrl()))
            dataDispatcherForm.setApiurl(map.getMapUrl());

        //Load indicator layers
        List<AmpMapConfig> indicators = DbHelper.getMapsBySubType(MapConstants.MapSubType.INDICATOR);
        if(!indicators.isEmpty()) {
            dataDispatcherForm.setIndicators(indicators);
        }
        
        if (filter == null) {
            filter = new MapFilter();
            if (request.getParameter("public") != null
                    && request.getParameter("public").equalsIgnoreCase("true")) {
                filter.setFromPublicView(true);
            }
            initializeFilter(filter,request);
            dataDispatcherForm.setFilter(filter);
        } else {
            // Check if needed structures are loaded TODO: Check why this is
            // happening.
            if (filter.getStructureTypes() == null) {
                List<AmpStructureType> sts;
                sts = (List<AmpStructureType>) DbHelper.getAllStructureTypes();
                filter.setStructureTypes(sts);
            }
            if (request.getParameter("public") != null
                    && request.getParameter("public").equalsIgnoreCase("true")) {
                filter.setFromPublicView(true);
            }

        }
        
        List<AmpCategoryValue> categoryvaluesfinanisnt = null;
        categoryvaluesfinanisnt = (List<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
        filter.setFinancingInstruments(categoryvaluesfinanisnt);
        
        List<AmpCategoryValue> categoryvaluestypeofassis = null;
        categoryvaluestypeofassis = (List<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
        filter.setTypeofassistences(categoryvaluestypeofassis);
        
        List<AmpCategoryValue> categoryvaluesprojectstatus = null;
        categoryvaluesprojectstatus = (List<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY);
        filter.setProjectstatus(categoryvaluesprojectstatus);
        double gsLat=7.1881;
        double gsLong=21.0938;
        if (request.getParameter("exportreport") != null) {
            
            populateFilterForExport(filter);
            filter.setModeexport(true);
            //create ReportContextData if it doesn't exists
            ReportContextData.getFromRequest(true);
            AmpReports report = null;
            try{
                report = ARUtil.getReferenceToReport();
            } catch(Exception ex) {
                logger.error(ex.getMessage());
                ARUtil.generateReportResponse(response, ex.getMessage());
                return null;
            }
            AmpARFilter reportfilter = getReportFilter(report);
            if( !ARUtil.hasCurrentUserAccessRight(report) ) {
                ARUtil.generateReportResponse(response, "Access denied to ampReportId=%d", report.getId());
                return null;
            }
            filter.setReportfilterquery(reportfilter.getGeneratedFilterQuery());
            filter.setCurrencyId(reportfilter.getCurrency() == null ? null : reportfilter.getCurrency().getAmpCurrencyId());
            if (reportfilter.getRenderStartYear()!=null && reportfilter.getRenderStartYear()!=0){
                filter.setStartYear(reportfilter.getRenderStartYear().longValue());
            }else{
                Integer defaultStart = getDefaultStartYear(request);
                if (defaultStart!=-1){
                    filter.setStartYear(defaultStart.longValue());
                }
            }
            if (reportfilter.getRenderStartYear()!=null && reportfilter.getRenderStartYear()!=0){
                filter.setEndYear(reportfilter.getRenderEndYear().longValue());
            }else{
                Integer defaultEnd = getDefaultEndYear(request);
                if (defaultEnd!=-1){
                    filter.setEndYear(defaultEnd.longValue());
                }
            }
                
            Collection<AmpCategoryValueLocations> locs = reportfilter.getLocationSelected();
//            7.1881, 21.0938
            //Coordinates for center of Africa

            if (locs!=null && !locs.isEmpty()){
                Long[] sellocs= new Long[locs.size()];
                int i=0;
                for (AmpCategoryValueLocations loc : locs) {
                    sellocs[i] = loc.getId();

                    i++;
                }
                filter.setSelLocationIds(sellocs);
            }
        } else {
            filter.setModeexport(false);
        }
        //we set the map to center on one of the selected locations or else centre it in Africa
        if (FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.MULTI_COUNTRY_GIS_ENABLED) ) {
            if (!AmpLocationFormTableFeature.LOCATIONS_SELECTED.isEmpty()) {
                for (AmpActivityLocation ampActivityLocation : AmpLocationFormTableFeature.LOCATIONS_SELECTED) {
                    if (ampActivityLocation.getLocation().getGsLat() != null && !Objects.equals(ampActivityLocation.getLocation().getGsLat(), "")) {
                        gsLat = Double.parseDouble(ampActivityLocation.getLocation().getGsLat());
                    }
                    if (ampActivityLocation.getLocation().getGsLong() != null && !Objects.equals(ampActivityLocation.getLocation().getGsLong(), "")) {
                        gsLong = Double.parseDouble(ampActivityLocation.getLocation().getGsLong());
                    }
                }
            }
            logger.info("Latitude,Longitude " + gsLat + "," + gsLong);
            String hql = "update " + AmpGlobalSettings.class.getName() + " s set s.globalSettingsValue = " +
                    "case " +
                    "when s.globalSettingsName = :latName then :newLat " +
                    "when s.globalSettingsName = :longName then :newLong " +
                    "else s.globalSettingsValue " +
                    "end";
            Query query = PersistenceManager.getRequestDBSession().createQuery(hql);
            query.setParameter("latName", "Country Latitude", StringType.INSTANCE);
            query.setParameter("longName", "Country Longitude", StringType.INSTANCE);
            query.setParameter("newLat", String.valueOf(gsLat), StringType.INSTANCE);
            query.setParameter("newLong", String.valueOf(gsLong), StringType.INSTANCE);
            int rowCount = query.executeUpdate();
            logger.info("Updated settings for latitude. " + rowCount);
            FeaturesUtil.refreshSettingsCache();
        }

        if (request.getParameter("popup") != null
                && request.getParameter("popup").equalsIgnoreCase("true")) {
            return mapping.findForward("popup");

        }

        Collection<AmpStructureType> sts = new ArrayList<AmpStructureType>();
        sts = DbHelper.getAllStructureTypes();
        request.setAttribute("structureTypesList", sts);

        return mapping.findForward("forward");
    }
    
    private AmpARFilter getReportFilter(AmpReports report) throws ReportException {
        AmpARFilter reportFilter = ReportContextData.getFromRequest().getFilter();
        if( reportFilter == null ) {
            try{
                reportFilter = ReportContextData.getFromRequest().loadOrCreateFilter(false, report);
            } catch(Exception ex) {
                logger.error(ex.getMessage());
                throw new ReportException("Could not find filters attched to ampReportId="+(report==null? report : report.getAmpReportId()), ex);
            }
        }
        return reportFilter;
    }
    
    private Integer getDefaultStartYear(HttpServletRequest request) {
        AmpApplicationSettings tempSettings = getAppSetting(request);
        Integer rStart = -1;
        if (tempSettings != null && tempSettings.getReportStartYear() != null && tempSettings.getReportStartYear().intValue() != 0) {
            rStart = tempSettings.getReportStartYear();
        } else {
            String gvalue = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE);
            if (gvalue != null && !"".equalsIgnoreCase(gvalue) && Integer.parseInt(gvalue) > 0) {
                rStart = Integer.parseInt(gvalue);
            }
        }

        return rStart;
    }

    private Integer getDefaultEndYear(HttpServletRequest request) {
        AmpApplicationSettings tempSettings = getAppSetting(request);
        Integer rEnd = -1;
        if (tempSettings != null && tempSettings.getReportEndYear() != null && tempSettings.getReportEndYear().intValue() != 0) {
            rEnd = tempSettings.getReportEndYear();
        } else {
            String gvalue = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE);
            if (gvalue != null && !"".equalsIgnoreCase(gvalue) && Integer.parseInt(gvalue) > 0) {
                rEnd = Integer.parseInt(gvalue);
            }
        }
        return rEnd;
    }
    
    
    public static AmpApplicationSettings getAppSetting(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        TeamMember teamMember = (TeamMember) httpSession.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
        AmpApplicationSettings tempSettings = null;
        if (teamMember != null) {
            tempSettings = DbUtil.getTeamAppSettings(teamMember.getTeamId());
        }
        return tempSettings;
    }

    
    private void populateFilterForExport (MapFilter filter) {
        try {
            filter.setProgramElements(QueryUtil.initializePrograms());
        } catch (DgException e) {
            logger.error("Exception while initializing AmpThemes for GIS",e);
        }
        List<AmpCategoryValue> budgets = null;
        budgets = (List<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_BUDGET_KEY);
        filter.setBudgets(budgets);
        
    }
    /**
     * 
     * @param filter
     */
    private void initializeFilter(MapFilter filter,HttpServletRequest request) {
        String locale = RequestUtils.getNavigationLanguage(request).getCode();
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        try {
            if(filter.getSelSectorConfigId()==null){
                    filter.setSelSectorConfigId(SectorUtil.getPrimaryConfigClassification().getId());
            }
            filter.setSectorConfigs(SectorUtil.getAllClassificationConfigs());
            filter.setConfigWithSectorAndSubSectors(new ArrayList<EntityRelatedListHelper<AmpClassificationConfiguration,EntityRelatedListHelper<AmpSector,AmpSector>>>());
            List<AmpSector> sectors = org.digijava.module.esrigis.helpers.DbUtil
                        .getParentSectorsFromConfig(filter.getSelSectorConfigId());
            
            filter.setSectors(sectors);
            for(AmpClassificationConfiguration config: filter.getSectorConfigs()){
                List<AmpSector> currentConfigSectors = org.digijava.module.esrigis.helpers.DbUtil.getParentSectorsFromConfig(config.getId());
                List<EntityRelatedListHelper<AmpSector,AmpSector>> sectorsWithSubSectors = new ArrayList<EntityRelatedListHelper<AmpSector,AmpSector>>();
                Collections.sort((List)currentConfigSectors, new DbUtil.HelperAmpSectorNameComparator());
                for(AmpSector sector:currentConfigSectors){;
                    List<AmpSector> sectorList=new ArrayList<AmpSector>(sector.getSectors());
                    
                    Collections.sort((List)sectorList, new DbUtil.HelperAmpSectorNameComparator());
                    
                    sectorsWithSubSectors.add(new EntityRelatedListHelper<AmpSector,AmpSector>(sector,sectorList));
                }
                //Collections.sort((List)sectorsWithSubSectors, new DbUtil.HelperAmpSectorNameComparator());
                
                filter.getConfigWithSectorAndSubSectors().add(new EntityRelatedListHelper<AmpClassificationConfiguration,EntityRelatedListHelper<AmpSector,AmpSector>>(config,sectorsWithSubSectors));
                }
            } catch (DgException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        List<AmpOrgType> orgtypes = new ArrayList<AmpOrgType>(DbUtil.getAllOrgTypes());
        filter.setOrganizationsType(orgtypes);
        
        filter.buildOrganizationsByOrgGroup();
        
        if (filter.getRegions() == null) {
            try {
                filter.setRegions(new ArrayList<AmpCategoryValueLocations>(
                        DynLocationManagerUtil.getRegionsOfDefCountryHierarchy()));
                List<EntityRelatedListHelper<AmpCategoryValueLocations,AmpCategoryValueLocations>> regionWithZones = new ArrayList<EntityRelatedListHelper<AmpCategoryValueLocations,AmpCategoryValueLocations>>();
                for(AmpCategoryValueLocations region:filter.getRegions()){
                    List<AmpCategoryValueLocations> zones=new ArrayList<AmpCategoryValueLocations>(region.getChildLocations());
                    regionWithZones.add(new EntityRelatedListHelper<AmpCategoryValueLocations,AmpCategoryValueLocations>(region,zones));
                }
                filter.setRegionWithZones(regionWithZones);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        if (filter.getStartYear() == null) {
            long year = (long) Util.getCurrentFiscalYear();

            filter.setDefaultStartYear(year - 3);
            filter.setStartYear(year - 3);
            filter.setStartYearFilter(year - 3);
            filter.setEndYear(year);
            filter.setDefaultEndYear(year);
        }
        
        filter.setCurrencies(CurrencyUtil.getUsableCurrencies());           
        filter.setYears(new TreeMap<Integer, Integer>());
        int yearFrom = Integer
                .parseInt(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.YEAR_RANGE_START));
        int countYear = Integer
                .parseInt(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
        long maxYear = yearFrom + countYear;
        if (maxYear < filter.getStartYear()) {
            maxYear = filter.getStartYear();
        }
        for (int i = yearFrom; i <= maxYear; i++) {
            filter.getYears().put(i,i);
        }
        
        Collection calendars = DbUtil.getAllFisCalenders();
        if (calendars != null) {
            filter.setFiscalCalendars(new ArrayList(calendars));
        }

        String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
        if (value != null) {
            Long fisCalId = Long.parseLong(value);
            filter.setFiscalCalendarId(fisCalId);
            filter.setDefaultFiscalCalendarId(fisCalId);
        }
        if (filter.getLargestProjectNumber() == null) {
            filter.setLargestProjectNumber(10);
        }
        if (filter.getDivideThousands() == null) {
            filter.setDivideThousands(false);
        }
        if (filter.getDivideThousandsDecimalPlaces() == null) {
            filter.setDivideThousandsDecimalPlaces(0);
        }
        if (filter.getRegions() == null) {
            try {
                filter.setRegions(new ArrayList<AmpCategoryValueLocations>(
                        DynLocationManagerUtil
                                .getLocationsOfTypeAdmLevel1OfDefCountry()));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Long[] regionId = filter.getRegionIds();
        List<AmpCategoryValueLocations> zones = new ArrayList<AmpCategoryValueLocations>();

        if (regionId != null && regionId.length != 0 && regionId[0] != -1) {
            AmpCategoryValueLocations region;
            try {
                region = LocationUtil
                        .getAmpCategoryValueLocationById(regionId[0]);
                if (region.getChildLocations() != null) {
                    zones.addAll(region.getChildLocations());

                }
            } catch (DgException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        List<AmpStructureType> sts = new ArrayList<AmpStructureType>();
        sts = (List<AmpStructureType>) DbHelper.getAllStructureTypes();
        filter.setStructureTypes(sts);
        
        List<AmpCategoryValue> categoryvaluesfinanisnt = null;
        categoryvaluesfinanisnt = (List<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
        filter.setFinancingInstruments(categoryvaluesfinanisnt);
        
        List<AmpCategoryValue> categoryvaluestypeofassis = null;
        categoryvaluestypeofassis = (List<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
        filter.setTypeofassistences(categoryvaluestypeofassis);
        
        List<AmpCategoryValue> categoryvaluesprojectstatus = null;
        categoryvaluesprojectstatus = (List<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY);
        filter.setProjectstatus(categoryvaluesprojectstatus);


        List<AmpCategoryValue> budgets = null;
        budgets = (List<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_BUDGET_KEY);
        filter.setBudgets(budgets);

        filter.setPeacebuildingMarkers(DbHelper.getPeacebuildingMarkers());
        if (filter.getPeacebuildingMarkers() != null &&
                !filter.getPeacebuildingMarkers().isEmpty()) {
            filter.setSelectedPeacebuildingMarkerId(filter.getPeacebuildingMarkers().get(0).getId());
        }
        //DO WE NEED TO FILTER OUT THIS BASED ON FEATURE MANAGER?   
        try {
            filter.setProgramElements(QueryUtil.initializePrograms());
        } catch (DgException e) {
            logger.error("Exception while initializing AmpThemes for GIS",e);
        }
        
    }
    
    
    public ActionForward displayIcon(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        String index = request.getParameter("id");

        if (index != null) {
            try {
                Long structureTypeId = Long.parseLong(index);
                AmpStructureType structureType = DbHelper
                        .getStructureType(structureTypeId);
                ServletOutputStream os = response.getOutputStream();
                if (structureType.getIconFile() != null) {
                    response.setContentType(structureType
                            .getIconFileContentType());
                    os.write(structureType.getIconFile());
                    os.flush();
                } else {
                    BufferedImage bufferedImage = new BufferedImage(30, 30,
                            BufferedImage.TRANSLUCENT);
                    ImageIO.write(bufferedImage, "png", os);
                    os.flush();
                }
            } catch (NumberFormatException nfe) {
                logger.error("Trying to parse " + index + " to int");
            }
        } else {
            BufferedImage bufferedImage = new BufferedImage(30, 30,
                    BufferedImage.TRANSLUCENT);
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(bufferedImage, "png", os);
            os.flush();
        }
        return null;
    }
}
