package org.dgfoundation.amp.codegenerators;

import java.util.ArrayList;
import java.util.List;


/**
 * Tree element used in dimensions code generation. 
 * 
 * @author acartaleanu
 *
 */
public class TreeNode {
    final Long parentId;
    final long id;
    final String name;
    final List<TreeNode> children;
    public TreeNode(long id, String name, Long parentId) {
        this.id = id;
        this.name = CodeGenerator.cleanup(name);
        this.parentId = parentId;
        this.children = new ArrayList<TreeNode>();
    }
}
