package org.dgfoundation.amp.nireports.testcases;


/**
 * Constants used in off-db NiReportsEngine testcases
 * @author acartaleanu
 *
 */
public class TestModelConstants {

    /*Funding columns, used in funding code generation:*/
    /*as MetaSetInfo*/
    public final static String PLEDGE_ID = "pledge_id";
    public final static String TRANSACTION_TYPE = "transaction_type";
    public final static String AGREEMENT_ID = "agreement_id";
    public final static String RECIPIENT_ORG = "recipient_org";
    public final static String RECIPIENT_ROLE = "recipient_role";
    public final static String SOURCE_ROLE = "source_role";
    public final static String ADJUSTMENT_TYPE = "adjustment_type";
    /*as coordinates*/
    public final static String DONOR_ORG_ID = "donor_org_id";
    public final static String FUNDING_STATUS_ID = "funding_status_id";
    public final static String MODE_OF_PAYMENT_ID = "mode_of_payment_id";
    public final static String TERMS_ASSIST_ID = "terms_assist_id";
    public final static String FINANCING_INSTRUMENT_ID = "financing_instrument_id";
    
    /*Dimension depth constants*/
    public final static int SECTORS_DIMENSION_DEPTH = 3;
    public final static int PROGRAMS_DIMENSION_DEPTH = 4;
    public final static int ORGS_DIMENSION_DEPTH = 3;
    public final static int LOCATIONS_DIMENSION_DEPTH = 4;
    public final static int CATEGORIES_DIMENSION_DEPTH = 2;
    
    /*Dimension level constants*/
    public static int LEVEL_CATEGORY = 1;
    public static int LEVEL_ORGANIZATION_TYPE = 0;
    public static int LEVEL_ORGANIZATION_GROUP = 1;
    public static int LEVEL_ORGANIZATION = 2;
    public static final int ADM_LEVEL_0 = 0;
    public static final int ADM_LEVEL_1 = 1;
    public static final int ADM_LEVEL_2 = 2;
    public static final int ADM_LEVEL_3 = 3;
    public static int LEVEL_SECTOR = 0;
    public static int LEVEL_SUBSECTOR = 1;
    public static int LEVEL_SUBSUBSECTOR = 2;
    public static int LEVEL_PROGRAM_0 = 0;
    public static int LEVEL_PROGRAM_1 = 1;
    public static int LEVEL_PROGRAM_2 = 2;
}
