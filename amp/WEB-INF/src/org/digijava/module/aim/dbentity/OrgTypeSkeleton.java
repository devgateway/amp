package org.digijava.module.aim.dbentity;


import org.dgfoundation.amp.ar.viewfetcher.*;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.util.HierarchyListable;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class OrgTypeSkeleton implements Comparable<OrgTypeSkeleton>, HierarchyListable {
    private String orgTypeName;
    private Long orgTypeId;
    private String orgTypeCode;
    private Boolean isGovernmental;
    private String classification;
    
    private boolean translatable;
    

    public OrgTypeSkeleton(Long id, String name, String code, Boolean governmental, String classification) {
        this.orgTypeId = id;
        this.orgTypeName = name;
        this.orgTypeCode = code;
        this.isGovernmental = governmental;
        this.classification = classification;
        this.translatable = true;
    }
    
    
    
    @Override
    public String getLabel() {
        return this.orgTypeName;
    }

    @Override
    public String getUniqueId() {
        return String.valueOf(this.orgTypeId);
    }

    @Override
    public String getAdditionalSearchString() {
        return this.getOrgTypeCode();
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
    
    @Override
    public int compareTo(OrgTypeSkeleton o) {
        return this.orgTypeName.compareTo((o).getOrgTypeName());
    }
    public static List<OrgTypeSkeleton> populateTypeSkeletonList() {
        final List<OrgTypeSkeleton> orgTypes= new ArrayList<OrgTypeSkeleton>();
        PersistenceManager.getSession().doWork(new Work(){
                public void execute(Connection conn) throws SQLException {
                    ViewFetcher v = DatabaseViewFetcher.getFetcherForView("amp_org_type", 
                            "", TLSUtils.getEffectiveLangCode(), new HashMap<PropertyDescription, ColumnValuesCacher>(), conn, "*");
                    RsInfo rsi = v.fetch(null);
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        orgTypes.add(new OrgTypeSkeleton(nullInsteadOfZero(rs.getLong("amp_org_type_id")), 
                                                        rs.getString("org_type"), 
                                                        rs.getString("org_type_code"),
                                                        rs.getBoolean("org_type_is_governmental"),
                                                        rs.getString("org_type_classification")));
                    }
                    rsi.close();
                }
            });
        return orgTypes;

    }

    public String getOrgTypeName() {
        return orgTypeName;
    }

    public Long getOrgTypeId() {
        return orgTypeId;
    }

    public String getOrgTypeCode() {
        return orgTypeCode;
    }

    public Boolean getIsGovernmental() {
        return isGovernmental;
    }

    public String getClassification() {
        return classification;
    }
}
