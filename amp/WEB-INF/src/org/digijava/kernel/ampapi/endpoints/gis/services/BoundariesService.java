package org.digijava.kernel.ampapi.endpoints.gis.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoundariesService {

    protected static Logger logger = Logger.getLogger(BoundariesService.class);

    private static final String CONTEXT_PATH = TLSUtils.getRequest().getServletContext().getRealPath("/");
    private static final String BOUNDARY_PATH = "gis" + File.separator + "boundaries" + File.separator;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Return the list .json files for this country as a JSONArray object.
     * 
     * @return
     */
    public static List<Boundary> getBoundaries() {
        String countryIso = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_COUNTRY);
        String path = CONTEXT_PATH + BOUNDARY_PATH + countryIso.toUpperCase() + File.separator + "list.json";
        try (InputStream is = new FileInputStream(path)) {
            String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
            return MAPPER.readValue(jsonTxt, new TypeReference<List<Boundary>>() { });
        } catch (IOException e) {
            logger.error("Failed to load boundaries for " + countryIso, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Return the list of .json files for this country as a Map with the adm-N
     * for key.
     * 
     * @return
     */
    public static Map<String, Boundary> getBoundariesAsList() {
        List<Boundary> boundaries = BoundariesService.getBoundaries();
        Map<String, Boundary> boundariesMap = new HashMap<>();
        for (Boundary boundary : boundaries) {
            boundariesMap.put(boundary.getId().getLabel(), boundary);
        }
        return boundariesMap;
    }
}
