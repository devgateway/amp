package org.digijava.module.aim.dbentity ;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.RequiredValidation.SUBMIT;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.validator.groups.Submit;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "AMP_MEASURE")
@TranslatableClass (displayName = "Measure")
public class AmpMeasure  implements Serializable, Cloneable, Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_measure_seq_generator")
    @SequenceGenerator(name = "amp_measure_seq_generator", sequenceName = "AMP_MEASURE_seq", allocationSize = 1)
    @Column(name = "amp_measure_id")
    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampMeasureId;

    @Column(name = "name", columnDefinition = "text")
    @TranslatableField
    @Interchangeable(fieldTitle = "Name", label = "Measure", importable = true,
            interValidators = @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class))
    private String name;

    @Column(name = "measureDate")
    @Interchangeable(fieldTitle = "Measure Date", importable = true,
            fmPath = "/Activity Form/Issues Section/Issue/Measure/Date")
    private Date measureDate;

    @ManyToOne
    @JoinColumn(name = "amp_issue_id", referencedColumnName = "amp_issue_id", nullable = false)
    @InterchangeableBackReference

    private AmpIssues issue;

    @OneToMany(mappedBy = "measure", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ampActorId ASC")
    @Interchangeable(fieldTitle = "Actors", importable = true,
            fmPath = "/Activity Form/Issues Section/Issue/Measure/Actor")
    private Set<AmpActor> actors;

    
    
    public Date getMeasureDate() {
        return measureDate;
    }
    public void setMeasureDate(Date measureDate) {
        this.measureDate = measureDate;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getAmpMeasureId() {
        return ampMeasureId;
    }
    public void setAmpMeasureId(Long ampMeasureId) {
        this.ampMeasureId = ampMeasureId;
    }
    
    public AmpIssues getIssue() {
        return issue;
    }
    public void setIssue(AmpIssues issue) {
        this.issue = issue;
    }

    public Set<AmpActor> getActors() {
        return actors;
    }
    public void setActors(Set<AmpActor> actors) {
        this.actors = actors;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    @Override
    public Object getIdentifier() {
        return ampMeasureId;
    }
}
