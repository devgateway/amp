<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance property="newsForm"/>

<digi:ref href="css/demoUI.css" rel="stylesheet" type="text/css" />



<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="10" height="25"><digi:img src="images/ui/teaserTitleLeft.gif"/></td>
		<td width="100%" height="25" class="teaserTitleBody">
			&nbsp;News
		</td>
		<td width="5" height="25"><digi:img src="images/ui/teaserTitleRight.gif"/></td>
	</tr>
	<tr>
		<td width="10" height="25"><digi:img src="images/ui/teaserExtrainfoLeft.gif"/></td>
		<td width="100%" height="25" class="teaserExtraBody">
			&nbsp;<digi:msg default="News items">1111</digi:msg>
		</td>
		<td width="5" height="25"><digi:img src="images/ui/teaserExtrainfoRight.gif"/></td>
	</tr>
	<tr>
	<td class="bodyLeftTile" width="10"><digi:img src="images/ui/spacer.gif"/></td>
	<td class="bodyField" width="100%">


		<table width="180" border="0" align="center" cellpadding="5" cellspacing="1" bordercolor="#FFFFFF">
		<tr>
		<td>

		<c:if test="${!empty newsForm.newsList}">
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
				
				<br>
				<span class="dta">
					<digi:date name="newsList" property="releaseDate" format="dd MMM yyyy"/>
				</span></p>
				</c:forEach>
		</c:if>		
		</tr></td>
		  <tr> 
		    <td><div align="right"><digi:link href="/viewAllNews.do?all=all"><font color="#1A8CFF" size="-2">
		    <digi:trn key="news:viewAll">View All</digi:trn></font></digi:link><br>
		        <digi:secure authenticated="true">
		          <digi:link href="/showCreateNewsItem.do"><font color="#1A8CFF" size="-2"><digi:trn key="news:addNewItem">Add New Item</digi:trn></font></digi:link>
		        </digi:secure>
		        </div></td>
		  </tr>
		</table>

		
			</td>
	<td class="bodyRightTile" width="10"><digi:img src="images/ui/spacer.gif"/></td>
	</tr>
	
	<tr>
	<td width="10" height="11"><digi:img src="images/ui/teaserBottomLeft.gif"/></td>
	<td width="100%" height="11" class="teaserBottomBody"><digi:img src="images/ui/spacer.gif"/></td>
	<td width="5" height="11"><digi:img src="images/ui/teaserBottomRight.gif"/></td>
	</tr>	
</table>