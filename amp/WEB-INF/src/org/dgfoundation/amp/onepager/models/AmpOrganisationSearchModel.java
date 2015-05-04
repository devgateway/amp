/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */

package org.dgfoundation.amp.onepager.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dgfoundation.amp.ar.FilterParam;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpOrgGroup;

/**
 * @author mpostelnicu@dgateway.org since Sep 28, 2010
 */
public class AmpOrganisationSearchModel extends AbstractAmpAutoCompleteModel<AmpOrganisation> {
    private static final Logger logger = LoggerFactory.getLogger(AmpOrganisationSearchModel.class);

    public enum PARAM implements AmpAutoCompleteModelParam {
	TYPE_FILTER, GROUP_FILTER
    };

    public AmpOrganisationSearchModel(String input, String language, Map<AmpAutoCompleteModelParam, Object> params) {
	super(input, language, params);
	// TODO Auto-generated constructor stub
    }

    private static final long serialVersionUID = 8211300754918658832L;
    private Session session;

    @Override
    protected List<AmpOrganisation> load() {
	final List<AmpOrganisation> ret = new ArrayList<AmpOrganisation>();

	session = PersistenceManager.getSession();

	session.doWork(new Work() {
		@SuppressWarnings("deprecation")
		@Override
		public void execute(Connection connection) throws SQLException {
		    String sqlQuery = "SELECT org.amp_org_id, org.name, org.acronym, org.org_type, orgname.translation  from amp_organisation org"
				    + " LEFT OUTER JOIN amp_content_translation orgname ON org.amp_org_id = orgname.object_id"
				    + " AND orgname.field_name = ? "
				    + " AND orgname.object_class =? "
				    + " AND orgname.locale = ?" 
				    + " WHERE (org.deleted IS NULL OR org.deleted = ?)";
		    AmpOrgGroup orgroup =null;

			if (getParams()!=null && getParams().get(PARAM.GROUP_FILTER) != null) {
			    orgroup = (AmpOrgGroup) getParams().get(PARAM.GROUP_FILTER);
			    sqlQuery = sqlQuery + " AND org_grp_id = ?";
			}

		    if (input.length() > 0) {
			sqlQuery = sqlQuery + " AND (((orgname.translation ILIKE ? OR acronym ILIKE ?"
					+ ") AND orgname.translation is null)"
					+ " OR ((orgname.translation ILIKE ? OR acronym ILIKE ?) AND orgname.translation is not null))";
		    }
		    AmpOrgType orgtype =null;
		    if (getParams()!=null && getParams().get(PARAM.TYPE_FILTER) != null) {
		    orgtype = (AmpOrgType) getParams().get(PARAM.TYPE_FILTER);
			sqlQuery = sqlQuery + " AND org_grp_id in( "+
					" select  amp_org_grp_id from amp_org_group where org_type=?)";
		    }
		    
		    Integer maxResults = (Integer) getParams().get(AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
		    if (maxResults != null && maxResults.intValue() != 0) {
			sqlQuery = sqlQuery + " LIMIT " + maxResults;
		    }
		    
		    ArrayList<FilterParam> params = new ArrayList<FilterParam>();
		    
		    params.add(new FilterParam("name", java.sql.Types.VARCHAR));
		    params.add(new FilterParam("org.digijava.module.aim.dbentity.AmpOrganisation", java.sql.Types.VARCHAR));
		    params.add(new FilterParam(TLSUtils.getEffectiveLangCode(), java.sql.Types.VARCHAR));
		    params.add(new FilterParam(false, java.sql.Types.BOOLEAN));
		    
			if (getParams() != null && getParams().get(PARAM.GROUP_FILTER) != null) {
				params.add(new FilterParam(orgroup.getIdentifier(), java.sql.Types.BIGINT));
			}
		    if (input!=null && input.length() > 0) {
			    params.add(new FilterParam("%"+input + "%", java.sql.Types.VARCHAR));
			    params.add(new FilterParam("%"+input + "%", java.sql.Types.VARCHAR));
			    params.add(new FilterParam("%"+input + "%", java.sql.Types.VARCHAR));
			    params.add(new FilterParam("%"+input + "%", java.sql.Types.VARCHAR));
		    }
		    if (getParams() != null && getParams().get(PARAM.TYPE_FILTER) != null) {
				params.add(new FilterParam(orgtype.getIdentifier(), java.sql.Types.BIGINT));
		    }
		    
		    RsInfo rsi = SQLUtils.rawRunQuery(connection, sqlQuery, params);
		    ResultSet rs = rsi.rs;
		    while (rs.next()) {
			AmpOrganisation orgtoadd = new AmpOrganisation();
			orgtoadd.setAmpOrgId(rs.getLong("amp_org_id"));
			if (rs.getString("translation") != null) {
			    orgtoadd.setName(rs.getString("translation"));
			} else {
			    orgtoadd.setName(rs.getString("name"));
			}
			orgtoadd.setAcronym(rs.getString("acronym"));
			orgtoadd.setAcronymAndName(rs.getString("acronym") + "-" + (rs.getString("name")));
			ret.add(orgtoadd);
		    }
		    rsi.close();
		}
    });

	Collections.sort(ret);
	return ret;
    }

}
