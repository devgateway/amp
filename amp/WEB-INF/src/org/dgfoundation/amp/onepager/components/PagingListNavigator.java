/*
 * Copyright (c) 2013 Development Gateway (www.developmentgateway.org)
 */

package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Component that shows a navigation bar for the PagingListEditor
 *
 * @author aartimon@developmentgateway.org
 * @since 27 June 2013
 */
public class PagingListNavigator<T> extends Panel {
    //number of pages to show before the current page and after it
    private static final int VIEW_SIZE = 5;
    private AbstractReadOnlyModel<List<Integer>> model;

    public PagingListNavigator(String id, final PagingListEditor<T> ple) {
        super(id);
        this.setOutputMarkupId(true);



        model = new AbstractReadOnlyModel<List<Integer>>() {
            @Override
            public List<Integer> getObject() {
                int currentPage = ple.getCurrentPage();
                int startPage = Math.max(ple.getFirstPage() + 1, currentPage - VIEW_SIZE);
                int stopPage = Math.min(ple.getLastPage() - 1, currentPage + VIEW_SIZE);

                ArrayList<Integer> ret = new ArrayList<Integer>();
                ret.add(ple.getFirstPage());
                for (int i = startPage; i <= stopPage; i++) {
                    ret.add(i);
                }
                if (ple.getFirstPage() < ple.getLastPage())
                    ret.add(ple.getLastPage());

                return ret;
            }
        };

        ListView<Integer> list = new ListView<Integer>("naviList", model) {
            @Override
            protected void populateItem(final ListItem<Integer> item) {
                AjaxLink<Integer> link = new AjaxLink<Integer>("link", item.getModel()) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        ple.goToPage(item.getModelObject());
                        target.add(ple.getParent());
                        target.add(PagingListNavigator.this);
                    }
                    //Label inspired
                    @Override
                    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
                        String body;
                        Integer page = (Integer) getDefaultModelObject();
                        if (page.equals(Integer.valueOf(ple.getCurrentPage())))
                            body = "" + page;
                        else
                            body = "<a href='#'>" + page + "</a>";

                        replaceComponentTagBody(markupStream, openTag, body);
                    }
                };
                item.add(link);
                Label separator = new Label("separator", " ");
                item.add(separator);
            }
        };
        add(list);
    }
    
    public boolean isVisible() {
        return model.getObject().size() > 1;
    }


}
