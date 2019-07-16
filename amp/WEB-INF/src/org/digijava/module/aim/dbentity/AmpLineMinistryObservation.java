package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.Output;

@TranslatableClass (displayName = "Line Ministry Observation")
public class AmpLineMinistryObservation implements Serializable, Versionable, Cloneable {
    private static final long serialVersionUID = 1L;
    //IATI-check: to be ignored
    
//  @Interchangeable(fieldTitle="ID")
    private Long ampLineMinistryObservationId;
    @TranslatableField
//  @Interchangeable(fieldTitle="Name")
    private String name;
    private AmpActivityVersion activity;
//  @Interchangeable(fieldTitle="Line Ministry Observation Measures",fmPath="/Activity Form/Line Ministry Observations/Observation/Measure")
    private Set<AmpLineMinistryObservationMeasure> lineMinistryObservationMeasures;
//  @Interchangeable(fieldTitle="Observation Date",fmPath="/Activity Form/Line Ministry Observations/Observation/Date")
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

    public Long getAmpLineMinistryObservationId() {
        return ampLineMinistryObservationId;
    }

    public void setAmpLineMinistryObservationId(Long ampLineMinistryObservationId) {
        this.ampLineMinistryObservationId = ampLineMinistryObservationId;
    }

    public Set<AmpLineMinistryObservationMeasure> getLineMinistryObservationMeasures() {
        return lineMinistryObservationMeasures;
    }

    public void setLineMinistryObservationMeasures(
            Set<AmpLineMinistryObservationMeasure> lineMinistryObservationMeasures) {
        this.lineMinistryObservationMeasures = lineMinistryObservationMeasures;
    }

    public Date getObservationDate() {
        return observationDate;
    }

    public void setObservationDate(Date observationDate) {
        this.observationDate = observationDate;
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpLineMinistryObservation aux = (AmpLineMinistryObservation) obj;
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
                new Output(null, new String[] { "Name" }, new Object[] { this.name != null ? this.name
                        : "" }));
        if (this.observationDate != null) {
            out.getOutputs().add(new Output(null, new String[] { "Date" }, new Object[] { this.observationDate }));
        }
        
        if (this.lineMinistryObservationMeasures != null){
            Output outs = new Output(new ArrayList<Output>(), new String[] {"Measures" }, new Object[] { "" });
            outs.setOutputs(new ArrayList<Output>());
            
            Iterator<AmpLineMinistryObservationMeasure> it1 = this.lineMinistryObservationMeasures.iterator();
            while (it1.hasNext()) {
                AmpLineMinistryObservationMeasure measure = (AmpLineMinistryObservationMeasure) it1.next();
                outs.getOutputs().add(new Output(null, new String[] {"Name"}, new Object[] {measure.getName() != null ? measure.getName() : ""}));
                
                if (measure.getActors() != null && measure.getActors().size() > 0){
                    String[] actors = new String[measure.getActors().size()];
                    Iterator<AmpLineMinistryObservationActor> it2 = measure.getActors().iterator();
                    int i = 0;
                    while (it2.hasNext()) {
                        AmpLineMinistryObservationActor actor = (AmpLineMinistryObservationActor) it2.next();
                        if (actor.getName() != null){
                            actors[i] = actor.getName();
                            if (it2.hasNext())
                                actors[i] += ", ";
                            i++;
                        }
                    }
                    Output out1 = new Output(new ArrayList<Output>(), new String[] {"Actors"}, actors);
                    
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
        
        if (lineMinistryObservationMeasures != null){
            Iterator<AmpLineMinistryObservationMeasure> it1 = lineMinistryObservationMeasures.iterator();
            while (it1.hasNext()) {
                AmpLineMinistryObservationMeasure m = (AmpLineMinistryObservationMeasure) it1.next();
                if (m.getName() != null)
                    value += m.getName();
                if (m.getActors() != null){
                    Iterator<AmpLineMinistryObservationActor> it2 = m.getActors().iterator();
                    while (it2.hasNext()) {
                        AmpLineMinistryObservationActor a = (AmpLineMinistryObservationActor) it2.next();
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
        AmpLineMinistryObservation aux = (AmpLineMinistryObservation) clone();
        aux.activity = newActivity;
        aux.ampLineMinistryObservationId = null;

        if (aux.lineMinistryObservationMeasures != null && aux.lineMinistryObservationMeasures.size() > 0){
            Set<AmpLineMinistryObservationMeasure> set = new HashSet<AmpLineMinistryObservationMeasure>();
            Iterator<AmpLineMinistryObservationMeasure> i = aux.lineMinistryObservationMeasures.iterator();
            while (i.hasNext()) {
                AmpLineMinistryObservationMeasure newMeasure = (AmpLineMinistryObservationMeasure) i.next().clone();
                newMeasure.setAmpLineMinistryObservationMeasureId(null);
                newMeasure.setLineMinistryObservation(aux);
                set.add(newMeasure);
            }
            aux.lineMinistryObservationMeasures = set;
        }
        else
            aux.lineMinistryObservationMeasures = null;     
        
        return aux;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }




    public static class LineMinistryObservationComparator implements Comparator<AmpLineMinistryObservation>, Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(AmpLineMinistryObservation arg0, AmpLineMinistryObservation arg1) {
            if(arg0.getObservationDate()!=null && arg1.getObservationDate()!=null) 
                return arg0.getObservationDate().compareTo(arg1.getObservationDate());
            if(arg0.getAmpLineMinistryObservationId()!=null && arg1.getAmpLineMinistryObservationId()!=null) 
                return arg0.getAmpLineMinistryObservationId().compareTo(arg1.getAmpLineMinistryObservationId());
            if(arg0.getAmpLineMinistryObservationId()!=null && arg1.getAmpLineMinistryObservationId()==null) 
                return -1;
            if(arg0.getAmpLineMinistryObservationId()==null && arg1.getAmpLineMinistryObservationId()!=null) 
                return 1;
            return arg0.hashCode()-arg1.hashCode();
        }
        
    }
}
