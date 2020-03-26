package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.*;

import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.dgfoundation.amp.ar.dimension.LocationsDimension;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.NameableOrIdentifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * 
 * @author medea
 */
@TranslatableClass (displayName = "Location")
public class AmpCategoryValueLocations implements Identifiable, Comparable<AmpCategoryValueLocations>, 
        HierarchyListable, ARDimensionable, Serializable, AmpAutoCompleteDisplayable,OrgProfileValue, NameableOrIdentifiable {
    //IATI-check: this is not to be ignored, but not importable, since it's obtained from possible values
    private Long id;
    @TranslatableField
    private String name;
    private AmpCategoryValue parentCategoryValue;
    private AmpCategoryValueLocations parentLocation;
    private Set<AmpCategoryValueLocations> childLocations = new HashSet<>();
    private String description;
    private String gsLat;
    private String gsLong;
    private String geoCode;
    private String code;
    private String iso3;
    private String fullName;
    
    private Boolean deleted;
    
    private boolean translateable   = false;
    private String iso;
    
    
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeoCode() {
        return geoCode;
    }

    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }

    public String getGsLat() {
        return gsLat;
    }

    public void setGsLat(String gsLat) {
        this.gsLat = gsLat;
    }

    public String getGsLong() {
        return gsLong;
    }

    public void setGsLong(String gsLong) {
        this.gsLong = gsLong;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AmpCategoryValue getParentCategoryValue() {
        return parentCategoryValue;
    }

    public void setParentCategoryValue(AmpCategoryValue parentCategoryValue) {
        this.parentCategoryValue = parentCategoryValue;
    }

    public AmpCategoryValueLocations getParentLocation() {
        return parentLocation;
    }

    public void setParentLocation(AmpCategoryValueLocations parentLocation) {
        this.parentLocation = parentLocation;
    }

    public Set<AmpCategoryValueLocations> getChildLocations() {
        return childLocations;
    }

    public void setChildLocations(Set<AmpCategoryValueLocations> childLocations) {
        this.childLocations = childLocations;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AmpCategoryValueLocations)) {
            return false;
        }
        AmpCategoryValueLocations that = (AmpCategoryValueLocations) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Object getIdentifier() {
        return this.id;
    }

    @Override
    public Collection<AmpCategoryValueLocations> getChildren() {
        return this.childLocations;
    }

    @Override
    public int getCountDescendants() {
        int ret = 1;
        if (this.getChildren() != null) {
            for (HierarchyListable hl : this.getChildren())
                ret += hl.getCountDescendants();
        }
        return ret;
    }
    @Override
    public String getLabel() {
        return this.name;
    }
    @Override
    public String getAutoCompleteLabel() {
        return getFormattedLocationName(this);
    }
    protected String getFormattedLocationName(AmpCategoryValueLocations l) {
        return getFormattedLocationName(new StringBuffer(), l).toString();
    }

    protected StringBuffer getFormattedLocationName(StringBuffer output,
            AmpCategoryValueLocations l) {
        if (l.getParentLocation() != null)
            getFormattedLocationName(output, l.getParentLocation());
        return output.append("[").append(l.getName()).append("] ");
    }

    @Override
    public String getUniqueId() {
        return String.valueOf(this.id.longValue());
    }
    
    @Override
    public boolean getTranslateable() {
        return translateable;
    }

    @Override
    public void setTranslateable(boolean translateable) {
        this.translateable = translateable;
    }
    
    public Boolean getDeleted() {
        return deleted;
    }
    
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    
    public boolean isSoftDeleted() {
        return Boolean.TRUE.equals(deleted);
    }

    @Override
    public Class getDimensionClass() {
        return LocationsDimension.class;
    }

    @Override
    public AmpAutoCompleteDisplayable getParent() {
        // TODO Auto-generated method stub
        return parentLocation;
    }

    @Override
    public <T extends AmpAutoCompleteDisplayable> Collection<T> getSiblings() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends AmpAutoCompleteDisplayable> Collection<T> getVisibleSiblings() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getAdditionalSearchString() {
        return this.code;
    }
    
    @Override
    public List<ValueTranslatabePair> getValuesForOrgReport(){
        List<ValueTranslatabePair> values=new ArrayList<ValueTranslatabePair>();
        ValueTranslatabePair value=new ValueTranslatabePair(Arrays.asList(new String[]{getName()}),false);
        values.add(value);
        return values;
    }

    @Override
    public String[] getSubHeaders() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int compareTo(AmpCategoryValueLocations o) {
        return id.compareTo(o.getId());
    }
    
    /**
     * computes the name of the ACVL in the form [Country][Region]...[Location]
     * @return
     */
    public String getHierarchicalName()
    {
        String myName = "[" + this.getName() + "]";
        if (this.getParentLocation() == null)
            return myName;
        return getParentLocation().getHierarchicalName() + myName;
    }
    
    public static String hqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpCategoryValueLocations.class, "name").getSQLFunctionCall(idSource + ".id");
    }
}
