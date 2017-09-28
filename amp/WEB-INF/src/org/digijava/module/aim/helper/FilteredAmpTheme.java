package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpTheme;
/**
 * 
 * @author Alex Gartner
 * Contains the filtered information of an AmpTheme. Newlines are filtered.
 * Needed for the view. 
 */
public class FilteredAmpTheme extends AmpTheme {
    private String filterString (String str){
        if(str==null) return "";
        try{
            String ret  = str.replaceAll("\n"," ");
            ret         = ret.replaceAll("\r","");
            ret         = ret.replaceAll("'", "`"); //change by Arty - AMP-5276 - "'" causes javascript errors
            ret         = ret.replaceAll("\t","&nbsp;&nbsp;&nbsp;");
            return ret;
        }
        catch(Exception E) {
            E.printStackTrace();
            return null;
        }
    } 
    public FilteredAmpTheme( AmpTheme ampTheme) {
        this.setAmpThemeId(     ampTheme.getAmpThemeId() );
        this.setName(           ampTheme.getName() );
        this.setThemeCode(      ampTheme.getThemeCode() );
        this.setBudgetProgramCode(  ampTheme.getBudgetProgramCode() );
        this.setTypeCategoryValue(  ampTheme.getTypeCategoryValue() );
        this.setDescription(    filterString(ampTheme.getDescription()) );
        this.setLeadAgency(     filterString(ampTheme.getLeadAgency()) );
        this.setTargetGroups(   filterString(ampTheme.getTargetGroups()) );
        this.setBackground(     filterString(ampTheme.getBackground()) );
        this.setObjectives(     filterString(ampTheme.getObjectives()) );
        this.setOutputs(        filterString(ampTheme.getOutputs()) );
        this.setBeneficiaries(  filterString(ampTheme.getBeneficiaries()) );
        this.setEnvironmentConsiderations(  filterString(ampTheme.getEnvironmentConsiderations()) );
    }
    public static List<FilteredAmpTheme> transformAmpThemeList(Collection<AmpTheme> coll) {
        List<FilteredAmpTheme> newList      = new ArrayList<FilteredAmpTheme>();
        Iterator<AmpTheme> iterator = coll.iterator();
        while ( iterator.hasNext() ) {
            AmpTheme ampTheme               = iterator.next();
            FilteredAmpTheme    fAmpTheme   = new FilteredAmpTheme(ampTheme);
            newList.add(fAmpTheme);
        }
        return newList;
    }
}
