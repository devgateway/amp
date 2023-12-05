    package org.dgfoundation.amp.onepager.components.fields;

    import org.apache.wicket.AttributeModifier;
    import org.apache.wicket.Component;
    import org.apache.wicket.ajax.AjaxRequestTarget;
    import org.apache.wicket.behavior.AttributeAppender;
    import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
    import org.apache.wicket.markup.html.WebMarkupContainer;
    import org.apache.wicket.markup.html.basic.Label;
    import org.apache.wicket.markup.html.form.FormComponent;
    import org.apache.wicket.markup.html.list.ListItem;
    import org.apache.wicket.markup.html.list.ListView;
    import org.apache.wicket.markup.html.panel.Panel;
    import org.apache.wicket.model.AbstractReadOnlyModel;
    import org.apache.wicket.model.IModel;
    import org.apache.wicket.model.Model;
    import org.apache.wicket.model.PropertyModel;
    import org.dgfoundation.amp.onepager.models.EditorWrapperModel;
    import org.dgfoundation.amp.onepager.models.ResourceTranslationModel;
    import org.dgfoundation.amp.onepager.models.TranslationDecoratorModel;
    import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
    import org.dgfoundation.amp.onepager.util.AttributePrepender;
    import org.dgfoundation.amp.onepager.web.pages.OnePager;
    import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
    import org.digijava.module.aim.annotations.translation.TranslatableField;
    import org.digijava.module.translation.util.ContentTranslationUtil;

    import java.lang.reflect.Field;
    import java.util.List;
    import java.util.Set;

/**
 * @author aartimon@developmentgateway.org
 * @since 03 October 2013
 */
public class TranslationDecorator extends Panel {
    private TranslationDecoratorModel tdm;
    private Model<String> langModel;

    private IModel<Boolean> switchingDisabled = new Model<Boolean>(Boolean.FALSE);
    private Component currentLabel;
    private TranslationDecorator(String id, final IModel<?> model, final Component component) {
        this(id, model, component,false) ;
    }
    
