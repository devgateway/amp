package org.digijava.kernel.ampapi.endpoints.common;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.PublicServices;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.filetype.MimeUtil;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpOfflineService;
import org.digijava.kernel.services.AmpVersionService;
import org.digijava.kernel.util.SpringUtil;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpOfflineCompatibleVersionRange;
import org.digijava.module.aim.dbentity.AmpOfflineRelease;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * This class should have all end point related to the configuration of amp
 *
 * @author Diego Dimunzio
 */
@Path("amp")
@Api("amp")
public class AmpConfiguration {

    private Logger logger = LoggerFactory.getLogger(AmpConfiguration.class);

    private AmpVersionService ampVersionService = SpringUtil.getBean(AmpVersionService.class);

    private AmpOfflineService ampOfflineService = SpringUtil.getBean(AmpOfflineService.class);

    @GET
    @Path("/settings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "Settings")
    @ApiOperation(
            value = "Retrieve general AMP settings",
            notes = "This endpoint provides access to general AMP configuration settings including " +
                    "currency information, date format, and other system-wide settings. " +
                    "These settings are essential for client applications to properly format and display data.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "General settings",
            response = AmpGeneralSettings.class))
    public Response getSettings() {
        return PublicServices.buildOkResponseWithOriginHeaders(SettingsUtils.getGeneralSettings());
    }

