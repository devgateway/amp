package org.dgfoundation.amp.onepager.components.features;

import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.request.resource.ResourceReference;

public class CustomResourceLinkResourceLink<Void> extends ResourceLink<Void> {
    public CustomResourceLinkResourceLink(String id, ResourceReference resourceReference) {
        super(id, resourceReference);
    }
    private ExportExcelResourceReference exportExcelResourceReference;
    public void setReference(ExportExcelResourceReference resourceReference) {
        this.exportExcelResourceReference=resourceReference;
    }

}
