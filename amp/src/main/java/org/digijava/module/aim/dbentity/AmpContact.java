package org.digijava.module.aim.dbentity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.contact.ContactEPConstants;
import org.digijava.kernel.ampapi.endpoints.contact.ContactFieldsConstants;
import org.digijava.kernel.ampapi.endpoints.contact.dto.ContactView;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.kernel.validators.common.SizeValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.Output;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.io.Serializable;
import java.util.*;

/**
 * holds contact user's information
 * @author Dare
 *
 */
@TranslatableClass (displayName = "Contact")
public class AmpContact implements Comparable, Serializable, Cloneable, Versionable, Identifiable {

    @Interchangeable(fieldTitle = "ID")
    @JsonProperty(ContactEPConstants.ID)
    @JsonView(ContactView.Summary.class)
    private Long id;

    @Interchangeable(fieldTitle = "Name", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    @JsonProperty(ContactEPConstants.NAME)
    @JsonView(ContactView.Summary.class)
    private String name;

    @Interchangeable(fieldTitle = "Last Name", importable = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class))
    @JsonProperty(ContactEPConstants.LAST_NAME)
    @JsonView(ContactView.Summary.class)
    private String lastname;

    @Interchangeable(fieldTitle = "Title", importable = true, pickIdOnly = true,
        discriminatorOption = CategoryConstants.CONTACT_TITLE_KEY)
    @JsonIgnore
    private AmpCategoryValue title;

    @TranslatableField
    @Interchangeable(fieldTitle = "Organization Name", importable = true)
    @JsonIgnore
    private String organisationName;

    @TranslatableField
    @Interchangeable(fieldTitle = "Function", importable = true)
    @JsonIgnore
    private String function;

    @Interchangeable(fieldTitle = "Office Address", importable = true)
    @JsonIgnore
    private String officeaddress;

    // do we need it?
    @JsonIgnore
    private String temporaryId;

    @JsonIgnore
    private String nameAndLastName;

    @JsonIgnore
    private String fullname;

    /**
     * currently these fields are not usable, but will become when we decide
     * to link contact list to calendar and messaging
     */
    @JsonIgnore
    private Boolean shared; //is contact shared between amp users

    @Interchangeable(fieldTitle = ContactFieldsConstants.CREATED_BY, pickIdOnly = true)
    @JsonIgnore
    private AmpTeamMember creator; //who created the contact

    @JsonIgnore
    private SortedSet<AmpActivityContact> activityContacts;

    @Interchangeable(fieldTitle = "Organisation Contacts", importable = true,
            validators = @Validators(unique = FMVisibility.ALWAYS_VISIBLE_FM))
    @JsonIgnore
    private Set<AmpOrganisationContact> organizationContacts = new HashSet<>();

    @InterchangeableDiscriminator(discriminatorField = "name", settings = {
            @Interchangeable(fieldTitle = ContactEPConstants.EMAIL,
                    discriminatorOption = Constants.CONTACT_PROPERTY_NAME_EMAIL,
                    interValidators = @InterchangeableValidator(
                            value = SizeValidator.class,
                            attributes = "max=" + ContactEPConstants.CONTACT_PROPERTY_MAX_SIZE),
                    importable = true,
                    type = AmpContactEmailProperty.class),
            @Interchangeable(fieldTitle = ContactEPConstants.PHONE,
                    discriminatorOption = Constants.CONTACT_PROPERTY_NAME_PHONE,
                    interValidators = @InterchangeableValidator(
                            value = SizeValidator.class,
                            attributes = "max=" + ContactEPConstants.CONTACT_PROPERTY_MAX_SIZE),
                    importable = true,
                    type = AmpContactPhoneProperty.class),
            @Interchangeable(fieldTitle = ContactEPConstants.FAX,
                    discriminatorOption = Constants.CONTACT_PROPERTY_NAME_FAX,
                    interValidators = @InterchangeableValidator(
                            value = SizeValidator.class,
                            attributes = "max=" + ContactEPConstants.CONTACT_PROPERTY_MAX_SIZE),
                    importable = true,
                    type = AmpContactFaxProperty.class)})
    @JsonIgnore
    private SortedSet<AmpContactProperty> properties = new TreeSet<>();

