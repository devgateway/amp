package org.digijava.module.esrigis.action;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.UrlValidator;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.esrigis.dbentity.AmpMapConfig;
import org.digijava.module.esrigis.form.MapsConfigurationForm;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapConstants;

public class MapsConfiguration extends DispatchAction {
	private static Logger logger = Logger.getLogger(MapsConfiguration.class);
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
		Collection<AmpMapConfig> maps = new ArrayList<AmpMapConfig>();
		maps = DbHelper.getMaps();
		request.setAttribute("mapList", maps);
		return mapping.findForward("list");
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		Collection<AmpMapConfig> maps = new ArrayList<AmpMapConfig>();
		maps = DbHelper.getMaps();
		request.setAttribute("mapList", maps);
		return mapping.findForward("list");
	}
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		if (request.getParameter("id") != null) {
			Long mapId = Long.parseLong(request.getParameter("id"));
			AmpMapConfig map = DbHelper.getMap(mapId);
			DbHelper.delete(map);
		}
		return mapping.findForward("delete");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception

	{

		MapsConfigurationForm mapForm = (MapsConfigurationForm) form;
		if (request.getParameter("id") != null) {
			Long mapId = Long.parseLong(request.getParameter("id"));
			AmpMapConfig map = DbHelper.getMap(mapId);
			//TODO: Set information
			mapForm.setUrl(map.getMapUrl());
			mapForm.setMapId(map.getId());
			mapForm.setAdmin1(map.getCountyField());
			mapForm.setAdmin2(map.getDistrictField());
			mapForm.setGeoId(map.getGeoIdField());
			mapForm.setMapType(map.getMapType());
			mapForm.setMapSubType(map.getMapSubType());
			mapForm.setConfigName(map.getConfigName());
			// Rules for showing the two lists that define a map: type and subtype
			HashMap<Integer, String> mapTypeList = new HashMap<Integer, String>();
			//Add the currently selected one, plus any other that is not defined, for now, put everyting
			//mapTypeList.put(map.getMapType(), MapConstants.mapTypeNames.get(map.getMapType()));
			mapForm.setMapTypeList(MapConstants.mapTypeNames);
			mapForm.setMapSubTypeList(MapConstants.mapSubTypeNames);
		}
		return mapping.findForward("addEdit");
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		

		MapsConfigurationForm mapForm = (MapsConfigurationForm) form;
		HttpSession session = request.getSession();
		
		//Validations
		ActionMessages errors = new ActionMessages();
		String[] schemes = {"http","https"};
		UrlValidator urlValidator = new UrlValidator(schemes);
		
		//Open street maps url use parameters in the form of {x}. For example http://{s}.tile.openstreetmap.org/{x}/{y}/{z}.png
		//we need to allow this as a valid url http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Tile_servers
		String cleanedUrl = mapForm.getUrl().replaceAll("\\{[a-z]{1}\\}","XX");
		if(checkEmptyFields(mapForm)){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.mapConfiguration.emptyFields"));
    	}
		else if (!urlValidator.isValid(cleanedUrl)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.mapConfiguration.badUrl"));
		}	
		
		if (errors.size() > 0)
		{
			saveErrors(request, errors);
			return mapping.findForward("addEdit");
		}
	
		
		AmpMapConfig map;
		if (mapForm.getMapId() != null
				&& mapForm.getMapId() > 0) {
			map = DbHelper.getMap(mapForm.getMapId());
		} else {
			map = new AmpMapConfig();
		}
		map.setMapUrl(mapForm.getUrl());
		map.setCountyField(mapForm.getAdmin1());
		map.setDistrictField(mapForm.getAdmin2());
		map.setGeoIdField(mapForm.getGeoId());
		map.setMapSubType(mapForm.getMapSubType());
		map.setMapType(mapForm.getMapType());
		if (mapForm.getLegend().getFileData().length > 0) map.setLegendImage(mapForm.getLegend().getFileData());
		map.setConfigName(mapForm.getConfigName());
		map.setCountField(mapForm.getCount());
		DbHelper.save(map);

		return mapping.findForward("save");

	}

	public ActionForward displayLegend(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		String index = request.getParameter("id");

		if (index != null) {
				try {
					Long mapId = Long.parseLong(index);
					AmpMapConfig map = DbHelper.getMap(mapId);
					ServletOutputStream os = response.getOutputStream();
					if (map.getLegendImage() != null) {
//						response.setContentType(structureType.getIconFileContentType());
						os.write(map.getLegendImage());
						os.flush();
					}
					else
					{
						BufferedImage bufferedImage = new BufferedImage(30, 30,
								BufferedImage.TRANSLUCENT);
						ImageIO.write(bufferedImage, "png", os);
						os.flush();
					}
				} catch (NumberFormatException nfe) {
					logger.error("Trying to parse " + index + " to int");
				}
		} else {
			BufferedImage bufferedImage = new BufferedImage(30, 30,
					BufferedImage.TRANSLUCENT);
			ServletOutputStream os = response.getOutputStream();
			ImageIO.write(bufferedImage, "png", os);
			os.flush();
		}
		return null;
	}	

	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MapsConfigurationForm mapForm = (MapsConfigurationForm) form;
		Boolean isReset = Boolean.valueOf(request.getParameter("reset"));

		if (isReset) {
			mapForm.setReset(true);
			mapForm.reset(mapping, request);
			mapForm.setReset(false);
		}
		
		mapForm.setMapTypeList(MapConstants.mapTypeNames);
		mapForm.setMapSubTypeList(MapConstants.mapSubTypeNames);
		return mapping.findForward("addEdit");
	}

	private boolean checkEmptyFields (MapsConfigurationForm form) {
		if (form.getUrl() == null || form.getUrl().trim().isEmpty() || form.getCount() == null 
				|| form.getCount().trim().isEmpty())
		{
			return true;
		}
		else {
			return false;
		}
	}
	
