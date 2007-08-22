package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.util.ProgramUtil;
import java.util.ArrayList;
import org.digijava.module.aim.dbentity.AmpTheme;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;

public class AddProgram
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.
        Exception {

        EditActivityForm eaform=(EditActivityForm)form;
        eaform.setReset(false);
        List prl = getParents();
        List prLevels = new ArrayList();
        String selectedThemeId = request.getParameter("themeid");
        String opStatus = request.getParameter("op");
        String strLevel=request.getParameter("selPrgLevel");

        if(selectedThemeId==null && opStatus==null && strLevel==null){
            prLevels.add(prl);
            eaform.setProgramLevels(prLevels);
            eaform.setSelPrograms(null);
            return mapping.findForward("forward");
        }

        if(selectedThemeId == null) {
            if(eaform.getProgramLevels() != null) {
                prLevels = eaform.getProgramLevels();
            }
            if(prLevels.size() == 0) {
                prLevels.add(prl);
                eaform.setProgramLevels(prLevels);
            }
            return mapping.findForward("forward");
        } else if(selectedThemeId.equals("-1")) {
            if(strLevel!=null){
                Long level=Long.valueOf(strLevel);
                prLevels = eaform.getProgramLevels();
                int sz=prLevels.size();
                for(int i = level.intValue(); i < sz; i++) {
                    prLevels.remove(level.intValue());
                }
            }
            if(opStatus != null) {
                return mapping.findForward("added");
            } else {
                return mapping.findForward("forward");
            }
        } else if(selectedThemeId.equals("")) {
            if(opStatus != null) {
                return mapping.findForward("added");
            } else {
                return mapping.findForward("forward");
            }
        }

        if(opStatus != null) {
            if(opStatus.equals("add")) {
                List prgLst = new ArrayList();
                if(eaform.getActPrograms()!=null){
                    prgLst=eaform.getActPrograms();
                }
                AmpTheme prg = new AmpTheme();
                AmpTheme theme=null;
                prg = ProgramUtil.getThemeObject(Long.valueOf(selectedThemeId));

                Iterator itr=prgLst.listIterator();
                while(itr.hasNext()){
                    theme=(AmpTheme)itr.next();
                    if(validateData(prg,theme)){
                         return mapping.findForward("added");
                    }
                }
                
                // no persistent field setProgramviewname
                prg.setProgramviewname(ProgramUtil.getHierarchyName(prg));
                prgLst.add(prg); 
                eaform.setActPrograms(prgLst);
                
            }
            return mapping.findForward("added");
        }

        int ind = 0;
        boolean opflag = false;
        AmpTheme prg = null;
        prLevels = eaform.getProgramLevels();
        if(prLevels == null) {
            prLevels.add(getParents());
        }else if(prLevels.size() == 0) {
            prLevels.add(getParents());

        }

        ListIterator prItr = prLevels.listIterator();
        ListIterator subPrgItr;
        while(prItr.hasNext()) {
            ArrayList subPrg = (ArrayList) prItr.next();
            if(subPrg != null) {
                subPrgItr = subPrg.listIterator();
                while(subPrgItr.hasNext()) {
                    prg = (AmpTheme) subPrgItr.next();
                    if(selectedThemeId.equals(prg.getAmpThemeId().
                                              toString())) {
                        ArrayList subPrograms = getThenmes(Long.valueOf(
                            selectedThemeId));
                        if(subPrograms != null) {
                            ind = prItr.nextIndex();
                            if(subPrograms.size() != 0) {
                                if(prItr.hasNext()) {
                                    prItr.next();
                                    prItr.set(subPrograms);
                                    ind = prItr.nextIndex();
                                } else {
                                    prLevels.add(getThenmes(Long.valueOf(
                                        selectedThemeId)));
                                    ind=0;
                                }
                                opflag = true;
                            }
                        }
                        break;
                    }
                }
            }
            if(opflag) {
                break;
            }
        }

        if(ind != 0) {
            int sz=prLevels.size();
            for(int i = ind; i < sz; i++) {
                prLevels.remove(ind);
            }
        }
        eaform.setProgramLevels(prLevels);
        return mapping.findForward("forward");
    }

    private ArrayList getThenmes(Long parentid) {
        ArrayList prl = new ArrayList(ProgramUtil.getSubThemes(parentid));
        return prl;
    }

    private ArrayList getParents() {
        ArrayList prl = new ArrayList(ProgramUtil.getParentThemes());
        return prl;
    }

    private boolean validateData(AmpTheme theme1,AmpTheme theme2){
        if(theme1.getAmpThemeId().equals(theme2.getAmpThemeId())){
            return true;
        }
        return false;
    }
}


