package org.digijava.kernel.ampapi.endpoints.common;

/**
 * This object is used to return mapId after an {@link org.digijava.module.esrigis.dbentity.AmpApiState} is saved.
 * TODO refactor endpoints to return Long instead of this wrapper. a long is also a valid json.
 *
 * @author Octavian Ciubotaru
 */
public class MapIdWrapper {

    private Long mapId;

    public MapIdWrapper(Long mapId) {
        this.mapId = mapId;
    }

    public Long getMapId() {
        return mapId;
    }
}
