/**
 * 
 */
package org.digijava.module.dataExchange.iati;

import org.apache.commons.lang.ArrayUtils;

/**
 * Stores Import/Export rules
 * @author Nadejda Mandrescu
 */
public class IatiRules {
	public static final IatiVersion[] IMPORT_VERSIONS = {
		IatiVersion.V_1_01
		};
	public static final IatiVersion[] EXPORT_VERSIONS = {
		IatiVersion.V_1_03
		};
	
	private static final String[] MULTILINGUAL_ELEMENTS_V1_01 = {
		"iati-activity/title", 
		"iati-activity/description", 
		"iati-activity/transaction/description",
		"iati-activity/result/title",
		"iati-activity/result/description",
		"iati-activity/result/indicator/description",
		"iati-activity/result/indicator/baseline/comment",
		"iati-activity/result/indicator/period/target/comment",
		"iati-activity/result/indicator/period/actual/comment"
		};
	private static final String[] MULTILINGUAL_ELEMENTS_V1_02 =  MULTILINGUAL_ELEMENTS_V1_01;
	private static final String[] MULTILINGUAL_ELEMENTS_V1_03 =  (String[])ArrayUtils.addAll(MULTILINGUAL_ELEMENTS_V1_02, new String[] {
		"iati-activity/crs-add/loan-terms/repayment-type",
		"iati-activities/iati-activity/crs-add/loan-terms/repayment-plan"
	});
	private static final String[] MULTILINGUAL_ELEMENTS_V1_04 =  (String[])ArrayUtils.addAll(MULTILINGUAL_ELEMENTS_V1_03, new String[]{
		"iati-activity/conditions/condition/"
	});
	/**
	 * @param iatiVersion the IATI version
	 * @return list of IATI elements which translation can be stored in AmpActivity as a translated content
	 */
	public static String[] getMultilingualElements(IatiVersion iatiVersion) {
		switch(iatiVersion) {
		case V_1_01: return MULTILINGUAL_ELEMENTS_V1_01;
		case V_1_02: return MULTILINGUAL_ELEMENTS_V1_02;
		case V_1_03: return MULTILINGUAL_ELEMENTS_V1_03;
		case V_1_04: return MULTILINGUAL_ELEMENTS_V1_04; 
		default: return null; 
		}
	}
}
