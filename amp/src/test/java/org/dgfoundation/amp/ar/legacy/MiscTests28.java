package org.dgfoundation.amp.ar.legacy;

import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.hibernate.jdbc.Work;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Pledges Form tests
 * @author Dolghier Constantin
 *
 */
public class MiscTests28 extends ReportsTestCase {

    @Test
    public void testComparingObjectVisibility()
    {
        AmpFieldsVisibility afv1 = new AmpFieldsVisibility();
        afv1.setId(2l);
        
        AmpFieldsVisibility afvNullId = new AmpFieldsVisibility();
        AmpFieldsVisibility afv2 = new AmpFieldsVisibility();
        afv2.setId(3l);
        
        assertEquals(0, afv1.compareTo(afv1));
        assertEquals(0, afvNullId.compareTo(afvNullId));
        assertTrue(afvNullId.compareTo(afv1) > 0);
        assertTrue(afv1.compareTo(afvNullId) < 0);
        
        assertTrue(afv1.compareTo(afv2) < 0);
        assertTrue(afv2.compareTo(afv1) > 0);
        
        AmpFieldsVisibility[] fields = new AmpFieldsVisibility[] {afv1, afv2, afvNullId};
        for(int i = 0; i < fields.length; i++)
            for(int j = 0; j < fields.length; j++)
            {
                assertEquals(i == j, fields[i].equals(fields[j]));
                assertEquals(i == j, fields[j].equals(fields[i]));
            }
    }

    @Test
    public void testComparingObjectVisibilityInterClass()
    {
        AmpObjectVisibility[] fields = new AmpObjectVisibility[] {new AmpFieldsVisibility(), new AmpFeaturesVisibility(), new AmpModulesVisibility(), new AmpTemplatesVisibility()};
        for(int i = 0; i < fields.length; i++)
            fields[i].setId((i + 1) * 3l);
        
        for(int i = 0; i < fields.length; i++)
            for(int j = 0; j < fields.length; j++)
            {
                assertEquals(fields[i].getClass().getName().compareTo(fields[j].getClass().getName()), fields[i].compareTo(fields[j]));
                assertEquals(fields[j].getClass().getName().compareTo(fields[i].getClass().getName()), fields[j].compareTo(fields[i]));
                assertEquals(i == j, fields[i].equals(fields[j]));
                assertEquals(j == i, fields[j].equals(fields[i]));
            }
    }

    @Test
    public void testProgramLevelsViews() {
        PersistenceManager.getSession().doWork(new Work() {

            @Override
            public void execute(Connection connection) throws SQLException {                
            try(RsInfo rsi = SQLUtils.rawRunQuery(connection, "SELECT * from all_programs_with_levels where amp_theme_id IN (1, 4, 8)", null)) {                
                ResultSet rs = rsi.rs;
                while (rs.next()) {
                    int themeId = rs.getInt("amp_theme_id");
                    switch (themeId) {
                    case 1:
                        assertEquals(rs.getString("name"), "Program #1");
                        assertEquals(rs.getInt("id0"), 1);
                        assertEquals(rs.getInt("id1"), 0); // zero stands for null
                        assertEquals(rs.getInt("id2"), 0);
                        break;
                        
                    case 4:
                        assertEquals(rs.getString("name"), "Older Program");
                        assertEquals(rs.getInt("id0"), 4);
                        assertEquals(rs.getInt("id1"), 0); // zero stands for null
                        assertEquals(rs.getInt("id2"), 0);
                        break;                      
                        
                    case 8:
                        assertEquals(rs.getString("name"), "OP112 name");
                        assertEquals(rs.getInt("id0"), 4);
                        assertEquals(rs.getInt("id1"), 5); // zero stands for null
                        assertEquals(rs.getInt("id2"), 6);
                        assertEquals(rs.getInt("id3"), 8);
                        assertEquals(rs.getInt("id4"), 0);
                        break;                      
                    default:
                        fail("should not have a row with amp_theme_id of " + themeId);
                    }
                }
            }
            }           
        });
    }

    @Test
    public void testProgramLevelsFunctions() {
        PersistenceManager.getSession().doWork(new Work() {

            protected void checkSingleValue(java.sql.Connection conn, Long v, String query) throws SQLException {
                try(RsInfo rs = SQLUtils.rawRunQuery(conn, query, null)) {
                    if (!rs.rs.next())
                        fail("query returned no results");
                    Long l = rs.rs.getLong(1);
                    Object obj = rs.rs.getObject(1);
                    if (v == null) {
                        assertTrue("result should have been null but is " + obj + " for query " + query, obj == null);
                        return;
                    }
                    assertEquals("while running " + query, v.longValue(), l.longValue());
                }
            }
            
            @Override
            public void execute(Connection conn) throws SQLException {              
                checkSingleValue(conn, 0l, "select getprogramdepth(1)");
                checkSingleValue(conn, 1l, "select getprogramdepth(2)");
                checkSingleValue(conn, 1l, "select getprogramdepth(3)");
                checkSingleValue(conn, 3l, "select getprogramdepth(7)");
                
                checkSingleValue(conn, null, "select getprogramlevel(1, 1)");
                checkSingleValue(conn, 1l, "select getprogramlevel(1, 0)");
                
                checkSingleValue(conn, 4l, "select getprogramlevel(7, 0)");
                checkSingleValue(conn, 5l, "select getprogramlevel(7, 1)");
                checkSingleValue(conn, 6l, "select getprogramlevel(7, 2)");
                checkSingleValue(conn, 7l, "select getprogramlevel(7, 3)");
            }           
        });
    }
    
    @Before
    public void setUp() {
        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
    }
}
