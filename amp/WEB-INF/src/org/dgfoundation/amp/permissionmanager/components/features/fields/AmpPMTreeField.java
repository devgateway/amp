/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.fields;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;

/**
 * @author dan
 *
 */
public class AmpPMTreeField extends Panel {




    /**
     * @param id
     * @param model
     * @param fmName
     */
    public AmpPMTreeField(String id, IModel<?> model, String fmName) {
        super(id);
        // TODO Auto-generated constructor stub
        
        CheckBox fieldCheck =   new CheckBox("fieldFM", new PropertyModel(((DefaultMutableTreeNode)model.getObject()).getUserObject(), "checked"));
        fieldCheck.setOutputMarkupId(true);
        add(fieldCheck);
        
        Label fieldName = new Label("fieldFMName",((DefaultMutableTreeNode)model.getObject()).getUserObject().toString());
        fieldName.setOutputMarkupId(true);
        add(fieldName);
    }

}
