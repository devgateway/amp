/**
 * 
 */
package org.dgfoundation.amp.onepager.web.pages;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.PageLink;
import org.dgfoundation.amp.onepager.components.RequiredTextField;

/**
 * @author mihai
 *
 */
public class HelloWorld extends WebPage {

	/**
	 * 
	 */
	public HelloWorld() {
		add(new Label("message", "Hello World!"));
        add(new BookmarkablePageLink<Void>("artyLink", ArtyPage.class));
		RequiredTextField f = new RequiredTextField("reqtext");
		add(f);
	}
	
	

	

}
