package org.digijava.module.aim.startup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.dgfoundation.amp.visibility.data.MeasuresVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.hibernate.Session;

/**
 * this class scans for newly-added measures in amp_measures which are not present there and adds them, in all the templates installed in the system. The just-added measure are then enabled in case the MeasuresVisibility-by-AF algorithm for the said template would enable them<br />
 * A measure might be missing in the FM for one of the following reasons: <br /><ol>
 * <li>it has been deleted by one of the prior commits to AMP-19577</li>
 * <li>it has been added in the meantime</li></ol>
 * @author Constantin Dolghier
 *
 */
public class RecreateFMEntries {
	
	protected static final Logger logger = Logger.getLogger(RecreateFMEntries.class);
	
	public void doIt(ServletContext ampContext) {
		logger.info("adding newly-created-measures entries to the FM and enabling them according to the AF configuration...");

		List<String>  measuresToRestore = new ArrayList<>();
		measuresToRestore.addAll(MeasuresVisibility.allMeasures);		 
		/* // the code commented below is for the case list of incompletely-deleted-by-prior-commits to AMP-19451 measures
		
		List<String> adjustments = Arrays.asList("Actual", "Planned", "Pipeline");
		List<String> transactions = Arrays.asList(ArConstants.COMMITMENT, ArConstants.DISBURSEMENT, ArConstants.EXPENDITURE, ArConstants.DISBURSEMENT_ORDERS);
		for(String adj:adjustments)
			for(String tr:transactions)
				measuresToRestore.add(adj + " " + tr);
		measuresToRestore.add("Actual Disbursements - Capital");
		measuresToRestore.add("Planned Disbursements - Capital");*/
		long measuresModuleId = PersistenceManager.getLong(
				PersistenceManager.getSession().createSQLQuery("select mod.id from amp_modules_visibility mod JOIN amp_modules_visibility rep ON lower(rep.name)='reporting' and rep.id = mod.parent WHERE lower(mod.name)='measures'")
				.uniqueResult());
		for(Object templateIdObj: PersistenceManager.getSession().createSQLQuery("select id from amp_templates_visibility").list()) {
			long templateId = PersistenceManager.getLong(templateIdObj);
			MeasuresVisibility mv = new MeasuresVisibility();
			Set<String> visibleMeasuresByAf = mv.detectVisibleData_AF(templateId);
			createAndInsertValues(templateId, measuresModuleId, new HashSet<>(measuresToRestore), visibleMeasuresByAf);
		}
		PersistenceManager.getSession().flush();
		logger.info("\t...done!");
	}
	
	protected void createAndInsertValues(Long templateId, Long moduleId, Set<String> rawFeatureNames, Set<String> featuresToSet) {
		Session session = PersistenceManager.getSession();
		Set<String> featureNames = new TreeSet<>(rawFeatureNames);
		AmpTemplatesVisibility template = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class, templateId);
		AmpModulesVisibility module = (AmpModulesVisibility) session.load(AmpModulesVisibility.class, moduleId);
		for(AmpObjectVisibility preexistentFeature:module.getOrCreateItems())
			featureNames.remove(preexistentFeature.getName());
		for(String featureName:featureNames) {
			logger.error(String.format("Measure <%s> does not exist in the FM database, template <%s>, adding it: %s", featureName, template.getName(), featuresToSet.contains(featureName) ? "enabled" : "disabled"));
			AmpFeaturesVisibility feature = new AmpFeaturesVisibility();
			feature.setParent(module);
			feature.setName(featureName);
			feature.setHasLevel(false);
							
			module.getOrCreateItems().add(feature);
			session.save(feature);
			
	      	if (featuresToSet.contains(featureName)) {
				template.getFeatures().add(feature);
	      	}
		}
		session.update(module);
		session.update(template);
		session.flush();
	}
}
