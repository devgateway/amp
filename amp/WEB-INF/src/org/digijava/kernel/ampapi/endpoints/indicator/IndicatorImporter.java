/**
 *
 */
package org.digijava.kernel.ampapi.endpoints.indicator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author apicca
 */
public class IndicatorImporter {

    private static final Logger LOGGER = Logger.getLogger(IndicatorImporter.class);
    private ApiEMGroup errors = new ApiEMGroup();

    public ApiEMGroup getApiErrors() {
        return errors;
    }

    public enum Option {
        OVERWRITE, NEW
    }

    /**
     * Return the errorc
     *
     * @return returns
     * @throws org.digijava.module.aim.exception.AimException
     */
    public Collection<JsonBean> processExcelFile(InputStream inputStream) {
        POIFSFileSystem fsFileSystem = null;
        Collection<JsonBean> locationIndicatorValueList = new ArrayList<JsonBean>();
        Set<String> geoIdsWithProblems = new HashSet<String>();
        try {
            fsFileSystem = new POIFSFileSystem(inputStream);
            HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);

            HSSFSheet hssfSheet = workBook.getSheetAt(0);
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
                    if (CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.equalsCategoryValue(admLevelValue)) {
                        isCountryLevel = true;
                    }
                    break;
                }
            }

            if (selectedAdmLevel == null) {
                errors.addApiErrorMessage(IndicatorErrors.INEXISTANT_ADM_LEVEL, IndicatorEPConstants.ADM_LEVEL_ID + admLevel);
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

                    JsonBean result = new JsonBean();
                    result.set(IndicatorEPConstants.VALUE, Double.valueOf(value));
                    result.set(IndicatorEPConstants.ID, locationObject.getId());
                    result.set(IndicatorEPConstants.GEO_CODE_ID, locationObject.getGeoCode());
                    result.set(IndicatorEPConstants.NAME, locationObject.getName());
                    locationIndicatorValueList.add(result);
                }
            }

        } catch (NullPointerException e) {
            errors.addApiErrorMessage(IndicatorErrors.UNKNOWN_ERROR, " Cannot import indicator values ");
        } catch (IllegalStateException e) {
            errors.addApiErrorMessage(IndicatorErrors.INCORRECT_CONTENT, " File is not ok ");
        } catch (IOException e) {
            errors.addApiErrorMessage(IndicatorErrors.INCORRECT_CONTENT, " File is not ok ");
        } catch (Exception e) {
            errors.addApiErrorMessage(IndicatorErrors.UNKNOWN_ERROR, " Cannot import indicator values ");
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

    public static JsonBean importIndicator(InputStream uploadedInputStream) {
        JsonBean result = new JsonBean();
        byte[] fileData = new byte[0];
        try {
            fileData = org.apache.commons.io.IOUtils.toByteArray(uploadedInputStream);
        } catch (IOException e) {
            throw new WebApplicationException(e);
        }

        InputStream inputStream = new ByteArrayInputStream(fileData);

        IndicatorImporter importer = new IndicatorImporter();
        Collection<JsonBean> locationIndicatorValueList = importer.processExcelFile(inputStream);

        if (!importer.getApiErrors().isEmpty()) {
            EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_BAD_REQUEST);
            return ApiError.toError(importer.getApiErrors().getAllErrors());
        }

        result.set(IndicatorEPConstants.VALUES, locationIndicatorValueList);
        return result;
    }

}
