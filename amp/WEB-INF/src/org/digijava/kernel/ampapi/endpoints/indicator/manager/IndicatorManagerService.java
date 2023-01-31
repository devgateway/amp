package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.util.SectorUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Indicator Manager Service
 * 
 * @author vchihai
 */
public class IndicatorManagerService {

    protected static final Logger logger = Logger.getLogger(IndicatorManagerService.class);

    public List<AmpIndicatorDTO> getIndicators() {
        Session session = PersistenceManager.getSession();

        List<AmpIndicator> indicators = session.createCriteria(AmpIndicator.class)
                .addOrder(Order.asc("id"))
                .list();

        return indicators.stream()
                .map(AmpIndicatorDTO::new)
                .collect(Collectors.toList());
    }


    public List<AmpSectorDTO> getSectors() {
        return SectorUtil.getAllParentSectors().stream()
                .map(AmpSectorDTO::new)
                .collect(Collectors.toList());
    }

    public AmpIndicatorDTO getIndicatorById(final String id) {
        Session session = PersistenceManager.getSession();

        AmpIndicator indicator = (AmpIndicator) session.get(AmpIndicator.class, Long.valueOf(id));
        if (indicator != null) {
            return new AmpIndicatorDTO(indicator);
        }

        throw new IllegalArgumentException("Indicator with id " + id + " not found");
    }
}
