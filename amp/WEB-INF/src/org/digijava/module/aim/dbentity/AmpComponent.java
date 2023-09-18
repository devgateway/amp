/*
 * AmpComponent.java
 * Created : 9th March, 2005
 */

package org.digijava.module.aim.dbentity;

import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.ampapi.endpoints.activity.discriminators.AmpComponentFundingDiscriminationConfigurer;
import org.digijava.kernel.ampapi.endpoints.activity.values.providers.ComponentTypePossibleValuesProvider;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.*;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.Output;

import java.io.Serializable;
import java.util.*;

import static org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants.*;

/**
 * Persister class for Components
 * @author Priyajith
 */
@TranslatableClass (displayName = "Component")
public class AmpComponent implements Serializable, Comparable<AmpComponent>, Versionable, Cloneable, Identifiable {
    
    //IATI-check: to be ignored

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampComponentId;

    @InterchangeableBackReference
    private AmpActivityVersion activity;

    @Interchangeable(fieldTitle = COMPONENT_TITLE,
            interValidators = @InterchangeableValidator(RequiredValidator.class), importable = true,
            fmPath="/Activity Form/Components/Component/Component Information/Component Title")
    @TranslatableField
    private String title;

    @Interchangeable(fieldTitle = COMPONENT_DESCRIPTION, importable = true,
            fmPath="/Activity Form/Components/Component/Component Information/Description")
    @TranslatableField
    private String description;

    private java.sql.Timestamp creationdate;

    private String code;
    private String subProjectId;

    public String getSubProjectId() {
        return subProjectId;
    }

    public void setSubProjectId(String subProjectId) {
        this.subProjectId = subProjectId;
    }
    @InterchangeableDiscriminator(discriminatorField = "transactionType",
        configurer = AmpComponentFundingDiscriminationConfigurer.class,
        settings = {
            @Interchangeable(fieldTitle = ArConstants.COMMITMENT, discriminatorOption = "" + Constants.COMMITMENT,
                    fmPath = "/Activity Form/Components/Component/Components Commitments/Commitment Table",
                    importable = true),
            @Interchangeable(fieldTitle = ArConstants.DISBURSEMENT, discriminatorOption = "" + Constants.DISBURSEMENT,
                    fmPath = "/Activity Form/Components/Component/Components Disbursements/Disbursement Table",
                    importable = true),
            @Interchangeable(fieldTitle = ArConstants.EXPENDITURE, discriminatorOption = "" + Constants.EXPENDITURE,
                    fmPath = "/Activity Form/Components/Component/Components Expenditures/Expenditure Table",
                    importable = true)
    })
    private Set<AmpComponentFunding> fundings = new HashSet<>();

    public static class AmpComponentComparator implements Comparator<AmpComponent>{
        @Override
        public int compare(AmpComponent o1, AmpComponent o2) {
            return staticCompare(o1, o2);
        }
        
        public static int staticCompare(AmpComponent o1, AmpComponent o2) {
            if (o1 == null)
                return 1;
            if (o2 == null)
                return -1;
            if (o2.getTitle() == null)
                return -1;
            if (o1.getTitle() == null)
                return 1;
            int ret = o1.getTitle().compareTo(o2.getTitle());
            return ret;
        }
    }

    @PossibleValues(ComponentTypePossibleValuesProvider.class)
    @Interchangeable(fieldTitle = COMPONENT_TYPE, importable = true, pickIdOnly = true,
            fmPath = "/Activity Form/Components/Component/Component Information/Component Type")
    private AmpComponentType type;
    
    private String Url;

