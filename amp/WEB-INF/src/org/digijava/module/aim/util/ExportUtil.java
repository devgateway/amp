package org.digijava.module.aim.util;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.h2.util.StringUtils;

import java.util.Set;

/**
 * Created by anpicca on 24/11/2016.
 */
public class ExportUtil {
    private static Logger logger = Logger.getLogger(ExportUtil.class);

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
            if (!StringUtils.isNullOrEmpty(internal.getInternalId())) {
                result += internal.getInternalId();
            }
        }
        result += " \n";
        return result;
    }

    public static String getIndicatorValueType(AmpIndicatorValue value) {
        if (value.getValueType() == AmpIndicatorValue.ACTUAL ) {
            return "Current";
        } else if (value.getValueType() == AmpIndicatorValue.BASE) {
            return "Base";
        } else if (value.getValueType() == AmpIndicatorValue.TARGET) {
            return "Target";
        } else if (value.getValueType() == AmpIndicatorValue.REVISED) {
            return "Revised Target";
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

}
