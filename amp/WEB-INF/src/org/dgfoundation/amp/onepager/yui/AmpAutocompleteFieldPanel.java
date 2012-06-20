/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.yui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.resource.TextTemplateResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.fields.AmpFieldPanel;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.models.AmpAutoCompleteModelParam;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.util.DbUtil;

/**
 * Autocomplete Combobox Component based on YUI 2.8.x (or upper). This component
 * can be used to show an autocomplete editbox {@linkplain http
 * ://developer.yahoo.com/yui/examples/autocomplete/ac_combobox.html}
 * 
 * @author mpostelnicu@dgateway.org
 * @since Jun 14, 2011
 * @see {@linkplain http ://ptrthomas.wordpress.com/2009/08/12/wicket
 *      -tutorial-yui-autocomplete-using-json-and-ajax/}
 */
public abstract class AmpAutocompleteFieldPanel<CHOICE> extends
		AmpFieldPanel<String> implements IAjaxIndicatorAware {

	private static final long serialVersionUID = 1L;
	public static final String ACRONYM_DELIMITER_START = "(";
	private static final String ACRONYM_DELIMITER_STOP_PART = ")";
	public static final String ACRONYM_DELIMITER_STOP = ACRONYM_DELIMITER_STOP_PART + " - ";
	public static final String BOLD_DELIMITER_START = "<b>";
	public static final String BOLD_DELIMITER_STOP = "</b> ";
	
	private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();

	/**
	 * The model to retrieve the list of options
	 */
	protected Class<? extends AbstractAmpAutoCompleteModel<CHOICE>> objectListModelClass;

	/**
	 * The text field of the autocomplete
	 */
	private TextField<String> textField;

	public TextField<String> getTextField() {
		return textField;
	}

	public void setTextField(TextField<String> textField) {
		this.textField = textField;
	}

	/**
	 * The container holding the list div
	 */
	private WebMarkupContainer container;

	/**
	 * The button that shows all options
	 */
	private WebMarkupContainer toggleButton;

	/**
	 * Message indicator - loading panel or
	 */
	private WebMarkupContainer indicator;
	
	/**
	 * If YUI client side datasource cache should be used (some instances of this control may require no cache)
	 */
	private boolean useCache=true;
	

	
	

	/**
	 * Behavior that is invoked when an item is selected in YUI list. The
	 * {@link #onBeforeRender()} method is overiden and adds a JS function
	 * wrapping the {@link AbstractAmpAutoCompleteModel} callbackUrl
	 */
	private AbstractDefaultAjaxBehavior onSelectBehavior;

	/**
	 * The list model parameters, if any
	 */
	protected Map<AmpAutoCompleteModelParam, Object> modelParams;

	public Map<AmpAutoCompleteModelParam, Object> getModelParams() {
		return modelParams;
	}

	/**
	 * Initializes the {@link #onSelectBehavior} callBackUrl Adds the
	 * wicketAjaxGet function that can call the Wicket behavior from JavaScript
	 */
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		final CharSequence url = onSelectBehavior.getCallbackUrl();
		textField.add(new AbstractBehavior() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				String js = "function callWicket"
						+ textField.getMarkupId()
						+ "(selectedString) { var wcall = wicketAjaxGet ('"
						+ url
						+ "&selectedString='+selectedString, function() { }, function() { } ) }";
				response.renderJavaScript(js,
						"callWicket-" + textField.getMarkupId());
			}
		});

	}

	/**
	 * @see org.apache.wicket.ajax.IAjaxIndicatorAware#getAjaxIndicatorMarkupId()
	 */
	@Override
	public String getAjaxIndicatorMarkupId() {
		return indicatorAppender.getMarkupId();
	}

	/**
	 * Instantiates a new autocomplete component
	 * 
	 * @param id
	 *            the Wicket id
	 * @param fmName
	 *            the FM name @see {@link AmpFMTypes}
	 * @param objectListModelClass
	 *            the model to retrieve the list of items
	 */
	public AmpAutocompleteFieldPanel(
			String id,
			String fmName,
			Class<? extends AbstractAmpAutoCompleteModel<CHOICE>> objectListModelClass) {
		this(id, fmName, false, objectListModelClass);
	}
	
	/**
	 * 
	 * @param id
	 * @param fmName
	 * @param hideLabel  the FM name @see {@link AmpFMTypes}
	 * @param objectListModelClass the model to retrieve the list of items
	 * @param useCache if YUI should use client side cache
	 */
	public AmpAutocompleteFieldPanel(
			String id,
			String fmName,
			boolean hideLabel,
			Class<? extends AbstractAmpAutoCompleteModel<CHOICE>> objectListModelClass, boolean useCache) {
		this(id,fmName,hideLabel,objectListModelClass);
		this.useCache=useCache;
	}
	
	


	/**
	 * 
	 * @param id
	 * @param fmName  the FM name @see {@link AmpFMTypes}
	 * @param objectListModelClass the model to retrieve the list of items
	 * @param useCache if YUI should use client side cache
	 */
	public AmpAutocompleteFieldPanel(
			String id,
			String fmName,
			Class<? extends AbstractAmpAutoCompleteModel<CHOICE>> objectListModelClass, boolean useCache) {
		this(id, fmName, false, objectListModelClass,useCache);
	}
	
	public AmpAutocompleteFieldPanel(
			String id,
			String fmName,
			boolean hideLabel,
			Class<? extends AbstractAmpAutoCompleteModel<CHOICE>> objectListModelClass,
			final Class<? extends AmpAutocompleteFieldPanel> clazz, final String jsName,final String autoCompeleteVar) {
		super(id, null, fmName, hideLabel, true);
		this.modelParams = new HashMap<AmpAutoCompleteModelParam, Object>();
		this.objectListModelClass = objectListModelClass;
		toggleButton = new WebMarkupContainer("toggleButton");
		toggleButton.setOutputMarkupId(true);
		add(toggleButton);
		textField = new TextField<String>("text", new Model<String>());
		textField.setOutputMarkupId(true);
		add(textField);
		container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);    
		add(new YuiAutoCompleteBehavior(){
			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);
				response.renderJavaScriptReference(new PackageResourceReference(
						AmpAutocompleteFieldPanel.class,
						"AmpAutocompleteCommonScripts.js"));
				
				IModel variablesModel = new AbstractReadOnlyModel() {
					public Map getObject() {
						Map<String, CharSequence> variables = new HashMap<String, CharSequence>(
								2);
						variables.put("noResults", TranslatorUtil.getTranslation("No results found"));
						return variables;
					}
				};
				response.renderJavaScriptReference(new TextTemplateResourceReference(clazz, jsName, variablesModel));
				/*
				 * currently renderOnDomReadyJavaScript doesn't work as expected in IE8
				 * that is why jquery's $(document).ready has been added here
				 */
				
				String disableControl = "true";
				if (textField.getParent().isEnabled())
					disableControl = "false";
				response.renderOnDomReadyJavaScript("$(document).ready(function() {"+getJsVarName()
						+ " = new YAHOO.widget."+ autoCompeleteVar+"('"
						+ textField.getMarkupId() + "', '" + getCallbackUrl()
						+ "', '" + container.getMarkupId() + "', '"
						+ toggleButton.getMarkupId() + "', '"
						+ indicator.getMarkupId() + "', " +useCache+ ", " + disableControl + ");" 
						+ "});");
			}
		});

		indicator = new WebMarkupContainer("indicator");
		indicator.setOutputMarkupId(true);
		add(indicator);

		onSelectBehavior = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void respond(final AjaxRequestTarget target) {
				String selectedString = getRequestCycle().getRequest().getRequestParameters().getParameterValue("selectedString").toString();
				// hide loading icon:
				target.appendJavaScript("YAHOO.util.Dom.get('"
						+ indicator.getMarkupId() + "').style.display = 'none';");
				CHOICE choice = getSelectedChoice(DbUtil.deFilter(selectedString, false));
				onSelect(target, choice);
			}
		};
		textField.add(onSelectBehavior);
	}
	
	/**
	 * Constructs a new component. Initializes all subcomponents
	 * 
	 * @see YuiAutoComplete#YuiAutoComplete(String, String, Class)
	 * @param id
	 * @param hideLabel
	 *            if true, the visible text label of the component is not shown
	 * @param model
	 */
	public AmpAutocompleteFieldPanel(
			String id,
			String fmName,
			boolean hideLabel,
			Class<? extends AbstractAmpAutoCompleteModel<CHOICE>> objectListModelClass) {
		this(id, fmName, hideLabel, objectListModelClass, AmpAutocompleteFieldPanel.class, "AmpAutocompleteFieldPanel.js","WicketAutoComplete");
	}

	/**
	 * Gets the string value from the specified choice
	 * 
	 * @param choice
	 *            the choice that needs value extraction
	 * @return the unique string value of the choice
	 */
	protected abstract String getChoiceValue(final CHOICE choice);

	/**
	 * @param choice
	 *            the current choice to be rendered
	 * @return the level in the tree of the current choice, or null if no levels
	 *         should be drawn
	 */
	public abstract Integer getChoiceLevel(CHOICE choice);

	/**
	 * Invoked when the choice is picked from the autocomplete list
	 * 
	 * @param target
	 * @param choice
	 *            the <CHOICE> object selected
	 */
	protected abstract void onSelect(AjaxRequestTarget target, CHOICE choice);
	
	protected boolean showAcronyms(){
		return false;
	}
	
	protected String getAcronym(CHOICE choice){
		return null;
	}
	

	// @Override
	// public void updateModel() {
	// textField.updateModel();
	// }

	/**
	 * Returns the real JS name of the textfield component
	 * 
	 * @return
	 */
	private String getJsVarName() {
		return "YAHOO.widget." + textField.getMarkupId();
	}

	/**
	 * Returns an array of choiceValueS obtained by invoking
	 * {@link #getChoiceValue(Object)} for each object within
	 * {@link #getChoices(String)}
	 * 
	 * @param input
	 *            the search string that will be passed to
	 *            {@link #getChoices(String)}
	 * @return the array with values
	 */
	protected String[][] getChoiceValues(String input) {
		Collection<CHOICE> choices = getChoices(input);
		List<String[]> choiceValues = new ArrayList<String[]>();
		for (CHOICE choice : choices) {
			Integer choiceLevel = getChoiceLevel(choice);
			String choiceValue = getChoiceValue(choice);
			if (showAcronyms()){
				String acronym = getAcronym(choice);
				if (acronym != null){
					acronym = acronym.replace(ACRONYM_DELIMITER_START, " ");
					acronym = acronym.replace(ACRONYM_DELIMITER_STOP_PART, " ");
					acronym = acronym.trim();
					choiceValue = ACRONYM_DELIMITER_START + acronym + ACRONYM_DELIMITER_STOP + choiceValue;
				}
			}
				
			choiceValues.add(new String[] { choiceValue,
					choiceLevel != null ? choiceLevel.toString() : "0" });
		}

		return choiceValues.toArray(new String[0][0]);
	}

	/**
	 * Dynamically instantiates the {@link AbstractAmpAutoCompleteModel} given
	 * when this component was constructed (which was stored in
	 * {@link #objectListModelClass}). Passes the @param input and the
	 * {@link #modelParams} in its constructor and then uses this to query
	 * {@link AbstractAmpAutoCompleteModel#getObject()} and to get the list of
	 * choices
	 * 
	 * @param input
	 * @return
	 */
	protected Collection<CHOICE> getChoices(String input) {
		/*
		 * when adjusting minimum length for the search keyword, also
		 * adjust "autoComplete.minQueryLength" in the js file 
		 */
		if (input != null && input.length() > 0 && input.length() < 1)
			return Collections.emptyList();
		Constructor<? extends AbstractAmpAutoCompleteModel<CHOICE>> constructor;
		try {
			constructor = objectListModelClass.getConstructor(String.class,
					Map.class);
			AbstractAmpAutoCompleteModel<CHOICE> newInstance = constructor
					.newInstance(input, modelParams);
			return newInstance.getObject();
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	

	protected CHOICE getSelectedChoice(String input) {
	
		Constructor<? extends AbstractAmpAutoCompleteModel<CHOICE>> constructor;
		try {
			
			if (showAcronyms() && input.indexOf(ACRONYM_DELIMITER_STOP) != -1){
				input = input.substring(input.indexOf(ACRONYM_DELIMITER_STOP) + ACRONYM_DELIMITER_STOP.length());
			}
			
			int extraInfoEnd = input.indexOf(BOLD_DELIMITER_STOP);
			if (extraInfoEnd >= 0)
				input = input.substring(extraInfoEnd + BOLD_DELIMITER_STOP.length());
			
			constructor = objectListModelClass.getConstructor(String.class,
					Map.class);
			AbstractAmpAutoCompleteModel<CHOICE> newInstance = constructor
					.newInstance(input, modelParams);
			newInstance.getParams().put(AbstractAmpAutoCompleteModel.PARAM.EXACT_MATCH, true);
			Collection<CHOICE> choices = newInstance.getObject();
			
			if(choices==null || choices.size()==0) throw new RuntimeException("Cannot find selection object " + input +"!");
			CHOICE[] allChoices = (CHOICE[]) choices.toArray();
			return allChoices[allChoices.length - 1];
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	

	/**
	 * YAHOO.widget.WicketAutoComplete using {@link YuiAutoComplete#textField
	 * #getMarkupId()} and {@link YuiAutoComplete#container#getMarkupId()}. The
	 * select all button uses {@link YuiAutoComplete#toggleButton#getMarkupId()}
	 * The {@link #respond(AjaxRequestTarget)} method invokes the
	 * {@link JsonUtils#marshal(Object)} to get a JSON object which is sent to
	 * YUI's dataSource.responseArray
	 * 
	 * @author mpostelnicu@dgateway.org
	 * @since Jun 15, 2011
	 */
	private class YuiAutoCompleteBehavior extends AbstractDefaultAjaxBehavior {

		private static final long serialVersionUID = 1L;

		@Override
		protected void respond(AjaxRequestTarget target) {
			Request request = getRequestCycle().getRequest(); 
			IRequestParameters paramMap = request.getRequestParameters();
			StringValue queryS = paramMap.getParameterValue("q");
			String query = queryS.toString();
			String[][] result = getChoiceValues(query);
			String jsonResult = OnePagerUtil.jsonMarshal(result);
			target.appendJavaScript(getJsVarName()
					+ ".dataSource.responseArray = " + jsonResult + ";");
		}

	}

}
