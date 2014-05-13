<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>


<script type="text/javascript"
	src="<digi:file src='module/aim/scripts/reportWizard/myDragAndDropObjects.js'/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript"
	src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dragDropTree.js"/>"></script>
<link rel="stylesheet" type="text/css"
	href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">
	<digi:instance property="aimOrgProfileReport" />
<script type="text/javascript">
	function initializeDragAndDrop() {
		selectedCols= new Array();
		new YAHOO.util.DDTarget('source_col_ul');
		new YAHOO.util.DDTarget('dest_col_ul');
		columnDragAndDropObject = new MyDragAndDropObject('source_col_ul',
				'dest_col_ul');
		columnDragAndDropObject.createDragAndDropItems();
		<c:forEach items="${aimOrgProfileReport.selectedColumns}" var="dbId">
		selectedCols.push('${dbId}');
		</c:forEach>
		MyDragAndDropObject.selectObjsByDbId ("source_col_ul", "dest_col_ul", selectedCols);

	}
	YAHOO.util.Event.addListener(window, "load", initializeDragAndDrop);
	function makeReport() {
		var selectedColumns = $("#dest_col_ul  input[name='selectedColumns']");
		if (selectedColumns.length < 1) {
			alert("<digi:trn>Please select at least on column</digi:trn>");
			return;
		}
		//alert(selectedColumns);

		var params = '';
		var i = 0;
		for (; i < selectedColumns.length - 1; i++) {
			params += selectedColumns[i].value + "&selectedColumns=";
		}
		params += selectedColumns[i].value;
		document.aimOrgProfileReport.selectedColumnsList.value = params;
		document.aimOrgProfileReport.submit();
	}
</script>
<digi:context name="digiContext" property="context" />
<digi:form action="/organizationReportWizard.do" method="post">
<input type="hidden" name="selectedColumnsList" id="selectedColumnsList" value=""/>
<html:hidden property="action" value="makeReport"/>
	<div id="wizard_container" class="yui-navset-right"
		style="padding-right: 0em;">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="1000">
					<div class="step_head_lng">
						<div id="rgTitle" class="step_head_cont">
							<digi:trn>Report Generator</digi:trn>
						</div>
					</div>
				</td>
			</tr>
			<tr valign="top">
				<td class="main_side">
					<div class="yui-content">
						<div id="columns_step_div" class="main_side_cont yui-hidden"
							style="${topBottomPadding}">
							<br />
							<table align="center" cellpadding="0" cellspacing="0"
								style="width: 735px;" border=0>
								<tr>
									<td width="340px" align="center">
										<fieldset class="main_side_cont">
											<legend>
												<span class="legend_label"><digi:trn
														key="rep:wizard:availableColumns">Available Columns</digi:trn></span>
											</legend>
											<ul id="source_col_ul" class="draglist">
												<c:forEach var="column"
													items="${aimOrgProfileReport.columns}">
													<li class="list1" id="id_${column}"><input
														type="checkbox"
														style='line-height: 15px; margin-top: 6px;' id="${column}"
														name="selectedColumns" value="${column}" /> <digi:trn>${column}</digi:trn></li>
												</c:forEach>
											</ul>
										</fieldset>
									</td>
									<td valign="middle" align="center">
										<button type="button"
											onClick="MyDragAndDropObject.selectObjs('source_col_ul', 'dest_col_ul')"
											style="border: none;">
											<img src="/TEMPLATE/ampTemplate/img_2/ico_arr_right.gif" />
										</button> <br /> <br />
										<button type="button"
											onClick="MyDragAndDropObject.deselectObjs('dest_col_ul','source_col_ul')"
											style="border: none;">
											<img src="/TEMPLATE/ampTemplate/img_2/ico_arr_left.gif" />
										</button>
									</td>
									<td width="340px" align="center">
										<fieldset class="main_side_cont">
											<legend>
												<span class="legend_label"><digi:trn>Selected Columns</digi:trn></span>
											</legend>
											<ul id="dest_col_ul" class="draglist"
												style="line-height: 20px;">
											</ul>
										</fieldset>
									</td>
								</tr>
								<tr>
									<td colspan="3" style="text-align: center; padding-top:15px;">
										<button type="button" name="" class="buttonx"
											onclick="makeReport()" value="makeReport">
											<digi:trn>Make Report</digi:trn>
										</button>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</td>
			</tr>

		</table>
	</div>
	<div id="generatingPanel"></div>
</digi:form>