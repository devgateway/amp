package org.digijava.module.aim.dbentity ;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.RequiredValidation.SUBMIT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.util.Output;

public class AmpIssues implements Serializable, Versionable, Cloneable {

    //IATI-check: to be ignored

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampIssueId;

    @Interchangeable(fieldTitle = "Name", label = "Issue", importable = true, required = SUBMIT)
    private String name ;

    @InterchangeableBackReference
    private AmpActivityVersion activity;

    @Interchangeable(fieldTitle = "Measures", importable = true, fmPath = "/Activity Form/Issues Section/Issue/Measure")
    private Set<AmpMeasure> measures = new HashSet<>();

    @Interchangeable(fieldTitle = "Issue Date", importable = true, fmPath = "/Activity Form/Issues Section/Issue/Date")
    private Date issueDate;
 
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getAmpIssueId() {
        return ampIssueId;
    }
    public void setAmpIssueId(Long ampIssueId) {
        this.ampIssueId = ampIssueId;
    }
    
    public AmpActivityVersion getActivity() {
        return activity;
    }
    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }

    public Set<AmpMeasure> getMeasures() {
        return measures;
    }
    public void setMeasures(Set<AmpMeasure> measures) {
        this.measures = measures;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
    public boolean equals(Object arg) {
        if (arg instanceof AmpIssues) {
            AmpIssues issue = (AmpIssues) arg;
            return issue.getAmpIssueId().equals(ampIssueId);
        }
        throw new ClassCastException();
    }
     */
    public Date getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }
    
    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpIssues aux = (AmpIssues) obj;
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
        if (this.issueDate != null) {
            out.getOutputs().add(new Output(null, new String[] { "Date" }, new Object[] { this.issueDate }));
        }
        
        if (this.measures != null){
            Output outs = new Output(new ArrayList<Output>(), new String[] {"Measures" }, new Object[] { "" });
            outs.setOutputs(new ArrayList<Output>());
            
            Iterator<AmpMeasure> it1 = this.measures.iterator();
            while (it1.hasNext()) {
                AmpMeasure measure = (AmpMeasure) it1.next();
                outs.getOutputs().add(new Output(null, new String[] {"Name"}, new Object[] {measure.getName() != null ? measure.getName() : ""}));
                
                if (measure.getActors() != null && measure.getActors().size() > 0){
                    String[] actors = new String[measure.getActors().size()];
                    Iterator<AmpActor> it2 = measure.getActors().iterator();
                    int i = 0;
                    while (it2.hasNext()) {
                        AmpActor actor = (AmpActor) it2.next();
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
        if (issueDate != null)
            value += issueDate;
        
        if (measures != null){
            Iterator<AmpMeasure> it1 = measures.iterator();
            while (it1.hasNext()) {
                AmpMeasure m = (AmpMeasure) it1.next();
                if (m.getName() != null)
                    value += m.getName();
                if (m.getActors() != null){
                    Iterator<AmpActor> it2 = m.getActors().iterator();
                    while (it2.hasNext()) {
                        AmpActor a = (AmpActor) it2.next();
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
        AmpIssues aux = (AmpIssues) clone();
        aux.activity = newActivity;
        aux.ampIssueId = null;
        Set<AmpMeasure> setMeasures = new HashSet<>();
        if (aux.measures != null && aux.measures.size() > 0) {
            Iterator<AmpMeasure> i = aux.measures.iterator();
            while (i.hasNext()) {
                AmpMeasure newMeasure = (AmpMeasure) i.next().clone();
                newMeasure.setAmpMeasureId(null);
                newMeasure.setIssue(aux);
                Set<AmpActor> setActors = new HashSet<>();
                if (newMeasure.getActors() != null && newMeasure.getActors().size() > 0) {
                    Iterator<AmpActor> j = newMeasure.getActors().iterator();
                    while (j.hasNext()) {
                        AmpActor newActor = (AmpActor) j.next().clone();
                        newActor.setAmpActorId(null);
                        newActor.setMeasure(newMeasure);
                        setActors.add(newActor);
                    }
                    newMeasure.setActors(setActors);
                }
                newMeasure.setActors(setActors);
                setMeasures.add(newMeasure);
            }
        }
        aux.measures = setMeasures;
        return aux;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
    
}
