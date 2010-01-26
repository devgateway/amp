package org.digijava.module.widget.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.orgProfile.util.OrgProfileUtil;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidgetOrgProfile;
import org.digijava.module.widget.form.OrgProfileWidgetForm;
import org.digijava.module.widget.helper.WidgetUpdatePlaceHelper;
import org.digijava.module.widget.util.OrgProfileWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;

/**
 *
 * @author medea
 */
public class OrgProfileManager  extends DispatchAction {
       @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return viewAll(mapping, form, request, response);
    }

    /*
     * forwards to the page where user can view all pages 
     */
    public ActionForward viewAll(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
       
    	HttpSession session = request.getSession();
		if (!RequestUtils.isAdmin(response, session, request)) {
			return null;
		}   

    	OrgProfileWidgetForm orgForm = (OrgProfileWidgetForm) form;
    	orgForm.setPlaces(WidgetUtil.getAllOrgProfilePlaces());
        orgForm.setOrgProfilePages(OrgProfileWidgetUtil.getAllOrgProfileWidgets());
        return mapping.findForward("forward");

    }
    
      /*
     * forwards to the page where user can view all pages 
     */
    public ActionForward create(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        OrgProfileWidgetForm orgForm = (OrgProfileWidgetForm) form;
        orgForm.setType(null);
        orgForm.setId(null);
        orgForm.setPlaces(WidgetUtil.getAllOrgProfilePlaces());
        orgForm.setSelPlaces(null);
        return mapping.findForward("create");

    }
    
    /*
     * forwards to the page where user can view all pages 
     */
    public ActionForward update(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        OrgProfileWidgetForm orgForm = (OrgProfileWidgetForm) form;
        AmpWidgetOrgProfile orgProfWidget = OrgProfileWidgetUtil.getAmpWidgetOrgProfile(orgForm.getId());
        orgForm.setType(orgProfWidget.getType());
        orgForm.setPlaces(WidgetUtil.getAllOrgProfilePlaces());
        List<AmpDaWidgetPlace> places = WidgetUtil.getWidgetPlaces(orgProfWidget.getId());
        if (places == null) {
            orgForm.setSelPlaces(new Long[0]);
        } else {
            Long[] placeIDs = new Long[places.size()];
            int i = 0;
            for (AmpDaWidgetPlace place : places) {
                placeIDs[i++] = place.getId();
            }
            orgForm.setSelPlaces(placeIDs);
        }
        return mapping.findForward("create");

    }
    
     /*
     * save Org profile 
     */
    public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        OrgProfileWidgetForm orgForm = (OrgProfileWidgetForm) form;          
        String[] selPlaceId = orgForm.getSelectedId().split(",");
        for(int i=0;i<selPlaceId.length;i++ ){
            String placeId=selPlaceId[i];
            Collection<AmpDaWidgetPlace> places=new ArrayList<AmpDaWidgetPlace>();
            AmpDaWidgetPlace place=WidgetUtil.getPlace(Long.parseLong(placeId));
            places.add(place);
            AmpWidgetOrgProfile widget=orgForm.getOrgProfilePages().get(i);
            WidgetUtil.updatePlacesWithWidget(places,widget);
        }
       return viewAll(mapping, form, request, response);

    }
    
    /*
     * delete Org profile 
     */
    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        OrgProfileWidgetForm orgForm = (OrgProfileWidgetForm) form;
        AmpWidgetOrgProfile orgWidget = OrgProfileWidgetUtil.getAmpWidgetOrgProfile(orgForm.getId());
        List<AmpDaWidgetPlace> places = WidgetUtil.getWidgetPlaces(orgWidget.getId());
        if (places != null && places.size() > 0) {
            WidgetUtil.updatePlacesWithWidget(places, null);
        }
        OrgProfileWidgetUtil.delete(orgWidget);
        return viewAll(mapping, form, request, response);

    }
    

}
