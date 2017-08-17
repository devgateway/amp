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
    }

    private void toggleotherInfo(final boolean b) {
        if (this.otherInfo != null) {
            this.otherInfo.setVisible(b);
            if (this.otherInfo.isVisible()) {
                this.otherInfo.getTextAreaContainer().setRequired(true);
            }
        }
    }

    private void updateFields(final AjaxRequestTarget target) {
        target.add(this.otherInfo.getParent());
    }

    private void updateOtherInfo() {
        toggleotherInfo(isOtherInfoVisible());
    }

    @Override
    protected void onUpdate(final AjaxRequestTarget target) {
        updateOtherInfo();
        updateFields(target);
    }

    private boolean isOtherInfoVisible() {
        if (this.getFormComponent() != null && this.getFormComponent().getModelObject() != null) {
            if (this.getFormComponent().getModelObject() instanceof AmpCategoryValue) {
                AmpCategoryValue value = (AmpCategoryValue) this.getFormComponent().getModelObject();
                return isOtherSelected(value.getValue());
            } else {
                Set<AmpCategoryValue> values = (Set<AmpCategoryValue>) this.getFormComponent().getModelObject();
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

    protected void onBind() {
        super.onBind();
        updateOtherInfo();
    }
}
