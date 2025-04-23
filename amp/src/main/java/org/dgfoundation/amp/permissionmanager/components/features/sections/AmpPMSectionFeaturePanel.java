/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerUtil;
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
        this(id,null, fmName);  
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @throws Exception
     */
    public AmpPMSectionFeaturePanel(String id, IModel model, String fmName)
            throws Exception {
        this(id, model, fmName, false);

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
        Image img = new Image("perm_open",  new Model<String>(""));
        img.add(new AttributeModifier("src", "/TEMPLATE/ampTemplate/img_2/ico_perm_open.gif"));
        
        add(img);

    }

    public TransparentWebMarkupContainer getSliderPM() {
        return sliderPM;
    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        if(!jsAdded)
            response.render(OnDomReadyHeaderItem.forScript(OnePagerUtil.getToggleJS(getSliderPM())));
        jsAdded=true;
    }
    
}
