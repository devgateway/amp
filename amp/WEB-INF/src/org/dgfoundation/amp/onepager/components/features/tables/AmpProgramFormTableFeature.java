/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerMessages;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.fields.*;
import org.dgfoundation.amp.onepager.events.DirectProgramMappingUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.models.AmpThemeSearchModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpDividePercentageField;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ProgramUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            final IModel<AmpActivityVersion> am, final String programSettingsString) throws Exception{
        this( id,  fmName,
                 am,  programSettingsString,false);
    }
    
    public AmpProgramFormTableFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am, final String programSettingsString,boolean required) throws Exception {
        super(id, am, fmName,false,required);
        final IModel<Set<AmpActivityProgram>> setModel=new PropertyModel<Set<AmpActivityProgram>>(am,"actPrograms");
        if (setModel.getObject() == null)
            setModel.setObject(new HashSet<AmpActivityProgram>());
        
        AmpActivityProgramSettings setting=ProgramUtil.getAmpActivityProgramSettings(programSettingsString);
        final IModel<AmpActivityProgramSettings> programSettings = (setting != null) ? PersistentObjectModel.getModel(setting):null;

        AbstractReadOnlyModel<List<AmpActivityProgram>> listModel = new AbstractReadOnlyModel<List<AmpActivityProgram>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<AmpActivityProgram> getObject() {
                Set<AmpActivityProgram> allProgs = setModel.getObject();
                Set<AmpActivityProgram> specificProgs = new HashSet<AmpActivityProgram>();
                
                if (programSettings!=null&&programSettings.getObject() != null && allProgs!=null){
                    Iterator<AmpActivityProgram> it = allProgs.iterator();
                    while (it.hasNext()) {
                        AmpActivityProgram prog = it.next();
                        if (prog != null && prog.getProgramSetting() != null && prog.getProgramSetting().getAmpProgramSettingsId().equals(programSettings.getObject().getAmpProgramSettingsId()))
                            specificProgs.add(prog);
                    }
                }
                
                return new ArrayList<AmpActivityProgram>(specificProgs);
            }
        };

        WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
        add(wmc);
        AjaxIndicatorAppender iValidator = new AjaxIndicatorAppender();
        wmc.add(iValidator);
        
        final AmpPercentageCollectionValidatorField<AmpActivityProgram> percentageValidationField = new AmpPercentageCollectionValidatorField<AmpActivityProgram>(
                "programPercentageTotal", listModel, "programPercentageTotal") {
            @Override
            public Number getPercentage(AmpActivityProgram item) {
                return item.getProgramPercentage();
            }
        };
        percentageValidationField.setIndicatorAppender(iValidator);
        add(percentageValidationField);
        
        final AmpMinSizeCollectionValidationField<AmpActivityProgram> minSizeCollectionValidationField = new AmpMinSizeCollectionValidationField<AmpActivityProgram>(
                "minSizeProgramValidator", listModel, "minSizeProgramValidator"){
            @Override
            protected void onConfigure() {
                super.onConfigure();
                
                //the required star should be visible, depending on whether the validator is active or not
                reqStar.setVisible(isVisible());
                        
            }       
        };
        minSizeCollectionValidationField.setIndicatorAppender(iValidator);
        add(minSizeCollectionValidationField);
        
        final AmpUniqueCollectionValidatorField<AmpActivityProgram> uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<AmpActivityProgram>(
                "uniqueProgramsValidator", listModel, "uniqueProgramsValidator") {
            @Override
            public Object getIdentifier(AmpActivityProgram t) {
                return t.getProgram().getName();
            }   
        };
        uniqueCollectionValidationField.setIndicatorAppender(iValidator);
        add(uniqueCollectionValidationField);
        final AmpMaxSizeCollectionValidationField <AmpActivityProgram> maxSizeCollectionValidationField = 
                new  AmpMaxSizeCollectionValidationField<AmpActivityProgram>("maxSizeProgramValidator",listModel, "max Size Program Validator");
        boolean programAllowsMultiple = (programSettings != null) && (!programSettings.getObject().isAllowMultiple());
        maxSizeCollectionValidationField.setVisibilityAllowed(programAllowsMultiple);
        maxSizeCollectionValidationField.setOutputMarkupPlaceholderTag(true);
        add(maxSizeCollectionValidationField);

        final AmpTreeCollectionValidatorField<AmpActivityProgram> treeCollectionValidatorField = new AmpTreeCollectionValidatorField<AmpActivityProgram>("treeValidator", listModel, "Tree Validator") {
            @Override
            public AmpAutoCompleteDisplayable getItem(AmpActivityProgram t) {
                return t.getProgram();
            }
        };
        treeCollectionValidatorField.setIndicatorAppender(iValidator);
        add(treeCollectionValidatorField);

        final AmpProgramMappingValidatorField<AmpActivityProgram> programMappingValidatorField =
                new AmpProgramMappingValidatorField<AmpActivityProgram>("programsMappingValidator", setModel,
                        listModel, programSettingsString, "Program Mapping Validator") {
                    @Override
                    public AmpAutoCompleteDisplayable getItem(AmpActivityProgram t) {
                        return t.getProgram();
                    }
                };
        programMappingValidatorField.setIndicatorAppender(iValidator);
        add(programMappingValidatorField);


        AmpLabelFieldPanel l = new AmpLabelFieldPanel("program", new Model(""),
                TranslatorUtil.getTranslatedText("Program"));
        this.getTableHeading().add(l);
        list = new ListView<AmpActivityProgram>("listProgs", listModel) {
            private static final long serialVersionUID = 7218457979728871528L;
            @Override
            protected void populateItem(final ListItem<AmpActivityProgram> item) {
                final MarkupContainer listParent=this.getParent();
                
                PropertyModel<Double> percModel = new PropertyModel<Double>(
                        item.getModel(), "programPercentage");
                AmpPercentageTextField percentageField = new AmpPercentageTextField("percent", percModel, "programPercentage",percentageValidationField);
                percentageField.getTextContainer().add(new AttributeModifier("style", "width: 40px;"));
                item.add(percentageField);
                
                item.add(new Label("name", item.getModelObject().getHierarchyNames(true)).setEscapeModelStrings(false));
                
                AmpDeleteLinkField delProgram = new AmpDeleteLinkField(
                        "delProgram", "Delete Program") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        if (ProgramUtil.isSourceMappedProgram(setting)) {
                            if (hasMappedPrograms(setModel.getObject(), item.getModelObject())) {
                                String message = TranslatorUtil.getTranslation(
                                        OnePagerMessages.HAS_MAPPED_PROGRAMS_ALERT_MSG);
                                target.appendJavaScript(OnePagerUtil.createJSAlert(message));
                                return;
                            }
                            send(AmpProgramFormTableFeature.this.getPage(),
                                    Broadcast.BREADTH, new DirectProgramMappingUpdateEvent(target));
                        }
                        setModel.getObject().remove(item.getModelObject());
                        target.add(listParent);
                        list.removeAll();
                        percentageValidationField.reloadValidationField(target);
                        uniqueCollectionValidationField.reloadValidationField(target);
                        minSizeCollectionValidationField.reloadValidationField(target);
                        treeCollectionValidatorField.reloadValidationField(target);
                        maxSizeCollectionValidationField.reloadValidationField(target);
                        programMappingValidatorField.reloadValidationField(target);
                    }
                };
                item.add(delProgram);
            }
        };
        list.setReuseItems(true);
        add(list);


        add(new AmpDividePercentageField<AmpActivityProgram>("dividePercentage", "Divide Percentage", "Divide Percentage", setModel, new Model<ListView<AmpActivityProgram>>(list),programSettingsString){
            private static final long serialVersionUID = 1L;

            @Override
            public void setPercentage(AmpActivityProgram loc, int val) {
                loc.setProgramPercentage((float) val);
            }
            @Override
            public int getPercentage(AmpActivityProgram loc) {
                return (int)((float)(loc.getProgramPercentage()));
            }
            @Override
            public boolean itemInCollection(AmpActivityProgram item) {
                if (item != null && item.getProgramSetting() != null && item.getProgramSetting().getAmpProgramSettingsId() == programSettings.getObject().getAmpProgramSettingsId())
                    return true;
                return false;
            }
            
        });
        
        final AmpAutocompleteFieldPanel<AmpTheme> searchThemes=new AmpAutocompleteFieldPanel<AmpTheme>("search","Add Program",
                programSettingsString, AmpThemeSearchModel.class) {
            /**
             * 
             */
            private static final long serialVersionUID = 9205254457879832345L;

            @Override
            protected String getChoiceValue(AmpTheme choice) {
                //transientBoolean used internally to flag the default theme
                if (choice.isTransientBoolean())
                    return BOLD_DELIMITER_START +TranslatorUtil.getTranslatedText("Default program") + BOLD_DELIMITER_STOP + DbUtil.filter(choice.getName());
                else
                    return DbUtil.filter(choice.getName());
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
                        logger.error(e.getMessage(), e);
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
                
                if (setModel.getObject() == null)
                    setModel.setObject(new HashSet<AmpActivityProgram>());
                setModel.getObject().add(aap);

                list.removeAll();
                target.add(list.getParent());
                percentageValidationField.reloadValidationField(target);
                uniqueCollectionValidationField.reloadValidationField(target);
                minSizeCollectionValidationField.reloadValidationField(target);
                treeCollectionValidatorField.reloadValidationField(target);
                maxSizeCollectionValidationField.reloadValidationField(target);
                programMappingValidatorField.reloadValidationField(target);

                if (ProgramUtil.isSourceMappedProgram(setting)) {
                    send(getPage(), Broadcast.BREADTH, new DirectProgramMappingUpdateEvent(target));
                }
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

        if (ProgramUtil.isDestinationMappedProgram(setting)) {
            searchThemes.add(UpdateEventBehavior.of(DirectProgramMappingUpdateEvent.class));
        }

        searchThemes.getModelParams().put(AmpThemeSearchModel.PARAM.PROGRAM_TYPE, programSettingsString);
        searchThemes.getModelParams().put(AmpThemeSearchModel.PARAM.ACTIVITY_PROGRAMS, setModel);

        add(searchThemes);
    }

    private boolean hasMappedPrograms(Set<AmpActivityProgram> activityPrograms, AmpActivityProgram ap) {
        AmpTheme sourceProgram = ap.getProgram();
        Map<AmpTheme, Set<AmpTheme>> programMappingMap = ProgramUtil.loadProgramMappings();
        if (programMappingMap.containsKey(sourceProgram)) {
            return activityPrograms.stream()
                    .filter(p -> programMappingMap.get(sourceProgram)
                    .contains(p.getProgram())).findAny().isPresent();
        }

        return false;
    }

}
