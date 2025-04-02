/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.digijava.module.aim.util;

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Used to display tree like structures in selectors
 * @author mpostelnicu@dgateway.org
 * since Oct 22, 2010
 */
public interface AmpAutoCompleteDisplayable<C> extends Comparable<C> {
    
    public static class AmpAutoCompleteComparator implements Comparator<AmpAutoCompleteDisplayable> {
        Collator collator;
        public AmpAutoCompleteComparator() {
            collator=Collator.getInstance();
            collator.setStrength(Collator.PRIMARY);
        }
        
        @Override
        public int compare(AmpAutoCompleteDisplayable o1,
                AmpAutoCompleteDisplayable o2) {
            return collator.compare(o1.getAutoCompleteLabel(),o2.getAutoCompleteLabel());
        }
        
    };

    default Iterable<AmpAutoCompleteDisplayable<C>> ancestors() {
        return () -> new AncestorIterator<>(getParent());
    }

    class AncestorIterator<C> implements Iterator<AmpAutoCompleteDisplayable<C>> {

        private AmpAutoCompleteDisplayable<C> node;

        private AncestorIterator(AmpAutoCompleteDisplayable<C> node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public AmpAutoCompleteDisplayable<C> next() {
            AmpAutoCompleteDisplayable<C> ret = node;
            node = node.getParent();
            return ret;
        }
    }
    
    AmpAutoCompleteDisplayable<C> getParent();
    <T extends AmpAutoCompleteDisplayable> Collection<T> getSiblings();
    <T extends AmpAutoCompleteDisplayable> Collection<T> getVisibleSiblings();
    String getAutoCompleteLabel();
    
    default <T extends AmpAutoCompleteDisplayable> Collection<T> getNonDeletedChildren() {
        return getSiblings();
    }

}
