package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
/**
 * 
 * @author jdeanquin elfleco
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class SimpleJsonBean {
	Object id;
    String code;
    String name;
    String displayName;

    String filterId;
    String type;
    List<SimpleJsonBean> children;
	public SimpleJsonBean(){
		
	}
    public SimpleJsonBean(Object id,String name,String code,Long type) {
    	this(id,name, code);
    	this.type=type.toString();
    }
    public SimpleJsonBean(Object id,String name,String code,String displayName) {
    	this(id,name,code);
    	this.displayName=displayName;
    }

    public SimpleJsonBean(Object id,String name,String code) {
    	this(id,name);
    	this.code=code;
    }
    public SimpleJsonBean(Object id,String name) {
    	this.id=id;
    	this.name=name;
    }
    

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

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

    public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<SimpleJsonBean> getChildren() {
        return children;
    }

    public void setChildren(List<SimpleJsonBean> children) {
        this.children = children;
    }
	public String getFilterId() {
		return filterId;
	}
	public void setFilterId(String filterId) {
		this.filterId = filterId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
