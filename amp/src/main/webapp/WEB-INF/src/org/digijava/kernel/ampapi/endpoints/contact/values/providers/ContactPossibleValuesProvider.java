package org.digijava.kernel.ampapi.endpoints.contact.values.providers;

import java.util.List;
import java.util.stream.Collectors;

import org.digijava.kernel.ampapi.endpoints.activity.AmpPossibleValuesDAO;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpContact;

import com.google.common.collect.ImmutableMap;

/**
 * @author Nadejda Mandrescu
 */
public class ContactPossibleValuesProvider implements PossibleValuesProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<AmpContact> contacts = InterchangeUtils.getSessionWithPendingChanges()
                .createCriteria(AmpContact.class)
                .setCacheable(true)
                .setCacheRegion(AmpPossibleValuesDAO.CACHE)
                .list();
        return contacts.stream().map(this::toPossibleValue).collect(Collectors.toList());
    }

    private PossibleValue toPossibleValue(AmpContact contact) {
        return new PossibleValue(contact.getId(), contact.getNameAndLastName(),  ImmutableMap.of());
    }

    @Override
    public boolean isAllowed(Long id) {
        return PersistenceManager.getSession().get(AmpContact.class, id) != null;
    }

}
