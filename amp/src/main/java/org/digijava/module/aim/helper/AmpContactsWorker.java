package org.digijava.module.aim.helper;

import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpContactPhoneProperty;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AmpContactsWorker {
    
    /**
     * Create contact property of specified type(email,phone or fax)
     * @param propertyName
     * @return
     */
    public static ContactPropertyHelper createProperty(String propertyName, String value, String phoneType) {
        ContactPropertyHelper newProperty=new ContactPropertyHelper();
        newProperty.setName(propertyName);
        newProperty.setValue(value);
        if(propertyName.equals(Constants.CONTACT_PROPERTY_NAME_PHONE)){
            try {
                newProperty.setPhoneTypeId(Long.parseLong(phoneType));
            } catch (NumberFormatException nfEx) {
                newProperty.setPhoneTypeId(new Long(0));
            }
        }
        return newProperty;
    }
    
    public static ContactPropertyHelper createProperty(String propertyName) {       
        return createProperty(propertyName,"","");
    }
    
    /**
     * builds AmpContact properties from helper properties list
     * @param helperProperties
     * @return
     */
    public static Set<AmpContactProperty> buildAmpContactProperties(ContactPropertyHelper[] helperProperties){
        Set<AmpContactProperty> retVal=null;
        if(helperProperties!=null && helperProperties.length>0){
            for (ContactPropertyHelper helperProperty : helperProperties) {
                AmpContactProperty property = AmpContactProperty.instantiate(helperProperty.getName());
                property.setName(helperProperty.getName());
                if(helperProperty.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE)){
                    ((AmpContactPhoneProperty) property).setType(
                            CategoryManagerUtil.getAmpCategoryValueFromDb(helperProperty.getPhoneTypeId(), false));
                    property.setValue(helperProperty.getValue());
                }else{
                    property.setValue(helperProperty.getValue());
                }
                if(retVal==null){
                    retVal=new HashSet<AmpContactProperty>();
                }
                retVal.add(property);
            }
        }
        return retVal;
    }
    
    /**
     * builds Helper Contact Properties from db entities
     * @param properties
     * @return
     */
    public static List<ContactPropertyHelper> buildHelperContactProperties(Set<AmpContactProperty> properties){
        List<ContactPropertyHelper> retVal=null;
        if(properties!=null && properties.size()>0){
            for (AmpContactProperty property : properties) {
                ContactPropertyHelper contactProperty=new ContactPropertyHelper();
                contactProperty.setName(property.getName());
                if (property.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE)
                        && ((AmpContactPhoneProperty) property).getType() != null) {
                    contactProperty.setPhoneTypeId(((AmpContactPhoneProperty) property).getType().getId());
                }
                contactProperty.setValue(property.getValue());

                if(retVal==null){
                    retVal=new ArrayList<ContactPropertyHelper>();
                }
                retVal.add(contactProperty);
            }
        }
        return retVal;      
    }
    
    /**
     * gets all activity contacts and divides it into subgroups(donor,mofed, e.t.c.)
     * @param activityContacts
     * @param eaForm
     */
    public static void copyContactsToSubLists(List<AmpActivityContact> activityContacts, EditActivityForm eaForm){
        //fill activity's donor contact List
        if(eaForm.getContactInformation().getContactType()!=null && eaForm.getContactInformation().getContactType().equals(Constants.DONOR_CONTACT)){
            List<AmpActivityContact> donorContacts=null;
            for (AmpActivityContact cont : activityContacts) {
                if(cont.getContactType().equals(Constants.DONOR_CONTACT)){
                    if(donorContacts==null){
                        donorContacts=new ArrayList<AmpActivityContact>();
                    }
                    donorContacts.add(cont);
                }
            }
            eaForm.getContactInformation().setDonorContacts(donorContacts);
        }
        //fill activity's mofed contact list
        else if(eaForm.getContactInformation().getContactType()!=null && eaForm.getContactInformation().getContactType().equals(Constants.MOFED_CONTACT)){
            List<AmpActivityContact> mofedContacts=null;
            for (AmpActivityContact cont : activityContacts) {
                if(cont.getContactType()!=null && cont.getContactType().equals(Constants.MOFED_CONTACT)){
                    if(mofedContacts==null){
                        mofedContacts=new ArrayList<AmpActivityContact>();
                    }
                    mofedContacts.add(cont);
                }
            }
            eaForm.getContactInformation().setMofedContacts(mofedContacts);
        }
        //fill project coordinator contact list
        else if(eaForm.getContactInformation().getContactType()!=null && eaForm.getContactInformation().getContactType().equals(Constants.PROJECT_COORDINATOR_CONTACT)){
            List<AmpActivityContact> projCoordinatorContacts=null;
            for (AmpActivityContact cont : activityContacts) {
                if(cont.getContactType().equals(Constants.PROJECT_COORDINATOR_CONTACT)){
                    if(projCoordinatorContacts==null){
                        projCoordinatorContacts=new ArrayList<AmpActivityContact>();
                    }
                    projCoordinatorContacts.add(cont);
                }
            }
            eaForm.getContactInformation().setProjCoordinatorContacts(projCoordinatorContacts);
        }
        //fill sector ministry contact list
        else if(eaForm.getContactInformation().getContactType()!=null && eaForm.getContactInformation().getContactType().equals(Constants.SECTOR_MINISTRY_CONTACT)){
            List<AmpActivityContact> sectorMinistryContacts=null;
            for (AmpActivityContact cont : activityContacts) {
                if(cont.getContactType().equals(Constants.SECTOR_MINISTRY_CONTACT)){
                    if(sectorMinistryContacts==null){
                        sectorMinistryContacts=new ArrayList<AmpActivityContact>();
                    }
                    sectorMinistryContacts.add(cont);
                }
            }
            eaForm.getContactInformation().setSectorMinistryContacts(sectorMinistryContacts);
        }
        //fill implementing/executing agency contact list
        else if(eaForm.getContactInformation().getContactType()!=null && eaForm.getContactInformation().getContactType().equals(Constants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT)){
            List<AmpActivityContact> implExecAgencyContacts=null;
            for (AmpActivityContact cont : activityContacts) {
                if(cont.getContactType().equals(Constants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT)){
                    if(implExecAgencyContacts==null){
                        implExecAgencyContacts=new ArrayList<AmpActivityContact>();
                    }
                    implExecAgencyContacts.add(cont);
                }
            }
            eaForm.getContactInformation().setImplExecutingAgencyContacts(implExecAgencyContacts);
        }
    }
}
