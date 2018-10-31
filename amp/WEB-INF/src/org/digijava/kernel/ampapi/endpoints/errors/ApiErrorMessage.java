package org.digijava.kernel.ampapi.endpoints.errors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Defines API Error Message template and stores custom value if needed
 * 
 * @author Nadejda Mandrescu
 */
public class ApiErrorMessage {
    /** Message custom Error Code [0..99] within its component/method */
    public final Integer id;
    /** 
     * General error description (laconic), automatically translated.<br>
     * For custom details per error, you must use: <br>
     * <dl>
     * <dt>value </dt> <dd> for non-translatable details like exception messages </dd>
     * <dt>prefix </dt> <dd> for custom translatable detail prefix </dd>
     * </dl>
     */
    public final String description;
    /** (Optional) Error message prefix, custom value, e.g. "Missing fields: " */
    public final String prefix;
    /** (Optional) Error details (e.g. "project_title"), without prefix */
    public final List<String> values;
    
    /**
     * Defines an ApiErrorMessage 
     * @param id see {@link #id}
     * @param description see {@link #description}
     * @param prefix see {@link #prefix}
     */
    public ApiErrorMessage(Integer id, String description, String prefix) {
        this(id, description, prefix, null);
    }
    
    /**
     * Defines an ApiErrorMessahe 
     * @param id see {@link #id id}
     * @param description see {@link #description}
     */
    public ApiErrorMessage(Integer id, String description) {
        this(id, description, null, null);
    }

    private ApiErrorMessage(int id, String description, String prefix, List<String> values) {
        if (id <0 || id > 99) {
            throw new RuntimeException(String.format("Invalid id = %d, must be within [0..99] range.", id));
        }
        if (description == null) {
            throw new RuntimeException("Description is mandatory");
        }
        this.id = id;
        this.description = description;
        this.prefix = prefix;
        this.values = values;
    }

    /**
     * Configures an {@link #ApiErrorMessage(Integer, String, String)} with more details
     * @param value details, see {@link #values}
     */
    public ApiErrorMessage withDetails(String value) {
        List<String> newValues = new ArrayList<>();
        if (values != null) {
            newValues.addAll(values);
        }
        newValues.add(value);
        return new ApiErrorMessage(id, description, prefix, newValues);
    }

    /**
     * Configures an {@link #ApiErrorMessage(Integer, String, String)} with more details
     * @param details details, see {@link #values}
     */
    public ApiErrorMessage withDetails(List<String> details) {
        List<String> newValues = new ArrayList<>();
        if (values != null) {
            newValues.addAll(values);
        }
        newValues.addAll(details);
        return new ApiErrorMessage(id, description, prefix, newValues);
    }
    
    @Override
    public String toString() {
        return "[" + id + "] " + 
                (prefix == null ? "" : "(" + prefix + ") ")
                + description +
                (values == null ? "" :  " : " + values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiErrorMessage message = (ApiErrorMessage) o;
        return Objects.equals(id, message.id) &&
                Objects.equals(description, message.description) &&
                Objects.equals(prefix, message.prefix) &&
                Objects.equals(values, message.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, prefix, values);
    }
}
