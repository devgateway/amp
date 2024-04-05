package org.dgfoundation.amp.testutils;

import org.dgfoundation.amp.Util;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapFilter;

import java.util.Arrays;
import java.util.List;

public class ActivityNameFilteringMapFilter extends MapFilter
{
    
    List<String> activityNames;
    
    public ActivityNameFilteringMapFilter(int transactionType, String... activityNames)
    {
        super();
        this.activityNames = Arrays.asList(activityNames);
        fillWithDefaultTestingValues(transactionType);
    }
    
    protected void fillWithDefaultTestingValues(int transactionType)
    {       
        this.setTransactionType(transactionType);
        this.setFiscalCalendarId(4L); // Gregorian Calendar
        this.setStartYear(1980L);
        this.setEndYear(2020L);
        this.setSelLocationIds(null);
        this.setZoneIds(null);
        this.setSelSectorIds(null);
        this.setSelStructureTypes(null);
        this.setSelprojectstatus(null);
        this.setSelLocationIds(null);
        this.setSelorganizationsTypes(null);
        this.setSelectedBudget(null);
        this.setSeltypeofassistence(null);
        this.setSelfinancingInstruments(null);
        this.setFromPublicView(null);
        this.setModeexport(false);
    }
    
    @Override
    public List<Long> buildFilteredActivitiesList()
    {
        try
        {
            String query = "SELECT amp_activity_id from amp_activity WHERE name IN (" + Util.toCSString(activityNames) + ")";
            return DbHelper.getInActivities(query);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}

