/**
 * 
 */
package org.digijava.module.dataExchange.dbentity;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
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
	private TreeMap<Long,String> sortedLabels;
	private DEMappingFields ampField;
	
	public TreeMap<Long, String> getSortedLabels() {
//		ValueComparator bvc =  new ValueComparator(this.labels);
//		sortedLabels = new TreeMap<Long,String>(bvc);
//		sortedLabels.putAll(this.labels);
		return sortedLabels;
	}
	
	public void setSortedLabels(TreeMap<Long, String> sortedLabels) {
		this.sortedLabels = sortedLabels;
	}
	public DEMappingFields getAmpField() {
		return ampField;
	}

	public void setAmpField(DEMappingFields ampField) {
		this.ampField = ampField;
	}

	public HashMap<Long, String> getLabels() {
		

		return labels;
	}

	private void sortLabels(){
		ValueComparator bvc =  new ValueComparator(this.labels);
		sortedLabels = new TreeMap<Long,String>(bvc);
		sortedLabels.putAll(this.labels);
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
//		this.labels.put(new Long(-1), "Add new");
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
			for (Iterator<AmpOrgType> it = c.iterator(); it.hasNext();) {
				AmpOrgType item = (AmpOrgType) it.next();
				this.labels.put(item.getAmpOrgTypeId(), item.getLabel());
			}
		}
		if(DataExchangeConstants.IATI_ORGANIZATION.compareTo(f.getIatiPath())==0){
			for (Iterator<AmpOrganisation> it = c.iterator(); it.hasNext();) {
				AmpOrganisation item = (AmpOrganisation) it.next();
				this.labels.put(item.getAmpOrgId(), item.getLabel());
			}
		}
		if(DataExchangeConstants.IATI_LOCATION.compareTo(f.getIatiPath())==0){
			for (Iterator<AmpCategoryValueLocations> it = c.iterator(); it.hasNext();) {
				AmpCategoryValueLocations item = (AmpCategoryValueLocations) it.next();
				this.labels.put(item.getId(), item.getName());
			}
		}
		if(DataExchangeConstants.IATI_ACTIVITY_STATUS.compareTo(f.getIatiPath())==0){
			for (Iterator<AmpCategoryValue> it = c.iterator(); it.hasNext();) {
				AmpCategoryValue item = (AmpCategoryValue) it.next();
				this.labels.put(item.getId(), item.getLabel());
			}
		}
		if(DataExchangeConstants.IATI_VOCABULARY_CODE.compareTo(f.getIatiPath())==0){
			for (Iterator<AmpSectorScheme> it = c.iterator(); it.hasNext();) {
				AmpSectorScheme item = (AmpSectorScheme) it.next();
				this.labels.put(item.getAmpSecSchemeId(), item.getSecSchemeName());
			}
		}
		if(DataExchangeConstants.IATI_SECTOR.compareTo(f.getIatiPath())==0){
			for (Iterator<AmpSector> it = c.iterator(); it.hasNext();) {
				AmpSector item = (AmpSector) it.next();
				this.labels.put(item.getAmpSectorId(), item.getLabel());
			}
		}
		//type of assistance
		if(DataExchangeConstants.IATI_FINANCE_TYPE.compareTo(f.getIatiPath())==0){
			for (Iterator<AmpCategoryValue> it = c.iterator(); it.hasNext();) {
				AmpCategoryValue item = (AmpCategoryValue) it.next();
				this.labels.put(item.getId(), item.getLabel());
			}
		}
		//financing instrument
		if(DataExchangeConstants.IATI_AID_TYPE.compareTo(f.getIatiPath())==0){
			for (Iterator<AmpCategoryValue> it = c.iterator(); it.hasNext();) {
				AmpCategoryValue item = (AmpCategoryValue) it.next();
				this.labels.put(item.getId(), item.getLabel());
			}
		}
		if(DataExchangeConstants.IATI_DISBURSEMENT_CHANNEL.compareTo(f.getIatiPath())==0){
			for (Iterator<AmpCategoryValue> it = c.iterator(); it.hasNext();) {
				AmpCategoryValue item = (AmpCategoryValue) it.next();
				this.labels.put(item.getId(), item.getLabel());
			}
		}
		if(DataExchangeConstants.IATI_ACTIVITY.compareTo(f.getIatiPath())==0){
			
			for (Iterator it = c.iterator(); it.hasNext();) {
				AmpActivity item = (AmpActivity) it.next();
				this.labels.put(item.getAmpActivityGroup().getAmpActivityGroupId(), item.getName());
			}
		}
		this.sortLabels();
	}
	
	class ValueComparator implements Comparator {

		  Map base;
		  public ValueComparator(Map base) {
		      this.base = base;
		  }

		  public int compare(Object a, Object b) {
//			if("Add new".compareTo((String)this.base.get(a)) ==0) return -1;
//			if("Add new".compareTo((String)this.base.get(b)) ==0) return 1;
		    return ((String)this.base.get(a)).compareTo((String)this.base.get(b));
		}
	}

}
