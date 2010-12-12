/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.image.Image;

import com.visural.wicket.component.dropdown.DropDownImageResourceRef;

/**
 * @author mpostelnicu@dgateway.org
 * since Oct 21, 2010
 */
public class AmpComboboxFieldPanel<T> extends AmpFieldPanel<T> {

	

	protected final AbstractAmpAutoCompleteTextField<T> autoComplete;

	public AbstractAmpAutoCompleteTextField<T> getAutoComplete() {
		return autoComplete;
	}

	/**
	 * @param id
	 * @param fmName
	 */
	public AmpComboboxFieldPanel(String id, String fmName, final AbstractAmpAutoCompleteTextField<T> autoComplete) {
		super(id, fmName);
		this.autoComplete = autoComplete;
		newLine.setVisible(false);
		add(autoComplete);
		AjaxLink link=new AjaxLink("dropdownLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				autoComplete.setModelObject("");
				autoComplete.getChoices("");
				String js=String.format("$('#%s').trigger('change');",autoComplete.getMarkupId());
				target.appendJavascript(js);
//				target.addComponent(autoComplete);
			}
		};
		link.add(new Image("dropdown",new DropDownImageResourceRef()));
		add(link);
	}

}
