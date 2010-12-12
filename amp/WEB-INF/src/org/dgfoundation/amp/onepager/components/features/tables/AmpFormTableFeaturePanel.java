/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.Collection;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;

/**
 * Implements the aspect of a feature table. This is actually a table within a
 * form section. It shows a nice table heading with the name of the feature.
 * This acts like a {@link AmpFMTypes#FEATURE}
 * 
 * @author mpostelnicu@dgateway.org since Oct 20, 2010
 * @param <T> the type of the model that holds the collection to be iterated
 * @param <L> the type of the object displayed on each row of the {@link ListView}
 */
public abstract class AmpFormTableFeaturePanel<T,L> extends AmpFeaturePanel<T> {

	protected final IModel<T> model;
	protected WebMarkupContainer titleHeader;
	protected WebMarkupContainer tableHeading;
	
	protected ListView<L> list;

	public ListView<L> getList() {
		return list;
	}
	
	/**
	 * this is incomplete, do not use it just yet
	 * @param target
	 * @param source
	 * @deprecated
	 */
	public void toggleHeading(AjaxRequestTarget target,Collection<L> source) {
		boolean prevVisible=tableHeading.isVisible();
		boolean currVisible=(source.size()!=0);
		tableHeading.setVisible(currVisible);
		if(prevVisible!=currVisible) target.addComponent(tableHeading);
	}

	public WebMarkupContainer getTitleHeader() {
		return titleHeader;
	}

	public WebMarkupContainer getTableHeading() {
		return tableHeading;
	}

	/**
	 * Sets the title header rowspan based on how many columns you want to show in this table
	 * The default is set in the markup to 3
	 * @param rowspan
	 */
	public void setTitleHeaderColSpan(final Integer colspan) {
		titleHeader.add(new AttributeModifier("colspan", new Model<Integer>(colspan)));	
	}

	/**
	 * 
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpFormTableFeaturePanel(String id, final IModel<T> model,
			String fmName) throws Exception {
		this(id, model, fmName, false);
	}

	/**
	 * 
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLeadingNewLine
	 * @throws Exception
	 */
	public AmpFormTableFeaturePanel(String id, final IModel<T> model,
			String fmName, boolean hideLeadingNewLine) throws Exception {
		super(id, model, fmName);
		this.model = model;
		WebMarkupContainer leadingNewLine = new WebMarkupContainer("leadingNewLine");
		leadingNewLine.setVisible(!hideLeadingNewLine);
		add(leadingNewLine);
		tableHeading = new WebMarkupContainer("tableHeading");
		tableHeading.setOutputMarkupId(true);
		add(tableHeading);
		titleHeader = new WebMarkupContainer("titleHeader");
		remove(labelContainer);
		titleHeader.add(labelContainer);
		add(titleHeader);
	}

}
