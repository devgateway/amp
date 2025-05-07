/**
 * 
 */
package org.digijava.module.contentrepository.jcrentity;

import org.digijava.module.contentrepository.exception.LabelPropertiesEmptyCrException;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import javax.jcr.*;
import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Gartner
 *
 */
public class Label implements Serializable {
    
    private String name;
    
    /* FOLDER / LEAF */
    private String type;
    private List<Label> children;
    private String color;
    private String backgroundColor;
    private String uuid;
    
    transient private Node node;
    
    public Label ( Node node ) {
        this.node           = node;
        this.loadState();
    }
    
    

    public Label(String name, String type, String color,
            String backgroundColor) {
        this.name = name;
        this.type = type;
        this.color = color;
        this.backgroundColor = backgroundColor;
    }

    public void loadState () {
        
        Property name               =  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.LABEL_PROPERTY_NAME);
        Property color              =  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.LABEL_PROPERTY_COLOR);
        Property type               =  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.LABEL_PROPERTY_TYPE);
        Property backgroundColor    =  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.LABEL_PROPERTY_BACKGROUNDCOLOR);
        
        if ( name != null && backgroundColor != null && color != null && 
                type != null  ) {
            try {
                this.name               = URLDecoder.decode( name.getString() ,"UTF-8");
                this.color              = color.getString();
                this.backgroundColor    = backgroundColor.getString();
                this.type               = type.getString();
                this.uuid               = node.getIdentifier();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
    }
    
    /**
     * 
     * @param parentNode either parentNode (in case one is creating a new label or 
     * this.node (in case of editing an existing label) needs to be not null. In this order. 
     */
    public void saveState (Node parentNode) throws Exception  {
        Session session     = null;
        if ( parentNode != null) {
            this.node       = parentNode.addNode( CrConstants.LABEL_NODE_NAME );
            this.node.addMixin("mix:referenceable");
            session         = parentNode.getSession();
        }
        else 
            session         = this.node.getSession();
        
        if ( name != null && color != null && backgroundColor != null && type != null ) {
            this.node.setProperty(CrConstants.LABEL_PROPERTY_NAME, URLEncoder.encode(this.name, "UTF-8") );
            this.node.setProperty(CrConstants.LABEL_PROPERTY_COLOR, this.color );
            this.node.setProperty(CrConstants.LABEL_PROPERTY_BACKGROUNDCOLOR, this.backgroundColor );
            this.node.setProperty(CrConstants.LABEL_PROPERTY_TYPE, this.type );
            
            session.save();
        }
        else
            throw new LabelPropertiesEmptyCrException("No properties of the Label object should be empty when saving the state to jcr");
    }
    
    @Override
    public boolean equals(Object obj) {
        if ( ! (obj instanceof Label) )
            return false;
        Label l     = (Label) obj;
        if ( this.uuid == null || l.getUuid() == null ) {
            return false;
        }
        
        return this.uuid.equals( l.getUuid() );
    };

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the children
     */
    public List<Label> getChildren() {
        if ( this.children != null )
            return this.children;
        else {
            this.children = new ArrayList<Label>();
            try {
                NodeIterator nIter  = this.node.getNodes();
                while (nIter.hasNext()) {
                    Node child          = nIter.nextNode();
                    Label childLabel    = new Label(child);
                    this.children.add(childLabel);
                }
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
            return this.children;
        }
    }


    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the backgroundColor
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return the node
     */
    public Node getNode() {
        return node;
    }

    /**
     * @param node the node to set
     */
    public void setNode(Node node) {
        this.node = node;
    }



    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }



    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    
    
}

