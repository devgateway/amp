package org.digijava.kernel.persistence;

import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Non-persistent implementation of {@code InMemoryManager} which is backed by an in-memory map.
 * <p>
 * Mainly intended for testing purposes, where a full blown persistent system isn't required.
 * <p>
 * Took from Haiti.
 *
 * @author Octavian Ciubotaru
 */
public class InMemoryCategoryValuesManager implements InMemoryManager<AmpCategoryValue> {
    
    private static InMemoryCategoryValuesManager instance;
    
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
    
    private Map<Long, AmpCategoryValue> categories = new HashMap<>();
    
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
    
    public static InMemoryCategoryValuesManager getInstance() {
        if (instance == null) {
            instance = new InMemoryCategoryValuesManager();
        }
        
        return instance;
    }
    
    private InMemoryCategoryValuesManager() {
        implementationLevel = newCategoryClass(7L, "Implementation Level", "implementation_level");
        implementationLocation = newCategoryClass(10L, "Implementation Location", "implementation_location");
        activityBudget = newCategoryClass(42L, "Activity Budget", "activity_budget");
        typeOfAssistance = newCategoryClass(11L, "Type of Assistance", "type_of_assistence");
        financingInstrument = newCategoryClass(12L, "Financing Instrument", "financing_instrument");
        adjustmentType = newCategoryClass(44L, "Adjustment Type", "adjustment_type");
        activityStatus = newCategoryClass(6L, "Activity Status", "activity_status");

        implementationLevels = new ImplementationLevels();
        implementationLevels.toBeSpecified = newCategory(361L, implementationLevel, "A spécifier",0);
        implementationLevels.central = newCategory(332L, implementationLevel, "Central",1);
        implementationLevels.regional = newCategory(69L, implementationLevel, "Regional", 2);
        implementationLevels.international = newCategory(122L, implementationLevel, "International", 3);
        implementationLevels.national = newCategory(70L, implementationLevel, "National", 4);
        implementationLevels.both = newCategory(71L, implementationLevel, "Both", 5);

        implementationLocations = new ImplementationLocations();
        implementationLocations.country = newCategory(76L, implementationLocation, "Administrative Level 0", 0);
        implementationLocations.region = newCategory(77L, implementationLocation, "Administrative Level 1", 1);
        implementationLocations.zone = newCategory(119L, implementationLocation, "Administrative Level 2", 2);
        implementationLocations.district = newCategory(123L, implementationLocation, "Administrative Level 3", 3);
        implementationLocations.communalSection = newCategory(162L, implementationLocation, "Administrative Level 4", 4);
        implementationLocations.unspecified = newCategory(367L, implementationLocation, "Administrative Level 5", 5);

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
        addToCategoriesList(implementationLevels.toBeSpecified, implementationLevels.central, implementationLevels.regional,
                implementationLevels.international, implementationLevels.national, implementationLevels.both);
        addToCategoriesList(implementationLocations.country, implementationLocations.region, implementationLocations.zone,
                implementationLocations.district, implementationLocations.communalSection, implementationLocations.unspecified);
        
        activityBudgets = new ActivityBudgets();
        activityBudgets.offBudget = newCategory(260L, activityBudget, "Off Budget", 0);
        activityBudgets.onBudget = newCategory(261L, activityBudget, "On Budget", 1);
        addToCategoriesList(activityBudgets.onBudget, activityBudgets.offBudget);
        
        typeOfAssistanceValues = new TypeOfAssistanceValues();
        typeOfAssistanceValues.grant = newCategory(80L, typeOfAssistance, "Grant", 0);
        typeOfAssistanceValues.inKind = newCategory(81L, typeOfAssistance, "In-kind", 1);
        typeOfAssistanceValues.loan = newCategory(82L, typeOfAssistance, "Loan", 2);
        addToCategoriesList(typeOfAssistanceValues.grant, typeOfAssistanceValues.inKind, typeOfAssistanceValues.loan);
        
        financingInstruments = new FinancingInstruments();
        financingInstruments.programSupport = newCategory(183L, financingInstrument, "Support to project/programme", 0);
        financingInstruments.debtRelief = newCategory(184L, financingInstrument, "Debt relief", 1);
        addToCategoriesList(financingInstruments.programSupport, financingInstruments.debtRelief);
        
        adjustmentTypes = new AdjustmentTypes();
        adjustmentTypes.planned = newCategory(326L, adjustmentType, "Planned", 0);
        adjustmentTypes.actual = newCategory(327L, adjustmentType, "Actual", 1);
        addToCategoriesList(adjustmentTypes.planned, adjustmentTypes.actual);
        
        activityStatuses = new ActivityStatuses();
        activityStatuses.closed = newCategory(264L, activityStatus, "Closed", 0);
        activityStatuses.ongoing = newCategory(263L, activityStatus, "Ongoing", 1);
        addToCategoriesList(activityStatuses.closed, activityStatuses.ongoing);
        
    }
    
    private void addToCategoriesList(AmpCategoryValue... acvs) {
        for (AmpCategoryValue acv : acvs) {
            categories.put(acv.getId(), acv);
        }
    }
    
    public List<AmpCategoryValue> getAllValues() {
        return new ArrayList<>(categories.values());
    }
    
    public AmpCategoryValue get(Long id) {
        return categories.get(id);
    }
    
    private void markUsedBy(AmpCategoryValue cv1, AmpCategoryValue... cv2Array) {
        for (AmpCategoryValue cv2 : cv2Array) {
            cv1.getUsedByValues().add(cv2);
            cv2.getUsedValues().add(cv1);
        }
    }
    
    private AmpCategoryValue newCategory(long id, AmpCategoryClass categoryClass, String value, Integer index) {
        AmpCategoryValue category = new AmpCategoryValue();
        category.setId(id);
        category.setAmpCategoryClass(categoryClass);
        category.setValue(value);
        category.setIndex(index);
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
