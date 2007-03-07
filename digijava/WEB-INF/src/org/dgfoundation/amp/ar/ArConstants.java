/**
 * ArConstants.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 15, 2006
 *
 */
public final class ArConstants {
	public final static MetaInfo []prefixes=new MetaInfo[] {
		//PLEASE KEEP THE SAME ORDER IN prefixes AND suffixes !!
		
		new MetaInfo(GenericViews.HTML,"/repository/aim/view/ar/html/"),
		new MetaInfo(GenericViews.XLS,"org.dgfoundation.amp.ar.view.xls."),
		new MetaInfo(GenericViews.PDF,"org.dgfoundation.amp.ar.view.pdf."),
		new MetaInfo(GenericViews.PRINT,"/repository/aim/view/ar/print/"),
		new MetaInfo(GenericViews.TREE,"/repository/aim/view/ar/tree/"),
		new MetaInfo(GenericViews.FOLDABLE,"/repository/aim/view/ar/foldable/"),		
		};

	public final static MetaInfo []suffixes=new MetaInfo[] {
		new MetaInfo(GenericViews.HTML,".jsp"),
		new MetaInfo(GenericViews.XLS,"XLS"),
		new MetaInfo(GenericViews.PDF,"PDF"),
		new MetaInfo(GenericViews.PRINT,".jsp"),
		new MetaInfo(GenericViews.TREE,".jsp"),
		new MetaInfo(GenericViews.FOLDABLE,".jsp"),		
		};

	//metainfo categs:
	public final static String ADJUSTMENT_TYPE="Adjustment Type";
	public final static String TRANSACTION_TYPE="Transaction Type";
	public final static String FUNDING_TYPE="Funding Type";
	public final static String TERMS_OF_ASSISTANCE="Terms of Assistance";
	public final static String YEAR="Year";
	public final static String QUARTER="Quarter";
	public final static String PERSPECTIVE="Perspective";

	//report types
	public final static String DONOR="Donor";
	public final static String REGION="Region";
	public final static String COMPONENT="Component";

	
	//metainfo values:
	public final static String COMMITMENT="Commitments";
	public final static String DISBURSEMENT="Disbursements";
	public final static String EXPENDITURE="Expenditures";
	
	public final static String PLANNED="Planned";
	public final static String ACTUAL="Actual";
	
	public final static String UNDISBURSED_BALANCE="Undisbursed Balance";
	
	//maldives only:
	public final static String SECTOR_PERCENTAGE="Sector Percentage";
	

	//hierarchysorter
	public final static String HIERARCHY_SORTER_TITLE="Title";
}
