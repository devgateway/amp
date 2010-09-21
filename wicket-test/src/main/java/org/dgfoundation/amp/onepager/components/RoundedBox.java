package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.border.Border;

public class RoundedBox extends Border
{
    public RoundedBox(String id)
    {
        super(id);
     

       // add(getCssContributor());
    }
   
    /** Subclasses can override */
    protected HeaderContributor getCssContributor()
    {
        return CSSPackageResource.getHeaderContribution(RoundedBox.class, "RoundedBox.css");
    }
}