    @GET
    @Path("/settings/gis")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "GisSettings")
    @ApiOperation(
            value = "Retrieve GIS settings",
            notes = "This endpoint provides access to Geographic Information System (GIS) configuration settings.\n\n" +
                    "The response includes map configuration parameters, layer definitions, coordinate systems, " +
                    "and other GIS-related settings that are essential for client applications to properly " +
                    "render and interact with geographic data visualizations.\n\n" +
                    "These settings are used by the GIS module to configure maps and geographic data displays.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK, message = "GIS settings",
            response = AmpGeneralSettings.class))
    public Response getGISSettings() {
        return PublicServices.buildOkResponseWithOriginHeaders(SettingsUtils.getGisSettings());
    }



    @OPTIONS
    @Path("/settings")
    @ApiOperation(
            value = "Describe options for endpoint",
            notes = "Enables Cross-Origin Resource Sharing for endpoint")
    public Response describeTopsDashboard() {
        return PublicServices.buildOkResponseWithOriginHeaders("");
    }

    @GET
    @Path("/amp-offline-version-check")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "version-check")
    @ApiOperation(
            value = "Check if AMP Offline App is compatible with current AMP version",
            notes = "This endpoint verifies compatibility between the AMP Offline application and the current AMP server version.\n\n" +
                    "The AMP Offline version is automatically detected from the User-Agent header, which must follow this format:\n" +
                    "AMPOffline/{version} ({os}; {arch})\n\n" +
                    "Example: `AMPOffline/1.0.0 (windows; 32)`\n\n" +
                    "The response includes:\n" +
                    "- Whether the client version is compatible with the server\n" +
                    "- If AMP Offline is enabled on this server\n" +
                    "- The current AMP server version\n" +
                    "- Information about the latest compatible AMP Offline release\n" +
                    "- Server ID information for verification")
    public VersionCheckResponse ampOfflineVersionCheck(@QueryParam("server-id") String serverId) {

        AmpOfflineRelease clientRelease = detectClientRelease();

        VersionCheckResponse response = new VersionCheckResponse();
        response.setAmpOfflineCompatible(ampVersionService.isAmpOfflineCompatible(clientRelease));
        response.setAmpOfflineEnabled(FeaturesUtil.isAmpOfflineEnabled());
        response.setAmpVersion(ampVersionService.getVersionInfo().getAmpVersion());
        response.setLatestAmpOffline(ampOfflineService.findLastRelease(clientRelease));
        response.setServerId(getServerId());
        response.setServerIdMatch(isServerIdMatch(serverId));

        return response;
    }

    public static AmpOfflineRelease detectClientRelease() {
        AmpOfflineRelease release = null;
        if (AmpClientModeHolder.isOfflineClient()) {
            try {
                String userAgent = TLSUtils.getRequest().getHeader("User-Agent");
                release = AmpOfflineRelease.fromUserAgent(userAgent);
            } catch (IllegalArgumentException e) {
                ApiErrorResponse error = ApiError.toError(
                        AmpConfigurationErrors.INVALID_INPUT.withDetails(e.getMessage()));
                throw new ApiRuntimeException(error);
            }
        }
        return release;
    }

    @GET
    @Path("/amp-offline-release")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiOperation(
            value = "List latest AMP Offline releases for each OS/Arch combination",
            notes = "This endpoint returns a list of the most recent AMP Offline application releases " +
                    "that are compatible with the current AMP server version.\n\n" +
                    "The response includes separate releases for different operating systems (Windows, macOS) " +
                    "and architecture types (32-bit, 64-bit).\n\n" +
                    "Each release entry contains version information, release date, download URL, " +
                    "and system requirements.")
    public List<AmpOfflineRelease> getAmpOfflineReleases() {
        return ampOfflineService.getLatestCompatibleReleases();
    }

    @GET
    @Path("/amp-offline-release/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ApiOperation(
            value = "Download AMP Offline release binary file",
            notes = "This endpoint allows downloading the installation file for a specific AMP Offline release.\n\n" +
                    "The file is returned as an attachment with the appropriate content type and filename.\n\n" +
                    "Use the release ID obtained from the /amp-offline-release endpoint to specify which " +
                    "release version you want to download.")
    public Response getAmpOfflineReleaseFile(@ApiParam("Release ID from the /amp-offline-release endpoint") @PathParam("id") Long id) {
        File file = ampOfflineService.getReleaseFile(id);

        ContentDisposition contentDisposition = ContentDisposition.type("attachment")
                .fileName(file.getName())
                .size(file.length())
                .build();

        String mimeType = MimeUtil.detectMimeType(file, MediaType.APPLICATION_OCTET_STREAM);

        return Response.ok(file, mimeType)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .build();
    }

    @GET
    @Path("global-settings")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "global-settings", authTypes = AuthRule.AUTHENTICATED)
    @ApiOperation(
            value = "Retrieve all AMP Global Settings",
            notes = "This endpoint provides access to all global configuration settings in the AMP system.\n\n" +
                    "Global settings control system-wide behaviors and defaults such as currency display, " +
                    "fiscal calendar configuration, approval workflows, and many other aspects of the system.\n\n" +
                    "This endpoint requires authentication as it may contain sensitive configuration information.")
    @ApiResponses(@ApiResponse(
            code = HttpServletResponse.SC_OK,
            message = "A map containing all global settings where key is the setting name and value is the setting value. " +
                    "For example: {\"Currency Code\": \"USD\", \"Default Country\": \"US\", ...}"))
    public Map<String, String> getGlobalSettings() {
        return FeaturesUtil.getGlobalSettings().stream()
                .filter(s -> s.getGlobalSettingsValue() != null)
                .collect(Collectors.toMap(
                        AmpGlobalSettings::getGlobalSettingsName,
                        AmpGlobalSettings::getGlobalSettingsValue));
    }

    @GET
    @Path("global-settings/public")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "public-global-settings")
    @ApiOperation(
            value = "Retrieve all AMP Global Settings",
            notes = "This endpoint provides access to all global configuration settings in the AMP system.\n\n" +
                    "Global settings control system-wide behaviors and defaults such as currency display, " +
                    "fiscal calendar configuration, approval workflows, and many other aspects of the system.\n\n" +
                    "This endpoint requires authentication as it may contain sensitive configuration information.")
    @ApiResponses(@ApiResponse(
            code = HttpServletResponse.SC_OK,
            message = "A map containing all global settings where key is the setting name and value is the setting value. " +
                    "For example: {\"Currency Code\": \"USD\", \"Default Country\": \"US\", ...}"))
    public Map<String, String> getPublicGlobalSettings() {
        return FeaturesUtil.getGlobalSettings().stream()
                .filter(s -> s.getGlobalSettingsValue() != null
                        && PublicGlobalSettings.SETTINGS.contains(s.getGlobalSettingsName()))
                .collect(Collectors.toMap(
                        AmpGlobalSettings::getGlobalSettingsName,
                        AmpGlobalSettings::getGlobalSettingsValue));
    }

    @GET
    @Path("compatible-version-range")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "getCompatibleVersionRanges", ui = false, authTypes = AuthRule.IN_ADMIN)
    @ApiOperation(
            value = "Retrieve all compatible AMP Offline version ranges",
            notes = "This endpoint returns a list of version ranges that define which AMP Offline versions " +
                    "are compatible with the current AMP server version.\n\n" +
                    "Each version range specifies a minimum and maximum AMP Offline version that can work " +
                    "with this AMP server. This information is used to determine if a client needs to upgrade " +
                    "their AMP Offline application.\n\n" +
                    "This endpoint requires administrator privileges as it provides system compatibility information.")
    public List<AmpOfflineCompatibleVersionRange> getCompatibleVersionRanges() {
        return ampVersionService.getCompatibleVersionRanges();
    }

    @PUT
    @Path("compatible-version-range")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "addCompatibleVersionRange", ui = false, authTypes = AuthRule.IN_ADMIN)
    @ApiOperation(
            value = "Create a new AMP Offline compatibility version range",
            notes = "This endpoint allows administrators to define a new version range that specifies which " +
                    "AMP Offline versions are compatible with the current AMP server.\n\n" +
                    "The version range must include a minimum and maximum version number in semantic versioning " +
                    "format (e.g., 1.0.0). The range is inclusive, meaning both the minimum and maximum versions " +
                    "are considered compatible.\n\n" +
                    "If the provided version range overlaps with existing ranges or contains invalid version " +
                    "numbers, the request will be rejected with an appropriate error message.\n\n" +
                    "This endpoint requires administrator privileges.")
    public AmpOfflineCompatibleVersionRange addCompatibleVersionRange(AmpOfflineCompatibleVersionRange versionRange) {
        try {
            return ampVersionService.addCompatibleVersionRange(versionRange);
        } catch (IllegalArgumentException e) {
            ApiErrorResponse error = ApiError.toError(AmpConfigurationErrors.INVALID_INPUT.withDetails(e.getMessage()));
            throw new ApiRuntimeException(Response.Status.BAD_REQUEST, error);
        }
    }

    @POST
    @Path("compatible-version-range/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "updateCompatibleVersionRange", ui = false, authTypes = AuthRule.IN_ADMIN)
    @ApiOperation(
            value = "Update an existing AMP Offline compatibility version range",
            notes = "This endpoint allows administrators to modify an existing version range that defines " +
                    "which AMP Offline versions are compatible with the current AMP server.\n\n" +
                    "The version range must include a minimum and maximum version number in semantic versioning " +
                    "format (e.g., 1.0.0). The range is inclusive, meaning both the minimum and maximum versions " +
                    "are considered compatible.\n\n" +
                    "The ID in the path must match an existing version range. If the updated version range " +
                    "overlaps with other existing ranges or contains invalid version numbers, the request " +
                    "will be rejected with an appropriate error message.\n\n" +
                    "This endpoint requires administrator privileges.")
    public AmpOfflineCompatibleVersionRange updateCompatibleVersionRange(@PathParam("id") Long id,
                                                                         AmpOfflineCompatibleVersionRange versionRange) {
        try {
            versionRange.setId(id);
            return ampVersionService.updateCompatibleVersionRange(versionRange);
        } catch (IllegalArgumentException e) {
            ApiErrorResponse error = ApiError.toError(AmpConfigurationErrors.INVALID_INPUT.withDetails(e.getMessage()));
            throw new ApiRuntimeException(Response.Status.BAD_REQUEST, error);
        }
    }

    @DELETE
    @Path("compatible-version-range/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "deleteCompatibleVersionRange", ui = false, authTypes = AuthRule.IN_ADMIN)
    @ApiOperation(
            value = "Delete an existing AMP Offline compatibility version range",
            notes = "This endpoint allows administrators to remove a version range that defines which " +
                    "AMP Offline versions are compatible with the current AMP server.\n\n" +
                    "The ID in the path must match an existing version range. If the specified version range " +
                    "does not exist, the request will return an error.\n\n" +
                    "Deleting a version range means that AMP Offline clients with versions in that range " +
                    "will no longer be considered compatible with this AMP server and may be required to upgrade.\n\n" +
                    "This endpoint requires administrator privileges.")
    public AmpOfflineCompatibleVersionRange deleteCompatibleVersionRange(@PathParam("id") Long id) {
        return ampVersionService.deleteCompatibleVersionRange(id);
    }

    @GET
    @Path("offline/{arch}/latest-mac.yml")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(
            value = "Get information about the latest AMP Offline release for macOS",
            notes = "This endpoint returns metadata about the latest compatible AMP Offline release for macOS " +
                    "in YAML format. This information is used by the AMP Offline auto-update mechanism.\n\n" +
                    "The response includes version number, release date, file path, and SHA-512 hash for " +
                    "verifying the integrity of the downloaded file.\n\n" +
                    "The architecture parameter specifies whether to return information for 32-bit or 64-bit systems.")
    public Response getOfflineLatestMac(
            @ApiParam(allowableValues = "32,64", example = "64") @PathParam("arch") String arch) {
        return getOfflineReleaseYml(arch, AmpOfflineRelease.MAC_OS, "zip");
    }

    @GET
    @Path("offline/{arch}/latest.yml")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(
            value = "Get information about the latest AMP Offline release for Windows",
            notes = "This endpoint returns metadata about the latest compatible AMP Offline release for Windows " +
                    "in YAML format. This information is used by the AMP Offline auto-update mechanism.\n\n" +
                    "The response includes version number, release date, file path, and SHA-512 hash for " +
                    "verifying the integrity of the downloaded file.\n\n" +
                    "The architecture parameter specifies whether to return information for 32-bit or 64-bit systems.")
    public Response getOfflineLatestWin(
            @ApiParam(allowableValues = "32,64", example = "64") @PathParam("arch") String arch) {
        return getOfflineReleaseYml(arch, AmpOfflineRelease.WINDOWS, "exe");
    }

    private Response getOfflineReleaseYml(String arch, String os, String extension) {
        requireValidArch(arch);

        AmpOfflineRelease release = ampOfflineService.getLatestCompatibleReleases()
                .stream()
                .filter(r -> r.getOs().equals(os) && r.getArch().equals(arch))
                .findFirst()
                .orElse(null);

        if (release != null) {
            try {
                SimpleDateFormat df = new SimpleDateFormat(EPConstants.ISO8601_DATE_AND_TIME_FORMAT);
                File file = ampOfflineService.getReleaseFile(release);
                String hash = Files.hash(file, Hashing.sha512()).toString();
                String yml = String.format("version: %s\nreleaseDate: '%s'\npath: %d.%s\nsha512: %s\n",
                        release.getVersion(), df.format(release.getDate()), release.getId(), extension, hash);
                return Response.ok(yml).build();
            } catch (IOException e) {
                logger.error("Failed to compute hash for release file.", e);
                ApiErrorResponse error = ApiError.toError("Failed to compute hash for release file.");
                throw new ApiRuntimeException(Response.Status.INTERNAL_SERVER_ERROR, error);
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("offline/{arch}/{id}.exe")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ApiOperation(
            value = "Download AMP Offline installation file for Windows",
            notes = "This endpoint allows downloading the installation file (.exe) for a specific AMP Offline " +
                    "release for Windows operating systems.\n\n" +
                    "The file is returned as an attachment with the appropriate content type and filename.\n\n" +
                    "The architecture parameter specifies whether to download the 32-bit or 64-bit version.\n\n" +
                    "Use the release ID obtained from the /amp-offline-release endpoint or from the latest.yml " +
                    "metadata to specify which release version you want to download.")
    public Response getWinReleaseFile(
            @ApiParam(allowableValues = "32,64", example = "64") @PathParam("arch") String arch,
            @ApiParam(value = "Release id") @PathParam("id") Long id) {
        requireValidArch(arch);
        return getAmpOfflineReleaseFile(id);
    }

    @GET
    @Path("offline/{arch}/{id}.zip")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ApiOperation(
            value = "Download AMP Offline installation file for macOS",
            notes = "This endpoint allows downloading the installation file (.zip) for a specific AMP Offline " +
                    "release for macOS operating systems.\n\n" +
                    "The file is returned as an attachment with the appropriate content type and filename.\n\n" +
                    "The architecture parameter specifies whether to download the 32-bit or 64-bit version.\n\n" +
                    "Use the release ID obtained from the /amp-offline-release endpoint or from the latest-mac.yml " +
                    "metadata to specify which release version you want to download.")
    public Response getMacReleaseFile(
            @ApiParam(allowableValues = "32,64", example = "64") @PathParam("arch") String arch,
            @ApiParam(value = "Release id") @PathParam("id") Long id) {
        requireValidArch(arch);
        return getAmpOfflineReleaseFile(id);
    }

    private void requireValidArch(String arch) {
        if (!"32".equals(arch) && !"64".equals(arch)) {
            ApiErrorResponse error = ApiError.toError(
                    AmpConfigurationErrors.INVALID_INPUT.withDetails("Invalid architecture"));
            throw new ApiRuntimeException(Response.Status.BAD_REQUEST, error);
        }
    }

    public String getServerId() {
        return FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_SERVER_ID);
    }

    private boolean isServerIdMatch(String serverId) {
        return getServerId() != null ? getServerId().equals(serverId) : false;
    }

}
