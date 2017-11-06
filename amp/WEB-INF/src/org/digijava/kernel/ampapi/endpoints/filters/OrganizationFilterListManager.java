package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.util.OrganisationUtil;
import org.digijava.module.aim.util.OrganizationSkeleton;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 * This class generates the filter list (tree) object for organizations
 * 
 * @author Viorel Chihai
 *
 */
public final class OrganizationFilterListManager implements FilterListManager {
    
    private static final int COL_ORG_ID_POS = 0;
    private static final int COL_ORG_NAME_POS = 1;
    private static final int COL_ORG_ACR_POS = 2;
    private static final int COL_ORG_GRP_POS = 3;
    private static final int COL_ORG_ROLES_POS = 4;
    
    private static OrganizationFilterListManager organizationFilterListManager;
    
    /**
     * 
     * @return FiltersManager instance
     */
    public static OrganizationFilterListManager getInstance() {
        if (organizationFilterListManager == null) {
            organizationFilterListManager = new OrganizationFilterListManager();
        }

        return organizationFilterListManager;
    }
    
    private OrganizationFilterListManager() { }

    @Override
    public FilterList getFilterList() {
        List<FilterTreeDefinition> orgTreeDefinitions = getOrgTreeDefinitions();
        List<FilterTreeNode> orgTreeItems = getOrgTreeItems();
        
        return new FilterList(orgTreeDefinitions, orgTreeItems);
    }
    
    /**
     * @return List<FilterTreeNode>
     */
    public List<FilterTreeNode> getOrgTreeItems() {
        List<FilterTreeNode> items = new ArrayList<>();

        List<OrganizationSkeleton> orgsWithRoles = getAllOrganizationsWithRoles();
        Map<Long, List<FilterTreeNode>> orgFilterNodes = orgsWithRoles.stream()
                .collect(Collectors.groupingBy(OrganizationSkeleton::getOrgGrpId,
                        Collectors.mapping(os -> getFilterNodeFromOrgSkeleton(os), Collectors.toList())));

        List<AmpOrgType> orgTypes = getOrgTypes();
        for (AmpOrgType orgType : orgTypes) {
            FilterTreeNode typeNode = new FilterTreeNode();
            typeNode.setId(orgType.getAmpOrgTypeId());
            typeNode.setName(orgType.getName());

            for (AmpOrgGroup orgGroup : orgType.getOrgGroups()) {
                if (orgFilterNodes.containsKey(orgGroup.getAmpOrgGrpId())) {
                    FilterTreeNode groupNode = new FilterTreeNode();
                    groupNode.setId(orgGroup.getAmpOrgGrpId());
                    groupNode.setName(orgGroup.getName());

                    orgFilterNodes.get(orgGroup.getAmpOrgGrpId()).forEach(o -> groupNode.addChild(o));

                    typeNode.addChild(groupNode);
                }
            }

            items.add(typeNode);
        }

        return items;
    }

    /**
     * @param treeDefinitions
     */
    public List<FilterTreeDefinition> getOrgTreeDefinitions() {
        List<FilterTreeDefinition> treeDefinitions = new ArrayList<>();
        List<AmpRole> visibleRoles = getVisibleRoles();

        for (AmpRole role : visibleRoles) {
            FilterTreeDefinition treeDefinition = new FilterTreeDefinition();
            treeDefinition.setId(role.getAmpRoleId());
            treeDefinition.setName(role.getName());
            treeDefinition.setDisplayName(TranslatorWorker.translateText(role.getName()));
            treeDefinition.setFilterIds(FiltersConstants.ORG_ROLE_CODE_TO_FILTER_TREE_IDS.get(role.getRoleCode()));
            treeDefinitions.add(treeDefinition);
        }

        return treeDefinitions;
    }

    private FilterTreeNode getFilterNodeFromOrgSkeleton(OrganizationSkeleton os) {
        FilterTreeNode orgNode = new FilterTreeNode();

        orgNode.setId(os.getAmpOrgId());
        orgNode.setName(os.getName());
        orgNode.setAcronym(os.getAcronym());
        orgNode.setTreeIds(os.getRoleIds());

        return orgNode;
    }

    /**
     * 
     * @return List<AmpOrgType> organization types
     */
    private List<AmpOrgType> getOrgTypes() {
        Session session = PersistenceManager.getSession();
        List<AmpOrgType> orgTypes = session.createCriteria(AmpOrgType.class).addOrder(Order.asc("orgType")).list();

        return orgTypes;
    }

    /**
     * 
     * @return List<AmpRole> visible Roles
     */
    public List<AmpRole> getVisibleRoles() {
        Set<String> visibleColumns = ColumnsVisibility.getVisibleColumns();

        List<String> visibleRoleCodes = OrganisationUtil.ROLE_CODE_TO_COLUMN_MAP.entrySet().stream()
                .filter(e -> visibleColumns.contains(e.getValue())).map(Entry::getKey).collect(Collectors.toList());

        Session session = PersistenceManager.getSession();
        List<AmpRole> allRoles = session.createCriteria(AmpRole.class).list();

        List<AmpRole> visibleRoles = allRoles.stream().filter(r -> visibleRoleCodes.contains(r.getRoleCode()))
                .collect(Collectors.toList());

        return visibleRoles;
    }

    /**
     * 
     * @return List<OrganizationSkeleton> organizations with roles
     */
    public List<OrganizationSkeleton> getAllOrganizationsWithRoles() {
        List<Long> visibleRoleIds = getVisibleRoles().stream().map(AmpRole::getAmpRoleId).collect(Collectors.toList());

        Session session = PersistenceManager.getSession();
        String query = "SELECT orgId, orgName, orgAcronym, grpId, roles FROM v_all_organizations_with_roles";
        List<Object[]> rows = session.createSQLQuery(query).list();
        
        ArrayList<OrganizationSkeleton> orgsWithRoles = new ArrayList<OrganizationSkeleton>();
        for (Object[] row : rows) {
            String treeIds = (String) row[COL_ORG_ROLES_POS];
            List<Long> orgRoleIds = Arrays.stream(treeIds.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            orgRoleIds.retainAll(visibleRoleIds);

            if (!orgRoleIds.isEmpty()) {
                OrganizationSkeleton org = new OrganizationSkeleton();
                org.setAmpOrgId(Long.parseLong(row[COL_ORG_ID_POS].toString()));
                org.setName(row[COL_ORG_NAME_POS] != null ? row[COL_ORG_NAME_POS].toString() : "");
                org.setAcronym(row[COL_ORG_ACR_POS] != null ? row[COL_ORG_ACR_POS].toString() : "");
                org.setOrgGrpId(Long.parseLong(row[COL_ORG_GRP_POS].toString()));
                org.setRoleIds(orgRoleIds);

                orgsWithRoles.add(org);
            }
        }

        return orgsWithRoles;
    }

}
