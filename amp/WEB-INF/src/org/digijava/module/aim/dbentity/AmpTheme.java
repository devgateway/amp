package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.dgfoundation.amp.ar.dimension.NPODimension;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.annotations.interchange.PossibleValueValue;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.aim.util.HierarchyListableComparator;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.NameableOrIdentifiable;
import org.digijava.module.aim.util.SoftDeletable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.util.TreeSet;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_THEME")
@TranslatableClass (displayName = "Theme")
public class AmpTheme implements Serializable, SoftDeletable, Identifiable, ARDimensionable, HierarchyListable,
        AmpAutoCompleteDisplayable<AmpTheme>, NameableOrIdentifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_theme_seq_generator")
    @SequenceGenerator(name = "amp_theme_seq_generator", sequenceName = "AMP_THEME_seq", allocationSize = 1)
    @Column(name = "amp_theme_id")
    @PossibleValueId
    private Long ampThemeId;

    @ManyToOne
    @JoinColumn(name = "parent_theme_id")
    private AmpTheme parentThemeId;

    @Column(name = "theme_code")
    private String themeCode;

    @Column(name = "budget_program_code")
    private String budgetProgramCode;

    @Column(name = "name")
    @PossibleValueValue
    @TranslatableField
    private String name;

    @Column(name = "level_")
    private Integer indlevel;

    @Column(name = "description")
    @TranslatableField
    private String description;

    @Column(name = "language")
    private String language;

    @Column(name = "version")
    private String version;

    @Column(name = "lead_agency")
    private String leadAgency;

    @Column(name = "target_groups")
    private String targetGroups;

    @Column(name = "background")
    private String background;

    @Column(name = "objectives")
    private String objectives;

    @Column(name = "outputs")
    private String outputs;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "beneficiaries")
    private String beneficiaries;

    @Column(name = "environment_considerations")
    private String environmentConsiderations;

    @Column(name = "external_financing")
    private Double externalFinancing;

    @Column(name = "internal_financing")
    private Double internalFinancing;

    @Column(name = "total_financing")
    private Double totalFinancing;

    @ManyToOne
    @JoinColumn(name = "type_category_value_id")
    private AmpCategoryValue typeCategoryValue;

