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
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.*;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

public class AmpOutcomeOutputService {
    public List<AmpOutcomeDTO> getAllOutcomes() {
        Session session = PersistenceManager.getSession();
        List<AmpOutcome> outcomes = session.createQuery("select o from AmpOutcome o left join fetch o.outputs", AmpOutcome.class).list();
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
        Set<AmpOutcome> outcomes = new HashSet<>();
        for (Long id : dto.getOutcomeIds()) {
            AmpOutcome outcome = session.get(AmpOutcome.class, id);
            if (outcome != null) {
                outcomes.add(outcome);
            }
        }
        output.setOutcomes(outcomes);
        session.save(output);
        // Ensure bidirectional relationship
        for (AmpOutcome outcome : outcomes) {
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
        // Prepare new outcomes set
        Set<AmpOutcome> newOutcomes = Optional.ofNullable(dto.getOutcomeIds())
                .orElse(Collections.emptyList())
                .stream()
                .map(oid -> session.get(AmpOutcome.class, oid))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        // Remove output from outcomes that are no longer linked
        output.getOutcomes().stream()
                .filter(o -> !newOutcomes.contains(o))
                .forEach(o -> {
                    o.getOutputs().remove(output);
                    session.update(o);
                });
        // Add output to new outcomes
        newOutcomes.stream()
                .filter(o -> !output.getOutcomes().contains(o))
                .forEach(o -> {
                    o.getOutputs().add(output);
                    session.update(o);
                });
        output.setOutcomes(newOutcomes);
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
        if (!indicators.isEmpty() && !forceDelete) {
            StringBuilder msg = new StringBuilder("Cannot delete Output because it has linked Indicators. Please re-assign these Indicators to a different Output or confirm deletion to orphan them.");
            msg.append("\n Warning: There are ").append(indicators.size()).append(" active indicators linked to this Output. Deleting this Output will orphan those indicators.");
            throw new ApiRuntimeException(BAD_REQUEST, ApiError.toError(msg.toString()));
        }
        // Unlink indicators if forceDelete is true
        if (!indicators.isEmpty() && forceDelete) {
            for (AmpIndicator indicator : indicators) {
                indicator.setOutput(null);
                session.update(indicator);
            }
            session.flush(); // Ensure DB is updated before deleting output
        }
        session.delete(output);
        session.flush();
    }

    public AmpOutputDTO getOutputById(Long id) {
        Session session = PersistenceManager.getSession();
        AmpOutput output = session.get(AmpOutput.class, id);
        if (output == null) return null;
        AmpOutputDTO dto = toOutputDTO(output);
        dto.setOutcomes(output.getOutcomes().stream().map(this::toOutcomeDTO).collect(Collectors.toList()));
        return dto;
    }


    private AmpOutcomeDTO toOutcomeDTO(AmpOutcome outcome) {
        AmpOutcomeDTO dto = new AmpOutcomeDTO();
        dto.setId(outcome.getId());
        dto.setName(outcome.getName());
        dto.setDescription(outcome.getDescription());
        dto.setOutputs(outcome.getOutputs().stream().map(this::toOutputDTO).collect(Collectors.toList()));
        if (outcome.getOutputs() != null) {
            dto.setOutputIds(outcome.getOutputs().stream().map(AmpOutput::getId).collect(Collectors.toList()));
        }
        return dto;
    }

    private AmpOutputDTO toOutputDTO(AmpOutput output) {
        AmpOutputDTO dto = new AmpOutputDTO();
        dto.setId(output.getId());
        dto.setName(output.getName());
        dto.setDescription(output.getDescription());
        if (output.getOutcomes() != null) {
            dto.setOutcomeIds(output.getOutcomes().stream().map(AmpOutcome::getId).collect(Collectors.toList()));
        }
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
