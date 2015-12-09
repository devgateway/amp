package org.dgfoundation.amp.nireports;

import java.util.Optional;

public class IdValuePair extends ImmutablePair<Long, Optional<String>> {
	
	public IdValuePair(Long id, String value) {
		super(id, Optional.ofNullable(value));
	}
	
	public IdValuePair(Long id, Optional<String> value) {
		super(id, value == null ? Optional.ofNullable(null) : value);
	}
	
}
