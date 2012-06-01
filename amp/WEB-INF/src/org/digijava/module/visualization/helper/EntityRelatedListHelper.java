package org.digijava.module.visualization.helper;
import java.util.List;

public class EntityRelatedListHelper <T,S>  {
	private T mainEntity;
	private List<S> subordinateEntityList;
	public EntityRelatedListHelper(T entity,List<S> relatedEntities){
		this.mainEntity=entity;
		this.subordinateEntityList=relatedEntities;
	}
	public T getMainEntity() {
		return mainEntity;
	}
	public List<S> getSubordinateEntityList() {
		return subordinateEntityList;
	}
	
}
