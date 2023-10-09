/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.yui;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.json.JsonUtils;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
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
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.fields.AmpFieldPanel;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.models.AmpAutoCompleteModelParam;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.proxy.HibernateProxyHelper;

import javax.jcr.RepositoryException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Autocomplete Combobox Component based on YUI 2.8.x (or upper). This component
 * can be used to show an autocomplete editbox {@linkplain http://developer.yahoo.com/yui/examples/autocomplete/ac_combobox.html}
 * 
 * @author mpostelnicu@dgateway.org
 * @since Jun 14, 2011
 * @see {@linkplain http://ptrthomas.wordpress.com/2009/08/12/wicket-tutorial-yui-autocomplete-using-json-and-ajax/}
 */
public abstract class AmpAutocompleteFieldPanel<CHOICE> extends
        AmpFieldPanel<CHOICE> implements IAjaxIndicatorAware {

    private static final long serialVersionUID = 1L;
    public static final String ACRONYM_DELIMITER_START = "(";
    private static final String ACRONYM_DELIMITER_STOP_PART = ")";
    public static final String ACRONYM_DELIMITER_STOP = ACRONYM_DELIMITER_STOP_PART + " - ";
    public static final String BOLD_DELIMITER_START = "<b>";
    public static final String BOLD_DELIMITER_STOP = "</b> ";
    
    private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();
    protected boolean reuseObjects; 

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
     * store the class of CHOICE, needed to load the selected object by id
     */
    private Class<CHOICE> objClass;
    
    

    /**
     * Behavior that is invoked when an item is selected in YUI list. The
     * {@link #onBeforeRender()} method is overiden and adds a JS function
     * wrapping the {@link AbstractAmpAutoCompleteModel} callbackUrl
     */
    private final AbstractDefaultAjaxBehavior onSelectBehavior;

    /**
     * The list model parameters, if any
     */
    protected Map<AmpAutoCompleteModelParam, Object> modelParams;

    public Map<AmpAutoCompleteModelParam, Object> getModelParams() {
        return modelParams;
    }

    /**
     * Initializes the {@link #onSelectBehavior} callBackUrl Adds the
     * Wicket.Ajax.get function that can call the Wicket behavior from JavaScript
     */
    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        textField.add(new Behavior() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                final CharSequence url = onSelectBehavior.getCallbackUrl();
                String js = "function callWicket"
                        + textField.getMarkupId()
                        + "(selectedString) { " +
                        "var wcall = Wicket.Ajax.get({\"u\":\""
                        + url
                        + "&selectedString=\"+selectedString})}";
                response.render(JavaScriptHeaderItem.forScript(js, "callWicket-" + textField.getMarkupId()));
                //TODO: REMOVE it no issues, pre-Wicket6
                //response.renderJavaScript(js,
                //      "callWicket-" + textField.getMarkupId());
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
        this(id,
        fmName,"",
        objectListModelClass);
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
            String fmName,String aditionalTooltipKey,
            Class<? extends AbstractAmpAutoCompleteModel<CHOICE>> objectListModelClass) {
        this(id, fmName,aditionalTooltipKey, false, objectListModelClass,false);
    }
    
    /**
     * 
     * @param id id
     * @param fmName FM name
     * @param hideLabel  the FM name @see {@link AmpFMTypes}
     * @param objectListModelClass the model to retrieve the list of items
     * @param useCache if YUI should use client side cache
     */
    public AmpAutocompleteFieldPanel(
            String id,
            String fmName,
            boolean hideLabel,
            Class<? extends AbstractAmpAutoCompleteModel<CHOICE>> objectListModelClass, boolean useCache) {
        this(id,fmName,hideLabel,false,objectListModelClass,"");
        this.useCache=useCache;
    }
    
    


    /**
     * 
     * @param id id
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
            final Class<? extends AmpAutocompleteFieldPanel> clazz, final String jsName,final String autoCompeleteVar,String aditionalTooltipKey,boolean showTooltipIfLabelHidden) {
        this(id,fmName,aditionalTooltipKey,hideLabel,objectListModelClass,clazz, jsName,autoCompeleteVar,showTooltipIfLabelHidden);     
    }

    public AmpAutocompleteFieldPanel(
            String id,
            String fmName,String aditionalTooltipKey,
            boolean hideLabel,
            Class<? extends AbstractAmpAutoCompleteModel<CHOICE>> objectListModelClass,
            final Class<? extends AmpAutocompleteFieldPanel> clazz, final String jsName,final String autoCompeleteVar,boolean showTooltipIfLabelHidden) {
        //super(id, null, fmName, hideLabel );
        super(id, null,showTooltipIfLabelHidden,aditionalTooltipKey, fmName,hideLabel,"",false);
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
                response.render(JavaScriptHeaderItem.forUrl("/TEMPLATE/ampTemplate/js_2/yui/yahoo/yahoo-min.js"));
                response.render(JavaScriptHeaderItem.forUrl("/TEMPLATE/ampTemplate/js_2/yui/event/event-min.js"));
                response.render(JavaScriptHeaderItem.forUrl("/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"));
                response.render(JavaScriptHeaderItem.forUrl("/TEMPLATE/ampTemplate/js_2/yui/datasource/datasource-min.js"));
                response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(
                        AmpAutocompleteFieldPanel.class,
                        "AmpAutocompleteCommonScripts.js")));
                IModel variablesModel = new AbstractReadOnlyModel() {
                    public Map getObject() {
                        Map<String, CharSequence> variables = new HashMap<String, CharSequence>(
                                2);
                        variables.put("noResults", Strings.toEscapedUnicode(TranslatorUtil.getTranslation("No results found")));
                        return variables;
                    }
                };
                response.render(JavaScriptHeaderItem.forReference(new TextTemplateResourceReference(clazz, jsName, PackageTextTemplate.DEFAULT_CONTENT_TYPE, "UTF-8",variablesModel)));
                /*
                 * currently renderOnDomReadyJavaScript doesn't work as expected in IE8
                 * that is why jquery's $(document).ready has been added here
                 */
                
                String disableControl = "true";
                if (textField.getParent().isEnabled())
                    disableControl = "false";
                response.render(OnDomReadyHeaderItem.forScript("$(document).ready(function() {"+getJsVarName()
                        + " = new YAHOO.widget." + autoCompeleteVar + "('"
                        + textField.getMarkupId() + "', '" + getCallbackUrl()
                        + "', '" + container.getMarkupId() + "', '"
                        + toggleButton.getMarkupId() + "', '"
                        + indicator.getMarkupId() + "', " + useCache + ", " + disableControl + ", "
                        + SiteUtils.isEffectiveLangRTL() + ");"
                        + "});"));
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
                CHOICE choice = null;
                if (objClass.isAssignableFrom(NodeWrapper.class)){
                    //we need to treat Jackrabbit Node differently
                    String objId = selectedString;
                    //get all choices
                    Collection<CHOICE> choices = getChoices(null);
                    for (CHOICE ch: choices){
                        NodeWrapper nw = (NodeWrapper) ch;
                        String uuid = null;
                        try {
                            uuid = nw.getNode().getIdentifier();
                        } catch (RepositoryException e) {
                            logger.error("Can't get uuid for node: ", e);
                            break;
                        }
                        if (objId.equals(uuid)){
                            choice = ch;
                            break;
                        }
                    }
                }
                else{
                    Long objId = Long.parseLong(selectedString);
                    // hide loading icon:
                    choice = getSelectedChoice(objId);
                }
                target.appendJavaScript("$('#" + indicator.getMarkupId() + "').hide();");
                onSelect(target, choice);
            }
        };
        textField.add(onSelectBehavior);
    }

    public AmpAutocompleteFieldPanel(
            String id,
            String fmName,
            boolean hideLabel,boolean showTooltipIfLabelHidden,
            Class<? extends AbstractAmpAutoCompleteModel<CHOICE>> objectListModelClass,String additionalTooltipKey) {
        this(id,fmName,additionalTooltipKey,hideLabel,objectListModelClass,showTooltipIfLabelHidden);
        
    }
    /**
     * Constructs a new component. Initializes all subcomponents
     * 
     * @param id
     * @param hideLabel
     *            if true, the visible text label of the component is not shown
     */
    public AmpAutocompleteFieldPanel(
            String id,
            String fmName,String aditionalTooltipKey,
            boolean hideLabel,
            Class<? extends AbstractAmpAutoCompleteModel<CHOICE>> objectListModelClass,boolean showTooltipIfLabelHidden) {
        this(id, fmName,aditionalTooltipKey, hideLabel, objectListModelClass, AmpAutocompleteFieldPanel.class, "AmpAutocompleteFieldPanel.js","WicketAutoComplete",showTooltipIfLabelHidden);
    }

    /**
     * Gets the object id for the specified choice
     * Beware: it returns String uuid for Jackrabbit nodes
     *
     * @param choice
     *            the choice that needs value extraction
     * @return the unique id value of the choice
     */
    protected String getChoiceId(final CHOICE choice){
        if (objClass == null){
            objClass=(Class<CHOICE>)HibernateProxyHelper.getClassWithoutInitializingProxy(choice);
        }
        if (choice instanceof NodeWrapper){
            //we can't use the ContentTranslationUtil for Jackrabbit items, since it works only with hibernate
            NodeWrapper nodeWrapper = (NodeWrapper) choice;
            String uuid = null;
            try {
                uuid = nodeWrapper.getNode().getIdentifier();
            } catch (RepositoryException e) {
                logger.error("can't get node's uuid:" + e);
                return null;
            }
            return uuid;
        }
        else{
            return String.valueOf(ContentTranslationUtil.getObjectId(choice));
        }
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
            String choiceId = getChoiceId(choice);
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
                    choiceLevel != null ? choiceLevel.toString() : "0", choiceId});
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
        
        AmpAuthWebSession session = (AmpAuthWebSession) this.getSession();
        String language=session.getLocale().getLanguage();
        
        try {
            constructor = objectListModelClass.getConstructor(String.class,String.class,
                    Map.class);
            AbstractAmpAutoCompleteModel<CHOICE> newInstance = constructor
                    .newInstance(input, language, modelParams);
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

    /**
     * Retrieve the object coresponding to the user's selection
     * @param objId
     * @return
     */
    protected CHOICE getSelectedChoice(Long objId) {
        return (CHOICE) PersistenceManager.getSession().get(objClass, objId);
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

    public boolean isReuseObjects() {
        return reuseObjects;
    }

    public void setReuseObjects(boolean reuseObjects) {
        this.reuseObjects = reuseObjects;
    }

}
