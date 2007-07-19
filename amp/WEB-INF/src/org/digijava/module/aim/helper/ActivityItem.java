package org.digijava.module.aim.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * Activity helper bean.
 * Used to store activity data in more user friendly way.
 * Also suports converting Activity to XML for Ajax like use or any other use.
 * @author Irakli Kbiashvili
 *
 */
public class ActivityItem {

	public static final String TAG_NAME = "activity";
	
	public static final String DONORS_TAG_NAME = "donors";

	public static final String PARAM_STATUS = "status";

	public static final String PARAM_NAME = "name";

	public static final String PARAM_ID = "id";

	public static final String PARAM_PROPOSED_AMOUNT = "proposedAmount";

	public static final String PARAM_ACTUAL_AMOUNT = "actualAmount";
	
	public static final String PARAM_PLANNED_AMOUNT = "plannedAmount";
	
	public static final String PARAM_DATE = "date";

	private Long id;

	private String name;

	private String status;

	private String proposedAmount = "error";
	
	private String plannedAmount = "error";
	
	private String actualAmount = "error";

	private String startDate;
	//end date not used 
	private String endDate;
	
	private List donors;

	/**
	 * Default constructor.
	 * Not very usful.
	 */
	public ActivityItem() {

	}

	/**
	 * Constructs Helper bean from db entity.
	 * See next constructor, which is used in this one.
	 * @param entity AmpActivity bean
	 * @see ActivityItem#ActivityItem(AmpActivity, DateFormat)
	 */
	public ActivityItem(AmpActivity entity) throws Exception{
		this(entity,new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT));
	}

	/**
	 * Constructs helper bean from db entity.
	 * Also DateFormat is passed to frmat dates
	 * @param entity AmpActivity db entity to construct helper from
	 * @param frmt date formatter
	 */
	public ActivityItem(AmpActivity entity,DateFormat frmt) throws Exception {
		if (entity != null) {
			AmpCategoryValue statusValue	= CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, entity.getCategories());
			if (statusValue != null)
				status		= statusValue.getValue();
			id = entity.getAmpActivityId();
			name = entity.getName();
			name = name.replaceAll("&","&amp;");
			try {
				ActivityUtil.ActivityAmounts amounts = ActivityUtil.getActivityAmmountIn(entity,"USD");
				proposedAmount=amounts.proposedAmout();
				actualAmount=amounts.actualAmount();
				plannedAmount=amounts.plannedAmount();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (entity.getCreatedDate() != null){
				startDate = frmt.format(entity.getCreatedDate());
			}else{
				startDate = "";
			}
			donors = setDonors(entity.getFunding());
		}
	}

	/**
	 * Generates XML.
	 * Result will include some properties as attributes,
	 * and donors list as child nodes.
	 * @return String representing XML of the Activity.
	 */
	public String getXml() {
		String result = "";
		if (id != null) {
			result += "<" + TAG_NAME;
			result += " " + PARAM_ID + "=\"" + id.toString()+ "\"";
			result += " " + PARAM_STATUS + "=\"" + getStatusValue() + "\"";
			result += " " + PARAM_NAME + "=\"" + getNameValue() + "\"";
			result += " " + PARAM_PROPOSED_AMOUNT + "=\"" + getProposedAmount() + "\"";
			result += " " + PARAM_ACTUAL_AMOUNT + "=\"" + getActualAmount() + "\"";
			result += " " + PARAM_PLANNED_AMOUNT + "=\"" + getPlannedAmount() + "\"";
			result += " " + PARAM_DATE + "=\"" + getStartDate() + "\"";
			result += ">";
			result += "<" + DONORS_TAG_NAME + ">";
			result += getDonorsList();
			result += "</" + DONORS_TAG_NAME + ">";
			result += "</" + TAG_NAME + ">";
		}
		return result;
	}

	/**
	 * Constructs donors list for XML
	 * @return String representing donors tags with attributes
	 */
	private String getDonorsList(){
		String result = "";
		if (donors != null && donors.size()>0){
			for (Iterator iter = donors.iterator(); iter.hasNext();) {
				LabelValueBean lvb = (LabelValueBean) iter.next();
				result += "<donor id=\""+lvb.getValue()+"\" name=\""+lvb.getLabel()+"\"/>";
			}
		}
		return result;
	}
	
	/**
	 * Converts donor organizations Set from AmpActivity to list of LabelValueBeans
	 * Organizations name is label, and org Id is value of the bean.
	 * @param donors Set of funding orgs from AmpActivity
	 * @return list of LabelValueBean objects
	 */
	private List setDonors(Set donors){
		List result = new ArrayList();
		if (donors != null && donors.size() > 0){
			for (Iterator iter = donors.iterator(); iter.hasNext();) {
				AmpFunding donor = (AmpFunding) iter.next();
				String donorName = donor.getAmpDonorOrgId().getName();
				Long donorId = donor.getAmpDonorOrgId().getAmpOrgId();
				LabelValueBean lvb = new LabelValueBean(donorName,donorId.toString());
				result.add(lvb);
			}
		}
		return result;
	}
	
	private String getStatusValue() {
		return getStatus();
	}

	private String getNameValue() {
		return getName();
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public List getDonors() {
		return donors;
	}

	public void setDonors(List donors) {
		this.donors = donors;
	}

	public String getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(String actualAmount) {
		this.actualAmount = actualAmount;
	}

	public String getPlannedAmount() {
		return plannedAmount;
	}

	public void setPlannedAmount(String plannedAmount) {
		this.plannedAmount = plannedAmount;
	}

	public String getProposedAmount() {
		return proposedAmount;
	}

	public void setProposedAmount(String proposedAmount) {
		this.proposedAmount = proposedAmount;
	}

}
