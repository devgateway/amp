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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

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
				//The following line was commented out because it added only the parent hierarchy in the list.
				//getParams().put(AbstractAmpAutoCompleteModel.PARAM.EXACT_MATCH, false);
				if (input.trim().length() > 0){
					Object o = getTextCriterion("name", input);
					if (o instanceof SimpleExpression){
						crit.add(((SimpleExpression)o).ignoreCase());
					}
					else{
						crit.add((Criterion)o);
					}
				}

				Integer maxResults = (Integer) getParams().get(
						AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
				if (maxResults != null && maxResults != 0)
					crit.setMaxResults(maxResults);

				List<AmpTheme> themes = new ArrayList<AmpTheme>();

				themes = crit.list();
				if(!isExactMatch()){
					//This code avoids mixing programs. Individual AmpTheme objects are connected to AmpActivityProgramSettings through ONLY the root AmpTheme
					//as default hierarchy.
					ArrayList<AmpTheme> sameProgramThemes = new ArrayList<AmpTheme>();
					for(AmpTheme theme : themes){
						AmpTheme parentTheme = theme.getRootTheme();
						if(parentTheme.getAmpThemeId().equals(def.getAmpThemeId())){
							sameProgramThemes.add(theme);
						}
					}
					ret.addAll((Collection<? extends AmpTheme>) createTreeView(sameProgramThemes));
				}
				else
				{
					ret.addAll((Collection<? extends AmpTheme>) createTreeView(themes));
				}

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
