package org.dgfoundation.amp.onepager.util;

import java.util.Iterator;
import java.util.Set;


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField;



public abstract class AmpDividePercentageField<T> extends AmpAjaxLinkField {
	
	private static final long serialVersionUID = 1L;
	
	private IModel<Set<T>> setModel;
	private ListView<T> list;
	
	public AmpDividePercentageField(String id, String fmName,
			String buttonCaption, IModel<Set<T>> setModel, ListView<T> list) {
		super(id, fmName, buttonCaption);
		this.setModel = setModel;
		this.list = list;
	}

	@Override
	protected void onClick(final AjaxRequestTarget target) {
		Set<T> set = setModel.getObject();
		if (set.size() == 0)
			return;
		
		int size = 0;
		Iterator<T> it = set.iterator();
		while (it.hasNext()) {
			T t = (T) it.next();
			if (itemInCollection(t))
				size++;
		}
		
		if (size == 0)
			return;
		
		int alloc = 100/size;
		it = set.iterator();
		while (it.hasNext()) {
			T loc = (T) it.next();
			if (!itemInCollection(loc))
				continue;
			setPercentage(loc, alloc);
		}
		
		int dif = 100 - alloc*size;
		int delta = 1;
		if (dif < 0)
			delta = -1;
		it = set.iterator();
		while (dif != 0 && it.hasNext()){
			T loc = (T) it.next();
			if (!itemInCollection(loc))
				continue;
			setPercentage(loc, getPercentage(loc) + delta);
			dif = dif - delta;
		}
		list.removeAll();
		list.getParent().visitChildren(AmpCollectionValidatorField.class, new IVisitor<AmpCollectionValidatorField>() {
			@Override
			public Object component(AmpCollectionValidatorField component) {
				component.reloadValidationField(target);
				return CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
			}
		});
		target.add(list.getParent());
	}
	
	public abstract void setPercentage(T item, int val);
	
	public abstract int getPercentage(T item);
	
	/**
	 * For items that share the same collection contained by "setModel", but are displayed in different lists
	 * return true if the current item is in the right collection
	 * @param item
	 * @return
	 */
	public abstract boolean itemInCollection(T item);
}
