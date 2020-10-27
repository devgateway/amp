/**
 * 
 */
package org.dgfoundation.amp.ar.dyn;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.Session;

/**
 * @author Alex Gartner
 *
 */
public class ColumnSavingEngine {
    
    private static Logger logger = Logger.getLogger(ColumnSavingEngine.class);
    
    AmpColumns newColumn;
    
    /**
     * name of the feature under which the field corresponding to this column will appear
     */
    String featureName;
    ServletContext sCtx;
    
    public ColumnSavingEngine(AmpColumns newColumn, String featureName,
            ServletContext sCtx) {
        super();
        this.newColumn = newColumn;
        this.featureName = featureName;
        this.sCtx = sCtx;
    }
    
    private boolean existFieldinDB(AmpTreeVisibility atv)
    {
        AmpFieldsVisibility fieldByNameFromRoot = atv.getFieldByNameFromRoot( newColumn.getColumnName() );
        if(fieldByNameFromRoot==null) return false;
        return true;
    }
    private boolean isFeatureTheParent(AmpTreeVisibility atv)
    {
        AmpTreeVisibility featureByNameFromRoot = atv.getFeatureTreeByNameFromRoot( this.featureName );
        if(featureByNameFromRoot!=null)
            if(featureByNameFromRoot.getItems()!=null)
                {
                    if(featureByNameFromRoot.getItems().containsKey( newColumn.getColumnName() )) return true;
                }
        return false;
    }
    
    public boolean saveField(HttpSession session) {
        boolean ret = false;
        try{
            AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility)FeaturesUtil.getAmpTreeVisibility(this.sCtx, session); 


            if(ampTreeVisibility!=null)
                if(!existFieldinDB(ampTreeVisibility)){
                    synchronized (this) {
                        if(FeaturesUtil.getFieldVisibility( newColumn.getColumnName() )==null)
                        {
                            AmpFeaturesVisibility featureByNameFromRoot = ampTreeVisibility.getFeatureByNameFromRoot( featureName );
                            Long id=null;
                            if(featureByNameFromRoot!=null)
                            {
                                id = featureByNameFromRoot.getId();
                                try {//extra verification...
                                    if(FeaturesUtil.getFieldVisibility(newColumn.getColumnName())!=null){
                                        FeaturesUtil.updateFieldWithFeatureVisibility(ampTreeVisibility.getFeatureByNameFromRoot(featureName).getId(),newColumn.getColumnName());
                                    } else {
                                        FeaturesUtil.insertFieldWithFeatureVisibility(ampTreeVisibility.getRoot().getId(),id, newColumn.getColumnName(), "no", true);
                                    }

                                    AmpTemplatesVisibility  currentTemplate = (AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
                                    ampTreeVisibility. buildAmpTreeVisibility(currentTemplate);
                                    FeaturesUtil.setAmpTreeVisibility(this.sCtx, session, ampTreeVisibility);
                                    ret = true;
                                } catch (DgException ex) {
                                    ex.printStackTrace();
                                    ret = false;
                                }
                            } else {
                                logger.info("Field: "+newColumn.getColumnName() + " has the parent: "+featureName+ " which doesn't exist in DB");
                                ret     = false;
                            }
                        }
                    }
                }
            ampTreeVisibility=(AmpTreeVisibility) FeaturesUtil.getAmpTreeVisibility(this.sCtx, session);
            if(ampTreeVisibility!=null)
                if(!isFeatureTheParent(ampTreeVisibility)){
                    FeaturesUtil.updateFieldWithFeatureVisibility(ampTreeVisibility.getFeatureByNameFromRoot(featureName).getId(),newColumn.getColumnName());
                    AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
                    ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
                    FeaturesUtil.setAmpTreeVisibility(this.sCtx, session, ampTreeVisibility);

                } 
            //             }

        }catch (Exception e) {
            logger.error("error in field visibility. pls check the field: "+newColumn.getColumnName() +" or its parent: "+featureName);
            logger.error(e.getMessage(), e);
            ret     = false;
        }
        return ret;
    }
    
    public void saveColumn () {
        Session dbSession = null;
        try {
            dbSession = PersistenceManager.getSession();
//beginTransaction();
            
            dbSession.save( this.newColumn );

//session.flush();
            //tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void startSavingProcess() {
        boolean fieldSaved  = (this.featureName == null) || this.saveField(null);
        
        if (fieldSaved ) 
            this.saveColumn();
        else
            logger.error("There was an error saving the field for column " + newColumn.getColumnName() + ". So the AmpColumns object was not saved either" );
    }

}
