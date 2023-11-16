package org.dgfoundation.amp.ar.legacy;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.testutils.ReportTestingUtils;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.admin.helper.AmpActivityFake;
import org.digijava.module.aim.ar.util.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.ActivityForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.*;
import org.junit.Test;

import java.util.*;

public class MultilingualTests extends ReportsTestCase {

    protected List<String> loadSectors(List<Long> ids){
        List<AmpSector> sectors = PersistenceManager.getSession().createQuery("from " + AmpSector.class.getName() + " s").list();
        List<String> res = new ArrayList<>();
        for(AmpSector sec:sectors){
            res.add(sec.getName());
        }
        return res;
    }

    @Test
    public void testTopSectorNames()
    {
        List<Long> sectorIds = Arrays.asList(new Long[] {6476L, 6482L, 6488L, 6493L, 6480L});
        
        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
        
        List<String> sectorNamesEn = loadSectors(sectorIds);
        
        TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
        List<String> sectorNamesRu = loadSectors(sectorIds);
        
        assertTrue(sectorNamesEn.contains("3 NATIONAL COMPETITIVENESS"));
        assertTrue(sectorNamesEn.contains("02 TRANSDNISTRIAN CONFLICT"));
        assertTrue(sectorNamesEn.contains("4 HUMAN RESOURCES"));

        assertTrue(sectorNamesRu.contains("4 ЧЕЛОВЕЧЕСКИЕ РЕСУРСЫ"));
        assertTrue(sectorNamesRu.contains("3 НАЦИОНАЛЬНАЯ КОНКУРЕНТНОСПОСОБНОСТЬ"));
        assertTrue(sectorNamesRu.contains("02 ПРИДНЕСТРОВСКИЙ КОНФЛИКТ"));

        //System.out.println(sectorNamesEn.size() + sectorNamesRu.size());
    }

    @Test
    public void testActivityDonorNames(){
        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
        Set<String> donorNamesEn =
                new HashSet<String>(
                        PersistenceManager.getSession().createQuery("SELECT " + AmpOrganisation.hqlStringForName("org") + " FROM " + AmpOrganisation.class.getName() + " org").list()
                    );

        TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
        Set<String> donorNamesRu =
                new HashSet<String>(
                        PersistenceManager.getSession().createQuery("SELECT " + AmpOrganisation.hqlStringForName("org") + " FROM " + AmpOrganisation.class.getName() + " org").list()
                    );

        assertEquals(donorNamesEn.size(), donorNamesRu.size());
        assertTrue(donorNamesEn.contains("Finland"));
        assertTrue(donorNamesRu.contains("Финляндия"));
        assertTrue(donorNamesEn.contains("World Bank"));
        assertTrue(donorNamesRu.contains("Всемирный банк"));
    }

    @Test
    public void testAmpActivityResume()
    {
        //public static Map<Long, Object[]> getAllTeamAmpActivitiesResume(Long teamId, boolean includedraft, String keyword, String...fieldsList) {
        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
        Map<Long, Object[]> actResumesEn = TeamUtil.getAllTeamAmpActivitiesResume(4L, true, null, "ampActivityId", "name");
        TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
        Map<Long, Object[]> actResumesRu = TeamUtil.getAllTeamAmpActivitiesResume(4L, true, null, "ampActivityId", "name");
        
        assertEquals("Project with documents", actResumesEn.get(23L)[1]);
        assertEquals("Проект с документами", actResumesRu.get(23L)[1]);
    }

    @Test
    public void testGetOrgsByRole()
    {
        // we actually just test that we don't crash + couple of sanity checks
        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
        Set<String> orgNamesEn = collectOrgNames(ReportsUtil.getAllOrgByRoleOfPortfolio("DN"));
        
        TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
        Set<String> orgNamesRu = collectOrgNames(ReportsUtil.getAllOrgByRoleOfPortfolio("DN"));
    
        assertTrue(orgNamesEn.contains("Finland"));
        assertTrue(orgNamesEn.contains("Ministry of Economy"));
        assertTrue(orgNamesEn.contains("Ministry of Finance"));
        assertTrue(orgNamesEn.contains("USAID"));
        
        assertTrue(orgNamesRu.contains("Всемирный банк"));
        assertTrue(orgNamesRu.contains("Министерство финансов"));
        assertTrue(orgNamesRu.contains("Норвегия"));
        ////System.out.println("msh " + orgNamesEn.size() + ", " + orgNamesRu.size());
    }

