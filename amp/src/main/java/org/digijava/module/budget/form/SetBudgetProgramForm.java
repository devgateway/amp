package org.digijava.module.budget.form;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpTheme;

public class SetBudgetProgramForm extends ActionForm{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    ArrayList<AmpTheme>programs;
    Long seletedprogram;
    public ArrayList<AmpTheme> getPrograms() {
        return programs;
    }
    public void setPrograms(ArrayList<AmpTheme> programs) {
        this.programs = programs;
    }
    public Long getSeletedprogram() {
        return seletedprogram;
    }
    public void setSeletedprogram(Long seletedprogram) {
        this.seletedprogram = seletedprogram;
    }
    
}
