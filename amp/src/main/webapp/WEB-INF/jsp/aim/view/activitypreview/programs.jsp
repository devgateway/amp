<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
	parameter (put in request scope): $programs_list, $programs_name
 --%>
<c:if test="${not empty programs_list}">
	<span class="legend_label word_break"><c:out value="${programs_name}" /></span>
 	<table width="100%" cellSpacing="2" cellPadding="1" style="font-size:11px;">
		<c:forEach var="program" items="${programs_list}">
			<tr>
				<td width="85%">
					<span class="word_break bold"><c:out value="${program.hierarchyNames}"/></span>
				</td>
				<td width="15%" align="right"><b><c:out value="${program.programPercentage}"/>%</b></td>
			</tr>
		</c:forEach>
	</table>
	<hr>
</c:if>
	
	