package org.digijava.module.aim.dbentity;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.discriminators.FiscalYearPossibleValuesProvider;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.PossibleValues;

public class AmpAPIFiscalYear {

    protected Long id;
    
    @Interchangeable(fieldTitle = "Year", importable = true, uniqueConstraint = true, 
            required = ActivityEPConstants.REQUIRED_ALWAYS)
    @PossibleValues(FiscalYearPossibleValuesProvider.class)
    protected Long year;
    
    public AmpAPIFiscalYear() { }
    
    public AmpAPIFiscalYear(Long year) {
        super();
        this.id = year;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AmpAPIFiscalYear) {
            return year.equals(((AmpAPIFiscalYear) obj).getYear());
        }
        
        return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
}
