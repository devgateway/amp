package org.digijava.kernel.ampapi.endpoints.postgis;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.codehaus.jackson.node.DoubleNode;
import org.codehaus.jackson.node.TextNode;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.helpers.geojson.FeatureGeoJSON;
import org.digijava.kernel.ampapi.helpers.geojson.PointGeoJSON;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ColorRampUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class PostgisUtil {
	protected static final Logger logger = Logger.getLogger(PostgisUtil.class);

	public static AmpIndicatorAccessType getAmpIndicatorAccessTypeFromDb(Long valueId) {
        if (valueId == null)
            return null;
        try
        {
            Session dbSession = PersistenceManager.getSession();
            AmpIndicatorAccessType retVal = (AmpIndicatorAccessType) dbSession.get(AmpIndicatorAccessType.class, valueId);
            return retVal;
        }
        catch(Exception e)
        {
            return null;
        }
	}

    public static List <AmpIndicatorAccessType> getAmpIndicatorAccessTypes() {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select indAccessType from "
                + AmpIndicatorAccessType.class.getName() + " indAccessType";
        Query qry = dbSession.createQuery(queryString);
        return qry.list();
    }

    public static JsonBean buildIndicatorLayerJson(AmpIndicatorLayer indicator) {
        JsonBean indicatorJson = new JsonBean();

        indicatorJson.set("id", indicator.getId());
        indicatorJson.set("name", indicator.getName());
        indicatorJson.set("description", indicator.getDescription());
        indicatorJson.set("numberOfClasses", indicator.getNumberOfClasses());
        indicatorJson.set("unit", indicator.getUnit());
        indicatorJson.set("admLevelId", indicator.getAdmLevel().getId());
        indicatorJson.set("accessTypeId", indicator.getAccessType().getId());

        if (indicator.getColorRamp()!=null) {
            String[] colors = new String[indicator.getColorRamp().size()];
            int i=0;
            for (AmpIndicatorColor indicatorColor: indicator.getColorRamp()){
                if (indicatorColor.getPayload() == 1) {
                    long colorId = ColorRampUtil.getColorId(indicatorColor.getColor());
                    indicatorJson.set("colorRampId", colorId);
                }
                colors[i++] = indicatorColor.getColor();
            }
            indicatorJson.set("colorRamp", colors);
        }

        return indicatorJson;
    }

    public static AmpIndicatorLayer setIndicatorLayer(JsonBean indicator,HttpServletRequest httpRequest) {
        AmpIndicatorLayer indicatorLayer = new AmpIndicatorLayer();

        if (indicator.get("id")!=null) {
            indicatorLayer.setId(Long.valueOf(String.valueOf(indicator.get("id"))));
        } else{
            indicatorLayer.setId(null);
        }

        indicatorLayer.setName(String.valueOf(indicator.get("name")));
        indicatorLayer.setDescription(String.valueOf(indicator.get("description")));
        indicatorLayer.setNumberOfClasses(Long.valueOf(String.valueOf(indicator.get("numberOfClasses"))));
        indicatorLayer.setUnit(String.valueOf(indicator.get("unit")));
        indicatorLayer.setCreatedOn(new Date());
        TeamMember tm = (TeamMember) httpRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
        if (tm != null) {
            AmpTeamMember ampTeamMember = TeamUtil.getAmpTeamMember(tm.getMemberId());
            if (ampTeamMember != null) {
                indicatorLayer.setCreatedBy(ampTeamMember);
            }
        }

        indicatorLayer.setAccessType(PostgisUtil.getAmpIndicatorAccessTypeFromDb(Long.valueOf(String.valueOf(indicator.get("accessTypeId")))));
        indicatorLayer.setAdmLevel(CategoryManagerUtil.getAmpCategoryValueFromDb(Long.valueOf(String.valueOf(indicator.get("admLevelId")))));

        if (indicator.get("colorRamp")!=null) {
            Set<AmpIndicatorColor> colorRamp = new HashSet<AmpIndicatorColor>();
            String[] colorRampColors = ColorRampUtil.getColorRamp(Integer.valueOf(String.valueOf(indicator.get("colorRamp"))),
                    indicatorLayer.getNumberOfClasses());
            for (int i = 0; i < colorRampColors.length; i++) {
                AmpIndicatorColor color = new AmpIndicatorColor();
                int payload = i + 1;
                color.setPayload((long) payload);
                color.setColor(colorRampColors[i]);
                colorRamp.add(color);
            }
            indicatorLayer.setColorRamp(colorRamp);
        }

        return indicatorLayer;
    }

    public static JsonBean getIndicatorColor(AmpIndicatorColor indicatorColor) {
        JsonBean color = new JsonBean();
        color.set("color", indicatorColor.getColor());
        color.set("indicatorColorId", indicatorColor.getIndicatorColorId());
        color.set("payload", indicatorColor.getPayload());
        return color;
    }

    public static JsonBean getList(Collection<JsonBean> indicatorLayerList, Integer offset, Integer count) {
        int start = 0;
        if (count==null) count = 10;
        if (offset==null) offset = count;
        int end = indicatorLayerList.size();
        if (offset != null && count != null && offset < indicatorLayerList.size()) {
            start = offset.intValue();
            if (indicatorLayerList.size() > (offset + count)) {
                end = offset + count;
            }
        }
        JsonBean result = new JsonBean();
        JsonBean page = new JsonBean();

        Collection<JsonBean> col = new ArrayList(indicatorLayerList).subList(start, end);
        page.set("recordsPerPage",count);
        page.set("currentPageNumber", offset / count);
        page.set("totalPageCount",col.size());
        page.set("totalRecords",indicatorLayerList.size());

        result.set("page",page);
        result.set("data",col);

        return result;
    }

    public static void populateIndicatorLayerTableValues(HSSFSheet sheet,int rowIndex, AmpCategoryValue categoryValue) {
        Set<AmpCategoryValueLocations> locations = DynLocationManagerUtil
                .getLocationsByLayer(categoryValue);
        for (AmpCategoryValueLocations location : locations) {
            int cellIndex = 0;
            HSSFRow row = sheet.createRow(rowIndex++);
            HSSFCell cell = row.createCell(cellIndex++);
            cell.setCellValue(location.getName());
            cell = row.createCell(cellIndex++);
            cell.setCellValue(location.getGeoCode());
            List <AmpLocationIndicatorValue> values = DynLocationManagerUtil.getLocationIndicatorValueByLocation(location);
            for (AmpLocationIndicatorValue value:values) {
                cell = row.createCell(cellIndex++);
                cell.setCellValue(value.getValue());
            }

        }

    }

    public static FeatureGeoJSON getLocation(Double lat, Double lon, String name, double score) {
        FeatureGeoJSON fgj = new FeatureGeoJSON();
        PointGeoJSON pg = new PointGeoJSON();
        pg.coordinates.add(lon);
        pg.coordinates.add(lat);
        fgj.properties.put("name", new TextNode(name));
        fgj.properties.put("score", new DoubleNode(score));
        fgj.geometry = pg;
        return fgj;
    }
}