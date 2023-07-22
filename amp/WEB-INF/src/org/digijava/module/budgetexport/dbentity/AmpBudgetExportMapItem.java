package org.digijava.module.budgetexport.dbentity;

/**
 * User: flyer
 * Date: 2/2/12
 * Time: 5:44 PM
 */
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "amp_budget_export_map_item")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpBudgetExportMapItem {
    public static final int MAP_MATCH_LEVEL_NONE = 0;
    public static final int MAP_MATCH_LEVEL_SOME = 1;
    public static final int MAP_MATCH_LEVEL_EXACT = 2;
    public static final int MAP_MATCH_LEVEL_MANUAL = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_budget_export_map_rule_seq")
    @SequenceGenerator(name = "amp_budget_export_map_rule_seq", sequenceName = "amp_budget_export_map_rule_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "imp_code")
    private String importedCode;

    @Column(name = "amp_object_id")
    private Long ampObjectID;

    @Column(name = "imported_label")
    private String importedLabel;

    @Column(name = "amp_label")
    private String ampLabel;

    @Column(name = "additional_label")
    private String additionalLabel;

    @Column(name = "match_level")
    private int matchLevel;

    @Column(name = "approved")
    private boolean approved;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "rule")
    private AmpBudgetExportMapRule rule;


    private boolean showAdditionalLabelCol;


    //NonPersistent field
    private boolean warning;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpBudgetExportMapRule getRule() {
        return rule;
    }

    public void setRule(AmpBudgetExportMapRule rule) {
        this.rule = rule;
    }

    public String getImportedCode() {
        return importedCode;
    }

    public void setImportedCode(String importedCode) {
        this.importedCode = importedCode;
    }

    public Long getAmpObjectID() {
        return ampObjectID;
    }

    public void setAmpObjectID(Long ampObjectID) {
        this.ampObjectID = ampObjectID;
    }

    public int getMatchLevel() {
        return matchLevel;
    }

    public void setMatchLevel(int matchLevel) {
        this.matchLevel = matchLevel;
    }

    public String getImportedLabel() {
        return importedLabel;
    }

    public void setImportedLabel(String importedLabel) {
        this.importedLabel = importedLabel;
    }

    public String getAmpLabel() {
        return ampLabel;
    }

    public void setAmpLabel(String ampLabel) {
        this.ampLabel = ampLabel;
    }

    public String getAdditionalLabel() {
        return additionalLabel;
    }

    public void setAdditionalLabel(String additionalLabel) {
        this.additionalLabel = additionalLabel;
    }

    public boolean isShowAdditionalLabelCol() {
        return showAdditionalLabelCol;
    }

    public void setShowAdditionalLabelCol(boolean showAdditionalLabelCol) {
        this.showAdditionalLabelCol = showAdditionalLabelCol;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
        this.warning = warning;
    }
}
