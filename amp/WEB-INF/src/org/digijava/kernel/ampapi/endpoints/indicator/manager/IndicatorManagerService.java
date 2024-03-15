package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xpath.operations.Bool;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorUtils;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.ModuleUtils;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE;
import static org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE;


/**
 * Indicator Manager Service
 *
 * @author vchihai
 * @author Timothy Mugo
 */
public class IndicatorManagerService {

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static final String FILTER_BY_PROGRAM = "Filter By Program";

    public static final String FILTER_BY_INDICATOR_LOCATION = "Filter By Indicator Location";
    public static final String FILTER_BY_SECTOR = "Filter By Sector";

    public static String INDICATOR_CATEGORY_KEY = "core_indicator_type";

    protected static final Logger logger = Logger.getLogger(IndicatorManagerService.class);

    private TranslationUtil contentTranslator = new TranslationUtil();

    public List<MEIndicatorDTO> getMEIndicators() {
        Session session = PersistenceManager.getSession();

        List<AmpIndicator> indicators = session.createCriteria(AmpIndicator.class)
                .addOrder(Order.asc("id"))
                .list();

        return indicators.stream()
                .map(MEIndicatorDTO::new)
                .collect(Collectors.toList());
    }


    public MEIndicatorDTO getMEIndicatorById(final Long indicatorId) {
        Session session = PersistenceManager.getSession();

        AmpIndicator indicator = (AmpIndicator) session.get(AmpIndicator.class, indicatorId);
        if (indicator != null) {
            return new MEIndicatorDTO(indicator);
        }

        throw new ApiRuntimeException(BAD_REQUEST,
                ApiError.toError("Indicator with id " + indicatorId + " not found"));
    }

    public MEIndicatorDTO createMEIndicator(final MEIndicatorDTO indicatorRequest) {
        Session session = PersistenceManager.getSession();
        AmpIndicator indicator = new AmpIndicator();
        String name = indicatorRequest.getName();
        validateYear(indicatorRequest);
        //TODO see why the following line was commented out
        //validateNameProgramSectorUnique(name, indicatorRequest, session);

        validateIndicatorName(indicatorRequest.getName(), session);
        validateIndicatorCode(indicatorRequest.getCode(), session);

        indicator.setName(indicatorRequest.getName());
        indicator.setDescription(indicatorRequest.getDescription());

        indicator.setCode(indicatorRequest.getCode());
        indicator.setType(indicatorRequest.isAscending() ? "A" : "D");
        indicator.setCreationDate(indicatorRequest.getCreationDate());

        Set<AmpIndicatorGlobalValue> indicatorValues = new HashSet<>();

        if (indicatorRequest.getProgramId() != null) {
            indicator.setProgram(ProgramUtil.getTheme(indicatorRequest.getProgramId()));
            validateProgramSettingsAndGlobalValues(indicatorRequest, indicator);
        }

        if (indicatorRequest.getBaseValue() != null) {
            if (indicatorRequest.getBaseValue().getOriginalValueDate() != null || indicatorRequest.getBaseValue().getRevisedValueDate() != null) {
                String baseStartYear = String.valueOf(DateConversion.getYear(DateConversion.convertDateToString(indicatorRequest.getBaseValue().getOriginalValueDate())));
                String baseEndYear = String.valueOf(DateConversion.getYear(DateConversion.convertDateToString(indicatorRequest.getBaseValue().getRevisedValueDate())));

                validateYearRange( baseStartYear, baseEndYear ,indicatorRequest.getBaseValue(), "Base");
            }

            AmpIndicatorGlobalValue validatedBaseValues = validateBaseValues(indicatorRequest);
            indicatorValues.add(validatedBaseValues);
        }

        if (indicatorRequest.getTargetValue() != null) {
            if (indicatorRequest.getTargetValue().getOriginalValueDate() != null || indicatorRequest.getTargetValue().getRevisedValueDate() != null) {
                String targetStartYear = String.valueOf(DateConversion.getYear(DateConversion.convertDateToString(indicatorRequest.getTargetValue().getOriginalValueDate())));
                String targetEndYear = String.valueOf(DateConversion.getYear(DateConversion.convertDateToString(indicatorRequest.getTargetValue().getRevisedValueDate())));

                validateYearRange(targetStartYear, targetEndYear,indicatorRequest.getTargetValue(), "Target");
            }
            AmpIndicatorGlobalValue validatedTargetValues = validateTargetValues(indicatorRequest);
            indicatorValues.add(validatedTargetValues);
        }

        indicatorValues.stream().forEach(value -> value.setIndicator(indicator));
        indicator.setIndicatorValues(indicatorValues);

        Set<AmpSector> sectors = indicatorRequest.getSectorIds().stream()
                .map(id -> (AmpSector) session.get(AmpSector.class, id))
                .collect(Collectors.toSet());
        indicator.setSectors(sectors);

        if (indicatorRequest.getIndicatorsCategory() != null) {
            AmpCategoryValue categoryValue = (AmpCategoryValue) session.get(AmpCategoryValue.class, indicatorRequest.getIndicatorsCategory());
            indicator.setIndicatorsCategory(categoryValue);
        }



        session.save(indicator);

        return new MEIndicatorDTO(indicator);
    }

