<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:instance property="highlightItemForm" />
<table width="97%" cellpadding="0" cellspacing="0">
	<tr>
		<td noWrap class="dgTitle" valign="top" width="100%">
			&nbsp;&nbsp;<digi:trn key="highlights:allHighlights">All Highlights</digi:trn>
		</td>
		<td noWrap align="right">
			<table cellpadding="0" cellspacing="0" border="0" width="100%">
				  <tr width="100%" align="right"> 
				    <td align="right" valign="top">
					<c:if test="${!empty highlightItemForm.prev}">
					  <digi:link href="/viewAllHighlights.do" paramName="highlightItemForm" paramId="nav" paramProperty="prev" >&laquo;<span class="bold"><digi:trn key="highlights:previous">Previous</digi:trn></span></digi:link>&nbsp;
					</c:if>
				    </td>	
				    <td align="right" valign="top" nowrap>
					    <c:if test="${!empty highlightItemForm.pages}"> 
						 <c:forEach var="pages" items="${highlightItemForm.pages}">
						    <c:if test="${! pages.currPage}">
				             <digi:link href="/viewAllHighlights.do" paramName="pages" paramId="pageNo" paramProperty="pageNo"><span class="underline"><c:out value="${pages.pageNo}"/></span></digi:link>
				            </c:if>
				            <c:if test="${pages.currPage}">
				              <span class="underline"><c:out value="${pages.pageNo}"/></span>
				            </c:if>
				          </c:forEach> 
				        </c:if> 
				    </td>
				    <td align="right" valign="top">	
					<c:if test="${highlightItemForm.next}">
					  &nbsp;<digi:link href="/viewAllHighlights.do" paramName="highlightItemForm" paramId="nav" paramProperty="next" ><span class="bold"><digi:trn key="highlights:next">Next</digi:trn></span>&raquo;</digi:link>
					</c:if>
				    </td>	
				  </tr> 
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="2"><digi:img alt="" src="module/common/images/shim.gif" border="0" height="4" width="1"/></td>
	</tr>
