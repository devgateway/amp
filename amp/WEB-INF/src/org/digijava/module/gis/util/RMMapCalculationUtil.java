package org.digijava.module.gis.util;

import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 5/18/11
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class RMMapCalculationUtil {

    public static Object[] getAllFundingsByLocations (Object[] activityFundingData,
                                                   Object[] activityRegionalFundingData,
                                                   Collection<AmpCategoryValueLocations> locations){
        Object[] retVal = null;
        Object[] activityFundings = getFundingsByLocations (activityFundingData, locations);
        Object[] regionalFundings = getFundingsByLocationsForRegFnds (activityRegionalFundingData, locations);


        return combineResults (activityFundings, regionalFundings);
    }

    private static Object[] getFundingsByLocations (Object[] data, Collection<AmpCategoryValueLocations> locations){
        Object[] retVal = null;

        if (data != null && data instanceof Object[] && data.length > 0) {
            List fundings = (List)data[0];
            Map <Long, Map> sectorPercentageMap = (Map <Long, Map>) data[1];
            Map <Long, Map> programPercentageMap = (Map <Long, Map>) data[2];
            Map <Long, Map> locationPercentageMap = (Map <Long, Map>) data[3];


            if (sectorPercentageMap != null) {
                applySectorOrProgramPercentages (fundings, sectorPercentageMap);
            }

            if (programPercentageMap != null) {
                applySectorOrProgramPercentages (fundings, programPercentageMap);
            }

            Map<Long, String> locationIdNameMap = getLocationIdNameMap (locations);

            Map<String, Set> locationGroupedFnds = groupFundingsByLocationAndApplyPercentages (fundings, locationPercentageMap, locationIdNameMap);

            retVal = calculateTotalsAndApplyExchangeRates (locationGroupedFnds, null);
        } else {
            retVal = new Object[]{new HashMap()};
        }

        return retVal;
    }

    private static Object[] getFundingsByLocationsForRegFnds (Object[] data, Collection<AmpCategoryValueLocations> locations){
        Object[] retVal = null;

        if (data != null && data instanceof Object[] && data.length > 0) {
            List fundings = (List)data[0];
            Map <Long, Map> sectorPercentageMap = (Map <Long, Map>) data[1];
            Map <Long, Map> programPercentageMap = (Map <Long, Map>) data[2];



            if (sectorPercentageMap != null) {
                applySectorOrProgramPercentages (fundings, sectorPercentageMap);
            }

            if (programPercentageMap != null) {
                applySectorOrProgramPercentages (fundings, programPercentageMap);
            }

            Map<Long, String> locationIdNameMap = getLocationIdNameMap (locations);

            Map<String, Set> locationGroupedFnds = groupFundingsByLocationForRegFundings (fundings, locationIdNameMap);

            retVal = calculateTotalsAndApplyExchangeRates (locationGroupedFnds, null);
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

    private static void applySectorOrProgramPercentages (List fundings, Map <Long, Map> sectorOrProgramPercentageMap) {
        for (Object fundingInfoObj: fundings) {
            Object[] fundingInfo = (Object[]) fundingInfoObj;
            BigDecimal ammount = new BigDecimal((Double)fundingInfo[0]);
            Long activityId = (Long)fundingInfo[4];

            Map sectorOrProgramPercentageMapItem = sectorOrProgramPercentageMap.get(activityId);

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



    private static Map<String, Set> groupFundingsByLocationAndApplyPercentages (List fundings, Map <Long, Map> locationPercentageMap, Map<Long, String> locationIdNameMap) {
        Map <String, Set> retVal = new HashMap <String, Set> ();
        Map<String, AmpCurrency> currencyCodeObjectMap = getCurrencyCodeObjectMap();

        for (Object fundingInfoObj: fundings) {
            Object[] fundingInfo = (Object[]) fundingInfoObj;
            Double ammount = (Double)fundingInfo[0];
            Integer type = (Integer)fundingInfo[1];
            Date date = (Date)fundingInfo[2];
            String currencyCode = (String)fundingInfo[3];
            Long activityId = (Long)fundingInfo[4];

            Map locationPercentageMapItem = locationPercentageMap.get(activityId);
            if (locationPercentageMapItem != null) {
            Set <Long> locKeySet = locationPercentageMapItem.keySet();
                for (Long locKey : locKeySet) {
                    if (!retVal.containsKey(locationIdNameMap.get(locKey))) {
                        retVal.put(locationIdNameMap.get(locKey), new HashSet());
                    }

                    AmpFundingDetail forCalculations = new AmpFundingDetail();
                    forCalculations.setAmpCurrencyId(currencyCodeObjectMap.get(currencyCode));
                    forCalculations.setTransactionAmount(new BigDecimal(ammount).multiply(new BigDecimal((Float)locationPercentageMapItem.get(locKey))).divide(new BigDecimal(100)).doubleValue());
                    forCalculations.setTransactionDate(date);
                    forCalculations.setTransactionType(type);

                    retVal.get(locationIdNameMap.get(locKey)).add(forCalculations);


                }
            }
        }
        return retVal;
    }

    private static Map<String, Set> groupFundingsByLocationForRegFundings (List fundings, Map<Long, String> locationIdNameMap) {
        Map <String, Set> retVal = new HashMap <String, Set> ();
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

    private static Object[] calculateTotalsAndApplyExchangeRates (Map <String, Set> fundings, String currencyCode) {
        Map locationFundingMap = new HashMap();
        FundingData totalFunding = new FundingData(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));

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
            FundingCalculationsHelper fch = new FundingCalculationsHelper();
            fch.doCalculations(fundingsForCurrentLoc, currencyCode);

            BigDecimal commitment = fch.getTotActualComm().getValue();
            BigDecimal disbursement = fch.getTotActualDisb().getValue();
            BigDecimal expenditure = fch.getTotActualExp().getValue();

            FundingData forCurLocation = new FundingData(commitment, disbursement, expenditure);

            totalFunding.setCommitment(totalFunding.getCommitment().add(commitment));
            totalFunding.setDisbursement(totalFunding.getDisbursement().add(disbursement));
            totalFunding.setExpenditure(totalFunding.getExpenditure().add(expenditure));

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
        List <AmpCurrency> currencyList = CurrencyUtil.getAmpCurrency();
        for (AmpCurrency currency: currencyList) {
            retVal.put(currency.getCurrencyCode(), currency);
        }
        return retVal;
    }
}
