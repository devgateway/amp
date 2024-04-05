package org.digijava.module.aim.util;

import org.dgfoundation.amp.ar.viewfetcher.*;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * lightweight alternative to AmpOrgGroup, usable in the (vast) majority of places in AMP where a full OrgGroup is not needed, but just a name and id
 * @author Dolghier Constantin
 *
 */
public class OrgGroupSkeleton implements Comparable<OrgGroupSkeleton>, HierarchyListable
{
    private Long ampOrgGrpId;   
    private String orgGrpName;  
    private String orgGrpCode;
    private Long orgGrpType;
    private boolean translatable;
    
    public OrgGroupSkeleton() { }
    
    public OrgGroupSkeleton(Long id, String name, String code, Long orgTypeId) {
        this.ampOrgGrpId = id;
        this.orgGrpName = name;
        this.orgGrpCode = code;
        this.orgGrpType = orgTypeId; 
        this.translatable = true;
    }
    
    public void setAmpOrgGrpId(Long id) {
        this.ampOrgGrpId = id;
    }
    
    public void setOrgGrpName(String name) {
        this.orgGrpName = name;
    }
    
    
    public Long getAmpOrgGrpId() {
        return ampOrgGrpId;
    }

    public String getOrgGrpName() {
        return orgGrpName;
    }

    public Long getOrgTypeId() {
        return this.orgGrpType;
    }

    public String getOrgGrpCode() {
        return orgGrpCode;
    }

    @Override
    public int compareTo(OrgGroupSkeleton org)
    {
        if (this.orgGrpName == null)
        {
            if (org.orgGrpName == null)
                return 0; // null == null
            return -1; // null < [anything]
        }
        if (org.orgGrpName == null)
            return 1; // [anything] > null
        
        return this.orgGrpName.trim().compareTo(org.orgGrpName.trim());
    }
    
    @Override
    public String toString()
    {
        return String.format("%s (id: %d)", this.orgGrpName, this.ampOrgGrpId);
    }
    
    private static Long nullInsteadOfZero(long val) {
        if (val == 0) {
            return null;
        }
        else return val;
    }

    public static List<OrgGroupSkeleton>  populateSkeletonOrgGroupsList() {
        final List<OrgGroupSkeleton> orgGroups = new ArrayList<OrgGroupSkeleton>();
        PersistenceManager.getSession().doWork(new Work(){
                public void execute(Connection conn) throws SQLException {
                    ViewFetcher v = DatabaseViewFetcher.getFetcherForView("amp_org_group", 
                            "", TLSUtils.getEffectiveLangCode(), new HashMap<PropertyDescription, ColumnValuesCacher>(), conn, "*");
                    RsInfo rsi = v.fetch(null);
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        orgGroups.add(new OrgGroupSkeleton(nullInsteadOfZero(rs.getLong("amp_org_grp_id")), 
                                                        rs.getString("org_grp_name"), 
                                                        rs.getString("org_grp_code"), 
                                                        nullInsteadOfZero(rs.getLong("org_type"))));
                    }
                    rsi.close();
                }
            });
        return orgGroups;
    }

    @Override
    public String getLabel() {
        return this.getOrgGrpName();
    }

    @Override
    public String getUniqueId() {
        return String.valueOf(this.getAmpOrgGrpId());
    }

    @Override
    public String getAdditionalSearchString() {
        return this.orgGrpCode;

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

    
    
    
    
}
