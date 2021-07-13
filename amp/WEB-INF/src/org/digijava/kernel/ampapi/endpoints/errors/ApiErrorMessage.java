package org.digijava.kernel.ampapi.endpoints.errors;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.dgfoundation.amp.annotations.checkstyle.IgnoreCanonicalNames;

/**
 * Defines API Error Message template and stores custom value if needed
 *
 * @author Nadejda Mandrescu
 */
public class ApiErrorMessage {
    
    public static final int MAX_ERROR_CODE = 99;
    
    private final Integer typeId;
    
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

    /** (Optional) Unique error details (e.g. "project_title"), without prefix */
    @IgnoreCanonicalNames
    public final Set<String> values;

    /** (Optional) Flags if this error should be treated as a generic error under the default 00 package code */
    @IgnoreCanonicalNames
    public final boolean isGeneric;
    
    public Integer getId() {
        return id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Set<String> getValues() {
        return values;
    }
    
    /**
     *
     * @param typeId - the type of error
     * @param id
     * @param description
     */
    public ApiErrorMessage(int typeId, int id, String description) {
        this(typeId, id, description, null);
    }
    
    public ApiErrorMessage(int typeId, int id, String description, String prefix) {
        this(typeId, id, description, prefix, null, false);
    }
    
    private ApiErrorMessage(int typeId, int id, String description, String prefix, Set<String> values,
                            boolean isGeneric) {
        if (typeId < 0 || typeId > MAX_ERROR_CODE) {
            throw new RuntimeException(String.format("Invalid typeId = %d, must be within [0..%d] range.",
                    typeId, MAX_ERROR_CODE));
        }
        
        if (id < 0 || id > MAX_ERROR_CODE) {
            throw new RuntimeException(String.format("Invalid id = %d, must be within [0..%d] range.",
                    id, MAX_ERROR_CODE));
        }
        if (description == null) {
            throw new RuntimeException("Description is mandatory");
        }
        this.typeId = typeId;
        this.id = id;
        this.description = description;
        this.prefix = prefix;
        this.values = values;
        this.isGeneric = isGeneric;
    }

    /**
     * Configures an {@link #ApiErrorMessage(int, int, String)} with more details
     * @param value details, see {@link #values}
     */
    public ApiErrorMessage withDetails(String value) {
        Set<String> newValues = new LinkedHashSet<>();
        if (values != null) {
            newValues.addAll(values);
        }
        if (value != null) {
            newValues.add(value);
        }
        return new ApiErrorMessage(typeId, id, description, prefix, newValues, isGeneric);
    }
    
    /**
     * Configures an {@link #ApiErrorMessage(int, int, String)} with prefix
     * @param prefix
     * @return
     */
    public ApiErrorMessage withPrefix(String prefix) {
        return new ApiErrorMessage(typeId, id, description, prefix);
    }

    /**
     * Configures an {@link #ApiErrorMessage(int, int, String)} with more details
     * @param details details, see {@link #values}
     */
    public ApiErrorMessage withDetails(Collection<String> details) {
        Set<String> newValues = new LinkedHashSet<>();
        if (values != null) {
            newValues.addAll(values);
        }
        newValues.addAll(details);
        return new ApiErrorMessage(typeId, id, description, prefix, newValues, isGeneric);
    }
    
    public String getErrorId() {
        return String.format(ApiError.ERROR_PATTERN, typeId, id);
    }
    
    @Override
    public String toString() {
        return "[" + (isGeneric ? "generic" : "package") + "]" + "[" + id + "] "
                + (prefix == null ? "" : "(" + prefix + ") ")
                + description
                + (values == null ? "" :  " : " + values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiErrorMessage message = (ApiErrorMessage) o;
        return Objects.equals(id, message.id)
                && Objects.equals(description, message.description)
                && Objects.equals(prefix, message.prefix)
                && Objects.equals(values, message.values)
                && isGeneric == message.isGeneric;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, prefix, values, isGeneric);
    }
}
