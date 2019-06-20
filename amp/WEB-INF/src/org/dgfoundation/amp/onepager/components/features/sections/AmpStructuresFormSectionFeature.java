/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.PagingListEditor;
import org.dgfoundation.amp.onepager.components.PagingListNavigator;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.helper.structure.ColorData;
import org.dgfoundation.amp.onepager.helper.structure.CoordinateData;
import org.dgfoundation.amp.onepager.helper.structure.MapData;
import org.dgfoundation.amp.onepager.helper.structure.StructureData;
import org.digijava.kernel.ampapi.endpoints.util.ObjectMapperUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpStructureCoordinate;
import org.digijava.module.aim.dbentity.AmpStructureType;
import org.digijava.module.aim.util.StructuresUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class AmpStructuresFormSectionFeature extends
        AmpFormSectionFeaturePanel {

    private static final long serialVersionUID = -6654390083754446344L;

    private static final int COLOR_OFFSET_CATEGORY_VALUE = 7;
    
    protected Collection<AmpStructureType> structureTypes;

    public AmpStructuresFormSectionFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        final PropertyModel<Set<AmpStructure>> setModel=new PropertyModel<Set<AmpStructure>>(am,"structures");
        if (setModel.getObject() == null)
            setModel.setObject(new TreeSet<AmpStructure>());
        final PagingListEditor<AmpStructure> list;

        this.structureTypes =   StructuresUtil.getAmpStructureTypes();
        
        IModel<List<AmpStructure>> listModel = new AbstractReadOnlyModel<List<AmpStructure>>() {
            private static final long serialVersionUID = 3706184421459839220L;

            @Override
            public ArrayList<AmpStructure> getObject() {
                if (setModel != null && setModel.getObject() != null)
                    return new ArrayList<AmpStructure>(setModel.getObject());
                else
                    return null;
            }
        };
        
        final TransparentWebMarkupContainer containter = new TransparentWebMarkupContainer("listWithPaginator"); 
        containter.setOutputMarkupId(true);
        list = new PagingListEditor<AmpStructure>("list", setModel) {
            
            
            @Override
            protected void onPopulateItem(
                    org.dgfoundation.amp.onepager.components.ListItem<AmpStructure> item) {
                IModel<AmpStructure> structureModel = item.getModel();
                final AmpTextFieldPanel<String> name = new AmpTextFieldPanel<String>("name", new PropertyModel<String>(structureModel, "title"), "Structure Title",true, true);
                name.setOutputMarkupId(true);
                name.getTextContainer().add(new AttributeAppender("size", new Model("10px"), ";"));
                name.setTextContainerDefaultMaxSize();
                name.getTextContainer().setRequired(true);
                if (name.isComponentMultilingual()) {
                    name.getTextContainer().add(new AttributeAppender("style", "margin-bottom:40px;"));
                }
                name.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        target.add(name);
                    }
                });
                             
                item.add(name);
                
                final AmpTextAreaFieldPanel description = new AmpTextAreaFieldPanel("description", new PropertyModel<String>(structureModel, "description"),"Structure Description",false, true, true);
                description.setOutputMarkupId(true);

                String descriptionStyle;
                if (description.isComponentMultilingual()) {
                    descriptionStyle ="margin-bottom: 55px;"; 
                }
                else {
                    descriptionStyle ="margin-bottom: 20px;";
                }
                description.getTextAreaContainer().add(new AttributeModifier("style",descriptionStyle ));
                item.add(description);      

                final AmpTextFieldPanel<String> longitude = new AmpTextFieldPanel<String>("longitude", new PropertyModel<String>(structureModel, "longitude"),"Structure Longitude", true, true);
                longitude.setOutputMarkupId(true);
                longitude.setTextContainerDefaultMaxSize();

                longitude.getTextContainer().add(new AttributeAppender("size", new Model("7px"), ";"));
                longitude.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        longitude.getTextContainer().setEnabled(!hasCoordinates(structureModel));
                        target.add(longitude);
                    }
                });
                item.add(longitude);

                final AmpTextFieldPanel<String> latitude = new AmpTextFieldPanel<String>("latitude", new PropertyModel<String>(structureModel, "latitude"),"Structure Latitude", true, true);
                latitude.setTextContainerDefaultMaxSize();
                latitude.setOutputMarkupId(true);

                latitude.getTextContainer().add(new AttributeAppender("size", new Model("7px"), ";"));
                latitude.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        latitude.getTextContainer().setEnabled(!hasCoordinates(structureModel));
                        target.add(latitude);
                    }
                });
                item.add(latitude);

                final AmpTextFieldPanel<String> shape = new AmpTextFieldPanel<String>("shape", new PropertyModel<String>(structureModel, "shape"),"Structure Shape", true, true);
                shape.setOutputMarkupId(true);                
                shape.getTextContainer().add(new AttributeAppender("size", new Model("7px"), ";"));
                shape.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {                        
                        target.add(shape);
                    }
                });
                item.add(shape);

                ListEditorRemoveButton delbutton = new ListEditorRemoveButton("deleteStructure", "Delete Structure"){

                    @Override
                    protected void onClick(AjaxRequestTarget target) {
                        target.add(containter);
                        super.onClick(target);
                        Component component = containter.get("paging");
                        if(component instanceof PagingListNavigator) {
                            PagingListNavigator paging = (PagingListNavigator)component;
                            boolean v = paging.isVisible();
                            if(!v) {
                                ((PagingListEditor)containter.get("list")).goToLastPage();
                            }
                            paging.setVisible(v);
                        }
                    }
                    
                };
                item.add(delbutton);

                final AmpAjaxLinkField viewCoords = new AmpAjaxLinkField("viewCoords", "Map", "View") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        StructureData data = new StructureData();
                        
                        AmpStructure structure = structureModel.getObject();
                        if (structure.getCoordinates() != null) {
                            List<CoordinateData> coordinates = new ArrayList<>();
                            for (AmpStructureCoordinate coord : structure.getCoordinates()) {
                                coordinates.add(new CoordinateData(coord.getLatitude(), coord.getLongitude()));
                            }
                            data.setCoordinates(coordinates);
                        }
                        data.setTitle(TranslatorWorker.translateText("Coordinates"));
                        data.setShape(Optional.ofNullable(structure.getShape()).orElse(""));
                        data.setLatitudeColName(TranslatorWorker.translateText("Latitude"));
                        data.setLongitudeColName(TranslatorWorker.translateText("Longitude"));
                        data.setSelectedShape(TranslatorWorker.translateText("Selected Shape"));
                        data.setNoData(TranslatorWorker.translateText("No Data"));
                        target.appendJavaScript("viewCoordinates('" + data.asJsonString() + "');");
                    }
                };
                item.add(viewCoords);

                final AmpAjaxLinkField openMapPopup = new AmpAjaxLinkField("openMapPopup", "Map", "Map") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        latitude.getTextContainer().setEnabled(true);
                        longitude.getTextContainer().setEnabled(true);
                        viewCoords.getButton().setEnabled(true);
                        target.add(latitude);
                        target.add(longitude);
                        target.add(viewCoords);
    
                        MapData data = new MapData();
                        
                        List<ColorData> structureColors = new ArrayList<>();
                        Collection<AmpCategoryValue> categoryValues = CategoryManagerUtil
                                .getAmpCategoryValueCollectionByKeyExcludeDeleted(
                                        CategoryConstants.GIS_STRUCTURES_COLOR_CODING_KEY);
                        for (AmpCategoryValue v : categoryValues) {
                            structureColors.add(new ColorData(v.getId(), v.getValue()));
                        }

                        data.setStructureColors(structureColors);
                        data.setStructure(getDataFromStructureModel(structureModel));

                        target.appendJavaScript("gisPopup($('#" + this.getMarkupId() + "')[0], '" + data.asJsonString()
                                + "'); return false;");
                    }
                };
                item.add(openMapPopup);

                final TextField<String> coords = new TextField<String>("coords",
                        new PropertyModel<String>(structureModel, "coords"));
                coords.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        if (coords.getDefaultModelObject() != null) {
                            StructureData data = ObjectMapperUtils.readValueFromString(
                                    coords.getDefaultModelObject().toString(), StructureData.class);
                            AmpStructure structure = structureModel.getObject();
                            structure.getCoordinates().clear();
                            for (CoordinateData coord : data.getCoordinates()) {
                                AmpStructureCoordinate ampStructureCoordinate = new AmpStructureCoordinate();
                                ampStructureCoordinate.setStructure(structure);
                                ampStructureCoordinate.setLatitude(coord.getLatitude());
                                ampStructureCoordinate.setLongitude(coord.getLongitude());
                                structure.getCoordinates().add(ampStructureCoordinate);
                            }
                            viewCoords.getButton().setEnabled(hasCoordinates(structureModel));
                            target.add(viewCoords);
                        }

                    }
                });

                coords.setOutputMarkupId(true);
                item.add(coords);
                
                final TextField<String> tempId = new TextField<String>("tempId",
                        new PropertyModel<String>(structureModel, "tempId"));    
                tempId.setOutputMarkupId(true);                
                tempId.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        target.add(tempId);
                    }
                });                
                item.add(tempId);
                
                final TextField<String> structureColorId = new TextField<String>("structureColorId",
                        new PropertyModel<String>(structureModel, "structureColorId"));
                structureColorId.setOutputMarkupId(true);
                structureColorId.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        AmpStructure structure = structureModel.getObject();
                        if (structureColorId.getDefaultModelObject() == null) {
                            structure.setStructureColor(null);
                        } else {
                            Long id = Long.parseLong(structureColorId.getDefaultModelObject().toString());
                            structure.setStructureColor(CategoryManagerUtil.getAmpCategoryValueFromDb(id));
                        }
                        target.add(structureColorId);
                    }
                });
                item.add(structureColorId);               
                
                latitude.getTextContainer().setEnabled(!hasCoordinates(structureModel));
                longitude.getTextContainer().setEnabled(!hasCoordinates(structureModel));
                viewCoords.getButton().setEnabled(hasCoordinates(structureModel));
            }
        };
        containter.add(list);

        final PagingListNavigator<AmpStructure> pln = new PagingListNavigator<AmpStructure>("paging", list);
        containter.add(pln);
        add(containter);


        AmpAjaxLinkField addbutton = new AmpAjaxLinkField("addbutton", "Add Structure", "Add Structure") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpStructure stru = new AmpStructure();
                list.addItem(stru);
                target.add(this.getParent());
                target.add(containter);
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(this.getParent()));
                list.goToLastPage();
                boolean visible = pln.isVisible();
                pln.setVisible(visible);
            }
        };
        
       addbutton.getButton().add(new AttributeModifier("class", new Model("addStructure button_green_btm")));
       add(addbutton);
        
        
    }

    public StructureData getDataFromStructureModel(IModel<AmpStructure> structureModel) {
        StructureData structureData = new StructureData();
        AmpStructure structure = structureModel.getObject();

        List<CoordinateData> coordinates = new ArrayList<>();
        if (structure.getCoordinates() != null) {
            for (AmpStructureCoordinate coord : structure.getCoordinates()) {
                coordinates.add(new CoordinateData(coord.getLatitude(), coord.getLongitude()));
            }
        }

        String colorValue = structure.getStructureColor() != null
                ? structure.getStructureColor().getValue().substring(0, COLOR_OFFSET_CATEGORY_VALUE) : null;

        structureData.setLatitude(Optional.ofNullable(structure.getLatitude()).orElse(""));
        structureData.setLongitude(Optional.ofNullable(structure.getLongitude()).orElse(""));
        structureData.setShape(Optional.ofNullable(structure.getShape()).orElse(""));
        structureData.setTitle(structure.getTitle());
        structureData.setCoordinates(coordinates);
        structureData.setColorValue(colorValue);

        return structureData;
    }

    private boolean hasCoordinates(IModel<AmpStructure> structureModel) {
        return structureModel.getObject().getCoordinates() != null && structureModel.getObject().
                getCoordinates().size() > 0;
    }
}
