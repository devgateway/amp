package org.digijava.module.fundingpledges.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Data;

import org.dgfoundation.amp.algo.AmpCollections;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesProgram;
import org.digijava.module.fundingpledges.dbentity.ValueWithPercentage;

/**
 * a value with an id, a name and a percentage
 * @author Dolghier Constantin
 *
 */
@Data
public class IdNamePercentage implements Comparable<IdNamePercentage>{
	public Long id;
	public String name;
	
	public Long rootId;
	public String rootName;
	
	public String hierarchicalName;
	public Float percentage;
	
	public IdNamePercentage(ValueWithPercentage val)
	{
		//this.dbId = val.getDbId();
		this.id = val.getInternalId();
		this.name = val.getName();
		
		this.rootId = val.getRootId();
		this.rootName = val.getRootName();
		
		this.hierarchicalName = val.getHierarchicalName();
		this.percentage = val.getPercentage();
	}
	
	public IdNamePercentage(Long id, Long rootId, String name, String hierarchicalName)
	{
		this.id = id;
		this.rootId = rootId;
		this.name = name;
		this.hierarchicalName = hierarchicalName;
	}
	
	public static List<IdNamePercentage> fromList(Collection<? extends ValueWithPercentage> vals)
	{
		List<IdNamePercentage> res = new ArrayList<>();
		for(ValueWithPercentage val:vals)
			res.add(new IdNamePercentage(val));
		return res;
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
	
	
}
