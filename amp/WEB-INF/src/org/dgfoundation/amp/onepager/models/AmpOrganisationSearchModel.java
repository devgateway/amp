/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mpostelnicu@dgateway.org since Sep 28, 2010
 */
public class AmpOrganisationSearchModel extends
		AbstractAmpAutoCompleteModel<AmpOrganisation> {
	private static final Logger logger = LoggerFactory.getLogger(AmpOrganisationSearchModel.class);
	public enum PARAM implements AmpAutoCompleteModelParam {
		TYPE_FILTER, GROUP_FILTER
	};

	public AmpOrganisationSearchModel(String input,String language,Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, language, params);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 8211300754918658832L;
	private Session session;

	@Override
	protected List<AmpOrganisation> load() {
		final List<AmpOrganisation> ret = new ArrayList<AmpOrganisation>();

		try {
			session = PersistenceManager.getRequestDBSession();
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		session.doWork(new Work() {
			@SuppressWarnings("deprecation")
			@Override
			public void execute(Connection connection) throws SQLException {
				String sqlQuery = "SELECT org.amp_org_id, org.name, org.acronym, org.org_type, orgname.translation  from amp_organisation org"
						+ " JOIN amp_content_translation orgname ON org.amp_org_id = orgname.object_id"
						+ " AND orgname.field_name = 'name'"
						+ " AND orgname.object_class ='org.digijava.module.aim.dbentity.AmpOrganisation'"
						+ " AND orgname.locale = '" + TLSUtils.getEffectiveLangCode() + "'"
						+ " WHERE (org.deleted IS NULL OR org.deleted = false)";
				
				if (params != null) {
					if (getParams().get(PARAM.GROUP_FILTER) != null) { 
						AmpOrgGroup orgroup = (AmpOrgGroup) getParams().get(PARAM.GROUP_FILTER);
						sqlQuery = sqlQuery +  " AND org_grp_id = " + orgroup.getIdentifier();
					}
				}
				
				if (input.length() > 0 ){
					sqlQuery = sqlQuery + " AND (((orgname.translation ILIKE '%"+ input + "%' OR acronym ILIKE '%"+ input +"%')"
							+ " AND orgname.translation is null)"
							+ " OR ((orgname.translation ILIKE '%" +input+ "%' OR acronym ILIKE '%"+ input +"%')"
							+ " AND orgname.translation is not null))" ;
				}
				
				if (getParams().get(PARAM.TYPE_FILTER) != null){
					AmpOrgType orgtype =  (AmpOrgType) getParams().get(PARAM.TYPE_FILTER);
					sqlQuery = sqlQuery +  " AND org.orgtype = " + orgtype.getIdentifier();
				}
				
				Integer maxResults = (Integer) getParams().get(AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
				if (maxResults != null && maxResults.intValue() != 0){
					sqlQuery = sqlQuery + " LIMIT " + maxResults;
				}
				
				
				RsInfo rsi = SQLUtils.rawRunQuery(connection, sqlQuery, null);
				ResultSet rs = rsi.rs;
				while (rs.next()) {	
					AmpOrganisation orgtoadd = new AmpOrganisation();
					orgtoadd.setAmpOrgId(rs.getLong("amp_org_id"));
					if (rs.getString("translation") != null){
						orgtoadd.setName(rs.getString("translation"));
					}else{
						orgtoadd.setName(rs.getString("name"));
					}
					orgtoadd.setAcronymAndName(rs.getString("acronym"));
					ret.add(orgtoadd);
				}

			}
		});
		return ret;
	}

}
