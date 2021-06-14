package org.digijava.kernel.ampapi.endpoints.scorecard;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.digijava.kernel.ampapi.endpoints.common.model.Org;
import org.digijava.kernel.ampapi.endpoints.common.model.OrgGroup;
import org.digijava.kernel.ampapi.endpoints.common.model.OrgType;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.DashboardsService;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Donor;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.DonorIdsWrapper;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.DonorScoreCardStats;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.DonorTreeNode;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.DonorTreeRoot;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.DonorsNoUpdatesWrapper;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.FilteredDonors;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Quarter;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.QuarterStats;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.SettingsBean;
import org.digijava.kernel.ampapi.endpoints.scorecard.service.ScorecardExcelExporter;
import org.digijava.kernel.ampapi.endpoints.scorecard.service.ScorecardNoUpdateDonor;
import org.digijava.kernel.ampapi.endpoints.scorecard.service.ScorecardService;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.module.aim.dbentity.AmpScorecardOrganisation;
import org.digijava.module.aim.dbentity.AmpScorecardSettings;
import org.digijava.module.aim.dbentity.AmpScorecardSettingsCategoryValue;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class should have all endpoints related to the Donor Scorecard -
 * AMP-20002
 *
 * @author Emanuel Perez
 */

@Path("scorecard")
@Api("scorecard")
public class DonorScorecard {

    private static final Logger logger = Logger.getLogger(DonorScorecard.class);

