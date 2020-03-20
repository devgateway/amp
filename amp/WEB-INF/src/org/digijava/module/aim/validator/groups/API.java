package org.digijava.module.aim.validator.groups;

/**
 * API Bean Validation group.
 *
 * <p>All validators that must be enforced <em>only</em> when object comes from API must be marked with this group.</p>
 *
 * <p>It can be used when validators for API are more strict than the ones applied in WebUI.
 * Hibernate will apply all validators from Default group. Thus if we add validator just to API group we can avoid
 * the more strict validator from being applied for changes coming from WebUI.</p>
 *
 * @author Octavian Ciubotaru
 */
public interface API {
}
