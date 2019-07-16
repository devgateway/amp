package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jcr.Node;

import org.apache.jackrabbit.core.persistence.PersistenceManager;
import org.dgfoundation.amp.onepager.models.AmpActivityModel;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.Output;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * 
 * @author Alex Gartner
 *
 */
public class AmpActivityDocument extends ObjectReferringDocument implements Serializable, Versionable, Cloneable {

    private Long id;
    
    private AmpActivityVersion ampActivity;

    @Interchangeable(fieldTitle = "Document Type", importable = true, required = ActivityEPConstants.REQUIRED_ALWAYS)
    private String documentType;
    
    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public AmpActivityVersion getAmpActivity() {
        return ampActivity;
    }

    public void setAmpActivity(AmpActivityVersion ampActivity) {
        this.ampActivity = ampActivity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    protected void detach() {
        ampActivity.getActivityDocuments().remove(this);
        this.ampActivity        = null;
    }
    
    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpActivityDocument aux = (AmpActivityDocument) obj;
        String original = this.getUuid() != null ? this.getUuid() : "";
        String copy = aux.getUuid() != null ? aux.getUuid() : "";
        if (original.equals(copy)) {
            return true;
        }
        return false;
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        if (this.getUuid() != null){
            out.getOutputs()
            .add(
                    new Output(null, new String[] { "Document UUID" },
                            new Object[] { this.getUuid() }));
        }
        
        if (this.documentType != null) {
            out.getOutputs()
                    .add(
                            new Output(null, new String[] { "Document Type" },
                                    new Object[] { this.documentType }));
        }

        return out;
    }

    @Override
    public Object getValue() {
        String value = "";
        value += this.documentType;
        value += this.getUuid();
        return value;
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpActivityDocument aux = (AmpActivityDocument) clone();
        aux.id = null;
        aux.ampActivity = newActivity;
        return aux;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
}
