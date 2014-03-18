package org.digijava.module.categorymanager.util;

import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import lombok.Data;

/**
 * shim of if (id, value) used for JSPs
 * @author Dolghier Constantin
 *
 */
@Data
public class IdWithValueShim {
	public Long id;
	public String value;
	
	public IdWithValueShim(Long id, String value)
	{
		this.id = id;
		this.value = value;
	}
	
	public IdWithValueShim(AmpCategoryValue acv)
	{
		this(acv.getId(), acv.getValue());
	}
	
	public IdWithValueShim(AmpOrgGroup grp)
	{
		this(grp.getAmpOrgGrpId(), grp.getOrgGrpName());
	}
}
