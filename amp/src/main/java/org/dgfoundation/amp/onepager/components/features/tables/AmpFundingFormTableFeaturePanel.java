/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;



import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.ListEditor;
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
public abstract class AmpFundingFormTableFeaturePanel<T,L> extends AmpFormTableFeaturePanel<T,L> {
    protected ListEditor<L> list;
    @Override
    public ListView getList() {
        throw new RuntimeException("NOT ALLOWED!");
    }
    
    public ListEditor<L> getEditorList(){
        return list;
    }
    
    public AmpFundingFormTableFeaturePanel(String id, final IModel<T> model,
            String fmName) throws Exception {
        super(id, model, fmName);
    }
    public AmpFundingFormTableFeaturePanel(String id, final IModel<T> model, String fmName, boolean hideLeadingNewLine)  {
        super(id, model, fmName, hideLeadingNewLine);
    }
    public AmpFundingFormTableFeaturePanel(String id, final IModel<T> model, String fmName, boolean hideLeadingNewLine, boolean showRequiredStar){
        super(id, model, fmName, hideLeadingNewLine, showRequiredStar);
    }
}
