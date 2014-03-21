package org.digijava.module.fundingpledges.form;


import lombok.Data;
import org.dgfoundation.amp.algo.AmpCollections;

/**
 * a value with an id, a name and a percentage
 * @author Dolghier Constantin
 *
 */
@Data
public class IdNamePercentage implements Comparable<IdNamePercentage>{
	public final Long id;
	public final String name;
	
	public final Long rootId;
	public final String rootName;
	
	public final String hierarchicalName;
	public Float percentage;
		
	public IdNamePercentage(Long id, String name, Long rootId, String rootName, String hierarchicalName)
	{
		this.id = id;
		this.name = name;
		this.rootId = rootId;
		this.rootName = rootName;
		this.hierarchicalName = hierarchicalName;
	}
	
	public IdNamePercentage(Long id, String name, String hierarchicalName)
	{
		this(id, name, id, name, hierarchicalName);
	}
		
	@Override
	public int compareTo(IdNamePercentage oth)
	{
		Integer a = AmpCollections.nullCompare(this.id, oth.id);
		if (a != null)
			return a;
		a = AmpCollections.nullCompare(this.name, oth.name);
		return a == null ? 0 : a.intValue();
	}
	
	public IdNamePercentage setPercentage(Float perc){
		this.percentage = perc;
		return this;
	}
}
