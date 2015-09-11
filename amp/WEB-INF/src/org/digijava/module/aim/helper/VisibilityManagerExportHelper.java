package org.digijava.module.aim.helper;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;

import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.dgfoundation.amp.visibility.feed.fm.schema.FeatureType;
import org.dgfoundation.amp.visibility.feed.fm.schema.FieldType;
import org.dgfoundation.amp.visibility.feed.fm.schema.ModuleType;
import org.dgfoundation.amp.visibility.feed.fm.schema.ObjectFactory;
import org.dgfoundation.amp.visibility.feed.fm.schema.TemplateType;
import org.dgfoundation.amp.visibility.feed.fm.schema.VisibilityTemplates;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
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
 * XML Example (sample dummy data):
 * <visibilityTemplates>
 * 	<template name="Donor">
 *  	<module name="Desktop" visible="true">
 *  		<module name="Reports" visible="true">
 *  			<feature name="Reports Wizard" visible="true">
 *  				field visible="false">Column Title</field>
 *  				<field visible="true">Cumulative Commitments</field>
 *  			</feature>
 *  		</module>
 * 		</module>
 * 	</template>
 * </visibilityTemplates>
 * 
 * @see http://docs.google.com/Doc?id=djf3gch_61d8rkn5dp&invite=dv359m5
 */

public class VisibilityManagerExportHelper {
	
	private static SimpleDateFormat myFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	/**
	 * 
	 * @return VisibilityTemplates
	 * 
	 */
	public VisibilityTemplates BuildVisibility() {
		ObjectFactory objFactory = new ObjectFactory();
		VisibilityTemplates vtemplate;
		try {
			vtemplate = objFactory.createVisibilityTemplates();
			TemplateType templates = null;
			//Get all Templates 
			for (Iterator Tempiter = FeaturesUtil.getAMPTemplatesVisibility().iterator(); Tempiter.hasNext();) {
				templates = objFactory.createTemplateType();
				AmpTemplatesVisibility amptemplates = (AmpTemplatesVisibility) Tempiter.next();
				templates.setName(amptemplates.getName());
				AmpTemplatesVisibility currenttemplate = FeaturesUtil.getTemplateById(amptemplates.getId());
				AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
				ampTreeVisibility.buildAmpTreeVisibilityMultiLevel(currenttemplate);
				//Modules
				Iterator moditer = ampTreeVisibility.getItems().values().iterator();
				while (moditer.hasNext()) {
					ModuleType modules = objFactory.createModuleType();
					AmpTreeVisibility ampmoduletree = (AmpTreeVisibility) moditer.next();
					AmpModulesVisibility ampmodule = (AmpModulesVisibility) ampmoduletree.getRoot();
					modules.setName(ampmodule.getName());
					modules.setVisible(ampmodule.isVisibleTemplateObj(currenttemplate));
					//Submodules for each module	
					Iterator submoduleiter = ampmodule.getSubmodules().iterator();
					while (submoduleiter.hasNext()) {
						ModuleType submodules = objFactory.createModuleType();
						AmpModulesVisibility ampsubmodule = (AmpModulesVisibility) submoduleiter.next();
						submodules.setName(ampsubmodule.getName());
						submodules.setVisible(ampsubmodule.isVisibleTemplateObj(currenttemplate));
						//Features for each Submodule
						if (ampsubmodule.getItems() != null) {
							Iterator featuresiter = ampsubmodule.getItems().iterator();
							while (featuresiter.hasNext()) {
								AmpFeaturesVisibility ampfeatures = (AmpFeaturesVisibility) featuresiter.next();
								FeatureType feature = objFactory.createFeatureType();
								feature.setName(ampfeatures.getName());
								feature.setVisible(ampfeatures.isVisibleTemplateObj(currenttemplate));
								//Fields for Submodules Features
								if (ampfeatures.getItems() != null) {
									Iterator fielditer = ampfeatures.getItems().iterator();
									while (fielditer.hasNext()) {
										AmpFieldsVisibility ampfield = (AmpFieldsVisibility) fielditer.next();
										FieldType field = objFactory.createFieldType();
										field.setValue(ampfield.getName());
										field.setVisible(ampfield.isVisibleTemplateObj(currenttemplate));
										feature.getField().add(field);
									}
								}
								submodules.getFeature().add(feature);
							}
						}
						modules.getModule().add(submodules);
					}
					if (ampmodule.getItems() != null) {
						//Modules Features 
						Iterator featuresiter = ampmodule.getItems().iterator();
						while (featuresiter.hasNext()) {
							AmpFeaturesVisibility ampfeatures = (AmpFeaturesVisibility) featuresiter.next();
							FeatureType feature = objFactory.createFeatureType();
							feature.setName(ampfeatures.getName());
							feature.setVisible(ampfeatures.isVisibleTemplateObj(currenttemplate));
							//Fields For Modules Features	
							if (ampfeatures.getItems() != null) {
								Iterator fielditer = ampfeatures.getItems().iterator();
								while (fielditer.hasNext()) {
									AmpFieldsVisibility ampfield = (AmpFieldsVisibility) fielditer.next();
									FieldType field = objFactory.createFieldType();
									field.setValue(ampfield.getName());
									field.setVisible(ampfield.isVisibleTemplateObj(currenttemplate));
									feature.getField().add(field);
								}
							}
							modules.getFeature().add(feature);
						}
					}
					templates.getModule().add(modules);
				}
				vtemplate.getTemplate().add(templates);
			}
			return vtemplate;
		} catch (JAXBException e) {
			e.printStackTrace();
		}catch(Exception ex){
			System.err.println(ex);
		}
		return null;
	}
	
