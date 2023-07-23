package org.digijava.module.contentrepository.dbentity.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.jcrentity.Label;
import org.digijava.module.contentrepository.util.DocumentOrganizationManager;

/**
 * filters a document by a set of AND conditions
 * @author Alex Gartner
 *
 */
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "CR_DOCUMENT_FILTER")
@Cacheable
public class DocumentFilter {
    
    public final static String SESSION_LAST_APPLIED_PUBLIC_FILTER   = "SESSION_LAST_APPLIED_PUBLIC_FILTER";
    
    public final static String SOURCE_PRIVATE_DOCUMENTS = "private_documents";
    public final static String SOURCE_TEAM_DOCUMENTS    = "team_documents";
    public final static String SOURCE_SHARED_DOCUMENTS  = "shared_documents";
    public final static String SOURCE_PUBLIC_DOCUMENTS  = "public_documents";
    
    public static final Long KEYWORDS_MODE_ANY = 0L;
    public static final Long KEYWORDS_MODE_ALL = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CR_DOCUMENT_FILTER_SEQ")
    @SequenceGenerator(name = "CR_DOCUMENT_FILTER_SEQ", sequenceName = "CR_DOCUMENT_FILTER_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "source")
    private String source;

    @Column(name = "base_username")
    private String baseUsername;

    @Column(name = "base_team_id")
    private Long baseTeamId;

    @Column(name = "public_view_position")
    private Long publicViewPosition;

    @Column(name = "filter_from_date")
    private String filterFromDate;

    @Column(name = "filter_to_date")
    private String filterToDate;

    @ElementCollection(fetch = FetchType.LAZY)
    @JoinColumn(name = "cr_document_filter_id")
    @Column(name = "label_uuid")
    private List<String> filterLabelsUUID;

    @ElementCollection(fetch = FetchType.LAZY)
    @JoinColumn(name = "cr_document_filter_id")
    @Column(name = "doc_type")
    private List<Long> filterDocTypeIds;

    @ElementCollection(fetch = FetchType.LAZY)
    @JoinColumn(name = "cr_document_filter_id")
    @Column(name = "doc_type")
    private List<String> filterFileType;

    @ElementCollection(fetch = FetchType.LAZY)
    @JoinColumn(name = "cr_document_filter_id")
    @Column(name = "team_ids")
    private List<Long> filterTeamIds;

    @ElementCollection(fetch = FetchType.LAZY)
    @JoinColumn(name = "cr_document_filter_id")
    @Column(name = "filter_owners")
    private List<String> filterOwners;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_", referencedColumnName = "amp_team_mem_id")
    private AmpTeamMember user;
    


    private Long organisationId;

    @Transient
    private List<Label> filterLabels;

    
    private Long filterKeywordMode = KEYWORDS_MODE_ANY;
    

    @Transient
    private List<String> filterKeywords;
    
    public DocumentFilter() {
        
    }
    
    public DocumentFilter(DocumentManagerForm docForm) {
        
        source = DocumentFilter.SOURCE_PUBLIC_DOCUMENTS;
        if (docForm.getOtherUsername() != null && docForm.getOtherTeamId() != null) {
            source = DocumentFilter.SOURCE_PRIVATE_DOCUMENTS;
        } else if (docForm.getOtherUsername() == null && docForm.getOtherTeamId() != null) {
            source = DocumentFilter.SOURCE_TEAM_DOCUMENTS;
        } else if (docForm.getShowSharedDocs() != null) {
            source = DocumentFilter.SOURCE_SHARED_DOCUMENTS;
        }
        
        baseUsername = docForm.getOtherUsername();
        baseTeamId = docForm.getOtherTeamId();
        
        if (docForm.getFilterLabelsUUID() != null) {
            filterLabelsUUID = Arrays.asList(docForm.getFilterLabelsUUID());
        }

        if (docForm.getFilterDocTypeIds() != null) {
            filterDocTypeIds = Arrays.asList(docForm.getFilterDocTypeIds());
            filterDocTypeIds = new ArrayList<Long>(filterDocTypeIds);
            filterDocTypeIds.remove(new Long(0));
            filterDocTypeIds.remove(new Long(-1));
        }

        if (docForm.getFilterFileTypes() != null) {
            filterFileType = Arrays.asList(docForm.getFilterFileTypes());
            filterFileType = new ArrayList<String>(filterFileType);
            filterFileType.remove("-1");
        }

        if (docForm.getFilterOwners() != null) {
            filterOwners = Arrays.asList(docForm.getFilterOwners());
            filterOwners = new ArrayList<String>(filterOwners);
            filterOwners.remove("-1");
        }

        if (docForm.getFilterTeamIds() != null) {
            filterTeamIds = Arrays.asList(docForm.getFilterTeamIds());
            filterTeamIds = new ArrayList<Long>(filterTeamIds);
            filterTeamIds.remove(new Long(0));
            filterTeamIds.remove(new Long(-1));
        }

        if (docForm.getFilterKeywords() != null) {
            filterKeywords = Arrays.asList(docForm.getFilterKeywords()).stream()
                    .filter(keyword -> StringUtils.isNotBlank(keyword))
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }
        
        if (docForm.getFilterOrganisations() != null) {
            organisationId = Long.parseLong(docForm.getFilterOrganisations());
        }
        
        if (docForm.getKeywordMode() != null) {
            filterKeywordMode = docForm.getKeywordMode();
        }
        
        filterFromDate = docForm.getFilterFromDate();
        filterToDate = docForm.getFilterToDate();
        
    }
    
    public List<DocumentData> applyFilter(Collection<DocumentData> col) {
        String fileTypeFilter="image/";
        List<DocumentData> retCol  = new ArrayList<DocumentData>();
        if ( col != null ) {
            for ( DocumentData dd: col ) 
            {
            
                // dear future coder: NEVER EVER write pass = true. The sole reason to modify pass'es value is to put it to false
                boolean pass = true;
                if (!isNullOrEmpty(filterDocTypeIds) && !this.filterDocTypeIds.contains(dd.getCmDocTypeId())) {
                    pass = false;
                }
                
                if (!isNullOrEmpty(filterFileType) && (!this.filterFileType.contains(dd.getContentType())
                        && !(this.filterFileType.contains(fileTypeFilter)
                                && dd.getContentType().contains(fileTypeFilter)))) {
                    pass = false;
                }
                
                if (!isNullOrEmpty(filterTeamIds) && !this.filterTeamIds.contains(dd.getCreatorTeamId())) {
                    pass = false;
                }
                
                if (!isNullOrEmpty(filterOwners) && !this.filterOwners.contains(dd.getCreatorEmail())) {
                    pass = false;
                }
                
                if ((this.organisationId != null) && (this.organisationId > 0))
                {
                    Set<Long> organisationIds = DocumentOrganizationManager.getInstance()
                            .getDocToOrgIdsByUUID(dd.getUuid());
                    
                    pass &= organisationIds.contains(this.organisationId);
                }
                
                if (!isNullOrEmpty(filterLabelsUUID)) {
                    if ( dd.getLabels() != null ) {
                        ArrayList<String> ddLabelsUUID  = new ArrayList<String>();  
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
                    } else {
                        pass = false;
                    }
                }
                
                if (!isNullOrEmpty(this.filterKeywords)) {
                    String title = dd.getTitle() == null ? null : dd.getTitle().toLowerCase();
                    String name = dd.getName() == null ? null : dd.getName().toLowerCase();
                    Set<Boolean> keywordFound = this.filterKeywords.stream()
                            .map(keyword -> StringUtils.indexOf(title, keyword) != StringUtils.INDEX_NOT_FOUND
                                    || StringUtils.indexOf(name, keyword) != StringUtils.INDEX_NOT_FOUND)
                            .collect(Collectors.toSet());
                    if (KEYWORDS_MODE_ALL.equals(this.filterKeywordMode)) {
                        pass &= !keywordFound.contains(Boolean.FALSE);
                    } else {
                        pass &= keywordFound.contains(Boolean.TRUE);
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
    
    
    
    private boolean isNullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
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
        if (this.user != null) {
            return this.user.getUser().getEmail();
        }
        return baseUsername;
    }

    public void setBaseUsername(String baseUsername) {
        this.baseUsername = baseUsername;
    }

    public Long getBaseTeamId() {
        if (this.user != null) {
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

    public Long getFilterKeywordMode() {
        return filterKeywordMode;
    }

    public void setFilterKeywordMode(Long filterKeywordMode) {
        this.filterKeywordMode = filterKeywordMode;
    }
    
}
