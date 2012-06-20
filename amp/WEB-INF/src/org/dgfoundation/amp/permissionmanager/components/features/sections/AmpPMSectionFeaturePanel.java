/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;

/**
 * @author dan
 *
 */
public class AmpPMSectionFeaturePanel extends AmpFeaturePanel implements IHeaderContributor{

	private TransparentWebMarkupContainer sliderPM;
	private boolean jsAdded=false;
	
	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMSectionFeaturePanel(String id, String fmName) throws Exception {
		super(id, fmName);
		// TODO Auto-generated constructor stub
		sliderPM = new TransparentWebMarkupContainer("sliderPM");
		sliderPM.setOutputMarkupId(true);
		add(sliderPM);
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMSectionFeaturePanel(String id, IModel model, String fmName)
			throws Exception {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
		sliderPM = new TransparentWebMarkupContainer("sliderPM");
		sliderPM.setOutputMarkupId(true);
		add(sliderPM);

	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLabel
	 * @throws Exception
	 */
	public AmpPMSectionFeaturePanel(String id, IModel model, String fmName, boolean hideLabel) throws Exception {
		super(id, model, fmName, hideLabel);
		// TODO Auto-generated constructor stub
		sliderPM = new TransparentWebMarkupContainer("sliderPM");
		sliderPM.setOutputMarkupId(true);
		add(sliderPM);

	}

	public TransparentWebMarkupContainer getSliderPM() {
		return sliderPM;
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		if(!jsAdded)
			response.renderOnDomReadyJavaScript(OnePagerUtil.getToggleJS(getSliderPM()));
		jsAdded=true;
	}
	
}
