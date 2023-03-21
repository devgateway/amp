package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;


/**
 * Indicator Manager Service
 *
 * @author vchihai
 * @author Timothy Mugo
 */
public class IndicatorManagerService {

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
            AmpIndicatorGlobalValue validatedBaseValues = validateBaseValues(indicatorRequest);
            indicatorValues.add(validatedBaseValues);
        }

        if (indicatorRequest.getTargetValue() != null) {
            AmpIndicatorGlobalValue validatedTargetValues = validateTargetValues(indicatorRequest);
            indicatorValues.add(validatedTargetValues);
        }

        indicatorValues.stream().forEach(value -> value.setIndicator(indicator));
        indicator.setIndicatorValues(indicatorValues);

        Set<AmpSector> sectors = indicatorRequest.getSectorIds().stream()
                .map(id -> (AmpSector) session.get(AmpSector.class, id))
                .collect(Collectors.toSet());
        indicator.setSectors(sectors);

        session.save(indicator);

        return new MEIndicatorDTO(indicator);
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
                .map(ProgramSchemeDTO::new)
                .collect(Collectors.toList());
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

            if (indRequest.getProgramId() != null) {
                indicator.setProgram(ProgramUtil.getTheme(indRequest.getProgramId()));
                validateProgramSettingsAndGlobalValues(indRequest, indicator);
            }

            if (indRequest.getBaseValue() != null) {
                AmpIndicatorGlobalValue validatedBaseValues = validateBaseValues(indRequest);
                indicator.getIndicatorValues().add(validatedBaseValues);
            }

            if (indRequest.getTargetValue() != null) {
                AmpIndicatorGlobalValue validatedTargetValues = validateTargetValues(indRequest);
                indicator.getIndicatorValues().add(validatedTargetValues);
            }

            Set<AmpSector> sectors = indRequest.getSectorIds().stream()
                    .map(id -> (AmpSector) session.get(AmpSector.class, id))
                    .collect(Collectors.toSet());
            indicator.getSectors().clear();
            indicator.getSectors().addAll(sectors);

            if (indicator.getProgram() != null && indRequest.getProgramId() == null) {
                indicator.getProgram().getIndicators().remove(indicator);
                indicator.setProgram(null);
            }

            session.update(indicator);
            return new MEIndicatorDTO(indicator);
        }

        throw new ApiRuntimeException(BAD_REQUEST,
                ApiError.toError("Indicator with id " + indicatorId + " not found"));
    }

    public void validateProgramSettingsAndGlobalValues(final MEIndicatorDTO indicatorRequest, final AmpIndicator indicator) {
        if (indicatorRequest.getProgramId() != null) {
            indicator.setProgram(ProgramUtil.getTheme(indicatorRequest.getProgramId()));

            if (indicator.getProgram() != null) {
                AmpActivityProgramSettings indicatorSettings = ProgramUtil.getProgramSettingFromTheme(indicator.getProgram());

                if (indicatorSettings != null) {
                    Date baseOriginalValueDate = indicatorRequest.getBaseValue().getOriginalValueDate();
                    Date targetOriginalValueDate = indicatorRequest.getTargetValue().getOriginalValueDate();

                    if (indicatorSettings.getStartDate() != null) {
                        if (baseOriginalValueDate != null) {
                            if (!baseOriginalValueDate.equals(indicatorSettings.getStartDate())) {
                                throw new ApiRuntimeException(BAD_REQUEST,
                                        ApiError.toError("Base original value date must be equal program start date"));
                            }
                        } else {
                            throw new ApiRuntimeException(BAD_REQUEST,
                                    ApiError.toError("Base original value date is required since program start date is set"));
                        }
                    }

                    if (indicatorSettings.getEndDate() != null) {
                        if (targetOriginalValueDate != null) {
                            if (!targetOriginalValueDate.equals(indicatorSettings.getEndDate())) {
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
        new AmpIndicatorGlobalValue();
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
}
