/**
 * 
 */
package org.dgfoundation.amp.onepager.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * @author mihai
 * Shared methods beteween {@link ChoiceComponentVisualErrorBehavior} and {@link ComponentVisualErrorBehavior}
 * These two types of ajax validation behaviors have common methods but a different ancestor
 */
public interface AmpComponentVisualErrorInterface {
	public static final String INVALID_CLASS="formcomponent invalid";
	
	public String getPreviousClass();
	public void setPreviousClass(String s);
	public Component getUpdateComponent();
	public FormComponent<?> getRelatedFormComponent();
}
