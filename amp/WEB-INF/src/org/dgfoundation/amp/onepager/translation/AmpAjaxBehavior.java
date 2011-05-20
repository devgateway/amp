/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.translation;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author aartimon@dginternational.org
 * since Oct 7, 2010
 */
public class AmpAjaxBehavior extends AbstractDefaultAjaxBehavior{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The respond message called, by the translator component, through AJAX. 
	 */
	@Override
	protected void respond(AjaxRequestTarget target) {
		String pEditorKey = RequestCycle.get().getRequest().getParameter("editorKey");
		String pEditorVal = RequestCycle.get().getRequest().getParameter("editorVal");
		String pLabelId = RequestCycle.get().getRequest().getParameter("labelId");
		  
		target.appendJavascript("updateLabel(\""+pLabelId+"\", \""+pEditorVal+"\");showLabel(\""+pLabelId+"\");window.status='';");
	}
	
}
