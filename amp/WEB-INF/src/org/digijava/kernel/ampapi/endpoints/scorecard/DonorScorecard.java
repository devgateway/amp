package org.digijava.kernel.ampapi.endpoints.scorecard;

import static javax.servlet.http.HttpServletResponse.SC_OK;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Quarter;
import org.digijava.kernel.ampapi.endpoints.scorecard.service.ScorecardExcelExporter;
import org.digijava.kernel.ampapi.endpoints.scorecard.service.ScorecardNoUpdateDonor;
import org.digijava.kernel.ampapi.endpoints.scorecard.service.ScorecardService;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.module.aim.dbentity.AmpScorecardOrganisation;
import org.digijava.module.aim.dbentity.AmpScorecardSettings;
import org.digijava.module.aim.dbentity.AmpScorecardSettingsCategoryValue;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * This class should have all endpoints related to the Donor Scorecard -
 * AMP-20002
 * 
 * @author Emanuel Perez
 * 
 */

@Path("scorecard")
@Api("scorecard")
public class DonorScorecard {

    @GET
    @Path("/export")
    @Produces("application/vnd.ms-excel")
    @ApiMethod(ui = false, id = "DonorScorecar")
    @ApiOperation(
            value = "Retrieve an excel file with all the quarters, desired period, donors and the updated projects.",
            notes = "Creates an excel workbook having the headers with all the Quarters spanning the desired period, "
                    + "the donors as columns and each cell (for a given donor and quarter) painted with a color "
                    + "depending on the number of updated projects for a given donor on a quarter.")
    public StreamingOutput getDonorScorecard(@Context HttpServletResponse webResponse) {

        webResponse.setHeader("Content-Disposition", "attachment; filename=donorScorecard.xls");

        StreamingOutput streamOutput = new StreamingOutput() {
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    ScorecardService service = new ScorecardService ();
                    List<Quarter> quarters = service.getQuarters ();
                    ScorecardExcelExporter exporter = new ScorecardExcelExporter();
                    HSSFWorkbook wb = exporter.generateExcel(service.getFilteredDonors(), quarters,
                            service.getOrderedScorecardCells(service.getDonorActivityUpdates()));
                    wb.write(output);
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };
        return streamOutput;

    }
    
