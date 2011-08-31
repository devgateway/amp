package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.dgfoundation.amp.ar.dimension.LocationsDimension;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * 
 * @author medea
 */
public class AmpCategoryValueLocations implements Identifiable,
		HierarchyListable, ARDimensionable, Serializable {

	private Long id;
	private String name;
	private AmpCategoryValue parentCategoryValue;
	private AmpCategoryValueLocations parentLocation;
	private Set<AmpCategoryValueLocations> childLocations;
	private String description;
	private String gsLat;
	private String gsLong;
	private String geoCode;
	private String code;
	private String iso3;
	
	private boolean translateable	= true;

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

	private String iso;

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
		if (o instanceof AmpCategoryValueLocations) {
			AmpCategoryValueLocations loc = (AmpCategoryValueLocations) o;
			return id.equals(loc.getId());
		} else
			return false;
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
	public String getUniqueId() {
		return this.id + "";
	}
	
	@Override
	public boolean getTranslateable() {
		return translateable;
	}

	@Override
	public void setTranslateable(boolean translateable) {
		this.translateable = translateable;
	}

	@Override
	public Class getDimensionClass() {
		return LocationsDimension.class;
	}
}