    private void validateYear(MEIndicatorDTO value) {
        String startYear = FeaturesUtil.getGlobalSettingValue(START_YEAR_DEFAULT_VALUE);
        String endYear = FeaturesUtil.getGlobalSettingValue(END_YEAR_DEFAULT_VALUE);
        validateYearRange(startYear, endYear, value.getBaseValue(), "Base");
        validateYearRange(startYear, endYear, value.getTargetValue(), "Target");

    }

    private void validateYearRange(String startYear, String endYear, AmpIndicatorGlobalValue value, String error){
        String startInString = "01/01/" + startYear;
        DateTime dateTime = DateTime.parse(startInString, formatter);

        if (value.getOriginalValueDate() != null) {
            if (dateTime.isAfter(value.getOriginalValueDate().getTime())) {
                throw new ApiRuntimeException(BAD_REQUEST,
                        ApiError.toError(error + " Original value date "
                                + simpleDateFormat.format(value.getOriginalValueDate())
                                + " should be greater than " + startInString));
            }

        }

        String endInString = "01/01/" + endYear;
        dateTime = DateTime.parse(endInString, formatter);

        if(value.getRevisedValueDate() != null) {
            if (dateTime.isAfter(value.getRevisedValueDate().getTime())) {
                throw new ApiRuntimeException(BAD_REQUEST,
                        ApiError.toError(error + "Revised value date "
                                + simpleDateFormat.format(value.getRevisedValueDate())
                                + " should be greater than " + endInString));
            }
        }

    }


    private void validateNameProgramSectorUnique(String name, MEIndicatorDTO indicator, Session session) {
        long indicatorSector = -1;
        long indicatorProgram = -1;
        if (!indicator.getSectorIds().isEmpty()) {
            indicatorSector = session.createCriteria(AmpIndicator.class)
                    .add(Restrictions.eq("name", name))
                    .createCriteria("sectors")
                    .add(Restrictions.in("ampSectorId", indicator.getSectorIds()))
                    .list().size();
        }


        if (indicator.getProgramId() != null) {
            indicatorProgram = session.createCriteria(AmpIndicator.class)
                    .add(Restrictions.eq("name", name))
                    .createCriteria("programs")
                    .add(Restrictions.eq("ampThemeId", indicator.getProgramId()))
                    .list().size();
        }

        if (indicatorSector > 0 && indicatorProgram > 0) {
            throw new ApiRuntimeException(BAD_REQUEST,
                    ApiError.toError("Indicator with name " + indicator.getName() + " sectors "
                            + StringUtils.join(indicator.getSectorIds(), ",")
                            + " and program " + indicator.getProgramId()  + " already exist"));
        }

        if (indicatorSector > 0  && indicatorProgram == -1) {
            throw new ApiRuntimeException(BAD_REQUEST,
                    ApiError.toError("Indicator with name " + indicator.getName() + " and sectors "
                            + StringUtils.join(indicator.getSectorIds(), ",") + " already exist"));
        } else if (indicatorSector == -1 && indicatorProgram > 0) {
            throw new ApiRuntimeException(BAD_REQUEST,
                    ApiError.toError("Indicator with name " + indicator.getName() + " and program "
                            + indicator.getProgramId() + " already exist"));
        }

    }

