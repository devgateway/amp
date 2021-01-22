package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.Output;

@TranslatableClass(displayName = "Regional Observation")
public class AmpRegionalObservation implements Serializable, Versionable, Cloneable, Identifiable {

    @InterchangeableId
    @Interchangeable(fieldTitle = "ID")
    private Long ampRegionalObservationId;
    @Interchangeable(fieldTitle = "Name", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    @TranslatableField
    private String name;
    private AmpActivityVersion activity;
    @Interchangeable(fieldTitle = "Measures", fmPath = "/Activity Form/Regional "
            + "Observations/Observation/Measure", importable = true)
    private Set<AmpRegionalObservationMeasure> regionalObservationMeasures = new HashSet<>();
    @Interchangeable(fieldTitle = "Date", fmPath = "/Activity Form/Regional Observations/Observation/Date")
    private Date observationDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AmpActivityVersion getActivity() {
        return activity;
    }

    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }

    public Long getAmpRegionalObservationId() {
        return ampRegionalObservationId;
    }

    public void setAmpRegionalObservationId(Long ampRegionalObservationId) {
        this.ampRegionalObservationId = ampRegionalObservationId;
    }

    public Set<AmpRegionalObservationMeasure> getRegionalObservationMeasures() {
        return regionalObservationMeasures;
    }

    public void setRegionalObservationMeasures(Set<AmpRegionalObservationMeasure> regionalObservationMeasures) {
        this.regionalObservationMeasures = regionalObservationMeasures;
    }

    public Date getObservationDate() {
        return observationDate;
    }

    public void setObservationDate(Date observationDate) {
        this.observationDate = observationDate;
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpRegionalObservation aux = (AmpRegionalObservation) obj;
        String original = this.name != null ? this.name : "";
        String copy = aux.name != null ? aux.name : "";
        if (original.equals(copy)) {
            return true;
        }
        return false;
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(
                new Output(null, new String[]{"Name"}, new Object[]{this.name != null ? this.name
                        : ""}));
        if (this.observationDate != null) {
            out.getOutputs().add(new Output(null, new String[]{"Date"}, new Object[]{this.observationDate}));
        }

        if (this.regionalObservationMeasures != null) {
            Output outs = new Output(new ArrayList<Output>(), new String[]{"Measures"}, new Object[]{""});
            outs.setOutputs(new ArrayList<Output>());

            Iterator<AmpRegionalObservationMeasure> it1 = this.regionalObservationMeasures.iterator();
            while (it1.hasNext()) {
                AmpRegionalObservationMeasure measure = (AmpRegionalObservationMeasure) it1.next();
                outs.getOutputs().add(new Output(null, new String[]{"Name"},
                        new Object[]{measure.getName() != null ? measure.getName() : ""}));

                if (measure.getActors() != null && measure.getActors().size() > 0) {
                    String[] actors = new String[measure.getActors().size()];
                    Iterator<AmpRegionalObservationActor> it2 = measure.getActors().iterator();
                    int i = 0;
                    while (it2.hasNext()) {
                        AmpRegionalObservationActor actor = (AmpRegionalObservationActor) it2.next();
                        if (actor.getName() != null) {
                            actors[i] = actor.getName();
                            if (it2.hasNext())
                                actors[i] += ", ";
                            i++;
                        }
                    }
                    Output out1 = new Output(new ArrayList<Output>(), new String[]{"Actors"}, actors);

                    outs.getOutputs().add(out1);
                }
            }
            out.getOutputs().add(outs);
        }

        return out;
    }

    @Override
    public Object getValue() {
        String value = "";
        if (name != null)
            value += name;
        if (observationDate != null)
            value += observationDate;

        if (regionalObservationMeasures != null) {
            Iterator<AmpRegionalObservationMeasure> it1 = regionalObservationMeasures.iterator();
            while (it1.hasNext()) {
                AmpRegionalObservationMeasure m = (AmpRegionalObservationMeasure) it1.next();
                if (m.getName() != null)
                    value += m.getName();
                if (m.getActors() != null) {
                    Iterator<AmpRegionalObservationActor> it2 = m.getActors().iterator();
                    while (it2.hasNext()) {
                        AmpRegionalObservationActor a = (AmpRegionalObservationActor) it2.next();
                        if (a.getName() != null)
                            value += a.getName();
                    }
                }
            }
        }

        return value;
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpRegionalObservation aux = (AmpRegionalObservation) clone();
        aux.activity = newActivity;
        aux.ampRegionalObservationId = null;

        if (aux.regionalObservationMeasures != null && aux.regionalObservationMeasures.size() > 0) {
            Set<AmpRegionalObservationMeasure> set = new HashSet<AmpRegionalObservationMeasure>();
            Iterator<AmpRegionalObservationMeasure> i = aux.regionalObservationMeasures.iterator();
            while (i.hasNext()) {
                AmpRegionalObservationMeasure newMeasure = (AmpRegionalObservationMeasure) i.next().clone();
                newMeasure.setAmpRegionalObservationMeasureId(null);
                newMeasure.setRegionalObservation(aux);
                set.add(newMeasure);
            }
            aux.regionalObservationMeasures = set;
        } else {
            aux.regionalObservationMeasures = null;
        }
        return aux;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    @Override
    public Object getIdentifier() {
        return ampRegionalObservationId;
    }

    public static class RegionalObservationComparator implements Comparator<AmpRegionalObservation>, Serializable {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(AmpRegionalObservation arg0, AmpRegionalObservation arg1) {
            if (arg0.getObservationDate() != null && arg1.getObservationDate() != null) {
                return arg0.getObservationDate().compareTo(arg1.getObservationDate());
            } else if (arg0.getAmpRegionalObservationId() != null && arg1.getAmpRegionalObservationId() != null) {
                return arg0.getAmpRegionalObservationId().compareTo(arg1.getAmpRegionalObservationId());
            } else if (arg0.getAmpRegionalObservationId() != null && arg1.getAmpRegionalObservationId() == null) {
                return -1;
            } else if (arg0.getAmpRegionalObservationId() == null && arg1.getAmpRegionalObservationId() != null) {
                return 1;
            } else {
                return arg0.hashCode() - arg1.hashCode();
            }
        }

    }
}
