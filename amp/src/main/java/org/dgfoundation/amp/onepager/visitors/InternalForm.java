package org.dgfoundation.amp.onepager.visitors;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmitter;
import org.apache.wicket.markup.html.form.IFormVisitorParticipant;
import org.apache.wicket.model.IModel;

public class InternalForm<T> extends Form<T> implements IFormVisitorParticipant {
    private static final long serialVersionUID = 1L;

    public InternalForm(String name) {
        super(name);
    }

    public InternalForm(String name, IModel<T> model) {
        super(name, model);
    }

    public boolean processChildren() {
        IFormSubmitter submitter = getRootForm().findSubmittingButton();
        if (submitter == null)
            return false;
        
        return submitter.getForm() == this;
    }
}
