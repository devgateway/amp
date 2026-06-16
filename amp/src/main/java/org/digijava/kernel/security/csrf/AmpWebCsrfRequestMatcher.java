package org.digijava.kernel.security.csrf;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class AmpWebCsrfRequestMatcher implements RequestMatcher {

    private static final Set<String> SAFE_METHODS = new HashSet<>(Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));

    /**
     * Wicket paths are protected by Wicket's own CsrfPreventionRequestCycleListener
     * (Origin/Referer header check) registered in OnePagerApp. Spring CSRF is not needed there.
     */
    private final AntPathRequestMatcher wicketMatcher = new AntPathRequestMatcher("/wicket/**");

    private final RequestMatcher readOnlyPostMatcher = new OrRequestMatcher(
            new AntPathRequestMatcher("/search/search.do", "POST"),
            new AntPathRequestMatcher("/aim/filterDesktopActivities.do", "POST"),
            new AntPathRequestMatcher("/aim/searchDesktopActivities.do", "POST"),
            new AntPathRequestMatcher("/aim/validateReportsFilterPicker.do", "POST"),
            new AntPathRequestMatcher("/aim/export*.do", "POST"),
            new AntPathRequestMatcher("/help/*Export.do", "POST"),
            new AntPathRequestMatcher("/calendar/viewEvents.do", "POST"),
            new AntPathRequestMatcher("/calendar/viewListEvents.do", "POST"),
            new AntPathRequestMatcher("/calendar/viewMonthEvents.do", "POST"),
            new AntPathRequestMatcher("/calendar/viewYearEvents.do", "POST"));

    @Override
    public boolean matches(HttpServletRequest request) {
        // Wicket paths handled by Wicket's CsrfPreventionRequestCycleListener
        if (wicketMatcher.matches(request)) {
            return false;
        }
        if (isPublicDocTabManagerStateChange(request)) {
            return true;
        }
        if (isContentRepositoryDocumentDelete(request)) {
            return true;
        }

        String method = request.getMethod();
        if (method != null && SAFE_METHODS.contains(method.toUpperCase(Locale.ROOT))) {
            return false;
        }

        if (isReadOnlyDocFromTemplatePost(request)) {
            return false;
        }
        if (isReadOnlyDocumentManagerPost(request)) {
            return false;
        }

        return !readOnlyPostMatcher.matches(request);
    }

    private boolean isReadOnlyDocumentManagerPost(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())
                || !"/contentrepository/documentManager.do".equals(getRequestPath(request))
                || isMultipart(request)) {
            return false;
        }

        return "true".equalsIgnoreCase(request.getParameter("ajaxDocumentList"));
    }

    private boolean isMultipart(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase(Locale.ROOT).startsWith("multipart/form-data");
    }

    private boolean isReadOnlyDocFromTemplatePost(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())
                || !"/contentrepository/docFromTemplate.do".equals(getRequestPath(request))) {
            return false;
        }

        String action = request.getParameter("actType");
        return action == null || action.length() == 0
                || "loadTemplates".equalsIgnoreCase(action)
                || "getTemplate".equalsIgnoreCase(action);
    }

    private boolean isPublicDocTabManagerStateChange(HttpServletRequest request) {
        if (!"/contentrepository/publicDocTabManager.do".equals(getRequestPath(request))) {
            return false;
        }

        String action = request.getParameter("action");
        return "save".equalsIgnoreCase(action)
                || "savePositions".equalsIgnoreCase(action)
                || "delete".equalsIgnoreCase(action);
    }

    private boolean isContentRepositoryDocumentDelete(HttpServletRequest request) {
        return "/contentrepository/deleteForDocumentManager.do".equals(getRequestPath(request));
    }

    private String getRequestPath(HttpServletRequest request) {
        String path = request.getServletPath();
        if (path == null || path.length() == 0) {
            path = request.getRequestURI();
            String contextPath = request.getContextPath();
            if (contextPath != null && contextPath.length() > 0 && path.startsWith(contextPath)) {
                path = path.substring(contextPath.length());
            }
        }
        return path;
    }
}