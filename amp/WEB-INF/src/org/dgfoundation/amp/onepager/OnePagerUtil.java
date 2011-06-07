/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.FMUtil;

/**
 * @author mpostelnicu@dgateway.org since Nov 5, 2010
 */
public final class OnePagerUtil {

	protected static Logger logger = Logger.getLogger(OnePagerUtil.class);
	
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
	
	
	/**
	 * Cascade the {@link #switchFmVisible(AjaxRequestTarget)} to all children of this {@link AmpComponentPanel}
	 * @param target
	 */
	public static void cascadeFmVisible(AjaxRequestTarget target, final boolean visible, Component c) {
		if (c instanceof MarkupContainer){
			//logger.info("Attempting cascadeFmVisible on "+c.getId());
			MarkupContainer m = (MarkupContainer) c;
			for (int i = 0; i < m.size(); i++) {
				Component component = m.get(i);
				if(component instanceof AmpComponentPanel) {
					FMUtil.changeFmVisible(component, visible);
				}
				cascadeFmVisible(target,visible, component);
			}
		}
	}

	/**
	 * Cascade the {@link #switchFmEnabled(AjaxRequestTarget)} to all children of this {@link AmpComponentPanel}
	 * @param target
	 */
	public static void cascadeFmEnabled(AjaxRequestTarget target, boolean enabled, Component c) {
		if (c instanceof MarkupContainer){
			MarkupContainer m = (MarkupContainer) c;
			for (int i = 0; i < m.size(); i++) {
				Component component = m.get(i);
				if(component instanceof AmpComponentPanel) {
					FMUtil.changeFmEnabled(component, enabled);
				} else {
					
				}
				cascadeFmEnabled(target, enabled, component);
			}
		}
	}
}
