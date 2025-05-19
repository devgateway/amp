/**
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.validators.AmpProgramMappingCollectionValidator;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.toSet;

/**
 * @author Viorel Chihai
 *
 */
public abstract class AmpProgramMappingValidatorField<T extends AmpActivityProgram>
        extends AmpCollectionValidatorField<T, String> {

    private final IModel<Set<AmpActivityProgram>> activityProgramsModel;
    private final String programType;

    /**
     * @param id
     * @param collectionModel
     * @param fmName
     */
    public AmpProgramMappingValidatorField(String id, IModel<Set<AmpActivityProgram>> activityProgramsModel,
                                           IModel<? extends Collection<T>> collectionModel, String programType,
                                           String fmName) {
        super(id, collectionModel, fmName, new AmpProgramMappingCollectionValidator());
        this.activityProgramsModel = activityProgramsModel;
        this.programType = programType;
        hiddenContainer.setType(String.class);
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField
     * #getHiddenContainerModel(org.apache.wicket.model.IModel)
     */
    @Override
    public IModel getHiddenContainerModel(
            final IModel<? extends Collection<T>> collectionModel) {
        Model<String> model = new Model<String>() {
            @Override
            public String getObject() {
                Set<AmpTheme> quickItems = new TreeSet<>();

                for (T t : collectionModel.getObject()) {
                    quickItems.add((AmpTheme) getItem(t));
                }

                Set<String> ret = new TreeSet<>();

                String destProgram = FeaturesUtil.
                        getGlobalSettingValue(GlobalSettingsConstants.MAPPING_DESTINATION_PROGRAM);

                if (destProgram != null && getCurrentRootTheme() != null
                        && getCurrentRootTheme().getAmpThemeId().equals(Long.valueOf(destProgram))) {
                    Set<AmpTheme> mappedDirectPrograms = ProgramUtil.loadProgramMappings()
                            .values().stream().flatMap(Collection::stream).collect(toSet());
                    for (T t : collectionModel.getObject()) {
                        AmpTheme program = (AmpTheme) getItem(t);
                        if (!mappedDirectPrograms.contains(program)) {
                            ret.add("" + getItem(t));
                        }
                    }
                }

                if (ret.size() > 0) {
                    return ret.toString();
                }

                return "";
            }
        };
        return model;
    }

    public abstract AmpAutoCompleteDisplayable getItem(T t);

    private AmpTheme getCurrentRootTheme() {
        try {
            AmpActivityProgramSettings aaps = ProgramUtil.getAmpActivityProgramSettings(programType);
            return aaps.getDefaultHierarchy();
        } catch (DgException e) {
            throw new RuntimeException(e);
        }
    }

}
