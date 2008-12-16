/**
 * 
 */
package org.dgfoundation.amp.test.contentrepository;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.digijava.module.contentrepository.action.DocumentManager;
import org.digijava.module.contentrepository.form.DocumentManagerForm;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

/**
 * @author MOUHAMAD
 * 
 */
public class DocumentManagerDefaultTest extends BasicActionTestCaseAdapter {

	/**
	 *  
	 */
	protected static final int INFO = 1;
	protected static final int ERROR = 2;

	/**
	 * 
	 */
	protected static Logger logger = Logger
			.getLogger(DocumentManagerDefaultTest.class);

	/**
	 * 
	 */
	protected DocumentManager documentManagerAction;
	protected DocumentManagerForm myForm;
	protected MockHttpSession session;
	protected MockHttpServletRequest request;

	/**
	 * 
	 */
	public void setUp() throws Exception {
		log("Entering setUp method");
		//
		super.setUp();
		Configuration.initConfig();
		//
		ServletContext context = getActionMockObjectFactory()
				.getMockServletContext();
		context.setAttribute(DocumentManager.class.getName(),
				documentManagerAction);
		//
		documentManagerAction = new DocumentManager();
		myForm = (DocumentManagerForm) createActionForm(DocumentManagerForm.class);
		//
		session = getActionMockObjectFactory().getMockSession();
		request = getActionMockObjectFactory().getMockRequest();
		//
		TestUtil.setLocaleEn(request);
		TestUtil.setSiteDomain(request);
		TestUtil.setCurrentMemberFirstATLTeam(session);
		//		
		log("Exiting setUp method");
	}

	/**
	 * 
	 */
	protected void setDocumentManagerForm(boolean ajaxDocList, String docDesc,
			Long docLang, String doclistSession, String docNotes,
			String docTitle, Long docType, FormFile fileData, String type,
			String uuid, String webLink, boolean webResource) {
		if (myForm != null) {
			log("myForm not null");
			myForm.setAjaxDocumentList(ajaxDocList);
			myForm.setDocDescription(docDesc);
			myForm.setDocLang(docLang);
			myForm.setDocListInSession(doclistSession);
			myForm.setDocNotes(docNotes);
			myForm.setDocTitle(docTitle);
			myForm.setDocType(docType);
			myForm.setFileData(fileData);
			myForm.setType(type);
			myForm.setUuid(uuid);
			myForm.setWebLink(webLink);
			myForm.setWebResource(webResource);
		}
	}

	/**
	 * 
	 */
	protected static void log(String msg) {
		log(INFO, null, msg);
	}

	/**
	 * 
	 */
	protected static void log(Throwable t, String msg) {
		log(ERROR, null, msg);
	}

	/**
	 * 
	 */
	protected static void log(int type, Throwable t, String msg) {
		switch (type) {
		case INFO:
			logger.info(msg);
			break;
		case ERROR:
			if (t != null) {
				logger.error(msg, t);
			}
			break;
		default:
		}
	}
}
