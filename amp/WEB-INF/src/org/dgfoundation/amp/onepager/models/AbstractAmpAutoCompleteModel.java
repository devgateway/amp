/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.model.LoadableDetachableModel;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.AmpComboboxDisplayable;

/**
 * @author mpostelnicu@dgateway.org
 * since Oct 13, 2010
 */
public abstract class AbstractAmpAutoCompleteModel<T> extends
		LoadableDetachableModel<java.util.List<T>> {
	
	private static final Integer MAX_RESULTS_VALUE=0;
	
	public enum PARAM implements AmpAutoCompleteModelParam {MAX_RESULTS};
	
	private static final long serialVersionUID = 361801375994474348L;

	public Map<AmpAutoCompleteModelParam, Object> getParams() {
		return params;
	}

	protected Map<AmpAutoCompleteModelParam, Object> params;
	protected String input;
	
	public Object getParam(AmpAutoCompleteModelParam p) {
		if(params==null) return null;
		return params.get(p);
	}
	
	/**
	 * This recursive method adds to the sector root tree the given sector. It
	 * does this only if this sector has no parent (it's a root sector).
	 * Otherwise it will add itself to the parent sector tree (this becoming
	 * visible in rendering). It also makes sure the parent, if not null, goes
	 * through the same loop thus adding its way to the root parent. If our
	 * current sector is a search hit (it was discovered by mysql search) it
	 * also adds its children, if any, to the results.
	 * 
	 * @param tree
	 *            the output tree with sector results
	 * @param obj
	 *            the current sector to be added
	 * @param searchHit
	 *            if this current sector is a search hit (if it has been found
	 *            externally by mysql or its through recursive call
	 */
	protected void addToRootTree(Collection tree, AmpComboboxDisplayable obj,
			boolean searchHit) {
		if (obj.getParent() == null)
			tree.add(obj);
		else {
			if (searchHit)
				obj.getVisibleSiblings().addAll(obj.getSiblings());
			obj.getParent().getVisibleSiblings().add(obj);
			addToRootTree(tree, obj.getParent(), false);
		}
	}

	/**
	 * Helper method that translates the results tree into a results list,
	 * displayable by the dropdown. This recursive method adds the current
	 * sector to the list and then all its children
	 * 
	 * @param list
	 *            the output list
	 * @param obj
	 *            the sector to be added to the output list
	 */
	protected void addToRootList(List list, AmpComboboxDisplayable obj) {
		list.add(obj);
		Collection<AmpComboboxDisplayable> children = obj.getVisibleSiblings();
		for (AmpComboboxDisplayable ampSector : children) {
			addToRootList(list, ampSector);
		}
	}

	/**
	 * Creates the tree view for comboboxes
	 * @param l the input list of objects to be displayed in tree-like structure
	 * @return the list of full objects with parents that can be directly diplayed by the combobox
	 */
	protected List createTreeView(List l) {
		List ret=new ArrayList();
		Set<Object> root = new TreeSet();
		for (Object o : l)
			addToRootTree(root, (AmpComboboxDisplayable) o, true);
		for (Object o : root)
			addToRootList(ret, (AmpComboboxDisplayable) o);

		return ret;
	}

	public AbstractAmpAutoCompleteModel(String input,Map <AmpAutoCompleteModelParam,Object> params) {
		super();
		this.input = input;
		this.params=params;
		if(params!=null && getParam(PARAM.MAX_RESULTS)==null) this.params.put(PARAM.MAX_RESULTS, MAX_RESULTS_VALUE);
	}

}
