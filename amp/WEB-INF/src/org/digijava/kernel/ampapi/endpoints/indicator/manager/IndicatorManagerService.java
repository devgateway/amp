package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorGlobalValue;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.digijava.module.aim.helper.Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE;
import static org.digijava.module.aim.helper.Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE;


/**
 * Indicator Manager Service
 *
 * @author vchihai
 */
public class IndicatorManagerService {

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

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
        validateNameProgramSectorUnique(name, indicatorRequest, session);

        indicator.setName(indicatorRequest.getName());
        indicator.setDescription(indicatorRequest.getDescription());

        indicator.setCode(indicatorRequest.getCode());
        indicator.setType(indicatorRequest.isAscending() ? "A" : "D");
        indicator.setCreationDate(indicatorRequest.getCreationDate());

        Set<AmpIndicatorGlobalValue> indicatorValues = new HashSet<>();
        if (indicatorRequest.getBaseValue() != null) {
            indicatorValues.add(indicatorRequest.getBaseValue());
        }
        if (indicatorRequest.getTargetValue() != null) {
            indicatorValues.add(indicatorRequest.getTargetValue());
        }
        indicatorValues.stream().forEach(value -> value.setIndicator(indicator));
        indicator.setIndicatorValues(indicatorValues);

        Set<AmpSector> sectors = indicatorRequest.getSectorIds().stream()
                .map(id -> (AmpSector) session.get(AmpSector.class, id))
                .collect(Collectors.toSet());
        indicator.setSectors(sectors);

        Set<AmpTheme> programs = indicatorRequest.getProgramIds().stream()
                .map(id -> (AmpTheme) session.get(AmpTheme.class, id))
                .collect(Collectors.toSet());
        indicator.setPrograms(programs);

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

        if (dateTime.isAfter(value.getOriginalValueDate().getTime())) {
            throw new ApiRuntimeException(BAD_REQUEST,
                    ApiError.toError(error + " Original value date " + simpleDateFormat.format(value.getOriginalValueDate())
                            + " should be greater than " + startInString));
        }

        String endInString = "01/01/" + endYear;
        dateTime = DateTime.parse(endInString, formatter);

        if (dateTime.isAfter(value.getRevisedValueDate().getTime())) {
            throw new ApiRuntimeException(BAD_REQUEST,
                    ApiError.toError(error + "Revised value date " + simpleDateFormat.format(value.getRevisedValueDate())
                            + " should be greater than " + endInString));
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


        if (!indicator.getProgramIds().isEmpty()) {
            indicatorProgram = session.createCriteria(AmpIndicator.class)
                    .add(Restrictions.eq("name", name))
                    .createCriteria("programs")
                    .add(Restrictions.in("ampThemeId", indicator.getProgramIds()))
                    .list().size();
        }

        if (indicatorSector > 0 && indicatorProgram > 0) {
            throw new ApiRuntimeException(BAD_REQUEST,
                    ApiError.toError("Indicator with name " + indicator.getName() + " sectors " +
                            StringUtils.join(indicator.getSectorIds(), ",") +
                            " and programs " + StringUtils.join(indicator.getProgramIds(), ",") + " already exist"));
        }

        if (indicatorSector > 0  && indicatorProgram == -1) {
            throw new ApiRuntimeException(BAD_REQUEST,
                    ApiError.toError("Indicator with name " + indicator.getName() + " and sectors " +
                            StringUtils.join(indicator.getSectorIds(), ",") + " already exist"));
        } else if (indicatorSector == -1 && indicatorProgram > 0) {
            throw new ApiRuntimeException(BAD_REQUEST,
                    ApiError.toError("Indicator with name " + indicator.getName() + " and program " +
                            StringUtils.join(indicator.getProgramIds(), ",") + " already exist"));
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

    public MEIndicatorDTO updateMEIndicator(final Long indicatorId, final MEIndicatorDTO indRequest) {
        Session session = PersistenceManager.getSession();
        AmpIndicator indicator = (AmpIndicator) session.get(AmpIndicator.class, indicatorId);
        if (indicator != null) {
            indicator.setName(indRequest.getName());
            indicator.setDescription(indRequest.getDescription());

            indicator.setCode(indRequest.getCode());
            indicator.setType(indRequest.isAscending() ? "A" : "D");
            indicator.setCreationDate(indRequest.getCreationDate());

            if (indRequest.getBaseValue() != null) {
                if (indicator.getBaseValue() != null) {
                    indicator.getBaseValue().setOriginalValue(indRequest.getBaseValue().getOriginalValue());
                    indicator.getBaseValue().setOriginalValueDate(indRequest.getBaseValue().getOriginalValueDate());
                    indicator.getBaseValue().setRevisedValue(indRequest.getBaseValue().getRevisedValue());
                    indicator.getBaseValue().setRevisedValueDate(indRequest.getBaseValue().getRevisedValueDate());
                } else {
                    indicator.getIndicatorValues().add(indRequest.getBaseValue());
                    indicator.getBaseValue().setIndicator(indicator);
                }
            }

            if (indRequest.getTargetValue() != null) {
                if (indicator.getTargetValue() != null) {
                    indicator.getTargetValue().setOriginalValue(indRequest.getTargetValue().getOriginalValue());
                    indicator.getTargetValue().setOriginalValueDate(indRequest.getTargetValue().getOriginalValueDate());
                    indicator.getTargetValue().setRevisedValue(indRequest.getTargetValue().getRevisedValue());
                    indicator.getTargetValue().setRevisedValueDate(indRequest.getTargetValue().getRevisedValueDate());
                } else {
                    indicator.getIndicatorValues().add(indRequest.getTargetValue());
                    indicator.getTargetValue().setIndicator(indicator);
                }
            }

            Set<AmpSector> sectors = indRequest.getSectorIds().stream()
                    .map(id -> (AmpSector) session.get(AmpSector.class, id))
                    .collect(Collectors.toSet());
            indicator.getSectors().clear();
            indicator.getSectors().addAll(sectors);

            indicator.getPrograms().clear();
            if (!indRequest.getProgramIds().isEmpty()) {
                Set<AmpTheme> programs = indRequest.getProgramIds().stream()
                        .map(id -> (AmpTheme) session.get(AmpTheme.class, id))
                        .collect(Collectors.toSet());
                indicator.getPrograms().addAll(programs);
            }

            session.update(indicator);
            return new MEIndicatorDTO(indicator);
        }

        throw new ApiRuntimeException(BAD_REQUEST,
                ApiError.toError("Indicator with id " + indicatorId + " not found"));
    }

}
