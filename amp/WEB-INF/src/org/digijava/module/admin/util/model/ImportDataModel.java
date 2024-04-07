package org.digijava.module.admin.util.model;

import java.util.List;

public class ImportDataModel {
    private Long internal_id;
    private String amp_id;
    private String project_title;
    private String description;
    private String objective;
    private Object document_space;
    private boolean is_draft;
    private Object last_imported_at;
    private Object last_imported_by;
    private Object original_completion_date;
    private List<Sector> primary_sectors;
    private List<Sector> secondary_sectors;
    private List<Location> locations;
    private List<DonorOrganization> donor_organization;
    private List<Object> responsible_organization;
    private List<Object> executing_agency;
    private List<Object> activity_internal_ids;
    private List<Funding> fundings;
    private List<Object> issues;
    private Long team;
    private PpcAmount ppc_amount;
    private List<Object> donor_contact_information;
    private List<Object> project_coordinator_contact_information;
    private List<Object> sector_ministry_contact_information;
    private List<Object> mofed_contact_information;
    private List<Object> implementing_executing_agency_contact_information;
    private List<Object> structures;
    private Object proposed_start_date;
    private Object actual_start_date;
    private Object actual_approval_date;
    private Object actual_completion_date;
    private Long created_by;
    private String creation_date;
    private String update_date;
    private Object iati_last_update_date;
    private Object approved_by;
    private Object approval_date;
    private Long approval_status;
    private Object archived;
    private List<Object> indicators;
    private List<Object> activity_documents;
    private Long activity_status;
    private Long activity_budget;
    private Object implementation_level;
    private Object implementation_location;
    private Object cris_number;
    private List<Program> national_plan_objective;
    private List<Object> primary_programs;
    private List<Program> secondary_programs;
    private List<Program> tertiary_programs;
    private ActivityGroup activity_group;
    private Long modified_by;
    private Long activity_type;

    public Long getInternal_id() {
        return internal_id;
    }

    public void setInternal_id(Long internal_id) {
        this.internal_id = internal_id;
    }

    public String getAmp_id() {
        return amp_id;
    }

    public void setAmp_id(String amp_id) {
        this.amp_id = amp_id;
    }

    public String getProject_title() {
        return project_title;
    }

