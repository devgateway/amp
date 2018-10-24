package org.dgfoundation.amp.metamodel.type;

/**
 * <p>Value type in the domain model.</p>
 * Can represent:
 * <ul><li>a primitive like number/string/boolean</li>
 * <li>or an object like {@link java.util.Date}, {@link org.digijava.module.aim.dbentity.AmpOrganisation}
 * or {@link org.digijava.module.aim.dbentity.AmpTheme}</li></ul>
 *
 * TODO we're currently using toString() to display the value in output and also for comparison
 * TODO issue #1 might not be as nice as some specific possible locale or setting dependent formats (eg date/boolean)
 * TODO issue #2 should we use equals() in comparisons? what if equals returns false but toString() the same?
 *
 * @author Octavian Ciubotaru
 */
public final class ValueType implements Type {
}
