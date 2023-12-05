/*
 * Copyright (c) 2013 Development Gateway (www.developmentgateway.org)
 */

package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.Set;

/**
 * ListEditor that supports paging with the help of PagingListNavigator component
 *
 * @author aartimon@developmentgateway.org
 * @since 27 June 2013
 */
public abstract class PagingListEditor<T> extends ListEditor<T> {
    private final static int DEFAULT_ITEMS_PER_PAGE = 10;
    private final static int FIRST_PAGE = 1;
    private int currentPage;
    private final int itemsPerPage = DEFAULT_ITEMS_PER_PAGE;

    public PagingListEditor(String id, IModel<Set<T>> model) {
        super(id, model);
        this.currentPage = FIRST_PAGE;
    }

    protected final int getLastPage(){
        double size = items.size();
        Double d = Math.ceil(size/itemsPerPage);
        return Math.max(1, d.intValue());
    }

    protected final int nextPage(){
        if (currentPage < getLastPage())
            currentPage++;
        return currentPage;
    }

    protected final int previousPage(){
        if (currentPage > FIRST_PAGE)
            currentPage--;
        return currentPage;
    }

    public final int goToPage(int page){
        if (FIRST_PAGE <= page && page <= getLastPage())
            currentPage = page;
        return currentPage;
    }

    public final int goToLastPage(){
        return goToPage(getLastPage());
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getFirstPage() {
        return FIRST_PAGE;
    }

    /**
     * Since the editorOnBeforeRender renders the items every time
     * we're no longer rendering new items separately
     * @param value
     */
    @Override
    public void addItem(T value) {
        if (items == null)
            items = new ArrayList<T>();
        items.add(value);
        updateModel();
    }


    /**
     * Override the editorOnBeforeRender method to render the items
     * in the current page every time
     */
    @Override
    protected void editorOnBeforeRender() {
        this.removeAll();
        if (!hasBeenRendered())
            prepareItemsForRendering();
        int startItem = (currentPage - 1) * itemsPerPage;
        int endItem = Math.min(items.size(), (currentPage) * itemsPerPage);
        for (int i = startItem; i < endItem; i++){
            renderItem(i);
        }
    }
}
