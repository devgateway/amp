/**
 * 
 */
package org.digijava.module.dataExchange.dbentity;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.dataExchange.util.DataExchangeConstants;

/**
 * @author dan
 *
 */
public class DEMappingFieldsDisplay {
	

	/**
	 * 
	 */
	
	private HashMap<Long,String> labels;
	private DEMappingFields ampField;
	
	public DEMappingFields getAmpField() {
		return ampField;
	}

	public void setAmpField(DEMappingFields ampField) {
		this.ampField = ampField;
	}

	public HashMap<Long, String> getLabels() {
		return labels;
	}

	public void setLabels(HashMap<Long, String> labels) {
		this.labels = labels;
	}

	public DEMappingFieldsDisplay() {
		// TODO Auto-generated constructor stub
	}

	public DEMappingFieldsDisplay(DEMappingFields f, Collection c) {
		super();
		this.ampField = f;
		this.labels = new HashMap<Long,String> ();
		this.labels.put(new Long(-1), "Add new");
//		public static final String IATI_ORGANIZATION_TYPE = "Organization Type";
//		public static final String IATI_ORGANIZATION = "Organization";
//		public static final String IATI_LOCATION = "Location";
//		public static final String IATI_ACTIVITY_STATUS = "Activity Status";
//		public static final String IATI_VOCABULARY_CODE = "Vocabulary Code";
//		public static final String IATI_SECTOR = "Sector";
//		public static final String IATI_FINANCE_TYPE = "Finance Type";
//		public static final String IATI_AID_TYPE = "Aid Type";
//		public static final String IATI_DISBURSEMENT_CHANNEL = "Disbursement Channel";
//		public static final String TEST = "test";
//		public static final String IATI_ACTIVITY = "Activity";
		
		if(DataExchangeConstants.IATI_ORGANIZATION_TYPE.compareTo(f.getIatiPath())==0){
			for (Iterator it = c.iterator(); it.hasNext();) {
				AmpOrgType ampOrgType = (AmpOrgType) it.next();
				this.labels.put(ampOrgType.getAmpOrgTypeId(), ampOrgType.getLabel());
			}
		}
		if(DataExchangeConstants.IATI_ORGANIZATION.compareTo(f.getIatiPath())==0){
			
		}
		if(DataExchangeConstants.IATI_LOCATION.compareTo(f.getIatiPath())==0){
			
		}
		if(DataExchangeConstants.IATI_ACTIVITY_STATUS.compareTo(f.getIatiPath())==0){
			
		}
		if(DataExchangeConstants.IATI_VOCABULARY_CODE.compareTo(f.getIatiPath())==0){
			
		}
		if(DataExchangeConstants.IATI_SECTOR.compareTo(f.getIatiPath())==0){
			
		}
		if(DataExchangeConstants.IATI_FINANCE_TYPE.compareTo(f.getIatiPath())==0){
			
		}
		if(DataExchangeConstants.IATI_AID_TYPE.compareTo(f.getIatiPath())==0){
			
		}
		if(DataExchangeConstants.IATI_DISBURSEMENT_CHANNEL.compareTo(f.getIatiPath())==0){
			
		}
		if(DataExchangeConstants.IATI_ACTIVITY.compareTo(f.getIatiPath())==0){
			
			for (Iterator it = c.iterator(); it.hasNext();) {
				AmpActivity item = (AmpActivity) it.next();
				this.labels.put(item.getAmpActivityGroup().getAmpActivityGroupId(), item.getName());
			}
		}
	}
	

}
