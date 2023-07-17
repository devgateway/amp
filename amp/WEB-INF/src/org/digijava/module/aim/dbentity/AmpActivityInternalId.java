package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;

import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.util.Output;
import javax.persistence.*;

@Entity
@Table(name = "AMP_ACTIVITY_INTERNAL_ID")
public class AmpActivityInternalId implements Serializable, Versionable, Cloneable {
    //IATI-check: used. 
    private static final long serialVersionUID = 469552292854192522L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_activity_internal_id_seq_generator")
    @SequenceGenerator(name = "amp_activity_internal_id_seq_generator", sequenceName = "AMP_ACTIVITY_INTERNAL_ID_seq", allocationSize = 1)
    @Column(name = "id")
    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_activity_id", nullable = false)
    @InterchangeableBackReference

    private AmpActivityVersion ampActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amp_org_id", nullable = false)
    @Interchangeable(fieldTitle = "Organization", importable = true, pickIdOnly = true)

    private AmpOrganisation organisation;

    @Column(name = "internal_id")
    @Interchangeable(fieldTitle = "Internal ID", importable = true,
            fmPath = "/Activity Form/Activity Internal IDs/Internal IDs/internalId")
    private String internalId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpOrganisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(AmpOrganisation organisation) {
        this.organisation = organisation;
    }

    public AmpActivityVersion getAmpActivity() {
        return ampActivity;
    }

    public void setAmpActivity(AmpActivityVersion ampActivity) {
        this.ampActivity = ampActivity;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpActivityInternalId aux = (AmpActivityInternalId) obj;
        String original = "" + this.organisation;
        String copy = "" + aux.organisation;
        if (original.equals(copy)) {
            return true;
        }
        return false;
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        
        String orgName = this.organisation.getName();
        if (this.organisation != null 
                && this.organisation.getDeleted() != null && this.organisation.getDeleted()) {
            out.setDeletedValues(true);
            orgName += " (" + TranslatorWorker.translateText("deleted") + ")";
        }
        out.getOutputs().add(new Output(null, new String[] {"Organization"}, new Object[] {orgName}));
        
        out.getOutputs().add(new Output(null, new String[] { "Internal Id" }, new Object[] { this.internalId }));
        return out;
    }

    @Override
    public Object getValue() {
        return "" + this.internalId;
    }
    
    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpActivityInternalId aux = (AmpActivityInternalId) clone();
        aux.ampActivity = newActivity;
        aux.id = null;
        return aux;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
