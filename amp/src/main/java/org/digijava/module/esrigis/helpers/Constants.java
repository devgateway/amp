package org.digijava.module.esrigis.helpers;

public class Constants {
    public static final int DONOR_AGENCY = 0;
    public static final int EXECUTING_AGENCY = 1;
    public static final int BENEFICIARY_AGENCY = 2;
    public static final int RESPONSIBLE_ORGANIZATION = 3;
    
    public static class DashboardType {
        public static final int DONOR = 1;
        public static final int REGION = 2;
        public static final int SECTOR = 3;
        public static final int DEALDASHBOARD = 4;
    }
    //TODO: Remove this constants from the more general aim.Constants class
    public static class GlobalSettings {
        public static final String YEAR_RANGE_START         = "Year Range Start";
        public static final String NUMBER_OF_YEARS_IN_RANGE = "Number of Years in Range";
        public static final String START_YEAR_DEFAULT_VALUE = "Change Range Default Start Value";
        public static final String END_YEAR_DEFAULT_VALUE   = "Change Range Default End Value";
        public static final int TOP_LIMIT = 5;
    };

}
