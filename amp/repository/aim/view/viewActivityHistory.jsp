<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-nested" prefix="nested"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>

<digi:instance property="aimViewActivityHistoryForm" />
	<!-- 
	<digi:trn>Activity Versions</digi:trn>
	<br />
	 -->
	<div style="height: 540px; overflow: scroll;">
		<table width="100%" cellspacing="0" cellpadding="0" id="dataTable">
			<tr bgcolor="#999999" height="20">
				<td bgcolor="#999999">&nbsp;</td>
				<td bgcolor="#999999"><strong><digi:trn>Last modified by</digi:trn></strong></td>
				<td bgcolor="#999999"><strong><digi:trn>Date</digi:trn></strong></td>
				<td bgcolor="#999999"><strong><digi:trn>Action</digi:trn></strong></td>
			</tr>
			<c:forEach items="${aimViewActivityHistoryForm.activities}" var="item"
				varStatus="status">
				<tr>
				<c:if test="${item.modifiedBy eq null}">
					<td>
					<input name="compareCheckboxes" type="checkbox" value="${item.ampActivityId}" onchange="monitorCheckbox()" onclick="monitorCheckbox()" onkeyup="monitorCheckbox()" onkeypress="monitorCheckbox()"/>
					</td>
					<td>
						${item.activityCreator.user.firstNames} ${item.activityCreator.user.lastName}
						<c:if test="${empty item.activityCreator.user.firstNames}">
						<digi:trn>Empty</digi:trn>
						</c:if>
					</td>
					<td>
						<fmt:formatDate type="both" value="${item.createdDate}"
						dateStyle="short" timeStyle="short" /> 
						<c:if test="${empty item.createdDate}">
						<digi:trn>Empty</digi:trn>
						</c:if>
					</td>
					<td>
						<c:if test="${aimViewActivityHistoryForm.activityId ne item.ampActivityId}">
							<logic:equal  name="aimViewActivityHistoryForm" property="enableadvanceoptions" value="true">
								<div onclick="javascript:setVersion(${item.ampActivityId})" style="color:black;cursor:pointer;">
									<digi:trn>Make this the current version</digi:trn>
								</div>
							</logic:equal>
						</c:if>
						<c:if test="${aimViewActivityHistoryForm.activityId eq item.ampActivityId}">
							<strong><digi:trn>Current Version</digi:trn></strong>
						</c:if>
					</td>
				</c:if>
				<c:if test="${item.modifiedBy ne null}">
					<td>
					<input name="compareCheckboxes" type="checkbox" value="${item.ampActivityId}" onchange="monitorCheckbox()" onclick="monitorCheckbox()" onkeyup="monitorCheckbox()" onkeypress="monitorCheckbox()"/>
					</td>
					<td>
						${item.modifiedBy.user.firstNames} 
						<c:if test="${empty item.modifiedBy.user.firstNames}">
						<digi:trn>Empty</digi:trn>
						</c:if>
					</td>
					<td>
						<fmt:formatDate type="both" value="${item.modifiedDate}"
						dateStyle="short" timeStyle="short" /> 
						<c:if test="${empty item.modifiedDate}">
						<digi:trn>Empty</digi:trn>
						</c:if>
					</td>
					<td>
						<c:if test="${aimViewActivityHistoryForm.activityId ne item.ampActivityId}">
							<logic:equal name="aimViewActivityHistoryForm" property="enableadvanceoptions" value="true">
								<div onclick="javascript:setVersion(${item.ampActivityId})" style="color:black;cursor:pointer;">
									<digi:trn>Make this the current version</digi:trn>
								</div>
							</logic:equal>
						</c:if>
						<c:if test="${aimViewActivityHistoryForm.activityId eq item.ampActivityId}">
							<strong><digi:trn>Current Version</digi:trn></strong>
						</c:if>
					</td>
				</c:if>
				</tr>
			</c:forEach>
		</table>
	</div>
<digi:form action="/compareActivityVersions.do" method="post" type="aimCompareActivityVersionsForm" >
<input type="hidden" name="activityCurrentVersion" id="activityCurrentVersion" value="" />
<input type="hidden" name="action" id="action" value=""/>
<input type="hidden" name="activityOneId" id="activityOneId" />
<input type="hidden" name="activityTwoId" id="activityTwoId" />
<input type="hidden" name="showMergeColumn" id="showMergeColumn" />
<input type="hidden" name="method" id="method" />
<input type="hidden" name="ampActivityId" id="ampActivityId" />
<input type="button" id="SubmitButton" value="<digi:trn>Compare versions</digi:trn>" onclick="submitCompare()"/>
</digi:form>