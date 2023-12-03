<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:errors/>
<digi:form action="/viewActivityHistory.do" method="post" styleId="compareForm">
	<html:hidden property="actionMethod" styleId="actionMethod"/>
	<input type="hidden" name="ampActivityId" id="ampActivityId" value="${aimViewActivityHistoryForm.activityId}" />
	<c:set var="changesTable" scope="request" value="${requestScope.changesTable}"/>
	<div id="content"  class="yui-skin-sam">
		<div id="demo" class="yui-navset">
			<ul id="MyTabs" class="yui-nav">
				<li class="selected">
					<a/><div><digi:trn>Summary changes</digi:trn></div></a>
				</li>
			</ul>
		</div>
		<div class="contentstyle summary-change-container" id="ajaxcontentarea">
			<table border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF" id="dataTable" width="100%">
				<tr>
					<td style="background-repeat: repeat-x; font-size: 12px; border-left-width: 1px; width: 13%">
						<div align="center">
							${changesTable}
						</div>
					</td>
			</table>
			<br/>
			<input type="button" value="<digi:trn>Back to current version of the activity</digi:trn>" onclick="javascript:back()" />
		</div>
	</div>
</digi:form>

<script language="Javascript">
    function back() {
        document.getElementById("actionMethod").value = "cancel";
        document.getElementById('compareForm').submit();
    }
</script>