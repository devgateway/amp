package org.dgfoundation.amp.newreports;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;


/**
 * describes the per-run settings of the environment under which to run a report
 * current implementation limitation in AMP: pledgesFilter is only applied in pledge reports (and those disregard workspace filters)
 * @author Constantin Dolghier
 */
public class ReportEnvironment {

    private static final GlobalActivityIdsGenerator GLOBAL_ACTIVITY_IDS_GENERATOR = new GlobalActivityIdsGenerator();

    public final String locale;
    public final IdsGeneratorSource workspaceFilter;
    public final IdsGeneratorSource pledgesFilter;

    /**
     * the currency code used for rendering the report IFF the report specification does not specify any
     */
    public final String defaultCurrencyCode;

    public ReportEnvironment(String locale, IdsGeneratorSource workspaceFilter, String defaultCurrencyCode) {
        this(locale, workspaceFilter, null, defaultCurrencyCode);
    }

    public ReportEnvironment(String locale, IdsGeneratorSource workspaceFilter, IdsGeneratorSource pledgesFilter, String defaultCurrencyCode) {
        this.locale = locale;
        this.workspaceFilter = workspaceFilter;
        this.pledgesFilter = pledgesFilter;
        this.defaultCurrencyCode = defaultCurrencyCode;
    }

    public static ReportEnvironment buildFor(HttpServletRequest request) {
        TLSUtils.populate(request);
        IdsGeneratorSource workspaceFilter;
        if (TLSUtils.isFilterGlobally()) {
            workspaceFilter = GLOBAL_ACTIVITY_IDS_GENERATOR;
        } else if (request != null && request.getAttribute(OVERRIDDEN_WORKSPACE_FILTER) != null) {
            workspaceFilter = (IdsGeneratorSource) request.getAttribute(OVERRIDDEN_WORKSPACE_FILTER);
        } else if (request != null && request.getSession() != null
                && request.getSession().getAttribute(Constants.COMPLETE_TEAM_FILTER) != null) {
            workspaceFilter = (IdsGeneratorSource) request.getSession().getAttribute(Constants.COMPLETE_TEAM_FILTER);
        } else {
            TeamMember tm = request != null && request.getSession() != null
                    ? (TeamMember) request.getSession().getAttribute("currentMember") : null;
            HttpSession session = request == null ? null : request.getSession(true);
            workspaceFilter = TeamUtil.initCompleteTeamFilter(session, tm);
        }
        String currencyCode = AmpARFilter.getDefaultCurrency().getCurrencyCode();
        return new ReportEnvironment(TLSUtils.getEffectiveLangCode(), workspaceFilter, currencyCode);
    }

    public static final String OVERRIDDEN_WORKSPACE_FILTER = "overrideWorkspaceFilter";
}