    public AmpContact(){

    }

    public AmpContact(String name, String lastName){
        this.name=name;
        this.lastname=lastName;
    }

    public Long getId() {
        return id;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public AmpCategoryValue getTitle() {
        return title;
    }
    public void setTitle(AmpCategoryValue title) {
        this.title = title;
    }
    public String getOrganisationName() {
        return organisationName;
    }
    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }
    public Boolean getShared() {
        return shared;
    }
    public void setShared(Boolean shared) {
        this.shared = shared;
    }
    public AmpTeamMember getCreator() {
        return creator;
    }
    public void setCreator(AmpTeamMember creator) {
        this.creator = creator;
    }

    public SortedSet<AmpActivityContact> getActivityContacts() {
        return activityContacts;
    }
    public void setActivityContacts(SortedSet<AmpActivityContact> activityContacts) {
        this.activityContacts = activityContacts;
    }
    public String getTemporaryId() {
        return temporaryId;
    }
    public void setTemporaryId(String temporaryId) {
        this.temporaryId = temporaryId;
    }

    public String getFunction() {
        return function;
    }
    public void setFunction(String function) {
        this.function = function;
    }
    public String getOfficeaddress() {
        return officeaddress;
    }
    public void setOfficeaddress(String officeaddress) {
        this.officeaddress = officeaddress;
    }
    public SortedSet<AmpContactProperty> getProperties() {
        return properties;
    }
    public void setProperties(SortedSet<AmpContactProperty> properties) {
        this.properties = properties;
    }

    public Set<AmpOrganisationContact> getOrganizationContacts() {
        return organizationContacts;
    }

    public void setOrganizationContacts(Set<AmpOrganisationContact> organizationContacts) {
        this.organizationContacts = organizationContacts;
    }

    @Override
    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        AmpContact a = (AmpContact) arg0;
        if (this.getId() != null && a.getId() != null) {
            return this.getId().compareTo(a.getId());
        } else {
            if(this.getTemporaryId()!=null&&a.getTemporaryId()!=null){
                return this.getTemporaryId().compareTo(a.getTemporaryId());
            }
            return 1;
        }
    }

    public String getNameAndLastName() {
        nameAndLastName = (name != null) ? name + " " + lastname : "-";
        return nameAndLastName;
    }

    public void setNameAndLastName(String nameAndLastName) {
        this.nameAndLastName = nameAndLastName;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @JsonIgnore
    public List<String> getEmails (){
        List<String> emails =null;
        if (this.properties!= null ) {
            for (AmpContactProperty prop : this.properties) {
                if (prop.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL)){
                    if(emails ==null){
                        emails= new ArrayList<String>();
                    }
                    emails.add(prop.getValue());
                }

            }
        }
        return emails;
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpContact aux = (AmpContact) obj;
        String original = "" + this.getId();
        String copy = "" + aux.getId();
        if (original.equals(copy)) {
            return true;
        }
        return false;
    }

    @Override
    @JsonIgnore
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(
                new Output(null, new String[] { "Contact" }, new Object[] { this.getLastname() + " "
                        + this.getName() }));
        return out;
    }

    @Override
    @JsonIgnore
    public Object getValue() {
        return "" + this.name + "-" + this.lastname;
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpContact aux = (AmpContact) clone();
        aux.id = null;
        if(this.getActivityContacts()==null){
            this.setActivityContacts(new TreeSet<AmpActivityContact>());
        }
        return aux;
    }

    @Override
    @JsonIgnore
    public Object getIdentifier() {
        return id;
    }

}
