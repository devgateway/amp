<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script type="text/javascript">
<!--
	function editWidget(id){
		<digi:context name="justSubmit" property="context/module/moduleinstance/indicatorchartwidgets.do~actType=edit" />
		myForm.action="<%=justSubmit%>";  
		myForm.submit();
	}
	function deleteWidget(id){
		<digi:context name="justSubmit" property="context/module/moduleinstance/indicatorchartwidgets.do~actType=delete" />
		myForm.action="<%=justSubmit%>";  
		myForm.submit();
	}
//-->
</script>

<digi:instance property="gisIndicatorChartForm" />
<digi:form action="/indicatorchartwidgets.do">


<table width="60%" border="0" cellpadding="15">
	<tr>
		<td>
			<span class="crumb">
              <c:set var="translation">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
              </html:link>&nbsp;&gt;&nbsp;
                <digi:trn key="admin:Navigation:indicatorchartWidgets">Indicator chart widgets</digi:trn>
			</span>
		</td>
	</tr>
	<tr>
		<td>
			<span class="subtitle-blue"><digi:trn key="gis:indicatorChartWidgetManager:pageHeader">Indicator chart widget Manager</digi:trn></span>
		</td>
	</tr>
	<tr>
		<td>
			<a href="/widget/indicatorchartwidgets.do~actType=create"><digi:trn key="gis:indicatorchartwidgets:createNew">Create new widget</digi:trn></a>
		</td>
	</tr>
	<tr>
		<td>
			
			<table border="0" width="100%" align="center" style="font-family:verdana;font-size:11px;">
				<tr bgColor="#d7eafd">
					<td nowrap="nowrap" width="40%">
						<strong><digi:trn key="gis:indicatorChartWidget:widgetName">Widget Name</digi:trn></strong>
					</td>
					<td nowrap="nowrap" width="40%">
						<strong><digi:trn key="gis:indicatorChartWidget:indicatorName">Indicator Name</digi:trn></strong>
					</td>
					<td>
						<strong><digi:trn key="gis:indicatorChartWidget:oeprations">Operation</digi:trn></strong>
					</td>
				</tr>
				<c:forEach var="wi" items="${gisIndicatorChartForm.widgets}" varStatus="stat">
					<tr>
						<td nowrap="nowrap">
							${wi.name}
						</td>
						<td nowrap="nowrap">
						 	${wi.indicator.indicator.name}
						</td>
						<td nowrap="nowrap">
							<a href="/widget/indicatorchartwidgets.do~actType=edit~widgetId=${wi.id}">
								<digi:trn key="gis:editLink">Edit</digi:trn>
							</a>
							&nbsp;
							<a href="/widget/indicatorchartwidgets.do~actType=delete~widgetId=${wi.id}">
								<img border="0" src='<digi:file src="images/deleteIcon.gif"/>'>
							</a>
						</td>
					</tr>
				</c:forEach>
			</table>


		</td>
	</tr>
</table>

</digi:form>