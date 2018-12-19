package org.digijava.kernel.ampapi.endpoints.gpi;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpGPINiAidOnBudget;
import org.digijava.module.aim.dbentity.AmpGPINiDonorNotes;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class GPIEndPointsTest {

    @Rule
    public JacksonInTestRule jacksonInTestRule = new JacksonInTestRule(this::resolve);

    @Test
    public void testAidOnBudgetSerialization() throws JsonProcessingException {
        AmpGPINiAidOnBudget aidOnBudget = new AmpGPINiAidOnBudget();
        aidOnBudget.setAmpGPINiAidOnBudgetId(123L);
        aidOnBudget.setAmount(600d);

        AmpCurrency currency = new AmpCurrency();
        currency.setAmpCurrencyId(1L);
        currency.setCurrencyCode("USD");
        aidOnBudget.setCurrency(currency);

        AmpOrganisation donor = new AmpOrganisation();
        donor.setAmpOrgId(34L);
        donor.setName("USAID");
        aidOnBudget.setDonor(donor);

        aidOnBudget.setIndicatorDate(midnight(2018, 11, 28));

        ObjectMapper objectMapper = new ObjectMapper();
        String actualJson = objectMapper.writeValueAsString(aidOnBudget);

        assertEquals("{\"amount\":600.0,"
                + "\"indicatorDate\":\"2018-11-28\","
                + "\"id\":123,"
                + "\"currencyCode\":\"USD\","
                + "\"donorId\":34}",
                actualJson);
    }

    @Test
    public void testAidOnBudgetDeserialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        AmpGPINiAidOnBudget aidOnBudget = objectMapper.readValue("{\"amount\":600.0,"
                + "\"indicatorDate\":\"2018-11-28\","
                + "\"id\":123,"
                + "\"currencyCode\":\"USD\","
                + "\"donorId\":34}", AmpGPINiAidOnBudget.class);

        assertEquals(Double.valueOf(600), aidOnBudget.getAmount());
        assertEquals(Long.valueOf(123), aidOnBudget.getAmpGPINiAidOnBudgetId());
        assertEquals(midnight(2018, 11, 28), aidOnBudget.getIndicatorDate());

        assertEquals("USD", aidOnBudget.getCurrency().getCurrencyCode());
        assertEquals(Long.valueOf(1), aidOnBudget.getCurrency().getAmpCurrencyId());

        assertEquals("USAID", aidOnBudget.getDonor().getName());
        assertEquals(Long.valueOf(34), aidOnBudget.getDonor().getAmpOrgId());
    }

    @Test
    public void testNoteSerialization() throws JsonProcessingException {
        AmpGPINiDonorNotes note = new AmpGPINiDonorNotes();
        note.setAmpGPINiDonorNotesId(55L);
        note.setIndicatorCode("43");
        note.setNotes("lorem ipsum");
        note.setNotesDate(midnight(2015, 7, 6));

        AmpOrganisation donor = new AmpOrganisation();
        donor.setAmpOrgId(108L);
        donor.setName("Finland");
        note.setDonor(donor);

        ObjectMapper objectMapper = new ObjectMapper();
        String actualJson = objectMapper.writeValueAsString(note);

        assertEquals("{\"notes\":\"lorem ipsum\","
                        + "\"notesDate\":\"2015-07-06\","
                        + "\"indicatorCode\":\"43\","
                        + "\"id\":55,"
                        + "\"donorId\":108}",
                actualJson);
    }

    @Test
    public void testNotesDeserialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        AmpGPINiDonorNotes notes = objectMapper.readValue("{\"indicatorCode\":\"1\","
                + "\"notesDate\":\"2018-11-08\","
                + "\"donorId\":34,"
                + "\"notes\":\"Note 1\"}", AmpGPINiDonorNotes.class);

        assertNotNull(notes);
        assertEquals("Note 1", notes.getNotes());
        assertEquals(Long.valueOf(34), notes.getDonor().getAmpOrgId());
        assertEquals("USAID", notes.getDonor().getName());
        assertEquals(midnight(2018, 11, 8), notes.getNotesDate());
        assertEquals("1", notes.getIndicatorCode());
    }

    private Date midnight(int year, int monthOfYear, int dayOfMonth) {
        return new DateTime()
                .withDate(year, monthOfYear, dayOfMonth)
                .withTimeAtStartOfDay()
                .toDate();
    }

    private Object resolve(ObjectIdGenerator.IdKey idKey) {
        if (idKey.scope.equals(AmpCurrency.class) && idKey.key.equals("USD")) {
            AmpCurrency currency = new AmpCurrency();
            currency.setAmpCurrencyId(1L);
            currency.setCurrencyCode("USD");
            return currency;
        }
        if (idKey.scope.equals(AmpOrganisation.class) && idKey.key.equals(34L)) {
            AmpOrganisation donor = new AmpOrganisation();
            donor.setAmpOrgId(34L);
            donor.setName("USAID");
            return donor;
        }
        return null;
    }
}