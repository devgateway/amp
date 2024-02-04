package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.dimension.NPODimension;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author Sebas New add/edit theme action
 */
public class AddTheme extends Action {
    private static Logger logger = Logger.getLogger(AddTheme.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }
        String event = request.getParameter("event");

        // new top level program
        if (event != null && event.equals("add")) {
            return addnew(mapping, form, request, response);
        }

        // save a top level program
        if (event.equals("save")) {
            return save(mapping, form, request, response);
        }
        // Edit any level program
        if (event != null && event.equals("edit")) {
            return edit(mapping, form, request, response);
        }
        // save any edited program
        if (event.equals("saveEdit")) {
            return saveEdit(mapping, form, request, response);
        }
        // prepare for new sub program
        if (event.equals("addSubProgram")) {
            return addSubProgram(mapping, form, request, response);
        }
        // Save the new sub program
        if (event.equals("saveSubProgram")) {
            return save(mapping, form, request, response);
        }

        return null;
    }

    /**
     * Prepare a blank form to enter a new program
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward addnew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ThemeForm themeForm = (ThemeForm) form;
        String event = request.getParameter("event");
        themeForm.setProgramName(null);
        themeForm.setProgramCode(null);
        themeForm.setBudgetProgramCode(null);
        themeForm.setProgramDescription(null);
        themeForm.setProgramTypeCategValId(0L);
        themeForm.setPrgLanguage(null);
        themeForm.setVersion(null);
        themeForm.setEvent("save");
        return mapping.findForward("addEditForm");

    }

    /**
     * Save the new program
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ThemeForm themeForm = (ThemeForm) form;
        if(checkExisting(mapping,form,request,response)) return  mapping.findForward("addEditForm");
        AmpTheme ampTheme = new AmpTheme();
        fillTheme(ampTheme, themeForm);
        ARUtil.clearDimension(NPODimension.class);
        DbUtil.add(ampTheme);
        themeForm.setEvent("close");
        return mapping.findForward("addEditForm");
    }

    /**
     * Prepare the form to edit a program
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ThemeForm themeForm = (ThemeForm) form;
        String event = request.getParameter("event");
        Long id = Long.parseLong(request.getParameter("themeId"));
        AmpTheme ampTheme = ProgramUtil.getThemeById(id);
        fillForm(themeForm, ampTheme);
        themeForm.setEvent("saveEdit");
        return mapping.findForward("addEditForm");

    }

    /**
     * Checks if a different theme with same name and code exists
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public boolean checkExisting(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ThemeForm themeForm = (ThemeForm) form;
        
        boolean existing = false;
        AmpTheme theme = ProgramUtil.getTheme(themeForm.getProgramName());
        if (theme != null) {
            
            if (themeForm.getThemeId() != null
                    && !themeForm.getThemeId().equals(theme.getAmpThemeId())
                    && theme.getName().equals(themeForm.getProgramName())
                    && !Boolean.TRUE.equals(theme.getDeleted()))
                existing = true;

            if (themeForm.getThemeId() == null
                    && theme.getName().equals(themeForm.getProgramName())
                    && !Boolean.TRUE.equals(theme.getDeleted()))
                existing = true;
        }

        AmpTheme themeByCode = ProgramUtil.getThemeByCode(themeForm
                .getProgramCode());
        if (themeByCode != null) {
            if (themeByCode != null
                    && themeForm.getThemeId() != null
                    && !themeForm.getThemeId().equals(
                            themeByCode.getAmpThemeId())
                    && (themeByCode.getThemeCode().equals(themeForm
                            .getProgramCode()))
                    && !Boolean.TRUE.equals(theme.getDeleted()))
                existing = true;

            if (themeForm.getThemeId() == null
                    && themeByCode.getThemeCode().equals(
                            themeForm.getProgramCode())
                    && !Boolean.TRUE.equals(theme.getDeleted()))
                existing = true;
        }

        if (existing) {
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.aim.theme.existingTheme"));
            saveErrors(request, errors);
        }

        return existing;
    }
    
    
    /**
     * Save the current edited program
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward saveEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ThemeForm themeForm = (ThemeForm) form;
        if(checkExisting(mapping,form,request,response)) return  mapping.findForward("addEditForm");
        String event = request.getParameter("event");
        AmpTheme tempTheme = new AmpTheme();
        fillTheme(tempTheme, themeForm);
        ARUtil.clearDimension(NPODimension.class);
        ProgramUtil.updateTheme(tempTheme);
        themeForm.setEvent("close");
        return mapping.findForward("addEditForm");
    }

    /**
     * Prepare a blank form to enter a new sub program
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward addSubProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ThemeForm themeForm = (ThemeForm) form;
        Long id = new Long(Long.parseLong(request.getParameter("themeId")));
        int indlevel = Integer.parseInt(request.getParameter("indlevel"));

        themeForm.setParentId(id);
        
        AmpTheme parent= ProgramUtil.getThemeById(themeForm.getParentId());
        themeForm.setParentProgram(parent.getName());
        
        themeForm.setPrgLevel(indlevel);
        themeForm.setProgramName(null);
        themeForm.setProgramCode(null);
        themeForm.setBudgetProgramCode(null);
        themeForm.setProgramDescription(null);
        themeForm.setProgramTypeCategValId(0L);
        themeForm.setPrgLanguage(null);
        themeForm.setVersion(null);

        // next event
        themeForm.setEvent("saveSubProgram");
        return mapping.findForward("addEditForm");
    }

    /**
     * Save the new sub program
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward saveSubProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ThemeForm themeForm = (ThemeForm) form;

        AmpTheme ampTheme = new AmpTheme();
        fillTheme(ampTheme, themeForm);
        ARUtil.clearDimension(NPODimension.class);
        DbUtil.add(ampTheme);

        themeForm.setEvent("");
        return mapping.findForward("saved");
    }

    /**
     * Prepare the form to edit a sub program
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward editSubProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ThemeForm themeForm = (ThemeForm) form;
        String event = request.getParameter("event");
        Long id = new Long(Long.parseLong(request.getParameter("themeId")));
        Long rootid = new Long(Long.parseLong(request.getParameter("rootId")));
        AmpTheme ampTheme = ProgramUtil.getThemeById(id);
        themeForm.setRootId(rootid);
        themeForm.setEvent("saveEditSubProgram");
        fillForm(themeForm, ampTheme);
        return mapping.findForward("addEditForm");
    }

    /**
     * Save the current edited sub program
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward saveEditSubProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        return null;
    }

    /**
     * Fill theme using form values
     * 
     * @param ampTheme
     * @param themeForm
     */
    private void fillTheme(AmpTheme ampTheme, ThemeForm themeForm) {
        ampTheme.setAmpThemeId(themeForm.getThemeId());
        ampTheme.setName(themeForm.getProgramName());
        ampTheme.setThemeCode(themeForm.getProgramCode());
        ampTheme.setBudgetProgramCode(themeForm.getBudgetProgramCode());
        ampTheme.setDescription(themeForm.getProgramDescription());
        ampTheme.setTypeCategoryValue(CategoryManagerUtil.getAmpCategoryValueFromDb(themeForm.getProgramTypeCategValId()));
        ampTheme.setLeadAgency(themeForm.getProgramLeadAgency());
        ampTheme.setTargetGroups(themeForm.getProgramTargetGroups());
        ampTheme.setBackground(themeForm.getProgramBackground());
        ampTheme.setObjectives(themeForm.getProgramObjectives());
        ampTheme.setOutputs(themeForm.getProgramOutputs());
        ampTheme.setBeneficiaries(themeForm.getProgramBeneficiaries());
        ampTheme.setEnvironmentConsiderations(themeForm.getProgramEnvironmentConsiderations());
        ampTheme.setShowInRMFilters(themeForm.getShowInRMFilters());

        if (themeForm.getParentId() != null && themeForm.getParentId().longValue()!=0 ) {
            ampTheme.setParentThemeId(ProgramUtil.getThemeById(themeForm.getParentId()));
            int indlevel = themeForm.getPrgLevel();
            int level = indlevel + 1;
            ampTheme.setIndlevel(new Integer(level));
        } else {
            ampTheme.setParentThemeId(null);
            ampTheme.setIndlevel(0);
        }

        ampTheme.setLanguage(null);
        ampTheme.setVersion(null);
        themeForm.setProgramName(null);
        themeForm.setProgramCode(null);
        themeForm.setBudgetProgramCode(null);
        themeForm.setProgramDescription(null);
        themeForm.setProgramTypeCategValId(0L);
        ampTheme.setExternalFinancing(themeForm.getProgramExternalFinancing());
        ampTheme.setInternalFinancing(themeForm.getProgramInernalFinancing());
        ampTheme.setTotalFinancing(themeForm.getProgramTotalFinancing());

    }

    /**
     * fill form using theme values
     * 
     * @param themeForm
     * @param ampTheme
     */
    private void fillForm(ThemeForm themeForm, AmpTheme ampTheme) {

        themeForm.setThemeId(ampTheme.getAmpThemeId());
        if (ampTheme.getParentThemeId() != null) {
            AmpTheme parent= ampTheme.getParentThemeId();
            themeForm.setParentId(parent.getAmpThemeId());
            themeForm.setParentProgram(parent.getName());
        }

        themeForm.setProgramName(ampTheme.getName());
        themeForm.setProgramCode(ampTheme.getThemeCode());
        themeForm.setBudgetProgramCode(ampTheme.getBudgetProgramCode());
        themeForm.setProgramDescription(ampTheme.getDescription());
        if (ampTheme.getTypeCategoryValue() != null)
            themeForm.setProgramTypeCategValId(ampTheme.getTypeCategoryValue().getId());
        else {
            logger.error("AmpTheme " + ampTheme.getName() + " has Program Type null which should not be allowed.");
            themeForm.setProgramTypeCategValId(0L);
        }
        themeForm.setProgramLeadAgency(ampTheme.getLeadAgency());
        themeForm.setProgramBackground(ampTheme.getBackground());
        themeForm.setProgramTargetGroups(ampTheme.getTargetGroups());
        themeForm.setProgramObjectives(ampTheme.getObjectives());
        themeForm.setProgramOutputs(ampTheme.getOutputs());
        themeForm.setProgramBeneficiaries(ampTheme.getBeneficiaries());
        themeForm.setProgramEnvironmentConsiderations(ampTheme.getEnvironmentConsiderations());
        themeForm.setShowInRMFilters (ampTheme.getShowInRMFilters());

        if (ampTheme.getExternalFinancing() == null) {
            themeForm.setProgramExternalFinancing(Double.valueOf(0));
        } else {
            themeForm.setProgramExternalFinancing(ampTheme.getExternalFinancing());
        }
        if (ampTheme.getInternalFinancing() == null) {
            themeForm.setProgramInernalFinancing(Double.valueOf(0));
        } else {
            themeForm.setProgramInernalFinancing(ampTheme.getInternalFinancing());
        }
        if (ampTheme.getTotalFinancing() == null) {
            themeForm.setProgramTotalFinancing(Double.valueOf(0));
        } else {
            themeForm.setProgramTotalFinancing(ampTheme.getTotalFinancing());
        }
    }
}