//	
//	public ActionForward MapList(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws java.lang.Exception {
//		MapsConfigurationForm cform = (MapsConfigurationForm) form;
//		Collection<AmpMapConfig> maps = new ArrayList<AmpMapConfig>();
//		maps = DbHelper.getMaps();
//		for (Iterator iterator = maps.iterator(); iterator.hasNext();) {
//			AmpMapConfig mapConfig = (AmpMapConfig) iterator.next();
//			switch (mapConfig.getMapType()) {
//			case 1:
//				cform.setBasemap(mapConfig.getMapUrl());
//				break;
//			case 2:
//				cform.setMainmap(mapConfig.getMapUrl());
//				cform.setAdmin1(mapConfig.getCountyField());
//				cform.setAdmin2(mapConfig.getDistrictField());
//				cform.setGeoid(mapConfig.getGeoIdField());
//				break;
//			case 4:
//				cform.setGeometry(mapConfig.getMapUrl());
//				break;
//			case 5:
//				cform.setApi(mapConfig.getMapUrl());
//				break;
//			case 7:
//				cform.setGeolocator(mapConfig.getMapUrl());
//				break;
//			case 8:
//				cform.setBasemapsroot(mapConfig.getMapUrl());
//				break;
//			case 9:
//				cform.setNational(mapConfig.getMapUrl());
//				break;
//			case 10:
//				cform.setPoverty(mapConfig.getMapUrl());
//				break;
//			case 11:
//				cform.setPopulation(mapConfig.getMapUrl());
//				break;
//			default:
//				break;
//			}
//		}
//
//		return mapping.findForward("list");
//	}

//	public ActionForward saveOld(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//
//		MapsConfigurationForm cform = (MapsConfigurationForm) form;
//		Collection<AmpMapConfig> maps = new ArrayList<AmpMapConfig>();
//		maps = DbHelper.getMaps();
//		AmpMapConfig maptosave = null;
//		Integer maptype = new Integer(cform.getSelectedtype());
//		for (Iterator iterator = maps.iterator(); iterator.hasNext();) {
//			AmpMapConfig MapConfig = (AmpMapConfig) iterator.next();
//			if (MapConfig.getMapType() == maptype) {
//				maptosave = MapConfig;
//				break;
//			}
//		}
//		if (maptosave != null) {
//			maptosave.setMapType(maptype);
//			maptosave.setMapUrl(cform.getSelectedurl().trim());
//			if (!"".equalsIgnoreCase(cform.getSelectedadmin1())) {
//				maptosave.setCountyField(cform.getSelectedadmin1().trim());
//			}
//			if (!"".equalsIgnoreCase(cform.getSelectedadmin2())) {
//				maptosave.setDistrictField(cform.getSelectedadmin2().trim());
//			}
//			if (!"".equalsIgnoreCase(cform.getSelectedgeoid())) {
//				maptosave.setGeoIdField(cform.getSelectedgeoid().trim());
//			}
//		} else {
//			maptosave = new AmpMapConfig();
//			maptosave.setMapType(maptype);
//			maptosave.setMapUrl(cform.getSelectedurl().trim());
//			if (!"".equalsIgnoreCase(cform.getSelectedadmin1())) {
//				maptosave.setCountyField(cform.getSelectedadmin1().trim());
//			}
//			if (!"".equalsIgnoreCase(cform.getSelectedadmin2())) {
//				maptosave.setDistrictField(cform.getSelectedadmin2().trim());
//			}
//			if (!"".equalsIgnoreCase(cform.getSelectedgeoid())) {
//				maptosave.setGeoIdField(cform.getSelectedgeoid().trim());
//			}
//		}
//		if (!"".equalsIgnoreCase(maptosave.getMapUrl())) {
//			DbHelper.saveMapConfig(maptosave);
//		}
//		return MapList(mapping, cform, request, response);
//
//	}
//
//	public ActionForward savelegend(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		MapsConfigurationForm cform = (MapsConfigurationForm) form;
//		FormFile myFile = cform.getLegend();
//		String contentType = myFile.getContentType();
//		// povertymap.png
//		String fileName = "";
//		if (cform.getSelectedlegend()==1){
//			fileName="legend-poverty.jpg";
//		}else{
//			fileName="population-legend.jpg";
//		}
//		
//		byte[] fileData = myFile.getFileData();
//		String filePath = getServlet().getServletContext().getRealPath("/")
//				+ "TEMPLATE/ampTemplate/img_2/gis";
//		if (!fileName.equals("")) {
//			//System.out.println("Server path:" + filePath);
//
//			// Create file
//			
//			File fileToCreate = new File(filePath, fileName);
//
//			// If file does not exists create file
//			//if (!fileToCreate.exists()) {
//				FileOutputStream fileOutStream = new FileOutputStream(fileToCreate,false);
//
//				fileOutStream.write(myFile.getFileData());
//				fileOutStream.flush();
//				fileOutStream.close();
//			//}
//
//		}
//		return MapList(mapping, cform, request, response);
//
//	}

}
