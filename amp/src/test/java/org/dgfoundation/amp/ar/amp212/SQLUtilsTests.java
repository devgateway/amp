package org.dgfoundation.amp.ar.amp212;

import java.util.LinkedHashMap;
import java.util.Set;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.testutils.AmpTestCase;

import org.junit.Test;

/**
 * ETL Testcases
 * @author Dolghier Constantin
 *
 */
public class SQLUtilsTests extends AmpTestCase {

    /**
     * tests that SQLUtils fetches column types in the correct order and with the correct column types
     */
    @Test
    public void testColumnTypeFetching() {
        LinkedHashMap<String, String> cols = SQLUtils.getTableColumnsWithTypes("dg_site_domain", true);
        assertEquals("{site_domain_id=bigint, site_domain=character varying, site_path=character varying, site_id=bigint, language_code=character varying, is_default=boolean, enable_security=boolean}", cols.toString());
        
        cols = SQLUtils.getTableColumnsWithTypes("v_regions", true);
        assertEquals("{amp_activity_id=bigint, region_name=character varying, region_id=bigint, percentage=real, cnt_nulls=bigint}", cols.toString());
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
