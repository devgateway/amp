package org.dgfoundation.amp.nireports.testcases;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;




/**
 * HNDNode stands for Hardcoded Ni Dimension Node -- a leaf in the tree of a NiDimension hierarchical model.
 * A list of HNDNodes is supplied to the constructor of a test Ni dimension.
 * NiDimension code generators generate a list of HNDNodes (just like NiColumn generators create lists of cells). 
 * 
 * @author acartaleanu
 *
 */
public class HNDNode {

    
    public final long id;
    public final String name;
    public final List<HNDNode> children;
    
    public HNDNode(long id, String name, List<HNDNode> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }

    public Set<Long> getChildrenIds() {
        Set<Long> res = new HashSet<Long>();
        for (HNDNode node : this.children)
            res.add(node.id);
        return res;
    }
    public static HNDNode element(long id, String name, HNDNode ... children) {
        return new HNDNode(id, name, Arrays.asList(children));
    }
    
    public String toString() {
        return String.format("%d : %s", this.id, this.name);
    }
}
