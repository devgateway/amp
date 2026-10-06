package org.digijava.kernel.ampapi.endpoints.indicator.manager.service;

import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.AmpCategoryValueDTO;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpIndicatorDisaggregationValue;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

public class DisaggregationService {

    private static String sanitizeOptionValue(String raw) {
        if (raw == null) return "";
        // Strip HTML/XML tags
        return raw.replaceAll("<[^>]*>", "").trim();
    }

    public List<AmpCategoryValueDTO> getDisaggregationOptions(Long categoryValueId) {
        Session session = PersistenceManager.getSession();
        String keyName = "indicator_disaggregation_" + categoryValueId;
        AmpCategoryClass optionClass = (AmpCategoryClass) session.createQuery("from AmpCategoryClass where keyName = :key")
                .setParameter("key", keyName)
                .uniqueResult();
        if (optionClass == null) return new ArrayList<>();
        List<AmpCategoryValue> values = session.createQuery("from AmpCategoryValue where ampCategoryClass.id = :classId order by index", AmpCategoryValue.class)
                .setParameter("classId", optionClass.getId())
                .list();
        List<AmpCategoryValueDTO> dtos = new ArrayList<>();
        for (AmpCategoryValue value : values) {
            dtos.add(new AmpCategoryValueDTO(value));
        }
        return dtos;
    }

    public AmpCategoryValueDTO addDisaggregationOption(Long categoryValueId, AmpCategoryValueDTO option) {
        String sanitizedValue = sanitizeOptionValue(option.getValue());
        if (sanitizedValue.isEmpty()) {
            throw new BadRequestException("Option value must not be blank or contain only HTML tags.");
        }
        Session session = PersistenceManager.getSession();
        String keyName = "indicator_disaggregation_" + categoryValueId;
        AmpCategoryClass optionClass = (AmpCategoryClass) session.createQuery("from AmpCategoryClass where keyName = :key")
                .setParameter("key", keyName)
                .uniqueResult();
        if (optionClass == null) {
            optionClass = new AmpCategoryClass();
            optionClass.setKeyName(keyName);
            optionClass.setName("Disaggregation Option for " + categoryValueId);
            optionClass.setDescription("Options for disaggregation category value " + categoryValueId);
            optionClass.setIsMultiselect(false);
            optionClass.setIsOrdered(true);
            session.save(optionClass);
            session.flush();
        }
        AmpCategoryValue value = new AmpCategoryValue();

        value.setAmpCategoryClass(optionClass);
        // Set index Column to next available
        Query<Integer> query = session.createQuery("select max(index) from AmpCategoryValue where ampCategoryClass.id = :classId", Integer.class);
        query.setParameter("classId", optionClass.getId());
        Integer maxIndex = Optional.ofNullable(query.uniqueResult()).orElse(0);
        value.setIndex(maxIndex + 1);
        value.setValue(sanitizedValue);
        value.setDeleted(false);
        session.save(value);
        session.flush();
        return option;
    }

    public AmpCategoryValueDTO updateDisaggregationOption(Long optionId, AmpCategoryValueDTO value) {
        String sanitizedValue = sanitizeOptionValue(value.getValue());
        if (sanitizedValue.isEmpty()) {
            throw new BadRequestException("Option value must not be blank or contain only HTML tags.");
        }
        Session session = PersistenceManager.getSession();
        AmpCategoryValue existing = session.get(AmpCategoryValue.class, optionId);
        if (existing == null) return null;
        existing.setValue(sanitizedValue);
        session.update(existing);
        session.flush();
        return value;
    }

    public void deleteDisaggregationOption(Long optionId) {
        Session session = PersistenceManager.getSession();
        AmpCategoryValue option = session.get(AmpCategoryValue.class, optionId);
        if (option != null) {
            if (isOptionLinkedToIndicator(session, optionId)) {
                throw new ApiRuntimeException(BAD_REQUEST, ApiError.toError("This disaggregation option cannot be "
                        + "deleted because it is used by an indicator. Remove the disaggregation from the "
                        + "indicator before deleting this option."));
            }
            // AmpCategoryClass.possibleValues is cascade="all-delete-orphan" + lazy="false".
            // The parent class is eagerly loaded and its possibleValues list is already in the
            // session, so we must remove this option from that list BEFORE deleting it —
            // otherwise Hibernate's cascade will try to re-save the deleted object on flush.
            AmpCategoryClass categoryClass = option.getAmpCategoryClass();
            if (categoryClass != null) {
                categoryClass.getPossibleValues().remove(option);
            }
            session.delete(option);
            session.flush();
        }
    }

    private boolean isOptionLinkedToIndicator(Session session, Long optionId) {
        Long linkedCount = session.createQuery("select count(disaggValue.id) from "
                + AmpIndicatorDisaggregationValue.class.getName() + " disaggValue "
                + "where disaggValue.parentCategory.id = :optionId "
                + "or disaggValue.childCategory.id = :optionId", Long.class)
                .setParameter("optionId", optionId)
                .uniqueResult();
        return linkedCount != null && linkedCount > 0;
    }
}
