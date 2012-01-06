package org.dgfoundation.amp.ar;

import java.util.Set;

public class CapitalExpenditureCellGenerator extends SyntheticCellGenerator {

	public CapitalExpenditureCellGenerator(String metaDataName,
			String measureName, String originalMeasureName) {
		super(metaDataName, measureName, originalMeasureName);
	}

	@Override
	public double computeAmount(double originalAmount, Set metaData) {
		MetaInfo<Double> mi	= MetaInfo.getMetaInfo( metaData, this.getMetaDataName() );
		Double capitalPercent	= 0.0;
		if ( mi != null && mi.getValue() != null ) {
			capitalPercent		= mi.getValue();
		}
		double ret	= originalAmount * (100.0 - capitalPercent) / 100.0;
		
		return ret;
	}

}
