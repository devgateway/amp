/**
 * 
 */
package org.dgfoundation.amp.onepager.web.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
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
		RequiredTextField f = new RequiredTextField("reqtext");
		add(f);
	}
	
	

	

}
