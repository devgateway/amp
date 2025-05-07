package org.digijava.module.aim.helper;

import org.apache.struts.util.LabelValueBean;

import java.text.Collator;
import java.util.Comparator;

/**
 * Class is used to compare LabelValueBean
 * in different locales using label values
 *
 * @author medea
 */
public class LabelValueBeanComparator implements Comparator<LabelValueBean>{
    private Collator collator;
    public LabelValueBeanComparator(Collator collator){
        this.collator=collator;

    }

    public int compare(LabelValueBean o1, LabelValueBean o2) {
       return collator.compare(o1.getLabel(), o2.getLabel());
    }

}
