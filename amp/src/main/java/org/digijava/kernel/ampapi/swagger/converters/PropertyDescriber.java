package org.digijava.kernel.ampapi.swagger.converters;

import io.swagger.models.properties.Property;

/**
 * Use in pair with custom serializers to provider the swagger model.
 *
 * The serializer must also implement this interface in order to describe the property.
 *
 * @author Octavian Ciubotaru
 */
public interface PropertyDescriber {

    Property describe();
}
