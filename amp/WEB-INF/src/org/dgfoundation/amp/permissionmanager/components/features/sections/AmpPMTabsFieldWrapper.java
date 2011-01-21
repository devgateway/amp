/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.fields.AmpFieldPanel;

/**
 * @author dan
 *
 */
public class AmpPMTabsFieldWrapper extends AmpFieldPanel {

	/**
	 * @param id
	 * @param fmName
	 * @param hideLabel
	 */
	public AmpPMTabsFieldWrapper(String id, String fmName, boolean hideLabel) {
		super(id, fmName, hideLabel);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param fmName
	 * @param model
	 */
	public AmpPMTabsFieldWrapper(String id, String fmName, IModel model) {
		super(id, fmName, model);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLabel
	 */
	public AmpPMTabsFieldWrapper(String id, IModel model, String fmName,
			boolean hideLabel) {
		super(id, model, fmName, hideLabel);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param fmName
	 */
	public AmpPMTabsFieldWrapper(String id, String fmName) {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 */
	public AmpPMTabsFieldWrapper(String id, IModel model, String fmName) {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
	}
	
	public AmpPMTabsFieldWrapper(String id, String fmName, List<ITab> tabs, boolean hideLabel) {
		super(id, fmName,hideLabel);
		AjaxTabbedPanel atp = new AjaxTabbedPanel("tabs", tabs);
		atp.setOutputMarkupId(true);
		add(atp);
	}

}
