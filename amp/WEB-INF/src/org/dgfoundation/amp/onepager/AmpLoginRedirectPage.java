package org.dgfoundation.amp.onepager;

import org.apache.wicket.markup.html.pages.RedirectPage;

public class AmpLoginRedirectPage extends RedirectPage{

	public AmpLoginRedirectPage() {
		this(null);
	}
	
	public AmpLoginRedirectPage(CharSequence url) {
		super("/showLayout.do?layout=login");
	}

}
