<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c" %>

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