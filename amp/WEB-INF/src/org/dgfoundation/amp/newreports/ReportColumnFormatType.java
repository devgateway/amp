package org.dgfoundation.amp.newreports;

/**
 * Enum using to identify the type of the column (number|date|text)
 * Used for formatting the cells in reports
 * 
 * @author Viorel Chihai
 *
 */
public enum ReportColumnFormatType {
    TEXT("text"), 
    DATE("date"),
    NUMBER("number");

    private final String text;

    /**
     * @param text
     */
    private ReportColumnFormatType(final String text) {
        this.text = text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
