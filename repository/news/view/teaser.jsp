
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:instance property="newsForm"/>
	<c:if test="${!empty newsForm.newsList}">
	<table width="180" border="0" align="center" cellpadding="5" cellspacing="1" bordercolor="#FFFFFF" bgcolor="#006699">
		<tr>
			<td bgcolor="#FFFFFF">
				<c:forEach var="newsList" items="${newsForm.newsList}" >
					<p><font color="#1A8CFF">&raquo;</font>
				<c:if test="${!empty newsList.title}">
					<c:if test="${!empty newsList.description}">
						<digi:link href="/showNewsItemDetails.do" paramName="newsList" paramId="activeNewsItem" paramProperty="id">
							<c:if test="${!empty newsList.title}">
								<bean:write name="newsList" property="title" filter="false"/>
							</c:if>
						</digi:link>
					</c:if>
					<c:if test="${empty newsList.description}">
					  <a href='<bean:write name="newsList" property="sourceUrl"/>' class="text">
						<bean:write name="newsList" property="title" filter="false"/>
					  </a>
					</c:if>					
				</c:if>
	 	 		  <span class="dta">
 				 <c:out value="${newsList.releaseDate}"/></span></p></c:forEach>
		</td>
		</tr>
		<tr>
			<td bgcolor="#FFFFFF">
				<div align="right">
					<digi:link href="/viewAllNews.do?all=all"><font color="#1A8CFF" size="-2">
					<digi:trn key="news:viewAll">View All</digi:trn></font></digi:link><br>
					<digi:secure authenticated="true">
					<digi:link href="/showCreateNewsItem.do"><font color="#1A8CFF" size="-2">
					<digi:trn key="news:addNewItem">Add New Item</digi:trn></font></digi:link></digi:secure>
				</div>
			</td>
		</tr>
	</table></c:if>