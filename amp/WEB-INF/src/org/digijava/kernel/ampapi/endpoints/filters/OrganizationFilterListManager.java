package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.OrganisationUtil;
import org.digijava.module.aim.util.OrganizationSkeleton;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import com.google.common.collect.ImmutableMap;

/**
 * This class generates the filter list (tree) object for organizations
 *
 * @author Viorel Chihai
 */
public final class OrganizationFilterListManager implements FilterListManager {

    private static final int COL_ORG_ID_POS = 0;
    private static final int COL_ORG_NAME_POS = 1;
    private static final int COL_ORG_ACR_POS = 2;
    private static final int COL_ORG_GRP_POS = 3;
    private static final int COL_ORG_ROLES_POS = 4;

    private static final String ORGANIZATIONS_ITEMS_NAME = "organizations";

    private static final Map<String, String> ORG_ROLE_CODE_TO_TAB =
            new ImmutableMap.Builder<String, String>()
                    .put(Constants.ROLE_CODE_DONOR, EPConstants.TAB_ORGANIZATIONS)
                    .put(Constants.ROLE_CODE_IMPLEMENTING_AGENCY, EPConstants.TAB_ALL_AGENCIES)
                    .put(Constants.ROLE_CODE_BENEFICIARY_AGENCY, EPConstants.TAB_ALL_AGENCIES)
                    .put(Constants.ROLE_CODE_EXECUTING_AGENCY, EPConstants.TAB_ALL_AGENCIES)
                    .put(Constants.ROLE_CODE_RESPONSIBLE_ORG, EPConstants.TAB_ALL_AGENCIES)
                    .put(Constants.ROLE_CODE_CONTRACTING_AGENCY, EPConstants.TAB_ALL_AGENCIES)
                    .put(Constants.ROLE_CODE_REGIONAL_GROUP, EPConstants.TAB_ALL_AGENCIES)
                    .put(Constants.ROLE_CODE_SECTOR_GROUP, EPConstants.TAB_ALL_AGENCIES)
                    .put(Constants.ROLE_CODE_COMPONENT_FUNDING_ORGANIZATION, EPConstants.TAB_ALL_AGENCIES)
                    .put(Constants.ROLE_CODE_COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION, EPConstants.TAB_ORGANIZATIONS)
                    .build();

    private static OrganizationFilterListManager organizationFilterListManager;

    /**
     * @return FiltersManager instance
     */
    public static OrganizationFilterListManager getInstance() {
        if (organizationFilterListManager == null) {
            organizationFilterListManager = new OrganizationFilterListManager();
        }

        return organizationFilterListManager;
    }

    private OrganizationFilterListManager() {
    }

    @Override
    public FilterList getFilterList() {
        List<FilterListDefinition> orgTreeDefinitions = getOrgListDefinitions();
        Map<String, List<FilterListTreeNode>> orgTreeItems = getOrgListItems();

        return new FilterList(orgTreeDefinitions, orgTreeItems);
    }

    /**
     * @return List<FilterTreeNode>
     */
    public Map<String, List<FilterListTreeNode>> getOrgListItems() {
        Map<String, List<FilterListTreeNode>> items = new HashMap<>();
        List<FilterListTreeNode> orgItems = new ArrayList<>();

        List<OrganizationSkeleton> orgsWithRoles = getAllOrganizationsWithRoles();
        Map<Long, List<FilterListTreeNode>> orgFilterNodes = orgsWithRoles.stream()
                .collect(Collectors.groupingBy(OrganizationSkeleton::getOrgGrpId,
                        Collectors.mapping(os -> getFilterNodeFromOrgSkeleton(os), Collectors.toList())));

        List<AmpOrgType> orgTypes = getOrgTypes();
        for (AmpOrgType orgType : orgTypes) {
            FilterListTreeNode typeNode = new FilterListTreeNode();
            typeNode.setId(orgType.getAmpOrgTypeId());
            typeNode.setName(orgType.getName());

            List<AmpOrgGroup> orderedGroups = orgType.getOrgGroups().stream()
                    .sorted(Comparator.comparing(AmpOrgGroup::getName))
                    .collect(Collectors.toList());

            for (AmpOrgGroup orgGroup : orderedGroups) {
                if (orgFilterNodes.containsKey(orgGroup.getAmpOrgGrpId())) {
                    FilterListTreeNode groupNode = new FilterListTreeNode();
                    groupNode.setId(orgGroup.getAmpOrgGrpId());
                    groupNode.setName(orgGroup.getName());

                    orgFilterNodes.get(orgGroup.getAmpOrgGrpId()).forEach(o -> groupNode.addChild(o));

                    typeNode.addChild(groupNode);
                }
            }

            orgItems.add(typeNode);
        }
        orgItems.add(getUndefinedOption());

        items.put(ORGANIZATIONS_ITEMS_NAME, orgItems);

        return items;
    }

