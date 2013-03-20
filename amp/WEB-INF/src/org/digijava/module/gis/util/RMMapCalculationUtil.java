package org.digijava.module.gis.util;

import org.dgfoundation.amp.harvest.DBUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.*;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.gis.dbentity.GisMap;
import org.digijava.module.gis.dbentity.GisMapSegment;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 5/18/11
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class RMMapCalculationUtil {

	public static List<AmpTeam> getWorkspacesForRequest(GisFilterForm filter)
	{
		List <AmpTeam> workspaces = null;

	    if (filter.isCurWorkspaceOnly() && filter.getCurWorkspace() != null) {
	        workspaces = new ArrayList<AmpTeam>();
	        workspaces.add(filter.getCurWorkspace());
	      	DbUtil.addAllChildWorkspaces(filter.getCurWorkspace(), workspaces, null);
	    }
	    return workspaces;
	}
	
    public static Object[] getAllFundingsFiltered (GisFilterForm filter, boolean isRegional, boolean isPublic) {

        Collection<Long> primartSectors = longArrayToColl(filter.getSelectedSectors());
        Collection<Long> secondarySectors = longArrayToColl(filter.getSelectedSecondarySectors());
//        Collection<Long> tertiarySectors = null;//longArrayToColl(filter.getSelectedTertiarySectors());
        Collection<Long> typeOfAssistanceIds = longArrayToColl(filter.getSelectedTypeOfAssistance());

        Set<Long> sectorCollector = new HashSet<Long>();

        if (primartSectors != null) {
            sectorCollector.addAll(primartSectors);
        }
//        if (secondarySectors != null) {
//            sectorCollector.addAll(secondarySectors);
//        }
//        if (tertiarySectors != null) {
//            sectorCollector.addAll(tertiarySectors);
//        }


        Collection<Long> donorTypeIds = longArrayToColl(filter.getSelectedDonorTypes());
        Collection<Long> donorGroupIds = longArrayToColl(filter.getSelectedDonorGroups());
        Collection<Long> donnorAgencyIds = longArrayToColl(filter.getSelectedDonnorAgency());
        Collection<Long> programsIds = longArrayToColl(filter.getSelectedNatPlanObj());

        String defaultCountryISO = null;

        try {
        defaultCountryISO = FeaturesUtil.getDefaultCountryIso();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Collection <AmpCategoryValueLocations> locations = DbUtil.getSelectedLocations(defaultCountryISO, new Integer(filter.getMapLevel()));

        Calendar fStartDate = Calendar.getInstance();
                fStartDate.set(Integer.parseInt(filter.getFilterStartYear()), 0, 1, 0, 0, 0);
        Calendar fEndDate = Calendar.getInstance();
        	fEndDate.set(Integer.parseInt(filter.getFilterEndYear()), 11, 31, 23, 59, 59);
	        //fEndDate.add(Calendar.SECOND, -1); // THERE IS A BUG IN JDK - THIS METHOD YIELDS A RESULT IN 20120 on 1/1/2013!

        	//BOZO: debugging code for AMP-14742
//        	fStartDate.set(2008, 6, 31, 0, 0, 0);
//        	fEndDate.set(2008, 6, 31, 23, 59, 29);
        String currencyCode = filter.getSelectedCurrency();


        List <AmpTeam> workspaces = getWorkspacesForRequest(filter);

        //boolean includeCildLocations = filter.getMapLevel() == 3;
        boolean includeCildLocations = true;

        Object[] activityFundings = null;
        Object[] activityRegionalFundings = null;


        if (filter.getMapModeFin() == null || !filter.getMapModeFin().equalsIgnoreCase("pledgesData")) {
            if (isRegional == GisUtil.GIS_DONOR_FUNDINGS) {
            
            // activityFundings[0] = List<Object[7]>
            activityFundings = DbUtil.getActivityFundings(sectorCollector,
            											   secondarySectors,
                                                           programsIds,
                                                           donnorAgencyIds,
                                                           donorGroupIds,
                                                           donorTypeIds,
                                                           includeCildLocations,
                                                           locations,
                                                           workspaces,
                                                           typeOfAssistanceIds,
                                                           fStartDate.getTime(),
                                                           fEndDate.getTime(),
                                                           isPublic,
                                                           false,
                                                           filter.shouldFilterBySectors());
            }

            if (isRegional == GisUtil.GIS_REGIONAL_FUNDINGS) {
            activityRegionalFundings = DbUtil.getActivityRegionalFundings(sectorCollector,
                                                                           programsIds,
                                                                           donnorAgencyIds,
                                                                           donorGroupIds,
                                                                           donorTypeIds,
                                                                           includeCildLocations,
                                                                           locations,
                                                                           workspaces,
                                                                           fStartDate.getTime(),
                                                                           fEndDate.getTime(),
                                                                           isPublic);
            }
        } else {
            activityFundings = DbUtil.getPledgeFundings(sectorCollector,
                                                       programsIds,
                                                       donnorAgencyIds,
                                                       donorGroupIds,
                                                       donorTypeIds,
                                                       includeCildLocations,
                                                       locations,
                                                       workspaces,
                                                       typeOfAssistanceIds,
                                                       fStartDate.getTime(),
                                                       fEndDate.getTime(),
                                                       isPublic);
        }


        Object[] fundingList = getAllFundingsByLocations(activityFundings,
                                                         activityRegionalFundings,
                                                         includeCildLocations,
                                                         locations,
                                                         currencyCode,
                                                         false);


        return fundingList;
    }

    public static Object[] getFundingsFilteredForRegReport (GisFilterForm filter, Long locId, boolean isRegional, boolean isPublic) {

        Collection<Long> primarySectors = longArrayToColl(filter.getSelectedSectors());
        Collection<Long> secondarySectors = longArrayToColl(filter.getSelectedSecondarySectors());
        //Collection<Long> secondarySectors = null;
        //Collection<Long> tertiarySectors = longArrayToColl(filter.getSelectedTertiarySectors());
       // Collection<Long> tertiarySectors = null;

        Set sectorCollector = new HashSet();

        if (primarySectors != null) {
            sectorCollector.addAll(primarySectors);
        }
//        if (secondarySectors != null) {
//            sectorCollector.addAll(secondarySectors);
//        }
//        if (tertiarySectors != null) {
//            sectorCollector.addAll(tertiarySectors);
//        }


        Collection<Long> donorTypeIds = longArrayToColl(filter.getSelectedDonorTypes());
        Collection<Long> donorGroupIds = longArrayToColl(filter.getSelectedDonorGroups());
        Collection<Long> donnorAgencyIds = longArrayToColl(filter.getSelectedDonnorAgency());
        Collection<Long> programsIds = longArrayToColl(filter.getSelectedNatPlanObj());
        Collection<Long> typeOfAssistanceIds = longArrayToColl(filter.getSelectedTypeOfAssistance());

        String defaultCountryISO = null;

        try {
        defaultCountryISO = FeaturesUtil.getDefaultCountryIso();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //Collection <AmpCategoryValueLocations> locations = DbUtil.getSelectedLocations(defaultCountryISO, new Integer(filter.getMapLevel()));

        Collection <AmpCategoryValueLocations> locations = new ArrayList <AmpCategoryValueLocations>();

        try {
            locations.add(LocationUtil.getAmpCategoryValueLocationById(locId));
        } catch (DgException ex) {

        }


        Calendar fStartDate = Calendar.getInstance();
                fStartDate.set(Integer.parseInt(filter.getFilterStartYear()), 0, 1, 0, 0, 0);
        Calendar fEndDate = Calendar.getInstance();
                fEndDate.set(Integer.parseInt(filter.getFilterEndYear()), 11, 31, 23, 59, 59);
		        //fEndDate.add(Calendar.SECOND, -1); // THERE IS A BUG IN JDK - THIS METHOD YIELDS A RESULT IN 20120 on 1/1/2013!

        String currencyCode = filter.getSelectedCurrency();

        List <AmpTeam> workspaces = getWorkspacesForRequest(filter);

        //boolean includeCildLocations = filter.getMapLevel() == 3;
        boolean includeCildLocations = true;

        Object[] activityFundings = null;
        Object[] activityRegionalFundings = null;
        if (!isRegional) {
        	// activityFundings[0] = List<Object[7]>
            activityFundings = DbUtil.getActivityFundings(sectorCollector,
	            											   secondarySectors,
                                                               programsIds,
                                                               donnorAgencyIds,
                                                               donorGroupIds,
                                                               donorTypeIds,
                                                               includeCildLocations,
                                                               locations,
                                                               workspaces, typeOfAssistanceIds, fStartDate.getTime(), fEndDate.getTime(), 
                                                               isPublic, true, filter.getSelectedSecondarySectors() != null);
        } else {
            activityRegionalFundings = DbUtil.getActivityRegionalFundings(sectorCollector,
                                                                               programsIds,
                                                                               donnorAgencyIds,
                                                                               donorGroupIds,
                                                                               donorTypeIds,
                                                                               includeCildLocations,
                                                                               locations,
                                                                               workspaces, fStartDate.getTime(), fEndDate.getTime(), isPublic);
        }
        Object[] fundingList = getAllFundingsByLocations(activityFundings, activityRegionalFundings, includeCildLocations, locations, currencyCode, true);


        return fundingList;
    }

    private static Collection <Long> longArrayToColl(Long[] arrToAdd) {
        Collection<Long> retVal = null;
        if (arrToAdd != null) {
            retVal = new ArrayList<Long>();
            for (Long id : arrToAdd) {
                retVal.add(id);
            }
        }
        return retVal;
    }

    /**
     * 
     * @param activityFundingData either Object[6] or Object[7], depending on source.
     * @param activityRegionalFundingData
     * @param includeCildLocations
     * @param locations
     * @param currencyCode
     * @param detailedActData
     * @return
     */
    public static Object[] getAllFundingsByLocations (Object[] activityFundingData,
                                                   Object[] activityRegionalFundingData,
                                                   boolean includeCildLocations,
                                                   Collection<AmpCategoryValueLocations> locations,
                                                   String currencyCode,
                                                   boolean detailedActData){
        Object[] activityFundings = getFundingsByLocations (activityFundingData, locations, currencyCode, detailedActData, includeCildLocations);
        Object[] regionalFundings = getFundingsByLocationsForRegFnds (activityRegionalFundingData, locations, currencyCode, detailedActData);
        return combineResults (activityFundings, regionalFundings);
    }

    //Returns location parent map for level 3. Level 2 locations have the same value in key and val
    private static Map<Long, Long> getLocParentMapforLevelThree (Collection<AmpCategoryValueLocations> locations, boolean goToLevelThree) {
        Map<Long, Long> retVal = new HashMap <Long, Long>();

        for (AmpCategoryValueLocations loc: locations) {
            retVal.put(loc.getId(), loc.getId());
            if (goToLevelThree && loc.getChildLocations() != null && !loc.getChildLocations().isEmpty()) {
                for (AmpCategoryValueLocations childLoc: loc.getChildLocations()) {
                    retVal.put(childLoc.getId(), loc.getId());
                }
            }
        }

        return retVal;

    }

    private static Map<Long, Long> getLocParentMap (Collection<AmpCategoryValueLocations> locations, boolean goLowerLevels) {
        Map<Long, Long> retVal = new HashMap <Long, Long>();

        for (AmpCategoryValueLocations loc: locations) {
            retVal.put(loc.getId(), loc.getId());
            List<AmpCategoryValueLocations> children = getChildLocations(loc);
            for (AmpCategoryValueLocations childLoc : children) {
                retVal.put(childLoc.getId(), loc.getId());
            }
        }
        return retVal;
    }
    
    private static List<AmpCategoryValueLocations> getChildLocations (AmpCategoryValueLocations parentLoc) {
        List<AmpCategoryValueLocations> retVal = new ArrayList<AmpCategoryValueLocations>();
        if (parentLoc.getChildLocations() != null && !parentLoc.getChildLocations().isEmpty()) {
            for (AmpCategoryValueLocations childLoc: parentLoc.getChildLocations()) {
                retVal.add(childLoc);
                if (childLoc.getChildLocations() != null && !childLoc.getChildLocations().isEmpty()) {
                    retVal.addAll(getChildLocations(childLoc));
                }
            }
        }

        return retVal;
    }

    /**
     * 
     * @param data either Object[6] or Object[7], depending on source.
     * @param locations
     * @param currencyCode
     * @param detailedActData
     * @param inculedChildLocations
     * @return
     */
    private static Object[] getFundingsByLocations (Object[] data, Collection<AmpCategoryValueLocations> locations, String currencyCode, boolean detailedActData, boolean inculedChildLocations){
        Object[] retVal = null;

        if (data != null && data instanceof Object[] && data.length > 0) {
            List<Object[]> fundings = (List<Object[]>)data[0];
            Map <Long, Map<Long, Float>> sectorPercentageMap = (Map <Long, Map<Long, Float>>) data[1];
            Map <Long, Map<Long, Float>> programPercentageMap = (Map <Long, Map<Long, Float>>) data[2];
            Map <Long, Map<Long, Float>> locationPercentageMap = (Map <Long, Map<Long, Float>>) data[3];
            Map <Long, Map<Long, Float>> secondarySectorPercentageMap = data.length >= 5 ? (Map <Long, Map<Long, Float>>) data[4] : null;


            if (sectorPercentageMap != null) {
                applySectorOrProgramPercentages (fundings, sectorPercentageMap);
            }

            if (secondarySectorPercentageMap != null){
            	applySectorOrProgramPercentages(fundings, secondarySectorPercentageMap);
            }
            
            if (programPercentageMap != null) {
                applySectorOrProgramPercentages (fundings, programPercentageMap);
            }

            Map<Long, String> locationIdNameMap = getLocationIdNameMap (locations);

            //Map<String, Set> locationGroupedFnds = groupFundingsByLocationAndApplyPercentages (fundings, locationPercentageMap, locationIdNameMap, getLocParentMapforLevelThree(locations, inculedChildLocations));
            Map<String, Set<AmpFundingDetail>> locationGroupedFnds = groupFundingsByLocationAndApplyPercentages (fundings, locationPercentageMap, locationIdNameMap, getLocParentMap(locations, inculedChildLocations));


            retVal = calculateTotalsAndApplyExchangeRates (locationGroupedFnds, currencyCode, detailedActData);
        } else {
            retVal = new Object[]{new HashMap()};
        }

        return retVal;
    }

    private static Object[] getFundingsByLocationsForRegFnds (Object[] data, Collection<AmpCategoryValueLocations> locations, String currencyCode, boolean detailedActData){
        Object[] retVal = null;

        if (data != null && data instanceof Object[] && data.length > 0) {
            List fundings = (List)data[0];
            Map <Long, Map<Long, Float>> sectorPercentageMap = (Map <Long, Map<Long, Float>>) data[1];
            Map <Long, Map<Long, Float>> programPercentageMap = (Map <Long, Map<Long, Float>>) data[2];



            if (sectorPercentageMap != null) {
                applySectorOrProgramPercentages (fundings, sectorPercentageMap);
            }

            if (programPercentageMap != null) {
                applySectorOrProgramPercentages (fundings, programPercentageMap);
            }

            Map<Long, String> locationIdNameMap = getLocationIdNameMap (locations);

            Map<String, Set<AmpFundingDetail>> locationGroupedFnds = groupFundingsByLocationForRegFundings (fundings, locationIdNameMap);

            retVal = calculateTotalsAndApplyExchangeRates (locationGroupedFnds, currencyCode, detailedActData);
        } else {
            retVal = new Object[]{new HashMap()};
        }

        return retVal;
    }

    private static Object[] combineResults (Object[] res1, Object[] res2) {
        Object[] retVal = null;

        //If do not have any results
        if (res1.length < 2 && res2.length < 2 ) {
            retVal = res1;
        //If have only param 2 results
        } else  if (res1.length < 2 && res2.length == 2 ) {
            retVal = res2;
        //If have only param 1 results
        } else if (res1.length == 2 && res2.length < 2 ) {
            retVal = res1;
        //If have both
        } else {
            Map<String, Map> res1RegFndMap = (Map<String, Map>)res1[0];
            Map<String, Map> res2RegFndMap = (Map<String, Map>)res2[0];
            FundingData totalFnd1 = (FundingData)res1[1];
            FundingData totalFnd2 = (FundingData)res2[1];

            totalFnd1.add(totalFnd2);

            Set<String> res1RegFndMapKeys = res1RegFndMap.keySet();
            Set<String> res2RegFndMapKeys = res2RegFndMap.keySet();
            Set<String> matchedRegions = new HashSet<String> ();
            for (String reg1name : res1RegFndMapKeys) {
                for (String reg2name : res2RegFndMapKeys) {
                    if (reg2name.equals(reg1name)) {
                        FundingData regFnd1 = (FundingData)res1RegFndMap.get(reg1name);
                        FundingData regFnd2 = (FundingData)res2RegFndMap.get(reg1name);
                        regFnd1.add(regFnd2);
                        matchedRegions.add(reg1name);
                    }
                }
            }

            //add unmatched from res2RegFndMap into res1RegFndMap
            for (String reg2name : res2RegFndMapKeys) {
                if (!matchedRegions.contains(reg2name)) {
                    res1RegFndMap.put(reg2name, res2RegFndMap.get(reg2name));
                }
            }

            retVal = res1;
        }
        return retVal;
    }

    /**
     * @param fundings either Object[6] or Object[7], depending on source.
     * @param sectorOrProgramPercentageMap
     */
    public static void applySectorOrProgramPercentages (List<Object[]> fundings, Map <Long, Map<Long, Float>> sectorOrProgramPercentageMap) {
        for (Object[] fundingInfo: fundings) {
            BigDecimal ammount = new BigDecimal((Double)fundingInfo[0]);
            Long activityId = (Long)fundingInfo[5];

            Map<Long, Float> sectorOrProgramPercentageMapItem = sectorOrProgramPercentageMap.get(activityId);

            if (sectorOrProgramPercentageMapItem != null) {
                Collection <Float> values = sectorOrProgramPercentageMapItem.values();
                Float totalPercentage = null;

                for (Float val : values) {
                    if (totalPercentage == null) {
                        totalPercentage = val;
                    } else {
                        totalPercentage = totalPercentage + val;
                    }
                }
                fundingInfo[0] = new Double (ammount.multiply(new BigDecimal(totalPercentage)).divide(new BigDecimal(100)).doubleValue());
            }
        }
    }

    public final static Long UNALLOCATED_REGION_KEY = -99L;
    public final static String UNALLOCATED_REGION_NAME = "Region Unallocated";
    public final static Set<Long> UNALLOCATED_REGION_LOC_KEY_SET = Collections.unmodifiableSet(new HashSet<Long>(){{this.add(UNALLOCATED_REGION_KEY);}});

    private static Map<String, Set<AmpFundingDetail>> groupFundingsByLocationAndApplyPercentages (List<Object[]> fundings, Map <Long, Map<Long, Float>> locationPercentageMap, Map<Long, String> locationIdNameMap, Map<Long, Long> locationParentMap) {
        Map <String, Set<AmpFundingDetail>> retVal = new HashMap <String, Set<AmpFundingDetail>> ();
        Map<String, AmpCurrency> currencyCodeObjectMap = getCurrencyCodeObjectMap();

        for (Object[] fundingInfo: fundings) {
            Double ammount = (Double)fundingInfo[0];
            Integer type = (Integer)fundingInfo[1];
            Long adjustementTypeId = (Long)fundingInfo[2];
            Date date = (Date)fundingInfo[3];
            String currencyCode = (String)fundingInfo[4];
            Long activityId = (Long)fundingInfo[5];
            Double fixedExchangeRate = fundingInfo.length >= 7 ? (Double) fundingInfo[6] : null;
            if ((fixedExchangeRate != null) && fixedExchangeRate <= 1e-10)
            	fixedExchangeRate = null;
            
            Map<Long, Float> locationPercentageMapItem = locationPercentageMap.get(activityId);
            Set<Long> locKeySet;
            
            if (locationPercentageMapItem != null) {
            	locKeySet = locationPercentageMapItem.keySet();
            }
            else
            	locKeySet = UNALLOCATED_REGION_LOC_KEY_SET;
                
            Map<Long, AmpCategoryValue> adjustementIdTypeMap = new HashMap <Long, AmpCategoryValue> ();
                
            for (Long locKey : locKeySet) {
            	// For L2+L3 location summing
            	     	
            	String regionName = locKey == UNALLOCATED_REGION_KEY ? UNALLOCATED_REGION_NAME : locationIdNameMap.get(locationParentMap.get(locKey));
            	Float locationPercentage = locKey == UNALLOCATED_REGION_KEY ? 100.0f : (Float) locationPercentageMapItem.get(locKey);
            	if (!retVal.containsKey(regionName)) {
            		retVal.put(regionName, new HashSet<AmpFundingDetail>());
            	}

            	AmpCategoryValue adjTypeCatVal = null;

            	if (!adjustementIdTypeMap.containsKey(adjustementTypeId)) {
            		adjustementIdTypeMap.put(adjustementTypeId, CategoryManagerUtil.getAmpCategoryValueFromDb(adjustementTypeId));
            	}

            	adjTypeCatVal = adjustementIdTypeMap.get(adjustementTypeId);

            	AmpFundingDetail forCalculations = new AmpFundingDetail();
            	forCalculations.setAmpCurrencyId(currencyCodeObjectMap.get(currencyCode));
            	forCalculations.setTransactionAmount(new BigDecimal(ammount).multiply(new BigDecimal(locationPercentage)).divide(new BigDecimal(100)).doubleValue());
            	forCalculations.setAdjustmentType(adjTypeCatVal);
            	forCalculations.setTransactionDate(date);
            	forCalculations.setTransactionType(type);
				forCalculations.setFixedExchangeRate(fixedExchangeRate);

            	// Once do not use AmpFundingDetail as persistent object, and need activity ID for next calculations
            	// using ampFundDetailId to store appropriate activity id.
            	forCalculations.setAmpFundDetailId(activityId);

            	retVal.get(regionName).add(forCalculations);
            }
        }
        return retVal;
    }

    private static Map<String, Set<AmpFundingDetail>> groupFundingsByLocationForRegFundings (List fundings, Map<Long, String> locationIdNameMap) {
        Map <String, Set<AmpFundingDetail>> retVal = new HashMap <String, Set<AmpFundingDetail>> ();
        Map<String, AmpCurrency> currencyCodeObjectMap = getCurrencyCodeObjectMap();

        for (Object fundingInfoObj: fundings) {
            Object[] fundingInfo = (Object[]) fundingInfoObj;
            Double ammount = (Double)fundingInfo[0];
            Integer type = (Integer)fundingInfo[1];
            Date date = (Date)fundingInfo[2];
            String currencyCode = (String)fundingInfo[3];
            Long locationId = (Long)fundingInfo[5];

           if (!retVal.containsKey(locationIdNameMap.get(locationId))) {
                retVal.put(locationIdNameMap.get(locationId), new HashSet());
            }

            AmpFundingDetail forCalculations = new AmpFundingDetail();
            forCalculations.setAmpCurrencyId(currencyCodeObjectMap.get(currencyCode));
            forCalculations.setTransactionAmount((new BigDecimal(ammount)).doubleValue());
            forCalculations.setTransactionDate(date);
            forCalculations.setTransactionType(type);

            retVal.get(locationIdNameMap.get(locationId)).add(forCalculations);
        }
        return retVal;
    }

    private static Object[] calculateTotalsAndApplyExchangeRates (Map <String, Set<AmpFundingDetail>> fundings, String currencyCode, boolean detailedActData) {
        Map locationFundingMap = new HashMap();
        FundingData totalFunding = new FundingData(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));

        if (currencyCode == null) {
            String baseCurr	= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
            if ( baseCurr == null ){
                baseCurr	= "USD";
            }
            currencyCode = baseCurr;
        }

        Set <String> locations = fundings.keySet();
        for (String locName : locations) {
            Set <AmpFundingDetail> fundingsForCurrentLoc = fundings.get(locName);

           // FundingCalculationsHelper fch = new FundingCalculationsHelper();

            List detailedActDataList = null;
            if(detailedActData) {

                detailedActDataList = new ArrayList();

                Map <Long, List> activityGrouped = null;
                activityGrouped = new HashMap <Long, List> ();
                for (AmpFundingDetail fndDet : fundingsForCurrentLoc) {
                    if (!activityGrouped.containsKey(fndDet.getAmpFundDetailId())) {
                        activityGrouped.put (fndDet.getAmpFundDetailId(), new ArrayList<AmpFundingDetail>());
                    }
                    activityGrouped.get(fndDet.getAmpFundDetailId()).add(fndDet);
                }

                Set<Long> actIds = activityGrouped.keySet();

                Map <Long, String> allActIdNames = DbUtil.getActivityNames(actIds);
                Map <Long, Set> allActIdSectorNames =  DbUtil.getActivitySectorNames(actIds);
                Map <Long, Set> allActIdDonorNames =  DbUtil.getActivityDonorNames(actIds);
                Map <Long, Set> allActIdLocationNames =  DbUtil.getActivityLocationNames(actIds);

                for (Long actId : actIds) {
                    FundingCalculationsHelper fch = new FundingCalculationsHelper();
                    fch.doCalculations(activityGrouped.get(actId), currencyCode);
                    ActivityLocationFunding actData = new ActivityLocationFunding(
                            fch.getTotActualComm().getValue(),
                            fch.getTotActualDisb().getValue(),
                            fch.getTotActualExp().getValue(),
                            fch.getTotPlanDisb().getValue());
                    actData.setActivityId(actId);
                    actData.setActivityName(allActIdNames.get(actId));
                    actData.setTopSectors(allActIdSectorNames.get(actId));
                    actData.setLocations(allActIdLocationNames.get(actId));
                    actData.setDonorOrgs(allActIdDonorNames.get(actId));

                    detailedActDataList.add(actData);
                }

                int gg = 1;

            }


            FundingCalculationsHelper fch = new FundingCalculationsHelper();
            fch.doCalculations(fundingsForCurrentLoc, currencyCode);

            BigDecimal commitment = fch.getTotActualComm().getValue();
            BigDecimal disbursement = fch.getTotActualDisb().getValue();
            BigDecimal expenditure = fch.getTotActualExp().getValue();
            BigDecimal plannedDisbursement = fch.getTotPlanDisb().getValue();

            FundingData forCurLocation = new FundingData(commitment, disbursement, expenditure, plannedDisbursement);

            totalFunding.setCommitment(totalFunding.getCommitment().add(commitment));
            totalFunding.setDisbursement(totalFunding.getDisbursement().add(disbursement));
            totalFunding.setExpenditure(totalFunding.getExpenditure().add(expenditure));
            totalFunding.setPlannedDisbursement(totalFunding.getPlannedDisbursement().add(plannedDisbursement));

            forCurLocation.setActivityLocationFundingList(detailedActDataList);


            locationFundingMap.put(locName, forCurLocation);

        }

        Object[] retVal = new Object[2];
        retVal[0] = locationFundingMap;
        retVal[1] = totalFunding;
        return retVal;
    }


    private static Map<Long, String> getLocationIdNameMap (Collection<AmpCategoryValueLocations> locations) {
        Map<Long, String> retVal = new HashMap <Long, String>();
        for (AmpCategoryValueLocations loc : locations) {
            retVal.put(loc.getId(), loc.getName());
        }
        return retVal;
    }

    private static Map<String, AmpCurrency> getCurrencyCodeObjectMap () {
        Map<String, AmpCurrency> retVal = new HashMap <String, AmpCurrency> ();
        List <AmpCurrency> currencyList = CurrencyUtil.getActiveAmpCurrencyByName();
        for (AmpCurrency currency: currencyList) {
            retVal.put(currency.getCurrencyCode(), currency);
        }
        return retVal;
    }

    public static List prepareHilightSegments(List segmentData, GisMap map,
                                        Double min, Double max, MapColorScheme scheme) {

        float deltaVal = max.floatValue() - min.floatValue();

        int deltaRed, deltaGreen, deltaBlue;
        float coeffRed = 0f;
        float coeffGreen = 0f;
        float coeffBlue = 0f;

        if (scheme.getType().equalsIgnoreCase(MapColorScheme.COLOR_SCHEME_GRADIENT)) {
            deltaRed = scheme.getGradientMaxColor().getRed() - scheme.getGradientMinColor().getRed();
            deltaGreen = scheme.getGradientMaxColor().getGreen() - scheme.getGradientMinColor().getGreen();
            deltaBlue = scheme.getGradientMaxColor().getBlue() - scheme.getGradientMinColor().getBlue();


            if (deltaVal > 0) {
                coeffRed = deltaRed / deltaVal;
                coeffGreen = deltaGreen / deltaVal;
                coeffBlue = deltaBlue / deltaVal;
            } else {
                coeffRed = 1;
                coeffGreen = 1;
                coeffBlue = 1;
            }
        }

        List retVal = new ArrayList();
        Iterator it = map.getSegments().iterator();

        while (it.hasNext()) {
            GisMapSegment segment = (GisMapSegment) it.next();
            for (int idx = (int) 0; idx < segmentData.size(); idx++) {
                SegmentData sd = (SegmentData) segmentData.get(idx);
                if (sd.getSegmentCode().equalsIgnoreCase(segment.getSegmentCode())) {
                    HilightData hData = new HilightData();
                    hData.setSegmentId((int) segment.getSegmentId());

                    if (scheme.getType().equalsIgnoreCase(MapColorScheme.COLOR_SCHEME_GRADIENT)) {
                        float red = (Float.parseFloat(sd.getSegmentValue()) -
                                       min.floatValue()) * coeffRed + scheme.getGradientMinColor().getRed();
                        float green = (Float.parseFloat(sd.getSegmentValue()) -
                                       min.floatValue()) * coeffGreen + scheme.getGradientMinColor().getGreen();
                        float blue = (Float.parseFloat(sd.getSegmentValue()) -
                                       min.floatValue()) * coeffBlue + scheme.getGradientMinColor().getBlue();
                        if (deltaVal > 0) {
                        hData.setColor(new ColorRGB((int) red, (int) green, (int) blue));
                        } else {
                            hData.setColor(scheme.getGradientMaxColor());

                        }
                    } else if (scheme.getType().equalsIgnoreCase(MapColorScheme.COLOR_SCHEME_PREDEFINED)) {
                        float percentage;
                        if (deltaVal > 0) {
                            percentage = (Float.parseFloat(sd.getSegmentValue()) - min.floatValue()) / deltaVal * 100f;
                        } else {
                            percentage = 100f;
                        }
                        ColorRGB rangeColor = null;
                        for (MapColorSchemePredefinedItem item : scheme.getPredefinedColors()) {
                            if ((item.getStart() <= percentage && item.getLessThen() > percentage) || (item.getStart() <= percentage && item.getLessThen()==100f && percentage == 100f)) {
                                rangeColor = item.getColor();
                                break;
                            }
                        }
                        hData.setColor(rangeColor);
                    }
                    retVal.add(hData);
                }
            }
        }
        return retVal;
    }

    public static boolean isRegion(GisMap map, String regCode) {
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
}