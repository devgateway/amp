<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<jsp:include page="teamPagesHeader.jsp" flush="true" />

<bean:define id="translation">
	<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
</bean:define>
<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
My Desktop
</digi:link> &gt; All Reports

<p/>

<link rel="stylesheet" href="<digi:file src="module/aim/css/newamp.css"/>"/>


<table class=clsTable cellSpacing=0 cellPadding=0 width="100%" border=0>
	<!-- attach filters -->
	<tr>
		<td>
		 <jsp:include page="/repository/aim/view/ar/toolBar.jsp"/>
		</td>
	</tr>
	<tr>
		<td>
		 <jsp:include page="/repository/aim/view/ar/NewFilters.jsp"/>
		</td>
	</tr>

	<tr>
		<td>
			Description: <i><bean:write scope="session" name="reportMeta" property="reportDescription"/></i>
		</td>
	</tr>


	<tr>
		<td><!-- begin big report table -->
		<table class=clsInnerTable cellSpacing=0 cellPadding=0 width="100%" border=0>
			<bean:define id="viewable" name="report"
				type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
			<jsp:include page="/repository/aim/view/ar/viewableItem.jsp" />
		</table>
		<!-- end of big report table --></td>
	</tr>
</table>
