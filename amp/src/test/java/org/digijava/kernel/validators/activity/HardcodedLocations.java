package org.digijava.kernel.validators.activity;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * Small subset of Haiti location tree.
 *
 * @author Octavian Ciubotaru
 */
public class HardcodedLocations {

    private AmpCategoryValueLocations root;

    public HardcodedLocations(HardcodedCategoryValues categoryValues) {

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

        setParentCategoryValues(categoryValues);
    }

    private void setParentCategoryValues(HardcodedCategoryValues categoryValues) {
        HardcodedCategoryValues.ImplementationLocations implLocs =
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


    public AmpCategoryValueLocations getAmpLocation(String... path) {
        return getLocation(ImmutableList.copyOf(path));
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
}
