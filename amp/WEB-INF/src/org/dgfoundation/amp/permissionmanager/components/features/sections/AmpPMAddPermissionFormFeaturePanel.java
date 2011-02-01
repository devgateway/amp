/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMAddPermFormTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.gateperm.core.CompositePermission;

/**
 * @author dan
 *
 */
public class AmpPMAddPermissionFormFeaturePanel extends AmpComponentPanel {

	/**
	 * @param id
	 * @param fmName
	 * @param fmType
	 */
	public AmpPMAddPermissionFormFeaturePanel(String id, String fmName, AmpFMTypes fmType) {
		super(id, fmName, fmType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param fmName
	 */
	public AmpPMAddPermissionFormFeaturePanel(String id, String fmName) {
		super(id, fmName);
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param fmBehavior
	 */
	public AmpPMAddPermissionFormFeaturePanel(String id, IModel model,	String fmName, AmpFMTypes fmBehavior) {
		super(id, model, fmName, fmBehavior);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception 
	 */
	public AmpPMAddPermissionFormFeaturePanel(String id, final IModel<CompositePermission> cpModel, String fmName, String permissionActionTitle, boolean isEdit) {
		super(id, cpModel, fmName);
		
		final Form addPermForm = new Form ("addPermissionForm")
		{
			protected void onSubmit() {
				System.out.println("addPermissionForm submitted");
			}
		};
		addPermForm.setOutputMarkupId(true);
		
		addPermForm.add(new Label("permissionActionTitle", permissionActionTitle));
		
		AmpTextFieldPanel permName = new AmpTextFieldPanel("permissionName", new PropertyModel(cpModel,"name"), "Permission Name", false, true);
		addPermForm.add(permName);
	
		final AmpPMAddPermFormTableFeaturePanel permGatesFormTable = new AmpPMAddPermFormTableFeaturePanel("permissionFormTable", cpModel, "Permission Form Table", false);
		permGatesFormTable.setTableWidth(300);
		addPermForm.add(permGatesFormTable);
		
		addPermForm.add(new AjaxFallbackLink("resetPermissionButton"){
			//@Override
			public void onClick(AjaxRequestTarget target) {
				cpModel.setObject(null);
				addPermForm.clearInput();
				target.addComponent(AmpPMAddPermissionFormFeaturePanel.this);
			}
		});
		
		Button saveAndSubmit = new Button("savePermissionButton") {

			//AjaxRequestTarget target, Form<?> form
			public void onSubmit() {
					// TODO Auto-generated method stub
					System.out.println("savePermissionButton  submit pressed");
					try {
						PMUtil.savePermission(cpModel,permGatesFormTable.getGatesSet());
					} catch (DgException e) {
						e.printStackTrace();
					}
					cpModel.setObject(null);
					addPermForm.clearInput();
					//target.addComponent(AmpPMAddPermissionFormFeaturePanel.this);
					System.out.println("PM new permission created");
			}


		};
		addPermForm.add(saveAndSubmit);
		
		//adding the form
		add(addPermForm);
	}

}
