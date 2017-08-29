package org.dgfoundation.amp.onepager.util;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.util.Set;

/**
 * Created by Aldo Picca.
 */
public class OtherInfoBehavior extends AjaxFormComponentUpdatingBehavior {
    private static final long serialVersionUID = 1L;
    private AmpTextAreaFieldPanel otherInfo;
    private static final String OTHER_INFO_KEY = "other";

    public OtherInfoBehavior(final String event, final AmpTextAreaFieldPanel otherInfo) {
        super(event);
        this.otherInfo = otherInfo;
        this.otherInfo.setOutputMarkupPlaceholderTag(true);
        this.otherInfo.setVisible(false);
        this.otherInfo.setIgnoreFmVisibility(true);
        this.otherInfo.setIgnorePermissions(true);
    }

    @Override
    protected void onUpdate(final AjaxRequestTarget target) {
        updateOtherInfo();
        updateFields(target);
    }

    protected void onBind() {
        super.onBind();
        updateOtherInfo();
    }

    private void updateOtherInfo() {
        toggleOtherInfo(isOtherInfoVisible());
    }

    private void updateFields(final AjaxRequestTarget target) {
        target.add(this.otherInfo);
    }

    private void toggleOtherInfo(final boolean b) {
        if (this.otherInfo != null) {
            this.otherInfo.setVisible(b);
            if (this.otherInfo.isVisible()) {
                this.otherInfo.getTextAreaContainer().setRequired(true);
            }
        }
    }

    private boolean isOtherInfoVisible() {
        if (this.getFormComponent().getModelObject() instanceof AmpCategoryValue) {
            AmpCategoryValue value = (AmpCategoryValue) this.getFormComponent().getModelObject();
            return isOtherSelected(value.getValue());
        } else {
            Set<AmpCategoryValue> values = (Set<AmpCategoryValue>) this.getFormComponent().getModelObject();
            if (values != null) {
                for (AmpCategoryValue value : values) {
                    if (isOtherSelected(value.getValue())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isOtherSelected(final String value) {
        return OTHER_INFO_KEY.equalsIgnoreCase(value);
    }

}
