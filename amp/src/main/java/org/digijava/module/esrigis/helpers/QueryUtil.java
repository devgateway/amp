package org.digijava.module.esrigis.helpers;
/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 */
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.HierarchyListableUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.OrganizationSkeleton;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;

public class QueryUtil {
     public static final BigDecimal ONEHUNDERT = new BigDecimal(100);
     private static Logger logger = Logger.getLogger(QueryUtil.class);

     
    public static MapFilter getNewFilter(HttpServletRequest request){
        MapFilter filter = new MapFilter();
        //List<AmpOrgGroup> orgGroups = new ArrayList(DbUtil.getAllOrgGroups());
        //filter.setOrgGroups(orgGroups);
                
        filter.buildOrganizationsByOrgGroup();
        
        if (filter.getRegions() == null) {
            try {
                filter.setRegions(new ArrayList<AmpCategoryValueLocations>(
                        DynLocationManagerUtil.
                        getRegionsOfDefCountryHierarchy()));
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
        
        List<OrganizationSkeleton> orgs = null;

        if (filter.getOrgGroupId() == null || filter.getOrgGroupId() == -1) {
            filter.setOrgGroupId(-1l);
        }
        if (filter.getSelLocationIds() == null) {
            Long[] locationIds={-1l};
            filter.setSelLocationIds(locationIds);
        }
        
        orgs = DbUtil.getDonorOrganisationByGroupId(filter.getOrgGroupId(), false); 
        filter.setOrganizations(orgs);
        
        List<AmpOrgType> orgtypes = new ArrayList<AmpOrgType>(DbUtil.getAllOrgTypes());
        filter.setOrganizationsType(orgtypes);
        
        try {
            if(filter.getSelSectorConfigId()==null){
                    filter.setSelSectorConfigId(SectorUtil.getPrimaryConfigClassification().getId());
            }
            filter.setSectorConfigs(SectorUtil.getAllClassificationConfigs());
            filter.setConfigWithSectorAndSubSectors(new ArrayList<EntityRelatedListHelper<AmpClassificationConfiguration,EntityRelatedListHelper<AmpSector,AmpSector>>>());
            List<AmpSector> sectors = org.digijava.module.esrigis.helpers.DbUtil.getParentSectorsFromConfig(filter.getSelSectorConfigId());
            filter.setSectors(sectors);
            for(AmpClassificationConfiguration config: filter.getSectorConfigs()){
                List<AmpSector> currentConfigSectors = org.digijava.module.esrigis.helpers.DbUtil.getParentSectorsFromConfig(config.getId());
                List<EntityRelatedListHelper<AmpSector,AmpSector>> sectorsWithSubSectors = new ArrayList<EntityRelatedListHelper<AmpSector,AmpSector>>();
                for(AmpSector sector:currentConfigSectors){;
                    List<AmpSector> sectorList=new ArrayList<AmpSector>(sector.getSectors());
                    sectorsWithSubSectors.add(new EntityRelatedListHelper<AmpSector,AmpSector>(sector,sectorList));
                }
                filter.getConfigWithSectorAndSubSectors().add(new EntityRelatedListHelper<AmpClassificationConfiguration,EntityRelatedListHelper<AmpSector,AmpSector>>(config,sectorsWithSubSectors));
                }
            } catch (DgException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
        if (filter.getStartYear() == null) {
            long year = (long) Util.getCurrentFiscalYear();
            
            filter.setDefaultStartYear(year - 3);
            filter.setStartYear(year - 3);
            filter.setStartYearFilter(year - 3);
            filter.setEndYear(year);
            filter.setDefaultEndYear(year);
        }
        filter.setYears(new TreeMap<Integer, Integer>());
        int yearFrom = Integer
                .parseInt(FeaturesUtil
                        .getGlobalSettingValue(Constants.GlobalSettings.YEAR_RANGE_START));
        int countYear = Integer
                .parseInt(FeaturesUtil
                        .getGlobalSettingValue(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
        long maxYear = yearFrom + countYear;
        if (maxYear < filter.getStartYear()) {
            maxYear = filter.getStartYear();
        }
        for (int i = yearFrom; i <= maxYear; i++) {
            filter.getYears().put(i, i);
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
                filter.setRegions(new ArrayList<>(DynLocationManagerUtil.getLocationsOfTypeAdmLevel1OfDefCountry()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Long[] regionId = filter.getRegionIds();
        List<AmpCategoryValueLocations> zones = new ArrayList<AmpCategoryValueLocations>();

        if (regionId != null && regionId.length!=0 && regionId[0] != -1) {
            AmpCategoryValueLocations region;
            try {
                region = LocationUtil.getAmpCategoryValueLocationById(regionId[0]);
                if (region.getChildLocations() != null) {
                    zones.addAll(region.getChildLocations());
                }
            } catch (DgException e) {
                e.printStackTrace();
            }

        }
        filter.setZones(zones);
        filter.setCurrencies(CurrencyUtil.getUsableCurrencies());
        filter.setPeacebuildingMarkers(DbHelper.getPeacebuildingMarkers());
        if (filter.getPeacebuildingMarkers() != null &&
                !filter.getPeacebuildingMarkers().isEmpty()) {
            filter.setSelectedPeacebuildingMarkerId(filter.getPeacebuildingMarkers().get(0).getId());
        }
        if (filter.getProgramElements() == null) {
            try {
                filter.setProgramElements(initializePrograms());
            } catch (DgException e) {
                logger.error ("Exception trying to get a new filter",e);
            }
        }

        filter.setIsinitialized(true);
        return filter;
        
    }
    
    public static String getPercentage(BigDecimal base, BigDecimal pct){
        return FormatHelper.formatNumber(base.multiply(pct).divide(ONEHUNDERT).doubleValue()); 
    }
    public static boolean inArray(Object comparableObject, Object[] objects){
        for(Object oC : objects){
            if(oC.equals(comparableObject)) return true;
        }
        return false;
    }
    
    public static Map<String,AmpTheme> initializePrograms () throws DgException {
        Map<String,AmpTheme> programLevelMap = new HashMap <String,AmpTheme> ();
        List<AmpTheme> allPrograms  = ProgramUtil.getAllThemes(true);
        HashMap<Long, AmpTheme> progMap     = ProgramUtil.prepareStructure(allPrograms);
        
        AmpActivityProgramSettings primaryPrgSetting = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM);
        AmpTheme primaryProg = null;
        if (primaryPrgSetting!=null && primaryPrgSetting.getDefaultHierarchy() != null) {
            primaryProg = progMap.get(primaryPrgSetting.getDefaultHierarchyId() );
            HierarchyListableUtil.changeTranslateable(primaryProg, false);
            programLevelMap.put("selectedPrimaryPrograms", primaryProg);
        }
        AmpTheme secondaryProg = null;
        AmpActivityProgramSettings secondaryPrg = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM);
        if (secondaryPrg!=null && secondaryPrg.getDefaultHierarchy() != null) {
            secondaryProg   = progMap.get(secondaryPrg.getDefaultHierarchyId() );
            HierarchyListableUtil.changeTranslateable(secondaryProg, false);
            programLevelMap.put("selectedSecondaryPrograms", secondaryProg);
                    
        }       
        AmpActivityProgramSettings natPlanSetting       = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
        if (natPlanSetting != null && natPlanSetting.getDefaultHierarchy() != null) {
            AmpTheme nationalPlanningProg   = progMap.get(natPlanSetting.getDefaultHierarchyId() );
            HierarchyListableUtil.changeTranslateable(nationalPlanningProg, false);
            programLevelMap.put("selectedNatPlanObj", nationalPlanningProg);
            
                
        }
        return programLevelMap;
    
}


}
