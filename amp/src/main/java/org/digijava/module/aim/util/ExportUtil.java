package org.digijava.module.aim.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by anpicca on 24/11/2016.
 */
public class ExportUtil {

    private static Logger logger = Logger.getLogger(ExportUtil.class);

    public static final int COMPONENT_FM_FIELD_TYPE = 0;
    public static final int COMPONENT_FM_FIELD_AMOUNT = 1;
    public static final int COMPONENT_FM_FIELD_CURRENCY = 2;
    public static final int COMPONENT_FM_FIELD_TRANSCTION_DATE = 3;
    public static final int COMPONENT_FM_FIELD_ORGANISATION = 4;
    public static final int COMPONENT_FM_FIELD_SECOND_REPORTING = 5;
    public static final int COMPONENT_FM_FIELD_DESCRIPTION = 6;
    public static final String GPI_TYPE_YES_NO = "yes-no";

    public static String buildInternalId(Set<AmpActivityInternalId> internalIds) {
        String result = "";
        for (AmpActivityInternalId internal : internalIds) {
            result += getInternalData(internal);
        }
        return result;
    }

    private static String getInternalData(AmpActivityInternalId internal) {
        String result = "[" + internal.getOrganisation().getName() + "] ";
        if (FeaturesUtil.isVisibleModule("/Activity Form/Activity Internal IDs/Internal IDs/internalId")) {
            result += "\t ";
            if (StringUtils.isNotEmpty(internal.getInternalId())) {
                result += internal.getInternalId();
            }
        }
        result += " \n";
        return result;
    }

    public static final Map<Integer, String> INDICATOR_VALUE_NAME = Collections.unmodifiableMap(new HashMap<Integer, String>() {{
        put(AmpIndicatorValue.ACTUAL, "Current Value");
        put(AmpIndicatorValue.BASE, "Base Value");
        put(AmpIndicatorValue.TARGET, "Target Value");
        put(AmpIndicatorValue.REVISED, "Revised Target Value");
    }});

    public static String getIndicatorValueType(AmpIndicatorValue value) {
        if (value.getValueType() == AmpIndicatorValue.ACTUAL ) {
            return "Current";
        } else if (value.getValueType() == AmpIndicatorValue.BASE) {
            return "Base";
        } else if (value.getValueType() == AmpIndicatorValue.TARGET) {
            return "Target";
        } else if (value.getValueType() == AmpIndicatorValue.REVISED) {
            return "Revised";
        }
        return null;
    }

    public static String getIndicatorSectors(IndicatorActivity indicator) {
        String result = "";
        for (AmpSector sector : indicator.getIndicator().getSectors()) {
            result += sector.getName() + "\n";
        }
        return result;
    }

    public static String getContactInformation(AmpContact contact) {
        String output = "";
        String emails = "";
        String telephones = "";
        Set<AmpContactProperty> contactProperties = contact.getProperties();
        if (contactProperties != null) {
            emails = contactProperties.stream().filter(z -> Constants.CONTACT_PROPERTY_NAME_EMAIL.equals(z.getName())).limit(2).map(entry -> entry.getValue()).collect(Collectors.joining(", "));

            telephones = contactProperties.stream().filter(z -> Constants.CONTACT_PROPERTY_NAME_PHONE.equals(z.getName())).limit(2).map(entry -> entry.getValue()).collect(Collectors.joining(", "));
        }
        output += contact.getName() + " " + contact.getLastname()
                + (StringUtils.isNotEmpty(emails) ? " - " + emails : "")
                + (StringUtils.isNotEmpty(telephones) ? " - " + telephones : "") + ";\n";
        return output;
    }
}
