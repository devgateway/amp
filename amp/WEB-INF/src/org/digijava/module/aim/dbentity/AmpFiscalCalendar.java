package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.nireports.TranslatedDate;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;
import org.digijava.module.aim.helper.fiscalcalendar.EthiopianBasedWorker;
import org.digijava.module.aim.helper.fiscalcalendar.EthiopianFiscalBasedWorker;
import org.digijava.module.aim.helper.fiscalcalendar.GregorianBasedWorker;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.digijava.module.aim.helper.fiscalcalendar.NepaliBasedWorker;
import org.digijava.module.aim.util.Identifiable;

public class AmpFiscalCalendar implements Serializable, Identifiable, OrgProfileValue, CalendarConverter {
    
    //IATI-check: to be ignored
//  @Interchangeable(fieldTitle="Fiscal Calendar ID")
    private Long ampFiscalCalId;
//  @Interchangeable(fieldTitle="Start Month Number")
    private Integer startMonthNum;
//  @Interchangeable(fieldTitle="Year Offset")
    private Integer yearOffset;
//  @Interchangeable(fieldTitle="Start Day Number")
    private Integer startDayNum;
//  @Interchangeable(fieldTitle="Name")
    private String name;
//  @Interchangeable(fieldTitle="Description")
    private String description;
//  @Interchangeable(fieldTitle="Base Calendar")
    private String baseCal;
//  @Interchangeable(fieldTitle="Is Fiscal")
    private Boolean isFiscal; // This indicates whether calendar is fiscal or

    // not.
    
    private Set<AmpCurrency> constantCurrencies;

    /**
     * @return
     */
    public Long getAmpFiscalCalId() {
        return ampFiscalCalId;
    }

    /**
     * @return
     */
    public Integer getStartDayNum() {
        return startDayNum;
    }

    /**
     * @return
     */
    public Integer getStartMonthNum() {
        return startMonthNum;
    }

    /**
     * @return
     */
    public Integer getYearOffset() {
        return yearOffset;
    }

    /**
     * @param long1
     */
    public void setAmpFiscalCalId(Long long1) {
        ampFiscalCalId = long1;
    }

    /**
     * @param i
     */
    public void setStartDayNum(Integer i) {
        startDayNum = i;
    }

    /**
     * @param i
     */
    public void setStartMonthNum(Integer i) {
        startMonthNum = i;
    }

    /**
     * @param i
     */
    public void setYearOffset(Integer i) {
        yearOffset = i;
    }

    public String getBaseCal() {
        return baseCal;
    }

    public void setBaseCal(String string) {
        baseCal = string;
    }

    public String getName() {
        return name;
    }

    public void setName(String string) {
        name = string;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String string) {
        description = string;
    }

    public Long getIdentifier() {
        return this.getAmpFiscalCalId();
    }

    public String toString() {
        return name;
    }

    @JsonIgnore
    public ICalendarWorker getworker() {
        if (this.getBaseCal().equalsIgnoreCase("GREG-CAL")) {
            return new GregorianBasedWorker(this);
        } else if (this.getBaseCal().equalsIgnoreCase("NEP-CAL")) {
            return new NepaliBasedWorker(this);

        } else if (this.getBaseCal().equalsIgnoreCase("ETH-CAL")) {
            if (isFiscal)
                return new EthiopianFiscalBasedWorker(this);
            else
                return new EthiopianBasedWorker(this);
        }
        return null;

    }

    @Override
    public boolean getIsFiscal() {
        return isFiscal == null ? false : isFiscal.booleanValue();
    }

    public void setIsFiscal(Boolean isFiscal) {
        this.isFiscal = isFiscal;
    }
    @Override
    public List<ValueTranslatabePair> getValuesForOrgReport(){
        List<ValueTranslatabePair> values=new ArrayList<ValueTranslatabePair>();
        ValueTranslatabePair value=new ValueTranslatabePair(Arrays.asList(new String[]{getName()}),false);
        values.add(value);
        return values;
    }

    @Override
    public String[] getSubHeaders() {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * @return the constantCurrencies
     */
    @JsonIgnore
    public Set<AmpCurrency> getConstantCurrencies() {
        return constantCurrencies;
    }

    /**
     * @param constantCurrencies the constantCurrencies to set
     */
    public void setConstantCurrencies(Set<AmpCurrency> constantCurrencies) {
        this.constantCurrencies = constantCurrencies;
    }

    @Override
    public TranslatedDate translate(Date date, String prefix) {
        ICalendarWorker worker = this.getworker();
        worker.setTime(date);
        try {
            return new TranslatedDate(worker.getYear(), worker.getFiscalYear(prefix), worker.getQuarter(), 
                    worker.getMonth().getMonthId(), worker.getMonth().getMonthStr());
        }
        catch(Exception e) {throw AlgoUtils.translateException(e);}
    }

    @Override
    public String getDefaultFiscalYearPrefix() {
        return this.getworker().getDefaultFiscalPrefix();
    }
    
    @Override
    public int parseYear(String year, String prefix) {
        return this.getworker().parseYear(year, prefix);
    }
    
    @Override
    public int parseYear(String year) {
        String prefix = TranslatorWorker.translateText(this.getworker().getDefaultFiscalPrefix());
        return this.getworker().parseYear(year, prefix);
    }

}
