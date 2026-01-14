package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.model.IModel;
import org.apache.wicket.markup.html.list.ListView;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;

/**
 * Implements the aspect of a feature table. This is actually a table within a
 * form section. It shows a nice table heading with the name of the feature.
 * This acts like a {@link AmpFMTypes#FEATURE}
 *
 * @author tmugo@developmentgateway.org since Apr 25 2023
 * @param <T> the type of the model that holds the collection to be iterated
 * @param <L> the type of the object displayed on each row of the {@link ListView}
 */
public abstract class AmpMEFormTableFeaturePanel<T, L> extends AmpFormTableFeaturePanel<T, L> {
    protected ListEditor<L> list;

    public ListView getList() {
        throw new RuntimeException("NOT ALLOWED!");
    }

    public ListEditor<L> getEditorList() {
        return list;
    }

    public AmpMEFormTableFeaturePanel(String id,final IModel<T> model, String fmName, boolean hideLeadingNewLine) throws Exception {
        super(id, model, fmName, hideLeadingNewLine);
    }
}
