<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@taglib uri="/taglib/struts-html" prefix="html"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<digi:instance property="aimNPDForm" />
<c:set var="colspanInd">
${fn:length(aimNPDForm.selYears)*3+1}
</c:set>

<table width="100%" cellpadding="0" cellspacing="0"
	class="inside" style="margin-top: 20px;">
	<tbody>
		<tr>
			<td colspan="${colspanInd}" align="center" background="/TEMPLATE/ampTemplate/img_2/ins_header.gif"
				class="indicator_title"><digi:trn>Indicators</digi:trn></td>
		</tr>
		<tr>
		<td width="400" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside"
				style="background-repeat: repeat-x; font-size: 12px; border-top: none">&nbsp;</td>
		<c:forEach var="year" items="${aimNPDForm.selYears}">
		<th colspan="3" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside"
				style="background-repeat: repeat-x; font-size: 12px; border-right: 1px solid #cccccc"><b
				class="ins_title">${year}</b><b class="ins_title"></b></th>
		</c:forEach>
		</tr>

		<tr bgcolor=#f2f2f2>
			<td class="inside normal"><digi:trn>Indicator Name</digi:trn></td>
			<c:forEach var="year" items="${aimNPDForm.selYears}">
				<td align="center" class="inside_inner_title"><span
					class="desktop_project_name normal" style="padding-left:0px !important;"> <digi:trn
					key="aim:indGrid:baseVal">Base</digi:trn></span></td>
				<td align="center" class="inside_inner_title"><span
					class="desktop_project_name normal"> <digi:trn
					key="aim:indGrid:actualVal">Actual</digi:trn></span></td>
				<td align="center" class="inside_inner_title"><span
					class="desktop_project_name normal"><digi:trn
					key="aim:indGrid:targetVal">Target</digi:trn></span></td>

			</c:forEach>
		</tr>
		<c:if test="${!empty aimNPDForm.indicators}">
			<c:forEach var="indRow" items="${aimNPDForm.indicators}">
				<tr>
					<td class="inside normal"><span title="${indRow.description}"><c:out value="${indRow.name}"/></span>
					</td>
					<c:forEach var="val" items="${indRow.values}">
						<td class="inside">${val.baseValue}</td>
						<td class="inside">${val.actualValue}</td>
						<td class="inside">${val.targetValue}</td>

					</c:forEach>
				</tr>
			</c:forEach>
		</c:if>
	</tbody>
</table>
<br/>


