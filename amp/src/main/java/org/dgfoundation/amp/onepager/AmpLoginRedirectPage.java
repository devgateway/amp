/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager;

import org.apache.wicket.markup.html.pages.RedirectPage;

/**
 * @author aartimon@dginternational.org 
 * @since Jan 3, 2011
 */
public class AmpLoginRedirectPage extends RedirectPage{

    public AmpLoginRedirectPage() {
        this(null);
    }
    
    public AmpLoginRedirectPage(CharSequence url) {
        super("/aim");
    }

}
