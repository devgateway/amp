package org.digijava.module.contentrepository.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.contentrepository.dbentity.NodeLastApprovedVersion;
import org.digijava.module.contentrepository.dbentity.TeamNodePendingVersion;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

public class ApproveVersion extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
        String versionId=request.getParameter("versionId");
        String baseNodeUUID = request.getParameter("baseNodeUUID");
        //remove record that hided this node from team members
        TeamNodePendingVersion pendingVersion=DocumentManagerUtil.isGivenVersionPendingApproval(versionId);
        if(pendingVersion!=null){
            DbUtil.delete(pendingVersion);
        }       
        //update team node's last approved version
        NodeLastApprovedVersion lastAppVersion=DocumentManagerUtil.getlastApprovedVersionOfTeamNode(baseNodeUUID);
        //String lastApprovedNodeVersionUUID=DocumentManagerUtil.getNodeOfLastVersion(baseNodeUUID, request).getUUID();
        if(lastAppVersion!=null){
            lastAppVersion.setVersionID(versionId);
        }else{
            lastAppVersion=new NodeLastApprovedVersion(baseNodeUUID, versionId);
        }                   
        DbUtil.saveOrUpdateObject(lastAppVersion);
        DocumentManagerUtil.logoutJcrSessions(request);
        return null;
    }
}
