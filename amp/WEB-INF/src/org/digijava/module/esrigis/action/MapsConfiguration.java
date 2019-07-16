package org.digijava.module.esrigis.action;

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

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
            mapForm.setLegendNotes(map.getLegendNotes());
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
        if (mapForm.getMapSubType().equals(MapConstants.MapSubType.INDICATOR)) {
            //only if map is indicator we save the legend if its indicator
            map.setLegendNotes(mapForm.getLegendNotes());
        }
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
//                      response.setContentType(structureType.getIconFileContentType());
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
    
}
