/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.util.upload.FileItem;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.fields.AmpLabelFieldPanel;
import org.dgfoundation.amp.onepager.components.upload.FileUploadPanel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpStructureImg;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author mmoras
 * @since Jan 2, 2013
 */
public class AmpStructureImgListComponent<T> extends AmpComponentPanel {
    private static final long serialVersionUID = 1L;

    public AmpStructureImgListComponent(String id, String fmName,
                                        final IModel<AmpStructure> model, IModel<AmpActivityVersion> am) {
        super(id, model, fmName);
        init(model, am);
    }
    
    private void init(final IModel<AmpStructure> model, IModel<AmpActivityVersion> am){
        String activityId = "new";
        if (am.getObject().getAmpActivityId() != null)
            activityId = Long.toString(am.getObject().getAmpActivityId());
        final IModel<FileItem> fileItemModel = new Model<FileItem>();

        final ListEditor<AmpStructureImg> imgList = createImgList(model);
        add(imgList);

        FileUploadPanel fileUpload = new FileUploadPanel("imgFile", activityId, fileItemModel);
        add(fileUpload);


        add(new AjaxLink("addimgbutton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                if (fileItemModel.getObject() == null) {
                    String error = TranslatorUtil.getTranslatedText("You should select an image to add.");
                    String alert = "alert('" + error + "');";
                    target.appendJavaScript(alert);
                    return;
                }

                FileItem fileUp = fileItemModel.getObject();
                if (fileUp.getContentType() == null || !fileUp.getContentType().startsWith("image/")) {
                    String error = TranslatorUtil.getTranslatedText("The selected file is not an image. Please select an image file");
                    String alert = "alert('" + error + "');";
                    target.appendJavaScript(alert);
                    return;
                }

                AmpStructureImg img = new AmpStructureImg();
                img.setImgFile(fileUp.get());
                img.setImgFileName(fileUp.getName());
                img.setContentType(fileUp.getContentType());
                imgList.addItem(img);

                fileItemModel.setObject(null); //reset upload
                target.add(imgList.getParent());
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(imgList.getParent()));
            }

        });
    }
    
    private ListEditor<AmpStructureImg> createImgList(IModel<AmpStructure> structureModel){
        
        final PropertyModel<Set<AmpStructureImg>> setModel=new PropertyModel<Set<AmpStructureImg>>(structureModel,"images");
        if (setModel.getObject() == null)
            setModel.setObject(new TreeSet<AmpStructureImg>());
        
        final ListEditor<AmpStructureImg> imgList = new ListEditor<AmpStructureImg>("imgList", setModel) {
            @Override
            protected void onPopulateItem(ListItem<AmpStructureImg> item) {

                final AmpStructureImg modelObject = item.getModelObject();
                DynamicImageResource imgResource = new DynamicImageResource() {
                    @Override
                    protected byte[] getImageData(Attributes attributes) {
                        return modelObject.getImgFile();
                    }
                };
                Image img = new Image("imgFileDisplay", imgResource);
                item.add(img);

                PropertyModel<AmpStructureImg> imgModel = new PropertyModel<AmpStructureImg>(item.getModelObject(), "imgFileName");
                AmpLabelFieldPanel<AmpStructureImg> label = new AmpLabelFieldPanel<AmpStructureImg>("imgFileNameLbl", imgModel, "");
                item.add(label);

                ListEditorRemoveButton delbutton = new ListEditorRemoveButton("deleteStructureImg", "Delete Image");
                item.add(delbutton);

            }

        };
        return imgList;
    }   
}
