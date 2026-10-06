package org.digijava.module.contentrepository.helper;

import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.contentrepository.util.DocumentOrganizationManager;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * an instance of this class is constructed whenever it is needed to build a list of all the possible filtered-value-values (see filters.jsp for an example)
 *
 */
public class FilterValues {
    public List<String> possibleOwners;
    public List<KeyValue> possibleTeams;
    public List<KeyValue> possibleFileTypes;
    public List<KeyValue> possibleOrganisations;
    
    public FilterValues(HttpServletRequest request) {
        possibleOwners = TeamMemberUtil.getAllTeamMemberUserMails();
        Collections.sort(possibleOwners);
        Collection<AmpTeam> allTeams    = TeamUtil.getAllTeams();
        if ( allTeams != null ) {
            possibleTeams       = new ArrayList<KeyValue>();
            for (AmpTeam team: allTeams ) {
                KeyValue kv     = new KeyValue(team.getAmpTeamId().toString() , team.getName() );
                possibleTeams.add(kv);
            }
        }
        List<AmpOrganisation> allOrganisations = DocumentOrganizationManager.getInstance().getAllUsedOrganisations();
        possibleOrganisations = new ArrayList<KeyValue>();
        if (allOrganisations != null)
        {
            java.util.SortedSet<KeyValue> orgs = new java.util.TreeSet<KeyValue>(KeyValue.valueComparator);
            for(AmpOrganisation org:allOrganisations)
                orgs.add(new KeyValue(org.getAmpOrgId().toString(), org.getAcronymAndName()));
            possibleOrganisations.addAll(orgs);
        }
        
        possibleFileTypes = new ArrayList<KeyValue>();

        possibleFileTypes.add(new KeyValue("application/pdf", TranslatorWorker.translateText("PDF Document")));
        possibleFileTypes.add(new KeyValue("application/msword", TranslatorWorker.translateText("Word Document")));
        possibleFileTypes.add(new KeyValue("application/vnd.ms-excel", 
                TranslatorWorker.translateText("Excel Spreadsheet")));
        possibleFileTypes.add(new KeyValue("text/plain", TranslatorWorker.translateText("Text")));
        possibleFileTypes.add(new KeyValue("application/zip", TranslatorWorker.translateText("Zip file")));
        possibleFileTypes.add(new KeyValue("image/", TranslatorWorker.translateText("Images")));  
    }

    public List<String> getPossibleOwners() {
        return possibleOwners;
    }

    public void setPossibleOwners(List<String> possibleOwners) {
        this.possibleOwners = possibleOwners;
    }

    public List<KeyValue> getPossibleTeams() {
        return possibleTeams;
    }

    public void setPossibleTeams(List<KeyValue> possibleTeams) {
        this.possibleTeams = possibleTeams;
    }

    public List<KeyValue> getPossibleFileTypes() {
        return possibleFileTypes;
    }

    public void setPossibleFileTypes(List<KeyValue> possibleFileTypes) {
        this.possibleFileTypes = possibleFileTypes;
    }
    
    public List<KeyValue> getPossibleOrganisations()
    {
        return possibleOrganisations;
    }
    
    public void setPossibleOrganisations(List<KeyValue> possibleOrganisations)
    {
        this.possibleOrganisations = possibleOrganisations;
    }
    
}
