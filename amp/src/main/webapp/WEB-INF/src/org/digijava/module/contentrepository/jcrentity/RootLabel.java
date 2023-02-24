/**
 * 
 */
package org.digijava.module.contentrepository.jcrentity;

import java.net.URLEncoder;

import javax.jcr.Node;
import javax.jcr.Session;

import org.digijava.module.contentrepository.exception.LabelPropertiesEmptyCrException;
import org.digijava.module.contentrepository.helper.CrConstants;

/**
 * @author Alex Gartner
 *
 */
public class RootLabel extends Label { 

    public RootLabel(Node node) {
        super(node);
    }

    public RootLabel(String name, String color,
            String backgroundColor) {
        super(name, CrConstants.LABEL_TYPE_FOLDER, color, backgroundColor);
    }
    
    @Override
    public void saveState (Node parentNode) throws Exception  {
        Session session     = null;
        if ( parentNode != null) {
            this.setNode( parentNode.addNode( CrConstants.LABEL_ROOT_NODE_NAME ) );
            this.getNode().addMixin("mix:referenceable");
            session         = parentNode.getSession();
        }
        else 
            session         = this.getNode().getSession();
        
        if ( this.getName() != null && this.getColor() != null && this.getBackgroundColor() != null ) {
            
            this.getNode().setProperty(CrConstants.LABEL_PROPERTY_NAME, URLEncoder.encode(this.getName(), "UTF-8") );
            this.getNode().setProperty(CrConstants.LABEL_PROPERTY_COLOR, this.getColor() );
            this.getNode().setProperty(CrConstants.LABEL_PROPERTY_BACKGROUNDCOLOR, this.getBackgroundColor() );
            this.getNode().setProperty(CrConstants.LABEL_PROPERTY_TYPE, this.getType() );
            
            session.save();
        }
        else
            throw new LabelPropertiesEmptyCrException("Name, color, bgcolor property of RootLabel object should not be empty when saving the state to jcr");
    }
    
}