//    @OneToMany(mappedBy = "themeId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IndicatorTheme> indicators;

    @OneToMany(mappedBy = "defaultHierarchy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpActivityProgramSettings> programSettings;

    @OneToMany(mappedBy = "parentThemeId")
    private Set<AmpTheme> siblings;

    @Column(name = "is_budget_program")
    private Integer isBudgetProgram;

    @Column(name = "show_in_rm")
    private Boolean showInRMFilters;
    private static final long serialVersionUID = 1L;

    @Transient

    private Integer isbudgetprogram;
    @Transient
    private String encodeName;

    
    /**
     * don't be fooled by the name - it gets the children
     */
    @Transient
    private boolean transientBoolean;


    
    private transient Collection<AmpTheme> transientChildren;

    @Transient
    private boolean translateable   = true;
    
    public Boolean getShowInRMFilters() {
        return showInRMFilters;
    }

    public void setShowInRMFilters(Boolean showInRMFilters) {
        this.showInRMFilters = showInRMFilters;
    }
@Transient
    private String programviewname;

    /**
     * @return
     */
    public Long getAmpThemeId() {
        return ampThemeId;
    }

    /**
     * @return Returns the parentThemeId.
     */
    public AmpTheme getParentThemeId() {
        return parentThemeId;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return
     */
    public String getThemeCode() {
        return themeCode;
    }

    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param long1
     */
    public void setAmpThemeId(Long long1) {
        ampThemeId = long1;
    }

    /**
     * @param parentThemeId The parentThemeId to set.
     */
    public void setParentThemeId(AmpTheme parentThemeId) {
        this.parentThemeId = parentThemeId;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * @param string
     */
    public void setLanguage(String string) {
        language = string;
    }

    /**
     * @param string
     */
    public void setThemeCode(String string) {
        themeCode = string;
    }

    public void setName(String string) {
        name = string;
    }

    /**
     * @param string
     */
    public void setVersion(String string) {
        version = string;
    }

    /**
     * @return Returns the indicators.
     */
    public Set<IndicatorTheme> getIndicators() {
        return indicators;
    }

//    public AmpActivityVersion getActivityId() {
//        return activityId;
//    }

    /**
     * @param indicators The indicators to set.
     */
    public void setIndicators(Set<IndicatorTheme> indicators) {
        this.indicators = indicators;
    }

    /**
     * @return Returns the indlevel.
     */
    public Integer getIndlevel() {
        if(indlevel == null)
            indlevel = new Integer(0);
        return indlevel;
    }

    /**
     * @param indlevel The indlevel to set.
     */
    public void setIndlevel(Integer indlevel) {
        if(indlevel == null)
            indlevel = new Integer(0);
        this.indlevel = indlevel;
    }

//  public void setActivityId(AmpActivityVersion activityId) {
//        this.activityId = activityId;
//    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AmpTheme)) return false;
        if (obj == null)
            throw new NullPointerException();

        if (!(obj instanceof AmpTheme))
            throw new ClassCastException();

        AmpTheme theme = (AmpTheme) obj;
        return (theme.getAmpThemeId().equals(ampThemeId));
    }

    @Override
    public int hashCode()
    {
        return this.ampThemeId == null ? null : this.ampThemeId.hashCode();
    }

    public void setLeadAgency (String leadAgency) {
        this.leadAgency = leadAgency;
    }
    public String getLeadAgency () {
        return this.leadAgency;
    }

    public void setTargetGroups (String targetGroups) {
        this.targetGroups   = targetGroups;
    }
    public String getTargetGroups () {
        return this.targetGroups;
    }


    public void setBackground (String background) {
        this.background = background;
    }
    public String getBackground () {
        return this.background;
    }

    public void setObjectives (String objectives) {
        this.objectives = objectives;
    }
    public String getObjectives() {
        return this.objectives;
    }

    public void setOutputs (String outputs) {
        this.outputs    = outputs;
    }
    public String getOutputs () {
        return this.outputs;
    }

    public void setBeneficiaries (String beneficiaries) {
        this.beneficiaries  = beneficiaries;
    }
    public String getBeneficiaries () {
        return this.beneficiaries;
    }

    public void setEnvironmentConsiderations (String environmentConsiderations) {
        this.environmentConsiderations  = environmentConsiderations;
    }
    public String getEnvironmentConsiderations () {
        return this.environmentConsiderations;
    }

    public String getProgramviewname() {
        return programviewname;
    }

    public void setProgramviewname(String programviewname) {
        this.programviewname = programviewname;
    }

    public AmpCategoryValue getTypeCategoryValue() {
        return typeCategoryValue;
    }

    public void setTypeCategoryValue(AmpCategoryValue typeCategoryValue) {
        this.typeCategoryValue = typeCategoryValue;
    }

    public String getEncodeName() {
        if(name != null) {
                encodeName = name.replace("'", " ");
                //encodeName = encodeName.replaceAll("\"", "\\\'");
                ////System.out.println(encodeName);

        }
        return encodeName;
    }

        public Set<AmpActivityProgramSettings> getProgramSettings() {
                return programSettings;
        }

        public void setEncodeName(String encodeName) {
        this.encodeName = encodeName;
    }

        public void setProgramSettings(Set<AmpActivityProgramSettings> programSettings) {
                this.programSettings = programSettings;
        }

        public Object getIdentifier() {
           return this.getAmpThemeId();
        }
        
        @Override
        public String toString() {
        return name;
    }

        /**
         * @return the totalFinancing
         */
        public Double getTotalFinancing() {
            return FeaturesUtil.applyThousandsForVisibility(totalFinancing);
        }

        /**
         * @param totalFinancing the totalFinancing to set
         */
        public void setTotalFinancing(Double totalFinancing) {
            this.totalFinancing = FeaturesUtil.applyThousandsForEntry(totalFinancing);
        }

        /**
         * @return the externalFinancing
         */
        public Double getExternalFinancing() {
            return FeaturesUtil.applyThousandsForVisibility(externalFinancing);
        }

        /**
         * @param externalFinancing the externalFinancing to set
         */
        public void setExternalFinancing(Double externalFinancing) {
            this.externalFinancing = FeaturesUtil.applyThousandsForEntry(externalFinancing);
        }

        /**
         * @return the internalFinancing
         */
        public Double getInternalFinancing() {
            return FeaturesUtil.applyThousandsForVisibility(internalFinancing);
        }

        /**
         * @param internalFinancing the internalFinancing to set
         */
        public void setInternalFinancing(Double internalFinancing) {
            this.internalFinancing = FeaturesUtil.applyThousandsForEntry(internalFinancing);
        }

        public String getBudgetProgramCode() {
            return budgetProgramCode;
        }

        public void setBudgetProgramCode(String budgetProgramCode) {
            this.budgetProgramCode = budgetProgramCode;
        }

        @Override
        public Class getDimensionClass() {
            return NPODimension.class;
        }

        public Integer getIsbudgetprogram() {
            return isbudgetprogram;
        }

        public void setIsbudgetprogram(Integer isbudgetprogram) {
            this.isbudgetprogram = isbudgetprogram;
        }

        @Override
        public Collection<AmpTheme> getChildren() {
            if (transientChildren == null)
                transientChildren = new TreeSet( new HierarchyListableComparator() );
            return transientChildren;
        }

        @Override
        public int getCountDescendants() {
            int ret = 1;
            if ( this.getChildren() != null ) {
                for ( HierarchyListable hl: this.getChildren() )
                    ret += hl.getCountDescendants();
            }
            return ret;
        }

        @Override
        public String getLabel() {
            return this.name;
        }

        @Override
        public String getAutoCompleteLabel() {
            return this.name;
        }

        @Override
        public String getUniqueId() {
            return this.ampThemeId + "";
        }
        
        @Override
        public boolean getTranslateable() {
            return translateable;
        }

        @Override
        public void setTranslateable(boolean translateable) {
            this.translateable = translateable;
        }

        @Override
        public AmpAutoCompleteDisplayable getParent() {
            return getParentThemeId();
        }

        @Override
        /**
         * don't be fooled by the name - it gets the children
         */
        public Collection<AmpTheme> getSiblings() {
            return siblings;
        }
        
        public void setSiblings(Set<AmpTheme> siblings) {
            this.siblings = siblings;
        }

        @Override
        public Collection<AmpTheme> getVisibleSiblings() {
            return getChildren();
        }

        @Override
        public int compareTo(AmpTheme arg0) {
            return ampThemeId.compareTo((arg0).getAmpThemeId()); 
        }

        public boolean isTransientBoolean() {
            return transientBoolean;
        }

        public void setTransientBoolean(boolean transientBoolean) {
            this.transientBoolean = transientBoolean;
        }

    public String getAdditionalSearchString() {
        return this.description;
    }

    public AmpTheme getRootTheme() {
        AmpTheme currentTheme = this;
        while(currentTheme.getParentThemeId() != null)
        {
            currentTheme = currentTheme.getParentThemeId();
        }
        return currentTheme;
    }

    public String getHierarchicalName()
    {
        String selfName = "[" + this.getName() + "]";
        if (this.getParentThemeId() == null)
            return selfName;
        return this.getParentThemeId().getHierarchicalName() + selfName;
    }
        
    public static String sqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpTheme.class, "name").getSQLFunctionCall(idSource);
    }

    public static String hqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpTheme.class, "name").getSQLFunctionCall(idSource + ".ampThemeId");
    }

    public Boolean getDeleted() {
        return deleted;
    }
    
    public boolean isSoftDeleted() {
        return Boolean.TRUE.equals(deleted);
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public Collection<AmpAutoCompleteDisplayable> getNonDeletedChildren() {
        Collection<AmpTheme> children = getSiblings();
        Collection<AmpAutoCompleteDisplayable> res = new ArrayList<>(children.size());
        for (AmpTheme theme: children) {
            if (!theme.isSoftDeleted())
                res.add(theme);
        }
        return res;
    }
}
