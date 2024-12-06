package org.digijava.module.aim.dbentity;

import org.digijava.kernel.ampapi.endpoints.activity.values.AmpIndicatorRiskRatingsPossibleValuesProvider;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.util.Output;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.util.*;


/**
 * Project Indicator.
 * This is connection between indicator and activity. Most fields are in parent class.
 * Check hibernate mapping in IndicatorConnection.hbm.xml 
 * @see IndicatorConnection
 * @author Irakli Kobiashvili
 *
 */
public class IndicatorActivity extends IndicatorConnection implements Versionable, Cloneable {

    //IATI-check: to be ignored
    private static final long serialVersionUID = 2L;
    
    /**
     * Activity
     */
    @InterchangeableBackReference
    private AmpActivityVersion activity;
    
    /**
     * Indicator risk.
     * Actually risk is in each connection of indicator and activity.
     */
    @PossibleValues(AmpIndicatorRiskRatingsPossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Risk", importable = true, pickIdOnly = true,
            fmPath = "/Activity Form/M&E/ME Item/Risk")
    private AmpIndicatorRiskRatings risk;

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
        StringBuilder value = new StringBuilder();
        if (getIndicator() != null)
            value.append(getIndicator().getName());
        if (risk != null)
            value.append(risk.getRatingName());
        if (values != null){
            ArrayList<AmpIndicatorValue> list = new ArrayList<AmpIndicatorValue>(values);
            list.sort(new Comparator<AmpIndicatorValue>() {
                @Override
                public int compare(AmpIndicatorValue arg0,
                                   AmpIndicatorValue arg1) {
                    return arg0.getValueType() - arg1.getValueType();
                }
            });

            for (AmpIndicatorValue ind : list) {
                value.append(ind.getValueType()).append(ind.getValue()).append(ind.getValueDate());
            }
        }
        if (logFrame != null) {
            value.append(logFrame.getValue());
        }

        return value.toString();
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
            for (AmpIndicatorValue ind : values) {
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
                        new Output(null, new String[]{typeString}, new Object[]{ind.getValue()}));
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
            for (AmpIndicatorValue value : aux.values) {
                AmpIndicatorValue ampIndicatorValue = (AmpIndicatorValue) value.clone();
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
//        Map<Integer, AmpIndicatorValue> tree = new TreeMap<Integer, AmpIndicatorValue>();

        //Order used in Activity form
        List<Integer> listSorted = Arrays.asList(AmpIndicatorValue.BASE, AmpIndicatorValue.TARGET, AmpIndicatorValue.REVISED, AmpIndicatorValue.ACTUAL);

//        for (AmpIndicatorValue value : this.getValues()) {
//            tree.put(listSorted.indexOf(value.getValueType()), value);
//        }
        ArrayList<AmpIndicatorValue> sorted = new ArrayList<>(getValues());
        sorted.sort(
                Comparator.<AmpIndicatorValue, Integer> comparing(v -> listSorted.indexOf(v.getValueType()))
                        .thenComparing(Comparator.nullsFirst(Comparator.comparing(AmpIndicatorValue::getValueDate))));
        return sorted;

//        return new ArrayList<AmpIndicatorValue>(tree.values());
    }
    
}
