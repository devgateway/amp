package org.digijava.module.aim.dbentity;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.RequiredValidation.SUBMIT;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.discriminators.AmpActivityProgramDiscriminatorConfigurer;
import org.digijava.kernel.ampapi.endpoints.activity.discriminators.AmpFundingAmountDiscriminationConfigurer;
import org.digijava.kernel.ampapi.endpoints.activity.discriminators.AmpOrgRoleDiscriminationConfigurer;
import org.digijava.kernel.ampapi.endpoints.activity.discriminators.AmpActivitySectorDiscriminationConfigurer;
import org.digijava.kernel.ampapi.endpoints.activity.discriminators.AmpRegionalFundingDiscriminationConfigurer;
import org.digijava.kernel.ampapi.endpoints.activity.values.ApprovalStatusPossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.activity.values.FiscalYearPossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.validators.activity.RegionLocationValidator;
import org.digijava.kernel.validators.activity.TreeCollectionValidator;
import org.digijava.kernel.validators.activity.UniqueActivityTitleValidator;
import org.digijava.kernel.validators.activity.ComponentFundingOrgRoleValidator;
import org.digijava.kernel.validators.activity.ImplementationLevelValidator;
import org.digijava.kernel.validators.activity.MultiStakeholderPartnershipValidator;
import org.digijava.kernel.validators.activity.OnBudgetValidator;
import org.digijava.kernel.validators.common.TotalPercentageValidator;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.kernel.validators.common.SizeValidator;
import org.digijava.module.aim.annotations.activityversioning.VersionableCollection;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldSimple;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.interchange.TimestampField;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.aim.validator.approval.AllowedApprovalStatus;
import org.digijava.module.aim.validator.approval.AllowedApprover;
import org.digijava.module.aim.validator.contact.PrimaryContact;
import org.digijava.module.aim.validator.fundings.FundingOrgRole;
import org.digijava.module.aim.validator.groups.API;
import org.digijava.module.aim.validator.groups.Submit;
import org.digijava.module.aim.validator.user.MatchExistingCreator;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permissible;
import org.hibernate.Query;
import org.hibernate.Session;

@TranslatableClass (displayName = "Activity Form Field")
@InterchangeableValidator(UniqueActivityTitleValidator.class)
@InterchangeableValidator(ComponentFundingOrgRoleValidator.class)
@InterchangeableValidator(ImplementationLevelValidator.class)
@InterchangeableValidator(RegionLocationValidator.class)
@InterchangeableValidator(value = OnBudgetValidator.class, groups = Submit.class, attributes = "required=ND")
@InterchangeableValidator(value = MultiStakeholderPartnershipValidator.class, groups = Submit.class,
        attributes = "required=ND")
