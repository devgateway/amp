package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;

@TranslatableClass(displayName = "Regional Observation Measure")
public class AmpRegionalObservationMeasure implements Serializable, Cloneable {
    @InterchangeableId
    @Interchangeable(fieldTitle = "ID")
    private Long ampRegionalObservationMeasureId;
    @Interchangeable(fieldTitle = "Name", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    @TranslatableField
    private String name;
    private AmpRegionalObservation regionalObservation;
    @Interchangeable(fieldTitle = "Actors", fmPath = "/Activity Form/Regional Observations/Observation/Measure/Actor")
    private Set<AmpRegionalObservationActor> actors;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<AmpRegionalObservationActor> getActors() {
        return actors;
    }

    public void setActors(Set<AmpRegionalObservationActor> actors) {
        this.actors = actors;
    }

    public Long getAmpRegionalObservationMeasureId() {
        return ampRegionalObservationMeasureId;
    }

    public void setAmpRegionalObservationMeasureId(Long ampRegionalObservationMeasureId) {
        this.ampRegionalObservationMeasureId = ampRegionalObservationMeasureId;
    }

    public AmpRegionalObservation getRegionalObservation() {
        return regionalObservation;
    }

    public void setRegionalObservation(AmpRegionalObservation regionalObservation) {
        this.regionalObservation = regionalObservation;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        AmpRegionalObservationMeasure aux = (AmpRegionalObservationMeasure) super.clone();
        aux.setAmpRegionalObservationMeasureId(null);

        if (aux.actors != null && aux.actors.size() > 0) {
            Set<AmpRegionalObservationActor> set = new HashSet<AmpRegionalObservationActor>();
            Iterator<AmpRegionalObservationActor> i = aux.actors.iterator();
            while (i.hasNext()) {
                AmpRegionalObservationActor newActor = (AmpRegionalObservationActor) i.next().clone();
                newActor.setAmpRegionalObservationActorId(null);
                newActor.setMeasure(aux);
                set.add(newActor);
            }
            aux.actors = set;
        } else {
            aux.actors = null;
        }
        return aux;
    }

}
