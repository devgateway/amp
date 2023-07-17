package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

import org.digijava.kernel.ampapi.endpoints.common.CommonFieldsConstants;
import org.digijava.kernel.ampapi.endpoints.contact.values.providers.ContactPossibleValuesProvider;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableBackReference;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;
import org.digijava.module.aim.annotations.interchange.PossibleValues;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.util.Output;
import javax.persistence.*;

@Entity
@Table(name = "AMP_ACTIVITY_CONTACT")
@TranslatableClass (displayName="ActivityContact")
public class AmpActivityContact implements Versionable, Comparable, Serializable, Cloneable {
    @Id
    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ACTIVITY_CONTACT_seq")
    @SequenceGenerator(name = "AMP_ACTIVITY_CONTACT_seq", sequenceName = "AMP_ACTIVITY_CONTACT_seq", allocationSize = 1)    @Column(name = "activity_contact_id")
    private Long id;

    @Column(name = "contact_type")
    private String contactType;

    @Column(name = "is_primary_contact")
    @Interchangeable(fieldTitle = ActivityFieldsConstants.PRIMARY_CONTACT, importable = true)
    private Boolean primaryContact;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    @InterchangeableBackReference
    private AmpActivityVersion activity;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    @PossibleValues(ContactPossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Contact", pickIdOnly = true, importable = true, uniqueConstraint = true,
            interValidators = @InterchangeableValidator(RequiredValidator.class),
            commonPV = CommonFieldsConstants.COMMON_CONTACT)
    private AmpContact contact;


    public AmpActivityVersion getActivity() {
        return activity;
    }
    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }
    public AmpContact getContact() {
        return contact;
    }
    public void setContact(AmpContact contact) {
        this.contact = contact;
    }
    public Boolean getPrimaryContact() {
        return primaryContact;
    }
    public void setPrimaryContact(Boolean primaryContact) {
        this.primaryContact = primaryContact;
    }
    public String getContactType() {
        return contactType;
    }
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpActivityContact aux = (AmpActivityContact) obj;
        String original = "" + this.contact.getId();
        String copy = "" + aux.contact.getId();
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
                new Output(null, new String[] { "Contact" }, new Object[] { this.contact.getLastname() + " "
                        + this.contact.getName() }));
        return out;
    }

    @Override
    public Object getValue() {
        return "" + this.contactType + "-" + this.primaryContact;
    }

    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        if (!(o instanceof AmpActivityContact)) return -1;
        AmpActivityContact aac = (AmpActivityContact)o;

        if (this.getContact() == null || aac.getContact() == null) return -1;

        int result = this.getContact().compareTo(aac.getContact());
        if(result==0){
            if ((this.activity != null || aac.getActivity() != null) && (this.activity == null || aac.getActivity() == null))
                return -1;
            if (this.activity != null && this.activity != null)
                result=this.activity.compareTo(aac.getActivity());
        }
        if(result==0){
            if ((this.contactType != null || aac.getContactType() != null) || (this.contactType == null || this.getContactType() == null))
                return -1;
            if (this.contactType != null && this.getContactType() != null)
                result=this.contactType.compareTo(aac.getContactType());
        }
        return result;
    }
    
    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpActivityContact aux = (AmpActivityContact) clone();
        aux.activity = newActivity;
        aux.id = null;
                if(this.contact.getActivityContacts()==null){
                    this.contact.setActivityContacts(new TreeSet<AmpActivityContact>());
                }
        this.contact.getActivityContacts().add(aux);
        return aux;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
