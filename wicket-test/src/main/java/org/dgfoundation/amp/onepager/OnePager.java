/**
 * 
 */
package org.dgfoundation.amp.onepager;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.dgfoundation.amp.onepager.web.pages.HelloWorld;

/**
 * @author mihai
 *
 */
public class OnePager extends WebApplication {

	/* (non-Javadoc)
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		// TODO Auto-generated method stub
		return HelloWorld.class;
	}
	
	 public OnePager() {
	    }



}
