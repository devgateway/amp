/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpUniqueCollectionValidatorField;
import org.dgfoundation.amp.onepager.models.AmpThemeSearchModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;

/**
 * @author aartimon@dginternational.org
 * since Oct 21, 2010
 */
public class AmpProgramFormTableFeature extends AmpFormTableFeaturePanel <AmpActivityVersion,AmpActivityProgram>{

	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpProgramFormTableFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am, final String programSettingsString) throws Exception {
		super(id, am, fmName);
		final IModel<Set<AmpActivityProgram>> setModel=new PropertyModel<Set<AmpActivityProgram>>(am,"actPrograms");
		if (setModel.getObject() == null)
			setModel.setObject(new HashSet<AmpActivityProgram>());
		
		
		final IModel<AmpActivityProgramSettings> programSettings = PersistentObjectModel.getModel(ProgramUtil.getAmpActivityProgramSettings(programSettingsString));

		AbstractReadOnlyModel<List<AmpActivityProgram>> listModel = new AbstractReadOnlyModel<List<AmpActivityProgram>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<AmpActivityProgram> getObject() {
				Set<AmpActivityProgram> allProgs = setModel.getObject();
				Set<AmpActivityProgram> specificProgs = new HashSet<AmpActivityProgram>();
				
				if (programSettings.getObject() != null){
					Iterator<AmpActivityProgram> it = allProgs.iterator();
					while (it.hasNext()) {
						AmpActivityProgram prog = (AmpActivityProgram) it.next();
						if (prog != null && prog.getProgramSetting() != null && prog.getProgramSetting().getAmpProgramSettingsId() == programSettings.getObject().getAmpProgramSettingsId())
							specificProgs.add(prog);
					}
				}
				
				return new ArrayList<AmpActivityProgram>(specificProgs);
			}
		};

		
		final AmpPercentageCollectionValidatorField<AmpActivityProgram> percentageValidationField = new AmpPercentageCollectionValidatorField<AmpActivityProgram>(
				"programPercentageTotal", listModel, "programPercentageTotal") {
			@Override
			public Number getPercentage(AmpActivityProgram item) {
				return item.getProgramPercentage();
			}
		};
		
		add(percentageValidationField);
		
		
		final AmpUniqueCollectionValidatorField<AmpActivityProgram> uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<AmpActivityProgram>(
				"uniqueProgramsValidator", listModel, "uniqueProgramsValidator") {
			@Override
		 	public Object getIdentifier(AmpActivityProgram t) {
				return t.getProgram().getName();
		 	}	
		};
		
		add(uniqueCollectionValidationField);
		
		list = new ListView<AmpActivityProgram>("listProgs", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpActivityProgram> item) {
				final MarkupContainer listParent=this.getParent();
				
				PropertyModel<Double> percModel = new PropertyModel<Double>(
						item.getModel(), "programPercentage");
				AmpPercentageTextField percentageField = new AmpPercentageTextField("percent", percModel, "programPercentage",percentageValidationField);

				item.add(percentageField);
				
				item.add(new Label("name", item.getModelObject().getProgram().getName()));
				
				AmpDeleteLinkField delProgram = new AmpDeleteLinkField(
						"delProgram", "Delete Program") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(listParent);
						list.removeAll();
						percentageValidationField.reloadValidationField(target);
						uniqueCollectionValidationField.reloadValidationField(target);
					}
				};
				item.add(delProgram);
			}
		};
		list.setReuseItems(true);
		add(list);
		
		
		final AmpAutocompleteFieldPanel<AmpTheme> searchThemes=new AmpAutocompleteFieldPanel<AmpTheme>("search","Add Program",AmpThemeSearchModel.class) {
			@Override
			protected String getChoiceValue(AmpTheme choice) {
				//transientBoolean used internally to flag the default theme
				if (choice.isTransientBoolean())
					return "<b>" +TranslatorUtil.getTranslatedText("Default program") + ":</b> " + choice.getName();
				else
					return choice.getName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target,
					AmpTheme choice) {
				/*
				 * if the default program has been selected
				 * since it is a fake AmpTheme we need to load it from the db
				 */
				if (choice.isTransientBoolean()){
					AmpActivityProgramSettings aaps;
					try {
						aaps = ProgramUtil.getAmpActivityProgramSettings(programSettingsString);
					} catch (DgException e) {
						logger.error(e);
						return;
					}
					choice = aaps.getDefaultHierarchy();
				}
				
				AmpActivityProgram aap = new AmpActivityProgram();
				aap.setActivity(am.getObject());
				aap.setProgram(choice);
				aap.setProgramSetting(programSettings.getObject());
				
				if(list.size()>0)
					aap.setProgramPercentage(0f);
				else 
					aap.setProgramPercentage(100f); 
				
				setModel.getObject().add(aap);

				list.removeAll();
				target.addComponent(list.getParent());
				percentageValidationField.reloadValidationField(target);
				uniqueCollectionValidationField.reloadValidationField(target);
			}

			@Override
			public Integer getChoiceLevel(AmpTheme choice) {
				int i=0;
				AmpTheme c = choice;
				while(c.getParentThemeId()!=null) {
					i++;
					c = c.getParentThemeId();
				}
				return i;

			}
		};
		
		searchThemes.getModelParams().put(AmpThemeSearchModel.PARAM.PROGRAM_TYPE, programSettingsString); 		
		add(searchThemes);
	}

}
