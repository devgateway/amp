/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.util;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier;
import org.apache.wicket.markup.resolver.IComponentResolver;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.TransparentFMWebMarkupContainer;

/**
 * @author aartimon@dginternational.org
 * since Oct 6, 2010
 */
public class FMComponentResolver implements IComponentResolver {

    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Logger.getLogger(FMComponentResolver.class);
    
    public static final String FM_COMPONENT_PREFIX = "fm_";
    public final static MetaDataKey TRANSLATION_KEY = new MetaDataKey(){
        private static final long serialVersionUID = 1L;
    };
    
    
    static {
        // register "wicket:fm"
        WicketTagIdentifier.registerWellKnownTagName("fm");
    }

    /**
     * This method is called by the component resolver when a "wicket:trn" tag is encountered.
     * Method will attach to the markup a TrnLabel component.
     */
    @Override
    public Component resolve(MarkupContainer container,
            MarkupStream markupStream, ComponentTag tag) {

        if (tag.getName().compareTo("fm") == 0) {
            // generate an id for our component
            final String id = FM_COMPONENT_PREFIX + container.getPage().getAutoIndex();
            // get the label's initial value, stored inside the tag
            //String value = markupStream.get(markupStream.getCurrentIndex() + 1).toString();
            TransparentWebMarkupContainer twmc = new TransparentWebMarkupContainer(id); 

            String jsFriendly = tag.getAttributes().getString("jsFriendly");
            if ((jsFriendly != null) && (jsFriendly.compareTo("true") == 0)) {
                twmc.setRenderBodyOnly(true);
            }
            
            String fmNames = tag.getAttributes().getString("fmNames");
            if (fmNames != null){
                twmc = new TransparentFMWebMarkupContainer(id, new Model<String>(fmNames));
            }
            
            //commented due to new resolve api
            //container.autoAdd(twmc, markupStream);
            
            // Yes, we handled the tag
            return twmc;
        }
        
        // We were not able to handle the tag
        return null;
    }
}
