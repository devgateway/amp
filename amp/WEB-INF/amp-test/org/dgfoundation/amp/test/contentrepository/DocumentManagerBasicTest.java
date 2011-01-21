/**
 * 
 */
package org.dgfoundation.amp.test.contentrepository;

import org.apache.struts.action.ActionForward;
import org.digijava.module.contentrepository.action.DocumentManager;

/**
 * @author MOUHAMAD
 * 
 */
public class DocumentManagerBasicTest extends DocumentManagerDefaultTest {

	/**
	 * 
	 */
	public void testAddDocument() {
		log("Entering testAddDocument");
		//
		setDocumentManagerForm(false, null, null, null, null, null, null, null,
				null, null, null, false);
		//
		ActionForward forward = actionPerform(DocumentManager.class, myForm);
		//
		log("Forwarded to : " + forward.getName());
		//
		verifyNoActionMessages();
		verifyNoActionMessages();
		//
		verifyForward(forward.getName());
		//
		log("Exiting testAddDocument");
	}

	/**
	 * 
	 */
	public void testDownloadDocument() {
		log("Entering testDownloadDocument");
		//
		log("Exiting testDownloadDocument");
	}

	/**
	 * 
	 */
	public void testAddVersion2Document() {
		log("Entering testAddVersion2Document");
		//
		log("Exiting testAddVersion2Document");
	}

	/**
	 * 
	 */
	public void testShowDifferentDocumentVersion() {
		log("Entering testShowDifferentDocumentVersion");
		//
		log("Exiting testShowDifferentDocumentVersion");
	}

	/**
	 * 
	 */
	public void testPublishDocument() {
		log("Entering testPublishDocument");
		//
		log("Exiting testPublishDocument");
	}

	/**
	 * 
	 */
	public void testUnPublishDocument() {
		log("Entering testUnPublishDocument");
		//
		log("Exiting testUnPublishDocument");
	}

	/**
	 * 
	 */
	public void testDeleteDocument() {
		log("Entering testDeleteDocument");
		//
		log("Exiting testDeleteDocument");
	}
}
