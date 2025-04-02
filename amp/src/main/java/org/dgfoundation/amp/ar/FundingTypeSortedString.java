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
    private int order;  

    public FundingTypeSortedString(String string, int order) {
        super(string);
        this.order = order;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.SortedString#getOrder()
     */
    @Override
    public int getOrder() {
        return order;       
    }
}