    private TranslationDecorator(String id, final IModel<?> model, final Component component,boolean checkDefaultLanguageOnSwitch) {
        super(id, model);

        setOutputMarkupId(true);

        IModel<List<String>> locales = new AbstractReadOnlyModel<List<String>>() {
            @Override
            public List<String> getObject() {
                return TranslatorUtil.getLocaleCache();
            }
        };

        //for the current language on the page we set null as a value
        langModel = new Model<String>(null);

        if (model instanceof TranslationDecoratorModel){
            tdm = (TranslationDecoratorModel) model;
            tdm.setLangModel(langModel);
        }

        final IModel<Set<String>> availableTrnLocales = new AbstractReadOnlyModel<Set<String>>() {
            @Override
            public Set<String> getObject() {
                return tdm.getLangForAvailableTrn();
            }
        };

        add(new AttributeModifier("class", "trncontainer"));

        ListView<String> list = new ListView<String>("languages", locales) {
            @Override
            protected void populateItem(final ListItem<String> item) {
                final String language = item.getModelObject();
                IndicatingAjaxLink link = new IndicatingAjaxLink("link") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        //Only allow switching if the current language is set and checkDefaultLanguageOnSwitch is true 
                        if (checkDefaultLanguageOnSwitch && !language.equals(getSession().getLocale().getLanguage())
                                && component.getDefaultModel().getObject() == null) {

                        } else {
                            if (language.equals(getSession().getLocale().getLanguage()))
                                langModel.setObject(null);
                            else
                                langModel.setObject(language);

                            if (component instanceof FormComponent) {
                                FormComponent fc = (FormComponent) component;
                                fc.clearInput();
                            }

                            target.add(TranslationDecorator.this);
                            target.add(component);
                        }
                    }
                };
                String classValue = "tab" + item.getIndex();

                //Set the compareValue to null if the current language is the page language
                String compareValue = null;
                if (!language.equals(getSession().getLocale().getLanguage()))
                    compareValue = language;

                Model<String> labelModel = new Model<String>(language) {
                    @Override
                    public String getObject() {
                        String display = super.getObject();
                        if (availableTrnLocales.getObject().contains(display))
                            display += " &#x2713;";
                        return display;
                    }
                };


                Label label = new Label("title", labelModel);
                label.setEscapeModelStrings(false);
                label.setOutputMarkupId(true);

                //set the css for the current selected tab
                if ((langModel.getObject() == null && compareValue == null) || (langModel.getObject() != null && langModel.getObject().equals(compareValue))){
                    currentLabel = label;
                    classValue += " selected";
                }



                link.add(label);
                if (component instanceof AmpTextAreaFieldPanel){
                    AmpTextAreaFieldPanel area = (AmpTextAreaFieldPanel)component;
                    //if we have a wysiwyg editor we need to close it when switching between tabs
                    if (area.isWysiwyg()){
                        link.add(new AttributePrepender("onclick", Model.of("$('#" + area.getCloseLink().getMarkupId() + "').click();"), " "));
                        link.add(new AttributeAppender("onclick", Model.of("return false;"), " "));
                    }
                } else if (component.getClass().getEnclosingClass() != null &&
                        (component.getClass().getEnclosingClass().isAssignableFrom(AmpTextFieldPanel.class) ||
                                component.getClass().getEnclosingClass().isAssignableFrom(AmpTextAreaFieldPanel.class))) {
                    link.add(new AttributeAppender("onclick", Model.of("return false;"), " "));
                }
                item.add(new AttributeModifier("class", classValue));
                item.add(link);

            }
        };
        add(list);
        component.add(new AttributeAppender("style", "margin: 0px;"));
    }

    @Override
    protected void onBeforeRender() {
        //set the activity model
        if (!hasBeenRendered())
            tdm.setAm(((OnePager)getPage()).getActivityModel());
        super.onBeforeRender();

    }
    
    public static Component of(String id, IModel<?> model, Component textContainer){
        return of(id, model, textContainer,false);
    }
    
    public static Component of(String id, IModel<?> model, Component textContainer,Boolean checkDefaultLanguageOnSwitch){
        if (isTranslatable(model)){
            TranslationDecorator td = new TranslationDecorator(id, model, textContainer,checkDefaultLanguageOnSwitch);
            return td;
        }
        else{
            WebMarkupContainer wmc = new WebMarkupContainer(id);
            wmc.setVisibilityAllowed(false);
            return wmc;
        }
    }

    public static IModel proxyModel(IModel<String> model){
        if (isTranslatable(model))
            return new TranslationDecoratorModel(model);
        return model;
    }

    private static boolean isTranslatable(IModel<?> model){
        if (!ContentTranslationUtil.multilingualIsEnabled())
            return false; //Multilingual disabled

        if (model instanceof TranslationDecoratorModel)
            return true;
        if (model instanceof PropertyModel){
            PropertyModel<String> pm = (PropertyModel<String>) model;
            Field field = pm.getPropertyField();
            if (field != null && field.getAnnotation(TranslatableField.class) != null)
                return true;
        }
        if (model instanceof EditorWrapperModel){
            EditorWrapperModel em = (EditorWrapperModel) model;
            if (em.getKeyModel() instanceof PropertyModel){
                PropertyModel pm = (PropertyModel) em.getKeyModel();
                Field field = pm.getPropertyField();
                if (field.getAnnotation(VersionableFieldTextEditor.class) != null)
                    return true;
            }
        }
        if (model instanceof ResourceTranslationModel) {
            return true;
        }
        return false;
    }

    public IModel<Boolean> getSwitchingDisabled() {
        return switchingDisabled;
    }

    public Component getCurrentLabel() {
        return currentLabel;
    }

    public Model<String> getLangModel() {
        return langModel;
    }
}
