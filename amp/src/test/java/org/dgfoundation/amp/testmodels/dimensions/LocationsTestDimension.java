package org.dgfoundation.amp.testmodels.dimensions;

import static org.dgfoundation.amp.testmodels.dimensions.HNDNode.element;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.testmodels.TestModelConstants;



public class LocationsTestDimension extends HardcodedNiDimension {

	public final static LocationsTestDimension instance = new LocationsTestDimension("locs", TestModelConstants.LOCATIONS_DIMENSION_DEPTH);
	
	public LocationsTestDimension(String name, int depth) {
		super(name, depth);
	}

	@Override
	protected List<HNDNode> buildHardcodedElements() {
		return Arrays.asList(
				element(8977, "Moldova", 
					element(9085, "Anenii Noi County", 
						element(9108, "Bulboaca", 
							element(9118, "Bulboaca Veche" ) ), 
						element(9109, "Hulboaca" ), 
						element(9110, "Dolboaca" ) ), 
					element(9086, "Balti County", 
						element(9111, "Glodeni", 
							element(9117, "Viisoara" ) ), 
						element(9112, "Raureni" ), 
						element(9113, "Apareni" ) ), 
					element(9087, "Cahul County", 
						element(9120, "AAA", 
							element(9121, "some new location" ) ) ), 
					element(9088, "Chisinau City" ), 
					element(9089, "Chisinau County" ), 
					element(9090, "Drochia County" ), 
					element(9091, "Dubasari County" ), 
					element(9092, "Edinet County" ), 
					element(9093, "Falesti County" ), 
					element(9094, "Hincesti County" ), 
					element(9095, "Ialoveni County" ), 
					element(9096, "Lapusna County" ), 
					element(9097, "Leova County" ), 
					element(9098, "Nisporeni County" ), 
					element(9099, "Orhei County" ), 
					element(9100, "Riscani County" ), 
					element(9101, "Soroca County" ), 
					element(9102, "Straseni County" ), 
					element(9103, "Telenesti County" ), 
					element(9104, "Tighina County" ), 
					element(9105, "Transnistrian Region", 
						element(9114, "Tiraspol" ), 
						element(9115, "Slobozia" ), 
						element(9116, "Camenca" ) ), 
					element(9106, "U.T.A. Gagauzia" ), 
					element(9107, "Ungheni County" ) ), 
				element(8991, "Maldives" ), 
				element(9032, "Sweden" )); 
	}
}
