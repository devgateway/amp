/**
 *
 */
package org.digijava.kernel.ampapi.endpoints.indicator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.GenericErrors;
import org.digijava.kernel.ampapi.endpoints.exception.AmpWebApplicationException;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author apicca
 */
public class IndicatorImporter {

    private static final Logger LOGGER = Logger.getLogger(IndicatorImporter.class);
    // AMP-SEC-006/077: cap upload size before it is fully buffered/parsed to avoid unbounded memory use
    private static final long MAX_UPLOAD_SIZE_BYTES = 100L * 1024 * 1024;
    private ApiEMGroup errors = new ApiEMGroup();

    public ApiEMGroup getApiErrors() {
        return errors;
    }

    /**
     * Return the errorc
     *
     * @return returns
     * @throws org.digijava.module.aim.exception.AimException
     */
    public List<LocationIndicatorValueResult> processExcelFile(InputStream inputStream, long admLevelId) {
        List<LocationIndicatorValueResult> locationIndicatorValueList = new ArrayList<>();
        Set<String> geoIdsWithProblems = new HashSet<String>();
        try (Workbook workBook = WorkbookFactory.create(inputStream)) {
            Sheet hssfSheet = workBook.getSheetAt(0);
            Row hssfRow = hssfSheet.getRow(0);
            Cell admLevelCell = hssfRow.getCell(0);
            String admLevel = "";
            AmpCategoryValue selectedAdmLevel = null;
            if (admLevelCell != null) {
                admLevel = admLevelCell.getStringCellValue();
            }
            List<AmpCategoryValue> implLocs = new ArrayList<AmpCategoryValue>(
                    CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.IMPLEMENTATION_LOCATION_KEY));

            boolean isCountryLevel = false;
            for (AmpCategoryValue admLevelValue : implLocs) {
                if (admLevel.equalsIgnoreCase(admLevelValue.getValue()) && admLevelValue.isVisible()) {
                    selectedAdmLevel = admLevelValue;
                    if (CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.equalsCategoryValue(admLevelValue)) {
                        isCountryLevel = true;
                        String  defCountry = DynLocationManagerUtil.getDefCountryIso();

                        if (defCountry.equalsIgnoreCase("zz")) {
                        isCountryLevel =false;
                        }
                    }
                    break;
                }
            }

            if (selectedAdmLevel == null) {
                errors.addApiErrorMessage(IndicatorErrors.INEXISTANT_ADM_LEVEL, IndicatorEPConstants.ADM_LEVEL_ID + admLevel);
            } else {
                if (selectedAdmLevel.getId() != admLevelId) {
                    errors.addApiErrorMessage(IndicatorErrors.INVALID_IMPORT_INVALID_ADMIN_LEVEL, IndicatorEPConstants.ADM_LEVEL_ID + admLevel);
                }
            }

            int physicalNumberOfCells = hssfRow.getPhysicalNumberOfCells();
            int indicatorNumberOfCells = 1;
            if (indicatorNumberOfCells + 2 < physicalNumberOfCells) {
                errors.addApiErrorMessage(IndicatorErrors.NUMBER_NOT_MATCH, " physical number of cells not match ");
            }

            for (int j = 1; j < hssfSheet.getPhysicalNumberOfRows(); j++) {
                hssfRow = hssfSheet.getRow(j);
                if (hssfRow != null) {
                    AmpCategoryValueLocations locationObject = null;
                    String id = null;

                    if (!isCountryLevel) {
                        Cell idCell = hssfRow.getCell(1);
                        if (idCell != null) {
                            id = getValue(idCell);
                            //some versions of excel converts to numeric and adds a .0 at the end
                            if (StringUtils.isNotEmpty(id) && !".0".equals(id)) {
                                id = id.replace(".0", "");
                                locationObject = DynLocationManagerUtil.getLocationById(Long.parseLong(id), selectedAdmLevel);
                                if (locationObject == null) {
                                    geoIdsWithProblems.add(id);
                                    continue;
                                }
                            } else {
                                continue;
                            }
                        }
                    }

                    Cell cell = hssfRow.getCell(2);
                    String value = getValue(cell);

                    if (isCountryLevel) {
                        locationObject = DynLocationManagerUtil.getDefaultCountry();
                    }

                    if(value == null) {
                        LOGGER.info(" Missing value for " + locationObject.getName());
                        continue;
                    }

                    LocationIndicatorValueResult locationIndicatorValue = new LocationIndicatorValueResult();
                    locationIndicatorValue.setValue(Double.valueOf(value));
                    locationIndicatorValue.setId(locationObject.getId());
                    locationIndicatorValue.setGeoCodeId(locationObject.getGeoCode());
                    locationIndicatorValue.setName(locationObject.getName());
                    locationIndicatorValueList.add(locationIndicatorValue);
                }
            }

        } catch (NullPointerException e) {
            errors.addApiErrorMessage(GenericErrors.UNKNOWN_ERROR, " Cannot import indicator values ");
        } catch (InvalidFormatException e) {
            errors.addApiErrorMessage(IndicatorErrors.INVALID_FILE_TYPE, " File is not a valid Excel (.xls/.xlsx) file ");
        } catch (IllegalStateException e) {
            errors.addApiErrorMessage(IndicatorErrors.INCORRECT_CONTENT, " File is not ok ");
        } catch (IOException e) {
            errors.addApiErrorMessage(IndicatorErrors.INCORRECT_CONTENT, " File is not ok ");
        } catch (Exception e) {
            errors.addApiErrorMessage(GenericErrors.UNKNOWN_ERROR, " Cannot import indicator values ");
        }
        if (geoIdsWithProblems.size() > 0) {
            errors.addApiErrorMessage(IndicatorErrors.LOCATION_NOT_FOUND, geoIdsWithProblems.toString());
        }
        if(locationIndicatorValueList.isEmpty()) {
            errors.addApiErrorMessage(IndicatorErrors.INVALID_IMPORT_NO_VALUE, null);
        }

        return locationIndicatorValueList;
    }

    private String getValue(Cell cell) {
        String value = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    value = (cell.getStringCellValue() != null && cell.getStringCellValue().trim().equals("")) ? null : cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    value = "" + cell.getNumericCellValue();
                    break;
            }
        }
        return value;
    }

    public static IndicatorImporterResult importIndicator(InputStream uploadedInputStream, long admLevelId) {

        byte[] fileData;
        try {
            fileData = org.apache.commons.io.IOUtils.toByteArray(
                    new org.apache.commons.io.input.BoundedInputStream(uploadedInputStream, MAX_UPLOAD_SIZE_BYTES + 1));
        } catch (IOException e) {
            throw new WebApplicationException(e);
        }

        if (fileData.length > MAX_UPLOAD_SIZE_BYTES) {
            throw new AmpWebApplicationException(Response.Status.BAD_REQUEST,
                    ApiError.toError(IndicatorErrors.FILE_TOO_LARGE));
        }

        InputStream inputStream = new ByteArrayInputStream(fileData);

        IndicatorImporter importer = new IndicatorImporter();
        List<LocationIndicatorValueResult> locationIndValueList = importer.processExcelFile(inputStream, admLevelId);

        if (!importer.getApiErrors().isEmpty()) {
            throw new AmpWebApplicationException(Response.Status.BAD_REQUEST,
                    ApiError.toError(importer.getApiErrors().getAllErrors()));
        }

        return new IndicatorImporterResult(locationIndValueList);
    }

}
