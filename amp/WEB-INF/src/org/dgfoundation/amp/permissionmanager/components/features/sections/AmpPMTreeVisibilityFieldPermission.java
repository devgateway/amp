/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Alignment;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Unit;
import org.apache.wicket.extensions.markup.html.tree.table.IColumn;
import org.apache.wicket.extensions.markup.html.tree.table.PropertyRenderableColumn;
import org.apache.wicket.extensions.markup.html.tree.table.PropertyTreeColumn;
import org.apache.wicket.markup.html.tree.AbstractTree;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMLinkTree;

/**
 * @author dan
 *
 */
public class AmpPMTreeVisibilityFieldPermission extends AmpPMBaseTreePanel {

	private LinkTree tree;
	
	/**
	 * @param id
	 * @param fmName
	 * @param fmType
	 */
	public AmpPMTreeVisibilityFieldPermission(String id, String fmName,
			AmpFMTypes fmType) {
		super(id, fmName, fmType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param fmName
	 */
	public AmpPMTreeVisibilityFieldPermission(String id, String fmName) {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param fmBehavior
	 */
	public AmpPMTreeVisibilityFieldPermission(String id, IModel model, String fmName, AmpFMTypes fmBehavior) {
		super(id, model, fmName, fmBehavior);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 */
	public AmpPMTreeVisibilityFieldPermission(String id, IModel model, String fmName) {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
        IColumn columns[] = new IColumn[] {
                new PropertyTreeColumn(new ColumnLocation(Alignment.MIDDLE, 8, Unit.PROPORTIONAL),"Tree Column (middle)", "userObject.property1"),
                new PropertyRenderableColumn(new ColumnLocation(Alignment.LEFT, 7, Unit.EM), "L2","userObject.property2"), };

        //tree = new TreeTable("tree", createTreeModel(), columns);
        tree = new AmpPMLinkTree("tree", createTreeModel());
        tree.getTreeState().setAllowSelectMultiple(false);
        add(tree);
        tree.getTreeState().collapseAll();
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.permissionmanager.components.features.sections.AmpPMBaseTreePanel#getTree()
	 */
	@Override
	protected AbstractTree getTree() {
		// TODO Auto-generated method stub
		return tree;
	}

}
