package org.digijava.module.gis.util;

import org.digijava.module.aim.dbentity.AmpSector;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class SectorRefCount {
    public SectorRefCount() {
    }

    public SectorRefCount(AmpSector sector, int count) {
        this.sector = sector;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public AmpSector getSector() {
        return sector;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setSector(AmpSector sector) {
        this.sector = sector;
    }

    AmpSector sector;
    int count;
}
