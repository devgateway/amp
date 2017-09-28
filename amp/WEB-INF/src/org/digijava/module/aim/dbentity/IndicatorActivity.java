package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.Output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Project Indicator.
 * This is connection between indicator and activity. Most fields are in parent class.
 * Check hibernate mapping in IndicatorConnection.hbm.xml 
 * @see IndicatorConnection
 * @author Irakli Kobiashvili
 *
 */
public class IndicatorActivity extends IndicatorConnection implements Versionable, Cloneable{

    //IATI-check: to be ignored
    private static final long serialVersionUID = 2L;
    
    /**
     * Activity
     */
//  @Interchangeable(fieldTitle="Activity", recursive=true)
    private AmpActivityVersion activity;
    
    /**
     * Indicator risk.
     * Actually risk is in each connection of indicator and activity.
     */
//  @Interchangeable(fieldTitle="Risk", importable = true)
    private AmpIndicatorRiskRatings risk;
        

    public AmpActivityVersion getActivity() {
        return activity;
    }

    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }

    public AmpIndicatorRiskRatings getRisk() {
        return risk;
    }

    public void setRisk(AmpIndicatorRiskRatings risk) {
        this.risk = risk;
    }
    
    @Override
    public boolean equals(Object obj) {
        IndicatorActivity ia = (IndicatorActivity) obj; 
        return getId().compareTo(ia.getId()) == 0;
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        IndicatorActivity aux = (IndicatorActivity) obj;
        return aux.getIndicator().getIndicatorId() == getIndicator().getIndicatorId();
    }

    @Override
    public Object getValue() {
        String value = "";
        if (getIndicator() != null)
            value += getIndicator().getName();
        if (risk != null)
            value += risk.getRatingName();
        if (values != null){
            ArrayList<AmpIndicatorValue> list = new ArrayList<AmpIndicatorValue>(values);
            Collections.sort(list, new Comparator<AmpIndicatorValue>() {
                @Override
                public int compare(AmpIndicatorValue arg0,
                        AmpIndicatorValue arg1) {
                    return arg0.getValueType() - arg1.getValueType();
                }
            });
            
            Iterator<AmpIndicatorValue> it = list.iterator();
            while (it.hasNext()) {
                AmpIndicatorValue ind = (AmpIndicatorValue) it
                        .next();
                value += ind.getValueType() + "" + ind.getValue() + "" + ind.getValueDate();
                if (ind.getLogFrame() != null)
                    value += ind.getLogFrame().getValue();
            }
        }
        
        return value;
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(
                new Output(null, new String[] { "Name" }, new Object[] { this.getIndicator() != null ? this.getIndicator().getName()
                        : "Empty Name" }));
        if (risk != null)
            out.getOutputs().add(new Output(null, new String[] {" Risk rating"}, new Object[] {risk.getRatingName()}));
        if (values != null){
            Iterator<AmpIndicatorValue> it = values.iterator();
            while (it.hasNext()) {
                AmpIndicatorValue ind = (AmpIndicatorValue) it
                        .next();
                String typeString = "";
                switch (ind.getValueType()) {
                case AmpIndicatorValue.BASE:
                    typeString += "Base Value";
                    break;
                case AmpIndicatorValue.ACTUAL:
                    typeString += "Actual Value";
                    break;
                case AmpIndicatorValue.REVISED:
                    typeString += "Revised Value";
                    break;
                case AmpIndicatorValue.TARGET:
                    typeString += "Target Value";
                    break;
                default:
                    typeString += "Unknown Value";
                    break;
                }
                //typeString += ":&nbsp;";
                out.getOutputs().add(
                        new Output(null, new String[] {typeString}, new Object[]{ind.getValue()}));
            }
        }
        return out;
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        IndicatorActivity aux = (IndicatorActivity) clone();
        aux.activity = newActivity;
        aux.setId(null);
        
        if (aux.values != null && aux.values.size() > 0){
            HashSet<AmpIndicatorValue> set = new HashSet<AmpIndicatorValue>();
            Iterator<AmpIndicatorValue> i = aux.values.iterator();
            while (i.hasNext()) {
                AmpIndicatorValue ampIndicatorValue = (AmpIndicatorValue) i.next().clone();
                ampIndicatorValue.setIndValId(null);
                ampIndicatorValue.setIndicatorConnection(aux);
                set.add(ampIndicatorValue);
            }
            aux.values = set;
        }
        else
            aux.values = null;
        return aux;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    public String getLogFrame() {
        if (this.getValues() != null && this.getValues().size() > 0) {
            AmpIndicatorValue indicatorValue = this.getValues().iterator().next();
            if (indicatorValue != null && indicatorValue.getLogFrame() != null) {
                return indicatorValue.getLogFrame().getValue();
            }
        }
        return "";
    }

    public List<AmpIndicatorValue> getValuesSorted() {
        Map<Integer, AmpIndicatorValue> tree = new TreeMap<Integer, AmpIndicatorValue>();

        //Order used in Activity form
        List<Integer> listSorted = Arrays.asList(AmpIndicatorValue.BASE, AmpIndicatorValue.TARGET, AmpIndicatorValue.REVISED, AmpIndicatorValue.ACTUAL);

        for (AmpIndicatorValue value : this.getValues()) {
            tree.put(listSorted.indexOf(value.getValueType()), value);
        }

        return new ArrayList<AmpIndicatorValue>(tree.values());
    }
}
