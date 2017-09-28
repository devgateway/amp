package org.digijava.module.contentrepository.action;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

public class RejectVersion extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
        String versionId        =request.getParameter("versionId");
        String baseNodeUUID     = request.getParameter("baseNodeUUID");
        
        Node node= DocumentManagerUtil.getWriteNode(baseNodeUUID, request);
        String baseVersionUuid  = node.getBaseVersion().getUUID();
        VersionHistory vh       = node.getVersionHistory();
        VersionIterator vit     = vh.getAllVersions();  
        
        Version prevVersion     = null;
        
        while (vit.hasNext()) {
            Version v = vit.nextVersion();
            NodeIterator nIter  = v.getNodes();
            if ( nIter.hasNext() ) {
                Node n              = nIter.nextNode();
                if ( n.getUUID().equals(versionId) ){
                    if ( baseVersionUuid.equals(v.getUUID()) && prevVersion != null ) {
                        node.restore( prevVersion, false);
                    }
                    vh.removeVersion(v.getName());
                    DocumentManagerUtil.deleteTeamNodePendingVersion(baseNodeUUID, versionId);
                    break;
                }
            }
            prevVersion         = v;
        }
        DocumentManagerUtil.logoutJcrSessions(request);
        return null;
    }
}