</table>
<table width="97%" align="center" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td class="bgblack" colspan="4"><digi:img src="module/common/images/shim.gif" height="1" width="1" /></td>
	</tr>
	<tr>
		<td noWrap class="bggray2" align="left"> &nbsp; 
		  <c:if test="${highlightItemForm.orderByTitle}">
			<digi:link href="/viewAllHighlights.do?sort=title&order=asc"><span class="bold"><digi:trn key="highlights:highlight1">Highlight</digi:trn></span></digi:link>
			<digi:img src="module/highlights/images/up-arrow-icon.gif" />
		  </c:if>  
		  <c:if test="${! highlightItemForm.orderByTitle}">
			<digi:link href="/viewAllHighlights.do?sort=title&order=desc"><span class="bold"><digi:trn key="highlights:highlight1">Highlight</digi:trn></span></digi:link>
			<digi:img src="module/highlights/images/down-arrow-icon.gif" />
		  </c:if>  
	  </td>
	  <td noWrap class="bggray2" align="left"> &nbsp; <span class="bold"><digi:trn key="highlights:description">Description</digi:trn></span></td>
	  <digi:secure actions="ADMIN">
	  <td noWrap class="bggray2" align="left">
	      <c:if test="${highlightItemForm.orderByAuthor}">
			<digi:link href="/viewAllHighlights.do?sort=author&order=asc"><span class="bold"><digi:trn key="highlights:author">Author</digi:trn></span></digi:link>
			<digi:img src="module/highlights/images/up-arrow-icon.gif" />
		  </c:if>  
		  <c:if test="${! highlightItemForm.orderByAuthor}">
			<digi:link href="/viewAllHighlights.do?sort=author&order=desc"><span class="bold"><digi:trn key="highlights:author">Author</digi:trn></span></digi:link>
			<digi:img src="module/highlights/images/down-arrow-icon.gif" />
		  </c:if>  
	  </td>
	  <td noWrap class="bggray2" align="center"><span class="bold"><digi:trn key="highlights:admin">Admin</digi:trn></span></td>
		</digi:secure>  
	</tr>
  <c:if test="${!empty	highlightItemForm.highlightsList}">
  <c:forEach var="highlightsList" items="${highlightItemForm.highlightsList}">
  <tr>
  	<td align="left" valign="top">
	  <table width="100%">
		 <tr>
	       <digi:secure actions="ADMIN">
		   <c:if test="${! highlightsList.isPublic}">
		  	  <td valign="top" align="center" width="10"><digi:img src="module/common/images/bullet_new.gif" /></td>
 	        </c:if>
		   </digi:secure>  
		    <c:if test="${! highlightsList.isPublic}">
		  	  <td valign="top" align="center" width="10"><digi:img src="module/common/images/bullet_new.gif" /></td>
 	        </c:if>
			<td align="left" valign="top">
  			  <c:if test="${!empty highlightsList.title}">
			    <digi:context name="viewHighlight" property="context/module/moduleinstance/viewHighlight.do" />
			     <digi:secure actions="ADMIN">
				 <c:if test="${! highlightsList.isPublic}">
				  <a href="<%= viewHighlight%>?activeHighlightId=<c:out value='${highlightsList.id}' />" onclick="window.open(this.href, 'users', 'HEIGHT=500,resizable=yes,scrollbars=yes,WIDTH=500');return false;">
				     <c:out value="${highlightsList.title}" escapeXml="false" /></a>
				  </c:if>
				 </digi:secure>  
				 <c:if test="${highlightsList.isPublic}">
				  <a href="<%= viewHighlight%>?activeHighlightId=<c:out value='${highlightsList.id}' />" target="_blank">
				      <c:out value="${highlightsList.title}" escapeXml="false" /></a>
				  </c:if>
				</c:if>  
				<BR><c:out value="${highlightsList.creationDate}" />
				<digi:secure actions="ADMIN">
				<BR><digi:link href="/showEditHighlight.do" paramName="highlightsList"  paramId="activeHighlightId" paramProperty="id">[<span class="blue"><digi:trn key="highlights:edit">Edit</digi:trn></span>]</digi:link>   
				</digi:secure>  
			  </td>
	      </tr>
	  </table>
   </td>
  <td align="left" valign="top">
  <table>
  	<tr>
		<td align="left" valign="top">
		  <table width="100%">
		  	<tr>
				<td align="left" valign="top">
			    <td noWrap align="center" valign="top">
			     <digi:secure actions="ADMIN">
				 <c:if test="${! highlightsList.isPublic}">
				   <c:if test="${highlightsList.showImage}">
					  <digi:context name="showImg" property="context/module/moduleinstance/showImage.do" />
					  <img src= '<%= showImg%>?activeHighlightId=<c:out value="${highlightsList.id}" />&shrink=true&png=true' alt="Go to the source of the highlight" width="50" height="50" border="0" />
				   </c:if>
				   <c:if test="${! highlightsList.showImage}">
					&nbsp;
				   </c:if>
			     </c:if> 
			    </digi:secure> 
				<c:if test="${highlightsList.isPublic}">
					<c:if test="${highlightsList.showImage}">
					  <digi:context name="showImg" property="context/module/moduleinstance/showImage.do" />
					  <img src= '<%= showImg%>?activeHighlightId=<c:out value="${highlightsList.id}" />&shrink=true&png=true' alt="Go to the source of the highlight" width="50" height="50" border="0" />
				   </c:if>
				   <c:if test="${! highlightsList.showImage}">
					&nbsp;
				  </c:if>
			     </c:if> 
			   </td>
			</tr>
		  </table>
	   </td>
	   <td align="left" valign="top">
	   <c:if test="${!empty highlightsList.topic}">
	    <digi:context name="viewHighlight" property="context/module/moduleinstance/viewHighlight.do" />
	    <digi:secure actions="ADMIN">
		<c:if test="${! highlightsList.isPublic}">
		<c:out value="${highlightsList.topic}" escapeXml="false" />
			<a href="<%= viewHighlight%>?activeHighlightId=<c:out value="${highlightsList.id}" />" onclick="window.open(this.href, 'users', 'HEIGHT=500,resizable=yes,scrollbars=yes,WIDTH=500');return false;">
			  ...
			</a> 
		</c:if>    
		</digi:secure>
		<c:if test="${highlightsList.isPublic}">
		<c:out value="${highlightsList.topic}" escapeXml="false" />
		  <a href="<%= viewHighlight%>?activeHighlightId=<c:out value="${highlightsList.id}" />"  target="_blank">
			  ...
		  </a> 
		</c:if>  
	   </c:if>   
	  </td>
  </tr>
 </table>
