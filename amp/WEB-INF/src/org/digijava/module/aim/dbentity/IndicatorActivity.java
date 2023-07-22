package org.digijava.module.aim.dbentity;

import org.digijava.kernel.ampapi.endpoints.activity.values.AmpIndicatorRiskRatingsPossibleValuesProvider;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.util.Output;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import javax.persistence.*;
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
@Entity
@DiscriminatorValue("a")
public class IndicatorActivity extends IndicatorConnection implements Versionable, Cloneable {

    //IATI-check: to be ignored
    private static final long serialVersionUID = 2L;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_activity_id")
    @InterchangeableBackReference

    private AmpActivityVersion activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "risk")
    @PossibleValues(AmpIndicatorRiskRatingsPossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Risk", importable = true, pickIdOnly = true,
            fmPath = "/Activity Form/M&E/ME Item/Risk")
    private AmpIndicatorRiskRatings risk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ind_val_category")
    @Interchangeable(fieldTitle = "Log Frame", importable = true, pickIdOnly = true,
            discriminatorOption = CategoryConstants.LOGFRAME_KEY,
            fmPath = "/Activity Form/M&E/ME Item/Logframe Category")
    private AmpCategoryValue logFrame;
    


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

    public AmpCategoryValue getLogFrame() {
        return logFrame;
    }

    public void setLogFrame(AmpCategoryValue logFrame) {
        this.logFrame = logFrame;
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        IndicatorActivity aux = (IndicatorActivity) obj;
        return aux.getIndicator().getIndicatorId().equals(getIndicator().getIndicatorId());
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
            }
        }
        if (logFrame != null) {
            value += logFrame.getValue();
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
        if (logFrame != null) {
            out.getOutputs().add(new Output(null, new String[]{"Log frame"}, new Object[]{logFrame.getValue()}));
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
