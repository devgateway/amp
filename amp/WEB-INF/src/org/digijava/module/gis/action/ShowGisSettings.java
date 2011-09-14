package org.digijava.module.gis.action;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.gis.dbentity.GisSettings;
import org.digijava.module.gis.form.GisSettingsForm;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.gis.util.GisUtil;
import org.digijava.module.gis.util.MapColorScheme;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: George
 * Date: 4/2/11
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShowGisSettings extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if (request.getParameter("action") != null && request.getParameter("action").equalsIgnoreCase("update")) {
            return update(mapping, form, request, response);
        }

        GisSettingsForm frm = (GisSettingsForm) form;

        Site site = RequestUtils.getSite(request);
        ModuleInstance modInst = RequestUtils.getModuleInstance(request);

        GisSettings sets = DbUtil.getGisSettings (site.getSiteId(), modInst.getInstanceName());
        frm.setGisSettings(sets);

        Collection<AmpSectorScheme> secShcemes = SectorUtil.getSectorSchemes();
        Collection<AmpTheme> topLevelPrograms = ProgramUtil.getParentThemes();

        Long[] secSchemesSelected = null;
        if (secShcemes != null) {
            List tmpList = new ArrayList<Long>();
            for (AmpSectorScheme scheme : secShcemes) {
                if (scheme.getShowInRMFilters() != null && scheme.getShowInRMFilters().booleanValue()) {
                    tmpList.add(scheme.getAmpSecSchemeId());
                }
            }
            secSchemesSelected = new Long[tmpList.size()];
            tmpList.toArray(secSchemesSelected);
        }

        Long[] programsSelected = null;
        if (topLevelPrograms != null) {
            List tmpList = new ArrayList<Long>();
            for (AmpTheme program : topLevelPrograms) {
                if (program.getShowInRMFilters() != null && program.getShowInRMFilters().booleanValue()) {
                    tmpList.add(program.getAmpThemeId());
                }
            }
            programsSelected = new Long[tmpList.size()];
            tmpList.toArray(programsSelected);
        }

        frm.setProgramsSelected(programsSelected);
        frm.setSecSchemesSelected(secSchemesSelected);

        frm.setSecShcemes(secShcemes);
        frm.setTopLevelPrograms(topLevelPrograms);

        Map<String, MapColorScheme> availColorSchemesMap = GisUtil.getAllMapColorSchemePresets();
        MapColorScheme selColorScheme = GisUtil.getActiveColorScheme(request);

        List <MapColorScheme> availSchemes = new ArrayList<MapColorScheme>();
        if (availColorSchemesMap != null && !availColorSchemesMap.isEmpty()) {
            Set <String> keySet = availColorSchemesMap.keySet();
            for (String key : keySet) {
                availSchemes.add(availColorSchemesMap.get(key));
            }
        } else {
            availSchemes.add(MapColorScheme.getDefaultColorScheme());
        }

        frm.setAvailableColorSchemes(availSchemes);
        frm.setSelectedColorPreset(selColorScheme.getName());

        return mapping.findForward("forward");
    }

    public ActionForward update(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        GisSettingsForm frm = (GisSettingsForm) form;


        org.digijava.module.aim.util.DbUtil.update(frm.getGisSettings());

        Collection<AmpSectorScheme> secShcemes = SectorUtil.getSectorSchemes();
        Collection<AmpTheme> topLevelPrograms = ProgramUtil.getParentThemes();

        Set secSchemesSelectedSet = new HashSet(Arrays.asList(frm.getSecSchemesSelected()));
        for (AmpSectorScheme scheme : secShcemes) {
            if (secSchemesSelectedSet.contains(scheme.getAmpSecSchemeId())) {
                scheme.setShowInRMFilters(new Boolean(true));
            } else {
                scheme.setShowInRMFilters(new Boolean(false));
            }
            org.digijava.module.aim.util.DbUtil.update(scheme);
        }

        Set programsSelectedSet = new HashSet(Arrays.asList(frm.getProgramsSelected()));
        for (AmpTheme program : topLevelPrograms) {
            if (programsSelectedSet.contains(program.getAmpThemeId())) {
                program.setShowInRMFilters(new Boolean(true));
            } else {
                program.setShowInRMFilters(new Boolean(false));
            }
            org.digijava.module.aim.util.DbUtil.update(program);

        }





        return mapping.findForward("forward");
    }

}
