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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;


/**
 * Indicator Manager Service
 *
 * @author vchihai
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

        AmpTheme program = ProgramUtil.getThemeById(indicatorRequest.getProgramId());
        indicator.setProgram(program);

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

            indicator.getProgram().getIndicators().remove(indicator);
            if (indRequest.getProgramId() != null) {

            }

            session.update(indicator);
            return new MEIndicatorDTO(indicator);
        }

        throw new ApiRuntimeException(BAD_REQUEST,
                ApiError.toError("Indicator with id " + indicatorId + " not found"));
    }

}