	/**
	 * 
	 * @param vtemplate
	 */
	public void ImportFm(VisibilityTemplates vtemplate) {
		Session hbsession;
		try {
			hbsession = PersistenceManager.getRequestDBSession();
			for (Iterator vtemplateiter = vtemplate.getTemplate().iterator(); vtemplateiter.hasNext();) {
				TemplateType xmltemplate = (TemplateType) vtemplateiter.next();
				String templatename = getImportedTemplateName(xmltemplate);
				AmpTemplatesVisibility currenttemplate = new AmpTemplatesVisibility();//; FeaturesUtil.getTemplateById(FeaturesUtil.insertreturnTemplate(templatename));
				currenttemplate.setName(templatename);
				
				AmpTreeVisibility modeltree = new AmpTreeVisibility();
				modeltree.buildAmpTreeVisibilityMultiLevel(FeaturesUtil.getDefaultAmpTemplateVisibility());
				currenttemplate.setItems(new TreeSet());
				currenttemplate.setFeatures(new TreeSet());
				currenttemplate.setFields(new TreeSet());
				Iterator moditer = modeltree.getItems().values().iterator();
				while(moditer.hasNext()){
					AmpTreeVisibility amptree = (AmpTreeVisibility)moditer.next();
					AmpModulesVisibility ampmodule = (AmpModulesVisibility) amptree.getRoot();
					for (Iterator xmlmoditer = xmltemplate.getModule().iterator(); xmlmoditer.hasNext();) {
						ModuleType xmlmodule = (ModuleType) xmlmoditer.next();
						if (xmlmodule.getName().equalsIgnoreCase(ampmodule.getName())){
							if (xmlmodule.isVisible()) {
								currenttemplate.getItems().add(ampmodule);
							}
							
							buildFeaturesAndFields(currenttemplate, ampmodule, xmlmodule);
							
							for (Iterator submoduleiter = ampmodule.getSubmodules().iterator();submoduleiter.hasNext();) {
								AmpModulesVisibility submodule = (AmpModulesVisibility) submoduleiter.next();
								for (Iterator xmlsubmoditer = xmlmodule.getModule().iterator(); xmlsubmoditer.hasNext();) {
									ModuleType xmlsubmodule = (ModuleType) xmlsubmoditer.next();
									if(xmlsubmodule.getName().equalsIgnoreCase(submodule.getName())){
										if (xmlsubmodule.isVisible()) {
											currenttemplate.getItems().add(submodule);
										}
										
										buildFeaturesAndFields(currenttemplate, submodule, xmlsubmodule);
									}
								}	
							}
						}
					}
				}

				hbsession.save(currenttemplate);
				
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	private String getImportedTemplateName(TemplateType xmltemplate) {
		return xmltemplate.getName()+"-"+ myFormatter.format(new Date());
	}

	private void buildFeaturesAndFields(AmpTemplatesVisibility currentTemplate,
			AmpModulesVisibility ampmodule, ModuleType xmlmodule) {
		Iterator modfeaturesiter = ampmodule.getItems().iterator();
		while (modfeaturesiter.hasNext()) {
			AmpFeaturesVisibility feature = (AmpFeaturesVisibility) modfeaturesiter.next();
			for (Iterator xmlfeatureiter = xmlmodule.getFeature().iterator(); xmlfeatureiter.hasNext();) {
				FeatureType xmlfeature = (FeatureType) xmlfeatureiter.next();
				if(xmlfeature.getName().equalsIgnoreCase(feature.getName())){
					if (xmlfeature.isVisible()) {
						currentTemplate.getFeatures().add(feature);
					}
					
					Iterator fielditer = feature.getItems().iterator();
					while(fielditer.hasNext()){
						AmpFieldsVisibility field = (AmpFieldsVisibility) fielditer.next();
						for (Iterator xmlfieliter = xmlfeature.getField().iterator(); xmlfieliter.hasNext();) {
							FieldType xmlfield = (FieldType) xmlfieliter.next();
							if(xmlfield.getValue().equalsIgnoreCase(field.getName()) ){
								currentTemplate.getFields().add(field);
							}
						}
					}
				}
			}
		}
	}
	
}
