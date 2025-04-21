package org.digijava.kernel.ampapi.endpoints.gis.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;

import static org.digijava.module.aim.util.LocationConstants.MULTI_COUNTRY_ISO_CODE;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static net.bull.javamelody.internal.common.Parameters.getServletContext;

public class BoundariesService {

    protected static Logger logger = Logger.getLogger(BoundariesService.class);

    private static final String BOUNDARY_PATH =  "WEB-INF/gis" + File.separator + "boundaries" + File.separator;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Return the list .json files for this country as a JSONArray object.
     *
     * @return
     */
    public static List<Boundary> getBoundaries() {
        String path = BOUNDARY_PATH + "regional-list.json";
        logger.info("Country ISO: "+ DynLocationManagerUtil.getDefaultCountry().getIso());
        if (!FeaturesUtil.isVisibleFeature(GisConstants.MULTICOUNTRY_ENABLED) && !DynLocationManagerUtil.getDefaultCountry().getIso().equals(MULTI_COUNTRY_ISO_CODE))
        {
                String countryIso = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_COUNTRY);
                if (countryIso != null) {
                    path = BOUNDARY_PATH + countryIso.toUpperCase() + File.separator + "list.json";
                }

        }
        logger.info("Boundaries path is: "+path);
        try (InputStream is = Files.newInputStream(Paths.get(path))) {
            String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
            return MAPPER.readValue(jsonTxt, new TypeReference<List<Boundary>>() { });
        } catch (IOException e) {
            logger.error("Failed to load boundaries for BOAD", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Return the list of .json files for this country
     * fo
     *
     * @return
     */
    public static Set<String> getAdmLevelsForBoundaries() {
        List<Boundary> boundaries = BoundariesService.getBoundaries();
        Set<String> admLevels = new HashSet<>();
        for (Boundary boundary : boundaries) {
            admLevels.add(boundary.getAdmLevel().getLabel());
        }
        return admLevels;
    }

}
