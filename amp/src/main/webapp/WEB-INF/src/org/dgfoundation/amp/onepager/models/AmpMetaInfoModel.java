/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.ar.MetaInfo;

/**
 * Wrapper model for {@link MetaInfo} reading and writing the
 * {@link MetaInfo#getValue()}
 * 
 * @author mpostelnicu@dgateway.org since Nov 12, 2010
 */
public class AmpMetaInfoModel<T extends Comparable<? super T>> implements
        IModel<MetaInfo<T>> {

    /**
     * 
     */
    private static final long serialVersionUID = 9142316072150840972L;
    private IModel<T> model;
    private MetaInfo<T>[] array;

    /**
     * Constructs a new model object taking as input the target model and an
     * Array of {@link MetaInfo} objects This array is used by the
     * {@link #getObject()} method, to find and return the correct
     * {@link MetaInfo} object based on the identifier read from model
     * 
     * @param model
     * @param array
     */
    public AmpMetaInfoModel(IModel<T> model, MetaInfo<T>[] array) {
        this.model = model;
        this.array = array;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void detach() {
        model.detach();
    }

    @Override
    public MetaInfo<T> getObject() {
        T object = model.getObject();
        if(object==null) return null;
        for (int i = 0; i < array.length; i++)
            if (array[i].getValue().compareTo(object) == 0)
                return array[i];
        throw new RuntimeException("Cannot find " + MetaInfo.class.getName()
                + " for object " + object.toString());
    }

    @Override
    public void setObject(MetaInfo<T> object) {
        model.setObject(object.getValue());
    }

}
