package org.digijava.module.contentrepository.dbentity.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.jcrentity.Label;

/**
 * @author Alex Gartner
 *
 */
public class DocumentFilter {
	
	public final static String SESSION_LAST_APPLIED_PUBLIC_FILTER	= "SESSION_LAST_APPLIED_PUBLIC_FILTER";
	
	public final static String SOURCE_PRIVATE_DOCUMENTS	= "private_documents";
	public final static String SOURCE_TEAM_DOCUMENTS	= "team_documents";
	public final static String SOURCE_SHARED_DOCUMENTS	= "shared_documents";
	public final static String SOURCE_PUBLIC_DOCUMENTS	= "public_documents";
	
	private Long id;
	
	private String name;
	private String source;
	
	private String baseUsername;
	private Long baseTeamId;
	
	private AmpTeamMember user;
		
	private List<String> filterLabelsUUID;
	private List<Label> filterLabels;
	
	private List<Long> filterDocTypeIds;
	private List<String> filterFileType;
	
	private List<Long> filterTeamIds;
	private List<String> filterOwners;
	
	private Long publicViewPosition;
	
	private List<String> filterKeywords;
	
	public DocumentFilter( ) {
		
	}
	
	
	public DocumentFilter(String source, List<String> filterLabelsUUID, List<Long> filterDocTypeIds,
			List<String> filterFileType, List<Long> filterTeamIds,
			List<String> filterOwners,List<String> filterKeywords, String baseUsername, Long baseTeamId) {
		
		this.source = source;
		
		this.baseUsername		= baseUsername;
		this.baseTeamId			= baseTeamId;
		
		if ( filterLabelsUUID != null && filterLabelsUUID.size() > 0 )
			this.filterLabelsUUID = filterLabelsUUID;
		
		if ( filterDocTypeIds != null && filterDocTypeIds.size() > 0 )
			this.filterDocTypeIds = filterDocTypeIds;
		
		if ( filterFileType != null && filterFileType.size() > 0 )
			this.filterFileType = filterFileType;
		
		if ( filterTeamIds != null && filterTeamIds.size() > 0 )
			this.filterTeamIds = filterTeamIds;
		
		if ( filterOwners != null && filterOwners.size() > 0 )
			this.filterOwners = filterOwners;
		
		if(filterKeywords !=null && filterKeywords.size() > 0){
			this.filterKeywords = filterKeywords;
		}
		
	}
	
	public Collection<DocumentData> applyFilter(Collection<DocumentData> col) {
		String fileTypeFilter="image/";
		ArrayList<DocumentData> retCol	= new ArrayList<DocumentData>();
		if ( col != null ) {
			for ( DocumentData dd: col ) {
				boolean pass	= true;
				if ( this.filterDocTypeIds != null && !this.filterDocTypeIds.contains( dd.getCmDocTypeId() ) ) {					
					pass	= false;
				}else if ( this.filterFileType != null && (!this.filterFileType.contains(dd.getContentType()) &&
						!(this.filterFileType.contains(fileTypeFilter) && dd.getContentType().contains(fileTypeFilter)))	) 
					pass	= false;
				else if ( this.filterTeamIds != null && !this.filterTeamIds.contains( dd.getCreatorTeamId() ) )
					pass	= false;
				else if ( this.filterOwners != null && !this.filterOwners.contains( dd.getCreatorEmail() ) )
					pass	= false;
				else if ( this.filterLabelsUUID != null ) {
					if ( dd.getLabels() != null ) {
						ArrayList<String> ddLabelsUUID	= new ArrayList<String>();	
						for ( Label l: dd.getLabels() ) 
							ddLabelsUUID.add( l.getUuid() );
						for ( String uuid: this.filterLabelsUUID ) 
							if ( !ddLabelsUUID.contains(uuid) ) {
								pass	= false;
								break;
							}
					}
					else 
						pass	= false;
						
				}else if (this.filterKeywords !=null && this.filterKeywords.size() >0){
					int iterationNo = 0;
					for (String keyword : this.filterKeywords) {
						if(dd.getTitle().toLowerCase().indexOf(keyword.toLowerCase()) != -1
								||  dd.getName().toLowerCase().indexOf(keyword.toLowerCase()) != -1 ){
							pass = true;
							break;
						}else if (iterationNo == (this.filterKeywords.size()-1)){
							pass = false;
						}
						iterationNo++;
					}
					
				}
				
				if (pass)
					retCol.add(dd);
			}
		}
		
		return retCol;
	}
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public AmpTeamMember getUser() {
		return user;
	}
	public void setUser(AmpTeamMember user) {
		this.user = user;
	}
	public List<String> getFilterLabelsUUID() {
		return filterLabelsUUID;
	}
	public void setFilterLabelsUUID(List<String> filterLabelsUUID) {
		this.filterLabelsUUID = filterLabelsUUID;
	}
	public List<Label> getFilterLabels() {
		return filterLabels;
	}
	public void setFilterLabels(List<Label> filterLabels) {
		this.filterLabels = filterLabels;
	}
	
	public List<Long> getFilterDocTypeIds() {
		return filterDocTypeIds;
	}

	public void setFilterDocTypeIds(List<Long> filterDocTypeIds) {
		this.filterDocTypeIds = filterDocTypeIds;
	}

	public List<String> getFilterFileType() {
		return filterFileType;
	}
	public void setFilterFileType(List<String> filterFileType) {
		this.filterFileType = filterFileType;
	}
	public List<Long> getFilterTeamIds() {
		return filterTeamIds;
	}
	public void setFilterTeamIds(List<Long> filterTeamIds) {
		this.filterTeamIds = filterTeamIds;
	}

	public List<String> getFilterOwners() {
		return filterOwners;
	}

	public void setFilterOwners(List<String> filterOwners) {
		this.filterOwners = filterOwners;
	}

	public String getBaseUsername() {
		if ( this.user != null ) {
			return this.user.getUser().getEmail();
		}
		return baseUsername;
	}

	public void setBaseUsername(String baseUsername) {
		this.baseUsername = baseUsername;
	}

	public Long getBaseTeamId() {
		if ( this.user != null ) {
			return this.user.getAmpTeam().getAmpTeamId();
		}
		return baseTeamId;
	}

	public void setBaseTeamId(Long baseTeamId) {
		this.baseTeamId = baseTeamId;
	}

	public Long getPublicViewPosition() {
		return publicViewPosition;
	}

	public void setPublicViewPosition(Long publicViewPosition) {
		this.publicViewPosition = publicViewPosition;
	}


	public List<String> getFilterKeywords() {
		return filterKeywords;
	}


	public void setFilterKeywords(List<String> filterKeywords) {
		this.filterKeywords = filterKeywords;
	}
	
	
}