<digi:secure actions="ADMIN">
  <td align="left" valign="top">
    <c:out value="${highlightsList.authorFirstName}" /> 
    <c:out value="${highlightsList.authorLastName}" />    
  </td>
  <td noWrap align="left" valign="top">
  <c:if test="${! highlightsList.isPublic}">
    <digi:trn key="highlights:notPublic">not Public</digi:trn> <digi:link href="/togglePublic.do" paramName="highlightsList" paramId="activeHighlightId" paramProperty="id"><digi:trn key="highlights:toggle">toggle</digi:trn></digi:link> 
  </c:if>
  <c:if test="${highlightsList.isPublic}">
    <digi:trn key="highlights:Public">Public</digi:trn> <digi:link href="/togglePublic.do" paramName="highlightsList" paramId="activeHighlightId" paramProperty="id"><digi:trn key="highlights:toggle">toggle</digi:trn></digi:link> 
  </c:if>
  <br>
  <c:if test="${highlightsList.haveImage}">
    <c:if test="${highlightsList.showImage}">
       <digi:trn key="highlights:hideImage">Hide Image</digi:trn> <digi:link href="/toggleImage.do" paramName="highlightsList" paramId="activeHighlightId" paramProperty="id"><digi:trn key="highlights:toggle">toggle</digi:trn></digi:link></td>
    </c:if>
     <c:if test="${! highlightsList.showImage}">
       <digi:trn key="highlights:showImage">Show Image</digi:trn> <digi:link href="/toggleImage.do" paramName="highlightsList" paramId="activeHighlightId" paramProperty="id"><digi:trn key="highlights:toggle">toggle</digi:trn></digi:link></td>
   </c:if>
  </c:if>
  </td>
</digi:secure>  
</tr>
<tr>
	<td class="bggray2" colspan="4"><digi:img src="module/common/images/shim.gif" height="1" width="1" /></td>
</tr>
</c:forEach>
</table>

<table cellpadding="0" cellspacing="0" border="0" width=97%>
  <tr width="100%" align="right"> 
    <td align="right" valign="top" width="100%">
	<c:if test="${!empty highlightItemForm.prev}">
	  <digi:link href="/viewAllHighlights.do" paramName="highlightItemForm" paramId="nav" paramProperty="prev" >&laquo;<span class="bold"><digi:trn key="highlights:previous">Previous</digi:trn></span></digi:link>&nbsp;
	</c:if>
    </td>	
    <td align="right" valign="top" nowrap>
	<c:if test="${!empty highlightItemForm.pages}">
	    <c:forEach var="pages" items="${highlightItemForm.pages}">
		    <c:if test="${! pages.currPage}">
             <digi:link href="/viewAllHighlights.do" paramName="pages" paramId="pageNo" paramProperty="pageNo"><span class="underline"><c:out value="${pages.pageNo}"/></span></digi:link>
            </c:if>
            <c:if test="${pages.currPage}">
              <span class="underline"><c:out value="${pages.pageNo}" /></span>
            </c:if>
          </c:forEach> 
        </c:if> 
    </td>
    <td align="right" valign="top">	
	<c:if test="${!empty highlightItemForm.next}">
	  &nbsp;<digi:link href="/viewAllHighlights.do" paramName="highlightItemForm" paramId="nav" paramProperty="next" ><span class="bold"><digi:trn key="highlights:next">Next</digi:trn></span>&raquo;</digi:link>
	</c:if>
    </td>	
  </tr> 
</table>
</c:if>