/*
 * Copyright (c) 2013 Development Gateway (www.developmentgateway.org)
 */

package org.dgfoundation.amp.onepager.helper;

import org.digijava.module.aim.dbentity.AmpAhsurvey;

import java.io.Serializable;
import java.util.Comparator;

/**
 * serializable comparator to be used in sorting the PI questionnaires
 * in the activity form
 *
 * @author aartimon@developmentgateway.org
 * @since 10 Jan 2013
 */
public class PIFormSectionSurveyComparator implements Comparator<AmpAhsurvey>, Serializable {

    @Override
    public int compare(AmpAhsurvey a, AmpAhsurvey b) {
        int result;
        if (a.getAmpDonorOrgId() != null && a.getAmpDonorOrgId() != null){
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
        return -1;
    }
}
