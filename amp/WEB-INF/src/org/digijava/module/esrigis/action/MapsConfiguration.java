package org.digijava.module.esrigis.action;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpStructureType;
import org.digijava.module.esrigis.dbentitiy.AmpMapConfig;
import org.digijava.module.esrigis.form.MapsConfigurationForm;
import org.digijava.module.esrigis.form.StructureTypeForm;
import org.digijava.module.esrigis.helpers.DbHelper;

public class MapsConfiguration extends DispatchAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
		return MapList(mapping, form, request, response);
	}

	public ActionForward MapList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
		MapsConfigurationForm cform = (MapsConfigurationForm) form;
		Collection<AmpMapConfig> maps = new ArrayList<AmpMapConfig>();
		maps = DbHelper.getMaps();
		for (Iterator iterator = maps.iterator(); iterator.hasNext();) {
			AmpMapConfig mapConfig = (AmpMapConfig) iterator.next();
			switch (mapConfig.getMaptype()) {
			case 1:
				cform.setBasemap(mapConfig.getMapurl());
				break;
			case 2:
				cform.setMainmap(mapConfig.getMapurl());
				cform.setAdmin1(mapConfig.getCountyfield());
				cform.setAdmin2(mapConfig.getDistrictfield());
				cform.setGeoid(mapConfig.getGeoidfield());
				break;
			case 4:
				cform.setGeometry(mapConfig.getMapurl());
				break;
			case 5:
				cform.setApi(mapConfig.getMapurl());
				break;
			case 7:
				cform.setGeolocator(mapConfig.getMapurl());
				break;
			case 8:
				cform.setBasemapsroot(mapConfig.getMapurl());
				break;
			case 9:
				cform.setNational(mapConfig.getMapurl());
				break;
			case 10:
				cform.setPoverty(mapConfig.getMapurl());
				break;
			case 11:
				cform.setPopulation(mapConfig.getMapurl());
				break;
			default:
				break;
			}
		}

		return mapping.findForward("list");
	}

	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MapsConfigurationForm cform = (MapsConfigurationForm) form;
		Collection<AmpMapConfig> maps = new ArrayList<AmpMapConfig>();
		maps = DbHelper.getMaps();
		AmpMapConfig maptosave = null;
		Integer maptype = new Integer(cform.getSelectedtype());
		for (Iterator iterator = maps.iterator(); iterator.hasNext();) {
			AmpMapConfig MapConfig = (AmpMapConfig) iterator.next();
			if (MapConfig.getMaptype() == maptype) {
				maptosave = MapConfig;
				break;
			}
		}
		if (maptosave != null) {
			maptosave.setMaptype(maptype);
			maptosave.setMapurl(cform.getSelectedurl().trim());
			if (!"".equalsIgnoreCase(cform.getSelectedadmin1())) {
				maptosave.setCountyfield(cform.getSelectedadmin1().trim());
			}
			if (!"".equalsIgnoreCase(cform.getSelectedadmin2())) {
				maptosave.setDistrictfield(cform.getSelectedadmin2().trim());
			}
			if (!"".equalsIgnoreCase(cform.getSelectedgeoid())) {
				maptosave.setGeoidfield(cform.getSelectedgeoid().trim());
			}
		} else {
			maptosave = new AmpMapConfig();
			maptosave.setMaptype(maptype);
			maptosave.setMapurl(cform.getSelectedurl().trim());
			if (!"".equalsIgnoreCase(cform.getSelectedadmin1())) {
				maptosave.setCountyfield(cform.getSelectedadmin1().trim());
			}
			if (!"".equalsIgnoreCase(cform.getSelectedadmin2())) {
				maptosave.setDistrictfield(cform.getSelectedadmin2().trim());
			}
			if (!"".equalsIgnoreCase(cform.getSelectedgeoid())) {
				maptosave.setGeoidfield(cform.getSelectedgeoid().trim());
			}
		}
		if (!"".equalsIgnoreCase(maptosave.getMapurl())) {
			DbHelper.saveMapConfig(maptosave);
		}
		return MapList(mapping, cform, request, response);

	}

	public ActionForward savelegend(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MapsConfigurationForm cform = (MapsConfigurationForm) form;
		FormFile myFile = cform.getLegend();
		String contentType = myFile.getContentType();
		// povertymap.png
		String fileName = "";
		if (cform.getSelectedlegend()==1){
			fileName="legend-poverty.jpg";
		}else{
			fileName="population-legend.jpg";
		}
		
		byte[] fileData = myFile.getFileData();
		String filePath = getServlet().getServletContext().getRealPath("/")
				+ "TEMPLATE/ampTemplate/img_2/gis";
		if (!fileName.equals("")) {
			System.out.println("Server path:" + filePath);

			// Create file
			
			File fileToCreate = new File(filePath, fileName);

			// If file does not exists create file
			//if (!fileToCreate.exists()) {
				FileOutputStream fileOutStream = new FileOutputStream(fileToCreate,false);

				fileOutStream.write(myFile.getFileData());
				fileOutStream.flush();
				fileOutStream.close();
			//}

		}
		return MapList(mapping, cform, request, response);

	}

}