    public AmpActivityVersion getActivity() {
        return activity;
    }

    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }

    public Long getAmpComponentId() {
        return ampComponentId;
    }
    public void setAmpComponentId(Long ampComponentId) {
        this.ampComponentId = ampComponentId;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    
    public AmpComponentType getType() {
        return type;
    }
    public void setType(AmpComponentType type) {
        this.type = type;
    }
    
    public void setUrl(String url) {
        Url = url;
    }
    public String getUrl() {
        return Url;
    }
    
    /**
     * A simple string comparison to sort components by title
     */
    public int compareTo(AmpComponent o) {
        return AmpComponentComparator.staticCompare(this, o);
    }
    /**
     * If overriding is really needed please fully comment on the reason!
     *
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AmpComponent))
            return false;

        AmpComponent target=(AmpComponent) obj;
        if (this.ampComponentId == null)
            return super.equals(obj);
        
        if (target!=null && this.ampComponentId!=null){
            return (this.getAmpComponentId().equals(target.getAmpComponentId()));
        }
        return false;
    }
    @Override
    public int hashCode() {
        if( this.ampComponentId ==null) return 0;
        return this.ampComponentId.hashCode();
    }
     */
    
    public java.sql.Timestamp getCreationdate() {
        return creationdate;
    }
    public void setCreationdate(java.sql.Timestamp creationdate) {
        this.creationdate = creationdate;
    }
    
    public Set<AmpComponentFunding> getFundings() {
        return fundings;
    }
    
    public void setFundings(Set<AmpComponentFunding> fundings) {
        this.fundings = fundings;
    }
    
    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpComponent aux = (AmpComponent) obj;
        return this.getValue().equals(aux.getValue());
    }
    
    private static final Comparator<AmpComponentFunding> COMPONENT_FUNDING_COMPARATOR = new Comparator<AmpComponentFunding>() {
        public int compare(AmpComponentFunding o1, AmpComponentFunding o2) {
            AmpComponentFunding aux1 = (AmpComponentFunding) o1;
            AmpComponentFunding aux2 = (AmpComponentFunding) o2;

            if (aux1.getTransactionType().equals(aux2.getTransactionType())) {
                if (aux1.getTransactionAmount().equals(aux2.getTransactionAmount())) {
                    return aux1.getTransactionDate().compareTo(aux2.getTransactionDate());
                } else {
                    return aux1.getTransactionAmount().compareTo(aux2.getTransactionAmount());
                }
            } else {
                return aux1.getTransactionType().compareTo(aux2.getTransactionType());
            }
        }
    };

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        
        out.getOutputs().add(
                new Output(null, new String[] { "Title" }, new Object[] { this.title != null ? this.title
                        : "Empty Title" }));
        if (this.description != null && !this.description.trim().equals("")) {
            out.getOutputs()
                    .add(new Output(null, new String[] { "Description" }, new Object[] { this.description }));
        }
        if (this.code != null && !this.code.trim().equals("")) {
            out.getOutputs().add(new Output(null, new String[] { "Code" }, new Object[] { this.code }));
        }
        if (this.creationdate != null) {
            out.getOutputs().add(
                    new Output(null, new String[] { "Creation Date" }, new Object[] { this.creationdate }));
        }
        if (this.Url != null && !this.Url.trim().equals("")) {
            out.getOutputs().add(new Output(null, new String[] { "URL" }, new Object[] { this.Url }));
        }
        
        List<AmpComponentFunding> auxFundings = new ArrayList<AmpComponentFunding>(this.fundings);
        auxFundings.sort(COMPONENT_FUNDING_COMPARATOR);

        for (AmpComponentFunding funding : auxFundings) {
            String transactionType = "";

            switch (funding.getTransactionType()) {
                case 0:
                    transactionType = "Commitments";
                    break;
                case 1:
                    transactionType = "Disbursements";
                    break;
                case 2:
                    transactionType = "Expenditures";
                    break;
                case 3:
                    transactionType = "Disbursement Orders";
                    break;
                case 4:
                    transactionType = "MTEF Projection";
                    break;
            }

            out.getOutputs().add(new Output(null, new String[]{"Trn"}, new Object[]{transactionType}));
            out.getOutputs().add(new Output(null, new String[]{"Value"}, new Object[]{
                    " " + funding.getAdjustmentType().getValue() + " - ", funding.getTransactionAmount(),
                    " ", funding.getCurrency(), " - ", funding.getTransactionDate()}));
        }
        
        
        return out;
    }
    
    @Override
    public Object getValue() {
        StringBuilder ret = new StringBuilder();
        ret.append("-").append(this.code).append("-").append(this.description).append("-").append(this.Url).append("-").append(this.creationdate);
        
        List<AmpComponentFunding> auxFundings = new ArrayList<AmpComponentFunding>(this.fundings);
        auxFundings.sort(COMPONENT_FUNDING_COMPARATOR);

        for (AmpComponentFunding funding : auxFundings) {
            ret.append(funding.getTransactionType()).append("-").append(funding.getTransactionAmount()).append("-").append(funding.getCurrency()).append("-").append(funding.getTransactionDate());
        }
        
        return ret.toString();
    }
    
    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpComponent auxComponent = (AmpComponent) clone();
        auxComponent.setActivity(newActivity);
        auxComponent.setAmpComponentId(null);
        
        if (auxComponent.getFundings() != null && auxComponent.getFundings().size() > 0) {
            Set<AmpComponentFunding> auxSetFundings = new HashSet<AmpComponentFunding>();
            for (AmpComponentFunding auxComponentFunding : auxComponent.getFundings()) {
                AmpComponentFunding newComponentFunding = (AmpComponentFunding) auxComponentFunding.clone();
                newComponentFunding.setAmpComponentFundingId(null);
                newComponentFunding.setComponent(auxComponent);
                auxSetFundings.add(newComponentFunding);
            }
            auxComponent.setFundings(auxSetFundings);
        } else {
            auxComponent.setFundings(null);
        }
        
        return auxComponent;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
    
    @Override
    public String toString() {
        return title;
    }

    @Override
    public Object getIdentifier() {
        return ampComponentId;
    }
}
