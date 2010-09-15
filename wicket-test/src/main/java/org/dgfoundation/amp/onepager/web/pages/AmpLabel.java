package org.dgfoundation.amp.onepager.web.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class AmpLabel extends Label {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private void fm(String id){
		if (id.compareTo("testHide") == 0)
			setVisible(false);
	}

	public AmpLabel(String id, IModel<?> model) {
		super(id, model);
		fm(id);
	}

	public AmpLabel(String id, String label) {
		super(id, label);
		fm(id);
	}
	
	public AmpLabel(String id) {
		super(id);
		fm(id);
	}

	
	
}
