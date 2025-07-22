package org.dgfoundation.amp.newreports;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * class holding a "Option" unit
 * @author Dolghier Constantin
 *
 */
public enum AmountsUnits {
    
    AMOUNTS_OPTION_UNITS(AmpARFilter.AMOUNT_OPTION_IN_UNITS, 1, "Amounts are in units"),
    AMOUNTS_OPTION_THOUSANDS(AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS, 1000, "Amounts are in thousands (000)"),
    AMOUNTS_OPTION_MILLIONS(AmpARFilter.AMOUNT_OPTION_IN_MILLIONS, 1000 * 1000, "Amounts are in millions (000 000)"),
    AMOUNTS_OPTION_BILLIONS(AmpARFilter.AMOUNT_OPTION_IN_BILLIONS, 1000 * 1000 * 1000, "Amounts are in billions (000 000 000)");
    
    private final static Map<Integer, AmountsUnits> CODE_TO_VALUE = new HashMap<>();
    private final static SortedMap<Integer, AmountsUnits> DIVIDER_TO_VALUE = new TreeMap<>();
    
    static {
        for(AmountsUnits v:AmountsUnits.values()) {
            CODE_TO_VALUE.put(v.code, v);
            DIVIDER_TO_VALUE.put(v.divider, v);
        }
    }
    
    /**
     * one of AmpARFilter.AMOUNT_OPTION_IN_XXXX values
     */
    public final int code;
    /**
     * 10^0, 10^3, 10^6 etc
     */
    public final int divider;

    /**
     * a <strong>non-translated</strong> string user message to be used in the UI
     */
    public final String userMessage;
    
    private AmountsUnits(int code, int divider, String userMessage) {
        if (divider <= 0) 
            throw new RuntimeException("incorrect divider value: " + divider);
        this.code = code;
        this.divider = divider;
        this.userMessage = userMessage;
    }
    
    /**
     * builds an instance based foo AmpARFilter.AMOUNTS_UNITS_XX
     * @param code
     * @return
     */
    public static AmountsUnits getForValue(int code) {
        if (CODE_TO_VALUE.containsKey(code))
            return CODE_TO_VALUE.get(code);
        
        throw new RuntimeException("unknown AmpARFilter amount code: " + code);
    }

    /**
     * Returns AmountUnits for a specified divider.
     * @param divider 1, 1000, etc
     * @return AmountUnits corresponding to the divider
     */
    public static AmountsUnits getForDivider(int divider) {
        if (DIVIDER_TO_VALUE.containsKey(divider))
            return DIVIDER_TO_VALUE.get(divider);

        throw new RuntimeException("Unknown AmountsUnits with divider: " + divider);
    }
    
    /**
     * returns the value stored in GlobalSettings
     * @return
     */
    public static AmountsUnits getDefaultValue() {
        int amountsUnitCode = Integer.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS));
        return getForValue(amountsUnitCode);
    }
    
    public static int getAmountDivider(int code) {
        return AmountsUnits.getForValue(code).divider;
    }

    public static int getAmountCode(final int divider) {
        return AmountsUnits.getForDivider(divider).code;
    }
}
