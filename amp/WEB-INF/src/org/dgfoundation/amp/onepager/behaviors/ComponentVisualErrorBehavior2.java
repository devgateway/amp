package org.dgfoundation.amp.onepager.behaviors;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxRegionMarkupIdProvider;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessages;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.string.Strings;
import org.digijava.kernel.translator.TranslatorWorker;

public class ComponentVisualErrorBehavior2 extends Behavior implements IAjaxRegionMarkupIdProvider {
	private static final Logger logger = Logger.getLogger(ComponentVisualErrorBehavior2.class);
	public static final String INVALID_CLASS="formcomponent invalid";
	private static final long serialVersionUID = 1L;

    @Override
    public void onConfigure(Component component) {
        super.onConfigure(component);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
	public void onComponentTag(Component component, ComponentTag tag) {
		if (!((FormComponent<?>) component).isValid() && !isCustomErrorBehavior(component, tag)) {
			String c1 = tag.getAttribute("class");
			if (c1 == null) {
                tag.put("class", INVALID_CLASS);
            } else {
                tag.put("class", INVALID_CLASS + " " + c1);
            }
		}
	}

    /**
     * This is the method that defines if the custom visual handling is needed based on the component and it's tag
     * @param component
     * @param tag
     * @return true if custom handling behavior is needed
     */
    private boolean isCustomErrorBehavior(Component component, ComponentTag tag) {
        /**
         * We can not only return false here, but also
         * do component.getParent().getTextLabel() and then set the error red border for the textLabel if needed
         */
        if (RadioChoice.class.getName().equals(component.getClass().getName())) {
            return true;
        }
        return false;
    }
	
	@Override
	public void beforeRender(Component component) {
		Response r = component.getResponse();
		r.write("<span id=\"");
		r.write(getAjaxRegionMarkupId(component));
		r.write("\">");
	}
	
	@Override
	public void afterRender(Component component) {
		FeedbackMessages messages = component.getFeedbackMessages();
		Response r = component.getResponse();
		if (component.hasFeedbackMessage()){
			r.write("<ul class=\"feedbackPanel\">");
			for (FeedbackMessage message: messages){
				r.write("<li class=\"feedbackPanel");
				r.write(message.getLevelAsString().toUpperCase());
				r.write("\">");
				r.write(TranslatorWorker.translateText(Strings.escapeMarkup(message.getMessage().toString()).toString()));
				r.write("</li>");
				logger.error("Rendered error: " + message.getMessage().toString());
				message.markRendered();
			}
            r.write("</ul>");
		}
		r.write("</span>");
	}

	@Override
	public String getAjaxRegionMarkupId(Component component) {
		return component.getMarkupId() + "_fd";
	}	
}
