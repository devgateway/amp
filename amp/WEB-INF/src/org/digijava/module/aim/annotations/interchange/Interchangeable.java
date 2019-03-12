package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

@Retention(RetentionPolicy.RUNTIME)
public @interface Interchangeable {
    /**
     * field title -- a capitalized, preferrably unabbreviated name for the field.
     * <p>fieldTitle must match the label name used in activity form. Usually it coincides with fmName.</p>
     * example: "Sectors"; "AMP Internal ID"
     */
    String fieldTitle();

    /**
     * Field label that will be used in UI. If not specified defaults to {@link #fieldTitle()}.
     */
    String label() default ActivityEPConstants.FIELD_TITLE;

    /**
     * <pre>
     * Path in the Feature Manager, corresponding to enabling / disabling said field in AF
     * Usage examples:
     * fmPath = "/Activity Form/Organization/Donor Organization"  
     * fmPath = FMVisibility.ANY_FM + "/Activity Form/Organizations/Donor Organization
     *                                |/Activity Form/Funding/Search Funding Organizations/Search Organizations"
     * fmPath = FMVisibility.PARENT_FM + "/sectorPercentage"
     * </pre>   
     */
    String fmPath() default "";
    
    /**
     * Whether the collection field in AF supports several values. It will be used just for collection fields
     * (like: one activity might have several sectors assigned, or only one)
     */
    boolean multipleValues() default true;
    
    /**
     * whether this field is to be imported on the target system
     * example: ID fields are usually not imported, since are assigned by the 
     * importing DB automatically
     */
    boolean importable() default false;
    
    /**
     *Whether the field is always required, required for non-draft saves, or not required. 
     */
    ActivityEPConstants.RequiredValidation required() default ActivityEPConstants.RequiredValidation.NONE;
    
    /**
     * The field is required or not based on fm path. By default, if requiredFmPath is visible, the required is SUBMIT
     */
    String requiredFmPath() default "";
    
    /**
     * Used during serialization to replace an object with id. The object must implement {@link Identifiable}.
     * <p>Example:
     * <pre>{@code
     * class Example {
     *    @literal @Interchangeable(fieldTitle = "transaction_type", pickIdOnly = true)
     *     private AmpCategoryValue transactionType;
     * }}</pre>
     * Will result in the following json:
     * <pre>
     * {
     *   "transaction_type": 123
     * }
     * </pre>
     *
     * <p>During deserialization, id from json will be converted back using
     * {@link ValueConverter#getObjectById(java.lang.Class, java.lang.Long)}.
     */
    boolean pickIdOnly() default false;

    Class<?> type() default DefaultType.class;

    /**
     * Specifies the dependencies used for later checking in DependencyValidator. 
     * Dependencies (path and value) are encoded via {@link InterchangeDependencyMapper} public static strings,
     * like {@link InterchangeDependencyMapper#ON_BUDGET_CODE}. 
     * @return
     */
    String[] dependencies() default {};
    
    /**
     * <p>Filter objects by specified discriminator value.
     * Must be specified in {@link InterchangeableDiscriminator#settings}.
     * <p>When annotating {@link AmpCategoryValue}, this value must be specified to allow listing possible values for a
     * category class. Can be used inside {@link InterchangeableDiscriminator#settings} or standalone.
     * <p>Category in discriminated field example:
     * <pre>
     * &#64;InterchangeableDiscriminator(discriminatorField="categories",
     *   settings = {
     *     &#64;Interchangeable(fieldTitle = "Activity Status", discriminatorOption = "act_status", pickIdOnly=true),
     *     &#64;Interchangeable(fieldTitle = "Type of Cooperation", discriminatorOption = "coop_type", pickIdOnly=true)
     * })
     * protected Set&#60;AmpCategoryValue&#62; categories;
     * </pre>
     * <p>Standalone category example:
     * <pre>
     * &#64;Interchangeable(fieldTitle = "Adjustment Type", discriminatorOption = "adj_type", pickIdOnly = true)
     * private AmpCategoryValue adjustmentType;
     * </pre>
     */
    String discriminatorOption() default "";
    
    Validators validators() default @Validators;

    /** regex pattern used for validation (mail, phone, fax) */
    String regexPattern() default "";
    
    /* constraints for multi-level validators */
    boolean uniqueConstraint() default false;

    /**
     * Used to mark percentage field for an object in collection for which all percentages must sum to 100.
     * <p>Not used for validation in the backend buy might be useful for API clients in future to determine if
     * this constraint is enabled or not (via FM rules).</p>
     */
    boolean percentageConstraint() default false;

    int sizeLimit() default 1;

    /**
     * If type property is set to this class then type will be determined via reflection.
     */
    final class DefaultType {
    }

}
