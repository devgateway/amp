package org.digijava.module.budgetexport.dbentity;

/**
 * User: flyer
 * Date: 2/6/12
 * Time: 4:44 PM
 */
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "amp_budget_export_csv_item")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpBudgetExportCSVItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_budget_export_csv_item_seq")
    @SequenceGenerator(name = "amp_budget_export_csv_item_seq", sequenceName = "amp_budget_export_csv_item_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "label")
    private String label;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "rule")
    private AmpBudgetExportMapRule rule;

    
    public AmpBudgetExportCSVItem(){}
    public AmpBudgetExportCSVItem(String code, String label, AmpBudgetExportMapRule rule){
        this.code = code;
        this.label = label;
        this.rule = rule;
    }
    
    
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
