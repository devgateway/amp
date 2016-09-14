<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<html:hidden property="filter.currencyCode" styleId="currencyCode" />
<html:hidden property="filter.topLists" styleId="topLists" />
<html:hidden property="filter.decimalsToShow" styleId="decimalsToShow" />
<html:hidden property="filter.startYear" styleId="startYear"/>
<html:hidden property="filter.endYear" styleId="endYear" />
<html:hidden property="filter.defaultStartYear" styleId="defaultStartYear"/>
<html:hidden property="filter.defaultEndYear" styleId="defaultEndYear" />
<html:hidden property="filter.yearToCompare" styleId="yearToCompare"/>
<html:hidden property="filter.dashboardType" styleId="dashboardType" />
<html:hidden property="filter.workspaceOnly" styleId="workspaceOnly"/>
<html:hidden property="filter.showGroupsNotOrgs" styleId="showGroupsNotOrgs"/>
<html:hidden property="filter.showAmountsInThousands" styleId="showAmountsInThousands"/>
<html:hidden property="filter.showAmountsInThousandsDefault" styleId="showAmountsInThousandsDefault"/>
<html:hidden property="filter.showMonochrome" styleId="showMonochrome"/>
<html:hidden property="filter.commitmentsVisible" styleId="commitmentsVisible"/>
<html:hidden property="filter.disbursementsVisible" styleId="disbursementsVisible" />
<html:hidden property="filter.expendituresVisible" styleId="expendituresVisible" />
<html:hidden property="filter.pledgeVisible" styleId="pledgeVisible"/>
<html:hidden property="filter.transactionType" styleId="transactionType" />
<html:hidden property="filter.agencyType" styleId="agencyType" />
<html:hidden property="filter.currencyId" styleId="currencyId" />
<html:hidden property="filter.agencyTypeDefault" styleId="agencyTypeDefault" />
<html:hidden property="filter.currencyIdDefault" styleId="currencyIdDefault" />
<html:hidden property="filter.fiscalCalendarId" styleId="fiscalCalendarId" />
<html:hidden property="filter.defaultFiscalCalendarId" styleId="defaultFiscalCalendarId" />
<html:hidden property="filter.groupSeparator" styleId="groupSeparator" />
<html:hidden property="filter.decimalSeparator" styleId="decimalSeparator" />
<html:hidden property="filter.fromPublicView" styleId="fromPublicView" />
<html:hidden property="filter.fromGenerator" styleId="fromGenerator" />
<html:hidden property="filter.flashSliderLabels" styleId="flashSliderLabels" />
<html:hidden property="filter.adjustmentType" styleId="adjustmentType" />

<feature:display name="Background of organization" module="Dashboard Org. Info">
	<input type="hidden" id="BackgroundOrganizationVisible" value="true" />
</feature:display>
<feature:display name="Org. Description" module="Dashboard Org. Info">
	<input type="hidden" id="OrgDescriptionVisible" value="true" />
</feature:display>
<feature:display name="Key Areas of Focus" module="Dashboard Org. Info">
	<input type="hidden" id="KeyAreasFocusVisible" value="true" />
</feature:display>

<!--  Start Global variables for all flash files -->
<input type="hidden" id="GlobalFontSize" value="11" />
<input type="hidden" id="GlobalFontFamily" value="Arial" />
<input type="hidden" id="GlobalFontWeight" value="bold" />
<input type="hidden" id="trnMessagePanel" value="<digi:trn jsFriendly='true'>Empty Dataset</digi:trn>" />
<input type="hidden" id="trnMessageEmpty" value="<digi:trn jsFriendly='true'>No data to show</digi:trn>" />
<input type="hidden" id="trnMessageLoadingPanel" value="<digi:trn jsFriendly='true'>Loading...</digi:trn>" />
<input type="hidden" id="trnMessageLoading" value="<digi:trn jsFriendly='true'>Loading...</digi:trn>" />
<input type="hidden" id="trnMessageYearError" value="<digi:trn jsFriendly='true'>Please select at least two different years</digi:trn>" />
<input type="hidden" id="trnMessageError" value="<digi:trn jsFriendly='true'>Error</digi:trn>" />
<input type="hidden" id="trnYears" value="<digi:trn jsFriendly='true'>Years</digi:trn>" />
<input type="hidden" id="trnCurrency" value="<digi:trn jsFriendly='true'>Currency</digi:trn>" />

