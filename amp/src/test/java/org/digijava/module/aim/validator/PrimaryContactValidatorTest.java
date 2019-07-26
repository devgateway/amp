package org.digijava.module.aim.validator;

import static org.digijava.module.aim.validator.ConstraintMatchers.inIterableNode;
import static org.digijava.module.aim.validator.ConstraintMatchers.inIterableNodeAtKey;
import static org.digijava.module.aim.validator.ConstraintMatchers.nodeAtKey;
import static org.digijava.module.aim.validator.ConstraintMatchers.propertyNode;
import static org.digijava.module.aim.validator.ConstraintMatchers.violationWithPath;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.validator.contact.PrimaryContact;
import org.digijava.module.aim.validator.contact.PrimaryContactValidator;
import org.hamcrest.Matcher;
import org.junit.Test;

/**
 * @author Viorel Chihai
 */
public class PrimaryContactValidatorTest extends AbstractActivityValidatorTest<PrimaryContactValidator> {

    @Test
    public void testNotAppliedInHibernate() {
        AmpActivity activity = new AmpActivity();
        activity.setActivityContacts(ImmutableSet.of(newActivityContact(Constants.DONOR_CONTACT, false)));

        Set<ConstraintViolation<AmpActivity>> violations = getValidator().validate(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullContacts() {
        AmpActivity activity = new AmpActivity();
        activity.setActivityContacts(null);

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testEmptyContacts() {
        AmpActivity activity = new AmpActivity();
        activity.setSectors(ImmutableSet.of());

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullContactType() {
        AmpActivity activity = new AmpActivity();
        activity.setActivityContacts(ImmutableSet.of(
                newActivityContact(null, true)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testNullPrimaryContact() {
        AmpActivity activity = new AmpActivity();
        activity.setActivityContacts(ImmutableSet.of(
                newActivityContact(Constants.DONOR_CONTACT, null)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }

    @Test
    public void testValidPrimaryTwoContacts() {
        AmpActivity activity = new AmpActivity();
        activity.setActivityContacts(ImmutableSet.of(
                newActivityContact(Constants.DONOR_CONTACT, true),
                newActivityContact(Constants.MOFED_CONTACT, true)));

        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);

        assertThat(violations, emptyIterable());
    }
    
    @Test
    public void testValidPrimaryContactType() {
        AmpActivity activity = new AmpActivity();
        activity.setActivityContacts(ImmutableSet.of(
                newActivityContact(Constants.DONOR_CONTACT, true),
                newActivityContact(Constants.DONOR_CONTACT, false)));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, emptyIterable());
    }
    
    @Test
    public void testInvalidPrimarySameContactType() {
        AmpActivity activity = new AmpActivity();
        activity.setActivityContacts(ImmutableSet.of(
                newActivityContact(Constants.DONOR_CONTACT, true),
                newActivityContact(Constants.DONOR_CONTACT, true)));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
    
        assertThat(violations, contains(primaryContactViolation(Constants.DONOR_CONTACT)));
    }
    
    @Test
    public void testMultipleValidPrimaryContacts() {
        AmpActivity activity = new AmpActivity();
        activity.setActivityContacts(ImmutableSet.of(
                newActivityContact(Constants.DONOR_CONTACT, true),
                newActivityContact(Constants.MOFED_CONTACT, null),
                newActivityContact(Constants.MOFED_CONTACT, true),
                newActivityContact(Constants.SECTOR_MINISTRY_CONTACT, false)));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations, emptyIterable());
    }
    
    @Test
    public void testMultipleInvalidPrimaryContacts() {
        AmpActivity activity = new AmpActivity();
        activity.setActivityContacts(ImmutableSet.of(
                newActivityContact(Constants.DONOR_CONTACT, true),
                newActivityContact(Constants.DONOR_CONTACT, true),
                newActivityContact(Constants.MOFED_CONTACT, true),
                newActivityContact(Constants.MOFED_CONTACT, true),
                newActivityContact(Constants.SECTOR_MINISTRY_CONTACT, false)));
        
        Set<ConstraintViolation<AmpActivity>> violations = validateForAPI(activity);
        
        assertThat(violations,
                containsInAnyOrder(
                        primaryContactViolation(Constants.DONOR_CONTACT),
                        primaryContactViolation(Constants.MOFED_CONTACT)));
    }

    /**
     * Matcher for an activity contact primary constraint violation.
     */
    private Matcher<ConstraintViolation> primaryContactViolation(String type) {
        return violationWithPath(PrimaryContact.class,
                ImmutableList.of(propertyNode("activityContacts"), inIterableNodeAtKey("activityContacts", type)));
    }

    private AmpActivityContact newActivityContact(String contactType, Boolean primary) {
        AmpActivityContact activityContact = new AmpActivityContact();
        activityContact.setContactType(contactType);
        activityContact.setPrimaryContact(primary);
        
        return activityContact;
    }

}