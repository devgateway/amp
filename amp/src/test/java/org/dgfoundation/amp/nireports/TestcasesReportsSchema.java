package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.mondrian.MondrianETL;
import org.dgfoundation.amp.newreports.IdsGeneratorSource;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.NiReportsEngine;


public class TestcasesReportsSchema extends AmpReportsSchema {
	public static IdsGeneratorSource workspaceFilter;
	public final static TestcasesReportsSchema instance = new TestcasesReportsSchema(); 
	
	private TestcasesReportsSchema() {
	}

	@Override
	public Set<Long> getWorkspaceActivities(NiReportsEngine engine) {
		if (engine.spec.isAlsoShowPledges())
			return AmpCollections.union(workspaceFilter.getIds(), getWorkspacePledges(engine).stream().map(z -> z + MondrianETL.PLEDGE_ID_ADDER).collect(Collectors.toSet()));
		
		return workspaceFilter.getIds();
	}
}
