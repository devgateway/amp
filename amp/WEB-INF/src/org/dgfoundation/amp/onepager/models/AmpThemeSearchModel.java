/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * @author aartimon@dginternational.org since Oct 22, 2010
 */
public class AmpThemeSearchModel extends AbstractAmpAutoCompleteModel<AmpTheme> {

	public AmpThemeSearchModel(String input,
			Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, params);
	}

	private static final long serialVersionUID = 1L;

	public enum PARAM implements AmpAutoCompleteModelParam {
		PROGRAM_TYPE
	};

	@Override
	protected Collection<AmpTheme> load() {
		try {
			List<AmpTheme> ret = new ArrayList<AmpTheme>();
			Session session = null;
			try {
				session = PersistenceManager.getRequestDBSession();

				String pType = (String) getParams().get(PARAM.PROGRAM_TYPE);
				AmpActivityProgramSettings aaps = ProgramUtil
						.getAmpActivityProgramSettings(pType);
				AmpTheme def = aaps.getDefaultHierarchy();
				
				Criteria crit = session.createCriteria(AmpTheme.class);
				crit.setCacheable(true);

				if (input.trim().length() > 0)
					crit.add(getTextCriterion("name", input));
				
				if(!isExactMatch())
					crit.add(Restrictions.eq("parentThemeId",def));

				Integer maxResults = (Integer) getParams().get(
						AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
				if (maxResults != null && maxResults != 0)
					crit.setMaxResults(maxResults);

				List<AmpTheme> themes = new ArrayList<AmpTheme>();

				themes = crit.list();
				ret.addAll((Collection<? extends AmpTheme>) createTreeView(themes));

				if (isExactMatch())
					return ret;

				
				if (def != null) {
					AmpTheme defUsed = new AmpTheme();
					defUsed.setName(def.getName());
					defUsed.setAmpThemeId(def.getAmpThemeId());
					defUsed.setTransientBoolean(true);
					ret.add(0, defUsed);
				}
			} catch (Exception e) {
				throw new DgException("Cannot retrive all themes from db", e);
			} finally {
				try {
					PersistenceManager.releaseSession(session);
				} catch (HibernateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return ret;
		} catch (DgException e) {
			throw new RuntimeException(e);
		}
	}

}
