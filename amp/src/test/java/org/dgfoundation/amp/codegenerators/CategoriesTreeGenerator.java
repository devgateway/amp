package org.dgfoundation.amp.codegenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Categories node generator (has to be a special case since nodes are contained in two different tables,
 * and columns are heterogenous).
 * @author acartaleanu
 *
 */
public class CategoriesTreeGenerator extends TreeGenerator {

    public CategoriesTreeGenerator() {
        super(null, null, null, null);
    }

    
    private void connectNodesToParents(Map<Long, TreeNode> children, Map<Long, TreeNode> parents) {
        for (Map.Entry<Long, TreeNode> entry : children.entrySet()) {
            parents.get(entry.getValue().parentId).children.add(entry.getValue());
        }
    }
    
    @Override
    public List<TreeNode> generateRoots() {
        final List<TreeNode> roots = new ArrayList<TreeNode>();
        Map<Long, TreeNode> accs = getLevelNodes("acc_id", "acc_name", "null as noparent", "v_ni_category_values");
        Map<Long, TreeNode> acvs = getLevelNodes("acv_id", "acv_name", "acc_id", "v_ni_category_values");
        
        connectNodesToParents(acvs, accs);
        roots.addAll(accs.values());
        return roots;
    }

}
