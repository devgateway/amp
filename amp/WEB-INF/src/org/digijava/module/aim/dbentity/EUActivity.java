/**
 *
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.Output;

/**
 * @author mihai
 */
public class EUActivity implements Serializable, Identifiable, Versionable, Cloneable {
    private static final long serialVersionUID = 7061222006441976421L;


    private Long id;
    private Date transactionDate;
    private String name;
    private String textId;
    private String inputs;
    private Double totalCost;
    private AmpCurrency totalCostCurrency;
    private Set contributions;
    private String assumptions;
    private String progress;
    private Date dueDate;
    private AmpActivityVersion activity;

    private Long desktopCurrencyId;


    public void setDesktopCurrencyId(Long desktopCurrencyId) {
        this.desktopCurrencyId = desktopCurrencyId;
    }


    public double getTotalContributionsConverted() throws AimException  {
        double res=0;
        Iterator i=contributions.iterator();
        while (i.hasNext()) {
            EUActivityContribution element = (EUActivityContribution) i.next();
            double val=CurrencyWorker.convertToUSD(element.getAmount().doubleValue(),element.getAmountCurrency().getCurrencyCode());
            res+=val;
        }
        double finalRes=CurrencyWorker.convertFromUSD(res,desktopCurrencyId);
        return finalRes;
    }


    public double getTotalCostConverted() throws AimException {
        double usdAmount = CurrencyWorker.convertToUSD(getTotalCost().doubleValue(),totalCostCurrency.getCurrencyCode());
        double finalAmount=CurrencyWorker.convertFromUSD(usdAmount,desktopCurrencyId);
        return finalAmount;
    }

    public AmpActivityVersion getActivity() {
        return activity;
    }


    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }


    /**
     *
     */
    public EUActivity() {
        super();
        contributions=new HashSet();
    }


    public String getAssumptions() {
        return assumptions;
    }


    public void setAssumptions(String assumptions) {
        this.assumptions = assumptions;
    }


    public Set getContributions() {
        return contributions;
    }


    public void setContributions(Set contributions) {
        this.contributions = contributions;
    }


    public Date getDueDate() {
        return dueDate;
    }


    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getInputs() {
        return inputs;
    }


    public void setInputs(String inputs) {
        this.inputs = inputs;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getProgress() {
        return progress;
    }


    public void setProgress(String progress) {
        this.progress = progress;
    }


    public String getTextId() {
        return textId;
    }


    public void setTextId(String textId) {
        this.textId = textId;
    }


    public Double getTotalCost() {
        return FeaturesUtil.applyThousandsForVisibility(totalCost);
    }


    public void setTotalCost(Double totalCost) {
        this.totalCost = FeaturesUtil.applyThousandsForEntry(totalCost);
    }


    public AmpCurrency getTotalCostCurrency() {
        return totalCostCurrency;
    }


    public void setTotalCostCurrency(AmpCurrency totalCostCurrency) {
        this.totalCostCurrency = totalCostCurrency;
    }


    public Object getIdentifier() {
        return id;
    }


    public Long getDesktopCurrencyId() {
        return desktopCurrencyId;
    }


    public Date getTransactionDate() {
        return transactionDate;
    }


    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }


    @Override
    public boolean equalsForVersioning(Object obj) {
        EUActivity aux = (EUActivity) obj;
        String original = this.name != null ? this.name : "";
        String copy = aux.name != null ? aux.name : "";
        if (original.equals(copy)) {
            return true;
        }
        return false;
    }


    @Override
    public Object getValue() {
        return this.name != null ? this.name : "";
    }


    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(
                new Output(null, new String[] { "Name" }, new Object[] { this.name != null ? this.name
                        : "Empty Name" }));
        return out;
    }


    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        EUActivity aux = (EUActivity) clone();
        aux.activity = newActivity;
        aux.id = null;
        
        if (aux.contributions != null && aux.contributions.size() > 0){
            Set<EUActivityContribution> set = new HashSet<EUActivityContribution>();
            Iterator<EUActivityContribution> i = aux.contributions.iterator();
            while (i.hasNext()) {
                EUActivityContribution newAC = (EUActivityContribution) i
                        .next().clone();
                newAC.setId(null);
                newAC.setEuActivity(aux);
                set.add(newAC);
            }
            aux.contributions = set;
        }
        else
            aux.contributions = null;
        
        return aux;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
    
}
