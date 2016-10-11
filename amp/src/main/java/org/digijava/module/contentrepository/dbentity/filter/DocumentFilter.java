package org.digijava.module.contentrepository.dbentity.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.jcrentity.Label;
import org.digijava.module.contentrepository.util.DocToOrgDAO;

/**
 * filters a document by a set of AND conditions
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
	
	/**
      * the AmpOrganisationId to filter by. null, zero or negative value = don't filter by this field 
      */
	private Long organisationId;
	
	private AmpTeamMember user;
		
	private List<String> filterLabelsUUID;
	private List<Label> filterLabels;
	
	private List<Long> filterDocTypeIds;
	private List<String> filterFileType;
	
	private List<Long> filterTeamIds;
	private List<String> filterOwners;
	
	private String filterFromDate;
	private String filterToDate;
	
	private Long publicViewPosition;
	
	private List<String> filterKeywords;
	
	public DocumentFilter( ) {
		
	}
	
	
	public DocumentFilter(String source, List<String> filterLabelsUUID, List<Long> filterDocTypeIds,
			List<String> filterFileType, List<Long> filterTeamIds,
			List<String> filterOwners,List<String> filterKeywords, String baseUsername, Long baseTeamId, Long orgId, String filterFromDate, String filterToDate) {
		
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
		
		if(filterFromDate !=null && filterFromDate.length() > 0){
			this.filterFromDate = filterFromDate;
		}
		
		if(filterToDate !=null && filterToDate.length() > 0){
			this.filterToDate = filterToDate;
		}
		
		this.organisationId = orgId;
		
	}
	
	public Collection<DocumentData> applyFilter(Collection<DocumentData> col) {
		String fileTypeFilter="image/";
		ArrayList<DocumentData> retCol	= new ArrayList<DocumentData>();
		if ( col != null ) {
			for ( DocumentData dd: col ) 
			{
			
			    // dear future coder: NEVER EVER write pass = true. The sole reason to modify pass'es value is to put it to false
				boolean pass	= true;
				if ( this.filterDocTypeIds != null && !this.filterDocTypeIds.contains( dd.getCmDocTypeId() ) )					
					pass = false;
				
				if ( this.filterFileType != null && (!this.filterFileType.contains(dd.getContentType()) &&
						!(this.filterFileType.contains(fileTypeFilter) && dd.getContentType().contains(fileTypeFilter)))	) 
					pass = false;
				
				if ( this.filterTeamIds != null && !this.filterTeamIds.contains( dd.getCreatorTeamId() ) )
					pass = false;
				
				if ( this.filterOwners != null && !this.filterOwners.contains( dd.getCreatorEmail() ) )
					pass = false;
				
				if ((this.organisationId != null) && (this.organisationId > 0))
				{
					java.util.Set<Long> organisationIds = DocToOrgDAO.getDocToOrgIdsByUUID(dd.getUuid());
					
					pass &= organisationIds.contains(this.organisationId);
				}
				
				if ( this.filterLabelsUUID != null ) {
					if ( dd.getLabels() != null ) {
						ArrayList<String> ddLabelsUUID	= new ArrayList<String>();	
						for ( Label l: dd.getLabels() ) 
							ddLabelsUUID.add( l.getUuid() );
						boolean tempPass = false;
						for ( String uuid: this.filterLabelsUUID ){
							if ( ddLabelsUUID.contains(uuid) ) {
								tempPass = true;
							}
						}
						if (!tempPass)
							pass = false;
					}
					else 
						pass	= false;
						
				}
				
				if (this.filterKeywords !=null && this.filterKeywords.size() > 0){
					int iterationNo = 0;
					for (String keyword : this.filterKeywords) {
						if ((dd.getTitle() != null && dd.getTitle().toLowerCase().indexOf(keyword.toLowerCase()) != -1)
								|| (dd.getName() != null && dd.getName().toLowerCase().indexOf(keyword.toLowerCase()) != -1)){
							pass &= true;
							break;
						}else if (iterationNo == (this.filterKeywords.size() - 1)){
							pass &= false;
						}
						iterationNo++;
					}
					
				}
				
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
			    if (this.filterFromDate !=null && this.filterFromDate.length() > 0){
					Calendar cal = Calendar.getInstance();
					Calendar ddCal = Calendar.getInstance();
					try {
						cal.setTime(sdf1.parse(this.filterFromDate));
						ddCal.setTime(sdf2.parse(dd.getCalendar()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if (ddCal.before(cal))
						pass = false;
				}
				
				if (this.filterToDate !=null && this.filterToDate.length() > 0){
					Calendar cal = Calendar.getInstance();
					Calendar ddCal = Calendar.getInstance();
					try {
						cal.setTime(sdf1.parse(this.filterToDate));
						ddCal.setTime(sdf2.parse(dd.getCalendar()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if (ddCal.after(cal))
						pass = false;
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

	public Long getOrganisationId()
	{
		return organisationId;
	}
	
	public void setOrganisationId(Long orgId)
	{
		this.organisationId = orgId;
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


	public String getFilterFromDate() {
		return filterFromDate;
	}


	public void setFilterFromDate(String filterFromDate) {
		this.filterFromDate = filterFromDate;
	}


	public String getFilterToDate() {
		return filterToDate;
	}


	public void setFilterToDate(String filterToDate) {
		this.filterToDate = filterToDate;
	}
	
	
}
