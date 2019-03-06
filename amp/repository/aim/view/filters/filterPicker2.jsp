<%@page import="org.dgfoundation.amp.ar.ReportContextData" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

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