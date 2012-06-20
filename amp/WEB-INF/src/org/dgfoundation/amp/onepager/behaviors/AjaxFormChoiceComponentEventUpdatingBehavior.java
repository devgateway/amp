/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dgfoundation.amp.onepager.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;

/**
 * This is very similar with {@link AjaxFormChoiceComponentUpdatingBehavior} but it has an {@link AjaxEventBehavior} as its superclass.
 * This means it can receive events. This is used by AMP validation. The original Wicket event for {@link RadioChoice}S does not support events.
 * We simulate onclick using jquery, during form submission 
 * 
 * @author jcompagner
 * @author mihai
 * 
 * @see AmpButtonField 
 * @see AjaxFormChoiceComponentUpdatingBehavior
 * @see RadioChoice
 * 
 */
public abstract class AjaxFormChoiceComponentEventUpdatingBehavior extends AjaxEventBehavior
{
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param event
	 *            event to trigger this behavior
	 */
	public AjaxFormChoiceComponentEventUpdatingBehavior(final String event)
	{
		super(event);
	}

	/**
	 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
	 */
	@Override
	public void renderHead(Component component, IHeaderResponse response)
	{
		super.renderHead(component, response);

		AppendingStringBuffer asb = new AppendingStringBuffer();
		asb.append("function attachChoiceHandlers(markupId, callbackScript) {\n");
		asb.append(" var inputNodes = wicketGet(markupId).getElementsByTagName('input');\n");
		asb.append(" for (var i = 0 ; i < inputNodes.length ; i ++) {\n");
		asb.append(" var inputNode = inputNodes[i];\n");
		asb.append(" if (!inputNode.type) continue;\n");
		asb.append(" if (!(inputNode.className.indexOf('wicket-'+markupId)>=0)&&!(inputNode.id.indexOf(markupId+'-')>=0)) continue;\n");
		asb.append(" var inputType = inputNode.type.toLowerCase();\n");
		asb.append(" if (inputType == 'checkbox' || inputType == 'radio') {\n");
		asb.append(" Wicket.Event.add(inputNode, 'click', callbackScript);\n");
		asb.append(" }\n");
		asb.append(" }\n");
		asb.append("}\n");

		response.renderJavaScript(asb, "attachChoice");

		response.renderOnLoadJavaScript("attachChoiceHandlers('" + getComponent().getMarkupId() +
			"', function() {" + getEventHandler() + "});");

	}

	/**
	 * Listener invoked on the ajax request. This listener is invoked after the component's model
	 * has been updated.
	 * 
	 * @param target
	 */
	protected abstract void onUpdate(AjaxRequestTarget target);

	/**
	 * Called to handle any error resulting from updating form component. Errors thrown from
	 * {@link #onUpdate(AjaxRequestTarget)} will not be caught here.
	 * 
	 * The RuntimeException will be null if it was just a validation or conversion error of the
	 * FormComponent
	 * 
	 * @param target
	 * @param e
	 */
	protected void onError(AjaxRequestTarget target, RuntimeException e)
	{
		if (e != null)
		{
			throw e;
		}
	}

	/**
	 * 
	 * @see org.apache.wicket.behavior.AbstractAjaxBehavior#onBind()
	 */
	@Override
	protected void onBind()
	{
		super.onBind();

		if (!AjaxFormChoiceComponentEventUpdatingBehavior.appliesTo(getComponent()))
		{
			throw new WicketRuntimeException("Behavior " + getClass().getName() +
				" can only be added to an instance of a RadioChoice/CheckboxChoice/RadioGroup/CheckGroup");
		}

		if (getComponent() instanceof RadioGroup || getComponent() instanceof CheckGroup)
		{
			getComponent().setRenderBodyOnly(false);
		}
	}

	/**
	 * 
	 * @return FormComponent
	 */
	protected final FormComponent<?> getFormComponent()
	{
		return (FormComponent<?>)getComponent();
	}

	/**
	 * @return event handler
	 */
	protected final CharSequence getEventHandler()
	{
		return generateCallbackScript(new AppendingStringBuffer("wicketAjaxPost('").append(
			getCallbackUrl()).append(
			"', wicketSerializeForm(document.getElementById('" + getComponent().getMarkupId() +
				"',false))"));
	}

	/**
	 * 
	 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected final void onEvent(final AjaxRequestTarget target)
	{
		final FormComponent<?> formComponent = getFormComponent();

		try
		{
			formComponent.inputChanged();
			formComponent.validate();
			if (formComponent.hasErrorMessage())
			{
				formComponent.invalid();

				onError(target, null);
			}
			else
			{
				formComponent.valid();
				formComponent.updateModel();
				onUpdate(target);
			}
		}
		catch (RuntimeException e)
		{
			onError(target, e);

		}
	}

	/**
	 * @param component
	 * @return if the component applies to the {@link AjaxFormChoiceComponentUpdatingBehavior}
	 */
	static boolean appliesTo(Component component)
	{
		return (component instanceof RadioChoice) ||
			(component instanceof CheckBoxMultipleChoice) || (component instanceof RadioGroup) ||
			(component instanceof CheckGroup);
	}
}
