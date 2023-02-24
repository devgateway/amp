package org.digijava.kernel.ampapi.endpoints.dto.gis;

import static org.dgfoundation.amp.ar.ColumnConstants.ACTIVITY_ID;
import static org.dgfoundation.amp.ar.ColumnConstants.SSC_MODALITIES;
import static org.dgfoundation.amp.ar.ColumnConstants.PRIMARY_SECTOR;
import static org.dgfoundation.amp.ar.ColumnConstants.DONOR_COUNTRY;

public final class SscDashboardObjectFactory {

    private SscDashboardObjectFactory() {

    }

    public static SscDashboardObject getSscDashboardObject(String columnName, Long objectId) {
        SscDashboardObject o;
        switch (columnName) {
            case ACTIVITY_ID:
                o = new SscDashboardActivity(objectId);
                break;
            case SSC_MODALITIES:
                o = new SscDashboardModality(objectId);
                break;
            case PRIMARY_SECTOR:
                o = new SscDashboardSector(objectId);
                break;
            case DONOR_COUNTRY:
                o = new SscDashboardCountry(objectId);
                break;
            default:
                return null;
        }
        return o;
    }
}
