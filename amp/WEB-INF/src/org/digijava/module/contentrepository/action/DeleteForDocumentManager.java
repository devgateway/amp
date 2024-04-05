/**
 * 
 */
package org.digijava.module.contentrepository.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import javax.jcr.Node;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Alex Gartner
 *
 */
public class DeleteForDocumentManager extends Action {
        public ActionForward execute(ActionMapping mapping, ActionForm form,
                javax.servlet.http.HttpServletRequest request,
                javax.servlet.http.HttpServletResponse response)
                throws java.lang.Exception {
            String nodeUUID     = request.getParameter("uuid");
            if (nodeUUID == null)
                throw new Exception("Parameter uuid is required.");
            
            Node node                   = DocumentManagerUtil.getWriteNode(nodeUUID, request);
            NodeWrapper nodeWrapper     = new NodeWrapper(node);
            
            Collection<KeyValue> objs   = nodeWrapper.getObjectsUsingThisDocument();
            if  ( objs != null && objs.size() > 0 ) {
                Map<String,String> msgMap   = keyValueColToMessageMap( objs );
                request.setAttribute("DeleteDocumentResponseCode", "errDocInUse" );
                request.setAttribute("DeleteDocumentResponse", msgMap);
                return mapping.findForward("forward");
            }
            
            
            Boolean deleted             = nodeWrapper.deleteNode(request);
            //PrintWriter out               = response.getWriter();
            
            if ( deleted == null ) {
                request.setAttribute("DeleteDocumentResponseCode", "errGeneral");
                request.setAttribute("DeleteDocumentResponse", "Error! Document Manager is unable to delete the specified document.");
                //out.println("<font color='red'>Error. Document Manager is unable to delete the specified document</font>");
            }
            else {
                    if ( !deleted.booleanValue() ) {
                        request.setAttribute("DeleteDocumentResponseCode", "errRights");
                        request.setAttribute("DeleteDocumentResponse", "Error! You do not have the right to delete the specified document.");
                        //out.println("<font color='red'>Error! You do not have the right to delete the specified document</font>");
                    }
                    else {
                        request.setAttribute("DeleteDocumentResponseCode", "ok");
                        request.setAttribute("DeleteDocumentResponse", "Document deleted successfully");
                        //out.println("<div id='successfullDiv'>Document deleted successfully</div>");
                    }
            }
            DocumentManagerUtil.logoutJcrSessions(request);
            return mapping.findForward("forward");
        }
        
        private static Map<String,String> keyValueColToMessageMap(Collection<KeyValue> keyValueCol) {
            HashMap<String, String> map = new HashMap<String, String>(); 
            Iterator<KeyValue> iter     = keyValueCol.iterator();
            
            while ( iter.hasNext() ) {
                KeyValue kv     = iter.next();
                if ( !map.containsKey(kv.getKey()) ) {
                    map.put(kv.getKey(), kv.getValue() );
                }
                else
                    map.put(kv.getKey(), map.get(kv.getKey())+", "+kv.getValue()  );
            }
            
            return map;
        }

}
