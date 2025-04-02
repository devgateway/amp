package org.dgfoundation.amp.onepager.components;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.translation.LabelTranslatorBehaviour;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;

import static org.dgfoundation.amp.onepager.components.ExpandableListEditor.DEFAULT_ITEMS_PER_PAGE;

/**
 * Component that shows a navigation bar for the ExpandableListEditor
 *
 * @author Viorel Chihai
 */

public class ExpandableListNavigator<T> extends Panel {

    protected static Logger logger = Logger.getLogger(ExpandableListNavigator.class);

    private AbstractReadOnlyModel<String> loadInfoModel;

    public ExpandableListNavigator(String id, final ExpandableListEditor<T> ple) {
        super(id);

        loadInfoModel = new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                int currentLoadSize = ple.getCurrentIndex() * DEFAULT_ITEMS_PER_PAGE;
                int allItemsSize = ple.getListSize();
                int currentItemsSize = currentLoadSize < allItemsSize ? currentLoadSize : allItemsSize;

                return String.format("%s/%s", currentItemsSize, allItemsSize);
            }
        };
        add(new Label("loadInfo", loadInfoModel));

        IndicatingAjaxLink link = new IndicatingAjaxLink("loadMoreButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                ple.loadMore();
                target.add(ple.getParent());
                target.add(ExpandableListNavigator.this);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(ple.getCurrentIndex() * DEFAULT_ITEMS_PER_PAGE < ple.getListSize());
            }
        };

        AmpAuthWebSession session = (AmpAuthWebSession) getSession();
        Site site = session.getSite();

        String buttonCaption = "Load More";
        String genKey = TranslatorWorker.generateTrnKey(buttonCaption);
        String translatedValue;
        link.add(new AttributeModifier("value", new Model(buttonCaption)));
        try {
            translatedValue = TranslatorWorker.getInstance(genKey).
                    translateFromTree(genKey, site, session.getLocale().getLanguage(),
                            buttonCaption, TranslatorWorker.TRNTYPE_LOCAL, null);
            link.add(new AttributeModifier("value", new Model(translatedValue)));
        } catch (WorkerException e) {
            logger.error("Can't translate:", e);
        }

        if (TranslatorUtil.isTranslatorMode(getSession())) {
            link.setOutputMarkupId(true);
            link.add(new LabelTranslatorBehaviour());
            link.add(new AttributeAppender("style", new Model("text-decoration: underline; color: #0CAD0C;"), ""));
            link.add(new AttributeModifier("key", genKey));
        }

        add(link);
    }
}