    @GET
    @Path("/quickStats")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Retrieve a quick view of the audit logger.",
            notes = "<dl>\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>oranizations</b><dd> - the count of active organisations for the past quarter\n"
                    + "<dt><b>projects</b><dd> - the count of projects with action in the past quarter\n"
                    + "<dt><b>users</b><dd> - the count of users logged into the System in the past quarter\n"
                    + "</dl></br></br>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "  \"organizations\": 52,\n"
                    + "  \"projects\": 181,\n"
                    + "  \"users\": 23\n"
                    + "}</pre>")
    public JsonBean getPastQuarterOrganizationsCount() {
        ScorecardService scorecardService = new ScorecardService();
        
        JsonBean jsonBean = new JsonBean();
        jsonBean.set("organizations", scorecardService.getPastQuarterOrganizationsCount());
        jsonBean.set("projects", scorecardService.getPastQuarterProjectsCount());
        jsonBean.set("users", scorecardService.getPastQuarterUsersCount());
        
        return jsonBean;
    }
    
    @POST
    @Path("/manager/settings")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Save the donor scorecard settings.",
            notes = "<h3>Sample Input:</h3><pre>\n"
                    + "{\n"
                    + "   \"validationPeriod\": true,\n"
                    + "   \"percentageThreshold\": 10,\n"
                    + "   \"validationTime\": 5,\n"
                    + "   \"categoryValues\": [{\n"
                    + "      \"id\": 1\n"
                    + "   }]\n"
                    + "}</pre>")
    @ApiResponses(@ApiResponse(code = SC_OK, message = "empty or a string with the error"))
    public String saveScorecardSettings(final JSONObject settingsBean) {
        String message = null;
        List<AmpScorecardSettings> scorecardSettingsList = (List<AmpScorecardSettings>) DbUtil.getAll(AmpScorecardSettings.class);
        List<AmpCategoryValue> allCategoryValues = (List<AmpCategoryValue>) CategoryManagerUtil.
                getAmpCategoryValueCollectionByKey("activity_status");
        
        AmpScorecardSettings settings = scorecardSettingsList.isEmpty() ? new AmpScorecardSettings() : scorecardSettingsList.get(0);
        String validationTime = settingsBean.getString("validationTime");
        
        settings.setValidationPeriod(Boolean.parseBoolean(settingsBean.getString("validationPeriod")));
        settings.setPercentageThreshold(Double.parseDouble(settingsBean.getString("percentageThreshold")));
        if (StringUtils.isNotBlank(validationTime) && !"0".equalsIgnoreCase(validationTime)) {
            settings.setValidationTime(Integer.parseInt(validationTime));
        }
        
        JSONArray categoryValues = settingsBean.getJSONArray("categoryValues");
        
        Set <AmpScorecardSettingsCategoryValue> closedStatuses = new HashSet<AmpScorecardSettingsCategoryValue>();
        
        if (categoryValues != null) {
            Set<String> selectedValesSet = new HashSet<String>();
            for (int i=0; i < categoryValues.size(); i++) {
                selectedValesSet.add(categoryValues.getJSONObject(i).getString("id"));
            }
            
            for (AmpCategoryValue categoryValue : allCategoryValues) {
                if (selectedValesSet.contains(Long.toString(categoryValue.getId()))) {
                    AmpScorecardSettingsCategoryValue scSettingsCategoryValue = new AmpScorecardSettingsCategoryValue();
                    scSettingsCategoryValue.setAmpCategoryValueStatus(categoryValue);
                    scSettingsCategoryValue.setAmpScorecardSettings(settings);
                    closedStatuses.add(scSettingsCategoryValue);
                }
            }
        }   
        
        
        settings.getClosedStatuses().clear();
        settings.getClosedStatuses().addAll(closedStatuses);

        try {
            DbUtil.saveOrUpdateObject(settings);
        } catch (Exception e) {
            message = e.getLocalizedMessage();
        }
        
        return message;
    }
    
    @POST
    @Path("/manager/donors/noupdates")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Save the noUpdate donors.",
            notes = "<dl>\n"
                    + "Used in Scorecard Manager (admin section), receive a list of donors that are not to be "
                    + "excluded in the scorecard.\n"
                    + "</dl></br></br>\n"
                    + "</br>\n"
                    + "<h3>Sample Input:</h3><pre>\n"
                    + "{\n"
                    + "  \"donorsNoUpdates\": [26]\n"
                    + "}</pre>")
    @ApiResponses(@ApiResponse(code = SC_OK, message = "empty or a string with the error"))
    public String getDonorsNoUpdates(@ApiParam("list of donors") final JSONObject donorsBean) {
        String message = null;
        
        JSONArray donorsArray = donorsBean.getJSONArray("donorsNoUpdates");
        String[] selectedDonorsValues = new String[donorsArray.size()];
        for (int i=0; i < donorsArray.size(); i++) {
            selectedDonorsValues[i] = donorsArray.get(i).toString();
        }
        
        DbUtil.deleteAllNoUpdateOrgs(false);
        
        if (selectedDonorsValues.length > 0) {
            for (int i=0; i<selectedDonorsValues.length; i++) {
                AmpScorecardOrganisation org = new AmpScorecardOrganisation();
                org.setAmpDonorId(Long.parseLong(selectedDonorsValues[i]));
                org.setModifyDate(new Date());
                org.setToExclude(false);
                try {
                    DbUtil.saveOrUpdateObject(org);
                } catch (Exception e) {
                    message = e.getLocalizedMessage();
                }
            }
        }
        
        return message;
    }

    @POST
    @Path("/manager/donors/filtered")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Retrieve and provide a list of the filtered donors.",
            notes = "<dl>\n"
                    + "Used in Scorecard Manager (admin section), receive the list of selected donors in the "
                    + "scorecard manager that are to be excluded in the scorecard.\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>allFilteredDonors</b><dd> - the list of the filtered donors\n"
                    + "<dt><b>noUpdatesFilteredDonors</b><dd> - the list of noupdate donors\n"
                    + "</dl></br></br>\n"
                    + "</br>\n"
                    + "<h3>Sample Input:</h3><pre>\n"
                    + "{\n"
                    + "   \"donorIds\": [671]\n"
                    + "}</pre>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "  \"allFilteredDonors\": [\n"
                    + "    {\n"
                    + "      \"id\": 22,\n"
                    + "      \"name\": \"Irish Aid\"\n"
                    + "    },\n"
                    + "    {\n"
                    + "      \"id\": 1640,\n"
                    + "      \"name\": \"Anti-Crime Capacity Building Program\"\n"
                    + "    },\n"
                    + " ...\n"
                    + "  ],\n"
                    + "  \"noUpdatesFilteredDonors\": [\n"
                    + "    {\n"
                    + "      \"id\": 26,\n"
                    + "      \"name\": \"Irish Aid\"\n"
                    + "    }\n"
                    + "  ]\n"
                    + "}</pre>")
    public JsonBean getFilteredDonors(@ApiParam("list of donors") final JSONObject donorsBean) {
        ScorecardService scorecardService = new ScorecardService();
        String message = null;
        
        // delete all excluded organizations from the amp_scorecard_organisation table having to_exclude = true
        DbUtil.deleteAllNoUpdateOrgs(true);
        
        JSONArray donorsArray = (JSONArray) donorsBean.get("donorIds");
        Set<Long> donorIds = new HashSet<Long>();
        for (int i=0; i < donorsArray.size(); i++) {
            Long donorId = Long.parseLong(donorsArray.getString(i));
            AmpScorecardOrganisation org = new AmpScorecardOrganisation();
            org.setAmpDonorId(donorId);
            org.setModifyDate(new Date());
            org.setToExclude(true);
            
            try {
                DbUtil.saveOrUpdateObject(org);
            } catch (Exception e) {
                // todo think of the exception type we're throwing here
                throw new RuntimeException("Failed to load donor list");
            }
            
            donorIds.add(donorId);
        }
        
        List<ScorecardNoUpdateDonor> allDonors = scorecardService.getScorecardDonors(true);
        List<JsonBean> allFilteredDonors = new ArrayList<JsonBean>();
        
        for (ScorecardNoUpdateDonor donor : allDonors) {
            if (!donorIds.contains(donor.getAmpDonorId())) {
                JsonBean jsonDonor = new JsonBean();
                jsonDonor.set("id", donor.getAmpDonorId());
                jsonDonor.set("name",  donor.getName());
                allFilteredDonors.add(jsonDonor);
            }
        }
        
        List<JsonBean> noUpdatesFilteredDonors = new ArrayList<JsonBean>();
        List<ScorecardNoUpdateDonor> noUpdatedDonors = scorecardService.getScorecardDonors(false);
        
        for (ScorecardNoUpdateDonor noUpdateDonor: noUpdatedDonors) {
            if (!donorIds.contains(noUpdateDonor.getAmpDonorId())) {
                JsonBean jsonDonor = new JsonBean();
                jsonDonor.set("id", noUpdateDonor.getAmpDonorId());
                jsonDonor.set("name",  noUpdateDonor.getName());
                noUpdatesFilteredDonors.add(jsonDonor);
            }
        }
        
        JsonBean jsonBean = new JsonBean();
        jsonBean.set("allFilteredDonors", allFilteredDonors);
        jsonBean.set("noUpdatesFilteredDonors", noUpdatesFilteredDonors);
        
        return jsonBean;
    }

    @GET
    @Path("/manager/donors")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Retrieve and provide a list of all the donors.",
            notes = "<dl>\n"
                    + "Used for filter tree in Donor Scorecard Manager.\n"
                    + "The JSON object holds information regarding:\n"
                    + "<dt><b>key</b><dd> - the key of the donor\n"
                    + "<dt><b>title</b><dd> - the title of the donors\n"
                    + "<dt><b>folder</b><dd> - true|false\n"
                    + "<dt><b>children</b><dd> - array or childres with the same structure than the donors JSON\n"
                    + "</dl></br></br>\n"
                    + "</br>\n"
                    + "<h3>Sample Output:</h3><pre>\n"
                    + "{\n"
                    + "  \"title\": \"Donors\",\n"
                    + "  \"children\": [\n"
                    + "    {\n"
                    + "      \"key\": 1,\n"
                    + "      \"title\": \"Government of Timor-Leste\",\n"
                    + "      \"folder\": true,\n"
                    + "      \"children\": [\n"
                    + "        {\n"
                    + "          \"key\": 70,\n"
                    + "          \"title\": \"RDTL Line Ministry\",\n"
                    + "          \"folder\": true,\n"
                    + "          \"children\": [\n"
                    + "            {\n"
                    + "              \"key\": 153,\n"
                    + "              \"title\": \"Ministry of Health\",\n"
                    + "              \"folder\": false,\n"
                    + "              \"selected\": true\n"
                    + "            }\n"
                    + "          ]\n"
                    + "        },\n"
                    + " ......\n"
                    + "    }\n"
                    + "   ]\n"
                    + " }</pre>")
    public JsonBean getAllDonors() {
        
        List<JsonBean> orgs = QueryUtil.getDonors(false);
        List<JsonBean> orgTypes = QueryUtil.getOrgTypes();
        List<JsonBean> orgGroups = QueryUtil.getOrgGroups();
        
        List<JsonBean> donorsTree = new ArrayList<JsonBean>();
        for (JsonBean orgType : orgTypes) {
            JsonBean currentType = new JsonBean();
            currentType.set("key", orgType.get("id"));
            currentType.set("title", orgType.get("name"));
            currentType.set("folder", true);
            
            Set<Long> orgGrpIds = new HashSet<Long>((ArrayList<Long>) orgType.get("groupIds"));
            List<JsonBean> typeChildren = new ArrayList<JsonBean>();
            for (JsonBean orgGroup : orgGroups) {
                JsonBean currentGroup = new JsonBean();
                if (orgGrpIds.contains((Long) orgGroup.get("id"))) {
                    currentGroup.set("key", orgGroup.get("id"));
                    currentGroup.set("title",  orgGroup.get("name"));
                    currentGroup.set("folder", true);
                    
                    List<JsonBean> groupChildren = new ArrayList<JsonBean>();
                    Set<Long> orgIds = new HashSet<Long>((ArrayList<Long>) orgGroup.get("orgIds"));
                    for (JsonBean org : orgs) {
                        JsonBean currentOrg = new JsonBean();
                        if (orgIds.contains((Long) org.get("id"))) {
                            currentOrg.set("key", org.get("id"));
                            currentOrg.set("title",  org.get("name"));
                            currentOrg.set("folder", false);
                            currentOrg.set("selected", true);
                            groupChildren.add(currentOrg);
                        }
                    }
                    currentGroup.set("children", groupChildren);
                    typeChildren.add(currentGroup);
                }
            }
            
            currentType.set("children", typeChildren);
            donorsTree.add(currentType);
        }
        
        JsonBean jsonBean = new JsonBean();
        jsonBean.set("title", "Donors");
        jsonBean.set("children", donorsTree);
        
        return jsonBean;
    }
}
