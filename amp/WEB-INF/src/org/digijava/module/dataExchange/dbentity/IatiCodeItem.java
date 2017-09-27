package org.digijava.module.dataExchange.dbentity;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 4/2/14
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class IatiCodeItem {
    private Long id;
    private String code;
    private String name;
    private IatiCodeType type;
    private Date updateDate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IatiCodeType getType() {
        return type;
    }

    public void setType(IatiCodeType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