    public void deleteMEIndicator(final Long indicatorId) {
        Session session = PersistenceManager.getSession();

        AmpIndicator indicator = (AmpIndicator) session.get(AmpIndicator.class, indicatorId);
        if (indicator != null) {
            if (indicator.getValuesActivity().isEmpty()) {
                session.delete(indicator);
            } else {
                throw new ApiRuntimeException(BAD_REQUEST,
                        ApiError.toError("Indicator with id " + indicatorId + " cannot be deleted because "
                                + "it is used by activities"));
            }
        } else {
            throw new ApiRuntimeException(BAD_REQUEST,
                    ApiError.toError("Indicator with id " + indicatorId + " not found"));
        }
    }

    public List<SectorDTO> getSectors() {
        return SectorUtil.getAllParentSectors(true).stream()
                .map(SectorDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProgramDTO> getPrograms() {
        try {

            return ProgramUtil.getAllThemes(true, true).stream()
                    .map(ProgramDTO::new)
                    .collect(Collectors.toList());
        } catch (DgException e) {
            throw new ApiRuntimeException(ApiError.toError(e.getMessage()));
        }
    }

    public List<ProgramSchemeDTO> getProgramScheme() {
        return ProgramUtil.getAmpActivityProgramSettingsList(false).stream()
                .filter(program -> program.getDefaultHierarchy() != null)
                .map(ProgramSchemeDTO::new)
                .collect(Collectors.toList());
    }

    public MEIndicatorDTO updateMEIndicator(final Long indicatorId, final MEIndicatorDTO indRequest) {
        Session session = PersistenceManager.getSession();
        AmpIndicator indicator = (AmpIndicator) session.get(AmpIndicator.class, indicatorId);
        if (indicator != null) {
            if (!indicator.getName().equals(indRequest.getName())) {
                validateIndicatorName(indRequest.getName(), session);
            }

            if (!indicator.getCode().equals(indRequest.getCode())) {
                validateIndicatorCode(indRequest.getCode(), session);
            }

            indicator.setName(indRequest.getName());
            indicator.setDescription(indRequest.getDescription());

            indicator.setCode(indRequest.getCode());
            indicator.setType(indRequest.isAscending() ? "A" : "D");
            indicator.setCreationDate(indRequest.getCreationDate());

            if (indRequest.getProgramId() != null) {
                indicator.setProgram(ProgramUtil.getTheme(indRequest.getProgramId()));
                validateProgramSettingsAndGlobalValues(indRequest, indicator);
            }

            Set <AmpIndicatorGlobalValue> updatedValues = new HashSet<>();

            if (indRequest.getBaseValue() != null) {
                AmpIndicatorGlobalValue validatedBaseValues = validateBaseValues(indRequest);
                updatedValues.add(validatedBaseValues);
                indicator.getIndicatorValues().add(validatedBaseValues);
                indicator.getBaseValue().setIndicator(indicator);
            }

            if (indRequest.getTargetValue() != null) {
                AmpIndicatorGlobalValue validatedTargetValues = validateTargetValues(indRequest);
                updatedValues.add(validatedTargetValues);
                indicator.getIndicatorValues().add(validatedTargetValues);
                indicator.getTargetValue().setIndicator(indicator);
            }

            indicator.getIndicatorValues().clear();
            updatedValues.forEach(value -> value.setIndicator(indicator));
            indicator.getIndicatorValues().addAll(updatedValues);

            Set<AmpSector> sectors = indRequest.getSectorIds().stream()
                    .map(id -> (AmpSector) session.get(AmpSector.class, id))
                    .collect(Collectors.toSet());
            indicator.getSectors().clear();
            indicator.getSectors().addAll(sectors);

            if (indicator.getProgram() != null && indRequest.getProgramId() == null) {
                indicator.getProgram().getIndicators().remove(indicator);
                indicator.setProgram(null);
            }

            if (indRequest.getIndicatorsCategory() != null) {
                AmpCategoryValue categoryValue = (AmpCategoryValue) session.get(AmpCategoryValue.class, indRequest.getIndicatorsCategory());
                indicator.setIndicatorsCategory(categoryValue);
            }

            session.update(indicator);
            return new MEIndicatorDTO(indicator);
        }

        throw new ApiRuntimeException(BAD_REQUEST,
                ApiError.toError("Indicator with id " + indicatorId + " not found"));
    }

    public void validateProgramSettingsAndGlobalValues(final MEIndicatorDTO indicatorRequest,
                                                       final AmpIndicator indicator) {
        if (indicatorRequest.getProgramId() != null) {
            indicator.setProgram(ProgramUtil.getTheme(indicatorRequest.getProgramId()));

            if (indicator.getProgram() != null) {
                AmpActivityProgramSettings indSettings = ProgramUtil.getProgramSettingFromTheme(indicator.getProgram());

                if (indSettings != null) {
                    Date baseOriginalValueDate = null;
                    Date targetOriginalValueDate = null;

                    if (indicatorRequest.getBaseValue() != null) {
                        if (indicatorRequest.getBaseValue().getOriginalValueDate() != null) {
                            baseOriginalValueDate = indicatorRequest.getBaseValue().getOriginalValueDate();
                        }
                    }

                    if (indicatorRequest.getTargetValue() != null) {
                        if (indicatorRequest.getTargetValue().getOriginalValueDate() != null) {
                            targetOriginalValueDate = indicatorRequest.getTargetValue().getOriginalValueDate();
                        }
                    }

                    if (indSettings.getStartDate() != null) {
                        if (baseOriginalValueDate != null) {
                            if (!baseOriginalValueDate.equals(indSettings.getStartDate())) {
                                throw new ApiRuntimeException(BAD_REQUEST,
                                        ApiError.toError("Base original value date must be equal "
                                                + "program start date"));
                            }
                        } else {
                            throw new ApiRuntimeException(BAD_REQUEST,
                                    ApiError.toError("Base original value date is required since "
                                            + "program start date is set"));
                        }
                    }

                    if (indSettings.getEndDate() != null) {
                        if (targetOriginalValueDate != null) {
                            if (!targetOriginalValueDate.equals(indSettings.getEndDate())) {
                                throw new ApiRuntimeException(BAD_REQUEST,
                                        ApiError.toError("Target value date must be equal program end date"));
                            }
                        } else {
                            throw new ApiRuntimeException(BAD_REQUEST,
                                    ApiError.toError("Target value date is required since program end date is set"));
                        }
                    }

                }
            }
        }
    }

    public AmpIndicatorGlobalValue validateBaseValues(final MEIndicatorDTO indicatorRequest) {
        AmpIndicatorGlobalValue baseValues = indicatorRequest.getBaseValue();

        if (indicatorRequest.getBaseValue() != null) {
            Double originalBaseValue = indicatorRequest.getBaseValue().getOriginalValue();
            Double revisedBaseValue = indicatorRequest.getBaseValue().getRevisedValue();
            Date baseOriginalValueDate = indicatorRequest.getBaseValue().getOriginalValueDate();
            Date baseRevisedValueDate = indicatorRequest.getBaseValue().getRevisedValueDate();

            if (originalBaseValue != null && revisedBaseValue != null) {
                if (originalBaseValue > revisedBaseValue) {
                    throw new ApiRuntimeException(BAD_REQUEST,
                            ApiError.toError("Revised base value must be greater than original value"));
                }

                baseValues.setOriginalValue(originalBaseValue);
                baseValues.setRevisedValue(revisedBaseValue);
            }

            if (baseOriginalValueDate != null && baseRevisedValueDate != null) {
                if (baseOriginalValueDate.after(baseRevisedValueDate)) {
                    throw new ApiRuntimeException(BAD_REQUEST,
                            ApiError.toError("Revised base value date must be greater than original value date"));
                }

                baseValues.setOriginalValueDate(baseOriginalValueDate);
                baseValues.setRevisedValueDate(baseRevisedValueDate);
            }

        }

        return baseValues;
    }

    public AmpIndicatorGlobalValue validateTargetValues(final MEIndicatorDTO indicatorRequest) {
        AmpIndicatorGlobalValue targetValues = indicatorRequest.getTargetValue();

        if (indicatorRequest.getTargetValue() != null) {
            Double originalTargetValue = indicatorRequest.getTargetValue().getOriginalValue();
            Double revisedTargetValue = indicatorRequest.getTargetValue().getRevisedValue();
            Date targetOriginalValueDate = indicatorRequest.getTargetValue().getOriginalValueDate();
            Date targetRevisedValueDate = indicatorRequest.getTargetValue().getRevisedValueDate();

            if (originalTargetValue != null && revisedTargetValue != null) {
                if (originalTargetValue > revisedTargetValue) {
                    throw new ApiRuntimeException(BAD_REQUEST,
                            ApiError.toError("Revised target value must be greater than original value"));
                }

                targetValues.setOriginalValue(originalTargetValue);
                targetValues.setRevisedValue(revisedTargetValue);
            }

            if (targetOriginalValueDate != null && targetRevisedValueDate != null) {
                if (targetOriginalValueDate.after(targetRevisedValueDate)) {
                    throw new ApiRuntimeException(BAD_REQUEST,
                            ApiError.toError("Revised target value date must be greater than original value date"));
                }

                targetValues.setOriginalValueDate(targetOriginalValueDate);
                targetValues.setRevisedValueDate(targetRevisedValueDate);
            }
        }

        return targetValues;
    }

    public void validateIndicatorName(String name, Session session) {
        AmpIndicator existingIndicator = (AmpIndicator) session.createCriteria(AmpIndicator.class)
                .addOrder(Order.asc("id"))
                .add(org.hibernate.criterion.Restrictions.eq("name", name))
                .setMaxResults(1)
                .uniqueResult();
        if (existingIndicator != null) {
            throw new ApiRuntimeException(BAD_REQUEST,
                    ApiError.toError("Indicator with name " + name + " already exists"));
        }
    }

    public void validateIndicatorCode(String code, Session session) {
        AmpIndicator existingIndicator = (AmpIndicator) session.createCriteria(AmpIndicator.class)
                .addOrder(Order.asc("id"))
                .add(org.hibernate.criterion.Restrictions.eq("code", code))
                .setMaxResults(1)
                .uniqueResult();
        if (existingIndicator != null) {
            throw new ApiRuntimeException(BAD_REQUEST,
                    ApiError.toError("Indicator with code " + code + " already exists"));
        }
    }

    public List<AmpCategoryValueDTO> getCategoryValues () {
        Session session = PersistenceManager.getSession();

        List <AmpCategoryValue> categoryValues = session.createQuery("select o from " + AmpCategoryValue.class.getName() + " o "
                        + "where o.ampCategoryClass.keyName=:keyName")
                .setString("keyName", INDICATOR_CATEGORY_KEY).list();

        return categoryValues.stream()
                .map(AmpCategoryValueDTO::new)
                .collect(Collectors.toList());
    }
}
