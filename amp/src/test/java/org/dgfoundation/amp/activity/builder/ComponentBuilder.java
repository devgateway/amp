package org.dgfoundation.amp.activity.builder;

import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;

/**
 * @author Octavian Ciubotaru
 */
public class ComponentBuilder {

    private ActivityBuilder activityBuilder;

    AmpComponent component;

    ComponentBuilder(ActivityBuilder activityBuilder, AmpComponent component) {
        this.activityBuilder = activityBuilder;
        this.component = component;
    }

    public ComponentFundingBuilder buildFunding(int transactionType) {
        AmpComponentFunding funding = new AmpComponentFunding();
        funding.setComponent(component);
        funding.setTransactionType(transactionType);
        return new ComponentFundingBuilder(this, funding);
    }

    public ActivityBuilder addComponent() {
        activityBuilder.activity.getComponents().add(component);
        return activityBuilder;
    }

    public ComponentBuilder withTitle(String title) {
        component.setTitle(title);
        return this;
    }
}
