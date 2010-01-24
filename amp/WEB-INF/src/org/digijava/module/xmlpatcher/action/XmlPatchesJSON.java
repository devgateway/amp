/**
 * XmlPatchesJSON.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.action;

import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatch;
import org.digijava.module.xmlpatcher.util.XmlPatcherUtil;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
public class XmlPatchesJSON extends Action {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
		
		
		String startIndex=request.getParameter("startIndex");
		String results=request.getParameter("results");
		int startIndexInt=0;
		int resultsInt=10;
		if(startIndex!=null) startIndexInt=Integer.parseInt(startIndex);
		if(results!=null) resultsInt=Integer.parseInt(results);
	
        JSONObject json = new JSONObject();
    
        JSONArray jsonArray = new JSONArray();
        
        List<AmpXmlPatch> allDiscoveredPatches = XmlPatcherUtil.getAllDiscoveredPatches(startIndexInt,resultsInt);
        for( AmpXmlPatch patch : allDiscoveredPatches) {
        	 JSONObject jsonPatch = new JSONObject();
        	 jsonPatch.put("ID", patch.getPatchId());
        	 jsonPatch.put("Discovered", sdf.format(patch.getDiscovered()));
        	 jsonPatch.put("Location", patch.getLocation());
        	 jsonPatch.put("State", patch.getState());       	 
        	 jsonArray.add(jsonPatch);
        }
        
        Integer totalRecords=XmlPatcherUtil.countAllDiscoveredPatches();
        json.put("recordsReturned",allDiscoveredPatches.size());
        json.put("totalRecords",totalRecords);
        json.put("startIndex",startIndexInt);
        json.put("sort",null);
        json.put("dir","asc");
        json.put("pageSize", 10);
        
        json.put("items",jsonArray);
        
		response.setContentType("text/json-comment-filtered");
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
