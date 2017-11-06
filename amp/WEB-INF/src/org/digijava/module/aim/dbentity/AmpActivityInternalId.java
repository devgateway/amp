package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.Output;

public class AmpActivityInternalId implements Serializable, Versionable, Cloneable {
    //IATI-check: used. 
    private static final long serialVersionUID = 469552292854192522L;
    
    private Long id;

    @Interchangeable(fieldTitle="Organization", importable=true, pickIdOnly=true)
    private AmpOrganisation organisation;

    @Interchangeable(fieldTitle="AMP Activity", pickIdOnly=true)
    private AmpActivityVersion ampActivity;
    
    @Interchangeable(fieldTitle="Internal ID", importable=true)
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
        out.getOutputs().add(
                new Output(null, new String[] { "Organization" }, new Object[] { this.organisation.getName() }));
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
