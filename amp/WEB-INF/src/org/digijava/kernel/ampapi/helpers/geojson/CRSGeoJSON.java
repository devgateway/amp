package org.digijava.kernel.ampapi.helpers.geojson;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

/**
 * A Bean representation of a GeoJSON coordinate reference system (CRS) object.
 */
@JsonTypeInfo(use=Id.NAME,include=As.PROPERTY,property="type")
@JsonSubTypes({
	@JsonSubTypes.Type(value=NamedCRSGeoJSON.class,  name="name"),
	@JsonSubTypes.Type(value=LinkedCRSGeoJSON.class, name="link")
})
abstract class CRSGeoJSON implements Validation {}
