package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;

public class ProgramsForm extends ActionForm implements Serializable{
	private int programType;
	private Long[] selectedNPOPrograms;
	private Long[] selectedPPrograms;
	private Long[] selectedSPrograms;
	private List programLevels;
	private Long selPrograms[];
	private Long selProgramId;

	private List actPrograms;
	private Collection programCollection;
	private Long selectedPrograms[];
	private Long program;
	private String programDescription;
	private AmpActivityProgramSettings nationalSetting;
	private List nationalPlanObjectivePrograms;
	private AmpActivityProgramSettings primarySetting;
	private List primaryPrograms;
	private List secondaryPrograms;
	private AmpActivityProgramSettings secondarySetting;

	private String visibleProgram = null;

	public AmpActivityProgramSettings getNationalSetting() {
		return nationalSetting;
	}

	public void setNationalSetting(AmpActivityProgramSettings nationalSetting) {
		this.nationalSetting = nationalSetting;
	}

	public List getNationalPlanObjectivePrograms() {
		return nationalPlanObjectivePrograms;
	}

	public void setNationalPlanObjectivePrograms(List nationalPlanObjectivePrograms) {
		this.nationalPlanObjectivePrograms = nationalPlanObjectivePrograms;
	}

	public AmpActivityProgramSettings getPrimarySetting() {
		return primarySetting;
	}

	public void setPrimarySetting(AmpActivityProgramSettings primarySetting) {
		this.primarySetting = primarySetting;
	}

	public List getPrimaryPrograms() {
		return primaryPrograms;
	}

	public void setPrimaryPrograms(List primaryPrograms) {
		this.primaryPrograms = primaryPrograms;
	}

	public AmpActivityProgramSettings getSecondarySetting() {
		return secondarySetting;
	}

	public void setSecondarySetting(AmpActivityProgramSettings secondarySetting) {
		this.secondarySetting = secondarySetting;
	}

	public List getSecondaryPrograms() {
		return secondaryPrograms;
	}

	public void setSecondaryPrograms(List secondaryPrograms) {
		this.secondaryPrograms = secondaryPrograms;
	}

	public Long getProgram() {
		return program;
	}

	public void setProgram(Long program) {
		this.program = program;
	}

	public String getProgramDescription() {
		return programDescription;
	}

	public void setProgramDescription(String programDescription) {
		this.programDescription = programDescription;
	}

	public List getProgramLevels() {
		return programLevels;
	}

	public void setProgramLevels(List programLevels) {
		this.programLevels = programLevels;
	}

	public Long[] getSelPrograms() {
		return selPrograms;
	}

	public void setSelPrograms(Long[] selPrograms) {
		this.selPrograms = selPrograms;
	}

	public Long getSelProgramId() {
		return selProgramId;
	}

	public void setSelProgramId(Long selProgramId) {
		this.selProgramId = selProgramId;
	}

	public List getActPrograms() {
		return actPrograms;
	}

	public void setActPrograms(List actPrograms) {
		this.actPrograms = actPrograms;
	}

	public Collection getProgramCollection() {
		return programCollection;
	}

	public void setProgramCollection(Collection programCollection) {
		this.programCollection = programCollection;
	}

	public Long[] getSelectedPrograms() {
		return selectedPrograms;
	}

	public void setSelectedPrograms(Long[] selectedPrograms) {
		this.selectedPrograms = selectedPrograms;
	}

	public int getProgramType() {
		return programType;
	}

	public void setProgramType(int programType) {
		this.programType = programType;
	}

	public Long[] getSelectedNPOPrograms() {
		return selectedNPOPrograms;
	}

	public void setSelectedNPOPrograms(Long[] selectedNPOPrograms) {
		this.selectedNPOPrograms = selectedNPOPrograms;
	}

	public Long[] getSelectedPPrograms() {
		return selectedPPrograms;
	}

	public void setSelectedPPrograms(Long[] selectedPPrograms) {
		this.selectedPPrograms = selectedPPrograms;
	}

	public Long[] getSelectedSPrograms() {
		return selectedSPrograms;
	}

	public void setSelectedSPrograms(Long[] selectedSPrograms) {
		this.selectedSPrograms = selectedSPrograms;
	}

}
