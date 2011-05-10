/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * @author mpostelnicu@dgateway.org since Nov 5, 2010
 */
public final class OnePagerUtil {

	/**
	 * Returns an {@link AbstractReadOnlyModel} wrapping a list from a source SetModel.
	 * Useful to return a {@link ListView} compliant model out of a Hibernate Set 
	 * @param <T> The type of object in the set
	 * @param setModel the given set model
	 * @return the returned model
	 */
	public final static <T> AbstractReadOnlyModel<List<T>> getReadOnlyListModelFromSetModel(final IModel<Set<T>> setModel) {
		return new AbstractReadOnlyModel<List<T>>() {
			private static final long serialVersionUID = 3706184421459839210L;

			@Override
			public List<T> getObject() {
				return new ArrayList<T>(setModel.getObject());
			}
		};

	}

	/**
	 * Returns an {@link AbstractReadOnlyModel} wrapping a list from a source ArrayList.
	 * Useful to return a {@link ListView} compliant model out of a Hibernate Set 
	 * @param <T> The type of object in the set
	 * @param list
	 * @return the returned model
	 */
	public final static <T> AbstractReadOnlyModel<List<T>> getReadOnlyListModelFromArray(final ArrayList<T> list) {
		return new AbstractReadOnlyModel<List<T>>() {
			private static final long serialVersionUID = 3706184421459839210L;

			@Override
			public List<T> getObject() {
				return list;
			}
		};

	}
}
