<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance id="wform" property="gisTableWidgetTeaserForm"/>

<bean:define id="wtable" name="wform" property="table"/>
<table id="widgetOuter" border="0" cellpadding="20">
	<tr>
		<td>
		
			<table id="widgetInner" width="${wtable.width}">
				<c:forEach var="wrow" items="${wtable.rows}" varStatus="varStat">
					<tr>
						<td nowrap="nowrap">
							${wrow.pk} === AVOE 
						</td>
					</tr>
				</c:forEach>
			</table>
		
		</td>
	</tr>
</table>