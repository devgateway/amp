<%@page import="org.dgfoundation.amp.ar.ReportContextData" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://digijava.org/CategoryManager" prefix="category" %>
<%@ taglib uri="http://digijava.org/fields" prefix="field" %>
<%@ taglib uri="http://digijava.org/features" prefix="feature" %>
<%@ taglib uri="http://digijava.org/modules" prefix="module" %>

<%
    pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

<link rel="stylesheet" type="text/css" href="/src/main/webapp/WEB-INF/TEMPLATE/ampTemplate/css/yui/tabview.css"/>
<link type="text/css" href="css_2/tabs.css" rel="stylesheet"/>
<link href="/src/main/webapp/WEB-INF/TEMPLATE/ampTemplate/css_2/yui_tabs.css" rel="stylesheet" type="text/css"></link>
<link href="/src/main/webapp/WEB-INF/TEMPLATE/ampTemplate/css_2/yui_datatable.css" rel="stylesheet" type="text/css"></link>
<link href="/src/main/webapp/WEB-INF/TEMPLATE/ampTemplate/css_2/desktop_yui_tabs.css" rel="stylesheet" type="text/css"></link>
<link rel="stylesheet" type="text/css" href="/src/main/webapp/WEB-INF/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/tabview-core.css">
<link href="/src/main/webapp/WEB-INF/TEMPLATE/ampTemplate/css_2/yui_popins.css" rel="stylesheet" type="text/css"></link>
<link rel="stylesheet" type="text/css" href="/src/main/webapp/WEB-INF/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css">
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