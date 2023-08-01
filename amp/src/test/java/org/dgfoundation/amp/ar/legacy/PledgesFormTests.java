package org.dgfoundation.amp.ar.legacy;

import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.fundingpledges.action.DisableableKeyValue;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.PledgeForm;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Pledges Form tests
 * @author Dolghier Constantin
 *
 */
public class PledgesFormTests extends ReportsTestCase {

    @Test
    @Ignore
    public void testPledgeFormDocuments(){
        
        PledgeForm pledgeForm = new PledgeForm();
        pledgeForm.importPledgeData(PledgesEntityHelper.getPledgesById(3L));
        assertEquals(3, pledgeForm.getSelectedDocs().size());
//      assertShimEquals(pledgeForm.getSelectedDocsList().get(0), "aaaa.png", "документ проекта", "4e478d3e-41a4-4b35-b4de-52c07ecd9d5a", 480000);
//      assertShimEquals(pledgeForm.getSelectedDocsList().get(1), "AMP-17265-amp27.patch", "yahoo", "96e0a2be-53b0-4f16-bb02-a8bc7dc46778", 480000);
//      assertShimEquals(pledgeForm.getSelectedDocsList().get(2), "SSC Implementation Notes.doc", "some ssc implementation notes", "03b4ed7a-b4d7-4204-8a0b-613131edf9f0", 480000);
    }

    @Test
    @Ignore
    public void testPledgeFormFundingUtils(){
        PledgeForm pledgeForm = new PledgeForm();
        pledgeForm.importPledgeData(PledgesEntityHelper.getPledgesById(3L));
        assertEquals(3, pledgeForm.getSelectedFunding().size());
        assertEquals("[767 676 curr: 96, ToA: 2119, fundingYear: 2006, pledgeTypeId: 2137, 200 000 curr: 95, ToA: 2124, fundingYear: 2020, pledgeTypeId: 2137, 1 curr: 95, ToA: 2119, fundingYear: 2005, pledgeTypeId: 2137]", 
                pledgeForm.getSelectedFunding().toString());
    }

    @Test
    @Ignore
    public void testPledgeFormUtils(){
        PledgeForm pledgeForm = new PledgeForm();
        pledgeForm.importPledgeData(PledgesEntityHelper.getPledgesById(3L));
        
        // sectors
        testDisableableKeyValues(pledgeForm.getAllRootSectors(), new DisableableKeyValue(-1l, "Please select from below", true),
                new DisableableKeyValue(57l, "DAC 5 Sector Code", true),
                new DisableableKeyValue(58l, "Budget Classification", true));
        pledgeForm.setSelectedRootSector(58l);
        assertEquals("[enabled: KeyValue: (-1, Please select from below), enabled: KeyValue: (6480, 02 TRANSDNISTRIAN CONFLICT), enabled: KeyValue: (6475, 1-DEMOCRATIC COUNTRY), enabled: KeyValue: (6476, » 1.1 Democracy consolidation), enabled: KeyValue: (6477, » 1.2 Judiciary system), enabled: KeyValue: (6478, » 1.3 Corruption fight), enabled: KeyValue: (6479, » 1.4 Borders and law order), disabled: KeyValue: (6481, 3 NATIONAL COMPETITIVENESS), enabled: KeyValue: (6487, 4 HUMAN RESOURCES), enabled: KeyValue: (6488, » 4.1 Education), enabled: KeyValue: (6489, » 4.2 Health), enabled: KeyValue: (6490, » 4.3 Labor force), enabled: KeyValue: (6491, » 4.4 Social protection), disabled: KeyValue: (6492, 5 REGIONAL DEVELOPMENT)]",
                pledgeForm.getAllLegalSectors().toString());
        
        // programs
        testDisableableKeyValues(pledgeForm.getAllRootPrograms(), new DisableableKeyValue(-1l, "Please select from below", true),
                new DisableableKeyValue(4l, "Older Program", true),
                new DisableableKeyValue(1l, "Program #1", true));
        pledgeForm.setSelectedRootProgram(4l);
        assertEquals("[enabled: KeyValue: (-1, Please select from below), disabled: KeyValue: (4, Older Program), disabled: KeyValue: (5, » OP1 name), disabled: KeyValue: (6, » » OP11 name), disabled: KeyValue: (7, » » » OP111 name), enabled: KeyValue: (8, » » » OP112 name), enabled: KeyValue: (9, » OP2 name)]", 
                pledgeForm.getAllLegalPrograms().toString());
        
        // locations
        pledgeForm.setLevelId(69l);
        assertEquals("[KeyValue: (0, Please select from below), KeyValue: (77, Region), KeyValue: (78, Zone), KeyValue: (79, District)]", 
                pledgeForm.getAllValidImplementationLocationChoices().toString());
        pledgeForm.setImplemLocationLevel(78l);
        assertEquals("[enabled: KeyValue: (0, Please select from below), enabled: KeyValue: (9108, Bulboaca), enabled: KeyValue: (9109, Hulboaca), enabled: KeyValue: (9110, Dolboaca), disabled: KeyValue: (9111, Glodeni), disabled: KeyValue: (9112, Raureni), disabled: KeyValue: (9113, Apareni), disabled: KeyValue: (9114, Tiraspol), enabled: KeyValue: (9115, Slobozia), enabled: KeyValue: (9116, Camenca), disabled: KeyValue: (9120, AAA)]", 
                pledgeForm.getAllValidLocations().toString());      
    }

