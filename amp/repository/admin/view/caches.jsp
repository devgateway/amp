<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<digi:instance property="cacheForm" />
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr class="yellow">
		<td><digi:img src="module/admin/images/yellowLeftTile.gif" border="0" width="20"/></td>
		<td width="100%">
			<font class="sectionTitle">
				<digi:trn key="admin:caches">Caches</digi:trn>
			</font>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="yellow" valign="top" align="left">

<digi:errors/>
<table width="100%" cellpadding="0" cellspacing="0">
<tr><td colspan="5">&nbsp;</td></tr>
<tr>
   <td align= "center" colspan="5"><b><digi:trn key="admin:hibernateCaches">Hibernate Caches</digi:trn></b></td>
 </tr>   
<tr><td colspan="5">&nbsp;</td></tr>
<tr bgcolor="silver">
   <td align= "center"><b><digi:trn key="admin:cacheKey">Cache Key</digi:trn></b></td>
   <td align= "right"><b><digi:trn key="admin:cacheSize">Cache Size</digi:trn></b></td>
   <td align= "center"><b><digi:trn key="admin:numberOfCacheGets">Gets</digi:trn></b></td>
   <td align= "center"><b><digi:trn key="admin:numberOfCachePuts">Puts</digi:trn></b></td>
   <td align= "center">&nbsp;</td>   
 </tr>   
  <logic:iterate indexId="ind" id="cachesList" name="cacheForm" property="hibernateCachesList">
    <c:set var="isEven" value="${ind % 2}"/>
    <tr 
    <c:if test="${isEven == 0}">bgcolor="#EBEBEB"</c:if>
    <c:if test="${isEven == 1}">bgcolor="#D2D2DB"</c:if>
    >
  	<td align="left" valign="top">
  	    <c:out value="${cachesList.name}" />
  	</td>
  	<td align="right" valign="top">
  	    <c:out value="${cachesList.size}" />
  	</td>
  	<td align="right" valign="top">
  	    <c:out value="${cachesList.numberOfGets}" />
  	</td>
  	<td align="right" valign="top">
  	    <c:out value="${cachesList.numberOfPuts}" />
  	</td>
  	<td align="center" valign="top">
  	    <digi:link href="/showCaches.do" paramName="cachesList" paramProperty="name" paramId="hibernateKey"><digi:trn key="admin:clear">clear</digi:trn></digi:link>
  	</td>
    </tr>
  </logic:iterate> 
 <tr><td colspan="5" bgcolor="silver">&nbsp;</td></tr>
 <tr>
   <td align="right"><b><digi:trn key="admin:numberOfCaches">Total number of caches</digi:trn></b></td>
   <td align="right"><c:out value="${cacheForm.hibernateCacheCount}" /></td>
 </tr>   
 <tr>
   <td align="right"><b><digi:trn key="admin:numberOfItemsInCaches">Total number of items in all caches</digi:trn></b></td>
   <td align="right"><c:out value="${cacheForm.hibernateTotalCount}" /></td>
 </tr>   
</table>
		</td>
	</tr>
</table>