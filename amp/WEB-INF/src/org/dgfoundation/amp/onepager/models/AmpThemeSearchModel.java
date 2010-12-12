/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.onepager.models.AmpSectorSearchModel.PARAM;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author aartimon@dginternational.org since Oct 22, 2010
 */
public class AmpThemeSearchModel extends
		AbstractAmpAutoCompleteModel<AmpTheme> {

	public AmpThemeSearchModel(String input,
			Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, params);
	}

	private static final long serialVersionUID = 1L;
	public enum PARAM implements AmpAutoCompleteModelParam {
		PROGRAM_TYPE
	};
	
	@Override
	protected List<AmpTheme> load() {
		try {
			List<AmpTheme> ret = new ArrayList<AmpTheme>();
			try {
				Session session = PersistenceManager.getRequestDBSession();
				String queryString = " from " + AmpTheme.class.getName()
						+ " t WHERE t.name like :name";
				Query qry = session.createQuery(queryString);
				Integer maxResults = (Integer) getParams().get(
						AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
				if (maxResults != null && maxResults != 0)
					qry.setMaxResults(maxResults);
				qry.setString("name", "%" + input + "%");
				
				List<AmpTheme> themes = new ArrayList<AmpTheme>();
				
				themes = qry.list();
				ret = createTreeView(themes);
				
				String pType = (String) getParams().get(
						PARAM.PROGRAM_TYPE);
				AmpActivityProgramSettings aaps = ProgramUtil.getAmpActivityProgramSettings(pType);
				AmpTheme def = aaps.getDefaultHierarchy();
				if (def != null){
					AmpTheme defUsed = new AmpTheme();
					defUsed.setName(def.getName());
					defUsed.setAmpThemeId(def.getAmpThemeId());
					defUsed.setTransientBoolean(true);
					ret.add(0, defUsed);
				}
				session.close();
			} catch (Exception e) {
				throw new DgException("Cannot retrive all themes from db",e);
			}
			return ret;
		} catch (DgException e) {
			throw new RuntimeException(e);
		}
	}

}
