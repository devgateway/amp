/**
 * 
 */
package org.digijava.module.aim.helper;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.exception.NoDocumentTypeException;
import org.digijava.module.contentrepository.action.SelectDocumentDM;

/**
 * @author Alex Gartner
 *
 */
public class ActivityDocumentsUtil {
	public static void injectActivityDocuments(HttpServletRequest request, Set<AmpActivityDocument> docs) throws NoDocumentTypeException {
    	Iterator<AmpActivityDocument> iter		= docs.iterator();
    	
    	while (iter.hasNext()) {
    		AmpActivityDocument doc	= iter.next();
    		if (doc.getDocumentType() == null)
    					throw new NoDocumentTypeException("No document type found in document with uuid " + doc.getUuid());
    		HashSet<String> UUIDs	= SelectDocumentDM.getSelectedDocsSet(request, doc.getDocumentType(), true);
    		UUIDs.add(doc.getUuid());
    	}
		
	}

}
