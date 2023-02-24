package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.model.IModel;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.Comparator;
import java.util.Set;

/**
 * ListEditor that supports can be loaded with more items
 *
 * @author Viorel Chihai
 */
public abstract class ExpandableListEditor<T> extends ListEditor<T> {

    public static final int DEFAULT_ITEMS_PER_PAGE = 10;

    private int currentLoadIndex;

    private final int itemsPerPage = DEFAULT_ITEMS_PER_PAGE;

    public ExpandableListEditor(String id, IModel<Set<T>> model) {
        super(id, model, null);
    }

    public ExpandableListEditor(String id, IModel<Set<T>> model, Comparator<T> comparator) {
        super(id, model, comparator);
        this.currentLoadIndex = 1;
    }

    protected final int getLastIndex() {
        double size = items.size();
        Double d = Math.ceil(size / itemsPerPage);
        return Math.max(1, d.intValue());
    }

    protected final int getListSize() {
        return items.size();
    }

    protected final int loadMore() {
        if (currentLoadIndex < getLastIndex()) {
            currentLoadIndex++;
        }
        return currentLoadIndex;
    }

    public int getCurrentIndex() {
        return currentLoadIndex;
    }

    /**
     * Override the editorOnBeforeRender method to render the items based on load index
     */
    @Override
    protected void editorOnBeforeRender() {
        if (isExpandable()) {
            this.removeAll();
            prepareItemsForRendering();
            int endItem = Math.min(items.size(), (currentLoadIndex) * itemsPerPage);
            for (int i = 0; i < endItem; i++) {
                renderItem(i);
            }
        } else {
            super.editorOnBeforeRender();
        }
    }

    public boolean isExpandable() {
        return FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.ACTIVITY_FORM_FUNDING_SECTION_DESIGN);
    }

}
