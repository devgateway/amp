/*
 * Measures.java
 * Created : 07-Sep-2005
 */
package org.digijava.module.aim.helper;

import java.util.ArrayList;

import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpMeasure;

public class Measures {
    private Long id;
    private String name;
    private String nameTrimmed ;
    private String measureDate;
    private ArrayList<AmpActor> actors;
    
    public Measures() {}
    public Measures(AmpMeasure measure) {
        this.id = measure.getAmpMeasureId();
        this.name = measure.getName();
        this.nameTrimmed = (name!=null)?name.replace(" ", ""):"";
        this.measureDate = FormatHelper.formatDate(measure.getMeasureDate());
        this.actors = new ArrayList<AmpActor>(measure.getActors());
    }
    
    /**
     * @return Returns the actors.
     */
    public ArrayList<AmpActor> getActors() {
        return actors;
    }
    /**
     * @param actors The actors to set.
     */
    public void setActors(ArrayList<AmpActor> actors) {
        this.actors = actors;
    }
    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg) {
        if (arg instanceof Measures) {
            Measures measure = (Measures) arg;
            return measure.getId().equals(id);
        }
        throw new ClassCastException();
    }
    public String getNameTrimmed() {
        return nameTrimmed;
    }
    public void setNameTrimmed(String nameTrimmed) {
        this.nameTrimmed = nameTrimmed;
    }
    public String getMeasureDate() {
        return measureDate;
    }
    public void setMeasureDate(String measureDate) {
        this.measureDate = measureDate;
    }
}