    @Test
    public void testGetOrgGroups()
    {
        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
        Set<String> allEnGroups = collectOrgGrpNames(DbUtil.getAllOrgGroups());
        assertTrue(allEnGroups.contains("American"));
        assertTrue(allEnGroups.contains("European"));
        assertTrue(allEnGroups.contains("International"));
        assertTrue(allEnGroups.contains("National"));
        assertFalse(allEnGroups.contains("Американская"));
        assertFalse(allEnGroups.contains("Европейская"));
        assertFalse(allEnGroups.contains("Международная"));
        assertFalse(allEnGroups.contains("Национальная"));

        
        TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
        Set<String> allRuGroups = collectOrgGrpNames(DbUtil.getAllOrgGroups());
        assertTrue(allRuGroups.contains("Американская"));
        assertTrue(allRuGroups.contains("Европейская"));
        assertTrue(allRuGroups.contains("Международная"));
        assertTrue(allRuGroups.contains("Национальная"));
        assertFalse(allRuGroups.contains("American"));
        assertFalse(allRuGroups.contains("European"));
        assertFalse(allRuGroups.contains("International"));
        assertFalse(allRuGroups.contains("National"));

        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");      
        assertNotNull(DbUtil.getAmpOrgGroupByName("American"));
        assertNotNull(DbUtil.getAmpOrgGroupByName("International"));
        assertNull(DbUtil.getAmpOrgGroupByName("dummy_nonexisting"));

        TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");      
        assertNotNull(DbUtil.getAmpOrgGroupByName("Американская"));
        assertNotNull(DbUtil.getAmpOrgGroupByName("Международная"));
        assertNull(DbUtil.getAmpOrgGroupByName("dummy_nonexisting"));
    }

    @Test
    public void testGetOrgTypes()
    {
        //DbUtil.getAllOrgTypes(); nothing to test - superfluous
    }
    
    public Set<String> collectOrgGrpNames(Collection<AmpOrgGroup> in)
    {
        Set<String> res = new LinkedHashSet<String>();
        for(AmpOrgGroup aaf:in)
            res.add(aaf.getOrgGrpName());
        return res;
    }
    
    public Set<String> collectOrgNames(Collection<AmpOrganisation> in)
    {
        Set<String> res = new LinkedHashSet<String>();
        for(AmpOrganisation aaf:in)
            res.add(aaf.getName());
        return res;
    }
    
    public Set<String> collectNames(List<AmpActivityFake> in)
    {
        Set<String> res = new LinkedHashSet<String>();
        for(AmpActivityFake aaf:in)
            res.add(aaf.getName());
        return res;
    }

    @Test
    public void testComponentsSearching()
    {
        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
        assertTrue(ComponentsUtil.checkComponentNameExists("First Component", 1L));
        assertFalse(ComponentsUtil.checkComponentNameExists("Первый подпроект", 1L));

        TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
        assertFalse(ComponentsUtil.checkComponentNameExists("First Component", 1L));        
        assertTrue(ComponentsUtil.checkComponentNameExists("Первый подпроект", 1L));
    }

    @Test
    public void testActivitiesSearching()
    {
        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
        Set<String> actiesEn = collectNames(ActivityUtil.getAllActivitiesAdmin(null, null, ActivityForm.DataFreezeFilter.ALL));
        assertTrue(actiesEn.contains("Eth Water"));
        assertTrue(actiesEn.contains("Proposed Project Cost 1 - USD"));
        assertTrue(actiesEn.contains("mtef activity 2"));
        assertFalse(actiesEn.contains("Вода Eth"));
        assertFalse(actiesEn.contains("Предполагаемая стоймость проекта 1 - USD"));
        assertFalse(actiesEn.contains("Проект МТЕФ 2"));    

        
        TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
        Set<String> actiesRu = collectNames(ActivityUtil.getAllActivitiesAdmin(null, null, ActivityForm.DataFreezeFilter.ALL));
        assertTrue(actiesRu.contains("Вода Eth"));
        assertTrue(actiesRu.contains("Предполагаемая стоймость проекта 1 - USD"));
        assertTrue(actiesRu.contains("Проект МТЕФ 2")); 
        assertFalse(actiesRu.contains("Eth Water"));
        assertFalse(actiesRu.contains("Proposed Project Cost 1 - USD"));
        assertFalse(actiesRu.contains("mtef activity 2"));
        
        
        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
        actiesEn = collectNames(ActivityUtil.getAllActivitiesAdmin("mtef", null, ActivityForm.DataFreezeFilter.ALL));
        assertTrue(actiesEn.contains("mtef activity 2"));
        assertTrue(actiesEn.contains("mtef activity 1"));
        assertTrue(actiesEn.contains("Pure MTEF Project"));
        assertTrue(actiesEn.contains("Test MTEF directed"));
        assertFalse(actiesEn.contains("Проект МТЕФ 2"));
        assertFalse(actiesEn.contains("Проект МТЕФ 1"));
        assertFalse(actiesEn.contains("Тест направленных МТЕФ"));
        assertFalse(actiesEn.contains("Чисто-МТЕФ-Проект"));

        
        TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
        actiesRu = collectNames(ActivityUtil.getAllActivitiesAdmin("МТЕФ", null, ActivityForm.DataFreezeFilter.ALL));
        assertTrue(actiesRu.contains("Проект МТЕФ 2"));
        assertTrue(actiesRu.contains("Проект МТЕФ 1")); 
        assertTrue(actiesRu.contains("Тест направленных МТЕФ"));    
        assertTrue(actiesRu.contains("Чисто-МТЕФ-Проект")); 
        
        assertFalse(actiesRu.contains("mtef activity 2"));
        assertFalse(actiesRu.contains("mtef activity 1"));
        assertFalse(actiesRu.contains("Pure MTEF Project"));
        assertFalse(actiesRu.contains("Test MTEF directed"));       
    }

