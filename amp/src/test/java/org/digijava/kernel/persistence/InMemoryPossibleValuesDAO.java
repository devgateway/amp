package org.digijava.kernel.persistence;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesDAO;
import org.digijava.module.aim.dbentity.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Non-persistent implementation of {@code PossibleValuesDAO} which is backed by an in-memory map.
 * <p>
 * Mainly intended for testing purposes, where a full blown persistent system isn't required.
 *
 * @author Viorel Chihai
 */
public class InMemoryPossibleValuesDAO implements PossibleValuesDAO {
    
    @Override
    public List<Object[]> getCategoryValues(String discriminatorOption) {
        return InMemoryCategoryValuesManager.getInstance().getAllValues()
                .stream()
                .filter(c -> c.getAmpCategoryClass().getKeyName().equals(discriminatorOption))
                .map(c -> Arrays.asList(c.getId(), c.getValue(), c.getDeleted(), c.getIndex(), discriminatorOption)
                        .toArray())
                .collect(Collectors.toList());
    }
    
    @Override
    public <T> List<T> getGenericValues(Class<T> entity) {
        if (entity.equals(AmpTeamMember.class)) {
            return InMemoryTeamMemberManager.getInstance().getAllValues();
        }
        
        return null;
    }
    
    @Override
    public AmpClassificationConfiguration getAmpClassificationConfiguration(String name) {
        return null;
    }
    
    @Override
    public List<Object[]> getThemes(String configType) {
        return null;
    }

    @Override
    public List<AmpThemeMapping> getMappedThemes() {
        return null;
    }
    
    @Override
    public boolean isThemeValid(String configType, Long id) {
        return false;
    }
    
    @Override
    public List<Object[]> getSectors(String configType) {
        return null;
    }
    
    @Override
    public boolean isSectorValid(String configType, Long id) {
        return false;
    }
    
    @Override
    public List<Object[]> getPossibleLocations() {
        return InMemoryLocationManager.getInstance().getAllValues()
                .stream()
                .map(loc -> Arrays.asList(loc,
                        loc.getId(),
                        loc.getName(),
                        loc.getParentLocation().getId(),
                        loc.getParentLocation().getName(),
                        loc.getParentCategoryValue().getId(),
                        loc.getParentCategoryValue().getValue(),
                        loc.getIso())
                        .toArray())
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean isLocationValid(Long id) {
        return InMemoryLocationManager.getInstance().get(id) != null;
    }
    
    @Override
    public List<AmpOrganisation> getOrganisations() {
        return InMemoryOrganisationManager.getInstance().getAllValues();
    }
    
    @Override
    public boolean isOrganizationValid(Long id) {
        return InMemoryOrganisationManager.getInstance().get(id) != null;
    }

    @Override
    public List<AmpIndicatorRiskRatings> getIndicatorRiskRatings() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean isIndicatorRiskRatingValid(Long id) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public List<AmpIndicator> getIndicators() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean isIndicatorValid(Long id) {
        throw new RuntimeException("Not Implemented");
    }
}
