package org.digijava.kernel.geocoding.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.similarity.FuzzyScore;
import org.digijava.kernel.geocoding.client.GeoCoderClient;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * @author Viorel Chihai
 */
public final class GeoCodingLocationMatcher {

    private static final Logger logger = LoggerFactory.getLogger(GeoCodingLocationMatcher.class);

    private static final long MAX_LEVENSHTEIN_DISTANCE = 3;
    private static final long MIN_FUZZY_DISTANCE = 10;
    private static final double MAX_COORD_DISTANCE = 0.0001;

    private static final FuzzyScore fz = new FuzzyScore(Locale.getDefault());

    private GeoCodingLocationMatcher() {

    }

    public static boolean match(final AmpCategoryValueLocations ampLocation,
                                final GeoCoderClient.GeoCodingLocation location) {
        if ((matchByNameWithLevenshteinDistance(ampLocation, location)
                && matchByAdministrativeLevel(ampLocation, location))
                || matchByGeoCode(ampLocation, location) || matchByCoordinates(ampLocation, location)) {
            logger.debug(String.format("Matched [%s] with [%s] ", location.getName(), ampLocation.getName()));
            logger.debug(String.format("Match by adm: %s", matchByAdministrativeLevel(ampLocation, location)));
            logger.debug(String.format("Match by geocode: %s %s", matchByGeoCode(ampLocation, location),
                    location.getGeoCode()));
            logger.debug(String.format("Match by coord: %s", matchByCoordinates(ampLocation, location)));
            return true;
        }

        if (matchByAdministrativeLevel(ampLocation, location)) {
            return matchByNameWithFuzzySearch(ampLocation, location);
        }

        return false;
    }

    private static boolean matchByNameWithLevenshteinDistance(final AmpCategoryValueLocations ampLocation,
                                                              final GeoCoderClient.GeoCodingLocation location) {
        return StringUtils.getLevenshteinDistance(ampLocation.getName(), location.getName()) < MAX_LEVENSHTEIN_DISTANCE;
    }

    private static boolean matchByNameWithFuzzySearch(final AmpCategoryValueLocations ampLocation,
                                                              final GeoCoderClient.GeoCodingLocation location) {
        return fz.fuzzyScore(location.getName(), ampLocation.getName()) > MIN_FUZZY_DISTANCE;
    }

    private static boolean matchByAdministrativeLevel(final AmpCategoryValueLocations ampLocation,
                                               final GeoCoderClient.GeoCodingLocation location) {
        String locCode = StringUtils.equalsIgnoreCase(location.getLevelCode(), "PCLI") ? "ADM0"
                : location.getLevelCode();
        return StringUtils.equalsIgnoreCase("ADM" + ampLocation.getParentCategoryValue().getIndex(), locCode);
    }

    private static boolean matchByGeoCode(final AmpCategoryValueLocations ampLocation,
                                   final GeoCoderClient.GeoCodingLocation location) {
        if (StringUtils.isBlank(ampLocation.getGeoCode()) || StringUtils.isBlank(String.valueOf(location.getGeoCode()))) {
            return false;
        }
        return StringUtils.equalsIgnoreCase(ampLocation.getGeoCode(), String.valueOf(location.getGeoCode()));
    }

    private static boolean matchByCoordinates(final AmpCategoryValueLocations ampLocation,
                                       final GeoCoderClient.GeoCodingLocation location) {
        Double locLng = getCoordinateAsDouble(ampLocation.getGsLong());
        Double locLat = getCoordinateAsDouble(ampLocation.getGsLat());
        if (locLng == null || locLat == null || location.getLat() == null || location.getLng() == null) {
            return false;
        }

        return Math.abs(locLat - location.getLat()) + Math.abs(locLng - location.getLng()) < MAX_COORD_DISTANCE;
    }

    private static Double getCoordinateAsDouble(String coord) {
        if (StringUtils.isBlank(coord)) {
            return null;
        }

        try {
            return Double.parseDouble(coord);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}
