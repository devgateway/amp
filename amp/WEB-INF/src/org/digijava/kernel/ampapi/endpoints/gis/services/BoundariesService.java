package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

public class BoundariesService {

	protected static Logger logger = Logger.getLogger(BoundariesService.class);

	private static final String CONTEXT_PATH = TLSUtils.getRequest()
			.getServletContext().getRealPath("/");
	private static final String BOUNDARY_PATH = "src" + File.separator + "main"
			+ File.separator + "resources" + File.separator + "gis"
			+ File.separator + "boundaries" + File.separator;

	/**
	 * Return the list .json files for this country as a JSONArray object.
	 * 
	 * @param path
	 * @return
	 */
	public static JSONArray getBoundaries() {
		String countryIso = FeaturesUtil
				.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_COUNTRY);
		String path = CONTEXT_PATH + BOUNDARY_PATH + countryIso.toUpperCase()
				+ File.separator + "list.json";
		JSONArray json = null;
		try {
			InputStream is = new FileInputStream(path);
			String jsonTxt = IOUtils.toString(is);
			json = (JSONArray) JSONSerializer.toJSON(jsonTxt);

		} catch (IOException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return json;
	}

	/**
	 * Return the list of .json files for this country as a Map with the adm-N
	 * for key.
	 * 
	 * @return
	 */
	public static Map<String, JSONObject> getBoundariesAsList() {
		JSONArray boundariesJSON = BoundariesService.getBoundaries();
		List boundariesList = Arrays.asList(boundariesJSON.toArray());
		Map<String, JSONObject> boundariesMap = new HashMap<>();
		for (final Object adm : boundariesList) {
			boundariesMap.put(((JSONObject) adm).get("id").toString(),
					(JSONObject) adm);
		}
		return boundariesMap;
	}
}