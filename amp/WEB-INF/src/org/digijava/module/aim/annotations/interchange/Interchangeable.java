package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.common.values.ValueConverter;
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
     * Path to the common possible values that will be checked for values to avoid rebuilding the same list of options
     * for fields that depend on the same possible values like organization lists.
     * Note: initial usage in activity import validation. Later can use for export and values EP in AMP-25943 
     * @return the alias for common possible values
     */
    String commonPV() default "";

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
     * The field is read only or not based on fm path. If readOnlyFmPath is visible, read only is true
     */
    String readOnlyFmPath() default "";
    
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
     * {@link ValueConverter#getObjectById(java.lang.Class, java.lang.Object)}.
     */
    boolean pickIdOnly() default false;

    Class<?> type() default DefaultType.class;

    /**
     * <p>Mark field whose value depends on some external condition. For example implementation location depends
     * on implementation level. Or organization group used in pledge must match the group of the organization used in
     * donor funding.</p>
     *
     * <p>To make a field required under certain conditions see {@link #requiredDependencies()}.</p>
     *
     * @return list of dependencies
     */
    String[] dependencies() default {};

    /**
     * Mark field as required only under some conditions. For example field becomes visible as in case
     * of on budget fields. Or fields were visible but not required as in case of type of assistance and
     * financing instrument.
     *
     * @return list of required dependencies
     */
    String[] requiredDependencies() default {};

    /**
     * FM entry to control dependency required validator.
     */
    String dependencyRequiredFMPath() default "";

    /**
     * Whether the field is always required, required for non-draft saves, or not required.
     * Applies only for fields with dependencies.
     */
    ActivityEPConstants.RequiredValidation dependencyRequired() default ActivityEPConstants.RequiredValidation.NONE;
    
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

    /**
     * List of constraint validators that apply to the current field.
     */
    InterchangeableValidator[] interValidators() default {};

    /* constraints for multi-level validators */
    boolean uniqueConstraint() default false;

    /**
     * Used to mark percentage field for an object in collection for which all percentages must sum to 100.
     * <p>Not used for validation in the backend buy might be useful for API clients in future to determine if
     * this constraint is enabled or not (via FM rules).</p>
     */
    boolean percentageConstraint() default false;

    /**
     * If type property is set to this class then type will be determined via reflection.
     */
    final class DefaultType {
    }

}
