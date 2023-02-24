<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<!-- paginator, viewFormat = ${viewFormat} -->

<c:set var="max_value" scope="request"><%=Integer.MAX_VALUE%></c:set>
<c:choose>
	<c:when test="${(viewFormat=='html2') || (viewFormat=='html') }">
		<jsp:include page="paginators/paginator_html2.jsp"></jsp:include>
	</c:when>
	<c:when test="${viewFormat=='foldable' }">
		<jsp:include page="paginators/paginator_foldable.jsp"></jsp:include>
	</c:when>
	<c:when test="${viewFormat=='print' }">
		<jsp:include page="paginators/paginator_print.jsp"></jsp:include>
	</c:when>			
	<c:otherwise>
		<!-- no paginator, unknown format: ${viewFormat} -->
	</c:otherwise>
</c:choose>
