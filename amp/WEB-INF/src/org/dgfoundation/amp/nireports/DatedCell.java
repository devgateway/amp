package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.meta.CategCell;

/**
 * a cell which has a date
 * @author Dolghier Constantin
 *
 */
public interface DatedCell extends CategCell {
//	public final static String CATEG_YEAR = "Year";
//	public final static String CATEG_QUARTER = "Quarter";
//	public final static String CATEG_MONTH = "Month";
//	
//	public default int getYear() {
//		return NiUtils.getInt(getMetaInfo().getMetaInfo(CATEG_YEAR).v);
//	}
//	
//	public default int getQuarter() {
//		return NiUtils.getInt(getMetaInfo().getMetaInfo(CATEG_QUARTER).v);
//	}
//
//	public default int getMonth() {
//		return NiUtils.getInt(getMetaInfo().getMetaInfo(CATEG_MONTH).v);
//	}
	
	public TranslatedDate getTranslatedDate();
	
//	public default int getYear() {
//		return getTranslatedDate().year;
//	}
//
//	public default int getQuarter() {
//		return getTranslatedDate().quarter;
//	}
//
//	public default int getMonth() {
//		return getTranslatedDate().month;
//	}
}
