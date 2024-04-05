package org.dgfoundation.amp.ar.amp212;

import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.test.categories.DatabaseTests;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * ETL Testcases
 * @author Dolghier Constantin
 *
 */
@Category(DatabaseTests.class)
public class SQLUtilsTests extends AmpTestCase {

    @BeforeClass
    public static void beforeClass() {
        StandaloneAMPInitializer.initialize();
    }

    /**
     * tests that SQLUtils fetches column types in the correct order and with the correct column types
     */
    @Test
    public void testColumnTypeFetching() {
        LinkedHashMap<String, String> cols = SQLUtils.getTableColumnsWithTypes("dg_site_domain", true);
        assertEquals("{site_domain_id=bigint, site_domain=character varying, site_path=character varying, site_id=bigint, language_code=character varying, is_default=boolean, enable_security=boolean}", cols.toString());
        
        cols = SQLUtils.getTableColumnsWithTypes("v_adm_level_2", true);
        assertEquals("{amp_activity_id=bigint, adm_level_2_name=character varying, adm_level_2_id=bigint, percentage=real, cnt_nulls=bigint}", cols.toString());
    }
    
    /**
     * tests that SQLUtils fetches columns in correct order
     */
    @Test
    public void testColumnList() {
        Set<String> colNames = SQLUtils.getTableColumns("amp_role");
        assertEquals("[amp_role_id, role_code, name, type, description, language]", colNames.toString());
    }
}
