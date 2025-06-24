package org.digijava.module.aim.dbentity;

import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AmpLineMinistryObservationMeasure implements Serializable, Cloneable {
    //IATI-check: to be ignored

    @InterchangeableId
    @Interchangeable(fieldTitle = "ID")
    private Long ampLineMinistryObservationMeasureId;

    @Interchangeable(fieldTitle = "Name", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private String name;

    @InterchangeableBackReference
    private AmpLineMinistryObservation lineMinistryObservation;

    @Interchangeable(fieldTitle = "Actors", importable = true,
            fmPath = "/Activity Form/Line Ministry Observations/Observation/Measure/Actor")
    private Set<AmpLineMinistryObservationActor> actors = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAmpLineMinistryObservationMeasureId() {
        return ampLineMinistryObservationMeasureId;
    }

    public void setAmpLineMinistryObservationMeasureId(
            Long ampLineMinistryObservationMeasureId) {
        this.ampLineMinistryObservationMeasureId = ampLineMinistryObservationMeasureId;
    }

    public AmpLineMinistryObservation getLineMinistryObservation() {
        return lineMinistryObservation;
    }

    public void setLineMinistryObservation(
            AmpLineMinistryObservation lineMinistryObservation) {
        this.lineMinistryObservation = lineMinistryObservation;
    }

    public void setActors(Set<AmpLineMinistryObservationActor> actors) {
        this.actors = actors;
    }
    
    public Set<AmpLineMinistryObservationActor> getActors() {
        return actors;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        AmpLineMinistryObservationMeasure aux = (AmpLineMinistryObservationMeasure) super.clone();
        aux.setAmpLineMinistryObservationMeasureId(null);
        
        if (aux.actors != null && aux.actors.size() > 0){
            Set<AmpLineMinistryObservationActor> set = new HashSet<AmpLineMinistryObservationActor>();
            Iterator<AmpLineMinistryObservationActor> i = aux.actors.iterator();
            while (i.hasNext()) {
                AmpLineMinistryObservationActor newActor = (AmpLineMinistryObservationActor) i.next().clone();
                newActor.setAmpLineMinistryObservationActorId(null);
                newActor.setMeasure(aux);
                set.add(newActor);
            }
            aux.actors = set;
        }
        else
            aux.actors = null;
        
        return aux;
    }
    
}
