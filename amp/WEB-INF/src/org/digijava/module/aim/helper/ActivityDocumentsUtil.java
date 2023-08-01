/**
 * 
 */
package org.digijava.module.aim.helper;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.exception.NoDocumentTypeException;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Alex Gartner
 *
 */
public class ActivityDocumentsUtil {
    /**
     * 
     * @param request
     * @param docs - a set of AmpActivityDocument objects
     * @throws NoDocumentTypeException
     */
    public static void injectActivityDocuments(HttpServletRequest request, Set<AmpActivityDocument> docs) throws NoDocumentTypeException {
        Iterator<AmpActivityDocument> iter      = docs.iterator();
        
        while (iter.hasNext()) {
            AmpActivityDocument doc = iter.next();
            if (doc.getDocumentType() == null)
                        throw new NoDocumentTypeException("No document type found in document with uuid " + doc.getUuid());
            HashSet<String> UUIDs   = SelectDocumentDM.getSelectedDocsSet(request, doc.getDocumentType(), true);
            UUIDs.add(doc.getUuid());
        }
        
    }
    
    public static Collection<String> getNamesOfActForDoc(String uuid) {
        Session session = null;
        Query qry = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select " + AmpActivityVersion.hqlStringForName("a") + " " +
                " from " + AmpActivityDocument.class.getName() + " ad, " + AmpActivityVersion.class.getName() + " a " +
                " where ad.ampActivity=a AND ad.uuid=:uuid";
            
            qry     = session.createQuery(queryString);
            qry.setString("uuid", uuid);
            
            Collection<String> ret  = qry.list();
            return ret;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

}