<!--  End Global variables for all flash files -->
<input type="hidden" id="${graph.containerId}ShowFontFamily" value="Verdana"/>
<input type="hidden" id="${graph.containerId}DataAction" value="get${graph.containerId}GraphData" />
<input type="hidden" id="${graph.containerId}DataField" value="dataField" />
<input type="hidden" id="${graph.containerId}StartYear" value="" />
<input type="hidden" id="${graph.containerId}EndYear" value="" />

<input type="hidden" id="${graph.containerId}ItemId"/>
<input type="hidden" id="${graph.containerId}TitleLegendTrn" value="<digi:trn>${graph.name}</digi:trn>" />
			<c:if test="${fromLaunchGraphAction}">
				<%@ include file="graphTypesOptions.jsp" %>
			</c:if>
			<c:if test="${!fromLaunchGraphAction}">
			<fieldset class="chartFieldset">
				<legend><span id="${graph.containerId}TitleLegend" class=legend_label ></span></legend>


				
					<a onclick="toggleHeader(this, '${graph.containerId}Header')" style=""><img src="/TEMPLATE/ampTemplate/img_2/ico_perm_open.gif" vspace="5" align="absMiddle"/> <digi:trn>Show settings</digi:trn></a>
					<div id="${graph.containerId}Header" class="chart_header" style="display:none;">
					<digi:trn>Title</digi:trn> <input type="text" id="${graph.containerId}Title" value="" size="50">
					<br /><digi:trn>Size</digi:trn>
					<select id="${graph.containerId}FontSize">
						<option value="12">12</option>
						<option value="13">13</option>
						<option value="14">14</option>
						<option value="15">15</option>
						<option value="16">16</option>
					</select>
					&nbsp;<input type="checkbox" id="${graph.containerId}Bold"><label for="${graph.containerId}Bold"><digi:trn>Bold</digi:trn></label><br/>
					<input type="checkbox" id="${graph.containerId}ShowLegend" checked="checked"><label id="${graph.containerId}ShowLegendLabel" for="${graph.containerId}ShowLegend"><digi:trn>Show legend</digi:trn></label>
					&nbsp;<input type="checkbox" id="${graph.containerId}Divide"><label id="${graph.containerId}DivideLabel" for="${graph.containerId}Divide"><digi:trn>Divide by thousands</digi:trn></label>
					<br /><input type="checkbox" id="${graph.containerId}ShowDataLabel"><label id="${graph.containerId}ShowDataLabelLabel" for="${graph.containerId}ShowDataLabel"><digi:trn>Show data label</digi:trn></label>
					&nbsp;<input type="checkbox" id="${graph.containerId}RotateDataLabel"><label id="${graph.containerId}RotateDataLabelLabel" for="${graph.containerId}RotateDataLabel"><digi:trn>Rotate data label</digi:trn></label>
					&nbsp;<input type="checkbox" id="${graph.containerId}Ignore" style="display: none;" checked="checked"><label id="${graph.containerId}IgnoreLabel" style="display: none;" for="${graph.containerId}Ignore"><digi:trn>Ignore big values</digi:trn></label></br>
					<input type="button" class="buttonx" value="<digi:trn>Update chart</digi:trn>" onclick="updateGraph(event, '${graph.containerId}')">
					</div>
					<%@ include file="graphTypesOptions.jsp" %>
					<br />
					<br />
				</c:if>
				<div class="flashcontent" name="flashContent">
					<div id="${graph.containerId}">
						<a href="http://www.adobe.com/go/getflashplayer" title="<digi:trn>Click here to get Adobe Flash player</digi:trn>">
							<img src="/TEMPLATE/ampTemplate/img_2/get_flash_player.gif" alt="Get Adobe Flash player" />
						</a>
					</div>
				</div>
				<c:if test="${!fromLaunchGraphAction}">
				<div align="right">
					<br /><a href="javascript:document.getElementById('dashboard_name').scrollIntoView(true);"><digi:trn>Back to Top</digi:trn></a>
				</div> 
			</fieldset>
			</c:if>