    @GET
    @Path("/stats")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieve donor scorecard stats for the specified year and quarter.",
            notes = "If the year is not specified the current one will be used. "
                    + "If the quarter is not specified, the last one will be used")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "Donor ScoreCard stats for the selected "
            + "quarter and year",
            response = DonorScoreCardStats.class))
    public Response getScoreCardStats(@ApiParam("Year") @QueryParam("year") Integer year,
                                      @ApiParam(value = "Quarter", allowableValues = "1,2,3,4")
                                      @QueryParam("quarter") Integer quarter) {
        return DashboardsService.buildOkResponseWithOriginHeaders(
                new ScorecardService().getDonorScorecardStats(year, quarter));
    }

    @OPTIONS
    @Path("/stats")
    @ApiOperation(
            value = "Describe options for endpoint",
            notes = "Enables Cross-Origin Resource Sharing for endpoint")
    public Response describeScoreCardStats() {
        return DashboardsService.buildOkResponseWithOriginHeaders("");
    }

    @GET
    @Path("/export")
    @Produces("application/vnd.ms-excel")
    @ApiOperation(
            value = "Retrieve an excel file with all the quarters, desired period, donors and the updated projects.",
            notes = "Creates an excel workbook having the headers with all the Quarters spanning the desired period, "
                    + "the donors as columns and each cell (for a given donor and quarter) painted with a color "
                    + "depending on the number of updated projects for a given donor on a quarter.")
    public StreamingOutput getDonorScorecard(@Context HttpServletResponse webResponse) {

        webResponse.setHeader("Content-Disposition", "attachment; filename=donorScorecard.xls");

        return new StreamingOutput() {
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    ScorecardService service = new ScorecardService();
                    List<Quarter> quarters = service.getSettingsQuarters();
                    ScorecardExcelExporter exporter = new ScorecardExcelExporter();
                    int startYear = service.getDefaultStartYear();
                    int endYear = service.getDefaultEndYear();
                    HSSFWorkbook wb = exporter.generateExcel(service.getFilteredDonors(), quarters,
                            service.getOrderedScorecardCells(startYear, endYear,
                                    service.getDonorActivityUpdates(startYear, endYear)));
                    wb.write(output);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new WebApplicationException(e);
                }
            }
        };
    }

    @GET
    @Path("/quickStats")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Retrieve a quick view of the audit logger.")
    public QuarterStats getPastQuarterOrganizationsCount() {
        ScorecardService scorecardService = new ScorecardService();

        return new QuarterStats(
                scorecardService.getPastQuarterOrganizationsCount(),
                scorecardService.getPastQuarterProjectsCount(),
                scorecardService.getPastQuarterUsersCount());
    }

    @POST
    @Path("/manager/settings")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Save the donor scorecard settings.")
    public void saveScorecardSettings(SettingsBean settingsBean) {
        List<AmpScorecardSettings> scorecardSettingsList =
                (List<AmpScorecardSettings>) DbUtil.getAll(AmpScorecardSettings.class);
        List<AmpCategoryValue> allCategoryValues = (List<AmpCategoryValue>) CategoryManagerUtil.
                getAmpCategoryValueCollectionByKey("activity_status");

        AmpScorecardSettings settings = scorecardSettingsList.isEmpty() ? new AmpScorecardSettings()
                : scorecardSettingsList.get(0);

        settings.setValidationPeriod(settingsBean.getValidationPeriod());
        settings.setPercentageThreshold(settingsBean.getPercentageThreshold());
        if (settingsBean.getValidationTime() != null && settingsBean.getValidationTime() > 0) {
            settings.setValidationTime(settingsBean.getValidationTime());
        }

        List<SettingsBean.CategoryValue> categoryValues = settingsBean.getCategoryValues();

        Set<AmpScorecardSettingsCategoryValue> closedStatuses = new HashSet<AmpScorecardSettingsCategoryValue>();

        if (categoryValues != null) {
            Set<Long> selectedValesSet = new HashSet<>();
            for (int i = 0; i < categoryValues.size(); i++) {
                selectedValesSet.add(categoryValues.get(i).getId());
            }

            for (AmpCategoryValue categoryValue : allCategoryValues) {
                if (selectedValesSet.contains(categoryValue.getId())) {
                    AmpScorecardSettingsCategoryValue scSettingsCategoryValue = new AmpScorecardSettingsCategoryValue();
                    scSettingsCategoryValue.setAmpCategoryValueStatus(categoryValue);
                    scSettingsCategoryValue.setAmpScorecardSettings(settings);
                    closedStatuses.add(scSettingsCategoryValue);
                }
            }
        }

        settings.setQuarters(String.join(",", settingsBean.getQuarters()));
        settings.getClosedStatuses().clear();
        settings.getClosedStatuses().addAll(closedStatuses);

        try {
            DbUtil.saveOrUpdateObject(settings);
        } catch (Exception e) {
            throw new ApiRuntimeException(
                    ApiError.toError("Exception while saving settings object: " + e.getMessage()));
        }
    }

    @POST
    @Path("/manager/donors/noupdates")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Save the noUpdate donors.",
            notes = "Used in Scorecard Manager (admin section), receive a list of donors that are not to be "
                    + "excluded in the scorecard.")
    public void getDonorsNoUpdates(@ApiParam("list of donors") DonorsNoUpdatesWrapper donorsBean) {
        DbUtil.deleteAllNoUpdateOrgs(false);

        for (Long donorId : donorsBean.getDonorsNoUpdates()) {
            AmpScorecardOrganisation org = new AmpScorecardOrganisation();
            org.setAmpDonorId(donorId);
            org.setModifyDate(new Date());
            org.setToExclude(false);
            try {
                DbUtil.saveOrUpdateObject(org);
            } catch (Exception e) {
                throw new ApiRuntimeException(
                        ApiError.toError("Exception while saving settings object: " + e.getMessage()));
            }
        }
    }

    @POST
    @Path("/manager/donors/filtered")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Retrieve and provide a list of the filtered donors.",
            notes = "Used in Scorecard Manager (admin section), receive the list of selected donors in the "
                    + "scorecard manager that are to be excluded in the scorecard.")
    public FilteredDonors getFilteredDonors(@ApiParam("list of donors") DonorIdsWrapper donorsBean) {
        ScorecardService scorecardService = new ScorecardService();

        // delete all excluded organizations from the amp_scorecard_organisation table having to_exclude = true
        DbUtil.deleteAllNoUpdateOrgs(true);

        Set<Long> donorIds = new HashSet<>(donorsBean.getDonorIds());
        for (Long donorId : donorIds) {
            AmpScorecardOrganisation org = new AmpScorecardOrganisation();
            org.setAmpDonorId(donorId);
            org.setModifyDate(new Date());
            org.setToExclude(true);

            try {
                DbUtil.saveOrUpdateObject(org);
            } catch (Exception e) {
                throw new ApiRuntimeException(
                        ApiError.toError("Exception while saving scorecard organization object: " + e.getMessage()));
            }
        }

        List<ScorecardNoUpdateDonor> allDonors = scorecardService.getScorecardDonors(true);
        List<Donor> allFilteredDonors = new ArrayList<>();

        for (ScorecardNoUpdateDonor donor : allDonors) {
            if (!donorIds.contains(donor.getAmpDonorId())) {
                allFilteredDonors.add(new Donor(donor.getAmpDonorId(), donor.getName()));
            }
        }

        List<Donor> noUpdatesFilteredDonors = new ArrayList<>();
        List<ScorecardNoUpdateDonor> noUpdatedDonors = scorecardService.getScorecardDonors(false);

        for (ScorecardNoUpdateDonor noUpdateDonor : noUpdatedDonors) {
            if (!donorIds.contains(noUpdateDonor.getAmpDonorId())) {
                noUpdatesFilteredDonors.add(new Donor(noUpdateDonor.getAmpDonorId(), noUpdateDonor.getName()));
            }
        }

        return new FilteredDonors(allFilteredDonors, noUpdatesFilteredDonors);
    }

    @GET
    @Path("/manager/donors")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Retrieve and provide a list of all the donors.",
            notes = "Used for filter tree in Donor Scorecard Manager.")
    public DonorTreeRoot getAllDonors() {
        List<Org> orgs = QueryUtil.getDonors(false);
        List<OrgType> orgTypes = QueryUtil.getOrgTypes();
        List<OrgGroup> orgGroups = QueryUtil.getOrgGroups();

        List<DonorTreeNode> donorsTree = new ArrayList<>();
        for (OrgType orgType : orgTypes) {
            Set<Long> orgGrpIds = orgType.getGroupIds();
            List<DonorTreeNode> typeChildren = new ArrayList<>();
            for (OrgGroup orgGroup : orgGroups) {
                if (orgGrpIds.contains(orgGroup.getId())) {
                    List<DonorTreeNode> groupChildren = new ArrayList<>();
                    Set<Long> orgIds = orgGroup.getOrgIds();
                    for (Org org : orgs) {
                        if (orgIds.contains(org.getId())) {
                            groupChildren.add(new DonorTreeNode(org.getId(), org.getName(), false, true));
                        }
                    }
                    typeChildren.add(new DonorTreeNode(orgGroup.getId(), orgGroup.getName(), true, groupChildren));
                }
            }
            donorsTree.add(new DonorTreeNode(orgType.getId(), orgType.getName(), true, typeChildren));
        }

        return new DonorTreeRoot(donorsTree);
    }
}
