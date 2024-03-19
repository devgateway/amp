<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<bean:define id="privateXmlItemList" name="xmlItemList" scope="request" toScope="page" />
<ul>
	<c:forEach var="xmlItem" items="${privateXmlItemList}" >
		<li>
			<strong>
				<c:out value="${xmlItem.dateString }" /> <digi:trn><c:out value="${xmlItem.name }" /> </digi:trn>:
			</strong> 
			<c:out value="${xmlItem.description }" />
			<c:if test="${xmlItem.children != null && not empty xmlItem.children }">
				<bean:define id="xmlItemList" name="xmlItem" property="children" toScope="request" />
				<jsp:include page="HierarchycalItemViewer.jsp"></jsp:include>
			</c:if>
		</li>
	</c:forEach>
</ul>