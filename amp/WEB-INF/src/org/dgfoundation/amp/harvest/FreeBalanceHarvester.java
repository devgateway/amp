/**
 * FreeBalanceHarvester.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.harvest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.hibernate.Session;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpExternalMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTermsAssist;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 8, 2006
 * 
 */
public class FreeBalanceHarvester {

	//public static SimpleDateFormat dateFormatter = new SimpleDateFormat("") ;
	
	
	public static DecimalFormat mf = new DecimalFormat("###,###,###,###,###.##");
	
	public static final String separator = "~";

	private static Map colNames = null;

	private static List generateTableVals(String line) {
		ArrayList ret = new ArrayList();
		StringTokenizer st = new StringTokenizer(line, separator);
		while (st.hasMoreTokens()) {
			String element = (String) st.nextToken().trim();
			if(element==null) element=" ";
			ret.add(element);
		}
		return ret;
	}

	private static Map generateTableNames(String line) {
		Map ret = new HashMap();
		StringTokenizer st = new StringTokenizer(line, separator);
		int idx = 0;
		while (st.hasMoreTokens()) {
			String element = (String) st.nextToken().trim();
			ret.put(element, new Integer(idx++));
		}
		return ret;
	}

	private static String getTableVal(String columnName, List rowVals) {
		Integer idx = (Integer) colNames.get(columnName);
		String value = (String) rowVals.get(idx.intValue());

		return value;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//SessionFactory sf = StandaloneInitializer.initialize();

		Map regMap = DBUtil.getExternalMapping(AmpExternalMapping.SOURCE_FB,
				AmpExternalMapping.REGION_ID, true);
		Map donorMap = DBUtil.getExternalMapping(AmpExternalMapping.SOURCE_FB,
				AmpExternalMapping.DONOR_ID, true);
		Map projectMap = DBUtil.getExternalMapping(
				AmpExternalMapping.SOURCE_FB, AmpExternalMapping.PROJECT_ID,
				true);
		// Map
		// componentMap=DBUtil.getExternalMapping(AmpExternalMapping.SOURCE_FB,AmpExternalMapping.COMPONENT_ID,true);

		try {
			LineNumberReader lnr = new LineNumberReader(new BufferedReader(
					new FileReader("/home/mihai/ircbp.csv")));
			String line = null;
			Session session=PersistenceManager.getSession();
			
			int lineNr=1;
			
			do {
				if (line == null)
					colNames = generateTableNames(lnr.readLine());
				line = lnr.readLine();
				
				if(line==null) break;

				List vals = generateTableVals(line);

				boolean budget=false;
				
				String extActivityId=null;
				String componentName=null;
				String compFundName=null;
				String externalDonorId=null;
				String date=null;
				String amount=null;
				
				int trType=budget?Constants.COMMITMENT:Constants.EXPENDITURE;
				
				if(!budget) {
				
				 extActivityId = getTableVal(
						"trn1_47e_coa_elem_rolup_2.roll_into_ele_id", vals);
				// String
				// extComponentId=getTableVal("trn1_47e_coa_elem_rolup_1.roll_into_ele_id",vals);
				componentName = getTableVal(
						"trn1_47e_coa_elem_desc_2.coa_elem_desc", vals);
				compFundName = getTableVal(
						"trn1_47e_coa_elem_desc_3.coa_elem_desc", vals);
				externalDonorId=getTableVal("element_2", vals);
				date=getTableVal("exp_vchr_date", vals);

				amount = getTableVal(
						"exp_item_amt_dom", vals);
				
				} else {
					extActivityId = getTableVal(
							"trn1_47e_coa_elem_rolup_2.roll_into_ele_id", vals);

					componentName = getTableVal(
							"Component", vals);
					
					compFundName=  getTableVal(
							"Object", vals);
					
					date="01/01/2003";

					
					amount = getTableVal(
							"Ann. Budget", vals);
					
					externalDonorId=getTableVal("element_2", vals);
				}
				
				
				Number n=mf.parse(amount.replace('$',' ').trim());
				
				//do not add zeros
				if(n.doubleValue()==0) continue;
				
				////System.out.println("line="+(++lineNr)+" activity="+extActivityId+" component="+componentName+" expenditure="+compFundName+" amount="+n.toString());


				
				Long activityId = new Long((String) projectMap
						.get(extActivityId));
				

				
				
				Long donorId = new Long((String) donorMap
						.get(externalDonorId));
				
				
				AmpActivity a = (AmpActivity) session.load(AmpActivity.class,activityId);
				
				AmpOrganisation o=(AmpOrganisation) session.load(AmpOrganisation.class,donorId);
				
				AmpCurrency curr=(AmpCurrency) session.load(AmpCurrency.class,new Long(21));
				
				AmpTermsAssist ata=(AmpTermsAssist) session.load(AmpTermsAssist.class,new Long(1));
				
				//AmpModality am= (AmpModality) session.load(AmpModality.class,new Long(3));
				
//beginTransaction();

				//DONOR FUNDING INSERTION
				//check if we have the donor added:
				/*
				AmpFunding af = null;
				Iterator i = a.getFunding().iterator();
				while (i.hasNext()) {
					AmpFunding element = (AmpFunding) i.next();
					if (element.getAmpDonorOrgId().getOrgCode().equals(o.getOrgCode())) {
						af = element;
						break;
					}
				}

				
				if(af==null) {
					af=new AmpFunding();
					af.setAmpActivityId(a);
					af.setFundingDetails(new HashSet());
					af.setAmpTermsAssistId(ata);
					af.setAmpDonorOrgId(o);
					af.setModalityId(am);
					af.setFinancingId(o.getOrgCode());
					a.getFunding().add(af);
				}
				

//				 check if we have a funding
				AmpFundingDetail afd = null;
				i = af.getFundingDetails().iterator();
				while (i.hasNext()) {
					AmpFundingDetail element = (AmpFundingDetail) i
							.next();
					if (element.getExpCategory().equals(compFundName) && element.getTransactionType().intValue()==trType) {
						afd = element;
						
						//add amount
						
						double oldVal=0;
						if(element.getTransactionAmount()!=null) oldVal=element.getTransactionAmount().doubleValue();
						double newVal=n.doubleValue();
						
						element.setTransactionAmount(new Double(oldVal+newVal));
						
						break;
					}
				}
				if (afd == null) {
					afd = new AmpFundingDetail();
					afd.setExpCategory(compFundName);
					afd.setAmpCurrencyId(curr);
					afd.setAmpFundingId(af);
					afd.setAdjustmentType(new Integer(Constants.ACTUAL));
					afd.setTransactionType(new Integer(trType));
					afd.setReportingOrgId(o);
					afd.setOrgRoleCode(af.getAmpDonorOrgId().getName()+"-"+af.getAmpDonorOrgId().getOrgCode());
					if(!date.trim().equals(""))
						afd.setTransactionDate(dateFormatter.parse(date));
					afd.setPerspectiveId(ap);
					
					afd.setTransactionAmount(new Double(n.doubleValue()));
					af.getFundingDetails().add(afd);
				}

				
				*/
				
				// check if we already have a component with that name
				// if (componentMap.get(extComponentId)!=null) componentId=new
				// Long((String)componentMap.get(extComponentId));

				//COMPONENT INSERTION
				
				AmpComponent ac = null;
				Iterator i = a.getComponents().iterator();
				while (i.hasNext()) {
					AmpComponent element = (AmpComponent) i.next();
					if (element.getTitle().equals(componentName)) {
						ac = element;
						break;
					}
				}
				
				if (ac == null) {
					ac = new AmpComponent();
					
					//ac.setComponentFundings(new HashSet());
					ac.setDescription("");
					ac.setTitle(componentName);
				//	ac.setActivity(a);
					a.getComponents().add(ac);
				}

				// check if we have a funding	
				AmpComponentFunding acf = null;
				// i = ac.getComponentFundings().iterator();
				i = ComponentsUtil.getComponentFunding(ac.getAmpComponentId()).iterator();
				while (i.hasNext()) {
					AmpComponentFunding element = (AmpComponentFunding) i
							.next();
					if (element.getDescription().equals(compFundName) && element.getTransactionType().intValue()==trType) {
						acf = element;
						
						//add amount
						
						double oldVal=0;
						if(element.getTransactionAmount()!=null) oldVal=element.getTransactionAmount().doubleValue();
						double newVal=n.doubleValue();
						
						element.setTransactionAmount(new Double(oldVal+newVal));
						
						break;
					}
				}
				if (acf == null) {
					acf = new AmpComponentFunding();
					acf.setDescription(compFundName);
					acf.setCurrency(curr);
					acf.setComponent(ac);
					acf.setAdjustmentType(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getAmpCategoryValueFromDB());
					acf.setTransactionType(new Integer(trType));
					acf.setReportingOrganization(o);
					if(!date.trim().equals(""))
						acf.setTransactionDate(DateTimeUtil.parseDate(date));
					
					acf.setTransactionAmount(new Double(n.doubleValue()));
					//ac.getComponentFundings().add(acf);
				}
				
				session.saveOrUpdate(a);

				//tx.commit();
				
				
			} while (line != null);
			
			PersistenceManager.releaseSession(session);

			lnr.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}