package org.digijava.kernel.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * Non-persistent implementation of {@code InMemoryManager} which is backed by an in-memory map.
 * <p>
 * Mainly intended for testing purposes, where a full blown persistent system isn't required.
 * <p>
 * Small subset of Haiti location tree.
 *
 * @author Octavian Ciubotaru
 */
public class InMemoryLocationManager implements InMemoryManager<AmpLocation> {
    
    private static InMemoryLocationManager instance;
    
    InMemoryCategoryValuesManager categoryValueManager = InMemoryCategoryValuesManager.getInstance();
    
    private AmpCategoryValueLocations root;
    
    private Map<Long, AmpLocation> ampLocations = new HashMap<>();
    
    public static InMemoryLocationManager getInstance() {
        if (instance == null) {
            instance = new InMemoryLocationManager();
        }
        
        return instance;
    }
    
    private InMemoryLocationManager() {
        root = location(96L, "Haiti",
                location(1901L, "Artibonite",
                        location(1911L, "Dessalines",
                                location(1962L, "Desdunes",
                                        location(2713L, "Ville de Desdunes")),
                                location(1962L, "Dessalines",
                                        location(2699L, "Ville de Dessalines (ou Marchand)"))),
                        location(1914L, "Marmelade Arrondissement"),
                        location(1915L, "Saint-Marc Arrondissement",
                                location(1975L, "Saint-Marc",
                                        location(2680L, "Quartier de Montrouis"),
                                        location(2679L, "Ville de Saint-Marc")),
                                location(1976L, "Verettes",
                                        location(2689L, "Quartier Liancourt"),
                                        location(2687L, "Ville de Verrettes")))),
                location(1903L, "Grande Anse"),
                location(1904L, "Nippes"));
        
        root.setIso3("HTI");
        root.setIso("ht");
        
        setParentCategoryValues(categoryValueManager);
        addToAmpLocationsList(root);
    }
    
    private void addToAmpLocationsList(AmpCategoryValueLocations location) {
        ampLocations.put(location.getId(), getAmpLocation(location));
        for (AmpCategoryValueLocations loc : location.getChildren()) {
            addToAmpLocationsList(loc);
        }
    }
    
    private void setParentCategoryValues(InMemoryCategoryValuesManager categoryValues) {
        InMemoryCategoryValuesManager.ImplementationLocations implLocs =
                categoryValues.getImplementationLocations();
        
        ImmutableList<AmpCategoryValue> cvByLevel = ImmutableList.of(
                implLocs.getCountry(),
                implLocs.getRegion(),
                implLocs.getZone(),
                implLocs.getDistrict(),
                implLocs.getCommunalSection());
        
        setParentCategoryValue(root, cvByLevel, 0);
    }
    
    private void setParentCategoryValue(AmpCategoryValueLocations loc, List<AmpCategoryValue> cvByLevel, int level) {
        loc.setParentCategoryValue(cvByLevel.get(level));
        for (AmpCategoryValueLocations child : loc.getChildLocations()) {
            setParentCategoryValue(child, cvByLevel, level + 1);
        }
    }
    
    private AmpCategoryValueLocations location(Long id, String name, AmpCategoryValueLocations... children) {
        AmpCategoryValueLocations loc = new AmpCategoryValueLocations();
        loc.setId(id);
        loc.setName(name);
        loc.setChildLocations(ImmutableSet.copyOf(children));
        for (AmpCategoryValueLocations child : children) {
            child.setParentLocation(loc);
        }
        return loc;
    }
    
    
    public AmpLocation getAmpLocation(String... path) {
        AmpCategoryValueLocations location = getLocation(ImmutableList.copyOf(path));
        
        AmpLocation ampLocation = new AmpLocation();
        ampLocation.setAmpLocationId(location.getId());
        ampLocation.setLocation(location);
        
        return getAmpLocation(location);
    }
    
    public AmpLocation getAmpLocation(AmpCategoryValueLocations location) {
        AmpLocation ampLocation = new AmpLocation();
        ampLocation.setAmpLocationId(location.getId());
        ampLocation.setLocation(location);
        
        return ampLocation;
    }
    
    public AmpCategoryValueLocations getLocation(List<String> path) {
        if (path.size() > 0 && root.getName().equals(path.get(0))) {
            return getLocation(root, path.subList(1, path.size()));
        } else {
            throw new IllegalArgumentException("No such location: " + path);
        }
    }
    
    private AmpCategoryValueLocations getLocation(AmpCategoryValueLocations loc, List<String> path) {
        if (path.size() == 0) {
            return loc;
        } else {
            String name = path.get(0);
            AmpCategoryValueLocations child = loc.getChildLocations().stream()
                    .filter(c -> c.getName().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No such location: " + name));
            return getLocation(child, path.subList(1, path.size()));
        }
    }
    
    @Override
    public AmpLocation get(Long id) {
        return ampLocations.get(id);
    }
    
    @Override
    public List<AmpLocation> getAllValues() {
        return new ArrayList<>(ampLocations.values());
    }
}
