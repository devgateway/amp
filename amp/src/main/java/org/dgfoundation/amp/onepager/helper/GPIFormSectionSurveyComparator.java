/*
 * Copyright (c) 2014 Development Gateway (www.developmentgateway.org)
 */

package org.dgfoundation.amp.onepager.helper;

import org.digijava.module.aim.dbentity.AmpGPISurvey;

import java.io.Serializable;
import java.util.Comparator;

/**
 * serializable comparator to be used in sorting the GPI questionnaires
 * in the activity form
 *
 * @author ginchauspe@developmentgateway.org
 * @since 05 Feb 2014
 */
public class GPIFormSectionSurveyComparator implements Comparator<AmpGPISurvey>, Serializable {

    @Override
    public int compare(AmpGPISurvey a, AmpGPISurvey b) {
        int result;
        /*if (a.getAmpDonorOrgId() != null && a.getAmpDonorOrgId() != null){
            result = a.getAmpDonorOrgId().getName().compareTo(b.getAmpDonorOrgId().getName());
            if (result != 0)
                return result;
            else{
                if (a.getSurveyDate() != null && b.getSurveyDate() != null)
                    return a.getSurveyDate().compareTo(b.getSurveyDate());
                else
                if (b.getSurveyDate() != null)
                    return 1;
                else
                    return -1;
            }
        }
        return -1;*/
        return 0;
    }
}
