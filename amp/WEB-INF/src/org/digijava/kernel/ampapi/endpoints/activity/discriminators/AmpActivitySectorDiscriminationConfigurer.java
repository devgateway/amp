package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.algo.Memoizer;
import org.digijava.kernel.ampapi.discriminators.DiscriminationConfigurer;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.util.SectorUtil;

/**
 * @author Octavian Ciubotaru
 */
public class AmpActivitySectorDiscriminationConfigurer implements DiscriminationConfigurer {

    private Memoizer<Map<String, AmpClassificationConfiguration>> map = new Memoizer<>(this::loadAll);

    private Map<String, AmpClassificationConfiguration> loadAll() {
        List<AmpClassificationConfiguration> configs = SectorUtil.getAllClassificationConfigs();
        Map<String, AmpClassificationConfiguration> map = new HashMap<>();
        for (AmpClassificationConfiguration config : configs) {
            map.put(config.getName(), config);
        }
        return map;
    }

    @Override
    public void configure(Object obj, String fieldName, String discriminationValue) {
        AmpActivitySector sector = (AmpActivitySector) obj;
        sector.setClassificationConfig(map.get().get(discriminationValue));
    }

}
