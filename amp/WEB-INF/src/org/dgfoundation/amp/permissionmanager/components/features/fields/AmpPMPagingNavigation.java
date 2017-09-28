/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.fields;

import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigation;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.collections.MicroMap;

/**
 * @author dan
 *
 */
public class AmpPMPagingNavigation extends AjaxPagingNavigation {

    /**
     * @param id
     * @param pageable
     */
    public AmpPMPagingNavigation(String id, IPageable pageable) {
        super(id, pageable);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param pageable
     * @param labelProvider
     */
    public AmpPMPagingNavigation(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
        super(id, pageable, labelProvider);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void populateItem(LoopItem loopItem) {
        // Get the index of page this link shall point to
        final long pageIndex = loopItem.getIndex();
        
        // Add a page link pointing to the page
        final AbstractLink link = super.newPagingNavigationLink("pageLink", pageable, pageIndex);
        link.add(new TitleAppender(pageIndex));
        long currentPage = pageable.getCurrentPage();
        if(currentPage == pageIndex)
            link.add(new AttributeModifier("class",new Model("paging_sel")));
        loopItem.add(link);
        
        // Add a page number label to the list which is enclosed by the link
        String label = "";
        if (labelProvider != null)
        {
            label = labelProvider.getPageLabel(pageIndex);
        }
        else
        {
            label = String.valueOf(pageIndex + 1);
        }
        link.add(new Label("pageNumber", label));
    }
    
    private final class TitleAppender extends Behavior
    {
        private static final long serialVersionUID = 1L;
        /** resource key for the message */
        private static final String RES = "PagingNavigation.page";
        /** page number */
        private final long page;

        /**
         * Constructor
         * 
         * @param page
         *            page number to use as the ${page} var
         */
        public TitleAppender(long page)
        {
            this.page = page;
        }

        /** {@inheritDoc} */
        @Override
        public void onComponentTag(Component component, ComponentTag tag)
        {
            Map<String, String> vars = new MicroMap<String, String>("page",
                String.valueOf(page + 1));
            tag.put("title", AmpPMPagingNavigation.this.getString(RES, Model.ofMap(vars)));
        }
    }
    
}
