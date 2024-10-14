/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Bytes;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.PagingListEditor;
import org.dgfoundation.amp.onepager.components.PagingListNavigator;
import org.dgfoundation.amp.onepager.components.features.CustomResourceLinkResourceLink;
import org.dgfoundation.amp.onepager.components.features.ExportExcelResourceReference;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.LatAndLongValidator;
import org.dgfoundation.amp.onepager.helper.structure.ColorData;
import org.dgfoundation.amp.onepager.helper.structure.CoordinateData;
import org.dgfoundation.amp.onepager.helper.structure.MapData;
import org.dgfoundation.amp.onepager.helper.structure.StructureData;
import org.digijava.kernel.ampapi.endpoints.util.ObjectMapperUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.StructuresUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

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
                longitude.getTextContainer().add(new LatAndLongValidator(-180d, 180d));

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
                latitude.getTextContainer().add(new LatAndLongValidator(-90d, 90d));
                latitude.getTextContainer().add(new AttributeAppender("size", new Model<>("7px"), ";"));
                latitude.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        latitude.getTextContainer().setEnabled(!hasCoordinates(structureModel));
                        target.add(latitude);
                    }
                });
                item.add(latitude);

                final AmpTextFieldPanel<String> shape = new AmpTextFieldPanel<String>("shape", new PropertyModel<>(structureModel, "shape"),"Structure Shape", true, true);
                shape.setOutputMarkupId(true);
                shape.getTextContainer().add(new AttributeAppender("size", new Model<>("7px"), ";"));
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


        final FileUploadField fileUploadField = new FileUploadField("fileUpload");
        fileUploadField.setOutputMarkupId(true);

        final Form<?> form = new Form<Void>("form")
        {
            private static final long serialVersionUID = 1L;
            @Override
            protected void onSubmit() {
                FileUpload upload = fileUploadField.getFileUpload();
                if (upload == null) {
                    logger.info("No file uploaded");
                } else {
                    logger.info("File-Name: " + upload.getClientFileName() + " File-Size: " +
                            Bytes.bytes(upload.getSize()));
                    try {
                        XSSFWorkbook workbook = new XSSFWorkbook(upload.getInputStream());
                        XSSFSheet sheet = workbook.getSheetAt(0);
                        Iterator<Row> rowIterator = sheet.iterator();
                        rowIterator.next();

                        while (rowIterator.hasNext()) {
                            XSSFRow row = (XSSFRow) rowIterator.next();
                            String title = getStringValueFromCell(row.getCell(0));
                            String description = getStringValueFromCell(row.getCell(1));
                            String latitude = getStringValueFromCell(row.getCell(2));
                            String longitude = getStringValueFromCell(row.getCell(3));

                            AmpStructure stru = new AmpStructure();
                            stru.setTitle(title);
                            stru.setDescription(description);
                            stru.setLatitude(latitude);
                            stru.setLongitude(longitude);
                            list.addItem(stru);
                            list.goToLastPage();
                        }


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        };
        form.setMultiPart(true);
        form.setMaxSize(Bytes.megabytes(10));
        form.add(fileUploadField);


        Button importStructures = new Button("importStructures");
//        importStructures.add(new AttributeModifier("disabled", "true"));
        importStructures.setOutputMarkupId(true);
        form.add(importStructures);
        fileUploadField.add(new AjaxFormComponentUpdatingBehavior("change") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                importStructures.setEnabled(true);
                importStructures.add(new AttributeModifier("disabled", (String) null)); // Enable the button
                target.add(importStructures);

            }
        });

        add(form);


        ResourceReference resourceReference = new ResourceReference("exportData-"+ System.currentTimeMillis()) {
            @Override
            public IResource getResource() {
                return new AbstractResource() {
                    @Override
                    protected ResourceResponse newResourceResponse(Attributes attributes) {
                        ResourceResponse response = new ResourceResponse();
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.setFileName("exported-structures-activity-"+am.getObject().getAmpId()+".xlsx");
                        response.disableCaching();
                        response.setWriteCallback(new WriteCallback() {
                            @Override
                            public void writeData(Attributes attributes) {
                                try (OutputStream out = attributes.getResponse().getOutputStream()) {
                                    writeExcelFile(out, list);
                                } catch (IOException e) {
                                    logger.error("Error writing data to file", e);
                                }
                            }
                        });
                        return response;
                    }
                };
            }
        };
        ResourceLink<Void> downloadLink = new ResourceLink<>("downloadLink", resourceReference);
        downloadLink.setOutputMarkupId(true);
        add(downloadLink);

        AmpAjaxLinkField exportStructures = new AmpAjaxLinkField("exportStructures", "Export Structures", "Export Structures") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                logger.info("Preparing to download data");
                String downloadLinkMarkupId = downloadLink.getMarkupId();
                target.add(list.getParent());
                target.appendJavaScript("document.getElementById('" + downloadLinkMarkupId + "').click();");
            }
        };
        add(exportStructures);

        exportStructures.getButton().add(new AttributeModifier("class", new Model("exportStructures button_blue_btm")));
        add(exportStructures);


    }
    private static String getStringValueFromCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    private void writeExcelFile(OutputStream out,PagingListEditor<AmpStructure> list) throws IOException {
        try(XSSFWorkbook workbook = new XSSFWorkbook()) {
            // Create Excel sheet
            XSSFSheet sheet = workbook.createSheet("Structures");

            // Create header row
            XSSFRow headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Title");
            headerRow.createCell(1).setCellValue("Description");
            headerRow.createCell(2).setCellValue("Latitude");
            headerRow.createCell(3).setCellValue("Longitude");

            int rowIndex = 1;
            for (Component child : list) {
                if (child instanceof ListItem) {
                    ListItem<AmpStructure> listItem = (ListItem<AmpStructure>) child;
                    AmpStructure structure = listItem.getModelObject();

                    // Create a new row for each structure
                    XSSFRow row = sheet.createRow(rowIndex);
                    if (structure != null) {
                        createCellIfNotNull(row, 0, structure.getTitle());
                        createCellIfNotNull(row, 1, structure.getDescription());
                        createCellIfNotNull(row, 2, structure.getLatitude());
                        createCellIfNotNull(row, 3, structure.getLongitude());
                    }
                    rowIndex++;
                }
            }

            // Write workbook content to the output stream
            workbook.write(out);

            // Respond with the written content
        } catch (IOException e) {
            logger.error("Error exporting data to Excel", e);
        }
    }

    private void createCellIfNotNull(XSSFRow row, int columnIndex, Object value) {
        if (value != null) {
            row.createCell(columnIndex).setCellValue(value.toString());
        }
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
        String gsLat = "7.1881";
        String gsLong = "21.0938";
        String defaultShape = "";
        String colorValue = structure.getStructureColor() != null
                ? structure.getStructureColor().getValue().substring(0, COLOR_OFFSET_CATEGORY_VALUE) : null;
        if (structure.getLatitude() != null && structure.getLongitude() != null && structure.getShape() == null) {
            defaultShape = "Point";
        }
        structureData.setLatitude(Optional.ofNullable(structure.getLatitude()).orElse(gsLat));
        structureData.setLongitude(Optional.ofNullable(structure.getLongitude()).orElse(gsLong));
        structureData.setShape(Optional.ofNullable(structure.getShape()).orElse(defaultShape));
        structureData.setTitle(structure.getTitle());
        structureData.setCoordinates(coordinates);
        structureData.setColorValue(colorValue);

        return structureData;
    }

    private boolean hasCoordinates(IModel<AmpStructure> structureModel) {
        return structureModel.getObject().getCoordinates() != null && structureModel.getObject().
                getCoordinates().size() > 0;
    }

    private void handleFileUpload(final FileUpload uploadedFile) throws IOException {
        // Write the uploaded file to a temporary location
        File tempFile = new File("temp_" + uploadedFile.getClientFileName());
        uploadedFile.writeTo(tempFile);

        try (InputStream inputStream = new FileInputStream(tempFile)) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            // ...
        } catch (IOException e) {
            // Handle exception
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        } finally {
            // Delete the temporary file
            tempFile.delete();
        }
    }
}


