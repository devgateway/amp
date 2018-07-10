package org.dgfoundation.amp.ar.legacy;

import java.util.List;

import org.dgfoundation.amp.nireports.testcases.ColumnReportDataModel;
import org.dgfoundation.amp.nireports.testcases.GroupColumnModel;
import org.dgfoundation.amp.nireports.testcases.GroupReportModel;
import org.dgfoundation.amp.nireports.testcases.SimpleColumnModel;
import org.dgfoundation.amp.testutils.*;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.hibernate.Query;
import org.junit.Test;


import static org.dgfoundation.amp.testutils.ReportTestingUtils.NULL_PLACEHOLDER;


/**
 * testcase for Directed Disbursements (AMP-15337)
 * @author Dolghier Constantin
 *
 */
public class DirectedDisbursementsTests extends ReportsTestCase
{

    protected List<AmpActivity> getAllActivities() throws Exception
    {
         org.hibernate.Session session = PersistenceManager.getRequestDBSession();
          String qryStr = "select a from " + AmpActivity.class.getName() + " a ";
          Query qry = session.createQuery(qryStr);
          return (List<AmpActivity>) qry.list();
    }
        
    /**
     * a flat report containing RealDisbursements of a single activity
     */
    @Test
    public void testFlatReport()
    {
        // ========================= one more report ===============================
        GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-15337-real-disbursements", 
                ColumnReportDataModel.withColumns("AMP-15337-real-disbursements", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2013", 
                                        GroupColumnModel.withSubColumns("Real Disbursements", 
                                                SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "545 000"),
                                                SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "100 000"),
                                                SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "15 000")
                                                ))),
                        GroupColumnModel.withSubColumns("Total Costs", 
                                        GroupColumnModel.withSubColumns("Real Disbursements", 
                                                SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "545 000"),
                                                SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "100 000"),
                                                SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "15 000")
                        ))));
        
        runReportTest("flat Directed Disbursements Report", "AMP-15337-real-disbursements", new String[] {"Eth Water"}, fddr_correct);
    }
    
    /**
     * same report as {@link #testFlatReport()}, but with a hierarchy by Beneficiary Agency
     */
    @Test
    public void testByBeneficiary()
    {
        /// ========================= one more report ===============================
        GroupReportModel by_benef_ddr_correct = GroupReportModel.withGroupReports("AMP-15337-real-disbursements-by-beneficiary",
                GroupReportModel.withColumnReports("AMP-15337-real-disbursements-by-beneficiary",                   
                ColumnReportDataModel.withColumns("Beneficiary Agency: Water Foundation", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2013", 
                                        GroupColumnModel.withSubColumns("Real Disbursements", 
                                                SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "5 000")
                                                ))),
                        GroupColumnModel.withSubColumns("Total Costs", 
                                        GroupColumnModel.withSubColumns("Real Disbursements", 
                                                SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "5 000")
                        ))),
                        ColumnReportDataModel.withColumns("Beneficiary Agency: Water Org", 
                                SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                                GroupColumnModel.withSubColumns("Funding", 
                                        GroupColumnModel.withSubColumns("2013", 
                                                GroupColumnModel.withSubColumns("Real Disbursements", 
                                                        SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "10 000")
                                                        ))),
                                GroupColumnModel.withSubColumns("Total Costs", 
                                                GroupColumnModel.withSubColumns("Real Disbursements", 
                                                        SimpleColumnModel.withContents("IMPL-BENF", "Eth Water", "10 000")
                                )))
                        
                )); 
        
        runReportTest("by beneficiary Directed Disbursements Report", "AMP-15337-real-disbursements-by-beneficiary", new String[] {"Eth Water"}, by_benef_ddr_correct);
    }
    
    /**
     * same report as {@link #testFlatReport()}, but with a hierarchy by Donor Agency
     */
    @Test
    public void testByDonor()
    {
        /// ========================= one more report ===============================
        GroupReportModel by_donor_ddr_correct = GroupReportModel.withGroupReports("AMP-15337-real-disbursements-by-donor",
                GroupReportModel.withColumnReports("AMP-15337-real-disbursements-by-donor",                 
                ColumnReportDataModel.withColumns("Donor Agency: Finland", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2013", 
                                        GroupColumnModel.withSubColumns("Real Disbursements", 
                                                SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "20 000")
                                                ))),
                        GroupColumnModel.withSubColumns("Total Costs", 
                                        GroupColumnModel.withSubColumns("Real Disbursements", 
                                                SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "20 000")
                        ))),
                        ColumnReportDataModel.withColumns("Donor Agency: Norway", 
                                SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                                GroupColumnModel.withSubColumns("Funding", 
                                        GroupColumnModel.withSubColumns("2013", 
                                                GroupColumnModel.withSubColumns("Real Disbursements", 
                                                        SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "110 000")
                                                        ))),
                                GroupColumnModel.withSubColumns("Total Costs", 
                                                GroupColumnModel.withSubColumns("Real Disbursements", 
                                                        SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "110 000")
                                ))),
                                
                        ColumnReportDataModel.withColumns("Donor Agency: USAID", 
                                SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                                GroupColumnModel.withSubColumns("Funding", 
                                        GroupColumnModel.withSubColumns("2013", 
                                                GroupColumnModel.withSubColumns("Real Disbursements", 
                                                        SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "415 000")
                                                        ))),
                                GroupColumnModel.withSubColumns("Total Costs", 
                                                GroupColumnModel.withSubColumns("Real Disbursements", 
                                                        SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "415 000")
                                )))
                                                                
                )); 
        
        runReportTest("by donor Directed Disbursements Report", "AMP-15337-real-disbursements-by-donor", new String[] {"Eth Water"}, by_donor_ddr_correct);
    }
    
    /**
     * same report as {@link #testFlatReport()}, but with a hierarchy by Executing Agency
     */
    @Test
    public void testByExecuting()
    {
        /// ========================= one more report ===============================
        GroupReportModel by_exec_ddr_correct = GroupReportModel.withGroupReports("AMP-15337-real-disbursements-by-executing",
                GroupReportModel.withColumnReports("AMP-15337-real-disbursements-by-executing",                 
                ColumnReportDataModel.withColumns("Executing Agency: UNDP", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2013", 
                                        GroupColumnModel.withSubColumns("Real Disbursements", 
                                                SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "300 000"),
                                                SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "80 000")
                                                ))),
                        GroupColumnModel.withSubColumns("Total Costs", 
                                        GroupColumnModel.withSubColumns("Real Disbursements", 
                                                SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "300 000"),
                                                SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "80 000")
                        ))),
                        ColumnReportDataModel.withColumns("Executing Agency: World Bank", 
                                SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                                GroupColumnModel.withSubColumns("Funding", 
                                        GroupColumnModel.withSubColumns("2013", 
                                                GroupColumnModel.withSubColumns("Real Disbursements", 
                                                        SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "245 000"),
                                                        SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "20 000")
                                                        ))),
                                GroupColumnModel.withSubColumns("Total Costs", 
                                                GroupColumnModel.withSubColumns("Real Disbursements", 
                                                        SimpleColumnModel.withContents("DN-EXEC", "Eth Water", "245 000"),
                                                        SimpleColumnModel.withContents("EXEC-IMPL", "Eth Water", "20 000")
                                )))
                        
                )); 
        
        runReportTest("by executing Directed Disbursements Report", "AMP-15337-real-disbursements-by-executing", new String[] {"Eth Water"}, by_exec_ddr_correct);
                
    }

    @Test
    public void testActualDisbursementsNotDoubleCounted()
    {
        GroupReportModel by_exec_ddr_correct = GroupReportModel.withGroupReports("AMP-15988-actual-disbursements-doublecounting-real-disbursements",
                GroupReportModel.withColumnReports("AMP-15988-actual-disbursements-doublecounting-real-disbursements",                  
                ColumnReportDataModel.withColumns("Donor Agency: Finland", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2013", 
                                    SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "20 000"),
                                    SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER)
                                    )),
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "20 000"),
                                SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER)
                        )),

                ColumnReportDataModel.withColumns("Donor Agency: Norway", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2013", 
                                    SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "110 000"),
                                    SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER)
                                    )),
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "110 000"),
                                SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER)
                        )),

                ColumnReportDataModel.withColumns("Donor Agency: USAID", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2013", 
                                    SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "415 000"),
                                    SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER)
                                    )),
                        GroupColumnModel.withSubColumns("Total Costs", 
                                SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "415 000"),
                                SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER)
                        ))                      
                )).withTrailCells(null, "545 000", "0", "545 000", "0");    
        
        runReportTest("actual disbursements on real-disbursements-containing activity doublecounting", "AMP-15988-actual-disbursements-doublecounting-real-disbursements", new String[] {"Eth Water"}, by_exec_ddr_correct);        
    }
    
    /**
     * "Donor Agency" column for an activity with multiple donors and intermediary donors
     */
    @Test
    public void testDonorAgencyFlat()
    {
        // ========================= one more report ===============================
        GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-16093-no-hier", 
                ColumnReportDataModel.withColumns("AMP-16093-no-hier", 
                        SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                        SimpleColumnModel.withContents("Donor Agency", "Eth Water", "[Finland, Norway, USAID]"),
                        GroupColumnModel.withSubColumns("Funding", 
                                GroupColumnModel.withSubColumns("2013", 
                                        SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER),
                                        SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000")
                                        )),
                        GroupColumnModel.withSubColumns("Total Costs", 
                                        SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER),
                                        SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000")
                        )));
        
        runReportTest("flat Donor Agency Report", "AMP-16093-no-hier", new String[] {"Eth Water"}, fddr_correct);
    }

    @Test
    public void testDonorAgencyHier()
    {
        // ========================= one more report ===============================
        GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-16093-with-hier", 
                GroupReportModel.withColumnReports("AMP-16093-with-hier", 
                        ColumnReportDataModel.withColumns("Donor Agency: Finland", 
                                SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                                GroupColumnModel.withSubColumns("Funding", 
                                        GroupColumnModel.withSubColumns("2013", 
                                                SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER),
                                                SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "20 000"))),
                                    GroupColumnModel.withSubColumns("Total Costs", 
                                        SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER),
                                        SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "20 000"))), 
                                        
                        ColumnReportDataModel.withColumns("Donor Agency: Norway", 
                                SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                                    GroupColumnModel.withSubColumns("Funding", 
                                        GroupColumnModel.withSubColumns("2013", 
                                            SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER),
                                                    SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "110 000"))),
                                        GroupColumnModel.withSubColumns("Total Costs", 
                                            SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER),
                                            SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "110 000"))),

                        ColumnReportDataModel.withColumns("Donor Agency: USAID", 
                                SimpleColumnModel.withContents("Project Title", NULL_PLACEHOLDER),
                                    GroupColumnModel.withSubColumns("Funding", 
                                        GroupColumnModel.withSubColumns("2013", 
                                            SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER),
                                                    SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "415 000"))),
                                        GroupColumnModel.withSubColumns("Total Costs", 
                                            SimpleColumnModel.withContents("Actual Commitments", NULL_PLACEHOLDER),
                                            SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "415 000")))
                                            
            ));
        
        runReportTest("hier Donor Agency Report", "AMP-16093-with-hier", new String[] {"Eth Water"}, fddr_correct);
    }

}