    public List<FilterListDefinition> getOrgListDefinitions() {
        List<FilterListDefinition> listDefinitions = new ArrayList<>();
        List<AmpRole> visibleRoles = getVisibleRoles();

        for (AmpRole role : visibleRoles) {
            if (FiltersConstants.ORG_ROLE_CODE_TO_FILTER_LIST_IDS.get(role.getRoleCode()) != null) {
                FilterListDefinition listDefinition = new FilterListDefinition();
                listDefinition.setId(role.getAmpRoleId());
                listDefinition.setName(role.getName());
                listDefinition.setDisplayName(TranslatorWorker.translateText(role.getName()));
                listDefinition.setFilterIds(FiltersConstants.ORG_ROLE_CODE_TO_FILTER_LIST_IDS.get(role.getRoleCode()));
                listDefinition.setFiltered(false);
                listDefinition.setItems(ORGANIZATIONS_ITEMS_NAME);
                listDefinition.setTab(ORG_ROLE_CODE_TO_TAB.get(role.getRoleCode()));
                listDefinitions.add(listDefinition);
            }
        }

        return listDefinitions;
    }

    private FilterListTreeNode getFilterNodeFromOrgSkeleton(OrganizationSkeleton os) {
        FilterListTreeNode orgNode = new FilterListTreeNode();

        orgNode.setId(os.getAmpOrgId());
        orgNode.setName(os.getName());
        orgNode.setAcronym(os.getAcronym());
        orgNode.setListDefinitionIds(os.getRoleIds());

        return orgNode;
    }

    /**
     * @return List<AmpOrgType> organization types
     */
    private List<AmpOrgType> getOrgTypes() {
        Session session = PersistenceManager.getSession();
        List<AmpOrgType> orgTypes = session.createCriteria(AmpOrgType.class).addOrder(Order.asc("orgType")).list();

        return orgTypes;
    }

    /**
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
     * @return List<OrganizationSkeleton> organizations with roles
     */
    public List<OrganizationSkeleton> getAllOrganizationsWithRoles() {
        List<Long> visibleRoleIds = getVisibleRoles().stream().map(AmpRole::getAmpRoleId).collect(Collectors.toList());

        Session session = PersistenceManager.getSession();
        String query = "SELECT orgId, orgName, orgAcronym, grpId, roles FROM v_all_organizations_with_roles "
                + "ORDER BY orgname";
        List<Object[]> rows = session.createNativeQuery(query).list();

        ArrayList<OrganizationSkeleton> orgsWithRoles = new ArrayList<OrganizationSkeleton>();
        for (Object[] row : rows) {
            String treeIds = (String) row[COL_ORG_ROLES_POS];
            List<Long> orgRoleIds = Arrays.stream(treeIds.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            orgRoleIds.retainAll(visibleRoleIds);

            if (!orgRoleIds.isEmpty() && row[COL_ORG_GRP_POS] != null) {
                OrganizationSkeleton org = new OrganizationSkeleton();
                org.setAmpOrgId(Long.parseLong(row[COL_ORG_ID_POS].toString()));
                org.setName(row[COL_ORG_NAME_POS] != null ? row[COL_ORG_NAME_POS].toString() : "");
                org.setAcronym(row[COL_ORG_ACR_POS] != null ? row[COL_ORG_ACR_POS].toString() : "");
                org.setOrgGrpId(Long.parseLong(row[COL_ORG_GRP_POS].toString()));
                org.setRoleIds(orgRoleIds);

                orgsWithRoles.add(org);
            }
        }

        List<OrganizationSkeleton> orderedOrgs = orgsWithRoles.stream()
                .sorted(Comparator.comparing(OrganizationSkeleton::getName))
                .collect(Collectors.toList());

        return orderedOrgs;
    }

}
