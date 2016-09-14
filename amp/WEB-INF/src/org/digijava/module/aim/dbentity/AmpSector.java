package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.*;

import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.dgfoundation.amp.ar.dimension.SectorDimension;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.NameableOrIdentifiable;
import org.digijava.module.aim.util.SoftDeletable;


@TranslatableClass (displayName = "Sector")
public class AmpSector implements Serializable, Comparable<AmpSector>, Identifiable,
		ARDimensionable, HierarchyListable, AmpAutoCompleteDisplayable,
		SoftDeletable, Cloneable, OrgProfileValue, NameableOrIdentifiable {
	@Interchangeable(fieldTitle="Sector ID", id=true)
	private Long ampSectorId;
	@Interchangeable(fieldTitle="Parent Sector ID", pickIdOnly=true)
	private AmpSector parentSectorId;
	@Interchangeable(fieldTitle="Sector Code")
	private String sectorCode;
	@Interchangeable(fieldTitle="Name", value=true)
	@TranslatableField
	private String name;
	@Interchangeable(fieldTitle="Type")
	private String type;
	private AmpOrganisation ampOrgId;
	@Interchangeable(fieldTitle="AMP Sector Scheme ID")
	private AmpSectorScheme ampSecSchemeId;
	@TranslatableField
	@Interchangeable(fieldTitle="Description")
	private String description;
	@Interchangeable(fieldTitle="Language")
	private String language;
	@Interchangeable(fieldTitle="Version")
	private String version;
	
	//unused anywhere
	@Deprecated
	private Set aidlist;
	
	
	@Interchangeable(fieldTitle="Indicators")
	private Set<AmpIndicator> indicators;
//	@Interchangeable(fieldTitle="Sectors", pickIdOnly=true)
	private Set<AmpSector> sectors;
	@Interchangeable(fieldTitle="Deleted")
	private Boolean deleted;

	private boolean translateable = true;

	@Interchangeable(fieldTitle="Sector Code Official")
	private String sectorCodeOfficial;

	@Interchangeable(fieldTitle="Segment Code")
	private String segmentCode;
	
	private transient Collection<AmpSector> transientChildren;

	public String getSegmentCode() {
		return segmentCode;
	}

	public void setSegmentCode(String segmentCode) {
		this.segmentCode = segmentCode;
	}
//
//	/**
//	 * @return
//	 */
	@Deprecated
	public Set getAidlist() {
		return aidlist;
	}

	/**
	 * @return
	 */
	public Set<AmpSector> getSectors() {
		return sectors;
	}

	public void setSectors(Set<AmpSector> sectors) {
		this.sectors = sectors;
	}


	
	
	public AmpSector getParentSectorId() {
		return parentSectorId;
	}

	/**
	 * @return
	 */
	public Long getAmpSectorId() {
		return ampSectorId;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public String getSectorCode() {
		return sectorCode;
	}

	/**
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param set
	 */
	@Deprecated
	public void setAidlist(Set set) {
		aidlist = set;
	}

	public void setParentSectorId(AmpSector sec) {
		this.parentSectorId = sec;
	}

	/**
	 * @param long1
	 */
	public void setAmpSectorId(Long long1) {
		ampSectorId = long1;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param string
	 */
	public void setSectorCode(String string) {
		sectorCode = string;
	}

	/**
	 * @param string
	 */
	public void setLanguage(String string) {
		language = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

	/**
	 * @param string
	 */
	public void setVersion(String string) {
		version = string;
	}

	public AmpOrganisation getAmpOrgId() {
		return ampOrgId;
	}

	public void setAmpOrgId(AmpOrganisation org) {
		this.ampOrgId = org;
	}

	/**
	 * @return
	 */
	public AmpSectorScheme getAmpSecSchemeId() {
		return ampSecSchemeId;
	}

	/**
	 * @param scheme
	 */
	public void setAmpSecSchemeId(AmpSectorScheme scheme) {
		ampSecSchemeId = scheme;
	}

	public int compareTo(AmpSector other) {
		return ampSectorId.compareTo(other.getAmpSectorId());
	}

	public String toString() {
		return name;
	}

	public Object getIdentifier() {
		return this.getAmpSectorId();
	}

	public Set<AmpIndicator> getIndicators() {
		return indicators;
	}

	public void setIndicators(Set<AmpIndicator> indicators) {
		this.indicators = indicators;
	}

	public Class getDimensionClass() {
		return SectorDimension.class;
	}

	public String getSectorCodeOfficial() {
		return sectorCodeOfficial;
	}

	public void setSectorCodeOfficial(String sectorCodeOfficial) {
		this.sectorCodeOfficial = sectorCodeOfficial;
	}
	
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
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
		return this.name;
	}

	@Override
	public String getUniqueId() {
		return this.ampSectorId.toString();
	}

	@Override
	public Collection<AmpSector> getChildren() {
		if (transientChildren == null)
			transientChildren = new TreeSet(new AmpAutoCompleteDisplayable.AmpAutoCompleteComparator());
		return transientChildren;
	}

	@Override
	public boolean getTranslateable() {
		return this.translateable;
	}

	@Override
	public void setTranslateable(boolean translateable) {
		this.translateable = translateable;

	}

	@Override
	public AmpAutoCompleteDisplayable getParent() {
		return parentSectorId;
	}

	@Override
	public Collection<AmpSector> getSiblings() {
		return sectors;
	}

	@Override
	public Collection<AmpSector> getVisibleSiblings() {
		return getChildren();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	public String getHierarchicalName()
	{
		String selfName = "[" + this.getName() + "]";
		if (this.getParentSectorId() == null)
			return selfName;
		return this.getParentSectorId().getHierarchicalName() + selfName;
	}

	
	public String getSectorPathString(){
		String ret = "[" + name + "]";
		AmpSector s = parentSectorId;
		while (s != null){
			ret = "[" + s.name + "]" + " - " + ret;
			s = s.getParentSectorId();
		}
		
		//ret = "[" + ampSecSchemeId.getSecSchemeName() + "]" + " - " + ret;
		return ret;
	}

    public String getAdditionalSearchString() {
        return this.sectorCode;
    }
    @Override
    public List<ValueTranslatabePair> getValuesForOrgReport(){
    	List<ValueTranslatabePair> values=new ArrayList<ValueTranslatabePair>();
    	ValueTranslatabePair value=new ValueTranslatabePair(Arrays.asList(new String[]{getSectorPathString()}),false);
    	values.add(value);
    	return values;
    }
    @Override
	public String[] getSubHeaders() {
		return null;
	}

    public static String sqlStringForName(String idSource)
    {
    	return InternationalizedModelDescription.getForProperty(AmpSector.class, "name").getSQLFunctionCall(idSource);
    }

    public static String hqlStringForName(String idSource)
    {
    	return InternationalizedModelDescription.getForProperty(AmpSector.class, "name").getSQLFunctionCall(idSource + ".ampSectorId");
    }

	@Override
	public Collection<AmpAutoCompleteDisplayable> getNonDeletedChildren() {
		Collection<AmpSector> children = getChildren();
		Collection<AmpAutoCompleteDisplayable> res = new ArrayList<>(children.size());
		for (AmpSector theme: children) {
			if (!theme.isSoftDeleted())
				res.add(theme);
		}
		return res;
	}

	@Override
	public boolean isSoftDeleted() {
		return Boolean.TRUE.equals(this.deleted);
	}
}
