<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://digijava.org/fields" prefix="field" %>
<%@ taglib uri="http://digijava.org/features" prefix="feature" %>
<%@ taglib uri="http://digijava.org/modules" prefix="module" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page import="org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicator" %>

<link rel="stylesheet" href="<digi:file src="module/admin/css/admin.css"/>">

<script type="text/javascript" src='<digi:file src="module/aim/scripts/table_utils.js"/>'>.</script>

<h1 class="admintitle"><digi:trn>Aid Effectiveness Indicator Manager</digi:trn></h1>
<digi:errors/>
<div class="confirmationMessage">
    <c:if test="${'saveSuccess' == confirmationMessage}">
        <digi:trn>Indicator has been saved successfully</digi:trn>
    </c:if>
</div>
<%-- This is the search form --%>
<digi:form action="/aidEffectivenessIndicatorsManager.do" method="post" styleId="searchForm">
    <html:hidden property="actionParam" value="search"/>
    <table style="font-family: verdana; font-size: 11px;" border="0" width="100%">
        <tr>
            <td>
                <b><digi:trn key="aim:indicatorType">Indicator Type</digi:trn>:</b>
                &nbsp;
                <html:select style="inp-text" property="indicatorType" styleId="searchIndicatorType">
                    <html:option value="-1">
                        -<digi:trn key="aim:selectIndicatorType">Choose One</digi:trn>-
                    </html:option>
                    <html:option value="<%=String.valueOf(AmpAidEffectivenessIndicator.IndicatorType.DROPDOWN_LIST.ordinal())%>">
                        <digi:trn key="aim:dropDownList">Drop down list</digi:trn>
                    </html:option>
                    <html:option value="<%=String.valueOf(AmpAidEffectivenessIndicator.IndicatorType.SELECT_LIST.ordinal())%>">
                        <digi:trn key="aim:radioOption">Radio option</digi:trn>
                    </html:option>
                </html:select>

                &nbsp;
                &nbsp;

                <b><digi:trn key="aim:keyword">Keyword</digi:trn>:</b>
                &nbsp;
                <html:text property="ampIndicatorName" styleId="searchAmpIndicatorName" />

                &nbsp;
                &nbsp;

                <b><digi:trn key="aim:activeOnly">Active Only</digi:trn>:</b>
                    &nbsp;
                <html:checkbox property="active" styleId="searchActive"/>

                &nbsp;
                &nbsp;

                <html:reset styleClass="dr-menu" onclick="javascript:cleanUpForm(); return false;">
                    <digi:trn key="btn:reset">Reset</digi:trn>
                </html:reset>

                &nbsp;

                <html:submit styleClass="dr-menu"><digi:trn key="btn:go">Go</digi:trn></html:submit>
            </td>

        </tr>
    </table>
</digi:form>
<%-- End of the search form --%>

<br/>
<br/>

<%-- Search result list--%>

