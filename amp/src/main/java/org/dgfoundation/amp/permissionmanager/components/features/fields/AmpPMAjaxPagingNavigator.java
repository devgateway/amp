/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.fields;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;

/**
 * @author dan
 *
 */
public class AmpPMAjaxPagingNavigator extends AjaxPagingNavigator {

    /**
     * @param id
     * @param pageable
     */
    public AmpPMAjaxPagingNavigator(final String id, final IPageable pageable){
        this(id, pageable, null);
    }

    /**
     * @param id
     * @param pageable
     * @param labelProvider
     */
    public AmpPMAjaxPagingNavigator(final String id, final IPageable pageable,final IPagingLabelProvider labelProvider){
        super(id,pageable,labelProvider);
    }
    
    @Override
    protected void onBeforeRender()
    {
        if (get("first") == null)
        {
            // Get the navigation bar and add it to the hierarchy
            PagingNavigation pagingNavigation = super.getPagingNavigation();
            pagingNavigation = newNavigation(getId(), super.getPageable(), null);
            add(pagingNavigation);

            // Add additional page links
            add(newPagingNavigationLink("first", super.getPageable(), 0).add(
                new TitleAppender("PagingNavigator.first")));
            add(newPagingNavigationIncrementLink("prev", super.getPageable(), -1).add(
                new TitleAppender("PagingNavigator.previous")));
            add(newPagingNavigationIncrementLink("next", super.getPageable(), 1).add(
                new TitleAppender("PagingNavigator.next")));
            add(newPagingNavigationLink("last", super.getPageable(), -1).add(
                new TitleAppender("PagingNavigator.last")));
        }
        super.onBeforeRender();
    }

    @Override
    protected PagingNavigation newNavigation(String id, IPageable pageable,
            IPagingLabelProvider labelProvider) {
        return new AmpPMPagingNavigation(id, pageable, labelProvider);
    }
    
    private final class TitleAppender extends Behavior
    {
        private static final long serialVersionUID = 1L;

        private final String resourceKey;

        /**
         * Constructor
         * 
         * @param resourceKey
         *            resource key of the message
         */
        public TitleAppender(String resourceKey)
        {
            this.resourceKey = resourceKey;
        }

        /** {@inheritDoc} */
        @Override
        public void onComponentTag(Component component, ComponentTag tag)
        {
            tag.put("title", AmpPMAjaxPagingNavigator.this.getString(resourceKey));
        }
    }
}
