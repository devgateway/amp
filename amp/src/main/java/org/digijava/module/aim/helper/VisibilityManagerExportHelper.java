package org.digijava.module.aim.helper;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.dgfoundation.amp.visibility.feed.fm.schema.*;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.xml.bind.JAXBException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;
/**
 * @author Diego Dimunzio
 * 
 *  Mihai Postelnicu Comments:
 * Feature Manager Visibility Export Schema 
 * The goal is to export the visibility settings in AMP's Feature Manager to an XML file that can be imported on another server.  
 * This is a relative easy schema as the visibility xml would only hold the name of the field/feature/module and its visibility status. 
 * however we do have several templates and even if we can make a single entity
 * that can represent the visibility status of all module/feature/field , for the sake of clarity and tree structure we will create three separate entities
 * Each object can be uniquely identified by its name inside its type only. 
 * Example: You can have a module called "Reports" and a feature called "Reports" but you NEVER have two features called "Reports". Therefore the name of the objects will be used as key along with the type.
 * 
 * For Activity Form features we had to import/export also the submodules tree
 * XML Example (sample dummy data):
 * <visibilityTemplates>
 *  <template name="Donor">
 *      <module name="Desktop" visible="true">
 *          <module name="Reports" visible="true">
 *              <feature name="Reports Wizard" visible="true">
 *                  <field visible="false">Column Title</field>
 *                  <field visible="true">Cumulative Commitments</field>
 *              </feature>
 *          </module>
 *      </module>
 *  </template>
 * </visibilityTemplates>
 * 
 * @see http://docs.google.com/Doc?id=djf3gch_61d8rkn5dp&invite=dv359m5
 */

public class VisibilityManagerExportHelper {
    
    private static SimpleDateFormat myFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static Logger logger = Logger.getLogger(VisibilityManagerExportHelper.class);
    
