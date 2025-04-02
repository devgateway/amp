package org.digijava.module.esrigis.action;
/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 * @author Diego Dimunzio
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.esrigis.dbentity.AmpMapConfig;
import org.digijava.module.esrigis.form.DataDispatcherForm;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapFilter;
import org.digijava.module.esrigis.helpers.QueryUtil;

public class DataDispatcher extends MultiAction {
    private static Logger logger = Logger.getLogger(DataDispatcher.class);

    public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DataDispatcherForm maphelperform = (DataDispatcherForm) form;
        MapFilter filter = maphelperform.getFilter();
        if (filter == null || !filter.isIsinitialized()) {
            maphelperform.setFilter(QueryUtil.getNewFilter(request));
            maphelperform.getFilter().setWorkspaceOnly(true);
        }
        response.setContentType("text/json");
        return modeSelect(mapping, maphelperform, request, response);
    }

    @Override
    public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (request.getParameter("getconfig") != null) { 
            return modeGetConfiguration(mapping, form, request, response);
        }else{
            throw new  UnsupportedOperationException("Please check for commit done in AMP-21992 to restore missing code");
        }

    }
    private ActionForward modeGetConfiguration(ActionMapping mapping,ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DataDispatcherForm maphelperform = (DataDispatcherForm) form;
        List<AmpMapConfig> maps = (List<AmpMapConfig>) DbHelper.getMaps();
        JSONArray jsonArray = new JSONArray();
        
        for (Iterator iterator = maps.iterator(); iterator.hasNext();) {
            AmpMapConfig map = (AmpMapConfig) iterator.next();
            jsonArray.add(map);
        }
        
        PrintWriter pw;
        try {
            pw = response.getWriter();
            pw.write(jsonArray.toString());
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;    
        
    }
}
