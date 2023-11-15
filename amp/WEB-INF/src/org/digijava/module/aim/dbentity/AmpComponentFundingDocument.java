package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "AMP_COMPONENT_FUNDING_DOCUMENTS")
@Cacheable
@DynamicUpdate
@Entity
public class AmpComponentFundingDocument extends ObjectReferringDocument implements Serializable, Cloneable {
    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_COMPONENT_FUNDING_DOCUMENTS_seq")
    @SequenceGenerator(name = "AMP_COMPONENT_FUNDING_DOCUMENTS_seq", sequenceName = "AMP_COMPONENT_FUNDING_DOCUMENTS_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    public AmpComponentFunding getAmpComponentFunding() {
        return ampComponentFunding;
    }

    public void setAmpComponentFunding(AmpComponentFunding ampComponentFunding) {
        this.ampComponentFunding = ampComponentFunding;
    }

    @InterchangeableBackReference
    private AmpComponentFunding ampComponentFunding;

//    @Interchangeable(fieldTitle = "Document Type", importable = true,
//            interValidators = @InterchangeableValidator(RequiredValidator.class))
//    private String documentType;
//
//    public String getDocumentType() {
//        return documentType;
//    }

//    public void setDocumentType(String documentType) {
//        this.documentType = documentType;
//    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    protected void detach() {
        ampComponentFunding.getComponentFundingDocuments().remove(this);
        this.ampComponentFunding     = null;
    }

//    @Override
//    public boolean equalsForVersioning(Object obj) {
//        AmpActivityDocument aux = (AmpActivityDocument) obj;
//        String original = this.getUuid() != null ? this.getUuid() : "";
//        String copy = aux.getUuid() != null ? aux.getUuid() : "";
//        return original.equals(copy);
//    }
//
//    @Override
//    public Output getOutput() {
//        Output out = new Output();
//        out.setOutputs(new ArrayList<Output>());
//        if (this.getUuid() != null){
//            out.getOutputs()
//                    .add(
//                            new Output(null, new String[] { "Document UUID" },
//                                    new Object[] { this.getUuid() }));
//        }
//
//        if (this.documentType != null) {
//            out.getOutputs()
//                    .add(
//                            new Output(null, new String[] { "Document Type" },
//                                    new Object[] { this.documentType }));
//        }
//
//        return out;
//    }
//
//    @Override
//    public Object getValue() {
//        String value = "";
//        value += this.documentType;
//        value += this.getUuid();
//        return value;
//    }

//    @Override
    public Object prepareMerge(AmpComponentFunding ampComponentFunding) throws CloneNotSupportedException {
        AmpComponentFundingDocument aux = (AmpComponentFundingDocument) clone();
        aux.id = null;
        aux.ampComponentFunding = ampComponentFunding;
        return aux;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
}
