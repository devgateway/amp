package org.dgfoundation.amp.nireports.schema;

/**
 * a Dimensional entity which has different values for different locales
 * @author Dolghier Constantin
 *
 * @param <K>
 */
public interface TranslatedNamedEntity<K> {
	public K getValueForLocale(String locale);
}
