/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.errors;

/**
 * Defines API Error Message template and stores custom value if needed
 * @author Nadejda Mandrescu
 */
public class ApiErrorMessage {
	/** Message custom Error Code [0..99] within its component/method*/
	public final Integer id;
	/** General error description (laconic) */
	public final String description;
	/** (Optional) Error message prefix, e.g. "Missing fields: "*/
	public final String prefix;
	/** Error details (e.g. "project_title"), without prefix */
	public final String value;
	
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
	 * @param id see {@link #id}
	 * @param description see {@link #description}
	 */
	public ApiErrorMessage(Integer id, String description) {
		this(id, description, null, null);
	}
	
	/**
	 * Configures an {@link #ApiErrorMessage(Integer, String, String)} with more details
	 * @param aem general error definition, see {@link #ApiErrorMessage(Integer, String, String)}
	 * @param value details, see {@link #value}
	 */
	public ApiErrorMessage(ApiErrorMessage aem, String value) {
		this(aem.id, aem.description, aem.prefix, aem.value == null ? value : aem.value + ", " + value);
	}
	
	private ApiErrorMessage(int id, String description, String prefix, String value) {
		if (id <0 || id > 99) {
			throw new RuntimeException(String.format("Invalid id = %n, must be within [0..99] range.", id));
		}
		if (description == null) {
			throw new RuntimeException(String.format("Description is mandatory"));
		}
		this.id = id;
		this.description = description;
		this.prefix = prefix;
		this.value = value;
	}
	
	public int hashCode() {
		return (19 + id) * 23 + description.hashCode();
	}
	
	@Override
	public String toString() {
		return "[" + id + "] " + 
				(prefix == null ? "" : "(" + prefix + ") ")
				+ description +
				(value == null ? "" :  " : " + value); 
	}

}
