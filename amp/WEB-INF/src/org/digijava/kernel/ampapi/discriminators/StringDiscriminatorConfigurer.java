package org.digijava.kernel.ampapi.discriminators;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Simplest case when discriminator is just a plain string.
 *
 * @author Octavian Ciubotaru
 */
public class StringDiscriminatorConfigurer implements DiscriminationConfigurer {

    @Override
    public void configure(Object obj, String fieldName, String discriminationValue) {
        try {
            PropertyUtils.setProperty(obj, fieldName, discriminationValue);
        } catch (ReflectiveOperationException e) {
            String msg = String.format("Could not restore discriminator value for object %s field %s value %s",
                    obj, fieldName, discriminationValue);
            throw new RuntimeException(msg, e);
        }
    }

}
