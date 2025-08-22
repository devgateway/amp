package org.digijava.kernel.ampapi.endpoints.indicator.manager.service;

import org.digijava.module.aim.dbentity.AmpOutcome;
import org.digijava.module.aim.dbentity.AmpOutput;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.dto.AmpOutcomeDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.dto.AmpOutputDTO;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import java.util.*;
import java.util.stream.Collectors;

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
        if (dto.getOutcomeIds() == null || dto.getOutcomeIds().isEmpty()) {
            throw new IllegalArgumentException("Output must be linked to at least one Outcome.");
        }
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

    public boolean deleteOutcome(Long id) {
        Session session = PersistenceManager.getSession();
        AmpOutcome outcome = session.get(AmpOutcome.class, id);
        if (outcome == null) return false;
        session.delete(outcome);
        session.flush();
        return true;
    }

    public boolean deleteOutput(Long id) {
        Session session = PersistenceManager.getSession();
        AmpOutput output = session.get(AmpOutput.class, id);
        if (output == null) return false;
        session.delete(output);
        session.flush();
        return true;
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
}
