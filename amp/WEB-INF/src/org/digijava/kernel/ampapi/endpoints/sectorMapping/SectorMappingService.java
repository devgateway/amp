package org.digijava.kernel.ampapi.endpoints.sectorMapping;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.values.ValueConverter;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.SectorUtil;

import java.io.Serializable;
import java.util.*;

/**
 * @author Diego Rossi
 */
public class SectorMappingService {

    private static final Logger LOGGER = Logger.getLogger(ValueConverter.class);
    private Map<Long, String> sectorClasses = new HashMap<>();
    private String getSectorClassById(Long id) {
        if (sectorClasses.isEmpty()) {
            sectorClasses.put(1L, AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME);
            sectorClasses.put(2L, AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME);
        }
        return sectorClasses.get(id);
    }

    public static class SingleSectorData implements Serializable {
        private Long id;
        private String value;
        private Boolean isPrimary;

        SingleSectorData(Long id, String value, Boolean isPrimary) {
            this.id = id;
            this.value = value;
            this.isPrimary = isPrimary;
        }

        public Long getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public Boolean getIsPrimary() {
            return isPrimary;
        }
    }

    /**
     * Returns a list of primary or secondary sectors by parameter.
     */
    public List<SingleSectorData> getClassifiedSectors(final Long classSector) {
        List<SingleSectorData> sectors = new ArrayList<>();

        String sectorClass = getSectorClassById(classSector);

        if (sectorClass == null) {
            LOGGER.error("Invalid sector class: " + classSector);
            return sectors;
        }

        List<AmpClassificationConfiguration> alClassConfig = SectorUtil.getAllClassificationConfigs();
        AmpClassificationConfiguration classConfig = findByName(alClassConfig, sectorClass);

        Long schemeId = classConfig.getClassification().getAmpSecSchemeId();
        Boolean isPrimary = classSector == 1L;
        SectorUtil.getSectorLevel1(schemeId.intValue()).forEach(sector -> {
            sectors.add(new SingleSectorData(((AmpSector) sector).getAmpSectorId(), ((AmpSector) sector).getName(), isPrimary));
        });

        return sectors;
    }

    public Collection getAllSectorMappings() {
        return SectorUtil.getAllSectorMappings();
    }

    public List<SingleSectorData> getSecondSectorsByPrimary(Long idPrimary) {
        List<SingleSectorData> secondaries = new ArrayList<>();
        Collection mappings = SectorUtil.getSectorMappingsByPrimary(idPrimary);

        if (mappings != null) {
            mappings.forEach(mapping -> {
                AmpSectorMapping sectorMapping = (AmpSectorMapping) mapping;
                AmpSector sector = sectorMapping.getDstSector();
                secondaries.add(new SingleSectorData(sector.getAmpSectorId(), sector.getName(), false));
            });
        }
        return secondaries;
    }

    public void createSectorsMapping(AmpSectorMapping mapping) throws DgException {
        SectorUtil.createSectorMapping(mapping);
    }

    public void deleteSectorMapping(Long id) throws DgException {
        SectorUtil.deleteSectorMapping(id);
    }

    private static AmpClassificationConfiguration findByName(List<AmpClassificationConfiguration> list, String name) {
        for (AmpClassificationConfiguration item : list) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
