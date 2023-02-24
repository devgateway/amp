/*
 * Copyright (c) 2013 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.model.IModel;

/**
 *
 * @author aartimon@developmentgateway.org
 * @since 12 Mar 2013
 */
public class AmpTableFundingAmountComponent<T> extends AmpFundingAmountComponent<T> {

    public AmpTableFundingAmountComponent(String id, IModel<T> model, String fmAmount, String propertyAmount, String fmCurrency, String propertyCurrency, String fmDate, String propertyDate, boolean isMTEFProjection) {
        super(id, model, fmAmount, propertyAmount, fmCurrency, propertyCurrency, fmDate, propertyDate, isMTEFProjection, true,null);
    }
}
