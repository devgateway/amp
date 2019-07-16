package org.digijava.module.contentrepository.jcrentity;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;

import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

public class LabelDAO {
    private Session session;
    private HttpServletRequest request;
    private boolean writeable;
    
    
    public LabelDAO ( HttpServletRequest request, boolean writeable) {
        this.request    = request;
        this.writeable  = writeable;
        if ( writeable )
            this.session    = DocumentManagerUtil.getWriteSession(request);
        else
            this.session    = DocumentManagerUtil.getReadSession(request);
    }
    
    private void createRootNode() {
        
        Label rootLabel         = new RootLabel("RootLabel", "black", "white");
        try {
            Session writeSession    = DocumentManagerUtil.getWriteSession(request);
            rootLabel.saveState(writeSession.getRootNode());
            writeSession.logout();
            
            LabelDAO dao    = new LabelDAO(request, true);
            dao.testLabelsCreate();
            dao.saveAndCloseSession();
            
            this.session.logout();
            this.session    = DocumentManagerUtil.getReadSession(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void saveAndCloseSession() {
        try{
            this.session.save();
            this.session.logout();
            this.session    = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void populateListWithChildLabels (ArrayList<Label> list, Label parentLabel) {
        List<Label> children    = parentLabel.getChildren();
        if ( children != null && children.size() > 0 ) {
            for (Label child : children) {
                list.add(child);
                this.populateListWithChildLabels(list, child);
            }
        }
        
        
    }
    
    public RootLabel getRootLabel() {
        Node rootLabel  = null;
        try {
            rootLabel = session.getRootNode().getNode(CrConstants.LABEL_ROOT_NODE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ( rootLabel == null && !writeable ) {
            this.createRootNode();
            rootLabel   = DocumentManagerUtil.getNodeByPath(session, CrConstants.LABEL_ROOT_NODE_NAME);
        }
        return new RootLabel(rootLabel);
    }
    
    public List<Label> getAllLabels() {
        ArrayList<Label> list   = new ArrayList<Label>();
        Label rootLabel         = this.getRootLabel();
        this.populateListWithChildLabels(list, rootLabel);
        
        return list;
    }
    
    public Label getLabel(String uuid) {
        
        if (uuid != null) {
            Node node       = null;
            if ( this.writeable )
                node        = DocumentManagerUtil.getWriteNode(uuid, this.request);
            else
                node        = DocumentManagerUtil.getReadNode(uuid, this.request);
            Label label     = new Label(node);
            return label;
        }
        else 
            throw new NullPointerException("uuid parameter cannot be null");
    }
    
    public boolean deleteLabel (String uuid) {
        if (uuid != null) {
            Node node       = DocumentManagerUtil.getWriteNode(uuid, this.request);
            try {
                Node parent     = node.getParent();
                node.remove();
                parent.save();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public void testLabelsCreate() {
        Label rootLabel         = this.getRootLabel();
        if ( rootLabel.getChildren() == null || rootLabel.getChildren().size() == 0 ) {
        
            Label folder            = new Label("FOLDER", CrConstants.LABEL_TYPE_FOLDER, "black", "white");
            Label label1            = new Label("Label1", CrConstants.LABEL_TYPE_LEAF, "white", "red");
            Label label2            = new Label("Label2", CrConstants.LABEL_TYPE_LEAF, "white", "green");
            
            try {
                folder.saveState( rootLabel.getNode() );
                label1.saveState(folder.getNode() );
                label2.saveState(folder.getNode() );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
}
