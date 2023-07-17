package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.common.CommonFieldsConstants;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.util.Output;
import org.digijava.module.aim.util.SerializableComparator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_ORG_ROLE")
public class AmpOrgRole implements Comparable<AmpOrgRole>, Serializable, Versionable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ORG_ROLE_seq")
    @SequenceGenerator(name = "AMP_ORG_ROLE_seq", sequenceName = "AMP_ORG_ROLE_seq", allocationSize = 1)
    @Column(name = "amp_org_role_id")
    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long ampOrgRoleId;

    @Column(name = "percentage")
    @Interchangeable(fieldTitle = "Percentage", importable = true, percentageConstraint = true,
            fmPath = FMVisibility.PARENT_FM + "/percentage",
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    private Float percentage;

    @Column(name = "additional_info")
    @Interchangeable(fieldTitle = "Additional Info", importable = true, label = "Department/Division",
            fmPath = FMVisibility.PARENT_FM + "/relOrgadditionalInfo")
    private String additionalInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity", nullable = false)
    @InterchangeableBackReference

    private AmpActivityVersion activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation", nullable = false)
    @Interchangeable(fieldTitle = "Organization", importable = true, pickIdOnly = true, uniqueConstraint = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class),
            commonPV = CommonFieldsConstants.COMMON_ORGANIZATION)
    private AmpOrganisation organisation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role", nullable = false)
    private AmpRole role;

    @OneToMany(mappedBy = "ampOrgRole", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ampOrgRoleBudgetId asc")
    @Interchangeable(fieldTitle = "Budgets", importable = true, fmPath = FMVisibility.PARENT_FM + "/Budget Code")

    private Set<AmpOrgRoleBudget> budgets =new HashSet<>();

    @OneToMany(mappedBy = "ampOrgRole", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpGPINiSurvey> gpiNiSurveys;
    

    public Float getPercentage() {
        return percentage;
    }
    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }
    /**
     * @return Returns the activity.
     */
    public AmpActivityVersion getActivity() {
        return activity;
    }
    /**
     * @param activity The activity to set.
     */
    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }
    /**
     * @return Returns the ampOrgRoleId.
     */
    public Long getAmpOrgRoleId() {
        return ampOrgRoleId;
    }
    /**
     * @param ampOrgRoleId The ampOrgRoleId to set.
     */
    public void setAmpOrgRoleId(Long ampOrgRoleId) {
        this.ampOrgRoleId = ampOrgRoleId;
    }
    /**
     * @return Returns the organisation.
     */
    public AmpOrganisation getOrganisation() {
        return organisation;
    }
    /**
     * @param organisation The organisation to set.
     */
    public void setOrganisation(AmpOrganisation organisation) {
        this.organisation = organisation;
    }
    /**
     * @return Returns the role.
     */
    public AmpRole getRole() {
        return role;
    }
    /**
     * @param role The role to set.
     */
    public void setRole(AmpRole role) {
        this.role = role;
    }
    
        
    /**
     * @return the additionalInfo
     */
    public String getAdditionalInfo() {
        return additionalInfo;
    }
    /**
     * @param additionalInfo the additionalInfo to set
     */
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
    
    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpOrgRole aux = (AmpOrgRole) obj;
        String original = "" + this.organisation.getAmpOrgId() + "-" + this.role.getAmpRoleId();
        String copy = "" + aux.organisation.getAmpOrgId() + "-" + aux.role.getAmpRoleId();
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
        
        out.getOutputs().add(new Output(null, new String[] { "Role" }, new Object[] { TranslatorWorker.translateText(this.role.getName()) }));
        if (this.percentage != null) {
            out.getOutputs().add(new Output(null, new String[] { "Percentage" }, new Object[] { this.percentage }));
        }
        if (this.additionalInfo != null && this.additionalInfo.trim().length() > 0)
            out.getOutputs().add(new Output(null, new String[] {"Department/Division"}, new Object[] {this.additionalInfo}));
        if (this.budgets != null){
            StringBuffer budgetCode = new StringBuffer();
            for (AmpOrgRoleBudget budget :budgets) {
            budgetCode.append(budget.getBudgetCode()+ ","); 
            }
            if (budgetCode.length()>0) {
                out.getOutputs().add(new Output(null, new String[] {"Budget Code"}, new Object[] {budgetCode.substring(0,budgetCode.length()-1)}));
            }
        }
        
        return out;
    }

    @Override
    public Object getValue() {
        StringBuffer budgetCode = new StringBuffer();
        for (AmpOrgRoleBudget budget :budgets) {
        budgetCode.append(budget.getBudgetCode()+ ","); 
        }
        return "" + this.percentage + "" + this.additionalInfo + ""+budgetCode.toString();
    }
    
    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpOrgRole clonedAmpOrgRole = (AmpOrgRole) clone();
        clonedAmpOrgRole.activity = newActivity;
        clonedAmpOrgRole.ampOrgRoleId = null;
    
        if (getGpiNiSurveys() != null && !getGpiNiSurveys().isEmpty()) {
            AmpGPINiSurvey clonedSurvey = (AmpGPINiSurvey) getGpiNiSurveys().iterator().next().clone();
            clonedSurvey.setAmpGPINiSurveyId(null);
            clonedSurvey.setAmpOrgRole(clonedAmpOrgRole);
        
            if (clonedSurvey.getResponses() != null) {
                final Set<AmpGPINiSurveyResponse> clonedSurveyResponses = new HashSet<AmpGPINiSurveyResponse>();
                clonedSurvey.getResponses().forEach(r -> {
                    try {
                        AmpGPINiSurveyResponse clonedResponse = (AmpGPINiSurveyResponse) r.clone();
                        clonedResponse.setAmpGPINiSurveyResponseId(null);
                        clonedResponse.setAmpGPINiSurvey(clonedSurvey);
                        clonedSurveyResponses.add(clonedResponse);
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                });
                clonedSurvey.getResponses().clear();
                clonedSurvey.getResponses().addAll(clonedSurveyResponses);
            }
            clonedAmpOrgRole.setGpiNiSurveys(null);
            clonedAmpOrgRole.setGpiNiSurveys(new HashSet<>());
            clonedAmpOrgRole.getGpiNiSurveys().add(clonedSurvey);
        } else {
            clonedAmpOrgRole.setGpiNiSurveys(null);
        }
        
        if (getBudgets() != null && !getBudgets().isEmpty()) {
            clonedAmpOrgRole.setBudgets(new HashSet<>());
            for (AmpOrgRoleBudget budget : getBudgets()) {
                AmpOrgRoleBudget clonedBudget = (AmpOrgRoleBudget) budget.clone();
                clonedBudget.setAmpOrgRole(clonedAmpOrgRole);
                clonedAmpOrgRole.getBudgets().add(clonedBudget);
            }
        } else {
            clonedAmpOrgRole.setBudgets(null);
        }
        
        return clonedAmpOrgRole;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
    
    @Override
    public int compareTo(AmpOrgRole arg0) {
        if (this.getAmpOrgRoleId() !=null && arg0 != null && arg0.getAmpOrgRoleId() != null) {
            return this.getAmpOrgRoleId().compareTo(arg0.getAmpOrgRoleId());
        } else if (arg0.getAmpOrgRoleId() == null && arg0 != null && arg0.getAmpOrgRoleId() == null) {
            Integer tempId1 = System.identityHashCode(this);
            Integer tempId2 = System.identityHashCode(arg0);
            return tempId1.compareTo(tempId2);
        } else {
            return -1;
        }
    }
    
    public final static Comparator<AmpOrgRole> BY_ACRONYM_AND_NAME_COMPARATOR = new SerializableComparator<AmpOrgRole>() {
        private static final long serialVersionUID = 1935052796869929272L;

        @Override
        public int compare(AmpOrgRole o1, AmpOrgRole o2) {
            if (o1 == null || o1.getOrganisation() == null ||o1.getOrganisation().getAcronymAndName() == null)
                return 1;
            if (o2 == null || o2.getOrganisation() == null ||o2.getOrganisation().getAcronymAndName() == null)
                return -1;
            return o1.getOrganisation().getAcronymAndName().compareTo(o2.getOrganisation().getAcronymAndName());
        }
    };
    
    public final static Comparator<AmpOrgRole> BY_ORG_AND_ROLE = new SerializableComparator<AmpOrgRole>() {
        private static final long serialVersionUID = 2047345669087531206L;

        @Override
        public int compare(AmpOrgRole o1, AmpOrgRole o2) {
            if (o1 == null && o2 == null)
                return 0;
            if (o1 == null || o1.getOrganisation() == null || o1.getRole() == null)
                return 1;
            if (o2 == null || o2.getOrganisation() == null || o2.getRole() == null)
                return -1;
            if (o1.getOrganisation().equals(o2.getOrganisation()) && o1.getRole().equals(o2.getRole()))
                return 0;
            if (o1.hashCode() < o2.hashCode())
                return -1;
            return 1;
        }
    };


    public Set<AmpOrgRoleBudget> getBudgets() {
        return budgets;
    }
    
    public void setBudgets(Set<AmpOrgRoleBudget> budgets) {
        this.budgets = budgets;
    }
    
    public AmpGPINiSurvey getGpiNiSurvey() {
        if (getGpiNiSurveys() != null && !getGpiNiSurveys().isEmpty()) {
            return getGpiNiSurveys().iterator().next();
        } else {
            return null;
        }
    }

    public void setGpiNiSurvey(AmpGPINiSurvey gpiNiSurvey) {
        if (getGpiNiSurveys() == null) {
            setGpiNiSurveys(new HashSet<>());
        }
        getGpiNiSurveys().add(gpiNiSurvey);
    }

    public boolean hasGpiNiSurvey(){
        return (getGpiNiSurveys()!=null && !getGpiNiSurveys().isEmpty());
    }
    public Set<AmpGPINiSurvey> getGpiNiSurveys() {
        return gpiNiSurveys;
    }
    public void setGpiNiSurveys(Set<AmpGPINiSurvey> gpiNiSurveys) {
        this.gpiNiSurveys = gpiNiSurveys;
    }
    
}
