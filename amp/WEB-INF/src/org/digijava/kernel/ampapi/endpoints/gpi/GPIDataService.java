package org.digijava.kernel.ampapi.endpoints.gpi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.dgfoundation.amp.gpi.reports.GPIRemark;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpGPINiAidOnBudget;
import org.digijava.module.aim.dbentity.AmpGPINiDonorNotes;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * 
 * @author gerald
 *
 */
public class GPIDataService {
	public static JsonBean getAidOnBudgetById(Long id) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}

		AmpGPINiAidOnBudget aidOnBudget = GPIUtils.getAidOnBudgetById(id);
		if (aidOnBudget != null)
			return modelToJsonBean(aidOnBudget);
		else
			return null;
	}

	public static JsonBean getAidOnBudgetList(Integer offset, Integer count, String orderBy, String sort) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}

		Integer total = GPIUtils.getAidOnBudgetCount();
		List<AmpGPINiAidOnBudget> aidOnBudgetList = GPIUtils.getAidOnBudgetList(offset, count, orderBy, sort, total);
		JsonBean data = new JsonBean();
		List<JsonBean> lst = new ArrayList<>();
		for (AmpGPINiAidOnBudget aidOnBudget : aidOnBudgetList) {
			lst.add(modelToJsonBean(aidOnBudget));
		}

		data.set("data", lst);
		data.set(GPIEPConstants.TOTAL_RECORDS, total);
		return data;
	}

	private static JsonBean modelToJsonBean(AmpGPINiAidOnBudget aidOnBudget) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}
		
		JsonBean data = new JsonBean();
		data.set(GPIEPConstants.FIELD_ID, aidOnBudget.getAmpGPINiAidOnBudgetId());
		data.set(GPIEPConstants.FIELD_DONOR_ID, aidOnBudget.getDonor().getAmpOrgId());
		data.set(GPIEPConstants.FIELD_CURRENCY_CODE, aidOnBudget.getCurrency().getCurrencyCode());
		data.set(GPIEPConstants.FIELD_AMOUNT, aidOnBudget.getAmount());
		data.set(GPIEPConstants.FIELD_DATE,
				DateTimeUtil.formatDate(aidOnBudget.getIndicatorDate(), GPIEPConstants.DATE_FORMAT));
		return data;
	}

	private static AmpGPINiAidOnBudget getAidOnBudget(JsonBean data) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}
		
		Long id;
		AmpGPINiAidOnBudget aidOnBudget = null;
		if (data.getString(GPIEPConstants.FIELD_ID) != null
				&& NumberUtils.isNumber(data.getString(GPIEPConstants.FIELD_ID))) {
			id = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_ID)));
			aidOnBudget = GPIUtils.getAidOnBudgetById(id);
		} else {
			aidOnBudget = new AmpGPINiAidOnBudget();
		}
		
		return aidOnBudget;
	}
	
	private static AmpGPINiAidOnBudget updateModel(AmpGPINiAidOnBudget aidOnBudget, JsonBean data){
		if (data.get(GPIEPConstants.FIELD_CURRENCY_CODE) != null) {
			String currencyCode = data.getString((GPIEPConstants.FIELD_CURRENCY_CODE));
			aidOnBudget.setCurrency(CurrencyUtil.getAmpcurrency(currencyCode));
		}

		if (data.getString(GPIEPConstants.FIELD_DONOR_ID) != null) {
			Long donorId = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_DONOR_ID)));
			aidOnBudget.setDonor(GPIUtils.getOrganisation(donorId));
		}

		if (data.get(GPIEPConstants.FIELD_AMOUNT) != null) {
			aidOnBudget.setAmount((Double.parseDouble(String.valueOf(data.get(GPIEPConstants.FIELD_AMOUNT)))));
		}

		if (data.getString(GPIEPConstants.FIELD_DATE) != null) {
			Date date = DateTimeUtil.parseDate(data.getString(GPIEPConstants.FIELD_DATE), GPIEPConstants.DATE_FORMAT);
			aidOnBudget.setIndicatorDate(date);
		}
		
		return aidOnBudget;
	}

	public static JsonBean saveAidOnBudget(JsonBean data) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}

		JsonBean result = new JsonBean();
		List<JsonBean> validationErrors = validateAidOnBudget(data);
		if (validationErrors.size() == 0) {
			AmpGPINiAidOnBudget aidOnBudget = getAidOnBudget(data);
			updateModel(aidOnBudget, data);
			GPIUtils.saveAidOnBudget(aidOnBudget);
			JsonBean saved = modelToJsonBean(aidOnBudget);
			result.set(GPIEPConstants.DATA, saved);
			result.set(GPIEPConstants.RESULT, GPIEPConstants.SAVED);
			if (data.get(GPIEPConstants.CID) != null) {
				saved.set(GPIEPConstants.CID, data.get(GPIEPConstants.CID));
			}
		} else {
			result.set(GPIEPConstants.DATA, data);
			result.set(GPIEPConstants.RESULT, GPIEPConstants.SAVE_FAILED);
			result.set(GPIEPConstants.ERRORS, validationErrors);
		}

		return result;
	}
	
	public static List<JsonBean> saveAidOnBudget(List<JsonBean> aidOnBudgetList) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}

		List<JsonBean> results = new ArrayList<>();
		for (JsonBean aidOnBudget : aidOnBudgetList) {
			results.add(saveAidOnBudget(aidOnBudget));
		}

		return results;
	}

	private static List<JsonBean> validateAidOnBudget(JsonBean data) {
		List<JsonBean> validationErrors = new ArrayList<>();
		Long donorId = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_DONOR_ID)));
		Date date = DateTimeUtil.parseDate(data.getString(GPIEPConstants.FIELD_DATE), GPIEPConstants.DATE_FORMAT);
		Long id = null;
		if (data.get(GPIEPConstants.FIELD_ID) != null) {
			id = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_ID)));
		}

		if (GPIUtils.checkAidOnBudgetExists(id, donorId, date)) {
			JsonBean error = new JsonBean();
			error.set(ApiError.getErrorCode(GPIErrors.AID_ON_BUDGET_DATE_DONOR_COMBINATION_EXISTS),
					GPIErrors.AID_ON_BUDGET_DATE_DONOR_COMBINATION_EXISTS.description);
			validationErrors.add(error);
		}

		return validationErrors;
	}

	public static JsonBean deleteAidOnBudgetById(Long id) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}

		JsonBean result = new JsonBean();
		GPIUtils.deleteAidOnBudget(id);
		result.set(GPIEPConstants.RESULT, GPIEPConstants.DELETED);
		return result;
	}

	public static JsonBean saveDonorNotes(JsonBean data) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}
		
		JsonBean result = new JsonBean();
		List<JsonBean> validationErrors = validateDonorNotes(data);

		if (validationErrors.isEmpty()) {			
			AmpGPINiDonorNotes donorNotes = getOrCreateDonorNotes(data);			
			GPIUtils.saveDonorNotes(donorNotes);
			JsonBean saved = modelToJsonBean(donorNotes);
			result.set(GPIEPConstants.DATA, saved);
			result.set(GPIEPConstants.RESULT, GPIEPConstants.SAVED);
			if (data.get(GPIEPConstants.CID) != null) {
				saved.set(GPIEPConstants.CID, data.get(GPIEPConstants.CID));
			}

		} else {
			result.set(GPIEPConstants.DATA, data);
			result.set(GPIEPConstants.RESULT, GPIEPConstants.SAVE_FAILED);
			result.set(GPIEPConstants.ERRORS, validationErrors);

		}
		return result;
	}
	
	public static List<JsonBean> saveDonorNotes(List<JsonBean> donorNotesList) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}
		
		List<JsonBean> results = new ArrayList<>();
		for (JsonBean donorNotes : donorNotesList) {
			results.add(saveDonorNotes(donorNotes));
		}
		return results;
	}
	
	private static AmpGPINiDonorNotes getOrCreateDonorNotes(JsonBean data){
		Long id;
		AmpGPINiDonorNotes donorNotes; 
		if (data.getString(GPIEPConstants.FIELD_ID) != null
				&& NumberUtils.isNumber(data.getString(GPIEPConstants.FIELD_ID))) {
			id = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_ID)));
			donorNotes = GPIUtils.getDonorNotesById(id);
		} else {
			donorNotes = new AmpGPINiDonorNotes();
		}

		donorNotes.setNotes(data.getString(GPIEPConstants.FIELD_NOTES));
		if (data.getString(GPIEPConstants.FIELD_NOTES_DATE) != null) {
			Date notesDate = DateTimeUtil.parseDate(data.getString(GPIEPConstants.FIELD_NOTES_DATE),
					GPIEPConstants.DATE_FORMAT);
			donorNotes.setNotesDate(notesDate);
		}

		if (data.getString(GPIEPConstants.FIELD_DONOR_ID) != null) {
			Long donorId = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_DONOR_ID)));
			donorNotes.setDonor(GPIUtils.getOrganisation(donorId));
		}
		
		donorNotes.setIndicatorCode(data.getString(GPIEPConstants.FIELD_INDICATOR_CODE));
		
		return donorNotes;
	}
	
	private static JsonBean modelToJsonBean(AmpGPINiDonorNotes donorNotes) {
		JsonBean data = new JsonBean();		
		data.set(GPIEPConstants.FIELD_ID, donorNotes.getAmpGPINiDonorNotesId());
		data.set(GPIEPConstants.FIELD_DONOR_ID, donorNotes.getDonor().getAmpOrgId());
		data.set(GPIEPConstants.FIELD_NOTES, donorNotes.getNotes());
		data.set(GPIEPConstants.FIELD_INDICATOR_CODE, donorNotes.getIndicatorCode());
		data.set(GPIEPConstants.FIELD_NOTES_DATE, DateTimeUtil.formatDate(donorNotes.getNotesDate(), GPIEPConstants.DATE_FORMAT));
		return data;
	}

	private static List<JsonBean> validateDonorNotes(JsonBean data) {
		List<JsonBean> validationErrors = new ArrayList<>();
		Long donorId = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_DONOR_ID)));
		Date date = DateTimeUtil.parseDate(data.getString(GPIEPConstants.FIELD_NOTES_DATE), GPIEPConstants.DATE_FORMAT);
		Long id = null;
		if (data.get(GPIEPConstants.FIELD_ID) != null) {
			id = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_ID)));
		}

		if (GPIUtils.checkDonorNotesExists(id, donorId, date, data.getString(GPIEPConstants.FIELD_INDICATOR_CODE))) {
			JsonBean error = new JsonBean();
			error.set(ApiError.getErrorCode(GPIErrors.DONOR_NOTES_DATE_DONOR_COMBINATION_EXISTS),
					GPIErrors.DONOR_NOTES_DATE_DONOR_COMBINATION_EXISTS.description);
			validationErrors.add(error);
		}

		return validationErrors;
	}

	public static JsonBean getDonorNotesList(Integer offset, Integer count, String orderBy, String sort, String indicatorCode) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}
		
		Integer total = GPIUtils.getDonorNotesCount(indicatorCode);
		List<AmpGPINiDonorNotes>  notesList = GPIUtils.getDonorNotesList(offset, count, orderBy, sort, total, indicatorCode);
		JsonBean data = new JsonBean();
		List<JsonBean> lst = new ArrayList<>();
		
		for (AmpGPINiDonorNotes notes : notesList) {
			lst.add(modelToJsonBean(notes));
		}
		
		data.set("data", lst);
		data.set(GPIEPConstants.TOTAL_RECORDS, total);
		return data;
	}
	
	public static JsonBean deleteDonorNotesById(Long id) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}
		
		JsonBean result = new JsonBean();
		GPIUtils.deleteDonorNotes(id);
		result.set(GPIEPConstants.RESULT, GPIEPConstants.DELETED);
		return result;
	}	
	
	private static boolean hasGPIDataRights() {		
		 TeamMember tm = TeamUtil.getCurrentMember(); AmpTeamMember atm =
		 TeamMemberUtil.getAmpTeamMember(tm.getMemberId()); 
		 return atm.getUser().hasNationalCoordinatorGroup() || atm.getUser().hasVerifiedDonor();		 
	}
	
	public static List<JsonBean> getUsersVerifiedOrganizations() {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}
		
		TeamMember tm = TeamUtil.getCurrentMember();
		AmpTeamMember atm = TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
		Set<AmpOrganisation> verifiedOrgs = atm.getUser().getAssignedOrgs();
		List<JsonBean> orgs = new ArrayList<>();
		
		for (AmpOrganisation verifiedOrg : verifiedOrgs) {			
				JsonBean org = new JsonBean();
				org.set("id", verifiedOrg.getAmpOrgId());
				org.set("name", verifiedOrg.getName());
				orgs.add(org);			
		}
		
		return orgs;
	}

	public static List<GPIRemark> getGPIRemarks(Long donorId, String donorType, Long from, Long to) {
		List<GPIRemark> remarks = new ArrayList<>();
		
		//TODO fectch data from DB
		remarks.add(new GPIRemark("Donor Agency 1", "2017-07-01", "Remark 1 Lorem ipsum dolor sit amet"));
		remarks.add(new GPIRemark("Donor Agency 2", "2015-12-02", "Remark 2 Lorem ipsum dolor sit amet"));
		remarks.add(new GPIRemark("Donor Agency 3", "2014-08-04", "Remark 3 Lorem ipsum dolor sit amet"));
		remarks.add(new GPIRemark("Donor Agency 1", "2015-03-20", "Remark 4 Lorem ipsum dolor sit amet"));
		
		return remarks;
	}

}
