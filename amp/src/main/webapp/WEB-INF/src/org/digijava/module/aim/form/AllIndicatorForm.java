package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.HashMap;

import org.apache.struts.action.ActionForm;

public class AllIndicatorForm extends ActionForm 
{
    private Collection prgIndicators;
    private Collection projIndicators;
    private Collection subPrograms;
    private boolean flag;
    private Collection allThemes;
    private Collection doubleColl;
    private HashMap map;
    private HashMap themeIndi;
    private boolean indicatorFlag;
    private boolean flagShow;
    private int programId;
    

    public Collection getSubPrograms() {
        return subPrograms;
    }
    /**
     * @param subProgram The subPrograms to set.
     */
    public void setSubPrograms(Collection subProgram) {
        this.subPrograms = subProgram;
    }
    public  void setThemeIndi(HashMap map) {
        this.themeIndi = map;
    }
    
    public HashMap getThemeIndi() {
        return themeIndi;
    }
    public  void setMap(HashMap map) {
        this.map = map;
    }
    
    public HashMap getMap() {
        return map;
    }
    
    public void setDoubleColl(Collection col) {
        this.doubleColl = col;
    }
    
    public Collection getDoubleColl() {
        return doubleColl;
    }
    public Collection getAllThemes() {
        return allThemes;
    }
    public void setAllThemes(Collection themes ) {
        this.allThemes = themes;
    }
    public void setProgramId(int prgId){
        this.programId=prgId;
    }
    
    public int getProgramId(){
        return programId;
    }
    
    public void setFlagShow(boolean bol){
        this.flagShow=bol;
    }
    
    public boolean getFlagShow(){
        return flagShow;
    }
    
    public void setIndicatorFlag(boolean bol){
        this.indicatorFlag=bol;
    }
    
    public boolean getIndicatorFlag(){
        return indicatorFlag;
    }
    /**
     * @return Returns the prgIndicators.
     */
    public Collection getPrgIndicators() {
        return prgIndicators;
    }
    /**
     * @param prgIndicators The prgIndicators to set.
     */
    public void setPrgIndicators(Collection prgIndicators) {
        this.prgIndicators = prgIndicators;
    }
    /**
     * @return Returns the projIndicators.
     */
    public Collection getProjIndicators() {
        return projIndicators;
    }
    /**
     * @param projIndicators The projIndicators to set.
     */
    public void setProjIndicators(Collection projIndicators) {
        this.projIndicators = projIndicators;
    }
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
}
