<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<table border="1">
	<!-- attach filters -->
	<tr>
		<td>
		 <jsp:include page="/repository/aim/view/ar/NewFilters.jsp"/>
		</td>
	</tr>

	<tr>
		<td><!-- begin big report table -->
		<table border="1">
			<bean:define id="viewable" name="report"
				type="org.dgfoundation.amp.ar.Viewable" toScope="request" />
			<jsp:include page="/repository/aim/view/ar/viewableItem.jsp" />
		</table>
		<!-- end of big report table --></td>
	</tr>
</table>