    @Test
    public void testActivitiesLoading() throws Exception
    {
        TeamMember member = new TeamMember(TeamMemberUtil.getAmpTeamMember(12L));// ATL in "test workspace"
        
        TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
        String z = Util.toCSString(Arrays.asList(ActivityUtil.loadActivitiesNamesAndIds(member)));      
        assertEquals(true, z.contains("Проект МТЕФ 2"));
        assertEquals(true, z.contains("проект с подпроектами"));
        assertEquals(true, z.contains("Предполагаемая стоймость проекта 1 - USD"));
        
        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
        z = Util.toCSString(Arrays.asList(ActivityUtil.loadActivitiesNamesAndIds(member)));     
        assertEquals(true, z.contains("mtef activity 2"));
        assertEquals(true, z.contains("activity with components"));
        assertEquals(true, z.contains("Proposed Project Cost 1 - USD"));
         //public static String[] loadActivitiesNamesAndIds(TeamMember member) throws DgException{
    }

    @Test
    public void testRewriteQuery() throws Exception
    {
        Map<String, String> renames = new HashMap<String, String>() {{put("name", "translateReportName()");put("report_description", "translateReportDescription()");}};
        String res = SQLUtils.rewriteQuery("amp_reports", "r", renames);
        String corQuery = "r.amp_report_id, translateReportName() AS name, r.options, "
                + "translateReportDescription() AS report_description, r.type, r.hide_activities, r.drilldown_tab, "
                + "r.publicreport, r.workspacelinked, r.budget_exporter, r.allow_empty_fund_cols, r.ownerid, "
                + "r.cv_activity_level, r.report_category, r.updated_date, r.published_date, r.also_show_pledges, "
                + "r.split_by_funding, r.show_original_currency";
        assertEquals(corQuery, res);
        
        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
        List<AmpComponent> comps = ActivityUtil.getComponents(ReportTestingUtils.getActivityIdByName("activity with components"));
        assertEquals(1, comps.size()); 
        assertEquals("First Component", comps.get(0).getTitle());
        assertEquals("First Component Description", comps.get(0).getDescription());
        
        TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
        comps = ActivityUtil.getComponents(ReportTestingUtils.getActivityIdByName("проект с подпроектами"));
        assertEquals(1, comps.size()); 
        assertEquals("Первый подпроект", comps.get(0).getTitle());
        assertEquals("Описание первого подпроекта", comps.get(0).getDescription());

//      TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
//      assertEquals("проект с подпроектами", ActivityUtil.getActivityByName("проект с подпроектами", null));
//      TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
//      assertEquals("activity with components", ActivityUtil.getActivityByName("activity with components", null));
    }

    @Test
    public void testReportsLookup() throws Exception
    {
//      TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
//      Map<Long, AmpReports> ruLanguageReports = buildMap(ARUtil.getAllPublicReports(false, null, null));
//      TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
    }

    @Test
    public void testStickyTranslations() throws Exception
    {
        for(int i = 0; i < 50; i++)
        {
            //TLSUtils.getThreadLocalInstance().setForcedLangCode(SiteUtils.getDefaultSite().getDefaultLanguage().getCode());
            TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
            String ruVer = ReportTestingUtils.getActivityName(2L);
            TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
            String enVer = ReportTestingUtils.getActivityName(2L);
            
            assertEquals("Вода Eth", ruVer);
            assertEquals("Eth Water", enVer);
        }
    }

    @Test
    public void testAmpActivityMappedCorrectly() throws Exception
    {
        TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
        String ruVer = ReportTestingUtils.getActivityName(24L);
        String ruVerView = ReportTestingUtils.getActivityName_notVersion(24L);
        
        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
        String enVer = ReportTestingUtils.getActivityName(24L);
        String enVerView = ReportTestingUtils.getActivityName_notVersion(24L);
        
        assertEquals("AMP-16257", "Вода Eth", ruVer);
        assertEquals("AMP-16257", "Eth Water", enVer);

        /** disabled because nobody will take care of this in the near future
        assertEquals("AMP-16257", "Вода Eth", ruVerView);
        assertEquals("AMP-16257", "Eth Water", enVerView);
        */
    }
}
