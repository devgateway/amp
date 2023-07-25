package org.dgfoundation.amp.ar.legacy;

import java.util.List;
import java.util.TreeMap;

import org.apache.struts.mock.MockHttpServletRequest;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.testutils.ReportTestingUtils;
import org.dgfoundation.amp.testutils.AmpRunnable;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.translation.util.MultilingualInputFieldValues;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * multilingual editor tests
 * @author Dolghier Constantin
 *
 */
public class MultilingualTests28 extends ReportsTestCase {

    private static List<String> locales;
    
    @Test
    public void testLoadingMultilingual()
    {
        MultilingualInputFieldValues mifv = new MultilingualInputFieldValues(AmpReports.class, 52L, "name", null, locales);
        assertEquals("AmpReports_name", mifv.getPrefix());
        assertEquals("name", mifv.getPropertyName());
        assertEquals("org.digijava.module.aim.dbentity.AmpReports", mifv.getClazz().getName());
        assertEquals("[en, fr, ru]", mifv.getLocales().toString()); // ORDER MATTERS
        assertEquals("{en=AMP-16415: English title, fr=AMP-16415: English title, ru=AMP-16415: Русское название}", 
                new TreeMap<String, String>(mifv.getTranslations()).toString());
    }

    @Test
    public void testLoadingMultilingualNotAllLanguages()
    {
        MultilingualInputFieldValues mifv = new MultilingualInputFieldValues(AmpReports.class, 53L, "name", null, locales);
        assertEquals("AmpReports_name", mifv.getPrefix());
        assertEquals("name", mifv.getPropertyName());
        assertEquals("org.digijava.module.aim.dbentity.AmpReports", mifv.getClazz().getName());
        assertEquals("[en, fr, ru]", mifv.getLocales().toString()); // ORDER MATTERS
        assertEquals("{en=AAA-English title, ru=ААА - Русское название}", 
                new TreeMap<String, String>(mifv.getTranslations()).toString());
    }
    
    /**
     * postgres is hanging on this testcase - investigate in better times why
     */
    @Test
    @Ignore
    public void testSerializationAllLanguagesFilled()
    {
        Session session = PersistenceManager.getSession();
        try
        {
            TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
            AmpReports victimReportEn = ReportTestingUtils.loadReportByName("victim_report");
        
            TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
            AmpReports victimReportRu = ReportTestingUtils.loadReportByName("дохлый отчет");
        
            assertEquals(Long.valueOf(54L), victimReportEn.getAmpReportId());
            assertEquals(Long.valueOf(54L), victimReportRu.getAmpReportId());
        
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addParameter("AmpReports_name_en", "english");
            request.addParameter("AmpReports_name_ru", "русский");
            MultilingualInputFieldValues.serialize(victimReportRu, "name", null, session, request);
            //System.out.println("done serializing");           
            
            // current language is now russian
            shouldFail(new AmpRunnable(){public void run(){
                    ReportTestingUtils.loadReportByName("victim_report");
                }});
            shouldFail(new AmpRunnable(){public void run(){
                    ReportTestingUtils.loadReportByName("дохлый отчет");
                }});
            //System.out.println("done testing 1");
            
            TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
            // current language is now english
            shouldFail(new AmpRunnable(){public void run(){
                    ReportTestingUtils.loadReportByName("victim_report");
                }});            
            shouldFail(new AmpRunnable(){public void run(){
                    ReportTestingUtils.loadReportByName("дохлый отчет");
                }});

            shouldFail(new AmpRunnable(){public void run(){
                    ReportTestingUtils.loadReportByName("русский");
                }});
                    
            //System.out.println("done testing 3");
            assertEquals(Long.valueOf(54L), ReportTestingUtils.loadReportByName("english"));
            
            TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
            assertEquals(Long.valueOf(54L), ReportTestingUtils.loadReportByName("русский"));
            
            //System.out.println("done testing 4");
        }
        finally
        {
            PersistenceManager.cleanupSession(session);
            session = PersistenceManager.getSession();
            session.createNativeQuery("UPDATE amp_reports SET name = 'victim_report' WHERE amp_report_id = 54").executeUpdate();
            session.createNativeQuery("DELETE FROM amp_content_translation WHERE object_id = 54 AND object_class like '%AmpReports'").executeUpdate();
            session.createNativeQuery("INSERT INTO amp_content_translation(id, object_class, object_id, field_name, locale, translation) VALUES " + 
                        "(nextval('amp_content_translation_seq'), 'org.digijava.module.aim.dbentity.AmpReports', 54, 'name', 'en', 'victim_report')").executeUpdate();
            session.createNativeQuery("INSERT INTO amp_content_translation(id, object_class, object_id, field_name, locale, translation) VALUES " + 
                    "(nextval('amp_content_translation_seq'), 'org.digijava.module.aim.dbentity.AmpReports', 54, 'name', 'ru', 'дохлый отчет')").executeUpdate();
            PersistenceManager.cleanupSession(session);
        }
        
        
        //MultilingualInputFieldValues.serialize(report, "name", null, mySession, request);
    }
    
    @Before
    public void setUp() {
        locales = TranslatorUtil.getLanguages();
    }
}
