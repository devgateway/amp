package org.digijava.module.contentrepository.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

public class FilterValues {
	public List<String> possibleOwners;
	public List<KeyValue> possibleTeams;
	public List<KeyValue> possibleFileTypes;
	
	public FilterValues() {
		
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
		
		possibleFileTypes			= new ArrayList<KeyValue>();
		possibleFileTypes.add( new KeyValue("application/pdf", "PDF Document") );
		possibleFileTypes.add( new KeyValue("application/msword", "Word Document") );
		possibleFileTypes.add( new KeyValue("application/vnd.ms-excel", "Excel Spreadsheet") );
		possibleFileTypes.add( new KeyValue("text/plain","Text") );
		
		
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
