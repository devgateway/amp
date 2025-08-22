package org.digijava.kernel.ampapi.endpoints.indicator.manager.service;

import org.digijava.module.aim.dbentity.AmpOutcome;
import org.digijava.module.aim.dbentity.AmpOutput;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.dto.AmpOutcomeDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.dto.AmpOutputDTO;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.*;
import java.util.stream.Collectors;

public class AmpOutcomeOutputService {
    public List<AmpOutcomeDTO> getAllOutcomes() {
        Session session = PersistenceManager.getSession();
        List<AmpOutcome> outcomes = session.createQuery("from AmpOutcome", AmpOutcome.class).list();
        return outcomes.stream().map(this::toOutcomeDTO).collect(Collectors.toList());
    }

    public List<AmpOutputDTO> getAllOutputs() {
        Session session = PersistenceManager.getSession();
        List<AmpOutput> outputs = session.createQuery("from AmpOutput", AmpOutput.class).list();
        return outputs.stream().map(this::toOutputDTO).collect(Collectors.toList());
    }

    public AmpOutcomeDTO createOutcome(AmpOutcomeDTO dto) {
        Session session = PersistenceManager.getSession();
        Transaction tx = session.beginTransaction();
        AmpOutcome outcome = new AmpOutcome();
        outcome.setName(dto.getName());
        outcome.setDescription(dto.getDescription());
        session.save(outcome);
        tx.commit();
        return toOutcomeDTO(outcome);
    }

    public AmpOutputDTO createOutput(AmpOutputDTO dto) {
        Session session = PersistenceManager.getSession();
        Transaction tx = session.beginTransaction();
        AmpOutput output = new AmpOutput();
        output.setName(dto.getName());
        output.setDescription(dto.getDescription());
        Set<AmpOutcome> outcomes = new HashSet<>();
        if (dto.getOutcomeIds() != null) {
            for (Long id : dto.getOutcomeIds()) {
                AmpOutcome outcome = session.get(AmpOutcome.class, id);
                if (outcome != null) {
                    outcomes.add(outcome);
                }
            }
        }
        output.setOutcomes(outcomes);
        session.save(output);
        tx.commit();
        return toOutputDTO(output);
    }

    public AmpOutcomeDTO updateOutcome(Long id, AmpOutcomeDTO dto) {
        Session session = PersistenceManager.getSession();
        Transaction tx = session.beginTransaction();
        AmpOutcome outcome = session.get(AmpOutcome.class, id);
        if (outcome == null) return null;
        outcome.setName(dto.getName());
        outcome.setDescription(dto.getDescription());
        session.update(outcome);
        tx.commit();
        return toOutcomeDTO(outcome);
    }

    public AmpOutputDTO updateOutput(Long id, AmpOutputDTO dto) {
        Session session = PersistenceManager.getSession();
        Transaction tx = session.beginTransaction();
        AmpOutput output = session.get(AmpOutput.class, id);
        if (output == null) return null;
        output.setName(dto.getName());
        output.setDescription(dto.getDescription());
        Set<AmpOutcome> outcomes = new HashSet<>();
        if (dto.getOutcomeIds() != null) {
            for (Long oid : dto.getOutcomeIds()) {
                AmpOutcome outcome = session.get(AmpOutcome.class, oid);
                if (outcome != null) {
                    outcomes.add(outcome);
                }
            }
        }
        output.setOutcomes(outcomes);
        session.update(output);
        tx.commit();
        return toOutputDTO(output);
    }

    public boolean deleteOutcome(Long id) {
        Session session = PersistenceManager.getSession();
        Transaction tx = session.beginTransaction();
        AmpOutcome outcome = session.get(AmpOutcome.class, id);
        if (outcome == null) return false;
        session.delete(outcome);
        tx.commit();
        return true;
    }

    public boolean deleteOutput(Long id) {
        Session session = PersistenceManager.getSession();
        Transaction tx = session.beginTransaction();
        AmpOutput output = session.get(AmpOutput.class, id);
        if (output == null) return false;
        session.delete(output);
        tx.commit();
        return true;
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

