package org.digijava.module.aim.startup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.dgfoundation.amp.visibility.data.MeasuresVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.Session;

/**
 * this class scans for newly-added measures in amp_measures which are not present there and adds them, in all the templates installed in the system. 
 * The just-added measure are then enabled in case the MeasuresVisibility-by-AF algorithm for the said template would enable them<br />
 * A measure might be missing in the FM for one of the following reasons: <br /><ol>
 * <li>it has been deleted by one of the prior commits to AMP-19577</li>
 * <li>it has been added in the meantime</li></ol>
 * @author Constantin Dolghier
 *
 */
public class RecreateFMEntries {
    
    protected static final Logger logger = Logger.getLogger(RecreateFMEntries.class);
    
    
    @SuppressWarnings("serial")
    protected static final Set<String> oldMeasuresToRestore = new HashSet<String>() {{
        add(MeasureConstants.ACTUAL_DISBURSEMENT_ORDERS);
        add(MeasureConstants.ACTUAL_EXPENDITURES);
        add(MeasureConstants.PLANNED_EXPENDITURES);
        add(MeasureConstants.PLANNED_DISBURSEMENT_ORDERS);
        add(MeasureConstants.PLEDGES_ACTUAL_PLEDGE);
        add(MeasureConstants.PIPELINE_COMMITMENTS);
        add(MeasureConstants.PLANNED_DISBURSEMENTS_CAPITAL);
        add(MeasureConstants.PLANNED_DISBURSEMENTS_EXPENDITURE);
        add(MeasureConstants.ACTUAL_DISBURSEMENTS_RECURRENT);
        add(MeasureConstants.ACTUAL_DISBURSEMENTS_CAPITAL);
        add(MeasureConstants.REAL_DISBURSEMENTS);
        add(MeasureConstants.ACTUAL_ESTIMATED_DISBURSEMENTS);
        add(MeasureConstants.PLANNED_ESTIMATED_DISBURSEMENTS);
        add(MeasureConstants.PIPELINE_ESTIMATED_DISBURSEMENTS);
        add(MeasureConstants.ACTUAL_RELEASE_OF_FUNDS);
        add(MeasureConstants.PLANNED_RELEASE_OF_FUNDS);
        add(MeasureConstants.PIPELINE_RELEASE_OF_FUNDS);
        add(MeasureConstants.OFFICIAL_DEVELOPMENT_AID_COMMITMENTS);
        add(MeasureConstants.BILATERAL_SSC_COMMITMENTS);
        add(MeasureConstants.TRIANGULAR_SSC_COMMITMENTS);
        add(MeasureConstants.PLANNED_COMMITMENTS);
        add(MeasureConstants.PLANNED_DISBURSEMENTS);
        add(MeasureConstants.ACTUAL_COMMITMENTS);
        add(MeasureConstants.ACTUAL_DISBURSEMENTS);
        add(MeasureConstants.PLEDGES_COMMITMENT_GAP);
        add(MeasureConstants.UNDISBURSED_BALANCE);
        add(MeasureConstants.PERCENTAGE_OF_DISBURSEMENT);
        add(MeasureConstants.PERCENTAGE_OF_TOTAL_COMMITMENTS);
    }};
    
    public void doIt(ServletContext ampContext) {
        logger.info("adding newly-created-measures entries to the FM and enabling them according to the AF configuration...");

        List<String> measuresToRestore = new ArrayList<>(MeasuresVisibility.allMeasures);
        
        long measuresModuleId = PersistenceManager.getLong(
                PersistenceManager.getRequestDBSession().createNativeQuery("select mod.id from amp_modules_visibility mod JOIN amp_modules_visibility rep "
                        + "ON lower(rep.name)='reporting' and rep.id = mod.parent WHERE lower(mod.name)='measures'")
                .uniqueResult());
        
        for(Object templateIdObj : PersistenceManager.getRequestDBSession().createNativeQuery("select id from amp_templates_visibility").list()) {
            long templateId = PersistenceManager.getLong(templateIdObj);
            MeasuresVisibility mv = new MeasuresVisibility();


            Set<String> enabledMeasuresByAF = new HashSet<>(mv.detectVisibleDataAF(templateId));
            
            
            String gsValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NEW_FIELDS_VISIBILITY);
            if ((gsValue == null || gsValue.equalsIgnoreCase("off"))) {
                for (String measureName : measuresToRestore) {
                    // control if the measure is new and remove from visibleMeasuresByAF (to be set as disabled)
                    // if the measure is an old one (prior 2.10) and was deleted meanwhile from FM,
                    // it should be enabled/disabled using AF visibility
                    if (!oldMeasuresToRestore.contains(measureName) && enabledMeasuresByAF.contains(measureName)) {
                        enabledMeasuresByAF.remove(measureName);
                    }
                }
            }
            
            createAndInsertMeasureFeatures(templateId, measuresModuleId, new HashSet<>(measuresToRestore), enabledMeasuresByAF);
        }
        
        PersistenceManager.getSession().flush();
        logger.info("\t...done!");
    }
    
    protected void createAndInsertMeasureFeatures(Long templateId, Long moduleId, Set<String> measuresToRestore, Set<String> enabledMeasuresByAF) {
        Session session = PersistenceManager.getSession();
        Set<String> measureNames = new TreeSet<>(measuresToRestore);
        AmpTemplatesVisibility template = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class, templateId);
        AmpModulesVisibility reportingModule = (AmpModulesVisibility) session.load(AmpModulesVisibility.class, moduleId);
        
        for(AmpObjectVisibility preexistentFeature : reportingModule.getOrCreateItems()) {
            measureNames.remove(preexistentFeature.getName());
        }
        
        for(String measureName : measureNames) {
            logger.error(String.format("Measure <%s> does not exist in the FM database, template <%s>, adding it: %s", measureName, template.getName(), enabledMeasuresByAF.contains(measureName) ? "enabled" : "disabled"));
            AmpFeaturesVisibility feature = new AmpFeaturesVisibility();
            feature.setParent(reportingModule);
            feature.setName(measureName);
            feature.setHasLevel(false);
                            
            reportingModule.getOrCreateItems().add(feature);
            logger.info("Saving feature to DB: "+feature.getName());
            session.save(feature);
            
            if (enabledMeasuresByAF.contains(measureName)) {
                template.getFeatures().add(feature);
            }
        }
        
        session.update(reportingModule);
        session.update(template);
        session.flush();
    }
}
