package org.digijava.kernel.ampapi.endpoints.common;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.net.HttpHeaders;
import com.sun.jersey.core.header.ContentDisposition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.filetype.MimeUtil;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.module.aim.dbentity.AmpOfflineRelease;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpOfflineService;
import org.digijava.kernel.util.SpringUtil;
import org.digijava.kernel.services.AmpVersionService;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpOfflineCompatibleVersionRange;
import org.digijava.module.aim.util.FeaturesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    @ApiOperation("General settings")
    public AmpGeneralSettings getSettings() {
        return SettingsUtils.getGeneralSettings();
    }

    @GET
    @Path("/amp-offline-version-check")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(ui = false, id = "version-check")
    @ApiOperation(
            value = "Check if AMP Offline App is compatible with AMP.",
            notes = "AMP Offline version is read from User-Agent header. Header must have the following form: "
                    + "AMPOffline/{version} ({os}; {arch}).\n\nExample: `AMPOffline/1.0.0 (windows; 32)`.")
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
    @ApiOperation("List latest AMP Offline releases for each OS/Arch combination.")
    public List<AmpOfflineRelease> getAmpOfflineReleases() {
        return ampOfflineService.getLatestCompatibleReleases();
    }

    @GET
    @Path("/amp-offline-release/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ApiOperation("Returns the AMP Offline release binary.")
    public Response getAmpOfflineReleaseFile(@ApiParam("Release id") @PathParam("id") Long id) {
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
    @ApiOperation(value = "Returns all AMP Global Settings.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK,
            message = "Response is a map containing all global settings where "
                    + "key is setting name and value is setting value."))
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
    @ApiOperation(value = "Returns all public AMP Global Settings.")
    @ApiResponses(@ApiResponse(code = HttpServletResponse.SC_OK,
            message = "Response is a map containing all public global settings where "
                    + "key is setting name and value is setting value."))
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
    @ApiOperation("Returns all compatible AMP Offline version ranges.")
    public List<AmpOfflineCompatibleVersionRange> getCompatibleVersionRanges() {
        return ampVersionService.getCompatibleVersionRanges();
    }

    @PUT
    @Path("compatible-version-range")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @ApiMethod(id = "addCompatibleVersionRange", ui = false, authTypes = AuthRule.IN_ADMIN)
    @ApiOperation("Create a new version range to denote AMP Offline compatibility.")
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
    @ApiOperation("Update an existing version range that denotes AMP Offline compatibility.")
    public AmpOfflineCompatibleVersionRange
    updateCompatibleVersionRange(@PathParam("id") Long id,
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
    @ApiOperation("Delete an existing version range that denotes AMP Offline compatibility.")
    public AmpOfflineCompatibleVersionRange deleteCompatibleVersionRange(@PathParam("id") Long id) {
        return ampVersionService.deleteCompatibleVersionRange(id);
    }

    @GET
    @Path("offline/{arch}/latest-mac.yml")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation("Returns info about latest AMP Offline release for macOS.")
    public Response getOfflineLatestMac(
            @ApiParam(allowableValues = "32,64", example = "64") @PathParam("arch") String arch) {
        return getOfflineReleaseYml(arch, AmpOfflineRelease.MAC_OS, "zip");
    }

    @GET
    @Path("offline/{arch}/latest.yml")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation("Returns info about latest AMP Offline release for Windows.")
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
    @ApiOperation("Returns the AMP Offline release binary for Windows.")
    public Response getWinReleaseFile(
            @ApiParam(allowableValues = "32,64", example = "64") @PathParam("arch") String arch,
            @ApiParam(value = "Release id") @PathParam("id") Long id) {
        requireValidArch(arch);
        return getAmpOfflineReleaseFile(id);
    }

    @GET
    @Path("offline/{arch}/{id}.zip")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ApiOperation("Returns the AMP Offline release binary for macOS.")
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
