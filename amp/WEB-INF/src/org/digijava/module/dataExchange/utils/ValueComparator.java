package org.digijava.module.dataExchange.utils;

import java.util.Comparator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 3/5/14
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ValueComparator  implements Comparator {

    Map base;
    public ValueComparator(Map base) {
        this.base = base;
    }

    public int compare(Object a, Object b) {
//			if("Add new".compareTo((String)this.base.get(a)) ==0) return -1;
//			if("Add new".compareTo((String)this.base.get(b)) ==0) return 1;
        if (this.base == null) return -1;
        if (this.base.get(a)== null) return -1;
        if (this.base.get(b)== null) return 1;
        return ((String)this.base.get(a)).compareTo((String)this.base.get(b));
    }
}
