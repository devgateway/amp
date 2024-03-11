<%@page import="org.dgfoundation.amp.ar.ReportContextData" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/category.tld" prefix="category" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/fieldVisibility.tld" prefix="field" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/featureVisibility.tld" prefix="feature" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/moduleVisibility.tld" prefix="module" %>

<%
    pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css"/>
<link type="text/css" href="css_2/tabs.css" rel="stylesheet"/>
<link href="/TEMPLATE/ampTemplate/css_2/yui_tabs.css" rel="stylesheet" type="text/css"></link>
<link href="/TEMPLATE/ampTemplate/css_2/yui_datatable.css" rel="stylesheet" type="text/css"></link>
<link href="/TEMPLATE/ampTemplate/css_2/desktop_yui_tabs.css" rel="stylesheet" type="text/css"></link>
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/tabview-core.css">
<link href="/TEMPLATE/ampTemplate/css_2/yui_popins.css" rel="stylesheet" type="text/css"></link>
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css">
<digi:instance property="aimReportsFilterPickerForm"/>
<digi:form action="/reportsFilterPicker.do" style="height: 100%">
    <bean:define id="reqBeanSetterObject" toScope="request" name="aimReportsFilterPickerForm"/>

    <html:hidden property="text"/>
    <html:hidden property="sourceIsReportWizard"/>
    <div id="tabview_container" class="content-direction yui-navset" style="display: block; overflow: hidden;
height: 80%; padding-bottom: 0px;margin-top: 15px;margin-left: 5px;margin-right: 5px">

    </div>
    <div class="clrfix content-direction" style="height: 15%;">

        <div style="clear:both;text-align:center;padding:2px 0px 0px 0px;margin-top: 20px;height: 15%;">
            <input type="hidden" name="ampReportId" value="${reportCD.ampReportId}"/>
            <input type="hidden" name="reportContextId" value="${reportCD.contextId}"/>

            <html:hidden property="defaultCurrency"/>

        </div>
    </div>

    <html:hidden property="workspaceonly" styleId="workspaceOnly"/>
</digi:form>