package org.dgfoundation.amp.nireports.amp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.algo.ExceptionConsumer;
import org.dgfoundation.amp.ar.viewfetcher.I18nDatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.SiteUtils;

public final class OrganisationsDimension extends SqlSourcedNiDimension {

	/**
	 * textual dimension info indexed by language
	 */
	Map<String, OrgsInfo> info;
		
	public OrganisationsDimension(String name) {
		super("Organisations dimension", "v_ni_orgs_dimension", Arrays.asList("amp_org_id", "amp_org_grp_id", "amp_org_type_id"));
	}


	@Override
	public boolean dimensionChanged(NiReportsEngine engine) {
		return true;
	}

	@Override
	protected void fetchAuxiliaryData(NiReportsEngine engine) {
		List<String> locales = TranslatorUtil.getLocaleCache(SiteUtils.getDefaultSite());
		AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);
		locales.forEach(locale -> {
			ViewFetcher fetcher = new I18nDatabaseViewFetcher(this.sourceViewName, null, locale, scratchpad.columnCachers, scratchpad.connection, "org_name", "org_grp_name", "org_type_name");
			OrgsInfo localeInfo = new OrgsInfo();
			fetcher.forEach(ExceptionConsumer.of(rs -> {
				addIfOk(localeInfo.organisations, rs.getLong("amp_org_id"), rs.getString("org_name"));
				addIfOk(localeInfo.orgGroups, rs.getLong("amp_org_grp_id"), rs.getString("org_grp_name"));
				addIfOk(localeInfo.orgTypes, rs.getLong("amp_org_type_id"), rs.getString("org_type_name"));
			}));
			
		});
	}

	public String getOrganisationName(long ampOrgId) {
		return info.get(TLSUtils.getEffectiveLangCode()).organisations.get(ampOrgId);
	}

	public String getOrgGroupName(long orgGrpId) {
		return info.get(TLSUtils.getEffectiveLangCode()).orgGroups.get(orgGrpId);
	}

	public String getOrgTypeName(long orgTypeId) {
		return info.get(TLSUtils.getEffectiveLangCode()).orgTypes.get(orgTypeId);
	}

	class OrgsInfo {
		public final Map<Long, String> organisations = new HashMap<>();
		public final Map<Long, String> orgGroups = new HashMap<>();
		public final Map<Long, String> orgTypes = new HashMap<>();
	}
}
