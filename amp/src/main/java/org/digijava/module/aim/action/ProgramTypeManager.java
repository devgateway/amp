/*
 * Program Type.java
 

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpProgramType;
import org.digijava.module.aim.form.ProgramTypeForm;
import org.digijava.module.aim.util.ProgramUtil;

*//**
 * 
 * @deprecated Use Category Manager instead.
 *
 *//*

public class ProgramTypeManager extends Action {
    
    private static Logger logger = Logger.getLogger(ProgramTypeManager.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) {
        
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }
        ProgramTypeForm progForm = (ProgramTypeForm) form;
        String event = request.getParameter("event");
        if(event!=null && event.equals("add"))
        {
            logger.info("in adding :P");
            progForm.setName(null);
            progForm.setDescription(null);
            return mapping.findForward("add");
        }
        if(event!=null && event.equals("saveNewPrgType"))
        {
            AmpProgramType ampPrg = new AmpProgramType();
            ampPrg.setTitle(progForm.getName());
            ampPrg.setDescription(progForm.getDescription());
            ProgramUtil.saveNewProgramType(ampPrg);
        }
        if(event!=null && event.equals("editPrgType"))
        {
            Long id = new Long (request.getParameter("prgTypeId"));
            Collection col = ProgramUtil.getProgramTypeForEdititng(id);
            Iterator itr = col.iterator();
            while(itr.hasNext())
            {
                AmpProgramType amp = (AmpProgramType)itr.next();
                logger.info(" this is the name "+ amp.getTitle());
                logger.info(" htis is the Id "+ amp.getAmpProgramId());
                progForm.setName(amp.getTitle());
                progForm.setId(amp.getAmpProgramId());
                progForm.setDescription(amp.getDescription());
            }
            return mapping.findForward("edit");
            
            
        }
        if(event!=null && event.equals("updatePrgType"))
        {
            AmpProgramType ampPrg = new AmpProgramType();
            Long id = new Long( request.getParameter("ProgramTypeId"));
            logger.info(" this is the name "+ progForm.getName());
            logger.info(" this is the ID "+ id);
            ampPrg.setAmpProgramId(id);
            ampPrg.setTitle(progForm.getName());
            ampPrg.setDescription(progForm.getDescription());
            ProgramUtil.updateProgramType(ampPrg);
        }
        if(event!=null && event.equals("delete"))
        {
            AmpProgramType ampPrg = new AmpProgramType();
            Long id = new Long( request.getParameter("prgTypeId"));
            logger.info(" this is the ID in delete... "+ id);
            ampPrg.setAmpProgramId(id);
            ampPrg.setTitle(progForm.getName());
            ampPrg.setDescription(progForm.getDescription());
            ProgramUtil.deleteProgramType(ampPrg);
        }
        
        progForm.setProgramNames(ProgramUtil.getProgramTypes());
        logger.info(" in here."); 
        return mapping.findForward("forward");
    }
}
*/
