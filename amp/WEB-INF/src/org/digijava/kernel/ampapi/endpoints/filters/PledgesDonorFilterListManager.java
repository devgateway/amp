package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.helper.Constants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import com.google.common.collect.ImmutableList;

/**
 * This class generates the filter list (tree) object for pledge donors
 * 
 * @author Viorel Chihai
 *
 */
public final class PledgesDonorFilterListManager implements FilterListManager {
    
    private static final List<String> PLEDGE_FILTER_IDS = 
            new ImmutableList.Builder<String>()
                .add(FiltersConstants.PLEDGES_DONOR_TYPE)
                .add(FiltersConstants.PLEDGES_DONOR_GROUP)
            .build();

    // AMP-28716: Name is the same than pledgeIdentification.jsp
    private static final String PLEDGES_DONORS_DEFINITION_NAME = "Organization Group";
    private static final String PLEDGES_DONORS_ITEMS_NAME = "donors";
    
    private static PledgesDonorFilterListManager pledgesDonorFilterListManager;
    private static Long donorRoleId = null;
    private static final Long ID = 99L;
    
    /**
     * 
     * @return FiltersManager instance
     */
    public static PledgesDonorFilterListManager getInstance() {
        if (pledgesDonorFilterListManager == null) {
            pledgesDonorFilterListManager = new PledgesDonorFilterListManager();
        }

        return pledgesDonorFilterListManager;
    }
    
    private PledgesDonorFilterListManager() { }

    @Override
    public FilterList getFilterList() {
        List<FilterListDefinition> orgTreeDefinitions = getPledgesDonorListDefinitions();
        Map<String, List<FilterListTreeNode>> orgTreeItems = getOrgListItems();
        
        return new FilterList(orgTreeDefinitions, orgTreeItems);
    }
    
    /**
     * @return List<FilterTreeNode>
     */
    public Map<String, List<FilterListTreeNode>> getOrgListItems() {
        Map<String, List<FilterListTreeNode>> items = new HashMap<>();
        List<FilterListTreeNode> orgItems = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        ids.add(ID);

        Set<Long> donorGroupsIds = getAllGroupIdsWithDonors();
        
        List<AmpOrgType> allOrgTypes = getOrgTypes();
        for (AmpOrgType orgType : allOrgTypes) {
            FilterListTreeNode typeNode = new FilterListTreeNode();
            typeNode.setId(orgType.getAmpOrgTypeId());
            typeNode.setName(orgType.getName());
            
            List<AmpOrgGroup> orgGroups = orgType.getOrgGroups().stream()
                .filter(orgGroup -> donorGroupsIds.contains(orgGroup.getAmpOrgGrpId()))
                .sorted(Comparator.comparing(AmpOrgGroup::getOrgGrpName))
                .collect(Collectors.toList());
            
            if (!orgGroups.isEmpty()) {
                for (AmpOrgGroup orgGroup : orgGroups) {
                    FilterListTreeNode groupNode = new FilterListTreeNode();
                    groupNode.setId(orgGroup.getAmpOrgGrpId());
                    groupNode.setName(orgGroup.getName());
                    // By setting listDefinitionIds we use the same logic than regular Donors in the filter widget.
                    groupNode.setListDefinitionIds(ids);
                    typeNode.addChild(groupNode);
                }
                
                orgItems.add(typeNode);
            }
        }
        orgItems.add(getUndefinedOption());
        
        items.put(PLEDGES_DONORS_ITEMS_NAME, orgItems);

        return items;
    }

    /**
     * @param treeDefinitions
     */
    public List<FilterListDefinition> getPledgesDonorListDefinitions() {
        List<FilterListDefinition> listDefinitions = new ArrayList<>();

        FilterListDefinition listDefinition = new FilterListDefinition();
        listDefinition.setName(PLEDGES_DONORS_DEFINITION_NAME);
        listDefinition.setDisplayName(TranslatorWorker.translateText(PLEDGES_DONORS_DEFINITION_NAME));
        listDefinition.setFilterIds(PLEDGE_FILTER_IDS);
        listDefinition.setItems(PLEDGES_DONORS_ITEMS_NAME);
        listDefinition.setTab(EPConstants.TAB_ORGANIZATIONS);
        listDefinition.setFiltered(true);
        listDefinition.setId(ID);
        listDefinitions.add(listDefinition);

        return listDefinitions;
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
     * @return Set<Long> organization groups with donors
     */
    public Set<Long> getAllGroupIdsWithDonors() {
        Session session = PersistenceManager.getSession();
        String query = "SELECT grpId FROM v_all_organizations_with_roles "
                + "WHERE roles like '%" + getDonorRoleId() + "%'";
        
        List<Long> rows = (List<Long>) session.createSQLQuery(query).list().stream()
                .map(o -> Long.parseLong(o.toString()))
                .collect(Collectors.toList());
        
        Set<Long> orgGroupIdsWithDonors = new HashSet<>(rows);
        
        return orgGroupIdsWithDonors;
    }
    
    public Long getDonorRoleId() {
        if (donorRoleId == null) {
            Session session = PersistenceManager.getSession();
            String queryString = "SELECT aor.ampRoleId FROM " + AmpRole.class.getName() + " aor "
                    + "WHERE aor.roleCode=:roleCode";
            
            Query query = session.createQuery(queryString);
            query.setParameter("roleCode", Constants.FUNDING_AGENCY);
            
            donorRoleId = (Long) query.uniqueResult();
        }
        
        return donorRoleId;
    }

}