<table width="100%">
<tr>
<td width="85%">
<logic:notEmpty name="searchResult">
    <table class="inside" width="100%" cellpadding="4" id="searchResultsTableId">

        <thead>
            <tr style="font-size:12px; font-weight:bold">
                <th align="center" bgcolor="#c7d4db"><digi:trn>Indicator Name</digi:trn></th>
                <th align="center" bgcolor="#c7d4db"><digi:trn>Indicator Tooltip</digi:trn></th>
                <th align="center" bgcolor="#c7d4db"><digi:trn>Is Active</digi:trn></th>
                <th align="center" bgcolor="#c7d4db"><digi:trn>Is Mandatory</digi:trn></th>
                <th align="center" bgcolor="#c7d4db"><digi:trn>Indicator Type</digi:trn></th>
                <th align="center" bgcolor="#c7d4db"></td>
            </tr>
        </thead>

        <logic:iterate name="searchResult" id="indicator" type="org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicator">
            <tr>
                <td width="25%">
                    <bean:write name='indicator' property='ampIndicatorName' />
                </td>
                <td width="25%">
                    <bean:write name='indicator' property='tooltipText' />
                </td>
                <td width="9%" align="center">
                    <c:choose>
                        <c:when test="${indicator.active}">
                            <digi:trn key="aim:yes">Yes</digi:trn>
                        </c:when>
                        <c:otherwise>
                            <digi:trn key="aim:no">No</digi:trn>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td width="9%" align="center">
                    <c:choose>
                        <c:when test="${indicator.mandatory}">
                            <digi:trn key="aim:yes">Yes</digi:trn>
                        </c:when>
                        <c:otherwise>
                            <digi:trn key="aim:no">No</digi:trn>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td width="15%" align="center">
                    <c:choose>
                        <c:when test="${indicator.indicatorType == 1}">
                            <digi:trn key="aim:radioOption">Radio option</digi:trn>
                        </c:when>
                        <c:otherwise>
                            <digi:trn key="aim:dropDownList">Drop down list</digi:trn>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td width="17%" align="center">
                    <digi:link href="/aidEffectivenessIndicatorsManager.do?actionParam=edit&ampIndicatorId=${indicator.ampIndicatorId}">
                        <img src="/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png" border="0" title="<digi:trn>Edit</digi:trn>"/>
                    </digi:link>
                    <c:set var="deleteAction">/aidEffectivenessIndicatorsManager.do?actionParam=delete&ampIndicatorId=${indicator.ampIndicatorId}</c:set>
                    <a onclick="return confirmDelete('${deleteAction}'); return false;">
                        <img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" title="<digi:trn>Delete</digi:trn>"/>
                    </a>
                </td>
            </tr>
        </logic:iterate>
    </table>





</logic:notEmpty>
<logic:empty name="searchResult">
    <div height="40px">
        <digi:trn>No results matching search criteria found</digi:trn>
    </div>
</logic:empty>
</td>

<td valign="top" align="right" width="15%">
    <table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">
        <tr>
            <td>
                <!-- Other Links -->
                <table cellpadding="0" cellspacing="0" width="120">
                    <tr>
                        <td bgColor=#c9c9c7 class="box-title">
                            <b style="font-size:12px; padding-left:5px;">
                                <digi:trn key="aim:otherLinks">
                                    Other links
                                </digi:trn>
                            </b>
                        </td>
                        <td background="module/aim/images/corner-r.gif" height="17" width=17>&nbsp;

                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td bgColor=#ffffff class=box-border>
                <table cellPadding=5 cellspacing="1" width="100%">
                    <tr>
                        <td class="inside">
                            <digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
                            <c:set var="translation">
                                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                            </c:set>
                            <digi:link href="/admin.do" title="${translation}" >
                            <digi:trn key="aim:AmpAdminHome">
                            Admin Home
                            </digi:trn>
                            </digi:link>
                        </td>
                    </tr>
                    <!-- end of other links -->
                </table>
            </td>
        </tr>
    </table>
<td>


</tr>
</table>


</br>

<input type="button" class="dr-menu" onclick="javascript: addIndicator(); return false"
    value='<digi:trn key="btn:add">Add</digi:trn>'>

<script type="text/javascript">
    function cleanUpForm() {
        document.getElementById('searchIndicatorType').value = -1;
        document.getElementById('searchAmpIndicatorName').value = '';
        document.getElementById('searchActive').checked  = false;
    }

    function confirmDelete(deleteAction) {
        var confirmMessage = '<digi:trn jsFriendly="true">Are you sure you want to delete this aid effectiveness indicator?</digi:trn>';
        if (confirm(confirmMessage) === true) {
            window.location.href = deleteAction;
        }
    }

    function addIndicator() {
        window.location.href = "/aidEffectivenessIndicatorsManager.do?actionParam=add";
    }

    setStripsTable("searchResultsTableId", "tableEven", "tableOdd");
    setHoveredTable("searchResultsTableId", true);
</script>