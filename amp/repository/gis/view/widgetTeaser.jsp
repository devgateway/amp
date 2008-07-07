<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:instance property="gisWidgetTeaserForm" />

<c:if test="${gisWidgetTeaserForm.rendertype==4}">
	<img src="/gis/widgetchart.do~widgetId=${gisWidgetTeaserForm.id}">		
</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==3}">
	<c:if test="${ ! empty gisWidgetTeaserForm.table}">
		<table 
			<c:if test="${gisWidgetTeaserForm.table.width != null}">
				width="${gisWidgetTeaserForm.table.width}" 
			</c:if>
			<c:if test="${gisWidgetTeaserForm.table.cssClass != null}">
				class="${gisWidgetTeaserForm.table.cssClass}" 
			</c:if>
			style="border : 1px solid silver"
			>
			<c:if test="${gisWidgetTeaserForm.table.nameAsTitle == true}">
				<tr>
					<td colspan="${fn:length(gisWidgetTeaserForm.table.headerRows)}">
						${gisWidgetTeaserForm.table.name}
					</td>
				</tr>
			</c:if>
			<c:forEach var="theader" items="${gisWidgetTeaserForm.table.headerRows}" varStatus="hstat">
				<tr>
					<c:forEach var="cell" items="${theader.cells}">
						<td nowrap="nowrap" style="border : 1px solid silver">
							<strong>${cell.value}</strong>
						</td>
					</c:forEach>
				</tr>
				<c:forEach var="trow" items="${gisWidgetTeaserForm.table.dataRows}" varStatus="dstat">
					<tr>
						<c:forEach var="tcell" items="${trow.cells}" varStatus="cstat">
							<td nowrap="nowrap" style="border : 1px solid silver">
								${tcell.value}
							</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</c:forEach>
		</table>
	</c:if>
			
</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==1}">
	<digi:trn key="gis:widgetTeaser:emptyPlace">empty teaser: </digi:trn>&nbsp;${gisWidgetTeaserForm.placeName}
</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==0}">
	<digi:trn key="gis:widgetTeaser:noParamSpecified">ERROR : no place param specified in layout definition for this teaser.</digi:trn>
</c:if>
