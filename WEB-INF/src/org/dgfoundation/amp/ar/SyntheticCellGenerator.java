package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;

public abstract class SyntheticCellGenerator {
	private String metaDataName	= null;
	private String measureName	= null;
	private String originalMeasureName	= null;
	
	public SyntheticCellGenerator(String metaDataName, String measureName,
			String originalMeasureName) {
		super();
		this.metaDataName = metaDataName;
		this.measureName = measureName;
		this.originalMeasureName = originalMeasureName;
	}




	public Collection<CategAmountCell> generate(Collection<CategAmountCell> cells, int order) {
		ArrayList<CategAmountCell> retCells	= new ArrayList<CategAmountCell>();
		if ( cells != null ) {
			for (CategAmountCell categAmountCell : cells) {
				String fundingType		= categAmountCell.getMetaValueString(ArConstants.FUNDING_TYPE);
				
				if ( originalMeasureName.equals(fundingType) ) {
					
					try {
						CategAmountCell newCell	= (CategAmountCell) categAmountCell.clone();
						newCell.setAmount( this.computeAmount(newCell.getInitialAmount(), categAmountCell.getMetaData()) );
						newCell.setMetaData(new HashSet<Object>() );
						Iterator<MetaInfo> iter	= categAmountCell.getMetaData().iterator();
						while ( iter.hasNext() ) {
							MetaInfo tempMi	= iter.next();
							if ( tempMi != null) {
								if ( ArConstants.FUNDING_TYPE.equals(tempMi.getCategory()) ) {
									MetaInfo<FundingTypeSortedString> newMi	= 
										new MetaInfo<FundingTypeSortedString>(ArConstants.FUNDING_TYPE, new FundingTypeSortedString(measureName, order));
									newCell.getMetaData().add(newMi);
								}
								else {
									MetaInfo newMi	= new MetaInfo(tempMi.getCategory(),tempMi.getValue() );
									newCell.getMetaData().add(newMi);
								}
							}
						}
						
						
						Collection<MetaInfo> mInfos	= this.syntheticMetaInfo();
						if ( mInfos != null && mInfos.size() > 0  ) {
							for (MetaInfo metaInfo : mInfos) {
								newCell.getMetaData().add(metaInfo);
							}
						}
						
						retCells.add(newCell);
						
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return retCells;
	}

	public boolean checkIfApplicabale(AmpReports reportMetadata) {
		for ( AmpReportMeasures repMes: reportMetadata.getMeasures() ) {
			AmpMeasures measure	= repMes.getMeasure();
			if ( this.measureName.equals(measure.getMeasureName())  ) 
				return true;
		}
		return false;
	}
	
	public abstract double computeAmount (double originalAmount, Set metaData); 
	
	public abstract Collection<MetaInfo> syntheticMetaInfo ();
	

	/**
	 * @return the metaDataName
	 */
	public String getMetaDataName() {
		return metaDataName;
	}






	/**
	 * @param metaDataName the metaDataName to set
	 */
	public void setMetaDataName(String metaDataName) {
		this.metaDataName = metaDataName;
	}






	/**
	 * @return the measureName
	 */
	public String getMeasureName() {
		return measureName;
	}






	/**
	 * @param measureName the measureName to set
	 */
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}






	/**
	 * @return the originalMeasureName
	 */
	public String getOriginalMeasureName() {
		return originalMeasureName;
	}






	/**
	 * @param originalMeasureName the originalMeasureName to set
	 */
	public void setOriginalMeasureName(String originalMeasureName) {
		this.originalMeasureName = originalMeasureName;
	} 
	
	
}
