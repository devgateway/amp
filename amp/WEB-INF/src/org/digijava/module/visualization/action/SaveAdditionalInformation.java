package org.digijava.module.visualization.action;

import java.io.OutputStreamWriter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.visualization.form.SaveInfoForm;
import org.digijava.module.visualization.util.DbUtil;

public class SaveAdditionalInformation  extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {
		SaveInfoForm infoForm = (SaveInfoForm)form;
		if (infoForm.getType() != null) {
			DbUtil.saveAdditionalInfo(infoForm.getId(), infoForm.getType(), infoForm.getBackground(), infoForm.getDescription(), infoForm.getKeyAreas());
			response.setContentType("text/json-comment-filtered");
			OutputStreamWriter outputStream = null;
			
			JSONObject root = new JSONObject();
			root.put("id", infoForm.getId());
			root.put("type", infoForm.getType());
			root.put("background", infoForm.getBackground());
			root.put("description", infoForm.getDescription());
			root.put("keyAreas", infoForm.getKeyAreas());
			
			try {
				outputStream = new OutputStreamWriter(response.getOutputStream(),"UTF-8");
				outputStream.write(root.toString());
			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
			}
		}
		return null;
	}

}
