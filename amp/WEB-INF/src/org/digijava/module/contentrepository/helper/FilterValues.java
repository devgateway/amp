package org.digijava.module.contentrepository.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

public class FilterValues {
	public List<String> possibleOwners;
	public List<KeyValue> possibleTeams;
	public List<KeyValue> possibleFileTypes;
	
	public FilterValues(HttpServletRequest request) {
		
		Collection<User> allUsers	= TeamMemberUtil.getAllTeamMemberUsers();
		if (allUsers != null) {
			possibleOwners		= new ArrayList<String>();
			
			for ( User u: allUsers ) {
				if (!possibleOwners.contains( u.getEmail() ))
					possibleOwners.add( u.getEmail() );
			}
		}
		Collections.sort(possibleOwners );
		
		Collection<AmpTeam> allTeams	= TeamUtil.getAllTeams();
		if ( allTeams != null ) {
			possibleTeams		= new ArrayList<KeyValue>();
			for (AmpTeam team: allTeams ) {
				KeyValue kv		= new KeyValue(team.getAmpTeamId().toString() , team.getName() );
				possibleTeams.add(kv);
			}
		}
		Site site = RequestUtils.getSite(request);
 	 	String siteId = site.getId().toString();
 	 	String locale = RequestUtils.getNavigationLanguage(request).getCode();
 	 	
		possibleFileTypes = new ArrayList<KeyValue>();
		try {
			possibleFileTypes.add( new KeyValue("application/pdf", TranslatorWorker.translateText("PDF Document",request)));
			possibleFileTypes.add( new KeyValue("application/msword",TranslatorWorker.translateText("Word Document",request)));
			possibleFileTypes.add( new KeyValue("application/vnd.ms-excel",TranslatorWorker.translateText("Excel Spreadsheet",request)));
			possibleFileTypes.add( new KeyValue("text/plain",TranslatorWorker.translateText("Text",request)));
			
		} catch (WorkerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
	
	
}
