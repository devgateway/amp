package org.digijava.kernel.validators.activity;

import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * Took from Haiti.
 *
 * @author Octavian Ciubotaru
 */
public class HardcodedCategoryValues {

    private AmpCategoryClass implementationLevel;

    private AmpCategoryClass implementationLocation;

    private AmpCategoryClass activityBudget;

    private AmpCategoryClass typeOfAssistance;

    private AmpCategoryClass financingInstrument;

    private AmpCategoryClass adjustmentType;

    private AmpCategoryClass activityStatus;

    private ImplementationLevels implementationLevels;

    private ImplementationLocations implementationLocations;

    private ActivityBudgets activityBudgets;

    private TypeOfAssistanceValues typeOfAssistanceValues;

    private FinancingInstruments financingInstruments;

    private AdjustmentTypes adjustmentTypes;

    private ActivityStatuses activityStatuses;

    public class ImplementationLevels {

        private AmpCategoryValue toBeSpecified;
        private AmpCategoryValue central;
        private AmpCategoryValue regional;
        private AmpCategoryValue international;
        private AmpCategoryValue national;
        private AmpCategoryValue both;

        public AmpCategoryValue getToBeSpecified() {
            return toBeSpecified;
        }

        public AmpCategoryValue getCentral() {
            return central;
        }

        public AmpCategoryValue getRegional() {
            return regional;
        }

        public AmpCategoryValue getInternational() {
            return international;
        }

        public AmpCategoryValue getNational() {
            return national;
        }

        public AmpCategoryValue getBoth() {
            return both;
        }
    }

    public class ImplementationLocations {

        private AmpCategoryValue country;
        private AmpCategoryValue region;
        private AmpCategoryValue zone;
        private AmpCategoryValue district;
        private AmpCategoryValue communalSection;
        private AmpCategoryValue unspecified;

        public AmpCategoryValue getCountry() {
            return country;
        }

        public AmpCategoryValue getRegion() {
            return region;
        }

        public AmpCategoryValue getZone() {
            return zone;
        }

        public AmpCategoryValue getDistrict() {
            return district;
        }

        public AmpCategoryValue getCommunalSection() {
            return communalSection;
        }

        public AmpCategoryValue getUnspecified() {
            return unspecified;
        }
    }

    public class ActivityBudgets {

        private AmpCategoryValue onBudget;
        private AmpCategoryValue offBudget;

        public AmpCategoryValue getOnBudget() {
            return onBudget;
        }

        public AmpCategoryValue getOffBudget() {
            return offBudget;
        }
    }

    public class TypeOfAssistanceValues {

        private AmpCategoryValue grant;
        private AmpCategoryValue inKind;
        private AmpCategoryValue loan;

        public AmpCategoryValue getGrant() {
            return grant;
        }

        public AmpCategoryValue getInKind() {
            return inKind;
        }

        public AmpCategoryValue getLoan() {
            return loan;
        }
    }

    public class FinancingInstruments {

        private AmpCategoryValue programSupport;
        private AmpCategoryValue debtRelief;

        public AmpCategoryValue getProgramSupport() {
            return programSupport;
        }

        public AmpCategoryValue getDebtRelief() {
            return debtRelief;
        }
    }

    public class AdjustmentTypes {

        private AmpCategoryValue planned;
        private AmpCategoryValue actual;

        public AmpCategoryValue getPlanned() {
            return planned;
        }

        public AmpCategoryValue getActual() {
            return actual;
        }
    }

    public class ActivityStatuses {

        private AmpCategoryValue ongoing;
        private AmpCategoryValue closed;

        public AmpCategoryValue getOngoing() {
            return ongoing;
        }

        public AmpCategoryValue getClosed() {
            return closed;
        }
    }

