<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<digi:instance property="invalidDataList" />

<table>
	<tr>
		<td>Activity</td>
		<td>AMP ID</td>
		<td>Number of sectors</td>
		<td>Total Percentage</td>
	</tr>
	<c:forEach var="act" items="${invalidDataList.invalidSectorpercentages}">
		<tr>
			<td>${act.activityName}</td>
			<td>${act.ampId}</td>
			<td>${act.numOfSectors}</td>
			<td>${act.totalPercentage}</td>
		</tr>
	</c:forEach>
</table>
