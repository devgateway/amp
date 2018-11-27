package org.digijava.kernel.ampapi.swagger.converters;

import io.swagger.models.Model;

/**
 * Use in pair with custom serializers to provider the swagger model.
 *
 * The serializer must also implement this interface in order to describe the model.
 *
 * @author Octavian Ciubotaru
 */
public interface ModelDescriber {

    Model describe();
}
