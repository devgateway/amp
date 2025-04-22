package org.dgfoundation.amp.reports.converters;

import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Viorel Chihai
 */
public class HardcodedSectors {

    private final AmpSectorScheme primarySectorScheme;

    private final AmpSectorScheme secondarySectorScheme;

    private Map<String, AmpSector> sectors;

    public HardcodedSectors() {
    
        primarySectorScheme = new AmpSectorScheme();
        primarySectorScheme.setSecSchemeName("Primary");
    
        secondarySectorScheme = new AmpSectorScheme();
        secondarySectorScheme.setSecSchemeName("Secondary");
        
        AmpSector sector = newSector(1L, "Primary Sector 1", primarySectorScheme);
    
        AmpSector sec1 = newSector(2L, "Sec 1", primarySectorScheme);
        sec1.setParentSectorId(sector);
    
        AmpSector sec1s1 = newSector(4L, "1.1", primarySectorScheme);
        sec1s1.setParentSectorId(sec1);
    
        AmpSector sec1s2 = newSector(5L, "1.2", primarySectorScheme);
        sec1s2.setParentSectorId(sec1);
    
        AmpSector sec2 = newSector(3L, "Secondary Sector 2", secondarySectorScheme);
    
        AmpSector sec2p1 = newSector(6L, "2.1", secondarySectorScheme);
        sec2p1.setParentSectorId(sec2);

        sectors = Stream.of(sector, sec1, sec1s1, sec1s2, sec2, sec2p1)
                .collect(Collectors.toMap(AmpSector::getName, t -> t));
    }

    public AmpSector getSector(String name) {
        return sectors.get(name);
    }
    
    private AmpSector newSector(long id, String name, AmpSectorScheme scheme) {
        AmpSector sector = new AmpSector();
        sector.setAmpSectorId(id);
        sector.setName(name);
        sector.setAmpSecSchemeId(scheme);
        
        return sector;
    }
}
