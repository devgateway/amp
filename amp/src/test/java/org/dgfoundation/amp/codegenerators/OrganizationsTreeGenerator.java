package org.dgfoundation.amp.codegenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Organizations dimension tree generator.
 * Separated to a special case because different levels have different parent names (org type->org group->org) 
 * @author acartaleanu
 *
 */
public class OrganizationsTreeGenerator extends TreeGenerator {

    public OrganizationsTreeGenerator() {
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
        Map<Long, TreeNode> orgTypes = getLevelNodes("amp_org_type_id", "org_type_name", "null as noparent", "v_all_organizations_with_levels");
        Map<Long, TreeNode> orgGroups = getLevelNodes("amp_org_grp_id", "org_grp_name", "amp_org_type_id", "v_all_organizations_with_levels");
        Map<Long, TreeNode> orgs= getLevelNodes("amp_org_id", "org_name", "amp_org_grp_id", "v_all_organizations_with_levels");
        connectNodesToParents(orgs, orgGroups);
        connectNodesToParents(orgGroups, orgTypes);
        roots.addAll(orgTypes.values());
        return roots;
    }

}
