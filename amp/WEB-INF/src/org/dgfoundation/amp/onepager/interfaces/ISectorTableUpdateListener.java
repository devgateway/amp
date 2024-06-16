package org.dgfoundation.amp.onepager.interfaces;

import org.digijava.module.aim.dbentity.AmpSector;
import java.util.List;

public interface ISectorTableUpdateListener {
    void onUpdate(List<AmpSector> data);
}
