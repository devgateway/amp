package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.Output;

public class AmpAnnualProjectBudget implements Serializable, Versionable, Cloneable, Comparable {

    //IATI-check: to be ignored
    private static final Logger logger = Logger.getLogger(AmpAnnualProjectBudget.class);
    
    private Long ampAnnualProjectBudgetId;
    
    @Interchangeable(fieldTitle="Amount", importable = true)
    private Double amount;
    
    @Interchangeable(fieldTitle="Year", importable = true)
    private Date year;
    
    @Interchangeable(fieldTitle="AMP Activity", pickIdOnly = true)
    private AmpActivityVersion activity;
    
    @Interchangeable(fieldTitle="Currency", importable = true, pickIdOnly = true)
    protected AmpCurrency ampCurrencyId;

    public Long getAmpAnnualProjectBudgetId() {
        return ampAnnualProjectBudgetId;
    }

    public void setAmpAnnualProjectBudgetId(Long ampAnnualProjectBudgetId) {
        this.ampAnnualProjectBudgetId = ampAnnualProjectBudgetId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public AmpActivityVersion getActivity() {
        return activity;
    }

    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpAnnualProjectBudget aux = (AmpAnnualProjectBudget) obj;
        String original = "" + this.getAmount() + this.getYear().getYear();
        String copy = "" + +aux.getAmount() + aux.getYear().getYear();
        if (original.equals(copy)) {
            return true;
        }
        return false;
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(new Output(null, new String[] { "AnnualProjectBudget" }, new Object[] { this }));
        return out;
    }

    @Override
    public Object getValue() {
        return "" + this.getAmount() + " " + this.getYear().getYear();
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpAnnualProjectBudget aux = (AmpAnnualProjectBudget) clone();
        aux.activity = newActivity;
        aux.ampAnnualProjectBudgetId = null;
        return aux;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int compareTo(Object other) {
        AmpAnnualProjectBudget oth = (AmpAnnualProjectBudget) other;
        int cmpClass = this.getClass().getName().compareTo(oth.getClass().getName());
        if (cmpClass != 0)
            return cmpClass; // normally we shouldn't be getting entries of
                                // different classes

        Long id1 = this.getAmpAnnualProjectBudgetId();
        Long id2 = oth.getAmpAnnualProjectBudgetId();

        if (id1 == null) {
            if (id2 == null)
                return 0;
            return 1; // nulls go to the end
        }
        if (id2 == null)
            return -1; // nulls go to the end

        return id1.compareTo(id2);
    }

    @Override
    public boolean equals(Object other) {
        return this.compareTo(other) == 0;
    }
    
    public AmpCurrency getAmpCurrencyId() {
        return ampCurrencyId;
    }

    public void setAmpCurrencyId(AmpCurrency ampCurrencyId) {
        this.ampCurrencyId = ampCurrencyId;
    }
}
