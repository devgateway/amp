/**
 * FundingTypeSortedString.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Nov 22, 2006
 * Customized ordering for FUNDING TYPE category
 */
public class FundingTypeSortedString extends SortedString {
	public final static MetaInfo []order=new MetaInfo[] {
		new MetaInfo(ArConstants.ACTUAL+" "+ArConstants.COMMITMENT,new Integer(2)),
		new MetaInfo(ArConstants.ACTUAL+" "+ArConstants.DISBURSEMENT,new Integer(4)),
		new MetaInfo(ArConstants.ACTUAL+" "+ArConstants.EXPENDITURE,new Integer(6)),
		new MetaInfo(ArConstants.PLANNED+" "+ArConstants.COMMITMENT,new Integer(1)),
		new MetaInfo(ArConstants.PLANNED+" "+ArConstants.DISBURSEMENT,new Integer(3)),
		new MetaInfo(ArConstants.PLANNED+" "+ArConstants.EXPENDITURE,new Integer(5)),
	};
	
	/**
	 * @param string
	 */
	public FundingTypeSortedString(String string) {
		super(string);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.SortedString#getOrder()
	 */
	public int getOrder() {
		int max=0;
		for(int i=0;i<order.length;i++) {
			int corder=((Integer)order[i].getValue()).intValue();
			if(corder>max) max=corder;
			if(string.equals(order[i].getCategory())) return  corder;
		}
		//if unknown, put it on the last position
		return max+1; 		
	}

}
