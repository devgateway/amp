

package org.digijava.module.orgProfile.action;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.orgProfile.form.OrgProfileExportOptionsForm;
import org.digijava.module.orgProfile.helper.ExportSettingHelper;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.dbentity.AmpWidgetOrgProfile;
import org.digijava.module.widget.helper.WidgetVisitor;
import org.digijava.module.widget.helper.WidgetVisitorAdapter;
import org.digijava.module.widget.util.WidgetUtil;

/**
 *
 * @author medea
 */
public class ExportOptionsAction extends DispatchAction {
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        OrgProfileExportOptionsForm orgForm = (OrgProfileExportOptionsForm) form;
        ServletContext ampContext = getServlet().getServletContext();
        List<AmpDaWidgetPlace> orgPlaces = WidgetUtil.getAllOrgProfilePlaces();
        List<ExportSettingHelper>  helpers = new ArrayList<ExportSettingHelper>();
        Iterator<AmpDaWidgetPlace> placeIter = orgPlaces.iterator();
            while (placeIter.hasNext()) {
                AmpDaWidgetPlace place = placeIter.next();
                 if (!FeaturesUtil.isVisibleFeature(place.getName(), ampContext)) {
                    continue;
                }
                AmpWidget wd = place.getAssignedWidget();
                final ArrayList<ExportSettingHelper>  widgetHelpers= new ArrayList<ExportSettingHelper>();
                if (wd != null) {
                      WidgetVisitor adapter = new WidgetVisitorAdapter() {
                        @Override
                        public void visit(AmpWidgetOrgProfile orgProfile) {
                           ExportSettingHelper helper=new  ExportSettingHelper(orgProfile,Constants.EXPORT_OPTION_CHART_DATA_SOURCE);
                           widgetHelpers.add(helper);
                        }
                    };
                    wd.accept(adapter);
                     if (!widgetHelpers.isEmpty()) {
                         helpers.add(widgetHelpers.get(0));

                     }
                }
            }
        orgForm.setSelectedFormatOfExport(Constants.EXPORT_TO_WORD);
        orgForm.setHelpers(helpers);
        return mapping.findForward("forward");
    }

    public ActionForward export(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        OrgProfileExportOptionsForm orgForm = (OrgProfileExportOptionsForm) form;
        request.setAttribute("orgProfileExportSettings", orgForm.getHelpers());
        request.setAttribute("orgProfileMonochrome", orgForm.isMonochromeOption());
        	
        String mappingForward="";
        switch(orgForm.getSelectedFormatOfExport()){
            case Constants.EXPORT_TO_WORD: mappingForward="exportToWord";break;
            case Constants.EXPORT_TO_PDF:  mappingForward="exportToPDF"; break;

        }
        return mapping.findForward(mappingForward);
     
    }
}
