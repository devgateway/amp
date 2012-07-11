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
<script type="text/javascript">
function expandCollapse(id){
	$("#org_"+id).toggle("slow");
	$("#img_exp_"+id).toggle();
	$("#img_col_"+id).toggle();
}
function exportToExcel() {
	var href="/aim/organizationReportWizard.do?action=generateExcelFile";
	if(navigator.appName.indexOf('Microsoft Internet Explorer') > -1){ //Workaround to allow HTTP REFERER to be sent in IE (AMP-12638)
		var popupName = "popup"+new Date().getTime();	
		var popupWindow =  window.open("", popupName, "height=500,width=780,menubar=no,scrollbars=yes,resizable");
		var referLink = document.createElement('a');
		referLink.href = href;
		referLink.target = popupName;
		document.body.appendChild(referLink);
		referLink.click();
		
	}
	else{
		openURLinResizableWindow(href, 780, 500);
	}
	
}
</script>

<!-- MAIN CONTENT PART START -->
<digi:instance property="aimOrgProfileReport" />
<digi:context name="digiContext" property="context" />
<digi:form action="/organizationReportWizard.do" method="post">
<html:hidden property="action" value="makeReport"/>
	<table width="1000px" border="0" cellspacing="0" cellpadding="0"
		align=center>

		<tr>

			<td width="1000px"><div class="step_head_lng">

					<div class="step_head_cont">
						<digi:trn>Donor Profile Report</digi:trn>
					</div>

				</div></td>


		</tr>

		<tr valign=top>

			<td class="main_side">

				<div class="main_side_cont">

					<table width="98%" border="0" cellspacing="0" cellpadding="0">

						<tr>

							<td><a href="/aim/organizationReportWizard.do" class="l_sm"><b>« <dig:trn>Back to report generator</dig:trn></b></a></td>

							<td align="right"><b><digi:trn>Organization</digi:trn></b> <html:select
									property="selectedOrgId" styleClass="dropdwn_sm" style="width:100px">
									<html:option value="-1">
										<digi:trn key="aim:all">All</digi:trn>
									</html:option>
									<html:optionsCollection name="aimOrgProfileReport"
										property="organizations" value="ampOrgId" label="name" />
								</html:select><b><digi:trn>Donor group</digi:trn> </b><html:select
									property="selectedOrgGropId" styleClass="dropdwn_sm" style="width:100px">
									<html:option value="-1">
										<digi:trn key="aim:all">All</digi:trn>
									</html:option>
									<html:optionsCollection name="aimOrgProfileReport"
										property="groups" value="ampOrgGrpId" label="orgGrpName" />
								</html:select> <b><digi:trn>Donor type</digi:trn></b><html:select
									property="selectedTypeId" styleClass="dropdwn_sm" style="width:100px">
									<html:option value="-1">
										<digi:trn key="aim:all">All</digi:trn>
									</html:option>
									<html:optionsCollection name="aimOrgProfileReport"
										property="orgTypes" value="ampOrgTypeId" label="orgType" />
								</html:select> <input type="submit" value="<digi:trn>Filter</digi:trn>" class="buttonx" /><table  border="0" cellspacing="0" cellpadding="0"  style="display: inline;">
									<tr>
										<td><span
											style="font-size: 16px; font-weight: normal; display: block; margin-left: 10px; margin-right: 10px; color: #C6C6C6;">|</span></td>
										<td><digi:img hspace="2" vspace="2"	src="/TEMPLATE/ampTemplate/img_2/ico_exc.gif"	border="0" alt="Export to Excel" /> </td>

										<td><a href=# onclick="exportToExcel();return false;"><digi:trn>Export to Excel</digi:trn></a></td>

									</tr>

								</table></td>
						</tr>
					</table>

					<center>
						<h1><digi:trn>Donor Profile Report</digi:trn></h1>
					</center>
					<c:forEach var="record" items="${aimOrgProfileReport.records}">
						<div class="gis_wht" style="height:25px;">
							<div class="gis_cont">
									<a href=#
										onclick="expandCollapse(${record.orgId});return false;" style="display:block; float:left;"><b>${record.organizationName}</b></a><span
										style="margin-top: 3px; margin-right: 3px; float: right;">
										<img src="/TEMPLATE/ampTemplate/img_2/ico_perm_open.gif"
										border="0" id="img_exp_${record.orgId}"
										onclick="expandCollapse(${record.orgId});return false;"><img
										style="display: none"
										src="/TEMPLATE/ampTemplate/img_2/ico_perm_close.gif"
										border="0" id="img_col_${record.orgId}"
										onclick="expandCollapse(${record.orgId});return false;">
									</span>
								<div id="org_${record.orgId}" style="display:none;">
								<center>
									<c:forEach var="helper" items="${record.helpers}">
										<c:if
											test="${helper.allTypeProperty||helper.ngoOnlyProperty==record.ngo}">
											<c:choose>
													<c:when test="${empty helper.subHeaders}">
														<digi:trn>${helper.columnName}</digi:trn>:
														<c:forEach var="value" items="${helper.values}">
																<c:forEach var="val" items="${value}"><b>${val}</b><br/></c:forEach>
															</c:forEach>
													</c:when>
													<c:otherwise>
													<hr />
													<h2>${helper.columnName}</h2>
													<table width="960" cellspacing="0" cellpadding="0"
														class="inside">
														<tbody>
															<tr>
																<c:forEach var="subHeader" items="${helper.subHeaders}">
																	<td nowrap="nowrap" background="images/ins_bg.gif"
																		class="inside" align=center><b class="ins_title">${subHeader}</b></td>
																</c:forEach>
															</tr>
															<c:set var="elSize">
														 ${fn:length(helper.subHeaders)}
														</c:set>
															<c:forEach var="value" items="${helper.values}"
																varStatus="status">
																<c:if test="${(status.index+1)%elSize==1}">
																	<tr>
																</c:if>
																<td bgcolor="#FFFFFF" class="inside" align=center><c:forEach var="val" items="${value}">${val}<br/></c:forEach></td>
																<c:if
																	test="${(status.index+1)%elSize==0}">
																	</tr>
																</c:if>
															</c:forEach>
														</tbody>
													</table>
												</c:otherwise>
											</c:choose>
										</c:if>
									</c:forEach>
								</center>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</td>
		</tr>
	</table>
</digi:form>
