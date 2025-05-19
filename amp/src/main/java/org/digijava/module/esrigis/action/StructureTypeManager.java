package org.digijava.module.esrigis.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.admin.exception.AdminException;
import org.digijava.module.aim.dbentity.AmpStructureType;
import org.digijava.module.esrigis.form.StructureTypeForm;
import org.digijava.module.esrigis.helpers.DbHelper;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

public class StructureTypeManager extends DispatchAction {
    private static Logger logger = Logger.getLogger(StructureTypeManager.class);

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {
        Collection<AmpStructureType> sts = new ArrayList<AmpStructureType>();
        sts = DbHelper.getAllStructureTypes();
        request.setAttribute("structureTypesList", sts);
        return mapping.findForward("list");
    }

    public ActionForward list(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {
        Collection<AmpStructureType> sts = new ArrayList<AmpStructureType>();
        sts = DbHelper.getAllStructureTypes();
        request.setAttribute("structureTypesList", sts);
        return mapping.findForward("list");
    }
    public ActionForward displayIcon(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        String index = request.getParameter("id");

        if (index != null) {
                try {
                    Long structureTypeId = Long.parseLong(index);
                    AmpStructureType structureType = DbHelper.getStructureType(structureTypeId);
                    ServletOutputStream os = response.getOutputStream();
                    if (structureType.getIconFile() != null) {
                        response.setContentType(structureType.getIconFileContentType());
                        os.write(structureType.getIconFile());
                        os.flush();
                    }
                    else
                    {
                        BufferedImage bufferedImage = new BufferedImage(30, 30,
                                BufferedImage.TRANSLUCENT);
                        ImageIO.write(bufferedImage, "png", os);
                        os.flush();
                    }
                } catch (NumberFormatException nfe) {
                    logger.error("Trying to parse " + index + " to int");
                }
        } else {
            BufferedImage bufferedImage = new BufferedImage(30, 30,
                    BufferedImage.TRANSLUCENT);
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(bufferedImage, "png", os);
            os.flush();
        }
        return null;
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        if (request.getParameter("id") != null) {
            Long structureTypeId = Long.parseLong(request.getParameter("id"));
            AmpStructureType structureType = DbHelper.getStructureType(structureTypeId);
            try{
                DbHelper.deleteStructureType(structureType);
            }
            catch(AdminException e){

                String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
                String locale = RequestUtils.getNavigationLanguage(request).getCode();
                
                ActionMessages errors = new ActionMessages();
                errors.add(
                        ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage(
                                "error.aim.structureTypeManager.typeBeingReferenced",
                                TranslatorWorker.translateText(e.getMessage())));
                saveErrors(request, errors);
            }
        }
        return mapping.findForward("delete");
    }

    public ActionForward add(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        StructureTypeForm structureTypeForm = (StructureTypeForm) form;
        Boolean isReset = Boolean.valueOf(request.getParameter("reset"));

        if (isReset) {
            structureTypeForm.setReset(true);
            structureTypeForm.reset(mapping, request);
//          request.getSession().removeAttribute("contentThumbnails");
            structureTypeForm.setReset(false);
        }
        return mapping.findForward("addEdit");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception

    {

        StructureTypeForm StructureTypeForm = (StructureTypeForm) form;

        if (request.getParameter("id") != null) {
            Long structureTypeId = Long.parseLong(request.getParameter("id"));
            AmpStructureType structureType = DbHelper.getStructureType(structureTypeId);
            StructureTypeForm.setAmpStructureFormId(structureTypeId);
            StructureTypeForm.setName(structureType.getName());
//          StructureTypeForm
//                  .setContentThumbnails(contentItem.getContentThumbnails());
//          request.getSession().setAttribute("contentThumbnails",
//                  StructureTypeForm.getContentThumbnails());
        }
        return mapping.findForward("addEdit");

    }

    public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        StructureTypeForm structureTypeForm = (StructureTypeForm) form;
        AmpStructureType structureType;
        boolean nameExists=false;
        AmpStructureType structure = DbHelper.getStructureTypesByName(structureTypeForm.getName());
        if(structureTypeForm.getAmpStructureFormId() != null
                && structureTypeForm.getAmpStructureFormId() > 0){
            if(structure !=null && structure.getTypeId()!=structureTypeForm.getAmpStructureFormId())
                nameExists=true;
        }else{
            if(structure!=null)
                nameExists=true;
        }
        
        if(nameExists){
            ActionMessages errors= new ActionMessages();
            errors.add("structure not unique", new ActionMessage("error.admin.structureNameMustBeUnique",TranslatorWorker.translateText("Structure with the given name already exists") ));
            saveErrors(request, errors);
        return mapping.findForward("addEdit");
        }
        
        if (structureTypeForm.getAmpStructureFormId() != null
                && structureTypeForm.getAmpStructureFormId() > 0) {
            structureType = DbHelper.getStructureType(structureTypeForm
                    .getAmpStructureFormId());
        } else {
            structureType = new AmpStructureType();
        }
        structureType.setName(structureTypeForm.getName());
        if(structureTypeForm.getIconFile().getFileData().length!=0){
            structureType.setGraphicType(structureTypeForm.getGraphicType());
            structureType.setIconFile(structureTypeForm.getIconFile().getFileData());
            structureType.setIconFileContentType(structureTypeForm.getIconFile().getContentType());
        }
//      contentItem.setContentThumbnails(StructureTypeForm.getContentThumbnails());
//      if (StructureTypeForm.getContentThumbnailsRemoved() != null
//              && StructureTypeForm.getContentThumbnailsRemoved().size() > 0)
//          DbHelper.removeThumbnails(contentItem,
//                  StructureTypeForm.getContentThumbnailsRemoved());
        DbHelper.saveStructureType(structureType);

        return mapping.findForward("save");

    }

}
