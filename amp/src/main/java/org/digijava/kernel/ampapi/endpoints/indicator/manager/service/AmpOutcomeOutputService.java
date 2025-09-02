package org.digijava.kernel.ampapi.endpoints.indicator.manager.service;

import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.MEIndicatorDTO;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpOutcome;
import org.digijava.module.aim.dbentity.AmpOutput;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.dto.AmpOutcomeDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.dto.AmpOutputDTO;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.*;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

public class AmpOutcomeOutputService {
    public List<AmpOutcomeDTO> getAllOutcomes() {
        Session session = PersistenceManager.getSession();
        List<AmpOutcome> outcomes = session.createQuery(
            "select distinct o from AmpOutcome o left join fetch o.outputs", AmpOutcome.class
        ).getResultList();
        // Ensure outputs are initialized to avoid collection eviction
        for (AmpOutcome outcome : outcomes) {
            Hibernate.initialize(outcome.getOutputs());
        }
        return outcomes.stream().map(this::toOutcomeDTO).collect(Collectors.toList());
    }

    public List<AmpOutputDTO> getAllOutputs() {
        Session session = PersistenceManager.getSession();
        List<AmpOutput> outputs = session.createQuery("from AmpOutput", AmpOutput.class).list();
        return outputs.stream().map(this::toOutputDTO).collect(Collectors.toList());
    }

    public AmpOutcomeDTO createOutcome(AmpOutcomeDTO dto) {
        Session session = PersistenceManager.getSession();
        AmpOutcome outcome = new AmpOutcome();
        outcome.setName(dto.getName());
        outcome.setDescription(dto.getDescription());
        session.save(outcome);
        session.flush();
        return toOutcomeDTO(outcome);
    }

    public AmpOutputDTO createOutput(AmpOutputDTO dto) {
        Session session = PersistenceManager.getSession();
        AmpOutput output = new AmpOutput();
        output.setName(dto.getName());
        output.setDescription(dto.getDescription());
        AmpOutcome outcome = session.get(AmpOutcome.class, dto.getOutcomeId());

        output.setOutcome(outcome);
        session.save(output);
        // Ensure bidirectional relationship
        if (outcome != null) {
            outcome.getOutputs().add(output);
            session.update(outcome);
        }
        session.flush();
        return toOutputDTO(output);
    }

    public AmpOutcomeDTO updateOutcome(Long id, AmpOutcomeDTO dto) {
        Session session = PersistenceManager.getSession();
        AmpOutcome outcome = session.get(AmpOutcome.class, id);
        if (outcome == null) return null;
        outcome.setName(dto.getName());
        outcome.setDescription(dto.getDescription());
        session.update(outcome);
        session.flush();
        return toOutcomeDTO(outcome);
    }

    public AmpOutputDTO updateOutput(Long id, AmpOutputDTO dto) {
        Session session = PersistenceManager.getSession();
        AmpOutput output = session.get(AmpOutput.class, id);
        if (output == null) return null;
        output.setName(dto.getName());
        output.setDescription(dto.getDescription());
        AmpOutcome prevOutcome = output.getOutcome();
        AmpOutcome newOutcome = session.get(AmpOutcome.class, dto.getOutcomeId());
        if (prevOutcome != null && !prevOutcome.getId().equals(dto.getOutcomeId())) {
            prevOutcome.getOutputs().remove(output);
            session.update(prevOutcome);
        }
        output.setOutcome(newOutcome);
        if (newOutcome != null && !newOutcome.getOutputs().contains(output)) {
            newOutcome.getOutputs().add(output);
            session.update(newOutcome);
        }
        session.update(output);
        session.flush();
        return toOutputDTO(output);
    }


    public void deleteOutput(Long id, boolean forceDelete) {
        Session session = PersistenceManager.getSession();
        AmpOutput output = session.get(AmpOutput.class, id);
        if (output == null) {
            throw new ApiRuntimeException(BAD_REQUEST, ApiError.toError("Output not found"));
        }
        List<AmpIndicator> indicators = session.createQuery(
                "FROM AmpIndicator ai WHERE ai.output.id = :outputId", AmpIndicator.class)
                .setParameter("outputId", id)
                .getResultList();
        if (!indicators.isEmpty()) {
            if(!forceDelete) {
                StringBuilder msg = new StringBuilder("Cannot delete Output because it has linked Indicators. Please re-assign these Indicators to a different Output or confirm deletion to orphan them.");
                msg.append("\n Warning: There are ").append(indicators.size()).append(" active indicators linked to this Output. Deleting this Output will orphan those indicators.");
                throw new ApiRuntimeException(BAD_REQUEST, ApiError.toError(msg.toString()));
            }
            else
            {
                //unlink indicators
                for (AmpIndicator indicator : indicators) {
                    indicator.setOutput(null);
                    session.update(indicator);
                }
                session.flush();
            }
        }
        session.delete(output);
        session.flush();
    }

