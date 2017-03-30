package org.digijava.module.aim.util;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.helper.Constants;
import org.h2.util.StringUtils;

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

    public static String getContactInformation(AmpContact contact) {
        String output = "";
        String emails = "";
        String telephones = "";
        Set<AmpContactProperty> contactProperties = contact.getProperties();
        if (contactProperties != null) {
            emails = contactProperties.stream().filter(z -> Constants.CONTACT_PROPERTY_NAME_EMAIL.equals(z.getName())).limit(2).map(entry -> entry.getValue()).collect(Collectors.joining(", "));

            telephones = contactProperties.stream().filter(z -> Constants.CONTACT_PROPERTY_NAME_PHONE.equals(z.getName())).limit(2).map(entry -> entry.getValue()).collect(Collectors.joining(", "));
        }
        output += contact.getName() + " " + contact.getLastname() + (clover.org.apache.commons.lang.StringUtils.isNotEmpty(emails) ? " - " + emails : "") + (clover.org.apache.commons.lang.StringUtils.isNotEmpty(telephones) ? " - " + telephones : "") + ";\n";
        return output;
    }
}
