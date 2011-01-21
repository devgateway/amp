package org.digijava.module.orgProfile.form;

import java.util.List;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.orgProfile.helper.ExportSettingHelper;

/**
 *
 * @author medea
 */
public class OrgProfileExportOptionsForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private List<ExportSettingHelper> helpers;
    private int selectedFormatOfExport;
    private boolean monochromeOption;
    private String actionType;

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

   
    public int getSelectedFormatOfExport() {
        return selectedFormatOfExport;
    }

    public void setSelectedFormatOfExport(int selectedFormatOfExport) {
        this.selectedFormatOfExport = selectedFormatOfExport;
    }

    public List<ExportSettingHelper> getHelpers() {
        return helpers;
    }

    public void setHelpers(List<ExportSettingHelper> helpers) {
        this.helpers = helpers;
    }

	public void setMonochromeOption(boolean monochromeOption) {
		this.monochromeOption = monochromeOption;
	}

	public boolean isMonochromeOption() {
		
		return monochromeOption;
	}
	public void reset(ActionMapping mapping,
            javax.servlet.http.HttpServletRequest request){
    	monochromeOption = false;
    }	
}
