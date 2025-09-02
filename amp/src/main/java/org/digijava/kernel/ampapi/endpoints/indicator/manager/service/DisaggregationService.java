package org.digijava.kernel.ampapi.endpoints.indicator.manager.service;

import org.digijava.kernel.ampapi.endpoints.indicator.manager.AmpCategoryValueDTO;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DisaggregationService {

    public List<AmpCategoryValueDTO> getDisaggregationOptions(Long categoryValueId) {
        Session session = PersistenceManager.getSession();
        String keyName = "indicator_disaggregation_" + categoryValueId;
        AmpCategoryClass optionClass = (AmpCategoryClass) session.createQuery("from AmpCategoryClass where keyName = :key")
                .setParameter("key", keyName)
                .uniqueResult();
        if (optionClass == null) return new ArrayList<>();
        List<AmpCategoryValue> values = session.createQuery("from AmpCategoryValue where ampCategoryClass.id = :classId order by indexColumn", AmpCategoryValue.class)
                .setParameter("classId", optionClass.getId())
                .list();
        List<AmpCategoryValueDTO> dtos = new ArrayList<>();
        for (AmpCategoryValue value : values) {
            dtos.add(new AmpCategoryValueDTO(value));
        }
        return dtos;
    }

    public AmpCategoryValueDTO addDisaggregationOption(Long categoryValueId, AmpCategoryValueDTO option) {
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
        // Set indexColumn to next available
        Query<Long> query = session.createQuery("select max(index) from AmpCategoryValue where ampCategoryClass.id = :classId", Long.class);
        query.setParameter("classId", optionClass.getId());
        Long maxIndex = Optional.ofNullable(query.uniqueResult()).orElse(0L);
        value.setIndex(maxIndex.intValue() + 1);
        value.setDeleted(false);
        session.save(value);
        session.flush();
        return option;
    }

    public AmpCategoryValueDTO updateDisaggregationOption(Long optionId, AmpCategoryValueDTO value) {
        Session session = PersistenceManager.getSession();
        AmpCategoryValue existing = session.get(AmpCategoryValue.class, optionId);
        if (existing == null) return null;
        existing.setValue(value.getValue());
        session.update(existing);
        session.flush();
        return value;
    }

    public void deleteDisaggregationOption(Long optionId) {
        Session session = PersistenceManager.getSession();
        AmpCategoryValue option = session.get(AmpCategoryValue.class, optionId);
        if (option != null) {
            session.delete(option);
            session.flush();
        }
    }
}
