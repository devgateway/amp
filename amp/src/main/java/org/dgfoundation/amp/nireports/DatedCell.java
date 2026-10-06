package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.meta.CategCell;

/**
 * an interface specifying that a cell has a date which can be filtered upon by either (in)equality or ordering relation
 * @author Dolghier Constantin
 *
 */
public interface DatedCell extends CategCell {
    public TranslatedDate getTranslatedDate();
}
