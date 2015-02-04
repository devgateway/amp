/**
 * 
 */
package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.module.aim.helper.Constants;

/**
 * @author Nadejda Mandrescu
 */
public class OrganisationUtil {
	
	@SuppressWarnings("serial")
	public static final Map<String, String> ROLE_CODE_TO_COLUMN_MAP = new HashMap<String, String>() {{
		put(Constants.ROLE_CODE_DONOR, ColumnConstants.DONOR_AGENCY);
		put(Constants.ROLE_CODE_IMPLEMENTING_AGENCY, ColumnConstants.IMPLEMENTING_AGENCY);
		//put(Constants.ROLE_CODE_REPORTING_AGENCY, null);
		put(Constants.ROLE_CODE_BENEFICIARY_AGENCY, ColumnConstants.BENEFICIARY_AGENCY);
		put(Constants.ROLE_CODE_EXECUTING_AGENCY, ColumnConstants.EXECUTING_AGENCY);
		put(Constants.ROLE_CODE_RESPONSIBLE_ORG, ColumnConstants.RESPONSIBLE_ORGANIZATION);
		put(Constants.ROLE_CODE_CONTRACTING_AGENCY, ColumnConstants.CONTRACTING_AGENCY);
	}};
	
	/**
	 * @return a list of role codes (e.g. 'BA', 'DN') that are enabled in Feature Manager
	 */
	public static final List<String> getVisibleRoles() {
		Set<String> visibleColumns = ColumnsVisibility.getVisibleColumns();
		List<String> roles = new ArrayList<String>();
		for (Entry<String, String> roleColumn : ROLE_CODE_TO_COLUMN_MAP.entrySet()) 
			if (visibleColumns.contains(roleColumn.getValue()))
				roles.add(roleColumn.getKey());
		return roles;
	}
}
