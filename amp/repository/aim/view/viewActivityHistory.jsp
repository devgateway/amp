<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/taglib/struts-nested" prefix="nested"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/fieldVisibility.tld" prefix="field"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/featureVisibility.tld" prefix="feature"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/moduleVisibility.tld" prefix="module"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/fn.tld" prefix="fn"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>

<digi:instance property="aimViewActivityHistoryForm" />
	<!-- 
	<digi:trn>Activity Versions</digi:trn>
	<br />
	 -->
	<div style="height: 450px; overflow: auto;">
		<table width="100%" cellspacing="0" cellpadding="0" id="dataTable">
			<tr bgcolor="#999999" height="20">
				<td bgcolor="#999999">&nbsp;</td>
				<td bgcolor="#999999"><strong><digi:trn>Last modified by</digi:trn></strong></td>
				<td bgcolor="#999999"><strong><digi:trn>Date</digi:trn></strong></td>
				<td bgcolor="#999999"><strong><digi:trn>Action</digi:trn></strong></td>
			</tr>
			<c:forEach items="${aimViewActivityHistoryForm.activities}" var="item" varStatus="status">
				<tr>
					<td>
					<logic:notEmpty name="currentMember" scope="session">
						<input name="compareCheckboxes" type="checkbox" value="${item.activityId}" onchange="monitorCheckbox()" onclick="monitorCheckbox()" onkeyup="monitorCheckbox()" onkeypress="monitorCheckbox()"/>
					</logic:notEmpty>
						<logic:equal name="aimViewActivityHistoryForm" property="enableSummaryChange" value="true">
							<input type="hidden" name="summaryChangesIds" id="summaryChangesIds" value="${item.activityId}" />
						</logic:equal>
					</td>
					<td>
						${item.modifiedBy}
						<c:if test="${empty item.modifiedBy}">
						<digi:trn>Not Available</digi:trn>
						</c:if>
					</td>
					<td>
						${item.modifiedDate}
						<c:if test="${empty item.modifiedDate}">
						<digi:trn>Empty</digi:trn>
						</c:if>
					</td>
					<td>
						<c:if test="${aimViewActivityHistoryForm.activityId ne item.activityId}">
							<logic:equal name="aimViewActivityHistoryForm" property="enableadvanceoptions" value="true">
								<div onclick="javascript:setVersion(${item.activityId})" style="color:black;cursor:pointer;">
									<digi:trn>Make this the current version</digi:trn>
								</div>
							</logic:equal>
						</c:if>
						<c:if test="${aimViewActivityHistoryForm.activityId eq item.activityId}">
							<strong><digi:trn>Current Version</digi:trn></strong>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</table>
		
	</div>
	
	<div style="position:absolute; bottom:2px;overflow:auto">
		<div style="float:left;">
		<digi:form action="/compareActivityVersions.do" method="post" type="aimCompareActivityVersionsForm" >
			<input type="hidden" name="activityCurrentVersion" id="activityCurrentVersion" value="" />
			<input type="hidden" name="action" id="action" value=""/>
			<input type="hidden" name="activityOneId" id="activityOneId" />
			<input type="hidden" name="activityTwoId" id="activityTwoId" />
			<input type="hidden" name="showMergeColumn" id="showMergeColumn" />
			<input type="hidden" name="method" id="method" />
			<input type="hidden" name="ampActivityId" id="ampActivityId" />
			<input type="hidden" name="activityId" id="activityId"
				   value="${aimViewActivityHistoryForm.activityId}" />
			<logic:notEmpty name="currentMember" scope="session">
				<input type="button" id="SubmitButton" value="<digi:trn>Compare versions</digi:trn>" onclick="submitCompare()"/>
			</logic:notEmpty>
		</digi:form>
		</div>
		<div style="float:left;padding-left: 5px;">
		<digi:form action="/viewActivityHistory.do" method="post" name="aimViewActivityHistoryForm" type="aimViewActivityHistoryForm" >
			<input type="hidden" name="actionMethod" id="actionMethod" />
			<input type="hidden" name="activityId" id="activityId"
				   value="${aimViewActivityHistoryForm.activityId}" />
			<logic:notEmpty name="currentUser" scope="session">
				<input type="button" id="SubmitSummaryButton" value="<digi:trn>Show Change Summary</digi:trn>" onclick="submitChangeSummary()"/>
			</logic:notEmpty>
			<logic:empty name="currentUser" scope="session">
				<field:display name="Show Change Summary" feature="Version History">
					<input type="button" id="SubmitSummaryButton" value="<digi:trn>Show Change Summary</digi:trn>" onclick="submitChangeSummary()"/>
				</field:display>
			</logic:empty>
		</digi:form>
		</div>
	</div>