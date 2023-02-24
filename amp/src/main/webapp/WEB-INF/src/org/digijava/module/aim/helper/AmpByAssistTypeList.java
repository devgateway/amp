/**
 * AmpByAssistTypeList.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Feb 10, 2006
 * 
 */
public class AmpByAssistTypeList extends ArrayList {
    public AmpByAssistTypeList() {
        super();
    }

    
    public boolean addAll(Collection c) {
        if(c==null) return true;
        Iterator i=c.iterator();
        while (i.hasNext()) {
            AmpByAssistTypeAmount element = (AmpByAssistTypeAmount) i.next();
            add(element);
        }
        return true;        
    }
    
    public boolean removeAll(Collection c) {
        if(c==null) return true;
        Iterator i=c.iterator();
        while (i.hasNext()) {
            AmpByAssistTypeAmount element = (AmpByAssistTypeAmount) i.next();
            remove(element);
        }
        return true;        
    }
    
    
    public boolean add(Object o) {
        AmpByAssistTypeAmount abt = (AmpByAssistTypeAmount) o;
        Iterator i = this.iterator();
        while (i.hasNext()) {
            AmpByAssistTypeAmount element = (AmpByAssistTypeAmount) i.next();
            if (element.getFundingTerms().equals(abt.getFundingTerms())) {
                element.addFundingAmount(abt.getFundingAmount());
                return true;
            }
            
        }
        super.add(abt.clone());
        return true;
    }
    
    
    public boolean remove(Object o) {
        AmpByAssistTypeAmount abt = (AmpByAssistTypeAmount) o;
        Iterator i = this.iterator();
        while (i.hasNext()) {
            AmpByAssistTypeAmount element = (AmpByAssistTypeAmount) i.next();
            if (element.getFundingTerms().equals(abt.getFundingTerms())) {
                element.substractFundingAmount(abt.getFundingAmount());
                return true;
            }
            
        }
        super.add(abt);
        return true;
    }
}
