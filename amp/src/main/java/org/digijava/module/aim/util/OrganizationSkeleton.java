package org.digijava.module.aim.util;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.*;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * lightweight alternative to AmpOrganisation, usable in the (vast) majority of places in AMP where a full Org is not needed, but just a name and id
 * @author Dolghier Constantin
 *
 */
public class OrganizationSkeleton implements Comparable<OrganizationSkeleton>, HierarchyListable
{
    private Long ampOrgId;
    private String name;
    private String acronym;
    private String code;
    private String description;
    private Long orgGrpId;
    private List<Long> roleIds;
    
    boolean translatable;
    public OrganizationSkeleton() {}
    
    public OrganizationSkeleton(Long id, String name, String acronym, String code, String description, Long orgGrpId) {
        this.ampOrgId = id;
        this.name = name;
        this.acronym = acronym;
        this.code = code;
        this.description = description;
        this.orgGrpId = orgGrpId;
    }
    
    public void setAmpOrgId(Long id) { 
        this.ampOrgId = id; 
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Long getAmpOrgId() {
        return ampOrgId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getAcronym() {
        return acronym;
    }

    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }

    public Long getOrgGrpId() {
        return orgGrpId;
    }
    
    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
    
    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }
    
    public void setOrgGrpId(Long orgGrpId) {
        this.orgGrpId = orgGrpId;
    }

    /**
     * mirroring {@link AmpOrganisation#compareTo(AmpOrganisation)}, hence the shameless copypaste
     */
    @Override
    public int compareTo(OrganizationSkeleton org)
    {
        if (this.name == null)
        {
            if (org.name == null)
                return 0; // null == null
            return -1; // null < [anything]
        }
        if (org.name == null)
            return 1; // [anything] > null
        
        return this.name.toLowerCase().trim().compareTo(org.name.toLowerCase().trim());
    }
    
    @Override
    public String toString()
    {
        return String.format("%s (id: %d)", this.getName(), this.getAmpOrgId());
    }
    @Override
    public String getLabel() {
        return this.name;
    }
    @Override
    public String getUniqueId() {
        return String.valueOf(this.ampOrgId);

    }
    @Override
    public String getAdditionalSearchString() {
        return this.acronym;
    }
    @Override
    public boolean getTranslateable() {
        return this.translatable;
    }
    @Override
    public void setTranslateable(boolean translatable) {
        this.translatable = translatable;
        
    }
    @Override
    public Collection<? extends HierarchyListable> getChildren() {
        return null;
    }
    @Override
    public int getCountDescendants() {
        return 1;
    }
    private static Long nullInsteadOfZero(long val) {
        if (val == 0) {
            return null;
        }
        else return val;
    }
    
    
    public static List<OrganizationSkeleton>  populateOrganisationSkeletonListByOrgGrpIp(final List<Long> orgGrpId,
            final List<String> roleCodes) {
        final List<OrganizationSkeleton> organizations = new ArrayList<OrganizationSkeleton>();
        PersistenceManager.getSession().doWork(new Work(){
                public void execute(Connection conn) throws SQLException {
                    String orgIdsSource = "SELECT DISTINCT(o.amp_org_id) "
                            + "FROM amp_organisation o, amp_org_role aor "
                            + "WHERE o.amp_org_id = aor.organisation and aor.role IN "
                            + "(SELECT r.amp_role_id FROM amp_role r WHERE r.role_code IN (" 
                            + Util.toCSStringForIN(roleCodes) + "))";
                    
                    String condition = "where org_Grp_Id in ("+ Util.toCSStringForIN(orgGrpId) +")" +
                                        " and amp_org_id in (" + orgIdsSource + ")";
                    ViewFetcher v = DatabaseViewFetcher.getFetcherForView("amp_organisation", 
                            condition, TLSUtils.getEffectiveLangCode(), new HashMap<PropertyDescription, ColumnValuesCacher>(), conn, "*");     
                    RsInfo rsi = v.fetch(null);
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        organizations.add(new OrganizationSkeleton(nullInsteadOfZero(rs.getLong("amp_org_id")), 
                                    rs.getString("name"),
                                    rs.getString("acronym"), 
                                    rs.getString("org_code"),
                                    rs.getString("description"),
                                    nullInsteadOfZero(rs.getLong("org_grp_id"))));
                    }
                }
            });
        return organizations;
    }
    public static List<OrganizationSkeleton>  populateOrganisationSkeletonList(final String rolecode) {
        final List<OrganizationSkeleton> organizations = new ArrayList<OrganizationSkeleton>();
        PersistenceManager.getSession().doWork(new Work(){
                public void execute(Connection conn) throws SQLException {
                    
                    String orgIdsSource;
                    if (rolecode.equals(Constants.ROLE_CODE_DONOR))
                    {
                        orgIdsSource = "select DISTINCT(amp_donor_org_id) FROM amp_funding";
                    }
                    else
                    {
                        orgIdsSource = "select DISTINCT(organisation) FROM amp_org_role WHERE role = (SELECT amp_role_id FROM amp_role WHERE role_code='" + rolecode + "')";
                    }
                    String condition = "WHERE amp_org_id IN (" + orgIdsSource + ") AND " +
                            "(deleted is null or deleted = false) ";
                    ViewFetcher v = DatabaseViewFetcher.getFetcherForView("amp_organisation", 
                            condition, TLSUtils.getEffectiveLangCode(), new HashMap<PropertyDescription, ColumnValuesCacher>(), conn, "*");     
                    RsInfo rsi = v.fetch(null);
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        organizations.add(new OrganizationSkeleton(nullInsteadOfZero(rs.getLong("amp_org_id")), 
                                    rs.getString("name"),
                                    rs.getString("acronym"), 
                                    rs.getString("org_code"),
                                    rs.getString("description"),
                                    nullInsteadOfZero(rs.getLong("org_grp_id"))));
                    }
                    rsi.close();
                }
            });
        return organizations;
    }
}
