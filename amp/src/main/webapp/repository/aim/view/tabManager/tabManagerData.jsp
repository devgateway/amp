<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@page import="org.digijava.module.aim.action.TabManagerAction" %>

<digi:instance property="aimTabManagerForm" />
<bean:define name="aimTabManagerForm" id="myForm" type="org.digijava.module.aim.form.TabManagerForm" toScope="request"/>

<c:if test="${myForm.exceptionOccurred}" >
	<font color="red">
		<c:if test="${myForm.dataException}">
			<digi:trn key="aim:tabmanager:err:retrievingData">Could not retrieve necessary data. Please try again.</digi:trn>
		</c:if>
		<c:if test="${myForm.saveException}">
			<digi:trn key="aim:tabmanager:err:savingData">Could not save your selection. Please try again.</digi:trn>
		</c:if>
	</font>
</c:if>

<c:if test="${myForm.dataSuccessful}">
	<digi:form action="/tabManager.do" method="post" >
		<div id="dataSuccessful" style="display: none;" />dataSuccessful</div>
		<br />
		<table cellspacing="5px" cellpadding="3px">
			<c:forEach var="k" begin="1" end="<%=TabManagerAction.MAX_NUM_OF_TABS %>" step="1" >
				<tr>
					<td>
						<digi:trn key="aim:tabmanager:tabPosition">Tab Position</digi:trn> ${k}:
					</td>
					<td>
						<html:select name="aimTabManagerForm" property="tabsId" value="${myForm.tabsId[k-1]}" onchange="tabManager.check();">
							<html:option value="0" >-- <digi:trn key="aim:tabmanager:selectNone">None</digi:trn> --</html:option>
							<c:forEach var="tab" items="${aimTabManagerForm.tabs}">
								<option value="${tab.ampReportId }" title="${tab.name }"
								<c:if test="${tab.ampReportId == myForm.tabsId[k-1]}">SELECTED</c:if>
								>
									<c:choose>									
										<c:when test="${fn:length(tab.name) > 40}" >
											${fn:substring(tab.name, 0, 40)}...
										</c:when>
										<c:otherwise>
											${tab.name}
										</c:otherwise>
									</c:choose>
								</option>
							</c:forEach>
						</html:select>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${aimTabManagerForm.defaultTeamTab != null}">
				<tr>
					<td>
						<digi:trn key="aim:tabmanager:tabPosition">Tab Position</digi:trn> <%=TabManagerAction.MAX_NUM_OF_TABS+1%>:
					</td>
					<td align="center" style="font-weight:normal;">
						${aimTabManagerForm.defaultTeamTab.name} 
						<span style="font-style: italic; font-weight: normal;">( <digi:trn key="aim:defaultTeamTab">Default Team Tab</digi:trn> )</span>
					</td>
				</tr>
			</c:if>
		</table>
	</digi:form>
</c:if>

<c:if test="${myForm.saveSuccessful}">
	<div id="saveSuccessful" style="display: none;">saveSuccessful</div>
</c:if>
