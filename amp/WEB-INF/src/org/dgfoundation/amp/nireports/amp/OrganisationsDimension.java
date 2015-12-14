package org.dgfoundation.amp.nireports.amp;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;

/**
 * 
 * a dimension consisting of (org_type[level=0], org_group[level=1], organisation[level=2]) 
 * @author Dolghier Constantin
 *
 */
public final class OrganisationsDimension extends SqlSourcedNiDimension<String> {
		
	public OrganisationsDimension(String name) {
		super("Organisations dimension", "v_ni_orgs_dimension", Arrays.asList("amp_org_type_id", "amp_org_grp_id", "amp_org_id"));
	}


//	@Override
//	protected void fetchAuxiliaryData(NiReportsEngine engine) {
//		List<String> locales = TranslatorUtil.getLocaleCache(SiteUtils.getDefaultSite());
//		AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);
//		locales.forEach(locale -> {
//			ViewFetcher fetcher = new I18nDatabaseViewFetcher(this.sourceViewName, null, locale, scratchpad.columnCachers, scratchpad.connection, "org_name", "org_grp_name", "org_type_name");
//			OrgsInfo localeInfo = new OrgsInfo();
//			fetcher.forEach(ExceptionConsumer.of(rs -> {
//				addIfOk(localeInfo.organisations, rs.getLong("amp_org_id"), rs.getString("org_name"));
//				addIfOk(localeInfo.orgGroups, rs.getLong("amp_org_grp_id"), rs.getString("org_grp_name"));
//				addIfOk(localeInfo.orgTypes, rs.getLong("amp_org_type_id"), rs.getString("org_type_name"));
//			}));
//			
//		});
//	}

//
//	public String getOrgGroupName(long orgGrpId) {
//		return info.get(TLSUtils.getEffectiveLangCode()).orgGroups.get(orgGrpId);
//	}
//
//	public String getOrgTypeName(long orgTypeId) {
//		return info.get(TLSUtils.getEffectiveLangCode()).orgTypes.get(orgTypeId);
//	}

//	@Override
//	protected Map<Long, String> fetchDimensionLevel(Connection conn, String locale, int level, Set<Long> ids) {
//		switch(level) {
//			case LEVEL_ORGANISATION:
//				return fetchOrganisations(conn, locale, ids);
//			case LEVEL_ORGANISATION_GROUP:
//				return fetchOrgGroups(conn, locale, ids);
//			case LEVEL_ORGANISATION_TYPE:
//				return fetchOrgTypes(conn, locale, ids);
//			default:
//				throw new RuntimeException("unknown level: " + level);
//		}
//	}
	
//	protected Map<Long, String> fetchOrganisations(Connection conn, String locale, Set<Long> ids) {
//		return DatabaseViewFetcher.fetchViewAsKeyValue(conn, locale, "amp_organisation", "amp_org_id", "org_name");
//	}
//	
//	protected Map<Long, String> fetchOrgGroups(Connection conn, String locale, Set<Long> ids) {
//		return DatabaseViewFetcher.fetchViewAsKeyValue(conn, locale, "amp_org_group", "amp_org_grp_id", "org_grp_name");		
//	}
//	
//	protected Map<Long, String> fetchOrgTypes(Connection conn, String locale, Set<Long> ids) {
//		return DatabaseViewFetcher.fetchViewAsKeyValue(conn, locale, "amp_org_group", "amp_org_type_id", "org_type");
//	}
	
	public final static int LEVEL_ORGANISATION_TYPE = 0;
	public final static int LEVEL_ORGANISATION_GROUP = 1;
	public final static int LEVEL_ORGANISATION = 2;
}