    public void setProject_title(String project_title) {
        this.project_title = project_title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public Object getDocument_space() {
        return document_space;
    }

    public void setDocument_space(Object document_space) {
        this.document_space = document_space;
    }

    public boolean isIs_draft() {
        return is_draft;
    }

    public void setIs_draft(boolean is_draft) {
        this.is_draft = is_draft;
    }

    public Object getLast_imported_at() {
        return last_imported_at;
    }

    public void setLast_imported_at(Object last_imported_at) {
        this.last_imported_at = last_imported_at;
    }

    public Object getLast_imported_by() {
        return last_imported_by;
    }

    public void setLast_imported_by(Object last_imported_by) {
        this.last_imported_by = last_imported_by;
    }

    public Object getOriginal_completion_date() {
        return original_completion_date;
    }

    public void setOriginal_completion_date(Object original_completion_date) {
        this.original_completion_date = original_completion_date;
    }

    public List<Sector> getPrimary_sectors() {
        return primary_sectors;
    }

    public void setPrimary_sectors(List<Sector> primary_sectors) {
        this.primary_sectors = primary_sectors;
    }

    public List<Sector> getSecondary_sectors() {
        return secondary_sectors;
    }

    public void setSecondary_sectors(List<Sector> secondary_sectors) {
        this.secondary_sectors = secondary_sectors;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<DonorOrganization> getDonor_organization() {
        return donor_organization;
    }

    public void setDonor_organization(List<DonorOrganization> donor_organization) {
        this.donor_organization = donor_organization;
    }

    public List<Object> getResponsible_organization() {
        return responsible_organization;
    }

    public void setResponsible_organization(List<Object> responsible_organization) {
        this.responsible_organization = responsible_organization;
    }

    public List<Object> getExecuting_agency() {
        return executing_agency;
    }

    public void setExecuting_agency(List<Object> executing_agency) {
        this.executing_agency = executing_agency;
    }

    public List<Object> getActivity_internal_ids() {
        return activity_internal_ids;
    }

    public void setActivity_internal_ids(List<Object> activity_internal_ids) {
        this.activity_internal_ids = activity_internal_ids;
    }

    public List<Funding> getFundings() {
        return fundings;
    }

    public void setFundings(List<Funding> fundings) {
        this.fundings = fundings;
    }

    public List<Object> getIssues() {
        return issues;
    }

    public void setIssues(List<Object> issues) {
        this.issues = issues;
    }

    public Long getTeam() {
        return team;
    }

    public void setTeam(Long team) {
        this.team = team;
    }

    public PpcAmount getPpc_amount() {
        return ppc_amount;
    }

    public void setPpc_amount(PpcAmount ppc_amount) {
        this.ppc_amount = ppc_amount;
    }

    public List<Object> getDonor_contact_information() {
        return donor_contact_information;
    }

    public void setDonor_contact_information(List<Object> donor_contact_information) {
        this.donor_contact_information = donor_contact_information;
    }

    public List<Object> getProject_coordinator_contact_information() {
        return project_coordinator_contact_information;
    }

    public void setProject_coordinator_contact_information(List<Object> project_coordinator_contact_information) {
        this.project_coordinator_contact_information = project_coordinator_contact_information;
    }

    public List<Object> getSector_ministry_contact_information() {
        return sector_ministry_contact_information;
    }

    public void setSector_ministry_contact_information(List<Object> sector_ministry_contact_information) {
        this.sector_ministry_contact_information = sector_ministry_contact_information;
    }

    public List<Object> getMofed_contact_information() {
        return mofed_contact_information;
    }

    public void setMofed_contact_information(List<Object> mofed_contact_information) {
        this.mofed_contact_information = mofed_contact_information;
    }

    public List<Object> getImplementing_executing_agency_contact_information() {
        return implementing_executing_agency_contact_information;
    }

    public void setImplementing_executing_agency_contact_information(List<Object> implementing_executing_agency_contact_information) {
        this.implementing_executing_agency_contact_information = implementing_executing_agency_contact_information;
    }

    public List<Object> getStructures() {
        return structures;
    }

    public void setStructures(List<Object> structures) {
        this.structures = structures;
    }

    public Object getProposed_start_date() {
        return proposed_start_date;
    }

    public void setProposed_start_date(Object proposed_start_date) {
        this.proposed_start_date = proposed_start_date;
    }

    public Object getActual_start_date() {
        return actual_start_date;
    }

    public void setActual_start_date(Object actual_start_date) {
        this.actual_start_date = actual_start_date;
    }

    public Object getActual_approval_date() {
        return actual_approval_date;
    }

    public void setActual_approval_date(Object actual_approval_date) {
        this.actual_approval_date = actual_approval_date;
    }

    public Object getActual_completion_date() {
        return actual_completion_date;
    }

    public void setActual_completion_date(Object actual_completion_date) {
        this.actual_completion_date = actual_completion_date;
    }

    public Long getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Long created_by) {
        this.created_by = created_by;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public Object getIati_last_update_date() {
        return iati_last_update_date;
    }

    public void setIati_last_update_date(Object iati_last_update_date) {
        this.iati_last_update_date = iati_last_update_date;
    }

    public Object getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(Object approved_by) {
        this.approved_by = approved_by;
    }

    public Object getApproval_date() {
        return approval_date;
    }

    public void setApproval_date(Object approval_date) {
        this.approval_date = approval_date;
    }

    public Long getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(Long approval_status) {
        this.approval_status = approval_status;
    }

    public Object getArchived() {
        return archived;
    }

    public void setArchived(Object archived) {
        this.archived = archived;
    }

    public List<Object> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<Object> indicators) {
        this.indicators = indicators;
    }

    public List<Object> getActivity_documents() {
        return activity_documents;
    }

    public void setActivity_documents(List<Object> activity_documents) {
        this.activity_documents = activity_documents;
    }

    public Long getActivity_status() {
        return activity_status;
    }

    public void setActivity_status(Long activity_status) {
        this.activity_status = activity_status;
    }

    public Long getActivity_budget() {
        return activity_budget;
    }

    public void setActivity_budget(Long activity_budget) {
        this.activity_budget = activity_budget;
    }

    public Object getImplementation_level() {
        return implementation_level;
    }

    public void setImplementation_level(Object implementation_level) {
        this.implementation_level = implementation_level;
    }

    public Object getImplementation_location() {
        return implementation_location;
    }

    public void setImplementation_location(Object implementation_location) {
        this.implementation_location = implementation_location;
    }

    public Object getCris_number() {
        return cris_number;
    }

    public void setCris_number(Object cris_number) {
        this.cris_number = cris_number;
    }

    public List<Program> getNational_plan_objective() {
        return national_plan_objective;
    }

    public void setNational_plan_objective(List<Program> national_plan_objective) {
        this.national_plan_objective = national_plan_objective;
    }

    public List<Object> getPrimary_programs() {
        return primary_programs;
    }

    public void setPrimary_programs(List<Object> primary_programs) {
        this.primary_programs = primary_programs;
    }

    public List<Program> getSecondary_programs() {
        return secondary_programs;
    }

    public void setSecondary_programs(List<Program> secondary_programs) {
        this.secondary_programs = secondary_programs;
    }

    public List<Program> getTertiary_programs() {
        return tertiary_programs;
    }

    public void setTertiary_programs(List<Program> tertiary_programs) {
        this.tertiary_programs = tertiary_programs;
    }

    public ActivityGroup getActivity_group() {
        return activity_group;
    }

    public void setActivity_group(ActivityGroup activity_group) {
        this.activity_group = activity_group;
    }

    public Long getModified_by() {
        return modified_by;
    }

    public void setModified_by(Long modified_by) {
        this.modified_by = modified_by;
    }

    public Long getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(Long activity_type) {
        this.activity_type = activity_type;
    }

    // Getters and setters
}





