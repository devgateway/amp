package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

public class BoundariesService {

	protected static Logger logger = Logger.getLogger(BoundariesService.class);

	/**
	 * Return the list .json files for this country as a JSONArray object.
	 * 
	 * @return
	 */
	public static JSONArray getBoundaries() {
		String countryIso = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_COUNTRY);
		String path = String.format("/gis/boundaries/%s/list.json", countryIso.toUpperCase());

		try (InputStream is = BoundariesService.class.getResourceAsStream(path)) {
			String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
			return (JSONArray) JSONSerializer.toJSON(jsonTxt);
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
	public static Map<String, JSONObject> getBoundariesAsList() {
		JSONArray boundariesJSON = BoundariesService.getBoundaries();
		Map<String, JSONObject> boundariesMap = new HashMap<>();
		for (final Object adm : boundariesJSON) {
			boundariesMap.put(String.valueOf(((JSONObject) adm).get("id")),
					(JSONObject) adm);
		}
		return boundariesMap;
	}
}