    public AmpOutputDTO getOutputById(Long id) {
        Session session = PersistenceManager.getSession();
        AmpOutput output = session.get(AmpOutput.class, id);
        if (output == null) return null;
        AmpOutputDTO dto = toOutputDTO(output);
        dto.setOutcome(toOutcomeDTO(output.getOutcome()));
        return dto;
    }


    private AmpOutcomeDTO toOutcomeDTO(AmpOutcome outcome) {
        AmpOutcomeDTO dto = new AmpOutcomeDTO();
        dto.setId(outcome.getId());
        dto.setName(outcome.getName());
        dto.setDescription(outcome.getDescription());
        if (outcome.getOutputs() != null) {
            dto.setOutputs(outcome.getOutputs().stream().map(this::toOutputDTO).collect(Collectors.toList()));
            dto.setOutputIds(outcome.getOutputs().stream().map(AmpOutput::getId).collect(Collectors.toList()));
        }
        return dto;
    }

    private AmpOutputDTO toOutputDTO(AmpOutput output) {
        AmpOutputDTO dto = new AmpOutputDTO();
        dto.setId(output.getId());
        dto.setName(output.getName());
        dto.setDescription(output.getDescription());
        dto.setOutcomeId(output.getOutcome() != null ? output.getOutcome().getId() : null);
        return dto;
    }


    public List<MEIndicatorDTO> getIndicatorsByOutputId(Long outputId) {
        Session session = PersistenceManager.getSession();
        List<AmpIndicator> indicators = session.createQuery(
                "FROM AmpIndicator ai WHERE ai.output.id = :outputId", AmpIndicator.class)
                .setParameter("outputId", outputId)
                .getResultList();
        return indicators.stream().map(MEIndicatorDTO::new).collect(Collectors.toList());
    }

    public List<AmpOutputDTO> getOutputsByOutcomeId(Long outcomeId) {
        Session session = PersistenceManager.getSession();
        AmpOutcome outcome = (AmpOutcome) session.get(AmpOutcome.class, outcomeId);
        if (outcome == null) return new ArrayList<>();
        List<AmpOutputDTO> outputDTOs = new ArrayList<>();
        for (AmpOutput output : outcome.getOutputs()) {
            outputDTOs.add(toOutputDTO(output));
        }
        return outputDTOs;
    }

    public void deleteOutcome(Long outcomeId) {
        Session session = PersistenceManager.getSession();
        AmpOutcome outcome = (AmpOutcome) session.get(AmpOutcome.class, outcomeId);
        if (outcome == null) {
            throw new ApiRuntimeException(BAD_REQUEST, ApiError.toError("Outcome not found"));
        }
        List<AmpOutput> outputs = new ArrayList<>(outcome.getOutputs());
        if (!outputs.isEmpty()) {
            List<Long> outputIds = outputs.stream().map(AmpOutput::getId).collect(Collectors.toList());
            List<AmpIndicator> indicators = session.createQuery(
                            "FROM AmpIndicator ai WHERE ai.output.id IN :outputIds", AmpIndicator.class)
                    .setParameter("outputIds", outputIds)
                    .getResultList();
            StringBuilder msg = new StringBuilder("Cannot delete Outcome because it has linked Outputs. Please re-assign or delete these Outputs first.");
            if (!indicators.isEmpty()) {
                msg.append("\n Warning: There are ").append(indicators.size()).append(" active indicators linked to these Outputs. Deleting this Outcome will orphan those indicators.");
            }
            throw new ApiRuntimeException(BAD_REQUEST, ApiError.toError(msg.toString()));
        }
        List<AmpIndicator> indicators = session.createQuery(
                        "FROM AmpIndicator ai WHERE ai.outcome.id = :outcomeId", AmpIndicator.class)
                .setParameter("outcomeId", outcomeId)
                .getResultList();
        if (!indicators.isEmpty()) {
            indicators.forEach(indicator -> {
                indicator.setOutcome(null);
                session.update(indicator);
            });
            session.flush();
        }
        session.delete(outcome);
        session.flush();
    }

}
