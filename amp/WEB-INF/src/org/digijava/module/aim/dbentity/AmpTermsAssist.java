package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

/**
 * 
 * @deprecated use AmpCategoryValue instead from Category Manager
 *
 */
public class AmpTermsAssist implements Serializable
{
	private Long ampTermsAssistId ;
	private String termsAssistCode ;
	private String termsAssistName ;
	
	

	/**
	 * @return
	 */
	public Long getAmpTermsAssistId() {
		return ampTermsAssistId;
	}

	/**
	 * @return
	 */
	public String getTermsAssistCode() {
		return termsAssistCode;
	}

	/**
	 * @return
	 */
	public String getTermsAssistName() {
		return termsAssistName;
	}

	/**
	 * @param long1
	 */
	public void setAmpTermsAssistId(Long long1) {
		ampTermsAssistId = long1;
	}

	/**
	 * @param string
	 */
	public void setTermsAssistCode(String string) {
		termsAssistCode = string;
	}

	/**
	 * @param string
	 */
	public void setTermsAssistName(String string) {
		termsAssistName = string;
	}

}