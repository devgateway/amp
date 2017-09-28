package org.dgfoundation.amp.codegenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Dimension tree generator (sectors, locations, programs).
 * @author acartaleanu
 *
 */
public class NaturalTreeGenerator extends TreeGenerator {


    public NaturalTreeGenerator(String table, String idColumn, String parentColumn, String nameColumn) {
        super(table, idColumn, parentColumn, nameColumn);
    }
    

    @Override
    protected List<TreeNode> generateRoots() {
        final List<TreeNode> roots = new ArrayList<TreeNode>();
        Map<Long, TreeNode> allNodes = getLevelNodes(idColumn, nameColumn, parentColumn, table);
        for (Map.Entry<Long, TreeNode> entry : allNodes.entrySet()) {
            TreeNode node = entry.getValue();
            if (node.parentId == null)
                roots.add(node);
            else 
                allNodes.get(node.parentId).children.add(node);
        }
        return roots;
    }
    
    @Override
    public List<TreeNode> getRoots() {
        return this.roots;
    }

}
