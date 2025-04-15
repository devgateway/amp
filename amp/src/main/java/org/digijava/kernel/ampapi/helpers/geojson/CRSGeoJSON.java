package org.digijava.kernel.ampapi.helpers.geojson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A Bean representation of a GeoJSON coordinate reference system (CRS) object.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value=NamedCRSGeoJSON.class,  name="name"),
    @JsonSubTypes.Type(value=LinkedCRSGeoJSON.class, name="link")
})
abstract class CRSGeoJSON implements Validation {}
