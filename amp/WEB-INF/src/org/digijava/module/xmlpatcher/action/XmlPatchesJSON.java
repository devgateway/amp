/**
 * XmlPatchesJSON.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;
import org.digijava.module.xmlpatcher.util.XmlPatcherUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
public class XmlPatchesJSON extends Action {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {
        
        
        String startIndex=request.getParameter("startIndex");
        String results=request.getParameter("results");
        String sort="patchId";
        String dir="";
        int startIndexInt=0;
        int resultsInt=10;
    
        if(startIndex!=null) startIndexInt=Integer.parseInt(startIndex);
        if(results!=null) resultsInt=Integer.parseInt(results);
        if(request.getParameter("sort")!=null) sort=request.getParameter("sort");
        if(request.getParameter("dir")!=null) dir=request.getParameter("dir");
        JSONObject json = new JSONObject();
    
        JSONArray jsonArray = new JSONArray();
        
        List<Object[]> allDiscoveredPatches = XmlPatcherUtil.getAllDiscoveredPatches(startIndexInt,resultsInt,sort,dir);
        for( Object[] patch : allDiscoveredPatches) {
             JSONObject jsonPatch = new JSONObject();
             jsonPatch.put("patchId", patch[0]);
             jsonPatch.put("discovered", sdf.format(patch[1]));
             jsonPatch.put("location", patch[2]);
             jsonPatch.put("state", XmlPatcherConstants.PatchStates.toString((Short)patch[3]));       
             jsonPatch.put("attempts",patch[4]);
             jsonArray.add(jsonPatch);
        }
        
        Integer totalRecords=XmlPatcherUtil.countAllDiscoveredPatches();
        json.put("totalRecords",totalRecords.toString());
        json.put("startIndex",Integer.toString(startIndexInt));
        json.put("pageSize", results);
        //json.put("recordsReturned",allDiscoveredPatches.size());
        json.put("sort",sort);
        json.put("dir",dir);
        
        json.put("items",jsonArray);
        
        response.setContentType("text/html");
        OutputStreamWriter outputStream =  null;
        
        try {
            outputStream = new OutputStreamWriter( response.getOutputStream(),"UTF-8");
            outputStream.write(json.toString());
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }       
        
        return null;
    }

}
