package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
/**
 * 
 * @author jdeanquin
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class SimpleJsonBean {
	Object id;
    String code;
    String name;
    List<SimpleJsonBean> children;
	public SimpleJsonBean(){
		
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

    public List<SimpleJsonBean> getChildren() {
        return children;
    }

    public void setChildren(List<SimpleJsonBean> children) {
        this.children = children;
    }

}
