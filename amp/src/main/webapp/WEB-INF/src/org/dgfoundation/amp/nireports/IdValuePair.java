package org.dgfoundation.amp.nireports;

/**
 * a "synonym" for {@link ImmutablePair}<Long, String>
 * @author Dolghier Constantin
 *
 */
public class IdValuePair extends ImmutablePair<Long, String> {
    
    public IdValuePair(Long id, String value) {
        super(id, value);
    }
}
