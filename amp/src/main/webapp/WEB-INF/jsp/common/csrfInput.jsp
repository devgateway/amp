<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.springframework.security.web.csrf.CsrfToken" %>
<%
CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
if (csrfToken != null) {
%>
<input type="hidden" name="<%= StringEscapeUtils.escapeHtml(csrfToken.getParameterName()) %>" value="<%= StringEscapeUtils.escapeHtml(csrfToken.getToken()) %>" />
<%
}
%>