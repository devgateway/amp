package org.digijava.kernel.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

/**
 * Container for constraint descriptors.
 *
 * @author Octavian Ciubotaru
 */
public class ConstraintDescriptors {

    private List<ConstraintDescriptor> descriptors;

    public ConstraintDescriptors(List<ConstraintDescriptor> descriptors) {
        this.descriptors = descriptors;
    }

    /**
     * Returns constraint descriptors matching provided validation groups.
     *
     * @param groups validation groups, empty set represents the default group
     * @return constraint descriptors matching the provided groups
     */
    public List<ConstraintDescriptor> getDescriptorsFor(Set<Class<?>> groups) {
        ArrayList<ConstraintDescriptor> result = new ArrayList<>();
        for (ConstraintDescriptor descriptor : descriptors) {
            if (descriptor.getGroups().isEmpty() || CollectionUtils.containsAny(descriptor.getGroups(), groups)) {
                result.add(descriptor);
            }
        }
        return result;
    }
}
