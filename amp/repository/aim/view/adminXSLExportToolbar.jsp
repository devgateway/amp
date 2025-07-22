<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<table border="0" align="center" bgcolor="#f2f2f2" width=100%>
	<tr>
		<td noWrap align=center valign="middle">
			<c:set var="translationxls">
				<digi:trn>Export to Excel</digi:trn>
			</c:set>
			<c:set var="translationPrinter">
				<digi:trn>Printer Friendly</digi:trn>
			</c:set>
			<a target="_blank" onclick="exportXSL(); return false;" title="${translationxls}" style="cursor: pointer;"> 
				<digi:img hspace="2" vspace="2"	src="/TEMPLATE/ampTemplate/imagesSource/common/ico_exc.gif"	border="0" alt="Export to Excel" /> 
			</a> 
			<digi:link styleId="printWin" href="#" onclick="window.print(); return false;" title="${translationPrinter }">
				<digi:img hspace="2" vspace="2"	src="/TEMPLATE/ampTemplate/imagesSource/common/ico_print.gif" border="0" alt="Printer Friendly" />
			</digi:link>
		</td>
	</tr>
</table>