/**
 * 
 */
package org.dgfoundation.amp.onepager.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * @author mihai
 *
 */
public interface AmpComponentVisualErrorInterface {
	public static final String INVALID_CLASS="formcomponent invalid";
	
	public String getPreviousClass();
	public void setPreviousClass(String s);
	public Component getUpdateComponent();
	public FormComponent<?> getRelatedFormComponent();
}
