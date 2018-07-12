package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.contact.ContactEPConstants;
import org.digijava.kernel.ampapi.endpoints.contact.ContactFieldsConstants;
import org.digijava.kernel.ampapi.endpoints.contact.ContactTitlePossibleValuesProvider;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.Output;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * holds contact user's information
 * @author Dare
 *
 */
@TranslatableClass (displayName = "Contact")
public class AmpContact implements Comparable, Serializable, Cloneable, Versionable, Identifiable {
    
    @Interchangeable(fieldTitle = "ID", id = true)
    private Long id;
    
    @Interchangeable(fieldTitle = "Name", importable = true, required = ActivityEPConstants.REQUIRED_ALWAYS)
    private String name;
    
    @Interchangeable(fieldTitle = "Last Name", importable = true, required = ActivityEPConstants.REQUIRED_ALWAYS)
    private String lastname;

    @PossibleValues(ContactTitlePossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Title", importable = true, pickIdOnly = true)
    private AmpCategoryValue title;

    @TranslatableField
    @Interchangeable(fieldTitle = "Organization Name", importable = true)
    private String organisationName;
    
    @TranslatableField
    @Interchangeable(fieldTitle = "Function", importable = true)
    private String function;
    
    @Interchangeable(fieldTitle = "Office Address", importable = true)
    private String officeaddress;
    
    // do we need it?
    private String temporaryId;
    
    private String nameAndLastName;
    
    private String fullname;
    
    /**
     * currently these fields are not usable, but will become when we decide 
     * to link contact list to calendar and messaging 
     */
    private Boolean shared; //is contact shared between amp users

    @Interchangeable(fieldTitle = ContactFieldsConstants.CREATED_BY, pickIdOnly = true)
    private AmpTeamMember creator; //who created the contact
    
    private SortedSet<AmpActivityContact> activityContacts;
    
    @Interchangeable(fieldTitle = "Organisation Contacts", importable = true, 
            validators = @Validators(unique = FMVisibility.ALWAYS_VISIBLE_FM))
    private Set<AmpOrganisationContact> organizationContacts;

    @Interchangeable(fieldTitle = "Properties")
    @InterchangeableDiscriminator(discriminatorField = "name", settings = {
            @Interchangeable(fieldTitle = ContactEPConstants.EMAIL, 
                    discriminatorOption = Constants.CONTACT_PROPERTY_NAME_EMAIL,
                    sizeLimit = ContactEPConstants.CONTACT_PROPERTY_MAX_SIZE, importable = true),
            @Interchangeable(fieldTitle = ContactEPConstants.PHONE, 
                    discriminatorOption = Constants.CONTACT_PROPERTY_NAME_PHONE,
                    sizeLimit = ContactEPConstants.CONTACT_PROPERTY_MAX_SIZE, importable = true),
            @Interchangeable(fieldTitle = ContactEPConstants.FAX, 
                    discriminatorOption = Constants.CONTACT_PROPERTY_NAME_FAX,
                    sizeLimit = ContactEPConstants.CONTACT_PROPERTY_MAX_SIZE, importable = true)
    })
    private SortedSet<AmpContactProperty> properties;

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
        nameAndLastName = (name != null) ? name + " " + lastname : name;
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
    public Output getOutput() {
        Output out = new Output();
        out.setOutputs(new ArrayList<Output>());
        out.getOutputs().add(
                new Output(null, new String[] { "Contact" }, new Object[] { this.getLastname() + " "
                        + this.getName() }));
        return out;
    }

    @Override
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
    public Object getIdentifier() {
        return id;
    }

}
