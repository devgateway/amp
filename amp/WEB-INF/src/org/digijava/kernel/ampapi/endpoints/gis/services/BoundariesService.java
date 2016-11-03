package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

public class BoundariesService {

	protected static Logger logger = Logger.getLogger(BoundariesService.class);

	/**
	 * Return the list .json files for this country.
	 * 
	 * @param path
	 * @return
	 */
	public static JSONArray getBoundaries() {
		String path = TLSUtils.getRequest().getServletContext()
				.getRealPath("/");
		String BOUNDARY_PATH = "src" + File.separator + "main" + File.separator
				+ "resources" + File.separator + "gis" + File.separator
				+ "boundaries" + File.separator;
		String countryIso = FeaturesUtil
				.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_COUNTRY);
		path = path + BOUNDARY_PATH + countryIso.toUpperCase() + File.separator
				+ "list.json";
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
}