    /**
     * Build the visibility templates in order to be exported in XML
     * 
     * @return VisibilityTemplates
     * 
     */
    public VisibilityTemplates buildVisibilityTemplate() {
        
        try {
            ObjectFactory objFactory = new ObjectFactory();
            VisibilityTemplates visibilityTemplates = objFactory.createVisibilityTemplates();
            
            //get all Templates 
            for (AmpTemplatesVisibility ampTemplate : (Iterable<AmpTemplatesVisibility>) FeaturesUtil.getAMPTemplatesVisibility()) {
                AmpTemplatesVisibility currentTemplate = FeaturesUtil.getTemplateById(ampTemplate.getId());

                AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
                ampTreeVisibility.buildAmpTreeVisibilityMultiLevel(currentTemplate);

                TemplateType templateNode = objFactory.createTemplateType();
                templateNode.setName(currentTemplate.getName());

                // add modules to the template
                for (AmpTreeVisibility moduleTree : ampTreeVisibility.getItems().values()) {
                    AmpModulesVisibility ampmodule = (AmpModulesVisibility) moduleTree.getRoot();
                    ModuleType moduleNode = buildModuleNode(ampmodule, objFactory, currentTemplate);

                    templateNode.getModule().add(moduleNode);
                }

                visibilityTemplates.getTemplate().add(templateNode);
            }
            
            return visibilityTemplates;
        } catch (Exception e) {
            logger.error("Error during the build of the template tree " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Build the module elements <field> 
     * @param AmpModulesVisibility module
     * @param ObjectFactory objFactory
     * @param AmpTemplatesVisibility ampTemplate
     */
    private ModuleType buildModuleNode(AmpModulesVisibility module, ObjectFactory objFactory, AmpTemplatesVisibility ampTemplate) throws JAXBException {
        
        ModuleType moduleNode = objFactory.createModuleType();
        moduleNode.setName(module.getName());
        moduleNode.setVisible(module.isVisibleTemplateObj(ampTemplate));
        
        //Recursively import all the submodules 
        for (AmpModulesVisibility ampSubModule : module.getSubmodules()) {
            ModuleType subModuleNode = buildModuleNode(ampSubModule, objFactory, ampTemplate);
            
            moduleNode.getModule().add(subModuleNode);
        }
        
        addFeaturesToModuleNode(moduleNode, module, objFactory, ampTemplate);
        
        return moduleNode;
    }
    
    /**
     * Add the feature elements into the module node <field> 
     * @param ModuleType moduleNode
     * @param AmpModulesVisibility module
     * @param ObjectFactory objFactory
     * @param AmpTemplatesVisibility ampTemplate
     */
    private void addFeaturesToModuleNode(ModuleType moduleNode, AmpModulesVisibility module, ObjectFactory objFactory, AmpTemplatesVisibility ampTemplate) throws JAXBException {
        
        for (AmpObjectVisibility ampFeature : module.getItems()) {
            FeatureType featureNode = objFactory.createFeatureType();
            featureNode.setName(ampFeature.getName());
            featureNode.setVisible(ampFeature.isVisibleTemplateObj(ampTemplate));
            
            addFieldsToFeatureNode(featureNode, ampFeature, objFactory, ampTemplate);
            
            moduleNode.getFeature().add(featureNode);
        }
    }
    
    /**
     * Add the fields elements into the feature node <field> 
     * @param FeatureType featureNode
     * @param AmpObjectVisibility ampFeature
     * @param ObjectFactory objFactory
     * @param AmpTemplatesVisibility ampTemplate
     */
    private void addFieldsToFeatureNode(FeatureType featureNode, AmpObjectVisibility ampFeature, ObjectFactory objFactory, AmpTemplatesVisibility ampTemplate) throws JAXBException {
        
        for (AmpObjectVisibility ampField : ampFeature.getItems()) {
            FieldType field = objFactory.createFieldType();
            field.setValue(ampField.getName());
            field.setVisible(ampField.isVisibleTemplateObj(ampTemplate));
            
            featureNode.getField().add(field);
        }
    }
    
    /** 
     * Creates a new template with all elements retrieved from the XML file
     * 
     * @param VisibilityTemplates vtemplate
     */
    public void importXmlVisbilityTemplate(VisibilityTemplates vtemplate) {
        
        Session hbsession;
        try {
            hbsession = PersistenceManager.getRequestDBSession();
            for (TemplateType xmlTemplate : (Iterable<TemplateType>) vtemplate.getTemplate()) {
                AmpTemplatesVisibility visibilityTemplate = new AmpTemplatesVisibility();
                String templateName = getImportedTemplateName(xmlTemplate);
                visibilityTemplate.setName(templateName);

                AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
                ampTreeVisibility.buildAmpTreeVisibilityMultiLevel(FeaturesUtil.getDefaultAmpTemplateVisibility());

                visibilityTemplate.setItems(new TreeSet<AmpObjectVisibility>());
                visibilityTemplate.setFeatures(new TreeSet<AmpFeaturesVisibility>());
                visibilityTemplate.setFields(new TreeSet<AmpFieldsVisibility>());

                for (AmpTreeVisibility ampVisibilityTree : ampTreeVisibility.getItems().values()) {
                    AmpModulesVisibility module = (AmpModulesVisibility) ampVisibilityTree.getRoot();

                    for (ModuleType moduleType : (Iterable<ModuleType>) xmlTemplate.getModule()) {
                        ModuleType xmlModule = moduleType;
                        importXmlModule(visibilityTemplate, module, xmlModule);
                    }
                }

                hbsession.save(visibilityTemplate);

            }
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Import the module into the template based on the xml element <module> 
     * @param AmpTemplatesVisibility template
     * @param AmpModulesVisibility module
     * @param ModuleType xmlModule
     */
    private void importXmlModule(AmpTemplatesVisibility template, AmpModulesVisibility module, ModuleType xmlModule) {
        
        if (module.getName().equalsIgnoreCase(xmlModule.getName())) {
            if (xmlModule.isVisible()) {
                template.getItems().add(module);
            }
            
            importXmlSubModules(template, module, xmlModule);
            importXmlFeatures(template, module, xmlModule);
        }
    }
    
    /**
     * Import the modules elements into the template based on the subelements <module> 
     * @param AmpTemplatesVisibility template
     * @param AmpModulesVisibility module
     * @param ModuleType xmlModule
     */
    private void importXmlSubModules(AmpTemplatesVisibility template, AmpModulesVisibility module, ModuleType xmlModule) {
        
        for (AmpModulesVisibility ampSubModule : module.getSubmodules()) {
            Iterator<ModuleType> xmlSubmoduleIt = xmlModule.getModule().iterator(); 
            while (xmlSubmoduleIt.hasNext()) {
                ModuleType xmlSubModule = xmlSubmoduleIt.next();
                importXmlModule(template, ampSubModule, xmlSubModule);
            }
        }
    }
    
    /**
     * Import the features elements into the template based on the element <feature> 
     * @param AmpTemplatesVisibility template
     * @param AmpModulesVisibility module
     * @param ModuleType xmlModule
     */
    private void importXmlFeatures(AmpTemplatesVisibility template, AmpModulesVisibility module, ModuleType xmlModule) {
        
        Iterator<AmpObjectVisibility> ampFeatureIt = module.getItems().iterator();
        while (ampFeatureIt.hasNext()) {
            AmpFeaturesVisibility ampFeature = (AmpFeaturesVisibility) ampFeatureIt.next();

            for (FeatureType featureType : (Iterable<FeatureType>) xmlModule.getFeature()) {
                FeatureType xmlFeature = featureType;
                if (ampFeature.getName().equals(xmlFeature.getName())) {
                    if (xmlFeature.isVisible()) {
                        template.getFeatures().add(ampFeature);
                    }

                    importXmlFields(template, ampFeature, xmlFeature);
                }
            }
        }
    }
    
    /**
     * Import the fields elements into the template based on the element <field> 
     * @param AmpTemplatesVisibility template
     * @param AmpFeaturesVisibility ampFeature
     * @param FeatureType xmlFeature
     */
    private void importXmlFields(AmpTemplatesVisibility template, AmpFeaturesVisibility ampFeature, FeatureType xmlFeature) {

        for (AmpObjectVisibility ampObjectVisibility : ampFeature.getItems()) {
            AmpFieldsVisibility ampField = (AmpFieldsVisibility) ampObjectVisibility;

            for (FieldType fieldType : (Iterable<FieldType>) xmlFeature.getField()) {
                FieldType xmlField = fieldType;

                if (ampField.getName().equals(xmlField.getValue()) && xmlField.isVisible()) {
                    template.getFields().add(ampField);
                }
            }
        }
    }
    
    
    private String getImportedTemplateName(TemplateType xmlTemplate) {
        return xmlTemplate.getName() + "-" + myFormatter.format(new Date());
    }
}
