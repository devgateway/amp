/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;

/**
 * Renderer for {@link MetaInfo} objects. Shows the
 * {@link MetaInfo#getCategory()} as the {@link #getDisplayValue(MetaInfo)}
 * 
 * @see AmpMetaInfoModel
 * @see MetaInfo
 * @author mpostelnicu@dgateway.org since Nov 12, 2010
 */
public class AmpMetaInfoRenderer<T extends Comparable<? super T>> implements
        IChoiceRenderer<MetaInfo<T>> {

    /**
     * 
     */
    public AmpMetaInfoRenderer() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Shows the {@link MetaInfo#getCategory()} as the
     * {@link #getDisplayValue(MetaInfo)}
     * 
     * @param object
     * @return
     */
    @Override
    public Object getDisplayValue(MetaInfo<T> object) {
        return TranslatorUtil.getTranslatedText(object.getCategory());
    }

    /**
     * Uses the {@link MetaInfo#getValue()#toString()} as the
     * {@link #getIdValue(MetaInfo, int)}
     * 
     * @param object
     * @param index
     * @return
     */
    @Override
    public String getIdValue(MetaInfo<T> object, int index) {
        return object.getValue().toString();
    }

}
