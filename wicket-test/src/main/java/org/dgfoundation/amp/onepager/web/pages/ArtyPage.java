/**
 * 
 */
package org.dgfoundation.amp.onepager.web.pages;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

/**
 * @author mihai
 *
 */
public class ArtyPage extends WebPage {

	/**
	 * 
	 */
	public ArtyPage() {
		add(new Label("message", "Hello World!"));
		WebMarkupContainer hideContainer = new WebMarkupContainer("hideContainer");
		Label l = new AmpLabel("testHide", "Hide Me!");
		Label l2 = new AmpLabel("testHide2", "Hide Me2!");
		
		hideContainer.add(l);
		hideContainer.add(l2);
		
		add(hideContainer);
	}

	

}
