package org.digijava.kernel.ampapi.saiku.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.olap4j.OlapException;
import org.olap4j.impl.ArrayNamedListImpl;
import org.olap4j.mdx.ParseTreeNode;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Measure;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Property;
import org.olap4j.metadata.Member.Type;
import org.saiku.service.olap.totals.TotalNode;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;

public class MyTotalNode extends TotalNode {
	public MyTotalNode(String[] captions) {
		super(captions, buildMeasures(captions), TotalAggregator.newInstanceByFunctionName("not"), null, captions.length);
		
		if (this.getTotalGroups().length < 1) 
			throw new RuntimeException("we ought to have at least depth one!");
		
		if (this.getTotalGroups()[0].length != captions.length)
			throw new RuntimeException("we ought to have exactly captions.length measures!");
		
		for(int i = 0; i < captions.length; i++)
			this.getTotalGroups()[0][i] = new MyTotalAggregator();
	}
	
	private static Measure[] buildMeasures(String[] names) {
		Measure[] res = new Measure[names.length];
		for(int i = 0; i < names.length; i++) {
			final String name = names[i];
			final org.olap4j.metadata.Hierarchy hier = new Hierarchy() {
				
				@Override
				public boolean isVisible() {return true;}
				
				@Override
				public String getUniqueName() {return "HIER/" + name;}
				
				@Override
				public String getName() {return "HIER/" + name;}
				
				@Override
				public String getDescription() {return name;}
				
				@Override
				public String getCaption() {return name;}
				
				@Override
				public boolean hasAll() {return true;}
				
				@Override
				public NamedList<Member> getRootMembers() throws OlapException {
					return new ArrayNamedListImpl<Member>() {
						@Override public String getName(Object arg0) {
							return name;
						}
					};
				}
				
				@Override
				public NamedList<Level> getLevels() {
					return new ArrayNamedListImpl<Level>() {
						@Override public String getName(Object arg0) {
							return name;
						}
					};
				}
				
				@Override
				public Dimension getDimension() {
					return new Dimension() {
						
						@Override
						public boolean isVisible() {
							return true;
						}
						
						@Override
						public String getUniqueName() {
							return "DIMENSION/" + name;
						}
						
						@Override
						public String getName() {
							return name;
						}
						
						@Override
						public String getDescription() {
							return name;
						}
						
						@Override
						public String getCaption() {
							return name;
						}
						
						@Override
						public NamedList<Hierarchy> getHierarchies() {
							return new ArrayNamedListImpl<Hierarchy>(Arrays.asList(new Hierarchy[1])) {
								@Override public String getName(Object arg0) {
									return name;
								}
							};
						}
						
						@Override
						public org.olap4j.metadata.Dimension.Type getDimensionType() throws OlapException {
							return Dimension.Type.MEASURE;
						}
						
						@Override
						public Hierarchy getDefaultHierarchy() {
							return null;
						}
					};
				}
				
				@Override
				public Member getDefaultMember() throws OlapException {
					return null;
				}
			};
			res[i] = new org.saiku.query.metadata.CalculatedMeasure(hier, names[i], names[i], "sum", new HashMap<String, String>());
		}
		return res;
	}
}

