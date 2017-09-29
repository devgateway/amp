package org.dgfoundation.amp.onepager.components.fields;

import org.apache.commons.collections.map.HashedMap;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpResourcesFormTableFeature;
import org.dgfoundation.amp.onepager.models.AmpResourcesSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.contentrepository.helper.NodeWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class wrapper for AmpAutocompleteFieldPanel to link the existing resources to the activity
 */
public class AmpExistingDocumentFieldPanel extends AmpFeaturePanel {


    public AmpExistingDocumentFieldPanel(final String id,
                                         final IModel<AmpActivityVersion> am,
                                         final String fmName,
                                         final AmpResourcesFormTableFeature resourcesList) throws Exception {
        super(id, am, fmName, true);


        // add the weblink
        final String newDocumentGenKey = TranslatorWorker.generateTrnKey(fmName);
        final AjaxLink addNewLink = new AjaxLink("panelLink") {
            public void onClick(AjaxRequestTarget target) {
                target.prependJavaScript(OnePagerUtil.getToggleChildrenJS(this));
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                configureTranslationMode(this, newDocumentGenKey, id + "H");
            }


        };
        addNewLink.add(new Label("linkText", TranslatorWorker.translateText(fmName)));



        WebMarkupContainer rc = new WebMarkupContainer("resourcePanel");
        rc.setOutputMarkupId(true);
        rc.add(new AttributeModifier("id", "id_" + this.getId()));


        // add autocomplete panel
        final AmpAutocompleteFieldPanel<NodeWrapper> searchDocs
                = new AmpAutocompleteFieldPanel<NodeWrapper>
                    ("autocompleteField",
                    "Resources Field",
                    AmpResourcesSearchModel.class) {

            @Override
            protected String getChoiceValue(NodeWrapper choice) {
                String title = choice.getTitle();
                if(title == null) {
                    Map<String, String> allTitles = choice.getTranslatedTitle() != null ? choice.getTranslatedTitle(): new HashMap<String, String>();
                    for (Map.Entry<String, String> entry: allTitles.entrySet()){
                        if(!entry.getKey().startsWith("jcr:")) {
                            title = entry.getValue();
                            break;
                        }
                    }
                }
                return title + " (" + choice.getUuid().hashCode() + ")";
            }

            @Override
            public void onSelect(AjaxRequestTarget target, NodeWrapper choice) {
                Set<AmpActivityDocument> existingActDocs = am.getObject().getActivityDocuments();
                if (am.getObject().getActivityDocuments() == null)
                    am.getObject().setActivityDocuments(new HashSet<AmpActivityDocument>());

                boolean docExists = false;

                if (choice.getUuid() != null) {
                    for (AmpActivityDocument ampActivityDocument : existingActDocs) {
                        if(ampActivityDocument.getUuid().equals(choice.getUuid())){
                            docExists = true;
                            break;
                        }
                    }
                }

                if (! docExists) {
                    AmpActivityDocument ad = new AmpActivityDocument();
                    ad.setAmpActivity(am.getObject());
                    ad.setUuid(choice.getUuid());
                    ad.setDocumentType(ActivityDocumentsConstants.RELATED_DOCUMENTS);
                    am.getObject().getActivityDocuments().add(ad);
                }

                HashSet<AmpActivityDocument> delItems = getSession().getMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS);
                if (delItems == null) {
                    delItems = new HashSet<AmpActivityDocument>();
                }

                for (AmpActivityDocument delItem : delItems) {
                    if (delItem.getUuid().equals(choice.getUuid())) {
                        delItems.remove(delItem);
                        break;
                    }
                }

                resourcesList.setRefreshExistingDocs(true);
                target.add(resourcesList);
                target.add(this);
            }

            @Override
            public Integer getChoiceLevel(NodeWrapper choice) {
                return 0;
            }
        };

        add(addNewLink);
        add(rc);
        rc.add(searchDocs);

    }

    private void configureTranslationMode (AjaxLink link, String key, String id) {
        if (TranslatorUtil.isTranslatorMode(getSession())){
            link.setOutputMarkupId(true);
            link.add(new AttributeAppender("style", new Model<String>("text-decoration: underline; color: #0CAD0C;"), ""));
            link.add(new AttributeModifier("key", key));
            link.add(new AttributeModifier("onclick", "$('#"+id+"').slideToggle();spawnEditBox(this.id)"));
        } else{
            link.add(AttributeModifier.remove("key"));
            link.add(AttributeModifier.remove("style"));
            link.add(AttributeModifier.remove("onclick"));
            link.add(new AttributeModifier("onclick", "$('#id_" + this.getId() + "').slideToggle();"));

        }
    }
}
