package org.digijava.module.widget.action;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidgetOrgProfile;
import org.digijava.module.widget.form.OrgProfileWidgetForm;
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
        
        OrgProfileWidgetForm orgForm = (OrgProfileWidgetForm) form;
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
        AmpWidgetOrgProfile orgProfWidget = null;
        List<AmpDaWidgetPlace> oldPlaces = null;
        List<AmpDaWidgetPlace> newPlaces = null;

        if (orgForm.getId() == null||orgForm.getId()==0) {
            orgProfWidget = new AmpWidgetOrgProfile();
        } else {
            orgProfWidget = OrgProfileWidgetUtil.getAmpWidgetOrgProfile(orgForm.getId());
            oldPlaces = WidgetUtil.getWidgetPlaces(orgProfWidget.getId());
        }       
        /*
         * Name is used in  the Widget Place Manager,
         * We could force the user to enter it manually,
         * but it will be hard  to explain them why we need specify name twice 
         * cause from user perspective name and type are the same...
        */
        String name="";
        switch(orgForm.getType().intValue()){
            case WidgetUtil.ORG_PROFILE_SUMMARY: name="Summary"; break;
             case WidgetUtil.ORG_PROFILE_TYPE_OF_AID: name="Type of Aid"; break;
              case WidgetUtil.ORG_PROFILE_PLEDGES_COMM_DISB: name="Pledges/Comm/Disb"; break;
               case WidgetUtil.ORG_PROFILE_ODA_PROFILE: name="ODA Profile"; break;
                case WidgetUtil.ORG_PROFILE_SECTOR_BREAKDOWN: name="Sector Breakdown"; break;
                  case WidgetUtil.ORG_PROFILE_REGIONAL_BREAKDOWN: name="Regional Breakdown"; break;
                   case WidgetUtil.ORG_PROFILE_PARIS_DECLARATION: name="Paris Declaration"; break;
                    case WidgetUtil.ORG_PROFILE_AID_PREDICTIBLITY: name="Aid predictability"; break;
        }
        if (OrgProfileWidgetUtil.orgProfileWidgetExists(orgForm.getType(), orgProfWidget.getId())) {
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.widget.widgetOrgProfile.widgetOrgTypeExist"));
            saveErrors(request, errors);
            return mapping.findForward("create");
        }
        orgProfWidget.setType(orgForm.getType());
        orgProfWidget.setName(name);
        OrgProfileWidgetUtil.saveWidget(orgProfWidget);
        if (orgForm.getSelPlaces() != null && orgForm.getSelPlaces().length > 0) {
            newPlaces = WidgetUtil.getPlacesWithIDs(orgForm.getSelPlaces());
            if (oldPlaces != null && newPlaces != null) {
                Collection<AmpDaWidgetPlace> deleted = AmpCollectionUtils.split(oldPlaces, newPlaces, new WidgetUtil.PlaceKeyWorker());
                WidgetUtil.updatePlacesWithWidget(oldPlaces, orgProfWidget);
                WidgetUtil.updatePlacesWithWidget(deleted, null);
            } else {
                WidgetUtil.updatePlacesWithWidget(newPlaces, orgProfWidget);
            }
        } else {
            WidgetUtil.clearPlacesForWidget(orgProfWidget.getId());
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
