package org.digijava.module.aim.util;

import static org.digijava.module.aim.util.DynLocationManagerUtil.ErrorCode.CORRECT_CONTENT;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

import java.util.List;

import com.google.common.collect.ImmutableList;
import org.digijava.kernel.AbstractIntegrationTest;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.DynLocationManagerForm;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.junit.Test;

public class DynLocationManagerUtilTest extends AbstractIntegrationTest {
    
    @Test
    public void testLocationImport() throws AimException {
        List<List<String>> rows = ImmutableList.of(
                ImmutableList.of("Database ID", "Administrative Level 0", "Administrative Level 1",
                        "Administrative Level 2", "Administrative Level 3", "Administrative Level 5",
                        "Latitude", "Longitude", "GeoID", "ISO", "ISO3"),
                ImmutableList.of("", "Disneyland", "", "", "", "", "0", "0", "", "DD", "DDD")
        );
        DynLocationManagerUtil.ErrorCode errorCode =
                DynLocationManagerUtil.importExcelFile(rows, DynLocationManagerForm.Option.NEW);
    
        assertEquals(CORRECT_CONTENT, errorCode);
    
        Integer cnt = (Integer) locationCriteria("Disneyland")
                .setProjection(Projections.rowCount())
                .uniqueResult();
        assertEquals((Integer) 1, cnt);
    }
    
    @Test
    public void testChangeParentLocation() throws AimException {
        
        initLocations();
    
        AmpCategoryValueLocations disneyland = getCategoryValueLocationByName("Disneyland");
        AmpCategoryValueLocations wonderland = getCategoryValueLocationByName("Wonderland");
        AmpCategoryValueLocations oz = getCategoryValueLocationByName("Oz");
        
        List<List<String>> updateList = ImmutableList.of(
                ImmutableList.of("Database ID", "Administrative Level 0", "Administrative Level 1",
                        "Administrative Level 2", "Administrative Level 3", "Administrative Level 5",
                        "Latitude", "Longitude", "GeoID", "ISO", "ISO3"),
                ImmutableList.of("" + disneyland.getId(), "Disneyland", "", "", "", "", "0", "0", "", "DD", "DDD"),
                ImmutableList.of("" + wonderland.getId(), "Wonderland", "", "", "", "", "0", "0", "", "WW", "WWW"),
                ImmutableList.of("" + oz.getId(), "Wonderland", "Oz", "", "", "", "0", "0", "", "", "")
        );
        DynLocationManagerUtil.ErrorCode errorCode =
                DynLocationManagerUtil.importExcelFile(updateList, DynLocationManagerForm.Option.OVERWRITE);


        assertEquals(CORRECT_CONTENT, errorCode);
    
        disneyland = (AmpCategoryValueLocations) locationCriteria("Disneyland").uniqueResult();
        wonderland = (AmpCategoryValueLocations) locationCriteria("Wonderland").uniqueResult();
        oz = (AmpCategoryValueLocations) locationCriteria("Oz").uniqueResult();
        
        assertEquals(0, disneyland.getChildLocations().size());
        assertThat(wonderland.getChildLocations(), containsInAnyOrder(oz));
        assertEquals(wonderland, oz.getParentLocation());

    }
    
    private void initLocations() {
        AmpCategoryValue country = CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.getAmpCategoryValueFromDB();
        AmpCategoryValue region = CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1.getAmpCategoryValueFromDB();
    
        AmpCategoryValueLocations disneyLand = new AmpCategoryValueLocations();
        disneyLand.setName("Disneyland");
        disneyLand.setParentCategoryValue(country);
        disneyLand.setGsLat("0");
        disneyLand.setGsLong("0");
        disneyLand.setIso("DD");
        disneyLand.setIso3("DDD");
    
        AmpCategoryValueLocations wonderLand = new AmpCategoryValueLocations();
        wonderLand.setName("Wonderland");
        wonderLand.setParentCategoryValue(country);
        wonderLand.setGsLat("0");
        wonderLand.setGsLong("0");
        wonderLand.setIso("WW");
        wonderLand.setIso3("WWW");
    
        AmpCategoryValueLocations oz = new AmpCategoryValueLocations();
        oz.setName("Oz");
        oz.setParentCategoryValue(region);
        oz.setGsLat("0");
        oz.setGsLong("0");
        oz.setParentLocation(disneyLand);
        
        disneyLand.getChildLocations().add(oz);
        
        PersistenceManager.getSession().save(disneyLand);
        PersistenceManager.getSession().save(wonderLand);
        PersistenceManager.getSession().save(oz);
        
    }
    
    private AmpCategoryValueLocations getCategoryValueLocationByName(String name) {
        return (AmpCategoryValueLocations) locationCriteria(name).uniqueResult();
    }
    
    public Criteria locationCriteria(String name) {
        return PersistenceManager.getSession()
                .createCriteria(AmpCategoryValueLocations.class)
                .add(Property.forName("name").eq(name));
    }
}