public abstract class AmpActivityFields extends Permissible implements Comparable<AmpActivityVersion>, Serializable,
LoggerIdentifiable, Cloneable {

    private static final long serialVersionUID = 1L;
    
    protected static String [] IMPLEMENTED_ACTIONS=new String[]{GatePermConst.Actions.EDIT};

    @Interchangeable(fieldTitle = "Project Impact", importable = true,
            fmPath = "/Activity Form/Identification/Project Impact")
    @VersionableFieldTextEditor(fieldTitle = "Project Impact")
    protected String projectImpact;

    @Interchangeable(fieldTitle = "Activity Summary", importable = true,
            fmPath = "/Activity Form/Identification/Activity Summary")
    @VersionableFieldTextEditor(fieldTitle = "Activity Summary")
    protected String activitySummary;

    @Interchangeable(fieldTitle = "Conditionalities", importable = true,
            fmPath = "/Activity Form/Identification/Conditionalities")
    @VersionableFieldTextEditor(fieldTitle = "Conditionality")
    protected String conditionality;

    @Interchangeable(fieldTitle = "Project Management", importable = true,
            fmPath = "/Activity Form/Identification/Project Management")
    @VersionableFieldTextEditor(fieldTitle = "Project Management")
    protected String projectManagement;

    //getter and setter never used
//  @Interchangeable(fieldTitle = "Activity Budget",fmPath="/Activity Form/Identification/Activity Budget", required="/Activity Form/Identification/Required Validator for Activity Budget")
    @VersionableFieldSimple(fieldTitle = "Activity Budget")
    protected Integer budget;
    
    @Interchangeable(fieldTitle = "Government Agreement Number", importable = true, fmPath = "/Activity Form/Identification/Government Agreement Number")
    @VersionableFieldSimple(fieldTitle = "Government Agreement Number")
    protected String govAgreementNumber;
    
    @Interchangeable(fieldTitle = "Budget Code Project ID", importable = true, fmPath = "/Activity Form/Identification/Budget Code Project ID")
    @VersionableFieldSimple(fieldTitle = "Budget Code Project ID")
    protected String budgetCodeProjectID;
    
    //getter and setter never used
    @Interchangeable(fieldTitle = "Budget Sector", importable = true, fmPath = "/Activity Form/Identification/Budget Classification")
    @VersionableFieldSimple(fieldTitle = "Budget Sector")
    protected Long budgetsector;
    
    //getter and setter never used
    @Interchangeable(fieldTitle = "Budget Organization", importable = true, fmPath = "/Activity Form/Identification/Budget Classification")
    @VersionableFieldSimple(fieldTitle = "Budget Organization")
    protected Long budgetorganization;
    
    @Interchangeable(fieldTitle = "Budget Department", importable = true, fmPath = "/Activity Form/Identification/Budget Classification")
    @VersionableFieldSimple(fieldTitle = "Budget Department")
    protected Long budgetdepartment;
    
    
    //getter and setter never used
    @Interchangeable(fieldTitle = "Budget Program", importable = true, fmPath = "/Activity Form/Identification/Budget Classification")
    @VersionableFieldSimple(fieldTitle = "Budget Program")
    protected Long budgetprogram;

    //protected String govAgreementNumber;

    @Interchangeable(fieldTitle = ActivityFieldsConstants.AMP_ACTIVITY_ID)
    @PermissibleProperty(type={Permissible.PermissibleProperty.PROPERTY_TYPE_ID})
    @VersionableFieldSimple(fieldTitle = "Internal ID", blockSingleChange = true)
    protected Long ampActivityId ;

    @Interchangeable(fieldTitle = ActivityFieldsConstants.AMP_ID)
    @VersionableFieldSimple(fieldTitle = "AMP Id", blockSingleChange = true)
    protected String ampId ;

    @Interchangeable(fieldTitle = ActivityFieldsConstants.PROJECT_TITLE, importable = true,
            fmPath = "/Activity Form/Identification/Project Title",
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    @PermissibleProperty(type={Permissible.PermissibleProperty.PROPERTY_TYPE_LABEL})
    @VersionableFieldSimple(fieldTitle = "Name", mandatoryForSingleChange = true)
    @TranslatableField
    protected String name ;
    
    @Interchangeable(fieldTitle = "Description", importable = true,
            fmPath = "/Activity Form/Identification/Description",
            interValidators = @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                    fmPath = "/Activity Form/Identification/Required Validator for Description"))
    @VersionableFieldTextEditor(fieldTitle = "Description")
    protected String description ;

    @Interchangeable(fieldTitle = "Project Comments", importable = true, fmPath = "/Activity Form/Identification/Project Comments")
    @VersionableFieldTextEditor(fieldTitle = "Project Comments")
    protected String projectComments ;
    
    @Interchangeable(fieldTitle = "Lessons Learned", importable = true, fmPath = "/Activity Form/Identification/Lessons Learned")
    @VersionableFieldTextEditor(fieldTitle = "Lessons Learned")
    protected String lessonsLearned;
    
    @Interchangeable(fieldTitle = "Objective", importable = true, fmPath = "/Activity Form/Identification/Objective",
            interValidators = @InterchangeableValidator(
                    value = RequiredValidator.class,
                    fmPath = "/Activity Form/Identification/Required Validator for Objective",
                    groups = Submit.class))
    @VersionableFieldTextEditor(fieldTitle = "Objective")
    protected String objective ;
    
    @Interchangeable(fieldTitle = "Purpose", importable = true, fmPath = "/Activity Form/Identification/Purpose")
    @VersionableFieldTextEditor(fieldTitle = "Purpose")
    protected String purpose;
    
    @Interchangeable(fieldTitle = "Results", importable = true, fmPath = "/Activity Form/Identification/Results")
    @VersionableFieldTextEditor(fieldTitle = "Results")
    protected String results;
    
    @Interchangeable(fieldTitle = "Document Space", importable = true)
    @VersionableFieldSimple(fieldTitle = "Document Space")
    protected String documentSpace;

    @Interchangeable(fieldTitle = ActivityFieldsConstants.IATI_IDENTIFIER, importable = true,
            fmPath = "/Activity Form/Identification/IATI Identifier",
            readOnlyFmPath = "/Activity Form/Identification/IATI Identifier Read Only")
    @VersionableFieldSimple(fieldTitle = ActivityFieldsConstants.IATI_IDENTIFIER)
    protected String iatiIdentifier;

    @Interchangeable(fieldTitle = ActivityFieldsConstants.IS_DRAFT,
            interValidators = @InterchangeableValidator(RequiredValidator.class), importable = true)
    @VersionableFieldSimple(fieldTitle = "Is Draft?", blockSingleChange = true)
    protected Boolean draft;

    @VersionableFieldSimple(fieldTitle = ActivityFieldsConstants.CHANGE_TYPE)
    protected String changeType;

    @Interchangeable(fieldTitle = ActivityFieldsConstants.LAST_IMPORTED_AT)
    @VersionableFieldSimple(fieldTitle = ActivityFieldsConstants.LAST_IMPORTED_AT)
    @TimestampField
    protected Date lastImportedAt;

    @Interchangeable(fieldTitle = ActivityFieldsConstants.LAST_IMPORTED_BY, pickIdOnly=true)
    @VersionableFieldSimple(fieldTitle = ActivityFieldsConstants.LAST_IMPORTED_BY)
    protected User lastImportedBy;

    @Interchangeable(fieldTitle = "Equal Oportunity", importable = true, fmPath = "/Activity Form/Cross Cutting Issues/Equal Opportunity")
    @VersionableFieldTextEditor(fieldTitle = "Equal Oportunity")
    protected String equalOpportunity;
    
    @Interchangeable(fieldTitle = "Environment", importable = true, fmPath = "/Activity Form/Cross Cutting Issues/Environment")
    @VersionableFieldTextEditor(fieldTitle = "Environment")
    protected String environment;
    
    @Interchangeable(fieldTitle = "Minorities", importable = true, fmPath = "/Activity Form/Cross Cutting Issues/Minorities")
    @VersionableFieldTextEditor(fieldTitle = "Minorities")
    protected String minorities;

    @VersionableFieldSimple(fieldTitle = "Language")
    protected String language ;

    @Interchangeable(fieldTitle = "Original Completion Date", importable = true,
            fmPath = "/Activity Form/Planning/Original Completion Date",
            interValidators = @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                    fmPath = "/Activity Form/Planning/Required Validator for Original Completion Date"))
    @VersionableFieldSimple(fieldTitle = "Original Completion Date")
    protected Date originalCompDate;
    
    @Interchangeable(fieldTitle = "Contracting Date", importable = true, fmPath = "/Activity Form/Planning/Final Date for Contracting")
    @VersionableFieldSimple(fieldTitle = "Contracting Date")
    protected Date contractingDate;
    
    @Interchangeable(fieldTitle = "Disbursement Date", importable = true, fmPath = "/Activity Form/Planning/Final Date for Disbursements")
    @VersionableFieldSimple(fieldTitle = "Disbursement Date")
    protected Date disbursmentsDate;
    
    @VersionableCollection(fieldTitle = "Sectors")
    @InterchangeableDiscriminator(discriminatorField = "classificationConfig.name",
            configurer = AmpActivitySectorDiscriminationConfigurer.class,
            settings = {
            @Interchangeable(fieldTitle = ActivityFieldsConstants.PRIMARY_SECTORS,
                    discriminatorOption = "Primary", importable = true,
                    fmPath = "/Activity Form/Sectors/Primary Sectors",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Sectors/Primary Sectors/minSizeSectorsValidator"),
                            @InterchangeableValidator(
                                    value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/sectorPercentageTotal"),
                            @InterchangeableValidator(
                                    value = TreeCollectionValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/treeSectorsValidator")
                    },
                    validators = @Validators(
                            unique = "/Activity Form/Sectors/Primary Sectors/uniqueSectorsValidator")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.SECONDARY_SECTORS,
                    discriminatorOption = "Secondary", importable = true,
                    fmPath = "/Activity Form/Sectors/Secondary Sectors",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Sectors/Secondary Sectors/minSizeSectorsValidator"),
                            @InterchangeableValidator(
                                    value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/sectorPercentageTotal"),
                            @InterchangeableValidator(
                                    value = TreeCollectionValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/treeSectorsValidator")},
                    validators = @Validators(
                            unique = "/Activity Form/Sectors/Secondary Sectors/uniqueSectorsValidator")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.TERTIARY_SECTORS,
                    discriminatorOption = "Tertiary", importable = true,
                    fmPath = "/Activity Form/Sectors/Tertiary Sectors",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Sectors/Tertiary Sectors/minSizeSectorsValidator"),
                            @InterchangeableValidator(
                                    value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/sectorPercentageTotal"),
                            @InterchangeableValidator(
                                    value = TreeCollectionValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/treeSectorsValidator")},
                    validators = @Validators(
                            unique = "/Activity Form/Sectors/Tertiary Sectors/uniqueSectorsValidator")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.TAG_SECTORS,
                    discriminatorOption = "Tag", importable = true,
                    fmPath = "/Activity Form/Sectors/Tag Sectors",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Sectors/Tag Sectors/minSizeSectorsValidator"),
                            @InterchangeableValidator(
                                    value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/sectorPercentageTotal"),
                            @InterchangeableValidator(
                                    value = TreeCollectionValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/treeSectorsValidator")},
                    validators = @Validators(
                            unique = "/Activity Form/Sectors/Tag Sectors/uniqueSectorsValidator"))
    })
    protected Set<AmpActivitySector> sectors = new HashSet<>();
    
//  @Interchangeable(fieldTitle = "Contracts", importable = true, fmPath="/Activity Form/Contracts")
    @VersionableCollection(fieldTitle = "Contracts")
    protected Set<IPAContract> contracts;
    
    //TTIL
    @Interchangeable(fieldTitle = ActivityFieldsConstants.LOCATIONS, importable = true,
            fmPath = "/Activity Form/Location",
            interValidators = {
                    @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                            fmPath = "/Activity Form/Location/Locations/Location required validator"),
                    @InterchangeableValidator(
                            value = TotalPercentageValidator.class,
                            fmPath = "/Activity Form/Location/Locations/locationPercentageTotal"),
                    @InterchangeableValidator(
                            value = TreeCollectionValidator.class,
                            fmPath = "/Activity Form/Location/Locations/Tree Validator")},
            validators = @Validators(
                    unique = "/Activity Form/Location/Locations/uniqueLocationsValidator"))
    @VersionableCollection(fieldTitle = ActivityFieldsConstants.LOCATIONS)
    protected Set<AmpActivityLocation> locations = new HashSet<>();
    
    @VersionableCollection(fieldTitle = "Org. Role")
    @InterchangeableDiscriminator(discriminatorField = "role.roleCode",
            configurer = AmpOrgRoleDiscriminationConfigurer.class, settings = {
            @Interchangeable(fieldTitle = ActivityFieldsConstants.DONOR_ORGANIZATION,
                    importable = true,
                    discriminatorOption = Constants.FUNDING_AGENCY,
                    fmPath = FMVisibility.ANY_FM + "/Activity Form/Organizations/Donor Organization"
                            + "|/Activity Form/Funding/Search Funding Organizations/Search Organizations",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Organizations/Donor Organization/Required Validator"),
                            @InterchangeableValidator(value = SizeValidator.class,
                                    attributes = "max=1",
                                    fmPath = "/Activity Form/Organizations/Donor Organization/Max Size Validator"),
                            @InterchangeableValidator(value = TotalPercentageValidator.class,
                                    fmPath = "/Activity Form/Organizations/Donor Organization/relOrgPercentageTotal")},
                    validators = @Validators(
                            unique = "/Activity Form/Organizations/Donor Organization/Unique Orgs Validator")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.RESPONSIBLE_ORGANIZATION,
                    importable = true,
                    discriminatorOption = Constants.RESPONSIBLE_ORGANISATION,
                    fmPath = "/Activity Form/Organizations/Responsible Organization",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = FMVisibility.PARENT_FM + "/Required Validator"),
                            @InterchangeableValidator(value = SizeValidator.class,
                                    attributes = "max=1",
                                    fmPath = FMVisibility.PARENT_FM + "/Max Size Validator"),
                            @InterchangeableValidator(value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/relOrgPercentageTotal")},
                    validators = @Validators(
                            unique = FMVisibility.PARENT_FM + "/Unique Orgs Validator")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.EXECUTING_AGENCY,
                    importable = true,
                    discriminatorOption = Constants.EXECUTING_AGENCY,
                    fmPath = "/Activity Form/Organizations/Executing Agency",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Organizations/Executing Agency/Required Validator"),
                            @InterchangeableValidator(value = SizeValidator.class,
                                    attributes = "max=1",
                                    fmPath = FMVisibility.PARENT_FM + "/Max Size Validator"),
                            @InterchangeableValidator(value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/relOrgPercentageTotal")},
                    validators = @Validators(
                            unique = FMVisibility.PARENT_FM + "/Unique Orgs Validator")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.IMPLEMENTING_AGENCY,
                    importable = true,
                    discriminatorOption = Constants.IMPLEMENTING_AGENCY,
                    fmPath = "/Activity Form/Organizations/Implementing Agency",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Organizations/Implementing Agency/Required Validator"),
                            @InterchangeableValidator(value = SizeValidator.class,
                                    attributes = "max=1",
                                    fmPath = FMVisibility.PARENT_FM + "/Max Size Validator"),
                            @InterchangeableValidator(value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/relOrgPercentageTotal")},
                    validators = @Validators(
                            unique = FMVisibility.PARENT_FM + "/Unique Orgs Validator")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.BENEFICIARY_AGENCY,
                    importable = true,
                    discriminatorOption = Constants.BENEFICIARY_AGENCY,
                    fmPath = "/Activity Form/Organizations/Beneficiary Agency",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Organizations/Beneficiary Agency/Required Validator"),
                            @InterchangeableValidator(value = SizeValidator.class,
                                    attributes = "max=1",
                                    fmPath = FMVisibility.PARENT_FM + "/Max Size Validator"),
                            @InterchangeableValidator(value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/relOrgPercentageTotal")},
                    validators = @Validators(
                            unique = FMVisibility.PARENT_FM + "/Unique Orgs Validator")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.CONTRACTING_AGENCY,
                    importable = true,
                    discriminatorOption = Constants.CONTRACTING_AGENCY,
                    fmPath = "/Activity Form/Organizations/Contracting Agency",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Organizations/Contracting Agency/Required Validator"),
                            @InterchangeableValidator(value = SizeValidator.class,
                                    attributes = "max=1",
                                    fmPath = FMVisibility.PARENT_FM + "/Max Size Validator"),
                            @InterchangeableValidator(value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/relOrgPercentageTotal")},
                    validators = @Validators(
                            unique = FMVisibility.PARENT_FM + "/Unique Orgs Validator")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.REGIONAL_GROUP,
                    importable = true,
                    discriminatorOption = Constants.REGIONAL_GROUP,
                    fmPath = "/Activity Form/Organizations/Regional Group",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Organizations/Regional Group/Required Validator"),
                            @InterchangeableValidator(value = SizeValidator.class,
                                    attributes = "max=1",
                                    fmPath = FMVisibility.PARENT_FM + "/Max Size Validator"),
                            @InterchangeableValidator(value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/relOrgPercentageTotal")},
                    validators = @Validators(
                            unique = FMVisibility.PARENT_FM + "/Unique Orgs Validator")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.SECTOR_GROUP,
                    importable = true,
                    discriminatorOption = Constants.SECTOR_GROUP,
                    fmPath = "/Activity Form/Organizations/Sector Group",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Organizations/Sector Group/Required Validator"),
                            @InterchangeableValidator(value = SizeValidator.class,
                                    attributes = "max=1",
                                    fmPath = FMVisibility.PARENT_FM + "/Max Size Validator"),
                            @InterchangeableValidator(value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/relOrgPercentageTotal")},
                    validators = @Validators(
                            unique = FMVisibility.PARENT_FM + "/Unique Orgs Validator"))
    })
    protected Set<AmpOrgRole> orgrole = new HashSet<>();
    
    @Interchangeable(fieldTitle = "Activity Internal IDs", importable = true, fmPath = "/Activity Form/Activity Internal IDs")
    @VersionableCollection(fieldTitle = "Activity Internal IDs")
    protected Set<AmpActivityInternalId> internalIds = new HashSet<>();
    
    @Valid
    @FundingOrgRole(groups = API.class)
    @Interchangeable(fieldTitle = ActivityFieldsConstants.FUNDINGS, importable = true,
            fmPath = "/Activity Form/Funding")
    @VersionableCollection(fieldTitle = ActivityFieldsConstants.FUNDINGS)
    protected Set<AmpFunding> funding = new HashSet<>();
    
    //TODO show this field?
    //TODO-reply: we should first figure out what it is
    /*seems obsolete*/
    @VersionableCollection(fieldTitle = "Progress")
    protected Set progress;
    
    /**
     * Old, not used anymore
     * @deprecated
     */
    @Deprecated
    protected Set documents ;
    

    @Interchangeable(fieldTitle = "Issues", importable = true, fmPath = "/Activity Form/Issues Section")
    @VersionableCollection(fieldTitle = "Issues")
    protected Set<AmpIssues> issues = new HashSet<>();

//  @Interchangeable(fieldTitle = "Regional Observations", importable = true, fmPath = "/Activity Form/Regional Observations")
    @VersionableCollection(fieldTitle = "Regional Observations")
    protected Set<AmpRegionalObservation> regionalObservations;

    @Interchangeable(fieldTitle = "Line Ministry Observations", importable = true,
            fmPath = "/Activity Form/Line Ministry Observations")
    @VersionableCollection(fieldTitle = "Line Ministry Observations")
    protected Set<AmpLineMinistryObservation> lineMinistryObservations = new HashSet<>();

    //seems obsolete
    @VersionableCollection(fieldTitle = "Costs")
    protected Set costs;
    
    @Interchangeable(fieldTitle = "Program Description", importable = true, fmPath = "/Activity Form/Program/Program Description")
    @VersionableFieldTextEditor(fieldTitle = "Program Description")
    protected String programDescription;

    @Interchangeable(fieldTitle = ActivityFieldsConstants.TEAM, pickIdOnly = true, label = "Created in workspace")
    @VersionableFieldSimple(fieldTitle = "Team")
    protected AmpTeam team;
    //@VersionableCollection(fieldTitle = "Members")
    protected Set member;

    protected String contactName;
    //protected AmpTeamMember updatedBy; !!! Use modifiedBy
    
    @InterchangeableDiscriminator(discriminatorField = "funType",
            configurer = AmpFundingAmountDiscriminationConfigurer.class,
            settings = {
                    @Interchangeable(fieldTitle = "PPC Amount", importable = true, discriminatorOption = "PROPOSED",
                            multipleValues = false,
                            fmPath = "/Activity Form/Funding/Overview Section/Proposed Project Cost"),
                    @Interchangeable(fieldTitle = "RPC Amount", importable = true, discriminatorOption = "REVISED",
                            multipleValues = false,
                            fmPath = "/Activity Form/Funding/Overview Section/Revised Project Cost")
            })
    @VersionableCollection(fieldTitle = "Project Costs")
    private Set<AmpFundingAmount> costAmounts = new HashSet<>();
    
    /**
     * 
     * @deprecated
     */
    @Deprecated
    protected Set referenceDocs;

    // use contFirstName and contLastName instead.
    // The field is defunct

    // Donor contact information
//  @Interchangeable(fieldTitle = "Contact First Name",fmPath="/Activity Form/Contacts/Donor Contact Information/contact first name")
    protected String contFirstName;
//  @Interchangeable(fieldTitle = "Contact Last Name",fmPath="/Activity Form/Contacts/Donor Contact Information/contact lastname")
    protected String contLastName;
//  @Interchangeable(fieldTitle = "Email",fmPath="/Activity Form/Contacts/Donor Contact Information/Add Contact Email/Add Contact Email")
    protected String email;
//  @Interchangeable(fieldTitle = "Donor Contact Title",fmPath="/Activity Form/Contacts/Donor Contact Information/Contact Title")
    protected String dnrCntTitle;
//  @Interchangeable(fieldTitle = "Donor Contact Organization",fmPath="/Activity Form/Contacts/Donor Contact Information/Contact Organizations")
    protected String dnrCntOrganization;
//  @Interchangeable(fieldTitle = "Donor Contact Phone Number",fmPath="/Activity Form/Contacts/Donor Contact Information/Add Contact Phone")
    protected String dnrCntPhoneNumber;
//  @Interchangeable(fieldTitle = "Donor Contact Fax Number",fmPath="/Activity Form/Contacts/Donor Contact Information/Add Contact Fax/Add Contact Fax")
    protected String dnrCntFaxNumber;

    // MOFED contact information
//  @Interchangeable(fieldTitle = "MOFED Contact First Name",fmPath="/Activity Form/Contacts/Mofed Contact Information/contact first name")
    protected String mofedCntFirstName;
//  @Interchangeable(fieldTitle = "MOFED Contact Last Name",fmPath="/Activity Form/Contacts/Mofed Contact Information/contact lastname")
    protected String mofedCntLastName;
//  @Interchangeable(fieldTitle = "MOFED Contact Email",fmPath="/Activity Form/Contacts/Mofed Contact Information/Add Contact Email/Add Contact Email")
    protected String mofedCntEmail;
//  @Interchangeable(fieldTitle = "MOFED Contact Title",fmPath="/Activity Form/Contacts/Mofed Contact Information/Contact Title")
    protected String mfdCntTitle;
//  @Interchangeable(fieldTitle = "MOFED Contact Organization",fmPath="/Activity Form/Contacts/Mofed Contact Information/Contact Organizations")
    protected String mfdCntOrganization;
//  @Interchangeable(fieldTitle = "MOFED Contact Phone Number",fmPath="/Activity Form/Contacts/Mofed Contact Information/Add Contact Phone")
    protected String mfdCntPhoneNumber;
//  @Interchangeable(fieldTitle = "MOFED Contact Fax Number",fmPath="/Activity Form/Contacts/Mofed Contact Information/Add Contact Fax")
    protected String mfdCntFaxNumber;

    // Project Coordinator contact information
//  @Interchangeable(fieldTitle = "Project Coordinator First Name",fmPath="/Activity Form/Contacts/Project Coordinator Contact Information/contact first name")
    protected String prjCoFirstName;
//  @Interchangeable(fieldTitle = "Project Coordinator Last Name",fmPath="/Activity Form/Contacts/Project Coordinator Contact Information/contact lastname")
    protected String prjCoLastName;
//  @Interchangeable(fieldTitle = "Project Coordinator Email",fmPath="/Activity Form/Contacts/Project Coordinator Contact Information/Add Contact Email/Add Contact Email")
    protected String prjCoEmail;
//  @Interchangeable(fieldTitle = "Project Coordinator Title",fmPath="/Activity Form/Contacts/Project Coordinator Contact Information/Contact Title")
    protected String prjCoTitle;
//  @Interchangeable(fieldTitle = "Project Coordinator Organization",fmPath="/Activity Form/Contacts/Project Coordinator Contact Information/Contact Organizations")
    protected String prjCoOrganization;
//  @Interchangeable(fieldTitle = "Project Coordinator Phone Number",fmPath="/Activity Form/Contacts/Project Coordinator Contact Information/Add Contact Phone")
    protected String prjCoPhoneNumber;
//  @Interchangeable(fieldTitle = "Project Coordinator Fax Number",fmPath="/Activity Form/Contacts/Project Coordinator Contact Information/Add Contact Fax")
    protected String prjCoFaxNumber;

    // Sector Ministry contact information
    
//  @Interchangeable(fieldTitle = "Sector Ministry Contact First Name",fmPath="/Activity Form/Contacts/Sector Ministry Contact Information/contact first name")
    protected String secMiCntFirstName;
//  @Interchangeable(fieldTitle = "Sector Ministry Contact Last Name",fmPath="/Activity Form/Contacts/Sector Ministry Contact Information/contact lastname")
    protected String secMiCntLastName;
//  @Interchangeable(fieldTitle = "Sector Ministry Contact Email",fmPath="/Activity Form/Contacts/Sector Ministry Contact Information/Add Contact Email/Add Contact Email")
    protected String secMiCntEmail;
//  @Interchangeable(fieldTitle = "Sector Ministry Contact Title",fmPath="/Activity Form/Contacts/Sector Ministry Contact Information/Contact Title")
    protected String secMiCntTitle;
//  @Interchangeable(fieldTitle = "Sector Ministry Contact Organization",fmPath="/Activity Form/Contacts/Sector Ministry Contact Information/Contact Organizations")
    protected String secMiCntOrganization;
//  @Interchangeable(fieldTitle = "Sector Ministry Contact Phone Number",fmPath="/Activity Form/Contacts/Sector Ministry Contact Information/Add Contact Phone")
    protected String secMiCntPhoneNumber;
//  @Interchangeable(fieldTitle = "Sector Ministry Contact Fax Number",fmPath="/Activity Form/Contacts/Sector Ministry Contact Information/Add Contact Fax")
    protected String secMiCntFaxNumber;
    
    @PrimaryContact(groups = API.class)
    @VersionableCollection(fieldTitle = "Activity Contacts")
    @InterchangeableDiscriminator(discriminatorField = "contactType", settings = {
            @Interchangeable(fieldTitle = ActivityFieldsConstants.DONOR_CONTACT, importable = true, discriminatorOption = Constants.DONOR_CONTACT, 
                            fmPath = "/Activity Form/Contacts/Donor Contact Information",
                            validators = @Validators(unique = "/Activity Form/Contacts/Donor Contact Information")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.PROJECT_COORDINATOR_CONTACT, importable = true, discriminatorOption = Constants.PROJECT_COORDINATOR_CONTACT, 
                            fmPath = "/Activity Form/Contacts/Project Coordinator Contact Information",
                            validators = @Validators(unique = "/Activity Form/Contacts/Project Coordinator Contact Information")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.SECTOR_MINISTRY_CONTACT, importable = true, discriminatorOption = Constants.SECTOR_MINISTRY_CONTACT, 
                            fmPath = "/Activity Form/Contacts/Sector Ministry Contact Information",
                            validators = @Validators(unique = "/Activity Form/Contacts/Sector Ministry Contact Information")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.MOFED_CONTACT, importable = true, discriminatorOption = Constants.MOFED_CONTACT, 
                            fmPath = "/Activity Form/Contacts/Mofed Contact Information",
                            validators = @Validators(unique = "/Activity Form/Contacts/Mofed Contact Information")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.IMPL_EXECUTING_AGENCY_CONTACT, importable = true, 
                            discriminatorOption = Constants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT, 
                            fmPath = "/Activity Form/Contacts/Implementing Executing Agency Contact Information",
                            validators = @Validators(unique = "/Activity Form/Contacts/Implementing Executing Agency Contact Information"))
    })
    protected Set<AmpActivityContact> activityContacts = new HashSet<>();

    @Interchangeable(fieldTitle = "Status Reason", importable = true, fmPath = "/Activity Form/Identification/Status Reason")
    @VersionableFieldTextEditor(fieldTitle = "Status Reason")
    protected String statusReason;
    
    @Interchangeable(fieldTitle = "Components", importable = true, fmPath = "/Activity Form/Components")
    @VersionableCollection(fieldTitle = ActivityFieldsConstants.COMPONENTS)
    protected Set<AmpComponent> components = new HashSet<>();

    @Interchangeable(fieldTitle = "Structures", importable = true, fmPath = "/Activity Form/Structures")
    @VersionableCollection(fieldTitle = "Structures")
    protected Set<AmpStructure> structures = new HashSet<>();

//  @Interchangeable(fieldTitle = "Component Fundings", importable = true, fmPath = "/Activity Form/Components")
//  @VersionableCollection(fieldTitle = "Component Fundings")
//  protected Set<AmpComponentFunding> componentFundings;

    @Interchangeable(fieldTitle = "Proposed Start Date", importable = true,
            fmPath = "/Activity Form/Planning/Proposed Start Date",
            interValidators = @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                    fmPath = "/Activity Form/Planning/Required Validator for Proposed Start Date"))
    @VersionableFieldSimple(fieldTitle = "Proposed Start Date")
    protected Date proposedStartDate;

    @Interchangeable(fieldTitle = "Actual Start Date", importable = true, fmPath = "/Activity Form/Planning/Actual Start Date")
    @VersionableFieldSimple(fieldTitle = "Actual Start Date")
    protected Date actualStartDate;
    
    @Interchangeable(fieldTitle = "Proposed Approval Date", importable = true, fmPath = "/Activity Form/Planning/Proposed Approval Date")
    @VersionableFieldSimple(fieldTitle = "Proposed Approval Date")
    protected Date proposedApprovalDate;
    
    @Interchangeable(fieldTitle = "Actual Approval Date", importable = true, fmPath = "/Activity Form/Planning/Actual Approval Date")
    @VersionableFieldSimple(fieldTitle = "Actual Approval Date")
    protected Date actualApprovalDate;
    
    @Interchangeable(fieldTitle = "Actual Completion Date", importable = true, fmPath = "/Activity Form/Planning/Actual Completion Date")
    @VersionableFieldSimple(fieldTitle = "Actual Completion Date")
    protected Date actualCompletionDate;
    
    @Interchangeable(fieldTitle = "Proposed Completion Date", importable = true, fmPath = "/Activity Form/Planning/Proposed Completion Date")
    @VersionableFieldSimple(fieldTitle = "Proposed Completion Date")
    protected Date proposedCompletionDate;

    @MatchExistingCreator(groups = API.class)
    @Interchangeable(fieldTitle = ActivityFieldsConstants.CREATED_BY, pickIdOnly = true, label = "Activity created by",
            importable = true)
    @VersionableFieldSimple(fieldTitle = ActivityFieldsConstants.CREATED_BY, blockSingleChange = true)
    protected AmpTeamMember activityCreator;
    
    @Interchangeable(fieldTitle = ActivityFieldsConstants.CREATED_DATE, label = "Activity created on")
    @VersionableFieldSimple(fieldTitle = "Creation Date", blockSingleChange = true)
    @TimestampField
    protected Date createdDate;
    
    @Interchangeable(fieldTitle = ActivityFieldsConstants.UPDATE_DATE,
            label = "Activity updated on")
    @VersionableFieldSimple(fieldTitle = "Update Date", blockSingleChange = true)
    @TimestampField
    protected Date updatedDate;

    @Interchangeable(fieldTitle = "Iati Last Update Date", importable = true)
    @VersionableFieldSimple(fieldTitle = "Iati Last Update Date", blockSingleChange = true)
    @TimestampField
    protected Date iatiLastUpdatedDate;

    @AllowedApprover(groups = API.class)
    @Interchangeable(fieldTitle = ActivityFieldsConstants.APPROVED_BY, pickIdOnly = true, importable = true)
    protected AmpTeamMember approvedBy;
    
    @Interchangeable(fieldTitle = ActivityFieldsConstants.APPROVAL_DATE, importable = true)
    @TimestampField
    protected Date approvalDate;

    @InterchangeableDiscriminator(discriminatorField = "transactionType",
            configurer = AmpRegionalFundingDiscriminationConfigurer.class, settings = {
            @Interchangeable(fieldTitle = ArConstants.REGIONAL_COMMITMENTS,
                    discriminatorOption = "" + Constants.COMMITMENT,
                    fmPath = ActivityEPConstants.REGIONAL_COMMITMENTS_FM_PATH,
                    importable = true),
            @Interchangeable(fieldTitle = ArConstants.REGIONAL_DISBURSEMENTS,
                    discriminatorOption = "" + Constants.DISBURSEMENT,
                    fmPath = ActivityEPConstants.REGIONAL_DISBURSEMENTS_FM_PATH,
                    importable = true),
            @Interchangeable(fieldTitle = ArConstants.REGIONAL_EXPENDITURES,
                    discriminatorOption = "" + Constants.EXPENDITURE,
                    fmPath = ActivityEPConstants.REGIONAL_EXPENDITURES_FM_PATH,
                    importable = true)})
    @VersionableCollection(fieldTitle = "Regional Fundings")
    protected Set<AmpRegionalFunding> regionalFundings = new HashSet<>();

    @AllowedApprovalStatus(groups = API.class)
    @Interchangeable(fieldTitle = ActivityFieldsConstants.APPROVAL_STATUS, pickIdOnly = true, importable = true)
    @PossibleValues(ApprovalStatusPossibleValuesProvider.class)
    @VersionableFieldSimple(fieldTitle = "Approval Status", blockSingleChange = true)
    private ApprovalStatus approvalStatus;

    // Aid Harmonization Survey Set
    // @Interchangeable(fieldTitle = "Surveys",fmPath="/Activity Form/Paris Indicators")
     @VersionableCollection(fieldTitle = "Surveys")
    protected Set<AmpAhsurvey> survey;
    
    // @Interchangeable(fieldTitle = "GPI Surveys",fmPath="/Activity Form/GPI")
     @VersionableCollection(fieldTitle = "GPI Surveys")
    protected Set <AmpGPISurvey> gpiSurvey;

    @Interchangeable(fieldTitle = "Line Ministry Rank", importable = true, fmPath = "/Activity Form/Planning/Line Ministry Rank")
    @VersionableFieldSimple(fieldTitle = "Line Ministry Rank")
    protected Integer lineMinRank;

    protected Collection actRankColl;

    @Interchangeable(fieldTitle = "Archived", importable=false)
    @VersionableFieldSimple(fieldTitle = "Archived")
    protected Boolean archived;

    //do we want to export deleted activities? 
    //do we expect to import deleted activities?
    //commenting for now
//  @Interchangeable(fieldTitle = "Deleted", importable=false)
    @VersionableFieldSimple(fieldTitle = "Deleted")
    protected Boolean deleted;

    @VersionableCollection(fieldTitle = "Aid Effectiveness")
    protected Set<AmpAidEffectivenessIndicatorOption> selectedEffectivenessIndicatorOptions;

    /**
     * Indicator connections.
     * This field contains {@link IndicatorActivity} beans which represent activity-indicator connections 
     * and contain set of values for this connection.
     * Please refer to AmpActivity.hbm.xml and IndicatorConnection.hbm.xml for details.
     */
    @Interchangeable(fieldTitle = "Indicators", fmPath = "/Activity Form/M&E", importable = true,
            validators = @Validators(unique = "/Activity Form/M&E/Unique MEs Validator"))
    @VersionableCollection(fieldTitle = "Indicators")
    protected Set<IndicatorActivity> indicators = new HashSet<>();

    @Interchangeable(fieldTitle = "Activity Documents", fmPath = "/Activity Form/Related Documents", importable = true)
    @VersionableCollection(fieldTitle = "Activity Documents")
    protected Set<AmpActivityDocument> activityDocuments = new HashSet<>();
    
    /* Categories */
    @InterchangeableDiscriminator(discriminatorField = "ampCategoryClass.keyName",
    settings = {
        @Interchangeable(fieldTitle = "Activity Status", importable = true, multipleValues = false,
                interValidators = @InterchangeableValidator(RequiredValidator.class),
                discriminatorOption = CategoryConstants.ACTIVITY_STATUS_KEY, fmPath="/Activity Form/Identification/Activity Status", pickIdOnly=true),
        @Interchangeable(fieldTitle = "Type of Cooperation", importable=true,  multipleValues = false,
                /* "/Activity Form/Identification/Type of Cooperation" is used for SSC Type of Cooperation in preview display,
                 * but in AF only the one from "/Activity Form/Funding/Overview Section/Type of Cooperation"
                 * also confirmed via AMP-20899
                 */
                discriminatorOption = CategoryConstants.TYPE_OF_COOPERATION_KEY, fmPath="/Activity Form/Funding/Overview Section/Type of Cooperation", pickIdOnly=true),
        @Interchangeable(fieldTitle = "Type of Implementation", importable=true,  multipleValues = false,
                /* "/Activity Form/Funding/Type of Implementation" is used for SSC Type of Implementation in preview display,
                 * but in AF only the one from "/Activity Form/Funding/Overview Section/Type of Implementation"
                 * also confirmed via AMP-20899
                 */
                discriminatorOption = CategoryConstants.TYPE_OF_IMPLEMENTATION_KEY,  fmPath="/Activity Form/Funding/Overview Section/Type of Implementation", pickIdOnly=true),
        @Interchangeable(fieldTitle = "Modalities", importable=true, multipleValues=false,
                /* "/Activity Form/Funding/Modalities" is used for SSC Modalities in preview display,
                 * but in AF only the one from "/Activity Form/Funding/Overview Section/Type of Cooperation"
                 * also confirmed via AMP-20899
                 */
                discriminatorOption = CategoryConstants.MODALITIES_KEY, fmPath="/Activity Form/Funding/Overview Section/Modalities", pickIdOnly=true),
        @Interchangeable(fieldTitle = "A C Chapter", label = "A.C. Chapter", importable = true, multipleValues = false,
                discriminatorOption = CategoryConstants.ACCHAPTER_KEY, fmPath="/Activity Form/Identification/A.C. Chapter", pickIdOnly=true), 
        @Interchangeable(fieldTitle = "Activity Budget", importable = true, multipleValues = false,
                interValidators = @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                        fmPath = "/Activity Form/Identification/Required Validator for Activity Budget"),
                discriminatorOption = CategoryConstants.ACTIVITY_BUDGET_KEY, fmPath="/Activity Form/Identification/Activity Budget", pickIdOnly=true), 
        @Interchangeable(fieldTitle = "Procurement System", importable=true, multipleValues=false, 
                discriminatorOption = CategoryConstants.PROCUREMENT_SYSTEM_KEY, fmPath="/Activity Form/Identification/Procurement System", pickIdOnly=true),
        @Interchangeable(fieldTitle = "Reporting System", importable=true, multipleValues=false, 
                discriminatorOption = CategoryConstants.REPORTING_SYSTEM_KEY, fmPath="/Activity Form/Identification/Reporting System", pickIdOnly=true), 
        @Interchangeable(fieldTitle = "Audit System", importable=true, multipleValues=false, 
                discriminatorOption = CategoryConstants.AUDIT_SYSTEM_KEY, fmPath="/Activity Form/Identification/Audit System", pickIdOnly=true),
        @Interchangeable(fieldTitle = "Institutions", importable=true, multipleValues=false, 
                discriminatorOption = CategoryConstants.INSTITUTIONS_KEY, fmPath="/Activity Form/Identification/Institutions", pickIdOnly=true),
        @Interchangeable(fieldTitle = "Project Implementing Unit", importable=true, multipleValues=false, 
                discriminatorOption = CategoryConstants.PROJECT_IMPLEMENTING_UNIT_KEY, fmPath="/Activity Form/Identification/Project Implementing Unit", pickIdOnly=true),
        @Interchangeable(fieldTitle = "Accession Instrument", importable=true, multipleValues=false, 
                discriminatorOption = CategoryConstants.ACCESSION_INSTRUMENT_KEY, fmPath="/Activity Form/Identification/Accession Instrument", pickIdOnly=true),
        @Interchangeable(fieldTitle = "Project Category", importable=true, multipleValues=false, 
                discriminatorOption = CategoryConstants.PROJECT_CATEGORY_KEY, fmPath="/Activity Form/Identification/Project Category", pickIdOnly=true),
        @Interchangeable(fieldTitle = "Implementation Level", importable=true, multipleValues=false, 
                discriminatorOption = CategoryConstants.IMPLEMENTATION_LEVEL_KEY, fmPath="/Activity Form/Location/Implementation Level", pickIdOnly=true),
        @Interchangeable(fieldTitle = "Implementation Location", importable = true, multipleValues = false,
                discriminatorOption = CategoryConstants.IMPLEMENTATION_LOCATION_KEY,
                dependencies = {ImplementationLevelValidator.IMPLEMENTATION_LOCATION_VALID_KEY},
                fmPath = "/Activity Form/Location/Implementation Location", pickIdOnly = true),
        @Interchangeable(fieldTitle = "Financial Instrument", importable = true, multipleValues = true,
                discriminatorOption = CategoryConstants.FINANCIAL_INSTRUMENT_KEY, fmPath="/Activity Form/Identification/Financial Instrument", pickIdOnly=true)
    })
    @VersionableCollection(fieldTitle = "Categories")
    protected Set<AmpCategoryValue> categories = new HashSet<>();

    @VersionableFieldTextEditor(fieldTitle = "Status Other Info")
    protected String statusOtherInfo;

    @VersionableFieldTextEditor(fieldTitle = "Project Category Other Info")
    protected String projectCategoryOtherInfo;

    @VersionableFieldTextEditor(fieldTitle = "Modalities Other Info")
    protected String modalitiesOtherInfo;

    /*
     * Tanzania adds
     */
    @Interchangeable(fieldTitle = "Indirect On Budget", importable = true, fmPath = "/Activity Form/Identification/Budget Extras/Indirect On Budget")
    @VersionableFieldSimple(fieldTitle = "Indirect On Budget")
    protected Boolean indirectOnBudget;
    
    @VersionableFieldSimple(fieldTitle = "FY")
    protected String FY;
    
    /*
     * This field is used for API only. The values are stored in database as a string using FY field
     */
    @Interchangeable(fieldTitle = "FY", importable = true, fmPath = "/Activity Form/Identification/Budget Extras/FY",
            dependencyRequiredFMPath = "/Activity Form/Identification/Budget Extras/Required Validator for fy",
            requiredDependencies = OnBudgetValidator.ON_BUDGET_KEY,
            dependencyRequired = SUBMIT,
            uniqueConstraint = true,
            validators = @Validators (unique = "/Activity Form/Identification/Budget Extras/FY"))
    @PossibleValues(FiscalYearPossibleValuesProvider.class)
    protected Set<Long> fiscalYears = new HashSet<>();
    
    @Interchangeable(fieldTitle = "Vote", importable = true,
            fmPath = "/Activity Form/Identification/Budget Extras/Vote",
            requiredDependencies = OnBudgetValidator.ON_BUDGET_KEY,
            dependencyRequired = SUBMIT)
    @VersionableFieldSimple(fieldTitle = "Vote")
    protected String vote;
    
    @Interchangeable(fieldTitle = "Sub Vote", label = "Sub-Vote", importable = true,
            fmPath = "/Activity Form/Identification/Budget Extras/Sub-Vote",
            requiredDependencies = OnBudgetValidator.ON_BUDGET_KEY,
            dependencyRequired = SUBMIT)
    @VersionableFieldSimple(fieldTitle = "Sub Vote")
    protected String subVote;
    
    @Interchangeable(fieldTitle = "Sub Program", label = "Sub-Program", importable = true,
            fmPath = "/Activity Form/Identification/Budget Extras/Sub-Program",
            requiredDependencies = OnBudgetValidator.ON_BUDGET_KEY,
            dependencyRequired = SUBMIT)
    @VersionableFieldSimple(fieldTitle = "Sub Program")
    protected String subProgram;

    @Interchangeable(fieldTitle = ActivityFieldsConstants.PROJECT_CODE, importable = true,
            requiredDependencies = OnBudgetValidator.ON_BUDGET_KEY,
            dependencyRequired = SUBMIT,
            fmPath = FMVisibility.ANY_FM + ActivityEPConstants.DONOR_PROJECT_CODE_FM_PATH
                    + "|" + ActivityEPConstants.BUDGET_EXTRAS_PROJECT_CODE_FM_PATH)
    @VersionableFieldSimple(fieldTitle = "Project Code")
    protected String projectCode;

    @Interchangeable(fieldTitle = "Ministry Code", importable = true,
            fmPath = "/Activity Form/Identification/Budget Extras/Ministry Code",
            requiredDependencies = OnBudgetValidator.ON_BUDGET_KEY,
            dependencyRequired = SUBMIT)
    @VersionableFieldSimple(fieldTitle = "Ministry Code")
    protected String ministryCode;

    
    @Interchangeable(fieldTitle = "CRIS Number", importable = true, fmPath = "/Activity Form/Identification/Cris Number")
    @VersionableFieldSimple(fieldTitle = "CRIS Number")
    protected String crisNumber;

    
    @Interchangeable(fieldTitle = "Government Approval Procedures", importable = true, fmPath = "/Activity Form/Identification/Government Approval Procedures")
    @VersionableFieldSimple(fieldTitle = "Government Approval Procedures")
    protected Boolean governmentApprovalProcedures;

    @Interchangeable(fieldTitle = "Joint Criteria", importable = true, fmPath = "/Activity Form/Identification/Joint Criteria")
    @VersionableFieldSimple(fieldTitle = "Joint Criteria")
    protected Boolean jointCriteria;

    @Interchangeable(fieldTitle = "Multi Stakeholder Partnership", importable = true,
            fmPath = "/Activity Form/Identification/Multi Stakeholder Partnership",
            interValidators = @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                    fmPath = "/Activity Form/Identification/Required Validator for Multi Stakeholder Partnership"))
    @VersionableFieldSimple(fieldTitle = "Multi Stakeholder Partnership")
    protected Boolean multiStakeholderPartnership;

    @Interchangeable(fieldTitle = "Multi Stakeholder Partners", importable = true,
            fmPath = "/Activity Form/Identification/Multi Stakeholder Partners",
            requiredDependencies = MultiStakeholderPartnershipValidator.MULTI_STAKEHOLDER_PARTNERSHIP_KEY,
            dependencyRequired = SUBMIT)
    @VersionableFieldSimple(fieldTitle = "Multi Stakeholder Partners")
    protected String multiStakeholderPartners;

    @Interchangeable(fieldTitle = "Humanitarian Aid", importable = true,
            fmPath = "/Activity Form/Identification/Humanitarian Aid",
            interValidators = @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                    fmPath = "/Activity Form/Identification/Required Validator for Humanitarian Aid"))
    @VersionableFieldSimple(fieldTitle = "Humanitarian Aid")
    protected Boolean humanitarianAid;

    //Can be Primary, Secondary,Tertiary or National Plan Objective
    @VersionableCollection(fieldTitle = "Act. Programs")
    @InterchangeableDiscriminator(discriminatorField = "programSetting.name",
            configurer = AmpActivityProgramDiscriminatorConfigurer.class, settings = {
            @Interchangeable(fieldTitle = ActivityFieldsConstants.NATIONAL_PLAN_OBJECTIVE,
                    discriminatorOption = "National Plan Objective", importable = true,
                    fmPath = "/Activity Form/Program/National Plan Objective",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Program/National Plan Objective/minSizeProgramValidator"),
                            @InterchangeableValidator(
                                    value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/programPercentageTotal"),
                            @InterchangeableValidator(
                                    value = TreeCollectionValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/Tree Validator")},
                    validators = @Validators(
                            unique = "/Activity Form/Program/National Plan Objective/uniqueProgramsValidator")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.PRIMARY_PROGRAMS,
                    discriminatorOption = "Primary Program", importable = true,
                    fmPath = "/Activity Form/Program/Primary Programs",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Program/Primary Programs/minSizeProgramValidator"),
                            @InterchangeableValidator(
                                    value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/programPercentageTotal"),
                            @InterchangeableValidator(
                                    value = TreeCollectionValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/Tree Validator")},
                    validators = @Validators(
                            unique = "/Activity Form/Program/Primary Programs/uniqueProgramsValidator")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.SECONDARY_PROGRAMS,
                    discriminatorOption = "Secondary Program", importable = true,
                    fmPath = "/Activity Form/Program/Secondary Programs",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Program/Secondary Programs/minSizeProgramValidator"),
                            @InterchangeableValidator(
                                    value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/programPercentageTotal"),
                            @InterchangeableValidator(
                                    value = TreeCollectionValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/Tree Validator")},
                    validators = @Validators(
                            unique = "/Activity Form/Program/Secondary Programs/uniqueProgramsValidator")),
            @Interchangeable(fieldTitle = ActivityFieldsConstants.TERTIARY_PROGRAMS,
                    discriminatorOption = "Tertiary Program", importable = true,
                    fmPath = "/Activity Form/Program/Tertiary Programs",
                    interValidators = {
                            @InterchangeableValidator(value = RequiredValidator.class, groups = Submit.class,
                                    fmPath = "/Activity Form/Program/Tertiary Programs/minSizeProgramValidator"),
                            @InterchangeableValidator(
                                    value = TotalPercentageValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/programPercentageTotal"),
                            @InterchangeableValidator(
                                    value = TreeCollectionValidator.class,
                                    fmPath = FMVisibility.PARENT_FM + "/Tree Validator")},
                    validators = @Validators(
                            unique = "/Activity Form/Program/Tertiary Programs/uniqueProgramsValidator"))})
    protected Set<AmpActivityProgram> actPrograms = new HashSet<>();

    @VersionableCollection(fieldTitle = "Act. Budget Structure")
    protected Set <AmpActivityBudgetStructure> actBudgetStructure;

    protected boolean createdAsDraft;

    /**
     * Fields for activity versioning.
     */
    @Interchangeable(fieldTitle = "Activity Group")
    protected AmpActivityGroup ampActivityGroup;
    
    protected Date modifiedDate;

    @Interchangeable(fieldTitle = ActivityFieldsConstants.MODIFIED_BY, pickIdOnly = true,
            label = "Activity last updated by", importable = true)
    @VersionableFieldSimple(fieldTitle = "Modified By")
    protected AmpTeamMember modifiedBy;
    
    protected Boolean mergedActivity;
    protected AmpActivityVersion mergeSource1;
    protected AmpActivityVersion mergeSource2;

    @Interchangeable(fieldTitle = CategoryConstants.FUNDING_SOURCES_NUMBER_NAME, importable = true, fmPath = "/Activity Form/Funding/Overview Section/Total Number of Funding Sources")
    @VersionableFieldSimple(fieldTitle = CategoryConstants.FUNDING_SOURCES_NUMBER_NAME)
    protected Integer fundingSourcesNumber;
    
    @Interchangeable(fieldTitle = "Proposed Project Life", importable = true, fmPath = "/Activity Form/Planning/Proposed Project Life")
    @VersionableFieldSimple(fieldTitle = "Proposed Project Life")
    protected Integer proposedProjectLife;

    /**
     * whether this is a PROJECT or a South-South Cooperation
     */
    @Interchangeable(fieldTitle = ActivityFieldsConstants.ACTIVITY_TYPE, pickIdOnly = true,
            label = "Activity Type", importable = false)
    protected Long activityType = org.dgfoundation.amp.onepager.util.ActivityUtil.ACTIVITY_TYPE_PROJECT; //default type

    @Interchangeable(fieldTitle = "PPC Annual Budgets", importable = true, fmPath = "/Activity Form/Funding/Overview Section/Proposed Project Cost/Annual Proposed Project Cost")
    @VersionableCollection(fieldTitle = "PPC Annual Budgets")

    protected Set<AmpAnnualProjectBudget> annualProjectBudgets = new HashSet<>();

        public Boolean getMergedActivity() {
            return mergedActivity;
        }

        public void setMergedActivity(Boolean mergedActivity) {
            this.mergedActivity = mergedActivity;
        }

        public AmpActivityVersion getMergeSource1() {
            return mergeSource1;
        }

        public void setMergeSource1(AmpActivityVersion mergeSource1) {
            this.mergeSource1 = mergeSource1;
        }

        public AmpActivityVersion getMergeSource2() {
            return mergeSource2;
        }

        public void setMergeSource2(AmpActivityVersion mergeSource2) {
            this.mergeSource2 = mergeSource2;
        }

        public Integer getBudget() {
            return budget;
        }

        public void setBudget(Integer budget) {
            this.budget = budget;
        }

        public boolean isCreatedAsDraft() {
            return createdAsDraft;
        }

        public void setCreatedAsDraft(boolean createdAsDraft) {
            this.createdAsDraft = createdAsDraft;
        }

        public Boolean isGovernmentApprovalProcedures() {
            return governmentApprovalProcedures;
        }

        public void setGovernmentApprovalProcedures(
                Boolean governmentApprovalProcedures) {
            this.governmentApprovalProcedures = governmentApprovalProcedures;
        }

        public Boolean isJointCriteria() {
            return jointCriteria;
        }

        public void setJointCriteria(Boolean jointCriteria) {
            this.jointCriteria = jointCriteria;
        }

        public Set<AmpCategoryValue> getCategories() {
            return categories;
        }

        public void setCategories(Set<AmpCategoryValue> categories) {
            this.categories = categories;
        }

        public Set<AmpActivityDocument> getActivityDocuments() {
            return activityDocuments;
        }

        public void setActivityDocuments(Set<AmpActivityDocument> activityDocuments) {
            this.activityDocuments = activityDocuments;
        }

        public Set getCosts() {
            return costs;
        }

        public void setCosts(Set costs) {
            this.costs = costs;
        }

        /**
         * @return
         */
        public String getAmpId() {
            return ampId;
        }

        /**
         * @return
         */
        public String getDescription() {
            return description;
        }

        /**
         * @return
         */
        public Set<AmpFunding> getFunding() {
            return funding;
        }



        /**
         * @return
         */
        public Set<AmpActivityInternalId> getInternalIds() {
            return internalIds;
        }

        /**
         * @return
         */
        public String getLanguage() {
            return language;
        }

        public Set<AmpActivityLocation> getLocations() {
            return locations;
        }

        /**
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         * @return
         */
        public String getObjective() {
            return objective;
        }

        /**
         * @return
         */
        public Set<AmpOrgRole> getOrgrole() {
            return orgrole;
        }

        /**
         * @return
         */
        public Set <AmpActivitySector> getSectors() {
            return sectors;
        }

        public Set<AmpIssues> getIssues() {
            return issues;
        }

        /**
         * @param string
         */
        public void setAmpId(String string) {
            ampId = string;
        }

        /**
         * @param string
         */
        public void setDescription(String string) {
            description = string;
        }

        /**
         * @param set
         */
        public void setFunding(Set set) {
            funding = set;
        }


        /**
         * @param set
         */
        public void setInternalIds(Set set) {
            internalIds = set;
        }

        public void setIssues(Set set) {
            issues = set;
        }

        /**
         * @param string
         */
        public void setLanguage(String string) {
            language = string;
        }

        /**
         * @param level
         */
        //public void setLevel(AmpLevel level) { // TO BE DELETED
        //this.level = level;
        //}

        /**
         * @param set
         */
        public void setLocations(Set<AmpActivityLocation> set) {
            locations = set;
        }

        /**
         * @param string
         */
        public void setName(String string) {
            name = string;
        }

        /**
         * @param string
         */
        public void setObjective(String string) {
            objective = string;
        }

        /**
         * @param set
         */
        public void setOrgrole(Set<AmpOrgRole> set) {
            orgrole = set;
        }


        /**
         * @param set
         */
        public void setSectors(Set <AmpActivitySector> set) {
            sectors = set;
        }

        /**
         * @param status
         */
        //public void setStatus(AmpStatus status) { // TO BE DELETED
        //this.status = status;
        //}



        /**
         * @return
         */
        public Long getAmpActivityId() {
            return ampActivityId;
        }

        /**
         * @param long1
         */
        public void setAmpActivityId(Long long1) {
            ampActivityId = long1;
        }

        /**
         * @return
         */
        public Set getProgress() {
            return progress;
        }

        /**
         * @return
         */
        @Deprecated
        public Set getDocuments() {
            return documents;
        }

        /**
         * @param progress
         */
        public void setProgress(Set progress) {
            this.progress = progress;
        }

        /**
         * @param documents
         */
        public void setDocuments(Set documents) {
            this.documents = documents;
        }

        public AmpTeam getTeam() {
            return team;
        }

        public void setTeam(AmpTeam team) {
            this.team = team;
        }

        public Set getMember() {
            return member;
        }

        public void setMember(Set member) {
            this.member = member;
        }
        /**
         * @return
         */
        public Date getOriginalCompDate() {
            return originalCompDate;
        }

        /**
         * @param date
         */
        public void setOriginalCompDate(Date date) {
            originalCompDate = date;
        }






        public int compareTo(AmpActivityVersion act) {
            // if (!(o instanceof AmpActivity)) throw new ClassCastException();
            //
            // AmpActivityVersion act = (AmpActivityVersion) o;

            //Added cos this method is comparing by Names, 
            //but many activities have NULL names and running script to correct records only once is temporary solution.
            String myName=(this.name==null)?"":this.name;
            String hisName=(act.getName()==null)?"":act.getName();
            return (myName.trim().toLowerCase().compareTo(hisName.trim().toLowerCase()));

        }

        public Set<AmpActivityContact> getActivityContacts() {
            return activityContacts;
        }

        public void setActivityContacts(Set<AmpActivityContact> activityContacts) {
            this.activityContacts = activityContacts;
        }

        /**
         * @return Returns the statusReason.
         */
        public String getStatusReason() {
            return statusReason;
        }
        /**
         * @param statusReason
         *            The statusReason to set.
         */
        public void setStatusReason(String statusReason) {
            this.statusReason = statusReason;
        }
        /**
         * @return Returns the components.
         */
        public Set<AmpComponent> getComponents() {
            return components;
        }
        /**
         * @param components
         *            The components to set.
         */
        public void setComponents(Set<AmpComponent> components) {
            this.components = components;
        }
        /**
         * @return Returns the structures.
         */
        public Set<AmpStructure> getStructures() {
            return structures;
        }
        /**
         * @param structures
         *            The structures to set.
         */
        public void setStructures(Set<AmpStructure> structures) {
            this.structures = structures;
        }

        /**
         * @return Returns the actualApprovalDate.
         */
        public Date getActualApprovalDate() {
            return actualApprovalDate;
        }
        /**
         * @param actualApprovalDate
         *            The actualApprovalDate to set.
         */
        public void setActualApprovalDate(Date actualApprovalDate) {
            this.actualApprovalDate = actualApprovalDate;
        }
        /**
         * @return Returns the actualCompletionDate.
         */
        public Date getActualCompletionDate() {
            return actualCompletionDate;
        }
        /**
         * @param actualCompletionDate
         *            The actualCompletionDate to set.
         */
        public void setActualCompletionDate(Date actualCompletionDate) {
            this.actualCompletionDate = actualCompletionDate;
        }
        /**
         * @return Returns the actualStartDate.
         */
        public Date getActualStartDate() {
            return actualStartDate;
        }
        /**
         * @param actualStartDate
         *            The actualStartDate to set.
         */
        public void setActualStartDate(Date actualStartDate) {
            this.actualStartDate = actualStartDate;
        }
        /**
         * @return Returns the proposedApprovalDate.
         */
        public Date getProposedApprovalDate() {
            return proposedApprovalDate;
        }
        /**
         * @param proposedApprovalDate
         *            The proposedApprovalDate to set.
         */
        public void setProposedApprovalDate(Date proposedApprovalDate) {
            this.proposedApprovalDate = proposedApprovalDate;
        }
        /**
         * @return Returns the proposedStartDate.
         */
        public Date getProposedStartDate() {
            return proposedStartDate;
        }
        /**
         * @param proposedStartDate
         *            The proposedStartDate to set.
         */
        public void setProposedStartDate(Date proposedStartDate) {
            this.proposedStartDate = proposedStartDate;
        }
        /**
         * @return Returns the createdDate.
         */
        public Date getCreatedDate() {
            return createdDate;
        }
        /**
         * @param createdDate
         *            The createdDate to set.
         */
        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }
        /**
         * @return Returns the updatedDate.
         */
        public Date getUpdatedDate() {
            return updatedDate;
        }
        /**
         * @param updatedDate
         *            The updatedDate to set.
         */
        public void setUpdatedDate(Date updatedDate) {
            this.updatedDate = updatedDate;
        }

        /**
         * @return Returns the contFirstName.
         */
        public String getContFirstName() {
            return contFirstName;
        }

        /**
         * @param contFirstName
         *            The contFirstName to set.
         */
        public void setContFirstName(String contFirstName) {
            this.contFirstName = contFirstName;
        }

        /**
         * @return Returns the contLastName.
         */
        public String getContLastName() {
            return contLastName;
        }

        /**
         * @param contLastName
         *            The contLastName to set.
         */
        public void setContLastName(String contLastName) {
            this.contLastName = contLastName;
        }
        /**
         * @return Returns the programDescription.
         */
        public String getProgramDescription() {
            return programDescription;
        }
        /**
         * @param programDescription
         *            The programDescription to set.
         */
        public void setProgramDescription(String programDescription) {
            this.programDescription = programDescription;
        }

        /**
         * @return Returns the activityCreator.
         */
        public AmpTeamMember getActivityCreator() {
            return activityCreator;
        }

        /**
         * @param activityCreator
         *            The activityCreator to set.
         */
        public void setActivityCreator(AmpTeamMember activityCreator) {
            this.activityCreator = activityCreator;
        }
        /**
         * @return Returns the mofedCntEmail.
         */
        public String getMofedCntEmail() {
            return mofedCntEmail;
        }
        /**
         * @param mofedCntEmail
         *            The mofedCntEmail to set.
         */
        public void setMofedCntEmail(String mofedCntEmail) {
            this.mofedCntEmail = mofedCntEmail;
        }
        /**
         * @return Returns the mofedCntFirstName.
         */
        public String getMofedCntFirstName() {
            return mofedCntFirstName;
        }
        /**
         * @param mofedCntFirstName
         *            The mofedCntFirstName to set.
         */
        public void setMofedCntFirstName(String mofedCntFirstName) {
            this.mofedCntFirstName = mofedCntFirstName;
        }
        /**
         * @return Returns the mofedCntLastName.
         */
        public String getMofedCntLastName() {
            return mofedCntLastName;
        }
        /**
         * @param mofedCntLastName
         *            The mofedCntLastName to set.
         */
        public void setMofedCntLastName(String mofedCntLastName) {
            this.mofedCntLastName = mofedCntLastName;
        }

        // Commented by Mikheil - in general, Hibernate classes do not need to
        // overrride
        //this method, because it may lead to incorrect functinoality
        /*
         * public boolean equals(Object obj) { if (obj == null) throw new
         * NullPointerException();
         *
         * if (!(obj instanceof AmpActivity)) throw new ClassCastException();
         *
         * AmpActivityVersion act = (AmpActivityVersion) obj; return
         * this.ampActivityId.equals(act.getAmpActivityId()); }
         */

        /**
         * @return Returns the regionalFundings.
         */
        public Set<AmpRegionalFunding> getRegionalFundings() {
            return regionalFundings;
        }

        /**
         * @param regionalFundings
         *            The regionalFundings to set.
         */
        public void setRegionalFundings(Set<AmpRegionalFunding> regionalFundings) {
            this.regionalFundings = regionalFundings;
        }



        /**
         * @return Returns the approvalStatus.
         */
        public ApprovalStatus getApprovalStatus() {
            return approvalStatus;
        }
        /**
         * @param approvalStatus
         *            The approvalStatus to set.
         */
        public void setApprovalStatus(ApprovalStatus approvalStatus) {
            this.approvalStatus = approvalStatus;
        }

        /**
         * @return Returns the survey.
         */
        public Set<AmpAhsurvey> getSurvey() {
            return survey;
        }

        public String getDocumentSpace() {
            return documentSpace;
        }

        /**
         * @param survey
         *            The survey to set.
         */
        public void setSurvey(Set<AmpAhsurvey> survey) {
            this.survey = survey;
        }

        public void setDocumentSpace(String documentSpace) {
            this.documentSpace = documentSpace;
        }

        public Integer getLineMinRank() {
            return lineMinRank;
        }

        public void setLineMinRank(Integer lineMinRank) {
            this.lineMinRank = lineMinRank;
        }

        public Collection getActRankColl() {
            return actRankColl;
        }

        public Date getProposedCompletionDate() {
            return proposedCompletionDate;
        }

        public void setActRankColl(Collection actRankColl) {
            this.actRankColl = actRankColl;
        }

        public void setProposedCompletionDate(Date proposedCompletionDate) {
            this.proposedCompletionDate = proposedCompletionDate;
        }

        public Date getContractingDate() {
            return contractingDate;
        }
        public void setContractingDate(Date contractingDate) {
            this.contractingDate = contractingDate;
        }

        public Date getDisbursmentsDate() {
            return disbursmentsDate;
        }

        public void setDisbursmentsDate(Date disbursmentsDate) {
            this.disbursmentsDate = disbursmentsDate;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getResults() {
            return results;
        }

        public void setResults(String results) {
            this.results = results;
        }

        public String getDnrCntFaxNumber() {
            return dnrCntFaxNumber;
        }

        public void setDnrCntFaxNumber(String dnrCntFaxNumber) {
            this.dnrCntFaxNumber = dnrCntFaxNumber;
        }

        public String getDnrCntOrganization() {
            return dnrCntOrganization;
        }

        public void setDnrCntOrganization(String dnrCntOrganization) {
            this.dnrCntOrganization = dnrCntOrganization;
        }

        public String getDnrCntPhoneNumber() {
            return dnrCntPhoneNumber;
        }

        public void setDnrCntPhoneNumber(String dnrCntPhoneNumber) {
            this.dnrCntPhoneNumber = dnrCntPhoneNumber;
        }

        public String getDnrCntTitle() {
            return dnrCntTitle;
        }

        public void setDnrCntTitle(String dnrCntTitle) {
            this.dnrCntTitle = dnrCntTitle;
        }

        public String getMfdCntFaxNumber() {
            return mfdCntFaxNumber;
        }

        public void setMfdCntFaxNumber(String mfdCntFaxNumber) {
            this.mfdCntFaxNumber = mfdCntFaxNumber;
        }

        public String getMfdCntOrganization() {
            return mfdCntOrganization;
        }

        public void setMfdCntOrganization(String mfdCntOrganization) {
            this.mfdCntOrganization = mfdCntOrganization;
        }

        public String getMfdCntPhoneNumber() {
            return mfdCntPhoneNumber;
        }

        public void setMfdCntPhoneNumber(String mfdCntPhoneNumber) {
            this.mfdCntPhoneNumber = mfdCntPhoneNumber;
        }

        public String getMfdCntTitle() {
            return mfdCntTitle;
        }

        public void setMfdCntTitle(String mfdCntTitle) {
            this.mfdCntTitle = mfdCntTitle;
        }

        public Object getObjectType() {
            return this.getClass().getName();
        }

        public Object getIdentifier() {
            return this.getAmpActivityId();
        }

        public String getObjectName() {
            return this.getAmpId()+" "+this.getName();
        }
            @Override
            public String getObjectFilteredName() {
            return DbUtil.filter(getObjectName());
        }

        public String getFY() {
            return FY;
        }

        public void setFY(String fy) {
            FY = fy;
        }

        public String getProjectCode() {
            return projectCode;
        }

        public void setProjectCode(String projectCode) {
            this.projectCode = projectCode;
        }

        public String getSubProgram() {
            return subProgram;
        }

        public void setSubProgram(String subProgram) {
            this.subProgram = subProgram;
        }

        public String getSubVote() {
            return subVote;
        }

        public void setSubVote(String subVote) {
            this.subVote = subVote;
        }

        public String getVote() {
            return vote;
        }

        public void setVote(String vote) {
            this.vote = vote;
        }

        public String getMinistryCode() {
            return ministryCode;
        }

        public void setMinistryCode(String ministryCode) {
            this.ministryCode = ministryCode;
        }

        public String getLessonsLearned() {
            return lessonsLearned;
        }

        public void setLessonsLearned(String lessonsLearned) {
            this.lessonsLearned = lessonsLearned;
        }

        public String getEnvironment() {
            return environment;
        }

        public void setEnvironment(String environment) {
            this.environment = environment;
        }

        public String getEqualOpportunity() {
            return equalOpportunity;
        }

        public void setEqualOpportunity(String equalOpportunity) {
            this.equalOpportunity = equalOpportunity;
        }

        public String getMinorities() {
            return minorities;
        }

        public void setMinorities(String minorities) {
            this.minorities = minorities;
        }

        @Override
        public String[] getImplementedActions() {
            return IMPLEMENTED_ACTIONS;
        }

        @Override
        public Class getPermissibleCategory() {
            return AmpActivityVersion.class;
        }

        public Set getReferenceDocs() {
            return referenceDocs;
        }

        public Boolean getDraft() {
            return draft;
        }

        public Set<AmpActivityProgram> getActPrograms() {
            return actPrograms;
        }
        
        public Set<AmpActivityBudgetStructure> getActBudgetStructure(){
            return actBudgetStructure;
        }
        
        public void setActBudgetStructure(Set<AmpActivityBudgetStructure> actBudgetStructure){
            this.actBudgetStructure = actBudgetStructure;
        }

        public void setReferenceDocs(Set referenceDocs) {
            this.referenceDocs = referenceDocs;
        }

        public void setDraft(Boolean draft) {
            this.draft = draft;
        }

        public String getChangeType() {
            return changeType;
        }

        public void setChangeType(String changeType) {
            this.changeType = changeType;
        }

        public Date getLastImportedAt() {
            return lastImportedAt;
        }

        public void setLastImportedAt(Date lastImportedAt) {
            this.lastImportedAt = lastImportedAt;
        }

        public User getLastImportedBy() {
            return lastImportedBy;
        }

        public void setLastImportedBy(User lastImportedBy) {
            this.lastImportedBy = lastImportedBy;
        }

        public void setActPrograms(Set<AmpActivityProgram> actPrograms) {
            this.actPrograms = actPrograms;
        }

        public Boolean getGovernmentApprovalProcedures() {
            return governmentApprovalProcedures;
        }

        public Boolean getMultiStakeholderPartnership() {
            return multiStakeholderPartnership;
        }

        public void setMultiStakeholderPartnership(Boolean multiStakeholderPartnership) {
            this.multiStakeholderPartnership = multiStakeholderPartnership;
        }

        public String getMultiStakeholderPartners() {
            return multiStakeholderPartners;
        }

        public void setMultiStakeholderPartners(String multiStakeholderPartners) {
            this.multiStakeholderPartners = multiStakeholderPartners;
        }

        public Boolean getJointCriteria() {
            return jointCriteria;
        }

        public Set<IndicatorActivity> getIndicators() {
            return indicators;
        }

        public void setIndicators(Set<IndicatorActivity> indicators) {
            this.indicators = indicators;
        }

        public String getGovAgreementNumber() {
            return govAgreementNumber;
        }

        public void setGovAgreementNumber(String govAgreementNumber) {
            this.govAgreementNumber = govAgreementNumber;
        }


        public String getProjectImpact() {
            return projectImpact;
        }

        public void setProjectImpact(final String projectImpact) {
            this.projectImpact = projectImpact;
        }

    public String getStatusOtherInfo() {
        return statusOtherInfo;
    }

    public void setStatusOtherInfo(final String statusOtherInfo) {
        this.statusOtherInfo = statusOtherInfo;
    }

    public String getProjectCategoryOtherInfo() {
        return projectCategoryOtherInfo;
    }

    public void setProjectCategoryOtherInfo(final String projectCategoryOtherInfo) {
        this.projectCategoryOtherInfo = projectCategoryOtherInfo;
    }

    public String getModalitiesOtherInfo() {
        return modalitiesOtherInfo;
    }

    public void setModalitiesOtherInfo(final String modalitiesOtherInfo) {
        this.modalitiesOtherInfo = modalitiesOtherInfo;
    }

    public String getActivitySummary() {
            return activitySummary;
        }

        public void setActivitySummary(String activitySummary) {
            this.activitySummary = activitySummary;
        }


        public String getConditionality() {
            return conditionality;
        }

        public void setConditionality(String conditionality) {
            this.conditionality = conditionality;
        }

        public String getProjectManagement() {
            return projectManagement;
        }

        public void setProjectManagement(String projectManagement) {
            this.projectManagement = projectManagement;
        }
        public String getPrjCoFirstName() {
            return prjCoFirstName;
        }

        public void setPrjCoFirstName(String prjCoFirstName) {
            this.prjCoFirstName = prjCoFirstName;
        }

        public String getPrjCoLastName() {
            return prjCoLastName;
        }

        public void setPrjCoLastName(String prjCoLastName) {
            this.prjCoLastName = prjCoLastName;
        }

        public String getPrjCoEmail() {
            return prjCoEmail;
        }

        public void setPrjCoEmail(String prjCoEmail) {
            this.prjCoEmail = prjCoEmail;
        }

        public String getPrjCoTitle() {
            return prjCoTitle;
        }

        public void setPrjCoTitle(String prjCoTitle) {
            this.prjCoTitle = prjCoTitle;
        }

        public String getPrjCoOrganization() {
            return prjCoOrganization;
        }

        public void setPrjCoOrganization(String prjCoOrganization) {
            this.prjCoOrganization = prjCoOrganization;
        }

        public String getPrjCoPhoneNumber() {
            return prjCoPhoneNumber;
        }

        public void setPrjCoPhoneNumber(String prjCoPhoneNumber) {
            this.prjCoPhoneNumber = prjCoPhoneNumber;
        }

        public String getPrjCoFaxNumber() {
            return prjCoFaxNumber;
        }

        public void setPrjCoFaxNumber(String prjCoFaxNumber) {
            this.prjCoFaxNumber = prjCoFaxNumber;
        }

        public String getSecMiCntFirstName() {
            return secMiCntFirstName;
        }

        public void setSecMiCntFirstName(String secMiCntFirstName) {
            this.secMiCntFirstName = secMiCntFirstName;
        }

        public String getSecMiCntLastName() {
            return secMiCntLastName;
        }

        public void setSecMiCntLastName(String secMiCntLastName) {
            this.secMiCntLastName = secMiCntLastName;
        }

        public String getSecMiCntEmail() {
            return secMiCntEmail;
        }

        public void setSecMiCntEmail(String secMiCntEmail) {
            this.secMiCntEmail = secMiCntEmail;
        }

        public String getSecMiCntTitle() {
            return secMiCntTitle;
        }

        public void setSecMiCntTitle(String secMiCntTitle) {
            this.secMiCntTitle = secMiCntTitle;
        }

        public String getSecMiCntOrganization() {
            return secMiCntOrganization;
        }

        public void setSecMiCntOrganization(String secMiCntOrganization) {
            this.secMiCntOrganization = secMiCntOrganization;
        }

        public String getSecMiCntPhoneNumber() {
            return secMiCntPhoneNumber;
        }

        public void setSecMiCntPhoneNumber(String secMiCntPhoneNumber) {
            this.secMiCntPhoneNumber = secMiCntPhoneNumber;
        }

        public String getSecMiCntFaxNumber() {
            return secMiCntFaxNumber;
        }

        public void setSecMiCntFaxNumber(String secMiCntFaxNumber) {
            this.secMiCntFaxNumber = secMiCntFaxNumber;
        }



        public String toString(){
            if(name!=null) return name;
            return super.toString();
        }


        public Boolean isHumanitarianAid( ) {
            return this.humanitarianAid ;
        }

        public void setHumanitarianAid(Boolean humanitarianAid) {
            this.humanitarianAid = humanitarianAid;
        }
        
        public AmpTeamMember getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(AmpTeamMember approvedBy) {
            this.approvedBy = approvedBy;
        }

        public Date getApprovalDate() {
            return approvalDate;
        }

        public void setApprovalDate(Date approvalDate) {
            this.approvalDate = approvalDate;
        }

        public String getCrisNumber() {
            return crisNumber;
        }

        public void setCrisNumber(String crisNumber) {
            this.crisNumber = crisNumber;
        }

        public Set<IPAContract> getContracts() {
            return contracts;
        }

        public void setContracts(Set<IPAContract> contracts) {
            this.contracts = contracts;
        }


        public String getDonors() {
            StringBuilder donors = new StringBuilder();
            if (this.getAmpActivityId() != null){
                Session session = PersistenceManager.getSession();
                String queryString = "select distinct donor from " + AmpFunding.class.getName() + " f inner join f.ampDonorOrgId donor inner join f.ampActivityId act ";
                queryString += " where act.ampActivityId=:activityId";
                Query qry = session.createQuery(queryString).setLong("activityId", this.getAmpActivityId());

                List<AmpOrganisation> organizations = qry.list();
                if (organizations != null && organizations.size() > 1) {
                    Collections.sort(organizations, new Comparator<AmpOrganisation>() {
                        public int compare(AmpOrganisation o1, AmpOrganisation o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                }

                for (AmpOrganisation donor : organizations) {
                    donors.append(donor.getName());
                    donors.append(", ");
                }

                if (donors.length() > 1) {
                    // remove last comma
                    donors.setLength(donors.length() - 2);
                }
            }
            return donors.toString();
        }

        public String getBudgetCodeProjectID() {
            return budgetCodeProjectID;
        }

        public void setBudgetCodeProjectID(String budgetCodeProjectID) {
            this.budgetCodeProjectID = budgetCodeProjectID;
        }

        public String getProjectComments() {
            return this.projectComments;
        }

        public void setProjectComments(String projectComments) {
            this.projectComments = projectComments;
        }

//      public Set<AmpComponentFunding> getComponentFundings() {
//          Set<AmpComponentFunding> res = new HashSet<>();
//          for(AmpComponent ampComponent:this.getComponents()) {
//              res.addAll(ampComponent.getFundings());
//          }
//          return res;
//      }
        
        public AmpActivityGroup getAmpActivityGroup() {
            return ampActivityGroup;
        }

        public void setAmpActivityGroup(AmpActivityGroup ampActivityGroup) {
            this.ampActivityGroup = ampActivityGroup;
        }

        public Date getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(Date modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public AmpTeamMember getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(AmpTeamMember modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        /**
         * @return the archived
         */
        public Boolean getArchived() {
            return archived;
        }

        /**
         * @param archived the archived to set
         */
        public void setArchived(Boolean archived) {
            this.archived = archived;
        }

        public static String[] getIMPLEMENTED_ACTIONS() {
            return IMPLEMENTED_ACTIONS;
        }

        public static void setIMPLEMENTED_ACTIONS(String[] implemented_actions) {
            IMPLEMENTED_ACTIONS = implemented_actions;
        }

        public Long getBudgetsector() {
            return budgetsector;
        }

        public void setBudgetsector(Long budgetsector) {
            this.budgetsector = budgetsector;
        }

        public Long getBudgetorganization() {
            return budgetorganization;
        }

        public void setBudgetorganization(Long budgetorganization) {
            this.budgetorganization = budgetorganization;
        }

        public Long getBudgetdepartment() {
            return budgetdepartment;
        }

        public void setBudgetdepartment(Long budgetdepartment) {
            this.budgetdepartment = budgetdepartment;
        }

        public Long getBudgetprogram() {
            return budgetprogram;
        }

        public void setBudgetprogram(Long budgetprogram) {
            this.budgetprogram = budgetprogram;
        }

        public Set<AmpRegionalObservation> getRegionalObservations() {
            return regionalObservations;
        }

        public void setRegionalObservations(Set<AmpRegionalObservation> regionalObservations) {
            this.regionalObservations = regionalObservations;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }
        
        public Set<AmpLineMinistryObservation> getLineMinistryObservations() {
            return lineMinistryObservations;
        }

        public void setLineMinistryObservations(
                Set<AmpLineMinistryObservation> lineMinistryObservations) {
            this.lineMinistryObservations = lineMinistryObservations;
        }

        public Boolean getIndirectOnBudget() {
            return indirectOnBudget;
        }

        public void setIndirectOnBudget(Boolean indirectOnBudget) {
            this.indirectOnBudget = indirectOnBudget;
        }

        public Long getActivityType() {
            return activityType;
        }

        public void setActivityType(Long activityType) {
            this.activityType = activityType;
        }

        public Integer getFundingSourcesNumber() {
            return fundingSourcesNumber;
        }

        public void setFundingSourcesNumber(Integer fundingSourcesNumber) {
            this.fundingSourcesNumber = fundingSourcesNumber;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            try {
                return (AmpActivityVersion) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new InternalError(e.toString());
            }
        }

        public Set <AmpGPISurvey> getGpiSurvey() {
            return gpiSurvey;
        }

        public void setGpiSurvey(Set <AmpGPISurvey> gpiSurvey) {
            this.gpiSurvey = gpiSurvey;
        }
        
        public Integer getProposedProjectLife() {
            return proposedProjectLife;
        }

        public void setProposedProjectLife(Integer proposedProjectLife) {
            this.proposedProjectLife = proposedProjectLife;
        }

        public Set<AmpAnnualProjectBudget> getAnnualProjectBudgets() {
            return annualProjectBudgets;
        }

        public void setAnnualProjectBudgets(
                Set<AmpAnnualProjectBudget> annualProjectBudgets) {
            this.annualProjectBudgets = annualProjectBudgets;
        }
        public Date getIatiLastUpdatedDate() {
            return iatiLastUpdatedDate;
        }

        public void setIatiLastUpdatedDate(Date iatiLastUpdatedDate) {
            this.iatiLastUpdatedDate = iatiLastUpdatedDate;
        }

        public Boolean getDeleted() {
            return deleted;
        }

        public void setDeleted(Boolean deleted) {
            this.deleted = deleted;
        }
        
        public Set<Long> getFiscalYears() {
            return fiscalYears;
        }

        public void setFiscalYears(Set<Long> fiscalYears) {
            this.fiscalYears = fiscalYears;
        }

        public Set<AmpAidEffectivenessIndicatorOption> getSelectedEffectivenessIndicatorOptions() {
            return selectedEffectivenessIndicatorOptions;
        }

        public void setSelectedEffectivenessIndicatorOptions(Set<AmpAidEffectivenessIndicatorOption> selectedEffectivenessIndicatorOptions) {
            this.selectedEffectivenessIndicatorOptions = selectedEffectivenessIndicatorOptions;
        }
        
        public AmpFundingAmount getProjectCostByType(AmpFundingAmount.FundingType type) {
            if (this.costAmounts != null && type != null) {
                for (AmpFundingAmount fa : costAmounts) {
                    if (type.equals(fa.getFunType())) {
                        return fa;
                    }
                }
            }
            return null;
        }

        /**
         * @return the costAmounts
         */
        public Set<AmpFundingAmount> getCostAmounts() {
            return costAmounts;
        }

        /**
         * @param costAmounts the costAmounts to set
         */
        public void setCostAmounts(Set<AmpFundingAmount> costAmounts) {
            this.costAmounts = costAmounts;
        }
    
    public String getIatiIdentifier() {
        return iatiIdentifier;
    }
    
    public void setIatiIdentifier(String iatiIdentifier) {
        this.iatiIdentifier = iatiIdentifier;
    }
    
}

