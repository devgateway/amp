package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.dimension.SectorDimension;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.util.SectorUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Iterator;

public class DeleteSector extends Action {

    private static Logger logger = Logger.getLogger(GetSectors.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }

        /*
         * modyfed by Xaxan
         */
        AddSectorForm deleteSectorForm = (AddSectorForm) form;
        String event = request.getParameter("event");
        String ampSectorId = request.getParameter("ampSectorId");
        String schemeId = request.getParameter("schemeId");
        logger.debug("Event================" + event + "\nampSectorId==================" + ampSectorId
                + "\nschemeId=====================" + schemeId);
        Integer _schemeId = new Integer(schemeId);
        Long __schemeId = new Long(schemeId);
        Long longSchemeId = new Long(ampSectorId);
        String forward = null;
        Long id = null;
        Collection subSectors = null;
        AmpSector aSector = new AmpSector();
        // aSector = SectorUtil.getAmpSector(deleteSectorForm.getAmpSectorId());
        aSector = SectorUtil.getAmpSector(longSchemeId);
        /*
         * String schemeId = request.getParameter("schemeId"); Integer _schemeId
         * = new Integer(schemeId); Long longSchemeId = new Long(schemeId);
         */

        // AmpSector aSector = new AmpSector();
        // aSector = SectorUtil.getAmpSector(deleteSectorForm.getAmpSectorId());
        if (event.equals("delete")) {
            id = deleteSectorForm.getAmpSectorId();
            int auxActivities = SectorUtil.getActivitiesForSector(id).size();
            if (auxActivities == 0) {
                if (SectorUtil.getAllChildSectors(aSector.getAmpSectorId()).isEmpty()) {
                    logger.debug("Sector dont have any child sector:");
                    ARUtil.clearDimension(SectorDimension.class);
                    SectorUtil.deleteSector(id);
                    Collection schemeGot = SectorUtil.getEditScheme(_schemeId);
                    if (aSector.getParentSectorId() == null) {
                        // if(SectorUtil.getAllChildSectors(aSector.getAmpSectorId()).isEmpty()){
                        // SectorUtil.deleteSector(id);
                        deleteSectorForm.setFormFirstLevelSectors(SectorUtil.getSectorLevel1(_schemeId));
                        Iterator itr = schemeGot.iterator();
                        while (itr.hasNext()) {
                            AmpSectorScheme ampScheme = (AmpSectorScheme) itr.next();
                            deleteSectorForm.setSecSchemeId(ampScheme.getAmpSecSchemeId());
                            deleteSectorForm.setSecSchemeName(ampScheme.getSecSchemeName());
                            deleteSectorForm.setSecSchemeCode(ampScheme.getSecSchemeCode());
                            deleteSectorForm.setParentId(ampScheme.getAmpSecSchemeId());
                        }
                        deleteSectorForm.setSectorName(aSector.getName());
                        deleteSectorForm.setSectorCode(aSector.getSectorCode());
                        if (deleteSectorForm.getSectorId() == null)
                            deleteSectorForm.setSectorId(new Long(schemeId));

                        /*
                         * } else { ActionMessages errors = new
                         * ActionMessages(); errors.add("title", new
                         * ActionMessage
                         * ("error.aim.deleteScheme.schemeSelected"));
                         * saveErrors(request, errors); //forward="cantDelete";
                         * }
                         */
                        logger.debug("level 1 deleted");
                        forward = "levelOneSectorDeleted";
                    } else if (aSector.getParentSectorId().getParentSectorId() == null) {
                        // if(SectorUtil.getAllChildSectors(aSector.getAmpSectorId()).isEmpty()){
                        // SectorUtil.deleteSector(id);
                        logger.debug("second level");
                        subSectors = SectorUtil.getAllChildSectors(__schemeId);
                        deleteSectorForm.setSubSectors(subSectors);
                        Iterator itr = subSectors.iterator();
                        while (itr.hasNext()) {
                            AmpSector ampScheme = (AmpSector) itr.next();
                            // deleteSectorForm.set
                            deleteSectorForm.setAmpSectorId(ampScheme.getAmpSectorId());
                            deleteSectorForm.setParentId(ampScheme.getAmpSectorId());
                            deleteSectorForm.setParentSectorId(ampScheme.getParentSectorId().getAmpSectorId());
                            deleteSectorForm.setSectorId(ampScheme.getParentSectorId().getAmpSectorId());
                        }
                        deleteSectorForm.setSectorName(aSector.getParentSectorId().getName());
                        deleteSectorForm.setSectorCode(aSector.getParentSectorId().getSectorCode());
                        deleteSectorForm.setSectorCodeOfficial(aSector.getParentSectorId().getSectorCodeOfficial());
                        if (deleteSectorForm.getSectorId() == null)
                            deleteSectorForm.setSectorId(new Long(schemeId));

                        /*
                         * } else { ActionMessages errors = new
                         * ActionMessages(); errors.add("title", new
                         * ActionMessage
                         * ("error.aim.deleteScheme.schemeSelected"));
                         * saveErrors(request, errors); //forward="cantDelete";
                         * }
                         */
                        logger.debug("level 2 deleted");
                        forward = "levelTwoSectorDeleted";
                    } else if (aSector.getParentSectorId().getParentSectorId().getParentSectorId() == null) {
                        // if(SectorUtil.getAllChildSectors(aSector.getAmpSectorId()).isEmpty()){
                        // SectorUtil.deleteSector(id);
                        logger.debug("3 rd level");
                        subSectors = SectorUtil.getAllChildSectors(__schemeId);
                        deleteSectorForm.setSubSectors(subSectors);
                        Iterator itr = subSectors.iterator();
                        while (itr.hasNext()) {
                            AmpSector ampScheme = (AmpSector) itr.next();
                            // deleteSectorForm.set
                            deleteSectorForm.setAmpSectorId(ampScheme.getAmpSectorId());
                            deleteSectorForm.setParentId(ampScheme.getAmpSectorId());
                            deleteSectorForm.setParentSectorId(ampScheme.getParentSectorId().getAmpSectorId());
                            deleteSectorForm.setSectorId(ampScheme.getParentSectorId().getAmpSectorId());
                        }
                        deleteSectorForm.setSectorName(aSector.getParentSectorId().getName());
                        deleteSectorForm.setSectorCode(aSector.getParentSectorId().getSectorCode());
                        deleteSectorForm.setSectorCodeOfficial(aSector.getParentSectorId().getSectorCodeOfficial());
                        if (deleteSectorForm.getSectorId() == null)
                            deleteSectorForm.setSectorId(new Long(schemeId));
                        /*
                         * } else { ActionMessages errors = new
                         * ActionMessages(); errors.add("title", new
                         * ActionMessage
                         * ("error.aim.deleteScheme.schemeSelected"));
                         * saveErrors(request, errors); //forward="cantDelete";
                         * }
                         */
                        logger.debug("level 3 deleted");
                        forward = "levelThreeSectorDeleted";
                    }
                    /*
                     * Iterator itr = schemeGot.iterator(); while
                     * (itr.hasNext()) { AmpSectorScheme ampScheme =
                     * (AmpSectorScheme) itr.next(); deleteSectorForm
                     * .setSecSchemeId(ampScheme.getAmpSecSchemeId());
                     * deleteSectorForm
                     * .setSecSchemeName(ampScheme.getSecSchemeName());
                     * deleteSectorForm
                     * .setSecSchemeCode(ampScheme.getSecSchemeCode());
                     * deleteSectorForm
                     * .setParentId(ampScheme.getAmpSecSchemeId());
                     * deleteSectorForm.setParentSectorId(ampScheme.get) }
                     */
                    // deleteSectorForm.setFormFirstLevelSectors(SectorUtil.getSectorLevel1(_schemeId));
                } else {
                    ActionMessages errors = new ActionMessages();
                    errors.add("title", new ActionMessage("error.aim.deleteScheme.sectorSelected"));
                    saveErrors(request, errors);
                    forward = "cantDelete";
                }
            } else {
                ActionMessages errors = new ActionMessages();
                String auxMsg = (auxActivities == 1) ? " activity." : " activities.";
                /*
                 * String existingActivities =
                 * "Can not delete sector because its being referenced by " +
                 * auxActivities + auxMsg;
                 */
                String existingActivities = "title";
                errors.add(existingActivities, new ActionMessage("error.aim.deleteSector.hasActivity"));
                saveErrors(request, errors);
                forward = "cantDelete";
                logger.info("Can not delete sector because it has activities.");
            }
        }
        return mapping.findForward(forward);
    }
}
