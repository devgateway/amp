package org.dgfoundation.amp.nireports.schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * an immutable implementation of {@link TranslatedNamedEntity}
 * @author Dolghier Constantin
 *
 * @param <K>
 */
public class TranslatedEntityImpl<K> implements TranslatedNamedEntity<K> {

	protected final Map<String, K> payloads;
	
	public TranslatedEntityImpl(Map<String, K> values) {
		this.payloads = Collections.unmodifiableMap(new HashMap<>(values));
	}
	
	@Override
	public K getValueForLocale(String locale) {
		return payloads.get(locale);
	}
}
