package org.digijava.kernel.ampapi.endpoints.async;

import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.ampapi.endpoints.activity.ApiContext;
import org.digijava.kernel.ampapi.filters.AmpClientModeHolder;
import org.digijava.kernel.ampapi.filters.ClientMode;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.util.TeamUtil;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static org.digijava.module.aim.util.FeaturesUtil.AMP_TREE_VISIBILITY_ATTR;

public final class AsyncUtil {
    private AsyncUtil() {

    }

    @NotNull
    public static ApiContext buildApiContext() {
        ApiContext apiContext = new ApiContext();
        HttpServletRequest request = TLSUtils.getRequest();
        apiContext.setSessionServletContext(request.getSession().getServletContext());
        apiContext.setTeamMember(TeamUtil.getCurrentMember());
        apiContext.setUser(TeamUtil.getCurrentUser());
        apiContext.setAmpTreeVisibility((AmpTreeVisibility) request.getSession()
                .getAttribute(AMP_TREE_VISIBILITY_ATTR));
        apiContext.setSite(TLSUtils.getSite());
        apiContext.setAmpTreeVisibilityModificationDate((Date) request.getSession()
                .getAttribute("ampTreeVisibilityModificationDate"));
        apiContext.setRequestURL(request.getRequestURL());
        apiContext.setRootPath(TLSUtils.getRequest().getServletContext().getRealPath("/"));

        ClientMode clientMode = null;

        if (AmpClientModeHolder.isOfflineClient()) {
            clientMode = ClientMode.AMP_OFFLINE;
        } else if (AmpClientModeHolder.isIatiImporterClient()) {
            clientMode = ClientMode.IATI_IMPORTER;
        }

        apiContext.setClientMode(clientMode);

        return apiContext;
    }
}
