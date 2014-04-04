package org.digijava.module.dataExchange.dbentity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 4/2/14
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class IatiCodeType {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss yyyy.MM.dd");

    private Long id;
    private String name;
    private String ampName;
    private String description;
    private Set<IatiCodeItem> items;
    private String fileName;
    private Date importDate;

    private Map<String, String> itemCodeNameMap; //non persistent

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmpName() {
        return ampName;
    }

    public void setAmpName(String ampName) {
        this.ampName = ampName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<IatiCodeItem> getItems() {
        return items;
    }

    public void setItems(Set<IatiCodeItem> items) {
        this.items = items;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getImportDateFormated() {
        String retVal = null;
        retVal = importDate != null ? dateFormat.format(importDate) : "N/A";
        return retVal;
    }

    private Map<String, String> generateItemCodeNameMap() {
        Map<String, String> retVal = new HashMap<String, String>();
        if (this.items != null && !this.items.isEmpty()) {
            for (IatiCodeItem item : this.items) {
                retVal.put(item.getCode(), item.getName());
            }
        }
        return retVal;
    }

    public String getNameForCode (String code) {
        if (this.itemCodeNameMap == null) {
            this.itemCodeNameMap = generateItemCodeNameMap();
        }
        return this.itemCodeNameMap.get(code);
    }

}
