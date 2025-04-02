package org.digijava.module.esrigis.action;

/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 * @author Diego Dimunzio
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.esrigis.dbentity.AmpMapConfig;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.esrigis.helpers.MapConstants;

public class EsriProxy extends Action {
    
    
    /*
     * "<url>[,<token>]" For ex. (secured server):
     * "http://myserver.mycompany.com/arcgis/rest/services,ayn2C2iPvqjeqWoXwV6rjmr43kyo23mhIPnXz2CEiMA6rVu0xR0St8gKsd0olv8a"
     * For ex. (non-secured server):
     * "http://sampleserver1.arcgisonline.com/arcgis/rest/services"
     */
    
    
    private String[] serverUrls;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        AmpMapConfig map = DbHelper.getMapByType(MapConstants.MapType.GEOLOCATOR_SERVICE);
        serverUrls[MapConstants.MapType.GEOLOCATOR_SERVICE]=map.getMapUrl();
        try {
            String reqUrl = request.getQueryString();
            boolean allowed = false;
            String token = null;
            for (String surl : serverUrls) {
                String[] stokens = surl.split("\\s*,\\s*");
                if (reqUrl.toLowerCase().contains(stokens[0].toLowerCase())) {
                    allowed = true;
                    if (stokens.length >= 2 && stokens[1].length() > 0)
                        token = stokens[1];
                    break;
                }
            }
            if (!allowed) {
                response.setStatus(403);
                return null;
            }
            if (token != null) {
                reqUrl = reqUrl + (reqUrl.indexOf("?") > -1 ? "&" : "?")
                        + "token=" + token;
            }
            URL url = new URL(reqUrl);
            
            String data = "";
            for(Enumeration e=request.getParameterNames(); e.hasMoreElements(); )
            {
               String paramName = (String)e.nextElement();
               String values = request.getParameter(paramName);
               data += "&" + URLEncoder.encode(paramName, "UTF-8") + "=" + URLEncoder.encode(values, "UTF-8");
            } 
            
            //send data to the server
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            wr.close();
            
            //Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            response.setContentType("text/json-comment-filtered");
            PrintWriter pw = response.getWriter();
            while ((line = rd.readLine()) != null) {
                JSON jso = JSONSerializer.toJSON(line);
                pw.write(jso.toString());
                
            }
            pw.flush();
            pw.close();
            rd.close();
            
        } catch (Exception e) {
            response.setStatus(500);
        }

        return null;
    }
}