    public HardcodedCategoryValues() {
        implementationLevel = newCategoryClass(7L, "Implementation Level", "implementation_level");
        implementationLocation = newCategoryClass(10L, "Implementation Location", "implementation_location");
        activityBudget = newCategoryClass(42L, "Activity Budget", "activity_budget");
        typeOfAssistance = newCategoryClass(11L, "Type of Assistance", "type_of_assistence");
        financingInstrument = newCategoryClass(12L, "Financing Instrument", "financing_instrument");
        adjustmentType = newCategoryClass(44L, "Adjustment Type", "adjustment_type");
        activityStatus = newCategoryClass(6L, "Activity Status", "activity_status");

        implementationLevels = new ImplementationLevels();
        implementationLevels.toBeSpecified = newCategory(361L, implementationLevel, "A spécifier");
        implementationLevels.central = newCategory(332L, implementationLevel, "Central");
        implementationLevels.regional = newCategory(69L, implementationLevel, "Regional");
        implementationLevels.international = newCategory(122L, implementationLevel, "International");
        implementationLevels.national = newCategory(70L, implementationLevel, "National");
        implementationLevels.both = newCategory(71L, implementationLevel, "Both");

        implementationLocations = new ImplementationLocations();
        implementationLocations.country = newCategory(76L, implementationLocation, "Country");
        implementationLocations.region = newCategory(77L, implementationLocation, "Region");
        implementationLocations.zone = newCategory(119L, implementationLocation, "Zone");
        implementationLocations.district = newCategory(123L, implementationLocation, "District");
        implementationLocations.communalSection = newCategory(162L, implementationLocation, "Communal section");
        implementationLocations.unspecified = newCategory(367L, implementationLocation, "Unspecified");

        markUsedBy(implementationLevels.toBeSpecified, implementationLocations.country);
        markUsedBy(implementationLevels.central, implementationLocations.country);
        markUsedBy(implementationLevels.regional,
                implementationLocations.communalSection,
                implementationLocations.zone,
                implementationLocations.district,
                implementationLocations.region);
        markUsedBy(implementationLevels.international, implementationLocations.country);
        markUsedBy(implementationLevels.national,
                implementationLocations.country,
                implementationLocations.unspecified);
        markUsedBy(implementationLevels.both,
                implementationLocations.communalSection,
                implementationLocations.zone,
                implementationLocations.district,
                implementationLocations.country,
                implementationLocations.region);

        activityBudgets = new ActivityBudgets();
        activityBudgets.offBudget = newCategory(260L, activityBudget, "Off Budget");
        activityBudgets.onBudget = newCategory(261L, activityBudget, "On Budget");

        typeOfAssistanceValues = new TypeOfAssistanceValues();
        typeOfAssistanceValues.grant = newCategory(80L, typeOfAssistance, "Grant");
        typeOfAssistanceValues.inKind = newCategory(81L, typeOfAssistance, "In-kind");
        typeOfAssistanceValues.loan = newCategory(82L, typeOfAssistance, "Loan");

        financingInstruments = new FinancingInstruments();
        financingInstruments.programSupport = newCategory(183L, financingInstrument, "Support to project/programme");
        financingInstruments.debtRelief = newCategory(184L, financingInstrument, "Debt relief");

        adjustmentTypes = new AdjustmentTypes();
        adjustmentTypes.planned = newCategory(326L, adjustmentType, "Planned");
        adjustmentTypes.actual = newCategory(327L, adjustmentType, "Actual");

        activityStatuses = new ActivityStatuses();
        activityStatuses.closed = newCategory(264L, activityStatus, "Closed");
        activityStatuses.ongoing = newCategory(263L, activityStatus, "Ongoing");
    }

    private void markUsedBy(AmpCategoryValue cv1, AmpCategoryValue... cv2Array) {
        for (AmpCategoryValue cv2 : cv2Array) {
            cv1.getUsedByValues().add(cv2);
            cv2.getUsedValues().add(cv1);
        }
    }

    private AmpCategoryValue newCategory(long id, AmpCategoryClass categoryClass, String value) {
        AmpCategoryValue category = new AmpCategoryValue();
        category.setId(id);
        category.setAmpCategoryClass(categoryClass);
        category.setValue(value);
        return category;
    }

    private AmpCategoryClass newCategoryClass(Long id, String name, String key) {
        AmpCategoryClass categoryClass = new AmpCategoryClass();
        categoryClass.setId(id);
        categoryClass.setName(name);
        categoryClass.setKeyName(key);
        return categoryClass;
    }

    public AmpCategoryClass getImplementationLevel() {
        return implementationLevel;
    }

    public AmpCategoryClass getImplementationLocation() {
        return implementationLocation;
    }

    public AmpCategoryClass getActivityBudget() {
        return activityBudget;
    }

    public AmpCategoryClass getTypeOfAssistance() {
        return typeOfAssistance;
    }

    public AmpCategoryClass getFinancingInstrument() {
        return financingInstrument;
    }

    public AmpCategoryClass getAdjustmentType() {
        return adjustmentType;
    }

    public AmpCategoryClass getActivityStatus() {
        return activityStatus;
    }

    public ImplementationLevels getImplementationLevels() {
        return implementationLevels;
    }

    public ImplementationLocations getImplementationLocations() {
        return implementationLocations;
    }

    public ActivityBudgets getActivityBudgets() {
        return activityBudgets;
    }

    public TypeOfAssistanceValues getTypeOfAssistanceValues() {
        return typeOfAssistanceValues;
    }

    public FinancingInstruments getFinancingInstruments() {
        return financingInstruments;
    }

    public AdjustmentTypes getAdjustmentTypes() {
        return adjustmentTypes;
    }

    public ActivityStatuses getActivityStatuses() {
        return activityStatuses;
    }
}
