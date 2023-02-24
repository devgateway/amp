package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.List;

import org.digijava.module.aim.dbentity.IndicatorTheme;

/**
 * Represents one row on the NPD table view.
 * @author Irakli Kobiashvili
 * @see IndicatorGridItem
 *
 */
public class IndicatorGridRow implements Comparable<IndicatorGridRow>{
    private Long id;
    private String name;
    private String description;
    private List<IndicatorGridItem> values;
    
    public IndicatorGridRow(IndicatorTheme connection,String[] years){
        this.id = connection.getIndicator().getIndicatorId();
        this.name = connection.getIndicator().getName();
        this.description=connection.getIndicator().getDescription();
        if (years != null){
            this.values = new ArrayList<IndicatorGridItem>(years.length);
            for (int i = 0; i < years.length; i++) {
                IndicatorGridItem item = new IndicatorGridItem(years[i],connection.getValues());
                this.values.add(i,item);
            }
        }
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<IndicatorGridItem> getValues() {
        return values;
    }
    public void setValues(List<IndicatorGridItem> values) {
        this.values = values;
    }
    public int compareTo(IndicatorGridRow row) {
        return this.id.compareTo(row.getId());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
