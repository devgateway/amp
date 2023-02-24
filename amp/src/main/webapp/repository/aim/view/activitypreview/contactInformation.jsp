<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<c:if test="${not empty contactInformation}">
	<c:forEach var="donorContact" items="${contactInformation}">
		<div>
			<span class="word_break bold"><c:out value="${donorContact.contact.name}" /></span>
			<span class="word_break bold"><c:out value="${donorContact.contact.lastname}"/></span>
			<c:if test="${not empty donorContact.contact.properties}">
			-
			</c:if>
            <c:set var="cont" value="0"/>
            <c:set var="hasPhone" value="false"/>
			<c:forEach var="property" items="${donorContact.contact.properties}">
				<c:if test="${property.name=='contact email'}">
                    <c:if test="${cont <2}">
                        <c:if test="${cont > 0}">, </c:if>
                        <span class="word_break bold"><c:out value="${property.value}" /></span>
                        <c:set var="cont" value="${cont + 1}"/>
                    </c:if>
				</c:if>
                <c:if test="${property.name=='contact phone'}">
                    <c:set var="hasPhone" value="true"/>
                </c:if>
			</c:forEach>
            <c:if test="${hasPhone == true}">
            -
            </c:if>
            <c:set var="cont" value="0"/>
            <c:forEach var="property" items="${donorContact.contact.properties}">
                <c:if test="${property.name=='contact phone'}">
                    <c:if test="${cont <2}">
                        <c:if test="${cont > 0}">, </c:if>
                        <span class="word_break bold"><c:out value="${property.value}" /></span>
                        <c:set var="cont" value="${cont + 1}"/>
                    </c:if>
                </c:if>
			</c:forEach>;
		</div>
	</c:forEach>
</c:if>