

package org.digijava.module.orgProfile.helper;

import org.digijava.module.widget.dbentity.AmpWidgetOrgProfile;


public class ExportSettingHelper {
    private AmpWidgetOrgProfile widget;
    private int selectedTypeOfExport;
    public ExportSettingHelper(){}
    public ExportSettingHelper(AmpWidgetOrgProfile widget,int selectedTypeOfExport){
        this.widget=widget;
        this.selectedTypeOfExport=selectedTypeOfExport;

    }
    public int getSelectedTypeOfExport() {
        return selectedTypeOfExport;
    }

    public void setSelectedTypeOfExport(int selectedTypeOfExport) {
        this.selectedTypeOfExport = selectedTypeOfExport;
    }

    public AmpWidgetOrgProfile getWidget() {
        return widget;
    }

    public void setWidget(AmpWidgetOrgProfile widget) {
        this.widget = widget;
    }


}