    @Test
    @Ignore
    public void testPledgeFundingCalculator(){
        FundingPledges pledge = PledgesEntityHelper.getPledgesById(3L);
        assertEquals("986879.40", String.format("%.2f", pledge.getTotalPledgedAmount("USD")));
        assertEquals("842399.95", String.format("%.2f", pledge.getTotalPledgedAmount("EUR"))); // approximate value
        
        pledge = PledgesEntityHelper.getPledgesById(4L);
        assertEquals("938069.75", String.format("%.2f", pledge.getTotalPledgedAmount("USD")));  // approximate value
        assertEquals("800999.00", String.format("%.2f", pledge.getTotalPledgedAmount("EUR")));
        
        pledge = PledgesEntityHelper.getPledgesById(5L);
        assertEquals("1061513.34", String.format("%.2f", pledge.getTotalPledgedAmount("USD")));  // approximate value
        assertEquals("780000.00", String.format("%.2f", pledge.getTotalPledgedAmount("EUR")));
    }

    @Test
    public void testFetchEntities()
    {
        List<FundingPledges> pledges = PledgesEntityHelper.fetchEntities(FundingPledges.class, "id", 3L);
        
        assertEquals(1, pledges.size());
        FundingPledges pledge = pledges.get(0);
        assertEquals("Test pledge 1", pledge.getTitleFreeText());
        assertNotNull(pledge.getTitle());
        assertEquals("ACVL Pledge Name 1", pledge.getTitle().getValue());
        assertEquals("European", pledge.getOrganizationGroup().getOrgGrpName());
        
        assertEquals(4, pledge.getSectorlist().size());
//      FundingPledgesSector fps = pledge.getSectorlist().iterator().next();
////        assertEquals("3 NATIONAL COMPETITIVENESS", fps.getSector().getName());
//      assertEquals(Float.valueOf(100.0f), fps.getSectorpercentage());
        
        assertEquals(3, pledge.getLocationlist().size());
        List<FundingPledgesLocation> locs = new ArrayList<>(pledge.getLocationlist());
        Collections.sort(locs, new Comparator<FundingPledgesLocation>(){
                    @Override public int compare(FundingPledgesLocation loc1, FundingPledgesLocation loc2)
                    {
                        return loc1.getLocation().getName().compareTo(loc2.getLocation().getName());
                    }});
        assertEquals("Balti County", locs.get(0).getLocation().getName());
        assertEquals("Cahul County", locs.get(1).getLocation().getName());
        assertEquals("Tiraspol", locs.get(2).getLocation().getName());
        
        TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
        pledges = PledgesEntityHelper.fetchEntities(FundingPledges.class, "id", 3L);
        
        assertEquals(1, pledges.size());
        pledge = pledges.get(0);
        assertEquals("Test pledge 1", pledge.getTitleFreeText());
        assertNotNull(pledge.getTitle());
        assertEquals("ACVL Pledge Name 1", pledge.getTitle().getValue());
        assertEquals("Европейская", pledge.getOrganizationGroup().getOrgGrpName());
        
        AmpOrganisation org = (AmpOrganisation) PledgesEntityHelper.fetchEntities(AmpOrganisation.class, "ampOrgId", 21698L, "name", "Finland").get(0);
        assertEquals(Long.valueOf(21698L), org.getAmpOrgId());
        assertEquals("Финляндия", org.getName());
        
        assertEquals(0, PledgesEntityHelper.fetchEntities(AmpOrganisation.class, "ampOrgId", 12L, "name", "Finland").size());
    }

    @Test
    public void testPledgesEntityHelperSanity()
    {
        FundingPledges pledge = PledgesEntityHelper.getPledgesById(3L);
        assertTrue(PledgesEntityHelper.getPledges().size() > 0); // more like a check on non-crashing
        assertTrue(PledgesEntityHelper.getFundingRelatedToPledges(pledge).size() == 0);
        assertEquals(1, PledgesEntityHelper.getPledgesByDonorGroup(18L).size());
        assertEquals("Test pledge 1", PledgesEntityHelper.getPledgesByDonorGroup(18L).get(0).getTitleFreeText());
        assertEquals(0, PledgesEntityHelper.getPledgesByDonor(21696L).size());
        assertEquals(0, PledgesEntityHelper.getPledgesByDonorAndTitle(21696L, "Test pledge 1").size());
        assertEquals(0, PledgesEntityHelper.getPledgesByDonorAndTitle(2L, "Test pledge 1").size());
        assertEquals(0, PledgesEntityHelper.getPledgesByDonorAndTitle(21696L, "Test pledge 321").size());
        assertEquals(3, PledgesEntityHelper.getPledgesDetails(3L).size());
        assertEquals(3, PledgesEntityHelper.getPledgesLocations(3L).size());
        assertEquals(2, PledgesEntityHelper.getPledgesPrograms(3L).size());
        assertEquals(4, PledgesEntityHelper.getPledgesSectors(3L).size());
        
        // non-existant pledge id       
        assertEquals(0, PledgesEntityHelper.getPledgesDetails(3333333L).size());
        assertEquals(0, PledgesEntityHelper.getPledgesLocations(3333333L).size());
        assertEquals(0, PledgesEntityHelper.getPledgesPrograms(3333333L).size());
        assertEquals(0, PledgesEntityHelper.getPledgesSectors(333333L).size());

    }
    
    @Before
    public void setUp() {
        StandaloneAMPInitializer.populateMockRequest();

        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");

        FeaturesUtil.overriddenFields.put("Use Free Text", true);
    }
    
}
